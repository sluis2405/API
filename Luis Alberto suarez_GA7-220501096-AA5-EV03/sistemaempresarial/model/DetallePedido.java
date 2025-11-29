// src/main/java/com/empresa/sistema/sistemaempresarial/model/DetallePedido.java
package com.empresa.sistema.sistemaempresarial.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "detalle_pedido")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetallePedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_detalle;

    @Column(name = "ID_pedido", nullable = false)
    private Long idPedido;

    @Column(name = "ID_producto", nullable = false)
    private Long idProducto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "subtotal", nullable = false)
    private Double subtotal;
}
