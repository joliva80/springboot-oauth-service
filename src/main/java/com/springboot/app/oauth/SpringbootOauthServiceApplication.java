package com.springboot.app.oauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
@EntityScan({"com.springboot.app.commonslib.model.entity"}) // Import the entity beans from commons-lib jar package files
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class}) // Exclude auto DB configuration, we only need jpa beans, no Database connection
public class SpringbootOauthServiceApplication
{
	/* 
		https://bcrypt-generator.com to store user passwords encrypted
		jwt.io to check the access_token is valid
		post http://localhost:8090/api-oauth/oauth/token
		Authorization -> Basic Auth -> username + password (plain text) will create a Authorization header basic base 64
		Body x-www-form-urlencoded  ->  username + password (plain text) + grant_type = password
	*/
	@Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

	public static void main(String[] args) {
		SpringApplication.run(SpringbootOauthServiceApplication.class, args);
	}
	

}
