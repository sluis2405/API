// src/main/java/com/empresa/sistema/sistemaempresarial/controller/PedidoController.java
package com.empresa.sistema.sistemaempresarial.controller;

import com.empresa.sistema.sistemaempresarial.model.Pedido;
import com.empresa.sistema.sistemaempresarial.repository.PedidoRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import java.time.LocalDateTime;
import com.empresa.sistema.sistemaempresarial.repository.UsuarioRepository;
import com.empresa.sistema.sistemaempresarial.repository.ProductoRepository;
import java.util.List;
import com.empresa.sistema.sistemaempresarial.model.DetallePedido;
import com.empresa.sistema.sistemaempresarial.repository.DetallePedidoRepository;
import com.empresa.sistema.sistemaempresarial.model.Usuario;

@Controller
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private DetallePedidoRepository detallePedidoRepository;



    @GetMapping("/pedidos")
    public String listarPedidos(HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        model.addAttribute("pedidos", pedidoRepository.findAll());
        return "pedidos";
    }


    @GetMapping("/pedidos/nuevo")
    public String mostrarFormularioNuevoPedido(HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null) return "redirect:/";

        model.addAttribute("pedido", new Pedido());
        model.addAttribute("usuarios", usuarioRepository.findAll());
        model.addAttribute("productos", productoRepository.findAll());
        return "pedido-form";
    }

    @PostMapping("/pedidos/guardar")
    public String guardarPedido(@ModelAttribute Pedido pedido) {
        // Asignar fecha actual si no viene
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(java.time.LocalDateTime.now());
        }
        // Asegurar estado por defecto
        if (pedido.getEstadoPedido() == null || pedido.getEstadoPedido().isBlank()) {
            pedido.setEstadoPedido("pendiente");
        }
        pedidoRepository.save(pedido);
        return "redirect:/pedidos";
    }

    // Mostrar formulario de edición
    @GetMapping("/pedidos/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + id));
        model.addAttribute("pedido", pedido);
        return "pedido-form"; // reutilizamos el mismo formulario
    }

    // Guardar edición
    @PostMapping("/pedidos/actualizar")
    public String actualizarPedido(@ModelAttribute Pedido pedido) {
        // Asegurar que la fecha no sea nula (en caso de que el formulario no la envíe)
        if (pedido.getFechaPedido() == null) {
            pedido.setFechaPedido(LocalDateTime.now());
        }
        pedidoRepository.save(pedido);
        return "redirect:/pedidos";
    }

    // Ver detalle de un pedido
    @GetMapping("/pedidos/detalle/{id}")
    public String verDetallePedido(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado con ID: " + id));
        model.addAttribute("pedido", pedido);
        return "pedido-detalle";
    }

    @PostMapping("/pedidos/eliminar/{id}")
    public String eliminarPedido(@PathVariable Long id) {
        pedidoRepository.deleteById(id);
        return "redirect:/pedidos";
    }
    @GetMapping("/pedidos/factura/{id}")
    public String generarFactura(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));



        Usuario cliente = usuarioRepository.findById(pedido.getIdCliente())
                .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado"));



        // Cargar los detalles del pedido
        List<DetallePedido> detalles = detallePedidoRepository.findByIdPedido(id);

        model.addAttribute("pedido", pedido);
        model.addAttribute("nombreCliente", cliente.getNombre());
        model.addAttribute("detalles", detalles);
        return "factura";
    }

    @GetMapping("/pedidos/guia/{id}")
    public String generarGuia(@PathVariable Long id, HttpSession session, Model model) {
        if (session.getAttribute("usuario") == null) {
            return "redirect:/";
        }

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pedido no encontrado"));

        model.addAttribute("pedido", pedido);
        return "guia";
    }




    }



