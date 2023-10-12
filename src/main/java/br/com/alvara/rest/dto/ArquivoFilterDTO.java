package br.com.alvara.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArquivoFilterDTO {
    private String nome_empresa;
    private String cnpj_empresa;
    private String numero_alvara;
    private String tipo_doc;
}
