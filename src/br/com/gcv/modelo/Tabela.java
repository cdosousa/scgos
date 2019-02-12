/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 21/11/2017
 */
public class Tabela {
    private String cdTabela;
    private String nomeTabela;
    private String dataVigencia;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;
    private boolean atualizacao;
    
    // Construto padrão da classe
    public Tabela(){
        
    }
    // Construto padrão sobrecarregado
    public Tabela(String cdTabela, String nomeTabela, String dataVigencia, String usuarioCadastro, 
            String dataCadastro, String dataModificacao, String situacao){
        setCdTabela(cdTabela);
        setNomeTabela(nomeTabela);
        setDataVigencia(dataVigencia);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdTabela
     */
    public String getCdTabela() {
        return cdTabela;
    }

    /**
     * @param cdTabela the cdTabela to set
     */
    public void setCdTabela(String cdTabela) {
        this.cdTabela = cdTabela;
    }

    /**
     * @return the nomeTabela
     */
    public String getNomeTabela() {
        return nomeTabela;
    }

    /**
     * @param nomeTabela the nomeTabela to set
     */
    public void setNomeTabela(String nomeTabela) {
        this.nomeTabela = nomeTabela;
    }

    /**
     * @return the dataVigencia
     */
    public String getDataVigencia() {
        return dataVigencia;
    }

    /**
     * @param dataVigencia the dataVigencia to set
     */
    public void setDataVigencia(String dataVigencia) {
        this.dataVigencia = dataVigencia;
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
}
