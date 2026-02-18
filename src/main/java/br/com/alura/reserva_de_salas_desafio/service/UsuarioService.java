package br.com.alura.reserva_de_salas_desafio.service;

import br.com.alura.reserva_de_salas_desafio.dto.UsuarioDto;
import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarUsuarioDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarUsuarioDto;
import br.com.alura.reserva_de_salas_desafio.entities.UsuarioEntity;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    public List<UsuarioDto> listar() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public UsuarioEntity buscarPorId(UsuarioDto dto) {
        return usuarioRepository.findById(dto.id()).orElseThrow(() -> new ValidacaoException("id de usuario não encrontrado."));
    }

    @Transactional
    public void cadastrar(CadastrarUsuarioDto dto) {
        boolean jaCadastrado = usuarioRepository.existsByNomeOrEmailOrTelefone(dto.nome(), dto.email(), dto.telefone());

        if (jaCadastrado) {
            throw new ValidacaoException("Usuario já cadastrado");
        }

        usuarioRepository.save(new UsuarioEntity(dto));
    }

    @Transactional
    public void atualizar(AtualizarUsuarioDto dto) {
        UsuarioEntity usuario = usuarioRepository.findById(dto.id()).orElseThrow(() -> new ValidacaoException("Reserva não encontrada"));
        usuario.atualizar(dto);
    }

    @Transactional
    public void apagarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }
}
