package org.bougattaya.billingservice.web;

import org.bougattaya.billingservice.entities.Bill;
import org.bougattaya.billingservice.feign.CustomerRestClient;
import org.bougattaya.billingservice.feign.ProductItemRestClient;
import org.bougattaya.billingservice.repository.BillRepository;
import org.bougattaya.billingservice.repository.ProductItemRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BillingController {
	
	private BillRepository billRepo;
	private ProductItemRepository productItemRepo;
	private CustomerRestClient custClient;
	private ProductItemRestClient prodClient;
	
	public BillingController(BillRepository br,ProductItemRepository pr,CustomerRestClient cc,ProductItemRestClient pc  ) {
		this.billRepo = br;
		this.productItemRepo = pr;
		this.custClient = cc;
		this.prodClient = pc;
	}
	
	@GetMapping(path = "/fullBill/{id}")
	public Bill getBill(@PathVariable(name="id") Long id) {
		Bill bill = billRepo.findById(id).get();
		
		//manipulation
		//BILL || customer
		//we are sure that the id exists so NO NEED FOR OPTIONAL HANDLING
		bill.setCustomer(custClient.getCustomerById(bill.getCustomerId()));
		//PRODUCTITEM || product
		bill.getProductItems().forEach(p->{
			p.setProductName(prodClient.getProductById(p.getId()).getName());	
		});
		
		return bill;
	}
}
