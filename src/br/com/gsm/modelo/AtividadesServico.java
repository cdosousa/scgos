/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 30/10/2017
 */
public class AtividadesServico extends Servicos{
    // variáveis de instância
    private String sequencia;
    private String cdAtividade;
    private double valorAtividade;
    
    // construtor padrão da classe
    public AtividadesServico(){
        super();
    }
    
    // construtor padrão da casse sobrecarregado
    public AtividadesServico(String sequencia,String cdServico, String cdAtividade, double valorAtividade, String usuarioCadastro,
            String dataCadastro, String dataModificacao, String situacao ){
        setSequencia(sequencia);
        setCdServico(cdServico);
        setCdAtividade(cdAtividade);
        setValorAtividade(valorAtividade);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the sequencia
     */
    public String getSequencia() {
        return sequencia;
    }

    /**
     * @param sequencia the sequencia to set
     */
    public void setSequencia(String sequencia) {
        this.sequencia = sequencia;
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
     * @return the valorAtividade
     */
    public double getValorAtividade() {
        return valorAtividade;
    }

    /**
     * @param valorAtividade the valorAtividade to set
     */
    public void setValorAtividade(double valorAtividade) {
        this.valorAtividade = valorAtividade;
    }
}