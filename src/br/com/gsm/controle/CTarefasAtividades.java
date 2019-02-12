/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.controle;

import br.com.gsm.dao.TarefasAtividadesDAO;
import br.com.gsm.modelo.Especialidades;
import br.com.gsm.modelo.Tarefas;
import br.com.gsm.modelo.TarefasAtividade;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 02/10/2017
 */
public class CTarefasAtividades {

    // variáveis de instancia
    private Connection conexao;
    private List<TarefasAtividade> resultado = null;
    private TarefasAtividade regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CTarefasAtividades(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<TarefasAtividade>();
        TarefasAtividadesDAO tadao = new TarefasAtividadesDAO(conexao);
        tadao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(TarefasAtividade ta, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        ta.setSequencia(regAtual.getSequencia());
        ta.setCdAtividade(regAtual.getCdAtividade());
        ta.setCdTarefa(regAtual.getCdTarefa());
        ta.setNomeTarefa(buscarTarefa(regAtual.getCdTarefa()));
        ta.setCdEspecialidade(regAtual.getCdEspecialidade());
        ta.setNomeEspecialidade(buscarEspecialidade(regAtual.getCdEspecialidade()));
        ta.setValorUnit(regAtual.getValorUnit());
        ta.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        ta.setDataCadastro(regAtual.getDataCadastro());
        ta.setDataModificacao(regAtual.getDataModificacao());
        if (regAtual.getSituacao() == null) {
            ta.setSituacao("0");
        } else {
            String situacao = regAtual.getSituacao();
            switch (situacao) {
                case "A":
                    ta.setSituacao("1");
                    break;
                case "I":
                    ta.setSituacao("2");
                    break;
                default:
                    ta.setSituacao("0");
                    break;
            }
        }
    }
    
    // Método para buscar nome da Tarefa
    private String buscarTarefa(String cdTarefa) {
        Tarefas t = new Tarefas();
        try {
            CTarefas pt = new CTarefas();
            String sqlpt = "SELECT * FROM  GSMTAREFAS WHERE CD_TAREFA = '" + cdTarefa + "'";
            pt.pesquisar(sqlpt);
            pt.mostrarPesquisa(t, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do nome da tarefa!\nPrograma: CTarefasAtividades.java\nErro: " + ex);
        }
        return t.getNomeTarefa();
    }
    
    // Método para buscar nome da Especialidade
    private String buscarEspecialidade(String cdEspecialidade) {
        Especialidades e = new Especialidades();
        try {
            CEspecialidades pe = new CEspecialidades();
            String sqlpe = "SELECT * FROM  GSMESPECIALIDADES WHERE CD_ESPECIALIDADE = '" + cdEspecialidade + "'";
            pe.pesquisar(sqlpe);
            pe.mostrarPesquisa(e, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do nome da Especialidade!\nPrograma: CTarefasAtividades.java\nErro: " + ex);
        }
        return e.getNomeEspecialidade();
    }
}