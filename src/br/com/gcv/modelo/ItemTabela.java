/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 21/11/2017
 */
public class ItemTabela extends Tabela{
    // variáveis de instância da tabela
    private String cdMaterial;
    private String cdUnidMedida;
    private double valorUnit;
    private double descAlcada;
    
    // variáveis de instância de registros correlatos
    private String nomeMaterial;
    private String nomeUnidMedia;
    
    // construtor padrão da classe
    public ItemTabela(){
        super();
    }
    
    // construtor padrão da classe sobrecarregado
    public ItemTabela(String cdTabela, String cdMaterial, String cdUnidMedida, double valorUnit,
            double descAlcada, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdTabela(cdTabela);
        setCdMaterial(cdMaterial);
        setCdUnidMedida(cdUnidMedida);
        setValorUnit(valorUnit);
        setDescAlcada(descAlcada);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
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
     * @return the descAlcada
     */
    public double getDescAlcada() {
        return descAlcada;
    }

    /**
     * @param descAlcada the descAlcada to set
     */
    public void setDescAlcada(double descAlcada) {
        this.descAlcada = descAlcada;
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
     * @return the nomeUnidMedia
     */
    public String getNomeUnidMedia() {
        return nomeUnidMedia;
    }

    /**
     * @param nomeUnidMedia the nomeUnidMedia to set
     */
    public void setNomeUnidMedia(String nomeUnidMedia) {
        this.nomeUnidMedia = nomeUnidMedia;
    }
}