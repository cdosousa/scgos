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
public class Categorias {
    private String cdCategoria;
    private String nomeCategoria;
    private String usuarioCadastro;
    private String gerarCodigo;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;
    
    // construtor padrão da classe
    public Categorias(){
        
    }
    // construtor sobrecarregado da classe
    public Categorias(String cdCategoria, String nomeCategoria, String gerarCodigo, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdCategoria(cdCategoria);
        setNomeCategoria(nomeCategoria);
        setGerarCodigo(gerarCodigo);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdCategoria
     */
    public String getCdCategoria() {
        return cdCategoria;
    }

    /**
     * @param cdCategoria the cdCategoria to set
     */
    public void setCdCategoria(String cdCategoria) {
        this.cdCategoria = cdCategoria;
    }

    /**
     * @return the nomeCategoria
     */
    public String getNomeCategoria() {
        return nomeCategoria;
    }

    /**
     * @param nomeCategoria the nomeCategoria to set
     */
    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }
    
    /**
     * @return the gerarCodigo
     */
    public String getGerarCodigo() {
        return gerarCodigo;
    }

    /**
     * @param gerarCodigo the gerarCodigo to set
     */
    public void setGerarCodigo(String gerarCodigo) {
        this.gerarCodigo = gerarCodigo;
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
