/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.gfc.modelo.Bancos;
import br.com.gfc.dao.TipoServicoEDIDAO;
import br.com.gfc.modelo.ContasBancarias;
import br.com.gfc.modelo.Portadores;
import br.com.gfc.modelo.TipoServicoEDI;
import br.com.modelo.DataSistema;
import br.com.modelo.HoraSistema;
import br.com.modelo.SessaoUsuario;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 21/11/2018
 */
public class CTipoServicoEDI {

    // variáveis de instancia
    private Connection conexao;
    private SessaoUsuario su;
    private List<TipoServicoEDI> resultado = null;
    private TipoServicoEDI regAtual;
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
    public CTipoServicoEDI(Connection conexao, SessaoUsuario su) {
        this.conexao = conexao;
        this.su = su;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<TipoServicoEDI>();
        TipoServicoEDIDAO tedidao = new TipoServicoEDIDAO(conexao);
        tedidao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(TipoServicoEDI tedi, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);

        tedi.setCdPortador(regAtual.getCdPortador());
        tedi.setNomePortador(buscarPortador(regAtual.getCdPortador()));
        tedi.setCdTipoServico(regAtual.getCdTipoServico());
        tedi.setNomeTipoServico(regAtual.getNomeTipoServico());
        tedi.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        tedi.setDataCadastro(regAtual.getDataCadastro());
        tedi.setHoraCadastro(regAtual.getHoraCadastro());
        tedi.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        tedi.setDataModificacao(regAtual.getDataModificacao());
        tedi.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                tedi.setSituacao("1");
                break;
            case "I":
                tedi.setSituacao("2");
                break;
        }
    }

    /**
     * Método para gravar o serviço no banco
     * @param tedi Objeto contendo as informações a serem gravadas
     * @param acao Tipo de ação a realizar sendo: A-Atualização ou N-Novo
     * @return
     * @throws SQLException 
     */
    public int gravarServico(TipoServicoEDI tedi, String acao) throws SQLException {
        DataSistema dat = new DataSistema();
        dat.setData("");
        HoraSistema hs = new HoraSistema();
        TipoServicoEDIDAO tedidao = new TipoServicoEDIDAO(conexao);
        if ("N".equals(acao)) {
            tedi.setUsuarioCadastro(su.getUsuarioConectado());
            tedi.setDataCadastro(dat.getData());
            tedi.setHoraCadastro(hs.getHora());
            tedidao.adicionar(tedi);
            return 1;
        }else{
            tedi.setUsuarioModificacao(su.getUsuarioConectado());
            tedi.setDataModificacao(dat.getData());
            tedi.setHoraModificacao(hs.getHora());
            tedidao.atualizar(tedi);
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
