package br.com.alura.reserva_de_salas_desafio.service;

import br.com.alura.reserva_de_salas_desafio.dto.SalaDto;
import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarSalaDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarSalaDto;
import br.com.alura.reserva_de_salas_desafio.entities.ReservaEntity;
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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class SalaServiceTest {

    @InjectMocks
    private SalaService service;

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private SalaEntity sala;

    @Mock
    private SalaDto salaDto;

    @Mock
    private CadastrarSalaDto cadastrarDto;

    @Mock
    private AtualizarSalaDto atualizarDto;

    @Test
    void deveriaListarTodasAsSalas() {
        service.listar();

        BDDMockito.then(salaRepository).should().findAll();
    }

    @Test
    void deveriaBuscarPeloIdDaSala() {
        BDDMockito.given(salaDto.id()).willReturn(1L);
        BDDMockito.given(salaRepository.findById(1L)).willReturn(Optional.of(sala));

        var resultado = service.buscarPorId(salaDto);

        Assertions.assertEquals(sala, resultado);
    }

    @Test
    void deveriaLancarExecaoAoBuscarPeloIdNaoEncontrado() {
        BDDMockito.given(salaDto.id()).willReturn(99L);
        BDDMockito.given(salaRepository.findById(99L)).willReturn(Optional.empty());


        Assertions.assertThrows(ValidacaoException.class, () -> service.buscarPorId(salaDto));
    }

    @Test
    void deveriaCadastrarSala() {
        service.cadastrar(cadastrarDto);

        BDDMockito.then(salaRepository).should().save(any(SalaEntity.class));
    }

    @Test
    void deveriaAtualizarSala() {
        BDDMockito.given(atualizarDto.id()).willReturn(1L);
        BDDMockito.given(salaRepository.findById(1L)).willReturn(Optional.of(sala));

        service.atualizar(atualizarDto);

        BDDMockito.then(sala).should().atualizarDados(atualizarDto);
    }

    @Test
    void deveriaLancarExecaoAoAtualizar() {
        BDDMockito.given(atualizarDto.id()).willReturn(99L);
        BDDMockito.given(salaRepository.findById(99L)).willReturn(Optional.empty());

        Assertions.assertThrows(ValidacaoException.class, () -> service.atualizar(atualizarDto));

        BDDMockito.then(sala).shouldHaveNoInteractions();
    }

    @Test
    void deveriaChamarORepositorioParaPagarReserva() {
        Long id = 1L;

        service.apagarSalaId(id);

        BDDMockito.then(salaRepository).should().deleteById(id);
    }
}