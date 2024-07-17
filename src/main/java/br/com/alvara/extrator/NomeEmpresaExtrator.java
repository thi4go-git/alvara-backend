package br.com.alvara.extrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class NomeEmpresaExtrator {
    private static final Logger LOG = LoggerFactory.getLogger(NomeEmpresaExtrator.class);
    private static final String INDEX_CNPJ = "CNPJ:";
    private static final String TAG_NOME_EMPRESA = "NOME DA EMPRESA: ";
    private static final String TAG_RAZAO_SOCIAL = "RAZÃO SOCIAL: ";
    private static final String TAG_LIC_AMBIENTAL = "CONCEDE A LICENÇA AMBIENTAL DECLARATÓRIA A";
    private static final String TAG_RISCO_NOME = "DE RISCONOME FANTASIA";

    public static String retornarNomeEmpresa(final String txt) {
        try {
            String tagIni = TAG_NOME_EMPRESA;
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(INDEX_CNPJ);
            return txt.substring(ini, fim).replace(tagIni, "").trim();
        } catch (Exception e) {
            LOG.error("::: Erro ao obter NOME_EMPRESA_1 :::");
        }

        try {
            String tagIni = TAG_RAZAO_SOCIAL;
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(INDEX_CNPJ);
            return txt.substring(ini, fim).replace(tagIni, "");
        } catch (Exception e) {
            LOG.error("::: Erro ao obter NOME_EMPRESA_2 :::");
        }

        try {
            String tagIni = TAG_LIC_AMBIENTAL;
            String tagFim = ", INSCRITA NO CNPJ";
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(tagFim);
            return txt.substring(ini, fim).replace(tagIni
                    , "");
        } catch (Exception e) {
            LOG.error("::: Erro ao obter NOME_EMPRESA_3 :::");
        }

        try {
            String tagIni = TAG_RISCO_NOME;
            String tagFim = "FONE(";
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(tagFim);
            return txt.substring(ini, fim).replace(tagIni
                    , "");
        } catch (Exception e) {
            LOG.error("::: Erro ao obter NOME_EMPRESA_4 :::");
        }

        try {
            String tagFim = "22,";
            int ini = txt.indexOf(INDEX_CNPJ) + 24;
            int fim = txt.indexOf(tagFim);
            return txt.substring(ini, fim)
                    .trim();
        } catch (Exception e) {
            LOG.error("::: Erro ao obter NOME_EMPRESA_5 :::");
        }

        try {
            String tagIni = "87.RAZÃOSOCIAL ";
            String tagFim = "NOMEFANTASIA";
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(tagFim);
            return txt.substring(ini, fim)
                    .replace(tagIni, "")
                    .trim();
        } catch (Exception e) {
            LOG.error("::: Erro ao obter NOME_EMPRESA_6 :::");
        }

        try {
            String tagIni = "CONCEDE O PRESENTE LICENCIAMENTO À:";
            String tagFim = "SECRETARIA MUNICIPAL DE SAÚDEDIRETORIA";
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(tagFim);
            return txt.substring(ini, fim)
                    .replace(tagIni, "")
                    .trim();
        } catch (Exception e) {
            LOG.error("::: Erro ao obter NOME_EMPRESA_7 :::");
        }

        return "Não Localizado!";
    }
}
