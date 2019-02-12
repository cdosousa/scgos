/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 * 
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 criado em 21/11/2018
 */
public class TipoServicoEDI extends Portadores{
    private String cdTipoServico;
    private String nomeTipoServico;
    
    /**
     * Construtor padrão da classe
     */
    public TipoServicoEDI(){
        
    }
    
    /**
     * construtor sobrecarregado da classe para injeção dos dados nas variáveis.
     * @param cdPortador
     * @param cdTipoServico
     * @param nomeTipoServico
     * @param usuarioCadastro
     * @param dataCadastro
     * @param horaCadastro
     * @param usuarioModificacao
     * @param dataModificacao
     * @param horaModificacao
     * @param situacao 
     */
    public TipoServicoEDI(String cdPortador, String cdTipoServico, String nomeTipoServico, String usuarioCadastro, String dataCadastro,
            String horaCadastro, String usuarioModificacao, String dataModificacao, String horaModificacao, String situacao){
        setCdPortador(cdPortador);
        setCdTipoServico(cdTipoServico);
        setNomeTipoServico(nomeTipoServico);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdTipoServico
     */
    public String getCdTipoServico() {
        return cdTipoServico;
    }

    /**
     * @param cdTipoServico the cdTipoServico to set
     */
    public void setCdTipoServico(String cdTipoServico) {
        this.cdTipoServico = cdTipoServico;
    }

    /**
     * @return the nomeTipoServico
     */
    public String getNomeTipoServico() {
        return nomeTipoServico;
    }

    /**
     * @param nomeTipoServico the nomeTipoServico to set
     */
    public void setNomeTipoServico(String nomeTipoServico) {
        this.nomeTipoServico = nomeTipoServico;
    }
}
