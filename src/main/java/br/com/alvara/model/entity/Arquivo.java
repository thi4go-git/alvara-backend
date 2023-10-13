package br.com.alvara.model.entity;

import br.com.alvara.model.enums.StatusDocumento;
import br.com.alvara.model.enums.TipoDocumento;
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
    @Column
    private Integer id;

    @Column
    private TipoDocumento tipo_doc;

    @Column(length = 150)
    private String nome_arquivo;

    @Column(length = 50)
    private String numero_alvara;

    @Column(length = 50)
    private String nome_empresa;

    @Column(length = 14)
    private String cnpj_empresa;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate data_emissao;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate data_vencimento;

    @Column
    private Integer expira;

    @Column(nullable = false)
    private byte[] pdf;

    @Column
    private String observacao;

    @Column
    private StatusDocumento status_documento;

    public Arquivo(byte[] pdf) {
        this.pdf = pdf;
    }

}
