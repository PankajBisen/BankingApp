package bankingapplication.dto;

import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class BankDto {


  private String bankName;
  @Pattern(regexp = "^[A-Za-z]{4}\\d{7}$", message = "Invalid format")
  private String ifscCode;
  private String branchName;
  private String address;
  private String city;
}
