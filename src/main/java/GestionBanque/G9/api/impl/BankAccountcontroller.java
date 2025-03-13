package GestionBanque.G9.api.impl;


import GestionBanque.G9.api.BankAccountApi;
import GestionBanque.G9.dto.*;
import GestionBanque.G9.exceptions.BalanceNotSufficientException;
import GestionBanque.G9.exceptions.BankNoteFoundException;
import GestionBanque.G9.exceptions.CustomerNotFoundException;
import GestionBanque.G9.model.BankAccount;
import GestionBanque.G9.service.BankAccountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin("*")
public class BankAccountcontroller implements BankAccountApi {

    private BankAccountService bankAccountService;

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        return bankAccountService.saveCustomer(customerDTO);

    }

    @Override
    public List<CustomerDTO> listCustomer() {
        return bankAccountService.listCustomer();
    }



    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double balanceInitial, double overDraft, Long customerId, String status) throws CustomerNotFoundException {
        return bankAccountService.saveCurrentBankAccount(balanceInitial,overDraft,customerId,status );
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double balanceInitial, double interestRate, Long customerId, String status) throws CustomerNotFoundException {
        return bankAccountService.saveSavingBankAccount(balanceInitial,interestRate,customerId,status);
    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankNoteFoundException {
        return bankAccountService.getBankAccount(accountId);
    }

    @Override
    public BankAccountDTO getBankAccountCurrentOrSaving(String accountId) throws BankNoteFoundException {
        return bankAccountService.getBankAccountCurrentOrSaving(accountId);
    }

    @Override
    public void debit(DebitDTO debitDTO) throws BankNoteFoundException, BalanceNotSufficientException {

         bankAccountService.debit(debitDTO.getAccountId(),debitDTO.getAmount(),debitDTO.getDescription());

    }

    @Override
    public void credit(CreditDTO creditDTO) throws BankNoteFoundException {
        bankAccountService.credit(creditDTO.getAccountId(),creditDTO.getAmount(),creditDTO.getDescription());
    }

    @Override
    public void transfer(TransferDTO transferDTO) throws BankNoteFoundException, BalanceNotSufficientException {
         bankAccountService.transfer(transferDTO.getAccountSource(),transferDTO.getAccountDestination(),transferDTO.getAmount());
    }


    @Override
    public List<BankAccountDTO> getAllBankAccount() {
        return bankAccountService.getAllBankAccount();
    }

    @Override
    public CustomerDTO getCustomerById(Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getCustomerById(customerId);
    }

    @Override
    public CustomerDTO updateCustomer(Long customerId, CustomerDTO customerDTO) throws CustomerNotFoundException {
         customerDTO.setId(customerId);
        return bankAccountService.updateCustomer(customerId,customerDTO);
    }

    @Override
    public void deleteCustomer(Long customerId) {
      bankAccountService.deleteCustomer(customerId);
    }

    @Override
    public List<AccountOperationDTO> getAllHistoryOfBankAccountById(String accountId) throws BankNoteFoundException {
        return bankAccountService.getAllHistoryOfBankAccountById(accountId);
    }

    @Override
    public AccountHistoriesDTO getAccountHistory( String accountId, int page, int size) throws BankNoteFoundException {

        return  bankAccountService.getAccountHistory(accountId,page,size);
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {
        return bankAccountService.searchCustomers("%"+keyword+"%");
    }

    @Override
    public List<BankAccountDTO> getAllBankAccountForCustomer(Long customerId) throws CustomerNotFoundException {
        return bankAccountService.getAllBankAccountForCustomer(customerId);
    }
}
