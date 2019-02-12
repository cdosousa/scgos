/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcs.controle;

import br.com.gcs.dao.GrupoProdutosDAO;
import br.com.gcs.modelo.GrupoProdutos;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristiano
 */
public class CGrupoProdutos {
    // variáveis de instancia
    private List<GrupoProdutos> resultado = null;
    private GrupoProdutos regAtual;
    private Connection conexao;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CGrupoProdutos(Connection conexao){
        this.conexao = conexao;
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<GrupoProdutos>();
        GrupoProdutosDAO gpdao = new GrupoProdutosDAO(conexao);
        gpdao.selecionar(resultado, sql);
        numReg = resultado.size();
        //gpdao.desconectar();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(GrupoProdutos gp, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        gp.setCdGrupo(regAtual.getCdGrupo());
        gp.setNomeGrupo(regAtual.getNomeGrupo());
        String gerarCodigo = regAtual.getGerarCodigo();
        switch(gerarCodigo){
            case "S":
                gp.setGerarCodigo("1");
                break;
            case "N":
                gp.setGerarCodigo("2");
                break;
        }
        gp.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        gp.setDataCadastro(regAtual.getDataCadastro());
        gp.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch(situacao){
            case "A":
                gp.setSituacao("1");
                break;
            case "I":
                gp.setSituacao("2");
                break;
        }
    }
}