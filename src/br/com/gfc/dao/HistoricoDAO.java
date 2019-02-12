/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.dao;

import br.com.gfc.modelo.Historico;
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
 * @version 0.01beta_0917 created on 24/09/2018
 */
public class HistoricoDAO{

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    private int numLinhas;
    private boolean statusConexao;

    /**
     * método construtor da classe para inicializar a conexão
     * @param conexao Objeto contento a instância de conexao do usuário com o banco de dados
     * @throws SQLException Lança uma exeção caso ocorrer erro
     */
    public HistoricoDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = this.conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }
    
    /**
     * método para poder criar o registro no banco
     * @param hs Objeto contendo os dados do histórico à ser criado
     */
    public void adicionar(Historico hs) {
        String sql = "INSERT INTO GFCHISTORICO(CD_HISTORICO, DESCRICAO, TIPO_HISTORICO, COMPLEMENTA_DOCUMENTO, COMPLEMENTA_EMISSAO, COMPLEMENTA_EMPRESA,"
                + " USUARIO_CADASTRO, DATA_CADASTRO, HORA_CADASTRO, USUARIO_MODIFICACAO, DATA_MODIFICACAO, HORA_MODIFICACAO, SITUACAO)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, hs.getCdHistorico());
            pstmt.setString(2, hs.getNomeHistorico());
            pstmt.setString(3, hs.getTipoHistorico());
            pstmt.setString(4, hs.getDocumentoComplementa());
            pstmt.setString(5, hs.getEmissaoComplementa());
            pstmt.setString(6, hs.getEmpresaComplementa());
            pstmt.setString(7, hs.getUsuarioCadastro());
            pstmt.setString(8, hs.getDataCadastro());
            pstmt.setString(9, hs.getHoraCadastro());
            pstmt.setString(10, hs.getUsuarioModificacao());
            pstmt.setString(11, hs.getDataModificacao());
            pstmt.setString(12, hs.getHoraModificacao());
            pstmt.setString(13, (hs.getSituacao()));
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Código de Histórico já cadastrada");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Histórico, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }
    
    /**
     * método para poder atualizar o registro
     * @param hs Objecto contendo os dados do histórico que serão atualizados
     */
    public void atualizar(Historico hs){
        String sql = "UPDATE GFCHISTORICO SET DESCRICAO = ?,"
                + "TIPO_HISTORICO = ?,"
                + "COMPLEMENTA_DOCUMENTO = ?,"
                + "COMPLEMENTA_EMISSAO = ?,"
                + "COMPLEMENTA_EMPRESA = ?,"
                + "USUARIO_MODIFICACAO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "HORA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + "WHERE CD_HISTORICO = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, hs.getNomeHistorico());
            pstmt.setString(2, hs.getTipoHistorico());
            pstmt.setString(3, hs.getDocumentoComplementa());
            pstmt.setString(4, hs.getEmissaoComplementa());
            pstmt.setString(5, hs.getEmpresaComplementa());
            pstmt.setString(6, hs.getUsuarioModificacao());
            pstmt.setString(7, hs.getDataModificacao());
            pstmt.setString(8, hs.getHoraModificacao());
            pstmt.setString(9, hs.getSituacao());
            pstmt.setString(10, hs.getCdHistorico());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Erro na atualização do registro!\nErr: "+ex);
        }
    }
    
    /**
     * Método para excluir registros
     * @param hs Objseto contendo o histórico a ser excluído
     */
    public void excluir(Historico hs){
        String sql = "DELETE FROM GFCHISTORICO WHERE CD_HISTORICO = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, hs.getCdHistorico());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro excluído com sucesso!");
        }catch (Exception ex) {
            if (ex.getMessage().contains("constraint")) {
                JOptionPane.showMessageDialog(null, "Não é possível excluir o registro, existe tarefas vinculadas!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na exclusão do registro!\nErr: " + ex);
            }
        }
    }

    /**
     * método para selecionar os registros no banco
     * @param resultado objeto List onde serã armazenados os resultados pesquisados
     * @param sql String contendo a sentença SQL a ser executada no banco
     */
    public void selecionar(List<Historico> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                //resultado = new ArrayList<Equipamentos>();
                //rs.next();
                while (rs.next()) {
                    try {
                        resultado.add(new Historico(
                                rs.getString("cd_historico"),
                                rs.getString("descricao"),
                                rs.getString("tipo_historico"),
                                rs.getString("complementa_documento"),
                                rs.getString("complementa_emissao"),
                                rs.getString("complementa_empresa"),
                                rs.getString("usuario_cadastro"),
                                rs.getString("data_cadastro"),
                                rs.getString("hora_cadastro"),
                                rs.getString("usuario_modificacao"),
                                rs.getString("data_modificacao"),
                                rs.getString("hora_modificacao"),
                                rs.getString("situacao")));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca do Registro!\nPrograma: HistoricoDAO.java\nErr: " + ex);
                    }
                }
            }
        }catch(SQLException sqlex){
            JOptionPane.showMessageDialog(null,"Erro de conexão com o banco de dados!\nErr: "+sqlex);
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nErr: "+ex);
        }
        finally{
            try{
                rs.close();
            }catch(SQLException ex){
                ex.printStackTrace();
                desconectar();
            }
        }
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