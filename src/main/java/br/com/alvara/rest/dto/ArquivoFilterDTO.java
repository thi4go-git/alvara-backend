package br.com.alvara.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArquivoFilterDTO {
    private String nomeEmpresa;
    private String cnpjEmpresa;
    private String numeroAlvara;
    private String tipoDoc;
    private String statusDocumento;
}
