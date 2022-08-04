package bankingapplication.repo;

import bankingapplication.entity.Account;
import bankingapplication.entity.Bank;
import bankingapplication.entity.Customer;
import bankingapplication.enum1.SavingOrCurrentBalance;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepo extends JpaRepository<Account, Long> {

  Account findByAccNo(String accNo);

  List<Account> findByUserInfoAndBankAndAccountType(Customer customer, Bank bank,
      SavingOrCurrentBalance accountType);

  List<Account> findByBank(Bank bank);

  List<Account> findByUserInfo(Customer customer);

  @Query(value = "select * from account  WHERE account_id like :key or acc_no like :key or  customer_id like :key ", nativeQuery = true)
  List<Account> findByTitleContent(@Param("key") String content);


}
