/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GFCPE0031
 */
package br.com.gfc.visao;

import br.com.gfc.controle.CContasBancarias;
import br.com.gfc.dao.ContasBancariasDAO;
import br.com.gfc.modelo.ContasBancarias;
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
public class PesquisarContasBancarias extends javax.swing.JDialog {

    private static Connection conexao;
    private String sql;
    private String cdConta;
    private ContasBancariasDAO cbdao;
    private CContasBancarias ccb;
    private ContasBancarias cb;
    private int indexFinal;
    private int indexAtual = 0;
    private static String chamador;
    private VerificarTecla vt;
    private String CdBanco;
    private String nomeBanco;
    private String CdAgencia;
    private String cdAgenciaDig;
    private String cdContaDig;
    private double txJuros;
    private final DataSistema dat;

    /**
     * Creates new form PesquisaModelo
     */
    public PesquisarContasBancarias(java.awt.Frame parent, boolean modal, String chamador, Connection conexao) {
        super(parent, modal);
        this.chamador = chamador;
        this.conexao = conexao;
        dat = new DataSistema();
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisa de Contas Bancárias");
        setaInicio();
        centralizarComponente();
        mudarLinha();
    }

    // setando as variáveis iniciais
    public void setaInicio() {
        cb = new ContasBancarias();
        ccb = new CContasBancarias(conexao);
        sql = "select c.cd_banco as Banco,"
                + "c.cd_agencia as 'Agência',"
                + "c.cd_agencia_dig as 'Agência Dig.',"
                + "c.cd_conta as Conta,"
                + "c.cd_conta_dig as 'Conta Dig.',"
                + "c.limite as Limite,"
                + "c.saldo as Saldo,"
                + "c.situacao as Sit"
                + " from gfccontas as c";
        buscarTodos();
        jTabContas.setModel(cbdao);
        ajustarTabela();
    }

    // criando um listener quando mudar de linha
    private void mudarLinha() {

        // verificar tecla para captuar quando a tecla entre for pressionada
        jTabContas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                vt = new VerificarTecla();
                if ("ENTER".equals(String.format("%s", vt.VerificarTecla(e)).toUpperCase()) && !"M".equals(chamador.toUpperCase())) {
                    salvarSelecao();
                }
            }

            @Override
            public void keyReleased(KeyEvent e
            ) {
                
            }
        });

        jTabContas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtual = jTabContas.getSelectedRow();
                ajustarTabela();
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
            cbdao = new ContasBancariasDAO(conexao);
            cbdao.setQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        jTabContas.getColumnModel().getColumn(0).setMinWidth(30);
        jTabContas.getColumnModel().getColumn(0).setPreferredWidth(30);
        jTabContas.getColumnModel().getColumn(1).setMinWidth(30);
        jTabContas.getColumnModel().getColumn(1).setPreferredWidth(30);
        jTabContas.getColumnModel().getColumn(2).setMinWidth(30);
        jTabContas.getColumnModel().getColumn(2).setPreferredWidth(30);
        jTabContas.getColumnModel().getColumn(3).setMinWidth(20);
        jTabContas.getColumnModel().getColumn(3).setPreferredWidth(20);
        jTabContas.getColumnModel().getColumn(4).setMinWidth(90);
        jTabContas.getColumnModel().getColumn(4).setPreferredWidth(90);
        jTabContas.getColumnModel().getColumn(5).setMinWidth(90);
        jTabContas.getColumnModel().getColumn(5).setPreferredWidth(90);
        jTabContas.getColumnModel().getColumn(6).setMinWidth(20);
        jTabContas.getColumnModel().getColumn(6).setPreferredWidth(20);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
        //CdBanco = String.format("%s", jTabContas.getValueAt(indexAtual, 0));
        jTexSelecao1.setText(String.format("%s", jTabContas.getValueAt(indexAtual, 1)));
        jTexSelecao2.setText(String.format("%s", jTabContas.getValueAt(indexAtual, 3)));
        //CdAgencia = jTexSelecao1.getText();
        //cdAgenciaDig = String.format("%s", jTabContas.getValueAt(indexAtual, 2));
        //cdConta = jTexSelecao2.getText();
        //cdContaDig = String.format("%s", jTabContas.getValueAt(indexAtual, 4));
        buscarCorrelatos();
    }

    // buscar tabelas correlatos
    private void buscarCorrelatos() {
        if (!jTexSelecao2.getText().isEmpty()) {
            String sql = "select * from gfccontas where cd_conta = "
                    + jTexSelecao2.getText()
                    + "";
            try {
                ccb.pesquisar(sql);
            } catch (SQLException ex) {
                Logger.getLogger(PesquisarContasBancarias.class.getName()).log(Level.SEVERE, null, ex);
            }
            ccb.mostrarPesquisa(cb, 0);
            atualizarTela();
        }
    }

    // Atualizar registros correlatos
    private void atualizarTela() {
        jTexTipoConta.setText(cb.getTipoConta());
        jTexTarifaAdm.setText(String.valueOf(cb.getTarifaAdm()));
        if (cb.getDataAbertura() != null) {
            jTexDataAbertura.setText(dat.getDataConv(Date.valueOf(cb.getDataAbertura())));
        }
        if (cb.getDataEncerrametno() != null) {
            jTexDataEncerramento.setText(dat.getDataConv(Date.valueOf(cb.getDataEncerrametno())));
        }
        jTexTxJuros.setText(String.valueOf(cb.getTaxaJuros()));
        jTexCadastradoPor.setText(cb.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(cb.getDataCadastro())));
        if (cb.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(cb.getDataModificacao())));
        }
    }

    // salvar seleção
    private void salvarSelecao() {
        CdBanco = cb.getCdBanco();
        nomeBanco = cb.getNomeBanco();
        cdConta = cb.getCdConta();
        cdContaDig = cb.getCdContaDig();
        CdAgencia = cb.getCdAgencia();
        cdAgenciaDig = cb.getCdAgenciaDig();
        txJuros = cb.getTaxaJuros();
        dispose();
    }

    /**
     * @return the CdBanco
     */
    public String getCdBanco() {
        return CdBanco;
    }

    /**
     * @return the nomeBanco
     */
    public String getNomeBanco() {
        return nomeBanco;
    }

    /**
     * @return the CdAgencia
     */
    public String getCdAgencia() {
        return CdAgencia;
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

    /**
     * @return the txJuros
     */
    public double getTxJuros() {
        return txJuros;
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
        jTabContas = new JTable(cbdao);
        jPanePesquisar = new javax.swing.JPanel();
        jLabPesquisar = new javax.swing.JLabel();
        jTexPesquisar = new javax.swing.JTextField();
        jPanResumo = new javax.swing.JPanel();
        jLabConta = new javax.swing.JLabel();
        jTexSelecao1 = new javax.swing.JTextField();
        jTexSelecao2 = new javax.swing.JTextField();
        jPanInformacoes = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTexTipoConta = new javax.swing.JTextField();
        jLabTarifaAdm = new javax.swing.JLabel();
        jTexTarifaAdm = new FormatarValor(FormatarValor.NUMERO);
        jLabComplemento = new javax.swing.JLabel();
        jTexDataAbertura = new javax.swing.JTextField();
        jLabBairro = new javax.swing.JLabel();
        jTexDataEncerramento = new javax.swing.JTextField();
        jLabTxJuros = new javax.swing.JLabel();
        jTexTxJuros = new FormatarValor(FormatarValor.NUMERO);
        jPanel1 = new javax.swing.JPanel();
        jLabCadastradoPor = new javax.swing.JLabel();
        jLabDataCadastro = new javax.swing.JLabel();
        jLabDataModificacao = new javax.swing.JLabel();
        jForDataCadastro = new javax.swing.JFormattedTextField();
        jTexCadastradoPor = new javax.swing.JTextField();
        jForDataModificacao = new javax.swing.JFormattedTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Contas Bancárias");
        setResizable(false);

        jPanTabela.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabContas.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.SystemColor.activeCaptionBorder, null));
        jTabContas.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabContas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Banco", "Agência", "Agência Díg.", "Conta", "Conta Díg.", "Limite", "Saldo", "Sit"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTabContas);
        if (jTabContas.getColumnModel().getColumnCount() > 0) {
            jTabContas.getColumnModel().getColumn(0).setResizable(false);
            jTabContas.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTabContas.getColumnModel().getColumn(1).setResizable(false);
            jTabContas.getColumnModel().getColumn(1).setPreferredWidth(30);
            jTabContas.getColumnModel().getColumn(2).setResizable(false);
            jTabContas.getColumnModel().getColumn(2).setPreferredWidth(30);
            jTabContas.getColumnModel().getColumn(3).setResizable(false);
            jTabContas.getColumnModel().getColumn(3).setPreferredWidth(60);
            jTabContas.getColumnModel().getColumn(4).setResizable(false);
            jTabContas.getColumnModel().getColumn(4).setPreferredWidth(20);
            jTabContas.getColumnModel().getColumn(5).setResizable(false);
            jTabContas.getColumnModel().getColumn(5).setPreferredWidth(90);
            jTabContas.getColumnModel().getColumn(6).setResizable(false);
            jTabContas.getColumnModel().getColumn(6).setPreferredWidth(90);
            jTabContas.getColumnModel().getColumn(7).setResizable(false);
            jTabContas.getColumnModel().getColumn(7).setPreferredWidth(20);
        }

        jPanePesquisar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabPesquisar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPesquisar.setText("Pesquisar Conta:");

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

        jLabConta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabConta.setText("Agência/Conta");

        jTexSelecao1.setEditable(false);
        jTexSelecao1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexSelecao1.setEnabled(false);

        jTexSelecao2.setEditable(false);
        jTexSelecao2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexSelecao2.setEnabled(false);

        jPanInformacoes.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações da Conta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Tipo:");

        jTexTipoConta.setEditable(false);
        jTexTipoConta.setEnabled(false);

        jLabTarifaAdm.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTarifaAdm.setText("Tarifa Adm.:");

        jTexTarifaAdm.setEditable(false);
        jTexTarifaAdm.setEnabled(false);

        jLabComplemento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabComplemento.setText("Data Abertura:");

        jTexDataAbertura.setEditable(false);
        jTexDataAbertura.setEnabled(false);

        jLabBairro.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabBairro.setText("Data Encerramento:");

        jTexDataEncerramento.setEditable(false);
        jTexDataEncerramento.setEnabled(false);

        jLabTxJuros.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTxJuros.setText("Tx. Juros:");

        jTexTxJuros.setEditable(false);
        jTexTxJuros.setEnabled(false);

        javax.swing.GroupLayout jPanInformacoesLayout = new javax.swing.GroupLayout(jPanInformacoes);
        jPanInformacoes.setLayout(jPanInformacoesLayout);
        jPanInformacoesLayout.setHorizontalGroup(
            jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInformacoesLayout.createSequentialGroup()
                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanInformacoesLayout.createSequentialGroup()
                        .addComponent(jLabComplemento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexDataAbertura, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabBairro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexDataEncerramento, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanInformacoesLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(8, 8, 8)
                        .addComponent(jTexTipoConta, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabTarifaAdm)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexTarifaAdm, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabTxJuros)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTexTxJuros, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(199, Short.MAX_VALUE))
        );
        jPanInformacoesLayout.setVerticalGroup(
            jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInformacoesLayout.createSequentialGroup()
                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTexTipoConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jLabTarifaAdm)
                    .addComponent(jTexTarifaAdm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabTxJuros)
                    .addComponent(jTexTxJuros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabComplemento)
                    .addComponent(jTexDataAbertura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabBairro)
                    .addComponent(jTexDataEncerramento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(42, Short.MAX_VALUE))
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

        javax.swing.GroupLayout jPanResumoLayout = new javax.swing.GroupLayout(jPanResumo);
        jPanResumo.setLayout(jPanResumoLayout);
        jPanResumoLayout.setHorizontalGroup(
            jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanResumoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanResumoLayout.createSequentialGroup()
                        .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanInformacoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanResumoLayout.createSequentialGroup()
                                .addComponent(jLabConta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanResumoLayout.setVerticalGroup(
            jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanResumoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabConta)
                    .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
            sql = "select c.cd_banco as Banco,"
                    + "c.cd_agencia as 'Agência',"
                    + "c.cd_agencia_dig as 'Agência Dig.',"
                    + "c.cd_conta as Conta,"
                    + "c.cd_conta_dig as 'Conta Dig.',"
                    + "c.limite as Limite,"
                    + "c.saldo as Saldo,"
                    + "c.situacao as Sit"
                    + " from gfccontas as c"
                    + " WHERE cd_conta LIKE '%"
                    + jTexPesquisar.getText().trim()
                    + "%'";
            //indexAtual = 0;
            buscarTodos();
            jTabContas.setModel(cbdao);
            if (jTabContas.getModel().getRowCount() > 0) {
                ajustarTabela();
            }
        } else {
            setaInicio();
        }
    }//GEN-LAST:event_jTexPesquisarKeyReleased

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
            java.util.logging.Logger.getLogger(PesquisarContasBancarias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisarContasBancarias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisarContasBancarias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisarContasBancarias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PesquisarContasBancarias dialog = new PesquisarContasBancarias(new javax.swing.JFrame(), true, chamador, conexao);
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
    private javax.swing.JLabel jLabBairro;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabComplemento;
    private javax.swing.JLabel jLabConta;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabPesquisar;
    private javax.swing.JLabel jLabTarifaAdm;
    private javax.swing.JLabel jLabTxJuros;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanInformacoes;
    private javax.swing.JPanel jPanResumo;
    private javax.swing.JPanel jPanTabela;
    private javax.swing.JPanel jPanePesquisar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabContas;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexDataAbertura;
    private javax.swing.JTextField jTexDataEncerramento;
    private javax.swing.JTextField jTexPesquisar;
    private javax.swing.JTextField jTexSelecao1;
    private javax.swing.JTextField jTexSelecao2;
    private javax.swing.JTextField jTexTarifaAdm;
    private javax.swing.JTextField jTexTipoConta;
    private javax.swing.JTextField jTexTxJuros;
    // End of variables declaration//GEN-END:variables

}
