package br.com.alvara.extrator;

import br.com.alvara.model.entity.Arquivo;
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

import static br.com.alvara.utils.DataUtils.retornarMes;

@Component
public class Pdf {

    private static final Logger LOG = LoggerFactory.getLogger(Pdf.class);
    private static final String NAO_ENCONTRADO = "Não Localizado!";
    private static final String CNPJ_NULO = "00000000000000";
    private static final String INDEX_CNPJ = "CNPJ:";
    private static final String TAG_NUM_ESTABALEC_EMPRESARIAL = "ESTABELECIMENTOEMPRESARIALNÚMERO";
    private static final String TAG_NUM_LICENCA = "Nº DA LICENÇA:";
    private static final String TAG_ESTABELECIMENTO_NUM = "ESTABELECIMENTO.NÚMERO";
    private static final String TAG_CERTIFICADO_PROTOCOLO = "CERTIFICADAPROTOCOLO:";
    private static final String TAG_NOME_EMPRESA = "NOME DA EMPRESA: ";
    private static final String TAG_RAZAO_SOCIAL = "RAZÃO SOCIAL: ";
    private static final String TAG_LIC_AMBIENTAL = "CONCEDE A LICENÇA AMBIENTAL DECLARATÓRIA A";
    private static final String TAG_RISCO_NOME = "DE RISCONOME FANTASIA";
    private static final String TAG_DE = "DE";
    private static final String SEPARADOR_COLCHETE_BARRA = "[/]";
    private static final String SEPARADOR_COLCHETE_VIRGULA = "[,]";


    public Arquivo lerPdf(File pdf, byte[] bytes) {
        try (PDDocument document = PDDocument.load(pdf)) {
            if (!document.isEncrypted()) {

                PDFTextStripperByArea stripper;
                stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                PDFTextStripper tStripper = new PDFTextStripper();
                String pdfFileInText = tStripper.getText(document);
                String[] lines = pdfFileInText.split("\\r?\\n");

                StringBuilder txtBuilder = new StringBuilder();
                for (String line : lines) {
                    txtBuilder.append(line.toUpperCase(Locale.ROOT));
                }

                String txtStr = txtBuilder.toString();

                TipoDocumento tipoDoc = retornarTipoDocumento(txtStr);
                LocalDate dataEmissao = retornarDtEmissao(txtStr, tipoDoc);

                return Arquivo.builder()
                        .pdf(bytes)
                        .nomeArquivo(pdf.getName())
                        .tipoDoc(tipoDoc)
                        .numeroAlvara(retornarNumeroDocumento(txtStr))
                        .nomeEmpresa(retornarNomeEmpresa(txtStr))
                        .cnpjEmpresa(retornarCnpj(txtStr, tipoDoc))
                        .dataEmissao(dataEmissao)
                        .dataVencimento(retornarDtVencimento(txtStr, tipoDoc, dataEmissao))
                        .build();
            }
        } catch (IOException e) {
            // Não tratar erro aqui
            LOG.info(e.toString());
        }
        return new Arquivo(bytes);
    }


    public TipoDocumento retornarTipoDocumento(String txt) {
        try {
            if (txt.contains("ALVARÁ DE LOCALIZAÇÃO E")) {
                return TipoDocumento.ALVARA_FUNCIONAMENTO;
            } else {
                if (txt.contains("LICENCIAMENTO SANITÁRIO")) {
                    return TipoDocumento.LICENCA_SANITARIA;
                } else {
                    if (txt.contains("LICENÇA AMBIENTAL DECLARATÓRIA")) {
                        return TipoDocumento.LICENCA_AMBIENTAL;
                    } else {
                        if (txt.contains("CORPO DE BOMBEIROS MILITAR")) {
                            return TipoDocumento.ALVARA_BOMBEIRO;
                        }
                    }
                }
            }
        } catch (Exception e) {
            return TipoDocumento.DESCONHECIDO;
        }
        return TipoDocumento.DESCONHECIDO;
    }

    public String retornarNumeroDocumento(String txt) {
        try {
            if (txt.contains(TAG_NUM_LICENCA)) {
                String tagIni = TAG_NUM_LICENCA;
                String tagFim = "CCP:";
                int ini = txt.indexOf(tagIni);
                int fim = txt.indexOf(tagFim);
                return txt.substring(ini, fim).replace(tagIni, "").trim();
            } else {
                if (txt.contains(TAG_NUM_ESTABALEC_EMPRESARIAL)) {
                    String tagIni = TAG_NUM_ESTABALEC_EMPRESARIAL;
                    int ini = txt.indexOf(tagIni);
                    String numero = txt.substring(ini).replace(tagIni, "").trim();
                    return refatoraNumero(numero);
                } else {
                    if (txt.contains(TAG_ESTABELECIMENTO_NUM)) {
                        String tagIni = TAG_ESTABELECIMENTO_NUM;
                        int ini = txt.indexOf(tagIni);
                        String numero = txt.substring(ini).replace(tagIni, "").trim();
                        return refatoraNumero(numero);
                    } else {
                        if (txt.contains(TAG_CERTIFICADO_PROTOCOLO)) {
                            String tagIni = TAG_CERTIFICADO_PROTOCOLO;
                            int ini = txt.indexOf(tagIni);
                            int fim = txt.indexOf(INDEX_CNPJ);
                            String numero = txt.substring(ini, fim).replace(tagIni, "").trim();
                            return refatoraNumero(numero);
                        }
                    }
                }
            }
        } catch (Exception e) {
            return NAO_ENCONTRADO;
        }
        return NAO_ENCONTRADO;
    }

    public String retornarNomeEmpresa(String txt) {
        try {
            if (txt.contains(TAG_NOME_EMPRESA)) {
                String tagIni = TAG_NOME_EMPRESA;
                int ini = txt.indexOf(tagIni);
                int fim = txt.indexOf(INDEX_CNPJ);
                return txt.substring(ini, fim).replace(tagIni, "").trim();
            } else {
                if (txt.contains(TAG_RAZAO_SOCIAL)) {
                    String tagIni = TAG_RAZAO_SOCIAL;
                    int ini = txt.indexOf(tagIni);
                    int fim = txt.indexOf(INDEX_CNPJ);
                    return txt.substring(ini, fim).replace(tagIni, "");
                } else {
                    if (txt.contains(TAG_LIC_AMBIENTAL)) {
                        String tagIni = TAG_LIC_AMBIENTAL;
                        String tagFim = ", INSCRITA NO CNPJ";
                        int ini = txt.indexOf(tagIni);
                        int fim = txt.indexOf(tagFim);
                        return txt.substring(ini, fim).replace(tagIni
                                , "");
                    } else {
                        if (txt.contains(TAG_RISCO_NOME)) {
                            String tagIni = TAG_RISCO_NOME;
                            String tagFim = "FONE(";
                            int ini = txt.indexOf(tagIni);
                            int fim = txt.indexOf(tagFim);
                            return txt.substring(ini, fim).replace(tagIni
                                    , "");
                        }
                    }
                }
            }
        } catch (Exception e) {
            return NAO_ENCONTRADO;
        }
        return NAO_ENCONTRADO;
    }

    public String refatoraNumero(String numero) {

        StringBuilder novoNumero = new StringBuilder();
        numero = numero.trim();

        String regex = "\\d+";
        for (int index = 0; index < numero.length(); index++) {
            if (numero.substring(index, (index + 1)).matches(regex)) {
                novoNumero.append(numero.charAt(index));
            } else {
                break;
            }
        }
        return novoNumero.toString();
    }


    public String retornarCnpj(String txt, TipoDocumento ticpoDoc) {

        try {
            if (ticpoDoc == TipoDocumento.ALVARA_FUNCIONAMENTO) {
                String tagFim = "ATIVIDADE(S)";
                int ini = txt.indexOf(INDEX_CNPJ);
                int fim = txt.indexOf(tagFim);
                return txt.substring(ini, fim).replace(INDEX_CNPJ, "")
                        .replace(".", "")
                        .replace("/", "")
                        .replace("-", "")
                        .trim();
            } else {
                if (ticpoDoc == TipoDocumento.LICENCA_SANITARIA) {
                    String tagFim = "ENDEREÇO:";
                    int ini = txt.indexOf(INDEX_CNPJ);
                    int fim = txt.indexOf(tagFim);
                    return txt.substring(ini, fim).replace(INDEX_CNPJ, "")
                            .replace(".", "")
                            .replace("/", "")
                            .replace("-", "").trim();
                } else {
                    if (ticpoDoc == TipoDocumento.LICENCA_AMBIENTAL) {
                        String tagIni = "INSCRITA NO CNPJ N° ";
                        String tagFim = ", NAS CONDIÇÕES ABAIXO";
                        int ini = txt.indexOf(tagIni);
                        int fim = txt.indexOf(tagFim);
                        return txt.substring(ini, fim).replace(tagIni, "")
                                .replace(".", "")
                                .replace("/", "")
                                .replace("-", "").trim();
                    } else {
                        if (ticpoDoc == TipoDocumento.ALVARA_BOMBEIRO) {
                            int ini = txt.indexOf(INDEX_CNPJ) + 5;
                            return txt.substring(ini, ini + 19).replace(INDEX_CNPJ, "")
                                    .replace(".", "")
                                    .replace("/", "")
                                    .replace("-", "").trim();
                        }
                    }
                }
            }
        } catch (Exception e) {
            return CNPJ_NULO;
        }
        return CNPJ_NULO;
    }

    public LocalDate retornarDtEmissao(String txt, TipoDocumento ticpoDoc) {
        try {
            if (ticpoDoc == TipoDocumento.LICENCA_SANITARIA) {
                String tagIni = "ESTE DOCUMENTO FOI EMITIDO EM";
                String tagFim = "SE IMPRESSO,";
                int ini = txt.indexOf(tagIni);
                int fim = txt.indexOf(tagFim);

                String dataStr = txt.substring(ini, fim)
                        .replace(tagIni, "")
                        .replace(" ", "")
                        .replace(TAG_DE, "/")
                        .trim();
                String[] obj = dataStr.split(SEPARADOR_COLCHETE_VIRGULA, -1);

                String[] objData = obj[1].split(SEPARADOR_COLCHETE_BARRA, -1);
                int dia = Integer.parseInt(objData[0]);
                int mes = Integer.parseInt(retornarMes(objData[1]));
                int ano = Integer.parseInt(objData[2]);

                return LocalDate.of(ano, mes, dia);
            } else {
                if (ticpoDoc == TipoDocumento.LICENCA_AMBIENTAL) {
                    String tagIni = "DATA DA EXPEDIÇÃO: ";
                    String tagFim = "DATA DE VALIDADE:";
                    int ini = txt.indexOf(tagIni);
                    int fim = txt.indexOf(tagFim);
                    String dataStr = txt.substring(ini, fim)
                            .replace(tagIni, "")
                            .replace(" ", "")
                            .replace(TAG_DE, "/")
                            .trim();
                    String[] obj = dataStr.split(SEPARADOR_COLCHETE_VIRGULA, -1);
                    String[] objData = obj[1].split(SEPARADOR_COLCHETE_BARRA, -1);
                    int dia = Integer.parseInt(objData[0]);
                    int mes = Integer.parseInt(retornarMes(objData[1]));
                    int ano = Integer.parseInt(objData[2]);

                    return LocalDate.of(ano, mes, dia);
                } else {
                    if (ticpoDoc == TipoDocumento.ALVARA_FUNCIONAMENTO) {
                        String tagIni = "ESTE DOCUMENTO FOI EMITIDO EM ";
                        String tagFim = "SE IMPRESSO";
                        int ini = txt.indexOf(tagIni);
                        int fim = txt.indexOf(tagFim);
                        String dataStr = txt.substring(ini, fim)
                                .replace(tagIni, "")
                                .replace(" ", "")
                                .replace(TAG_DE, "/")
                                .trim();
                        String[] obj = dataStr.split(SEPARADOR_COLCHETE_VIRGULA, -1);

                        String[] objData = obj[1].split(SEPARADOR_COLCHETE_BARRA, -1);
                        int dia = Integer.parseInt(objData[0]);
                        int mes = Integer.parseInt(retornarMes(objData[1]));
                        int ano = Integer.parseInt(objData[2]);

                        return LocalDate.of(ano, mes, dia);
                    } else {
                        if (ticpoDoc == TipoDocumento.ALVARA_BOMBEIRO) {
                            String tagIni = "BOMBEIRO MILITARAPARECIDA DE GOIÂNIA, ";
                            String tagFim = ".";
                            int ini = txt.indexOf(tagIni);
                            int fim = txt.indexOf(tagFim);
                            String dataStr = txt.substring(ini, fim)
                                    .replace(tagIni, "")
                                    .replace(" ", "")
                                    .replace(TAG_DE, "/")
                                    .trim();

                            String[] objData = dataStr.split(SEPARADOR_COLCHETE_BARRA, -1);
                            int dia = Integer.parseInt(objData[0]);
                            int mes = Integer.parseInt(retornarMes(objData[1]));
                            int ano = Integer.parseInt(objData[2]);

                            return LocalDate.of(ano, mes, dia);
                        }
                    }
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }


    public LocalDate retornarDtVencimento(String txt, TipoDocumento ticpoDoc,
                                          LocalDate dataEmissao) {
        try {
            if (ticpoDoc == TipoDocumento.ALVARA_BOMBEIRO) {
                if (dataEmissao != null) {
                    return dataEmissao.plusYears(1);
                }
            } else {
                if (ticpoDoc == TipoDocumento.ALVARA_FUNCIONAMENTO) {
                    String tagIni = "VENCIMENTO:";
                    int ini = txt.indexOf(tagIni);
                    int fim = txt.indexOf("OBSERVAÇÃO CCP:");
                    String dataStr = txt.substring(ini, fim)
                            .replace(tagIni, "")
                            .replace(" ", "")
                            .replace(TAG_DE, "/")
                            .trim();
                    String[] obj = dataStr.split(SEPARADOR_COLCHETE_VIRGULA, -1);

                    String[] objData = obj[1].split(SEPARADOR_COLCHETE_BARRA, -1);
                    int dia = Integer.parseInt(objData[0]);
                    int mes = Integer.parseInt(retornarMes(objData[1]));
                    int ano = Integer.parseInt(objData[2]);

                    return LocalDate.of(ano, mes, dia);
                }
            }

        } catch (Exception e) {
            return null;
        }
        return null;
    }


}