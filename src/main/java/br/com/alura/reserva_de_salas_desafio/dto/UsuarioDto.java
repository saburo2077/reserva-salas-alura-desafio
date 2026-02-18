package br.com.alura.reserva_de_salas_desafio.dto;

import br.com.alura.reserva_de_salas_desafio.entities.UsuarioEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UsuarioDto(@NotNull Long id, @NotBlank String nome, String email, String telefone) {

    public UsuarioDto(UsuarioEntity usuario) {
        this(usuario.getId(), usuario.getNome(), usuario.getEmail(), usuario.getTelefone());
    }
}
