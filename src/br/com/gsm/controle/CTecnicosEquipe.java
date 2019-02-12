/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.controle;

import br.com.gsm.dao.TecnicosEquipeDAO;
import br.com.gsm.modelo.Especialidades;
import br.com.gsm.modelo.Tecnicos;
import br.com.gsm.modelo.TecnicosEquipe;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 26/10/2017
 */
public class CTecnicosEquipe {
    // variáveis de instancia
    private Connection conexao;
    private List<TecnicosEquipe> resultado = null;
    private TecnicosEquipe regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CTecnicosEquipe(Connection conexao){
        this.conexao = conexao;
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<TecnicosEquipe>();
        TecnicosEquipeDAO tedao = new TecnicosEquipeDAO();
        tedao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(TecnicosEquipe te, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        te.setCdEquipe(regAtual.getCdEquipe());
        te.setCpfTecnico(regAtual.getCpfTecnico());
        te.setNomeTecnico(buscarTecnico(regAtual.getCpfTecnico()));
        te.setCdEspecialidade(regAtual.getCdEspecialidade());
        te.setNomeEspecialidade(buscarEspecialidade(regAtual.getCdEspecialidade()));
        te.setPagarIndicacao(regAtual.getPagarIndicacao());
        te.setPagarObra(regAtual.getPagarObra());
        te.setPagarComissao(regAtual.getPagarComissao());
        te.setPercObra(regAtual.getPercObra());
        te.setValorObra(regAtual.getValorObra());
        te.setPercIndicacao(regAtual.getPercIndicacao());
        te.setValorIndicacao(regAtual.getValorIndicacao());
        te.setPercComissao(regAtual.getPercComissao());
        te.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        te.setDataCadastro(regAtual.getDataCadastro());
        te.setDataModificacao(regAtual.getDataModificacao());
        String situacao = String.valueOf(regAtual.getSituacao());
        switch(situacao){
            case "A":
                te.setSituacao('1');
                break;
            case "I":
                te.setSituacao('2');
                break;
        }
    }
    
    // Método para buscar nome do Tecnico
    private String buscarTecnico(String cdCpfTecnico) {
        Tecnicos t = new Tecnicos();
        try {
            CTecnicos ct = new CTecnicos(conexao);
            String sqlt = "SELECT * FROM  GSMTECNICOS WHERE CPF = '" + cdCpfTecnico + "'";
            ct.pesquisar(sqlt);
            ct.mostrarPesquisa(t, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do nome da tarefa!\nPrograma: CTarefasAtividades.java\nErro: " + ex);
        }
        return t.getNomeTecnico();
    }
    
    // Método para buscar nome da Especialidade
    private String buscarEspecialidade(String cdEspecialidade) {
        Especialidades e = new Especialidades();
        try {
            CEspecialidades ce = new CEspecialidades();
            String sqle = "SELECT * FROM  GSMESPECIALIDADES WHERE CD_ESPECIALIDADE = '" + cdEspecialidade + "'";
            ce.pesquisar(sqle);
            ce.mostrarPesquisa(e, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do nome da tarefa!\nPrograma: CTarefasAtividades.java\nErro: " + ex);
        }
        return e.getNomeEspecialidade();
    }
}