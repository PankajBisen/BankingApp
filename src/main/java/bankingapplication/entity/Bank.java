package bankingapplication.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Table(name = "BankSave")
@Data
public class Bank {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long bankId;
  private String bankName;
  @Pattern(regexp = "^[A-Za-z]{4}\\d{7}$", message = "Invalid format")
  private String ifscCode;
  private String branchName;
  private String city;
  private String address;

  /*@OneToMany(cascade = CascadeType.DETACH,fetch = FetchType.EAGER,orphanRemoval = true)
  private List<Account> accounts ;*/
}
