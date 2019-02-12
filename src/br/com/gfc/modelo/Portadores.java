/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 07/11/2017
 */
public class Portadores extends ContasBancarias{
    private String cdPortador;
    private String nomePortador;
    private double taxaMulta;
    private double taxaCorrecao;
    private int diasLiquidacao;
    private int diasCartorio;
    
    // construtor padrão da classe
    public Portadores(){
        super();
    }
    
    // construtor padrão da classe sobrecarregado
    public Portadores(String cdBanco, String cdConta,String cdPortador, String NomePortador, 
            double taxaJuros, double taxaMulta, double taxaCorrecao, int diasLiquidacao, int diasCartorio,
            String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdBanco(cdBanco);
        setCdConta(cdConta);
        setCdPortador(cdPortador);
        setNomePortador(NomePortador);
        setTaxaJuros(taxaJuros);
        setTaxaMulta(taxaMulta);
        setTaxaCorrecao(taxaCorrecao);
        setDiasLiquidacao(diasLiquidacao);
        setDiasCartorio(diasCartorio);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
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
     * @return the taxaMulta
     */
    public double getTaxaMulta() {
        return taxaMulta;
    }

    /**
     * @param taxaMulta the taxaMulta to set
     */
    public void setTaxaMulta(double taxaMulta) {
        this.taxaMulta = taxaMulta;
    }

    /**
     * @return the taxaCorrecao
     */
    public double getTaxaCorrecao() {
        return taxaCorrecao;
    }

    /**
     * @param taxaCorrecao the taxaCorrecao to set
     */
    public void setTaxaCorrecao(double taxaCorrecao) {
        this.taxaCorrecao = taxaCorrecao;
    }

    /**
     * @return the diasLiquidacao
     */
    public int getDiasLiquidacao() {
        return diasLiquidacao;
    }

    /**
     * @param diasLiquidacao the diasLiquidacao to set
     */
    public void setDiasLiquidacao(int diasLiquidacao) {
        this.diasLiquidacao = diasLiquidacao;
    }

    /**
     * @return the diasCartorio
     */
    public int getDiasCartorio() {
        return diasCartorio;
    }

    /**
     * @param diasCartorio the diasCartorio to set
     */
    public void setDiasCartorio(int diasCartorio) {
        this.diasCartorio = diasCartorio;
    }
}
