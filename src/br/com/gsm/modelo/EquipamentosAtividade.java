/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 18/10/2017
 */
public class EquipamentosAtividade extends Equipamentos{
    private int sequencial;
    private String cdAtividade;
    private double valorUnit;
    
    //construtor padrão
    public EquipamentosAtividade(){
        super();
    }
    // construtor sobrecarregado
    public EquipamentosAtividade(int sequencial, String cdAtividade, String cdEquipamento, double valorUnit, String usuarioCadastro, 
            String dataCadastro, String dataModificacao, String situacao){
        setSequencial(sequencial);
        setCdAtividade(cdAtividade);
        setCdEquipamento(cdEquipamento);
        setValorUnit(valorUnit);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the sequencial
     */
    public int getSequencial() {
        return sequencial;
    }

    /**
     * @param sequencial the sequencial to set
     */
    public void setSequencial(int sequencial) {
        this.sequencial = sequencial;
    }

    /**
     * @return the cdAtividade
     */
    public String getCdAtividade() {
        return cdAtividade;
    }

    /**
     * @param cdAtividade the cdAtividade to set
     */
    public void setCdAtividade(String cdAtividade) {
        this.cdAtividade = cdAtividade;
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
}