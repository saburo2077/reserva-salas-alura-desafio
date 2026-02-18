package br.com.alura.reserva_de_salas_desafio.dto.cadastrar;

import jakarta.validation.constraints.NotBlank;

public record CadastrarUsuarioDto(@NotBlank String nome, @NotBlank String email, @NotBlank String telefone) {
}
