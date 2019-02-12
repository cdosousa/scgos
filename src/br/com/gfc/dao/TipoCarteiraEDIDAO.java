/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.dao;

import br.com.gfc.modelo.TipoCarteiraEDI;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01beta_0917 created on 23/11/2018
 */
public class TipoCarteiraEDIDAO{

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private ResultSet rs;
    private int numLinhas;
    private int pass = 0;
    private boolean statusConexao;

    // método construtor da classe para inicializar a conexão
    public TipoCarteiraEDIDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }
    
    // método para poder criar o registro no banco
    public void adicionar(TipoCarteiraEDI cedi) {
        String sql = "INSERT INTO GFCCARTEIRAEDI(CD_PORTADOR, CD_CARTEIRA, NOME_CARTEIRA, USUARIO_CADASTRO, DATA_CADASTRO, HORA_CADASTRO,"
                + " USUARIO_MODIFICACAO, DATA_MODIFICACAO, HORA_MODIFICACAO, SITUACAO)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cedi.getCdPortador());
            pstmt.setString(2, cedi.getCdCarteira());
            pstmt.setString(3, cedi.getNomeCarteira());
            pstmt.setString(4, cedi.getUsuarioCadastro());
            pstmt.setString(5, cedi.getDataCadastro());
            pstmt.setString(6, cedi.getHoraCadastro());
            pstmt.setString(7, cedi.getUsuarioModificacao());
            pstmt.setString(8, cedi.getDataModificacao());
            pstmt.setString(9, cedi.getHoraModificacao());
            pstmt.setString(10, cedi.getSituacao());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Código de Tipo carteira já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação da carteira, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }
    
    // método para poder atualizar o registro
    public void atualizar(TipoCarteiraEDI cedi){
        String sql = "UPDATE GFCCARTEIRAEDI SET NOME_CARTEIRA = ?,"
                + "USUARIO_MODIFICACAO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "HORA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + " WHERE CD_PORTADOR = ? AND CD_CARTEIRA = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cedi.getNomeCarteira());
            pstmt.setString(2, cedi.getUsuarioModificacao());
            pstmt.setString(3, cedi.getDataModificacao());
            pstmt.setString(4, cedi.getHoraModificacao());
            pstmt.setString(5, cedi.getSituacao());
            pstmt.setString(6, cedi.getCdPortador());
            pstmt.setString(7, cedi.getCdCarteira());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Erro na atualização do registro!\nErr: "+ex);
        }
    }
    
    // Método para excluir registros
    public void excluir(TipoCarteiraEDI cedi){
        String sql = "DELETE FROM GFCCARTEIRAEDI WHERE CD_PORTADOR = ? AND CD_CARTEIRA = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cedi.getCdPortador());
            pstmt.setString(2, cedi.getCdCarteira());
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

    // método para selecionar os registros no banco
    public void selecionar(List<TipoCarteiraEDI> resultado, String sql) {
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
                        resultado.add(new TipoCarteiraEDI(
                                rs.getString("cd_portador"),
                                rs.getString("cd_carteira"),
                                rs.getString("nome_carteira"),
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
        }catch(SQLException sqlex){
            JOptionPane.showMessageDialog(null,"Erro de conexão com o banco de dados!\nErr: "+sqlex+"\nSQL: "+sql);
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