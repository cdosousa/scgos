/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 04/12/2017
 */
public class TipoVerniz {
    private String cdTipoVerniz;
    private String nomeTipoVerniz;
    private double valor;
    private String descricaoComercial;
    private String usuarioCadastro;
    private String dataCadastro;
    private String usuarioModificacao;
    private String dataModificacao;
    private String situacao;
    
    private boolean atualizacao;
    
    // construtor padrao da classe
    public TipoVerniz(){
        
    }
    
    // construtor padrão sobrecarregado da classe
    public TipoVerniz(String cdTipoVerniz, String nomeTipoVerniz, double valor, String descricaoComercial, String usuarioCadastro, String dataCadastro, 
            String usuarioModificacao, String dataModificacao, String situacao){
        setCdTipoVerniz(cdTipoVerniz);
        setNomeTipoVerniz(nomeTipoVerniz);
        setValor(valor);
        setDescricaoComercial(descricaoComercial);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdTipoVerniz
     */
    public String getCdTipoVerniz() {
        return cdTipoVerniz;
    }

    /**
     * @param cdTipoVerniz the cdTipoVerniz to set
     */
    public void setCdTipoVerniz(String cdTipoVerniz) {
        this.cdTipoVerniz = cdTipoVerniz;
    }

    /**
     * @return the nomeTipoVerniz
     */
    public String getNomeTipoVerniz() {
        return nomeTipoVerniz;
    }

    /**
     * @param nomeTipoVerniz the nomeTipoVerniz to set
     */
    public void setNomeTipoVerniz(String nomeTipoVerniz) {
        this.nomeTipoVerniz = nomeTipoVerniz;
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
     * @return the usuarioModificacao
     */
    public String getUsuarioModificacao() {
        return usuarioModificacao;
    }

    /**
     * @param usuarioModificacao the usuarioModificacao to set
     */
    public void setUsuarioModificacao(String usuarioModificacao) {
        this.usuarioModificacao = usuarioModificacao;
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
     * @return the atualizacao
     */
    public boolean isAtualizacao() {
        return atualizacao;
    }

    /**
     * @param atualizacao the atualizacao to set
     */
    public void setAtualizacao(boolean atualizacao) {
        this.atualizacao = atualizacao;
    }

    /**
     * @return the valor
     */
    public double getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(double valor) {
        this.valor = valor;
    }

    /**
     * @return the descricaoComercial
     */
    public String getDescricaoComercial() {
        return descricaoComercial;
    }

    /**
     * @param descricaoComercial the descricaoComercial to set
     */
    public void setDescricaoComercial(String descricaoComercial) {
        this.descricaoComercial = descricaoComercial;
    }
}
