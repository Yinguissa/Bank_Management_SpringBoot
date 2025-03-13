package GestionBanque.G9.dto;


import GestionBanque.G9.enums.AccountStatus;

import java.util.Date;

public class BankAccountCustomer {
    private String id;
    private double Balance;
    private Date createdAt;
    private AccountStatus accountStatus;
    private double overDraft;
}
