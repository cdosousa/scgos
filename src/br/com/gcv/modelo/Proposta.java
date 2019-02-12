/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 11/12/20107
 */
public class Proposta extends ItemLocal {

    private String cdProposta;
    private String cdRevisao;
    private String tipoPedido;
    private String cdPedido;
    private double valorDescontos;
    private double ValorOutrosDescontos;
    private String cdVendedor;
    private String cdTecnico;
    private String prazoExecucao;
    private String dataEnvio;
    
    // Variáveis atributos correlatos
    private String nomeVendedor;
    private String nomeTecnico;
    
    // construtor padrão da classe
    public Proposta(){
        super();
    }
    
    // construto padrão sobrecarregado da classe
    public Proposta(String cdProposta, String cdRevisao, String cdAtendimento, String cdVistoria, String cdVendedor,
            String cdTecnico, String nomeCliente, String tipoPessoa, String telefone, String celular,
            String email, String tipoLogradouro, String logradouro, String numero, String complemento, 
            String bairro, String cdMunicipioIbge, String siglaUf, String cdCep, String tipoEndereco,
            String tipoPedido, String cdTipoPagamento, String cdCondPagamento, String cdPedido,
            double valorServico, double valorProdutos, double valorAdicionais, double valorDescontos,
            double valorOutrosDescontos, double valorTotalBruto, double valorTotalLiquido, String prazoExecucao,
            String obs, String dataEnvio,String usuarioCadastro, String dataCadastro, String horaCadastro, String usuarioModificacao,
            String dataModificacao, String horaModificacao, String situacao){
        setCdProposta(cdProposta);
        setCdRevisao(cdRevisao);
        setCdAtendimento(cdAtendimento);
        setCdVistoria(cdVistoria);
        setCdVendedor(cdVendedor);
        setCdTecnico(cdTecnico);
        setNomeRazaoSocial(nomeCliente);
        setTipoPessoa(tipoPessoa);
        setTelefone(telefone);
        setCelular(celular);
        setEmail(email);
        setTipoLogradouro(tipoLogradouro);
        setLogradouro(logradouro);
        setNumero(numero);
        setComplemento(complemento);
        setBairro(bairro);
        setCdMunicipioIbge(cdMunicipioIbge);
        setSiglaUf(siglaUf);
        setCdCep(cdCep);
        setTipoEndereco(tipoEndereco);
        setTipoPedido(tipoPedido);
        setCdTipoPagamento(cdTipoPagamento);
        setCdCondPagamento(cdCondPagamento);
        setCdPedido(cdPedido);
        setValorServico(valorServico);
        setValorProdutos(valorProdutos);
        setValorAdicionais(valorAdicionais);
        setValorDescontos(valorDescontos);
        setValorOutrosDescontos(valorOutrosDescontos);
        setValorTotalBruto(valorTotalBruto);
        setValorTotalLiquido(valorTotalLiquido);
        setPrazoExecucao(prazoExecucao);
        setObs(obs);
        setDataEnvio(dataEnvio);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }
    
    // construtor para mostrar os totais do itemlocal
    public Proposta(String cdProposta, String cdRevisao, double totalProduto, double totalServicos, 
            double totalAdicionais, double totalDescontos, double totalOutrosDescontos, double valorTotalBruto, double valorTotalLiquido){
        setCdProposta(cdProposta);
        setCdRevisao(cdRevisao);
        setValorProdutos(totalProduto);
        setValorServico(totalServicos);
        setValorAdicionais(totalAdicionais);
        setValorDescontos(totalDescontos);
        setValorOutrosDescontos(totalOutrosDescontos);
        setValorTotalBruto(valorTotalBruto);
        setValorTotalLiquido(valorTotalLiquido);
    }

    /**
     * @return the cdProposta
     */
    public String getCdProposta() {
        return cdProposta;
    }

    /**
     * @param cdProposta the cdProposta to set
     */
    public void setCdProposta(String cdProposta) {
        this.cdProposta = cdProposta;
    }

    /**
     * @return the tipoPedido
     */
    public String getTipoPedido() {
        return tipoPedido;
    }

    /**
     * @param tipoPedido the tipoPedido to set
     */
    public void setTipoPedido(String tipoPedido) {
        this.tipoPedido = tipoPedido;
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
     * @return the valorDescontos
     */
    public double getValorDescontos() {
        return valorDescontos;
    }

    /**
     * @param valorDescontos the valorDescontos to set
     */
    public void setValorDescontos(double valorDescontos) {
        this.valorDescontos = valorDescontos;
    }

    /**
     * @return the cdVendedor
     */
    public String getCdVendedor() {
        return cdVendedor;
    }

    /**
     * @param cdVendedor the cdVendedor to set
     */
    public void setCdVendedor(String cdVendedor) {
        this.cdVendedor = cdVendedor;
    }

    /**
     * @return the cdTecnico
     */
    public String getCdTecnico() {
        return cdTecnico;
    }

    /**
     * @param cdTecnico the cdTecnico to set
     */
    public void setCdTecnico(String cdTecnico) {
        this.cdTecnico = cdTecnico;
    }

    /**
     * @return the nomeVendedor
     */
    public String getNomeVendedor() {
        return nomeVendedor;
    }

    /**
     * @param nomeVendedor the nomeVendedor to set
     */
    public void setNomeVendedor(String nomeVendedor) {
        this.nomeVendedor = nomeVendedor;
    }

    /**
     * @return the nomeTecnico
     */
    public String getNomeTecnico() {
        return nomeTecnico;
    }

    /**
     * @param nomeTecnico the nomeTecnico to set
     */
    public void setNomeTecnico(String nomeTecnico) {
        this.nomeTecnico = nomeTecnico;
    }

    /**
     * @return the ValorOutrosDescontos
     */
    public double getValorOutrosDescontos() {
        return ValorOutrosDescontos;
    }

    /**
     * @param ValorOutrosDescontos the ValorOutrosDescontos to set
     */
    public void setValorOutrosDescontos(double ValorOutrosDescontos) {
        this.ValorOutrosDescontos = ValorOutrosDescontos;
    }

    /**
     * @return the prazoExecucao
     */
    public String getPrazoExecucao() {
        return prazoExecucao;
    }

    /**
     * @param prazoExecucao the prazoExecucao to set
     */
    public void setPrazoExecucao(String prazoExecucao) {
        this.prazoExecucao = prazoExecucao;
    }

    /**
     * @return the cdRevisao
     */
    public String getCdRevisao() {
        return cdRevisao;
    }

    /**
     * @param cdRevisao the cdRevisao to set
     */
    public void setCdRevisao(String cdRevisao) {
        this.cdRevisao = cdRevisao;
    }

    /**
     * @return the dataEnvio
     */
    public String getDataEnvio() {
        return dataEnvio;
    }

    /**
     * @param dataEnvio the dataEnvio to set
     */
    public void setDataEnvio(String dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

}
