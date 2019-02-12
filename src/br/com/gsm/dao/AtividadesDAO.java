/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.dao;

import br.com.fabrica.ConexaoDataBase;
import br.com.gsm.modelo.Atividades;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01beta_0917 created on 22/09/2017
 */
public class AtividadesDAO extends AbstractTableModel {

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    private int numLinhas;
    private int pass = 0;
    private boolean statusConexao;

    // método construtor da classe para inicializar a conexão
    public AtividadesDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        //this.conexao = new ConexaoDataBase().conexao();
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }

    // método para poder criar o registro no banco
    public void adicionar(Atividades atv) {
        String sql = "INSERT INTO GSMATIVIDADES(CD_ATIVIDADE, NOME_ATIVIDADE,VAL_TOTAL_TAREFAS,VAL_TOTAL_EQUIPAMENTOS, VAL_TOTAL_ATIVIDADE, USUARIO_CADASTRO, DATA_CADASTRO, DATA_MODIFICACAO, SITUACAO) VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, atv.getCdAtividade());
            pstmt.setString(2, atv.getNomeAtividade());
            pstmt.setDouble(3, atv.getValTotalTarefa());
            pstmt.setDouble(4, atv.getValTotalEquipamento());
            pstmt.setDouble(5, atv.getValTotalAtividade());
            pstmt.setString(6, atv.getUsuarioCadastro());
            pstmt.setString(7, atv.getDataCadastro());
            pstmt.setString(8, atv.getDataModificacao());
            pstmt.setString(9, atv.getSituacao());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Código de Atividade já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação da Atividade, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }

    // método para poder atualizar o registro
    public void atualizar(Atividades atv) {
        String sql = "UPDATE GSMATIVIDADES SET NOME_ATIVIDADE = ?,"
                + "VAL_TOTAL_TAREFAS = ?,"
                + "VAL_TOTAL_EQUIPAMENTOS = ?,"
                + "VAL_TOTAL_ATIVIDADE = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + "WHERE CD_ATIVIDADE = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, atv.getNomeAtividade());
            pstmt.setDouble(2, atv.getValTotalTarefa());
            pstmt.setDouble(3, atv.getValTotalEquipamento());
            pstmt.setDouble(4, atv.getValTotalAtividade());
            pstmt.setString(5, atv.getDataModificacao());
            pstmt.setString(6, atv.getSituacao());
            pstmt.setString(7, atv.getCdAtividade());
            pstmt.execute();
            pstmt.close();
            if (!atv.isAtualizacao()) {
                JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização do registro!\nErr: " + ex);
        }
    }

    // método para selecionar os registros no banco
    public void selecionar(List<Atividades> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new Atividades(
                                rs.getString("cd_atividade"),
                                rs.getString("nome_atividade"),
                                rs.getDouble("val_total_tarefas"),
                                rs.getDouble("val_total_equipamentos"),
                                rs.getDouble("val_total_atividade"),
                                rs.getString("usuario_cadastro"),
                                rs.getString("data_cadastro"),
                                rs.getString("data_modificacao"),
                                rs.getString("situacao")));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca do Registro!\nErr: " + ex);
                    }
                }
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nErr: " + ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                desconectar();
            }
        }
    }

    // Método para excluir registros
    public void excluir(Atividades atv) {
        String sql = "DELETE FROM GSMATIVIDADES WHERE CD_ATIVIDADE = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, atv.getCdAtividade());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro excluído com sucesso!");
        } catch (Exception ex) {
            if (ex.getMessage().contains("constraint")) {
                JOptionPane.showMessageDialog(null, "Não é possível excluir o registro, existe tarefas vinculadas!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na exclusão do registro!\nErr: " + ex);
            }
        }
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return false;
    }

    // método que obtém a classe que representa o número de colunas
    public Class getColumnClass(int column) throws IllegalStateException {
        //certificando que a conexão está disponível com o banco
        if (!statusConexao) {
            throw new IllegalStateException("Sem conexão com o Banco de Dados");
        }
        //determina a classe java de coluna
        try {
            String classeName = metaData.getColumnClassName(column + 1);
            //retorna o objeto da classe que representa o classname
            return Class.forName(classeName);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return Object.class; //se ocorrerem os problemas acima, supõe tipo Object
    }

    @Override
    public int getColumnCount() throws IllegalStateException {
        //certificando que a conexão está disponível com o banco
        if (!statusConexao) {
            throw new IllegalStateException("Sem conexão com o Banco de Dados");
        }
        // determina o número de linhas
        try {
            return metaData.getColumnCount();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0; //se ocorrerem problemas acima retorna 0 para o número de colunas
    }

    // método que obtém o nome de uma coluna particular no ResultSet
    public String getColumnName(int column) throws IllegalStateException {
        if (!statusConexao) {
            throw new IllegalStateException("Sem conexão com o Banco de Dados");
        }
        //determina o nome da coluna
        try {
            return metaData.getColumnLabel(column + 1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ""; //se ocorrerem problemas, retorna string vazia para o numero da coluna
    }

    @Override
    public int getRowCount() throws IllegalStateException {
        if (!statusConexao) {
            throw new IllegalStateException("Sem conexão com o Banco de Dados");
        }
        return numLinhas;
    }

    // método que obtem o valor na linha e coluna específica
    @Override
    public Object getValueAt(int row, int column) throws IllegalStateException {
        if (!statusConexao) {
            throw new IllegalStateException("Sem conexão com o Banco de Dados");
        }
        // obttem o valor na linha e coluna de ResultSet especificada
        try {
            rs.absolute(row + 1);
            return rs.getObject(column + 1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return ""; // se ocorrerem problemas retorna o objetct string vazio
    }

    // método que configura uma nova string de consulta com o banco
    public void setQuery(String query) throws SQLException, IllegalStateException {
        if (!statusConexao) {
            throw new IllegalStateException("Sem conexão com o Banco de Dados");
        }
        // especifica a consulta e executa
        rs = stmt.executeQuery(query);
        // obtém metadados para o resultSet
        metaData = rs.getMetaData();
        // determina o número de linhas em resultSet
        rs.last(); // move para a última linha
        numLinhas = rs.getRow(); //obtém o número da linha
        // notifica o Jtable que o modelo foi alterado
        fireTableStructureChanged();
    }

    // método para desconectar o banco
    public void desconectar() {
        if (statusConexao) {
            try {
                rs.close();
                stmt.close();
                conexao.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                statusConexao = false;
            }
        }
    }
}
