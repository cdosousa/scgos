/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 06/11/2017
 */
public class ContasBancarias extends Bancos{
    private String cdAgencia;
    private String cdAgenciaDig;
    private String tipoConta;
    private String cdConta;
    private String cdContaDig;
    private double limite;
    private double saldo;
    private double tarifaAdm;
    private double taxaJuros;
    private String dataAbertura;
    private String dataEncerrametno;
    
    // construtor padrão da classe
    public ContasBancarias(){
        super();
    }
    
    // construtor padrão sobrecarregado da classe
    public ContasBancarias(String cdBanco, String cdAgencia, String cdAgenciaDig, String tipoConta, String cdConta,
            String cdContaDig, double limite, double saldo, double tarifaAdm, double taxaJuros,
            String dataAbertura, String dataEncerrametno, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdBanco(cdBanco);
        setCdAgencia(cdAgencia);
        setCdAgenciaDig(cdAgenciaDig);
        setTipoConta(tipoConta);
        setCdConta(cdConta);
        setCdContaDig(cdContaDig);
        setLimite(limite);
        setSaldo(saldo);
        setTarifaAdm(tarifaAdm);
        setTaxaJuros(taxaJuros);
        setDataAbertura(dataAbertura);
        setDataEncerrametno(dataEncerrametno);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdAgencia
     */
    public String getCdAgencia() {
        return cdAgencia;
    }

    /**
     * @param cdAgencia the cdAgencia to set
     */
    public void setCdAgencia(String cdAgencia) {
        this.cdAgencia = cdAgencia;
    }

    /**
     * @return the cdAgenciaDig
     */
    public String getCdAgenciaDig() {
        return cdAgenciaDig;
    }

    /**
     * @param cdAgenciaDig the cdAgenciaDig to set
     */
    public void setCdAgenciaDig(String cdAgenciaDig) {
        this.cdAgenciaDig = cdAgenciaDig;
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
     * @return the cdContaDig
     */
    public String getCdContaDig() {
        return cdContaDig;
    }

    /**
     * @param cdContaDig the cdContaDig to set
     */
    public void setCdContaDig(String cdContaDig) {
        this.cdContaDig = cdContaDig;
    }

    /**
     * @return the limite
     */
    public double getLimite() {
        return limite;
    }

    /**
     * @param limite the limite to set
     */
    public void setLimite(double limite) {
        this.limite = limite;
    }

    /**
     * @return the saldo
     */
    public double getSaldo() {
        return saldo;
    }

    /**
     * @param saldo the saldo to set
     */
    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    /**
     * @return the tarifaAdm
     */
    public double getTarifaAdm() {
        return tarifaAdm;
    }

    /**
     * @param tarifaAdm the tarifaAdm to set
     */
    public void setTarifaAdm(double tarifaAdm) {
        this.tarifaAdm = tarifaAdm;
    }

    /**
     * @return the taxaJuros
     */
    public double getTaxaJuros() {
        return taxaJuros;
    }

    /**
     * @param taxaJuros the taxaJuros to set
     */
    public void setTaxaJuros(double taxaJuros) {
        this.taxaJuros = taxaJuros;
    }

    /**
     * @return the dataAbertura
     */
    public String getDataAbertura() {
        return dataAbertura;
    }

    /**
     * @param dataAbertura the dataAbertura to set
     */
    public void setDataAbertura(String dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    /**
     * @return the dataEncerrametno
     */
    public String getDataEncerrametno() {
        return dataEncerrametno;
    }

    /**
     * @param dataEncerrametno the dataEncerrametno to set
     */
    public void setDataEncerrametno(String dataEncerrametno) {
        this.dataEncerrametno = dataEncerrametno;
    }
}
