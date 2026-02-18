package br.com.alura.reserva_de_salas_desafio.controller;

import br.com.alura.reserva_de_salas_desafio.dto.SalaDto;
import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarSalaDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarSalaDto;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/salas")
public class SalaController {

    @Autowired
    private SalaService service;

    @GetMapping
    public ResponseEntity<List<SalaDto>> listar() {
        List<SalaDto> salas = service.listar();
        return ResponseEntity.ok(salas);
    }

    @GetMapping("/{id}")
    public ResponseEntity buscarPorId(SalaDto dto) {
        try {
            var reserva = service.buscarPorId(dto);
            return ResponseEntity.ok(reserva);
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody @Valid CadastrarSalaDto dto) {
        try {
            service.cadastrar(dto);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<String> atualizar(@RequestBody @Valid AtualizarSalaDto dto) {
        try {
            service.atualizar(dto);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagarPeloId(@PathVariable("id") Long id) {
        service.apagarSalaId(id);
        return ResponseEntity.noContent().build();
    }
}
