package br.com.alvara.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public class ApiErrors {
    @Getter
    private List<String> erros;

    public ApiErrors(String message) {
        this.erros = Arrays.asList(message);
    }

}
