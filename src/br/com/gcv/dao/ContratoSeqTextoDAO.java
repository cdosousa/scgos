/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.dao;

import br.com.gcv.modelo.ContratoSeqTexto;
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
 * @version 0.01beta_0917 created on 27/03/2018
 */
public class ContratoSeqTextoDAO extends AbstractTableModel {

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    private int numLinhas;
    private boolean statusConexao;

    // método construtor da classe para inicializar a conexão
    public ContratoSeqTextoDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }

    // método para poder criar o registro no banco
    public void adicionar(ContratoSeqTexto cst) {
        String sql = "INSERT INTO GCVCONTRATOSEQTEXTO(CD_CONTRATO,"
                + " CD_SEQUENCIA,"
                + " TEXTO_LONGO,"
                + " USUARIO_CADASTRO,"
                + " DATA_CADASTRO,"
                + " HORA_CADASTRO,"
                + " USUARIO_MODIFICACAO,"
                + " DATA_MODIFICACAO,"
                + " HORA_MODIFICACAO,"
                + " SITUACAO) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cst.getCdContrato());
            pstmt.setString(2, cst.getCdSequencia());
            pstmt.setString(3, cst.getTextoLongo());
            pstmt.setString(4, cst.getUsuarioCadastro());
            pstmt.setString(5, cst.getDataCadastro());
            pstmt.setString(6, cst.getHoraCadastro());
            pstmt.setString(7, cst.getUsuarioModificacao());
            pstmt.setString(8, cst.getDataModificacao());
            pstmt.setString(9, cst.getHoraModificacao());
            pstmt.setString(10, cst.getSituacao());
            pstmt.execute();
            pstmt.close();
            //JOptionPane.showMessageDialog(null, "ContratoXSequenciaXTexto gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Numero de ContratoXSequenciaXTexto já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do ContratoXSequenciaXTexto, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }

    // método para poder atualizar o registro
    public void atualizar(ContratoSeqTexto cst) {
        String sql = "UPDATE GCVCONTRATOSEQTEXTO SET TEXTO_LONGO = ?,"
                + " USUARIO_MODIFICACAO = ?,"
                + " DATA_MODIFICACAO = ?,"
                + " HORA_MODIFICACAO = ?,"
                + " SITUACAO = ?"
                + " WHERE CD_CONTRATO = ? AND CD_SEQUENCIA = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cst.getTextoLongo());
            pstmt.setString(2, cst.getUsuarioModificacao());
            pstmt.setString(3, cst.getDataModificacao());
            pstmt.setString(4, cst.getHoraModificacao());
            pstmt.setString(5, cst.getSituacao());
            pstmt.setString(6, cst.getCdContrato());
            pstmt.setString(7, cst.getCdSequencia());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "ContratoXSequenciaXTexto atualizado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização do ContratoXSequenciaXTexto!\nErr: " + ex);
        }
    }

    // método para selecionar os registros no banco
    public void selecionar(List<ContratoSeqTexto> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new ContratoSeqTexto(
                                rs.getString("cd_contrato"),
                                rs.getString("cd_sequencia"),
                                rs.getString("texto_longo"),
                                rs.getString("usuario_cadastro"),
                                rs.getString("data_cadastro"),
                                rs.getString("hora_cadastro"),
                                rs.getString("usuario_modificacao"),
                                rs.getString("data_modificacao"),
                                rs.getString("hora_modificacao"),
                                rs.getString("situacao")));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca do ContratoXSequenciaXTexto!\nErr: " + ex);
                    }
                }
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: ContratoSeqTextoDAO.java\nErr: " + sqlex);
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
    public void excluir(ContratoSeqTexto cst) {
        String sql = "DELETE FROM GCVCONTRATOSEQTEXTO WHERE CD_CONTRATO = ? AND CD_SEQUENCIA = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cst.getCdContrato());
            pstmt.setString(2, cst.getCdSequencia());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "ContratoXSequenciaXTexto excluído com sucesso!");
        } catch (Exception ex) {
            if (ex.getMessage().contains("CONSTRAINT")) {
                JOptionPane.showMessageDialog(null, "Este ContratoXSequenciaXTexto já está sendo usado, exclusão não permitida!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na exclusão do ContratoXSequenciaXTexto!\nErr: " + ex);
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