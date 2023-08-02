package br.com.alvara.rest.dto;

import br.com.alvara.anottation.CPFunico;
import br.com.alvara.anottation.UserUnico;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioDTO {

    @NotNull(message = "{campo.username.obrigatorio}")
    @UserUnico
    private String username;

    @NotEmpty(message = "{campo.password.obrigatorio}")
    private String password;

    @NotEmpty(message = "{campo.nome.obrigatorio}")
    private String nome;

    @NotNull(message = "{campo.cpf.obrigatorio}")
    @CPF(message = "{campo.cpf.invalido}")
    @CPFunico
    private String cpf;

}
