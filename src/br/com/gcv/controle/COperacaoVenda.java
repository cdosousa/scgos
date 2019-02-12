/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.gcv.dao.OperacaoVendaDAO;
import br.com.gcv.modelo.OperacaoVenda;
import br.com.gfr.controle.CCentroCustos;
import br.com.gfr.controle.CTiposOperacoes;
import br.com.gfr.modelo.CentroCustos;
import br.com.gfr.modelo.TiposOperacoes;
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
public class COperacaoVenda {

    // variáveis de instancia
    private Connection conexao;
    private List<OperacaoVenda> resultado = null;
    private OperacaoVenda regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public COperacaoVenda(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<OperacaoVenda>();
        OperacaoVendaDAO ovedao = new OperacaoVendaDAO(conexao);
        ovedao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(OperacaoVenda ove, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        ove.setCdOperacaoVenda(regAtual.getCdOperacaoVenda());
        ove.setNomeOperacaoVenda(regAtual.getNomeOperacaoVenda());
        ove.setCdTipoOper(regAtual.getCdTipoOper());
        String cdTipoOper = regAtual.getCdTipoOper();
        cdTipoOper = cdTipoOper.replace(".", "");
        ove.setNomeTipoOperacao(buscarTipoOperacao(cdTipoOper));
        String tipoFinalidade = regAtual.getTipoFinalidade().toUpperCase();
        switch(tipoFinalidade){
            case "GA":
                ove.setTipoFinalidade("1");
                break;
            case "RV":
                ove.setTipoFinalidade("2");
                break;
            case "RS":
                ove.setTipoFinalidade("3");
                break;
            case "SE":
                ove.setTipoFinalidade("4");
                break;
            case "VE":
                ove.setTipoFinalidade("5");
                break;
            default:
                ove.setTipoEstoque("0");
                break;
        }
        ove.setEmiteNfeVenda(regAtual.getEmiteNfeVenda());
        ove.setEmiteNfeServico(regAtual.getEmiteNfeServico());
        ove.setGeraCobranca(regAtual.getGeraCobranca());
        ove.setCdContrato(regAtual.getCdContrato());
        ove.setCdCCusto(regAtual.getCdCCusto());
        if(regAtual.getCdCCusto() != null)
            ove.setNomeCCusto(buscarCCusto(regAtual.getCdCCusto()));
        ove.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        ove.setDataCadastro(regAtual.getDataCadastro());
        ove.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                ove.setSituacao("1");
                break;
            case "I":
                ove.setSituacao("2");
                break;
        }
    }
    
    // Método para buscar nome do tipo de operacao
    private String buscarTipoOperacao(String cdTipoOper) {
        TiposOperacoes tp = new TiposOperacoes();
        try {
            CTiposOperacoes ctp = new CTiposOperacoes(conexao);
            String sqlpe = "SELECT * FROM  GFRTIPOOPERACAO WHERE CD_TIPOOPER = '" + cdTipoOper + "'";
            ctp.pesquisar(sqlpe);
            ctp.mostrarPesquisa(tp, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do nome do Tipo de Operação!\nPrograma: CTiposOperacoes.java\nErro: " + ex);
        }
        return tp.getNomeTipoOperacao();
    }
    
    /**
     * Métdo para buscar o nome do centro de custo
     * @param cdCCusto código do centro de custo a ser pesquisado
    */
    private String buscarCCusto(String cdCCusto){
        CentroCustos cc = new CentroCustos();
        try{
            CCentroCustos ccc = new CCentroCustos(conexao);
            String sqlcc = "SELECT * FROM GFRCCUSTO WHERE CD_CCUSTO = '" + cdCCusto
                    + "'";
            ccc.pesquisar(sqlcc);
            ccc.mostrarPesquisa(cc, 0);
        }catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Erro na busca do nome do Centro de Custo\nPrograma: COperacaoVenda.java" + ex);
        }
        return cc.getNomeCcusto();
    }
}