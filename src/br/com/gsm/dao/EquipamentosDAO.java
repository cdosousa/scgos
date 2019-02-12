/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.dao;

import br.com.fabrica.ConexaoDataBase;
import br.com.gsm.modelo.Equipamentos;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01beta_0917 created on 22/09/2017
 */
public class EquipamentosDAO extends AbstractTableModel {

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    private int numLinhas;
    private int pass = 0;
    private boolean statusConexao;

    // método construtor da classe para inicializar a conexão
    public EquipamentosDAO() throws SQLException {
        this.conexao = new ConexaoDataBase().conexao();
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }
    
    // método para poder criar o registro no banco
    public void adicionar(Equipamentos eqp) {
        String sql = "INSERT INTO GSMEQUIPAMENTOS(CD_EQUIPAMENTO, NOME_EQUIPAMENTO, USUARIO_CADASTRO, DATA_CADASTRO, DATA_MODIFICACAO, SITUACAO) VALUES(?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, eqp.getCdEquipamento());
            pstmt.setString(2, eqp.getNomeEquipamento());
            pstmt.setString(3, eqp.getUsuarioCadastro());
            pstmt.setString(4, eqp.getDataCadastro());
            pstmt.setString(5, eqp.getDataModificacao());
            pstmt.setString(6, eqp.getSituacao());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Código de Equipamento já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do equipamento, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }
    
    // método para poder atualizar o registro
    public void atualizar(Equipamentos egp){
        String sql = "UPDATE GSMEQUIPAMENTOS SET NOME_EQUIPAMENTO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + "WHERE CD_EQUIPAMENTO = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, egp.getNomeEquipamento());
            pstmt.setString(2, egp.getDataModificacao());
            pstmt.setString(3, egp.getSituacao());
            pstmt.setString(4, egp.getCdEquipamento());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Erro na atualização do registro!\nErr: "+ex);
        }
    }

    // método para selecionar os registros no banco
    public void selecionar(List<Equipamentos> resultado, String sql) {
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
                        resultado.add(new Equipamentos(
                                rs.getString("cd_equipamento"),
                                rs.getString("nome_equipamento"),
                                rs.getString("usuario_cadastro"),
                                rs.getString("data_cadastro"),
                                rs.getString("data_modificacao"),
                                rs.getString("situacao")));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca do Registro!\nErr: " + ex);
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
    
    public void excluir(Equipamentos ta){
        String sql = "DELETE FROM GSMEQUIPAMENTOS WHERE CD_EQUIPAMENTO = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, ta.getCdEquipamento());
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
