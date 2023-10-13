package br.com.alvara.model.repository.projection.impl;


import br.com.alvara.model.enums.StatusDocumento;
import br.com.alvara.model.repository.projection.ArquivoProjection;
import br.com.alvara.model.enums.TipoDocumento;
import lombok.Setter;

import java.time.LocalDate;


@Setter
public class ArquivoProjectionImpl implements ArquivoProjection {

    private Long id;
    private TipoDocumento tipo_doc;
    private String nome_arquivo;
    private String numero_alvara;
    private String nome_empresa;
    private String cnpj_empresa;
    private LocalDate data_emissao;
    private LocalDate data_vencimento;
    private Integer expira;
    private String observacao;
    private StatusDocumento status_documento;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public TipoDocumento getTipo_doc() {
        return tipo_doc;
    }

    @Override
    public String getNome_arquivo() {
        return nome_arquivo;
    }

    @Override
    public String getNumero_alvara() {
        return numero_alvara;
    }

    @Override
    public String getNome_empresa() {
        return nome_empresa;
    }

    @Override
    public String getCnpj_empresa() {
        return cnpj_empresa;
    }

    @Override
    public LocalDate getData_emissao() {
        return data_emissao;
    }

    @Override
    public LocalDate getData_vencimento() {
        return data_vencimento;
    }

    @Override
    public Integer getExpira() {
        return expira;
    }

    @Override
    public String getObservacao() {
        return observacao;
    }

    @Override
    public StatusDocumento getStatus_documento() {
        return status_documento;
    }
}
