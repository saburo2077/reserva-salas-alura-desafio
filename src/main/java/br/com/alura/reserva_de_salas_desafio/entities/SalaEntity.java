package br.com.alura.reserva_de_salas_desafio.entities;

import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarSalaDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarSalaDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "salas")
public class SalaEntity {

    public SalaEntity() {}

    public SalaEntity(CadastrarSalaDto dto) {
        this.capacidade = dto.capacidade();
        this.ativo = dto.ativo();
    }

    public void atualizarDados(AtualizarSalaDto dto) {
        this.capacidade = dto.capacidade();
        this.ativo = dto.ativo();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Min(value = 1, message = "A capacidade deve ser maior que zero")
    private int capacidade;
    private boolean ativo;

    public Long getId() {
        return id;
    }

    public int getCapacidade() {
        return capacidade;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
