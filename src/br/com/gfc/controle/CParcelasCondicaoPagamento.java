/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.gfc.dao.ParcelasCondicaoPagamentoDAO;
import br.com.gfc.modelo.ParcelasCondicaoPagamento;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 10/11/2017
 */
public class CParcelasCondicaoPagamento {

    // variáveis de instancia
    private Connection conexao;
    private List<ParcelasCondicaoPagamento> resultado = null;
    private ParcelasCondicaoPagamento regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    private String cdPortador;

    // Construtor padrão
    public CParcelasCondicaoPagamento(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<ParcelasCondicaoPagamento>();
        ParcelasCondicaoPagamentoDAO pcpdao = new ParcelasCondicaoPagamentoDAO(conexao);
        pcpdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(ParcelasCondicaoPagamento pcp, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        pcp.setCdCondpag(regAtual.getCdCondpag());
        pcp.setCdParcela(regAtual.getCdParcela());
        pcp.setPrazoDias(regAtual.getPrazoDias());
        pcp.setPercRateio(regAtual.getPercRateio());
        pcp.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        pcp.setDataCadastro(regAtual.getDataCadastro());
        pcp.setDataModificacao(regAtual.getDataModificacao());
        String situacao;
        if (regAtual.getSituacao() != null) {
            situacao = regAtual.getSituacao();
        }else{
            situacao = "";
        }
        switch (situacao) {
            case "A":
                pcp.setSituacao("1");
                break;
            case "I":
                pcp.setSituacao("2");
                break;
            default:
                pcp.setSituacao("0");
        }
    }
}
