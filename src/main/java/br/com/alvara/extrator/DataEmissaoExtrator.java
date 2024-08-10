package br.com.alvara.extrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;


import static br.com.alvara.utils.DataUtils.retornarMes;

public abstract class DataEmissaoExtrator {

    private DataEmissaoExtrator() {
    }

    private static final Logger LOG = LoggerFactory.getLogger(DataEmissaoExtrator.class);
    private static final String SEPARADOR_COLCHETE_BARRA = "/";
    private static final String SEPARADOR_COLCHETE_VIRGULA = ",";
    private static final String TAG_DE = "DE";
    private static final String DOCUMENTO_EMITIDO_EM = "ESTE DOCUMENTO FOI EMITIDO EM ";
    private static final String DEZEMBRO = "DEZEMBRO";
    private static final String SE_IMPRESSO = "SE IMPRESSO";
    private static final String BARRA_ZEMBRO = "/ZEMBRO"; //.OBSERVAÇÕES:
    private static final String OBSERVACOES = ".OBSERVAÇÕES:";

    public static LocalDate retornarDataEmissao(final String txt) {
        try {
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
        } catch (Exception e) {
            LOG.error("::: Erro ao obter DATA_EMISSAO_1 :::");
        }

        try {
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
        } catch (Exception e) {
            LOG.error("::: Erro ao obter DATA_EMISSAO_2 :::");
        }

        try {
            String tagIni = DOCUMENTO_EMITIDO_EM;
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(SE_IMPRESSO);
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
            LOG.error("::: Erro ao obter DATA_EMISSAO_3 :::");
        }

        try {
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
        } catch (Exception e) {
            LOG.error("::: Erro ao obter DATA_EMISSAO_4 :::");
        }

        try {
            String tagIni = "EMISSÃO: ";
            int ini = txt.indexOf(tagIni) + 9;
            String dataStr = txt.substring(ini, ini + 10)
                    .replace(tagIni, "")
                    .replace(" ", "")
                    .trim();
            String[] objData = dataStr.split(SEPARADOR_COLCHETE_BARRA, -1);
            int dia = Integer.parseInt(objData[0]);
            int mes = Integer.parseInt(retornarMes(objData[1]));
            int ano = Integer.parseInt(objData[2]);
            return LocalDate.of(ano, mes, dia);
        } catch (Exception e) {
            LOG.error("::: Erro ao obter DATA_EMISSAO_5 :::");
        }

        try {
            String tagFim = "SERVIÇO PROFISSIONAL";
            int fim = txt.indexOf(tagFim);
            String dataStr = txt.substring(fim - 10, fim)
                    .replace(" ", "")
                    .trim();
            String[] objData = dataStr.split(SEPARADOR_COLCHETE_BARRA, -1);
            int dia = Integer.parseInt(objData[0]);
            int mes = Integer.parseInt(retornarMes(objData[1]));
            int ano = Integer.parseInt(objData[2]);
            return LocalDate.of(ano, mes, dia);
        } catch (Exception e) {
            LOG.error("::: Erro ao obter DATA_EMISSAO_6 :::");
        }

        try {
            String tagIni = DOCUMENTO_EMITIDO_EM;
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(SE_IMPRESSO);
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
            LOG.error("::: Erro ao obter DATA_EMISSAO_7 :::");
        }

        try {
            String tagIni = "LOCAL E DATA: APARECIDA DE GOIÂNIA, ";
            String tagFim = "VALIDADE: ";
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
            LOG.error("::: Erro ao obter DATA_EMISSAO_8 :::");
        }

        try {
            String tagIni = "TECNICO:APARECIDA DE GOIÂNIA, ";
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(OBSERVACOES);
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
            LOG.error("::: Erro ao obter DATA_EMISSAO_9 :::");
        }

        try {
            String tagIni = ".APARECIDA DE GOIÂNIA, ";
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(OBSERVACOES);
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
            LOG.error("::: Erro ao obter DATA_EMISSAO_10 :::");
        }

        try {
            String tagIni = "CONSULTAS:64-34546650";
            String tagFim = "COMERCIAL C-2";
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
            LOG.error("::: Erro ao obter DATA_EMISSAO_11 :::");
        }

        try {
            String tagIni = "EXERCÍCIO DE 2024.GOIÂNIA,";
            String tagFim = ".OBSERVAÇÕESESTE";
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
            LOG.error("::: Erro ao obter DATA_EMISSAO_12 :::");
        }

        return null;
    }
}
