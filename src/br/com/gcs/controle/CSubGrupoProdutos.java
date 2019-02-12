/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcs.controle;

import br.com.gcs.dao.SubGrupoProdutosDAO;
import br.com.gcs.modelo.SubGrupoProdutos;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristiano
 */
public class CSubGrupoProdutos {
    // variáveis de instancia
    private List<SubGrupoProdutos> resultado = null;
    private SubGrupoProdutos regAtual;
    private Connection conexao;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CSubGrupoProdutos(Connection conexao){
        this.conexao = conexao;
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<SubGrupoProdutos>();
        SubGrupoProdutosDAO sgpdao = new SubGrupoProdutosDAO(conexao);
        sgpdao.selecionar(resultado, sql);
        numReg = resultado.size();
        //sgpdao.desconectar();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(SubGrupoProdutos sgp, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        sgp.setCdSubGrupo(regAtual.getCdSubGrupo());
        sgp.setCdGrupo(regAtual.getCdGrupo());
        sgp.setNomeSubGrupo(regAtual.getNomeSubGrupo());
        String gerarCodigo = regAtual.getGerarCodigo();
        switch(gerarCodigo){
            case "S":
                sgp.setGerarCodigo("1");
                break;
            case "N":
                sgp.setGerarCodigo("2");
                break;
        }
        sgp.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        sgp.setDataCadastro(regAtual.getDataCadastro());
        sgp.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch(situacao){
            case "A":
                sgp.setSituacao("1");
                break;
            case "I":
                sgp.setSituacao("2");
                break;
        }
    }
}