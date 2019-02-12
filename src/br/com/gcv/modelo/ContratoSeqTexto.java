/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_0917beta
 * @created on 27/03/2018
 */

public class ContratoSeqTexto extends Contrato {
    private String cdSequencia;
    private String textoLongo;
    
    /**
     * Construtor padrão da classe
     */
    public ContratoSeqTexto(){
        
    }
    
    /**
     * Construtor sobrecarregado da Classe
     */
    public ContratoSeqTexto(String cdContrato, String cdSequencia, String textoLongo, String usuarioCadastro, String dataCadastro, String horaCadastro, 
            String usuarioModificacao, String dataModificacao, String horaModificacao, String situacao){
        setCdContrato(cdContrato);
        setCdSequencia(cdSequencia);
        setTextoLongo(textoLongo);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdSequencia
     */
    public String getCdSequencia() {
        return cdSequencia;
    }

    /**
     * @param cdSequencia the cdSequencia to set
     */
    public void setCdSequencia(String cdSequencia) {
        this.cdSequencia = cdSequencia;
    }

    /**
     * @return the textoLongo
     */
    public String getTextoLongo() {
        return textoLongo;
    }

    /**
     * @param textoLongo the textoLongo to set
     */
    public void setTextoLongo(String textoLongo) {
        this.textoLongo = textoLongo;
    }
}