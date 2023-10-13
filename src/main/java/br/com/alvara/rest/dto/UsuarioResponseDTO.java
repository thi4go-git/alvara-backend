package br.com.alvara.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponseDTO {
    private Integer id;
    private String username;
    private String password;
    private String role;
    private boolean ativo;
    private String nome;
    private String cpf;
    private String email;
    private String celular;
}
