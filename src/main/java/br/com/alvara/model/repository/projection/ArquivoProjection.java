package br.com.alvara.model.repository.projection;


import br.com.alvara.model.enums.StatusDocumento;
import br.com.alvara.model.enums.TipoDocumento;

import java.time.LocalDate;

public interface ArquivoProjection {
    Long getId();

    TipoDocumento getTipoDoc();

    String getNomeArquivo();

    String getNumeroAlvara();

    String getNomeEmpresa();

    String getCnpjEmpresa();

    LocalDate getDataEmissao();

    LocalDate getDataVencimento();

    Integer getExpira();

    String getObservacao();

    StatusDocumento getStatusDocumento();
}
