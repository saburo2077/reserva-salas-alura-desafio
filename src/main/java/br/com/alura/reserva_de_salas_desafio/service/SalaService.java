package br.com.alura.reserva_de_salas_desafio.service;

import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarSalaDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarSalaDto;
import br.com.alura.reserva_de_salas_desafio.dto.SalaDto;
import br.com.alura.reserva_de_salas_desafio.entities.SalaEntity;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.repository.SalaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SalaService {

    @Autowired
    private SalaRepository salaRepository;

    @Transactional(readOnly = true)
    public List<SalaDto> listar() {
        return salaRepository.findAll()
                .stream()
                .map(SalaDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public SalaEntity buscarPorId(SalaDto dto) {
        return salaRepository.findById(dto.id())
                .orElseThrow(() -> new ValidacaoException("Sala com ID " + dto.id() + " não encontrada."));
    }

    @Transactional
    public void cadastrar(CadastrarSalaDto dto) {
        salaRepository.save(new SalaEntity(dto));
    }

    @Transactional
    public void atualizar(AtualizarSalaDto dto) {
        SalaEntity sala = salaRepository.findById(dto.id()).orElseThrow(() -> new ValidacaoException("Reserva não encontrada"));
        sala.atualizarDados(dto);
    }

    @Transactional
    public void apagarSalaId(Long id) {
        salaRepository.deleteById(id);
    }
}
