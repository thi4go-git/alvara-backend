package br.com.alvara.extrator;

import br.com.alvara.model.entity.Arquivo;
import br.com.alvara.model.enums.StatusDocumento;
import br.com.alvara.model.enums.TipoDocumento;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;
import java.util.Objects;

@Component
public class Pdf {

    private static final Logger LOG = LoggerFactory.getLogger(Pdf.class);


    public Arquivo lerPdf(File pdf, byte[] bytes) {
        try (PDDocument document = PDDocument.load(pdf)) {
            if (!document.isEncrypted()) {
                new PDFTextStripperByArea().setSortByPosition(true);
                String[] lines = new PDFTextStripper().getText(document).split("\\r?\\n");

                StringBuilder txtBuilder = new StringBuilder();
                for (String line : lines) {
                    txtBuilder.append(line.toUpperCase(Locale.ROOT));
                }

                String txtStr = txtBuilder.toString();

                TipoDocumento tipoDoc = retornarTipoDocumento(txtStr);
                LocalDate dataEmissao = DataEmissaoExtrator.retornarDataEmissao(txtStr);
                LocalDate dataVencimento = Objects.isNull(dataEmissao) ? null : DataVencimentoExtrator.retornarDataVencimento(txtStr, tipoDoc, dataEmissao);

                return Arquivo.builder()
                        .pdf(bytes)
                        .nomeArquivo(pdf.getName())
                        .tipoDoc(tipoDoc)
                        .numeroAlvara(NumeroDocumentoExtrator.retornarNumeroDocumento(txtStr))
                        .nomeEmpresa(NomeEmpresaExtrator.retornarNomeEmpresa(txtStr))
                        .cnpjEmpresa(CnpjExtrator.retornarCnpjEmpresa(txtStr))
                        .dataEmissao(dataEmissao)
                        .dataVencimento(dataVencimento)
                        .statusDocumento(StatusDocumento.ANALISE)
                        .build();
            } else {
                LOG.info("::: Não foi possível ler: Documento criptografado :::");
            }
        } catch (IOException e) {
            LOG.info(e.toString());
        }
        return new Arquivo(bytes);
    }


    public TipoDocumento retornarTipoDocumento(String txt) {
        try {
            if (txt.contains("ALVARÁ DE LOCALIZAÇÃO E")) {
                return TipoDocumento.ALVARA_FUNCIONAMENTO;
            } else if (
                    txt.contains("LICENCIAMENTO SANITÁRIO") || txt.contains("ALVARÁ SANITÁRIO")
            ) {
                return TipoDocumento.LICENCA_SANITARIA;
            } else if (txt.contains("LICENÇA AMBIENTAL DECLARATÓRIA")) {
                return TipoDocumento.LICENCA_AMBIENTAL;
            } else if (txt.contains("CORPO DE BOMBEIROS MILITAR")) {
                return TipoDocumento.ALVARA_BOMBEIRO;
            }
        } catch (Exception e) {
            return TipoDocumento.DESCONHECIDO;
        }
        return TipoDocumento.DESCONHECIDO;
    }
}