
package com.empresa.sistema.sistemaempresarial.controller.api;

import com.empresa.sistema.sistemaempresarial.model.Pedido;
import com.empresa.sistema.sistemaempresarial.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // ← ¡Clave! Devuelve JSON, no vistas
@RequestMapping("/api/pedidos") // ← Todas las rutas empiezan con /api/pedidos
public class PedidoApiController {

    @Autowired
    private PedidoRepository pedidoRepository;

    // GET: Obtener todos los pedidos
    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerTodos() {
        List<Pedido> pedidos = pedidoRepository.findAll();
        return ResponseEntity.ok(pedidos);
    }

    // GET: Obtener un pedido por ID
    @GetMapping("/{id}")
    public ResponseEntity<Pedido> obtenerPorId(@PathVariable Long id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
        return ResponseEntity.ok(pedido);
    }

    // POST: Crear un nuevo pedido
    @PostMapping
    public ResponseEntity<Pedido> crearPedido(@RequestBody Pedido pedido) {
        // Asegurar fecha si no viene
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(java.time.LocalDateTime.now());
        }
        Pedido guardado = pedidoRepository.save(pedido);
        return ResponseEntity.ok(guardado);
    }

    // PUT: Actualizar un pedido
    @PutMapping("/{id}")
    public ResponseEntity<Pedido> actualizarPedido(@PathVariable Long id, @RequestBody Pedido pedidoActualizado) {
        Pedido pedidoExistente = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));

        // Actualizar campos
        pedidoExistente.setProducto(pedidoActualizado.getProducto());
        pedidoExistente.setCantidad(pedidoActualizado.getCantidad());
        pedidoExistente.setTotal(pedidoActualizado.getTotal());
        pedidoExistente.setEstadoPedido(pedidoActualizado.getEstadoPedido());

        Pedido actualizado = pedidoRepository.save(pedidoExistente);
        return ResponseEntity.ok(actualizado);
    }

    // DELETE: Eliminar un pedido
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPedido(@PathVariable Long id) {
        pedidoRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}