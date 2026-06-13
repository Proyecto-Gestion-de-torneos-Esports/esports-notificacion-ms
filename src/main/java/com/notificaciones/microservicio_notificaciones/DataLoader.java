package com.notificaciones.microservicio_notificaciones;

import com.notificaciones.microservicio_notificaciones.model.Estado;
import com.notificaciones.microservicio_notificaciones.model.Notificacion;
import com.notificaciones.microservicio_notificaciones.repository.EstadoRepository;
import com.notificaciones.microservicio_notificaciones.repository.NotificacionRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Profile("dev")
@Configuration
public class DataLoader implements CommandLineRunner {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private EstadoRepository estadoRepository;

    @Override
    public void run(String... args) throws Exception{
        Faker faker = new Faker();
        Random random = new Random();

        Estado estado = new Estado();
        estado.setIdEstado(1L);
        estado.setEstado("Pendiente");
        estadoRepository.save(estado);

        Estado estado1 = new Estado();
        estado1.setIdEstado(2L);
        estado1.setEstado("Enviado");
        estadoRepository.save(estado1);

        for(int i = 0; i<3; i++) {
            Notificacion notificacion = new Notificacion();
            notificacion.setIdNotificacion((long) (i + 1));
            notificacion.setMensaje("La partida va a comenzar pronto");
            notificacion.setTipo("Aviso de partida");
            notificacion.setFecha_envio(faker.timeAndDate().past(365, TimeUnit.DAYS).atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString());
            notificacion.setEstado(estado);
            notificacionRepository.save(notificacion);
        }

        }
    }