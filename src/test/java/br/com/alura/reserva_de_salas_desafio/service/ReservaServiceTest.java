package br.com.alura.reserva_de_salas_desafio.service;

import br.com.alura.reserva_de_salas_desafio.dto.ReservaDto;
import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarReservaDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarReservaDto;
import br.com.alura.reserva_de_salas_desafio.entities.ReservaEntity;
import br.com.alura.reserva_de_salas_desafio.entities.SalaEntity;
import br.com.alura.reserva_de_salas_desafio.entities.UsuarioEntity;
import br.com.alura.reserva_de_salas_desafio.enums.StatusReserva;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.repository.ReservaRepository;
import br.com.alura.reserva_de_salas_desafio.validacoes.ValidacaoAtualizarReserva;
import br.com.alura.reserva_de_salas_desafio.validacoes.ValidacaoCadastrarReserva;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @InjectMocks
    private ReservaService service;

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private ReservaDto reservaDto;

    @Mock
    private SalaEntity sala;

    @Mock
    private ReservaEntity reserva;

    @Mock
    private UsuarioEntity usuario;

    @Mock
    private CadastrarReservaDto cadastrarDto;

    @Mock
    private ValidacaoCadastrarReserva v1;

    @Spy
    private List<ValidacaoCadastrarReserva> validacaoCadastrar = new ArrayList<>();

    @Mock
    private ValidacaoAtualizarReserva v2;

    @Spy
    private List<ValidacaoAtualizarReserva> validacaoAtualizar = new ArrayList<>();

    @Mock
    private AtualizarReservaDto atualizarDto;

    @BeforeEach
    void setUp() {
        validacaoCadastrar.add(v1);

        validacaoAtualizar.add(v2);
    }

    @Test
    void deveriaListarTodasAsReservas() {
        service.listar();

        BDDMockito.then(reservaRepository).should().findAll();
    }

    @Test
    void deveriaBuscarPeloIdDaReserva() {
        BDDMockito.given(reservaDto.id()).willReturn(1L);
        BDDMockito.given(reservaRepository.findById(1L)).willReturn(Optional.of(reserva));

        var resultado = service.buscarPorId(reservaDto);

        Assertions.assertEquals(reserva, resultado);
    }

    @Test
    void deveriaLancarExecaoAoBuscarPeloIdNaoEncontrado() {
        BDDMockito.given(reservaDto.id()).willReturn(99L);
        BDDMockito.given(reservaRepository.findById(99L)).willReturn(Optional.empty());


        Assertions.assertThrows(ValidacaoException.class, () -> service.buscarPorId(reservaDto));
    }

    @Test
    void deveriaCadastrarReserva() {
        service.cadastrar(cadastrarDto);

        BDDMockito.then(reservaRepository).should().save(any(ReservaEntity.class));
    }

    @Test
    void deveriaNaoCadastrarReserva() {
        BDDMockito.willThrow(new ValidacaoException("Erro")).given(v1).validar(cadastrarDto);

        Assertions.assertThrows(ValidacaoException.class, () -> service.cadastrar(cadastrarDto));

        BDDMockito.then(reservaRepository).shouldHaveNoInteractions();
    }

    @Test
    void deveriaAtualizarReserva() {
        BDDMockito.given(atualizarDto.id()).willReturn(1L);
        BDDMockito.given(reservaRepository.findById(1L)).willReturn(Optional.of(reserva));

        service.atualizar(atualizarDto);

        BDDMockito.then(v2).should().validar(atualizarDto);

        BDDMockito.then(reserva).should().atualizar(atualizarDto);
    }

    @Test
    void deveriaLancarExecaoAoAtualizar() {
        BDDMockito.given(atualizarDto.id()).willReturn(1L);
        BDDMockito.given(reservaRepository.findById(1L)).willReturn(Optional.empty());

        Assertions.assertThrows(ValidacaoException.class, () -> service.atualizar(atualizarDto));

        BDDMockito.then(reserva).shouldHaveNoInteractions();
    }

    @Test
    void deveriaChamarORepositorioParaPagarReserva() {
        Long id = 1L;

        service.apagarReserva(id);

        BDDMockito.then(reservaRepository).should().deleteById(id);
    }

    @Test
    void deveMudarStatusParaCancelada() {
        Long id = 1L;
        BDDMockito.given(reservaRepository.findById(id)).willReturn(Optional.of(reserva));
        BDDMockito.given(reserva.getStatusReserva()).willReturn(StatusReserva.ATIVA);

        service.cancelarReserva(id);

        BDDMockito.then(reserva).should().setStatusReserva(StatusReserva.CANCELADA);
    }

    @Test
    void deveLancarExecaoAoTentarCancelarReservaCancelada() {
        Long id = 1L;
        BDDMockito.given(reservaRepository.findById(id)).willReturn(Optional.of(reserva));
        BDDMockito.given(reserva.getStatusReserva()).willReturn(StatusReserva.CANCELADA);

        Assertions.assertThrows(ValidacaoException.class, () -> service.cancelarReserva(id));

        BDDMockito.then(reserva).should(BDDMockito.never()).setStatusReserva(any());
    }

    @Test
    void deveLancarExecaoAoTentarCancelarReservaInexistente() {
        Long id = 99L;
        BDDMockito.given(reservaRepository.findById(id)).willReturn(Optional.empty());

        Assertions.assertThrows(ValidacaoException.class, () -> service.cancelarReserva(id));

    }

    @Test
//    i changed how the method in ReservaService Worked
//
//    void deveriaRetornarUmaPaginaDeDtoAoBuscarConflitos() {
//        var pageable = Pageable.unpaged();
//
//        List<ReservaEntity> listaEntities = List.of(reserva);
//        Page<ReservaEntity> paginaEntities = new PageImpl<>(listaEntities);
//
//        BDDMockito.given(reservaDto.sala()).willReturn(sala);
//        BDDMockito.given(sala.getId()).willReturn(1L);
//        BDDMockito.given(reservaDto.dataInicio()).willReturn(LocalDate.now());
//        BDDMockito.given(reservaDto.dataFim()).willReturn(LocalDate.now().plusDays(1));
//
//        BDDMockito.given(reservaRepository.buscarConflitosPaginados(anyLong(), any(), any(), any(Pageable.class))).willReturn(paginaEntities);
//
//        BDDMockito.given(reserva.getId()).willReturn(1L);
//        BDDMockito.given(reserva.getDataInicio()).willReturn(LocalDate.now());
//        BDDMockito.given(reserva.getDataFim()).willReturn(LocalDate.now());
//        BDDMockito.given(reserva.getSalaEntity()).willReturn(sala);
//        BDDMockito.given(reserva.getUsuarioEntity()).willReturn(usuario);
//        BDDMockito.given(reserva.getStatusReserva()).willReturn(StatusReserva.ATIVA);
//
//        Page<ReservaDto> resultado = service.listarConflitos(reservaDto, pageable);
//
//        Assertions.assertNotNull(resultado);
//        Assertions.assertEquals(1, resultado.getContent().size());
//        Assertions.assertEquals(ReservaDto.class, resultado.getContent().get(0).getClass());
//
//        BDDMockito.then(reservaRepository).should().buscarConflitosPaginados(anyLong(), any(), any(), any());
//    }

    void deveriaRetornarUmaPaginaDeDtoAoBuscarConflitos() {
        var pageable = Pageable.unpaged();
        var dataInicio = LocalDate.now();
        var dataFim = LocalDate.now().plusDays(1);
        var salaId = 1L;

        List<ReservaEntity> listaEntities = List.of(reserva);
        Page<ReservaEntity> paginaEntities = new PageImpl<>(listaEntities);

        BDDMockito.given(reservaRepository.buscarConflitosPaginados(anyLong(), any(), any(), any(Pageable.class)))
                .willReturn(paginaEntities);

        BDDMockito.given(reserva.getId()).willReturn(1L);
        BDDMockito.given(reserva.getDataInicio()).willReturn(dataInicio);
        BDDMockito.given(reserva.getDataFim()).willReturn(dataFim);
        BDDMockito.given(reserva.getSalaEntity()).willReturn(sala);
        BDDMockito.given(reserva.getUsuarioEntity()).willReturn(usuario);
        BDDMockito.given(reserva.getStatusReserva()).willReturn(StatusReserva.ATIVA);

        Page<ReservaDto> resultado = service.listarConflitos(salaId, dataInicio, dataFim, pageable);

        Assertions.assertNotNull(resultado);
        Assertions.assertEquals(1, resultado.getContent().size());
        Assertions.assertEquals(ReservaDto.class, resultado.getContent().get(0).getClass());

        BDDMockito.then(reservaRepository).should().buscarConflitosPaginados(salaId, dataInicio, dataFim, pageable);
    }
}