/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01beta_0917
 * created in 22/09/2017
 */
public class Tarefas {
    private String cdTarefa;
    private String nomeTarefa;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;
    
    // construtor padrão da classe
    public Tarefas(){
        
    }
    
    // construtor sobrecarregado da classe
    public Tarefas(String cdTarefa, String nomeTarefa, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdTarefa(cdTarefa);
        setNomeTarefa(nomeTarefa);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdTarefa
     */
    public String getCdTarefa() {
        return cdTarefa;
    }

    /**
     * @param cdTarefa the cdTarefa to set
     */
    public void setCdTarefa(String cdTarefa) {
        this.cdTarefa = cdTarefa;
    }

    /**
     * @return the nomeTarefa
     */
    public String getNomeTarefa() {
        return nomeTarefa;
    }

    /**
     * @param nomeTarefa the nomeTarefa to set
     */
    public void setNomeTarefa(String nomeTarefa) {
        this.nomeTarefa = nomeTarefa;
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
     * @param dataModificacao the dataModificao to set
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
