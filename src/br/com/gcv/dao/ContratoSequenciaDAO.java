/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.dao;

import br.com.gcv.modelo.ContratoSequencia;
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
 * @version 0.01beta_0917 created on 21/03/2018
 */
public class ContratoSequenciaDAO extends AbstractTableModel {

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private PreparedStatement stmtCs;
    private ResultSet rs;
    private ResultSet rsCs;
    private ResultSetMetaData metaData;
    private int numLinhas;
    private boolean statusConexao;

    // método construtor da classe para inicializar a conexão
    public ContratoSequenciaDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }

    // método para poder criar o registro no banco
    public void adicionar(ContratoSequencia cs) {
        String sql = "INSERT INTO GCVCONTRATOSEQUENCIA(CD_CONTRATO,"
                + " CD_SEQUENCIA,"
                + " CD_SEQUENCIAPAI,"
                + " TIPO_SEQUENCIA,"
                + " CD_SEQANTECESSORA,"
                + " TITULO,"
                + " ALINHAMENTO_TITULO,"
                + " POSSUI_TEXTO,"
                + " QUEBRA_LINHA,"
                + " USUARIO_CADASTRO,"
                + " DATA_CADASTRO,"
                + " HORA_CADASTRO,"
                + " USUARIO_MODIFICACAO,"
                + " DATA_MODIFICACAO,"
                + " HORA_MODIFICACAO,"
                + " SITUACAO) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cs.getCdContrato());
            pstmt.setString(2, cs.getCdSequencia());
            pstmt.setString(3, cs.getCdSequenciaPai());
            pstmt.setString(4, cs.getTipoSequencia());
            pstmt.setString(5, cs.getCdSequenciaAtencessora());
            pstmt.setString(6, cs.getTitulo());
            pstmt.setString(7, cs.getPosicaoTitulo());
            pstmt.setString(8, cs.getPossuiTexto());
            pstmt.setString(9, cs.getQuebraLinha());
            pstmt.setString(10, cs.getUsuarioCadastro());
            pstmt.setString(11, cs.getDataCadastro());
            pstmt.setString(12, cs.getHoraCadastro());
            pstmt.setString(13, cs.getUsuarioModificacao());
            pstmt.setString(14, cs.getDataModificacao());
            pstmt.setString(15, cs.getHoraModificacao());
            pstmt.setString(16, cs.getSituacao());
            pstmt.execute();
            pstmt.close();
            //JOptionPane.showMessageDialog(null, "Sequencia Contrato gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Numero de Sequencia de Contrato já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação da Sequencia Contrato, informe o administrador do sistema!\nErr: " + ex +
                        "\nPossui Texto: "+cs.getPossuiTexto());
            }
        }
    }

    // método para poder atualizar o registro
    public void atualizar(ContratoSequencia cs) {
        String sql = "UPDATE GCVCONTRATOSEQUENCIA SET CD_SEQUENCIAPAI = ?,"
                + " CD_SEQANTECESSORA = ?,"
                + " TITULO = ?,"
                + " ALINHAMENTO_TITULO = ?,"
                + " POSSUI_TEXTO = ?,"
                + " QUEBRA_LINHA = ?,"
                + " USUARIO_MODIFICACAO = ?,"
                + " DATA_MODIFICACAO = ?,"
                + " HORA_MODIFICACAO = ?,"
                + " SITUACAO = ?"
                + " WHERE CD_CONTRATO = ? AND CD_SEQUENCIA = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cs.getCdSequenciaPai());
            pstmt.setString(2, cs.getCdSequenciaAtencessora());
            pstmt.setString(3, cs.getTitulo());
            pstmt.setString(4, cs.getPosicaoTitulo());
            pstmt.setString(5, cs.getPossuiTexto());
            pstmt.setString(6, cs.getQuebraLinha());
            pstmt.setString(7, cs.getUsuarioModificacao());
            pstmt.setString(8, cs.getDataModificacao());
            pstmt.setString(9, cs.getHoraModificacao());
            pstmt.setString(10, cs.getSituacao());
            pstmt.setString(11, cs.getCdContrato());
            pstmt.setString(12, cs.getCdSequencia());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Sequencia Contrato atualizado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização da Sequencia Contrato!\nErr: " + ex);
        }
    }

    

    // Método para excluir registros
    public void excluir(ContratoSequencia cs) {
        String sql = "DELETE FROM GCVCONTRATOSEQUENCIA WHERE CD_CONTRATO = ? AND CD_SEQUENCIA = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cs.getCdContrato());
            pstmt.setString(2, cs.getCdSequencia());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Sequencia Contrato excluído com sucesso!");
        } catch (Exception ex) {
            if (ex.getMessage().contains("CONSTRAINT")) {
                JOptionPane.showMessageDialog(null, "Esta Sequencia Contrato já está sendo usado, exclusão não permitida!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na exclusão da Sequencia Contrato!\nErr: " + ex);
            }
        }
    }
    
    /**
     * Método para pesquisar o Sequencia do Contrato e retornar o resultado da pesquisa em uma
     * arraylist.
     *
     * @param resultado
     * @param sql Recebe uma String contendo o Select para o banco de dados.
     * @return rsPed Retorna o resultSet contendo o conteúda da Select executada
     * no banco.
     */
    public ResultSet pesquisarSeqquenciaContrato(String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                stmtCs = conexao.prepareStatement(sql);
                rsCs = stmtCs.executeQuery(sql);
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: ContratoDAO.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nPrograma: ContratoDAO.java\nErr: " + ex);
        }
        return rsCs;
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