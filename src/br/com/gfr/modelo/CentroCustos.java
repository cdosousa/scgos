/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfr.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917
 * created on 29/09/2017
 */
public class CentroCustos {
    private String cdCcusto;
    private String nomeCcusto;
    private float percRateio;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;
    
    // construtor padrao
    public CentroCustos(){
        
    }
    // construtor sobrecarregado
    public CentroCustos(String cdCcusto, String nomeCcusto, float percRateio, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdCcusto(cdCcusto);
        setNomeCcusto(nomeCcusto);
        setPercRateio(percRateio);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdCcusto
     */
    public String getCdCcusto() {
        return cdCcusto;
    }

    /**
     * @param cdCcusto the cdCcusto to set
     */
    public void setCdCcusto(String cdCcusto) {
        this.cdCcusto = cdCcusto;
    }

    /**
     * @return the nomeCcusto
     */
    public String getNomeCcusto() {
        return nomeCcusto;
    }

    /**
     * @param nomeCcusto the nomeCcusto to set
     */
    public void setNomeCcusto(String nomeCcusto) {
        this.nomeCcusto = nomeCcusto;
    }

    /**
     * @return the percRateio
     */
    public float getPercRateio() {
        return percRateio;
    }

    /**
     * @param percRateio the percRateio to set
     */
    public void setPercRateio(float percRateio) {
        this.percRateio = percRateio;
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
}
