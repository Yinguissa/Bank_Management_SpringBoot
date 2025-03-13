package GestionBanque.G9.service;


import GestionBanque.G9.dto.*;
import GestionBanque.G9.exceptions.BalanceNotSufficientException;
import GestionBanque.G9.exceptions.BankNoteFoundException;
import GestionBanque.G9.exceptions.CustomerNotFoundException;
import GestionBanque.G9.entity.BankAccount;

import java.util.List;

public interface BankAccountService {
    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> listCustomer();
    CurrentBankAccountDTO saveCurrentBankAccount(double balanceInitial, double overDraft, Long customerId, String status) throws CustomerNotFoundException;

    SavingBankAccountDTO saveSavingBankAccount(double balanceInitial, double interestRate, Long customerId, String status) throws CustomerNotFoundException;
    BankAccountDTO getBankAccountCurrentOrSaving(String accountId) throws BankNoteFoundException;
    BankAccount getBankAccount(String accountId) throws BankNoteFoundException;
    void debit(String accountId, double amount,String description) throws BankNoteFoundException, BalanceNotSufficientException;

    void credit(String accountId, double amount,String description) throws BankNoteFoundException;

    void transfer(String accountIdSource,String accountIdDestination, double amount) throws BankNoteFoundException, BalanceNotSufficientException;

    List<BankAccountDTO> getAllBankAccount();

    CustomerDTO getCustomerById(Long customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(Long customerId,CustomerDTO customerDTO) throws CustomerNotFoundException;

    void deleteCustomer(Long customerId);

    List<AccountOperationDTO> getAllHistoryOfBankAccountById(String accountId) throws BankNoteFoundException;

    AccountHistoriesDTO getAccountHistory(String accountId,int page,int size) throws BankNoteFoundException;

    List<CustomerDTO> searchCustomers(String keyword);

    List<BankAccountDTO> getAllBankAccountForCustomer(Long customerId) throws CustomerNotFoundException;
}
