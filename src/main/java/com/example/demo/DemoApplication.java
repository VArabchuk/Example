package com.example.demo;

import com.example.demo.model.ExampleEntity;
import com.example.demo.repository.EntityRepository;
import java.util.stream.IntStream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {

		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(EntityRepository repository) {
		return args -> {
			int numberOfEntities = 10; // Тут можна змінювати кількість об'єктів
			IntStream.rangeClosed(1, numberOfEntities).forEach(i -> {
				ExampleEntity entity = new ExampleEntity();
				entity.setField1("Value " + i);
				entity.setField2("Value " + i);
				entity.setField3("Value " + i);
				entity.setField4("Value " + i);
				entity.setField5("Value " + i);
				entity.setField6("Value " + i);
				entity.setField7("Value " + i);
				entity.setField8("Value " + i);
				entity.setField9("Value " + i);
				entity.setField10("Value " + i);
				repository.save(entity);
			});
		};
	}
}
