/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfr.controle;

import br.com.gfr.dao.OrigemProdutoDAO;
import br.com.gfr.modelo.OrigemProduto;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917
 * created on 02/10/2017
 */
public class COrigemProduto {
    // variáveis de instancia
    private List<OrigemProduto> resultado = null;
    private OrigemProduto regAtual;
    private Connection conexao;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public COrigemProduto(Connection conexao){
        this.conexao = conexao;
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<OrigemProduto>();
        OrigemProdutoDAO opdao = new OrigemProdutoDAO(conexao);
        opdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(OrigemProduto op, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        op.setCdOrigem(regAtual.getCdOrigem());
        op.setNomeOrigem(regAtual.getNomeOrigem());
        op.setFinalidade(regAtual.getFinalidade());
        op.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        op.setDataCadastro(regAtual.getDataCadastro());
        op.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch(situacao){
            case "A":
                op.setSituacao("1");
                break;
            case "I":
                op.setSituacao("2");
                break;
        }
    }
}