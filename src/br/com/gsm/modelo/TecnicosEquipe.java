/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 26/10/2017
 */
public class TecnicosEquipe extends Equipes{
    private String cpfTecnico;
    
    // construtor padrão da classe
    public TecnicosEquipe(){
        super();
    }
    // construtor padrão da classe sobrecarregado
    public TecnicosEquipe(String cdEquipe, String cpfTecnico, String cdEspecialidade, char pagarIndicacao, char pagarObra, char pagarComissao,
            double percObra, double valorObra, double percIndicacao, double valorIndicacao, double percComissao, String usuarioCadastro,
            String dataCadastro, String dataModificacao, char situacao){
        setCdEquipe(cdEquipe);
        setCpfTecnico(cpfTecnico);
        setCdEspecialidade(cdEspecialidade);
        setPagarIndicacao(pagarIndicacao);
        setPagarObra(pagarObra);
        setPagarComissao(pagarComissao);
        setPercObra(percObra);
        setValorObra(valorObra);
        setPercIndicacao(percIndicacao);
        setValorIndicacao(valorIndicacao);
        setPercComissao(percComissao);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cpfTecnico
     */
    public String getCpfTecnico() {
        return cpfTecnico;
    }

    /**
     * @param cpfTecnico the cpfTecnico to set
     */
    public void setCpfTecnico(String cpfTecnico) {
        this.cpfTecnico = cpfTecnico;
    }
}