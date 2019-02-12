/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfr.controle;

import br.com.gfr.dao.CentroCustosDAO;
import br.com.gfr.modelo.CentroCustos;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristiano
 */
public class CCentroCustos {

    // variáveis de instancia
    private List<CentroCustos> resultado = null;
    private CentroCustos regAtual;
    private Connection conexao;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CCentroCustos(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<CentroCustos>();
        CentroCustosDAO ccdao = new CentroCustosDAO(conexao);
        ccdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(CentroCustos cc, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        cc.setCdCcusto(regAtual.getCdCcusto());
        cc.setNomeCcusto(regAtual.getNomeCcusto());
        cc.setPercRateio(regAtual.getPercRateio());
        cc.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        cc.setDataCadastro(regAtual.getDataCadastro());
        cc.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                cc.setSituacao("1");
                break;
            case "I":
                cc.setSituacao("2");
                break;
        }
    }
}
