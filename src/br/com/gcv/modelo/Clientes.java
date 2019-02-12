/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

import br.com.modelo.Empresa;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 09/11/2017
 */
public class Clientes extends Empresa{
    // variáveis de instância
    private String optanteSimples;
    private String optanteSimbahia;
    private String optanteSuframa;
    private String cdSuframa;
    private String numBanco;
    private String nomeBanco;
    private String agenciaBanco;
    private String numContaBanco;
    private String cdPortador;
    private String cdTipoPagamento;
    private String cdCondPagamento;
    private String nomeContato;
        
    // variáveis de instância de tabelas correlatos
    private String nomePortador;
    private String nomeTipoPagamento;
    private String nomeCondPag;
    
    // construtor padrão da classe
    public Clientes(){
        super();
    }
    
    // construtor padrão da classe sobrecarregado
    public Clientes(String cdCpfCnpj, String cdInscEstadual, String tipoPessoa, String nomeRazaoSocial, 
            String apelido, String optanteSimples, String optanteSimbahia, String optanteSuframa, 
            String cdSuframa, String tipoLogradouro, String logradouro, String numero, String complemento, 
            String bairro, String cdMunicipioIbge, String siglaUf, String cep, String numBanco, 
            String nomeBanco, String agenciaBanco, String numContaBanco, String cdPortador, String cdTipoPagamento, 
            String cdCondPagamento, String nomeContato, String telefone, String celular, String email, 
            String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdCpfCnpj(cdCpfCnpj);
        setCdInscEstadual(cdInscEstadual);
        setTipoPessoa(tipoPessoa);
        setNomeRazaoSocial(nomeRazaoSocial);
        setApelido(apelido);
        setOptanteSimples(optanteSimples);
        setOptanteSimbahia(optanteSimbahia);
        setOptanteSuframa(optanteSuframa);
        setCdSuframa(cdSuframa);
        setTipoLogradouro(tipoLogradouro);
        setLogradouro(logradouro);
        setNumero(numero);
        setComplemento(complemento);
        setBairro(bairro);
        setCdMunicipioIbge(cdMunicipioIbge);
        setSiglaUf(siglaUf);
        setCdCep(cep);
        setNumBanco(numBanco);
        setNomeBanco(nomeBanco);
        setAgenciaBanco(agenciaBanco);
        setNumContaBanco(numContaBanco);
        setCdPortador(cdPortador);
        setCdTipoPagamento(cdTipoPagamento);
        setCdCondPagamento(cdCondPagamento);
        setNomeContato(nomeContato);
        setTelefone(telefone);
        setCelular(celular);
        setEmail(email);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the optanteSimples
     */
    public String getOptanteSimples() {
        return optanteSimples;
    }

    /**
     * @param optanteSimples the optanteSimples to set
     */
    public void setOptanteSimples(String optanteSimples) {
        this.optanteSimples = optanteSimples;
    }

    /**
     * @return the optanteSimbahia
     */
    public String getOptanteSimbahia() {
        return optanteSimbahia;
    }

    /**
     * @param optanteSimbahia the optanteSimbahia to set
     */
    public void setOptanteSimbahia(String optanteSimbahia) {
        this.optanteSimbahia = optanteSimbahia;
    }

    /**
     * @return the optanteSuframa
     */
    public String getOptanteSuframa() {
        return optanteSuframa;
    }

    /**
     * @param optanteSuframa the optanteSuframa to set
     */
    public void setOptanteSuframa(String optanteSuframa) {
        this.optanteSuframa = optanteSuframa;
    }

    /**
     * @return the cdSuframa
     */
    public String getCdSuframa() {
        return cdSuframa;
    }

    /**
     * @param cdSuframa the cdSuframa to set
     */
    public void setCdSuframa(String cdSuframa) {
        this.cdSuframa = cdSuframa;
    }
    
    /**
     * @return the numBanco
     */
    public String getNumBanco() {
        return numBanco;
    }

    /**
     * @param numBanco the numBanco to set
     */
    public void setNumBanco(String numBanco) {
        this.numBanco = numBanco;
    }

    /**
     * @return the nomeBanco
     */
    public String getNomeBanco() {
        return nomeBanco;
    }

    /**
     * @param nomeBanco the nomeBanco to set
     */
    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
    }

    /**
     * @return the agenciaBanco
     */
    public String getAgenciaBanco() {
        return agenciaBanco;
    }

    /**
     * @param agenciaBanco the agenciaBanco to set
     */
    public void setAgenciaBanco(String agenciaBanco) {
        this.agenciaBanco = agenciaBanco;
    }

    /**
     * @return the numContaBanco
     */
    public String getNumContaBanco() {
        return numContaBanco;
    }

    /**
     * @param numContaBanco the numContaBanco to set
     */
    public void setNumContaBanco(String numContaBanco) {
        this.numContaBanco = numContaBanco;
    }

    /**
     * @return the cdPortador
     */
    public String getCdPortador() {
        return cdPortador;
    }

    /**
     * @param cdPortador the cdPortador to set
     */
    public void setCdPortador(String cdPortador) {
        this.cdPortador = cdPortador;
    }

    /**
     * @return the cdTipoPagamento
     */
    public String getCdTipoPagamento() {
        return cdTipoPagamento;
    }

    /**
     * @param cdTipoPagamento the cdTipoPagamento to set
     */
    public void setCdTipoPagamento(String cdTipoPagamento) {
        this.cdTipoPagamento = cdTipoPagamento;
    }

    /**
     * @return the cdCondPagamento
     */
    public String getCdCondPagamento() {
        return cdCondPagamento;
    }

    /**
     * @param cdCondPagamento the cdCondPagamento to set
     */
    public void setCdCondPagamento(String cdCondPagamento) {
        this.cdCondPagamento = cdCondPagamento;
    }

    /**
     * @return the nomeContato
     */
    public String getNomeContato() {
        return nomeContato;
    }

    /**
     * @param nomeContato the nomeContato to set
     */
    public void setNomeContato(String nomeContato) {
        this.nomeContato = nomeContato;
    }

    /**
     * @return the nomePortador
     */
    public String getNomePortador() {
        return nomePortador;
    }

    /**
     * @param nomePortador the nomePortador to set
     */
    public void setNomePortador(String nomePortador) {
        this.nomePortador = nomePortador;
    }

    /**
     * @return the nomeTipoPagamento
     */
    public String getNomeTipoPagamento() {
        return nomeTipoPagamento;
    }

    /**
     * @param nomeTipoPagamento the nomeTipoPagamento to set
     */
    public void setNomeTipoPagamento(String nomeTipoPagamento) {
        this.nomeTipoPagamento = nomeTipoPagamento;
    }

    /**
     * @return the nomeCondPag
     */
    public String getNomeCondPag() {
        return nomeCondPag;
    }

    /**
     * @param nomeCondPag the nomeCondPag to set
     */
    public void setNomeCondPag(String nomeCondPag) {
        this.nomeCondPag = nomeCondPag;
    }
}