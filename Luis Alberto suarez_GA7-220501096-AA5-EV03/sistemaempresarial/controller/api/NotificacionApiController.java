package com.empresa.sistema.sistemaempresarial.controller.api;

import com.empresa.sistema.sistemaempresarial.model.Pedido;
import com.empresa.sistema.sistemaempresarial.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionApiController {

    @Autowired
    private PedidoRepository pedidoRepository;

    // GET: Enviar notificación a un cliente
    @PostMapping("/enviar/{id}")
    public ResponseEntity<String> enviarNotificacion(@PathVariable Long id, @RequestParam String metodo) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        String mensaje = "✅ Notificación enviada al cliente del pedido #" + id +
                " por " + metodo + ". Estado: " + pedido.getEstadoPedido();

        return ResponseEntity.ok(mensaje);
    }

    // GET: Listar notificaciones pendientes
    @GetMapping
    public ResponseEntity<List<NotificacionDTO>> listarNotificaciones() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        List<NotificacionDTO> notificaciones = pedidos.stream()
                .filter(p -> "pendiente".equals(p.getEstadoPedido()))
                .map(p -> new NotificacionDTO(p.getId(), p.getIdCliente(), p.getProducto(), "pendiente"))
                .collect(Collectors.toList());

        return ResponseEntity.ok(notificaciones);
    }

    // Clase interna DTO para la notificación
    static class NotificacionDTO {
        private Long id;
        private Long idCliente;
        private String producto;
        private String estado;

        public NotificacionDTO(Long id, Long idCliente, String producto, String estado) {
            this.id = id;
            this.idCliente = idCliente;
            this.producto = producto;
            this.estado = estado;
        }

        // Getters y Setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }

        public Long getIdCliente() { return idCliente; }
        public void setIdCliente(Long idCliente) { this.idCliente = idCliente; }

        public String getProducto() { return producto; }
        public void setProducto(String producto) { this.producto = producto; }

        public String getEstado() { return estado; }
        public void setEstado(String estado) { this.estado = estado; }
    }
}