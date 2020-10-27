package com.innoveller.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.innoveller.demo.model.BankAccount;

import java.util.List;

@Repository
public interface BankRepository extends JpaRepository<BankAccount, Long> {
//    @Query("SELECT p FROM BankAccount p where id= :bank_account_id")
//    public BankAccount findAccountById(@Param("bank_account_id") Long bank_account_id);
}
