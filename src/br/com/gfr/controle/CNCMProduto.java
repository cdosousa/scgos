/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfr.controle;

import br.com.gfr.dao.NCMProdutoDAO;
import br.com.gfr.modelo.NCMProduto;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristiano
 */
public class CNCMProduto {

    // variáveis de instancia
    private List<NCMProduto> resultado = null;
    private NCMProduto regAtual;
    private Connection conexao;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CNCMProduto(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<NCMProduto>();
        NCMProdutoDAO ncmdao = new NCMProdutoDAO(conexao);
        ncmdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(NCMProduto ncm, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        ncm.setCdNcm(regAtual.getCdNcm());
        ncm.setNomeNcm(regAtual.getNomeNcm());
        ncm.setCdOrigemCsta(regAtual.getCdOrigemCsta());
        ncm.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        ncm.setDataCadastro(regAtual.getDataCadastro());
        ncm.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                ncm.setSituacao("1");
                break;
            case "I":
                ncm.setSituacao("2");
                break;
        }
    }
}
