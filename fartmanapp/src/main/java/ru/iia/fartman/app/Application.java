package ru.iia.fartman.app;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;
import ru.iia.fartman.site.services.LigaConfiger;

@SpringBootApplication

@ComponentScan("ru.iia")
public class Application {
	private static final Logger logger = LogManager.getLogger(Application.class);

	@Autowired
	private ApplicationContext context;

	public static void main(String... args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public LigaConfiger bean() {
		return new LigaConfiger();
	}
}
