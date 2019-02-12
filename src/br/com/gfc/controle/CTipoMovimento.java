/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.gfc.dao.TipoMovimentoDAO;
import br.com.gfc.modelo.ContasContabeis;
import br.com.gfc.modelo.TipoMovimento;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 27/02/2018
 */
public class CTipoMovimento {

    // variáveis de instancia
    private Connection conexao;
    private List<TipoMovimento> resultado = null;
    private TipoMovimento regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CTipoMovimento(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<TipoMovimento>();
        TipoMovimentoDAO tmdao = new TipoMovimentoDAO(conexao);
        tmdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }
    
    /**
     * método para selecionar registro da pesquisa
     * @param idxAtual índice do registro desejado
     * @return objeto contendo os registros desejados
     */
    public TipoMovimento selecionarPesquisa(int idxAtual){
        regAtual = resultado.get(idxAtual);
        return regAtual;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(TipoMovimento tm, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        tm.setCdTipoMovimento(regAtual.getCdTipoMovimento());
        tm.setNomeTipoMovimento(regAtual.getNomeTipoMovimento());
        tm.setCdContaReduzida(regAtual.getCdContaReduzida());
        tm.setNomeConta(buscarContaReduzida(regAtual.getCdContaReduzida()));
        switch (regAtual.getSituacaoLancamento()) {
            case "AB":
                tm.setSituacaoLancamento("1");
                break;
            case "CA":
                tm.setSituacaoLancamento("2");
                break;
            case "CO":
                tm.setSituacaoLancamento("3");
                break;
            case "LI":
                tm.setSituacaoLancamento("4");
                break;
            default:
                tm.setSituacaoLancamento("0");
                break;
        }
        switch (regAtual.getSituacaoContraPartida()) {
            case "AB":
                tm.setSituacaoContraPartida("1");
                break;
            case "CA":
                tm.setSituacaoContraPartida("2");
                break;
            case "CO":
                tm.setSituacaoContraPartida("3");
                break;
            case "LI":
                tm.setSituacaoContraPartida("4");
                break;
            default:
                tm.setSituacaoContraPartida("0");
                break;
        }
        tm.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        tm.setDataCadastro(regAtual.getDataCadastro());
        tm.setHoraCadastro(regAtual.getHoraCadastro());
        tm.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        tm.setDataModificacao(regAtual.getDataModificacao());
        tm.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                tm.setSituacao("1");
                break;
            case "I":
                tm.setSituacao("2");
                break;
        }
    }

    // Método par buscar nome do Municipio
    private String buscarContaReduzida(String cdContaReduzida) {
        ContasContabeis ctr = new ContasContabeis();
        try {
            CContasContabeis cctr = new CContasContabeis();
            String sqlcmu = "SELECT * FROM GFCPLANOCONTA WHERE REDUZIDO = '" + cdContaReduzida + "'";
            cctr.pesquisar(sqlcmu);
            cctr.mostrarPesquisa(ctr, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome o Município!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return ctr.getNomeConta();
    }
}