/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 26/10/2017
 */
public class Equipes extends Tecnicos{
    private String cdEquipe;
    private String nomeEquipe;
    private boolean atualizacao = false;
    
    // construtor padrão da classe
    public Equipes(){
        super();
        
    }
    // construtor padrão da classe sobrecarregado
    public Equipes(String cdEquipe, String nomeEquipe, String usuarioCadastro, String dataCadastro, String dataModificacao, char situacao){
        setCdEquipe(cdEquipe);
        setNomeEquipe(nomeEquipe);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdEquipe
     */
    public String getCdEquipe() {
        return cdEquipe;
    }

    /**
     * @param cdEquipe the cdEquipe to set
     */
    public void setCdEquipe(String cdEquipe) {
        this.cdEquipe = cdEquipe;
    }

    /**
     * @return the nomeEquipe
     */
    public String getNomeEquipe() {
        return nomeEquipe;
    }

    /**
     * @param nomeEquipe the nomeEquipe to set
     */
    public void setNomeEquipe(String nomeEquipe) {
        this.nomeEquipe = nomeEquipe;
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