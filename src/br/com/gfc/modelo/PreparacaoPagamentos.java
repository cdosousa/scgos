/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa criado em 17/09/2018
 * @version 0.01_beta0917
 */
public class PreparacaoPagamentos extends Lancamentos {

    private String cdPreparacao;
    private String dataLiquidacaoIni;
    private String dataLiquidacaoFin;
    private int quantidadeTitulos;
    private double valorTotal;
    private boolean gerarBoleto = false;
    private boolean gerarArquivo = false;

    /**
     * Construtor padrão da classe
     */
    public PreparacaoPagamentos() {

    }

    /**
     * Método para carregar o objeto com os valores da tabela
     *
     * @param cdPreparacao
     * @param dataLiquidacaoIni
     * @param dataLiquidacaoFin
     * @param cdPortador
     * @param cdTipoPagamento
     * @param cdTipoMovimento
     * @param quantidadeTitulos
     * @param valorTotal
     * @param usuarioCadastro
     * @param dataCadastro
     * @param horaCadastro
     * @param usuarioModificacao
     * @param dataModificacao
     * @param horaModificacao
     * @param situacao
     */
    public PreparacaoPagamentos(String cdPreparacao, String dataLiquidacaoIni, String dataLiquidacaoFin, String cdPortador, String cdTipoPagamento,
            String cdTipoMovimento, int quantidadeTitulos, double valorTotal, String usuarioCadastro, String dataCadastro, String horaCadastro,
            String usuarioModificacao, String dataModificacao, String horaModificacao, String situacao) {
        setCdPreparacao(cdPreparacao);
        setDataLiquidacaoIni(dataLiquidacaoIni);
        setDataLiquidacaoFin(dataLiquidacaoFin);
        setCdPortador(cdPortador);
        setCdTipoPagamento(cdTipoPagamento);
        setTipoMovimento(cdTipoMovimento);
        setQuantidadeTitulos(quantidadeTitulos);
        setValorTotal(valorTotal);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdPreparacao
     */
    public String getCdPreparacao() {
        return cdPreparacao;
    }

    /**
     * @param cdPreparacao the cdPreparacao to set
     */
    public void setCdPreparacao(String cdPreparacao) {
        this.cdPreparacao = cdPreparacao;
    }

    /**
     * @return the dataLiquidacaoIni
     */
    public String getDataLiquidacaoIni() {
        return dataLiquidacaoIni;
    }

    /**
     * @param dataLiquidacaoIni the dataLiquidacaoIni to set
     */
    public void setDataLiquidacaoIni(String dataLiquidacaoIni) {
        this.dataLiquidacaoIni = dataLiquidacaoIni;
    }

    /**
     * @return the dataLiquidacaoFin
     */
    public String getDataLiquidacaoFin() {
        return dataLiquidacaoFin;
    }

    /**
     * @param dataLiquidacaoFin the dataLiquidacaoFin to set
     */
    public void setDataLiquidacaoFin(String dataLiquidacaoFin) {
        this.dataLiquidacaoFin = dataLiquidacaoFin;
    }

    /**
     * @return the quantidadeTitulos
     */
    public int getQuantidadeTitulos() {
        return quantidadeTitulos;
    }

    /**
     * @param quantidadeTitulos the quantidadeTitulos to set
     */
    public void setQuantidadeTitulos(int quantidadeTitulos) {
        this.quantidadeTitulos = quantidadeTitulos;
    }

    /**
     * @return the valorTotal
     */
    public double getValorTotal() {
        return valorTotal;
    }

    /**
     * @param valorTotal the valorTotal to set
     */
    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    /**
     * @return the gerarBoleto
     */
    public boolean isGerarBoleto() {
        return gerarBoleto;
    }

    /**
     * @param gerarBoleto the gerarBoleto to set
     */
    public void setGerarBoleto(String gerarBoleto) {
        if("S".equals(gerarBoleto))
            this.gerarBoleto = true;
        else
            this.gerarBoleto = false;
    }

    /**
     * @return the gerarArquivo
     */
    public boolean isGerarArquivo() {
        return gerarArquivo;
    }

    /**
     * @param gerarArquivo the gerarArquivo to set
     */
    public void setGerarArquivo(String gerarArquivo) {
        if ("S".equals(gerarArquivo)) {
            this.gerarArquivo = true;
        } else {
            this.gerarArquivo = false;
        }
    }
}
