/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.gfc.dao.EdiOcorrenciaDAO;
import br.com.gfc.modelo.Bancos;
import br.com.gfc.modelo.EdiOcorrencia;
import br.com.modelo.DataSistema;
import br.com.modelo.HoraSistema;
import br.com.modelo.SessaoUsuario;
import java.io.IOException;
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
 * @version 0.01_beta0917 created on 12/03/2019
 */
public class CEdiOcorrencia {

    // variáveis de instancia
    private Connection conexao;
    private SessaoUsuario su;
    private List<EdiOcorrencia> resultado = null;
    private EdiOcorrencia regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CEdiOcorrencia(Connection conexao, SessaoUsuario su) {
        this.conexao = conexao;
        this.su = su;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<EdiOcorrencia>();
        EdiOcorrenciaDAO eodao = new EdiOcorrenciaDAO(conexao);
        eodao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(EdiOcorrencia eo, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        eo.setCdBanco(regAtual.getCdBanco());
        eo.setNomeBanco(buscarBanco(eo.getCdBanco()));
        eo.setCdOcorrencia(regAtual.getCdOcorrencia());
        eo.setNomeOcorrencia(regAtual.getNomeOcorrencia());
        eo.setLiquidarTitulo(regAtual.getLiquidarTitulo());
        eo.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        eo.setDataCadastro(regAtual.getDataCadastro());
        eo.setHoraCadastro(regAtual.getHoraCadastro());
        eo.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        eo.setDataModificacao(regAtual.getDataModificacao());
        eo.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                eo.setSituacao("1");
                break;
            case "I":
                eo.setSituacao("2");
                break;
        }
    }

    // Método par buscar nome do Banco
    private String buscarBanco(String cdBanco) {
        Bancos bco = new Bancos();
        try {
            CBancos cbco = new CBancos(conexao);
            String sqlcmu = "SELECT * FROM GFCBANCOS WHERE CD_BANCO = '" + cdBanco + "'";
            cbco.pesquisar(sqlcmu);
            cbco.mostrarPesquisa(bco, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome do Banco!\nPrograma CEdiOcorrencia.\nErro: " + ex);
        }
        return bco.getNomeBanco();
    }

    /**
     * Método para gravar a ocorrência no banco de dados
     *
     * @param modEo
     * @param acao
     * @return
     */
    public int gravarOcorrencia(EdiOcorrencia modEo, String acao) {
        DataSistema dat = new DataSistema();
        dat.setData("");
        HoraSistema hs = new HoraSistema();
        try {
            EdiOcorrenciaDAO eodao = new EdiOcorrenciaDAO(conexao);
            if ("N".equals(acao)) {
                modEo.setUsuarioCadastro(su.getUsuarioConectado());
                modEo.setDataCadastro(dat.getData());
                modEo.setHoraCadastro(hs.getHora());
                return eodao.adicionar(modEo);
            } else {
                modEo.setUsuarioModificacao(su.getUsuarioConectado());
                modEo.setDataModificacao(dat.getData());
                modEo.setHoraModificacao(hs.getHora());
                return eodao.atualizar(modEo);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CEdiOcorrencia.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    /**
     * Método para importar o arquivo de ocorrência no banco de dados
     * @param caminhoArquivo caminho do aquivo que será importado
     */
    public void importarOcorrencia(String caminhoArquivo) {
        try {
            EdiOcorrenciaDAO eodao = new EdiOcorrenciaDAO(conexao);
            EdiOcorrencia eo = new EdiOcorrencia();
            try {
                eodao.abrirAquivo(eo, caminhoArquivo, su.getUsuarioConectado());
            } catch (IOException ex) {
                Logger.getLogger(CEdiOcorrencia.class.getName()).log(Level.SEVERE, null, ex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro na Gravação do Arquivo!\nErr: " + ex);
            }

        } catch (SQLException ex) {
            Logger.getLogger(CEdiOcorrencia.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
