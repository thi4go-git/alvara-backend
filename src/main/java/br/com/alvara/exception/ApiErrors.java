package br.com.alvara.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.Collections;
import java.util.List;

@Getter
@AllArgsConstructor
public class ApiErrors {
    private List<String> erros;

    public ApiErrors(String message) {
        this.erros = Collections.singletonList(message);
    }

}
