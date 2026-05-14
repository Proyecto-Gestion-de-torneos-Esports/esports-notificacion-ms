package com.notificaciones.microservicio_notificaciones;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;


@SpringBootApplication
@EnableFeignClients
@EnableScheduling
public class MicroservicioNotificacionesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroservicioNotificacionesApplication.class, args);
	}

}
