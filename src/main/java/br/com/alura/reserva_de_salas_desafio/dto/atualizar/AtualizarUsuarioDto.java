package br.com.alura.reserva_de_salas_desafio.dto.atualizar;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizarUsuarioDto(@NotNull Long id, @NotBlank String nome, @NotBlank String email, @NotBlank String telefone) {
}
