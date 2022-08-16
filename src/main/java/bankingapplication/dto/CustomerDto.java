package bankingapplication.dto;

import bankingapplication.entity.Bank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

  private Long customerId;
  private String customerName;
  private String address;
  private String panCardNumber;
  private String aadhaarNumber;
  private String mobileNumber;
  private String emailId;
  private Long bankId;
  private String password;
}
