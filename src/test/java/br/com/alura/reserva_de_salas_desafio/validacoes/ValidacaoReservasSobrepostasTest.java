package br.com.alura.reserva_de_salas_desafio.validacoes;

import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarReservaDto;
import br.com.alura.reserva_de_salas_desafio.entities.SalaEntity;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.repository.ReservaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ValidacaoReservasSobrepostasTest {

    @InjectMocks
    private ValidacaoReservasSobrepostas validacao;

    @Mock
    private CadastrarReservaDto dto;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private SalaEntity sala;

    @Test
    void deveLancarExecaoQuandoTiverConflito() {
        BDDMockito.given(dto.sala()).willReturn(sala);
        BDDMockito.given(sala.getId()).willReturn(1L);
        BDDMockito.given(dto.inicio()).willReturn(LocalDate.now());
        BDDMockito.given(dto.fim()).willReturn(LocalDate.now().plusDays(1));

        BDDMockito.given(reservaRepository.existeSobreposicao(anyLong(), any(), any())).willReturn(true);

        Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }

    @Test
    void deveNaoLancarExecaoQuandoNaoTiverConflito() {
        BDDMockito.given(dto.sala()).willReturn(sala);
        BDDMockito.given(sala.getId()).willReturn(1L);

        BDDMockito.given(reservaRepository.existeSobreposicao(anyLong(), any(), any())).willReturn(false);

        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }

}