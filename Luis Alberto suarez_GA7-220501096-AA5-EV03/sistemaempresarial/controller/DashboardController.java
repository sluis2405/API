// src/main/java/com/empresa/sistema/sistemaempresarial/controller/DashboardController.java
package com.empresa.sistema.sistemaempresarial.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/dashboard")
    public String mostrarDashboard(HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        return "dashboard";
    }
}