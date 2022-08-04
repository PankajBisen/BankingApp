package bankingapplication.service.impl;

import bankingapplication.constant.ApplicationConstant;
import bankingapplication.entity.Transaction;
import bankingapplication.exception.BankException;
import bankingapplication.repo.TransactionRepo;
import bankingapplication.service.TransactionService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class TransactionImplService implements TransactionService {

  @Autowired
  private TransactionRepo transactionRepo;

  @Override
  public List<Transaction> transaction(Long accNo) {
    List<Transaction> transactions = transactionRepo.findByAccountNumberFrom(accNo);
    if(transactions.isEmpty()){
      throw  new BankException("No any Transaction For Given Id",HttpStatus.BAD_REQUEST);
    }else return transactions;
  }

  @Override
  public List<Transaction> transactionByDays(Long numberOfDays) {
    LocalDate localDate = LocalDate.now();
    LocalDate endDate = localDate.plusDays(numberOfDays);
    List<Transaction> transactions = transactionRepo.findByDateBetween(localDate, endDate);
    if (transactions.isEmpty()) {
      throw new BankException(ApplicationConstant.NO_TRANSACTION_IN_BETWEEN_DAYS,
          HttpStatus.BAD_REQUEST);
    }
    return transactions;
  }
}
