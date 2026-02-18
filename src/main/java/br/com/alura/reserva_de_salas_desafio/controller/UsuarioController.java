package br.com.alura.reserva_de_salas_desafio.controller;

import br.com.alura.reserva_de_salas_desafio.dto.UsuarioDto;
import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarUsuarioDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarUsuarioDto;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listar() {
        List<UsuarioDto> usuarios = service.listar();
        return ResponseEntity.ok(usuarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity buscarPorId(UsuarioDto dto) {
        try {
            var usuario = service.buscarPorId(dto);
            return ResponseEntity.ok(usuario);
        } catch (ValidacaoException exception) {
            return ResponseEntity.badRequest().body(exception.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<String> cadastrar(@RequestBody @Valid CadastrarUsuarioDto dto) {
        try {
            service.cadastrar(dto);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<String> atualizar(@RequestBody @Valid AtualizarUsuarioDto dto) {
        try {
            service.atualizar(dto);
            return ResponseEntity.ok().build();
        } catch (ValidacaoException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> apagarPeloId(@PathVariable("id") Long id) {
        service.apagarUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
