package GestionBanque.G9.service;


import GestionBanque.G9.entity.Customer;

import java.util.List;

public interface CustomerService {

    Customer saveCustomer(Customer customer);

    List<Customer> listCustomer();
}
