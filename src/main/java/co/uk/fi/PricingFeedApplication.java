package co.uk.fi;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@SpringBootApplication
public class PricingFeedApplication {

	public static void main(String[] args) {
		SpringApplication.run(PricingFeedApplication.class, args);
	}

}
