package bankingapplication.dto;

import bankingapplication.enum1.SavingOrCurrentBalance;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDto implements Serializable {

  private Long accountId;
  private String accNo;
  private SavingOrCurrentBalance accountType;
  private Long bankId;
  private Long customerId;
  private String name;
  private String ifscCode;
  private double amount;
  private boolean isBlocked;

}
