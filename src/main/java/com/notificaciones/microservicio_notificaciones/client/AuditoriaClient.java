package com.notificaciones.microservicio_notificaciones.client;

import com.notificaciones.microservicio_notificaciones.dto.AuditoriaRequestDTO;
import com.notificaciones.microservicio_notificaciones.dto.AuditoriaResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "microservicio-auditoria", url = "http://localhost:8011/api/auditoria")
public interface AuditoriaClient {

    @PostMapping
    public AuditoriaResponseDTO generarAuditoria(@RequestBody AuditoriaRequestDTO auditoria);

}
