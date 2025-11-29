package com.empresa.sistema.sistemaempresarial.controller;

import com.empresa.sistema.sistemaempresarial.model.Pedido;
import com.empresa.sistema.sistemaempresarial.model.Producto;
import com.empresa.sistema.sistemaempresarial.repository.PedidoRepository;
import com.empresa.sistema.sistemaempresarial.repository.ProductoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Controller
public class NotificacionController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @GetMapping("/notificaciones/enviar")
    public String mostrarFormularioNotificacion(HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        // Cargar todos los pedidos (guías) para el select
        List<Pedido> pedidos = pedidoRepository.findAll();
        model.addAttribute("pedidos", pedidos);
        return "notificacion-enviar";
    }

    @PostMapping("/notificaciones/enviar")
    public String enviarNotificacion(
            @RequestParam Long idPedido,
            @RequestParam String estado,
            @RequestParam String metodo,
            HttpSession session,
            Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }

        // Simular envío de notificación (sin API real)
        String mensaje = "✅ Notificación enviada al cliente del pedido #" + idPedido +
                " por " + metodo + ". Estado: " + estado;

        model.addAttribute("mensaje", mensaje);
        model.addAttribute("pedidos", pedidoRepository.findAll());
        return "notificacion-enviar";
    }
}