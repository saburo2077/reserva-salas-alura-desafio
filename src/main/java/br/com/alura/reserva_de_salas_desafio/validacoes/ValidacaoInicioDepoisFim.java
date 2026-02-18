package br.com.alura.reserva_de_salas_desafio.validacoes;

import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarReservaDto;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoInicioDepoisFim implements ValidacaoCadastrarReserva {

    public void validar(CadastrarReservaDto dto) {
        if (dto.inicio().isAfter(dto.fim())) {
            throw new ValidacaoException("A data de início não pode ser posterior à data de fim.");
        }
    }
}
