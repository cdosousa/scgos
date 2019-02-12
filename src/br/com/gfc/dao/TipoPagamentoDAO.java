/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.dao;

import br.com.gfc.modelo.TipoPagamento;
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
 * @version 0.01beta_0917 created on 10/11/2017
 */
public class TipoPagamentoDAO extends AbstractTableModel {

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    private int numLinhas;
    private int pass = 0;
    private boolean statusConexao;

    // método construtor da classe para inicializar a conexão
    public TipoPagamentoDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }
    
    // método para poder criar o registro no banco
    public void adicionar(TipoPagamento tp) {
        String sql = "INSERT INTO GFCTIPOPAGAMENTO(CD_TIPOPAGAMENTO, NOME_TIPO_PAGAMENTO, PERMITE_PARCELAMENTO,"
                + "EMITE_BOLETO, ENVIA_ARQ_BANCO, ENVIA_CARTORIO, DIAS_CARTORIO, CD_PORTADOR, TX_JUROS,"
                + "TX_MULTA, DIAS_LIQUIDACAO, USUARIO_CADASTRO, DATA_CADASTRO, DATA_MODIFICACAO, SITUACAO)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, tp.getCdTipoPagamento());
            pstmt.setString(2, tp.getNomeTipoPagamento());
            pstmt.setString(3, tp.getPermiteParcelamento());
            pstmt.setString(4, tp.getEmiteBoleto());
            pstmt.setString(5, tp.getEnviarArqBanco());
            pstmt.setString(6, tp.getEnviaCartorio());
            pstmt.setInt(7, tp.getDiasCartorio());
            pstmt.setString(8, tp.getCdPortador());
            pstmt.setDouble(9, tp.getTaxaJuros());
            pstmt.setDouble(10, tp.getTaxaMulta());
            pstmt.setInt(11, tp.getDiasLiquidacao());
            pstmt.setString(12, tp.getUsuarioCadastro());
            pstmt.setString(13, tp.getDataCadastro());
            pstmt.setString(14, tp.getDataModificacao());
            pstmt.setString(15, tp.getSituacao());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Tipo de Pagamento já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Tipo de Pagamento, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }
    
    // método para poder atualizar o registro
    public void atualizar(TipoPagamento tp){
        String sql = "UPDATE GFCTIPOPAGAMENTO SET NOME_TIPO_PAGAMENTO = ?,"
                + "PERMITE_PARCELAMENTO = ?,"
                + "EMITE_BOLETO = ?,"
                + "ENVIA_ARQ_BANCO = ?,"
                + "ENVIA_CARTORIO = ?,"
                + "DIAS_CARTORIO = ?,"
                + "CD_PORTADOR = ?,"
                + "TX_JUROS = ?,"
                + "TX_MULTA = ?,"
                + "DIAS_LIQUIDACAO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + " WHERE CD_TIPOPAGAMENTO = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, tp.getNomeTipoPagamento());
            pstmt.setString(2, tp.getPermiteParcelamento());
            pstmt.setString(3, tp.getEmiteBoleto());
            pstmt.setString(4, tp.getEnviarArqBanco());
            pstmt.setString(5, tp.getEnviaCartorio());
            pstmt.setInt(6, tp.getDiasCartorio());
            pstmt.setString(7, tp.getCdPortador());
            pstmt.setDouble(8, tp.getTaxaJuros());
            pstmt.setDouble(9, tp.getTaxaMulta());
            pstmt.setInt(10, tp.getDiasLiquidacao());
            pstmt.setString(11, tp.getDataModificacao());
            pstmt.setString(12, tp.getSituacao());
            pstmt.setString(13, tp.getCdTipoPagamento());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Erro na atualização do registro!\nErr: "+ex);
        }
    }
    
    // Método para excluir registros
    public void excluir(TipoPagamento tp){
        String sql = "DELETE FROM GFCTIPOPAGAMENTO WHERE CD_TIPOPAGAMENTO = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, tp.getCdTipoPagamento());
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
    public void selecionar(List<TipoPagamento> resultado, String sql) {
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
                        resultado.add(new TipoPagamento(
                                rs.getString("cd_tipopagamento"),
                                rs.getString("nome_tipo_pagamento"),
                                rs.getString("permite_parcelamento"),
                                rs.getString("emite_boleto"),
                                rs.getString("envia_arq_banco"),
                                rs.getString("envia_cartorio"),
                                rs.getInt("dias_cartorio"),
                                rs.getString("cd_portador"),
                                rs.getDouble("tx_juros"),
                                rs.getDouble("tx_multa"),
                                rs.getInt("dias_liquidacao"),
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