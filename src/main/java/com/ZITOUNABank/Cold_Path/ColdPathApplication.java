package com.ZITOUNABank.Cold_Path;

import org.apache.camel.spring.boot.CamelSpringBootApplicationController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.*;
import org.springframework.util.DefaultPropertiesPersister;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.TimeZone;



@SpringBootApplication
@ImportResource("classpath:camel-context.xml")
public class ColdPathApplication {


	public static void main(String[] args) {
		TimeZone timezone = TimeZone.getTimeZone("GMT+1");
		TimeZone.setDefault(timezone);


		ApplicationContext applicationContext = new SpringApplication(ColdPathApplication.class).run(args);
		CamelSpringBootApplicationController applicationController =
				applicationContext.getBean(CamelSpringBootApplicationController.class);
		applicationController.run();
	}



}
