package com.springboot.app.oauth;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EntityScan({"com.springboot.app.commonslib.model.entity"}) // Import the entity beans from commons-lib jar package files
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class}) // Exclude auto DB configuration, we only need jpa beans, no Database connection
public class SpringbootOauthServiceApplication { //implements CommandLineRunner{

	//@Autowired
	//private BCryptPasswordEncoder passwordEncode;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringbootOauthServiceApplication.class, args);
	}

	/*
	@Override
	public void run(String... args) throws Exception {
		String password = "1234";
		
		for (int i = 0; i < 4; i++) {
			String passwordBCrypt = passwordEncode.encode(password);
			System.out.println(passwordBCrypt);
		}
		
	}
	*/

}
