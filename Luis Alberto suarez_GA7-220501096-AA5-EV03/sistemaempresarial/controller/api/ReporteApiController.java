package com.empresa.sistema.sistemaempresarial.controller.api;

import com.empresa.sistema.sistemaempresarial.model.Pedido;
import com.empresa.sistema.sistemaempresarial.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
public class ReporteApiController {

    @Autowired
    private PedidoRepository pedidoRepository;

    // GET: Generar reporte mensual
    @GetMapping
    public ResponseEntity<ReporteDTO> generarReporte(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {

        LocalDate hoy = LocalDate.now();
        if (year == null) year = hoy.getYear();
        if (month == null) month = hoy.getMonthValue();

        LocalDateTime inicioMes = LocalDate.of(year, month, 1).atStartOfDay();
        LocalDateTime finMes = inicioMes.plusMonths(1).minusSeconds(1);

        List<Pedido> pedidos = pedidoRepository.findAll().stream()
                .filter(p -> p.getFechaPedido() != null &&
                        !p.getFechaPedido().isBefore(inicioMes) &&
                        !p.getFechaPedido().isAfter(finMes))
                .collect(Collectors.toList());

        double totalVentas = pedidos.stream()
                .mapToDouble(Pedido::getTotal)
                .sum();
        long totalPedidos = pedidos.size();

        // Productos más vendidos
        Map<String, Integer> productosVendidos = new LinkedHashMap<>();
        for (Pedido p : pedidos) {
            productosVendidos.merge(p.getProducto(), p.getCantidad(), Integer::sum);
        }
        List<Map.Entry<String, Integer>> topProductos = productosVendidos.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());

        // Clientes con más compras
        Map<Long, Long> clientesCompras = new LinkedHashMap<>();
        for (Pedido p : pedidos) {
            clientesCompras.merge(p.getIdCliente(), 1L, Long::sum);
        }
        List<Map.Entry<Long, Long>> topClientes = clientesCompras.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());

        ReporteDTO reporte = new ReporteDTO();
        reporte.setAnio(year);
        reporte.setMes(month);
        reporte.setTotalVentas(totalVentas);
        reporte.setTotalPedidos(totalPedidos);
        reporte.setTopProductos(topProductos);
        reporte.setTopClientes(topClientes);

        return ResponseEntity.ok(reporte);
    }

    // Clase interna DTO para el reporte
    static class ReporteDTO {
        private Integer anio;
        private Integer mes;
        private Double totalVentas;
        private Long totalPedidos;
        private List<Map.Entry<String, Integer>> topProductos;
        private List<Map.Entry<Long, Long>> topClientes;

        // Getters y Setters
        public Integer getAnio() { return anio; }
        public void setAnio(Integer anio) { this.anio = anio; }

        public Integer getMes() { return mes; }
        public void setMes(Integer mes) { this.mes = mes; }

        public Double getTotalVentas() { return totalVentas; }
        public void setTotalVentas(Double totalVentas) { this.totalVentas = totalVentas; }

        public Long getTotalPedidos() { return totalPedidos; }
        public void setTotalPedidos(Long totalPedidos) { this.totalPedidos = totalPedidos; }

        public List<Map.Entry<String, Integer>> getTopProductos() { return topProductos; }
        public void setTopProductos(List<Map.Entry<String, Integer>> topProductos) { this.topProductos = topProductos; }

        public List<Map.Entry<Long, Long>> getTopClientes() { return topClientes; }
        public void setTopClientes(List<Map.Entry<Long, Long>> topClientes) { this.topClientes = topClientes; }
    }
}