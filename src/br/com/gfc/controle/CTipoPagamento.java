/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.gfc.dao.TipoPagamentoDAO;
import br.com.gfc.modelo.Portadores;
import br.com.gfc.modelo.TipoPagamento;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 10/11/2017
 */
public class CTipoPagamento {

    // variáveis de instancia
    private Connection conexao;
    private List<TipoPagamento> resultado = null;
    private TipoPagamento regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    private String cdPortador;
    
    // Construtor padrão
    public CTipoPagamento(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<TipoPagamento>();
        TipoPagamentoDAO tpdao = new TipoPagamentoDAO(conexao);
        tpdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(TipoPagamento tp, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        tp.setCdTipoPagamento(regAtual.getCdTipoPagamento());
        tp.setNomeTipoPagamento(regAtual.getNomeTipoPagamento());
        tp.setPermiteParcelamento(regAtual.getPermiteParcelamento());
        tp.setEmiteBoleto(regAtual.getEmiteBoleto());
        tp.setEnviarArqBanco(regAtual.getEnviarArqBanco());
        tp.setEnviaCartorio(regAtual.getEnviaCartorio());
        tp.setDiasCartorio(regAtual.getDiasCartorio());
        tp.setCdPortador(regAtual.getCdPortador());
        this.cdPortador = regAtual.getCdPortador();
        tp.setNomePortador(buscarPortador(cdPortador));
        tp.setTaxaJuros(regAtual.getTaxaJuros());
        tp.setTaxaMulta(regAtual.getTaxaMulta());
        tp.setDiasLiquidacao(regAtual.getDiasLiquidacao());
        tp.setDataCadastro(regAtual.getDataCadastro());
        tp.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                tp.setSituacao("1");
                break;
            case "I":
                tp.setSituacao("2");
                break;
        }
    }

    // Método par buscar nome do Portador
    private String buscarPortador(String cdPortador) {
        Portadores po = new Portadores();
        try {
            CPortadores cpo = new CPortadores(conexao);
            String sqlcpo = "SELECT * FROM GFCPORTADOR WHERE CD_PORTADOR = '" + cdPortador + "'";
            cpo.pesquisar(sqlcpo);
            cpo.mostrarPesquisa(po, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome do Portador!\nPrograma CTipoPagamento.\nErro: " + ex);
        }
        return po.getNomeBanco();
    }
}
