/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.dao;

import br.com.gcv.modelo.EnderecoCobrancaCliente;
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
 * @version 0.01beta_0917 created on 17/11/2017
 */
public class EnderecoCobrancaClientesDAO extends AbstractTableModel {

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
    public EnderecoCobrancaClientesDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }

    // método para poder criar o registro no banco
    public void adicionar(EnderecoCobrancaCliente ecc) {
        String sql = "INSERT INTO GCVCLIENTES(CPF_CNPJ, SEQUENCIA,TIPO_LOGRADOURO, LOGRADOURO,NUMERO,COMPLEMENTO,"
                + "BAIRRO,CD_MUNICIPIO_IBGE,SIGLA_UF,CEP,USUARIO_CADASTRO,DATA_CADASTRO,DATA_MODIFICACAO,SITUACAO) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String cpfCnpj;
        String cep;
        cpfCnpj = ecc.getCdCpfCnpj();
        cpfCnpj = cpfCnpj.replace(".", "");
        cpfCnpj = cpfCnpj.replace("-", "");
        cpfCnpj = cpfCnpj.replace("/", "");
        cep = ecc.getCdCep();
        cep = cep.replace("-", "");
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cpfCnpj);
            pstmt.setInt(2, ecc.getSequencia());
            pstmt.setString(3, ecc.getTipoLogradouro());
            pstmt.setString(4, ecc.getLogradouro());
            pstmt.setString(5, ecc.getNumero());
            pstmt.setString(6, ecc.getComplemento());
            pstmt.setString(7, ecc.getBairro());
            pstmt.setString(8, ecc.getCdMunicipioIbge());
            pstmt.setString(9, ecc.getSiglaUf());
            pstmt.setString(10, cep);
            pstmt.setString(11, ecc.getUsuarioCadastro());
            pstmt.setString(12, ecc.getDataCadastro());
            pstmt.setString(13, ecc.getDataModificacao());
            pstmt.setString(14, String.valueOf(ecc.getSituacao()));
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Endereço de Cobranca já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Endereço de Cobrança, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }

    // método para poder atualizar o registro
    public void atualizar(EnderecoCobrancaCliente ecc) {
        String sql = "UPDATE GCVCLIENTES SET TIPO_LOGRADOURO = ?,"
                + "LOGRADOURO = ?,"
                + "NUMERO = ?,"
                + "COMPLEMENTO = ?,"
                + "BAIRRO = ?,"
                + "CD_MUNICIPIO_IBGE = ?,"
                + "SIGLA_UF = ?,"
                + "CEP = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + "WHERE CPF_CNPJ = ? AND "
                + "SEQUENCIA = ?";
        String cpfCnpj;
        cpfCnpj = ecc.getCdCpfCnpj();
        cpfCnpj = cpfCnpj.replace(".", "");
        cpfCnpj = cpfCnpj.replace("-", "");
        cpfCnpj = cpfCnpj.replace("/", "");
        String cep;
        cep = ecc.getCdCep();
        cep = cep.replace("-", "");
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, ecc.getTipoLogradouro());
            pstmt.setString(2, ecc.getLogradouro());
            pstmt.setString(3, ecc.getNumero());
            pstmt.setString(4, ecc.getComplemento());
            pstmt.setString(5, ecc.getBairro());
            pstmt.setString(6, ecc.getCdMunicipioIbge());
            pstmt.setString(7, ecc.getSiglaUf());
            pstmt.setString(8, cep);
            pstmt.setString(9, ecc.getDataModificacao());
            pstmt.setString(10, String.valueOf(ecc.getSituacao()));
            pstmt.setString(11, cpfCnpj);
            pstmt.setInt(12, ecc.getSequencia());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização do registro!\nErr: " + ex);
        }
    }

    // método para selecionar os registros no banco
    public void selecionar(List<EnderecoCobrancaCliente> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new EnderecoCobrancaCliente(
                                rs.getString("cpf_cnpj"),
                                rs.getInt("sequencia"),
                                rs.getString("tipo_logradouro"),
                                rs.getString("logradouro"),
                                rs.getString("numero"),
                                rs.getString("complemento"),
                                rs.getString("bairro"),
                                rs.getString("cd_municipio_ibge"),
                                rs.getString("sigla_uf"),
                                rs.getString("cep"),
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
    public void excluir(EnderecoCobrancaCliente ecc) {
        String sql = "DELETE FROM GCVENDCOBRANCA WHERE CPF_CNPJ = ? AND SEQUENCIA = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, ecc.getCdCpfCnpj());
            pstmt.setInt(2, ecc.getSequencia());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro excluído com sucesso!");
        } catch (Exception ex) {
            if (ex.getMessage().contains("CONSTRAINT")) {
                JOptionPane.showMessageDialog(null, "Este registro já está sendo usado, exclusão não permitida!");
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
