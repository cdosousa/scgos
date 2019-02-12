/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcs.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created in 29/09/2017
 */
public class GrupoProdutos {

    private String cdGrupo;
    private String nomeGrupo;
    private String gerarCodigo;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;

    // contrututor padrão da classe
    public GrupoProdutos(){
        
    }
    // construtor sobrecarregado da classe
    public GrupoProdutos(String cdGrupo, String nomeGrupo, String gerarCodigo, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao) {
        setCdGrupo(cdGrupo);
        setNomeGrupo(nomeGrupo);
        setGerarCodigo(gerarCodigo);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdGrupo
     */
    public String getCdGrupo() {
        return cdGrupo;
    }

    /**
     * @param cdGrupo the cdGrupo to set
     */
    public void setCdGrupo(String cdGrupo) {
        this.cdGrupo = cdGrupo;
    }

    /**
     * @return the nomeGrupo
     */
    public String getNomeGrupo() {
        return nomeGrupo;
    }

    /**
     * @param nomeGrupo the nomeGrupo to set
     */
    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }

    /**
     * @return the gerarCodigo
     */
    public String getGerarCodigo() {
        return gerarCodigo;
    }

    /**
     * @param gerarCodigo the geraGrupo to set
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
