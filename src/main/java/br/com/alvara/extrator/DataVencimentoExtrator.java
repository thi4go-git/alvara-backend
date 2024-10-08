package br.com.alvara.extrator;

import br.com.alvara.model.enums.TipoDocumento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

import static br.com.alvara.utils.DataUtils.retornarMes;

public class DataVencimentoExtrator {
    private static final Logger LOG = LoggerFactory.getLogger(DataVencimentoExtrator.class);
    private static final String TAG_DE = "DE";
    private static final String SEPARADOR_COLCHETE_BARRA = "/";
    private static final String SEPARADOR_COLCHETE_VIRGULA = ",";
    private static final String VENCIMENTO = "VENCIMENTO:";
    private static final String DEZEMBRO = "DEZEMBRO";
    private static final String BARRA_ZEMBRO = "/ZEMBRO";

    public static LocalDate retornarDataVencimento(String txt, TipoDocumento ticpoDoc,
                                                   LocalDate dataEmissao) {

        if (ticpoDoc == TipoDocumento.ALVARA_BOMBEIRO) {
            if (dataEmissao != null) {
                return dataEmissao.plusYears(1);
            }
        }

        try {
            int ini = txt.indexOf(VENCIMENTO);
            int fim = txt.indexOf("OBSERVAÇÃO CCP:");
            String dataStr = txt.substring(ini, fim)
                    .replace(VENCIMENTO, "")
                    .replace(" ", "")
                    .replace(TAG_DE, "/")
                    .trim();
            String[] obj = dataStr.split(SEPARADOR_COLCHETE_VIRGULA, -1);
            String[] objData = obj[1].split(SEPARADOR_COLCHETE_BARRA, -1);
            int dia = Integer.parseInt(objData[0]);
            int mes = Integer.parseInt(retornarMes(objData[1]));
            int ano = Integer.parseInt(objData[2]);
            return LocalDate.of(ano, mes, dia);
        } catch (Exception e) {
            LOG.error("::: Erro ao obter DATA_VENCIMENTO_1 :::");
        }

        try {
            String tagIni = "VALIDADE ATÉ: ";
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf("EMISSÃO:");
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
        } catch (Exception e) {
            LOG.error("::: Erro ao obter DATA_VENCIMENTO_2 :::");
        }

        try {
            String tagIni = "VALIDADE: ";
            String tagFim = "ESTE DOCUMENTO FOI EMITIDO";
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
        } catch (Exception e) {
            LOG.error("::: Erro ao obter DATA_VENCIMENTO_3 :::");
        }

        try {
            String tagIni = VENCIMENTO;
            String tagFim = "OBSERVAÇÃO CCP";
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(tagFim);
            String dataStr = txt.substring(ini, fim)
                    .replace(tagIni, "")
                    .replace(" ", "")
                    .replace(TAG_DE, "/")
                    .replace(BARRA_ZEMBRO, DEZEMBRO)
                    .trim();
            String[] obj = dataStr.split(SEPARADOR_COLCHETE_VIRGULA, -1);
            String[] objData = obj[1].split(SEPARADOR_COLCHETE_BARRA, -1);
            int dia = Integer.parseInt(objData[0]);
            int mes = Integer.parseInt(retornarMes(objData[1]));
            int ano = Integer.parseInt(objData[2]);

            return LocalDate.of(ano, mes, dia);
        } catch (Exception e) {
            LOG.error("::: Erro ao obter DATA_VENCIMENTO_4 :::");
        }

        try {
            String tagIni = VENCIMENTO;
            String tagFim = "OBSERVAÇÃOCCP";
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(tagFim);
            String dataStr = txt.substring(ini, fim)
                    .replace(tagIni, "")
                    .replace(" ", "")
                    .replace(TAG_DE, "/")
                    .replace(BARRA_ZEMBRO, DEZEMBRO)
                    .trim();
            String[] obj = dataStr.split(SEPARADOR_COLCHETE_VIRGULA, -1);
            String[] objData = obj[1].split(SEPARADOR_COLCHETE_BARRA, -1);
            int dia = Integer.parseInt(objData[0]);
            int mes = Integer.parseInt(retornarMes(objData[1]));
            int ano = Integer.parseInt(objData[2]);

            return LocalDate.of(ano, mes, dia);
        } catch (Exception e) {
            LOG.error("::: Erro ao obter DATA_VENCIMENTO_5 :::");
        }

        try {
            String tagIni = "DATA DE VALIDADE:";
            String tagFim = "A SECRETARIA DE MEIO AMBIENTE";
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(tagFim);
            String dataStr = txt.substring(ini, fim)
                    .replace(tagIni, "")
                    .replace(" ", "")
                    .replace(TAG_DE, "/")
                    .replace(BARRA_ZEMBRO, DEZEMBRO)
                    .trim();
            String[] obj = dataStr.split(SEPARADOR_COLCHETE_VIRGULA, -1);
            String[] objData = obj[1].split(SEPARADOR_COLCHETE_BARRA, -1);
            int dia = Integer.parseInt(objData[0]);
            int mes = Integer.parseInt(retornarMes(objData[1]));
            int ano = Integer.parseInt(objData[2]);

            return LocalDate.of(ano, mes, dia);
        } catch (Exception e) {
            LOG.error("::: Erro ao obter DATA_VENCIMENTO_6 :::");
        }

        try {
            String tagIni = "VALIDADE: ";
            String tagFim = "CADASTRO (CCP):";
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(tagFim);
            String dataStr = txt.substring(ini, fim)
                    .replace(tagIni, "")
                    .replace(" ", "")
                    .replace(TAG_DE, "/")
                    .replace(BARRA_ZEMBRO, DEZEMBRO)
                    .trim();
            String[] objData = dataStr.split(SEPARADOR_COLCHETE_BARRA, -1);
            int dia = Integer.parseInt(objData[0]);
            int mes = Integer.parseInt(retornarMes(objData[1]));
            int ano = Integer.parseInt(objData[2]);

            return LocalDate.of(ano, mes, dia);
        } catch (Exception e) {
            LOG.error("::: Erro ao obter DATA_VENCIMENTO_7 :::");
        }

        try {
            String tagIni = "VALIDADE ATÉ :";
            String tagFim = "A DIRETORIA DE VIGILÂNCIA";
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(tagFim);
            String dataStr = txt.substring(ini, fim)
                    .replace(tagIni, "")
                    .replace(" ", "")
                    .trim();
            String[] objData = dataStr.split(SEPARADOR_COLCHETE_BARRA, -1);
            int dia = Integer.parseInt(objData[0]);
            int mes = Integer.parseInt(retornarMes(objData[1]));
            int ano = Integer.parseInt(objData[2]);

            return LocalDate.of(ano, mes, dia);
        } catch (Exception e) {
            LOG.error("::: Erro ao obter DATA_VENCIMENTO_8 :::");
        }

        return null;
    }
}
