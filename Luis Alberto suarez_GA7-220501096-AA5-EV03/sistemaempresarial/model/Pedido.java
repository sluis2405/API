package com.empresa.sistema.sistemaempresarial.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_pedido")
    private Long id;

    @Column(name = "fecha_pedido")
    private LocalDateTime fechaPedido;

    @Column(name = "estado_pedido")
    private String estadoPedido;

    // 👇 Esto mapea el campo Java 'idCliente' a la columna 'ID_cliente' en MySQL
    @Column(name = "ID_cliente")
    private Long idCliente;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "producto")
    private String producto;

    @Column(name = "total")
    private Double total;

    @Column(name = "id_producto", nullable = false) // ← obligatorio en BD
    private Long idProducto;
}