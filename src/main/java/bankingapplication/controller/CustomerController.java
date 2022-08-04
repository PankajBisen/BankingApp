package bankingapplication.controller;

import bankingapplication.constant.UrlConstant;
import bankingapplication.dto.CustomerDto;
import bankingapplication.dto.MoneyTransferDto;
import bankingapplication.entity.Customer;
import bankingapplication.service.CustomerService;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(UrlConstant.CUSTOMER_URL)
@Getter
@Setter
@RequiredArgsConstructor
@CrossOrigin
public class CustomerController {

  private final CustomerService customerService;

  @PostMapping(UrlConstant.CUSTOMER_CREATE)
  public ResponseEntity<String> save(@RequestBody CustomerDto customerDto) {
    String s = customerService.save(customerDto);
    return new ResponseEntity<>(s, HttpStatus.CREATED);
  }

  @PutMapping(UrlConstant.TRANSFER_MONEY)
  public ResponseEntity<String> transferMoney(@RequestBody MoneyTransferDto transactionDto) {
    String s = customerService.transferMoney(transactionDto);
    return new ResponseEntity<>(s, HttpStatus.CREATED);
  }

  @GetMapping(UrlConstant.GET_BY_ID_OR_NAME_OR_MOBILENO_OR_EMAILID)
  public ResponseEntity<List<Customer>> getByIdAndName(@PathVariable String content) {
    return new ResponseEntity<>(customerService.getByIdAndName(content), HttpStatus.OK);
  }

  @GetMapping(UrlConstant.GET_ALL)
  public ResponseEntity<List<Customer>> getAllCustomer() {
    List<Customer> customers = customerService.getAllCustomer();
    return new ResponseEntity<>(customers, HttpStatus.OK);
  }

  @PutMapping(UrlConstant.UPDATE_CUSTOMER)
  public ResponseEntity<String> updateCustomer(@RequestBody CustomerDto customerDto,
      @PathVariable Long customerId) {
    String s = customerService.updateCustomer(customerDto, customerId);
    return new ResponseEntity<>(s, HttpStatus.OK);
  }

  @DeleteMapping(UrlConstant.DELETE_CUSTOMER)
  public ResponseEntity<String> deleteCustomer(@PathVariable Long customerId) {
    String s = customerService.deleteCustomer(customerId);
    return new ResponseEntity<>(s, HttpStatus.OK);
  }
}
