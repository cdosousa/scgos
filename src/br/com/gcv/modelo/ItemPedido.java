/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 2018_02Alpha criado em 06/02/2017
 */
public class ItemPedido extends Pedido {

    /**
     * Construtor padão da classe
     */
    public ItemPedido() {

    }
    
    /**
     * Construtor sobrecarregado da classe
     */
    public ItemPedido(String cdPedido, int sequencia, String cdMaterial, String cdUnidMedida, double quantidade,
            double valorUnitbruto, double valorUnitLiquido, double percDesc, double valorDesc, double totalItemBruto,
            double totalItemLiquido, String tipoItem, String obsItem, int cdLocalProposta, int sequenciaProposta,
            String usuarioCadastro, String dataCadastro, String horaCadastro, String usuarioModificacao, String dataModificacao,
            String horaModificacao, String situacao){
        setCdPedido(cdPedido);
        setSequencia(sequencia);
        setCdMaterial(cdMaterial);
        setCdUnidmedida(cdUnidMedida);
        setQuantidade(quantidade);
        setValorUnitBruto(valorUnitbruto);
        setValorUnitLiquido(valorUnitLiquido);
        setPercDesconto(percDesc);
        setValorDescontos(valorDesc);
        setValorTotalBruto(valorUnitbruto);
        setValorTotalItemLiquido(totalItemLiquido);
        setTipoItem(tipoItem);
        setObsItem(obsItem);
        setCdLocal(cdLocalProposta);
        setSequenciaAtend(sequenciaProposta);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }
}
