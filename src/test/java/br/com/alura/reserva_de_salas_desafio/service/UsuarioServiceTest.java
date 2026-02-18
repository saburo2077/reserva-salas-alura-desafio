package br.com.alura.reserva_de_salas_desafio.service;

import br.com.alura.reserva_de_salas_desafio.dto.UsuarioDto;
import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarUsuarioDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarUsuarioDto;
import br.com.alura.reserva_de_salas_desafio.entities.UsuarioEntity;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.repository.UsuarioRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @InjectMocks
    private UsuarioService service;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioEntity usuario;

    @Mock
    private UsuarioDto usuarioDto;

    @Mock
    private CadastrarUsuarioDto cadastrarDto;

    @Mock
    private AtualizarUsuarioDto atualizarDto;

    @Test
    void deveriaListarTodasOsUsuarios() {
        service.listar();

        BDDMockito.then(usuarioRepository).should().findAll();
    }

    @Test
    void deveriaBuscarPeloIdDoUsuario() {
        BDDMockito.given(usuarioDto.id()).willReturn(1L);
        BDDMockito.given(usuarioRepository.findById(1L)).willReturn(Optional.of(usuario));

        var resultado = service.buscarPorId(usuarioDto);

        Assertions.assertEquals(usuario, resultado);
    }

    @Test
    void deveriaLancarExecaoAoBuscarPeloIdNaoEncontrado() {
        BDDMockito.given(usuarioDto.id()).willReturn(99L);
        BDDMockito.given(usuarioRepository.findById(99L)).willReturn(Optional.empty());


        Assertions.assertThrows(ValidacaoException.class, () -> service.buscarPorId(usuarioDto));
    }

    @Test
    void deveriaCadastrarUsuario() {
        BDDMockito.given(cadastrarDto.nome()).willReturn("test name");
        BDDMockito.given(cadastrarDto.email()).willReturn("teste@email.com");
        BDDMockito.given(cadastrarDto.telefone()).willReturn("123456789");

        BDDMockito.given(usuarioRepository.existsByNomeOrEmailOrTelefone(anyString(), anyString(), anyString())).willReturn(false);

        service.cadastrar(cadastrarDto);

        BDDMockito.then(usuarioRepository).should().save(any(UsuarioEntity.class));
    }

    @Test
    void deveriaLancarExecaoAoTentarCadastrarUsuario() {
        BDDMockito.given(cadastrarDto.nome()).willReturn("test name");
        BDDMockito.given(cadastrarDto.email()).willReturn("teste@email.com");
        BDDMockito.given(cadastrarDto.telefone()).willReturn("123456789");

        BDDMockito.given(usuarioRepository.existsByNomeOrEmailOrTelefone(anyString(), anyString(), anyString())).willReturn(true);

        Assertions.assertThrows(ValidacaoException.class, () -> service.cadastrar(cadastrarDto));

        BDDMockito.then(usuarioRepository).should(BDDMockito.never()).save(any());
    }

    @Test
    void deveriaAtualizarUsuario() {
        BDDMockito.given(atualizarDto.id()).willReturn(1L);
        BDDMockito.given(usuarioRepository.findById(1L)).willReturn(Optional.of(usuario));

        service.atualizar(atualizarDto);

        BDDMockito.then(usuario).should().atualizar(atualizarDto);
    }

    @Test
    void deveriaLancarExecaoAoAtualizar() {
        BDDMockito.given(atualizarDto.id()).willReturn(99L);
        BDDMockito.given(usuarioRepository.findById(99L)).willReturn(Optional.empty());

        Assertions.assertThrows(ValidacaoException.class, () -> service.atualizar(atualizarDto));

        BDDMockito.then(usuario).shouldHaveNoInteractions();
    }

    @Test
    void deveriaChamarORepositorioParaPagarSala() {
        Long id = 1L;

        service.apagarUsuario(id);

        BDDMockito.then(usuarioRepository).should().deleteById(id);
    }
}