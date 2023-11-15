package uy.com.pf.care;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;

import java.nio.charset.StandardCharsets;
import java.util.Collections;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
//@ComponentScan(basePackages = {"uy.com.pf.care"})
@Log
public class CareApplication {

	public static void main(String[] args) {
		SpringApplication.run(CareApplication.class, args);
		log.info("*** Backend iniciado!!");
	}
}
