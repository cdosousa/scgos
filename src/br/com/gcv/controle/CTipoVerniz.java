/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.gcv.dao.TipoVernizDAO;
import br.com.gcv.modelo.TipoVerniz;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 04/12/2017
 */
public class CTipoVerniz {

    // variáveis de instancia
    private Connection conexao;
    private List<TipoVerniz> resultado = null;
    private TipoVerniz regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CTipoVerniz(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<TipoVerniz>();
        TipoVernizDAO tvzdao = new TipoVernizDAO(conexao);
        tvzdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(TipoVerniz tvz, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        tvz.setCdTipoVerniz(regAtual.getCdTipoVerniz());
        tvz.setNomeTipoVerniz(regAtual.getNomeTipoVerniz());
        tvz.setValor(regAtual.getValor());
        tvz.setDescricaoComercial(regAtual.getDescricaoComercial());
        tvz.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        tvz.setDataCadastro(regAtual.getDataCadastro());
        tvz.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        tvz.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                tvz.setSituacao("1");
                break;
            case "I":
                tvz.setSituacao("2");
                break;
        }
    }
}