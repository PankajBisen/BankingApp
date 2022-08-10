package bankingapplication.service;

import bankingapplication.entity.Transaction;
import java.util.List;

public interface TransactionService {

  List<Transaction> transaction(String accNo);

  List<Transaction> transactionByDays(Long numberOfDays);
}
