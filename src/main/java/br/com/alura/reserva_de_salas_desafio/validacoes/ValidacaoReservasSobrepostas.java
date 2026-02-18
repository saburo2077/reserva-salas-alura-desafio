package br.com.alura.reserva_de_salas_desafio.validacoes;

import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarReservaDto;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoReservasSobrepostas implements ValidacaoCadastrarReserva {

    @Autowired
    private ReservaRepository reservaRepository;

    public void validar(CadastrarReservaDto dto) {
        boolean conflito = reservaRepository.existeSobreposicao(dto.sala().getId(), dto.inicio(), dto.fim());

        if (conflito) {
            throw new ValidacaoException("A sala já está reservada para este período.");
        }
    }
}
