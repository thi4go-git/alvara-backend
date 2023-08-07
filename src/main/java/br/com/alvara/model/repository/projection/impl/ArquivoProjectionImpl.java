package br.com.alvara.model.repository.projection.impl;


import br.com.alvara.model.repository.projection.ArquivoProjection;
import br.com.alvara.model.tipo.TipoDocumento;
import lombok.Setter;
import java.time.LocalDate;



@Setter
public class ArquivoProjectionImpl implements ArquivoProjection {

    private Long id;
    private TipoDocumento tipoDoc;
    private String nomeArquivo;
    private String numeroAlvara;
    private String nomeEmpresa;
    private String cnpjEmpresa;
    private LocalDate dataEmissao;
    private LocalDate dataVencimento;
    private Integer expira;
    private String observacao;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public TipoDocumento getTipoDoc() {
        return tipoDoc;
    }

    @Override
    public String getNomeArquivo() {
        return nomeArquivo;
    }

    @Override
    public String getNumeroAlvara() {
        return numeroAlvara;
    }

    @Override
    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    @Override
    public String getCnpjEmpresa() {
        return cnpjEmpresa;
    }

    @Override
    public LocalDate getDataEmissao() {
        return dataEmissao;
    }

    @Override
    public LocalDate getDataVencimento() {
        return dataVencimento;
    }

    @Override
    public Integer getExpira() {
        return expira;
    }

    @Override
    public String getObservacao() {
        return observacao;
    }

}
