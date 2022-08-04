package bankingapplication.dto;

import bankingapplication.enum1.SavingOrCurrentBalance;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDate;
import lombok.Data;

@Data
public class MoneyTransferDto {
  private String accountNumberFrom;
  @JsonFormat(pattern="yyyy-MM-dd")
  private LocalDate date;
  private String accountNumberTo;
  private SavingOrCurrentBalance accountType;
  private double amount;
  private String ifscCode;
  private String name;
}
