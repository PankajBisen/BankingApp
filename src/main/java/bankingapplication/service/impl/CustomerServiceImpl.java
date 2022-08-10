package bankingapplication.service.impl;

import bankingapplication.constant.ApplicationConstant;
import bankingapplication.dto.CustomerDto;
import bankingapplication.dto.MoneyTransferDto;
import bankingapplication.entity.Account;
import bankingapplication.entity.Bank;
import bankingapplication.entity.Customer;
import bankingapplication.entity.Transaction;
import bankingapplication.enum1.SavingOrCurrentBalance;
import bankingapplication.exception.BankException;
import bankingapplication.repo.AccountRepo;
import bankingapplication.repo.BankRepo;
import bankingapplication.repo.CustomerRepo;
import bankingapplication.repo.TransactionRepo;
import bankingapplication.service.CustomerService;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {

  @Autowired
  private CustomerRepo customerRepo;
  @Autowired
  private AccountRepo accountRepo;
  @Autowired
  private TransactionRepo transactionRepo;
  @Autowired
  private BankRepo bankRepo;

  public String save(CustomerDto customerDto) {
    String aadhaarNumber = String.valueOf(customerDto.getAadhaarNumber());
    String panNumber = String.valueOf(customerDto.getPanCardNumber());
    String mobileNumber = String.valueOf(customerDto.getMobileNumber());
    String emailId = String.valueOf(customerDto.getEmailId());

    Customer byPanNuOrAadhaarNumOrMobileNum = customerRepo.findByPanCardNumberOrAadhaarNumber(
        Optional.ofNullable(panNumber), Optional.ofNullable(aadhaarNumber));
    Customer byMobileNumberOrEmailId = customerRepo.findByMobileNumberOrEmailId(
        Optional.ofNullable(mobileNumber), Optional.ofNullable(emailId));

    if (byPanNuOrAadhaarNumOrMobileNum != null) {
      return ApplicationConstant.PAN_OR_AADHAAR_NUMBER_NOT_UNIQUE;
    }
    if (byMobileNumberOrEmailId != null) {
      return ApplicationConstant.MOBILE_NUMBER_OR_EMAILID_NOT_UNIQUE;
    }
    if (aadhaarNumber.length() == 12 && panNumber.length() == 10 && mobileNumber.length() == 10) {
      customerRepo.save(dtoToEntityCustomer(customerDto));
      return ApplicationConstant.CUSTOMER_CREATED;
    }
    return ApplicationConstant.INVALID_PAN_AADHAAR_NUM;
  }

  @Override
  public List<CustomerDto> getAllCustomer(Long bankId) {
    Bank bank = bankRepo.findById(bankId).orElseThrow(
        () -> new BankException(ApplicationConstant.BANK_IS_NOT_FOUND, HttpStatus.BAD_REQUEST));
    List<Customer> customers = customerRepo.checkCustomerByBankId(bankId);
    if(customers.isEmpty()){
      throw new BankException(ApplicationConstant.NO_CUSTOMER_FOR_GIVEN_BANK_ID,HttpStatus.BAD_REQUEST);
    }
    return customers.stream().map(this::entityToDto).collect(Collectors.toList());
  }


  private CustomerDto entityToDto(Customer customer) {
    CustomerDto customerDto = new CustomerDto();
    customerDto.setCustomerId(customer.getCustomerId());
    customerDto.setCustomerName(customer.getCustomerName());
    customerDto.setAddress(customer.getAddress());
    customerDto.setAadhaarNumber(customer.getAadhaarNumber());
    customerDto.setPanCardNumber(customer.getPanCardNumber());
    customerDto.setMobileNumber(customer.getMobileNumber());
    customerDto.setEmailId(customer.getEmailId());
    customerDto.setBankId(customer.getBank().getBankId());
    return customerDto;
  }


  private Customer dtoToEntityCustomer(CustomerDto customerDto) {
    Customer customerInfo = new Customer();
    customerInfo.setCustomerName(customerDto.getCustomerName());
    customerInfo.setAddress(customerDto.getAddress());
    customerInfo.setAadhaarNumber(customerDto.getAadhaarNumber());
    customerInfo.setPanCardNumber(customerDto.getPanCardNumber());
    customerInfo.setMobileNumber(customerDto.getMobileNumber());
    customerInfo.setEmailId(customerDto.getEmailId());
    Bank byId = bankRepo.findById(customerDto.getBankId()).orElse(null);
    if (byId == null) {
      throw new BankException(
          "The bank " + ApplicationConstant.ID_INVALID + customerDto.getBankId(),
          HttpStatus.BAD_REQUEST);
    }
    customerInfo.setBank(byId);
    return customerInfo;
  }

  public String transferMoney(MoneyTransferDto transferDto) {

    Transaction transaction = new Transaction();
    Account fromAccount = accountRepo.findByAccNo(transferDto.getAccountNumberFrom());
    Account toAccount = accountRepo.findByAccNo(transferDto.getAccountNumberTo());

    if (Objects.isNull(fromAccount)) {
      throw new BankException(ApplicationConstant.NO_ACCOUNT_FOUND_FOR_GIVEN_ACCOUNT_NUMBER
          + transferDto.getAccountNumberFrom(), HttpStatus.BAD_REQUEST);
    }
    if (Objects.isNull(toAccount)) {
      throw new BankException(ApplicationConstant.NO_ACCOUNT_FOUND_FOR_GIVEN_ACCOUNT_NUMBER
          + transferDto.getAccountNumberFrom(), HttpStatus.BAD_REQUEST);
    }
    if (fromAccount.isBlocked()) {
      return String.format("Account no%d is blocked", fromAccount.getAccNo());
    }
    if (!transferDto.getIfscCode().equals(toAccount.getIfscCode())) {
      return ApplicationConstant.INVALID_IFSC_CODE + ApplicationConstant.DOES_NOT_MATCH
          + transferDto.getIfscCode();
    }
    if (transferDto.getAmount() > fromAccount.getAmount()) {
      return ApplicationConstant.ACCOUNT_BALANCE_LOW;
    }
    double fromAccountDebited = fromAccount.getAmount() - transferDto.getAmount();
    double toAccountCredited = toAccount.getAmount() + transferDto.getAmount();

    fromAccount.setAmount(fromAccountDebited);
    toAccount.setAmount(toAccountCredited);

    BeanUtils.copyProperties(transferDto, transaction);

    accountRepo.save(fromAccount);
    accountRepo.save(toAccount);
    transactionRepo.save(transaction);

    Account fromAccount1 = accountRepo.findByAccNo(transferDto.getAccountNumberFrom());
    String savingAcc = transfer(fromAccount1, SavingOrCurrentBalance.SAVING,
        fromAccountDebited < (SavingOrCurrentBalance.SAVING.getAmount()), true, transaction,
        ApplicationConstant.TRANSACTION_SUCCESSFUL_BUT_ACCOUNT_BLOCKED,
        SavingOrCurrentBalance.CURRENT,
        fromAccountDebited < (SavingOrCurrentBalance.CURRENT.getAmount()),
        ApplicationConstant.TRANSACTION_SUCCESSFUL_BUT_ACCOUNT_BLOCKED);
    if (savingAcc != null) {
      return savingAcc;
    }
    Account toAccount1 = accountRepo.findByAccNo(transferDto.getAccountNumberTo());
    String currentAcc = transfer(toAccount1, SavingOrCurrentBalance.CURRENT,
        fromAccountDebited > (SavingOrCurrentBalance.CURRENT.getAmount()), false, transaction,
        ApplicationConstant.TRANSACTION_SUCCESSFUL, SavingOrCurrentBalance.SAVING,
        fromAccountDebited > (SavingOrCurrentBalance.SAVING.getAmount()),
        ApplicationConstant.TRANSACTION_SUCCESSFUL);
    if (currentAcc != null) {
      return currentAcc;
    }
    return ApplicationConstant.TRANSACTION_SUCCESSFUL;

  }

  private String transfer(Account fromAccount1, SavingOrCurrentBalance saving,
      boolean fromAccountDebited, boolean isBlocked, Transaction transaction, String savingAcc,
      SavingOrCurrentBalance current, boolean fromAccountDebited1, String currentAcc) {
    if ((fromAccount1.getAccountType().name().equals(saving.name())) && fromAccountDebited) {
      fromAccount1.setBlocked(isBlocked);
      accountRepo.save(fromAccount1);
      transactionRepo.save(transaction);
      return savingAcc;
    }
    if ((fromAccount1.getAccountType().name().equals(current.name())) && fromAccountDebited1) {
      fromAccount1.setBlocked(isBlocked);
      accountRepo.save(fromAccount1);
      transactionRepo.save(transaction);
      return currentAcc;
    }
    return null;
  }

  @Override
  public List<Customer> getByIdAndName(String content) {
    List<Customer> customer = customerRepo.findByTitleContent("%" + content + "%");
    if (customer.isEmpty()) {
      throw new BankException(ApplicationConstant.CUSTOMER_NOT_FOUND, HttpStatus.NOT_FOUND);
    }
    return customer;
  }

  @Override
  public List<Customer> getAllCustomer() {
    List<Customer> customers = customerRepo.findAll();
    if (customers.isEmpty()) {
      throw new BankException(ApplicationConstant.CUSTOMER_NOT_FOUND, HttpStatus.BAD_REQUEST);
    }
    return customers;
  }

  @Override
  public String updateCustomer(CustomerDto customerDto, Long customerId) {
    Customer customer = customerRepo.findById(customerId).orElseThrow(
        () -> new BankException(ApplicationConstant.CUSTOMER_NOT_FOUND, HttpStatus.BAD_REQUEST));
    customer.setCustomerName(customerDto.getCustomerName());
    customer.setAddress(customerDto.getAddress());
    customer.setAadhaarNumber(customer.getAadhaarNumber());
    customer.setPanCardNumber(customer.getPanCardNumber());
    customer.setEmailId(customerDto.getEmailId());
    customer.setMobileNumber(customerDto.getMobileNumber());
    if (customerDto.getMobileNumber().length() == 10) {
      customerRepo.save(customer);
      return ApplicationConstant.CUSTOMER_UPDATED;
    }
    return ApplicationConstant.CHECK_MOBILE_NUMBER;
  }

  @Override
  public String deleteCustomer(Long customerId) {
    Customer customer = customerRepo.findById(customerId).orElseThrow(
        () -> new BankException(ApplicationConstant.CUSTOMER_NOT_FOUND, HttpStatus.BAD_REQUEST));
    List<Account> accounts = accountRepo.findByUserInfo(customer);
    if (!accounts.isEmpty()) {
      throw new BankException(
          ApplicationConstant.CANT_DELETE_CUSTOMER_BECAUSE_ACCOUNT_LINKED_WITH_CUSTOMER,
          HttpStatus.BAD_REQUEST);
    } else {
      customerRepo.deleteById(customerId);
    }
    return ApplicationConstant.CUSTOMER_DELETED;
  }
}

