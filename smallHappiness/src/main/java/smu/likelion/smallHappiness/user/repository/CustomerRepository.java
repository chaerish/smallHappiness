package smu.likelion.smallHappiness.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import smu.likelion.smallHappiness.user.entity.Customer;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByUserId(String userId);
    Optional<Customer> findByUserId(String userId);
}
