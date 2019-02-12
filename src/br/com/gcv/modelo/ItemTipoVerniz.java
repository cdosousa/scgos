/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author cristiano
 */
public class ItemTipoVerniz extends TipoVerniz{
    private String cdMaterial;
    private String cdUnidMedida;
    private double qtde;
    
    private String nomeMaterial;
    private String nomeUnidMedida;
    
    // construtor padrao da classe
    public ItemTipoVerniz(){
        super();
    }
    
    // construtor padrão sobrecarregado
    public ItemTipoVerniz(String cdTipoVerniz, String cdMaterial, String cdUnidMedida, double qtde, 
            String usuarioCadastro, String dataCadastro, String usuarioModificacao, String dataModificacao, 
            String situacao){
        setCdTipoVerniz(cdTipoVerniz);
        setCdMaterial(cdMaterial);
        setCdUnidMedida(cdUnidMedida);
        setQtde(qtde);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setUsuarioModificacao(usuarioModificacao);
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
     * @return the qtde
     */
    public double getQtde() {
        return qtde;
    }

    /**
     * @param qtde the qtde to set
     */
    public void setQtde(double qtde) {
        this.qtde = qtde;
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
     * @return the nomeUnidMedida
     */
    public String getNomeUnidMedida() {
        return nomeUnidMedida;
    }

    /**
     * @param nomeUnidMedida the nomeUnidMedida to set
     */
    public void setNomeUnidMedida(String nomeUnidMedida) {
        this.nomeUnidMedida = nomeUnidMedida;
    }
}
