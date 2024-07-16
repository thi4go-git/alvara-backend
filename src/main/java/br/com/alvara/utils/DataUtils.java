package br.com.alvara.utils;

import org.springframework.stereotype.Component;

@Component
public abstract class DataUtils {
    private DataUtils() {
    }


    public static String retornarMes(String mes) {
        switch (mes) {
            case "JANEIRO":
            case "01":
                return "01";
            case "FEVEREIRO":
            case "02":
                return "02";
            case "MARÇO":
            case "03":
                return "03";
            case "ABRIL":
            case "04":
                return "04";
            case "MAIO":
            case "05":
                return "05";
            case "JUNHO":
            case "06":
                return "06";
            case "JULHO":
            case "07":
                return "07";
            case "AGOSTO":
            case "08":
                return "08";
            case "SETEMBRO":
            case "09":
                return "09";
            case "OUTUBRO":
            case "10":
                return "10";
            case "NOVEMBRO":
            case "11":
                return "11";
            case "DEZEMBRO":
            case "12":
                return "12";
            default:
                return "00";
        }
    }
}
