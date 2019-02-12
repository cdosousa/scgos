/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcs.controle;

import br.com.gcs.dao.EssenciaProdutosDAO;
import br.com.gcs.modelo.EssenciaProdutos;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristiano
 */
public class CEssenciaProdutos {
    // variáveis de instancia
    private List<EssenciaProdutos> resultado = null;
    private EssenciaProdutos regAtual;
    private Connection conexao;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CEssenciaProdutos(Connection conexao){
        this.conexao = conexao;
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<EssenciaProdutos>();
        EssenciaProdutosDAO epdao = new EssenciaProdutosDAO(conexao);
        epdao.selecionar(resultado, sql);
        numReg = resultado.size();
        //epdao.desconectar();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(EssenciaProdutos ep, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        ep.setCdEssencia(regAtual.getCdEssencia());
        ep.setNomeEssencia(regAtual.getNomeEssencia());
        String gerarCodigo = regAtual.getGerarCodigo();
        switch(gerarCodigo){
            case "S":
                ep.setGerarCodigo("1");
                break;
            case "N":
                ep.setGerarCodigo("2");
                break;
        }
        ep.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        ep.setDataCadastro(regAtual.getDataCadastro());
        ep.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch(situacao){
            case "A":
                ep.setSituacao("1");
                break;
            case "I":
                ep.setSituacao("2");
                break;
        }
    }
}