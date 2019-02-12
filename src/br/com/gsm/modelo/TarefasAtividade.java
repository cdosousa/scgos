/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.modelo;

/**
 *
 * @author cristiano
 */
public class TarefasAtividade extends Atividades{
    private String sequencia;
    private String cdTarefa;
    private String cdEspecialidade;
    private double valorUnit;
    // variáveis de instâncias de tabelas correlatas
    private String nomeTarefa;
    private String nomeEspecialidade;
    
    // construtor padrao da classe
    public TarefasAtividade(){
        super();
    }
    
    // construtor sobrecarregado
    public TarefasAtividade(String sequencia, String cdAtividade, String cdTarefa, String cdEspecialidade, double valorUnit, String usuarioCadastro,
            String dataCadastro, String dataModificacao, String situacao){
        setSequencia(sequencia);
        setCdAtividade(cdAtividade);
        setCdTarefa(cdTarefa);
        setCdEspecialidade(cdEspecialidade);
        setCdEspecialidade(cdEspecialidade);
        setValorUnit(valorUnit);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }
    
    // construtor para correlatos
    public TarefasAtividade(String nomeTarefa, String nomeEspecialidade){
        setNomeTarefa(nomeTarefa);
        setNomeEspecialidade(nomeEspecialidade);
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
     * @return the cdTarefa
     */
    public String getCdTarefa() {
        return cdTarefa;
    }

    /**
     * @param cdTarefa the cdTarefa to set
     */
    public void setCdTarefa(String cdTarefa) {
        this.cdTarefa = cdTarefa;
    }

    /**
     * @return the cdEspecialidade
     */
    public String getCdEspecialidade() {
        return cdEspecialidade;
    }

    /**
     * @param cdEspecialidade the cdEspecialidade to set
     */
    public void setCdEspecialidade(String cdEspecialidade) {
        this.cdEspecialidade = cdEspecialidade;
    }

    /**
     * @return the valorUnit
     */
    public double getValorUnit() {
        return valorUnit;
    }
    
    /**
     * @param valorUnit the valorUnit to set
     */
    public void setValorUnit(double valorUnit) {
        this.valorUnit = valorUnit;
    }

    /**
     * @return the nomeTarefa
     */
    public String getNomeTarefa() {
        return nomeTarefa;
    }

    /**
     * @param nomeTarefa the nomeTarefa to set
     */
    public void setNomeTarefa(String nomeTarefa) {
        this.nomeTarefa = nomeTarefa;
    }

    /**
     * @return the nomeEspecialidade
     */
    public String getNomeEspecialidade() {
        return nomeEspecialidade;
    }

    /**
     * @param nomeEspecialidade the nomeEspecialidade to set
     */
    public void setNomeEspecialidade(String nomeEspecialidade) {
        this.nomeEspecialidade = nomeEspecialidade;
    }
}
