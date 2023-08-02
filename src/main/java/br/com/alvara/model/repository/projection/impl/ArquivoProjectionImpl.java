package br.com.alvara.model.repository.projection.impl;


import br.com.alvara.model.repository.projection.ArquivoProjection;
import br.com.alvara.model.tipo.TipoDocumento;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;

@Setter
public class ArquivoProjectionImpl  implements ArquivoProjection {
    private Integer id;
    private TipoDocumento tipo_doc;
    private String nome_arquivo;
    private String numero_alvara;
    private String nome_empresa;
    private String cnpj_empresa;
    private LocalDate data_emissao;
    private LocalDate data_vencimento;
    private Integer expira;
    private String observacao;

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public TipoDocumento getTipo_doc() {
        return this.tipo_doc;
    }

    @Override
    public String getNome_arquivo() {
        return null;
    }

    @Override
    public String getNumero_alvara() {
        return null;
    }

    @Override
    public String getNome_empresa() {
        return null;
    }

    @Override
    public String getCnpj_empresa() {
        return null;
    }

    @Override
    public LocalDate getData_emissao() {
        return null;
    }

    @Override
    public LocalDate getData_vencimento() {
        return null;
    }

    @Override
    public Integer getExpira() {
        return null;
    }

    @Override
    public String getObservacao() {
        return null;
    }
}
