package br.com.alvara.exception;

public class ErroLayoutPDF extends RuntimeException {
    public ErroLayoutPDF() {
        super("Erro ao processar: Layout Desconhecido");
    }
}
