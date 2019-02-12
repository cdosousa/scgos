/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.dao;

import br.com.fabrica.ConexaoDataBase;
import br.com.gcv.modelo.LocalAtendimento;
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
 * @version 0.01beta_0917 created on 23/11/2017
 */
public class LocalAtendimentoDAO extends AbstractTableModel {

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    private int numLinhas;
    private int ultSequencia;
    private int pass = 0;
    private boolean statusConexao;

    // método construtor da classe para inicializar a conexão
    public LocalAtendimentoDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }

    // método para poder criar o registro no banco
    public void adicionar(LocalAtendimento lat) {
        String sql = "INSERT INTO GCVLOCALATENDIMENTO(CD_ATENDIMENTO,"
                + "CD_LOCAL,"
                + "NOME_LOCAL,"
                + "METRAGE_AREA,"
                + "PERC_PERDA,"
                + "TIPO_PISO,"
                + "TIPO_RODAPE,"
                + "METRAGEM_RODAPE,"
                + "LARGURA,"
                + "COMPRIMENTO,"
                + "ESPESSURA,"
                + "TINGIMENTO,"
                + "CLAREAMENTO,"
                + "CD_MATERIAL_VERNIZ,"
                + "CD_ESSENCIA,"
                + "VALOR_SERVICOS,"
                + "VALOR_PRODUTOS,"
                + "VALOR_ADICIONAIS,"
                + "VALOR_TOTAL,"
                + "OBS_LOCAL,"
                + "USUARIO_CADASTRO,"
                + "DATA_CADASTRO,"
                + "HORA_CADASTRO,"
                + "USUARIO_MODIFICACAO,"
                + "DATA_MODIFICACAO,"
                + "HORA_MODIFICACAO,"
                + "SITUACAO) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, lat.getCdAtendimento());
            pstmt.setInt(2, lat.getCdLocal());
            pstmt.setString(3, lat.getNomeLocal());
            pstmt.setDouble(4, lat.getMetragemArea());
            pstmt.setDouble(5, lat.getPercPerda());
            pstmt.setString(6, lat.getTipoPiso());
            pstmt.setString(7, lat.getTipoRodape());
            pstmt.setDouble(8, lat.getMetragemRodape());
            pstmt.setDouble(9, lat.getLargura());
            pstmt.setString(10, lat.getComprimento());
            pstmt.setDouble(11, lat.getEspessura());
            pstmt.setString(12, lat.getTingimento());
            pstmt.setString(13, lat.getClareamento());
            pstmt.setString(14, lat.getCdTipolVerniz());
            pstmt.setString(15, lat.getCdEssencia());
            pstmt.setDouble(16, lat.getValorServico());
            pstmt.setDouble(17, lat.getValorProdutos());
            pstmt.setDouble(18, lat.getValorAdicionais());
            pstmt.setDouble(19, lat.getValorTotalBruto());
            pstmt.setString(20, lat.getObs());
            pstmt.setString(21, lat.getUsuarioCadastro());
            pstmt.setString(22, lat.getDataCadastro());
            pstmt.setString(23, lat.getHoraCadastro());
            pstmt.setString(24, lat.getUsuarioModificacao());
            pstmt.setString(25, lat.getDataModificacao());
            pstmt.setString(26, lat.getHoraModificacao());
            pstmt.setString(27, lat.getSituacao());
            pstmt.execute();
            pstmt.close();
            //JOptionPane.showMessageDialog(null, "Ambiente gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Ambiente já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Ambiente, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }

    // método para poder atualizar o registro
    public void atualizar(LocalAtendimento lat) {
        String sql = "UPDATE GCVLOCALATENDIMENTO SET NOME_LOCAL = ?,"
                + "METRAGE_AREA = ?,"
                + "PERC_PERDA = ?,"
                + "TIPO_PISO = ?,"
                + "TIPO_RODAPE = ?,"
                + "METRAGEM_RODAPE = ?,"
                + "LARGURA = ?,"
                + "COMPRIMENTO = ?,"
                + "ESPESSURA = ?,"
                + "TINGIMENTO = ?,"
                + "CLAREAMENTO = ?,"
                + "CD_MATERIAL_VERNIZ = ?,"
                + "CD_ESSENCIA = ?,"
                + "VALOR_SERVICOS = ?,"
                + "VALOR_PRODUTOS = ?,"
                + "VALOR_ADICIONAIS = ?,"
                + "VALOR_TOTAL = ?,"
                + "OBS_LOCAL = ?,"
                + "USUARIO_MODIFICACAO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "HORA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + " WHERE CD_ATENDIMENTO = ? AND CD_LOCAL = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, lat.getNomeLocal());
            pstmt.setDouble(2, lat.getMetragemArea());
            pstmt.setDouble(3, lat.getPercPerda());
            pstmt.setString(4, lat.getTipoPiso());
            pstmt.setString(5, lat.getTipoRodape());
            pstmt.setDouble(6, lat.getMetragemRodape());
            pstmt.setDouble(7, lat.getLargura());
            pstmt.setString(8, lat.getComprimento());
            pstmt.setDouble(9, lat.getEspessura());
            pstmt.setString(10, lat.getTingimento());
            pstmt.setString(11, lat.getClareamento());
            pstmt.setString(12, lat.getCdTipolVerniz());
            pstmt.setString(13, lat.getCdEssencia());
            pstmt.setDouble(14, lat.getValorServico());
            pstmt.setDouble(15, lat.getValorProdutos());
            pstmt.setDouble(16, lat.getValorAdicionais());
            pstmt.setDouble(17, lat.getValorTotalBruto());
            pstmt.setString(18, lat.getObs());
            pstmt.setString(19, lat.getUsuarioModificacao());
            pstmt.setString(20, lat.getDataModificacao());
            pstmt.setString(21, lat.getHoraModificacao());
            pstmt.setString(22, lat.getSituacao());
            pstmt.setString(23, lat.getCdAtendimento());
            pstmt.setInt(24, lat.getCdLocal());
            pstmt.execute();
            pstmt.close();
            //JOptionPane.showMessageDialog(null, "Ambiente atualizado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização do Ambiente!\nErr: " + ex);
        }
    }

    // método para selecionar os registros no banco
    public void selecionar(List<LocalAtendimento> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new LocalAtendimento(
                                rs.getString("cd_atendimento"),
                                rs.getInt("cd_local"),
                                rs.getString("nome_local"),
                                rs.getDouble("metrage_area"),
                                rs.getDouble("perc_perda"),
                                rs.getString("tipo_piso"),
                                rs.getString("tipo_rodape"),
                                rs.getDouble("metragem_rodape"),
                                rs.getDouble("largura"),
                                rs.getString("comprimento"),
                                rs.getDouble("espessura"),
                                rs.getString("tingimento"),
                                rs.getString("clareamento"),
                                rs.getString("cd_material_verniz"),
                                rs.getString("cd_essencia"),
                                rs.getDouble("valor_servicos"),
                                rs.getDouble("valor_produtos"),
                                rs.getDouble("valor_adicionais"),
                                rs.getDouble("valor_total"),
                                rs.getString("obs_local"),
                                rs.getString("usuario_cadastro"),
                                rs.getString("data_cadastro"),
                                rs.getString("hora_cadastro"),
                                rs.getString("usuario_modificacao"),
                                rs.getString("data_modificacao"),
                                rs.getString("hora_modificacao"),
                                rs.getString("situacao")));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca do Registro!\nPrograma: LocalAtendimentoDAO.java\nErr: " + ex);
                    }
                    ultSequencia = rs.getInt("cd_local");
                }
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: LocalAtendimentoDAO.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nPrograma: LocalAtendimentoDAO.java\nErr: " + ex);
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
    public void excluir(LocalAtendimento lat) {
        String sql = "DELETE FROM GCVLOCALATENDIMENTO WHERE CD_ATENDIMENTO = ? AND CD_LOCAL = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, lat.getCdAtendimento());
            pstmt.setInt(2, lat.getCdLocal());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Ambiente excluído com sucesso!");
        } catch (Exception ex) {
            if (ex.getMessage().contains("CONSTRAINT")) {
                JOptionPane.showMessageDialog(null, "Este Ambiente já está sendo usado, exclusão não permitida!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na exclusão do Ambiente!\nErr: " + ex);
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

    /**
     * @return the ultSequencia
     */
    public int getUltSequencia() {
        return ultSequencia;
    }
}
