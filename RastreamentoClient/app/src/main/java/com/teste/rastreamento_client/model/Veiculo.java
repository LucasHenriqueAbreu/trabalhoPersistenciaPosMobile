package com.teste.rastreamento_client.model;

/**
 * Created by Paulo on 18/11/2017.
 */

public class Veiculo {
    private long id;
    private String cod;

    public Veiculo(){}

    public Veiculo(Long id, String cod){
        this.id = id;
        this.cod = cod;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }
}
