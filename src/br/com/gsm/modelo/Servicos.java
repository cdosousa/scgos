/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 27/10/2017
 */
public class Servicos extends Atividades{
    private String cdServico;
    private String nomeServico;
    private String cdMaterial;
    private String cdUnidMedida;
    private String descricaoComercial;
    private double valorServico;
    private double valorMaterial;
    
    // variáveis de tabelas correlatos
    private String nomeMaterial;
    
    // construtor padrão da classe
    public Servicos(){
        super();
    }
    //construtor padrão da class sobrecarregado
    public Servicos(String cdServico, String nomeServico, String cdMaterial, String cdUnidMedida, String descricaoComercial, double valorServico, double valorMaterial, 
            double valorTotalAtividade, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdServico(cdServico);
        setNomeServico(nomeServico);
        setCdMaterial(cdMaterial);
        setCdUnidMedida(cdUnidMedida);
        setDescricaoComercial(descricaoComercial);
        setValorServico(valorServico);
        setValorMaterial(valorMaterial);
        setValTotalAtividade(valorTotalAtividade);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdServico
     */
    public String getCdServico() {
        return cdServico;
    }

    /**
     * @param cdServico the cdServico to set
     */
    public void setCdServico(String cdServico) {
        this.cdServico = cdServico;
    }

    /**
     * @return the nomeServico
     */
    public String getNomeServico() {
        return nomeServico;
    }

    /**
     * @param nomeServico the nomeServico to set
     */
    public void setNomeServico(String nomeServico) {
        this.nomeServico = nomeServico;
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
     * @return the cdUnidMedida
     */
    public String getCdUnidMedida() {
        return cdUnidMedida;
    }

    /**
     * @param cdUnidMedida the cdUnidMedida to set
     */
    public void setCdUnidMedida(String cdUnidMedida) {
        this.cdUnidMedida = cdUnidMedida;
    }

    /**
     * @return the valorServico
     */
    public double getValorServico() {
        return valorServico;
    }

    /**
     * @param valorServico the valorServico to set
     */
    public void setValorServico(double valorServico) {
        this.valorServico = valorServico;
    }

    /**
     * @return the valorMaterial
     */
    public double getValorMaterial() {
        return valorMaterial;
    }

    /**
     * @param valorMaterial the valorMaterial to set
     */
    public void setValorMaterial(double valorMaterial) {
        this.valorMaterial = valorMaterial;
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
     * @return the descricaoComercial
     */
    public String getDescricaoComercial() {
        return descricaoComercial;
    }

    /**
     * @param descricaoComercial the descricaoComercial to set
     */
    public void setDescricaoComercial(String descricaoComercial) {
        this.descricaoComercial = descricaoComercial;
    }
}