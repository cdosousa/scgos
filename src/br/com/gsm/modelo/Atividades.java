/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917
 * created on 22/09/2017
 */
public class Atividades {
    private String cdAtividade;
    private String nomeAtividade;
    private double valTotalTarefa;
    private double valTotalEquipamento;
    private double valTotalAtividade;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;
    private boolean atualizacao = false;
   
    //construtor padrão
    public Atividades(){
                
    }
    
    //construtor padrão sobrecarregado
    public Atividades(String cdAtividade, String nomeAtividade, double valTotalTarefa, double valTotalEquipamento, double valTotalAtividade, 
            String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdAtividade(cdAtividade);
        setNomeAtividade(nomeAtividade);
        setValTotalTarefa(valTotalTarefa);
        setValTotalEquipamento(valTotalEquipamento);
        setValTotalAtividade(valTotalAtividade);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
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
     * @return the nomeAtividade
     */
    public String getNomeAtividade() {
        return nomeAtividade;
    }

    /**
     * @param nomeAtividade the nomeAtividade to set
     */
    public void setNomeAtividade(String nomeAtividade) {
        this.nomeAtividade = nomeAtividade;
    }

    /**
     * @return the valTotalTarefa
     */
    public double getValTotalTarefa() {
        return valTotalTarefa;
    }

    /**
     * @param valTotalTarefa the valTotalTarefa to set
     */
    public void setValTotalTarefa(double valTotalTarefa) {
        this.valTotalTarefa = valTotalTarefa;
    }

    /**
     * @return the valTotalEquipamento
     */
    public double getValTotalEquipamento() {
        return valTotalEquipamento;
    }

    /**
     * @param valTotalEquipamento the valTotalEquipamento to set
     */
    public void setValTotalEquipamento(double valTotalEquipamento) {
        this.valTotalEquipamento = valTotalEquipamento;
    }

    /**
     * @return the valTotalAtividade
     */
    public double getValTotalAtividade() {
        return valTotalAtividade;
    }

    /**
     * @param valTotalAtividade the valTotalAtividade to set
     */
    public void setValTotalAtividade(double valTotalAtividade) {
        this.valTotalAtividade = valTotalAtividade;
    }
    
    /**
     * @return the usuarioCadastro
     */
    public String getUsuarioCadastro() {
        return usuarioCadastro;
    }

    /**
     * @param usuarioCadastro the usuarioCadastro to set
     */
    public void setUsuarioCadastro(String usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    /**
     * @return the dataCadastro
     */
    public String getDataCadastro() {
        return dataCadastro;
    }

    /**
     * @param dataCadastro the dataCadastro to set
     */
    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    /**
     * @return the dataModificacao
     */
    public String getDataModificacao() {
        return dataModificacao;
    }

    /**
     * @param dataModificacao the dataModificacao to set
     */
    public void setDataModificacao(String dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    /**
     * @return the situacao
     */
    public String getSituacao() {
        return situacao;
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    /**
     * @return the atualizacao
     */
    public boolean isAtualizacao() {
        return atualizacao;
    }

    /**
     * @param atualizacao the atualizacao to set
     */
    public void setAtualizacao(boolean atualizacao) {
        this.atualizacao = atualizacao;
    }
}
