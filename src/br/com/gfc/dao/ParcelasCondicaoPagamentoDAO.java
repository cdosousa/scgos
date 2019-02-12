/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.dao;

import br.com.gfc.modelo.ParcelasCondicaoPagamento;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01beta_0917 created on 10/11/2017
 */
public class ParcelasCondicaoPagamentoDAO extends AbstractTableModel {

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    NumberFormat formato;
    private int numLinhas;
    private int pass = 0;
    private boolean statusConexao;
    private double totalRateio;

    // método construtor da classe para inicializar a conexão
    public ParcelasCondicaoPagamentoDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
        formato = NumberFormat.getInstance();
    }
    
    // método para poder criar o registro no banco
    public void adicionar(ParcelasCondicaoPagamento pcp) {
        String sql = "INSERT INTO GFCPARCELACONDPAG(CD_CONDPAG,"
                + " CD_PARCELA,"
                + " PRAZO_DIAS,"
                + " PERC_RATEIO,"
                + " USUARIO_CADASTRO,"
                + " DATA_CADASTRO,"
                + " DATA_MODIFICACAO,"
                + " SITUACAO)"
                + " VALUES(?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, pcp.getCdCondpag());
            pstmt.setInt(2, pcp.getCdParcela());
            pstmt.setInt(3, pcp.getPrazoDias());
            pstmt.setDouble(4, pcp.getPercRateio());
            pstmt.setString(5, pcp.getUsuarioCadastro());
            pstmt.setString(6, pcp.getDataCadastro());
            pstmt.setString(7, pcp.getDataModificacao());
            pstmt.setString(8, pcp.getSituacao());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Parcela de Pagamento já cadastrada");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação da Parcela de Pagamento, informe o administrador do sistema!\nErr: " + ex);
            }
        } 
    }
    
    // método para poder atualizar o registro
    public void atualizar(ParcelasCondicaoPagamento pcp){
        String sql = "UPDATE GFCPARCELACONDPAG SET PRAZO_DIAS = ?,"
                + "PERC_RATEIO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + " WHERE CD_CONDPAG = ? AND CD_PARCELA = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setInt(1, pcp.getPrazoDias());
            pstmt.setDouble(2, pcp.getPercRateio());
            pstmt.setString(3, pcp.getDataModificacao());
            pstmt.setString(4, pcp.getSituacao());
            pstmt.setString(5, pcp.getCdCondpag());
            pstmt.setInt(6,  pcp.getCdParcela());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Erro na atualização do registro!\nErr: "+ex);
        }
    }
    
    // Método para excluir registros
    public void excluir(ParcelasCondicaoPagamento pcp){
        String sql = "DELETE FROM GFCPARCELACONDPAG WHERE CD_CONDPAG = ? AND CD_PARCELA = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, pcp.getCdCondpag());
            pstmt.setInt(2, pcp.getCdParcela());
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
    
    // método para retornar o valor total do Rateio
    public double getTotalRateio(){
        return totalRateio;
    }

    // método para selecionar os registros no banco
    public void selecionar(List<ParcelasCondicaoPagamento> resultado, String sql) {
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
                        resultado.add(new ParcelasCondicaoPagamento(
                                rs.getString("cd_condpag"),
                                rs.getInt("cd_parcela"),
                                rs.getInt("prazo_dias"),
                                rs.getDouble("perc_rateio"),
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
        totalRateio = 0;
        while(rs.next()){
            totalRateio += rs.getDouble(3);
        }
        // determina o número de linhas em resultSet
        rs.last(); // move para a última linha
        numLinhas = rs.getRow(); //obtém o número da linha
        // notifica o Jtable que o modelo foi alterado
        fireTableStructureChanged();
        fireTableDataChanged();
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