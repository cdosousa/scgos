/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.dao;

import br.com.gcv.modelo.Contato;
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
 * @version 0.01beta_0917 created on 22/11/2017
 */
public class ContatoDAO extends AbstractTableModel {

    //variáveis de instância
    private Connection conexao;
    private Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    private int numLinhas;
    private int ultSequencia;
    private int pass = 0;
    private boolean statusConexao;

    // método construtor da classe para inicializar a conexão
    public ContatoDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        //stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        stmt = conexao.createStatement();
        statusConexao = true;
    }

    // método para poder criar o registro no banco
    public void adicionar(Contato con) {
        String sql = "INSERT INTO GCVCONTATO(CD_ATENDIMENTO,"
                + " DATA_ATENDIMENTO,"
                + " HORA_ATENDIMENTO,"
                + " NOME_CLIENTE,"
                + " TIPO_PESSOA,"
                + " TELEFONE,"
                + " EMAIL,"
                + " CD_VISTORIA,"
                + " CD_PROPOSTA,"
                + " OBS,"
                + " USUARIO_CADASTRO,"
                + " DATA_CADASTRO,"
                + " HORA_CADASTRO,"
                + " USUARIO_MODIFICACAO,"
                + " DATA_MODIFICACAO,"
                + " HORA_MODIFICACAO,"
                + " SITUACAO) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String telefone = con.getTelefone();
        telefone = telefone.replace("(", "");
        telefone = telefone.replace(")", "");
        telefone = telefone.replace("-", "");
        telefone = telefone.replace(" ", "");
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, con.getCdAtendimento());
            pstmt.setString(2, con.getDataAtendimento());
            pstmt.setString(3, con.getHoraAtendimento());
            pstmt.setString(4, con.getNomeRazaoSocial());
            pstmt.setString(5, con.getTipoPessoa());
            pstmt.setString(6, telefone);
            pstmt.setString(7, con.getEmail());
            pstmt.setString(8, con.getCdVistoria());
            pstmt.setString(9, con.getCdProposta());
            pstmt.setString(10, con.getObs());
            pstmt.setString(11, con.getUsuarioCadastro());
            pstmt.setString(12, con.getDataCadastro());
            pstmt.setString(13, con.getHoraCadastro());
            pstmt.setString(14, con.getUsuarioModificacao());
            pstmt.setString(15, con.getDataModificacao());
            pstmt.setString(16, con.getHoraModificacao());
            pstmt.setString(17, con.getSituacao());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Contato gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Numero de Contato já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Contato, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }

    // método para poder atualizar o registro
    public void atualizar(Contato con) {
        String sql = "UPDATE GCVCONTATO SET NOME_CLIENTE = ?,"
                + " TIPO_PESSOA = ?,"
                + " TELEFONE = ?,"
                + " EMAIL = ?,"
                + " CD_VISTORIA = ?,"
                + " CD_PROPOSTA = ?,"
                + " OBS = ?,"
                + " SITUACAO = ?,"
                + " USUARIO_MODIFICACAO = ?,"
                + " DATA_MODIFICACAO = ?,"
                + " HORA_MODIFICACAO = ?"
                + " WHERE CD_ATENDIMENTO = ?";
        String telefone;
        telefone = con.getTelefone();
        telefone = telefone.replace("(", "");
        telefone = telefone.replace(")", "");
        telefone = telefone.replace("-", "");
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, con.getNomeRazaoSocial());
            pstmt.setString(2, con.getTipoPessoa());
            pstmt.setString(3, telefone);
            pstmt.setString(4, con.getEmail());
            pstmt.setString(5, con.getCdVistoria());
            pstmt.setString(6, con.getCdProposta());
            pstmt.setString(7, con.getObs());
            pstmt.setString(8, con.getSituacao());
            pstmt.setString(9, con.getUsuarioModificacao());
            pstmt.setString(10, con.getDataModificacao());
            pstmt.setString(11, con.getHoraModificacao());
            pstmt.setString(12, con.getCdAtendimento());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Contato atualizado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização do Contato!\nErr: " + ex);
        }
    }

    // método para selecionar os registros no banco
    public void selecionar(List<Contato> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new Contato(
                                rs.getString("cd_atendimento"),
                                rs.getString("data_atendimento"),
                                rs.getString("hora_atendimento"),
                                rs.getString("nome_cliente"),
                                rs.getString("tipo_pessoa"),
                                rs.getString("telefone"),
                                rs.getString("email"),
                                rs.getString("cd_vistoria"),
                                rs.getString("cd_proposta"),
                                rs.getString("obs"),
                                rs.getString("usuario_cadastro"),
                                rs.getString("data_cadastro"),
                                rs.getString("hora_cadastro"),
                                rs.getString("usuario_modificacao"),
                                rs.getString("data_modificacao"),
                                rs.getString("hora_modificacao"),
                                rs.getString("situacao")));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca do Contato!\nErr: " + ex);
                    }
                }
                pstmt.close();
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: ContatoDAO.java\nErr: " + sqlex);
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
    public void excluir(Contato con) {
        String sql = "DELETE FROM GCVCONTATO WHERE CD_ATENDIMENTO = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, con.getCdAtendimento());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Contato excluído com sucesso!");
        } catch (Exception ex) {
            if (ex.getMessage().contains("CONSTRAINT")) {
                JOptionPane.showMessageDialog(null, "Este Contato já está sendo usado, exclusão não permitida!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na exclusão do Contato!\nErr: " + ex);
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
