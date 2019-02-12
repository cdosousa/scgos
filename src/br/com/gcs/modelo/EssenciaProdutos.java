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
public class EssenciaProdutos {
    private String cdEssencia;
    private String nomeEssencia;
    private String gerarCodigo;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;
    
    // construtor padrão
    public EssenciaProdutos(){
        
    }
    // construtor sobrecarregado
    public EssenciaProdutos(String cdEssencia, String nomeEssencia, String gerarCodigo, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdEssencia(cdEssencia);
        setNomeEssencia(nomeEssencia);
        setGerarCodigo(gerarCodigo);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdEssencia
     */
    public String getCdEssencia() {
        return cdEssencia;
    }

    /**
     * @param cdEssencia the cdEssencia to set
     */
    public void setCdEssencia(String cdEssencia) {
        this.cdEssencia = cdEssencia;
    }

    /**
     * @return the nomeEssencia
     */
    public String getNomeEssencia() {
        return nomeEssencia;
    }

    /**
     * @param nomeEssencia the nomeEssencia to set
     */
    public void setNomeEssencia(String nomeEssencia) {
        this.nomeEssencia = nomeEssencia;
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