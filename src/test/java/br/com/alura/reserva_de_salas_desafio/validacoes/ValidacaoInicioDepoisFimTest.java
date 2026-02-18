package br.com.alura.reserva_de_salas_desafio.validacoes;

import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarReservaDto;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ValidacaoInicioDepoisFimTest {

    @InjectMocks
    private ValidacaoInicioDepoisFim validacao;

    @Mock
    private CadastrarReservaDto dto;

    @Test
    void deveLançarExcecaoQuandoDataInicioForMaiorQueFim() {
        var inicio = LocalDate.now().plusDays(1);
        var fim = LocalDate.now();

        BDDMockito.given(dto.inicio()).willReturn(inicio);
        BDDMockito.given(dto.fim()).willReturn(fim);

        Assertions.assertThrows(ValidacaoException.class, () -> validacao.validar(dto));
    }

    @Test
    void deveNaoLançarExcecaoQuandoDataInicioForMenorQueFim() {
        var inicio = LocalDate.now();
        var fim = LocalDate.now().plusDays(1);

        BDDMockito.given(dto.inicio()).willReturn(inicio);
        BDDMockito.given(dto.fim()).willReturn(fim);

        Assertions.assertDoesNotThrow(() -> validacao.validar(dto));
    }
}