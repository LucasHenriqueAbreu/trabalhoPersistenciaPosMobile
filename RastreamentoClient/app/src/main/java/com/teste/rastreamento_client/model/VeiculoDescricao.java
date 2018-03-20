package com.teste.rastreamento_client.model;

/**
 * Created by Paulo on 30/11/2017.
 */

public class VeiculoDescricao {
    private Long id;
    private Long id_rota;
    private Long id_veiculo;
    private Long status;

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    public Long getId_veiculo() {
        return id_veiculo;
    }

    public void setId_veiculo(Long id_veiculo) {
        this.id_veiculo = id_veiculo;
    }

    public Long getId_rota() {
        return id_rota;
    }

    public void setId_rota(Long id_rota) {
        this.id_rota = id_rota;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
