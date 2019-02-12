/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.gfc.dao.TipoLancamentoDAO;
import br.com.gfc.modelo.Portadores;
import br.com.gfc.modelo.TipoLancamento;
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
public class CTipoLancamento {
    // variáveis de instancia
    private Connection conexao;
    private List<TipoLancamento> resultado = null;
    private TipoLancamento regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CTipoLancamento(Connection conexao){
        this.conexao = conexao;
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<TipoLancamento>();
        TipoLancamentoDAO tldao = new TipoLancamentoDAO(conexao);
        tldao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(TipoLancamento tl, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        tl.setCdTipoLancamento(regAtual.getCdTipoLancamento());
        tl.setNomeTipoLancamento(regAtual.getNomeTipoLancamento());
        tl.setCdPortador(regAtual.getCdPortador());
        tl.setNomePortador(buscarPortador(regAtual.getCdPortador()));
        tl.setPrefixo(regAtual.getPrefixo());
        tl.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        tl.setDataCadastro(regAtual.getDataCadastro());
        tl.setHoraCadastro(regAtual.getHoraCadastro());
        tl.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        tl.setDataModificacao(regAtual.getDataModificacao());
        tl.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = regAtual.getSituacao();
        switch(situacao){
            case "A":
                tl.setSituacao("1");
                break;
            case "I":
                tl.setSituacao("2");
                break;
        }
    }
    
    // Método par buscar nome do Municipio
    private String buscarPortador(String cdPortador) {
        Portadores por = new Portadores();
        try {
            CPortadores cpor = new CPortadores(conexao);
            String sqlcmu = "SELECT * FROM GFCPORTADOR WHERE CD_PORTADOR = '" + cdPortador + "'";
            cpor.pesquisar(sqlcmu);
            cpor.mostrarPesquisa(por, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome o Município!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return por.getNomePortador();
    }
}