/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.dao;

import br.com.gcv.modelo.ItemProposta;
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
 * @version 0.01beta_0917 created on 11/12/2017
 */
public class ItemPropostaDAO extends AbstractTableModel {

    //variáveis de instância
    private Connection conexao;
    private Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    private int numLinhas;
    private boolean statusConexao;

    // método construtor da classe para inicializar a conexão
    public ItemPropostaDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        //stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }

    // método para poder criar o registro no banco
    public void adicionar(ItemProposta itp) {
        String sql = "INSERT INTO GCVITEMPROPOSTA(CD_PROPOSTA,"
                + "CD_REVISAO,"
                + "CD_LOCAL,"
                + "SEQUENCIA,"
                + "SEQUENCIA_ATEND,"
                + "CD_MATERIAL,"
                + "CD_UNIDMEDIDA,"
                + "QUANTIDADE,"
                + "VALOR_UNIT_BRUTO,"
                + "VALOR_UNIT_LIQUIDO,"
                + "PERC_DESC,"
                + "VALOR_DESC,"
                + "TOTAL_ITEM_BRUTO,"
                + "TOTAL_ITEM_LIQUIDO,"
                + "TIPO_ITEM,"
                + "OBS_ITEM,"
                + "USUARIO_CADASTRO,"
                + "DATA_CADASTRO,"
                + "HORA_CADASTRO,"
                + "USUARIO_MODIFICACAO,"
                + "DATA_MODIFICACAO,"
                + "HORA_MODIFICACAO,"
                + "SITUACAO) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, itp.getCdProposta());
            pstmt.setString(2, itp.getCdRevisao());
            pstmt.setInt(3, itp.getCdLocal());
            pstmt.setInt(4, itp.getSequencia());
            pstmt.setInt(5, itp.getSequenciaAtend());
            pstmt.setString(6, itp.getCdMaterial());
            pstmt.setString(7, itp.getCdUnidmedida());
            pstmt.setDouble(8, itp.getQuantidade());
            pstmt.setDouble(9, itp.getValorUnitBruto());
            pstmt.setDouble(10, itp.getValorUnitLiquido());
            pstmt.setDouble(11, itp.getPercDesconto());
            pstmt.setDouble(12, itp.getValorDescontos());
            pstmt.setDouble(13, itp.getValorTotalItemBruto());
            pstmt.setDouble(14, itp.getValorTotalItemLiquido());
            pstmt.setString(15, itp.getTipoItem());
            pstmt.setString(16, itp.getObsItem());
            pstmt.setString(17, itp.getUsuarioCadastro());
            pstmt.setString(18, itp.getDataCadastro());
            pstmt.setString(19, itp.getHoraCadastro());
            pstmt.setString(20, itp.getUsuarioModificacao());
            pstmt.setString(21, itp.getDataModificacao());
            pstmt.setString(22, itp.getHoraModificacao());
            pstmt.setString(23, itp.getSituacao());
            pstmt.execute();
            pstmt.close();
            //JOptionPane.showMessageDialog(null, "Item Gravado!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Item já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Item, informe o administrador do sistema!\nErr: " + ex);
                ex.printStackTrace();
            }
        }
    }

    // método para poder atualizar o registro
    public void atualizar(ItemProposta itp) {
        String sql = "UPDATE GCVITEMPROPOSTA SET CD_MATERIAL = ?,"
                + "CD_UNIDMEDIDA = ?,"
                + "QUANTIDADE = ?,"
                + "VALOR_UNIT_BRUTO = ?,"
                + "VALOR_UNIT_LIQUIDO = ?,"
                + "PERC_DESC = ?,"
                + "VALOR_DESC = ?,"
                + "TOTAL_ITEM_BRUTO = ?,"
                + "TOTAL_ITEM_LIQUIDO = ?,"
                + "OBS_ITEM = ?,"
                + "USUARIO_MODIFICACAO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "HORA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + " WHERE CD_PROPOSTA = ? AND CD_REVISAO = ? AND CD_LOCAL = ? AND SEQUENCIA = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, itp.getCdMaterial());
            pstmt.setString(2, itp.getCdUnidmedida());
            pstmt.setDouble(3, itp.getQuantidade());
            pstmt.setDouble(4, itp.getValorUnitBruto());
            pstmt.setDouble(5, itp.getValorUnitLiquido());
            pstmt.setDouble(6, itp.getPercDesconto());
            pstmt.setDouble(7, itp.getValorDescontos());
            pstmt.setDouble(8, itp.getValorTotalItemBruto());
            pstmt.setDouble(9, itp.getValorTotalItemLiquido());
            pstmt.setString(10, itp.getObsItem());
            pstmt.setString(11, itp.getUsuarioModificacao());
            pstmt.setString(12, itp.getDataModificacao());
            pstmt.setString(13, itp.getHoraModificacao());
            pstmt.setString(14, itp.getSituacao());
            pstmt.setString(15, itp.getCdProposta());
            pstmt.setString(16, itp.getCdRevisao());
            pstmt.setInt(17, itp.getCdLocal());
            pstmt.setInt(18, itp.getSequencia());
            pstmt.execute();
            pstmt.close();
            //JOptionPane.showMessageDialog(null, "Item Atualizado!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização do Item!\nErr: " + ex);
        }
    }

    // método para selecionar os registros no banco
    public void selecionar(List<ItemProposta> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new ItemProposta(
                                rs.getString("cd_proposta"),
                                rs.getString("cd_revisao"),
                                rs.getInt("cd_local"),
                                rs.getInt("sequencia"),
                                rs.getInt("sequencia_atend"),
                                rs.getString("cd_material"),
                                rs.getString("cd_unidmedida"),
                                rs.getDouble("quantidade"),
                                rs.getDouble("valor_unit_bruto"),
                                rs.getDouble("valor_unit_liquido"),
                                rs.getDouble("perc_desc"),
                                rs.getDouble("valor_desc"),
                                rs.getDouble("total_item_bruto"),
                                rs.getDouble("total_item_liquido"),
                                rs.getString("tipo_item"),
                                rs.getString("obs_item"),
                                rs.getString("usuario_cadastro"),
                                rs.getString("data_cadastro"),
                                rs.getString("hora_cadastro"),
                                rs.getString("usuario_modificacao"),
                                rs.getString("data_modificacao"),
                                rs.getString("hora_modificacao"),
                                rs.getString("situacao")));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca do Item!\nPrograma: ItemPropostaDAO.java\nErr: " + ex);
                    }
                }
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: ItemPropostaDAO.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nPrograma: ItemPropostaDAO.java\nErr: " + ex);
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
    public void excluir(ItemProposta itp) {
        String sql = "DELETE FROM GCVITEMPROPOSTA WHERE CD_PROPOSTA = ? AND CD_REVISAO = ? AND CD_LOCAL = ? AND SEQUENCIA = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, itp.getCdProposta());
            pstmt.setString(2, itp.getCdRevisao());
            pstmt.setInt(3, itp.getCdLocal());
            pstmt.setInt(4, itp.getSequencia());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Item excluído com sucesso!");
        } catch (Exception ex) {
            if (ex.getMessage().contains("CONSTRAINT")) {
                JOptionPane.showMessageDialog(null, "Este Item já está sendo usado, exclusão não permitida!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na exclusão do Item!\nErr: " + ex);
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
