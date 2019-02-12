/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa: GCVCA0050
 */
package br.com.gcv.visao;

// Objetos do registro Pai - Contrato
import br.com.controle.CBuscarSequencia;
import br.com.controle.CEmpresa;
import br.com.gcv.modelo.Contrato;
import br.com.gcv.dao.ContratoDAO;
import br.com.gcv.controle.CContrato;

// Objetos do registro Filho - Sequencia Contrato
import br.com.gcv.modelo.ContratoSequencia;
import br.com.gcv.dao.ContratoSequenciaDAO;
import br.com.gcv.controle.CContratoSequencia;

// Objetos do registro Neto - Texto do Contrato
import br.com.gcv.modelo.ContratoSeqTexto;
import br.com.gcv.dao.ContratoSeqTextoDAO;
import br.com.gcv.controle.CContratoSeqTexto;

// Objetos do registro Filho - Proposta Contrato
import br.com.gcv.controle.CReportContrato;
import br.com.gcv.modelo.Pedido;

// Objetos de instância de ambiente
import java.sql.Connection;
import br.com.modelo.ParametrosGerais;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoData;
import br.com.modelo.Empresa;
import br.com.modelo.HoraSistema;
import br.com.modelo.VerificarTecla;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_0917beta
 * @created on 27/03/2018
 */
public class ManterContrato extends javax.swing.JFrame {

    /**
     * Variáveis de instância de parâmetros de conexão do usuario
     */
    private static Connection conexao;
    private static SessaoUsuario su;
    private static ParametrosGerais pg;
    private VerificarTecla vt;
    private DataSistema dat;

    /**
     * Variáveis de instância da classe
     */
    private final boolean ISBOTAO = true;
    private String data = null;
    private static String chamador;

    /**
     * Variáveis de instância do registro Pai - Contrato
     */
    private List<Contrato> resultadoContrato;
    private Contrato con;
    private Contrato modcon;
    private ContratoDAO conDAO;
    private CContrato ccon;
    private int numRegCon;
    private int idxCorCon;
    private String sqlCon;
    private String operCon;
    private final String TABELA = "gcvcontrato";

    /**
     * Variáveis de instância do registro Filho - Sequencia Contrato
     */
    private ContratoSequencia seqcon;
    private CContratoSequencia cseqcon;
    private ContratoSequenciaDAO seqconDAO;
    private ContratoSequencia modseqcon;
    private DefaultTableModel seq;

    private int NumRegSeq;
    private int idxCorSeq;
    private String sqlSeq;
    private String operSeq;
    private int seqCon;
    private String seqPai;
    private int linhaSeq;
    private boolean esvaziarTabela = false;
    private boolean inclusao;

    private String zoomCdsequencia;

    /**
     * Variáveis de instância do registro Neto - Sequencia Contrato Texto
     */
    private ContratoSeqTexto cst;
    private ContratoSeqTexto modcst;
    private ContratoSeqTextoDAO cstDAO;
    private CContratoSeqTexto ccst;
    private String operCst;

    /**
     * Variáveis de instância do Objeto pedido
     */
    private Pedido modped;

    /**
     * Varoáveis de instância geral da classe
     */
    private Empresa emp;

    /**
     * Construtor padrão da classe para ser chamado via Menu do Sistema
     *
     * @param su Objeto de Instância do Usuário conectado
     * @param conexao Objeto de Instância de conexão do Usuário
     * @param pg Objeto de Instância dos parâmetros gerais do sistema
     * @param chamador
     */
    public ManterContrato(SessaoUsuario su, Connection conexao, ParametrosGerais pg, String chamador) {
        this.su = su;
        this.conexao = conexao;
        this.pg = pg;
        this.chamador = chamador;
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        monitoraLinhaItens();
        formatarCampos();
        setaContrato();
        setaSeqContrato();
        setaTextoSeqContrato();
        controleBotoes(!ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO);
        sqlCon = "Select * from gcvcontrato";
        dispose();
    }

    /**
     * Construtor sobrecarregado da classe para ser chamado via programa de
     * Pedidos ou Proposta
     *
     * @param su Objeto de Instância do Usuário conectado
     * @param conexao Objeto de Instância de conexão do Usuário
     * @param pg Objeto de Instância dos parâmetros gerais do sistema
     * @param modped Objeto de Instância do Pedido
     * @param chamador String indicando a origem do chamado do programa
     */
    public ManterContrato(SessaoUsuario su, Connection conexao, ParametrosGerais pg, Pedido modped, String chamador) {
        this.su = su;
        this.conexao = conexao;
        this.pg = pg;
        this.chamador = chamador;
        this.modped = modped;
        operCon = "N";
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO);
        setaContrato();
        setaSeqContrato();
        setaTextoSeqContrato();
        formatarCampos();
        sqlCon = "Select * from gcvcontrato where cd_pedido = '" + modped.getCdPedido()
                + "'";
        pesquisarContrato();
        dispose();
    }

    /**
     * Método para setar objetos de contrato
     */
    private void setaContrato() {
        con = new Contrato();
        modcon = new Contrato();
        try {
            ccon = new CContrato(conexao, su);
        } catch (SQLException ex) {
            Logger.getLogger(ManterContrato.class.getName()).log(Level.SEVERE, null, ex);
        }
        monitoraJanela();
    }

    /**
     * Método para setar objetos da sequencia do contrato
     */
    private void setaSeqContrato() {
        modseqcon = new ContratoSequencia();
        monitoraLinhaItens();
    }

    /**
     * Método para setar objetos do texto da sequencia do contrato
     */
    private void setaTextoSeqContrato() {
        modcst = new ContratoSeqTexto();
    }

    /**
     * Método para formatar campos do Contrato
     */
    private void formatarCampos() {
        jForDataEmissao.setDocument(new DefineCampoData());
        jForDataEnvio.setDocument(new DefineCampoData());
        jForDataAssinatura.setDocument(new DefineCampoData());
    }

    /**
     * Método para formatar campo CPF/CNPJ
     *
     * @param cdCpfCnpj String com o conteúdo a ser verificado
     */
    private void formatarCpfCnpj(String cdCpfCnpj) {
        if (cdCpfCnpj.toString().length() <= 11) {
            try {
                jForCdCpfCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
            } catch (ParseException ex) {
                Logger.getLogger(ManterContrato.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                jForCdCpfCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
            } catch (ParseException ex) {
                Logger.getLogger(ManterContrato.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Método para limpar tela do contrato
     */
    private void limparTelaContrato() {
        jTexCdContrato.setText("");
        jForDataEmissao.setText("");
        jComSituacao.setSelectedIndex(0);
        jForCdCpfCnpj.setText("");
        jTexNomeRazaoSocial.setText("");
        jForDataEnvio.setText("");
        jForDataAssinatura.setText("");
        jTexNomeResponsavel.setText("");
        jForCpfResponsavel.setText("");
        limparTextoSeqContrato();
    }

    /**
     * Método para limpar texto descritivo da sequencia do contrato
     */
    private void limparTextoSeqContrato() {
        jTexAreaSeqTexto.setText("");
    }

    /**
     * Método para pesquisar o Contrato
     */
    private void pesquisarContrato() {
        try {
            numRegCon = ccon.pesquisar(sqlCon);
            idxCorCon = 1;
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Argumento inválido na buscaErr: " + iae.getCause());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Pesquisar os Contratos!\nErr: " + ex);
        }
        if (numRegCon > 0) {
            upRegistroContrato();
        } else {
            JOptionPane.showMessageDialog(null, "Não foi localizado nenhum contrato!");
        }
    }

    /**
     * Método para pesquisar as cláusulas (sequências)do contrato
     */
    private void pesquisarSeqContrato() {
        sqlSeq = "select * from gcvcontratosequencia as s"
                + " where s.cd_contrato = '" + modcon.getCdContrato()
                + "' order by s.cd_seqantecessora";
        seq = new DefaultTableModel();
        try {
            cseqcon = new CContratoSequencia(conexao, su);
            NumRegSeq = cseqcon.pesquisar(sqlSeq);
            idxCorSeq += 1;
        } catch (SQLException ex) {
            Logger.getLogger(ManterContrato.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (NumRegSeq > 0) {
            jButGerarClausulas.setEnabled(false);
            seqCon = NumRegSeq;
            jTabSeqContrato.setModel(cseqcon.carregarItens());
            ajustarTabelaItem();
        } else {
            if ("N".equals(modcon.getModelo().toUpperCase())) {
                jButGerarClausulas.setEnabled(true);
            }
            seqCon = 0;
            if (esvaziarTabela) {
                esvaziarTabelaItem();
            }
            esvaziarTabela = false;
        }
    }

    /**
     * Método para pesquisar Texto da Sequencia do Contrato
     */
    private void pesquisarTextoSeqContrato() {
        String sqlcst = "select * from gcvcontratoseqtexto as t"
                + " where t.cd_contrato = '" + modcon.getCdContrato()
                + "' and t.cd_sequencia = '" + modseqcon.getCdSequencia()
                + "'";
        ccst = new CContratoSeqTexto(conexao);
        int numRegCst;
        try {
            numRegCst = ccst.pesquisar(sqlcst);
            if (numRegCst > 0) {
                ccst.mostrarPesquisa(modcst, 0);
                jTexAreaSeqTexto.setText(modcst.getTextoLongo());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManterContrato.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para atualizar os registro do contrato na tela
     */
    private void upRegistroContrato() {
        modcon = new Contrato();
        jTexRegTotal.setText(Integer.toString(numRegCon));
        jTexRegAtual.setText(Integer.toString(idxCorCon));
        modcon = ccon.mostrarPesquisa(idxCorCon - 1);
        DataSistema dat = new DataSistema();
        HoraSistema hs = new HoraSistema();
        jTexCdContrato.setText(modcon.getCdContrato());
        jForDataEmissao.setText(dat.getDataConv(Date.valueOf(modcon.getDataEmissao())));
        jComSituacao.setSelectedIndex(Integer.parseInt(modcon.getSituacao()));
        formatarCpfCnpj(modcon.getCdCpfCnpj().trim());
        jForCdCpfCnpj.setText(modcon.getCdCpfCnpj());
        jTexNomeRazaoSocial.setText(modcon.getNomeRazaoSocial());
        jTexNomeResponsavel.setText(modcon.getNomeResponsavel());
        jForCpfResponsavel.setText(modcon.getCpfResponsavel());
        jComModelo.setSelectedIndex(Integer.parseInt(modcon.getModelo()));
        if (modcon.getDataEnvio() != null) {
            jForDataEnvio.setText(dat.getDataConv(Date.valueOf(modcon.getDataEnvio())));
        }
        if (modcon.getDataAssinatura() != null) {
            jForDataAssinatura.setText(dat.getDataConv(Date.valueOf(modcon.getDataAssinatura())));
        }
        jTexCadPor.setText(modcon.getUsuarioCadastro());
        jForDataCad.setText(dat.getDataConv(Date.valueOf(modcon.getDataCadastro())));
        jForHoraCad.setText(modcon.getHoraCadastro());
        jTexModifPor.setText(modcon.getUsuarioModificacao());
        if (modcon.getDataModificacao() != null) {
            jForDataModif.setText(dat.getDataConv(Date.valueOf(modcon.getDataModificacao())));
            jForHoraModif.setText(modcon.getHoraModificacao());
        }
        // Habilitando/Desabilitando botões de navegação de registros
        if (numRegCon > idxCorCon) {
            jButProximo.setEnabled(true);
        } else {
            jButProximo.setEnabled(false);
        }
        if (idxCorCon > 1) {
            jButAnterior.setEnabled(true);
        } else {
            jButAnterior.setEnabled(false);
        }
        pesquisarSeqContrato();
    }

    /**
     * Método para ajustar tabela itens
     */
    private void ajustarTabelaItem() {
        jTabSeqContrato.getColumnModel().getColumn(0).setWidth(50);
        jTabSeqContrato.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTabSeqContrato.getColumnModel().getColumn(1).setWidth(10);
        jTabSeqContrato.getColumnModel().getColumn(1).setPreferredWidth(10);
        jTabSeqContrato.getColumnModel().getColumn(2).setWidth(10);
        jTabSeqContrato.getColumnModel().getColumn(2).setPreferredWidth(10);
        jTabSeqContrato.getColumnModel().getColumn(3).setWidth(10);
        jTabSeqContrato.getColumnModel().getColumn(3).setPreferredWidth(10);
        jTabSeqContrato.getColumnModel().getColumn(4).setWidth(200);
        jTabSeqContrato.getColumnModel().getColumn(4).setPreferredWidth(200);
        jTabSeqContrato.getColumnModel().getColumn(5).setWidth(30);
        jTabSeqContrato.getColumnModel().getColumn(5).setPreferredWidth(30);
        jTabSeqContrato.getColumnModel().getColumn(6).setWidth(20);
        jTabSeqContrato.getColumnModel().getColumn(6).setPreferredWidth(20);
        jTabSeqContrato.getColumnModel().getColumn(7).setWidth(50);
        jTabSeqContrato.getColumnModel().getColumn(7).setPreferredWidth(50);
        jTabSeqContrato.getColumnModel().getColumn(8).setWidth(20);
        jTabSeqContrato.getColumnModel().getColumn(8).setPreferredWidth(20);
        if (!inclusao) {
            modseqcon.setTipoSequencia(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 0).toString().substring(0, 2)));
            modseqcon.setCdSequencia(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 1)));
            modseqcon.setCdSequenciaPai(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 2)));
            modseqcon.setCdSequenciaAtencessora(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 3)));
            modseqcon.setTitulo(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 4)));
            modseqcon.setPosicaoTitulo(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 5)));
            modseqcon.setPossuiTexto(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 6).toString().substring(0, 1)));
            modseqcon.setQuebraLinha(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 7).toString().substring(0, 1)));
            modseqcon.setSituacao(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 8).toString().substring(0, 2)));
            if ("S".equals(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 6).toString().substring(0, 1).toUpperCase()))) {
                jButIncluirTexto.setEnabled(true);
                jButEditarTexto.setEnabled(true);
            } else {
                jButIncluirTexto.setEnabled(false);
                jButEditarTexto.setEnabled(false);
            }
        }
        pesquisarTextoSeqContrato();
        ajustaColunaTabela();
    }

    /**
     * Método para monitorar a mudança de linha da tabela
     */
    private void monitoraLinhaItens() {
        linhaSeq = 0;
        VerificarTecla vt = new VerificarTecla();
        // captura o número da linha posicionada
        jTabSeqContrato.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                linhaSeq = jTabSeqContrato.getSelectedRow();
                limparTextoSeqContrato();
                ajustarTabelaItem();
            }
        });

        // capitura a tecla digitada no teclado
        jTabSeqContrato.addKeyListener(new KeyListener() {

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                String tecla = vt.VerificarTecla(e).toUpperCase();
                // se a tecla digita for seta para baixo verifiar se é para incluir uma nova linha
                if ("ABAIXO".equals(tecla) && !inclusao && "A".equals(operCon)) {
                    if (jTabSeqContrato.getSelectedRow() == jTabSeqContrato.getRowCount() - 1) {
                        modseqcon = new ContratoSequencia();
                        inclusao = true;
                        jTabSeqContrato.setModel(cseqcon.adicionarLinha());
                        linhaSeq = jTabSeqContrato.getSelectedRow() + 1;
                        upValue();
                        ajustarTabelaItem();
                    }
                }

                // se a tecla digitada for a tecla F5 abre a pesquisa de sequencia de contrato
                if ("F5".equals(tecla) && (inclusao || "A".endsWith(operCon))) {
                    dowValue();
                    zoomSequencia();
                    if (jTabSeqContrato.getSelectedColumn() == 2) {
                        modseqcon.setCdSequenciaPai(zoomCdsequencia);
                    } else if (jTabSeqContrato.getSelectedColumn() == 3) {
                        modseqcon.setCdSequenciaAtencessora(zoomCdsequencia);
                    }
                    upValue();
                }

                // se a tecla pressionada for o ESC excluir a linha inserida
                if ("ESC".equals(tecla) && inclusao) {
                    jTabSeqContrato.setModel(cseqcon.excluirLinha(jTabSeqContrato.getSelectedRowCount()));
                    jTabSeqContrato.requestFocus();
                    inclusao = false;
                    ajustarTabelaItem();
                }

                // se a tecla pressionada for o Enter, salva a linha inserida no banco de dados
                if ("ENTER".equals(tecla) && (inclusao || "A".equals(operCon))) {
                    if (inclusao) {
                        inclusao = false;
                        operSeq = "N";
                    } else {
                        operSeq = "A";
                    }
                    salvarLinha();
                }

                // se a tecla pressionada for tabulação, atualizada os dados da linha posicionada na tabela
                if ("GUIA".equals(tecla) && "A".equals(operCon)) {
                    operSeq = "A";
                    dowValue();
                    upValue();
                }

                // se a tecla pressionada for Seta para cima, salva a linha que está sendo incluída no banco
                if ("ACIMA".equals(tecla) && inclusao) {
                    inclusao = false;
                    operSeq = "N";
                    salvarLinha();
                }

                // se a tecla pressionada for a Del/Delete, exclui a linha da tabela que está posicionado
                if ("EXCLUIR".equals(tecla) && "A".equals(operCon) && !inclusao) {
                    if (JOptionPane.showConfirmDialog(null, "Confirma exclusão do item?") == JOptionPane.OK_OPTION) {
                        int numReg = 0;
                        modseqcon = new ContratoSequencia();
                        dowValue();
                        try {
                            CContratoSequencia ccs = new CContratoSequencia(conexao, su);
                            String sql = "Select * from gcvcontratosequencia where cd_contrato = '" + modcon.getCdContrato()
                                    + "' and cd_sequencia = '" + modseqcon.getCdSequencia()
                                    + "'";
                            numReg = ccs.pesquisar(sql);
                            if (numReg > 0) {
                                ccs.mostrarPesquisa(0);
                            }
                        } catch (SQLException ex) {
                            Logger.getLogger(ManterContrato.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            seqconDAO = new ContratoSequenciaDAO(conexao);
                            seqconDAO.excluir(modseqcon);
                        } catch (SQLException ex) {
                            Logger.getLogger(ManterContrato.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        pesquisarSeqContrato();
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }

            /**
             * método para atualizar a linha na tabela
             */
            private void upValue() {
                modseqcon.setSituacao(jComSituacao.getSelectedItem().toString().trim());
                jTabSeqContrato.setModel(cseqcon.upNovaLinha(modseqcon, linhaSeq, 0, 1, 2, 3, 4, 5, 6, 7, 8));
            }

            /**
             * Método para carregar no objeto SequenciaContrato, os itens que
             * foram adicionados na tabela
             */
            private void dowValue() {
                modseqcon.setCdContrato(modcon.getCdContrato());
                modseqcon.setTipoSequencia(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 0)));
                modseqcon.setCdSequencia(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 1)));
                modseqcon.setCdSequenciaPai(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 2)));
                modseqcon.setCdSequenciaAtencessora(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 3)));
                modseqcon.setTitulo(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 4)));
                modseqcon.setPosicaoTitulo(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 5)));
                modseqcon.setPossuiTexto(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 6)));
                modseqcon.setQuebraLinha(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 7)));
                modseqcon.setSituacao(String.format("%s", jTabSeqContrato.getValueAt(linhaSeq, 8)));
            }

            /**
             * Método para salvar a linha no banco de dados
             */
            private void salvarLinha() {
                salvarSeqContrato();
            }
        });
    }

    /**
     * Método para Monitor a janela quando for fechada
     */
    private void monitoraJanela() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                if ("A".equals(operCon)) {
                    if (JOptionPane.showConfirmDialog(null, "Confirma sair do Contrato?") == JOptionPane.OK_OPTION) {
                        salvarContrato(false);
                    }
                }
                dispose();
            }
        });
    }

    /**
     * Método para bloquear campos do contrato
     */
    private void bloquearCampoContrato() {
        jTexCdContrato.setEditable(false);
        jForDataEmissao.setEditable(false);
        jComSituacao.setEditable(false);
        jForCdCpfCnpj.setEditable(false);
        jTexNomeRazaoSocial.setEditable(false);
        jComModelo.setEditable(false);
        jForDataEnvio.setEditable(false);
        jForDataAssinatura.setEditable(false);
        jTexNomeResponsavel.setEditable(false);
    }

    /**
     * Método para liberar campos bloqueados do contrato
     */
    private void liberarCamposContrato() {
        jTexCdContrato.setEditable(true);
        jForDataEmissao.setEditable(true);
        jComSituacao.setEditable(true);
        jForCdCpfCnpj.setEditable(true);
        jTexNomeRazaoSocial.setEditable(true);
        jComModelo.setEditable(true);
        jForDataEnvio.setEditable(true);
        jForDataAssinatura.setEditable(true);
        jTexNomeResponsavel.setEditable(true);
        jTexCdContrato.requestFocus();
    }

    /**
     * @param bEd Botão Editar
     * @param bSa Botão Salvar
     * @param bCa Botão Cancelar
     * @param bEx Botão Excluir
     * @param bNo Botão Novo
     * @param bCl Botão Sair
     */
    private void controleBotoes(boolean bEd, boolean bSa, boolean bCa, boolean bEx, boolean bNo, boolean bCl) {
        jButEditar.setEnabled(bEd);
        jButSalvar.setEnabled(bSa);
        jButCancelar.setEnabled(bCa);
        jButExcluir.setEnabled(bEx);
        jButNovo.setEnabled(bNo);
        jButSair.setEnabled(bCl);
    }

    /**
     * Método para dar zoom no campo Cliente
     */
    private void zoomClientes() {
        PesquisarClientes zoom = new PesquisarClientes(this, true, "P", conexao);
        zoom.setVisible(true);
        formatarCpfCnpj(zoom.getSelecao1().trim());
        jForCdCpfCnpj.setText(zoom.getSelecao1());
        jTexNomeRazaoSocial.setText(zoom.getSelecao2());
    }

    /**
     * Método para dar zoom no campo Sequencia Contrato
     */
    private void zoomSequencia() {
        PesquisarSequenciaContrato zoom = new PesquisarSequenciaContrato(this, true, "P", conexao, su, modseqcon.getCdContrato());
        zoom.setVisible(true);
        zoomCdsequencia = zoom.getCdSequencia();
    }

    /**
     * Método para salvar o Contrato
     */
    private void salvarContrato(boolean upTela) {
        con = new Contrato();
        dat = new DataSistema();
        if (jForCdCpfCnpj.getText().isEmpty() || jForCdCpfCnpj.getText().isEmpty() || jComSituacao.getSelectedItem().toString().substring(0, 1) == " ") {
            JOptionPane.showMessageDialog(null, "Os campos Constrato, Clientes e Situacao precisam ser preenchidos corretamente!");
        } else {
            con.setCdContrato(jTexCdContrato.getText().toUpperCase());
            con.setDataEmissao(dat.getDataConv(jForDataEmissao.getText()));
            String cdCpfCnpj = jForCdCpfCnpj.getText().replace(".", "");
            cdCpfCnpj = cdCpfCnpj.replace("-", "");
            cdCpfCnpj = cdCpfCnpj.replace("/", "");
            con.setCdCpfCnpj(cdCpfCnpj);
            con.setModelo(jComModelo.getSelectedItem().toString().substring(0, 1));
            if (jForDataEnvio.getText() != null) {
                if (!jForDataEnvio.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, jForDataEnvio.getText());
                    con.setDataEnvio(dat.getDataConv(jForDataEnvio.getText()));
                }
            }
            if (jForDataAssinatura.getText() != null) {
                if (!jForDataAssinatura.getText().isEmpty()) {
                    con.setDataAssinatura(dat.getDataConv(jForDataAssinatura.getText()));
                }
            }
            con.setNomeResponsavel(jTexNomeResponsavel.getText().toUpperCase());
            con.setCpfResponsavel(jForCpfResponsavel.getText().toUpperCase());
            con.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 2));
            gravarContratoBanco();
            controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
        }
    }

    /**
     * Método para gravar o Contrato no bando de dados
     */
    private void gravarContratoBanco() {
        dat.setData(data);
        data = dat.getData();
        HoraSistema hs = new HoraSistema();
        try {
            conDAO = new ContratoDAO(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(ManterContrato.class.getName()).log(Level.SEVERE, null, ex);
        }
        if ("N".equals(operCon)) {
            CBuscarSequencia cb = new CBuscarSequencia(su, TABELA, 9);
            con.setCdContrato(String.format("%s", data.replace("-", "").substring(0, 6) + cb.getRetorno()));
            con.setUsuarioCadastro(su.getUsuarioConectado());
            con.setDataCadastro(data);
            con.setHoraCadastro(hs.getHora());
            conDAO.adicionar(con);
            modcon.setCdContrato(con.getCdContrato());
            sqlCon = "select * from gcvcontrato where cd_contrato = '" + modcon.getCdContrato()
                    + "'";
        } else {
            con.setUsuarioModificacao(su.getUsuarioConectado());
            con.setDataModificacao(data);
            con.setHoraModificacao(hs.getHora());
            conDAO.atualizar(con);
        }
        operCon = "";
    }

    /**
     * Método para salvar o item no banco
     */
    private void salvarSeqContrato() {
        if (Integer.parseInt(modseqcon.getCdSequencia()) != 0 && modseqcon.getTitulo() != null) {
            dat = new DataSistema();
            seqcon = new ContratoSequencia();
            String data = null;
            seqcon.setCdContrato(modcon.getCdContrato());
            seqcon.setTipoSequencia(modseqcon.getTipoSequencia().toString().substring(0, 2));
            seqcon.setCdSequencia(modseqcon.getCdSequencia());
            seqcon.setCdSequenciaPai(modseqcon.getCdSequenciaPai());
            seqcon.setCdSequenciaAtencessora(modseqcon.getCdSequenciaAtencessora());
            seqcon.setTitulo(modseqcon.getTitulo());
            seqcon.setPosicaoTitulo(modseqcon.getPosicaoTitulo().toString().substring(0, 1));
            seqcon.setPossuiTexto(modseqcon.getPossuiTexto().toString().substring(0, 1));
            seqcon.setQuebraLinha(modseqcon.getQuebraLinha().toString().substring(0, 1));
            seqcon.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 2));
            dat.setData(data);
            data = dat.getData();
            gravarSeqContratoBanco();
        }
    }

    /**
     * Método para salvar o texto do item no banco
     */
    private void salvarSeqContratoTexto() {
        if (Integer.parseInt(modseqcon.getCdSequencia()) != 0 && modseqcon.getTitulo() != null) {
            dat = new DataSistema();
            cst = new ContratoSeqTexto();
            String data = null;
            cst.setCdContrato(modcon.getCdContrato());
            cst.setCdSequencia(modseqcon.getCdSequencia());
            cst.setTextoLongo(jTexAreaSeqTexto.getText().trim());
            cst.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 2));
            dat.setData(data);
            data = dat.getData();
            gravarTextoSeqContratoBanco();
        }
    }

    /**
     * Método para gravar a sequencia do contrato no Banco
     */
    private void gravarSeqContratoBanco() {
        HoraSistema hs = new HoraSistema();
        try {
            seqconDAO = new ContratoSequenciaDAO(conexao);
            if ("N".equals(operSeq)) {
                seqcon.setUsuarioCadastro(su.getUsuarioConectado());
                seqcon.setDataCadastro(data);
                seqcon.setHoraCadastro(hs.getHora());
                seqconDAO.adicionar(seqcon);
            } else {
                seqcon.setUsuarioModificacao(su.getUsuarioConectado());
                seqcon.setDataModificacao(data);
                seqcon.setHoraModificacao(hs.getHora());
                seqconDAO.atualizar(seqcon);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManterContrato.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para gravar o texto da sequencia do contrato no Banco
     */
    private void gravarTextoSeqContratoBanco() {
        HoraSistema hs = new HoraSistema();
        try {
            cstDAO = new ContratoSeqTextoDAO(conexao);
            if ("N".equals(operCst)) {
                cst.setUsuarioCadastro(su.getUsuarioConectado());
                cst.setDataCadastro(data);
                cst.setHoraCadastro(hs.getHora());
                cstDAO.adicionar(cst);
            } else {
                cst.setUsuarioModificacao(su.getUsuarioConectado());
                cst.setDataModificacao(data);
                cst.setHoraModificacao(hs.getHora());
                cstDAO.atualizar(cst);
            }
            jButSalvarTexto.setEnabled(false);
        } catch (SQLException ex) {
            Logger.getLogger(ManterContrato.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para excluir Contrato
     */
    private void excluirContrato() {
        if (!jTexCdContrato.getText().isEmpty()) {
            try {
                Contrato cc = new Contrato();
                cc.setCdContrato(jTexCdContrato.getText());
                ContratoDAO ccDAO = new ContratoDAO(conexao);
                ccDAO.excluir(cc);
                limparTelaContrato();
                pesquisarContrato();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral na exclusão do registro!");
            }
        }
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    /**
     * Método para imprimir Contrato
     */
    private void imprimirContrato() {
        new CReportContrato().abrirRelatorio(modcon.getCdContrato(), modcon.getCdContrato(), su, conexao, emp, pg, "RELATORIO", modcon);
    }

    /**
     * Método para esvaziar a tabela itens
     */
    private void esvaziarTabelaItem() {
        try {
            cseqcon = new CContratoSequencia(conexao, su);
        } catch (SQLException ex) {
            Logger.getLogger(ManterContrato.class.getName()).log(Level.SEVERE, null, ex);
        }
        jTabSeqContrato.setModel(new JTable().getModel());
        jTabSeqContrato.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabSeqContrato.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {"0", "", "", "", "", "0", "", "0", ""}
                },
                new String[]{"Tipo", "Seq.", "Seq. Pai", "Seq Antec.", "Título", "Posição Título", "Possui Texto?", "Quebra de Linha?", "Situação"}
        ) {
            Class[] types = new Class[]{
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
    }

    /**
     * Método para configurar as colunas da tabela
     */
    private void ajustaColunaTabela() {
        TableColumn tipoSeq = jTabSeqContrato.getColumnModel().getColumn(0);
        TableColumn seqAntecessora = jTabSeqContrato.getColumnModel().getColumn(3);
        TableColumn posicaoTitulo = jTabSeqContrato.getColumnModel().getColumn(5);
        TableColumn possuiTexto = jTabSeqContrato.getColumnModel().getColumn(6);
        TableColumn quebraLinha = jTabSeqContrato.getColumnModel().getColumn(7);
        JComboBox ts = new JComboBox(); // Objeto jcombox para tipoSequencia
        JComboBox pt = new JComboBox(); // Objeto jcombox para posicaoTitulo
        JComboBox ps = new JComboBox(); // Objeto jcombox para possuiTexto
        JComboBox ql = new JComboBox(); // Objeto jcombox para quebraLinha
        ts.addItem("01-Qualificação");
        ts.addItem("02-Objeto");
        ts.addItem("03-Cláusula");
        ts.addItem("04-Item Cláusula");
        ts.addItem("05-Parágrafo");
        ts.addItem("06-Item Parágrafo");
        pt.addItem("Centralizado");
        pt.addItem("Direita");
        pt.addItem("Esquerda");
        ps.addItem("Sim");
        ps.addItem("Não");
        ql.addItem("Sim");
        ql.addItem("Não");

        posicaoTitulo.setCellEditor(new DefaultCellEditor(pt));
        possuiTexto.setCellEditor(new DefaultCellEditor(ps));
        tipoSeq.setCellEditor(new DefaultCellEditor(ts));
        quebraLinha.setCellEditor(new DefaultCellEditor(ql));
        DefaultTableCellRenderer rd = new DefaultTableCellRenderer();
        DefaultTableCellRenderer seqAnt = new DefaultTableCellRenderer();
        rd.setToolTipText("Clique para ver opções");
        seqAnt.setToolTipText("Informe a sequencia antecessora a esta!");
        tipoSeq.setCellRenderer(rd);
        seqAntecessora.setCellRenderer(seqAnt);
        posicaoTitulo.setCellRenderer(rd);
        possuiTexto.setCellRenderer(rd);
        quebraLinha.setCellRenderer(rd);
    }

    private void gerarContrato() {
        ccon.gerarClausulasCliente(modped.getCdOperVenda());
    }

    private void enviarContrato() {
        if (buscarEmpresa() > 0) {
            new CReportContrato().abrirRelatorio(modcon.getCdContrato(), modcon.getCdContrato(), su, conexao, emp, pg, "EMAIL", modcon);
        }
    }

    private int buscarEmpresa() {
        emp = new Empresa();
        int numReg = 0;
        CEmpresa cemp = new CEmpresa(conexao);
        String sqlemp = "Select * from pgsempresa as e where e.situacao = 'A' and e.tipo_empresa = 'Emp'";
        try {
            numReg = cemp.pesquisar(sqlemp);
            if (numReg > 0) {
                cemp.mostrarPesquisa(emp, 0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManterPropostaComercialRev1.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numReg;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTooMenuFerramentas = new javax.swing.JToolBar();
        jButNovo = new javax.swing.JButton();
        jButEditar = new javax.swing.JButton();
        jButSalvar = new javax.swing.JButton();
        jButCancelar = new javax.swing.JButton();
        jButExcluir = new javax.swing.JButton();
        jButPesquisar = new javax.swing.JButton();
        jButAnterior = new javax.swing.JButton();
        jButProximo = new javax.swing.JButton();
        jButImprimir = new javax.swing.JButton();
        jButEnviar = new javax.swing.JButton();
        jButSair = new javax.swing.JButton();
        jPanGeral = new javax.swing.JPanel();
        jPanContrato = new javax.swing.JPanel();
        jLabCdContrato = new javax.swing.JLabel();
        jTexCdContrato = new javax.swing.JTextField();
        jLabDataEmissao = new javax.swing.JLabel();
        jForDataEmissao = new javax.swing.JFormattedTextField();
        jLabSituacao = new javax.swing.JLabel();
        jComSituacao = new javax.swing.JComboBox<>();
        jLabCliente = new javax.swing.JLabel();
        jForCdCpfCnpj = new javax.swing.JFormattedTextField();
        jTexNomeRazaoSocial = new javax.swing.JTextField();
        jLabModelo = new javax.swing.JLabel();
        jComModelo = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jLabDataEnvio = new javax.swing.JLabel();
        jForDataEnvio = new javax.swing.JFormattedTextField();
        jLabDataAssinatura = new javax.swing.JLabel();
        jForDataAssinatura = new javax.swing.JFormattedTextField();
        jLabNomeResponsavel = new javax.swing.JLabel();
        jTexNomeResponsavel = new javax.swing.JTextField();
        jLabCpfResponsável = new javax.swing.JLabel();
        jForCpfResponsavel = new javax.swing.JFormattedTextField();
        jButGerarClausulas = new javax.swing.JButton();
        jPanSeqContrato = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabSeqContrato = new javax.swing.JTable();
        jPanTextoSeqCont = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTexAreaSeqTexto = new javax.swing.JTextArea();
        jButIncluirTexto = new javax.swing.JButton();
        jButEditarTexto = new javax.swing.JButton();
        jButSalvarTexto = new javax.swing.JButton();
        jPanRodape = new javax.swing.JPanel();
        jForDataCad = new javax.swing.JFormattedTextField();
        jForDataModif = new javax.swing.JFormattedTextField();
        jLabCadPor = new javax.swing.JLabel();
        jTexCadPor = new javax.swing.JTextField();
        jTexModifPor = new javax.swing.JTextField();
        jLabModifPor = new javax.swing.JLabel();
        jTexRegAtual = new javax.swing.JTextField();
        jLabReg = new javax.swing.JLabel();
        jTexRegTotal = new javax.swing.JTextField();
        jForHoraCad = new javax.swing.JFormattedTextField();
        jForHoraModif = new javax.swing.JFormattedTextField();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Contrato");
        setMaximumSize(new java.awt.Dimension(1004, 763));
        setMinimumSize(new java.awt.Dimension(1004, 763));
        setPreferredSize(new java.awt.Dimension(1004, 763));

        jTooMenuFerramentas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTooMenuFerramentas.setRollover(true);

        jButNovo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Addthis-32.png"))); // NOI18N
        jButNovo.setText("Novo");
        jButNovo.setEnabled(false);
        jButNovo.setFocusable(false);
        jButNovo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButNovo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButNovoActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButNovo);

        jButEditar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Edit-32.png"))); // NOI18N
        jButEditar.setText("Editar");
        jButEditar.setFocusable(false);
        jButEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButEditar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButEditarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButEditar);

        jButSalvar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Ok-32.PNG"))); // NOI18N
        jButSalvar.setText("Salvar");
        jButSalvar.setEnabled(false);
        jButSalvar.setFocusable(false);
        jButSalvar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButSalvar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButSalvarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButSalvar);

        jButCancelar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Cancel-32.png"))); // NOI18N
        jButCancelar.setText("Cancelar");
        jButCancelar.setEnabled(false);
        jButCancelar.setFocusable(false);
        jButCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButCancelarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButCancelar);

        jButExcluir.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Delete-32.png"))); // NOI18N
        jButExcluir.setText("Excluir");
        jButExcluir.setEnabled(false);
        jButExcluir.setFocusable(false);
        jButExcluir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButExcluir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButExcluirActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButExcluir);

        jButPesquisar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Search-32.png"))); // NOI18N
        jButPesquisar.setText("Pesquisar");
        jButPesquisar.setFocusable(false);
        jButPesquisar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButPesquisar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButPesquisarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButPesquisar);

        jButAnterior.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButAnterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Back-32.png"))); // NOI18N
        jButAnterior.setText("Anterior");
        jButAnterior.setEnabled(false);
        jButAnterior.setFocusable(false);
        jButAnterior.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButAnterior.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButAnteriorActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButAnterior);

        jButProximo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButProximo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Next-32.png"))); // NOI18N
        jButProximo.setText("Próximo");
        jButProximo.setEnabled(false);
        jButProximo.setFocusable(false);
        jButProximo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButProximo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButProximo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButProximoActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButProximo);

        jButImprimir.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Print_32.png"))); // NOI18N
        jButImprimir.setText("Imprimir");
        jButImprimir.setFocusable(false);
        jButImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButImprimirActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButImprimir);

        jButEnviar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButEnviar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Mail-send-32.png"))); // NOI18N
        jButEnviar.setText("Enviar");
        jButEnviar.setFocusable(false);
        jButEnviar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButEnviar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButEnviarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButEnviar);

        jButSair.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Exit-32.png"))); // NOI18N
        jButSair.setText("Sair");
        jButSair.setFocusable(false);
        jButSair.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButSair.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButSairActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButSair);

        jPanGeral.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanGeral.setMaximumSize(new java.awt.Dimension(840, 514));
        jPanGeral.setMinimumSize(new java.awt.Dimension(840, 514));

        jPanContrato.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabCdContrato.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdContrato.setText("Contrato:");

        jLabDataEmissao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDataEmissao.setText("Data Emissão:");

        jForDataEmissao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataEmissao.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabSituacao.setText("Situação:");

        jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "PE-Pendente", "AE-Aguardando Envio", "AA-Aguardando Assinatura", "AS-Assinado", " " }));

        jLabCliente.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCliente.setText("Cliente:");

        jForCdCpfCnpj.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jForCdCpfCnpjKeyPressed(evt);
            }
        });

        jLabModelo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabModelo.setText("Contrato Modelo:");

        jComModelo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não" }));

        jLabDataEnvio.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDataEnvio.setText("Data Envio:");

        jForDataEnvio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataEnvio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabDataAssinatura.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDataAssinatura.setText("Data Assinatura:");

        jForDataAssinatura.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataAssinatura.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabNomeResponsavel.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNomeResponsavel.setText("Reponsável Cliente:");

        jLabCpfResponsável.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCpfResponsável.setText("C.P.F.: Responsável:");

        try {
            jForCpfResponsavel.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jButGerarClausulas.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButGerarClausulas.setText("Gerar Cláusulas");
        jButGerarClausulas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButGerarClausulasActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanContratoLayout = new javax.swing.GroupLayout(jPanContrato);
        jPanContrato.setLayout(jPanContratoLayout);
        jPanContratoLayout.setHorizontalGroup(
            jPanContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanContratoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanContratoLayout.createSequentialGroup()
                        .addGroup(jPanContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabCliente)
                            .addComponent(jLabCdContrato))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanContratoLayout.createSequentialGroup()
                                .addComponent(jTexCdContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 170, Short.MAX_VALUE)
                                .addComponent(jLabDataEmissao)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForDataEmissao, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(95, 95, 95)
                                .addComponent(jLabSituacao)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40, 40, 40))
                            .addGroup(jPanContratoLayout.createSequentialGroup()
                                .addComponent(jForCdCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexNomeRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabModelo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComModelo, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanContratoLayout.createSequentialGroup()
                        .addGroup(jPanContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanContratoLayout.createSequentialGroup()
                                .addComponent(jLabDataEnvio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForDataEnvio, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(367, 367, 367)
                                .addComponent(jButGerarClausulas))
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 786, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanContratoLayout.createSequentialGroup()
                                .addComponent(jLabDataAssinatura)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForDataAssinatura, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabNomeResponsavel)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexNomeResponsavel, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabCpfResponsável)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForCpfResponsavel, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanContratoLayout.setVerticalGroup(
            jPanContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanContratoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCdContrato)
                    .addComponent(jTexCdContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabDataEmissao)
                    .addComponent(jForDataEmissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabSituacao)
                    .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCliente)
                    .addComponent(jForCdCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabModelo)
                    .addComponent(jComModelo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabDataEnvio)
                    .addComponent(jForDataEnvio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButGerarClausulas))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabDataAssinatura)
                    .addComponent(jForDataAssinatura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabNomeResponsavel)
                    .addComponent(jTexNomeResponsavel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabCpfResponsável)
                    .addComponent(jForCpfResponsavel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanSeqContrato.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Detalhe do Contrato", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTabSeqContrato.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Tipo", "Seq.", "Seq. Pai", "Seq Antec.", "Título", "Posição Título", "Possui Texto?", "Quebrar Linha?", "Situação"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTabSeqContrato);
        if (jTabSeqContrato.getColumnModel().getColumnCount() > 0) {
            jTabSeqContrato.getColumnModel().getColumn(0).setResizable(false);
            jTabSeqContrato.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTabSeqContrato.getColumnModel().getColumn(1).setResizable(false);
            jTabSeqContrato.getColumnModel().getColumn(1).setPreferredWidth(10);
            jTabSeqContrato.getColumnModel().getColumn(2).setResizable(false);
            jTabSeqContrato.getColumnModel().getColumn(2).setPreferredWidth(10);
            jTabSeqContrato.getColumnModel().getColumn(3).setResizable(false);
            jTabSeqContrato.getColumnModel().getColumn(3).setPreferredWidth(10);
            jTabSeqContrato.getColumnModel().getColumn(4).setResizable(false);
            jTabSeqContrato.getColumnModel().getColumn(4).setPreferredWidth(200);
            jTabSeqContrato.getColumnModel().getColumn(5).setResizable(false);
            jTabSeqContrato.getColumnModel().getColumn(5).setPreferredWidth(30);
            jTabSeqContrato.getColumnModel().getColumn(6).setResizable(false);
            jTabSeqContrato.getColumnModel().getColumn(6).setPreferredWidth(20);
            jTabSeqContrato.getColumnModel().getColumn(7).setResizable(false);
            jTabSeqContrato.getColumnModel().getColumn(7).setPreferredWidth(50);
            jTabSeqContrato.getColumnModel().getColumn(8).setResizable(false);
            jTabSeqContrato.getColumnModel().getColumn(8).setPreferredWidth(20);
        }

        jPanTextoSeqCont.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Texto Descritivo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTexAreaSeqTexto.setColumns(20);
        jTexAreaSeqTexto.setLineWrap(true);
        jTexAreaSeqTexto.setRows(30);
        jTexAreaSeqTexto.setToolTipText("");
        jTexAreaSeqTexto.setWrapStyleWord(true);
        jTexAreaSeqTexto.setMaximumSize(new java.awt.Dimension(767, 2147483647));
        jTexAreaSeqTexto.setMinimumSize(new java.awt.Dimension(767, 22));
        jScrollPane2.setViewportView(jTexAreaSeqTexto);

        javax.swing.GroupLayout jPanTextoSeqContLayout = new javax.swing.GroupLayout(jPanTextoSeqCont);
        jPanTextoSeqCont.setLayout(jPanTextoSeqContLayout);
        jPanTextoSeqContLayout.setHorizontalGroup(
            jPanTextoSeqContLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
        );
        jPanTextoSeqContLayout.setVerticalGroup(
            jPanTextoSeqContLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 197, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jButIncluirTexto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButIncluirTexto.setText("Incluir Texto");
        jButIncluirTexto.setEnabled(false);
        jButIncluirTexto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButIncluirTextoActionPerformed(evt);
            }
        });

        jButEditarTexto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButEditarTexto.setText("Editar Texto");
        jButEditarTexto.setEnabled(false);
        jButEditarTexto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButEditarTextoActionPerformed(evt);
            }
        });

        jButSalvarTexto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButSalvarTexto.setText("Salvar Texto");
        jButSalvarTexto.setEnabled(false);
        jButSalvarTexto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButSalvarTextoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanSeqContratoLayout = new javax.swing.GroupLayout(jPanSeqContrato);
        jPanSeqContrato.setLayout(jPanSeqContratoLayout);
        jPanSeqContratoLayout.setHorizontalGroup(
            jPanSeqContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanSeqContratoLayout.createSequentialGroup()
                .addGroup(jPanSeqContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanTextoSeqCont, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanSeqContratoLayout.createSequentialGroup()
                        .addComponent(jButIncluirTexto)
                        .addGap(18, 18, 18)
                        .addComponent(jButEditarTexto)
                        .addGap(18, 18, 18)
                        .addComponent(jButSalvarTexto)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanSeqContratoLayout.setVerticalGroup(
            jPanSeqContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanSeqContratoLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanTextoSeqCont, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanSeqContratoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButIncluirTexto)
                    .addComponent(jButEditarTexto)
                    .addComponent(jButSalvarTexto))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanRodape.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jForDataCad.setEditable(false);
        jForDataCad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataCad.setEnabled(false);

        jForDataModif.setEditable(false);
        jForDataModif.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataModif.setEnabled(false);

        jLabCadPor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCadPor.setText("Cadastrado:");

        jTexCadPor.setEditable(false);
        jTexCadPor.setEnabled(false);

        jTexModifPor.setEditable(false);
        jTexModifPor.setEnabled(false);

        jLabModifPor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabModifPor.setText("Modificado:");

        jTexRegAtual.setEditable(false);
        jTexRegAtual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexRegAtual.setEnabled(false);

        jLabReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabReg.setText("\\");

            jTexRegTotal.setEditable(false);
            jTexRegTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            jTexRegTotal.setEnabled(false);

            jForHoraCad.setEditable(false);
            try {
                jForHoraCad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForHoraCad.setEnabled(false);

            jForHoraModif.setEditable(false);
            try {
                jForHoraModif.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForHoraModif.setEnabled(false);

            javax.swing.GroupLayout jPanRodapeLayout = new javax.swing.GroupLayout(jPanRodape);
            jPanRodape.setLayout(jPanRodapeLayout);
            jPanRodapeLayout.setHorizontalGroup(
                jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanRodapeLayout.createSequentialGroup()
                    .addGap(13, 13, 13)
                    .addComponent(jTexRegAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(3, 3, 3)
                    .addComponent(jLabReg, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(2, 2, 2)
                    .addComponent(jTexRegTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabCadPor)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexCadPor, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataCad, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForHoraCad, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabModifPor)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexModifPor, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataModif, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForHoraModif, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(260, Short.MAX_VALUE))
            );
            jPanRodapeLayout.setVerticalGroup(
                jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanRodapeLayout.createSequentialGroup()
                    .addGap(11, 11, 11)
                    .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTexRegTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabReg)
                            .addComponent(jTexRegAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jForDataCad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabCadPor)
                            .addComponent(jTexCadPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabModifPor)
                            .addComponent(jTexModifPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForDataModif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForHoraCad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForHoraModif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
            );

            javax.swing.GroupLayout jPanGeralLayout = new javax.swing.GroupLayout(jPanGeral);
            jPanGeral.setLayout(jPanGeralLayout);
            jPanGeralLayout.setHorizontalGroup(
                jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanGeralLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanRodape, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanSeqContrato, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanContrato, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanGeralLayout.setVerticalGroup(
                jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanGeralLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanSeqContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jMenuBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jMenu1.setText("Arquivo");

            jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItem1.setText("Novo");
            jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem1ActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItem1);

            jMenuItemSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemSalvar.setText("Salvar");
            jMenuItemSalvar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemSalvarActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItemSalvar);

            jMenuItemSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemSair.setText("Sair");
            jMenuItemSair.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemSairActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItemSair);

            jMenuBar.add(jMenu1);

            jMenu2.setText("Editar");

            jMenuItemEditar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemEditar.setText("Editar");
            jMenuItemEditar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemEditarActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItemEditar);

            jMenuBar.add(jMenu2);

            setJMenuBar(jMenuBar);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanGeral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanGeral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        jButNovoActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItemSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalvarActionPerformed
        // TODO add your handling code here:
        jButSalvarActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemSalvarActionPerformed

    private void jMenuItemSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jMenuItemSairActionPerformed

    private void jMenuItemEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEditarActionPerformed
        // TODO add your handling code here:
        jButEditarActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemEditarActionPerformed

    private void jButNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButNovoActionPerformed
        controleBotoes(!ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO);
        operCon = "N";
        limparTelaContrato();
        liberarCamposContrato();
        jComModelo.setSelectedIndex(2);
        jComSituacao.setSelectedIndex(1);

    }//GEN-LAST:event_jButNovoActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        controleBotoes(!ISBOTAO, ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO);
        operCon = "A";
        liberarCamposContrato();

    }//GEN-LAST:event_jButEditarActionPerformed

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        controleBotoes(ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
        salvarContrato(true);
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed

    }//GEN-LAST:event_jButCancelarActionPerformed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
        excluirContrato();
    }//GEN-LAST:event_jButExcluirActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        if (jTexCdContrato.getText().isEmpty()) {
            sqlCon = "Select * from gcvcontrato";
        } else {
            sqlCon = "Select * from gcvcontrato where cd_contrato = '" + jTexCdContrato.getText().toLowerCase()
                    + "'";
        }
        bloquearCampoContrato();
        pesquisarContrato();
        controleBotoes(ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
    }//GEN-LAST:event_jButPesquisarActionPerformed

    private void jButAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAnteriorActionPerformed
        idxCorCon -= 1;
        esvaziarTabelaItem();
        limparTextoSeqContrato();
        upRegistroContrato();
    }//GEN-LAST:event_jButAnteriorActionPerformed

    private void jButProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButProximoActionPerformed
        idxCorCon += 1;
        esvaziarTabelaItem();
        limparTextoSeqContrato();
        upRegistroContrato();
    }//GEN-LAST:event_jButProximoActionPerformed

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jForCdCpfCnpjKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jForCdCpfCnpjKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt))) {
            zoomClientes();
        }
    }//GEN-LAST:event_jForCdCpfCnpjKeyPressed

    private void jButIncluirTextoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButIncluirTextoActionPerformed
        operCst = "N";
        jTexAreaSeqTexto.setEnabled(true);
        jTexAreaSeqTexto.setEditable(true);
        jButSalvarTexto.setEnabled(true);
    }//GEN-LAST:event_jButIncluirTextoActionPerformed

    private void jButEditarTextoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarTextoActionPerformed
        operCst = "A";
        jTexAreaSeqTexto.setEnabled(true);
        jTexAreaSeqTexto.setEditable(true);
        jButSalvarTexto.setEnabled(true);
    }//GEN-LAST:event_jButEditarTextoActionPerformed

    private void jButSalvarTextoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarTextoActionPerformed
        salvarSeqContratoTexto();
    }//GEN-LAST:event_jButSalvarTextoActionPerformed

    private void jButImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButImprimirActionPerformed
        if (buscarEmpresa() > 0) {
            imprimirContrato();
        }
    }//GEN-LAST:event_jButImprimirActionPerformed

    private void jButGerarClausulasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButGerarClausulasActionPerformed
        if ("P".equals(chamador)) {
            gerarContrato();
            pesquisarSeqContrato();
        }
    }//GEN-LAST:event_jButGerarClausulasActionPerformed

    private void jButEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEnviarActionPerformed
        // TODO add your handling code here:
        enviarContrato();
    }//GEN-LAST:event_jButEnviarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManterContrato.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterContrato.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterContrato.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterContrato.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterContrato(su, conexao, pg, chamador).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButAnterior;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButEditar;
    private javax.swing.JButton jButEditarTexto;
    private javax.swing.JButton jButEnviar;
    private javax.swing.JButton jButExcluir;
    private javax.swing.JButton jButGerarClausulas;
    private javax.swing.JButton jButImprimir;
    private javax.swing.JButton jButIncluirTexto;
    private javax.swing.JButton jButNovo;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JButton jButProximo;
    private javax.swing.JButton jButSair;
    private javax.swing.JButton jButSalvar;
    private javax.swing.JButton jButSalvarTexto;
    private javax.swing.JComboBox<String> jComModelo;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JFormattedTextField jForCdCpfCnpj;
    private javax.swing.JFormattedTextField jForCpfResponsavel;
    private javax.swing.JFormattedTextField jForDataAssinatura;
    private javax.swing.JFormattedTextField jForDataCad;
    private javax.swing.JFormattedTextField jForDataEmissao;
    private javax.swing.JFormattedTextField jForDataEnvio;
    private javax.swing.JFormattedTextField jForDataModif;
    private javax.swing.JFormattedTextField jForHoraCad;
    private javax.swing.JFormattedTextField jForHoraModif;
    private javax.swing.JLabel jLabCadPor;
    private javax.swing.JLabel jLabCdContrato;
    private javax.swing.JLabel jLabCliente;
    private javax.swing.JLabel jLabCpfResponsável;
    private javax.swing.JLabel jLabDataAssinatura;
    private javax.swing.JLabel jLabDataEmissao;
    private javax.swing.JLabel jLabDataEnvio;
    private javax.swing.JLabel jLabModelo;
    private javax.swing.JLabel jLabModifPor;
    private javax.swing.JLabel jLabNomeResponsavel;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JPanel jPanContrato;
    private javax.swing.JPanel jPanGeral;
    private javax.swing.JPanel jPanRodape;
    private javax.swing.JPanel jPanSeqContrato;
    private javax.swing.JPanel jPanTextoSeqCont;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTabSeqContrato;
    private javax.swing.JTextArea jTexAreaSeqTexto;
    private javax.swing.JTextField jTexCadPor;
    private javax.swing.JTextField jTexCdContrato;
    private javax.swing.JTextField jTexModifPor;
    private javax.swing.JTextField jTexNomeRazaoSocial;
    private javax.swing.JTextField jTexNomeResponsavel;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}