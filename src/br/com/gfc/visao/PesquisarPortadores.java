/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GFCPE0041
 */
package br.com.gfc.visao;

import br.com.gfc.controle.CPortadores;
import br.com.gfc.dao.PortadoresDAO;
import br.com.gfc.modelo.Portadores;
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
 * @version 0.01_beta0917 created on 07/11/2017
 */
public class PesquisarPortadores extends javax.swing.JDialog {

    private static Connection conexao;
    private String sql;
    private PortadoresDAO pordao;
    private CPortadores cpor;
    private Portadores por;
    private int indexFinal;
    private int indexAtual = 0;
    private static String chamador;
    private VerificarTecla vt;
    private String cdPortador;
    private String nomePortador;
    private String cdBanco;
    private String nomeBanco;
    private String cdAgencia;
    private String cdAgenciaDig;
    private String cdConta;
    private String cdContaDig;
    private int diasCartorio;
    private int diasLiquidacao;
    private double txJuros;
    private double txMulta;
    private double txCorrecao;
    private final DataSistema dat;
    private boolean salvar = false;

    /**
     * Creates new form PesquisaModelo
     */
    public PesquisarPortadores(java.awt.Frame parent, boolean modal, String chamador, Connection conexao) {
        super(parent, modal);
        this.chamador = chamador;
        this.conexao = conexao;
        dat = new DataSistema();
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisa de Portadores");
        setaInicio();
        centralizarComponente();
        mudarLinha();
    }

    // setando as variáveis iniciais
    public void setaInicio() {
        por = new Portadores();
        cpor = new CPortadores(conexao);
        sql = "select p.cd_portador,"
                + "p.nome_portador,"
                + "p.cd_banco,"
                + "p.cd_conta,"
                + "c.cd_conta_dig,"
                + "p.situacao "
                + "from gfcportador as p "
                + "left outer join gfccontas as c on p.cd_banco = c.cd_banco and p.cd_conta = c.cd_conta"
                + " order by p.cd_portador";
        buscarTodos();
        jTabPortador.setModel(pordao);
        ajustarTabela();
    }

    // criando um listener quando mudar de linha
    private void mudarLinha() {
        // verificar tecla para captuar quando a tecla entre for pressionada
        jTabPortador.addKeyListener(new KeyListener() {
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

        jTabPortador.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtual = jTabPortador.getSelectedRow();
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
            pordao = new PortadoresDAO(conexao);
            pordao.setQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        jTabPortador.getColumnModel().getColumn(0).setMinWidth(30);
        jTabPortador.getColumnModel().getColumn(0).setPreferredWidth(30);
        jTabPortador.getColumnModel().getColumn(1).setMinWidth(200);
        jTabPortador.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTabPortador.getColumnModel().getColumn(2).setMinWidth(30);
        jTabPortador.getColumnModel().getColumn(2).setPreferredWidth(30);
        jTabPortador.getColumnModel().getColumn(3).setMinWidth(60);
        jTabPortador.getColumnModel().getColumn(3).setPreferredWidth(60);
        jTabPortador.getColumnModel().getColumn(4).setMinWidth(20);
        jTabPortador.getColumnModel().getColumn(4).setPreferredWidth(20);
        jTabPortador.getColumnModel().getColumn(5).setMinWidth(20);
        jTabPortador.getColumnModel().getColumn(5).setPreferredWidth(20);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
        jTexSelecao1.setText(String.format("%s", jTabPortador.getValueAt(indexAtual, 0)));
        jTexSelecao2.setText(String.format("%s", jTabPortador.getValueAt(indexAtual, 1)));
        jTexSelecao3.setText(String.format("%s", jTabPortador.getValueAt(indexAtual, 2)));
        cdPortador = String.format("%s", jTabPortador.getValueAt(indexAtual, 0));
        nomePortador = String.format("%s", jTabPortador.getValueAt(indexAtual, 1));
        buscarCorrelatos();
    }

    // buscar tabelas correlatos
    private void buscarCorrelatos() {
        if (!cdPortador.isEmpty()) {
            String sql = "select * from gfcportador where cd_portador = "
                    + cdPortador
                    + "";
            try {
                cpor.pesquisar(sql);
            } catch (SQLException ex) {
                Logger.getLogger(PesquisarPortadores.class.getName()).log(Level.SEVERE, null, ex);
            }
            cpor.mostrarPesquisa(por, 0);
            atualizarTela();
        }
    }

    // Atualizar registros correlatos
    private void atualizarTela() {
        jTexSelecao4.setText(por.getNomeBanco());
        jTexTipoConta.setText(por.getTipoConta());
        jTexTxMulta.setText(String.valueOf(por.getTaxaMulta()));
        jTxCorrecao.setText(String.valueOf(por.getTaxaCorrecao()));
        jTexTxJuros.setText(String.valueOf(por.getTaxaJuros()));
        txJuros = por.getTaxaJuros();
        jTexDiasLiquidacao.setText(String.valueOf(por.getDiasLiquidacao()));
        jTexDiasCartorio.setText(String.valueOf(por.getDiasCartorio()));
        jTexCadastradoPor.setText(por.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(por.getDataCadastro())));
        if (por.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(por.getDataModificacao())));
        }
    }

    // salvar seleção
    private void salvarSelecao() {
        this.cdPortador = por.getCdPortador();
        this.nomePortador = por.getNomePortador();
        this.cdBanco = por.getCdBanco();
        this.nomeBanco = por.getNomeBanco();
        this.cdAgencia = por.getCdAgencia();
        this.cdAgenciaDig = por.getCdAgenciaDig();
        this.cdConta = por.getCdConta();
        this.cdContaDig = por.getCdAgenciaDig();
        this.diasCartorio = por.getDiasCartorio();
        this.diasLiquidacao = por.getDiasLiquidacao();
        this.txJuros = por.getTaxaJuros();
        this.txMulta = por.getTaxaMulta();
        this.txCorrecao = por.getTaxaCorrecao();
        dispose();
    }

    /**
     * @return the CdPortador
     */
    public String getCdPortador() {
        return this.cdPortador;
    }

    /**
     * @return the nomePortador
     */
    public String getNomePortador() {
        return nomePortador;
    }

    /**
     * @return the diasCartorio
     */
    public int getDiasCartorio() {
        return diasCartorio;
    }

    /**
     * @return the diasLiquidacao
     */
    public int getDiasLiquidacao() {
        return diasLiquidacao;
    }

    /**
     * @return the txJuros
     */
    public double getTxJuros() {
        return txJuros;
    }

    /**
     * @return the txMulta
     */
    public double getTxMulta() {
        return txMulta;
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
        jTabPortador = new JTable(pordao);
        jPanePesquisar = new javax.swing.JPanel();
        jLabPesquisar = new javax.swing.JLabel();
        jTexPesquisar = new javax.swing.JTextField();
        jPanResumo = new javax.swing.JPanel();
        jLabPortador = new javax.swing.JLabel();
        jTexSelecao1 = new javax.swing.JTextField();
        jTexSelecao2 = new javax.swing.JTextField();
        jPanInformacoes = new javax.swing.JPanel();
        jLabTipoConta = new javax.swing.JLabel();
        jTexTipoConta = new javax.swing.JTextField();
        jLabTxMulta = new javax.swing.JLabel();
        jTexTxMulta = new FormatarValor(FormatarValor.NUMERO);
        jLabDiasLiquidacao = new javax.swing.JLabel();
        jTexDiasLiquidacao = new javax.swing.JTextField();
        jLabDiasCartorio = new javax.swing.JLabel();
        jTexDiasCartorio = new javax.swing.JTextField();
        jLabTxJuros = new javax.swing.JLabel();
        jTexTxJuros = new FormatarValor(FormatarValor.NUMERO);
        jLabel2 = new javax.swing.JLabel();
        jTxCorrecao = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabCadastradoPor = new javax.swing.JLabel();
        jLabDataCadastro = new javax.swing.JLabel();
        jLabDataModificacao = new javax.swing.JLabel();
        jForDataCadastro = new javax.swing.JFormattedTextField();
        jTexCadastradoPor = new javax.swing.JTextField();
        jForDataModificacao = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jTexSelecao3 = new javax.swing.JTextField();
        jTexSelecao4 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Portadores");
        setResizable(false);

        jPanTabela.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabPortador.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.SystemColor.activeCaptionBorder, null));
        jTabPortador.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabPortador.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Portador", "Nome", "Banco", "Conta", "Conta Díg.", "Sit"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabPortador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabPortadorKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTabPortador);
        if (jTabPortador.getColumnModel().getColumnCount() > 0) {
            jTabPortador.getColumnModel().getColumn(0).setResizable(false);
            jTabPortador.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTabPortador.getColumnModel().getColumn(1).setResizable(false);
            jTabPortador.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTabPortador.getColumnModel().getColumn(2).setResizable(false);
            jTabPortador.getColumnModel().getColumn(2).setPreferredWidth(30);
            jTabPortador.getColumnModel().getColumn(3).setResizable(false);
            jTabPortador.getColumnModel().getColumn(3).setPreferredWidth(60);
            jTabPortador.getColumnModel().getColumn(4).setResizable(false);
            jTabPortador.getColumnModel().getColumn(4).setPreferredWidth(20);
            jTabPortador.getColumnModel().getColumn(5).setResizable(false);
            jTabPortador.getColumnModel().getColumn(5).setPreferredWidth(20);
        }

        jPanePesquisar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabPesquisar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPesquisar.setText("Pesquisar Portador:");

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
                    .addComponent(jTexPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 674, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(11, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanTabelaLayout = new javax.swing.GroupLayout(jPanTabela);
        jPanTabela.setLayout(jPanTabelaLayout);
        jPanTabelaLayout.setHorizontalGroup(
            jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanePesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanTabelaLayout.setVerticalGroup(
            jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanePesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanResumo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabPortador.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPortador.setText("Portador/Banco:");

        jTexSelecao1.setEditable(false);
        jTexSelecao1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexSelecao1.setEnabled(false);

        jTexSelecao2.setEditable(false);
        jTexSelecao2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexSelecao2.setEnabled(false);

        jPanInformacoes.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações do Portador", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabTipoConta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTipoConta.setText("Tipo:");

        jTexTipoConta.setEditable(false);
        jTexTipoConta.setEnabled(false);

        jLabTxMulta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTxMulta.setText("Tx. Multa:");

        jTexTxMulta.setEditable(false);
        jTexTxMulta.setEnabled(false);

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

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Tx. Correção:");

        jTxCorrecao.setEditable(false);
        jTxCorrecao.setEnabled(false);

        javax.swing.GroupLayout jPanInformacoesLayout = new javax.swing.GroupLayout(jPanInformacoes);
        jPanInformacoes.setLayout(jPanInformacoesLayout);
        jPanInformacoesLayout.setHorizontalGroup(
            jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInformacoesLayout.createSequentialGroup()
                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanInformacoesLayout.createSequentialGroup()
                        .addComponent(jLabDiasLiquidacao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexDiasLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabDiasCartorio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexDiasCartorio, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanInformacoesLayout.createSequentialGroup()
                        .addComponent(jLabTipoConta)
                        .addGap(8, 8, 8)
                        .addComponent(jTexTipoConta, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabTxJuros)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexTxJuros, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabTxMulta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexTxMulta, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTxCorrecao, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        jPanInformacoesLayout.setVerticalGroup(
            jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInformacoesLayout.createSequentialGroup()
                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTexTipoConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabTipoConta)
                    .addComponent(jLabTxMulta)
                    .addComponent(jTexTxMulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabTxJuros)
                    .addComponent(jTexTxJuros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTxCorrecao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabDiasLiquidacao)
                    .addComponent(jTexDiasLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabDiasCartorio)
                    .addComponent(jTexDiasCartorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addGap(18, 18, 18)
                .addComponent(jLabDataCadastro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabDataModificacao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("/");

        jTexSelecao3.setEditable(false);
        jTexSelecao3.setEnabled(false);

        jTexSelecao4.setEditable(false);
        jTexSelecao4.setEnabled(false);

        javax.swing.GroupLayout jPanResumoLayout = new javax.swing.GroupLayout(jPanResumo);
        jPanResumo.setLayout(jPanResumoLayout);
        jPanResumoLayout.setHorizontalGroup(
            jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanResumoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanResumoLayout.createSequentialGroup()
                        .addComponent(jLabPortador)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, 201, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexSelecao3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexSelecao4, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanInformacoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanResumoLayout.setVerticalGroup(
            jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanResumoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabPortador)
                    .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jTexSelecao3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexSelecao4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4)
                .addComponent(jPanInformacoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanResumo, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            .addComponent(jPanTabela, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 722, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanResumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTexPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexPesquisarKeyReleased
        if (!jTexPesquisar.getText().isEmpty()) {
            sql = "select p.cd_portador,"
                    + "p.nome_portador,"
                    + "p.cd_banco,"
                    + "p.cd_conta,"
                    + "c.cd_conta_dig,"
                    + "p.situacao "
                    + "from gfcportador as p "
                    + "left outer join gfccontas as c on p.cd_banco = c.cd_banco and p.cd_conta = c.cd_conta"
                    + " WHERE p.nome_portador LIKE '%"
                    + jTexPesquisar.getText().trim()
                    + "%' order by p.cd_portador";
            //indexAtual = 0;
            buscarTodos();
            jTabPortador.setModel(pordao);
            if (jTabPortador.getModel().getRowCount() > 0) {
                ajustarTabela();
            }
        } else {
            setaInicio();
        }
    }//GEN-LAST:event_jTexPesquisarKeyReleased

    private void jTabPortadorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabPortadorKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("ENTER".equals(vt.VerificarTecla(evt))) {
            JOptionPane.showMessageDialog(null, vt.VerificarTecla(evt));
            salvarSelecao();
        }
    }//GEN-LAST:event_jTabPortadorKeyPressed

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
            java.util.logging.Logger.getLogger(PesquisarPortadores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisarPortadores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisarPortadores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisarPortadores.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PesquisarPortadores dialog = new PesquisarPortadores(new javax.swing.JFrame(), true, chamador, conexao);
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
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabDiasCartorio;
    private javax.swing.JLabel jLabDiasLiquidacao;
    private javax.swing.JLabel jLabPesquisar;
    private javax.swing.JLabel jLabPortador;
    private javax.swing.JLabel jLabTipoConta;
    private javax.swing.JLabel jLabTxJuros;
    private javax.swing.JLabel jLabTxMulta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanInformacoes;
    private javax.swing.JPanel jPanResumo;
    private javax.swing.JPanel jPanTabela;
    private javax.swing.JPanel jPanePesquisar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabPortador;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexDiasCartorio;
    private javax.swing.JTextField jTexDiasLiquidacao;
    private javax.swing.JTextField jTexPesquisar;
    private javax.swing.JTextField jTexSelecao1;
    private javax.swing.JTextField jTexSelecao2;
    private javax.swing.JTextField jTexSelecao3;
    private javax.swing.JTextField jTexSelecao4;
    private javax.swing.JTextField jTexTipoConta;
    private javax.swing.JTextField jTexTxJuros;
    private javax.swing.JTextField jTexTxMulta;
    private javax.swing.JTextField jTxCorrecao;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the txCorrecao
     */
    public double getTxCorrecao() {
        return txCorrecao;
    }

    /**
     * @return the cdBanco
     */
    public String getCdBanco() {
        return cdBanco;
    }

    /**
     * @return the nomeBanco
     */
    public String getNomeBanco() {
        return nomeBanco;
    }

    /**
     * @return the cdAgencia
     */
    public String getCdAgencia() {
        return cdAgencia;
    }

    /**
     * @return the cdAgenciaDig
     */
    public String getCdAgenciaDig() {
        return cdAgenciaDig;
    }

    /**
     * @return the cdConta
     */
    public String getCdConta() {
        return cdConta;
    }

    /**
     * @return the cdContaDig
     */
    public String getCdContaDig() {
        return cdContaDig;
    }

}
