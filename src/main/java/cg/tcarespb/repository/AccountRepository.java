package cg.tcarespb.repository;

import cg.tcarespb.models.Account;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, String> {
    boolean existsByEmailIgnoreCase(@NotNull String email);

    Optional<Account> findAccountByEmail(String email);

    Account findAccountByUserId(String userId);

    Account findAccountByEmployeeId(String employeeId);
    Account findAccountBySalerId(String salerId);


    void deleteAccountByEmployeeId(String employeeId);

    void deleteAccountByUserId(String userId);

    void deleteAccountBySalerId(String salerId);
}
