package com.FKFabian.medicalclinicproxy123;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableFeignClients
public class MedicalClinicProxyApplication {

	public static void main(String[] args) {
		SpringApplication.run(Medicalclinicproxy123Application.class, args);
	}

}
