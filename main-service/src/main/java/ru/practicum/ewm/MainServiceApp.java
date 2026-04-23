package ru.practicum.ewm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"ru.practicum.ewm", "ru.practicum.stat.client"})
public class MainServiceApp {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(MainServiceApp.class, args);

		// region easy test
//		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//		LocalDateTime now = LocalDateTime.now();
//
//		StatClient statClient = context.getBean(StatClient.class);
//		statClient.hit(
//				EndpointHitDto.builder()
//						.app("ru.practicum.ewm")
//						.uri("/event/1")
//						.ip("0.0.0.0")
//						.actionType("unique")
//						.timestamp(now)
//						.build()
//		);
//		List<ViewStatsDto> stats = statClient.getStat(
//				StatsRequest.builder()
//						.uris(List.of("/event/1"))
//						.start(now.format(formatter))
//						.end(LocalDateTime.now().format(formatter))
//						.unique(true)
//						.build()
//		);
//		System.out.println(stats);
		// endrerion easy test
	}
}
