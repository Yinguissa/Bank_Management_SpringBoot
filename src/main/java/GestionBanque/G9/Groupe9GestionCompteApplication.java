package GestionBanque.G9;

import GestionBanque.G9.dto.BankAccountDTO;
import GestionBanque.G9.dto.CurrentBankAccountDTO;
import GestionBanque.G9.dto.CustomerDTO;
import GestionBanque.G9.dto.SavingBankAccountDTO;
import GestionBanque.G9.enums.AccountStatus;
import GestionBanque.G9.exceptions.CustomerNotFoundException;
import GestionBanque.G9.service.BankAccountService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.stream.Stream;

@SpringBootApplication

public class Groupe9GestionCompteApplication {

    public static void main(String[] args) {
        SpringApplication.run(Groupe9GestionCompteApplication.class, args);
    }
    @Bean
   CommandLineRunner commandLineRunner(BankAccountService bankAccountService){
        return args -> {
            Stream.of("Alpha Ibrahima Yinguissa ","Mariame Sow","Bayo Fodé","Gsan-Tech").forEach(name->{
                CustomerDTO customer=new CustomerDTO();

                customer.setName(name);
                customer.setEmail(name+"@gmail.com");
                bankAccountService.saveCustomer(customer);
            });
            bankAccountService.listCustomer().forEach(customer -> {
                try {
                    bankAccountService.saveCurrentBankAccount(Math.random(),9000, customer.getId(), AccountStatus.CREATED.name());
                    bankAccountService.saveSavingBankAccount(Math.random(),5.9, customer.getId(),AccountStatus.CREATED.name());


                } catch (CustomerNotFoundException e) {
                    e.printStackTrace();
                }

            });

            List<BankAccountDTO> bankAccounts=  bankAccountService.getAllBankAccount();
            for (BankAccountDTO bankAccount:bankAccounts){


                for (int i=0;i<10;i++){
                    String accountId;
                    if (bankAccount instanceof SavingBankAccountDTO){
                        accountId=((SavingBankAccountDTO) bankAccount).getId();
                    }else {
                        accountId=((CurrentBankAccountDTO) bankAccount).getId();

                    }
                    bankAccountService.credit(accountId,1000+Math.random()*1200000,"Credit");
                    bankAccountService.debit(accountId,1000+Math.random()*9000,"Debit");
                }
            }
        };
    }

}
