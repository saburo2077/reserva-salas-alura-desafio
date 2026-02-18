package br.com.alura.reserva_de_salas_desafio.dto.cadastrar;

import br.com.alura.reserva_de_salas_desafio.entities.SalaEntity;
import br.com.alura.reserva_de_salas_desafio.entities.UsuarioEntity;
import br.com.alura.reserva_de_salas_desafio.enums.StatusReserva;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CadastrarReservaDto(@NotNull LocalDate inicio, @NotNull LocalDate fim, @NotNull SalaEntity sala,
                                  @NotNull UsuarioEntity usuario, @NotNull StatusReserva status) {
}
