package br.com.alura.reserva_de_salas_desafio.repository;

import br.com.alura.reserva_de_salas_desafio.entities.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    boolean existsByNomeOrEmailOrTelefone(String nome, String email, String telefone);
}
