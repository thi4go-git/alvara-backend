package br.com.alvara.model.entity;


import br.com.alvara.rest.dto.UsuarioDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;

    @Column(nullable = false)
    private boolean ativo;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(unique = true, nullable = false, length = 11)
    private String cpf;

    @Column(length = 100)
    private String email;

    @Column(length = 11)
    private String celular;

    @Column
    private byte[] foto;

    public Usuario(UsuarioDTO dto) {
        this.username = dto.getUsername();
        this.password = dto.getPassword();
        this.role = "USER";
        this.ativo = false;
        this.nome = dto.getNome();
        this.cpf = dto.getCpf();
    }
}
