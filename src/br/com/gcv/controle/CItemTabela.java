/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.gcs.controle.CMateriais;
import br.com.gcs.controle.CUnidadesMedida;
import br.com.gcs.modelo.Materiais;
import br.com.gcs.modelo.UnidadesMedida;
import br.com.gcv.dao.ItemTabelaDAO;
import br.com.gcv.modelo.ItemTabela;
import br.com.gcv.modelo.Tabela;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 21/11/2017
 */
public class CItemTabela {

    // variáveis de instancia
    private Connection conexao;
    private List<ItemTabela> resultado = null;
    private ItemTabela regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // correalatos
    private String cdTabela;
    private String nomeTabela;
    private String cdMaterial;
    private String nomeMaterial;
    private String cdUnidMedida;
    private String nomeUnidMedida;

    // Construtor padrão
    public CItemTabela(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<ItemTabela>();
        ItemTabelaDAO itadao = new ItemTabelaDAO(conexao);
        itadao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(ItemTabela ita, int idxAtual, boolean buscarMaterial) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        ita.setCdTabela(regAtual.getCdTabela());
        cdTabela = regAtual.getCdTabela();
        ita.setCdMaterial(regAtual.getCdMaterial());
        cdMaterial = regAtual.getCdMaterial();
        ita.setCdUnidMedida(regAtual.getCdUnidMedida());
        cdUnidMedida = regAtual.getCdUnidMedida();
        ita.setValorUnit(regAtual.getValorUnit());
        ita.setDescAlcada(regAtual.getDescAlcada());
        ita.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        ita.setDataCadastro(regAtual.getDataCadastro());
        ita.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                ita.setSituacao("1");
                break;
            case "I":
                ita.setSituacao("2");
                break;
        }
        if (buscarMaterial) {
            buscarCorrelatos();
            ita.setNomeTabela(nomeTabela);
            ita.setNomeMaterial(nomeMaterial);
            ita.setNomeUnidMedia(nomeUnidMedida);
        }
    }

    // Método para atualizar correlatos
    private void buscarCorrelatos() {
        nomeTabela = buscarTabela(cdTabela);
        nomeMaterial = buscarMaterial(cdMaterial);
        nomeUnidMedida = buscarUnidMedida(cdUnidMedida);
    }

    // Método para buscar nome da tabela de preco
    private String buscarTabela(String cdTabela) {
        Tabela tb = new Tabela();
        try {
            CTabela ctb = new CTabela(conexao);
            String sqltb = "SELECT * FROM  GCVTABELA WHERE CD_TABELA = '" + cdTabela + "'";
            ctb.pesquisar(sqltb);
            ctb.mostrarPesquisa(tb, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do nome da Tabela!\nPrograma: CItemTabela.java\nErro: " + ex);
        }
        return tb.getNomeTabela();
    }

    // Método para buscar nome do produto
    private String buscarMaterial(String cdMaterial) {
        Materiais mat = new Materiais();
        try {
            CMateriais cmat = new CMateriais(conexao);
            String sqltb = "SELECT * FROM  GCSMATERIAL WHERE CD_MATERIAL = '" + cdMaterial + "'";
            cmat.pesquisar(sqltb, false);
            cmat.mostrarPesquisa(mat, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do nome do Material!\nPrograma: CItemTabela.java\nErro: " + ex);
        }
        return mat.getNomeMaterial();
    }

    // Método para buscar nome da unidade de medida
    private String buscarUnidMedida(String cdUnidMedida) {
        UnidadesMedida um = new UnidadesMedida();
        try {
            CUnidadesMedida cum = new CUnidadesMedida(conexao);
            String sqltb = "SELECT * FROM  GCSUNIDMEDIDA WHERE CD_UNIDMEDIDA = '" + cdUnidMedida + "'";
            cum.pesquisar(sqltb);
            cum.mostrarPesquisa(um, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do nome da Unidade de Medida!\nPrograma: CItemTabela.java\nErro: " + ex);
        }
        return um.getNomeUnidMedida();
    }
}
