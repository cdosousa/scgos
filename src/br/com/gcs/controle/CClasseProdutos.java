/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcs.controle;

import br.com.gcs.dao.ClasseProdutosDAO;
import br.com.gcs.modelo.ClasseProdutos;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristiano
 */
public class CClasseProdutos {
    // variáveis de instancia
    private List<ClasseProdutos> resultado = null;
    private ClasseProdutos regAtual;
    private Connection conexao;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CClasseProdutos(Connection conexao){
        this.conexao = conexao;
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<ClasseProdutos>();
        ClasseProdutosDAO cpdao = new ClasseProdutosDAO(conexao);
        cpdao.selecionar(resultado, sql);
        numReg = resultado.size();
        //cpdao.desconectar();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(ClasseProdutos cp, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        cp.setCdClasse(regAtual.getCdClasse());
        cp.setNomeClasse(regAtual.getNomeClasse());
        cp.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        cp.setDataCadastro(regAtual.getDataCadastro());
        cp.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch(situacao){
            case "A":
                cp.setSituacao("1");
                break;
            case "I":
                cp.setSituacao("2");
                break;
        }
    }
}