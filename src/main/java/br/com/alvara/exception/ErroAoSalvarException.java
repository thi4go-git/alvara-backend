package br.com.alvara.exception;

public class ErroAoSalvarException extends RuntimeException {
    public ErroAoSalvarException(String cause) {
        super("Erro Ao processar PDF " + cause);
    }
}
