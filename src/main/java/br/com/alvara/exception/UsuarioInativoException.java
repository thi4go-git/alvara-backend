package br.com.alvara.exception;

public class UsuarioInativoException extends RuntimeException {
    public UsuarioInativoException() {
        super("Erro! Usuário inativo! ");
    }
}
