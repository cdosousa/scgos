/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcs.dao;

import br.com.fabrica.ConexaoDataBase;
import br.com.gcs.modelo.Fornecedores;
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
 * @version 0.01beta_0917 created on 30/07/2018
 */
public class FornecedoresDAO extends AbstractTableModel {

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
    public FornecedoresDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }

    // método para poder criar o registro no banco
    public void adicionar(Fornecedores fornec) {
        String sql = "INSERT INTO GCSFORNECEDORES(CPF_CNPJ, INSC_ESTADUAL,TIPO_PESSOA, NOME_RAZAOSOCIAL,"
                + "APELIDO, OPTANTE_SIMPLES, TIPO_LOGRADOURO, LOGRADOURO,NUMERO,COMPLEMENTO,BAIRRO,CD_MUNICIPIO_IBGE,"
                + "SIGLA_UF,CEP,NUM_BANCO,NOME_BANCO,AGENCIA_BANCO,NUM_CONTA_BANCO,CD_PORTADOR,"
                + "CD_TIPOPAGAMENTO,CD_CONDPAG,CONTATO,TELEFONE,CELULAR,EMAIL,USUARIO_CADASTRO,"
                + "DATA_CADASTRO,DATA_MODIFICACAO,SITUACAO) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String cpfCnpj;
        String telefone;
        String celular;
        String cep;
        cpfCnpj = fornec.getCdCpfCnpj();
        cpfCnpj = cpfCnpj.replace(".", "");
        cpfCnpj = cpfCnpj.replace("-", "");
        cpfCnpj = cpfCnpj.replace("/", "");
        telefone = fornec.getTelefone();
        telefone = telefone.replace("(", "");
        telefone = telefone.replace(")", "");
        telefone = telefone.replace("-", "");
        telefone = telefone.replace(" ", "");
        celular = fornec.getCelular();
        celular = celular.replace("(", "");
        celular = celular.replace(")", "");
        celular = celular.replace("-", "");
        celular = celular.replace(" ", "");
        cep = fornec.getCdCep();
        cep = cep.replace("-", "");
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cpfCnpj);
            pstmt.setString(2, fornec.getCdInscEstadual());
            pstmt.setString(3, fornec.getTipoPessoa());
            pstmt.setString(4, fornec.getNomeRazaoSocial());
            pstmt.setString(5, fornec.getApelido());
            pstmt.setString(6, fornec.getOptanteSimples());
            pstmt.setString(7, fornec.getTipoLogradouro());
            pstmt.setString(8, fornec.getLogradouro());
            pstmt.setString(9, fornec.getNumero());
            pstmt.setString(10, fornec.getComplemento());
            pstmt.setString(11, fornec.getBairro());
            pstmt.setString(12, fornec.getCdMunicipioIbge());
            pstmt.setString(13, fornec.getSiglaUf());
            pstmt.setString(14, cep);
            pstmt.setString(15, fornec.getNumBanco());
            pstmt.setString(16, fornec.getNomeBanco());
            pstmt.setString(17, fornec.getAgenciaBanco());
            pstmt.setString(18, fornec.getNumContaBanco());
            pstmt.setString(19, fornec.getCdPortador());
            pstmt.setString(20, fornec.getCdTipoPagamento());
            pstmt.setString(21, fornec.getCdCondPagamento());
            pstmt.setString(22, fornec.getNomeContato());
            pstmt.setString(23, telefone);
            pstmt.setString(24, celular);
            pstmt.setString(25, fornec.getEmail());
            pstmt.setString(26, fornec.getUsuarioCadastro());
            pstmt.setString(27, fornec.getDataCadastro());
            pstmt.setString(28, fornec.getDataModificacao());
            pstmt.setString(29, String.valueOf(fornec.getSituacao()));
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Numero CPF/CNPJ já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Fornecedor, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }

    // método para poder atualizar o registro
    public void atualizar(Fornecedores fornec) {
        String sql = "UPDATE GCSFORNECEDORES SET INSC_ESTADUAL = ?,"
                + "TIPO_PESSOA = ?,"
                + "NOME_RAZAOSOCIAL = ?,"
                + "APELIDO = ?,"
                + "OPTANTE_SIMPLES = ?,"
                + "TIPO_LOGRADOURO = ?,"
                + "LOGRADOURO = ?,"
                + "NUMERO = ?,"
                + "COMPLEMENTO = ?,"
                + "BAIRRO = ?,"
                + "CD_MUNICIPIO_IBGE = ?,"
                + "SIGLA_UF = ?,"
                + "CEP = ?,"
                + "NUM_BANCO = ?,"
                + "NOME_BANCO = ?,"
                + "AGENCIA_BANCO = ?,"
                + "NUM_CONTA_BANCO = ?,"
                + "CD_PORTADOR = ?,"
                + "CD_TIPOPAGAMENTO = ?,"
                + "CD_CONDPAG = ?,"
                + "CONTATO = ?,"
                + "TELEFONE = ?,"
                + "CELULAR = ?,"
                + "EMAIL = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + "WHERE CPF_CNPJ = ?";
        String cpfCnpj;
        String telefone;
        String celular;
        cpfCnpj = fornec.getCdCpfCnpj();
        cpfCnpj = cpfCnpj.replace(".", "");
        cpfCnpj = cpfCnpj.replace("-", "");
        cpfCnpj = cpfCnpj.replace("/", "");
        telefone = fornec.getTelefone();
        telefone = telefone.replace("(", "");
        telefone = telefone.replace(")", "");
        telefone = telefone.replace("-", "");
        celular = fornec.getCelular();
        celular = celular.replace("(", "");
        celular = celular.replace(")", "");
        celular = celular.replace("-", "");
        String cep;
        cep = fornec.getCdCep();
        cep = cep.replace("-", "");
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, fornec.getCdInscEstadual());
            pstmt.setString(2, fornec.getTipoPessoa());
            pstmt.setString(3, fornec.getNomeRazaoSocial());
            pstmt.setString(4, fornec.getApelido());
            pstmt.setString(5, fornec.getOptanteSimples());
            pstmt.setString(6, fornec.getTipoLogradouro());
            pstmt.setString(7, fornec.getLogradouro());
            pstmt.setString(8, fornec.getNumero());
            pstmt.setString(9, fornec.getComplemento());
            pstmt.setString(10, fornec.getBairro());
            pstmt.setString(11, fornec.getCdMunicipioIbge());
            pstmt.setString(12, fornec.getSiglaUf());
            pstmt.setString(13, cep);
            pstmt.setString(14, fornec.getNumBanco());
            pstmt.setString(15, fornec.getNomeBanco());
            pstmt.setString(16, fornec.getAgenciaBanco());
            pstmt.setString(17, fornec.getNumContaBanco());
            pstmt.setString(18, fornec.getCdPortador());
            pstmt.setString(19, fornec.getCdTipoPagamento());
            pstmt.setString(20, fornec.getCdCondPagamento());
            pstmt.setString(21, fornec.getNomeContato());
            pstmt.setString(22, telefone);
            pstmt.setString(23, celular);
            pstmt.setString(24, fornec.getEmail());
            pstmt.setString(25, fornec.getDataModificacao());
            pstmt.setString(26, String.valueOf(fornec.getSituacao()));
            pstmt.setString(27, cpfCnpj);
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização do registro!\nErr: " + ex);
        }
    }

    // método para selecionar os registros no banco
    public void selecionar(List<Fornecedores> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new Fornecedores(
                                rs.getString("cpf_cnpj"),
                                rs.getString("insc_estadual"),
                                rs.getString("tipo_pessoa"),
                                rs.getString("nome_razaosocial"),
                                rs.getString("apelido"),
                                rs.getString("optante_simples"),
                                rs.getString("tipo_logradouro"),
                                rs.getString("logradouro"),
                                rs.getString("numero"),
                                rs.getString("complemento"),
                                rs.getString("bairro"),
                                rs.getString("cd_municipio_ibge"),
                                rs.getString("sigla_uf"),
                                rs.getString("cep"),
                                rs.getString("num_banco"),
                                rs.getString("nome_banco"),
                                rs.getString("agencia_banco"),
                                rs.getString("num_conta_banco"),
                                rs.getString("cd_portador"),
                                rs.getString("cd_tipopagamento"),
                                rs.getString("cd_condpag"),
                                rs.getString("contato"),
                                rs.getString("telefone"),
                                rs.getString("celular"),
                                rs.getString("email"),
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
    public void excluir(Fornecedores fornec) {
        String sql = "DELETE FROM GCSFORNECEDORES WHERE CPF_CNPJ = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, fornec.getCdCpfCnpj());
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