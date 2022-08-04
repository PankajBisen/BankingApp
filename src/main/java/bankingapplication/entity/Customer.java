package bankingapplication.entity;

import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="customerInfo")
public class Customer {
  @Id
  @GeneratedValue
  @Column(name = "customerId")
  private Long customerId;
  private String customerName;
  private String address;
  private String panCardNumber;
  private String aadhaarNumber;
  private String mobileNumber;
  @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}")
  @NotEmpty(message = "Email cannot be empty")
  private String emailId;

 /* @OneToMany( mappedBy = "userInfo", cascade = CascadeType.DETACH,fetch = FetchType.EAGER,orphanRemoval = true)
  private List<Account> accounts ;*/
}
