/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917
 * created on 02/10/2017
 */
public class ContasContabeis {
    private String cdConta;
    private String nomeConta;
    private String tipoConta;
    private String contaPatrimonial;
    private String grandeMovimentacao;
    private String estabelecimento;
    private String reduzido;
    private String centroResultado;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;
    
    // contrutor padrão do módulo
    public ContasContabeis(){
        
    }
    // construtor sobrecarregado do módulo
    public ContasContabeis(String cdConta, String nomeConta, String tipoConta, String contaPatrimonial, String grandeMovimentacao, String estabelecimento, String reduzido, String centroResultado, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdConta(cdConta);
        setNomeConta(nomeConta);
        setTipoConta(tipoConta);
        setContaPatrimonial(contaPatrimonial);
        setGrandeMovimentacao(grandeMovimentacao);
        setEstabelecimento(estabelecimento);
        setReduzido(reduzido);
        setCentroResultado(centroResultado);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdConta
     */
    public String getCdConta() {
        return cdConta;
    }

    /**
     * @param cdConta the cdConta to set
     */
    public void setCdConta(String cdConta) {
        this.cdConta = cdConta;
    }

    /**
     * @return the nomeConta
     */
    public String getNomeConta() {
        return nomeConta;
    }

    /**
     * @param nomeConta the nomeConta to set
     */
    public void setNomeConta(String nomeConta) {
        this.nomeConta = nomeConta;
    }

    /**
     * @return the tipoConta
     */
    public String getTipoConta() {
        return tipoConta;
    }

    /**
     * @param tipoConta the tipoConta to set
     */
    public void setTipoConta(String tipoConta) {
        this.tipoConta = tipoConta;
    }

    /**
     * @return the contaPatrimonial
     */
    public String getContaPatrimonial() {
        return contaPatrimonial;
    }

    /**
     * @param contaPatrimonial the contaPatrimonial to set
     */
    public void setContaPatrimonial(String contaPatrimonial) {
        this.contaPatrimonial = contaPatrimonial;
    }

    /**
     * @return the grandeMovimentacao
     */
    public String getGrandeMovimentacao() {
        return grandeMovimentacao;
    }

    /**
     * @param grandeMovimentacao the grandeMovimentacao to set
     */
    public void setGrandeMovimentacao(String grandeMovimentacao) {
        this.grandeMovimentacao = grandeMovimentacao;
    }

    /**
     * @return the estabelecimento
     */
    public String getEstabelecimento() {
        return estabelecimento;
    }

    /**
     * @param estabelecimento the estabelecimento to set
     */
    public void setEstabelecimento(String estabelecimento) {
        this.estabelecimento = estabelecimento;
    }

    /**
     * @return the reduzido
     */
    public String getReduzido() {
        return reduzido;
    }

    /**
     * @param reduzido the reduzido to set
     */
    public void setReduzido(String reduzido) {
        this.reduzido = reduzido;
    }

    /**
     * @return the centroResultado
     */
    public String getCentroResultado() {
        return centroResultado;
    }

    /**
     * @param centroResultado the centroResultado to set
     */
    public void setCentroResultado(String centroResultado) {
        this.centroResultado = centroResultado;
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
