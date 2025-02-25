package fr.ensai.demo;

import fr.ensai.demo.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class DemoApplication {
	@Autowired
	private static BookService bookService;
	@Autowired
	@Qualifier("bookService")
	private static BookService bookService2;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		if (DemoApplication.bookService == DemoApplication.bookService2) {
		}
	}

}
