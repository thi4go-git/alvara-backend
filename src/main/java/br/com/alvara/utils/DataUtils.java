package br.com.alvara.utils;

import org.springframework.stereotype.Component;

@Component
public abstract class DataUtils {
    private DataUtils() {
    }


    public static String retornarMes(String mes) {
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
