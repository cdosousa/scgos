/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.controle;

import br.com.gsm.dao.TarefasDAO;
import br.com.gsm.modelo.Tarefas;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917
 * created on 02/10/2017
 */
public class CTarefas {
    // variáveis de instancia
    private List<Tarefas> resultado = null;
    private Tarefas regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CTarefas(){
        
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<Tarefas>();
        TarefasDAO tadao = new TarefasDAO();
        tadao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Tarefas ta, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        ta.setCdTarefa(regAtual.getCdTarefa());
        ta.setNomeTarefa(regAtual.getNomeTarefa());
        ta.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        ta.setDataCadastro(regAtual.getDataCadastro());
        ta.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch(situacao){
            case "A":
                ta.setSituacao("1");
                break;
            case "I":
                ta.setSituacao("2");
                break;
        }
    }
}