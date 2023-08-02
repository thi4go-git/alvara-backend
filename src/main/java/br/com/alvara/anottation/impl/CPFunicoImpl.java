package br.com.alvara.anottation.impl;

import br.com.alvara.anottation.CPFunico;
import br.com.alvara.service.implementation.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CPFunicoImpl implements ConstraintValidator<CPFunico, String> {

    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        return usuarioService.buscarPorCpf(cpf).getId() == null;
    }
}
