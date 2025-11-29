// src/main/java/com/empresa/sistema/sistemaempresarial/controller/ReporteController.java
package com.empresa.sistema.sistemaempresarial.controller;

import com.empresa.sistema.sistemaempresarial.model.Pedido;
import com.empresa.sistema.sistemaempresarial.repository.PedidoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.time.format.DateTimeFormatter;

@Controller
public class ReporteController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping("/reportes")
    public String mostrarReporte(
            @RequestParam(required = false) String fecha,      // para día específico
            @RequestParam(required = false) Integer year,       // para mes
            @RequestParam(required = false) Integer month,      // para mes
            HttpSession session,
            Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }

        LocalDate hoy = LocalDate.now();
        List<Pedido> todosPedidos = pedidoRepository.findAll();

        double totalVentas = 0;
        long totalPedidos = 0;
        List<Map.Entry<String, Integer>> topProductos = new ArrayList<>();
        List<Map.Entry<Long, Long>> topClientes = new ArrayList<>();
        String tipoReporte = "día";
        String rangoTexto = "";

        if (fecha != null && !fecha.isEmpty()) {
            // Reporte por día
            LocalDate fechaSel = LocalDate.parse(fecha);
            LocalDateTime inicio = fechaSel.atStartOfDay();
            LocalDateTime fin = fechaSel.atTime(23, 59, 59);

            List<Pedido> pedidos = todosPedidos.stream()
                    .filter(p -> p.getFechaPedido() != null &&
                            !p.getFechaPedido().isBefore(inicio) &&
                            !p.getFechaPedido().isAfter(fin))
                    .collect(Collectors.toList());

            totalVentas = pedidos.stream().mapToDouble(Pedido::getTotal).sum();
            totalPedidos = pedidos.size();
            rangoTexto = fechaSel.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("es")));
            model.addAttribute("fecha", fecha);

        } else if (year != null && month != null) {
            // Reporte mensual
            LocalDate inicioMes = LocalDate.of(year, month, 1);
            LocalDate finMes = inicioMes.plusMonths(1).minusDays(1);
            LocalDateTime inicio = inicioMes.atStartOfDay();
            LocalDateTime fin = finMes.atTime(23, 59, 59);

            List<Pedido> pedidos = todosPedidos.stream()
                    .filter(p -> p.getFechaPedido() != null &&
                            !p.getFechaPedido().isBefore(inicio) &&
                            !p.getFechaPedido().isAfter(fin))
                    .collect(Collectors.toList());

            totalVentas = pedidos.stream().mapToDouble(Pedido::getTotal).sum();
            totalPedidos = pedidos.size();
            rangoTexto = inicioMes.format(DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("es")));
            tipoReporte = "mes";
            model.addAttribute("year", year);
            model.addAttribute("month", month);

        } else {
            // Por defecto: mes actual
            int añoActual = hoy.getYear();
            int mesActual = hoy.getMonthValue();
            return "redirect:/reportes?year=" + añoActual + "&month=" + mesActual;
        }

        // --- Cálculos comunes ---
        Map<String, Integer> productosVendidos = new LinkedHashMap<>();
        for (Pedido p : todosPedidos) {
            if (p.getFechaPedido() != null) {
                // Solo incluir si está en el rango ya filtrado
                // (ya se filtró arriba, así que aquí usamos la lista "pedidos" si la tienes)
                // Pero para simplificar, volvemos a filtrar aquí (o mejor: pasa la lista ya filtrada)
            }
        }
        // 👇 Mejor: hacer los cálculos con la lista ya filtrada (pedidos)
        // Vamos a refactorizar arriba para tener "List<Pedido> pedidos" en ambos casos

        // Rehacemos los cálculos con la lista correcta
        List<Pedido> pedidosFiltrados;
        if (fecha != null && !fecha.isEmpty()) {
            LocalDate fechaSel = LocalDate.parse(fecha);
            LocalDateTime inicio = fechaSel.atStartOfDay();
            LocalDateTime fin = fechaSel.atTime(23, 59, 59);
            pedidosFiltrados = todosPedidos.stream()
                    .filter(p -> p.getFechaPedido() != null &&
                            !p.getFechaPedido().isBefore(inicio) &&
                            !p.getFechaPedido().isAfter(fin))
                    .collect(Collectors.toList());
        } else {
            LocalDate inicioMes = LocalDate.of(year, month, 1);
            LocalDate finMes = inicioMes.plusMonths(1).minusDays(1);
            LocalDateTime inicio = inicioMes.atStartOfDay();
            LocalDateTime fin = finMes.atTime(23, 59, 59);
            pedidosFiltrados = todosPedidos.stream()
                    .filter(p -> p.getFechaPedido() != null &&
                            !p.getFechaPedido().isBefore(inicio) &&
                            !p.getFechaPedido().isAfter(fin))
                    .collect(Collectors.toList());
        }

        totalVentas = pedidosFiltrados.stream().mapToDouble(Pedido::getTotal).sum();
        totalPedidos = pedidosFiltrados.size();

        Map<String, Integer> prodMap = new LinkedHashMap<>();
        for (Pedido p : pedidosFiltrados) {
            prodMap.merge(p.getProducto(), p.getCantidad(), Integer::sum);
        }
        topProductos = prodMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());

        Map<Long, Long> cliMap = new LinkedHashMap<>();
        for (Pedido p : pedidosFiltrados) {
            cliMap.merge(p.getIdCliente(), 1L, Long::sum);
        }
        topClientes = cliMap.entrySet().stream()
                .sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
                .limit(5)
                .collect(Collectors.toList());

        // Preparar datos para la vista
        model.addAttribute("totalVentas", totalVentas);
        model.addAttribute("totalPedidos", totalPedidos);
        model.addAttribute("topProductos", topProductos);
        model.addAttribute("topClientes", topClientes);
        model.addAttribute("rangoTexto", rangoTexto);

        // Datos para selects de año/mes
        List<Integer> anios = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            anios.add(hoy.getYear() - i);
        }
        model.addAttribute("anios", anios);

        return "reportes";
    }
}