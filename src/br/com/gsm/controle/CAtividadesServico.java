/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.controle;

import br.com.gsm.dao.AtividadesServicoDAO;
import br.com.gsm.modelo.Atividades;
import br.com.gsm.modelo.AtividadesServico;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 30/10/2017
 */
public class CAtividadesServico {

    // variáveis de instancia
    private Connection conexao;
    private List<AtividadesServico> resultado = null;
    private AtividadesServico regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CAtividadesServico(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<AtividadesServico>();
        AtividadesServicoDAO atsvdao = new AtividadesServicoDAO(conexao);
        atsvdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(AtividadesServico atsv, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        atsv.setSequencia(regAtual.getSequencia());
        atsv.setCdServico(regAtual.getCdServico());
        atsv.setCdAtividade(regAtual.getCdAtividade());
        atsv.setNomeAtividade(buscarAtividade(regAtual.getCdAtividade()));
        atsv.setValorAtividade(regAtual.getValorAtividade());
        atsv.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        atsv.setDataCadastro(regAtual.getDataCadastro());
        atsv.setDataModificacao(regAtual.getDataModificacao());
        if (regAtual.getSituacao() == null) {
            atsv.setSituacao("0");
        } else {
            String situacao = regAtual.getSituacao();
            switch (situacao) {
                case "A":
                    atsv.setSituacao("1");
                    break;
                case "I":
                    atsv.setSituacao("2");
                    break;
                default:
                    atsv.setSituacao("0");
                    break;
            }
        }
    }

    // Método para buscar nome da Tarefa
    private String buscarAtividade(String cdAtividade) {
        Atividades a = new Atividades();
        try {
            CAtividades ca = new CAtividades(conexao);
            String sqlca = "SELECT * FROM  GSMATIVIDADES WHERE CD_ATIVIDADE = '" + cdAtividade + "'";
            ca.pesquisar(sqlca);
            ca.mostrarPesquisa(a, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do nome da atividade!\nPrograma: CAtividadesTarefas.java\nErro: " + ex);
        }
        return a.getNomeAtividade();
    }
}