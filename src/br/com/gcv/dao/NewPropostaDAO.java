/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.dao;

import br.com.gcv.modelo.ItemProposta;
import br.com.gcv.modelo.LocalProposta;
import br.com.gcv.modelo.Proposta;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Método unificado para gravar os dados da Proposta Comercial no sistema, com
 * controle transacional
 *
 * @author Cristiano de Oliveira Sousa
 * @version 20180113_alpha criado em 13/01/2008
 * @param conexao
 */
public class NewPropostaDAO {

    // Variáveis de instância
    private Connection conexao;
    private PreparedStatement stmtPro;
    private PreparedStatement stmtLpr;
    private PreparedStatement stmtItp;
    private ResultSet rsPro;
    private ResultSet rsLpr;
    private ResultSet rsItp;
    private int numLinha;
    private int ultSeqLocal;
    private int ultSeqItem;
    private boolean statusConexao;

    // Construtor Padrão da Classe
    public NewPropostaDAO(Connection conexao) {
        this.conexao = conexao;
        statusConexao = true;
    }

    /**
     * Método para criar uma nova proposta completa no sistema
     *
     * @param pro Objeto contendo a proposta a ser criada
     * @param ltr Objeto contendo a array de Locais da proposta
     * @param itr Objeto contendo a arrau de Itens da Proposta
     *
     */
    public void criarProposta(Proposta pro, List<LocalProposta> lpr, List<ItemProposta> itp) {
        try {
            String cdRevisao = pro.getCdRevisao();
            //conexao.setAutoCommit(false);
            adicionarProposta(pro);
            for (int i = 0; i < lpr.size(); i++) {
                LocalProposta resultado = lpr.get(i);
                /*
                JOptionPane.showMessageDialog(null, "Gravando Local!\nNum. Interacao: " + i
                        + "\nProposta: " + resultado.getCdProposta()
                        + "\nSequencia: " + resultado.getCdLocal()
                        + "\nNome Local: " + resultado.getNomeLocal());
                 */
                resultado.setCdRevisao(pro.getCdRevisao());
                adicionarLocalProposta(resultado);
            }
            for (int j = 0; j < itp.size(); j++) {
                ItemProposta resultadoItem = itp.get(j);
                /*
                JOptionPane.showMessageDialog(null, "Gravando Item da Proposta!\nNum. Interacao: " + j
                        + "\nProposta: " + resultadoItem.getCdProposta()
                        + "\nSequencia: " + resultadoItem.getCdLocal()
                        + "\nMaterial: " + resultadoItem.getCdMaterial());
                 */
                resultadoItem.setCdRevisao(pro.getCdRevisao());
                adicionarItemProposta(resultadoItem);
            }
            stmtPro.close();
            stmtLpr.close();
            stmtItp.close();
            //conexao.commit();
        } catch (Exception exsql) {
            /*
            try {
                conexao.rollback();
            } catch (SQLException ex) {
                Logger.getLogger(NewPropostaDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
             */
            JOptionPane.showMessageDialog(null, "Erro na inclusão da proposta!\nErr:" + exsql);
        }
    }

    /**
     * Método para adicionar a Proposta Comercial no sistema.
     *
     * @param pro
     */
    private void adicionarProposta(Proposta pro) {
        String sql = "INSERT INTO GCVPROPOSTA(CD_PROPOSTA,"
                + "CD_REVISAO,"
                + "CD_ATENDIMENTO,"
                + "CD_VISTORIA,"
                + "CD_VENDEDOR,"
                + "CD_TECNICO,"
                + "NOME_CLIENTE,"
                + "TIPO_PESSOA,"
                + "TELEFONE,"
                + "CELULAR,"
                + "EMAIL,"
                + "TIPO_LOGRADOURO,"
                + "LOGRADOURO,"
                + "NUMERO,"
                + "COMPLEMENTO,"
                + "BAIRRO,"
                + "CD_MUNICIPIO_IBGE,"
                + "SIGLA_UF,"
                + "CEP,"
                + "TIPO_ENDERECO,"
                + "TIPO_PEDIDO,"
                + "CD_TIPOPAGAMENTO,"
                + "CD_CONDPAG,"
                + "CD_PEDIDO,"
                + "VALOR_SERVICOS,"
                + "VALOR_PRODUTOS,"
                + "VALOR_ADICIONAIS,"
                + "VALOR_DESCONTOS,"
                + "VALOR_OUTROS_DESCONTOS,"
                + "VALOR_TOTAL_BRUTO,"
                + "VALOR_TOTAL_LIQUIDO,"
                + "PRAZO_EXECUCAO,"
                + "OBS,"
                + "DATA_ENVIO,"
                + "USUARIO_CADASTRO,"
                + "DATA_CADASTRO,"
                + "HORA_CADASTRO,"
                + "USUARIO_MODIFICACAO,"
                + "DATA_MODIFICACAO,"
                + "HORA_MODIFICACAO,"
                + "SITUACAO) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String telefone = pro.getTelefone();
        telefone = telefone.replace("(", "");
        telefone = telefone.replace(")", "");
        telefone = telefone.replace("-", "");
        telefone = telefone.replace(" ", "");
        String celular = pro.getCelular();
        celular = celular.replace("(", "");
        celular = celular.replace(")", "");
        celular = celular.replace("-", "");
        celular = celular.replace(" ", "");
        String cep = pro.getCdCep();
        cep = cep.replace("-", "");
        if (pro.getValorProdutos() > 0) {
            if (pro.getValorServico() > 0) {
                pro.setTipoPedido("A");
            } else {
                pro.setTipoPedido("R");
            }
        } else if (pro.getValorServico() > 0) {
            pro.setTipoPedido("S");
        } else {
            pro.setTipoPedido(" ");
        }
        try {
            stmtPro = conexao.prepareStatement(sql);
            stmtPro.setString(1, pro.getCdProposta());
            stmtPro.setString(2, pro.getCdRevisao());
            stmtPro.setString(3, pro.getCdAtendimento());
            stmtPro.setString(4, pro.getCdVistoria());
            stmtPro.setString(5, pro.getCdVendedor());
            stmtPro.setString(6, pro.getCdTecnico());
            stmtPro.setString(7, pro.getNomeRazaoSocial());
            stmtPro.setString(8, pro.getTipoPessoa());
            stmtPro.setString(9, telefone);
            stmtPro.setString(10, celular);
            stmtPro.setString(11, pro.getEmail());
            stmtPro.setString(12, pro.getTipoLogradouro());
            stmtPro.setString(13, pro.getLogradouro());
            stmtPro.setString(14, pro.getNumero());
            stmtPro.setString(15, pro.getComplemento());
            stmtPro.setString(16, pro.getBairro());
            stmtPro.setString(17, pro.getCdMunicipioIbge());
            stmtPro.setString(18, pro.getSiglaUf());
            stmtPro.setString(19, cep);
            stmtPro.setString(20, pro.getTipoEndereco());
            stmtPro.setString(21, pro.getTipoPedido());
            stmtPro.setString(22, pro.getCdTipoPagamento());
            stmtPro.setString(23, pro.getCdCondPagamento());
            stmtPro.setString(24, pro.getCdPedido());
            stmtPro.setDouble(25, pro.getValorServico());
            stmtPro.setDouble(26, pro.getValorProdutos());
            stmtPro.setDouble(27, pro.getValorAdicionais());
            stmtPro.setDouble(28, pro.getValorDescontos());
            stmtPro.setDouble(29, pro.getValorOutrosDescontos());
            stmtPro.setDouble(30, pro.getValorTotalBruto());
            stmtPro.setDouble(31, pro.getValorTotalLiquido());
            stmtPro.setString(32, pro.getPrazoExecucao());
            stmtPro.setString(33, pro.getObs());
            stmtPro.setString(34, pro.getDataEnvio());
            stmtPro.setString(35, pro.getUsuarioCadastro());
            stmtPro.setString(36, pro.getDataCadastro());
            stmtPro.setString(37, pro.getHoraCadastro());
            stmtPro.setString(38, pro.getUsuarioModificacao());
            stmtPro.setString(39, pro.getDataModificacao());
            stmtPro.setString(40, pro.getHoraModificacao());
            stmtPro.setString(41, pro.getSituacao());
            stmtPro.execute();
            //JOptionPane.showMessageDialog(null, "Proposta gravada com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Numero de Proposta já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação da Proposta, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }

    /**
     * Método para atualizar a Proposta aprovado pelo cliente no sistema. Este módulo aprova a Proposta Corrente
     * e fecha as demais revisões desta proposta.
     * @param pro Objeto contendo a proposta a ser aprovada.
     */
    public void atualizarProposta(Proposta pro) {
        String sql = "UPDATE GCVPROPOSTA SET SITUACAO = ?,"
                + "USUARIO_MODIFICACAO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "HORA_MODIFICACAO = ?"
                + " WHERE CD_PROPOSTA = ?"
                + " AND CD_REVISAO = ?";
        try {
            stmtPro = conexao.prepareStatement(sql);
            stmtPro.setString(1, pro.getSituacao());
            stmtPro.setString(2, pro.getUsuarioModificacao());
            stmtPro.setString(3, pro.getDataModificacao());
            stmtPro.setString(4, pro.getHoraModificacao());
            stmtPro.setString(5, pro.getCdProposta());
            stmtPro.setString(6, pro.getCdRevisao());
            stmtPro.execute();
            stmtPro.close();
            fecharProposta(pro);
            //JOptionPane.showMessageDialog(null, "Proposta gravada com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização da Proposta!\nErr: " + ex);
        }
    }
    /**
     * Método criado para fechar as revisões das propostas que o cliente não aprovou.
     * @param pro 
     */
    private void fecharProposta(Proposta pro) {
        String sql = "UPDATE GCVPROPOSTA SET SITUACAO = ?,"
                + "USUARIO_MODIFICACAO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "HORA_MODIFICACAO = ?"
                + " WHERE CD_PROPOSTA = ?"
                + " AND CD_REVISAO <> ?";
        try {
            stmtPro = conexao.prepareStatement(sql);
            stmtPro.setString(1, "NF");
            stmtPro.setString(2, pro.getUsuarioModificacao());
            stmtPro.setString(3, pro.getDataModificacao());
            stmtPro.setString(4, pro.getHoraModificacao());
            stmtPro.setString(5, pro.getCdProposta());
            stmtPro.setString(6, pro.getCdRevisao());
            stmtPro.execute();
            stmtPro.close();
            //JOptionPane.showMessageDialog(null, "Proposta gravada com sucesso!");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na atualização da Proposta!\nErr: " + ex);
        }
    }

    /**
     * Método para atualizar a Proposta Comercial e o Local da Proposta
     * comercial. Neste método serão atualizados o registro Local da Proposta
     * bem como o Registro Proposta Comercial. A atualização será feita de Filho
     * para Pai.
     *
     * @param pro
     * @param lpr
     *
     */
    public void atualizarProposta(Proposta pro, LocalProposta ltr) {

    }

    /**
     * Método para atualizar a Proposta Comercial, o Local da Proposta e o Item
     * do Local da Proposta Comercial. Os registros serão atualizados da
     * sequencia de filho para pai.
     *
     * @param pro
     * @param lpr
     * @param itp
     */
    public void atualizarProposta(Proposta pro, LocalProposta ltr, ItemProposta itp) {

    }

    /**
     * Método para excluir a Proposta Comercial.
     *
     * @param pro
     * @param sql
     */
    public void excluirProposta(Proposta pro) {
        String sql = "DELETE FROM GCVPROPOSTA WHERE CD_PROPOSTA = ? AND CD_REVISAO = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, pro.getCdProposta());
            pstmt.setString(2, pro.getCdRevisao());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Proposta excluída com sucesso!");
        } catch (Exception ex) {
            if (ex.getMessage().contains("CONSTRAINT")) {
                JOptionPane.showMessageDialog(null, "Esta Proposta já está sendo usada, exclusão não permitida!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na exclusão da Proposta!\nErr: " + ex);
            }
        }
    }

    /**
     * Método para fazer pesquisar a Proposta Comericial e retornar o resultada
     * da pesquisa em uma arraylist.
     *
     * @param resultado
     * @param sql Recebe uma String contendo o Select para o banco de dados.
     * @return rsPro Retorna o resultSet contendo o conteúda da Select executada
     * no banco.
     */
    public ResultSet pesquisarProposta(String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                stmtPro = conexao.prepareStatement(sql);
                rsPro = stmtPro.executeQuery(sql);
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: NewPropostaDAO.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nPrograma: NewPropostaDAO.java\nErr: " + ex);
        }
        return rsPro;
    }

    /**
     * Método para adicionar os Locais da Proposta no sistema.
     *
     * @param lpr
     */
    private void adicionarLocalProposta(LocalProposta lpr) {
        String sql = "INSERT INTO GCVLOCALPROPOSTA(CD_PROPOSTA,"
                + "CD_REVISAO,"
                + "CD_LOCAL,"
                + "CD_LOCAL_ATEND,"
                + "NOME_LOCAL,"
                + "METRAGE_AREA,"
                + "PERC_PERDA,"
                + "TIPO_PISO,"
                + "TIPO_RODAPE,"
                + "METRAGEM_RODAPE,"
                + "LARGURA,"
                + "COMPRIMENTO,"
                + "ESPESSURA,"
                + "TINGIMENTO,"
                + "CLAREAMENTO,"
                + "CD_TIPOVERNIZ,"
                + "CD_ESSENCIA,"
                + "VALOR_SERVICOS,"
                + "VALOR_PRODUTOS,"
                + "VALOR_ADICIONAIS,"
                + "VALOR_DESCONTOS,"
                + "VALOR_OUTROS_DESCONTOS,"
                + "VALOR_TOTAL_BRUTO,"
                + "VALOR_TOTAL_LIQUIDO,"
                + "OBS_LOCAL,"
                + "USUARIO_CADASTRO,"
                + "DATA_CADASTRO,"
                + "HORA_CADASTRO,"
                + "USUARIO_MODIFICACAO,"
                + "DATA_MODIFICACAO,"
                + "HORA_MODIFICACAO,"
                + "SITUACAO) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            stmtLpr = conexao.prepareStatement(sql);
            stmtLpr.setString(1, lpr.getCdProposta());
            stmtLpr.setString(2, lpr.getCdRevisao());
            stmtLpr.setInt(3, lpr.getCdLocal());
            stmtLpr.setInt(4, lpr.getCdLocalAtend());
            stmtLpr.setString(5, lpr.getNomeLocal());
            stmtLpr.setDouble(6, lpr.getMetragemArea());
            stmtLpr.setDouble(7, lpr.getPercPerda());
            stmtLpr.setString(8, lpr.getTipoPiso());
            stmtLpr.setString(9, lpr.getTipoRodape());
            stmtLpr.setDouble(10, lpr.getMetragemRodape());
            stmtLpr.setDouble(11, lpr.getLargura());
            stmtLpr.setString(12, lpr.getComprimento());
            stmtLpr.setDouble(13, lpr.getEspessura());
            stmtLpr.setString(14, lpr.getTingimento());
            stmtLpr.setString(15, lpr.getClareamento());
            stmtLpr.setString(16, lpr.getCdTipolVerniz());
            stmtLpr.setString(17, lpr.getCdEssencia());
            stmtLpr.setDouble(18, lpr.getValorServico());
            stmtLpr.setDouble(19, lpr.getValorProdutos());
            stmtLpr.setDouble(20, lpr.getValorAdicionais());
            stmtLpr.setDouble(21, lpr.getValorDescontos());
            stmtLpr.setDouble(22, lpr.getValorOutrosDescontos());
            stmtLpr.setDouble(23, lpr.getValorTotalBruto());
            stmtLpr.setDouble(24, lpr.getValorTotalLiquido());
            stmtLpr.setString(25, lpr.getObs());
            stmtLpr.setString(26, lpr.getUsuarioCadastro());
            stmtLpr.setString(27, lpr.getDataCadastro());
            stmtLpr.setString(28, lpr.getHoraCadastro());
            stmtLpr.setString(29, lpr.getUsuarioModificacao());
            stmtLpr.setString(30, lpr.getDataModificacao());
            stmtLpr.setString(31, lpr.getHoraModificacao());
            stmtLpr.setString(32, lpr.getSituacao());
            stmtLpr.execute();
            //JOptionPane.showMessageDialog(null, "Ambiente gravado com sucesso!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Ambiente já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Ambiente, informe o administrador do sistema!\nErr: " + ex);
            }
        }
    }

    /**
     * Método para atualizar os Locais da Proposta no sistema.
     *
     * @param lpr
     */
    private void atualizarLocalProposta(LocalProposta lpr) {

    }

    /**
     * Método para excluir os Locais da Proposta no sistema.
     *
     * @param lpr
     * @param sql
     */
    public void excluirLocalProposta(LocalProposta lpr) {
        String sql = "DELETE FROM GCVLOCAlPROPOSTA WHERE CD_PROPOSTA = ? AND CD_REVISAO = ? AND CD_LOCAL = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, lpr.getCdProposta());
            pstmt.setString(2, lpr.getCdRevisao());
            pstmt.setInt(3, lpr.getCdLocal());
            pstmt.execute();
            pstmt.close();
            JOptionPane.showMessageDialog(null, "Ambiente excluído com sucesso!");
        } catch (Exception ex) {
            if (ex.getMessage().contains("CONSTRAINT")) {
                JOptionPane.showMessageDialog(null, "Este Ambiente já está sendo usado, exclusão não permitida!");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na exclusão do Ambiente!\nErr: " + ex);
            }
        }
    }

    /**
     * Método para pesquisar os locais da proposta no banco e retornar em uma
     * arraylist.
     *
     * @param resultado
     * @param sql
     */
    public ResultSet pesquisarLocalProposta(String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                stmtLpr = conexao.prepareStatement(sql);
                rsLpr = stmtLpr.executeQuery(sql);
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: LocalPropostaDAO.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nPrograma: LocalPropostaDAO.java\nErr: " + ex);
        }
        return rsLpr;
    }

    /**
     * Método para adicionar os itens da Proposta Comercial.
     *
     * @param itp
     */
    private void adicionarItemProposta(ItemProposta itp) {
        String sql = "INSERT INTO GCVITEMPROPOSTA(CD_PROPOSTA,"
                + "CD_REVISAO,"
                + "CD_LOCAL,"
                + "SEQUENCIA,"
                + "SEQUENCIA_ATEND,"
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
                + "USUARIO_CADASTRO,"
                + "DATA_CADASTRO,"
                + "HORA_CADASTRO,"
                + "USUARIO_MODIFICACAO,"
                + "DATA_MODIFICACAO,"
                + "HORA_MODIFICACAO,"
                + "SITUACAO) "
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            stmtItp = conexao.prepareStatement(sql);
            stmtItp.setString(1, itp.getCdProposta());
            stmtItp.setString(2, itp.getCdRevisao());
            stmtItp.setInt(3, itp.getCdLocal());
            stmtItp.setInt(4, itp.getSequencia());
            stmtItp.setInt(5, itp.getSequenciaAtend());
            stmtItp.setString(6, itp.getCdMaterial());
            stmtItp.setString(7, itp.getCdUnidmedida());
            stmtItp.setDouble(8, itp.getQuantidade());
            stmtItp.setDouble(9, itp.getValorUnitBruto());
            stmtItp.setDouble(10, itp.getValorUnitLiquido());
            stmtItp.setDouble(11, itp.getPercDesconto());
            stmtItp.setDouble(12, itp.getValorDescontos());
            stmtItp.setDouble(13, itp.getValorTotalItemBruto());
            stmtItp.setDouble(14, itp.getValorTotalItemLiquido());
            stmtItp.setString(15, itp.getTipoItem());
            stmtItp.setString(16, itp.getObsItem());
            stmtItp.setString(17, itp.getUsuarioCadastro());
            stmtItp.setString(18, itp.getDataCadastro());
            stmtItp.setString(19, itp.getHoraCadastro());
            stmtItp.setString(20, itp.getUsuarioModificacao());
            stmtItp.setString(21, itp.getDataModificacao());
            stmtItp.setString(22, itp.getHoraModificacao());
            stmtItp.setString(23, itp.getSituacao());
            stmtItp.execute();
            //JOptionPane.showMessageDialog(null, "Item Gravado!");
        } catch (SQLException ex) {
            if (ex.getMessage().contains("Duplicate")) {
                JOptionPane.showMessageDialog(null, "Item já cadastrado");
            } else {
                JOptionPane.showMessageDialog(null, "Erro na gravação do Item, informe o administrador do sistema!\nErr: " + ex);
                ex.printStackTrace();
            }
        }
    }

    /**
     * Método para atualizar o item da Proposta Comercial
     *
     * @param itp
     */
    private void atualizarItemProposta(ItemProposta itp) {

    }

    /**
     * Método para excluir o item da proposta.
     *
     * @param itp
     * @param sql
     */
    public void excluirItemProposta(ItemProposta itp) {
        String sql = "DELETE FROM GCVITEMPROPOSTA WHERE CD_PROPOSTA = ? AND CD_REVISAO = ? AND CD_LOCAL = ? AND SEQUENCIA = ?";
        try {
            PreparedStatement pstmt = conexao.prepareStatement(sql);
            pstmt.setString(1, itp.getCdProposta());
            pstmt.setString(2, itp.getCdRevisao());
            pstmt.setInt(3, itp.getCdLocal());
            pstmt.setInt(4, itp.getSequencia());
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
     * Método para pesquisar os itens da proposta e retorna em uma arraylist.
     *
     * @param resultado
     * @param sql
     */
    public ResultSet pesquisarItemProposta(String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                stmtItp = conexao.prepareStatement(sql);
                rsItp = stmtItp.executeQuery(sql);
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: ItemPropostaDAO.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nPrograma: ItemPropostaDAO.java\nErr: " + ex);
        }
        return rsItp;
    }

    /**
     * Método para atualizar os totais da Proposta Comercial.
     *
     * @param resultado
     * @param sql
     */
    public void totalizarItens(List<Proposta> resultado, String sql) {
        try {
            if (!statusConexao) {
                throw new IllegalStateException("Sem conexão com o banco de dados");
            } else {
                PreparedStatement pstmt = conexao.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery(sql);
                while (rs.next()) {
                    try {
                        resultado.add(new Proposta(
                                rs.getString("Proposta"),
                                rs.getString("Revisao"),
                                rs.getDouble("Total Produtos"),
                                rs.getDouble("Total Serviços"),
                                rs.getDouble("Adicionais"),
                                rs.getDouble("Descontos"),
                                rs.getDouble("Outros Descontos"),
                                rs.getDouble("Total Proposta Bruto"),
                                rs.getDouble("Total Proposta Liquido")
                        ));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca do Item!\nPrograma: ItemPropostaDAO.java\nErr: " + ex);
                    }
                }
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: PropostaDAO.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nPrograma: PropostaDAO.java\nErr: " + ex);
        }
    }
}
