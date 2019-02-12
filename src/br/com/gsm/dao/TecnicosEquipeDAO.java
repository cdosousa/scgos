/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.dao;

import br.com.fabrica.ConexaoDataBase;
import br.com.gsm.modelo.TecnicosEquipe;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01beta_0917 created on 26/10/2017
 */
public class TecnicosEquipeDAO extends AbstractTableModel {

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    private int numLinhas;
    private int ultSequencia;
    private int pass = 0;
    private boolean statusConexao;

    // método construtor da classe para inicializar a conexão
    public TecnicosEquipeDAO() throws SQLException {
        this.conexao = new ConexaoDataBase().conexao();
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }
    
    // método para poder criar o registro no banco
    public void adicionar(TecnicosEquipe te) {
        String sql = "INSERT INTO GSMTECNICOEQUIPE(CD_EQUIPE, CPF_TECNICO, CD_ESPECIALIDADE, PAGAR_INDICACAO, PAGAR_OBRA, PAGAR_COMISSAO,"
                + " PERC_OBRA, VALOR_OBRA, PERC_INDICACAO, VALOR_INDICACAO, PERC_COMISSAO, USUARIO_CADASTRO, DATA_CADASTRO, DATA_MODIFICACAO,"
                + " SITUACAO) VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, te.getCdEquipe());
            pstmt.setString(2, te.getCpfTecnico());
            pstmt.setString(3, te.getCdEspecialidade());
            pstmt.setString(4, String.valueOf(te.getPagarIndicacao()));
            pstmt.setString(5, String.valueOf(te.getPagarObra()));
            pstmt.setString(6, String.valueOf(te.getPagarComissao()));
            pstmt.setDouble(7, te.getPercObra());
            pstmt.setDouble(8, te.getValorObra());
            pstmt.setDouble(9, te.getPercIndicacao());
            pstmt.setDouble(10, te.getValorIndicacao());
            pstmt.setDouble(11, te.getPercComissao());
            pstmt.setString(12, te.getUsuarioCadastro());
            pstmt.setString(13, te.getDataCadastro());
            pstmt.setString(14, te.getDataModificacao());
            pstmt.setString(15, String.valueOf(te.getSituacao()));
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "CPF já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro do Tecnico, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }
    
    // método para poder atualizar o registro
    public void atualizar(TecnicosEquipe te){
        String sql = "UPDATE GSMTECNICOEQUIPE SET CD_ESPECIALIDADE = ?,"
                + "PAGAR_INDICACAO = ?,"
                + "PAGAR_OBRA = ?,"
                + "PAGAR_COMISSAO = ?,"
                + "PERC_OBRA = ?,"
                + "VALOR_OBRA = ?,"
                + "PERC_INDICACAO = ?,"
                + "VALOR_INDICACAO = ?,"
                + "PERC_COMISSAO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + "WHERE CPF_TECNICO = ? AND "
                + "CD_EQUIPE = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, te.getCdEspecialidade());
            pstmt.setString(2, String.valueOf(te.getPagarIndicacao()));
            pstmt.setString(3, String.valueOf(te.getPagarObra()));
            pstmt.setString(4, String.valueOf(te.getPagarComissao()));
            pstmt.setDouble(5, te.getPercObra());
            pstmt.setDouble(6, te.getValorObra());
            pstmt.setDouble(7, te.getPercIndicacao());
            pstmt.setDouble(8, te.getValorIndicacao());
            pstmt.setDouble(9, te.getPercComissao());
            pstmt.setString(10, te.getDataModificacao());
            pstmt.setString(11, String.valueOf(te.getSituacao()));
            pstmt.setString(12, te.getCpfTecnico());
            pstmt.setString(13, te.getCdEquipe());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Erro na atualização do registro!\nErr: "+ex);
        }
    }

    // método para selecionar os registros no banco
    public void selecionar(List<TecnicosEquipe> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                DecimalFormat df = new DecimalFormat("#,000.00");
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new TecnicosEquipe(
                                rs.getString("cd_equipe"),
                                rs.getString("cpf_tecnico"),
                                rs.getString("cd_especialidade"),
                                rs.getString("pagar_indicacao").toString().charAt(0),
                                rs.getString("pagar_obra").toString().charAt(0),
                                rs.getString("pagar_comissao").toString().charAt(0),
                                rs.getDouble("perc_obra"),
                                rs.getDouble("valor_obra"),
                                rs.getDouble("perc_indicacao"),
                                rs.getDouble("valor_indicacao"),
                                rs.getDouble("perc_comissao"),
                                rs.getString("usuario_cadastro"),
                                rs.getString("data_cadastro"),
                                rs.getString("data_modificacao"),
                                rs.getString("situacao").toString().charAt(0)));
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
    
    // Método para excluir registros
    public void excluir(TecnicosEquipe te) {
        String sql = "DELETE FROM GSMTECNICOEQUIPE WHERE CPF_TECNICO = ? AND "
                + "CD_EQUIPE = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, te.getCpfTecnico());
            pstmt.setString(2, te.getCdEquipe());
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