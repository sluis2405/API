// src/main/java/com/empresa/sistema/sistemaempresarial/repository/DetallePedidoRepository.java
package com.empresa.sistema.sistemaempresarial.repository;

import com.empresa.sistema.sistemaempresarial.model.DetallePedido;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {
    List<DetallePedido> findByIdPedido(Long idPedido);
}
