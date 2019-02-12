/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 criado em 27/02/2018
 */
public class TipoLancamento extends Portadores {
    private String cdTipoLancamento;
    private String nomeTipoLancamento;
    private int prefixo;
    private String horaCadastro;
    private String usuarioModificacao;
    private String horaModificacao;
    
    public TipoLancamento(){
        
    }
    
    public TipoLancamento(String cdTipoLancamento, String nomeTipoLancamento, String cdPortador, int prefixo,
            String usuarioCadastro, String dataCadastro, String horaCadastro, String usuarioModificacao,
            String dataModificacao, String horaModificacao, String situacao){
        setCdTipoLancamento(cdTipoLancamento);
        setNomeTipoLancamento(nomeTipoLancamento);
        setCdPortador(cdPortador);
        setPrefixo(prefixo);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdTipoLancamento
     */
    public String getCdTipoLancamento() {
        return cdTipoLancamento;
    }

    /**
     * @param cdTipoLancamento the cdTipoLancamento to set
     */
    public void setCdTipoLancamento(String cdTipoLancamento) {
        this.cdTipoLancamento = cdTipoLancamento;
    }

    /**
     * @return the nomeTipoLancamento
     */
    public String getNomeTipoLancamento() {
        return nomeTipoLancamento;
    }

    /**
     * @param nomeTipoLancamento the nomeTipoLancamento to set
     */
    public void setNomeTipoLancamento(String nomeTipoLancamento) {
        this.nomeTipoLancamento = nomeTipoLancamento;
    }

    /**
     * @return the prefixo
     */
    public int getPrefixo() {
        return prefixo;
    }

    /**
     * @param prefixo the prefixo to set
     */
    public void setPrefixo(int prefixo) {
        this.prefixo = prefixo;
    }

    /**
     * @return the horaCadastro
     */
    public String getHoraCadastro() {
        return horaCadastro;
    }

    /**
     * @return the usuarioModificacao
     */
    public String getUsuarioModificacao() {
        return usuarioModificacao;
    }

    /**
     * @return the horaModificacao
     */
    public String getHoraModificacao() {
        return horaModificacao;
    }

    /**
     * @param horaCadastro the horaCadastro to set
     */
    public void setHoraCadastro(String horaCadastro) {
        this.horaCadastro = horaCadastro;
    }

    /**
     * @param usuarioModificacao the usuarioModificacao to set
     */
    public void setUsuarioModificacao(String usuarioModificacao) {
        this.usuarioModificacao = usuarioModificacao;
    }

    /**
     * @param horaModificacao the horaModificacao to set
     */
    public void setHoraModificacao(String horaModificacao) {
        this.horaModificacao = horaModificacao;
    }
}
