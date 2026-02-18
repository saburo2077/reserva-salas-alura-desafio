package br.com.alura.reserva_de_salas_desafio.validacoes;

import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarReservaDto;

public interface ValidacaoCadastrarReserva {

    void validar(CadastrarReservaDto dto);
}
