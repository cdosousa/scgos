/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.gfc.dao.HistoricoDAO;
import br.com.gfc.modelo.Historico;
import br.com.modelo.DataSistema;
import br.com.modelo.HoraSistema;
import br.com.modelo.SessaoUsuario;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa criado em 24/09/2018
 * @version 0.01_beta0917
 */
public class CHistorico {

    // variáveis de instancia
    private List<Historico> resultado = null;
    private Historico regAtual;
    private Connection conexao;
    private SessaoUsuario su;
    private int idxAtual;
    private int numReg;
    private String sql;

    /**
     * Construtor padrão
     *
     * @param conexao Objeto contendo a instância de conexão do usuário com o
     * banco de dados
     */
    public CHistorico(Connection conexao, SessaoUsuario su) {
        this.conexao = conexao;
        this.su = su;
    }

    /**
     * método para acionar a pesquisa dos registro
     *
     * @param sql String com a sentença SQL a ser executada no banco de dados
     * @return retorna a quantidade de registros encontrada
     * @throws SQLException lança uma exeção caso ocorra erro
     */
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<Historico>();
        HistoricoDAO hsdao = new HistoricoDAO(conexao);
        hsdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    /**
     * método para prencher a tela com os registros pesquisados
     *
     * @param hs Objeto que será carregado com os dados do registro pesquisado
     * @param idxAtual indice do registro que deverá ser carregado
     */
    public void mostrarPesquisa(Historico hs, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        hs.setCdHistorico(regAtual.getCdHistorico());
        hs.setNomeHistorico(regAtual.getNomeHistorico());
        switch (regAtual.getTipoHistorico()) {
            case "Co":
                hs.setTipoHistorico("1");
                break;
            case "Fi":
                hs.setTipoHistorico("2");
                break;
            default:
                hs.setTipoHistorico("0");
                break;
        }
        switch (regAtual.getDocumentoComplementa()) {
            case "S":
                hs.setDocumentoComplementa("1");
                break;
            case "N":
                hs.setDocumentoComplementa("2");
                break;
            default:
                hs.setDocumentoComplementa("0");
                break;
        }
        switch (regAtual.getEmissaoComplementa()) {
            case "S":
                hs.setEmissaoComplementa("1");
                break;
            case "N":
                hs.setEmissaoComplementa("2");
                break;
            default:
                hs.setEmissaoComplementa("0");
                break;
        }
        switch (regAtual.getEmpresaComplementa()) {
            case "S":
                hs.setEmpresaComplementa("1");
                break;
            case "N":
                hs.setEmpresaComplementa("2");
                break;
            default:
                hs.setEmpresaComplementa("0");
                break;
        }
        hs.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        hs.setDataCadastro(regAtual.getDataCadastro());
        hs.setHoraCadastro(regAtual.getHoraCadastro());
        hs.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        hs.setDataModificacao(regAtual.getDataModificacao());
        hs.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                hs.setSituacao("1");
                break;
            case "I":
                hs.setSituacao("2");
                break;
        }
    }

    /**
     * Método para salvar o registro da tela
     *
     * @param hs
     * @param acao
     * @return
     */
    public int salvarHistorico(Historico hs, String acao) {
        DataSistema dat = new DataSistema();
        HoraSistema h = new HoraSistema();
        dat.setData("");
        try {
            HistoricoDAO dao = new HistoricoDAO(conexao);
            if ("N".equals(acao)) {
                hs.setUsuarioCadastro(su.getUsuarioConectado());
                hs.setDataCadastro(dat.getData());
                hs.setHoraCadastro(h.getHora());
                dao.adicionar(hs);
                return 1;
            } else {
                hs.setUsuarioModificacao(su.getUsuarioConectado());
                hs.setDataModificacao(dat.getData());
                hs.setHoraModificacao(h.getHora());
                dao.atualizar(hs);
                return 1;
            }
        } catch (SQLException ex) {
            mensagemTela("Erro na gravação do registro!\nErro: " + ex);
            return 0;
        }
    }

    /**
     * Método para excluir o registro atual da tela
     *
     * @return
     */
    public int excluirRegistro() {
        try {
            HistoricoDAO dao = new HistoricoDAO(conexao);
            dao.excluir(regAtual);
            return 1;
        } catch (SQLException ex) {
            mensagemTela("Erro na exclusão do registro!\nErro: " + ex);
            return 0;
        }
    }

    /**
     * Método para mostrar mensagem na tela
     *
     * @param msg
     */
    public void mensagemTela(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }
}
