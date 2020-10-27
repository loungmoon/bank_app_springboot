package com.innoveller.demo.repository;

import com.innoveller.demo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TransactionReposity extends JpaRepository<Transaction, Long> {
    @Query("SELECT p FROM Transaction p where bankAccount.id= :bank_account_id")
    public List<Transaction> find(@Param("bank_account_id") Long bank_account_id);

    @Query("from Transaction s where DATE(s.transactionDate) = :date")
    public List<Transaction> findByDate(@Param("date") Date date);

    @Query("from Transaction s where DATE(s.transactionDate) between :sdate and :edate")
    public List<Transaction> findByDateRange(@Param("sdate") Date sdate, @Param("edate") Date edate);
}
