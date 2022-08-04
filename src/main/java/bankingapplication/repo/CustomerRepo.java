package bankingapplication.repo;

import bankingapplication.entity.Customer;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, Long> {

  Customer findByPanCardNumberOrAadhaarNumber(Optional<String> panNumber,
      Optional<String> aadharNumber);

  Customer findByMobileNumberOrEmailId(Optional<String> mobileNumber, Optional<String> emailId);

  @Query(value = "select * from customer_info  WHERE customer_id like :key or customer_name like :key or mobile_number like :key or email_id like :key ", nativeQuery = true)
  List<Customer> findByTitleContent(@Param("key") String content);

}
