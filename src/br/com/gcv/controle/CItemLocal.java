/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.gcs.controle.CMateriais;
import br.com.gcs.modelo.Materiais;
import br.com.gcv.dao.ItemLocalDAO;
import br.com.gcv.modelo.ItemLocal;
import br.com.gcv.modelo.LocalAtendimento;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 28/11/2017
 */
public class CItemLocal {

    // variáveis de instancia
    private Connection conexao;
    private List<ItemLocal> resultado = null;
    private ItemLocal regAtual;
    private ItemLocal itl;
    //private ItemLocalDAO itldao;
    private DefaultTableModel modelo = new DefaultTableModel();
    private JTable conteudo;
    private DefaultTableModel itens;
    private NumberFormat ftq;
    private NumberFormat ftp;
    private NumberFormat ftv;
    private int idxAtual;
    private int numReg = 0;
    private String sql;
    private double valorUnitarioBrt;
    private double valorUnitarioLiq;

    private double totalServico;
    private double totalMaterial;
    private String cdTipoItem;

    // Construtor padrão
    public CItemLocal(Connection conexao) {
        ftq.getInstance();
        ftq = new DecimalFormat("###,###,##0.0000");
        ftp = ftq;
        ftv = ftq;
        ftq.setMaximumFractionDigits(3);
        ftp.setMaximumFractionDigits(3);
        ftv.setMaximumFractionDigits(2);
        this.conexao = conexao;
        itl = new ItemLocal();
        itl.setProxSequencia(0);
        modelo.addColumn("Seq.");
        modelo.addColumn("Cod.");
        modelo.addColumn("Descrição");
        modelo.addColumn("U.M");
        modelo.addColumn("Qtde");
        modelo.addColumn("Pr.Unit.");
        modelo.addColumn("Tot.Item");
        conteudo = new JTable(modelo);
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<ItemLocal>();
        ItemLocalDAO itldao = new ItemLocalDAO(conexao);
        itldao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para pesquisar totais dos registro
    public int pesquisarTotais(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<ItemLocal>();
        ItemLocalDAO itldao = new ItemLocalDAO(conexao);
        itldao.totalizarItens(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(ItemLocal itl, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        itl.setCdAtendimento(regAtual.getCdAtendimento());
        itl.setCdLocal(regAtual.getCdLocal());
        itl.setSequencia(regAtual.getSequencia());
        itl.setCdMaterial(regAtual.getCdMaterial());
        itl.setNomeMaterial(buscarMaterial(regAtual.getCdMaterial()));
        itl.setCdUnidmedida(regAtual.getCdUnidmedida());
        itl.setQuantidade(regAtual.getQuantidade());
        itl.setValorUnitBruto(regAtual.getValorUnitBruto());
        itl.setValorTotalItemBruto(regAtual.getValorTotalItemBruto());
        switch (regAtual.getTipoItem()) {
            case "R":
                itl.setTipoItem("1");
                break;
            default:
                itl.setTipoItem("2");
                break;
        }
        itl.setObsItem(regAtual.getObsItem());
        itl.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        itl.setDataCadastro(regAtual.getDataCadastro());
        itl.setHoraCadastro(regAtual.getHoraCadastro());
        itl.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        itl.setDataModificacao(regAtual.getDataModificacao());
        itl.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = String.valueOf(regAtual.getSituacao());
        switch (situacao) {
            case "AA":
                itl.setSituacao("1");
                break;
            case "AV":
                itl.setSituacao("2");
                break;
            case "NI":
                itl.setSituacao("3");
                break;
            default:
                itl.setSituacao("0");
        }
    }

    // método para atualizar total do ambiente
    public void upTotalAmbiente(LocalAtendimento late) {
        LocalAtendimento regAtual = resultado.get(0);
        late.setCdAtendimento(regAtual.getCdAtendimento());
        late.setCdLocal(regAtual.getCdLocal());
        late.setValorProdutos(regAtual.getValorProdutos());
        late.setValorServico(regAtual.getValorServico());
        late.setValorAdicionais(regAtual.getValorAdicionais());
        late.setValorTotalBruto(regAtual.getValorTotalBruto());
    }

    // método para carregar a tabela com itens adicionados
    public DefaultTableModel carregarItens() {
        if (numReg > 0) {
            for (int i = 0; i < numReg; i++) {
                mostrarPesquisa(itl, i);
                itens = (DefaultTableModel) conteudo.getModel();
                itens.addRow(new Object[]{itl.getSequencia(),
                    itl.getCdMaterial(),
                    itl.getNomeMaterial(),
                    itl.getCdUnidmedida(),
                    ftq.format(itl.getQuantidade()),
                    ftv.format(itl.getValorUnitBruto()),
                    ftv.format(itl.getValorTotalItemBruto()),
                    "N"});
                this.itl.setProxSequencia(itl.getSequencia());
                if ("2".equals(itl.getTipoItem())) {
                    totalServico += itl.getValorTotalItemBruto();
                } else {
                    totalMaterial += itl.getValorTotalItemBruto();
                }
            }
        }
        return itens;
    }

    // método para adicionar nova linha
    public DefaultTableModel adicionarLinha() {
        if (numReg == 0) {
            itens = (DefaultTableModel) conteudo.getModel();
            itens.addRow(new Object[]{1, null, null, null, "0,00", "0,00", "0,00"});
        } else {
            itens.addRow(new Object[]{itl.getProxSequencia(), null, null, null, "0,00", "0,00", "0,00"});
            itl.setSequencia(itl.getProxSequencia());
            itl.setProxSequencia(itl.getProxSequencia());
        }
        return itens;
    }

    // metodo para excluir linha nova
    public DefaultTableModel excluirLinha(int linha) {
        itens.removeRow(linha);
        ItemLocalDAO itldao;
        try {
            itldao = new ItemLocalDAO(conexao);
            itldao.excluir(itl);
        } catch (SQLException ex) {
            Logger.getLogger(CItemLocal.class.getName()).log(Level.SEVERE, null, ex);
        }
        return itens;
    }

    // metodo para adicionar valores a nova linha criada
    public DefaultTableModel upNovaLinha(ItemLocal itl, int linha, int idxCdMaterial, int idxDescricao,
            int idxUnidMed, int idxQtde, int idxValUnit, int idxValTotal, String controle) {
        itens.setValueAt(itl.getCdMaterial(), linha, idxCdMaterial);
        itens.setValueAt(itl.getNomeMaterial(), linha, idxDescricao);
        itens.setValueAt(itl.getCdUnidmedida(), linha, idxUnidMed);
        itens.setValueAt(ftq.format(itl.getQuantidade()), linha, idxQtde);
        itens.setValueAt(ftv.format(itl.getValorUnitBruto()), linha, idxValUnit);
        itens.setValueAt(ftv.format(itl.getValorTotalItemBruto()), linha, idxValTotal);
        //itldao.adicionar(itl);
        return itens;
    }

    // Método par buscar nome do Material
    public String buscarMaterial(String cdMaterial) {
        Materiais mat = new Materiais();
        try {
            CMateriais cmat = new CMateriais(conexao);
            String sqlctp = "SELECT * FROM GCSMATERIAL WHERE CD_MATERIAL = '" + cdMaterial + "'";
            cmat.pesquisar(sqlctp, false);
            cmat.mostrarPesquisa(mat, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome do Material!\nPrograma CItemLocal.\nErro: " + ex);
        }
        cdTipoItem = mat.getTipoProduto();
        return mat.getNomeMaterial();
    }

    /**
     * @return the totalServico
     */
    public double getTotalServico() {
        return totalServico;
    }

    /**
     * @return the totalMaterial
     */
    public double getTotalMaterial() {
        return totalMaterial;
    }

    /**
     * @return the cdTipoItem
     */
    public String getCdTipoItem() {
        return cdTipoItem;
    }
}