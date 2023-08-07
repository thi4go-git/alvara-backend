package br.com.alvara.model.entity;

import br.com.alvara.model.tipo.TipoDocumento;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Arquivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo_doc")
    private TipoDocumento tipoDoc;

    @Column(name = "nome_arquivo", length = 150)
    private String nomeArquivo;

    @Column(name = "numero_alvara", length = 50)
    private String numeroAlvara;

    @Column(name = "nome_empresa", length = 50)
    private String nomeEmpresa;

    @Column(name = "cnpj_empresa", length = 14)
    private String cnpjEmpresa;

    @Column(name = "data_emissao")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataEmissao;

    @Column(name = "data_vencimento")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate dataVencimento;

    @Column
    private Integer expira;

    @Column(nullable = false)
    private byte[] pdf;

    @Column
    private String observacao;

    public Arquivo(byte[] pdf) {
        this.pdf = pdf;
    }

}
