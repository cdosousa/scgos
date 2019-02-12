/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.gfc.controle.CBancos;
import br.com.gfc.dao.ContasBancariasDAO;
import br.com.gfc.modelo.ContasBancarias;
import br.com.gfc.modelo.Bancos;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917
 * created on 03/11/2017
 */
public class CContasBancarias {
    // variáveis de instancia
    private Connection conexao;
    private List<ContasBancarias> resultado = null;
    private ContasBancarias regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CContasBancarias(Connection conexao){
        this.conexao = conexao;
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<ContasBancarias>();
        ContasBancariasDAO cbdao = new ContasBancariasDAO(conexao);
        cbdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(ContasBancarias cb, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        cb.setCdBanco(regAtual.getCdBanco());
        cb.setNomeBanco(buscarBanco(regAtual.getCdBanco()));
        cb.setCdAgencia(regAtual.getCdAgencia());
        cb.setCdAgenciaDig(regAtual.getCdAgenciaDig());
        cb.setTipoConta(regAtual.getTipoConta());
        cb.setCdConta(regAtual.getCdConta());
        cb.setCdContaDig(regAtual.getCdContaDig());
        cb.setLimite(regAtual.getLimite());
        cb.setSaldo(regAtual.getSaldo());
        cb.setTarifaAdm(regAtual.getTarifaAdm());
        cb.setTaxaJuros(regAtual.getTaxaJuros());
        cb.setDataAbertura(regAtual.getDataAbertura());
        cb.setDataEncerrametno(regAtual.getDataEncerrametno());
        cb.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        cb.setDataCadastro(regAtual.getDataCadastro());
        cb.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch(situacao){
            case "A":
                cb.setSituacao("1");
                break;
            case "I":
                cb.setSituacao("2");
                break;
        }
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
}