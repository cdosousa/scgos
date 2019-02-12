/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.dao;

import br.com.gfc.modelo.PreparacaoPagamentos;
import br.com.gfc.modelo.PreparacaoTitulos;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01beta_0917 created on 17/09/2018
 */
public class PreparacaoPagamentosDAO {

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private ResultSet rs;
    private ResultSet rsTit;
    private ResultSetMetaData metaData;
    private int numLinhas;
    private int pass = 0;
    private boolean statusConexao;
    
    /**
     * método construtor da classe para inicializar a conexão
     *
     * @param conexao objeto contendo a sessão da conexão do usuário no bacno
     * @throws SQLException lança uma exeção quando der erro
     */
    public PreparacaoPagamentosDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }

    /**
     * método para poder criar o registro no banco
     *
     * @param pp objeto contendo os dados da preparação
     * @return falso ou verdadeiro para a criação do registro no banco
     */
    public int adicionarPreparacao(PreparacaoPagamentos pp) {
        String sql = "INSERT INTO GFCPREPARACAOPAGAMENTOS(CD_PREPARACAO,"
                + " DATA_LIQUIDACAO_INI,"
                + " DATA_LIQUIDACAO_FIN,"
                + " CD_PORTADOR,"
                + " CD_TIPOPAGAMENTO,"
                + " CD_TIPOMOVIMENTO,"
                + " QUANTIDADE_TITULOS,"
                + " VALOR_TOTAL, "
                + " USUARIO_CADASTRO,"
                + " DATA_CADASTRO,"
                + " HORA_CADASTRO,"
                + " USUARIO_MODIFICACAO,"
                + " DATA_MODIFICACAO,"
                + " HORA_MODIFICACAO,"
                + " SITUACAO)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, pp.getCdPreparacao());
            pstmt.setString(2, pp.getDataLiquidacaoIni());
            pstmt.setString(3, pp.getDataLiquidacaoFin());
            pstmt.setString(4, pp.getCdPortador());
            pstmt.setString(5, pp.getCdTipoPagamento());
            pstmt.setString(6, pp.getTipoMovimento());
            pstmt.setInt(7, pp.getQuantidadeTitulos());
            pstmt.setDouble(8, pp.getValorTotal());
            pstmt.setString(9, pp.getUsuarioCadastro());
            pstmt.setString(10, pp.getDataCadastro());
            pstmt.setString(11, pp.getHoraCadastro());
            pstmt.setString(12, pp.getUsuarioModificacao());
            pstmt.setString(13, pp.getDataModificacao());
            pstmt.setString(14, pp.getHoraModificacao());
            pstmt.setString(15, pp.getSituacao());
            pstmt.execute();
            pstmt.close();
            return 1;
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Número de Preparação já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação da Preparação, informe o administrador do sistema!\nErr: " + ex);
            }
            return 0;
        }
    }

    /**
     * Método para adicionar os títulos selecionados na preparação
     *
     * @param pt objeto contendo o título a ser adicionado
     * @return retorna falso ou verdadeiro para a gravação do título
     */
    public int adicionarPreparacaoTitulos(PreparacaoTitulos pt) {
        String sql = "INSERT INTO gfcpreparacaotitulos"
                + "(cd_preparacao,"
                + "cd_lancamento,"
                + "valor_saldo,"
                + "usuario_cadastro,"
                + "data_cadastro,"
                + "hora_cadastro,"
                + "usuario_modificacao,"
                + "data_modificacao,"
                + "hora_modificacao,"
                + "situacao)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, pt.getCdPreparacao());
            pstmt.setString(2, pt.getCdLancamento());
            pstmt.setDouble(3, pt.getValorSaldo());
            pstmt.setString(4, pt.getUsuarioCadastro());
            pstmt.setString(5, pt.getDataCadastro());
            pstmt.setString(6, pt.getHoraCadastro());
            pstmt.setString(7, pt.getUsuarioModificacao());
            pstmt.setString(8, pt.getDataModificacao());
            pstmt.setString(9, pt.getHoraModificacao());
            pstmt.setString(10, pt.getSituacao());
            pstmt.execute();
            pstmt.close();
            return 1;
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Título já cadastrado na preparação");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do título na preparação, informe o administrador do sistema!\nErr: " + ex);
            }
        }
        return 0;
    }

    /**
     * método para poder atualizarPreparacao o registro de lançamento
     *
     * @param pp objeto contendo o lançamento a ser atualizado.
     * @return retorna falso ou verdadeiro para a gravação do registro no banco
     */
    public int atualizarPreparacao(PreparacaoPagamentos pp) {
        String sql = "UPDATE GFCPREPARACAOPAGAMENTOS SET DATA_LIQUIDACAO_INI = ?,"
                + " DATA_LIQUIDACAO_FIN = ?,"
                + " QUANTIDADE_TITULOS = ?,"
                + " VALOR_TOTAL = ?,"
                + " USUARIO_MODIFICACAO = ?,"
                + " DATA_MODIFICACAO = ?,"
                + " HORA_MODIFICACAO = ?,"
                + " SITUACAO = ?"
                + " WHERE CD_PREPARACAO = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, pp.getDataLiquidacaoIni());
            pstmt.setString(2, pp.getDataLiquidacaoFin());
            pstmt.setInt(3, pp.getQuantidadeTitulos());
            pstmt.setDouble(4, pp.getValorTotal());
            pstmt.setString(4, pp.getUsuarioModificacao());
            pstmt.setString(5, pp.getDataModificacao());
            pstmt.setString(6, pp.getHoraModificacao());
            pstmt.setString(7, pp.getSituacao());
            pstmt.setString(8, pp.getCdPreparacao());
            pstmt.execute();
            pstmt.close();
            return 1;
            //JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização do registro!\nErr: " + ex);
        }
        return 0;
    }

    /**
     * Método para excluirPreparacao registros
     *
     * @param pp objeto contendo o registro que deverá ser excluído no banco
     */
    public void excluirPreparacao(PreparacaoPagamentos pp) {
        String sql = "DELETE FROM GFCPREPARACAOPAGAMENTOS WHERE CD_PREPARACAOPAGAMENTOS = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, pp.getCdPreparacao());
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

    /**
     * método para selecionarPreparacao os registros no banco
     *
     * @param resultado objeto List onde serão armazenados os registros que
     * serão pesquisados
     * @param sql String contendo a sentença sql para pesquisa no banco
     */
    public void selecionarPreparacao(List<PreparacaoPagamentos> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new PreparacaoPagamentos(
                                rs.getString("cd_preparacao"),
                                rs.getString("data_liquidacao_ini"),
                                rs.getString("data_liquidacao_fin"),
                                rs.getString("cd_portador"),
                                rs.getString("cd_tipopagamento"),
                                rs.getString("cd_tipomovimento"),
                                rs.getInt("quantidade_titulos"),
                                rs.getDouble("valor_total"),
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
    
    /**
     * método para selecionar os títulos da preparação de pagamento
     *
     * @param resultado objeto List onde serão armazenados os registros que
     * serão pesquisados
     * @param sql String contendo a sentença sql para pesquisa no banco
     */
    public void selecionarPreparacaoTitulos(List<PreparacaoTitulos> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rsTit = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new PreparacaoTitulos(
                                rsTit.getString("cd_preparacao"),
                                rsTit.getString("cd_lancamento"),
                                rsTit.getDouble("valor_saldo"),
                                rsTit.getString("usuario_cadastro"),
                                rsTit.getString("data_cadastro"),
                                rsTit.getString("hora_cadastro"),
                                rsTit.getString("usuario_modificacao"),
                                rsTit.getString("data_modificacao"),
                                rsTit.getString("hora_modificacao"),
                                rsTit.getString("situacao")));
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
                rsTit.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                desconectar();
            }
        }
    }

    /**
     * Método para buscar a última preparação do dia gravada no banco de dados
     *
     * @param sql String contendo a sentença SQL para pesquisar no banco de
     * dados
     * @return retorna o número da próxima sequencia que deverá ser utilizada.
     */
    public int buscarUltimaPrep(String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexao com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                if (rs.next()) {
                    return Integer.valueOf(rs.getString("Proximo"));
                } else {
                    return 0;
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
        return 0;
    }

    /**
     * método para desconectar o banco
     */
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
