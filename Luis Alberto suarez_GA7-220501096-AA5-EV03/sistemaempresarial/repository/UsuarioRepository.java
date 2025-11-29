package com.empresa.sistema.sistemaempresarial.repository;

import com.empresa.sistema. sistemaempresarial.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo); // ✅ Devuelve Optional
}