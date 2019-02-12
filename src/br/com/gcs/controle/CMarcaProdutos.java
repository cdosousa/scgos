/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcs.controle;

import br.com.gcs.dao.MarcaProdutosDAO;
import br.com.gcs.modelo.MarcaProdutos;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristiano
 */
public class CMarcaProdutos {
    // variáveis de instancia
    private List<MarcaProdutos> resultado = null;
    private MarcaProdutos regAtual;
    private Connection conexao;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CMarcaProdutos(Connection conexao){
        this.conexao = conexao;
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<MarcaProdutos>();
        MarcaProdutosDAO mpdao = new MarcaProdutosDAO(conexao);
        mpdao.selecionar(resultado, sql);
        numReg = resultado.size();
        //mpdao.desconectar();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(MarcaProdutos mp, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        mp.setCdMarca(regAtual.getCdMarca());
        mp.setNomeMarca(regAtual.getNomeMarca());
        mp.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        mp.setDataCadastro(regAtual.getDataCadastro());
        mp.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch(situacao){
            case "A":
                mp.setSituacao("1");
                break;
            case "I":
                mp.setSituacao("2");
                break;
        }
    }
}