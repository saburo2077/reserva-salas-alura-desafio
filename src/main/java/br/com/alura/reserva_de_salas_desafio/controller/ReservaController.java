package br.com.alura.reserva_de_salas_desafio.controller;

import br.com.alura.reserva_de_salas_desafio.dto.ReservaDto;
import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarReservaDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarReservaDto;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.service.ReservaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reservas")
public class ReservaController {

    @Autowired
    private ReservaService service;

    @GetMapping
    public ResponseEntity<List<ReservaDto>> listar() {
        List<ReservaDto> reservas = service.listar();
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/{id}")
    public ResponseEntity buscarPorId(ReservaDto dto) {
        try {
            var reserva = service.buscarPorId(dto);
            return ResponseEntity.ok(reserva);
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody @Valid CadastrarReservaDto dto) {
        try {
            service.cadastrar(dto);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<String> atualizar(@RequestBody @Valid AtualizarReservaDto dto) {
        try {
            service.atualizar(dto);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagarPeloId(@PathVariable("id") Long id) {
        service.apagarReserva(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/status/{id}")
    public ResponseEntity cancelar(@PathVariable Long id) {
        try {
            service.cancelarReserva(id);
            return ResponseEntity.noContent().build();
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/conflitos")
    public ResponseEntity<Page<ReservaDto>> listarConflitos(@RequestParam Long salaId, @RequestParam LocalDate dataInicio, @RequestParam LocalDate dataFim, @PageableDefault(size = 10, sort = {"dataInicio"}) Pageable pageable) {
        var pagina = service.listarConflitos(salaId, dataInicio, dataFim, pageable);
        return ResponseEntity.ok(pagina);
    }

}
