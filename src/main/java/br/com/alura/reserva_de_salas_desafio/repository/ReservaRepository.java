package br.com.alura.reserva_de_salas_desafio.repository;

import br.com.alura.reserva_de_salas_desafio.entities.ReservaEntity;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ReservaRepository extends JpaRepository<ReservaEntity, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT COUNT(r) > 0 FROM Reserva r " +
            "WHERE r.sala.id = :salaId " +
            "AND r.inicio < :fim " +
            "AND r.fim > :inicio")
    boolean existeSobreposicao(@Param("salaId") Long salaId,
                               @Param("inicio") LocalDate inicio,
                               @Param("fim") LocalDate fim);

//used the help of ai for this one,and the one above
    @Query("""
           SELECT r FROM Reserva r 
           WHERE r.sala.id = :salaId 
           AND r.statusReserva = 'ATIVO'
           AND r.dataInicio < :dataFim 
           AND r.dataFim > :dataInicio
           """)
    Page<ReservaEntity> buscarConflitosPaginados(@Param("salaId") Long salaId,
                                                 @Param("dataInicio") LocalDate dataInicio,
                                                 @Param("dataFim") LocalDate dataFim,
                                                 Pageable pageable);
}
