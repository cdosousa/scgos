/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.dao;

import br.com.gfc.modelo.Lancamentos;
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
 * @version 0.01beta_0917 created on 14/02/2018
 */
public class LancamentosDAO extends AbstractTableModel {

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    private int numLinhas;
    private int pass = 0;
    private boolean statusConexao;

    // método construtor da classe para inicializar a conexão
    public LancamentosDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }

    // método para poder criar o registro no banco
    public int adicionar(Lancamentos lan) {
        String sql = "INSERT INTO GFCLANCAMENTOS(CD_LANCAMENTO,"
                + " SEQUENCIAL,"
                + " TIPO_LANCAMENTO,"
                + " TIPO_MOVIMENTO,"
                + " TITULO,"
                + " CD_PARCELA,"
                + " CPF_CNPJ,"
                + " DATA_EMISSAO,"
                + " DATA_VENCIMENTO,"
                + " DATA_LIQUIDACAO,"
                + " VALOR_LANCAMENTO, "
                + " VALOR_SALDO,"
                + " CD_PORTADOR,"
                + " TIPO_DOCUMENTO,"
                + " DOCUMENTO,"
                + " CD_TIPOPAGAMENTO,"
                + " NUMERO_REMESSA,"
                + " VALOR_JUROS,"
                + " VALOR_MULTA,"
                + " VALOR_ATUALIZADO,"
                + " DIAS_CARTORIO,"
                + " DIAS_LIQUIDACAO,"
                + " GEROU_ARQUIVO,"
                + " CD_CONTA_REDUZIDA,"
                + " CD_CCUSTO,"
                + " CD_CONTRA_PARTIDA,"
                + " PREPARADO,"
                + " CD_HISTORICO,"
                + " COMPLEMENTO_HISTORICO,"
                + " USUARIO_CADASTRO,"
                + " DATA_CADASTRO,"
                + " HORA_CADASTRO,"
                + " USUARIO_MODIFICACAO,"
                + " DATA_MODIFICACAO,"
                + " HORA_MODIFICACAO,"
                + " SITUACAO)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, lan.getCdLancamento());
            pstmt.setInt(2, lan.getSequencial());
            pstmt.setString(3, lan.getTipoLancamento());
            pstmt.setString(4, lan.getTipoMovimento());
            pstmt.setString(5, lan.getTitulo());
            pstmt.setInt(6, lan.getCdParcela());
            pstmt.setString(7, lan.getCpfCnpj());
            pstmt.setString(8, lan.getDataEmissao());
            pstmt.setString(9, lan.getDataVencimento());
            pstmt.setString(10, lan.getDataLiquidacao());
            pstmt.setDouble(11, lan.getValorLancamento());
            pstmt.setDouble(12, lan.getValorSaldo());
            pstmt.setString(13, lan.getCdPortador());
            pstmt.setString(14, lan.getTipoDocumento());
            pstmt.setString(15, lan.getDocumento());
            pstmt.setString(16, lan.getCdTipoPagamento());
            pstmt.setString(17, lan.getNossoNumeroBanco());
            pstmt.setDouble(18, lan.getValorJuros());
            pstmt.setDouble(19, lan.getValorMulta());
            pstmt.setDouble(20, lan.getValorAtualizado());
            pstmt.setInt(21, lan.getDiasCartorio());
            pstmt.setInt(22, lan.getDiasLiquidacao());
            pstmt.setString(23, lan.getGerouArquivo());
            pstmt.setString(24, lan.getCdContaReduzida());
            pstmt.setString(25, lan.getCdCCusto());
            pstmt.setString(26, lan.getContraPartida());
            pstmt.setString(27, lan.getPreparado());
            pstmt.setString(28, lan.getCdHistorico());
            pstmt.setString(29, lan.getComplementoHistorico());
            pstmt.setString(30, lan.getUsuarioCadastro());
            pstmt.setString(31, lan.getDataCadastro());
            pstmt.setString(32, lan.getHoraCadastro());
            pstmt.setString(33, lan.getUsuarioModificacao());
            pstmt.setString(34, lan.getDataModificacao());
            pstmt.setString(35, lan.getHoraModificacao());
            pstmt.setString(36, lan.getSituacao());
            pstmt.execute();
            pstmt.close();
            return 0;
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Número de Lançamento já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Lançamento, informe o administrador do sistema!\nErr: " + ex);
            }
            return 1;
        }
    }

    /**
     * método para poder atualizar o registro de lançamento
     * @param lan objeto contendo o lançamento a ser atualizado.
     */
    public int atualizar(Lancamentos lan) {
        String sql = "UPDATE GFCLANCAMENTOS SET DATA_VENCIMENTO = ?,"
                + " DATA_LIQUIDACAO = ?,"
                + " VALOR_LANCAMENTO = ?,"
                + " VALOR_SALDO = ?,"
                + " CD_PORTADOR = ?,"
                + " NUMERO_REMESSA = ?,"
                + " VALOR_JUROS = ?,"
                + " VALOR_MULTA = ?,"
                + " VALOR_ATUALIZADO = ?,"
                + " DIAS_CARTORIO = ?,"
                + " DIAS_LIQUIDACAO = ?,"
                + " GEROU_ARQUIVO = ?,"
                + " CD_CONTA_REDUZIDA = ?,"
                + " CD_CCUSTO = ?,"
                + " CD_CONTRA_PARTIDA = ?,"
                + " PREPARADO = ?,"
                + " CD_HISTORICO = ?,"
                + " COMPLEMENTO_HISTORICO = ?,"
                + " USUARIO_MODIFICACAO = ?,"
                + " DATA_MODIFICACAO = ?,"
                + " HORA_MODIFICACAO = ?,"
                + " SITUACAO = ?"
                + " WHERE CD_LANCAMENTO = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, lan.getDataVencimento());
            pstmt.setString(2, lan.getDataLiquidacao());
            pstmt.setDouble(3, lan.getValorLancamento());
            pstmt.setDouble(4, lan.getValorSaldo());
            pstmt.setString(5, lan.getCdPortador());
            pstmt.setString(6, lan.getNossoNumeroBanco());
            pstmt.setDouble(7, lan.getValorJuros());
            pstmt.setDouble(8, lan.getValorMulta());
            pstmt.setDouble(9, lan.getValorAtualizado());
            pstmt.setInt(10, lan.getDiasCartorio());
            pstmt.setInt(11, lan.getDiasLiquidacao());
            pstmt.setString(12, lan.getGerouArquivo());
            pstmt.setString(13, lan.getCdContaReduzida());
            pstmt.setString(14, lan.getCdCCusto());
            pstmt.setString(15, lan.getContraPartida());
            pstmt.setString(16, lan.getPreparado());
            pstmt.setString(17, lan.getCdHistorico());
            pstmt.setString(18, lan.getComplementoHistorico());
            pstmt.setString(19, lan.getUsuarioModificacao());
            pstmt.setString(20, lan.getDataModificacao());
            pstmt.setString(21, lan.getHoraModificacao());
            pstmt.setString(22, lan.getSituacao());
            pstmt.setString(23, lan.getCdLancamento());
            pstmt.execute();
            pstmt.close();
            //JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização do registro!\nErr: " + ex);
            return 1;
        }
        return 0;
    }
    
    /**
     * Método para mudar o tipo de lancamento
     * @param lan Objeto contendo o lançamento a ser modificado
     * @param cdLancOriginal o número do lancamento original a ser alterado
     */
    public void mudarTipoLancamento(Lancamentos lan, String cdLancOriginal){
        String sql = "UPDATE GFCLANCAMENTOS SET CD_LANCAMENTO = ?,"
                + " TIPO_LANCAMENTO = ?,"
                + " USUARIO_MODIFICACAO = ?,"
                + " DATA_MODIFICACAO = ?,"
                + " HORA_MODIFICACAO = ?"
                + " WHERE CD_LANCAMENTO = ? AND SEQUENCIAL = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, lan.getCdLancamento());
            pstmt.setString(2, lan.getTipoLancamento());
            pstmt.setString(3, lan.getUsuarioModificacao());
            pstmt.setString(4, lan.getDataModificacao());
            pstmt.setString(5, lan.getHoraModificacao());
            pstmt.setString(6, cdLancOriginal);
            pstmt.setInt(7, lan.getSequencial());
            pstmt.execute();
            pstmt.close();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização do registro!\nErr: " + ex);
        }
    }

    // Método para excluir registros
    public void excluir(Lancamentos lan) {
        String sql = "DELETE FROM GFCLANCAMENTOS WHERE CD_LANCAMENTO = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, lan.getCdLancamento());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro excluído com sucesso!");
        } catch (Exception ex) {
            if (ex.getMessage().contains("constraint")) {
                JOptionPane.showMessageDialog(null, "Não é possível excluir o registro, existe tarefas vinculadas!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na exclusão do registro!\nErr: " + ex);
            }
        }
    }

    // método para selecionar os registros no banco
    public void selecionar(List<Lancamentos> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new Lancamentos(
                                rs.getString("cd_lancamento"),
                                rs.getInt("sequencial"),
                                rs.getString("tipo_lancamento"),
                                rs.getString("tipo_movimento"),
                                rs.getString("titulo"),
                                rs.getInt("cd_parcela"),
                                rs.getString("cpf_cnpj"),
                                rs.getString("data_emissao"),
                                rs.getString("data_vencimento"),
                                rs.getString("data_liquidacao"),
                                rs.getDouble("valor_lancamento"),
                                rs.getDouble("valor_saldo"),
                                rs.getString("cd_portador"),
                                rs.getString("tipo_documento"),
                                rs.getString("documento"),
                                rs.getString("cd_tipopagamento"),
                                rs.getString("numero_remessa"),
                                rs.getDouble("valor_juros"),
                                rs.getDouble("valor_multa"),
                                rs.getDouble("valor_atualizado"),
                                rs.getInt("dias_cartorio"),
                                rs.getInt("dias_liquidacao"),
                                rs.getString("gerou_arquivo"),
                                rs.getString("cd_conta_reduzida"),
                                rs.getString("cd_ccusto"),
                                rs.getString("cd_contra_partida"),
                                rs.getString("preparado"),
                                rs.getString("cd_historico"),
                                rs.getString("complemento_historico"),
                                rs.getString("usuario_cadastro"),
                                rs.getString("data_cadastro"),
                                rs.getString("hora_cadastro"),
                                rs.getString("usuario_modificacao"),
                                rs.getString("data_modificacao"),
                                rs.getString("hora_modificacao"),
                                rs.getString("situacao")));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca do Registro!\nErr: " + ex);
                    }
                }
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nErr: " + sqlex + "\nSQL: " + sql);
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