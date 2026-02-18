package br.com.alura.reserva_de_salas_desafio.controller;

import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarReservaDto;
import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarSalaDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarReservaDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarSalaDto;
import br.com.alura.reserva_de_salas_desafio.entities.ReservaEntity;
import br.com.alura.reserva_de_salas_desafio.entities.SalaEntity;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.service.SalaService;
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

@WebMvcTest(SalaController.class)
@AutoConfigureJsonTesters
class SalaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CadastrarSalaDto> jsonCadastrarDto;

    @Autowired
    private JacksonTester<AtualizarSalaDto> jsonAtualizarDto;

    @MockitoBean
    private SalaService service;

    @Test
    void deveriaDevolverCodigo200AoListarSalas() throws Exception {
        BDDMockito.given(service.listar()).willReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/salas")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deveriaDevolverCodigo200AoBuscarPorId() throws Exception {
        var id = 1L;

        BDDMockito.given(service.buscarPorId(ArgumentMatchers.any())).willReturn(new SalaEntity());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/salas/" + id)).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deveriaDevolverCodigo400AoBuscarPorId() throws Exception {
        var id = 99L;
        var mensagemErro = "Reserva com ID 99 não encontrada.";

        BDDMockito.given(service.buscarPorId(ArgumentMatchers.any())).willThrow(new ValidacaoException(mensagemErro));

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/salas/" + id)).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deveriaDevolverCodigo200AoTentarCadastrarSala() throws Exception {
        var dto = new CadastrarSalaDto(2, true);

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/salas").contentType(MediaType.APPLICATION_JSON).content(jsonCadastrarDto.write(dto).getJson()))
                .andExpect(MockMvcResultMatchers.status().isOk());

        BDDMockito.then(service).should().cadastrar(dto);
    }

    @Test
    void deveriaDevolverCodigo400AoTentarCadastrarSala() throws Exception {
        var dto = new CadastrarSalaDto(2, true);
        var mensagemErr = "Sala com este nome já cadastrada";

        BDDMockito.willThrow(new ValidacaoException(mensagemErr)).given(service).cadastrar(ArgumentMatchers.any());

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/salas").contentType(MediaType.APPLICATION_JSON).content(jsonCadastrarDto.write(dto).getJson()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deveriaDevolverCodigo200AoTentarAtualizarSala() throws Exception {
        var dto = new AtualizarSalaDto(1L, 2, true);

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/salas").contentType(MediaType.APPLICATION_JSON).content(jsonAtualizarDto.write(dto).getJson()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deveriaDevolverCodigo400AoTentarAtualizarSala() throws Exception {
        var dto = new AtualizarSalaDto(1L, 2, true);
        var mensagemErr = "Reserva não encontrada";

        BDDMockito.willThrow(new ValidacaoException(mensagemErr)).given(service).atualizar(ArgumentMatchers.any());

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/salas").contentType(MediaType.APPLICATION_JSON).content(jsonAtualizarDto.write(dto).getJson()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deveriaDevolverCodigo204AoTentarApagarSala() throws Exception {
        var id = 1L;

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/salas/" + id)).andExpect(MockMvcResultMatchers.status().isNoContent());

        BDDMockito.then(service).should().apagarSalaId(id);
    }
}