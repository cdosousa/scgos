/* * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 criado em 27/02/2018
 */
public class TipoMovimento extends ContasContabeis{
    private String cdTipoMovimento;
    private String nomeTipoMovimento;
    private String cdContaReduzida;
    private String situacaoLancamento;
    private String situacaoContraPartida;
    private String horaCadastro;
    private String usuarioModificacao;
    private String horaModificacao;
    
    public TipoMovimento(){
        
    }
    
    public TipoMovimento(String cdTipoMovimento, String nomeTipoMovimento, String cdContaReduzida,
            String situacaoLancamento, String situacaoContraPartida, String usuarioCadastro,
            String dataCadastro, String horaCadastro, String usuarioModificacao, String dataModificacao,
            String horaModificacao, String situacao){
        setCdTipoMovimento(cdTipoMovimento);
        setNomeTipoMovimento(nomeTipoMovimento);
        setCdContaReduzida(cdContaReduzida);
        setSituacaoLancamento(situacaoLancamento);
        setSituacaoContraPartida(situacaoContraPartida);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdTipoMovimento
     */
    public String getCdTipoMovimento() {
        return cdTipoMovimento;
    }

    /**
     * @param cdTipoMovimento the cdTipoMovimento to set
     */
    public void setCdTipoMovimento(String cdTipoMovimento) {
        this.cdTipoMovimento = cdTipoMovimento;
    }

    /**
     * @return the nomeTipoMovimento
     */
    public String getNomeTipoMovimento() {
        return nomeTipoMovimento;
    }

    /**
     * @param nomeTipoMovimento the nomeTipoMovimento to set
     */
    public void setNomeTipoMovimento(String nomeTipoMovimento) {
        this.nomeTipoMovimento = nomeTipoMovimento;
    }

    /**
     * @return the cdContaReduzida
     */
    public String getCdContaReduzida() {
        return cdContaReduzida;
    }

    /**
     * @param cdContaReduzida the cdContaReduzida to set
     */
    public void setCdContaReduzida(String cdContaReduzida) {
        this.cdContaReduzida = cdContaReduzida;
    }

    /**
     * @return the situacaoLancamento
     */
    public String getSituacaoLancamento() {
        return situacaoLancamento;
    }

    /**
     * @param situacaoLancamento the situacaoLancamento to set
     */
    public void setSituacaoLancamento(String situacaoLancamento) {
        this.situacaoLancamento = situacaoLancamento;
    }

    /**
     * @return the situacaoContraPartida
     */
    public String getSituacaoContraPartida() {
        return situacaoContraPartida;
    }

    /**
     * @param situacaoContraPartida the situacaoContraPartida to set
     */
    public void setSituacaoContraPartida(String situacaoContraPartida) {
        this.situacaoContraPartida = situacaoContraPartida;
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
}
