/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.dao;

import br.com.gcv.modelo.AcabamentoItemPed;
import br.com.gcv.modelo.ItemPedido;
import br.com.gcv.modelo.Pedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Método unificado para gravar os dados do Pedido Comercial no sistema, com
 * controle transacional
 *
 * @author Cristiano de Oliveira Sousa
 * @version 20180113_alpha criado em 08/02/2008
 * @param conexao
 */
public class PedidoDAO {

    // Variáveis de instância
    private Connection conexao;
    private PreparedStatement stmtPed;
    private PreparedStatement stmtItped;
    private PreparedStatement stmtAcab;
    private ResultSet rsPed;
    private ResultSet rsItped;
    private ResultSet rsAcab;
    private int numLinha;
    private int ultSeqItem;
    private boolean statusConexao;

    // Construtor Padrão da Classe
    public PedidoDAO(Connection conexao) {
        this.conexao = conexao;
        statusConexao = true;
    }

    /**
     * Método para criar apenas o pedido no sistema
     *
     * @param ped Objeto contendo o pedido a ser criado
     */
    public void criarPedido(Pedido ped) {
        try {
            //conexao.setAutoCommit(false);
            adicionarPedido(ped);
            //conexao.commit();
            stmtPed.close();
        } catch (SQLException exsql) {
            try {
                conexao.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(null, "Erro na inclusão do pedido!\nErr:" + exsql);
        }
    }

    /**
     * Método para criar apenas o item do pedido no sistema
     *
     * @param itped Objeto contendo o item do pedido a ser criado
     */
    public void criarItemPedido(ItemPedido itped) {
        try {
            // conexao.setAutoCommit(false);
            adicionarItemPedido(itped);
            // conexao.commit();
            stmtItped.close();
        } catch (SQLException exsql) {
            try {
                conexao.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(PedidoDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
            JOptionPane.showMessageDialog(null, "Erro na inclusão do item do pedido!\nErr:" + exsql);
        }
    }

    /**
     * Método para criar um novo pedido completo no sistema
     *
     * @param ped Objeto contendo o pedido a ser criado
     * @param itped Objeto contendo a array de Itens do Pedido
     *
     */
    public void criarPedido(Pedido ped, List<ItemPedido> itped) {
        try {
            adicionarPedido(ped);
            for (int i = 0; i < itped.size(); i++) {
                ItemPedido resultadoItem = itped.get(i);
                adicionarItemPedido(resultadoItem);
            }
            stmtPed.close();
            stmtItped.close();
        } catch (SQLException exsql) {
            JOptionPane.showMessageDialog(null, "Erro na inclusão do pedido!\nErr:" + exsql);
        }
    }

    /**
     * Método para cria o acabamento do item do pedido
     * @param 
     */
    public void criarAcabamentoItem(AcabamentoItemPed acab) {
        try {
            adicionarAcabamentoItem(acab);
            stmtAcab.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na inclusão do Acabamento!\nErr:" + ex);
        }
    }

    /**
     * Método para adicionar o Pedido Comercial no sistema.
     *
     * @param ped
     */
    private void adicionarPedido(Pedido ped) {
        String sql = "INSERT INTO GCVPEDIDO(CD_PEDIDO,"
                + "CD_PROPOSTA,"
                + "CD_REVISAO,"
                + "CD_VENDEDOR,"
                + "CD_TECNICO,"
                + "CPF_CNPJ,"
                + "TIPO_PEDIDO,"
                + "CD_TIPOPAGAMENTO,"
                + "CD_CONDPAG,"
                + "CD_OPERVENDA,"
                + "VALOR_SERVICOS,"
                + "VALOR_PRODUTOS,"
                + "VALOR_ADICIONAIS,"
                + "VALOR_DESCONTOS,"
                + "VALOR_OUTROS_DESCONTOS,"
                + "VALOR_TOTAL_BRUTO,"
                + "VALOR_TOTAL_LIQUIDO,"
                + "PRAZO_EXECUCAO,"
                + "DATA_INICIO,"
                + "OBS,"
                + "USUARIO_CADASTRO,"
                + "DATA_CADASTRO,"
                + "HORA_CADASTRO,"
                + "USUARIO_MODIFICACAO,"
                + "DATA_MODIFICACAO,"
                + "HORA_MODIFICACAO,"
                + "SITUACAO) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if (ped.getValorProdutos() > 0) {
            if (ped.getValorServico() > 0) {
                ped.setTipoPedido("A");
            } else {
                ped.setTipoPedido("R");
            }
        } else if (ped.getValorServico() > 0) {
            ped.setTipoPedido("S");
        } else {
            ped.setTipoPedido(" ");
        }
        try {
            stmtPed = conexao.prepareStatement(sql);
            stmtPed.setString(1, ped.getCdPedido());
            stmtPed.setString(2, ped.getCdProposta());
            stmtPed.setString(3, ped.getCdRevisao());
            stmtPed.setString(4, ped.getCdVendedor());
            stmtPed.setString(5, ped.getCdTecnico());
            stmtPed.setString(6, ped.getCdCpfCnpj());
            stmtPed.setString(7, ped.getTipoPedido());
            stmtPed.setString(8, ped.getCdTipoPagamento());
            stmtPed.setString(9, ped.getCdCondPagamento());
            stmtPed.setString(10, ped.getCdOperVenda());
            stmtPed.setDouble(11, ped.getValorServico());
            stmtPed.setDouble(12, ped.getValorProdutos());
            stmtPed.setDouble(13, ped.getValorAdicionais());
            stmtPed.setDouble(14, ped.getValorDescontos());
            stmtPed.setDouble(15, ped.getValorOutrosDescontos());
            stmtPed.setDouble(16, ped.getValorTotalBruto());
            stmtPed.setDouble(17, ped.getValorTotalLiquido());
            stmtPed.setString(18, ped.getPrazoExecucao());
            stmtPed.setString(19, ped.getDataInicio());
            stmtPed.setString(20, ped.getObs());
            stmtPed.setString(21, ped.getUsuarioCadastro());
            stmtPed.setString(22, ped.getDataCadastro());
            stmtPed.setString(23, ped.getHoraCadastro());
            stmtPed.setString(24, ped.getUsuarioModificacao());
            stmtPed.setString(25, ped.getDataModificacao());
            stmtPed.setString(26, ped.getHoraModificacao());
            stmtPed.setString(27, ped.getSituacao());
            stmtPed.execute();
            //JOptionPane.showMessageDialog(null, "Pedido gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Numero de Pedido já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Pedido, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }

    /**
     * Método para adicionar os itens no Pedido.
     *
     * @param itped
     */
    private void adicionarItemPedido(ItemPedido itped) {
        String sql = "INSERT INTO GCVITEMPEDIDO(CD_PEDIDO,"
                + "SEQUENCIA,"
                + "CD_MATERIAL,"
                + "CD_UNIDMEDIDA,"
                + "QUANTIDADE,"
                + "VALOR_UNIT_BRUTO,"
                + "VALOR_UNIT_LIQUIDO,"
                + "PERC_DESC,"
                + "VALOR_DESC,"
                + "TOTAL_ITEM_BRUTO,"
                + "TOTAL_ITEM_LIQUIDO,"
                + "TIPO_ITEM,"
                + "OBS_ITEM,"
                + "CD_LOCAL_PROPOSTA,"
                + "SEQUENCIA_PROPOSTA,"
                + "USUARIO_CADASTRO,"
                + "DATA_CADASTRO,"
                + "HORA_CADASTRO,"
                + "USUARIO_MODIFICACAO,"
                + "DATA_MODIFICACAO,"
                + "HORA_MODIFICACAO,"
                + "SITUACAO)"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            stmtItped = conexao.prepareStatement(sql);
            stmtItped.setString(1, itped.getCdPedido());
            stmtItped.setInt(2, itped.getSequencia());
            stmtItped.setString(3, itped.getCdMaterial());
            stmtItped.setString(4, itped.getCdUnidmedida());
            stmtItped.setDouble(5, itped.getQuantidade());
            stmtItped.setDouble(6, itped.getValorUnitBruto());
            stmtItped.setDouble(7, itped.getValorUnitLiquido());
            stmtItped.setDouble(8, itped.getPercDesconto());
            stmtItped.setDouble(9, itped.getValorDescontos());
            stmtItped.setDouble(10, itped.getValorTotalItemBruto());
            stmtItped.setDouble(11, itped.getValorTotalItemLiquido());
            stmtItped.setString(12, itped.getTipoItem());
            stmtItped.setString(13, itped.getObsItem());
            stmtItped.setInt(14, itped.getCdLocal());
            stmtItped.setInt(15, itped.getSequenciaAtend());
            stmtItped.setString(16, itped.getUsuarioCadastro());
            stmtItped.setString(17, itped.getDataCadastro());
            stmtItped.setString(18, itped.getHoraCadastro());
            stmtItped.setString(19, itped.getUsuarioModificacao());
            stmtItped.setString(20, itped.getDataModificacao());
            stmtItped.setString(21, itped.getHoraModificacao());
            stmtItped.setString(22, itped.getSituacao());
            stmtItped.execute();
            //JOptionPane.showMessageDialog(null, "Item Gravado!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Item já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Item, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }

    /**
     * Método para adicionar os itens no Pedido.
     *
     * @param acaitpe Objeto contendo os dados do acabamento
     */
    private void adicionarAcabamentoItem(AcabamentoItemPed acaitpe) {
        String sql = "INSERT INTO GCVACABAMENTOITEMPED(CD_PEDIDO,"
                + "SEQUENCIA,"
                + "CD_MATERIAL,"
                + "USUARIO_CADASTRO,"
                + "DATA_CADASTRO,"
                + "HORA_CADASTRO)"
                + "VALUES(?,?,?,?,?,?)";
        try {
            stmtAcab = conexao.prepareStatement(sql);
            stmtAcab.setString(1, acaitpe.getCdPedido());
            stmtAcab.setInt(2, acaitpe.getSequencia());
            stmtAcab.setString(3, acaitpe.getCdMaterial());
            stmtAcab.setString(4, acaitpe.getUsuarioCadastro());
            stmtAcab.setString(5, acaitpe.getDataCadastro());
            stmtAcab.setString(6, acaitpe.getHoraCadastro());
            stmtAcab.execute();
            JOptionPane.showMessageDialog(null, "Acabamento Gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Acabamento já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Acabamento, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }

    /**
     * Método para atualizar o Pedido no sistema. Este método atualizar apenas o
     * registro PAI do Pedido
     *
     * @param ped Objeto contendo o Pedido a ser adicionado
     *
     */
    public void atualizarPedido(Pedido ped) {
        String sql = "UPDATE GCVPEDIDO SET CD_VENDEDOR = ?,"
                + "CD_TECNICO = ?,"
                + "TIPO_PEDIDO = ?,"
                + "CD_TIPOPAGAMENTO = ?,"
                + "CD_CONDPAG = ?,"
                + "CD_OPERVENDA = ?,"
                + "VALOR_SERVICOS = ?,"
                + "VALOR_PRODUTOS = ?,"
                + "VALOR_ADICIONAIS = ?,"
                + "VALOR_DESCONTOS = ?,"
                + "VALOR_OUTROS_DESCONTOS = ?,"
                + "VALOR_TOTAL_BRUTO = ?,"
                + "VALOR_TOTAL_LIQUIDO = ?,"
                + "PRAZO_EXECUCAO = ?,"
                + "DATA_INICIO = ?,"
                + "OBS = ?,"
                + "USUARIO_MODIFICACAO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "HORA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + " WHERE CD_PEDIDO = ?";
        if (ped.getValorProdutos() > 0) {
            if (ped.getValorServico() > 0) {
                ped.setTipoPedido("A");
            } else {
                ped.setTipoPedido("R");
            }
        } else if (ped.getValorServico() > 0) {
            ped.setTipoPedido("S");
        } else {
            ped.setTipoPedido(" ");
        }
        try {
            stmtPed = conexao.prepareStatement(sql);
            stmtPed.setString(1, ped.getCdVendedor());
            stmtPed.setString(2, ped.getCdTecnico());
            stmtPed.setString(3, ped.getTipoPedido());
            stmtPed.setString(4, ped.getCdTipoPagamento());
            stmtPed.setString(5, ped.getCdCondPagamento());
            stmtPed.setString(6, ped.getCdOperVenda());
            stmtPed.setDouble(7, ped.getValorServico());
            stmtPed.setDouble(8, ped.getValorProdutos());
            stmtPed.setDouble(9, ped.getValorAdicionais());
            stmtPed.setDouble(10, ped.getValorDescontos());
            stmtPed.setDouble(11, ped.getValorOutrosDescontos());
            stmtPed.setDouble(12, ped.getValorTotalBruto());
            stmtPed.setDouble(13, ped.getValorTotalLiquido());
            stmtPed.setString(14, ped.getPrazoExecucao());
            stmtPed.setString(15, ped.getDataInicio());
            stmtPed.setString(16, ped.getObs());
            stmtPed.setString(17, ped.getUsuarioModificacao());
            stmtPed.setString(18, ped.getDataModificacao());
            stmtPed.setString(19, ped.getHoraModificacao());
            stmtPed.setString(20, ped.getSituacao());
            stmtPed.setString(21, ped.getCdPedido());
            stmtPed.execute();
            stmtPed.close();
            JOptionPane.showMessageDialog(null, "Pedido " + ped.getCdPedido() + " atualizado com sucesso!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização do Pedido!\nErr: " + ex);
        }

    }

    /**
     * Método para atualizar o item do Pedido
     *
     * @param itped
     */
    private void atualizarItemPedido(ItemPedido itped) {
        String sql = "UPDATE GCVITEMPEDIDO SET CD_MATERIAL = ?,"
                + "CD_UNIDMEDIDA = ?,"
                + "QUANTIDADE = ?,"
                + "VALOR_UNIT_BRUTO = ?,"
                + "VALOR_UNIT_LIQUIDO = ?,"
                + "PERC_DESC = ?,"
                + "VALOR_DESC = ?,"
                + "TOTAL_ITEM_BRUTO = ?,"
                + "TOTAL_ITEM_LIQUIDO = ?,"
                + "OBS_ITEM = ?,"
                + "USUARIO_MODIFICACAO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "HORA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + " WHERE CD_PEDIDO = ? AND SEQUENCIA = ?";
        try {
            stmtItped = conexao.prepareStatement(sql);
            stmtItped.setString(1, itped.getCdMaterial());
            stmtItped.setString(2, itped.getCdUnidmedida());
            stmtItped.setDouble(3, itped.getQuantidade());
            stmtItped.setDouble(4, itped.getValorUnitBruto());
            stmtItped.setDouble(5, itped.getValorUnitLiquido());
            stmtItped.setDouble(6, itped.getPercDesconto());
            stmtItped.setDouble(7, itped.getValorDescontos());
            stmtItped.setDouble(8, itped.getValorTotalItemBruto());
            stmtItped.setDouble(9, itped.getValorTotalItemLiquido());
            stmtItped.setString(10, itped.getObsItem());
            stmtItped.setString(11, itped.getUsuarioModificacao());
            stmtItped.setString(12, itped.getDataModificacao());
            stmtItped.setString(13, itped.getHoraModificacao());
            stmtItped.setString(14, itped.getSituacao());
            stmtItped.setString(15, itped.getCdPedido());
            stmtItped.setInt(16, itped.getSequencia());
            stmtItped.execute();
            stmtItped.close();
            //JOptionPane.showMessageDialog(null, "Item Atualizado!");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização do Item!\nErr: " + ex);
        }
    }
    
    /**
     * Método para adicionar os itens no Pedido.
     *
     * @param acaitpe Objeto contendo os dados do acabamento
     */
    public void atualizarAcabamentoItem(AcabamentoItemPed acaitpe) {
        String sql = "UPDATE GCVACABAMENTOITEMPED SET CD_MATERIAL = ?,"
                + " USUARIO_MODIFICACAO = ?,"
                + " DATA_MODIFICACAO = ?,"
                + " HORA_MODIFICACAO = ?"
                + " WHERE CD_PEDIDO = ? AND"
                + " SEQUENCIA = ?";
        try {
            stmtAcab = conexao.prepareStatement(sql);
            stmtAcab.setString(1, acaitpe.getCdMaterial());
            stmtAcab.setString(2, acaitpe.getUsuarioModificacao());
            stmtAcab.setString(3, acaitpe.getDataModificacao());
            stmtAcab.setString(4, acaitpe.getHoraModificacao());
            stmtAcab.setString(5, acaitpe.getCdPedido());
            stmtAcab.setInt(6, acaitpe.getSequencia());
            stmtAcab.execute();
            JOptionPane.showMessageDialog(null, "Acabamento Atualizado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Acabamento já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na atualização do Acabamento, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }

    /**
     * Método para atualizar o Pedido e o Item do Local da Proposta Comercial.
     * Os registros serão atualizar da sequencia de filho para pai.
     *
     * @param ped
     * @param itped
     */
    public void atualizarPedido(Pedido ped, ItemPedido itped) {

    }

    /**
     * Método para excluir o Pedido
     *
     * @param ped
     * @param sql
     */
    public void excluirPedido(Pedido ped) {
        String sql = "DELETE FROM GCVPEDIDO WHERE CD_PEDIDO = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, ped.getCdPedido());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Pedido excluído com sucesso!");
        } catch (Exception ex) {
            if (ex.getMessage().contains("CONSTRAINT")) {
                JOptionPane.showMessageDialog(null, "Esta Pedido já está sendo usada, exclusão não permitida!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na exclusão do Pedido!\nErr: " + ex);
            }
        }
    }

    /**
     * Método para excluir o item do Pedido.
     *
     * @param itp
     * @param sql
     */
    public void excluirItemPedido(ItemPedido itped) {
        String sql = "DELETE FROM GCVITEMPEDIDO WHERE CD_PEDIDO = ? AND SEQUENCIA = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, itped.getCdPedido());
            pstmt.setInt(2, itped.getSequencia());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Item excluído com sucesso!");
        } catch (Exception ex) {
            if (ex.getMessage().contains("CONSTRAINT")) {
                JOptionPane.showMessageDialog(null, "Este Item já está sendo usado, exclusão não permitida!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na exclusão do Item!\nErr: " + ex);
            }
        }
    }
    
    /**
     * Método para excluir o acabamento
     * @param acab 
     */
    public void excluirAcabamento(AcabamentoItemPed acab){
        String sql = "DELETE FROM GCVACABAMENTOITEMPED WHERE CD_PEDIDO = ? AND SEQUENCIA = ?";
        try{
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, acab.getCdPedido());
            pstmt.setInt(3, acab.getSequencia());
            pstmt.execute();
            pstmt.close();
        }catch(Exception ex){
            if (ex.getMessage().contains("CONSTRAINT")) {
                JOptionPane.showMessageDialog(null, "Este Acabamento já está sendo usado, exclusão não permitida!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na exclusão do Acabamento!\nErr: " + ex);
            }
        }
    }

    /**
     * Método para pesquisar o Pedido e retornar o resultado da pesquisa em uma
     * arraylist.
     *
     * @param resultado
     * @param sql Recebe uma String contendo o Select para o banco de dados.
     * @return rsPed Retorna o resultSet contendo o conteúda da Select executada
     * no banco.
     */
    public ResultSet pesquisarPedido(String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                stmtPed = conexao.prepareStatement(sql);
                rsPed = stmtPed.executeQuery(sql);
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: PedidoDAO.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nPrograma: PedidoDAO.java\nErr: " + ex);
        }
        return rsPed;
    }

    /**
     * Método para pesquisar os itens do pedido e retorna em uma arraylist.
     *
     * @param resultado
     * @param sql
     */
    public ResultSet pesquisarItemPedido(String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                stmtItped = conexao.prepareStatement(sql);
                rsItped = stmtItped.executeQuery(sql);
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: PedidoDAO.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nPrograma: PedidoDAO.java\nErr: " + ex);
        }
        return rsItped;
    }
    
    /**
     * Método para pesquisar o Acabamento do item do pedido
     * @param sql 
     */
    public ResultSet pesquisarAcabamento(String sql){
        try{
            if(!statusConexao){
                throw new IllegalStateException("Sem conexao com o banco de dados!");
            }else{
                stmtAcab = conexao.prepareStatement(sql);
                rsAcab = stmtAcab.executeQuery(sql);
            }
        }catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: PedidoDAO.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nPrograma: PedidoDAO.java\nErr: " + ex);
        }
        return rsAcab;
    }

    /**
     * Método para atualizar os totais do Pedido.
     *
     * @param resultado
     * @param sql
     */
    public void totalizarItens(List<Pedido> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new Pedido(
                                rs.getString("Pedido"),
                                rs.getDouble("Total Produtos"),
                                rs.getDouble("Total Serviços"),
                                rs.getDouble("Adicionais"),
                                rs.getDouble("Descontos"),
                                rs.getDouble("Outros Descontos"),
                                rs.getDouble("Total Proposta Bruto"),
                                rs.getDouble("Total Proposta Liquido")
                        ));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca do Item!\nPrograma: PedidoDAO.java\nErr: " + ex);
                    }
                }
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: PedidoDAO.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nPrograma: PedidoDAO.java\nErr: " + ex);
        }
    }
}
