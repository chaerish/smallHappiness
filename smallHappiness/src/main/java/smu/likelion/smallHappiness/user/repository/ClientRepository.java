package smu.likelion.smallHappiness.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smu.likelion.smallHappiness.user.entity.Client;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    boolean existsByUserId(String userId);
    boolean existsByRegNum(String regNum);
    Optional<Client> findByUserId(String userId);
}
