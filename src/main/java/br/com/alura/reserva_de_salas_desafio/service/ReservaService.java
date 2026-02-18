package br.com.alura.reserva_de_salas_desafio.service;

import br.com.alura.reserva_de_salas_desafio.dto.ReservaDto;
import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarReservaDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarReservaDto;
import br.com.alura.reserva_de_salas_desafio.entities.ReservaEntity;
import br.com.alura.reserva_de_salas_desafio.enums.StatusReserva;
import br.com.alura.reserva_de_salas_desafio.exceptions.ValidacaoException;
import br.com.alura.reserva_de_salas_desafio.repository.ReservaRepository;
import br.com.alura.reserva_de_salas_desafio.validacoes.ValidacaoAtualizarReserva;
import br.com.alura.reserva_de_salas_desafio.validacoes.ValidacaoCadastrarReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private List<ValidacaoCadastrarReserva> validacoesCadastrar;

    @Autowired
    private List<ValidacaoAtualizarReserva> validacoesAtualizar;

    @Transactional(readOnly = true)
    public List<ReservaDto> listar() {
        return reservaRepository.findAll()
                .stream()
                .map(ReservaDto::new)
                .toList();
    }

    @Transactional(readOnly = true)
    public ReservaEntity buscarPorId(ReservaDto dto) {
        return reservaRepository.findById(dto.id()).orElseThrow(() -> new ValidacaoException("Reserva com ID " + dto.id() + " não encontrada."));
    }

    @Transactional
    public void cadastrar(CadastrarReservaDto dto) {
        validacoesCadastrar.forEach(v -> v.validar(dto));

        reservaRepository.save(new ReservaEntity(dto));
    }

    @Transactional
    public void atualizar(AtualizarReservaDto dto) {
        validacoesAtualizar.forEach(v -> v.validar(dto));

        ReservaEntity reserva = reservaRepository.findById(dto.id()).orElseThrow(() -> new ValidacaoException("Reserva não encontrada para o ID informado."));
        reserva.atualizar(dto);
    }

    @Transactional
    public void apagarReserva(Long id) {
        reservaRepository.deleteById(id);
    }

    @Transactional
    public void cancelarReserva(Long id) {
        var reserva = reservaRepository.findById(id).orElseThrow(() -> new ValidacaoException("Reserva não encontrada"));

        if (reserva.getStatusReserva() == StatusReserva.CANCELADA) {
            throw new ValidacaoException("reserva já cancelada");
        }

        reserva.setStatusReserva(StatusReserva.CANCELADA);
    }

    //used ai's help for this
    @Transactional(readOnly = true)
    public Page<ReservaDto> listarConflitos(Long salaId, LocalDate inicio, LocalDate fim, Pageable pageable) {
        return reservaRepository.buscarConflitosPaginados(salaId, inicio, fim, pageable)
                .map(reserva -> new ReservaDto(reserva.getId(),
                        reserva.getDataInicio(),
                        reserva.getDataFim(), reserva.getSalaEntity(),
                        reserva.getUsuarioEntity(),
                        reserva.getStatusReserva()));
    }
}
