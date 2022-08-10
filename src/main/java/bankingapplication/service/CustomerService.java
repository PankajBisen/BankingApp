package bankingapplication.service;

import bankingapplication.dto.CustomerDto;
import bankingapplication.dto.MoneyTransferDto;
import bankingapplication.entity.Customer;
import java.util.List;


public interface CustomerService {

  String save(CustomerDto customerDto);

  String transferMoney(MoneyTransferDto transactionDto);

  List<Customer> getByIdAndName(String content);

  List<Customer> getAllCustomer();

  String updateCustomer(CustomerDto customerDto, Long customerId);

  String deleteCustomer(Long customerId);

  List<CustomerDto> getAllCustomer(Long bankId);
}