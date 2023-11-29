package Secret.Santa.Secret.Santa.repos;

import Secret.Santa.Secret.Santa.models.Gift;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IGiftRepo extends JpaRepository<Gift, Integer> {
}

