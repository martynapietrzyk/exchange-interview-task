package pl.interview.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.interview.model.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByCustomerPesel(String pesel);
}
