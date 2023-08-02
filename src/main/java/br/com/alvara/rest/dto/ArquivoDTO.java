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

    private TipoDocumento tipo_doc;

    @NotEmpty(message = "{campo.nome_arquivo.obrigatorio}")
    private String nome_arquivo;

    @NotEmpty(message = "{campo.numero_alvara.obrigatorio}")
    private String numero_alvara;

    @NotEmpty(message = "{campo.nome_empresa.obrigatorio}")
    private String nome_empresa;

    @CNPJ(message = "{campo.cnpj.invalido}")
    private String cnpj_empresa;

    private LocalDate data_emissao;

    private LocalDate data_vencimento;

    private Integer expira;

    private String observacao;
}
