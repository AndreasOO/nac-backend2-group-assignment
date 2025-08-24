package org.josandlin.productfetcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = "org.josandlin.library.entity")
@ComponentScan(basePackages = {
        "org.josandlin.productfetcher",
        "org.josandlin.library.mapper"
})
public class ProductFetcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductFetcherApplication.class, args);
	}

}
