package br.com.alura.reserva_de_salas_desafio.controller;

import br.com.alura.reserva_de_salas_desafio.dto.ReservaDto;
import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarReservaDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarReservaDto;
import br.com.alura.reserva_de_salas_desafio.entities.ReservaEntity;
import br.com.alura.reserva_de_salas_desafio.entities.SalaEntity;
import br.com.alura.reserva_de_salas_desafio.entities.UsuarioEntity;
import br.com.alura.reserva_de_salas_desafio.enums.StatusReserva;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.service.ReservaService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.AutoConfigureJsonTesters;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.List;

@WebMvcTest(ReservaController.class)
@AutoConfigureJsonTesters
class ReservaControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private JacksonTester<CadastrarReservaDto> jsonCadastrarDto;

    @Autowired
    private JacksonTester<AtualizarReservaDto> jsonAtualizarDto;

    @MockitoBean
    private ReservaService service;

    @Test
    void deveriaDevolverCodigo200AoListarReservas() throws Exception {
        BDDMockito.given(service.listar()).willReturn(List.of());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/reservas")).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deveriaDevolverCodigo200AoBuscarPorId() throws Exception {
        var id = 1L;

        BDDMockito.given(service.buscarPorId(ArgumentMatchers.any())).willReturn(new ReservaEntity());

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/reservas/" + id)).andExpect(MockMvcResultMatchers.status().isOk());
    }


    @Test
    void deveriaDevolverCodigo400AoBuscarPorId() throws Exception {
        var id = 99L;
        var mensagemErro = "Reserva com ID 99 não encontrada.";

        BDDMockito.given(service.buscarPorId(ArgumentMatchers.any())).willThrow(new ValidacaoException(mensagemErro));

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/reservas/" + id)).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deveriaDevolverCodigo200AoTentarCadastrarReserva() throws Exception {
        var sala = new SalaEntity();
        sala.setId(1L);
        var usuario = new UsuarioEntity();
        usuario.setId(1L);
        var status = StatusReserva.ATIVA;

        var dto = new CadastrarReservaDto(LocalDate.now(), LocalDate.now().plusDays(1), sala, usuario, status);

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/reservas").contentType(MediaType.APPLICATION_JSON).content(jsonCadastrarDto.write(dto).getJson()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deveriaDevolverCodigo400AoTentarCadastrarReserva() throws Exception {
        var sala = new SalaEntity();
        sala.setId(1L);
        var usuario = new UsuarioEntity();
        usuario.setId(1L);
        var status = StatusReserva.ATIVA;

        var dto = new CadastrarReservaDto(LocalDate.now(), LocalDate.now().plusDays(1), sala, usuario, status);
        var mensagemErr = "Sala já reservada";

        BDDMockito.willThrow(new ValidacaoException(mensagemErr)).given(service).cadastrar(ArgumentMatchers.any());

        mvc.perform(MockMvcRequestBuilders.post("/api/v1/reservas").contentType(MediaType.APPLICATION_JSON).content(jsonCadastrarDto.write(dto).getJson()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deveriaDevolverCodigo200AoTentarAtualizarAReserva() throws Exception {
        var sala = new SalaEntity();
        sala.setId(1L);
        var usuario = new UsuarioEntity();
        usuario.setId(1L);
        var status = StatusReserva.ATIVA;

        var dto = new AtualizarReservaDto(1L, LocalDate.now(), LocalDate.now().plusDays(1), sala, usuario, status);

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/reservas").contentType(MediaType.APPLICATION_JSON).content(jsonAtualizarDto.write(dto).getJson()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void deveriaDevolverCodigo400AoTentarAtualizarAReserva() throws Exception {
        var sala = new SalaEntity();
        sala.setId(1L);
        var usuario = new UsuarioEntity();
        usuario.setId(1L);
        var status = StatusReserva.ATIVA;

        var dto = new AtualizarReservaDto(99L, LocalDate.now(), LocalDate.now().plusDays(1), sala, usuario, status);
        var mensagemErr = "Reserva não encontrada para o ID informado.";

        BDDMockito.willThrow(new ValidacaoException(mensagemErr)).given(service).atualizar(ArgumentMatchers.any());

        mvc.perform(MockMvcRequestBuilders.put("/api/v1/reservas").contentType(MediaType.APPLICATION_JSON).content(jsonAtualizarDto.write(dto).getJson()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deveriaDevolverCodigo204AoTentarApagarReserva() throws Exception {
        var id = 1L;

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/reservas/" + id)).andExpect(MockMvcResultMatchers.status().isNoContent());

        BDDMockito.then(service).should().apagarReserva(id);
    }

    @Test
    void deveriaDevolverCodigo204AoTentarCancelarReserva() throws Exception {
        var id = 1L;

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/reservas/status/" + id)).andExpect(MockMvcResultMatchers.status().isNoContent());

        BDDMockito.then(service).should().cancelarReserva(id);
    }

    @Test
    void deveriaDevolverCodigo400AoTentarCancelarReserva() throws Exception {
        var id = 99L;
        var mensagemErr = "reserva já cancelada";

        BDDMockito.willThrow(new ValidacaoException(mensagemErr)).given(service).cancelarReserva(id);

        mvc.perform(MockMvcRequestBuilders.delete("/api/v1/reservas/status/" + id)).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void deveriaDevolverCodigo200AoBuscarConflitosComPaginacao() throws Exception {
        Page<ReservaDto> page = new PageImpl<>(List.of());

        BDDMockito.given(service.listarConflitos(ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any(), ArgumentMatchers.any())).willReturn(page);

        mvc.perform(MockMvcRequestBuilders.get("/api/v1/reservas/conflitos")
                .param("salaId", "1")
                .param("dataInicio", "2026-02-16")
                .param("dataFim", "2026-02-17"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}