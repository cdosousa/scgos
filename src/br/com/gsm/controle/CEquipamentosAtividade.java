/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.controle;

import br.com.gsm.dao.EquipamentosAtividadeDAO;
import br.com.gsm.modelo.Equipamentos;
import br.com.gsm.modelo.EquipamentosAtividade;
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
public class CEquipamentosAtividade {

    // variáveis de instancia
    private Connection conexao;
    private List<EquipamentosAtividade> resultado = null;
    private EquipamentosAtividade regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CEquipamentosAtividade(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<EquipamentosAtividade>();
        EquipamentosAtividadeDAO eadao = new EquipamentosAtividadeDAO(conexao);
        eadao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(EquipamentosAtividade ea, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        ea.setSequencial(regAtual.getSequencial());
        ea.setCdAtividade(regAtual.getCdAtividade());
        ea.setCdEquipamento(regAtual.getCdEquipamento());
        ea.setNomeEquipamento(buscarEquipamento(regAtual.getCdEquipamento()));
        ea.setValorUnit(regAtual.getValorUnit());
        ea.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        ea.setDataCadastro(regAtual.getDataCadastro());
        ea.setDataModificacao(regAtual.getDataModificacao());
        if (regAtual.getSituacao() == null) {
            ea.setSituacao("0");
        } else {
            String situacao = regAtual.getSituacao();
            switch (situacao) {
                case "A":
                    ea.setSituacao("1");
                    break;
                case "I":
                    ea.setSituacao("2");
                    break;
                default:
                    ea.setSituacao("0");
                    break;
            }
        }
    }

    // Método para buscar nome da Tarefa
    private String buscarEquipamento(String cdEquipamento) {
        Equipamentos e = new Equipamentos();
        try {
            CEquipamentos pe = new CEquipamentos();
            String sqlpe = "SELECT * FROM  GSMEQUIPAMENTOS WHERE CD_EQUIPAMENTO = '" + cdEquipamento + "'";
            pe.pesquisar(sqlpe);
            pe.mostrarPesquisa(e, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do nome da tarefa!\nPrograma: CTarefasAtividades.java\nErro: " + ex);
        }
        return e.getNomeEquipamento();
    }
}
