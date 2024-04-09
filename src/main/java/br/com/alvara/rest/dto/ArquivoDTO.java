package br.com.alvara.rest.dto;

import br.com.alvara.model.enums.StatusDocumento;
import br.com.alvara.model.enums.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ArquivoDTO {

    private Integer id;

    private TipoDocumento tipoDoc = null;

    @NotBlank(message = "{campo.nomeArquivo.obrigatorio}")
    private String nomeArquivo;

    @NotBlank(message = "{campo.numeroAlvara.obrigatorio}")
    private String numeroAlvara;

    @NotBlank(message = "{campo.nomeEmpresa.obrigatorio}")
    private String nomeEmpresa;

    @CNPJ(message = "{campo.cnpjEmpresa.invalido}")
    private String cnpjEmpresa;

    private LocalDate dataEmissao;

    private LocalDate dataVencimento;

    private Integer expira;

    private String observacao;

    private StatusDocumento statusDocumento = null;
}
