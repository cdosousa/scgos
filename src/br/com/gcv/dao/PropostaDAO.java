/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.dao;

import br.com.gcv.modelo.Proposta;
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
public class PropostaDAO extends AbstractTableModel {

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
    public PropostaDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        //stmt = conexao.createStatement();
        statusConexao = true;
    }

    // método para poder criar o registro no banco
    public void adicionar(Proposta pro) {
        String sql = "INSERT INTO GCVPROPOSTA(CD_PROPOSTA,"
                + "CD_REVISAO,"
                + "CD_ATENDIMENTO,"
                + "CD_VISTORIA,"
                + "CD_VENDEDOR,"
                + "CD_TECNICO,"
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
                + "TIPO_PEDIDO,"
                + "CD_TIPOPAGAMENTO,"
                + "CD_CONDPAG,"
                + "CD_PEDIDO,"
                + "VALOR_SERVICOS,"
                + "VALOR_PRODUTOS,"
                + "VALOR_ADICIONAIS,"
                + "VALOR_DESCONTOS,"
                + "VALOR_OUTROS_DESCONTOS,"
                + "VALOR_TOTAL_BRUTO,"
                + "VALOR_TOTAL_LIQUIDO,"
                + "PRAZO_EXECUCAO,"
                + "OBS,"
                + "DATA_ENVIO,"
                + "USUARIO_CADASTRO,"
                + "DATA_CADASTRO,"
                + "HORA_CADASTRO,"
                + "USUARIO_MODIFICACAO,"
                + "DATA_MODIFICACAO,"
                + "HORA_MODIFICACAO,"
                + "SITUACAO) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String telefone = pro.getTelefone();
        telefone = telefone.replace("(", "");
        telefone = telefone.replace(")", "");
        telefone = telefone.replace("-", "");
        telefone = telefone.replace(" ", "");
        String celular = pro.getCelular();
        celular = celular.replace("(", "");
        celular = celular.replace(")", "");
        celular = celular.replace("-", "");
        celular = celular.replace(" ", "");
        String cep = pro.getCdCep();
        cep = cep.replace("-", "");
        if (pro.getValorProdutos() > 0) {
            if (pro.getValorServico() > 0) {
                pro.setTipoPedido("A");
            } else {
                pro.setTipoPedido("R");
            }
        } else if (pro.getValorServico() > 0) {
            pro.setTipoPedido("S");
        } else {
            pro.setTipoPedido(" ");
        }
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, pro.getCdProposta());
            pstmt.setString(2, pro.getCdRevisao());
            pstmt.setString(3, pro.getCdAtendimento());
            pstmt.setString(4, pro.getCdVistoria());
            pstmt.setString(5, pro.getCdVendedor());
            pstmt.setString(6, pro.getCdTecnico());
            pstmt.setString(7, pro.getNomeRazaoSocial());
            pstmt.setString(8, pro.getTipoPessoa());
            pstmt.setString(9, telefone);
            pstmt.setString(10, celular);
            pstmt.setString(11, pro.getEmail());
            pstmt.setString(12, pro.getTipoLogradouro());
            pstmt.setString(13, pro.getLogradouro());
            pstmt.setString(14, pro.getNumero());
            pstmt.setString(15, pro.getComplemento());
            pstmt.setString(16, pro.getBairro());
            pstmt.setString(17, pro.getCdMunicipioIbge());
            pstmt.setString(18, pro.getSiglaUf());
            pstmt.setString(19, cep);
            pstmt.setString(20, pro.getTipoEndereco());
            pstmt.setString(21, pro.getTipoPedido());
            pstmt.setString(22, pro.getCdTipoPagamento());
            pstmt.setString(23, pro.getCdCondPagamento());
            pstmt.setString(24, pro.getCdPedido());
            pstmt.setDouble(25, pro.getValorServico());
            pstmt.setDouble(26, pro.getValorProdutos());
            pstmt.setDouble(27, pro.getValorAdicionais());
            pstmt.setDouble(28, pro.getValorDescontos());
            pstmt.setDouble(29, pro.getValorOutrosDescontos());
            pstmt.setDouble(30, pro.getValorTotalBruto());
            pstmt.setDouble(31, pro.getValorTotalLiquido());
            pstmt.setString(32, pro.getPrazoExecucao());
            pstmt.setString(33, pro.getObs());
            pstmt.setString(34, pro.getDataEnvio());
            pstmt.setString(35, pro.getUsuarioCadastro());
            pstmt.setString(36, pro.getDataCadastro());
            pstmt.setString(37, pro.getHoraCadastro());
            pstmt.setString(38, pro.getUsuarioModificacao());
            pstmt.setString(39, pro.getDataModificacao());
            pstmt.setString(40, pro.getHoraModificacao());
            pstmt.setString(41, pro.getSituacao());
            pstmt.execute();
            pstmt.close();
            //JOptionPane.showMessageDialog(null, "Atendimento gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Numero de Proposta já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação da Proposta, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }

    // método para poder atualizar o registro
    public void atualizar(Proposta pro) {
        String sql = "UPDATE GCVPROPOSTA SET CD_VENDEDOR = ?,"
                + "CD_TECNICO = ?,"
                + "NOME_CLIENTE = ?,"
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
                + "TIPO_PEDIDO = ?,"
                + "CD_TIPOPAGAMENTO = ?,"
                + "CD_CONDPAG = ?,"
                + "CD_PEDIDO = ?,"
                + "VALOR_SERVICOS = ?,"
                + "VALOR_PRODUTOS = ?,"
                + "VALOR_ADICIONAIS = ?,"
                + "VALOR_DESCONTOS = ?,"
                + "VALOR_OUTROS_DESCONTOS = ?,"
                + "VALOR_TOTAL_BRUTO = ?,"
                + "VALOR_TOTAL_LIQUIDO = ?,"
                + "PRAZO_EXECUCAO = ?,"
                + "OBS = ?,"
                + "DATA_ENVIO = ?,"
                + "USUARIO_MODIFICACAO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "HORA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + " WHERE CD_PROPOSTA = ? AND CD_REVISAO = ?";
        String telefone = pro.getTelefone();
        telefone = telefone.replace("(", "");
        telefone = telefone.replace(")", "");
        telefone = telefone.replace("-", "");
        telefone = telefone.replace(" ", "");
        String celular = pro.getCelular();
        celular = celular.replace("(", "");
        celular = celular.replace(")", "");
        celular = celular.replace("-", "");
        celular = celular.replace(" ", "");
        String cep = pro.getCdCep();
        cep = cep.replace("-", "");
        if (pro.getValorProdutos() > 0) {
            if (pro.getValorServico() > 0) {
                pro.setTipoPedido("A");
            } else {
                pro.setTipoPedido("R");
            }
        } else if (pro.getValorServico() > 0) {
            pro.setTipoPedido("S");
        } else {
            pro.setTipoPedido(" ");
        }
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, pro.getCdVendedor());
            pstmt.setString(2, pro.getCdTecnico());
            pstmt.setString(3, pro.getNomeRazaoSocial());
            pstmt.setString(4, pro.getTipoPessoa());
            pstmt.setString(5, telefone);
            pstmt.setString(6, celular);
            pstmt.setString(7, pro.getEmail());
            pstmt.setString(8, pro.getTipoLogradouro());
            pstmt.setString(9, pro.getLogradouro());
            pstmt.setString(10, pro.getNumero());
            pstmt.setString(11, pro.getComplemento());
            pstmt.setString(12, pro.getBairro());
            pstmt.setString(13, pro.getCdMunicipioIbge());
            pstmt.setString(14, pro.getSiglaUf());
            pstmt.setString(15, cep);
            pstmt.setString(16, pro.getTipoEndereco());
            pstmt.setString(17, pro.getTipoPedido());
            pstmt.setString(18, pro.getCdTipoPagamento());
            pstmt.setString(19, pro.getCdCondPagamento());
            pstmt.setString(20, pro.getCdPedido());
            pstmt.setDouble(21, pro.getValorServico());
            pstmt.setDouble(22, pro.getValorProdutos());
            pstmt.setDouble(23, pro.getValorAdicionais());
            pstmt.setDouble(24, pro.getValorDescontos());
            pstmt.setDouble(25, pro.getValorOutrosDescontos());
            pstmt.setDouble(26, pro.getValorTotalBruto());
            pstmt.setDouble(27, pro.getValorTotalLiquido());
            pstmt.setString(28, pro.getPrazoExecucao());
            pstmt.setString(29, pro.getObs());
            pstmt.setString(30, pro.getDataEnvio());
            pstmt.setString(31, pro.getUsuarioModificacao());
            pstmt.setString(32, pro.getDataModificacao());
            pstmt.setString(33, pro.getHoraModificacao());
            pstmt.setString(34, pro.getSituacao());
            pstmt.setString(35, pro.getCdProposta());
            pstmt.setString(36, pro.getCdRevisao());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Proposta atualizada com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização da Proposta!\nErr: " + ex);
        }
    }

    // método para selecionar os registros no banco
    public void selecionar(List<Proposta> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new Proposta(
                                rs.getString("cd_proposta"),
                                rs.getString("cd_revisao"),
                                rs.getString("cd_atendimento"),
                                rs.getString("cd_vistoria"),
                                rs.getString("cd_vendedor"),
                                rs.getString("cd_tecnico"),
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
                                rs.getString("tipo_pedido"),
                                rs.getString("cd_tipopagamento"),
                                rs.getString("cd_condpag"),
                                rs.getString("cd_pedido"),
                                rs.getDouble("valor_servicos"),
                                rs.getDouble("valor_produtos"),
                                rs.getDouble("valor_adicionais"),
                                rs.getDouble("valor_descontos"),
                                rs.getDouble("valor_outros_descontos"),
                                rs.getDouble("valor_total_bruto"),
                                rs.getDouble("valor_total_liquido"),
                                rs.getString("prazo_execucao"),
                                rs.getString("obs"),
                                rs.getString("data_envio"),
                                rs.getString("usuario_cadastro"),
                                rs.getString("data_cadastro"),
                                rs.getString("hora_cadastro"),
                                rs.getString("usuario_modificacao"),
                                rs.getString("data_modificacao"),
                                rs.getString("hora_modificacao"),
                                rs.getString("situacao")));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca da Proposta!\nErr: " + ex);
                    }
                }
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: AtendimentoDAO.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nPrograma: PropostaDAO.java\nErr: " + ex);
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
    public void excluir(Proposta pro) {
        String sql = "DELETE FROM GCVPROPOSTA WHERE CD_PROPOSTA = ? AND CD_REVISAO = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, pro.getCdProposta());
            pstmt.setString(2, pro.getCdRevisao());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Proposta excluída com sucesso!");
        } catch (Exception ex) {
            if (ex.getMessage().contains("CONSTRAINT")) {
                JOptionPane.showMessageDialog(null, "Esta Proposta já está sendo usada, exclusão não permitida!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na exclusão da Proposta!\nErr: " + ex);
            }
        }
    }

    // método para buscar totais no banco
    public void totalizarItens(List<Proposta> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new Proposta(
                                rs.getString("Proposta"),
                                rs.getString("Revisao"),
                                rs.getDouble("Total Produtos"),
                                rs.getDouble("Total Serviços"),
                                rs.getDouble("Adicionais"),
                                rs.getDouble("Descontos"),
                                rs.getDouble("Outros Descontos"),
                                rs.getDouble("Total Proposta Bruto"),
                                rs.getDouble("Total Proposta Liquido")
                        ));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca do Item!\nPrograma: ItemPropostaDAO.java\nErr: " + ex);
                    }
                }
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: PropostaDAO.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nPrograma: PropostaDAO.java\nErr: " + ex);
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
