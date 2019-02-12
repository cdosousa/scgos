/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_092917beta criado em 21/03/2018
 */
public class Contrato extends Clientes{
    private String cdContrato;
    private String cdPedido;
    private String dataEmissao;
    private String dataEnvio;
    private String dataAssinatura;
    private String nomeResponsavel;
    private String cpfResponsavel;
    private String modelo;
    private String horaCadastro;
    private String usuarioModificacao;
    private String horaModificacao;
    
    private int proximaSequencia;
    
    /**
     * Construtor padrão da classe
     */
    public Contrato(){
        
    }
    
    /**
     * Construtor sobrecarregado da classe
     */
    public Contrato(String cdContrato, String cdCpfCnpj, String cdPedido, String dataEmissao, String DataEnvio, String dataAssinatura, String nomeResponsavel,
            String cpfResponsavel, String modelo, String usuarioCadastro, String dataCadastro, String horaCadastro, String usuarioModificacao, String dataModificacao,
            String horaModificacao, String situacao){
        setCdContrato(cdContrato);
        setCdCpfCnpj(cdCpfCnpj);
        setCdPedido(cdPedido);
        setDataEmissao(dataEmissao);
        setDataEnvio(DataEnvio);
        setDataAssinatura(dataAssinatura);
        setNomeResponsavel(nomeResponsavel);
        setCpfResponsavel(cpfResponsavel);
        setModelo(modelo);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdContrato
     */
    public String getCdContrato() {
        return cdContrato;
    }

    /**
     * @param cdContrato the cdContrato to set
     */
    public void setCdContrato(String cdContrato) {
        this.cdContrato = cdContrato;
    }

    /**
     * @return the dataEmissao
     */
    public String getDataEmissao() {
        return dataEmissao;
    }

    /**
     * @param dataEmissao the dataEmissao to set
     */
    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    /**
     * @return the dataAssinatura
     */
    public String getDataAssinatura() {
        return dataAssinatura;
    }

    /**
     * @param dataAssinatura the dataAssinatura to set
     */
    public void setDataAssinatura(String dataAssinatura) {
        this.dataAssinatura = dataAssinatura;
    }

    /**
     * @return the nomeResponsavel
     */
    public String getNomeResponsavel() {
        return nomeResponsavel;
    }

    /**
     * @param nomeResponsavel the nomeResponsavel to set
     */
    public void setNomeResponsavel(String nomeResponsavel) {
        this.nomeResponsavel = nomeResponsavel;
    }

    /**
     * @return the horaCadastro
     */
    public String getHoraCadastro() {
        return horaCadastro;
    }

    /**
     * @param horaCadastro the horaCadastro to set
     */
    public void setHoraCadastro(String horaCadastro) {
        this.horaCadastro = horaCadastro;
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
     * @return the horaModificacao
     */
    public String getHoraModificacao() {
        return horaModificacao;
    }

    /**
     * @param horaModificacao the horaModificacao to set
     */
    public void setHoraModificacao(String horaModificacao) {
        this.horaModificacao = horaModificacao;
    }

    /**
     * @return the modelo
     */
    public String getModelo() {
        return modelo;
    }

    /**
     * @param modelo the modelo to set
     */
    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    /**
     * @return the dataEnvio
     */
    public String getDataEnvio() {
        return dataEnvio;
    }

    /**
     * @param dataEnvio the dataEnvio to set
     */
    public void setDataEnvio(String dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    /**
     * @return the cdPedido
     */
    public String getCdPedido() {
        return cdPedido;
    }

    /**
     * @param cdPedido the cdPedido to set
     */
    public void setCdPedido(String cdPedido) {
        this.cdPedido = cdPedido;
    }

    /**
     * @return the proximaSequencia
     */
    public int getProximaSequencia() {
        return proximaSequencia + 1;
    }

    /**
     * @param proximaSequencia the proximaSequencia to set
     */
    public void setProximaSequencia(int proximaSequencia) {
        this.proximaSequencia = proximaSequencia;
    }

    /**
     * @return the cpfResponsavel
     */
    public String getCpfResponsavel() {
        return cpfResponsavel;
    }

    /**
     * @param cpfResponsavel the cpfResponsavel to set
     */
    public void setCpfResponsavel(String cpfResponsavel) {
        this.cpfResponsavel = cpfResponsavel;
    }
}