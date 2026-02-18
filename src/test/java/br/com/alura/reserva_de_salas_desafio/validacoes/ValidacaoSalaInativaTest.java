package br.com.alura.reserva_de_salas_desafio.validacoes;

import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarReservaDto;
import br.com.alura.reserva_de_salas_desafio.entities.SalaEntity;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.repository.SalaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidacaoSalaInativaTest {

    @InjectMocks
    private ValidacaoSalaInativa validacao;

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private CadastrarReservaDto dto;

    @Mock
    private SalaEntity sala;

    @Test
    void deveLancarExcecaoQuandoSalaInativa() {
        BDDMockito.given(dto.sala()).willReturn(sala);
        BDDMockito.given(sala.getId()).willReturn(1L);

        BDDMockito.given(salaRepository.existsByIdAndAtivoTrue(1L)).willReturn(false);

        Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }

    @Test
    void deveNaoLancarExcecaoQuandoSalaInativa() {
        BDDMockito.given(dto.sala()).willReturn(sala);
        BDDMockito.given(sala.getId()).willReturn(1L);

        BDDMockito.given(salaRepository.existsByIdAndAtivoTrue(1L)).willReturn(true);

        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }
}