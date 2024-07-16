package br.com.alvara.extrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class CnpjExtrator {

    private static final Logger LOG = LoggerFactory.getLogger(CnpjExtrator.class);
    private static final String INDEX_CNPJ = "CNPJ:";
    private static final String TAG_ATIVIDADE = "ATIVIDADE(S)";
    private static final String TAG_EMPREENDIMENTOSCPF = "EMPREENDIMENTOSCPF/CNPJ";

    private static final int TAMANHO_CNPJ = 14;

    public static String retornarCnpjEmpresa(String txt) {
        System.out.println();
        try {
            int ini = txt.indexOf(INDEX_CNPJ);
            int fim = txt.indexOf(TAG_ATIVIDADE);
            String cnpj = txt.substring(ini, fim).replace(INDEX_CNPJ, "")
                    .replace(".", "")
                    .replace("/", "")
                    .replace("-", "")
                    .trim();
            if (cnpj.length() == TAMANHO_CNPJ) {
                return cnpj;
            }
        } catch (Exception e) {
            LOG.error("::: Erro ao obter CNPJ_1 :::");
        }

        try {
            String tagFim = "ENDEREÇO:";
            int ini = txt.indexOf(INDEX_CNPJ);
            int fim = txt.indexOf(tagFim);
            String cnpj = txt.substring(ini, fim).replace(INDEX_CNPJ, "")
                    .replace(".", "")
                    .replace("/", "")
                    .replace("-", "").trim();
            if (cnpj.length() == TAMANHO_CNPJ) {
                return cnpj;
            }
        } catch (Exception e) {
            LOG.error("::: Erro ao obter CNPJ_3 :::");
        }

        try {
            String tagIni = "INSCRITA NO CNPJ N° ";
            String tagFim = ", NAS CONDIÇÕES ABAIXO";
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(tagFim);
            String cnpj = txt.substring(ini, fim).replace(tagIni, "")
                    .replace(".", "")
                    .replace("/", "")
                    .replace("-", "").trim();
            if (cnpj.length() == 14) {
                return cnpj;
            }
        } catch (Exception e) {
            LOG.error("::: Erro ao obter CNPJ_4 :::");
        }

        try {
            int ini = txt.indexOf(INDEX_CNPJ) + 5;
            String cnpj = txt.substring(ini, ini + 19).replace(INDEX_CNPJ, "")
                    .replace(".", "")
                    .replace("/", "")
                    .replace("-", "").trim();
            if (cnpj.length() == TAMANHO_CNPJ) {
                return cnpj;
            }
        } catch (Exception e) {
            LOG.error("::: Erro ao obter CNPJ_5 :::");
        }

        try {
            String tagFim = "INSCRIÇÃO MUNICIPAL";
            int ini = txt.indexOf(TAG_EMPREENDIMENTOSCPF);
            int fim = txt.indexOf(tagFim);
            String cnpj = txt.substring(ini, fim).replace(TAG_EMPREENDIMENTOSCPF, "")
                    .trim();
            if (cnpj.length() == TAMANHO_CNPJ) {
                return cnpj;
            }
        } catch (Exception e) {
            LOG.error("::: Erro ao obter CNPJ_2 :::");
        }

        return "00000000000000";
    }
}
