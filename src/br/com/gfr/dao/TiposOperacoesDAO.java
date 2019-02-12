/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfr.dao;

import br.com.gfc.dao.*;
import br.com.gfr.modelo.TiposOperacoes;
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
 * @version 0.01beta_0917 created on 08/11/2017
 */
public class TiposOperacoesDAO extends AbstractTableModel {

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private ResultSet rs;
    private ResultSetMetaData metaData;
    private int numLinhas;
    private int pass = 0;
    private boolean statusConexao;

    // método construtor da classe para inicializar a conexão
    public TiposOperacoesDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = this.conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }
    
    // método para poder criar o registro no banco
    public void adicionar(TiposOperacoes top ) {
        String sql = "INSERT INTO GFRTIPOOPERACAO(CD_TIPOOPER, CD_CFOP, NOME_OPERACAO, DESCRICAO_CFOP, TIPO_ESTOQUE,"
                + "ALIQUOTA_PIS, ALIQUOTA_COFINS, ALIQUOTA_SIMPLES, ALIQUOTA_IPI, ALIQUOTA_ICMS, ALIQUOTA_SUFRAMA,"
                + "ALIQUOTA_SIMBAHIA, TRIBUTA_ICMS, TRIBUTA_IPI, TRIBUTA_SUFRAMA, TRIBUTA_SIMBAHIA, BASE_CAL_ICMS_OP,"
                + "ICMS_OP_BASE_RED, MVA, BASE_ICMS_ST_RED, ICMS_CADEIA_SEM_RED, ALIQUOTA_ISS, USUARIO_CADASTRO, DATA_CADASTRO,"
                + " DATA_MODIFICACAO, SITUACAO)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            String cdTipoOper = top.getCdTipoOper();
            cdTipoOper = cdTipoOper.replace(".", "");
            String cdCfop = top.getCdCfop();
            cdCfop = cdCfop.replace(".", "");
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cdTipoOper);
            pstmt.setString(2, cdCfop);
            pstmt.setString(3, top.getNomeTipoOperacao());
            pstmt.setString(4, top.getDescricaoCfop());
            pstmt.setString(5, top.getTipoEstoque());
            pstmt.setDouble(6, top.getAliquotaPis());
            pstmt.setDouble(7, top.getAliquotaCofins());
            pstmt.setDouble(8, top.getAliquotaSimples());
            pstmt.setDouble(9, top.getAliquotaIpi());
            pstmt.setDouble(10, top.getAliquotaIcms());
            pstmt.setDouble(11, top.getAliquotaSuframa());
            pstmt.setDouble(12, top.getAliquotaSimbahia());
            pstmt.setString(13, top.getTributaIcms());
            pstmt.setString(14, top.getTributaIpi());
            pstmt.setString(15, top.getTributaSuframa());
            pstmt.setString(16, top.getTributaSimbahia());
            pstmt.setDouble(17, top.getBaseCalculoIcmsOp());
            pstmt.setDouble(18, top.getIcmsOpBaseRed());
            pstmt.setDouble(19, top.getMva());
            pstmt.setDouble(20, top.getBaseIcmsStRed());
            pstmt.setDouble(21, top.getIcmsCadeiaSemRed());
            pstmt.setDouble(22, top.getAliquotaIss());
            pstmt.setString(23, top.getUsuarioCadastro());
            pstmt.setString(24, top.getDataCadastro());
            pstmt.setString(25, top.getDataModificacao());
            pstmt.setString(26, top.getSituacao());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Tipo de Operação já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Tipo de Operação, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }
    
    // método para poder atualizar o registro
    public void atualizar(TiposOperacoes top){
        String sql = "UPDATE GFRTIPOOPERACAO SET CD_CFOP = ?,"
                + " NOME_OPERACAO = ?,"
                + " DESCRICAO_CFOP = ?,"
                + " TIPO_ESTOQUE = ?,"
                + " ALIQUOTA_PIS = ?,"
                + " ALIQUOTA_COFINS = ?,"
                + " ALIQUOTA_SIMPLES = ?,"
                + " ALIQUOTA_IPI = ?,"
                + " ALIQUOTA_ICMS = ?,"
                + " ALIQUOTA_SUFRAMA = ?,"
                + " ALIQUOTA_SIMBAHIA = ?,"
                + " TRIBUTA_ICMS = ?,"
                + " TRIBUTA_IPI = ?,"
                + " TRIBUTA_SUFRAMA = ?,"
                + " TRIBUTA_SIMBAHIA = ?,"
                + " BASE_CAL_ICMS_OP = ?,"
                + " ICMS_OP_BASE_RED = ?,"
                + " MVA = ?,"
                + " BASE_ICMS_ST_RED = ?,"
                + " ICMS_CADEIA_SEM_RED = ?,"
                + " ALIQUOTA_ISS = ?,"
                + " DATA_MODIFICACAO = ?,"
                + " SITUACAO = ?"
                + " WHERE CD_TIPOOPER = ?";
        try{
            String cdTipoOper = top.getCdTipoOper();
            cdTipoOper = cdTipoOper.replace(".", "");
            String cdCfop = top.getCdCfop();
            cdCfop = cdCfop.replace(".", "");
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, cdCfop);
            pstmt.setString(2, top.getNomeTipoOperacao());
            pstmt.setString(3, top.getDescricaoCfop());
            pstmt.setString(4, top.getTipoEstoque());
            pstmt.setDouble(5, top.getAliquotaPis());
            pstmt.setDouble(6, top.getAliquotaCofins());
            pstmt.setDouble(7, top.getAliquotaSimples());
            pstmt.setDouble(8, top.getAliquotaIpi());
            pstmt.setDouble(9, top.getAliquotaIcms());
            pstmt.setDouble(10, top.getAliquotaSuframa());
            pstmt.setDouble(11, top.getAliquotaSimbahia());
            pstmt.setString(12, top.getTributaIcms());
            pstmt.setString(13, top.getTributaIpi());
            pstmt.setString(14, top.getTributaSuframa());
            pstmt.setString(15, top.getTributaSimbahia());
            pstmt.setDouble(16, top.getBaseCalculoIcmsOp());
            pstmt.setDouble(17, top.getIcmsOpBaseRed());
            pstmt.setDouble(18, top.getMva());
            pstmt.setDouble(19, top.getBaseIcmsStRed());
            pstmt.setDouble(20, top.getIcmsCadeiaSemRed());
            pstmt.setDouble(21, top.getAliquotaIss());
            pstmt.setString(22, top.getDataModificacao());
            pstmt.setString(23, top.getSituacao());
            pstmt.setString(24, cdTipoOper);
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Registro atualizado com sucesso!");
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Erro na atualização do registro!\nErr: "+ex);
        }
    }
    
    // Método para excluir registros
    public void excluir(TiposOperacoes top){
        String sql = "DELETE FROM GFRTIPOOPERACAO WHERE CD_TIPOOPER = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, top.getCdTipoOper());
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
    public void selecionar(List<TiposOperacoes> resultado, String sql) {
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
                        resultado.add(new TiposOperacoes(
                                rs.getString("cd_tipooper"),
                                rs.getString("cd_cfop"),
                                rs.getString("nome_operacao"),
                                rs.getString("descricao_cfop"),
                                rs.getString("tipo_estoque"),
                                rs.getDouble("aliquota_pis"),
                                rs.getDouble("aliquota_cofins"),
                                rs.getDouble("aliquota_simples"),
                                rs.getDouble("aliquota_ipi"),
                                rs.getDouble("aliquota_icms"),
                                rs.getDouble("aliquota_suframa"),
                                rs.getDouble("aliquota_simbahia"),
                                rs.getString("tributa_icms"),
                                rs.getString("tributa_ipi"),
                                rs.getString("tributa_suframa"),
                                rs.getString("tributa_simbahia"),
                                rs.getDouble("base_cal_icms_op"),
                                rs.getDouble("icms_op_base_red"),
                                rs.getDouble("mva"),
                                rs.getDouble("base_icms_st_red"),
                                rs.getDouble("icms_cadeia_sem_red"),
                                rs.getDouble("aliquota_iss"),
                                rs.getString("usuario_cadastro"),
                                rs.getString("data_cadastro"),
                                rs.getString("data_modificacao"),
                                rs.getString("situacao")));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca do Registro!\nPrograma: TiposOperacoesDAO.java\nErr: " + ex);
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