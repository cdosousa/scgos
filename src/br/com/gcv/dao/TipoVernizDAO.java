/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.dao;

import br.com.gcv.modelo.TipoVerniz;
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
 * @version 0.01beta_0917 created on 04/12/2017
 */
public class TipoVernizDAO extends AbstractTableModel {

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    private int numLinhas;
    private int pass = 0;
    private boolean statusConexao;

    // método construtor da classe para inicializar a conexão
    public TipoVernizDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }
    
    // método para poder criar o registro no banco
    public void adicionar(TipoVerniz tvz ) {
        String sql = "INSERT INTO GCVTIPOVERNIZ(CD_TIPOVERNIZ,"
                + " NOME_TIPOVERNIZ,"
                + " VALOR,"
                + " DESCRICAO_COMERCIAL,"
                + " USUARIO_CADASTRO,"
                + " DATA_CADASTRO,"
                + " USUARIO_MODIFICACAO,"
                + " DATA_MODIFICACAO,"
                + " SITUACAO)"
                + " VALUES(?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, tvz.getCdTipoVerniz());
            pstmt.setString(2, tvz.getNomeTipoVerniz());
            pstmt.setDouble(3, tvz.getValor());
            pstmt.setString(4, tvz.getDescricaoComercial());
            pstmt.setString(5, tvz.getUsuarioCadastro());
            pstmt.setString(6, tvz.getDataCadastro());
            pstmt.setString(7, tvz.getUsuarioModificacao());
            pstmt.setString(8, tvz.getDataModificacao());
            pstmt.setString(9, tvz.getSituacao());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Tipo de Verniz já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Tipo de Verniz, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }
    
    // método para poder atualizar o registro
    public void atualizar(TipoVerniz tvz){
        String sql = "UPDATE GCVTIPOVERNIZ SET NOME_TIPOVERNIZ = ?,"
                + " VALOR = ?,"
                + " DESCRICAO_COMERCIAL = ?,"
                + " USUARIO_MODIFICACAO = ? ,"
                + " DATA_MODIFICACAO = ?,"
                + " SITUACAO = ?"
                + " WHERE CD_TIPOVERNIZ = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, tvz.getNomeTipoVerniz());
            pstmt.setDouble(2, tvz.getValor());
            pstmt.setString(3, tvz.getDescricaoComercial());
            pstmt.setString(4, tvz.getUsuarioModificacao());
            pstmt.setString(5, tvz.getDataModificacao());
            pstmt.setString(6, tvz.getSituacao());
            pstmt.setString(7, tvz.getCdTipoVerniz());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Erro na atualização do registro!\nErr: "+ex);
        }
    }
    
    // Método para excluir registros
    public void excluir(TipoVerniz tvz){
        String sql = "DELETE FROM GCVTIPOVERNIZ WHERE CD_TIPOVERNIZ = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, tvz.getCdTipoVerniz());
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
    public void selecionar(List<TipoVerniz> resultado, String sql) {
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
                        resultado.add(new TipoVerniz(
                                rs.getString("cd_tipoverniz"),
                                rs.getString("nome_tipoverniz"),
                                rs.getDouble("valor"),
                                rs.getString("descricao_comercial"),
                                rs.getString("usuario_cadastro"),
                                rs.getString("data_cadastro"),
                                rs.getString("usuario_modificacao"),
                                rs.getString("data_modificacao"),
                                rs.getString("situacao")));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca do Registro!\nPrograma: TipoVernizDAO.java\nErr: " + ex);
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
    
    @Override
    public boolean isCellEditable(int row, int column){
        return false;
    }
    // método que obtém a classe que representa o número de colunas
    public Class getColumnClass(int column) throws IllegalStateException {
        //certificando que a conexão está disponível com o banco
        if(!statusConexao)
            throw new IllegalStateException("Sem conexão com o Banco de Dados");
        //determina a classe java de coluna
        try{
            String classeName = metaData.getColumnClassName(column + 1);
            //retorna o objeto da classe que representa o classname
            return Class.forName(classeName);
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return Object.class; //se ocorrerem os problemas acima, supõe tipo Object
    }
    
    @Override
    public int getColumnCount() throws IllegalStateException{
        //certificando que a conexão está disponível com o banco
        if(!statusConexao)
            throw new IllegalStateException("Sem conexão com o Banco de Dados");
        // determina o número de linhas
        try{
            return metaData.getColumnCount();
        }
        catch (SQLException ex){
            ex.printStackTrace();
        }
        return 0; //se ocorrerem problemas acima retorna 0 para o número de colunas
    }
    
    // método que obtém o nome de uma coluna particular no ResultSet
    public String getColumnName(int column) throws IllegalStateException{
        if(!statusConexao)
            throw new IllegalStateException("Sem conexão com o Banco de Dados");
        //determina o nome da coluna
        try{
            return metaData.getColumnLabel(column+1);
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return ""; //se ocorrerem problemas, retorna string vazia para o numero da coluna
    }
    
    @Override
    public int getRowCount() throws IllegalStateException{
        if(!statusConexao)
            throw new IllegalStateException("Sem conexão com o Banco de Dados");
        return numLinhas;
    }

    // método que obtem o valor na linha e coluna específica
    @Override
    public Object getValueAt(int row, int column)throws IllegalStateException{
        if(!statusConexao)
            throw new IllegalStateException("Sem conexão com o Banco de Dados");
        // obttem o valor na linha e coluna de ResultSet especificada
        try{
            rs.absolute(row + 1);
            return rs.getObject(column + 1);
        }
        catch(SQLException ex){
            ex.printStackTrace();
        }
        return ""; // se ocorrerem problemas retorna o objetct string vazio
        
    }
    
    // método que configura uma nova string de consulta com o banco
    public void setQuery(String query) throws SQLException, IllegalStateException{
        if(!statusConexao)
            throw new IllegalStateException("Sem conexão com o Banco de Dados");
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