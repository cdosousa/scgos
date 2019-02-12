/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa criado em 24/09/2018
 * @version 0.01_beta0918
 */
public class Historico {
    private String cdHistorico;
    private String nomeHistorico;
    private String tipoHistorico;
    private String documentoComplementa;
    private String emissaoComplementa;
    private String empresaComplementa;
    private String usuarioCadastro;
    private String dataCadastro;
    private String horaCadastro;
    private String usuarioModificacao;
    private String dataModificacao;
    private String horaModificacao;
    private String situacao;
    
    /**
     * construtor padrão para a classe
     */
    public Historico(){
        
    }
    
    /**
     * Construtor sobrecarregado da classe
     */
    public Historico(String cdHistorico, String nomeHistorico, String tipoHistorico, String documentoComplementa, String emissaoComplementa,
    String empresaComplementa, String usuarioCadastro, String dataCadastro, String horaCadastro, String usuarioModificacao, String dataModificacao,
    String horaModificacao, String situacao){
        setCdHistorico(cdHistorico);
        setNomeHistorico(nomeHistorico);
        setTipoHistorico(tipoHistorico);
        setDocumentoComplementa(documentoComplementa);
        setEmissaoComplementa(emissaoComplementa);
        setEmpresaComplementa(empresaComplementa);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdHistorico
     */
    public String getCdHistorico() {
        return cdHistorico;
    }

    /**
     * @param cdHistorico the cdHistorico to set
     */
    public void setCdHistorico(String cdHistorico) {
        this.cdHistorico = cdHistorico;
    }

    /**
     * @return the nomeHistorico
     */
    public String getNomeHistorico() {
        return nomeHistorico;
    }

    /**
     * @param nomeHistorico the nomeHistorico to set
     */
    public void setNomeHistorico(String nomeHistorico) {
        this.nomeHistorico = nomeHistorico;
    }

    /**
     * @return the tipoHistorico
     */
    public String getTipoHistorico() {
        return tipoHistorico;
    }

    /**
     * @param tipoHistorico the tipoHistorico to set
     */
    public void setTipoHistorico(String tipoHistorico) {
        this.tipoHistorico = tipoHistorico;
    }

    /**
     * @return the documentoComplementa
     */
    public String getDocumentoComplementa() {
        return documentoComplementa;
    }

    /**
     * @param documentoComplementa the documentoComplementa to set
     */
    public void setDocumentoComplementa(String documentoComplementa) {
        this.documentoComplementa = documentoComplementa;
    }

    /**
     * @return the emissaoComplementa
     */
    public String getEmissaoComplementa() {
        return emissaoComplementa;
    }

    /**
     * @param emissaoComplementa the emissaoComplementa to set
     */
    public void setEmissaoComplementa(String emissaoComplementa) {
        this.emissaoComplementa = emissaoComplementa;
    }

    /**
     * @return the empresaComplementa
     */
    public String getEmpresaComplementa() {
        return empresaComplementa;
    }

    /**
     * @param empresaComplementa the empresaComplementa to set
     */
    public void setEmpresaComplementa(String empresaComplementa) {
        this.empresaComplementa = empresaComplementa;
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
     * @return the horaCadastro
     */
    public String getHoraCadastro() {
        return horaCadastro;
    }

    /**
     * @param horaCadastro the horaCadastro to set
     */
    public void setHoraCadastro(String horaCadastro) {
        this.horaCadastro = horaCadastro;
    }

    /**
     * @return the usuarioModificacao
     */
    public String getUsuarioModificacao() {
        return usuarioModificacao;
    }

    /**
     * @param usuarioModificacao the usuarioModificacao to set
     */
    public void setUsuarioModificacao(String usuarioModificacao) {
        this.usuarioModificacao = usuarioModificacao;
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
     * @return the horaModificacao
     */
    public String getHoraModificacao() {
        return horaModificacao;
    }

    /**
     * @param horaModificacao the horaModificacao to set
     */
    public void setHoraModificacao(String horaModificacao) {
        this.horaModificacao = horaModificacao;
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

}
