/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.dao;

import br.com.fabrica.ConexaoDataBase;
import br.com.gsm.modelo.Tecnicos;
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
 * @version 0.01beta_0917 created on 23/10/2017
 */
public class TecnicosDAO extends AbstractTableModel {

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
    public TecnicosDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }

    // método para poder criar o registro no banco
    public void adicionar(Tecnicos tec) {
        String sql = "INSERT INTO GSMTECNICOS(CPF, NOME_TECNICO, RG, DATA_EMISSAO_RG, ORGAO_EXPEDIDOR_RG, TIPO_LOGRADOURO, LOGRADOURO, "
                + "NUMERO, COMPLEMENTO, BAIRRO, CD_MUNICIPIO_IBGE, CD_SIGLA_UF,CEP, TELEFONE, CELULAR, EMAIL, POSSUI_HABILITACAO,"
                + "CATEGORIA_CNH, NUM_CNH, USUARIO_CADASTRO, DATA_CADASTRO, DATA_MODIFICACAO, SITUACAO) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String cpf;
        String telefone;
        String celular;
        String cep;
        cpf = tec.getCpf();
        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-", "");
        telefone = tec.getTelefone();
        telefone = telefone.replace("(", "");
        telefone = telefone.replace(")", "");
        telefone = telefone.replace("-", "");
        telefone = telefone.replace(" ", "");
        celular = tec.getCelular();
        celular = celular.replace("(", "");
        celular = celular.replace(")", "");
        celular = celular.replace("-", "");
        celular = celular.replace(" ", "");
        cep = tec.getCep();
        cep = cep.replace("-", "");
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cpf);
            pstmt.setString(2, tec.getNomeTecnico());
            pstmt.setString(3, tec.getRg());
            pstmt.setString(4, tec.getDataEmissaoRg());
            pstmt.setString(5, tec.getOrgaoExpedidorRg());
            pstmt.setString(6, tec.getTipoLogradouro());
            pstmt.setString(7, tec.getLogradouro());
            pstmt.setString(8, tec.getNumero());
            pstmt.setString(9, tec.getComplemento());
            pstmt.setString(10, tec.getBairro());
            pstmt.setString(11, tec.getCdMunicipioIbge());
            pstmt.setString(12, tec.getCdSiglaUf());
            pstmt.setString(13, cep);
            pstmt.setString(14, telefone);
            pstmt.setString(15, celular);
            pstmt.setString(16, tec.getEmail());
            pstmt.setString(17, tec.getPossuiHabilitacao());
            pstmt.setString(18, tec.getCategoriaCnh());
            pstmt.setString(19, tec.getNumCnh());
            pstmt.setString(20, tec.getUsuarioCadastro());
            pstmt.setString(21, tec.getDataCadastro());
            pstmt.setString(22, tec.getDataModificacao());
            pstmt.setString(23, String.valueOf(tec.getSituacao()));
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Numero CPF já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Técnico, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }

    // método para poder atualizar o registro
    public void atualizar(Tecnicos tec) {
        String sql = "UPDATE GSMTECNICOS SET NOME_TECNICO = ?, "
                + "RG = ?, "
                + "DATA_EMISSAO_RG = ?, "
                + "ORGAO_EXPEDIDOR_RG = ?,"
                + "TIPO_LOGRADOURO = ?,"
                + "LOGRADOURO = ?,"
                + "NUMERO = ?,"
                + "COMPLEMENTO = ?,"
                + "BAIRRO = ?,"
                + "CD_MUNICIPIO_IBGE = ?,"
                + "CD_SIGLA_UF = ?,"
                + "CEP = ?,"
                + "TELEFONE = ?,"
                + "CELULAR = ?,"
                + "EMAIL = ?,"
                + "POSSUI_HABILITACAO = ?,"
                + "CATEGORIA_CNH = ?,"
                + "NUM_CNH = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + "WHERE CPF = ?";
        String cpf;
        String telefone;
        String celular;
        cpf = tec.getCpf();
        cpf = cpf.replace(".", "");
        cpf = cpf.replace("-", "");
        telefone = tec.getTelefone();
        telefone = telefone.replace("(", "");
        telefone = telefone.replace(")", "");
        telefone = telefone.replace("-", "");
        celular = tec.getCelular();
        celular = celular.replace("(", "");
        celular = celular.replace(")", "");
        celular = celular.replace("-", "");
        String cep;
        cep = tec.getCep();
        cep = cep.replace("-", "");
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, tec.getNomeTecnico());
            pstmt.setString(2, tec.getRg());
            pstmt.setString(3, tec.getDataEmissaoRg());
            pstmt.setString(4, tec.getOrgaoExpedidorRg());
            pstmt.setString(5, tec.getTipoLogradouro());
            pstmt.setString(6, tec.getLogradouro());
            pstmt.setString(7, tec.getNumero());
            pstmt.setString(8, tec.getComplemento());
            pstmt.setString(9, tec.getBairro());
            pstmt.setString(10, tec.getCdMunicipioIbge());
            pstmt.setString(11, tec.getCdSiglaUf());
            pstmt.setString(12, cep);
            pstmt.setString(13, telefone);
            pstmt.setString(14, celular);
            pstmt.setString(15, tec.getEmail());
            pstmt.setString(16, tec.getPossuiHabilitacao());
            pstmt.setString(17, tec.getCategoriaCnh());
            pstmt.setString(18, tec.getNumCnh());
            pstmt.setString(19, tec.getDataModificacao());
            pstmt.setString(20, String.valueOf(tec.getSituacao()));
            pstmt.setString(21, cpf);
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização do registro!\nErr: " + ex);
        }
    }

    // método para selecionar os registros no banco
    public void selecionar(List<Tecnicos> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new Tecnicos(
                                rs.getString("cpf"),
                                rs.getString("nome_tecnico"),
                                rs.getString("rg"),
                                rs.getString("data_emissao_rg"),
                                rs.getString("orgao_expedidor_rg"),
                                rs.getString("tipo_logradouro"),
                                rs.getString("logradouro"),
                                rs.getString("numero"),
                                rs.getString("complemento"),
                                rs.getString("bairro"),
                                rs.getString("cd_municipio_ibge"),
                                rs.getString("cd_sigla_uf"),
                                rs.getString("cep"),
                                rs.getString("telefone"),
                                rs.getString("celular"),
                                rs.getString("email"),
                                rs.getString("possui_habilitacao"),
                                rs.getString("categoria_cnh"),
                                rs.getString("num_cnh"),
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
    public void excluir(Tecnicos tec) {
        String sql = "DELETE FROM GSMTECNICOS WHERE CPF = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, tec.getCpf());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro excluído com sucesso!");
        } catch (Exception ex) {
            if(ex.getMessage().contains("CONSTRAINT")){
                JOptionPane.showMessageDialog(null, "Este registro já está sendo usado, exclusão não permitida!");
            }else{
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
