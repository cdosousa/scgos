/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 2018_02Alpha creado em 06/02/2018
 */
public class Pedido extends ItemProposta {

    private String cdPedido;
    private String cdOperVenda;
    private String nomeOperVenda;
    private String dataInicio;

    /**
     * Construtor padrão da classe
     */
    public Pedido() {

    }
    
    /**
     * Construtor sobrecarregado da classe
     */
    public Pedido(String cdPedido, String cdProposta, String cdRevisao, String cdVendedor, String cdTecnico,
            String cpfCnpj, String tipoPedido, String cdTipoPagamento, String cdCondPag, String cdOperVenda, double valorServicos,
            double valorProdutos, double valorAdicionais, double valorDescontos, double valorOutrosDescontos,
            double valorTotalBruto, double valorTotalLiquido, String prazoExecucao, String dataInicio, String obs, String usuarioCadastro,
            String dataCadastro, String horaCadastro, String usuarioModificacao, String dataModificacao,
            String horaModificacao, String situacao){
        setCdPedido(cdPedido);
        setCdProposta(cdProposta);
        setCdRevisao(cdRevisao);
        setCdVendedor(cdVendedor);
        setCdTecnico(cdTecnico);
        setCdCpfCnpj(cpfCnpj);
        setTipoPedido(tipoPedido);
        setCdTipoPagamento(cdTipoPagamento);
        setCdCondPagamento(cdCondPag);
        setCdOperVenda(cdOperVenda);
        setValorServico(valorServicos);
        setValorProdutos(valorProdutos);
        setValorAdicionais(valorAdicionais);
        setValorDescontos(valorDescontos);
        setValorOutrosDescontos(valorOutrosDescontos);
        setValorTotalBruto(valorTotalBruto);
        setValorTotalItemLiquido(valorTotalLiquido);
        setPrazoExecucao(prazoExecucao);
        setDataInicio(dataInicio);
        setObs(obs);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }
    
    /**
     * Método para atualizar os totais do pedido
     * @param cdPedido
     * @param valorProdutos
     * @param valorServicos
     * @param valorAdicionais
     * @param valorDescontos
     * @param valorOutrosDescontos
     * @param valorTotalBruto
     * @param valorTotalLiquido
     */
    public Pedido(String cdPedido, double valorProdutos, double valorServicos, double valorAdicionais,
            double valorDescontos, double valorOutrosDescontos, double valorTotalBruto, double valorTotalLiquido){
        setCdPedido(cdPedido);
        setValorProdutos(valorProdutos);
        setValorServico(valorServicos);
        setValorAdicionais(valorAdicionais);
        setValorDescontos(valorDescontos);
        setValorOutrosDescontos(valorOutrosDescontos);
        setValorTotalBruto(valorTotalBruto);
        setValorTotalLiquido(valorTotalLiquido);
    }

    /**
     * @return the cdPedido
     */
    public String getCdPedido() {
        return cdPedido;
    }

    /**
     * @param cdPedido the cdPedido to set
     */
    public void setCdPedido(String cdPedido) {
        this.cdPedido = cdPedido;
    }

    /**
     * @return the cdOperVenda
     */
    public String getCdOperVenda() {
        return cdOperVenda;
    }

    /**
     * @param cdOperVenda the cdOperVenda to set
     */
    public void setCdOperVenda(String cdOperVenda) {
        this.cdOperVenda = cdOperVenda;
    }

    /**
     * @return the nomeOperVenda
     */
    public String getNomeOperVenda() {
        return nomeOperVenda;
    }

    /**
     * @param nomeOperVenda the nomeOperVenda to set
     */
    public void setNomeOperVenda(String nomeOperVenda) {
        this.nomeOperVenda = nomeOperVenda;
    }

    /**
     * @return the dataInicio
     */
    public String getDataInicio() {
        return dataInicio;
    }

    /**
     * @param dataInicio the dataInicio to set
     */
    public void setDataInicio(String dataInicio) {
        this.dataInicio = dataInicio;
    }
}
