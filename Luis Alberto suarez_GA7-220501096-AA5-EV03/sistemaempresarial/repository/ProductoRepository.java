package com.empresa.sistema.sistemaempresarial.repository;

import com.empresa.sistema.sistemaempresarial.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
