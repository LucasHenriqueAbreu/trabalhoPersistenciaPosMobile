package com.teste.rastreamento_client.enums;

/**
 * Created by Paulo on 18/11/2017.
 */

public class Url {
    /**
     * Principal
     */
    private static final String base = "http://192.168.2.106:8080";

    /**
     * Setor
     */

    private static final String veiculo = base + "/veiculo";

    /**
     * Requisições
     */
    public static final String LOCALIZACAO = veiculo+"/local?id=";
}
