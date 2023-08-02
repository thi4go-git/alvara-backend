package br.com.alvara.model.entity;

import br.com.alvara.model.tipo.TipoDocumento;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Locale;

public class Pdf {

    final static String NAO_ENCONTRADO = "Não Localizado!";

    public Arquivo lerPdf(File pdf, byte[] bytes) {

        Arquivo arquivo = new Arquivo();
        try (PDDocument document = PDDocument.load(pdf)) {
            if (!document.isEncrypted()) {
                PDFTextStripperByArea stripper = null;
                stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);
                PDFTextStripper tStripper = new PDFTextStripper();
                String pdfFileInText = tStripper.getText(document);
                String lines[] = pdfFileInText.split("\\r?\\n");
                String txt = "";
                for (String line : lines) {
                    txt = txt + line.toUpperCase(Locale.ROOT);
                }
                arquivo.setPdf(bytes);
                arquivo.setNome_arquivo(pdf.getName());
                arquivo.setTipo_doc(retornarTipoDocumento(txt));
                arquivo.setNumero_alvara(retornarNumeroDocumento(txt));
                arquivo.setNome_empresa(retornarNomeEmpresa(txt));
                arquivo.setCnpj_empresa(retornarCnpj(txt, arquivo.getTipo_doc()));
                arquivo.setData_emissao(retornarDtEmissao(txt, arquivo.getTipo_doc()));
                arquivo.setData_vencimento(retornarDtVencimento(txt, arquivo.getTipo_doc(),
                        arquivo.getData_emissao()));

                return arquivo;
            }
        } catch (IOException e) {
            System.out.println(e.getCause());
        }
        return new Arquivo(bytes);
    }

    public File byteTofile(byte[] bytesArquivo, String nome) {
        File pdfExistente = new File(nome);
        if (pdfExistente.exists()) {
            pdfExistente.delete();
        }
        File file;
        try {
            byte[] bytes = bytesArquivo;
            file = new File(nome);
            try (FileOutputStream fos = new FileOutputStream(file)) {
                fos.write(bytes);
            }
            return file;
        } catch (IOException e) {
            System.out.println(e);
        }
        return null;
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
                            String tagFim = "CNPJ:";
                            int ini = txt.indexOf(tagIni);
                            int fim = txt.indexOf(tagFim);
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
                String tagFim = "CNPJ: ";
                int ini = txt.indexOf(tagIni);
                int fim = txt.indexOf(tagFim);
                return txt.substring(ini, fim).replace(tagIni, "").trim();
            } else {
                if (txt.contains("RAZÃO SOCIAL: ")) {
                    String tagIni = "RAZÃO SOCIAL: ";
                    String tagFim = "CNPJ:";
                    int ini = txt.indexOf(tagIni);
                    int fim = txt.indexOf(tagFim);
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
        String novoNumero = "";
        numero = numero.trim();
        String regex = "[0-9]+";
        for (int f = 0; f < numero.length(); f++) {
            if (numero.substring(f, (f + 1)).matches(regex)) {
                novoNumero = novoNumero + numero.substring(f, (f + 1));
            } else {
                break;
            }
        }
        return novoNumero;
    }


    public String retornarCnpj(String txt, TipoDocumento ticpoDoc) {
        try {
            if (ticpoDoc == TipoDocumento.ALVARA_FUNCIONAMENTO) {
                String tagIni = "CNPJ: ";
                String tagFim = "ATIVIDADE(S)";
                int ini = txt.indexOf(tagIni);
                int fim = txt.indexOf(tagFim);
                return txt.substring(ini, fim).replace(tagIni, "")
                        .replace(".", "")
                        .replace("/", "")
                        .replace("-", "")
                        .trim();
            } else {
                if (ticpoDoc == TipoDocumento.LICENCA_SANITARIA) {
                    String tagIni = "CNPJ:  ";
                    String tagFim = "ENDEREÇO:";
                    int ini = txt.indexOf(tagIni);
                    int fim = txt.indexOf(tagFim);
                    return txt.substring(ini, fim).replace(tagIni, "")
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
                            String tagIni = "CNPJ:";
                            int ini = txt.indexOf(tagIni) + 5;
                            return txt.substring(ini, ini + 19).replace(tagIni, "")
                                    .replace(".", "")
                                    .replace("/", "")
                                    .replace("-", "").trim();
                        }
                    }
                }
            }
        } catch (Exception e) {
            return "00000000000000";
        }
        return "00000000000000";
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
                String obj[] = dataStr.split("[,]", -1);

                String objData[] = obj[1].split("[/]", -1);
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
                    String obj[] = dataStr.split("[,]", -1);
                    String objData[] = obj[1].split("[/]", -1);
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
                        String obj[] = dataStr.split("[,]", -1);

                        String objData[] = obj[1].split("[/]", -1);
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

                            String objData[] = dataStr.split("[/]", -1);
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
                    String tagFim = "OBSERVAÇÃO CCP:";
                    int ini = txt.indexOf(tagIni);
                    int fim = txt.indexOf(tagFim);
                    String dataStr = txt.substring(ini, fim)
                            .replace(tagIni, "")
                            .replace(" ", "")
                            .replace("DE", "/")
                            .trim();
                    String obj[] = dataStr.split("[,]", -1);

                    String objData[] = obj[1].split("[/]", -1);
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