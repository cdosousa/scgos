/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.gcv.dao.TabelaDAO;
import br.com.gcv.modelo.Tabela;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 21/11/2017
 */
public class CTabela {

    // variáveis de instancia
    private Connection conexao;
    private List<Tabela> resultado = null;
    private Tabela regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CTabela(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<Tabela>();
        TabelaDAO tabdao = new TabelaDAO(conexao);
        tabdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Tabela tab, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        tab.setCdTabela(regAtual.getCdTabela());
        tab.setNomeTabela(regAtual.getNomeTabela());
        tab.setDataVigencia(regAtual.getDataVigencia());
        tab.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        tab.setDataCadastro(regAtual.getDataCadastro());
        tab.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                tab.setSituacao("1");
                break;
            case "I":
                tab.setSituacao("2");
                break;
        }
    }
}
