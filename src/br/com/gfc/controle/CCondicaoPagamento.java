/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.gfc.dao.CondicaoPagamentoDAO;
import br.com.gfc.modelo.Portadores;
import br.com.gfc.modelo.CondicaoPagamento;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 10/11/2017
 */
public class CCondicaoPagamento {

    // variáveis de instancia
    private Connection conexao;
    private List<CondicaoPagamento> resultado = null;
    private CondicaoPagamento regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    private String cdCondPag;
    
    // Construtor padrão
    public CCondicaoPagamento(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<CondicaoPagamento>();
        CondicaoPagamentoDAO cpdao = new CondicaoPagamentoDAO(conexao);
        cpdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(CondicaoPagamento cp, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        cp.setCdCondpag(regAtual.getCdCondpag());
        cp.setNomeCondPag(regAtual.getNomeCondPag());
        cp.setNumParcelas(regAtual.getNumParcelas());
        cp.setDataCadastro(regAtual.getDataCadastro());
        cp.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                cp.setSituacao("1");
                break;
            case "I":
                cp.setSituacao("2");
                break;
        }
    }
}
