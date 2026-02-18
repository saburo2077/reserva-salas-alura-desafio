package br.com.alura.reserva_de_salas_desafio.validacoes;

import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarReservaDto;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoSalaInativa implements ValidacaoCadastrarReserva {

    @Autowired
    private SalaRepository salaRepository;

    public void validar(CadastrarReservaDto dto) {
        boolean estaAtiva = salaRepository.existsByIdAndAtivoTrue(dto.sala().getId());

        if (!estaAtiva) {
            throw new ValidacaoException("Sala esta inativa");
        }
    }
}
