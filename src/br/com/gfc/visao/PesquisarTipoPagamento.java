/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GFCPE0051
 */
package br.com.gfc.visao;

import br.com.gfc.controle.CTipoPagamento;
import br.com.gfc.dao.TipoPagamentoDAO;
import br.com.gfc.modelo.TipoPagamento;
import br.com.modelo.VerificarTecla;
import br.com.modelo.DataSistema;
import br.com.modelo.FormatarValor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 10/11/2017
 */
public class PesquisarTipoPagamento extends javax.swing.JDialog {

    private static Connection conexao;
    private String sql;
    private TipoPagamentoDAO tpdao;
    private CTipoPagamento ctp;
    private TipoPagamento tp;
    private int indexFinal;
    private int indexAtual = 0;
    private static String chamador;
    private VerificarTecla vt;
    private String cdTipoPagamento;
    private String nomeTipoPagamento;
    private String cdPortador;
    private String nomePortador;
    private double txJuros;
    private final DataSistema dat;
    private boolean salvar = false;

    /**
     * Creates new form PesquisaModelo
     */
    public PesquisarTipoPagamento(java.awt.Frame parent, boolean modal, String chamador, Connection conexao) {
        super(parent, modal);
        this.chamador = chamador;
        this.conexao = conexao;
        dat = new DataSistema();
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Tipo de Pagamento");
        setaInicio();
        centralizarComponente();
        mudarLinha();
    }

    // setando as variáveis iniciais
    public void setaInicio() {
        tp = new TipoPagamento();
        ctp = new CTipoPagamento(conexao);
        sql = "select tp.cd_tipopagamento as 'Código',"
                + "tp.nome_tipo_pagamento as Nome,"
                + "tp.cd_portador as Portador,"
                + "tp.situacao as Sit"
                + " from gfctipopagamento as tp"
                + " order by tp.cd_tipopagamento";
        buscarTodos();
        jTabTipoPagamento.setModel(tpdao);
        ajustarTabela();
    }

    // criando um listener quando mudar de linha
    private void mudarLinha() {
        // verificar tecla para captuar quando a tecla entre for pressionada
        jTabTipoPagamento.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                vt = new VerificarTecla();
                if ("ENTER".equals(String.format("%s", vt.VerificarTecla(e)).toUpperCase()) && !"M".equals(chamador.toUpperCase())) {
                    salvar = true;
                    salvarSelecao();
                }
            }

            @Override
            public void keyReleased(KeyEvent e
            ) {
            }
        });

        jTabTipoPagamento.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtual = jTabTipoPagamento.getSelectedRow();
                if (!salvar) {
                    ajustarTabela();
                }
            }
        });
    }

    // centralizar no formulario
    // método para centralizar componente
    public void centralizarComponente() {
        Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dw = getSize();
        setLocation((ds.width - dw.width) / 2, (ds.height - dw.height) / 2);
    }

    //Metodo para buscar todos
    public void buscarTodos() {
        try {
            tpdao = new TipoPagamentoDAO(conexao);
            tpdao.setQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        jTabTipoPagamento.getColumnModel().getColumn(0).setMinWidth(30);
        jTabTipoPagamento.getColumnModel().getColumn(0).setPreferredWidth(30);
        jTabTipoPagamento.getColumnModel().getColumn(1).setMinWidth(300);
        jTabTipoPagamento.getColumnModel().getColumn(1).setPreferredWidth(300);
        jTabTipoPagamento.getColumnModel().getColumn(2).setMinWidth(30);
        jTabTipoPagamento.getColumnModel().getColumn(2).setPreferredWidth(30);
        jTabTipoPagamento.getColumnModel().getColumn(3).setMinWidth(20);
        jTabTipoPagamento.getColumnModel().getColumn(3).setPreferredWidth(20);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
        jTexSelecao1.setText(String.format("%s", jTabTipoPagamento.getValueAt(indexAtual, 0)));
        jTexSelecao2.setText(String.format("%s", jTabTipoPagamento.getValueAt(indexAtual, 1)));
        cdTipoPagamento = String.format("%s", jTabTipoPagamento.getValueAt(indexAtual, 0));
        nomeTipoPagamento = String.format("%s", jTabTipoPagamento.getValueAt(indexAtual, 1));
        buscarCorrelatos();
    }

    // buscar tabelas correlatos
    private void buscarCorrelatos() {
        if (!cdTipoPagamento.isEmpty()) {
            String sql = "select * from gfctipopagamento where cd_tipopagamento = '"
                    + cdTipoPagamento
                    + "'";
            try {
                ctp.pesquisar(sql);
            } catch (SQLException ex) {
                Logger.getLogger(PesquisarTipoPagamento.class.getName()).log(Level.SEVERE, null, ex);
            }
            ctp.mostrarPesquisa(tp, 0);
            atualizarTela();
        }
    }

    // Atualizar registros correlatos
    private void atualizarTela() {
        jTexCdPortador.setText(tp.getCdPortador());
        jTexNomePortador.setText(tp.getNomePortador());
        if ("S".equals(tp.getPermiteParcelamento())) {
            jChePermiteParcelamento.setSelected(true);
        } else {
            jChePermiteParcelamento.setSelected(false);
        }
        if ("S".equals(tp.getEnviarArqBanco())) {
            jCheEnviaArqBanco.setSelected(true);
        } else {
            jCheEnviaArqBanco.setSelected(false);
        }
        if ("S".equals(tp.getEmiteBoleto())) {
            jCheEmiteBoleto.setSelected(true);
        } else {
            jCheEmiteBoleto.setSelected(false);
        }
        if ("S".equals(tp.getEnviaCartorio())) {
            jCheEnviaCartorio.setSelected(true);
        } else {
            jCheEnviaCartorio.setSelected(false);
        }
        jTexTxMulta.setText(String.valueOf(tp.getTaxaMulta()));
        jTexTxJuros.setText(String.valueOf(tp.getTaxaJuros()));
        jTexDiasLiquidacao.setText(String.valueOf(tp.getDiasLiquidacao()));
        jTexDiasCartorio.setText(String.valueOf(tp.getDiasCartorio()));
        jTexCadastradoPor.setText(tp.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(tp.getDataCadastro())));
        if (tp.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(tp.getDataModificacao())));
        }
    }
;
    // salvar seleção
    private void salvarSelecao() {
        this.cdTipoPagamento = tp.getCdTipoPagamento();
        this.nomeTipoPagamento = tp.getNomeTipoPagamento();
        cdPortador = tp.getCdPortador();
        nomePortador = tp.getNomePortador();
        this.txJuros = tp.getTaxaJuros();
        dispose();
    }

    /**
     * @return the CdTipoPagamento
     */
    public String getCdTipoPagamento() {
        return this.cdTipoPagamento;
    }

    /**
     * @return the nomeTipoPagamento
     */
    public String getNomeTipoPagamento() {
        return this.nomeTipoPagamento;
    }

    //
    // capiturando o valor da linha
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanTabela = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabTipoPagamento = new JTable(tpdao);
        jPanePesquisar = new javax.swing.JPanel();
        jLabPesquisar = new javax.swing.JLabel();
        jTexPesquisar = new javax.swing.JTextField();
        jTexSelecao1 = new javax.swing.JTextField();
        jTexSelecao2 = new javax.swing.JTextField();
        jLabPortador = new javax.swing.JLabel();
        jPanInformacoes = new javax.swing.JPanel();
        jLabTxMulta = new javax.swing.JLabel();
        jTexTxMulta = new FormatarValor(FormatarValor.NUMERO);
        jLabDiasLiquidacao = new javax.swing.JLabel();
        jTexDiasLiquidacao = new javax.swing.JTextField();
        jLabDiasCartorio = new javax.swing.JLabel();
        jTexDiasCartorio = new javax.swing.JTextField();
        jLabTxJuros = new javax.swing.JLabel();
        jTexTxJuros = new FormatarValor(FormatarValor.NUMERO);
        jChePermiteParcelamento = new javax.swing.JCheckBox();
        jCheEmiteBoleto = new javax.swing.JCheckBox();
        jCheEnviaCartorio = new javax.swing.JCheckBox();
        jCheEnviaArqBanco = new javax.swing.JCheckBox();
        jLabCdPortador = new javax.swing.JLabel();
        jTexCdPortador = new javax.swing.JTextField();
        jTexNomePortador = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabCadastradoPor = new javax.swing.JLabel();
        jLabDataCadastro = new javax.swing.JLabel();
        jLabDataModificacao = new javax.swing.JLabel();
        jForDataCadastro = new javax.swing.JFormattedTextField();
        jTexCadastradoPor = new javax.swing.JTextField();
        jForDataModificacao = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Tipo Pagamento");
        setResizable(false);

        jPanTabela.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabTipoPagamento.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.SystemColor.activeCaptionBorder, null));
        jTabTipoPagamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabTipoPagamento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nome", "Portador", "Sit"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabTipoPagamento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabTipoPagamentoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTabTipoPagamento);
        if (jTabTipoPagamento.getColumnModel().getColumnCount() > 0) {
            jTabTipoPagamento.getColumnModel().getColumn(0).setResizable(false);
            jTabTipoPagamento.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTabTipoPagamento.getColumnModel().getColumn(1).setResizable(false);
            jTabTipoPagamento.getColumnModel().getColumn(1).setPreferredWidth(300);
            jTabTipoPagamento.getColumnModel().getColumn(2).setResizable(false);
            jTabTipoPagamento.getColumnModel().getColumn(2).setPreferredWidth(30);
            jTabTipoPagamento.getColumnModel().getColumn(3).setResizable(false);
            jTabTipoPagamento.getColumnModel().getColumn(3).setPreferredWidth(20);
        }

        jPanePesquisar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabPesquisar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPesquisar.setText("Pesquisar Tipo Pagamento:");

        jTexPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexPesquisarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanePesquisarLayout = new javax.swing.GroupLayout(jPanePesquisar);
        jPanePesquisar.setLayout(jPanePesquisarLayout);
        jPanePesquisarLayout.setHorizontalGroup(
            jPanePesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanePesquisarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanePesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabPesquisar)
                    .addComponent(jTexPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 531, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanePesquisarLayout.setVerticalGroup(
            jPanePesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanePesquisarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabPesquisar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTexSelecao1.setEditable(false);
        jTexSelecao1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexSelecao1.setEnabled(false);

        jTexSelecao2.setEditable(false);
        jTexSelecao2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexSelecao2.setEnabled(false);

        jLabPortador.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPortador.setText("Tp. Pgto.:");

        jPanInformacoes.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações do Tipo de Pagamento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabTxMulta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTxMulta.setText("Tx. Multa:");

        jTexTxMulta.setEditable(false);
        jTexTxMulta.setEnabled(false);
        jTexTxMulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTexTxMultaActionPerformed(evt);
            }
        });

        jLabDiasLiquidacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDiasLiquidacao.setText("Dias p/ Liquidação");

        jTexDiasLiquidacao.setEditable(false);
        jTexDiasLiquidacao.setEnabled(false);

        jLabDiasCartorio.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDiasCartorio.setText("Dias p/ Cartório:");

        jTexDiasCartorio.setEditable(false);
        jTexDiasCartorio.setEnabled(false);

        jLabTxJuros.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTxJuros.setText("Tx. Juros:");

        jTexTxJuros.setEditable(false);
        jTexTxJuros.setEnabled(false);

        jChePermiteParcelamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jChePermiteParcelamento.setText("Permitir Parcelamento");
        jChePermiteParcelamento.setEnabled(false);

        jCheEmiteBoleto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheEmiteBoleto.setText("Emitir Boleto");
        jCheEmiteBoleto.setEnabled(false);

        jCheEnviaCartorio.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheEnviaCartorio.setText("Enviar Cartório");
        jCheEnviaCartorio.setEnabled(false);

        jCheEnviaArqBanco.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheEnviaArqBanco.setText("Enviar Arquivo Banco");
        jCheEnviaArqBanco.setEnabled(false);

        jLabCdPortador.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabCdPortador.setText("Portador:");

        jTexCdPortador.setEditable(false);
        jTexCdPortador.setEnabled(false);

        jTexNomePortador.setEditable(false);
        jTexNomePortador.setEnabled(false);

        javax.swing.GroupLayout jPanInformacoesLayout = new javax.swing.GroupLayout(jPanInformacoes);
        jPanInformacoes.setLayout(jPanInformacoesLayout);
        jPanInformacoesLayout.setHorizontalGroup(
            jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInformacoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanInformacoesLayout.createSequentialGroup()
                        .addComponent(jLabCdPortador)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexCdPortador, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexNomePortador))
                    .addGroup(jPanInformacoesLayout.createSequentialGroup()
                        .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanInformacoesLayout.createSequentialGroup()
                                .addComponent(jLabDiasLiquidacao)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexDiasLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabDiasCartorio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexDiasCartorio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanInformacoesLayout.createSequentialGroup()
                                .addGap(2, 2, 2)
                                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jChePermiteParcelamento)
                                    .addComponent(jCheEnviaArqBanco))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jCheEmiteBoleto)
                                    .addComponent(jCheEnviaCartorio))
                                .addGap(18, 18, 18)
                                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanInformacoesLayout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabTxJuros))
                                    .addComponent(jLabTxMulta))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTexTxJuros, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTexTxMulta, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanInformacoesLayout.setVerticalGroup(
            jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInformacoesLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCdPortador)
                    .addComponent(jTexCdPortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomePortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jChePermiteParcelamento)
                        .addComponent(jCheEmiteBoleto))
                    .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabTxJuros)
                        .addComponent(jTexTxJuros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabTxMulta)
                        .addComponent(jTexTxMulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jCheEnviaCartorio)
                    .addComponent(jCheEnviaArqBanco))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabDiasCartorio)
                    .addComponent(jTexDiasCartorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabDiasLiquidacao)
                    .addComponent(jTexDiasLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabCadastradoPor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCadastradoPor.setText("Cadastrado por:");

        jLabDataCadastro.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDataCadastro.setText("Data Cadastro:");

        jLabDataModificacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDataModificacao.setText("Data Modificação:");

        jForDataCadastro.setEditable(false);
        jForDataCadastro.setEnabled(false);

        jTexCadastradoPor.setEditable(false);
        jTexCadastradoPor.setEnabled(false);

        jForDataModificacao.setEditable(false);
        jForDataModificacao.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabCadastradoPor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabDataCadastro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabDataModificacao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCadastradoPor)
                    .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabDataCadastro)
                    .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabDataModificacao)
                    .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanTabelaLayout = new javax.swing.GroupLayout(jPanTabela);
        jPanTabela.setLayout(jPanTabelaLayout);
        jPanTabelaLayout.setHorizontalGroup(
            jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanInformacoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanTabelaLayout.createSequentialGroup()
                        .addComponent(jLabPortador)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanTabelaLayout.createSequentialGroup()
                        .addGap(0, 2, Short.MAX_VALUE)
                        .addGroup(jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jPanePesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jScrollPane1))
                            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );
        jPanTabelaLayout.setVerticalGroup(
            jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanePesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabPortador)
                    .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanInformacoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTexPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexPesquisarKeyReleased
        if (!jTexPesquisar.getText().isEmpty()) {
            sql = "select tp.cd_tipopagamento as 'Código',"
                    + "tp.nome_tipo_pagamento as Nome,"
                    + "tp.cd_portador as Portador,"
                    + "tp.situacao as Sit"
                    + " from gfctipopagamento as tp "
                    + " WHERE tp.nome_tipo_pagamento LIKE '%"
                    + jTexPesquisar.getText().trim()
                    + "%' order by tp.cd_tipopagamento";
            //indexAtual = 0;
            buscarTodos();
            jTabTipoPagamento.setModel(tpdao);
            if (jTabTipoPagamento.getModel().getRowCount() > 0) {
                ajustarTabela();
            }
        } else {
            setaInicio();
        }
    }//GEN-LAST:event_jTexPesquisarKeyReleased

    private void jTabTipoPagamentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabTipoPagamentoKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("ENTER".equals(vt.VerificarTecla(evt))) {
            JOptionPane.showMessageDialog(null, vt.VerificarTecla(evt));
            salvarSelecao();
        }
    }//GEN-LAST:event_jTabTipoPagamentoKeyPressed

    private void jTexTxMultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTexTxMultaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTexTxMultaActionPerformed

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
            java.util.logging.Logger.getLogger(PesquisarTipoPagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisarTipoPagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisarTipoPagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisarTipoPagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PesquisarTipoPagamento dialog = new PesquisarTipoPagamento(new javax.swing.JFrame(), true, chamador, conexao);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JCheckBox jCheEmiteBoleto;
    private javax.swing.JCheckBox jCheEnviaArqBanco;
    private javax.swing.JCheckBox jCheEnviaCartorio;
    private javax.swing.JCheckBox jChePermiteParcelamento;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabCdPortador;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabDiasCartorio;
    private javax.swing.JLabel jLabDiasLiquidacao;
    private javax.swing.JLabel jLabPesquisar;
    private javax.swing.JLabel jLabPortador;
    private javax.swing.JLabel jLabTxJuros;
    private javax.swing.JLabel jLabTxMulta;
    private javax.swing.JPanel jPanInformacoes;
    private javax.swing.JPanel jPanTabela;
    private javax.swing.JPanel jPanePesquisar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabTipoPagamento;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexCdPortador;
    private javax.swing.JTextField jTexDiasCartorio;
    private javax.swing.JTextField jTexDiasLiquidacao;
    private javax.swing.JTextField jTexNomePortador;
    private javax.swing.JTextField jTexPesquisar;
    private javax.swing.JTextField jTexSelecao1;
    private javax.swing.JTextField jTexSelecao2;
    private javax.swing.JTextField jTexTxJuros;
    private javax.swing.JTextField jTexTxMulta;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the txJuros
     */
    public double getTxJuros() {
        return txJuros;
    }

    /**
     * @return the cdPortador
     */
    public String getCdPortador() {
        return cdPortador;
    }

    /**
     * @return the nomePortador
     */
    public String getNomePortador() {
        return nomePortador;
    }

}