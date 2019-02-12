/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_0917beta criado em 27/03/2018
 */
public class ContratoProposta extends Contrato{
    private String cdProposta;
    
    /**
     * Construto padrão da classe
     */
    public ContratoProposta(){
        
    }
    
    /**
     * Construtor sobrecarregado da Classe
     */
    public ContratoProposta(String cdContrado, String cdProposta, String usuarioCadastro, String dataCadastro, String horaCadastro,
            String usuarioModificacao, String dataModificacao, String horaModificacao, String situacao){
        setCdContrato(cdContrado);
        setCdProposta(cdProposta);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdProposta
     */
    public String getCdProposta() {
        return cdProposta;
    }

    /**
     * @param cdProposta the cdProposta to set
     */
    public void setCdProposta(String cdProposta) {
        this.cdProposta = cdProposta;
    }
}