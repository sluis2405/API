// src/main/java/com/empresa/sistema/sistemaempresarial/controller/SeguimientoController.java
package com.empresa.sistema.sistemaempresarial.controller;

import com.empresa.sistema.sistemaempresarial.model.Pedido;
import com.empresa.sistema.sistemaempresarial.repository.PedidoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import com.empresa.sistema.sistemaempresarial.model.Pedido;
import com.empresa.sistema.sistemaempresarial.repository.PedidoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SeguimientoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping("/seguimiento/consulta")
    public String mostrarConsulta(HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        List<Pedido> guias = pedidoRepository.findAll();
        model.addAttribute("guias", guias);
        return "seguimiento/consulta";
    }

    @PostMapping("/seguimiento/resultado")
    public String procesarConsulta(
            @RequestParam Long id_guia,
            HttpSession session,
            Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }

        Pedido pedido = pedidoRepository.findById(id_guia)
                .orElse(null);

        if (pedido == null) {
            model.addAttribute("error", "Guía no encontrada.");
            return "seguimiento/consulta";
        }

        String estadoMostrar;
        switch (pedido.getEstadoPedido()) {
            case "completado":
                estadoMostrar = "entregado";
                break;
            case "pendiente":
                estadoMostrar = "pendiente";
                break;
            default:
                estadoMostrar = "en tránsito";
        }

        model.addAttribute("idGuia", id_guia);
        model.addAttribute("estado", estadoMostrar);
        return "seguimiento/resultado";
    }

    @GetMapping("/seguimiento/resultado")
    public String mostrarResultadoGet(
            @RequestParam Long id_guia,
            HttpSession session,
            Model model) {
        return procesarConsulta(id_guia, session, model);
    }
}