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

@Component
public class Pdf {

    private static final Logger LOG = LoggerFactory.getLogger(Pdf.class);
    private static final String NAO_ENCONTRADO = "Não Localizado!";
    private static final String CNPJ_NULO = "00000000000000";
    private static final String INDEX_CNPJ = "CNPJ:";


    public Arquivo lerPdf(File pdf, byte[] bytes) {

        Arquivo arquivo = new Arquivo();
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

                arquivo.setPdf(bytes);
                arquivo.setNome_arquivo(pdf.getName());
                arquivo.setTipo_doc(retornarTipoDocumento(txtStr));
                arquivo.setNumero_alvara(retornarNumeroDocumento(txtStr));
                arquivo.setNome_empresa(retornarNomeEmpresa(txtStr));
                arquivo.setCnpj_empresa(retornarCnpj(txtStr, arquivo.getTipo_doc()));
                arquivo.setData_emissao(retornarDtEmissao(txtStr, arquivo.getTipo_doc()));
                arquivo.setData_vencimento(retornarDtVencimento(txtStr, arquivo.getTipo_doc(),
                        arquivo.getData_emissao()));

                return arquivo;
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
            if (txt.contains("Nº DA LICENÇA:")) {
                String tagIni = "Nº DA LICENÇA:";
                String tagFim = "CCP:";
                int ini = txt.indexOf(tagIni);
                int fim = txt.indexOf(tagFim);
                return txt.substring(ini, fim).replace(tagIni, "").trim();
            } else {
                if (txt.contains("ESTABELECIMENTOEMPRESARIALNÚMERO")) {
                    String tagIni = "ESTABELECIMENTOEMPRESARIALNÚMERO";
                    int ini = txt.indexOf(tagIni);
                    String numero = txt.substring(ini, txt.length()).replace(tagIni, "").trim();
                    return refatoraNumero(numero);
                } else {
                    if (txt.contains("ESTABELECIMENTO.NÚMERO")) {
                        String tagIni = "ESTABELECIMENTO.NÚMERO";
                        int ini = txt.indexOf(tagIni);
                        String numero = txt.substring(ini, txt.length()).replace(tagIni, "").trim();
                        return refatoraNumero(numero);
                    } else {
                        if (txt.contains("CERTIFICADAPROTOCOLO:")) {
                            String tagIni = "CERTIFICADAPROTOCOLO:";
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
            if (txt.contains("NOME DA EMPRESA: ")) {
                String tagIni = "NOME DA EMPRESA: ";
                int ini = txt.indexOf(tagIni);
                int fim = txt.indexOf(INDEX_CNPJ);
                return txt.substring(ini, fim).replace(tagIni, "").trim();
            } else {
                if (txt.contains("RAZÃO SOCIAL: ")) {
                    String tagIni = "RAZÃO SOCIAL: ";
                    int ini = txt.indexOf(tagIni);
                    int fim = txt.indexOf(INDEX_CNPJ);
                    return txt.substring(ini, fim).replace(tagIni, "");
                } else {
                    if (txt.contains("CONCEDE A LICENÇA AMBIENTAL DECLARATÓRIA A ")) {
                        String tagIni = "CONCEDE A LICENÇA AMBIENTAL DECLARATÓRIA A";
                        String tagFim = ", INSCRITA NO CNPJ";
                        int ini = txt.indexOf(tagIni);
                        int fim = txt.indexOf(tagFim);
                        return txt.substring(ini, fim).replace(tagIni
                                , "");
                    } else {
                        if (txt.contains("DE RISCONOME FANTASIA")) {
                            String tagIni = "DE RISCONOME FANTASIA";
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

        String regex = "[0-9]+";
        for (int f = 0; f < numero.length(); f++) {
            if (numero.substring(f, (f + 1)).matches(regex)) {
                novoNumero.append(numero.substring(f, (f + 1)));
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
                        .replace("DE", "/")
                        .trim();
                String[] obj = dataStr.split("[,]", -1);

                String[] objData = obj[1].split("[/]", -1);
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
                            .replace("DE", "/")
                            .trim();
                    String[] obj = dataStr.split("[,]", -1);
                    String[] objData = obj[1].split("[/]", -1);
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
                                .replace("DE", "/")
                                .trim();
                        String[] obj = dataStr.split("[,]", -1);

                        String[] objData = obj[1].split("[/]", -1);
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
                                    .replace("DE", "/")
                                    .trim();

                            String[] objData = dataStr.split("[/]", -1);
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
                            .replace("DE", "/")
                            .trim();
                    String[] obj = dataStr.split("[,]", -1);

                    String[] objData = obj[1].split("[/]", -1);
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


    public String retornarMes(String mes) {
        switch (mes) {
            case "JANEIRO":
                return "01";
            case "FEVEREIRO":
                return "02";
            case "MARÇO":
                return "03";
            case "ABRIL":
                return "04";
            case "MAIO":
                return "05";
            case "JUNHO":
                return "06";
            case "JULHO":
                return "07";
            case "AGOSTO":
                return "08";
            case "SETEMBRO":
                return "09";
            case "OUTUBRO":
                return "10";
            case "NOVEMBRO":
                return "11";
            case "DEZEMBRO":
                return "12";
            default:
                return "00";
        }
    }


}