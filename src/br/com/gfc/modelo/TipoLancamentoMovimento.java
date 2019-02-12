/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 */
public class TipoLancamentoMovimento extends TipoLancamento{
    private String cdTipoMovimento;
    private String nomeTipoMovimento;
    
    public TipoLancamentoMovimento(){
        super();
    }
    
    public TipoLancamentoMovimento(String cdTipoMovimento, String cdTipoLancamento, String usuarioCadastro,
            String dataCadastro, String horaCadastro, String usuarioModificacao, String dataModificacao,
            String horaModificacao, String situacao){
        setCdTipoMovimento(cdTipoMovimento);
        setCdTipoLancamento(cdTipoLancamento);
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
    
}
