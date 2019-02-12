/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_092017beta criado em 21/03/2018
 */
public class ContratoSequencia extends Contrato{
    private String cdSequencia;
    private String cdSequenciaPai;
    private String tipoSequencia;
    private String cdSequenciaAtencessora;
    private String titulo;
    private String posicaoTitulo;
    private String possuiTexto;
    private String quebraLinha;
    
    /**
     * Construtor padrão da classe
     */
    public ContratoSequencia(){
        
    }
    
    /**
     * Construtor sobrecarregado da Classe
     */
    public ContratoSequencia(String cdContrato, String cdSequencia, String cdSequenciaPai, String tipoSequencia, String cdSequenciaAtencessora, String titulo, String posicaoTitulo,
            String possuiTexto, String quebraLinha, String usuarioCadastro, String dataCadastro, String horaCadastro, String usuarioModificacao, String dataModificacao,
            String horaModificacao, String situacao){
        setCdContrato(cdContrato);
        setCdSequencia(cdSequencia);
        setCdSequenciaPai(cdSequenciaPai);
        setTipoSequencia(tipoSequencia);
        setCdSequenciaAtencessora(cdSequenciaAtencessora);
        setTitulo(titulo);
        setPosicaoTitulo(posicaoTitulo);
        setPossuiTexto(possuiTexto);
        setQuebraLinha(quebraLinha);
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
     * @return the cdSequenciaPai
     */
    public String getCdSequenciaPai() {
        return cdSequenciaPai;
    }

    /**
     * @param cdSequenciaPai the cdSequenciaPai to set
     */
    public void setCdSequenciaPai(String cdSequenciaPai) {
        this.cdSequenciaPai = cdSequenciaPai;
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the possuiTexto
     */
    public String getPossuiTexto() {
        return possuiTexto;
    }

    /**
     * @param possuiTexto the possuiTexto to set
     */
    public void setPossuiTexto(String possuiTexto) {
        this.possuiTexto = possuiTexto;
    }

    /**
     * @return the tipoSequencia
     */
    public String getTipoSequencia() {
        return tipoSequencia;
    }

    /**
     * @param tipoSequencia the tipoSequencia to set
     */
    public void setTipoSequencia(String tipoSequencia) {
        this.tipoSequencia = tipoSequencia;
    }

    /**
     * @return the quebraLinha
     */
    public String getQuebraLinha() {
        return quebraLinha;
    }

    /**
     * @param quebraLinha the quebraLinha to set
     */
    public void setQuebraLinha(String quebraLinha) {
        this.quebraLinha = quebraLinha;
    }

    /**
     * @return the cdSequenciaAtencessora
     */
    public String getCdSequenciaAtencessora() {
        return cdSequenciaAtencessora;
    }

    /**
     * @param cdSequenciaAtencessora the cdSequenciaAtencessora to set
     */
    public void setCdSequenciaAtencessora(String cdSequenciaAtencessora) {
        this.cdSequenciaAtencessora = cdSequenciaAtencessora;
    }

    /**
     * @return the posicaoTitulo
     */
    public String getPosicaoTitulo() {
        return posicaoTitulo;
    }

    /**
     * @param posicaoTitulo the posicaoTitulo to set
     */
    public void setPosicaoTitulo(String posicaoTitulo) {
        this.posicaoTitulo = posicaoTitulo;
    }
}