package br.com.alura.reserva_de_salas_desafio.dto;

import br.com.alura.reserva_de_salas_desafio.entities.SalaEntity;

public record SalaDto(Long id, Integer capacidade, Boolean ativo) {

    public SalaDto(SalaEntity sala) {
        this(sala.getId(), sala.getCapacidade(), sala.isAtivo());
    }
}
