/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * create on 17/11/2017
 */
public class EnderecoCobrancaCliente extends Clientes{
    private int sequencia;
    
    // construtor padrão da classe
    public EnderecoCobrancaCliente(){
        super();
    }
    
    // construtor sobrecarregado
    public EnderecoCobrancaCliente(String cdCpfCnpj, int sequencia, String tipoLogradouro, String logradouro,
            String numero, String complemento, String bairro, String cdMunicipioIbge, String siglaUf, String cdCep,
            String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdCpfCnpj(cdCpfCnpj);
        setSequencia(sequencia);
        setTipoLogradouro(tipoLogradouro);
        setLogradouro(logradouro);
        setNumero(numero);
        setComplemento(complemento);
        setBairro(bairro);
        setCdMunicipioIbge(cdMunicipioIbge);
        setSiglaUf(siglaUf);
        setCdCep(cdCep);
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
}
