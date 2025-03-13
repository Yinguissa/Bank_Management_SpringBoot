package GestionBanque.G9.service.Impl;

import GestionBanque.G9.dto.*;
import GestionBanque.G9.enums.AccountStatus;
import GestionBanque.G9.enums.OperationType;
import GestionBanque.G9.exceptions.BalanceNotSufficientException;
import GestionBanque.G9.exceptions.BankNoteFoundException;
import GestionBanque.G9.exceptions.CustomerNotFoundException;
import GestionBanque.G9.mappers.BankAccountmaperImpl;
import GestionBanque.G9.entity.*;
import GestionBanque.G9.repositorie.AccountOperationRepository;
import GestionBanque.G9.repositorie.BankAccountRepository;
import GestionBanque.G9.repositorie.CustomerRepository;
import GestionBanque.G9.service.BankAccountService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@Transactional
@AllArgsConstructor
@Slf4j
public class BankAccountServiceImpl  implements BankAccountService {

    private BankAccountRepository bankAccountRepository;

    private AccountOperationRepository accountOperationRepository;

    private CustomerRepository customerRepository;

    private BankAccountmaperImpl bankAccountmaper;

   // Logger logger= LoggerFactory.getLogger(this.getClass().getName());




    @Override
    public List<CustomerDTO> listCustomer() {
        log.info("getAll customer");
        log.info("using programing functional");
        List<Customer> customerList =  customerRepository.findAll();
        List<CustomerDTO> customerDTOList= customerList.stream()
                .map(customer -> bankAccountmaper.fromCustomer(customer))
                .collect(Collectors.toList());
        /*
        using programing imperative
        List<CustomerDTO> customerDTOList=new ArrayList<>();
        for (Customer customer:customers){
            CustomerDTO customerDTO=bankAccountmaper.fromCustomer(customer);
            customerDTOList.add(customerDTO);
        }*/
        return customerDTOList;
    }

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double balanceInitial, double overDraft, Long customerId, String status) throws CustomerNotFoundException {
         log.info("Enregistrement CompteCourant");
         Customer customer =customerRepository.findById(customerId).orElse(null);
          if (customer ==null){
              throw new CustomerNotFoundException("Client N'exite pas");
          }

        CurrentAccount currentAccount=new CurrentAccount();
          currentAccount.setId(UUID.randomUUID().toString());
          currentAccount.setCreatedAt(new Date());
          currentAccount.setBalance(balanceInitial);
          currentAccount.setOverDraft(overDraft);
          currentAccount.setCustomer(customer);
          currentAccount.setAccountStatus(AccountStatus.valueOf(status));


          CurrentAccount savedBankAccount=bankAccountRepository.save(currentAccount);
        return bankAccountmaper.fromCurrentBankAccount(savedBankAccount);
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double balanceInitial, double interestRate, Long customerId, String status) throws CustomerNotFoundException {
        log.info("Enregistrement Compte Epargné");
        Customer customer =customerRepository.findById(customerId).orElseThrow(()->new CustomerNotFoundException("customer not found"));
         if (customer ==null){
             throw new CustomerNotFoundException("Client non Trouvé");
         }

         SavingAccount savingAccount=new SavingAccount();
         savingAccount.setId(UUID.randomUUID().toString());
         savingAccount.setCreatedAt(new Date());
         savingAccount.setBalance(balanceInitial);
         savingAccount.setInterestRate(interestRate);
         savingAccount.setCustomer(customer);
         savingAccount.setAccountStatus(AccountStatus.valueOf(status));

         SavingAccount savedBankAccount=bankAccountRepository.save(savingAccount);
        return bankAccountmaper.fromSavingAccount(savedBankAccount);
    }


    @Override
    public BankAccountDTO getBankAccountCurrentOrSaving(String accountId) throws BankNoteFoundException {
        log.info("get bankAccountById");
        BankAccount bankAccount=getBankAccount(accountId);

         if ( bankAccount instanceof SavingAccount){
             SavingAccount savingAccount= (SavingAccount) bankAccount;
             return bankAccountmaper.fromSavingAccount(savingAccount);
         }else {

             CurrentAccount currentAccount=(CurrentAccount) bankAccount;
             return bankAccountmaper.fromCurrentBankAccount(currentAccount);
         }

    }

    @Override
    public BankAccount getBankAccount(String accountId) throws BankNoteFoundException {
       BankAccount bankAccount=bankAccountRepository.findById(accountId)
               .orElseThrow(()->new BankNoteFoundException("Compte Non Trouver"));
      return bankAccount;
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankNoteFoundException, BalanceNotSufficientException {
         BankAccount bankAccount=getBankAccount(accountId);
         if (bankAccount.getBalance()<amount){
             throw new BalanceNotSufficientException("Solde Insufficent");
         }
        AccountOperation operation=new AccountOperation();
         operation.setAmount(amount);
         operation.setOperationType(OperationType.DEBIT);
         operation.setDateOperation(new Date());
         operation.setDescription(description);
         operation.setBankAccount(bankAccount);
         accountOperationRepository.save(operation);
         bankAccount.setBalance(bankAccount.getBalance()-amount);
         bankAccountRepository.save(bankAccount);
         System.out.println(operation);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankNoteFoundException {

           BankAccount bankAccount=getBankAccount(accountId);


           AccountOperation operation=new AccountOperation();
           operation.setAmount(amount);
           operation.setOperationType(OperationType.CREDIT);
           operation.setDateOperation(new Date());
           operation.setDescription(description);
           operation.setBankAccount(bankAccount);
           accountOperationRepository.save(operation);
           bankAccount.setBalance(bankAccount.getBalance()+amount);
           bankAccountRepository.save(bankAccount);

    }

    @Override
    
    public void transfer(String accountIdSource, String accountIdDestination, double amount) throws BankNoteFoundException, BalanceNotSufficientException {
         debit(accountIdSource,amount,"Transfer to"+accountIdDestination);
         credit(accountIdDestination,amount,"Transfer from"+accountIdSource);
    }



    @Override
    public CustomerDTO getCustomerById(Long customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()->new CustomerNotFoundException("Client Non Trouver"));
        return bankAccountmaper.fromCustomer(customer);
    }

    @Override
    public CustomerDTO saveCustomer(CustomerDTO customerDTO) {
        log.info("Enregistrement nouveau compte");
        Customer customer =bankAccountmaper.fromCustomerDTO(customerDTO);
        Customer savedCustomer =customerRepository.save(customer);
        return bankAccountmaper.fromCustomer(savedCustomer);
    }

    @Override
    public CustomerDTO updateCustomer(Long customerId,CustomerDTO customerDTO) throws CustomerNotFoundException {
        log.info("Update customer");
        CustomerDTO customerUpdate=getCustomerById(customerId);
        if (customerUpdate==null){
            throw new CustomerNotFoundException("Client non Trouvez");
        }
        Customer customer =bankAccountmaper.fromCustomerDTO(customerDTO);
        Customer updateCustomer =customerRepository.save(customer);
        return bankAccountmaper.fromCustomer(updateCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
     log.info("Suppresion Du client");
     customerRepository.deleteById(customerId);
    }

    @Override
    public List<AccountOperationDTO> getAllHistoryOfBankAccountById(String accountId) throws BankNoteFoundException {
         BankAccount bankAccount=getBankAccount(accountId);

        List<AccountOperation>  accountOperations =accountOperationRepository.findByBankAccountId(bankAccount.getId());

        return accountOperations.stream().map(accountOperation ->
                bankAccountmaper.fromAccountOperation(accountOperation)).collect(Collectors.toList());
    }

    @Override
    public AccountHistoriesDTO getAccountHistory(String accountId, int page, int size) throws BankNoteFoundException {
         BankAccount bankAccount=getBankAccount(accountId);
       Page<AccountOperation> accountOperations = accountOperationRepository.findByBankAccountIdOrderByDateOperationDesc(accountId, PageRequest.of(page,size));
       AccountHistoriesDTO accountHistoriesDTO=new AccountHistoriesDTO();
       List<AccountOperationDTO>  accountHistoriesDTOList=accountOperations.getContent().stream().map(opp->
                bankAccountmaper.fromAccountOperation(opp)).collect(Collectors.toList());

        accountHistoriesDTO.setAccountOperationDTOList(accountHistoriesDTOList);
        accountHistoriesDTO.setAccountId(bankAccount.getId());
        accountHistoriesDTO.setBalance(bankAccount.getBalance());
        accountHistoriesDTO.setCurrentPage(page);
        accountHistoriesDTO.setPageSize(size);
        accountHistoriesDTO.setTotalPages(accountOperations.getTotalPages());
        //accountHistoriesDTO.setCurrentPage(accountHistoriesDTOList.indexOf(accountHistoriesDTO.getCurrentPage()));


        return accountHistoriesDTO;
    }

    @Override
    public List<CustomerDTO> searchCustomers(String keyword) {

       List<Customer> customers =customerRepository.searchCustomer(keyword);

        return customers.stream().map(customer ->
                bankAccountmaper.fromCustomer(customer)).collect(Collectors.toList());
    }
    @Override
    public List<BankAccountDTO> getAllBankAccount() {
        List<BankAccount> bankAccountList=   bankAccountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOList= bankAccountList.stream().map(bankAccount ->{
            if ( bankAccount instanceof SavingAccount){
                SavingAccount savingAccount= (SavingAccount) bankAccount;
                return bankAccountmaper.fromSavingAccount(savingAccount);
            }else {

                CurrentAccount currentAccount= (CurrentAccount) bankAccount;
                return bankAccountmaper.fromCurrentBankAccount(currentAccount);
            }

        }).collect(Collectors.toList());

        return bankAccountDTOList;
    }
    @Override
    public List<BankAccountDTO> getAllBankAccountForCustomer(Long customerId) throws CustomerNotFoundException {
         CustomerDTO customer=getCustomerById(customerId);
         List<BankAccount> bankAccountList=bankAccountRepository.findBankAccountByCustomer_Id(customer.getId());
         List<BankAccountDTO> bankAccountDTOList=bankAccountList.stream().map(bankAccount -> {
              if (bankAccount instanceof SavingAccount){
                   SavingAccount savingAccount= (SavingAccount) bankAccount;
                   return bankAccountmaper.fromSavingAccount(savingAccount);
              }else {
                  CurrentAccount currentAccount= (CurrentAccount) bankAccount;
                  return bankAccountmaper.fromCurrentBankAccount(currentAccount);
              }
         }).collect(Collectors.toList());

        return bankAccountDTOList;
    }
}
