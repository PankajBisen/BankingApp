package bankingapplication.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customerInfo")
public class Customer {

  @Id
  @GeneratedValue
  @Column(name = "customerId")
  private Long customerId;
  private String customerName;
  private String aadhaarNumber;
  private String address;
  private String panCardNumber;
  private String mobileNumber;
  @NotNull(message = "Email cannot be empty")
  private String emailId;
  private String password;

  @OneToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "bankId", referencedColumnName = "bankId")
  private Bank bank;
}