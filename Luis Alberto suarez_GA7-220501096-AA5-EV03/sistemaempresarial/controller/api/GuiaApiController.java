package com.empresa.sistema.sistemaempresarial.controller.api;

import com.empresa.sistema.sistemaempresarial.model.Pedido;
import com.empresa.sistema.sistemaempresarial.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/guias")
public class GuiaApiController {

    @Autowired
    private PedidoRepository pedidoRepository;

    // GET: Listar todas las guías (pedidos con estado "entregado")
    @GetMapping
    public ResponseEntity<List<Pedido>> listarGuias() {
        List<Pedido> guias = pedidoRepository.findAll().stream()
                .filter(p -> "pendiente".equals(p.getEstadoPedido()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(guias);
    }

    // GET: Obtener una guía por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerGuia(@PathVariable Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Guía no encontrada"));
        if (!"completado".equals(pedido.getEstadoPedido())) {
            throw new RuntimeException("El pedido no es una guía (no está completado)");
        }
        return ResponseEntity.ok(pedido);
    }
}
