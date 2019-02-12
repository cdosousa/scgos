/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 10/11/2017
 */
public class TipoPagamento extends Portadores {
    private String cdTipoPagamento;
    private String nomeTipoPagamento;
    private String permiteParcelamento;
    private String emiteBoleto;
    private String enviarArqBanco;
    private String enviaCartorio;
    
    // contrutor padrao da classe
    public TipoPagamento(){
        super();
    }
    
    // contrutor padrão da classe sobrecarregado
    public TipoPagamento(String cdTipoPagamento, String nomeTipoPagamento, String permiteParcelamento, 
            String emiteBoleto, String enviarArqBanco, String enviaCartorio, int diasCartorio, String cdPortador,
            double taxaJuros, double taxaMulta, int diasLiquidacao, String usuarioCadastro, String dataCadastro,
            String dataModificacao, String situacao){
        setCdTipoPagamento(cdTipoPagamento);
        setNomeTipoPagamento(nomeTipoPagamento);
        setPermiteParcelamento(permiteParcelamento);
        setEmiteBoleto(emiteBoleto);
        setEnviarArqBanco(enviarArqBanco);
        setEnviaCartorio(enviaCartorio);
        setDiasCartorio(diasCartorio);
        setCdPortador(cdPortador);
        setTaxaJuros(taxaJuros);
        setTaxaMulta(taxaMulta);
        setDiasLiquidacao(diasLiquidacao);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
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
     * @return the permiteParcelamento
     */
    public String getPermiteParcelamento() {
        return permiteParcelamento;
    }

    /**
     * @param permiteParcelamento the permiteParcelamento to set
     */
    public void setPermiteParcelamento(String permiteParcelamento) {
        this.permiteParcelamento = permiteParcelamento;
    }

    /**
     * @return the emiteBoleto
     */
    public String getEmiteBoleto() {
        return emiteBoleto;
    }

    /**
     * @param emiteBoleto the emiteBoleto to set
     */
    public void setEmiteBoleto(String emiteBoleto) {
        this.emiteBoleto = emiteBoleto;
    }

    /**
     * @return the enviarArqBanco
     */
    public String getEnviarArqBanco() {
        return enviarArqBanco;
    }

    /**
     * @param enviarArqBanco the enviarArqBanco to set
     */
    public void setEnviarArqBanco(String enviarArqBanco) {
        this.enviarArqBanco = enviarArqBanco;
    }

    /**
     * @return the enviaCartorio
     */
    public String getEnviaCartorio() {
        return enviaCartorio;
    }

    /**
     * @param enviaCartorio the enviaCartorio to set
     */
    public void setEnviaCartorio(String enviaCartorio) {
        this.enviaCartorio = enviaCartorio;
    }
}