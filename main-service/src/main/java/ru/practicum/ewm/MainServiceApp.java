package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
//@ComponentScan(value = {"evm", "client"})
public class MainServiceApp {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MainServiceApp.class, args);

		StatClient statClient = context.getBean(StatClient.class);
//		statClient.hit(new ParamHitDto("/event/1"));
//		StatDto stat = statClient.getStat(new ParamDto("/event/1"));
//		System.out.println(stat);
	}
}
