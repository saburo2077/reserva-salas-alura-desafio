package br.com.alura.reserva_de_salas_desafio.dto.atualizar;

import jakarta.validation.constraints.NotNull;

public record AtualizarSalaDto(@NotNull Long id, @NotNull Integer capacidade, Boolean ativo) {
}
