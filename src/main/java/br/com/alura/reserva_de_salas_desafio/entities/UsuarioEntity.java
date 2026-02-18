package br.com.alura.reserva_de_salas_desafio.entities;

import br.com.alura.reserva_de_salas_desafio.dto.atualizar.AtualizarUsuarioDto;
import br.com.alura.reserva_de_salas_desafio.dto.cadastrar.CadastrarUsuarioDto;
import jakarta.persistence.*;

@Entity
@Table(name = "usuarios")
public class UsuarioEntity {

    public UsuarioEntity() {}

    public UsuarioEntity(CadastrarUsuarioDto dto) {
        this.nome = dto.nome();
        this.email = dto.email();
        this.telefone = dto.telefone();
    }

    public void atualizar(AtualizarUsuarioDto dto) {
        this.nome = dto.nome();
        this.email = dto.email();
        this.telefone = dto.telefone();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;
    private String email;
    private String telefone;

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
