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
import br.com.gcv.dao.ItemTipoVernizDAO;
import br.com.gcv.modelo.ItemTipoVerniz;
import br.com.gcv.modelo.TipoVerniz;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 04/12/2017
 */
public class CItemTipoVerniz {

    // variáveis de instancia
    private Connection conexao;
    private List<ItemTipoVerniz> resultado = null;
    private ItemTipoVerniz regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // correalatos
    private String cdTipoVerniz;
    private String nomeTipoVerniz;
    private String cdMaterial;
    private String nomeMaterial;
    private String cdUnidMedida;
    private String nomeUnidMedida;

    // Construtor padrão
    public CItemTipoVerniz(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<ItemTipoVerniz>();
        ItemTipoVernizDAO itvdao = new ItemTipoVernizDAO(conexao);
        itvdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(ItemTipoVerniz itv, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        itv.setCdTipoVerniz(regAtual.getCdTipoVerniz());
        cdTipoVerniz = regAtual.getCdTipoVerniz();
        itv.setCdMaterial(regAtual.getCdMaterial());
        cdMaterial = regAtual.getCdMaterial();
        itv.setCdUnidMedida(regAtual.getCdUnidMedida());
        cdUnidMedida = regAtual.getCdUnidMedida();
        itv.setQtde(regAtual.getQtde());
        itv.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        itv.setDataCadastro(regAtual.getDataCadastro());
        itv.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        itv.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                itv.setSituacao("1");
                break;
            case "I":
                itv.setSituacao("2");
                break;
        }
        buscarCorrelatos();
        itv.setNomeTipoVerniz(nomeTipoVerniz);
        itv.setNomeMaterial(nomeMaterial);
        itv.setNomeUnidMedida(nomeUnidMedida);
    }

    // Método para atualizar correlatos
    private void buscarCorrelatos() {
        nomeTipoVerniz = buscarTipoVerniz(cdTipoVerniz);
        nomeMaterial = buscarMaterial(cdMaterial);
        nomeUnidMedida = buscarUnidMedida(cdUnidMedida);
    }

    // Método para buscar nome da tabela de preco
    private String buscarTipoVerniz(String cdTipoVerniz) {
        TipoVerniz tv = new TipoVerniz();
        try {
            CTipoVerniz ctv = new CTipoVerniz(conexao);
            String sqltb = "SELECT * FROM  GCVTIPOVERNIZ WHERE CD_TIPOVERNIZ = '" + cdTipoVerniz + "'";
            ctv.pesquisar(sqltb);
            ctv.mostrarPesquisa(tv, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do nome do Tipo Verniz!\nPrograma: CItemTipoVerniz.java\nErro: " + ex);
        }
        return tv.getNomeTipoVerniz();
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
            JOptionPane.showMessageDialog(null, "Erro na busca do nome do Material!\nPrograma: CItemTipoVerniz.java\nErro: " + ex);
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
            JOptionPane.showMessageDialog(null, "Erro na busca do nome da Unidade de Medida!\nPrograma: CItemTipoVerniz.java\nErro: " + ex);
        }
        return um.getNomeUnidMedida();
    }
}
