package org.josandlin.webapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = {
        "org.josandlin.library.entity.order",
        "org.josandlin.library.entity.product",
        "org.josandlin.library.entity.user"
})
@ComponentScan(basePackages = {
        "org.josandlin.webapp",
        "org.josandlin.library.mapper.order",
        "org.josandlin.library.mapper.product",
        "org.josandlin.library.mapper.user"
})
public class WebAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebAppApplication.class, args);
	}

}
