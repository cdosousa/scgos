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
 * created on 02/10/2017
 */
public class OrigemProduto {
    private String cdOrigem;
    private String nomeOrigem;
    private String finalidade;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;
    
    // construtor padrão da classe
    public OrigemProduto(){
        
    }
    
    // construtor sobrecarregado da classe
    public OrigemProduto(String cdOrigem, String nomeOrigem, String finalidade, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdOrigem(cdOrigem);
        setNomeOrigem(nomeOrigem);
        setFinalidade(finalidade);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdOrigem
     */
    public String getCdOrigem() {
        return cdOrigem;
    }

    /**
     * @param cdOrigem the cdOrigem to set
     */
    public void setCdOrigem(String cdOrigem) {
        this.cdOrigem = cdOrigem;
    }

    /**
     * @return the nomeOrigem
     */
    public String getNomeOrigem() {
        return nomeOrigem;
    }

    /**
     * @param nomeOrigem the nomeOrigem to set
     */
    public void setNomeOrigem(String nomeOrigem) {
        this.nomeOrigem = nomeOrigem;
    }

    /**
     * @return the finalidade
     */
    public String getFinalidade() {
        return finalidade;
    }

    /**
     * @param finalidade the finalidade to set
     */
    public void setFinalidade(String finalidade) {
        this.finalidade = finalidade;
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
