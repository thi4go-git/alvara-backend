package br.com.alvara.anottation;

import br.com.alvara.anottation.impl.UserUnicoImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserUnicoImpl.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserUnico {
    String message() default "Já existe um Usuário cadastrado com o Username informado";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
