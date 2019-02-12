/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.gfc.dao.TipoCarteiraEDIDAO;
import br.com.gfc.modelo.Portadores;
import br.com.gfc.modelo.TipoCarteiraEDI;
import br.com.modelo.DataSistema;
import br.com.modelo.HoraSistema;
import br.com.modelo.SessaoUsuario;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 21/11/2018
 */
public class CTipoCarteiraEDI {

    // variáveis de instancia
    private Connection conexao;
    private SessaoUsuario su;
    private List<TipoCarteiraEDI> resultado = null;
    private TipoCarteiraEDI regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    private String cdBanco;
    private String cdConta;
    private String cdContaDig;
    private String cdAgencia;
    private String cdAgenciaDig;
    private String tipoConta;

    // Construtor padrão
    public CTipoCarteiraEDI(Connection conexao, SessaoUsuario su) {
        this.conexao = conexao;
        this.su = su;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<TipoCarteiraEDI>();
        TipoCarteiraEDIDAO tedidao = new TipoCarteiraEDIDAO(conexao);
        tedidao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(TipoCarteiraEDI cedi, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);

        cedi.setCdPortador(regAtual.getCdPortador());
        cedi.setNomePortador(buscarPortador(regAtual.getCdPortador()));
        cedi.setCdCarteira(regAtual.getCdCarteira());
        cedi.setNomeCarteira(regAtual.getNomeCarteira());
        cedi.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        cedi.setDataCadastro(regAtual.getDataCadastro());
        cedi.setHoraCadastro(regAtual.getHoraCadastro());
        cedi.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        cedi.setDataModificacao(regAtual.getDataModificacao());
        cedi.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                cedi.setSituacao("1");
                break;
            case "I":
                cedi.setSituacao("2");
                break;
        }
    }

    /**
     * Método para gravar o serviço no banco
     * @param cedi Objeto contendo as informações a serem gravadas
     * @param acao Tipo de ação a realizar sendo: A-Atualização ou N-Novo
     * @return
     * @throws SQLException 
     */
    public int gravarCarteira(TipoCarteiraEDI cedi, String acao) throws SQLException {
        DataSistema dat = new DataSistema();
        dat.setData("");
        HoraSistema hs = new HoraSistema();
        TipoCarteiraEDIDAO cedidao = new TipoCarteiraEDIDAO(conexao);
        if ("N".equals(acao)) {
            cedi.setUsuarioCadastro(su.getUsuarioConectado());
            cedi.setDataCadastro(dat.getData());
            cedi.setHoraCadastro(hs.getHora());
            cedidao.adicionar(cedi);
            return 1;
        }else{
            cedi.setUsuarioModificacao(su.getUsuarioConectado());
            cedi.setDataModificacao(dat.getData());
            cedi.setHoraModificacao(hs.getHora());
            cedidao.atualizar(cedi);
            return 1;
        }
    }

    // Método par buscar nome do Banco
    private String buscarPortador(String cdPortador) {
        Portadores por = new Portadores();
        try {
            CPortadores cpor = new CPortadores(conexao);
            String sqlcpor = "SELECT * FROM GFCPORTADOR WHERE CD_PORTADOR = '" + cdPortador + "'";
            cpor.pesquisar(sqlcpor);
            cpor.mostrarPesquisa(por, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome do Banco!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return por.getNomePortador();
    }
}