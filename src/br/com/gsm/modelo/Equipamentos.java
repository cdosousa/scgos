/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.modelo;

/**
 *
 * @author cristiano
 * @version 0.01beta_0917
 * created in 22/09/2017
 */
public class Equipamentos {
    private String cdEquipamento;
    private String nomeEquipamento;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;
    
    // constrututor padrão da classe
    public Equipamentos(){
        
    }
    // construtor com parametros
    public Equipamentos(String cdEquipamento, String nomeEquipamento, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdEquipamento(cdEquipamento);
        setNomeEquipamento(nomeEquipamento);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdEquipamento
     */
    public String getCdEquipamento() {
        return cdEquipamento;
    }

    /**
     * @param cdEquipamento the cdEquipamento to set
     */
    public void setCdEquipamento(String cdEquipamento) {
        this.cdEquipamento = cdEquipamento;
    }

    /**
     * @return the nomeEquipamento
     */
    public String getNomeEquipamento() {
        return nomeEquipamento;
    }

    /**
     * @param nomeEquipamento the nomeEquipamento to set
     */
    public void setNomeEquipamento(String nomeEquipamento) {
        this.nomeEquipamento = nomeEquipamento;
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
