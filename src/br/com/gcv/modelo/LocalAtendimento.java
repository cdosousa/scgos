/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * create on 23/11/2017
 */
public class LocalAtendimento extends Atendimento{
    private int cdLocal;
    private String nomeLocal;
    private double metragemArea;
    private double percPerda;
    private String tipoPiso;
    private String tipoRodape;
    private double metragemRodape;
    private double largura;
    private String comprimento;
    private double espessura;
    private String tingimento;
    private String clareamento;
    private String cdTipoVerniz;
    private String cdEssencia;
    
    // variáveis de instância correlatos
    private String nomeTipoVerniz;
    private String nomeEssenciaMadeira;
    
    // construtor padrão da classe
    public LocalAtendimento(){
        super();
    }
    // construto padrão sobrecarregado da classe
    public LocalAtendimento(String cdAtendimento, int cdLocal, String nomeLocal,double metragemArea, 
            double percPerda, String tipoPiso, String tipoRodape, double metragemRodape, double largura,
            String comprimento, double espessura, String tingimento, String clareamento, String cdTipoVerniz,
            String cdEssencia, double valorServico, double valorProdutos, double valorAdicionais, double valorTotal,
            String obsLocal, String usuarioCadastro, String dataCadastro, String horaCadastro, String usuarioModificacao,
            String dataModificacao, String horaModificacao, String situacao){
        setCdAtendimento(cdAtendimento);
        setCdLocal(cdLocal);
        setNomeLocal(nomeLocal);
        setMetragemArea(metragemArea);
        setPercPerda(percPerda);
        setTipoPiso(tipoPiso);
        setTipoRodape(tipoRodape);
        setMetragemRodape(metragemRodape);
        setLargura(largura);
        setComprimento(comprimento);
        setEspessura(espessura);
        setTingimento(tingimento);
        setClareamento(clareamento);
        setCdTipoVerniz(cdTipoVerniz);
        setCdEssencia(cdEssencia);
        setValorServico(valorServico);
        setValorProdutos(valorProdutos);
        setValorAdicionais(valorAdicionais);
        setValorTotalBruto(valorTotal);
        setObs(obsLocal);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraCadastro(horaCadastro);
        setSituacao(situacao);
    }

    /**
     * @return the LocalAtendimento
     */
    public int getCdLocal() {
        return cdLocal;
    }

    /**
     * @param cdLocal the LocalAtendimento to set
     */
    public void setCdLocal(int cdLocal) {
        this.cdLocal = cdLocal;
    }

    /**
     * @return the nomeLocal
     */
    public String getNomeLocal() {
        return nomeLocal;
    }

    /**
     * @param nomeLocal the nomeLocal to set
     */
    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    /**
     * @return the metragemArea
     */
    public double getMetragemArea() {
        return metragemArea;
    }

    /**
     * @param metragemArea the metragemArea to set
     */
    public void setMetragemArea(double metragemArea) {
        this.metragemArea = metragemArea;
    }

    /**
     * @return the percPerda
     */
    public double getPercPerda() {
        return percPerda;
    }

    /**
     * @param percPerda the percPerda to set
     */
    public void setPercPerda(double percPerda) {
        this.percPerda = percPerda;
    }

    /**
     * @return the tipoPiso
     */
    public String getTipoPiso() {
        return tipoPiso;
    }

    /**
     * @param tipoPiso the tipoPiso to set
     */
    public void setTipoPiso(String tipoPiso) {
        this.tipoPiso = tipoPiso;
    }

    /**
     * @return the tipoRodape
     */
    public String getTipoRodape() {
        return tipoRodape;
    }

    /**
     * @param tipoRodape the tipoRodape to set
     */
    public void setTipoRodape(String tipoRodape) {
        this.tipoRodape = tipoRodape;
    }

    /**
     * @return the metragemRodape
     */
    public double getMetragemRodape() {
        return metragemRodape;
    }

    /**
     * @param metragemRodape the metragemRodape to set
     */
    public void setMetragemRodape(double metragemRodape) {
        this.metragemRodape = metragemRodape;
    }

    /**
     * @return the largura
     */
    public double getLargura() {
        return largura;
    }

    /**
     * @param largura the largura to set
     */
    public void setLargura(double largura) {
        this.largura = largura;
    }

    /**
     * @return the comprimento
     */
    public String getComprimento() {
        return comprimento;
    }

    /**
     * @param comprimento the comprimento to set
     */
    public void setComprimento(String comprimento) {
        this.comprimento = comprimento;
    }

    /**
     * @return the espessura
     */
    public double getEspessura() {
        return espessura;
    }

    /**
     * @param espessura the espessura to set
     */
    public void setEspessura(double espessura) {
        this.espessura = espessura;
    }

    /**
     * @return the tingimento
     */
    public String getTingimento() {
        return tingimento;
    }

    /**
     * @param tingimento the tingimento to set
     */
    public void setTingimento(String tingimento) {
        this.tingimento = tingimento;
    }

    /**
     * @return the clareamento
     */
    public String getClareamento() {
        return clareamento;
    }

    /**
     * @param clareamento the clareamento to set
     */
    public void setClareamento(String clareamento) {
        this.clareamento = clareamento;
    }

    /**
     * @return the cdTipoVerniz
     */
    public String getCdTipolVerniz() {
        return cdTipoVerniz;
    }

    /**
     * @param cdTipoVerniz the cdTipoVerniz to set
     */
    public void setCdTipoVerniz(String cdTipoVerniz) {
        this.cdTipoVerniz = cdTipoVerniz;
    }

    /**
     * @return the cdEssencia
     */
    public String getCdEssencia() {
        return cdEssencia;
    }

    /**
     * @param cdEssencia the cdEssencia to set
     */
    public void setCdEssencia(String cdEssencia) {
        this.cdEssencia = cdEssencia;
    }

    /**
     * @return the nomeTipoVerniz
     */
    public String getNomeTipoVerniz() {
        return nomeTipoVerniz;
    }

    /**
     * @param nomeTipoVerniz the nomeTipoVerniz to set
     */
    public void setNomeTipoVerniz(String nomeTipoVerniz) {
        this.nomeTipoVerniz = nomeTipoVerniz;
    }

    /**
     * @return the nomeEssenciaMadeira
     */
    public String getNomeEssenciaMadeira() {
        return nomeEssenciaMadeira;
    }

    /**
     * @param nomeEssenciaMadeira the nomeEssenciaMadeira to set
     */
    public void setNomeEssenciaMadeira(String nomeEssenciaMadeira) {
        this.nomeEssenciaMadeira = nomeEssenciaMadeira;
    }
}