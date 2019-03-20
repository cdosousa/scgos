/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.dao;

import br.com.gfc.modelo.EdiOcorrencia;
import br.com.modelo.DataSistema;
import br.com.modelo.HoraSistema;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01beta_0917 created on 13/03/2019
 */
public class EdiOcorrenciaDAO extends AbstractTableModel {

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    private int numLinhas;
    private int pass = 0;
    private boolean statusConexao;
    private Scanner entrada;

    // método construtor da classe para inicializar a conexão
    public EdiOcorrenciaDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }
    
    // método para poder criar o registro no banco
    public int adicionar(EdiOcorrencia eo) {
        String sql = "INSERT INTO GFCEDIOCORRENCIA(CD_BANCO, CD_OCORRENCIA, NOME_OCORRENCIA, LIQUIDAR_TITULOS"
                + ", USUARIO_CADASTRO, DATA_CADASTRO, HORA_CADASTRO, USUARIO_MODIFICACAO, HORA_MODIFICACAO, DATA_MODIFICACAO, SITUACAO)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, eo.getCdBanco());
            pstmt.setString(2, eo.getCdOcorrencia());
            pstmt.setString(3, eo.getNomeOcorrencia());
            pstmt.setString(4, eo.getLiquidarTitulo());
            pstmt.setString(5, eo.getUsuarioCadastro());
            pstmt.setString(6, eo.getDataCadastro());
            pstmt.setString(7, eo.getHoraCadastro());
            pstmt.setString(8, eo.getUsuarioModificacao());
            pstmt.setString(9, eo.getDataModificacao());
            pstmt.setString(10, eo.getHoraModificacao());
            pstmt.setString(11, eo.getSituacao());
            pstmt.execute();
            pstmt.close();
            return 1;
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Código de ocorrência já cadastrado para o banco!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação da ocorrência, informe o administrador do sistema!\nErr: " + ex);
            }
            return 0;
        }
    }
    
    // método para poder atualizar o registro
    public int atualizar(EdiOcorrencia eo){
        String sql = "UPDATE GFCEDIOCORRENCIA SET NOME_OCORRENCIA = ?,"
                + "USUARIO_MODIFICACAO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "HORA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + "WHERE CD_BANCO = ? AND CD_OCORRENCIA = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, eo.getNomeBanco());
            pstmt.setString(2, eo.getUsuarioModificacao());
            pstmt.setString(3, eo.getDataModificacao());
            pstmt.setString(4, eo.getHoraModificacao());
            pstmt.setString(5, eo.getSituacao());
            pstmt.setString(6, eo.getCdBanco());
            pstmt.setString(7, eo.getCdOcorrencia());
            pstmt.execute();
            pstmt.close();
            return 1;
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Erro na atualização do registro!\nErr: "+ex);
            return 0;
        }
    }
    
    // Método para excluir registros
    public void excluir(EdiOcorrencia eo){
        String sql = "DELETE FROM GFCEDIOCORRENCIA WHERE CD_BANCO = ? AND CD_OCORRENCIA = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, eo.getCdBanco());
            pstmt.setString(2, eo.getCdOcorrencia());
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
    public void selecionar(List<EdiOcorrencia> resultado, String sql) {
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
                        resultado.add(new EdiOcorrencia(
                                rs.getString("cd_banco"),
                                rs.getString("cd_ocorrencia"),
                                rs.getString("nome_ocorrencia"),
                                rs.getString("liquidar_titulos"),
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
    
    // método para importar ocorrência a partir de um arquivo
    public void abrirAquivo(EdiOcorrencia eo, String caminhoArquivo, String usuario) throws IOException {
        try {
            DataSistema dat = new DataSistema();
            HoraSistema hs = new HoraSistema();
            String data = null;
            dat.setData(data);
            data = dat.getData();
            entrada = new Scanner(Paths.get(caminhoArquivo));
            int count = 0;
            while (entrada.hasNextLine()) {

                Scanner linha = new Scanner(entrada.nextLine()).useDelimiter(";");
                count++;
                //JOptionPane.showMessageDialog(null,"Estou no While!\nLinha: "+count);
                eo.setCdBanco(linha.next());
                eo.setCdOcorrencia(linha.next().trim());
                eo.setNomeOcorrencia(linha.next());
                eo.setLiquidarTitulo(linha.next().trim());
                eo.setUsuarioCadastro(usuario);
                eo.setDataCadastro(data);
                eo.setHoraCadastro(hs.getHora());
                eo.setSituacao("A");
                linha.close();
                adicionar(eo);
            }
            JOptionPane.showMessageDialog(null, count + " registros atualizados!");
        } catch (FileNotFoundException fnfe) {
            System.err.println("Erro na abertuda do arquivo. Termindando");
            System.exit(1);
        } catch (IOException ioException) {
            System.err.println("Error opening file. Terminating.");
            System.exit(1); // terminate the program
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro Geral ao gravar o Registro!\nErr: " + ex);
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