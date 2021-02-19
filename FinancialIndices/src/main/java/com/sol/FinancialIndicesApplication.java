package com.sol;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableScheduling
public class FinancialIndicesApplication {

	public static void main(String[] args) {
		SpringApplication.run(FinancialIndicesApplication.class, args);
	}

}
