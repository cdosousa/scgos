/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.dao;

import br.com.fabrica.ConexaoDataBase;
import br.com.gcv.modelo.Clientes;
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
 * @version 0.01beta_0917 created on 14/11/2017
 */
public class ClientesDAO extends AbstractTableModel {

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
    public ClientesDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }

    // método para poder criar o registro no banco
    public void adicionar(Clientes cli) {
        String sql = "INSERT INTO GCVCLIENTES(CPF_CNPJ, INSC_ESTADUAL,TIPO_PESSOA, NOME_RAZAOSOCIAL,"
                + "APELIDO, OPTANTE_SIMPLES, OPTANTE_SIMBAHIA, OPTANTE_SUFRAMA, CD_SUFRAMA,"
                + "TIPO_LOGRADOURO, LOGRADOURO,NUMERO,COMPLEMENTO,BAIRRO,CD_MUNICIPIO_IBGE,"
                + "SIGLA_UF,CEP,NUM_BANCO,NOME_BANCO,AGENCIA_BANCO,NUM_CONTA_BANCO,CD_PORTADOR,"
                + "CD_TIPOPAGAMENTO,CD_CONDPAG,CONTATO,TELEFONE,CELULAR,EMAIL,USUARIO_CADASTRO,"
                + "DATA_CADASTRO,DATA_MODIFICACAO,SITUACAO) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String cpfCnpj;
        String telefone;
        String celular;
        String cep;
        cpfCnpj = cli.getCdCpfCnpj();
        cpfCnpj = cpfCnpj.replace(".", "");
        cpfCnpj = cpfCnpj.replace("-", "");
        cpfCnpj = cpfCnpj.replace("/", "");
        telefone = cli.getTelefone();
        telefone = telefone.replace("(", "");
        telefone = telefone.replace(")", "");
        telefone = telefone.replace("-", "");
        telefone = telefone.replace(" ", "");
        celular = cli.getCelular();
        celular = celular.replace("(", "");
        celular = celular.replace(")", "");
        celular = celular.replace("-", "");
        celular = celular.replace(" ", "");
        cep = cli.getCdCep();
        cep = cep.replace("-", "");
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cpfCnpj);
            pstmt.setString(2, cli.getCdInscEstadual());
            pstmt.setString(3, cli.getTipoPessoa());
            pstmt.setString(4, cli.getNomeRazaoSocial());
            pstmt.setString(5, cli.getApelido());
            pstmt.setString(6, cli.getOptanteSimples());
            pstmt.setString(7, cli.getOptanteSimbahia());
            pstmt.setString(8, cli.getOptanteSuframa());
            pstmt.setString(9, cli.getCdSuframa());
            pstmt.setString(10, cli.getTipoLogradouro());
            pstmt.setString(11, cli.getLogradouro());
            pstmt.setString(12, cli.getNumero());
            pstmt.setString(13, cli.getComplemento());
            pstmt.setString(14, cli.getBairro());
            pstmt.setString(15, cli.getCdMunicipioIbge());
            pstmt.setString(16, cli.getSiglaUf());
            pstmt.setString(17, cep);
            pstmt.setString(18, cli.getNumBanco());
            pstmt.setString(19, cli.getNomeBanco());
            pstmt.setString(20, cli.getAgenciaBanco());
            pstmt.setString(21, cli.getNumContaBanco());
            pstmt.setString(22, cli.getCdPortador());
            pstmt.setString(23, cli.getCdTipoPagamento());
            pstmt.setString(24, cli.getCdCondPagamento());
            pstmt.setString(25, cli.getNomeContato());
            pstmt.setString(26, telefone);
            pstmt.setString(27, celular);
            pstmt.setString(28, cli.getEmail());
            pstmt.setString(29, cli.getUsuarioCadastro());
            pstmt.setString(30, cli.getDataCadastro());
            pstmt.setString(31, cli.getDataModificacao());
            pstmt.setString(32, String.valueOf(cli.getSituacao()));
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Numero CPF/CNPJ já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Cliente, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }

    // método para poder atualizar o registro
    public void atualizar(Clientes cli) {
        String sql = "UPDATE GCVCLIENTES SET INSC_ESTADUAL = ?,"
                + "TIPO_PESSOA = ?,"
                + "NOME_RAZAOSOCIAL = ?,"
                + "APELIDO = ?,"
                + "OPTANTE_SIMPLES = ?,"
                + "OPTANTE_SIMBAHIA = ?,"
                + "OPTANTE_SUFRAMA = ?,"
                + "CD_SUFRAMA = ?,"
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
        cpfCnpj = cli.getCdCpfCnpj();
        cpfCnpj = cpfCnpj.replace(".", "");
        cpfCnpj = cpfCnpj.replace("-", "");
        cpfCnpj = cpfCnpj.replace("/", "");
        telefone = cli.getTelefone();
        telefone = telefone.replace("(", "");
        telefone = telefone.replace(")", "");
        telefone = telefone.replace("-", "");
        celular = cli.getCelular();
        celular = celular.replace("(", "");
        celular = celular.replace(")", "");
        celular = celular.replace("-", "");
        String cep;
        cep = cli.getCdCep();
        cep = cep.replace("-", "");
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cli.getCdInscEstadual());
            pstmt.setString(2, cli.getTipoPessoa());
            pstmt.setString(3, cli.getNomeRazaoSocial());
            pstmt.setString(4, cli.getApelido());
            pstmt.setString(5, cli.getOptanteSimples());
            pstmt.setString(6, cli.getOptanteSimbahia());
            pstmt.setString(7, cli.getOptanteSuframa());
            pstmt.setString(8, cli.getCdSuframa());
            pstmt.setString(9, cli.getTipoLogradouro());
            pstmt.setString(10, cli.getLogradouro());
            pstmt.setString(11, cli.getNumero());
            pstmt.setString(12, cli.getComplemento());
            pstmt.setString(13, cli.getBairro());
            pstmt.setString(14, cli.getCdMunicipioIbge());
            pstmt.setString(15, cli.getSiglaUf());
            pstmt.setString(16, cep);
            pstmt.setString(17, cli.getNumBanco());
            pstmt.setString(18, cli.getNomeBanco());
            pstmt.setString(19, cli.getAgenciaBanco());
            pstmt.setString(20, cli.getNumContaBanco());
            pstmt.setString(21, cli.getCdPortador());
            pstmt.setString(22, cli.getCdTipoPagamento());
            pstmt.setString(23, cli.getCdCondPagamento());
            pstmt.setString(24, cli.getNomeContato());
            pstmt.setString(25, telefone);
            pstmt.setString(26, celular);
            pstmt.setString(27, cli.getEmail());
            pstmt.setString(28, cli.getDataModificacao());
            pstmt.setString(29, String.valueOf(cli.getSituacao()));
            pstmt.setString(30, cpfCnpj);
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização do registro!\nErr: " + ex);
        }
    }

    // método para selecionar os registros no banco
    public void selecionar(List<Clientes> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new Clientes(
                                rs.getString("cpf_cnpj"),
                                rs.getString("insc_estadual"),
                                rs.getString("tipo_pessoa"),
                                rs.getString("nome_razaosocial"),
                                rs.getString("apelido"),
                                rs.getString("optante_simples"),
                                rs.getString("optante_simbahia"),
                                rs.getString("optante_suframa"),
                                rs.getString("cd_suframa"),
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
    public void excluir(Clientes cli) {
        String sql = "DELETE FROM GCVCLIENTES WHERE CPF_CNPJ = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cli.getCdCpfCnpj());
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
