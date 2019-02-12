/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcs.modelo;

import br.com.gcv.modelo.Clientes;

/**
 *
 * @author Cristiano de Oliveira Sousa criado em 30/07/2018
 * @version 0.01_beta0917
 */
public class Fornecedores extends Clientes{
    
    /**
     * construtor padrão da classe
     */
    public Fornecedores(){
        
    }
    
    /**
     * Construtor sobrecarregado da classe
     */
    public Fornecedores(String cdCpfCnpj, String cdInscEstadual, String tipoPessoa, String nomeRazaoSocial, 
            String apelido, String optanteSimples, String tipoLogradouro, String logradouro, String numero, String complemento, 
            String bairro, String cdMunicipioIbge, String siglaUf, String cdCep, String numBanco, 
            String nomeBanco, String agenciaBanco, String numContaBanco, String cdPortador, String cdTipoPagamento, 
            String cdCondPagamento, String nomeContato, String telefone, String celular, String email, 
            String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdCpfCnpj(cdCpfCnpj);
        setCdInscEstadual(cdInscEstadual);
        setTipoPessoa(tipoPessoa);
        setNomeRazaoSocial(nomeRazaoSocial);
        setApelido(apelido);
        setOptanteSimples(optanteSimples);
        setTipoLogradouro(tipoLogradouro);
        setLogradouro(logradouro);
        setNumero(numero);
        setComplemento(complemento);
        setBairro(bairro);
        setCdMunicipioIbge(cdMunicipioIbge);
        setSiglaUf(siglaUf);
        setCdCep(cdCep);
        setNumBanco(numBanco);
        setNomeBanco(nomeBanco);
        setAgenciaBanco(agenciaBanco);
        setNumContaBanco(numContaBanco);
        setCdPortador(cdPortador);
        setCdTipoPagamento(cdTipoPagamento);
        setCdCondPagamento(cdCondPagamento);
        setNomeContato(nomeContato);
        setTelefone(telefone);
        setCelular(celular);
        setEmail(email);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }
}