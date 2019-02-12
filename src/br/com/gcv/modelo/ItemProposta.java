/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 11/12/2017
 */
public class ItemProposta extends LocalProposta{
    private int sequenciaAtend;
    private double percDesconto;
    
    // contrutor padrão da classe
    public ItemProposta(){
        super();
    }
    
    // construtor padrão sobrecarregado da classe
    public ItemProposta(String cdProposta, String cdRevisao, int cdLocal, int sequencia, int sequenciaAtend, String cdMaterial,
            String cdUnidmedida, double quantidade, double valorUnitBruto, double valorUnitLiquido, double percDesconto, double valorDesc, 
            double valorTotalItemBruto, double valorTotalItemLiquido, String tipoItem, String obsItem, String usuarioCadastro, String dataCadastro,
            String horaCadastro, String usuarioModificacao, String dataModificacao, String horaModificacao,
            String situacao){
        setCdProposta(cdProposta);
        setCdRevisao(cdRevisao);
        setCdLocal(cdLocal);
        setSequencia(sequencia);
        setSequenciaAtend(sequenciaAtend);
        setCdMaterial(cdMaterial);
        setCdUnidmedida(cdUnidmedida);
        setQuantidade(quantidade);
        setValorUnitBruto(valorUnitBruto);
        setValorUnitLiquido(valorUnitLiquido);
        setPercDesconto(percDesconto);
        setValorDescontos(valorDesc);
        setValorTotalItemBruto(valorTotalItemBruto);
        setValorTotalItemLiquido(valorTotalItemLiquido);
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

    /**
     * @return the sequenciaAtend
     */
    public int getSequenciaAtend() {
        return sequenciaAtend;
    }

    /**
     * @param sequenciaAtend the sequenciaAtend to set
     */
    public void setSequenciaAtend(int sequenciaAtend) {
        this.sequenciaAtend = sequenciaAtend;
    }

    /**
     * @return the percDesconto
     */
    public double getPercDesconto() {
        return percDesconto;
    }

    /**
     * @param percDesconto the percDesconto to set
     */
    public void setPercDesconto(double percDesconto) {
        this.percDesconto = percDesconto;
    }
}
