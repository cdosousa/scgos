/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.dao;

import br.com.gfc.modelo.ContasBancarias;
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
 * @version 0.01beta_0917 created on 06/11/2017
 */
public class ContasBancariasDAO extends AbstractTableModel {

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    private int numLinhas;
    private int pass = 0;
    private boolean statusConexao;

    // método construtor da classe para inicializar a conexão
    public ContasBancariasDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }
    
    // método para poder criar o registro no banco
    public void adicionar(ContasBancarias cb) {
        String sql = "INSERT INTO GFCCONTAS(CD_BANCO, CD_AGENCIA, CD_AGENCIA_DIG, TIPO_CONTA, CD_CONTA,"
                + "CD_CONTA_DIG, LIMITE, SALDO, TARIFA_ADM, TX_JUROS, DATA_ABERTURA, DATA_ENCERRAMENTO,"
                + "USUARIO_CADASTRO, DATA_CADASTRO, DATA_MODIFICACAO, SITUACAO)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cb.getCdBanco());
            pstmt.setString(2, cb.getCdAgencia());
            pstmt.setString(3, cb.getCdAgenciaDig());
            pstmt.setString(4, cb.getTipoConta());
            pstmt.setString(5, cb.getCdConta());
            pstmt.setString(6, cb.getCdContaDig());
            pstmt.setDouble(7, cb.getLimite());
            pstmt.setDouble(8, cb.getSaldo());
            pstmt.setDouble(9, cb.getTarifaAdm());
            pstmt.setDouble(10, cb.getTaxaJuros());
            pstmt.setString(11, cb.getDataAbertura());
            pstmt.setString(12, cb.getDataEncerrametno());
            pstmt.setString(13, cb.getUsuarioCadastro());
            pstmt.setString(14, cb.getDataCadastro());
            pstmt.setString(15, cb.getDataModificacao());
            pstmt.setString(16, cb.getSituacao());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Código de conta já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação da conta, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }
    
    // método para poder atualizar o registro
    public void atualizar(ContasBancarias cb){
        String sql = "UPDATE GFCCONTAS SET CD_AGENCIA_DIG = ?,"
                + "TIPO_CONTA = ?,"
                + "CD_CONTA_DIG = ?,"
                + "LIMITE = ?,"
                + "SALDO = ?,"
                + "TARIFA_ADM = ?,"
                + "TX_JUROS = ?,"
                + "DATA_ABERTURA = ?,"
                + "DATA_ENCERRAMENTO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + "WHERE CD_BANCO = ? AND CD_AGENCIA = ? AND CD_CONTA = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cb.getCdAgenciaDig());
            pstmt.setString(2, cb.getTipoConta());
            pstmt.setString(3, cb.getCdContaDig());
            pstmt.setDouble(4, cb.getLimite());
            pstmt.setDouble(5, cb.getSaldo());
            pstmt.setDouble(6, cb.getTarifaAdm());
            pstmt.setDouble(7, cb.getTaxaJuros());
            pstmt.setString(8, cb.getDataAbertura());
            pstmt.setString(9, cb.getDataEncerrametno());
            pstmt.setString(10, cb.getDataModificacao());
            pstmt.setString(11, cb.getSituacao());
            pstmt.setString(12, cb.getCdBanco());
            pstmt.setString(13, cb.getCdAgencia());
            pstmt.setString(14, cb.getCdConta());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Erro na atualização do registro!\nErr: "+ex);
        }
    }
    
    // Método para excluir registros
    public void excluir(ContasBancarias cb){
        String sql = "DELETE FROM GFCCONTAS WHERE CD_BANCO = ? AND CD_AGENCIA = ? AND CD_CONTA = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cb.getCdBanco());
            pstmt.setString(2, cb.getCdAgencia());
            pstmt.setString(3, cb.getCdConta());
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
    public void selecionar(List<ContasBancarias> resultado, String sql) {
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
                        resultado.add(new ContasBancarias(
                                rs.getString("cd_banco"),
                                rs.getString("cd_agencia"),
                                rs.getString("cd_agencia_dig"),
                                rs.getString("tipo_conta"),
                                rs.getString("cd_conta"),
                                rs.getString("cd_conta_dig"),
                                rs.getDouble("limite"),
                                rs.getDouble("saldo"),
                                rs.getDouble("tarifa_adm"),
                                rs.getDouble("tx_juros"),
                                rs.getString("data_abertura"),
                                rs.getString("data_encerramento"),
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