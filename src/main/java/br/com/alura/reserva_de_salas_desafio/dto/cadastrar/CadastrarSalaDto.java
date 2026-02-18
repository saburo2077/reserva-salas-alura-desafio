package br.com.alura.reserva_de_salas_desafio.dto.cadastrar;

import jakarta.validation.constraints.NotNull;

public record CadastrarSalaDto(@NotNull Integer capacidade, Boolean ativo) {
}
