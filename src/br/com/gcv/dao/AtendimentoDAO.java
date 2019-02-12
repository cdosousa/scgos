/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.dao;

import br.com.gcv.modelo.Atendimento;
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
public class AtendimentoDAO extends AbstractTableModel {

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
    public AtendimentoDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        //stmt = conexao.createStatement();
        statusConexao = true;
    }

    // método para poder criar o registro no banco
    public void adicionar(Atendimento ate) {
        String sql = "INSERT INTO GCVATENDIMENTO(CD_ATENDIMENTO,"
                + "DATA_ATENDIMENTO,"
                + "HORA_ATENDIMENTO,"
                + "NOME_CLIENTE,"
                + "TIPO_PESSOA,"
                + "TELEFONE,"
                + "CELULAR,"
                + "EMAIL,"
                + "TIPO_LOGRADOURO,"
                + "LOGRADOURO,"
                + "NUMERO,"
                + "COMPLEMENTO,"
                + "BAIRRO,"
                + "CD_MUNICIPIO_IBGE,"
                + "SIGLA_UF,"
                + "CEP,"
                + "TIPO_ENDERECO,"
                + "VALOR_SERVICO,"
                + "VALOR_PRODUTOS,"
                + "VALOR_ADICIONAIS,"
                + "VALOR_TOTAL,"
                + "USUARIO_CADASTRO,"
                + "DATA_CADASTRO,"
                + "HORA_CADASTRO,"
                + "USUARIO_MODIFICACAO,"
                + "DATA_MODIFICACAO,"
                + "HORA_MODIFICACAO,"
                + "SITUACAO) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String telefone = ate.getTelefone();
        telefone = telefone.replace("(", "");
        telefone = telefone.replace(")", "");
        telefone = telefone.replace("-", "");
        telefone = telefone.replace(" ", "");
        String celular = ate.getCelular();
        celular = celular.replace("(", "");
        celular = celular.replace(")", "");
        celular = celular.replace("-", "");
        celular = celular.replace(" ", "");
        String cep = ate.getCdCep();
        cep = cep.replace("-", "");
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, ate.getCdAtendimento());
            pstmt.setString(2, ate.getDataAtendimento());
            pstmt.setString(3, ate.getHoraAtendimento());
            pstmt.setString(4, ate.getNomeRazaoSocial());
            pstmt.setString(5, ate.getTipoPessoa());
            pstmt.setString(6, telefone);
            pstmt.setString(7, celular);
            pstmt.setString(8, ate.getEmail());
            pstmt.setString(9, ate.getTipoLogradouro());
            pstmt.setString(10, ate.getLogradouro());
            pstmt.setString(11, ate.getNumero());
            pstmt.setString(12, ate.getComplemento());
            pstmt.setString(13, ate.getBairro());
            pstmt.setString(14, ate.getCdMunicipioIbge());
            pstmt.setString(15, ate.getSiglaUf());
            pstmt.setString(16, cep);
            pstmt.setString(17, ate.getTipoEndereco());
            pstmt.setDouble(18, ate.getValorServico());
            pstmt.setDouble(19, ate.getValorProdutos());
            pstmt.setDouble(20, ate.getValorAdicionais());
            pstmt.setDouble(21, ate.getValorTotalBruto());
            pstmt.setString(22, ate.getUsuarioCadastro());
            pstmt.setString(23, ate.getDataCadastro());
            pstmt.setString(24, ate.getHoraCadastro());
            pstmt.setString(25, ate.getUsuarioModificacao());
            pstmt.setString(26, ate.getDataModificacao());
            pstmt.setString(27, ate.getHoraModificacao());
            pstmt.setString(28, ate.getSituacao());
            pstmt.execute();
            pstmt.close();
            //JOptionPane.showMessageDialog(null, "Atendimento gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Numero de Atendimento já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Atendimento, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }

    // método para poder atualizar o registro
    public void atualizar(Atendimento ate) {
        String sql = "UPDATE GCVATENDIMENTO SET NOME_CLIENTE = ?,"
                + "TIPO_PESSOA = ?,"
                + "TELEFONE = ?,"
                + "CELULAR = ?,"
                + "EMAIL = ?,"
                + "TIPO_LOGRADOURO = ?,"
                + "LOGRADOURO = ?,"
                + "NUMERO = ?,"
                + "COMPLEMENTO = ?,"
                + "BAIRRO = ?,"
                + "CD_MUNICIPIO_IBGE = ?,"
                + "SIGLA_UF = ?,"
                + "CEP = ?,"
                + "TIPO_ENDERECO = ?,"
                + "VALOR_SERVICO = ?,"
                + "VALOR_PRODUTOS = ?,"
                + "VALOR_ADICIONAIS = ?,"
                + "VALOR_TOTAL = ?,"
                + "CD_PROPOSTA = ?,"
                + "USUARIO_MODIFICACAO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "HORA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + " WHERE CD_ATENDIMENTO = ?";
        String telefone = ate.getTelefone();
        telefone = telefone.replace("(", "");
        telefone = telefone.replace(")", "");
        telefone = telefone.replace("-", "");
        telefone = telefone.replace(" ", "");
        String celular = ate.getCelular();
        celular = celular.replace("(", "");
        celular = celular.replace(")", "");
        celular = celular.replace("-", "");
        celular = celular.replace(" ", "");
        String cep = ate.getCdCep();
        cep = cep.replace("-", "");
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, ate.getNomeRazaoSocial());
            pstmt.setString(2, ate.getTipoPessoa());
            pstmt.setString(3, telefone);
            pstmt.setString(4, celular);
            pstmt.setString(5, ate.getEmail());
            pstmt.setString(6, ate.getTipoLogradouro());
            pstmt.setString(7, ate.getLogradouro());
            pstmt.setString(8, ate.getNumero());
            pstmt.setString(9, ate.getComplemento());
            pstmt.setString(10, ate.getBairro());
            pstmt.setString(11, ate.getCdMunicipioIbge());
            pstmt.setString(12, ate.getSiglaUf());
            pstmt.setString(13, cep);
            pstmt.setString(14, ate.getTipoEndereco());
            pstmt.setDouble(15, ate.getValorServico());
            pstmt.setDouble(16, ate.getValorProdutos());
            pstmt.setDouble(17, ate.getValorAdicionais());
            pstmt.setDouble(18, ate.getValorTotalBruto());
            pstmt.setString(19, ate.getCdProposta());
            pstmt.setString(20, ate.getUsuarioModificacao());
            pstmt.setString(21, ate.getDataModificacao());
            pstmt.setString(22, ate.getHoraModificacao());
            pstmt.setString(23, ate.getSituacao());
            pstmt.setString(24, ate.getCdAtendimento());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Atendimento "+ate.getCdAtendimento()+" atualizado com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização do Atendimento!\nErr: " + ex);
        }catch (Exception e){
            JOptionPane.showMessageDialog(null, "Erro geral de atualização do Atendimento!\nErr: " + e);
        }
    }

    // método para selecionar os registros no banco
    public void selecionar(List<Atendimento> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new Atendimento(
                                rs.getString("cd_atendimento"),
                                rs.getString("data_atendimento"),
                                rs.getString("hora_atendimento"),
                                rs.getString("nome_cliente"),
                                rs.getString("tipo_pessoa"),
                                rs.getString("telefone"),
                                rs.getString("celular"),
                                rs.getString("email"),
                                rs.getString("tipo_logradouro"),
                                rs.getString("logradouro"),
                                rs.getString("numero"),
                                rs.getString("complemento"),
                                rs.getString("Bairro"),
                                rs.getString("cd_municipio_ibge"),
                                rs.getString("sigla_uf"),
                                rs.getString("cep"),
                                rs.getString("tipo_endereco"),
                                rs.getDouble("valor_servico"),
                                rs.getDouble("valor_produtos"),
                                rs.getDouble("valor_adicionais"),
                                rs.getDouble("valor_total"),
                                rs.getString("cd_proposta"),
                                rs.getString("usuario_cadastro"),
                                rs.getString("data_cadastro"),
                                rs.getString("hora_cadastro"),
                                rs.getString("usuario_modificacao"),
                                rs.getString("data_modificacao"),
                                rs.getString("hora_modificacao"),
                                rs.getString("situacao")));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca do Atendimento!\nErr: " + ex);
                    }
                }
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: AtendimentoDAO.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nPrograma: AtendimentoDAO.java\nErr: " + ex);
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
    public void excluir(Atendimento ate) {
        String sql = "DELETE FROM GCVATENDIMENTO WHERE CD_ATENDIMENTO = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, ate.getCdAtendimento());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Atendiemnto excluído com sucesso!");
        } catch (Exception ex) {
            if (ex.getMessage().contains("CONSTRAINT")) {
                JOptionPane.showMessageDialog(null, "Este Atendimento já está sendo usado, exclusão não permitida!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na exclusão do Atendimento!\nErr: " + ex);
            }
        }
    }
    
    // método para buscar totais no banco
    public void totalizarItens(List<Atendimento> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new Atendimento(
                                rs.getString("Atendimento"),
                                rs.getDouble("Total Produtos"),
                                rs.getDouble("Total Serviços"),
                                rs.getDouble("Adicionais"),
                                rs.getDouble("Total Atendimento")
                                ));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca do Item!\nPrograma: AtendimentoDAO.java\nErr: " + ex);
                    }
                }
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: AtendimentoDAO.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nPrograma: AtendimentoDAO.java\nErr: " + ex);
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                desconectar();
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
                //conexao.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            } finally {
                statusConexao = false;
            }
        }
    }
}
