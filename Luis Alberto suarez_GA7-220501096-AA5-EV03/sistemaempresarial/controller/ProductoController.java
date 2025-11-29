// src/main/java/com/empresa/sistema/sistemaempresarial/controller/ProductoController.java
package com.empresa.sistema.sistemaempresarial.controller;

import com.empresa.sistema.sistemaempresarial.model.Producto;
import com.empresa.sistema.sistemaempresarial.repository.ProductoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductoController {

    @Autowired
    private ProductoRepository productoRepository;

    // Listar productos
    @GetMapping("/productos")
    public String listarProductos(HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        model.addAttribute("productos", productoRepository.findAll());
        return "productos";
    }

    // Mostrar formulario para nuevo producto
    @GetMapping("/productos/nuevo")
    public String mostrarFormNuevoProducto(HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        model.addAttribute("producto", new Producto());
        return "producto-form";
    }

    // Guardar nuevo o actualizar producto
    @PostMapping("/productos/guardar")
    public String guardarProducto(@ModelAttribute Producto producto) {
        productoRepository.save(producto);
        return "redirect:/productos";
    }

    // Eliminar producto
    @PostMapping("/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id) {
        productoRepository.deleteById(id);
        return "redirect:/productos";
    }

    // Mostrar formulario de edición (cargar datos existentes)
    @GetMapping("/productos/editar/{id}")
    public String mostrarFormEditarProducto(
            @PathVariable Long id,
            HttpSession session,
            Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + id));
        model.addAttribute("producto", producto);
        return "producto-form";
    }


    // En ProductoController.java

    @GetMapping("/productos/inventario")
    public String mostrarInventario(HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        model.addAttribute("producto", new Producto());
        return "inventario"; // ← Esta vista será la misma que antes, pero sin guardar movimientos
    }

    @PostMapping("/productos/inventario/entrada")
    public String registrarEntrada(
            @RequestParam Long idProducto,
            @RequestParam Integer cantidad,
            HttpSession session) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        producto.setStock(producto.getStock() + cantidad);
        productoRepository.save(producto);

        // Redirigir a la lista de productos con mensaje de éxito
        return "redirect:/productos?mensaje=Entrada registrada con éxito";
    }

    @PostMapping("/productos/inventario/salida")
    public String registrarSalida(
            @RequestParam Long idProducto,
            @RequestParam Integer cantidad,
            HttpSession session,
            Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (producto.getStock() < cantidad) {
            // Si hay error, volvemos al formulario con mensaje
            return "redirect:/productos/inventario?error=Stock insuficiente";
        }

        producto.setStock(producto.getStock() - cantidad);
        productoRepository.save(producto);

        // Redirigir a productos con mensaje
        return "redirect:/productos?mensaje=Salida registrada con éxito";
    }


}