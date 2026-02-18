package br.com.alura.reserva_de_salas_desafio.validacoes;

import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarReservaDto;

public interface ValidacaoAtualizarReserva {

    void validar(AtualizarReservaDto dto);
}
