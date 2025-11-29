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
@RequestMapping("/api/seguimientos")
public class SeguimientoApiController {

    @Autowired
    private PedidoRepository pedidoRepository;

    // GET: Obtener estado de seguimiento de un pedido
    @GetMapping("/{id}")
    public ResponseEntity<String> obtenerSeguimiento(@PathVariable Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        String estado = "pendiente";
        if ("completado".equals(pedido.getEstadoPedido())) {
            estado = "entregado";
        } else if ("pendiente".equals(pedido.getEstadoPedido())) {
            long dias = ChronoUnit.DAYS.between(pedido.getFechaPedido().toLocalDate(), LocalDateTime.now().toLocalDate());
            if (dias > 3) {
                estado = "en tránsito";
            } else {
                estado = "pendiente";
            }
        }

        return ResponseEntity.ok(estado);
    }

    // GET: Listar todos los seguimientos
    @GetMapping
    public ResponseEntity<List<SeguimientoDTO>> listarSeguimientos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        List<SeguimientoDTO> seguimientos = pedidos.stream()
                .map(p -> {
                    String estado = "pendiente";
                    if ("completado".equals(p.getEstadoPedido())) {
                        estado = "entregado";
                    } else if ("pendiente".equals(p.getEstadoPedido())) {
                        long dias = ChronoUnit.DAYS.between(p.getFechaPedido().toLocalDate(), LocalDateTime.now().toLocalDate());
                        if (dias > 3) {
                            estado = "en tránsito";
                        } else {
                            estado = "pendiente";
                        }
                    }
                    return new SeguimientoDTO(p.getId(), p.getIdCliente(), p.getProducto(), estado);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(seguimientos);
    }

    // Clase interna DTO para el seguimiento
    static class SeguimientoDTO {
        private Long id;
        private Long idCliente;
        private String producto;
        private String estado;

        public SeguimientoDTO(Long id, Long idCliente, String producto, String estado) {
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