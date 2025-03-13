package GestionBanque.G9.api;


import GestionBanque.G9.dto.*;
import GestionBanque.G9.exceptions.BalanceNotSufficientException;
import GestionBanque.G9.exceptions.BankNoteFoundException;
import GestionBanque.G9.exceptions.CustomerNotFoundException;
import GestionBanque.G9.entity.BankAccount;
import org.springframework.web.bind.annotation.*;

import java.util.List;

public interface BankAccountApi {

    @RequestMapping(value = "/AjoutClient",method = RequestMethod.POST)
    CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO);


    @RequestMapping(value = "/TousLesClients",method = RequestMethod.GET)
    List<CustomerDTO> listCustomer();


    @RequestMapping(value = "AjouterCompteCourant/{customerId}",method = RequestMethod.POST)
    CurrentBankAccountDTO saveCurrentBankAccount(@RequestParam(name = "balanceInitial",defaultValue = "100000") double balanceInitial,
                                                 @RequestParam(name = "overDraft",defaultValue = "9000") double overDraft,
                                                 @PathVariable Long customerId,
                                                 @RequestParam (name = "status",required = false, defaultValue = "CREATED") String status) throws CustomerNotFoundException;


    @RequestMapping(value = "AjouterCompteEpargne/{customerId}",method = RequestMethod.POST)
    SavingBankAccountDTO saveSavingBankAccount(@RequestParam(name = "balanceInitial",defaultValue = "100000") double balanceInitial,
                                               @RequestParam(name = "interestRate",defaultValue = "5.9") double interestRate,
                                               @PathVariable Long customerId,
                                               @RequestParam (name = "status",required = false, defaultValue = "CREATED") String status
                                               ) throws CustomerNotFoundException;

    @RequestMapping(value = "obtenirCompteBancaireParId")
    BankAccount getBankAccount(String accountId) throws BankNoteFoundException;

    @RequestMapping(value = "compte/{accountId}",method = RequestMethod.GET)
    BankAccountDTO getBankAccountCurrentOrSaving(@PathVariable String accountId) throws BankNoteFoundException;

    @RequestMapping(value = "soldecompte",method = RequestMethod.POST)
    void debit( @RequestBody DebitDTO debitDTO) throws BankNoteFoundException, BalanceNotSufficientException;


    @RequestMapping(value = "creditAccount",method = RequestMethod.POST)
    void credit( @RequestBody CreditDTO creditDTO) throws BankNoteFoundException;

    @RequestMapping(value = "Transfert", method = RequestMethod.POST)
    void transfer(@RequestBody TransferDTO transferDTO) throws BankNoteFoundException, BalanceNotSufficientException;

    @RequestMapping(value = "/getAllBankAccount",method = RequestMethod.GET)
    List<BankAccountDTO> getAllBankAccount();

    @RequestMapping(value = "RecupererclientParId/{id}",method = RequestMethod.GET)
    CustomerDTO getCustomerById(@PathVariable(name = "id") Long customerId) throws CustomerNotFoundException;

    @RequestMapping(value = "modifierclient/{customerId}", method = RequestMethod.PUT)
    CustomerDTO updateCustomer(@PathVariable(name = "customerId") Long customerId, @RequestBody CustomerDTO customerDTO) throws CustomerNotFoundException;

    @RequestMapping(value = "supprimerclient/{customerId}",method = RequestMethod.DELETE)
    void deleteCustomer(@PathVariable Long customerId);

    @RequestMapping(value = "obtenirTouteHistoriqueParId/{accountId}",method = RequestMethod.GET)
    List<AccountOperationDTO> getAllHistoryOfBankAccountById(@PathVariable String accountId) throws BankNoteFoundException;


    @RequestMapping(value = "Recupererhistorique du comptebancaire/{accountId}",method = RequestMethod.GET)
    AccountHistoriesDTO getAccountHistory(@PathVariable String accountId,
                                          @RequestParam(name = "page",defaultValue = "0") int page,
                                          @RequestParam(name = "size",defaultValue = "5") int size)
            throws BankNoteFoundException;

    @RequestMapping(value = "client/nom",method = RequestMethod.GET)
    List<CustomerDTO> searchCustomers(@RequestParam(name = "keyword",defaultValue = "") String keyword);

    @RequestMapping(value = "CompteBancaireparClient/{customerId}",method = RequestMethod.GET)
    List<BankAccountDTO> getAllBankAccountForCustomer(@PathVariable Long customerId) throws CustomerNotFoundException;

}
