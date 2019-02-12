/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * 
 */
public class AcabamentoItemPed extends ItemPedido{
    private String tipoAcabamento;
    private String nomeTipoAcabamento;
    
    /**
     * Esta classe extende o item do pedido necessário apenas o construtor
     */
    public AcabamentoItemPed(){
        
    }
    
    /**
     * Construtor padrão sobrecarregado
     */
    public AcabamentoItemPed(String cdPedido, int sequencia, String cdMaterial, String usuarioCadastro, String dataCadastro, String horaCadastro,
            String usuarioModificao, String dataModificacao, String horaModificacao){
        setCdPedido(cdPedido);
        setSequencia(sequencia);
        setCdMaterial(cdMaterial);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
    }

    /**
     * @return the tipoAcabamento
     */
    public String getTipoAcabamento() {
        return tipoAcabamento;
    }

    /**
     * @param tipoAcabamento the tipoAcabamento to set
     */
    public void setTipoAcabamento(String tipoAcabamento) {
        this.tipoAcabamento = tipoAcabamento;
    }

    /**
     * @return the nomeTipoAcabamento
     */
    public String getNomeTipoAcabamento() {
        return nomeTipoAcabamento;
    }

    /**
     * @param nomeTipoAcabamento the nomeTipoAcabamento to set
     */
    public void setNomeTipoAcabamento(String nomeTipoAcabamento) {
        this.nomeTipoAcabamento = nomeTipoAcabamento;
    }
}
