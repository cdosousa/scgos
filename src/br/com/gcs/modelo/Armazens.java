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
public class Armazens {
    private String cdArmazem;
    private String nomeArmazem;
    private String tipoArmazem;
    private String permiteSeparacao;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;
    
    // construtor padrão
    public Armazens(){
        
    }
    // construtor sobrecarregado
    public Armazens(String cdArmazem, String nomeArmazem, String tipoArmazem, String permiteSeparacao, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdArmazem(cdArmazem);
        setNomeArmazem(nomeArmazem);
        setTipoArmazem(tipoArmazem);
        setPermiteSeparacao(permiteSeparacao);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdArmazem
     */
    public String getCdArmazem() {
        return cdArmazem;
    }

    /**
     * @param cdArmazem the cdArmazem to set
     */
    public void setCdArmazem(String cdArmazem) {
        this.cdArmazem = cdArmazem;
    }

    /**
     * @return the nomeArmazem
     */
    public String getNomeArmazem() {
        return nomeArmazem;
    }

    /**
     * @param nomeArmazem the nomeArmazem to set
     */
    public void setNomeArmazem(String nomeArmazem) {
        this.nomeArmazem = nomeArmazem;
    }

    /**
     * @return the tipoArmazem
     */
    public String getTipoArmazem() {
        return tipoArmazem;
    }

    /**
     * @param tipoArmazem the tipoArmazem to set
     */
    public void setTipoArmazem(String tipoArmazem) {
        this.tipoArmazem = tipoArmazem;
    }

    /**
     * @return the permiteSeparacao
     */
    public String getPermiteSeparacao() {
        return permiteSeparacao;
    }

    /**
     * @param permiteSeparacao the permiteSeparacao to set
     */
    public void setPermiteSeparacao(String permiteSeparacao) {
        this.permiteSeparacao = permiteSeparacao;
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
