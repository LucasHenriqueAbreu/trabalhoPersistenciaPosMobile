package com.teste.rastreamento_client.model;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paulo on 23/11/2017.
 */

public class Rota {

    private Long id;
    private Long idveiculo;

    private String descricao;
    private String destino;

    private List<LatLng> pontos;
    private int cor;

    public Rota(){
        this.pontos = new ArrayList<LatLng>();
    }

    public Rota(Long id, Long idveiculo, String descricao, String destino, List<LatLng> pontos){
        this.id = id;
        this.idveiculo = idveiculo;
        this.descricao = descricao;
        this.destino = destino;
        this.pontos = pontos;
    }

    public void addPonto(LatLng ponto){
        this.pontos.add(ponto);
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPontos(List<LatLng> pontos){
        this.pontos = pontos;
    }

    public List<LatLng> getPontos(){
        return this.pontos;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public Long getIdveiculo() {
        return idveiculo;
    }

    public void setIdveiculo(Long idveiculo) {
        this.idveiculo = idveiculo;
    }

    public int getCor() {
        return cor;
    }

    public void setCor(int cor) {
        this.cor = cor;
    }
}
