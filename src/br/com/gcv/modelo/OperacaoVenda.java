/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

import br.com.gfr.modelo.TiposOperacoes;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 10/11/2017
 */
public class OperacaoVenda extends TiposOperacoes{
    private String cdOperacaoVenda;
    private String nomeOperacaoVenda;
    private String tipoFinalidade;
    private String emiteNfeVenda;
    private String emiteNfeServico;
    private String geraCobranca;
    private String cdContrato;
    private String cdCCusto;
    
    private String nomeCCusto;
    
    // construtor padrão da classe
    public OperacaoVenda(){
        super();
    }
    
    // construtor padrão da classe sobrecarregado
    public OperacaoVenda(String cdOperacaoVenda, String nomeOperacaoVenda, String cdTipoOper, String tipoFinalidade, 
            String emiteNfeVenda, String emiteNfeServico, String geraCobranca, String cdContrato, String cdCCusto, String usuarioCadastro, String dataCadastro,
            String dataModificacao, String situacao){
        setCdOperacaoVenda(cdOperacaoVenda);
        setNomeOperacaoVenda(nomeOperacaoVenda);
        setCdTipoOper(cdTipoOper);
        setTipoFinalidade(tipoFinalidade);
        setEmiteNfeVenda(emiteNfeVenda);
        setEmiteNfeServico(emiteNfeServico);
        setGeraCobranca(geraCobranca);
        setCdContrato(cdContrato);
        setCdCCusto(cdCCusto);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdOperacaoVenda
     */
    public String getCdOperacaoVenda() {
        return cdOperacaoVenda;
    }

    /**
     * @param cdOperacaoVenda the cdOperacaoVenda to set
     */
    public void setCdOperacaoVenda(String cdOperacaoVenda) {
        this.cdOperacaoVenda = cdOperacaoVenda;
    }

    /**
     * @return the nomeOperacaoVenda
     */
    public String getNomeOperacaoVenda() {
        return nomeOperacaoVenda;
    }

    /**
     * @param nomeOperacaoVenda the nomeOperacaoVenda to set
     */
    public void setNomeOperacaoVenda(String nomeOperacaoVenda) {
        this.nomeOperacaoVenda = nomeOperacaoVenda;
    }

    /**
     * @return the tipoFinalidade
     */
    public String getTipoFinalidade() {
        return tipoFinalidade;
    }

    /**
     * @param tipoFinalidade the tipoFinalidade to set
     */
    public void setTipoFinalidade(String tipoFinalidade) {
        this.tipoFinalidade = tipoFinalidade;
    }

    /**
     * @return the emiteNfeVenda
     */
    public String getEmiteNfeVenda() {
        return emiteNfeVenda;
    }

    /**
     * @param emiteNfeVenda the emiteNfeVenda to set
     */
    public void setEmiteNfeVenda(String emiteNfeVenda) {
        this.emiteNfeVenda = emiteNfeVenda;
    }

    /**
     * @return the emiteNfeServico
     */
    public String getEmiteNfeServico() {
        return emiteNfeServico;
    }

    /**
     * @param emiteNfeServico the emiteNfeServico to set
     */
    public void setEmiteNfeServico(String emiteNfeServico) {
        this.emiteNfeServico = emiteNfeServico;
    }

    /**
     * @return the geraCobranca
     */
    public String getGeraCobranca() {
        return geraCobranca;
    }

    /**
     * @param geraCobranca the geraCobranca to set
     */
    public void setGeraCobranca(String geraCobranca) {
        this.geraCobranca = geraCobranca;
    }

    /**
     * @return the cdContrato
     */
    public String getCdContrato() {
        return cdContrato;
    }

    /**
     * @param cdContrato the cdContrato to set
     */
    public void setCdContrato(String cdContrato) {
        this.cdContrato = cdContrato;
    }

    /**
     * @return the cdCCusto
     */
    public String getCdCCusto() {
        return cdCCusto;
    }

    /**
     * @param cdCCusto the cdCCusto to set
     */
    public void setCdCCusto(String cdCCusto) {
        this.cdCCusto = cdCCusto;
    }

    /**
     * @return the nomeCCusto
     */
    public String getNomeCCusto() {
        return nomeCCusto;
    }

    /**
     * @param nomeCCusto the nomeCCusto to set
     */
    public void setNomeCCusto(String nomeCCusto) {
        this.nomeCCusto = nomeCCusto;
    }
}