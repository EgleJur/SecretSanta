package Secret.Santa.Secret.Santa.repos;

import Secret.Santa.Secret.Santa.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IUserRepo extends JpaRepository<User, Integer> {

    //    List<User> findByGroups(Group group);
    List<User> findByNameContainingIgnoreCase(String nameText);

    Optional<User> findByEmail(String email);

    //    List<User> findByGroups(Group group);

    @Modifying
    @Query(value = "DELETE FROM `users_in_groups` WHERE `user_id` = :USERID", nativeQuery = true)
    void deleteFromUsersInGroupsByUserId(@Param("USERID") Integer userId);

    //    @Transactional
    void delete(User user);

    boolean existsByEmail(String email);


}
