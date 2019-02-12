/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.gfc.dao.TipoLancamentoMovimentoDAO;
import br.com.gfc.modelo.TipoLancamento;
import br.com.gfc.modelo.TipoLancamentoMovimento;
import br.com.gfc.modelo.TipoMovimento;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 27/02/2018
 */
public class CTipoLancamentoMovimento {

    // variáveis de instancia
    private Connection conexao;
    private List<TipoLancamentoMovimento> resultado = null;
    private TipoLancamentoMovimento regAtual;
    private TipoLancamentoMovimentoDAO tlmDAO;
    private TipoLancamentoMovimento tipoLm;
    private DefaultTableModel modelo = new DefaultTableModel();
    private JTable conteudo;
    private DefaultTableModel tipoMovimentos;

    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CTipoLancamentoMovimento(Connection conexao) {
        this.conexao = conexao;
        try {
            tlmDAO = new TipoLancamentoMovimentoDAO(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(CTipoLancamentoMovimento.class.getName()).log(Level.SEVERE, null, ex);
        }
        tipoLm = new TipoLancamentoMovimento();
        modelo.addColumn("Cód.");
        modelo.addColumn("Descrição");
        conteudo = new JTable(modelo);

    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<TipoLancamentoMovimento>();
        TipoLancamentoMovimentoDAO tlmdao = new TipoLancamentoMovimentoDAO(conexao);
        tlmdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(TipoLancamentoMovimento tlm, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        tlm.setCdTipoLancamento(regAtual.getCdTipoLancamento());
        tlm.setNomeTipoLancamento(buscarTipoLancamento(regAtual.getCdTipoLancamento()));
        tlm.setCdTipoMovimento(regAtual.getCdTipoMovimento());
        tlm.setNomeTipoMovimento(buscarTipoMovimento(regAtual.getCdTipoMovimento()));
        tlm.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        tlm.setDataCadastro(regAtual.getDataCadastro());
        tlm.setHoraCadastro(regAtual.getHoraCadastro());
        tlm.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        tlm.setDataModificacao(regAtual.getDataModificacao());
        tlm.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                tlm.setSituacao("1");
                break;
            case "I":
                tlm.setSituacao("2");
                break;
        }
    }

    /**
     *
     * @param cdTipoLancamento
     * @return nomeTipoLancamento
     */
    private String buscarTipoLancamento(String cdTipoLancamento) {
        TipoLancamento tl = new TipoLancamento();
        try {
            CTipoLancamento ctl = new CTipoLancamento(conexao);
            String sqltl = "select * from gfctipolancamento where cd_tipolancamento = '"
                    + cdTipoLancamento
                    + "'";
            ctl.pesquisar(sqltl);
            ctl.mostrarPesquisa(tl, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome do Tipo de Lancamento!\nPrograma CTipoLancamentoMovimento.\nErro: " + ex);
        }
        return tl.getNomeTipoLancamento();
    }

    /**
     *
     * @param cdTipoMovimento
     * @return nomeTipoMovimento
     */
    private String buscarTipoMovimento(String cdTipoMovimento) {
        TipoMovimento tm = new TipoMovimento();
        try {
            CTipoMovimento ctm = new CTipoMovimento(conexao);
            String sqltl = "select * from gfctipomovimento where cd_tipomovimento = '"
                    + cdTipoMovimento
                    + "'";
            ctm.pesquisar(sqltl);
            ctm.mostrarPesquisa(tm, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome do Tipo de Movimento!\nPrograma CTipoLancamentoMovimento.\nErro: " + ex);
        }
        return tm.getNomeTipoMovimento();
    }

    /**
     * Método para carregar a tabela com os registros pesquisados
     *
     * @return DefaultTableModel retorna uma tabela padrão com os registros
     *
     */
    public DefaultTableModel carregarMovimentos() {
        if (numReg > 0) {
            for (int i = 0; i < numReg; i++) {
                mostrarPesquisa(tipoLm, i);
                tipoMovimentos = (DefaultTableModel) conteudo.getModel();
                tipoMovimentos.addRow(new Object[]{tipoLm.getCdTipoMovimento(),
                    tipoLm.getNomeTipoMovimento()
                });
            }
        }
        return tipoMovimentos;
    }

    /**
     * Método para adicionar nova linha na tabela
     *
     * @return DefaultTableModel contendo os itens e a linha adicionada
     */
    public DefaultTableModel adicionarLinha() {
        tipoMovimentos = (DefaultTableModel) conteudo.getModel();
        tipoMovimentos.addRow(new Object[]{null, null});
        return tipoMovimentos;
    }
    
    /**
     * Método para excluir linha
     * @param linha
     * @returm DafaultTableModel com os ajustes realizados
     */
    public DefaultTableModel excluirLinha(int linha){
        tipoMovimentos.removeRow(linha);
        tlmDAO.excluir(tipoLm);
        return tipoMovimentos;
    }
    
    /**
     * Método para adicionar valores a nova linha criada
     * @param tipoLm
     * @param linha
     * @param idxCdTipoMovimento
     * @param idxNomeTipoMovimento
     */
    public DefaultTableModel upNovaLinha(TipoLancamentoMovimento tipoLm, int linha, int idxCdTipoMovimento, int idxNomeTipoMovimento){
        tipoMovimentos.setValueAt(tipoLm.getCdTipoMovimento(), linha, idxCdTipoMovimento);
        tipoMovimentos.setValueAt(tipoLm.getNomeTipoMovimento(), linha, idxNomeTipoMovimento);
        return tipoMovimentos;
    }

}
