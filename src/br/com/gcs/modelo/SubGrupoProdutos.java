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
 * created on 02/10/2017
 */
public class SubGrupoProdutos {
    private String cdSubGrupo;
    private String cdGrupo;
    private String nomeSubGrupo;
    private String gerarCodigo;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;
    
    // construtor padrao da classe
    public SubGrupoProdutos(){
        
    }
    
    // construto padrão sobrecarregado
    public SubGrupoProdutos(String cdSubGrupo, String cdGrupo, String nomeSubGrupo, String gerarCodigo, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdSubGrupo(cdSubGrupo);
        setCdGrupo(cdGrupo);
        setNomeSubGrupo(nomeSubGrupo);
        setGerarCodigo(gerarCodigo);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdSubGrupo
     */
    public String getCdSubGrupo() {
        return cdSubGrupo;
    }

    /**
     * @param cdSubGrupo the cdSubGrupo to set
     */
    public void setCdSubGrupo(String cdSubGrupo) {
        this.cdSubGrupo = cdSubGrupo;
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
     * @return the nomeSubGrupo
     */
    public String getNomeSubGrupo() {
        return nomeSubGrupo;
    }

    /**
     * @param nomeGrupo the nomeSubGrupo to set
     */
    public void setNomeSubGrupo(String nomeSubGrupo) {
        this.nomeSubGrupo = nomeSubGrupo;
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