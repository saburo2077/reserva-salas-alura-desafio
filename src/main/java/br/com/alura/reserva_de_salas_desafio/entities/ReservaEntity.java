package br.com.alura.reserva_de_salas_desafio.entities;

import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarReservaDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarReservaDto;
import br.com.alura.reserva_de_salas_desafio.enums.StatusReserva;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "reservas")
public class ReservaEntity {

    public ReservaEntity() {}

    public ReservaEntity(CadastrarReservaDto dto) {
        this.dataInicio = dto.inicio();
        this.dataFim = dto.fim();
        this.salaEntity = dto.sala();
        this.usuarioEntity = dto.usuario();
        this.statusReserva = dto.status();
    }

    public void atualizar(AtualizarReservaDto dto) {
        this.dataInicio = dto.inicio();
        this.dataFim = dto.fim();
        this.salaEntity = dto.sala();
        this.usuarioEntity = dto.usuario();
        this.statusReserva = dto.status();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_fim", nullable = false)
    private LocalDate dataFim;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sala_id", nullable = false)
    private SalaEntity salaEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuarioEntity;

    @Enumerated(EnumType.STRING)
    private StatusReserva statusReserva;

    public Long getId() {
        return id;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public SalaEntity getSalaEntity() {
        return salaEntity;
    }

    public UsuarioEntity getUsuarioEntity() {
        return usuarioEntity;
    }

    public StatusReserva getStatusReserva() {
        return statusReserva;
    }

    public void setStatusReserva(StatusReserva statusReserva) {
        this.statusReserva = statusReserva;
    }
}
