/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 23/11/2017
 */
public class Atendimento extends Contato{
    private String tipoEndereco;
    private double valorServico;
    private double valorProdutos;
    private double valorAdicionais;
    private double valorTotalBruto;
    private double valorTotalLiquido;
    
    // construtor padrão da classe
    public Atendimento(){
        super();
    }
    
    // construtor padrão sobrecarregado
    public Atendimento(String cdAtendimento, String dataAtendimento, String horaAtendimento, String nomeCliente, String tipoPessoa,
            String telefone, String celular, String email,String tipoLogradouro, String logradouro, String numero,
            String complemento, String bairro, String cdMunicipioIbge, String siglaUf, String cdCep, String tipoEndereco,
            double valorServico, double valorProdutos, double valorAdicionais, double valorTotalBruto, String cd_proposta,  String usuarioCadastro, 
            String dataCadastro, String horaCadastro, String usuarioModificacao, String dataModificacao, 
            String horaModificacao, String situacao){
        setCdAtendimento(cdAtendimento);
        setDataAtendimento(dataAtendimento);
        setHoraAtendimento(horaAtendimento);
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
        setValorServico(valorServico);
        setValorProdutos(valorProdutos);
        setValorAdicionais(valorAdicionais);
        setValorTotalBruto(valorTotalBruto);
        setCdProposta(cd_proposta);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }
    
    // construtor para mostrar os totais do itemlocal
    public Atendimento(String cdAtendimento, double totalProduto, double totalServios, 
            double totalAdicionais, double valorTotalBruto){
        setCdAtendimento(cdAtendimento);
        setValorProdutos(totalProduto);
        setValorServico(totalServios);
        setValorAdicionais(totalAdicionais);
        setValorTotalBruto(valorTotalBruto);
    }

    /**
     * @return the tipoEndereco
     */
    public String getTipoEndereco() {
        return tipoEndereco;
    }

    /**
     * @param tipoEndereco the tipoEndereco to set
     */
    public void setTipoEndereco(String tipoEndereco) {
        this.tipoEndereco = tipoEndereco;
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
     * @return the valorProdutos
     */
    public double getValorProdutos() {
        return valorProdutos;
    }

    /**
     * @param valorProdutos the valorProdutos to set
     */
    public void setValorProdutos(double valorProdutos) {
        this.valorProdutos = valorProdutos;
    }

    /**
     * @return the valorAdicionais
     */
    public double getValorAdicionais() {
        return valorAdicionais;
    }

    /**
     * @param valorAdicionais the valorAdicionais to set
     */
    public void setValorAdicionais(double valorAdicionais) {
        this.valorAdicionais = valorAdicionais;
    }

    /**
     * @return the valorTotalBruto
     */
    public double getValorTotalBruto() {
        return valorTotalBruto;
    }

    /**
     * @param valorTotalBruto the valorTotalBruto to set
     */
    public void setValorTotalBruto(double valorTotalBruto) {
        this.valorTotalBruto = valorTotalBruto;
    }

    /**
     * @return the valorTotalLiquido
     */
    public double getValorTotalLiquido() {
        return valorTotalLiquido;
    }

    /**
     * @param valorTotalLiquido the valorTotalLiquido to set
     */
    public void setValorTotalLiquido(double valorTotalLiquido) {
        this.valorTotalLiquido = valorTotalLiquido;
    }
}
