/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 22/11/2017
 */
public class Contato extends Clientes{
    private String cdAtendimento;
    private String horaAtendimento;
    private String dataAtendimento;
    private String cdVistoria;
    private String cdProposta;
    private String obs;
    private String horaCadastro;
    private String usuarioModificacao;
    private String horaModificacao;
    private boolean atualizacao;
    private boolean voltar;
    
    // construtor padrão da classe
    public Contato(){
        
    }
    
    // Construto padrão sobrecarregado
    public Contato(String cdAtendimento, String dataAtendimento, String horaAtendimento, String nomeCliente, String tipoPessoa,
            String telefone, String email, String cdVistoria, String cdProposta, String obs, String usuarioCadastro, 
            String dataCadastro, String horaCadastro, String usuarioModificacao, String dataModificacao, String horaModificacao, String situacao){
        setCdAtendimento(cdAtendimento);
        setDataAtendimento(dataAtendimento);
        setHoraAtendimento(horaAtendimento);
        setNomeRazaoSocial(nomeCliente);
        setTipoPessoa(tipoPessoa);
        setTelefone(telefone);
        setEmail(email);
        setCdVistoria(cdVistoria);
        setCdProposta(cdProposta);
        setObs(obs);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdAtendimento
     */
    public String getCdAtendimento() {
        return cdAtendimento;
    }

    /**
     * @param cdAtendimento the cdAtendimento to set
     */
    public void setCdAtendimento(String cdAtendimento) {
        this.cdAtendimento = cdAtendimento;
    }

    /**
     * @return the dataAtendimento
     */
    public String getDataAtendimento() {
        return dataAtendimento;
    }

    /**
     * @param dataAtendimento the dataAtendimento to set
     */
    public void setDataAtendimento(String dataAtendimento) {
        this.dataAtendimento = dataAtendimento;
    }

    /**
     * @return the cdVistoria
     */
    public String getCdVistoria() {
        return cdVistoria;
    }

    /**
     * @param cdVistoria the cdVistoria to set
     */
    public void setCdVistoria(String cdVistoria) {
        this.cdVistoria = cdVistoria;
    }

    /**
     * @return the obs
     */
    public String getObs() {
        return obs;
    }

    /**
     * @param obs the obs to set
     */
    public void setObs(String obs) {
        this.obs = obs;
    }

    /**
     * @return the horaAtendimento
     */
    public String getHoraAtendimento() {
        return horaAtendimento;
    }

    /**
     * @param horaAtendimento the horaAtendimento to set
     */
    public void setHoraAtendimento(String horaAtendimento) {
        this.horaAtendimento = horaAtendimento;
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

    /**
     * @return the voltar
     */
    public boolean isVoltar() {
        return voltar;
    }

    /**
     * @param voltar the voltar to set
     */
    public void setVoltar(boolean voltar) {
        this.voltar = voltar;
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