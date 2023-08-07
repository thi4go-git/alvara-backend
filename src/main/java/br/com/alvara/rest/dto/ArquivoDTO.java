package br.com.alvara.rest.dto;

import br.com.alvara.model.tipo.TipoDocumento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CNPJ;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArquivoDTO {

    private Integer id;

    private TipoDocumento tipoDoc;

    @NotEmpty(message = "{campo.nomeArquivo.obrigatorio}")
    private String nomeArquivo;

    @NotEmpty(message = "{campo.numeroAlvara.obrigatorio}")
    private String numeroAlvara;

    @NotEmpty(message = "{campo.nomeEmpresa.obrigatorio}")
    private String nomeEmpresa;

    @CNPJ(message = "{campo.cnpjEmpresa.invalido}")
    private String cnpjEmpresa;

    private LocalDate dataEmissao;

    private LocalDate dataVencimento;

    private Integer expira;

    private String observacao;
}
