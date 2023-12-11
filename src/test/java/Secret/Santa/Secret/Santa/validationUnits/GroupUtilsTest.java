package Secret.Santa.Secret.Santa.validationUnits;

import Secret.Santa.Secret.Santa.exception.SantaValidationException;
import Secret.Santa.Secret.Santa.models.Group;
import Secret.Santa.Secret.Santa.repos.IGroupRepo;
import Secret.Santa.Secret.Santa.services.validationUnits.GroupUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GroupUtilsTest {

    @Mock
    private IGroupRepo groupRepository;

    @InjectMocks
    private GroupUtils groupUtils;

    @Test
    void getGroupById_ValidId_ReturnsGroup() {
        int groupId = 1;
        Group expectedGroup = new Group();
        when(groupRepository.findById(groupId)).thenReturn(java.util.Optional.of(expectedGroup));

        Group result = groupUtils.getGroupById(groupId);

        verify(groupRepository).findById(groupId);
        assertSame(expectedGroup, result);
    }

    @Test
    void getGroupById_InvalidId_ThrowsException() {
        int groupId = 1;
        when(groupRepository.findById(groupId)).thenReturn(java.util.Optional.empty());

        assertThrows(SantaValidationException.class, () -> groupUtils.getGroupById(groupId));
        verify(groupRepository).findById(groupId);
    }
}