package br.com.alura.reserva_de_salas_desafio.validacoes;

import br.com.alura.reserva_de_salas_desafio.dto.ReservaDto;
import br.com.alura.reserva_de_salas_desafio.enums.StatusReserva;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ValidacaoReservaStatusAtivo {

    @Autowired
    private ReservaRepository reservaRepository;

    public void validar(ReservaDto dto) {
        if (dto.id() != null) {
            var reserva = reservaRepository.findById(dto.id()).orElseThrow(() -> new ValidacaoException("Reserva não encontrada."));

            if (reserva.getStatusReserva() == StatusReserva.CANCELADA) {
                throw new ValidacaoException("Não é possível realizar operações em uma reserva cancelada.");
            }
        }
    }
}
