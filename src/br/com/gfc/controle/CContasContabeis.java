/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.gfc.dao.ContasContabeisDAO;
import br.com.gfc.modelo.ContasContabeis;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917
 * created on 29/09/2017
 */
public class CContasContabeis {
    // variáveis de instancia
    private List<ContasContabeis> resultado = null;
    private ContasContabeis regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CContasContabeis(){
        
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<ContasContabeis>();
        ContasContabeisDAO pldao = new ContasContabeisDAO();
        pldao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(ContasContabeis pl, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        pl.setCdConta(regAtual.getCdConta());
        pl.setNomeConta(regAtual.getNomeConta());
        String tipoConta = regAtual.getTipoConta();
        switch(tipoConta){
            case "A":
                pl.setTipoConta("1");
                break;
            case "S":
                pl.setTipoConta("2");
                break;
        }
        String contaPatrimonial = regAtual.getContaPatrimonial();
        switch(contaPatrimonial){
            case "S":
                pl.setContaPatrimonial("1");
                break;
            case "N":
                pl.setContaPatrimonial("2");
                break;
        }
        String grandeMovimentacao;
        grandeMovimentacao = regAtual.getGrandeMovimentacao();
        switch(grandeMovimentacao){
            case "S":
                pl.setGrandeMovimentacao("1");
                break;
            case "N":
                pl.setGrandeMovimentacao("2");
                break;
        }
        pl.setEstabelecimento(regAtual.getEstabelecimento());
        pl.setReduzido(regAtual.getReduzido());
        pl.setCentroResultado(regAtual.getCentroResultado());
        pl.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        pl.setDataCadastro(regAtual.getDataCadastro());
        pl.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch(situacao){
            case "A":
                pl.setSituacao("1");
                break;
            case "I":
                pl.setSituacao("2");
                break;
        }
    }
}