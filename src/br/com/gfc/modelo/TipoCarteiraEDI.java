/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @Version 0.01_beta0917 criado em 23/11/2018
 */
public class TipoCarteiraEDI extends Portadores{
    private String cdCarteira;
    private String nomeCarteira;
    
    /**
     * Construtor padrao da classe
     */
    public TipoCarteiraEDI(){
        
    }
    
    /**
     * Construtor sobrecarregado da classe
     * @param cdPortador
     * @param cdCarteira
     * @param nomeCarteira
     * @param usuarioCadastro
     * @param dataCadastro
     * @param horaCadastro
     * @param usuarioModificacao
     * @param dataModificacao
     * @param horaModificacao
     * @param situacao 
     */
    public TipoCarteiraEDI(String cdPortador, String cdCarteira, String nomeCarteira, String usuarioCadastro, String dataCadastro,
            String horaCadastro, String usuarioModificacao, String dataModificacao, String horaModificacao, String situacao){
        setCdPortador(cdPortador);
        setCdCarteira(cdCarteira);
        setNomeCarteira(nomeCarteira);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdCarteira
     */
    public String getCdCarteira() {
        return cdCarteira;
    }

    /**
     * @param cdCarteira the cdCarteira to set
     */
    public void setCdCarteira(String cdCarteira) {
        this.cdCarteira = cdCarteira;
    }

    /**
     * @return the nomeCarteira
     */
    public String getNomeCarteira() {
        return nomeCarteira;
    }

    /**
     * @param nomeCarteira the nomeCarteira to set
     */
    public void setNomeCarteira(String nomeCarteira) {
        this.nomeCarteira = nomeCarteira;
    }
}
