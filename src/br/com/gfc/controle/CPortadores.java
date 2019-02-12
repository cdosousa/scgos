/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.gfc.modelo.Bancos;
import br.com.gfc.dao.PortadoresDAO;
import br.com.gfc.modelo.ContasBancarias;
import br.com.gfc.modelo.Portadores;
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
 * @version 0.01_beta0917 created on 07/11/2017
 */
public class CPortadores {

    // variáveis de instancia
    private Connection conexao;
    private List<Portadores> resultado = null;
    private Portadores regAtual;
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
    public CPortadores(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<Portadores>();
        PortadoresDAO pordao = new PortadoresDAO(conexao);
        pordao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Portadores por, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        por.setCdBanco(regAtual.getCdBanco());
        this.cdBanco = regAtual.getCdBanco();
        por.setNomeBanco(buscarBanco(regAtual.getCdBanco()));
        por.setCdConta(regAtual.getCdConta());
        this.cdConta = regAtual.getCdConta();
        por.setCdPortador(regAtual.getCdPortador());
        por.setNomePortador(regAtual.getNomePortador());
        por.setTaxaJuros(regAtual.getTaxaJuros());
        por.setTaxaMulta(regAtual.getTaxaMulta());
        por.setTaxaCorrecao(regAtual.getTaxaCorrecao());
        por.setDiasLiquidacao(regAtual.getDiasLiquidacao());
        por.setDiasCartorio(regAtual.getDiasCartorio());
        por.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        por.setDataCadastro(regAtual.getDataCadastro());
        por.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                por.setSituacao("1");
                break;
            case "I":
                por.setSituacao("2");
                break;
        }
        buscarCorrrelatos();
        por.setCdAgencia(this.cdAgencia);
        por.setCdAgenciaDig(this.cdAgenciaDig);
        por.setCdContaDig(this.cdContaDig);
        por.setTipoConta(tipoConta);
    }
    
    // método para preencher correlatos
    private void buscarCorrrelatos(){
        buscarConta(this.cdBanco, this.cdConta);
    }
    // Método par buscar nome do Banco
    private String buscarBanco(String cdBanco) {
        Bancos bc = new Bancos();
        try {
            CBancos cbc = new CBancos(conexao);
            String sqlcbc = "SELECT * FROM GFCBANCOS WHERE CD_BANCO = '" + cdBanco + "'";
            cbc.pesquisar(sqlcbc);
            cbc.mostrarPesquisa(bc, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome do Banco!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return bc.getNomeBanco();
    }

    // Método para buscar dados da conta
    private void buscarConta(String cdBanco, String cdConta) {
        ContasBancarias cb = new ContasBancarias();
        try {
            CContasBancarias ccb = new CContasBancarias(conexao);
            String sqlccb = "SELECT * FROM GFCCONTAS WHERE CD_BANCO = '" + cdBanco + "' and"
                    + " cd_conta = '" + cdConta + "'";
            ccb.pesquisar(sqlccb);
            ccb.mostrarPesquisa(cb, 0);
        } catch (SQLException ex) {
            Logger.getLogger(CPortadores.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.cdContaDig = cb.getCdContaDig();
        this.cdAgencia = cb.getCdAgencia();
        this.cdAgenciaDig = cb.getCdAgenciaDig();
        this.tipoConta = cb.getTipoConta();
    }
}
