/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_betal092017
 * created in 25/09/2017
 */
public class Especialidades {
    //variáveis de Instância
    private String cdEspecialidade;
    private String nomeEspecialidade;
    private double txProdutividade;
    private double valorUnit;
    private char pagarIndicacao;
    private char pagarObra;
    private char pagarComissao;
    private double percIndicacao;
    private double percObra;
    private double percComissao;
    private double valorIndicacao;
    private double valorObra;
    private double valorComissao;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private char situacao;
    
    // construtor padrão
    public Especialidades(){
        
    }
    // constrututor secundário
    public Especialidades(String cdEspecialidade, String nomeEspecialidade, double txProdutividade, double valorUnit, char pagarIndicacao, char pagarObra,
            char pagarComissao, double percIndicacao, double percObra, double percComissao, double valorIndicacao, double valorObra, double valorComissao, String usuarioCadastro, String dataCadastro, String dataModificacao, char situacao){
        setCdEspecialidade(cdEspecialidade);
        setNomeEspecialidade(nomeEspecialidade);
        setTxProdutividade(txProdutividade);
        setValorUnit(valorUnit);
        setPagarIndicacao(pagarIndicacao);
        setPagarObra(pagarObra);
        setPagarComissao(pagarComissao);
        setPercIndicacao(percIndicacao);
        setPercObra(percObra);
        setPercComissao(percComissao);
        setValorIndicacao(valorIndicacao);
        setValorObra(valorObra);
        setValorComissao(valorComissao);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }
    

    /**
     * @return the cdEspecialidade
     */
    public String getCdEspecialidade() {
        return cdEspecialidade;
    }

    /**
     * @param cdEspecialidade the cdEspecialidade to set
     */
    public void setCdEspecialidade(String cdEspecialidade) {
        this.cdEspecialidade = cdEspecialidade;
    }

    /**
     * @return the nomeEspecialidade
     */
    public String getNomeEspecialidade() {
        return nomeEspecialidade;
    }

    /**
     * @param nomeEspecialidade the nomeEspecialidade to set
     */
    public void setNomeEspecialidade(String nomeEspecialidade) {
        this.nomeEspecialidade = nomeEspecialidade;
    }

    /**
     * @return the txProdutividade
     */
    public double getTxProdutividade() {
        return txProdutividade;
    }

    /**
     * @param txProdutividade the txProdutividade to set
     */
    public void setTxProdutividade(double txProdutividade) {
        this.txProdutividade = txProdutividade;
    }

    /**
     * @return the valorUnit
     */
    public double getValorUnit() {
        return valorUnit;
    }

    /**
     * @param valorUnit the valorUnit to set
     */
    public void setValorUnit(double valorUnit) {
        this.valorUnit = valorUnit;
    }

    /**
     * @return the pagarIndicacao
     */
    public char getPagarIndicacao() {
        return pagarIndicacao;
    }

    /**
     * @param pagarIndicacao the pagarIndicacao to set
     */
    public void setPagarIndicacao(char pagarIndicacao) {
        this.pagarIndicacao = pagarIndicacao;
    }

    /**
     * @return the pagarObra
     */
    public char getPagarObra() {
        return pagarObra;
    }

    /**
     * @param pagarObra the pagarObra to set
     */
    public void setPagarObra(char pagarObra) {
        this.pagarObra = pagarObra;
    }

    /**
     * @return the pagarComissao
     */
    public char getPagarComissao() {
        return pagarComissao;
    }

    /**
     * @param pagarComissao the pagarComissao to set
     */
    public void setPagarComissao(char pagarComissao) {
        this.pagarComissao = pagarComissao;
    }

    /**
     * @return the percIndicacao
     */
    public double getPercIndicacao() {
        return percIndicacao;
    }

    /**
     * @param percIndicacao the percIndicacao to set
     */
    public void setPercIndicacao(double percIndicacao) {
        this.percIndicacao = percIndicacao;
    }

    /**
     * @return the percObra
     */
    public double getPercObra() {
        return percObra;
    }

    /**
     * @param percObra the percObra to set
     */
    public void setPercObra(double percObra) {
        this.percObra = percObra;
    }

    /**
     * @return the percComissao
     */
    public double getPercComissao() {
        return percComissao;
    }

    /**
     * @param percComissao the percComissao to set
     */
    public void setPercComissao(double percComissao) {
        this.percComissao = percComissao;
    }
    
    /**
     * @return the valorIndicacao
     */
    public double getValorIndicacao() {
        return valorIndicacao;
    }

    /**
     * @param valorIndicacao the valorIndicacao to set
     */
    public void setValorIndicacao(double valorIndicacao) {
        this.valorIndicacao = valorIndicacao;
    }

    /**
     * @return the valorObra
     */
    public double getValorObra() {
        return valorObra;
    }

    /**
     * @param valorObra the valorObra to set
     */
    public void setValorObra(double valorObra) {
        this.valorObra = valorObra;
    }

    /**
     * @return the valorComissao
     */
    public double getValorComissao() {
        return valorComissao;
    }

    /**
     * @param valorComissao the valorComissao to set
     */
    public void setValorComissao(double valorComissao) {
        this.valorComissao = valorComissao;
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
    public char getSituacao() {
        return situacao;
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(char situacao) {
        this.situacao = situacao;
    }
}
