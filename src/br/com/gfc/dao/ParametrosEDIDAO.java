/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.dao;

import br.com.gfc.modelo.ParametrosEDI;
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
 * @version 0.01beta_0917 created on 26/11/2018
 */
public class ParametrosEDIDAO{

    //variáveis de instância
    private final Connection conexao;
    private final Statement stmt;
    private ResultSet rs;
    //private ResultSetMetaData metaData;
    private int numLinhas;
    private int pass = 0;
    private boolean statusConexao;

    // método construtor da classe para inicializar a conexão
    public ParametrosEDIDAO(Connection conexao) throws SQLException {
        this.conexao = conexao;
        stmt = conexao.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        statusConexao = true;
    }
    
    // método para poder criar o registro no banco
    public int adicionar(ParametrosEDI pedi) {
        String sql = "INSERT INTO GFCPARAMETROSEDI(CD_TIPOPAGAMENTO, CD_CODIGOBENEFICIARIO, NUMERO_SEQUENCIAL, SITUACAO_EDI, TIPO_ARQUIVO, CD_TIPOSERVICO,"
                + "USAR_VERSAO, VERSAO_LAYOUT, USAR_BOLETO_PERSON, CD_BOLETOPERSON, CD_CARTEIRA, TP_EMISSAO_BOLETO, TP_ENTREGA_BOLETO, CD_JUROS_MORA, TP_JUROS_MORA_DIA_TAXA,"
                + "CD_DESCONTO, CD_BAIXA_DEVOLUCAO, QTDE_DIAS_BAIXA_DEVOL, MENSAGEM1, MENSAGEM2, USUARIO_CADASTRO, DATA_CADASTRO, HORA_CADASTRO,"
                + " USUARIO_MODIFICACAO, DATA_MODIFICACAO, HORA_MODIFICACAO, SITUACAO)"
                + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, pedi.getCdTipoPagamento());
            pstmt.setString(2, pedi.getCdCodigoBeneficiario());
            pstmt.setInt(3, pedi.getNumeroSequencial());
            pstmt.setString(4, pedi.getSituacaoEdi());
            pstmt.setString(5, pedi.getTipoArquvo());
            pstmt.setString(6, pedi.getCdTipoServico());
            pstmt.setString(7, pedi.getUsarVersao());
            pstmt.setString(8, pedi.getVersaoLayout());
            pstmt.setString(9, pedi.getUsarBoletoPersonalizado());
            pstmt.setString(10, pedi.getCdBoletoPerson());
            pstmt.setString(11, pedi.getCdCarteira());
            pstmt.setString(12, pedi.getTipoEmissaoBoleto());
            pstmt.setString(13, pedi.getTipoEntregaBoleto());
            pstmt.setString(14, pedi.getCdJurosMora());
            pstmt.setString(15, pedi.getTipoJurosMoraDiaTx());
            pstmt.setString(16, pedi.getCdDesconto());
            pstmt.setString(17, pedi.getCdBaixaDevolucao());
            pstmt.setInt(18, pedi.getQtdeDiasBaixaDevol());
            pstmt.setString(19, pedi.getMensagem1());
            pstmt.setString(20, pedi.getMensagem2());
            pstmt.setString(21, pedi.getUsuarioCadastro());
            pstmt.setString(22, pedi.getDataCadastro());
            pstmt.setString(23, pedi.getHoraCadastro());
            pstmt.setString(24, pedi.getUsuarioModificacao());
            pstmt.setString(25, pedi.getDataModificacao());
            pstmt.setString(26, pedi.getHoraModificacao());
            pstmt.setString(27, pedi.getSituacao());
            pstmt.execute();
            pstmt.close();
            return 1;
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Parametro EDI já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Parametro EDI, informe o administrador do sistema!\nErr: " + ex);
            }
            return 0;
        }
    }
    
    // método para poder atualizar o registro
    public int atualizar(ParametrosEDI pedi){
        String sql = "UPDATE GFCPARAMETROSEDI SET CD_CODIGOBENEFICIARIO = ?,"
                + "NUMERO_SEQUENCIAL = ?,"
                + "SITUACAO_EDI = ?,"
                + "TIPO_ARQUIVO = ?,"
                + "CD_TIPOSERVICO = ?,"
                + "USAR_VERSAO = ?,"
                + "VERSAO_LAYOUT = ?,"
                + "USAR_BOLETO_PERSON = ?,"
                + "CD_BOLETOPERSON = ?,"
                + "CD_CARTEIRA = ?,"
                + "TP_EMISSAO_BOLETO = ?,"
                + "TP_ENTREGA_BOLETO = ?,"
                + "CD_JUROS_MORA = ?,"
                + "TP_JUROS_MORA_DIA_TAXA = ?,"
                + "CD_DESCONTO = ?,"
                + "CD_BAIXA_DEVOLUCAO = ?,"
                + "QTDE_DIAS_BAIXA_DEVOL = ?,"
                + "MENSAGEM1 = ?,"
                + "MENSAGEM2 = ?,"
                + "USUARIO_MODIFICACAO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "HORA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + " WHERE CD_TIPOPAGAMENTO = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, pedi.getCdCodigoBeneficiario());
            pstmt.setInt(2, pedi.getNumeroSequencial());
            pstmt.setString(3, pedi.getSituacaoEdi());
            pstmt.setString(4, pedi.getTipoArquvo());
            pstmt.setString(5, pedi.getCdTipoServico());
            pstmt.setString(6, pedi.getUsarVersao());
            pstmt.setString(7, pedi.getVersaoLayout());
            pstmt.setString(8, pedi.getUsarBoletoPersonalizado());
            pstmt.setString(9, pedi.getCdBoletoPerson());
            pstmt.setString(10, pedi.getCdCarteira());
            pstmt.setString(11, pedi.getTipoEmissaoBoleto());
            pstmt.setString(12, pedi.getTipoEntregaBoleto());
            pstmt.setString(13, pedi.getCdJurosMora());
            pstmt.setString(14, pedi.getTipoJurosMoraDiaTx());
            pstmt.setString(15, pedi.getCdDesconto());
            pstmt.setString(16, pedi.getCdBaixaDevolucao());
            pstmt.setInt(17, pedi.getQtdeDiasBaixaDevol());
            pstmt.setString(18, pedi.getMensagem1());
            pstmt.setString(19, pedi.getMensagem2());
            pstmt.setString(20, pedi.getUsuarioModificacao());
            pstmt.setString(21, pedi.getDataModificacao());
            pstmt.setString(22, pedi.getHoraModificacao());
            pstmt.setString(23, pedi.getSituacao());
            pstmt.setString(24, pedi.getCdTipoPagamento());
            pstmt.execute();
            pstmt.close();
            return 1;
        }catch(Exception ex){
            JOptionPane.showMessageDialog(null,"Erro na atualização do registro!\nErr: "+ex);
            return 0;
        }
    }
    
    // Método para excluir registros
    public void excluir(ParametrosEDI pedi){
        String sql = "DELETE FROM GFCPARAMETROSEDI WHERE CD_TIPOPAGAMENTO = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, pedi.getCdTipoPagamento());
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
    public void selecionar(List<ParametrosEDI> resultado, String sql) {
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
                        resultado.add(new ParametrosEDI(
                                rs.getString("cd_tipopagamento"),
                                rs.getString("cd_codigobeneficiario"),
                                rs.getInt("numero_sequencial"),
                                rs.getString("situacao_edi"),
                                rs.getString("tipo_arquivo"),
                                rs.getString("cd_tiposervico"),
                                rs.getString("usar_versao"),
                                rs.getString("versao_layout"),
                                rs.getString("usar_boleto_person"),
                                rs.getString("cd_boletoperson"),
                                rs.getString("cd_carteira"),
                                rs.getString("tp_emissao_boleto"),
                                rs.getString("tp_entrega_boleto"),
                                rs.getString("cd_juros_mora"),
                                rs.getString("tp_juros_mora_dia_taxa"),
                                rs.getString("cd_desconto"),
                                rs.getString("cd_baixa_devolucao"),
                                rs.getInt("qtde_dias_baixa_devol"),
                                rs.getString("mensagem1"),
                                rs.getString("mensagem2"),
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