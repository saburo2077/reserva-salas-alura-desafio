package br.com.alura.reserva_de_salas_desafio.dto;

import br.com.alura.reserva_de_salas_desafio.entities.ReservaEntity;
import br.com.alura.reserva_de_salas_desafio.entities.SalaEntity;
import br.com.alura.reserva_de_salas_desafio.entities.UsuarioEntity;
import br.com.alura.reserva_de_salas_desafio.enums.StatusReserva;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record ReservaDto(@NotNull Long id, @NotNull LocalDate dataInicio, @NotNull LocalDate dataFim, @NotNull SalaEntity sala,
                         @NotNull UsuarioEntity usuario, @NotNull StatusReserva statusReserva) {

    public ReservaDto(ReservaEntity reserva) {
        this(reserva.getId(), reserva.getDataInicio(), reserva.getDataFim(), reserva.getSalaEntity(), reserva.getUsuarioEntity(), reserva.getStatusReserva());
    }
}
