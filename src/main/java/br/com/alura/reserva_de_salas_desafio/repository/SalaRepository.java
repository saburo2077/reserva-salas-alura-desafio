package br.com.alura.reserva_de_salas_desafio.repository;

import br.com.alura.reserva_de_salas_desafio.entities.SalaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SalaRepository extends JpaRepository<SalaEntity, Long> {

    boolean existsByIdAndAtivoTrue(Long id);
}
