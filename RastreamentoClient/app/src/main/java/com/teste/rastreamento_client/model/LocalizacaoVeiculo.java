package com.teste.rastreamento_client.model;

import android.support.v4.os.LocaleListCompat;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.Map;
import java.util.Set;

/**
 * Created by Paulo on 18/11/2017.
 */

public class LocalizacaoVeiculo {
    private long id;

    private Veiculo veiculo;

    private double latitude;
    private double longitude;

    private Date datahora;

    public LocalizacaoVeiculo(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Veiculo getVeiculo() {
        return veiculo;
    }

    public void setVeiculo(Veiculo veiculo) {
        this.veiculo = veiculo;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Date getDatahora() {
        return this.datahora;//new Date(data);
    }

    public void setDatahora(Date datahora) {
        this.datahora = datahora; //.getTime();
    }

    public LatLng getPonto(){
        return new LatLng(this.latitude, this.longitude);
    }
}
