package uy.com.pf.care;

import lombok.extern.java.Log;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@Log
//@ComponentScan(basePackages = {"uy.com.pf.cuidados"})
public class CareApplication {

	public static void main(String[] args) {
		SpringApplication.run(CareApplication.class, args);
		//Logger log = (Logger) LoggerFactory.getLogger(CuidadosApplication.class);
		log.info("*** App iniciada!!");
	}

}
