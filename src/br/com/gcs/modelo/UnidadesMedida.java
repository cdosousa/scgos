/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcs.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917
 * created in 29/09/2017
 */
public class UnidadesMedida {
    private String cdUnidMedida;
    private String nomeUnidMedida;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;
    
    // constrututor padrão da classe
    public UnidadesMedida(){
        
    }

    public UnidadesMedida(String cdUnidMedida,String nomeUnidMedida, String usuarioCadastro, String dataCadastro,String dataModificacao, String situacao) {
        setCdUnidMedida(cdUnidMedida);
        setNomeUnidMedida(nomeUnidMedida);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdUnidMedida
     */
    public String getCdUnidMedida() {
        return cdUnidMedida;
    }

    /**
     * @param cdUnidMedida the cdUnidMedida to set
     */
    public void setCdUnidMedida(String cdUnidMedida) {
        this.cdUnidMedida = cdUnidMedida;
    }

    /**
     * @return the nomeUnidMedida
     */
    public String getNomeUnidMedida() {
        return nomeUnidMedida;
    }

    /**
     * @param nomeUnidMedida the nomeUnidMedida to set
     */
    public void setNomeUnidMedida(String nomeUnidMedida) {
        this.nomeUnidMedida = nomeUnidMedida;
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
