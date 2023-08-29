package fr.nablihatem3.costomermanager;



import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author Hatem NABLI
 * @version 1.0
 * @since 10/08/2023
 */
@SpringBootApplication( exclude = { SecurityAutoConfiguration.class })
public class CustomermanagerApplication {

	private static final int STRENGHT = 12;
	public static void main(String[] args) {
		SpringApplication.run(CustomermanagerApplication.class, args);
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder(STRENGHT);
	}

}
