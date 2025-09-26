package com.ecom.service;

import java.util.List;

import com.ecom.model.Order;
import com.ecom.model.Seller;
import com.ecom.model.Transaction;
import com.ecom.model.User;

public interface TransactionService {

    Transaction createTransaction(Order order);
    List<Transaction> getTransactionBySeller(Seller seller);
    List<Transaction>getAllTransactions();
}
