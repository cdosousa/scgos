/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 31/10/2017
 */
public class MateriaisServico extends Servicos{
    private int sequencia;
    private double qtdeMaterial;
    private double valorUnit;
    private String nomeUnidMedida;
    
    // contrutor padrão da casse
    public MateriaisServico(){
        super();
    }
    
    // construtor padrão sobrecarregado
    public MateriaisServico(int sequencia, String cdServico, String cdMaterial, String nomeMaterial, String cdUnidMedida, String nomeUnidMedida, double qtdeMaterial, 
            double valorUnit, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setSequencia(sequencia);
        setCdServico(cdServico);
        setCdMaterial(cdMaterial);
        setNomeMaterial(nomeMaterial);
        setCdUnidMedida(cdUnidMedida);
        setNomeUnidMedida(nomeUnidMedida);
        setQtdeMaterial(qtdeMaterial);
        setValorUnit(valorUnit);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
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
     * @return the qtdeMaterial
     */
    public double getQtdeMaterial() {
        return qtdeMaterial;
    }

    /**
     * @param qtdeMaterial the qtdeMaterial to set
     */
    public void setQtdeMaterial(double qtdeMaterial) {
        this.qtdeMaterial = qtdeMaterial;
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