/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GFCPE0130
 */
package br.com.gfc.visao;

import br.com.DAO.ConsultaModelo;
import br.com.gfc.controle.CHistorico;
import br.com.gfc.modelo.Historico;
import br.com.modelo.VerificarTecla;
import br.com.modelo.DataSistema;
import br.com.modelo.SessaoUsuario;
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
 * @version 0.01_beta0917 created on 02/10/2018
 */
public class PesquisarHistoricos extends javax.swing.JDialog {

    private static Connection conexao;
    private static SessaoUsuario su;
    private String sql;
    private ConsultaModelo htdao;
    private CHistorico cht;
    private Historico ht;
    private int indexFinal;
    private int indexAtual = 0;
    private static String chamador;
    private VerificarTecla vt;
    private String cdHistorico;
    private String nomeHistorico;
    private final DataSistema dat;
    private boolean salvar = false;

    /**
     * Creates new form PesquisaModelo
     */
    public PesquisarHistoricos(java.awt.Frame parent, boolean modal, String chamador, Connection conexao, SessaoUsuario su) {
        super(parent, modal);
        this.chamador = chamador;
        this.conexao = conexao;
        this.su = su;
        dat = new DataSistema();
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Históricos Financeiro");
        setaInicio();
        centralizarComponente();
        mudarLinha();
    }

    // setando as variáveis iniciais
    public void setaInicio() {
        ht = new Historico();
        cht = new CHistorico(conexao, su);
        sql = "select h.cd_historico as 'Código',"
                + "h.descricao as Nome,"
                + "h.situacao as Sit"
                + " from gfchistorico as h"
                + " where h.tipo_historico = 'Fi'"
                + " order by h.cd_historico";
        buscarTodos();
        jTabHistorico.setModel(htdao);
        ajustarTabela();
    }

    // criando um listener quando mudar de linha
    private void mudarLinha() {
        // verificar tecla para captuar quando a tecla entre for pressionada
        jTabHistorico.addKeyListener(new KeyListener() {
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

        jTabHistorico.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtual = jTabHistorico.getSelectedRow();
                if (!isSalvar()) {
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
            htdao = new ConsultaModelo(conexao);
            htdao.setQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        htdao.ajustarTabela(jTabHistorico, 30, 300, 20);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
        jTexCdHistorico.setText(String.format("%s", jTabHistorico.getValueAt(indexAtual, 0)));
        jTexNomeHistorico.setText(String.format("%s", jTabHistorico.getValueAt(indexAtual, 1)));
        cdHistorico = String.format("%s", jTabHistorico.getValueAt(indexAtual, 0));
        nomeHistorico = String.format("%s", jTabHistorico.getValueAt(indexAtual, 1));
        buscarCorrelatos();
    }

    // buscar tabelas correlatos
    private void buscarCorrelatos() {
        if (!cdHistorico.isEmpty()) {
            String sql = "select * from gfchistorico where cd_historico = '"
                    + cdHistorico
                    + "'";
            try {
                cht.pesquisar(sql);
            } catch (SQLException ex) {
                Logger.getLogger(PesquisarHistoricos.class.getName()).log(Level.SEVERE, null, ex);
            }
            cht.mostrarPesquisa(getHt(), 0);
            atualizarTela();
        }
    }

    // Atualizar registros correlatos
    private void atualizarTela() {
        if ("1".equals(getHt().getDocumentoComplementa())) {
            jCheDocumentoComplementa.setSelected(true);
        } else {
            jCheDocumentoComplementa.setSelected(false);
        }
        if ("1".equals(getHt().getEmissaoComplementa())) {
            jCheEmissaoComplementa.setSelected(true);
        } else {
            jCheEmissaoComplementa.setSelected(false);
        }
        if ("1".equals(getHt().getEmpresaComplementa())) {
            jCheEmpresaComplementa.setSelected(true);
        } else {
            jCheEmpresaComplementa.setSelected(false);
        }

        jTexCadPor.setText(getHt().getUsuarioCadastro());
        jForDataCad.setText(dat.getDataConv(Date.valueOf(getHt().getDataCadastro())));
        jForHoraCad.setText(getHt().getHoraCadastro());
        if (getHt().getDataModificacao() != null) {
            jTexModifPor.setText(getHt().getUsuarioModificacao());
            jForDataModif.setText(dat.getDataConv(Date.valueOf(getHt().getDataModificacao())));
            jForHoraModif.setText(getHt().getHoraModificacao());
        }
    }

    // salvar seleção
    private void salvarSelecao() {
        this.cdHistorico = getHt().getCdHistorico();
        this.nomeHistorico = getHt().getNomeHistorico();
        dispose();
    }

    /**
     * @return the CdHistorico
     */
    public String getCdHistorico() {
        return this.cdHistorico;
    }

    /**
     * @return the nomeHistorico
     */
    public String getNomeHistorico() {
        return this.nomeHistorico;
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
        jTabHistorico = new JTable(htdao);
        jPanePesquisar = new javax.swing.JPanel();
        jLabPesquisar = new javax.swing.JLabel();
        jTexPesquisar = new javax.swing.JTextField();
        jTexCdHistorico = new javax.swing.JTextField();
        jTexNomeHistorico = new javax.swing.JTextField();
        jLabCdHistorico = new javax.swing.JLabel();
        jPanInformacoes = new javax.swing.JPanel();
        jCheDocumentoComplementa = new javax.swing.JCheckBox();
        jCheEmpresaComplementa = new javax.swing.JCheckBox();
        jCheEmissaoComplementa = new javax.swing.JCheckBox();
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Tipo Pagamento");
        setResizable(false);

        jPanTabela.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabHistorico.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.SystemColor.activeCaptionBorder, null));
        jTabHistorico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabHistorico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nome", "Sit"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabHistorico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabHistoricoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTabHistorico);
        if (jTabHistorico.getColumnModel().getColumnCount() > 0) {
            jTabHistorico.getColumnModel().getColumn(0).setResizable(false);
            jTabHistorico.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTabHistorico.getColumnModel().getColumn(1).setResizable(false);
            jTabHistorico.getColumnModel().getColumn(1).setPreferredWidth(300);
            jTabHistorico.getColumnModel().getColumn(2).setResizable(false);
            jTabHistorico.getColumnModel().getColumn(2).setPreferredWidth(20);
        }

        jPanePesquisar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabPesquisar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPesquisar.setText("Pesquisar Histórico:");

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        jTexCdHistorico.setEditable(false);
        jTexCdHistorico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexCdHistorico.setEnabled(false);

        jTexNomeHistorico.setEditable(false);
        jTexNomeHistorico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexNomeHistorico.setEnabled(false);

        jLabCdHistorico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdHistorico.setText("Histórico:");

        jPanInformacoes.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações do Histórico", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jCheDocumentoComplementa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheDocumentoComplementa.setText("Complementar c/ Nº doc.");
        jCheDocumentoComplementa.setEnabled(false);

        jCheEmpresaComplementa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheEmpresaComplementa.setText("Complementar com nome da Empresa.");
        jCheEmpresaComplementa.setEnabled(false);

        jCheEmissaoComplementa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheEmissaoComplementa.setText("Complementar com Emissão.");
        jCheEmissaoComplementa.setEnabled(false);

        javax.swing.GroupLayout jPanInformacoesLayout = new javax.swing.GroupLayout(jPanInformacoes);
        jPanInformacoes.setLayout(jPanInformacoesLayout);
        jPanInformacoesLayout.setHorizontalGroup(
            jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInformacoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheDocumentoComplementa)
                    .addComponent(jCheEmissaoComplementa))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jCheEmpresaComplementa)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanInformacoesLayout.setVerticalGroup(
            jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInformacoesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jCheDocumentoComplementa)
                    .addComponent(jCheEmpresaComplementa))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheEmissaoComplementa)
                .addContainerGap(44, Short.MAX_VALUE))
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
                    .addGap(122, 122, 122))
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

            javax.swing.GroupLayout jPanTabelaLayout = new javax.swing.GroupLayout(jPanTabela);
            jPanTabela.setLayout(jPanTabelaLayout);
            jPanTabelaLayout.setHorizontalGroup(
                jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTabelaLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanTabelaLayout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanInformacoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanTabelaLayout.createSequentialGroup()
                            .addComponent(jLabCdHistorico)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexCdHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexNomeHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, 327, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(jPanePesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jScrollPane1))
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
                        .addComponent(jLabCdHistorico)
                        .addComponent(jTexCdHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanInformacoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                    .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
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
            sql = "select h.cd_historico as 'Código',"
                    + "h.descricao as Nome,"
                    + "h.situacao as Sit"
                    + " from gfchistorico as h"
                    + " WHERE h.descricao LIKE '%"
                    + jTexPesquisar.getText().trim()
                    + "%' order by h.cd_historico";
            //indexAtual = 0;
            buscarTodos();
            jTabHistorico.setModel(htdao);
            if (jTabHistorico.getModel().getRowCount() > 0) {
                ajustarTabela();
            }
        } else {
            setaInicio();
        }
    }//GEN-LAST:event_jTexPesquisarKeyReleased

    private void jTabHistoricoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabHistoricoKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("ENTER".equals(vt.VerificarTecla(evt))) {
            JOptionPane.showMessageDialog(null, vt.VerificarTecla(evt));
            salvarSelecao();
        }
    }//GEN-LAST:event_jTabHistoricoKeyPressed

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
            java.util.logging.Logger.getLogger(PesquisarHistoricos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisarHistoricos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisarHistoricos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisarHistoricos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PesquisarHistoricos dialog = new PesquisarHistoricos(new javax.swing.JFrame(), true, chamador, conexao,su);
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
    private javax.swing.JCheckBox jCheDocumentoComplementa;
    private javax.swing.JCheckBox jCheEmissaoComplementa;
    private javax.swing.JCheckBox jCheEmpresaComplementa;
    private javax.swing.JFormattedTextField jForDataCad;
    private javax.swing.JFormattedTextField jForDataModif;
    private javax.swing.JFormattedTextField jForHoraCad;
    private javax.swing.JFormattedTextField jForHoraModif;
    private javax.swing.JLabel jLabCadPor;
    private javax.swing.JLabel jLabCdHistorico;
    private javax.swing.JLabel jLabModifPor;
    private javax.swing.JLabel jLabPesquisar;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JPanel jPanInformacoes;
    private javax.swing.JPanel jPanRodape;
    private javax.swing.JPanel jPanTabela;
    private javax.swing.JPanel jPanePesquisar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabHistorico;
    private javax.swing.JTextField jTexCadPor;
    private javax.swing.JTextField jTexCdHistorico;
    private javax.swing.JTextField jTexModifPor;
    private javax.swing.JTextField jTexNomeHistorico;
    private javax.swing.JTextField jTexPesquisar;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the ht
     */
    public Historico getHt() {
        return ht;
    }

    /**
     * @return the salvar
     */
    public boolean isSalvar() {
        return salvar;
    }

}