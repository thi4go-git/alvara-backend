package br.com.alvara.model.repository.projection;


import br.com.alvara.model.tipo.TipoDocumento;

import java.time.LocalDate;

public interface ArquivoProjection {
    Long getId();

    TipoDocumento getTipo_doc();

    String getNome_arquivo();

    String getNumero_alvara();

    String getNome_empresa();

    String getCnpj_empresa();

    LocalDate getData_emissao();

    LocalDate getData_vencimento();

    Integer getExpira();

    String getObservacao();
}
