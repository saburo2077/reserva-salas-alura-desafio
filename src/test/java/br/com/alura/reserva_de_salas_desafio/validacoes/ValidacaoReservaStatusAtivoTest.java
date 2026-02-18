package br.com.alura.reserva_de_salas_desafio.validacoes;

import br.com.alura.reserva_de_salas_desafio.dto.ReservaDto;
import br.com.alura.reserva_de_salas_desafio.entities.ReservaEntity;
import br.com.alura.reserva_de_salas_desafio.enums.StatusReserva;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.repository.ReservaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidacaoReservaStatusAtivoTest {

    @InjectMocks
    private ValidacaoReservaStatusAtivo validacao;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ReservaDto dto;

    @Mock
    private ReservaEntity reserva;

    @Test
    void deveLancarExecaoQuandoReservajaEstiverCancelada() {
        BDDMockito.given(dto.id()).willReturn(1L);
        BDDMockito.given(reservaRepository.findById(1L)).willReturn(Optional.of(reserva));
        BDDMockito.given(reserva.getStatusReserva()).willReturn(StatusReserva.CANCELADA);

        Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }

    @Test
    void deveNaoLancarExecaoQuandoReservajaEstiverCancelada() {
        BDDMockito.given(dto.id()).willReturn(1L);
        BDDMockito.given(reservaRepository.findById(1L)).willReturn(Optional.of(reserva));
        BDDMockito.given(reserva.getStatusReserva()).willReturn(StatusReserva.ATIVA);

        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

    @Test
    void deveLancarExecaoQuandoReservaNaoForEncontradaNoBanco() {
        BDDMockito.given(dto.id()).willReturn(1L);
        BDDMockito.given(reservaRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }
}