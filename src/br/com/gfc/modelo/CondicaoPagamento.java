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
public class CondicaoPagamento {
    private String cdCondpag;
    private String nomeCondPag;
    private int numParcelas;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;
    private boolean atualizar = false;
    
    // variáveis de controle;
    private double totalRateio;
    private double residuo;

    
    // construtor padrão da classe
    public CondicaoPagamento(){
        
    }
    
    // construtor padrão sobrecarregado da classe
    public CondicaoPagamento(String cdCondpag, String nomeCondPag, int numParcelas, String usuarioCadastro, 
            String dataCadastro, String dataModificacao, String situacao){
        setCdCondpag(cdCondpag);
        setNomeCondPag(nomeCondPag);
        setNumParcelas(numParcelas);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdCondpag
     */
    public String getCdCondpag() {
        return cdCondpag;
    }

    /**
     * @param cdCondpag the cdCondpag to set
     */
    public void setCdCondpag(String cdCondpag) {
        this.cdCondpag = cdCondpag;
    }

    /**
     * @return the nomeCondPag
     */
    public String getNomeCondPag() {
        return nomeCondPag;
    }

    /**
     * @param nomeCondPag the nomeCondPag to set
     */
    public void setNomeCondPag(String nomeCondPag) {
        this.nomeCondPag = nomeCondPag;
    }

    /**
     * @return the numParcelas
     */
    public int getNumParcelas() {
        return numParcelas;
    }

    /**
     * @param numParcelas the numParcelas to set
     */
    public void setNumParcelas(int numParcelas) {
        this.numParcelas = numParcelas;
    }

    /**
     * @return the usuarioCadastro
     */
    public String getUsuarioCadastro() {
        return usuarioCadastro;
    }

    /**
     * @param usuarioCadastro the usuarioCadastro to set
     */
    public void setUsuarioCadastro(String usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    /**
     * @return the dataCadastro
     */
    public String getDataCadastro() {
        return dataCadastro;
    }

    /**
     * @param dataCadastro the dataCadastro to set
     */
    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    /**
     * @return the dataModificacao
     */
    public String getDataModificacao() {
        return dataModificacao;
    }

    /**
     * @param dataModificacao the dataModificacao to set
     */
    public void setDataModificacao(String dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    /**
     * @return the situacao
     */
    public String getSituacao() {
        return situacao;
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    /**
     * @return the atualizar
     */
    public boolean isAtualizar() {
        return atualizar;
    }

    /**
     * @param atualizar the atualizar to set
     */
    public void setAtualizar(boolean atualizar) {
        this.atualizar = atualizar;
    }

    /**
     * @return the totalRateio
     */
    public double getTotalRateio() {
        return totalRateio;
    }

    /**
     * @param totalRateio the totalRateio to set
     */
    public void setTotalRateio(double totalRateio) {
        this.totalRateio = totalRateio;
    }

    /**
     * @return the residuo
     */
    public double getResiduo() {
        return residuo;
    }

    /**
     * @param residuo the residuo to set
     */
    public void setResiduo(double residuo) {
        this.residuo = residuo;
    }
}