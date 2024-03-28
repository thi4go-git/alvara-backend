package br.com.alvara.rest.dto;

import br.com.alvara.anottation.CPFunico;
import br.com.alvara.anottation.UserUnico;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO {

    @NotBlank(message = "{campo.username.obrigatorio}")
    @UserUnico
    private String username;

    @NotBlank(message = "{campo.password.obrigatorio}")
    private String password;

    @NotBlank(message = "{campo.nome.obrigatorio}")
    private String nome;

    @NotBlank(message = "{campo.cpf.obrigatorio}")
    @CPF(message = "{campo.cpf.invalido}")
    @CPFunico
    private String cpf;

    @Email(message = "{campo.email.invalido}")
    private String email;

    private String celular;

}
