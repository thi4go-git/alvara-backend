package br.com.alvara.extrator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class NumeroDocumentoExtrator {
    private static final Logger LOG = LoggerFactory.getLogger(NumeroDocumentoExtrator.class);
    private static final String TAG_NUM_ESTABALEC_EMPRESARIAL = "ESTABELECIMENTOEMPRESARIALNÚMERO";
    private static final String TAG_NUM_LICENCA = "Nº DA LICENÇA:";
    private static final String TAG_ESTABELECIMENTO_NUM = "ESTABELECIMENTO.NÚMERO";
    private static final String TAG_CERTIFICADO_PROTOCOLO = "CERTIFICADAPROTOCOLO:";
    private static final String TAG_ALVARA_FUNC2 = "ALVARÁ DE LOCALIZAÇÃO E FUNCIONAMENTO Nº.";
    private static final String INDEX_CNPJ = "CNPJ:";

    public static String retornarNumeroDocumento(final String txt) {
        try {
            String tagIni = TAG_NUM_LICENCA;
            String tagFim = "CCP:";
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(tagFim);
            return txt.substring(ini, fim).replace(tagIni, "").trim();
        } catch (Exception e) {
            LOG.error("::: Erro ao obter NUMERO_DOCUMENTO_1 :::");
        }

        try {
            String tagIni = TAG_NUM_ESTABALEC_EMPRESARIAL;
            int ini = txt.indexOf(tagIni);
            String numero = txt.substring(ini).replace(tagIni, "").trim();
            return refatoraNumero(numero);
        } catch (Exception e) {
            LOG.error("::: Erro ao obter NUMERO_DOCUMENTO_2 :::");
        }

        try {
            String tagIni = TAG_ESTABELECIMENTO_NUM;
            int ini = txt.indexOf(tagIni);
            String numero = txt.substring(ini).replace(tagIni, "").trim();
            return refatoraNumero(numero);
        } catch (Exception e) {
            LOG.error("::: Erro ao obter NUMERO_DOCUMENTO_3 :::");
        }

        try {
            String tagIni = TAG_CERTIFICADO_PROTOCOLO;
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(INDEX_CNPJ);
            String numero = txt.substring(ini, fim).replace(tagIni, "").trim();
            return refatoraNumero(numero);
        } catch (Exception e) {
            LOG.error("::: Erro ao obter NUMERO_DOCUMENTO_4 :::");
        }

        try {
            String tagIni = TAG_ALVARA_FUNC2;
            String tagFim = "VALIDADE ATÉ:";
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(tagFim);
            return txt.substring(ini, fim).replace(tagIni, "").trim();
        } catch (Exception e) {
            LOG.error("::: Erro ao obter NUMERO_DOCUMENTO_5 :::");
        }

        try {
            String tagIni = "Nº DO PROTOCOLO: ";
            String tagFim = "VALIDADE:";
            int ini = txt.indexOf(tagIni);
            int fim = txt.indexOf(tagFim);
            return txt.substring(ini, fim).replace(tagIni, "").trim();
        } catch (Exception e) {
            LOG.error("::: Erro ao obter NUMERO_DOCUMENTO_6 :::");
        }

        return "Não Localizado!";
    }


    private static String refatoraNumero(String numero) {
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
}
