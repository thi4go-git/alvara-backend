package br.com.alvara.rest.dto;

import br.com.alvara.model.enums.StatusDocumento;
import br.com.alvara.model.enums.TipoDocumento;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ArquivoDownloadDTO {
    private Integer id;
    private TipoDocumento tipoDoc;
    private String nomeArquivo;
    private String numeroAlvara;
    private String nomeEmpresa;
    private String cnpjEmpresa;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataEmissao;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataVencimento;

    private Integer expira;
    private String observacao;
    private StatusDocumento statusDocumento;
    private byte[] pdf;
}
