package org.bougattaya.billingservice;

import java.util.Date;
import java.util.Random;

import org.bougattaya.billingservice.entities.Bill;
import org.bougattaya.billingservice.entities.ProductItem;
import org.bougattaya.billingservice.feign.*;
import org.bougattaya.billingservice.model.*;
import org.bougattaya.billingservice.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.hateoas.PagedModel;

@SpringBootApplication
@EnableFeignClients
public class BillingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BillingServiceApplication.class, args);
	}
	
	@Bean
	CommandLineRunner start(
			BillRepository billRepo, 
			ProductItemRepository productItemRepo, 
			CustomerRestClient customerClient, 
			ProductItemRestClient productItemClient) {
		
		return args -> {
			//init model
			Customer c = customerClient.getCustomerById(1L);
			PagedModel<Product> productPagedModel = productItemClient.pageProducts(0,5);
			
			//init entities
			Bill firstBill = billRepo.save(new Bill(null, new Date(), null, c.getId(), null));
			productPagedModel.forEach(p ->{
				ProductItem productItem = new ProductItem();
				productItem.setPrice(p.getPrice());
				productItem.setQuantity(1+ new Random().nextInt(50));
				productItem.setBill(firstBill);
				productItem.setProductId(p.getId());
				productItemRepo.save(productItem);
			});
			
		};
	}
	
} 
