/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcs.controle;

import br.com.gcs.dao.CategoriasDAO;
import br.com.gcs.modelo.Categorias;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristiano
 */
public class CCategorias {
    // variáveis de instancia
    private List<Categorias> resultado = null;
    private Categorias regAtual;
    private Connection conexao;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CCategorias(Connection conexao){
        this.conexao = conexao;
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<Categorias>();
        CategoriasDAO catdao = new CategoriasDAO(conexao);
        catdao.selecionar(resultado, sql);
        numReg = resultado.size();
        //catdao.desconectar();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Categorias cat, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        cat.setCdCategoria(regAtual.getCdCategoria());
        cat.setNomeCategoria(regAtual.getNomeCategoria());
        String gerarCodigo = regAtual.getGerarCodigo();
        switch(gerarCodigo){
            case "S":
                cat.setGerarCodigo("1");
                break;
            case "N":
                cat.setGerarCodigo("2");
                break;
        }
        cat.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        cat.setDataCadastro(regAtual.getDataCadastro());
        cat.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch(situacao){
            case "A":
                cat.setSituacao("1");
                break;
            case "I":
                cat.setSituacao("2");
                break;
        }
    }
}