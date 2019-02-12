/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcs.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917
 * created on 29/09/2017
 */
public class ClasseProdutos {
    private String cdClasse;
    private String nomeClasse;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;
    
    // construtor padrão
    public ClasseProdutos(){
        
    }
    
    // construtor sobrecarregado
    public ClasseProdutos(String cdClasse, String nomeClasse, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdClasse(cdClasse);
        setNomeClasse(nomeClasse);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdClasse
     */
    public String getCdClasse() {
        return cdClasse;
    }

    /**
     * @param cdClasse the cdClasse to set
     */
    public void setCdClasse(String cdClasse) {
        this.cdClasse = cdClasse;
    }

    /**
     * @return the nomeClasse
     */
    public String getNomeClasse() {
        return nomeClasse;
    }

    /**
     * @param nomeClasse the nomeClasse to set
     */
    public void setNomeClasse(String nomeClasse) {
        this.nomeClasse = nomeClasse;
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