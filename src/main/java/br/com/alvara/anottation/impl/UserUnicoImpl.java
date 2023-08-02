package br.com.alvara.anottation.impl;

import br.com.alvara.anottation.UserUnico;
import br.com.alvara.service.implementation.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UserUnicoImpl implements ConstraintValidator<UserUnico, String> {
    @Autowired
    private UsuarioServiceImpl usuarioService;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext context) {
        return usuarioService.buscarPorUsuername(username).getId() == null;
    }
}
