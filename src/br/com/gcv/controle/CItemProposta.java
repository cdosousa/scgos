/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.gcs.controle.CMateriais;
import br.com.gcs.modelo.Materiais;
import br.com.gcv.dao.ItemPropostaDAO;
import br.com.gcv.modelo.ItemProposta;
import br.com.gcv.modelo.LocalProposta;
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
 * @author Cristiano de Oliveira Sousa created on 11/12/2017
 */
public class CItemProposta {

    // variáveis de instancia
    private Connection conexao;
    private List<ItemProposta> resultado = null;
    private ItemProposta regAtual;
    private ItemProposta itp;
    private ItemPropostaDAO itpdao;
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

    private double totalServico = 0;
    private double totalMaterial = 0;
    private double totalDesconto = 0;
    private String tipoItem;

    // Construtor padrão
    public CItemProposta(Connection conexao) {
        ftq.getInstance();
        ftq = new DecimalFormat("###,###,##0.0000");
        ftp = ftq;
        ftv = ftq;
        ftq.setMaximumFractionDigits(3);
        ftp.setMaximumFractionDigits(3);
        ftv.setMaximumFractionDigits(2);
        this.conexao = conexao;
        try {
            itpdao = new ItemPropostaDAO(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(CItemProposta.class.getName()).log(Level.SEVERE, null, ex);
        }
        itp = new ItemProposta();
        itp.setProxSequencia(0);
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

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<ItemProposta>();
        itpdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(ItemProposta itp, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        itp.setCdProposta(regAtual.getCdProposta());
        itp.setCdRevisao(regAtual.getCdRevisao());
        itp.setCdLocal(regAtual.getCdLocal());
        itp.setSequencia(regAtual.getSequencia());
        itp.setSequenciaAtend(regAtual.getSequenciaAtend());
        itp.setCdMaterial(regAtual.getCdMaterial());
        itp.setNomeMaterial(buscarMaterial(regAtual.getCdMaterial()));
        itp.setCdUnidmedida(regAtual.getCdUnidmedida());
        itp.setQuantidade(regAtual.getQuantidade());
        itp.setValorUnitBruto(regAtual.getValorUnitBruto());
        itp.setValorUnitLiquido(regAtual.getValorUnitLiquido());
        itp.setPercDesconto(regAtual.getPercDesconto());
        itp.setValorDescontos(regAtual.getValorDescontos());
        itp.setValorTotalItemBruto(regAtual.getValorTotalItemBruto());
        itp.setValorTotalItemLiquido(regAtual.getValorTotalItemLiquido());
        itp.setTipoItem(regAtual.getTipoItem());
        itp.setObsItem(regAtual.getObsItem());
        itp.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        itp.setDataCadastro(regAtual.getDataCadastro());
        itp.setHoraCadastro(regAtual.getHoraCadastro());
        itp.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        itp.setDataModificacao(regAtual.getDataModificacao());
        itp.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = String.valueOf(regAtual.getSituacao());
        switch (situacao) {
            case "AA":
                itp.setSituacao("1");
                break;
            case "AV":
                itp.setSituacao("2");
                break;
            case "NI":
                itp.setSituacao("3");
                break;
            default:
                itp.setSituacao("0");
        }
    }

    // método para atualizar total do ambiente
    public void upTotalAmbiente(LocalProposta lpro) {
        LocalProposta regAtual = resultado.get(0);
        lpro.setCdProposta(regAtual.getCdProposta());
        lpro.setCdRevisao(regAtual.getCdRevisao());
        lpro.setCdLocal(regAtual.getCdLocal());
        lpro.setValorProdutos(regAtual.getValorProdutos());
        lpro.setValorServico(regAtual.getValorServico());
        lpro.setValorAdicionais(regAtual.getValorAdicionais());
        lpro.setValorDescontos(regAtual.getValorDescontos());
        lpro.setValorTotalBruto(regAtual.getValorTotalBruto());
        lpro.setValorTotalLiquido(regAtual.getValorTotalLiquido());
    }

    // método para carregar a tabela com itens adicionados
    public DefaultTableModel carregarItens() {
        if (numReg > 0) {
            for (int i = 0; i < numReg; i++) {
                mostrarPesquisa(itp, i);
                itens = (DefaultTableModel) conteudo.getModel();
                itens.addRow(new Object[]{itp.getSequencia(),
                    itp.getCdMaterial(),
                    itp.getNomeMaterial(),
                    itp.getCdUnidmedida(),
                    ftq.format(itp.getQuantidade()),
                    ftv.format(itp.getValorUnitBruto()),
                    ftp.format(itp.getPercDesconto()),
                    ftv.format(itp.getValorDescontos()),
                    ftv.format(itp.getValorTotalItemBruto()),
                    ftv.format(itp.getValorTotalItemLiquido()),
                    "N"});
                this.itp.setProxSequencia(itp.getSequencia());
                if ("S".equals(itp.getTipoItem())) {
                    totalServico += itp.getValorTotalItemBruto();
                } else {
                    totalMaterial += itp.getValorTotalItemBruto();
                }
                totalDesconto += itp.getValorDescontos();
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
            itens.addRow(new Object[]{itp.getProxSequencia(), null, null, null, "0,00", "0,00", "0,000", "0,00", "0,00", "0,00"});
            itp.setSequencia(itp.getProxSequencia());
            itp.setProxSequencia(itp.getProxSequencia());
        }
        return itens;
    }

    // metodo para excluir linha nova
    public DefaultTableModel excluirLinha(int linha) {
        itens.removeRow(linha);
        itpdao.excluir(itp);
        return itens;
    }

    // metodo para adicionar valores a nova linha criada
    public DefaultTableModel upNovaLinha(ItemProposta itp, int linha, int idxCdMaterial, int idxDescricao,
            int idxUnidMed, int idxQtde, int idxValUnitBruto, int idxPercDesc, int idxValorDesc, int idxValTotalBruto, int idxValTotalLiquido, String controle) {
        itens.setValueAt(itp.getCdMaterial(), linha, idxCdMaterial);
        itens.setValueAt(itp.getNomeMaterial(), linha, idxDescricao);
        itens.setValueAt(itp.getCdUnidmedida(), linha, idxUnidMed);
        itens.setValueAt(ftq.format(itp.getQuantidade()), linha, idxQtde);
        itens.setValueAt(ftv.format(itp.getValorUnitBruto()), linha, idxValUnitBruto);
        itens.setValueAt(ftp.format(itp.getPercDesconto()), linha, idxPercDesc);
        itens.setValueAt(ftv.format(itp.getValorTotalItemBruto() / 100 * itp.getPercDesconto()), linha, idxValorDesc);
        itens.setValueAt(ftv.format(itp.getValorTotalItemBruto()), linha, idxValTotalBruto);
        itens.setValueAt(ftv.format(itp.getValorTotalItemBruto() * (1 - itp.getPercDesconto() / 100)), linha, idxValTotalLiquido);
        //itldao.adicionar(itp);
        return itens;
    }

    // Método par buscar nome do Material
    private String buscarMaterial(String cdMaterial) {
        Materiais mat = new Materiais();
        String descricao = "";
        try {
            CMateriais cmat = new CMateriais(conexao);
            String sqlctp = "SELECT * FROM GCSMATERIAL WHERE CD_MATERIAL = '" + cdMaterial + "'";
            if (cmat.pesquisar(sqlctp, false) > 0) {
                cmat.mostrarPesquisa(mat, 0);
                tipoItem = mat.getTipoProduto();
                descricao =  mat.getNomeMaterial();
            }else{
                descricao = "";
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome do Material!\nPrograma CItemLocal.\nErro: " + ex);
        }
        return descricao;
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
