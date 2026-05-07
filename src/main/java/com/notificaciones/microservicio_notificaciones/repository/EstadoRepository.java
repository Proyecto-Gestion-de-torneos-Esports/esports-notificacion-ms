package com.notificaciones.microservicio_notificaciones.repository;

import com.notificaciones.microservicio_notificaciones.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoRepository extends JpaRepository <Estado, Long> {

}
