/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 28/11/2017
 */
public class ItemLocal extends LocalAtendimento{
    private int sequencia;
    private String cdMaterial;
    private String cdUnidmedida;
    private double quantidade;
    private double valorUnitBruto;
    private double valorUnitLiquido;
    private double valorTotalItemBruto;
    private double valorTotalItemLiquido;
    private String tipoItem;
    private String obsItem;
    
    // variáveis correlatos
    private String nomeMaterial;
    private int proxSequencia;
    
    // construtor da classe
    public ItemLocal(){
        super();
    }
    
    // construtor sobrecarregado da claase
    public ItemLocal(String cdAtendimento, int cdLocal, int sequencia, String cdMaterial, String cdUnidmedida,
            double quantidade, double valorUnitBruto, double valorTotalItemBruto, String tipoItem, String obsItem, String usuarioCadastro, String dataCadastro, String horaCadastro, String usuarioModificacao,
            String dataModificacao, String horaModificacao, String situacao){
        setCdAtendimento(cdAtendimento);
        setCdLocal(cdLocal);
        setSequencia(sequencia);
        setCdMaterial(cdMaterial);
        setCdUnidmedida(cdUnidmedida);
        setQuantidade(quantidade);
        setValorUnitBruto(valorUnitBruto);
        setValorTotalItemBruto(valorTotalItemBruto);
        setTipoItem(tipoItem);
        setObsItem(obsItem);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }
    
    // construtor para mostrar os totais do itemlocal
    public ItemLocal(String cdAtendimento, int cdLocal, double totalProduto, double totalServios, 
            double totalAdicionais, double valorTotal){
        setCdAtendimento(cdAtendimento);
        setCdLocal(cdLocal);
        setValorProdutos(totalProduto);
        setValorServico(totalServios);
        setValorAdicionais(totalAdicionais);
        setValorTotalBruto(valorTotal);
    }

    /**
     * @return the sequencia
     */
    public int getSequencia() {
        return sequencia;
    }

    /**
     * @param sequencia the sequencia to set
     */
    public void setSequencia(int sequencia) {
        this.sequencia = sequencia;
    }

    /**
     * @return the cdMaterial
     */
    public String getCdMaterial() {
        return cdMaterial;
    }

    /**
     * @param cdMaterial the cdMaterial to set
     */
    public void setCdMaterial(String cdMaterial) {
        this.cdMaterial = cdMaterial;
    }

    /**
     * @return the cdUnidmedida
     */
    public String getCdUnidmedida() {
        return cdUnidmedida;
    }

    /**
     * @param cdUnidmedida the cdUnidmedida to set
     */
    public void setCdUnidmedida(String cdUnidmedida) {
        this.cdUnidmedida = cdUnidmedida;
    }

    /**
     * @return the quantidade
     */
    public double getQuantidade() {
        return quantidade;
    }

    /**
     * @param quantidade the quantidade to set
     */
    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    /**
     * @return the valorUnitBruto
     */
    public double getValorUnitBruto() {
        return valorUnitBruto;
    }

    /**
     * @param valorUnitBruto the valorUnitBruto to set
     */
    public void setValorUnitBruto(double valorUnitBruto) {
        this.valorUnitBruto = valorUnitBruto;
    }

    /**
     * @return the valorTotalItemBruto
     */
    public double getValorTotalItemBruto() {
        return valorTotalItemBruto;
    }

    /**
     * @param totalItemBruto the valorTotalItemBruto to set
     */
    public void setValorTotalItemBruto(double valorTotalItemBruto) {
        this.valorTotalItemBruto = valorTotalItemBruto;
    }

    /**
     * @return the obsItem
     */
    public String getObsItem() {
        return obsItem;
    }

    /**
     * @param obsItem the obsItem to set
     */
    public void setObsItem(String obsItem) {
        this.obsItem = obsItem;
    }

    /**
     * @return the nomeMaterial
     */
    public String getNomeMaterial() {
        return nomeMaterial;
    }

    /**
     * @param nomeMaterial the nomeMaterial to set
     */
    public void setNomeMaterial(String nomeMaterial) {
        this.nomeMaterial = nomeMaterial;
    }

    /**
     * @return the proxSequencia
     */
    public int getProxSequencia() {
        return proxSequencia + 1;
    }

    /**
     * @param proxSequencia the proxSequencia to set
     */
    public void setProxSequencia(int proxSequencia) {
        this.proxSequencia = proxSequencia;
    }

    /**
     * @return the tipoItem
     */
    public String getTipoItem() {
        return tipoItem;
    }

    /**
     * @param tipoItem the tipoItem to set
     */
    public void setTipoItem(String tipoItem) {
        this.tipoItem = tipoItem;
    }

    /**
     * @return the valorUnitLiquido
     */
    public double getValorUnitLiquido() {
        return valorUnitLiquido;
    }

    /**
     * @param valorUnitLiquido the valorUnitLiquido to set
     */
    public void setValorUnitLiquido(double valorUnitLiquido) {
        this.valorUnitLiquido = valorUnitLiquido;
    }

    /**
     * @return the valorTotalItemLiquido
     */
    public double getValorTotalItemLiquido() {
        return valorTotalItemLiquido;
    }

    /**
     * @param valorTotalItemLiquido the valorTotalItemLiquido to set
     */
    public void setValorTotalItemLiquido(double valorTotalItemLiquido) {
        this.valorTotalItemLiquido = valorTotalItemLiquido;
    }
}
