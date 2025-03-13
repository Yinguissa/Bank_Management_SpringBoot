package GestionBanque.G9.repositorie;

import GestionBanque.G9.entity.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    List<BankAccount> findBankAccountByCustomer_Id(Long customerId );
}
