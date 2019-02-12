/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 10/11/2017
 */
public class ParcelasCondicaoPagamento extends CondicaoPagamento{
    private int cdParcela;
    private int prazoDias;
    private double percRateio;
    
    // construtor padrão da classe
    public ParcelasCondicaoPagamento(){
        super();
    }
    
    // construto padrão da classe sobrecarregado
    public ParcelasCondicaoPagamento(String cdCondPag, int cdParcela, int prazoDias, double percRateio,
            String usuarioCadastro, String dataCasdastro, String dataModificacao, String situacao){
        setCdCondpag(cdCondPag);
        setCdParcela(cdParcela);
        setPrazoDias(prazoDias);
        setPercRateio(percRateio);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCasdastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdParcela
     */
    public int getCdParcela() {
        return cdParcela;
    }

    /**
     * @param cdParcela the cdParcela to set
     */
    public void setCdParcela(int cdParcela) {
        this.cdParcela = cdParcela;
    }

    /**
     * @return the prazoDias
     */
    public int getPrazoDias() {
        return prazoDias;
    }

    /**
     * @param prazoDias the prazoDias to set
     */
    public void setPrazoDias(int prazoDias) {
        this.prazoDias = prazoDias;
    }

    /**
     * @return the percRateio
     */
    public double getPercRateio() {
        return percRateio;
    }

    /**
     * @param percRateio the percRateio to set
     */
    public void setPercRateio(double percRateio) {
        this.percRateio = percRateio;
    }
}