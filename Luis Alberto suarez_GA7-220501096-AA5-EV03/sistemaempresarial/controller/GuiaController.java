// src/main/java/com/empresa/sistema/sistemaempresarial/controller/GuiaController.java
package com.empresa.sistema.sistemaempresarial.controller;

import com.empresa.sistema.sistemaempresarial.model.Pedido;
import com.empresa.sistema.sistemaempresarial.repository.PedidoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class GuiaController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @GetMapping("/guias")
    public String listarGuias(HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        List<Pedido> guias = pedidoRepository.findAll();
        model.addAttribute("guias", guias); // ← lista de guías
        return "guias"; // ← devuelve "guias.html"
    }
}