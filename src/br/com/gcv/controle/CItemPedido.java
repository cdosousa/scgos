/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.gcs.controle.CMateriais;
import br.com.gcs.modelo.Materiais;
import br.com.gcv.dao.PedidoDAO;
import br.com.gcv.modelo.ItemPedido;
import br.com.gcv.modelo.LocalProposta;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 09/02/2017
 */
public class CItemPedido {

    // variáveis de instancia
    private Connection conexao;
    private ResultSet rsItped;
    private List<ItemPedido> resultado = null;
    private ItemPedido regAtual;
    private ItemPedido itped;
    private PedidoDAO itpeddao;
    private DefaultTableModel modelo = new DefaultTableModel();
    private JTable conteudo;
    private DefaultTableModel itens;
    private NumberFormat ftq;
    private NumberFormat ftp;
    private NumberFormat ftv;
    private int idxAtual;
    private int numReg = 0;
    private String sql;
    private String cdProposta;
    private String cdRevisao;
    private String cdTipoVerniz;
    private String nomeTipoVerniz;
    private double valorUnitarioBrt;
    private double valorUnitarioLiq;

    private double totalServico = 0;
    private double totalMaterial = 0;
    private double totalDesconto = 0;
    private String tipoItem;

    // Construtor padrão
    public CItemPedido(Connection conexao) {
        ftq.getInstance();
        ftq = new DecimalFormat("###,###,##0.0000");
        ftp = ftq;
        ftv = ftq;
        ftq.setMaximumFractionDigits(3);
        ftp.setMaximumFractionDigits(3);
        ftv.setMaximumFractionDigits(2);
        this.conexao = conexao;
        itpeddao = new PedidoDAO(conexao);
        itped = new ItemPedido();
        itped.setProxSequencia(0);
        modelo.addColumn("Seq.");
        modelo.addColumn("Cod.");
        modelo.addColumn("Descrição");
        modelo.addColumn("U.M");
        modelo.addColumn("Qtde");
        modelo.addColumn("Pr.Unit.");
        modelo.addColumn("Perc.Desc.");
        modelo.addColumn("Val.Desc.");
        modelo.addColumn("T.Ite.Brt");
        modelo.addColumn("T.Ite.Liq");
        conteudo = new JTable(modelo);
    }

    /**
     * método para acionar a pesquisa dos registro e retornar em um objeto
     * resultSet
     *
     * @param sql
     * @return numReg
     */
    public int pesquisar(String sql, String cdProposta, String cdRevisao) throws SQLException {
        this.sql = sql;
        this.cdProposta = cdProposta;
        this.cdRevisao = cdRevisao;
        resultado = new ArrayList<ItemPedido>();
        rsItped = itpeddao.pesquisarItemPedido(this.sql);
        carregarRegistrosItens();
        numReg = resultado.size();
        return numReg;
    }

    /**
     * Método criado para carregar os registro dos itens pesquisados em uma
     * objeto de arrayList
     */
    private void carregarRegistrosItens() throws SQLException {
        while (rsItped.next()) {
            try {
                resultado.add(new ItemPedido(
                        rsItped.getString("cd_pedido"),
                        rsItped.getInt("sequencia"),
                        rsItped.getString("cd_material"),
                        rsItped.getString("cd_unidmedida"),
                        rsItped.getDouble("quantidade"),
                        rsItped.getDouble("valor_unit_bruto"),
                        rsItped.getDouble("valor_unit_liquido"),
                        rsItped.getDouble("perc_desc"),
                        rsItped.getDouble("valor_desc"),
                        rsItped.getDouble("total_item_bruto"),
                        rsItped.getDouble("total_item_liquido"),
                        rsItped.getString("tipo_item"),
                        rsItped.getString("obs_item"),
                        rsItped.getInt("cd_local_proposta"),
                        rsItped.getInt("sequencia_proposta"),
                        rsItped.getString("usuario_cadastro"),
                        rsItped.getString("data_cadastro"),
                        rsItped.getString("hora_cadastro"),
                        rsItped.getString("usuario_modificacao"),
                        rsItped.getString("data_modificacao"),
                        rsItped.getString("hora_modificacao"),
                        rsItped.getString("situacao")));
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro na busca do Registro!\nPrograma: CItemPedido.java\nErr: " + ex);
            }
        }
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(ItemPedido itped, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        itped.setCdPedido(regAtual.getCdPedido());
        itped.setSequencia(regAtual.getSequencia());
        itped.setCdMaterial(regAtual.getCdMaterial());
        itped.setNomeMaterial(buscarMaterial(regAtual.getCdMaterial()));
        itped.setCdUnidmedida(regAtual.getCdUnidmedida());
        itped.setQuantidade(regAtual.getQuantidade());
        itped.setValorUnitBruto(regAtual.getValorUnitBruto());
        itped.setValorUnitLiquido(regAtual.getValorUnitLiquido());
        itped.setPercDesconto(regAtual.getPercDesconto());
        itped.setValorDescontos(regAtual.getValorDescontos());
        itped.setValorTotalItemBruto(regAtual.getValorTotalItemBruto());
        itped.setValorTotalItemLiquido(regAtual.getValorTotalItemLiquido());
        itped.setTipoItem(regAtual.getTipoItem());
        itped.setObsItem(regAtual.getObsItem());
        itped.setCdLocal(regAtual.getCdLocal());
        itped.setSequenciaAtend(regAtual.getSequenciaAtend());
        itped.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        itped.setDataCadastro(regAtual.getDataCadastro());
        itped.setHoraCadastro(regAtual.getHoraCadastro());
        itped.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        itped.setDataModificacao(regAtual.getDataModificacao());
        itped.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = String.valueOf(regAtual.getSituacao());
        switch (situacao) {
            case "AA":
                itped.setSituacao("1");
                break;
            case "AV":
                itped.setSituacao("2");
                break;
            case "NI":
                itped.setSituacao("3");
                break;
            default:
                itped.setSituacao("0");
        }
        if(buscarLocalProposta() > 0){
            itped.setCdTipoVerniz(cdTipoVerniz);
            itped.setNomeTipoVerniz(nomeTipoVerniz);
        }
    }

    // método para carregar a tabela com itens adicionados
    public DefaultTableModel carregarItens() {
        if (numReg > 0) {
            for (int i = 0; i < numReg; i++) {
                mostrarPesquisa(itped, i);
                itens = (DefaultTableModel) conteudo.getModel();
                itens.addRow(new Object[]{itped.getSequencia(),
                    itped.getCdMaterial(),
                    itped.getNomeMaterial(),
                    itped.getCdUnidmedida(),
                    ftq.format(itped.getQuantidade()),
                    ftv.format(itped.getValorUnitBruto()),
                    ftp.format(itped.getPercDesconto()),
                    ftv.format(itped.getValorDescontos()),
                    ftv.format(itped.getValorTotalItemBruto()),
                    ftv.format(itped.getValorTotalItemLiquido()),
                    "N"});
                this.itped.setProxSequencia(itped.getSequencia());
                if ("S".equals(itped.getTipoItem())) {
                    totalServico += itped.getValorTotalItemBruto();
                } else {
                    totalMaterial += itped.getValorTotalItemBruto();
                }
                totalDesconto += itped.getValorDescontos();
            }
        }
        return itens;
    }

    // método para adicionar nova linha
    public DefaultTableModel adicionarLinha() {
        if (numReg == 0) {
            itens = (DefaultTableModel) conteudo.getModel();
            itens.addRow(new Object[]{1, null, null, null, "0,00", "0,00", "0,000", "0,00", "0,00", "0,00"});
        } else {
            itens.addRow(new Object[]{itped.getProxSequencia(), null, null, null, "0,00", "0,00", "0,000", "0,00", "0,00", "0,00"});
            itped.setSequencia(itped.getProxSequencia());
            itped.setProxSequencia(itped.getProxSequencia());
        }
        return itens;
    }

    // metodo para excluir linha nova
    public DefaultTableModel excluirLinha(int linha) {
        itens.removeRow(linha);
        itpeddao.excluirItemPedido(itped);
        return itens;
    }

    // metodo para adicionar valores a nova linha criada
    public DefaultTableModel upNovaLinha(ItemPedido itped, int linha, int idxCdMaterial, int idxDescricao,
            int idxUnidMed, int idxQtde, int idxValUnitBruto, int idxPercDesc, int idxValorDesc, int idxValTotalBruto, int idxValTotalLiquido, String controle) {
        itens.setValueAt(itped.getCdMaterial(), linha, idxCdMaterial);
        itens.setValueAt(itped.getNomeMaterial(), linha, idxDescricao);
        itens.setValueAt(itped.getCdUnidmedida(), linha, idxUnidMed);
        itens.setValueAt(ftq.format(itped.getQuantidade()), linha, idxQtde);
        itens.setValueAt(ftv.format(itped.getValorUnitBruto()), linha, idxValUnitBruto);
        itens.setValueAt(ftp.format(itped.getPercDesconto()), linha, idxPercDesc);
        itens.setValueAt(ftv.format(itped.getValorTotalItemBruto() / 100 * itped.getPercDesconto()), linha, idxValorDesc);
        itens.setValueAt(ftv.format(itped.getValorTotalItemBruto()), linha, idxValTotalBruto);
        itens.setValueAt(ftv.format(itped.getValorTotalItemBruto() * (1 - itped.getPercDesconto() / 100)), linha, idxValTotalLiquido);
        //itldao.adicionar(itped);
        return itens;
    }

    // Método par buscar nome do Material
    private String buscarMaterial(String cdMaterial) {
        Materiais mat = new Materiais();
        try {
            CMateriais cmat = new CMateriais(conexao);
            String sqlctp = "SELECT * FROM GCSMATERIAL WHERE CD_MATERIAL = '" + cdMaterial + "'";
            cmat.pesquisar(sqlctp, false);
            cmat.mostrarPesquisa(mat, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome do Material!\nPrograma CItemLocal.\nErro: " + ex);
        }
        tipoItem = mat.getTipoProduto();
        return mat.getNomeMaterial();
    }

    /**
     * Método para buscar o local da proposta
     */
    private int buscarLocalProposta() {
        cdTipoVerniz = "";
        nomeTipoVerniz = "";
        int numReg = 0;
        LocalProposta lpr = new LocalProposta();
        try {
            CLocalProposta clpr = new CLocalProposta(conexao);
            String sqlcpr = "select * from gcvlocalproposta where cd_proposta = '" + cdProposta
                    + "' and cd_revisao = '" + cdRevisao
                    + "' and cd_local = " + regAtual.getCdLocal();
            numReg = clpr.pesquisar(sqlcpr);
            if (numReg > 0) {
                clpr.mostrarPesquisa(lpr, 0);
                cdTipoVerniz = lpr.getCdTipolVerniz();
                nomeTipoVerniz = lpr.getNomeTipoVerniz();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Local da Proposta!\nPrograma CItemPedido.\nErro: " + ex);
        }
        return numReg;
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
     * @return the totalDesconto
     */
    public double getTotalDesconto() {
        return totalDesconto;
    }

    /**
     * @return the tipoItem
     */
    public String getTipoItem() {
        return tipoItem;
    }
}
