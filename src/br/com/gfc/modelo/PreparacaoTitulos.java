/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa criado em 17/09/2018
 * @version 0.01_beta0917
 */
public class PreparacaoTitulos extends PreparacaoPagamentos {
    
    /**
     * Construtor padrão da classe
     */
    public PreparacaoTitulos(){
        
    }
    
    public PreparacaoTitulos(String cdPreparacao, String cdLancamento, double valorSaldo, String usuarioCadastro, String dataCadastro, String horaCadastro,
            String usuarioModificacao, String dataModificacao, String horaModificacao, String situacao){
        setCdPreparacao(cdPreparacao);
        setCdLancamento(cdLancamento);
        setValorSaldo(valorSaldo);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }
}