/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfr.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917
 * created on 06/10/2017
 */
public class NCMProduto {
    private String cdNcm;
    private String nomeNcm;
    private String cdOrigemCsta;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;
    
    // construtor padrao da classe
    public NCMProduto(){
        
    }
    
    // construtor padrão sobrecarregado
    public NCMProduto(String cdNcm, String nomeNcm, String cdOrigemCsta, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdNcm(cdNcm);
        setNomeNcm(nomeNcm);
        setCdOrigemCsta(cdOrigemCsta);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdNcm
     */
    public String getCdNcm() {
        return cdNcm;
    }

    /**
     * @param cdNcm the cdNcm to set
     */
    public void setCdNcm(String cdNcm) {
        this.cdNcm = cdNcm;
    }

    /**
     * @return the nomeNcm
     */
    public String getNomeNcm() {
        return nomeNcm;
    }

    /**
     * @param nomeNcm the nomeNcm to set
     */
    public void setNomeNcm(String nomeNcm) {
        this.nomeNcm = nomeNcm;
    }

    /**
     * @return the cdOrigemCsta
     */
    public String getCdOrigemCsta() {
        return cdOrigemCsta;
    }

    /**
     * @param cdOrigemCsta the cdOrigemCsta to set
     */
    public void setCdOrigemCsta(String cdOrigemCsta) {
        this.cdOrigemCsta = cdOrigemCsta;
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