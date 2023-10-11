package br.com.alvara.rest.dto;

import br.com.alvara.model.tipo.TipoDocumento;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArquivoResponseDTO {

    private Integer id;
    private TipoDocumento tipo_doc;
    private String nome_arquivo;
    private String numero_alvara;
    private String nome_empresa;
    private String cnpj_empresa;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate data_emissao;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate data_vencimento;

    private Integer expira;
    private String observacao;


}
