package com.innoveller.demo.web;

import com.innoveller.demo.dto.BankAccountDto;
import com.innoveller.demo.dto.TransactionDto;
import com.innoveller.demo.model.BankAccount;
import com.innoveller.demo.model.Transaction;
import com.innoveller.demo.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {
    @Autowired
    BankService service;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/home")
    public String viewHomePage(Model model, @RequestParam("accountId") Long accountId) {
        if (accountId != null && accountId != 0) {
            List<BankAccount> list = new ArrayList<>();
            BankAccount bankAccount = service.findAccountById(accountId);
            list.add(bankAccount);
            model.addAttribute("listBankAccount", list);
        } else {
            model.addAttribute("listBankAccount", service.getAllBankAccount());
        }

        BankAccount account = new BankAccount();
        model.addAttribute("account", account);
        return "home";
    }

    @PostMapping("/search")
    public String showList(Model model, @ModelAttribute("Bankaccount") BankAccount account) {
        return "redirect:/home?accountId=" + account.getId();
    }

//    @GetMapping("/")
//    public String viewHomePage(Model model){
//        model.addAttribute("listBankAccount", service.getAllBankAccount());
//
//        BankAccountDto bankAccountDto = new BankAccountDto();
//        model.addAttribute("bankAccountDto", bankAccountDto);
//        return "index";
//    }

    @GetMapping("/showNewAccountForm")
    public String showNewAccountForm(Model model) {
        BankAccountDto bankAccount = new BankAccountDto();
        model.addAttribute("bankAccount", bankAccount);
        return "new_account";
    }

    @PostMapping("/saveAccount")
    public String saveAccount(@ModelAttribute("BankAccountDto") BankAccountDto bankAccountDto) {
        service.createAccount(bankAccountDto);
        return "redirect:/index?accountId=0";
    }

    @GetMapping(value = "/deposit")
    public String deposit(Model model, @RequestParam("accountId") Long accountId) {
        BankAccount account = service.findAccountById(accountId);

        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setId(account.getId());
        model.addAttribute("bankAccountDto", bankAccountDto);

        return "deposit";
    }

    @PostMapping(value = "/deposit")
    public String depositPOST(@ModelAttribute("bankAccountDto") BankAccountDto bankAccountDto) {
        BankAccount account = service.findAccountById(bankAccountDto.getId());
        service.deposit(account, bankAccountDto.getBalance());
        return "redirect:/transaction";
    }

    @GetMapping(value = "/withdraw")
    public String withdraw(Model model, @RequestParam("accountId") Long accountId) {
        BankAccount account = service.findAccountById(accountId);

        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setId(account.getId());
        model.addAttribute("bankAccountDto", bankAccountDto);

        return "withdraw";
    }

    @PostMapping(value = "/withdraw")
    public String withdrawPOST(@ModelAttribute("bankAccountDto") BankAccountDto bankAccountDto) {
        BankAccount account = service.findAccountById(bankAccountDto.getId());
        service.withdraw(account, bankAccountDto.getBalance());
        return "redirect:/transaction";
    }

    @GetMapping("/transfer")
    public String transfer(Model model, @RequestParam("accountId") Long accountId) {
        BankAccount account = service.findAccountById(accountId);
        List<BankAccount> list = service.getAllBankAccount();
        List<BankAccount> newList = new ArrayList<>();
        for (BankAccount bankAccount : list) {
            if (!bankAccount.getId().equals(account.getId())) {
                newList.add(bankAccount);
            }
            model.addAttribute("listBankAccount", newList);
        }

        BankAccountDto bankAccountDto = new BankAccountDto();
        bankAccountDto.setId(account.getId());
        model.addAttribute("bankAccountDto", bankAccountDto);
        return "transfer";
    }

    @RequestMapping(value = "/transfer", method = RequestMethod.POST)
    public String transferPOST(@ModelAttribute("bankAccountDto") BankAccountDto bankAccountDto) {
        BankAccount fromAccount = service.findAccountById(bankAccountDto.getId());
        BankAccount toAccount = service.findAccountById(bankAccountDto.getReceiverId());
        service.transfer(fromAccount, toAccount, bankAccountDto.getBalance());
        return "redirect:/transaction";
    }

    @GetMapping("/transaction")
    public String viewAllTransaction(Model model) {
        model.addAttribute("transactionList", service.getAccountTransactionList());

        TransactionDto transactionDto = new TransactionDto();
        model.addAttribute("transactionDto", transactionDto);
        return "transaction";
    }

    @PostMapping(value = "/transaction", params = "date")
    public void showTransaction(Model model, @ModelAttribute("transactionDto") TransactionDto transactionDto) throws ParseException {
        if (transactionDto.getTransactionDate() != null) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<Transaction> list = service.reportForOneDay(simpleDateFormat.parse(transactionDto.getTransactionDate()));
            if (list != null) {
                model.addAttribute("transactionList", list);
            } else {
                model.addAttribute("transactionList", new ArrayList<>());
            }
        }
    }

    @PostMapping(value = "/transaction", params = "betweendate")
    public void searchBetweenDate(Model model, @ModelAttribute("transactionDto") TransactionDto transactionDto) throws ParseException {
        if (transactionDto.getStrStartDate() != null && transactionDto.getStrEndDate() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            List<Transaction> list = service.reportOfDateRange(dateFormat.parse(transactionDto.getStrStartDate()), dateFormat.parse(transactionDto.getStrEndDate()));
            if (list != null) {
                model.addAttribute("transactionList", list);
            } else {
                model.addAttribute("transactionList", new ArrayList<>());
            }
        }
    }
}