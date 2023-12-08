package Secret.Santa.Secret.Santa.services.impl;

import Secret.Santa.Secret.Santa.exception.SantaValidationException;
import Secret.Santa.Secret.Santa.models.DTO.GenerateSantaDTO;
import Secret.Santa.Secret.Santa.models.GenerateSanta;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.models.User;
import Secret.Santa.Secret.Santa.repos.IGenerateSantaRepo;
import Secret.Santa.Secret.Santa.services.IGenerateSantaService;
import Secret.Santa.Secret.Santa.validationUnits.GenerateSantaUtils;
import Secret.Santa.Secret.Santa.validationUnits.GroupUtils;
import Secret.Santa.Secret.Santa.validationUnits.UserUtils;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static Secret.Santa.Secret.Santa.mappers.GenerateSantaMapper.toSanta;

@Service
@RequiredArgsConstructor
public class GenerateSantaServiceImpl implements IGenerateSantaService {
    private static final Logger logger = LoggerFactory.getLogger(GenerateSantaServiceImpl.class);

    @Autowired
    private final IGenerateSantaRepo generateSantaRepository;

    private final GenerateSantaUtils generateSantaUtils;
    private final GroupUtils groupUtils;
    private final UserUtils userUtils;


//    public GenerateSantaServiceImpl(IGenerateSantaRepo generateSantaRepository,
//                                    GenerateSantaUtils generateSantaUtils, GroupUtils groupUtils, UserUtils userUtils) {
//        this.generateSantaRepository = generateSantaRepository;
//        this.generateSantaUtils = generateSantaUtils;
//        this.groupUtils = groupUtils;
//        this.userUtils = userUtils;
//    }

    @Override
    public List<GenerateSanta> getAllGenerateSantaByGroup(Integer groupId) {
        try {
            Group group = groupUtils.getGroupById(groupId);
            return generateSantaRepository.findByGroup(group);
        } catch (Exception e) {
            logger.error("Error retrieving all GenerateSanta by group ID: {}", groupId, e);
            throw e;
        }
    }

    @Override
    public GenerateSanta createGenerateSanta(GenerateSantaDTO generateSantaDTO) {
        try {
            return generateSantaRepository.save(toSanta(generateSantaDTO));
        } catch (Exception e) {
            logger.error("Error creating GenerateSanta", e);
            throw e;
        }
    }

    @Override
    public GenerateSanta getGenerateSantaBySantaAndGroup(Integer santaId, Integer groupId) {
        try {
            User santa = userUtils.getUserById(santaId);
            Group group = groupUtils.getGroupById(groupId);
            return generateSantaUtils.getBySantaAndGroup(santa, group);
        } catch (Exception e) {
            logger.error("Error retrieving GenerateSanta by santa ID: {} and group ID: {}", santaId, groupId, e);
            throw e;
        }
    }

    @Override
    public void deleteGenerateSantaById(Integer id) {
        try {
            generateSantaRepository.deleteById(id);
        } catch (Exception e) {
            logger.error("Error deleting GenerateSanta by ID: {}", id, e);
            throw e;
        }
    }

    @Override
    public void deleteGenerateSantaByGroup(Integer groupId) {
        try {
            Group group = groupUtils.getGroupById(groupId);
            generateSantaRepository.deleteByGroup(group);
        } catch (Exception e) {
            logger.error("Error deleting GenerateSanta by group ID: {}", groupId, e);
            throw e;
        }
    }

    @Override
    public void deleteGenerateSantaByUser(Integer userId, Integer groupId) {
        try {
            Group group = groupUtils.getGroupById(groupId);
            User user = userUtils.getUserById(userId);
            GenerateSanta generateSanta = generateSantaUtils.getBySantaAndGroup(user, group);
            GenerateSanta generateSantaRecipient = generateSantaUtils.getByUserAndGroup(user, group);
            generateSantaRecipient.setRecipient(generateSanta.getRecipient());
            generateSantaRepository.delete(generateSanta);
        } catch (Exception e) {
            logger.error("Error deleting GenerateSanta by user ID: {} and group ID: {}", userId, groupId, e);
            throw e;
        }
    }

    @Override
    public void randomSantaGenerator(Integer groupId) {

        Group group = groupUtils.getGroupById(groupId);
        if (generateSantaUtils.existsByGroup(group)) {
            throw new SantaValidationException("Generate santa for group exits: ", "", "", "");
        }
        List<User> usersInGroup = group.getUser();

        if (usersInGroup.size() <= 2) {
            logger.error("Not enough participants in the group to generate Secret Santa pairs.");
            throw new SantaValidationException("Exceeded maximum attempts to find a recipient for Santa: ", "", "", "");
        }

        List<User> shuffledUsers = new ArrayList<>(usersInGroup);
        Collections.shuffle(shuffledUsers);

        int maxAttempts = 100;
        Set<User> pairedSantas = new HashSet<>();
        Map<User, User> pairings = new HashMap<>();

        for (int i = 0; i < shuffledUsers.size(); i++) {
            User santa = usersInGroup.get(i);

            if (pairedSantas.contains(santa)) {
                continue;
            }

            User recipient = null;
            int attempts = 0;

            while (attempts <= maxAttempts) {
                recipient = shuffledUsers.get((i + attempts + 1) % shuffledUsers.size());

                if (!pairedSantas.contains(recipient) && !generateSantaUtils.alreadyPaired(santa, recipient, group)) {
                    pairedSantas.add(santa);
                    pairedSantas.add(recipient);
                    pairings.put(santa, recipient);
                    pairings.put(recipient, santa);
                    break;
                }

                attempts++;
            }

            if (attempts > maxAttempts) {
                logger.error("Exceeded maximum attempts to find a recipient for Santa: " + santa);
                break;
            }
        }

        for (Map.Entry<User, User> entry : pairings.entrySet()) {
            GenerateSanta santaPair = new GenerateSanta();
            santaPair.setGroup(group);
            santaPair.setSanta(entry.getKey());
            santaPair.setRecipient(entry.getValue());
            generateSantaRepository.save(santaPair);
        }
    }


}