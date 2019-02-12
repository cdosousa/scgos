/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.gcv.dao.ContratoSequenciaDAO;
import br.com.gcv.modelo.ContratoSequencia;
import br.com.modelo.SessaoUsuario;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 22/11/2017
 */
public class CContratoSequencia {

    // variáveis de instancia de conexão do usuário
    private Connection conexao;
    private SessaoUsuario su;

    // variáveis de instância dos objetos da classe
    private List<ContratoSequencia> resultado = null;
    private ContratoSequencia regAtual;
    private ContratoSequencia seqCon;
    private ContratoSequenciaDAO csdao;
    private DefaultTableModel modelo = new DefaultTableModel();
    private JTable conteudo;
    private DefaultTableModel itens;
    private int idxAtual;
    private ResultSet rsCon;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CContratoSequencia(Connection conexao, SessaoUsuario su) throws SQLException {
        this.conexao = conexao;
        this.su = su;
        csdao = new ContratoSequenciaDAO(conexao);
        seqCon = new ContratoSequencia();
        seqCon.setProximaSequencia(0);
        modelo.addColumn("Tipo");
        modelo.addColumn("Seq.");
        modelo.addColumn("Seq. Pai");
        modelo.addColumn("Seq Antec.");
        modelo.addColumn("Título");
        modelo.addColumn("Posição Título");
        modelo.addColumn("Possui Texto?");
        modelo.addColumn("Quebrar Linha?");
        modelo.addColumn("Situação");
        conteudo = new JTable(modelo);
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        resultado = new ArrayList<ContratoSequencia>();
        rsCon = csdao.pesquisarSeqquenciaContrato(sql);
        carregarRegistro();
        if (resultado.size() > 0) {
            //mostrarPesquisa(idxAtual);
            numReg = resultado.size();
        } else {
            numReg = 0;
        }
        return numReg;
    }

    // método para selecionar os registros no banco
    public void carregarRegistro() {
        try {
            while (rsCon.next()) {
                try {
                    resultado.add(new ContratoSequencia(
                            rsCon.getString("cd_contrato"),
                            rsCon.getString("cd_sequencia"),
                            rsCon.getString("cd_sequenciapai"),
                            rsCon.getString("tipo_sequencia"),
                            rsCon.getString("cd_seqantecessora"),
                            rsCon.getString("titulo"),
                            rsCon.getString("alinhamento_titulo"),
                            rsCon.getString("possui_texto"),
                            rsCon.getString("quebra_linha"),
                            rsCon.getString("usuario_cadastro"),
                            rsCon.getString("data_cadastro"),
                            rsCon.getString("hora_cadastro"),
                            rsCon.getString("usuario_modificacao"),
                            rsCon.getString("data_modificacao"),
                            rsCon.getString("hora_modificacao"),
                            rsCon.getString("situacao")));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro na busca do ContratoXSequencia!\nErr: " + ex);
                }
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: CContratoSequencia.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nErr: " + ex);
        } finally {
            try {
                rsCon.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // método para prencher a tela com os registros pesquisados
    public ContratoSequencia mostrarPesquisa(int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        String tipoSeq = regAtual.getTipoSequencia();
        switch (tipoSeq) {
            case "01":
                regAtual.setTipoSequencia("01-Qualificação");
                break;
            case "02":
                regAtual.setTipoSequencia("02-Objetivo");
                break;
            case "03":
                regAtual.setTipoSequencia("03-Cláusula");
                break;
            case "04":
                regAtual.setTipoSequencia("04-Parágrafo");
                break;
            case "05":
                regAtual.setTipoSequencia("05-Item Parágrafo");
                break;
            default:
                regAtual.setTipoSequencia(" ");
                break;
        }
        String posicaoTitulo = regAtual.getPosicaoTitulo();
        switch(posicaoTitulo){
            case "C":
                regAtual.setPosicaoTitulo("Centralizado");
                break;
            case "D":
                regAtual.setPosicaoTitulo("Direita");
                break;
            default:
                regAtual.setPosicaoTitulo("Esquerda");
                break;
        }
        String possuTexto = regAtual.getPossuiTexto();
        switch(possuTexto){
            case "S":
                regAtual.setPossuiTexto("Sim");
                break;
            case "N":
                regAtual.setPossuiTexto("Não");
                break;
            default:
                regAtual.setPossuiTexto(" ");
                break;
        }
        String quebraLinha = regAtual.getQuebraLinha();
        switch (quebraLinha) {
            case "S":
                regAtual.setQuebraLinha("Sim");
                break;
            case "N":
                regAtual.setQuebraLinha("Não");
                break;
            default:
                regAtual.setQuebraLinha(" ");
                break;
        }
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "PE":
                regAtual.setSituacao("PE-Pendente");
                break;
            case "AE":
                regAtual.setSituacao("AE-Aguardando Envio");
                break;
            case "AA":
                regAtual.setSituacao("AA-Aguardando Assinatura");
                break;
            case "AS":
                regAtual.setSituacao("AS-Assinado");
                break;
            default:
                regAtual.setSituacao(" ");
                break;
        }
        return regAtual;
    }

    /**
     * Método para carregar a tabela com os itens pesquisados
     *
     * @return DefaultTableModel com itens (Clausulas do contrato)
     */
    public DefaultTableModel carregarItens() {
        if (numReg > 0) {
            for (int i = 0; i < numReg; i++) {
                seqCon = mostrarPesquisa(i);
                itens = (DefaultTableModel) conteudo.getModel();
                itens.addRow(new Object[]{
                    seqCon.getTipoSequencia(),
                    seqCon.getCdSequencia(),
                    seqCon.getCdSequenciaPai(),
                    seqCon.getCdSequenciaAtencessora(),
                    seqCon.getTitulo(),
                    seqCon.getPosicaoTitulo(),
                    seqCon.getPossuiTexto(),
                    seqCon.getQuebraLinha(),
                    seqCon.getSituacao()
                });
                this.seqCon.setProximaSequencia(Integer.parseInt(seqCon.getCdSequencia()));
            }
        }
        return itens;
    }

    /**
     * Método para adicionar nova linha a sequencia do contrato
     *
     * @return DefaultTableModel itens Retorna a tabela com os itens do contrato
     */
    public DefaultTableModel adicionarLinha() {
        if (numReg == 0) {
            itens = (DefaultTableModel) conteudo.getModel();
            itens.addRow(new Object[]{"0", 1, 0, 0, "", "0", "", "0", ""});
        } else {
            itens.addRow(new Object[]{"0", seqCon.getProximaSequencia(), 0, 0, "", "0", "", "0", ""});
            seqCon.setCdSequencia(String.valueOf(seqCon.getProximaSequencia()));
            seqCon.setProximaSequencia(seqCon.getProximaSequencia());
        }
        return itens;
    }

    /**
     * Método para excluir nova linha criada
     *
     * @param linha Recebe o número da linha da tabela para ser excluída
     * @return DefaultTableModel itens Contendo a tabela atualizada
     */
    public DefaultTableModel excluirLinha(int linha) {
        itens.removeRow(linha);
        csdao.excluir(seqCon);
        return itens;
    }

    /**
     *
     * @param seqCon
     * @param linha
     * @param idxTipoSeq
     * @param idxSeq
     * @param idxSeqPai
     * @param idxSeqAntecessora
     * @param idxTitulo
     * @param idxPosicaoTitulo
     * @param idxPossuiTexto
     * @param idxQuebraLinha
     * @param idxSituacao
     * @return
     */
    public DefaultTableModel upNovaLinha(ContratoSequencia seqCon, int linha, int idxTipoSeq, int idxSeq, int idxSeqPai, int idxSeqAntecessora, int idxTitulo, int idxPosicaoTitulo, int idxPossuiTexto, int idxQuebraLinha, int idxSituacao) {
        itens.setValueAt(seqCon.getTipoSequencia(), linha, idxTipoSeq);
        itens.setValueAt(seqCon.getCdSequencia(), linha, idxSeq);
        itens.setValueAt(seqCon.getCdSequenciaPai(), linha, idxSeqPai);
        itens.setValueAt(seqCon.getCdSequenciaAtencessora(), linha, idxSeqAntecessora);
        itens.setValueAt(seqCon.getTitulo(), linha, idxTitulo);
        itens.setValueAt(seqCon.getPosicaoTitulo(), linha, idxPosicaoTitulo);
        itens.setValueAt(seqCon.getPossuiTexto(), linha, idxPossuiTexto);
        itens.setValueAt(seqCon.getQuebraLinha(), linha, idxQuebraLinha);
        itens.setValueAt(seqCon.getSituacao(), linha, idxSituacao);
        return itens;
    }
}