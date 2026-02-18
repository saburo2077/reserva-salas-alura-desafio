package br.com.alura.reserva_de_salas_desafio.controller;

import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarSalaDto;
import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarUsuarioDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarSalaDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarUsuarioDto;
import br.com.alura.reserva_de_salas_desafio.entities.SalaEntity;
import br.com.alura.reserva_de_salas_desafio.entities.UsuarioEntity;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(UsuarioController.class)
@AutoConfigureJsonTesters
class UsuarioControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CadastrarUsuarioDto> jsonCadastrarDto;

    @Autowired
    private JacksonTester<AtualizarUsuarioDto> jsonAtualizarDto;

    @MockitoBean
    private UsuarioService service;

    @Test
    void deveriaDevolverCodigo200AoListarUsuarios() throws Exception {
        BDDMockito.given(service.listar()).willReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/usuarios")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deveriaDevolverCodigo200AoBuscarPorId() throws Exception {
        var id = 1L;

        BDDMockito.given(service.buscarPorId(ArgumentMatchers.any())).willReturn(new UsuarioEntity());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/usuarios/" + id)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deveriaDevolverCodigo400AoBuscarPorId() throws Exception {
        var id = 99L;
        var mensagemErro = "Reserva com ID 99 não encontrada.";

        BDDMockito.given(service.buscarPorId(ArgumentMatchers.any())).willThrow(new ValidacaoException(mensagemErro));

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/usuarios/" + id)).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deveriaDevolverCodigo200AoTentarCadastrarUsuario() throws Exception {
        var dto = new CadastrarUsuarioDto("nomeTest", "test@email.com", "123456789");

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/usuarios").contentType(MediaType.APPLICATION_JSON).content(jsonCadastrarDto.write(dto).getJson()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        BDDMockito.then(service).should().cadastrar(dto);
    }

    @Test
    void deveriaDevolverCodigo400AoTentarCadastrarUsuario() throws Exception {
        var dto = new CadastrarUsuarioDto("nomeTest", "test@email.com", "123456789");
        var mensagemErr = "Usuario já cadastrado";

        BDDMockito.willThrow(new ValidacaoException(mensagemErr)).given(service).cadastrar(ArgumentMatchers.any());

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/usuarios").contentType(MediaType.APPLICATION_JSON).content(jsonCadastrarDto.write(dto).getJson()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deveriaDevolverCodigo200AoTentarAtualizarUsuario() throws Exception {
        var dto = new AtualizarUsuarioDto(1L, "nomeTest", "test@email.com", "123456789");

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/usuarios").contentType(MediaType.APPLICATION_JSON).content(jsonAtualizarDto.write(dto).getJson()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deveriaDevolverCodigo400AoTentarAtualizarUsuario() throws Exception {
        var dto = new AtualizarUsuarioDto(1L, "nomeTest", "test@email.com", "123456789");
        var mensagemErr = "Reserva não encontrada";

        BDDMockito.willThrow(new ValidacaoException(mensagemErr)).given(service).atualizar(dto);

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/usuarios").contentType(MediaType.APPLICATION_JSON).content(jsonAtualizarDto.write(dto).getJson()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deveriaDevolverCodigo204AoTentarApagarUsuario() throws Exception {
        var id = 1L;

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/usuarios/" + id)).andExpect(MockMvcResultMatchers.status().isNoContent());

        BDDMockito.then(service).should().apagarUsuario(id);
    }
}