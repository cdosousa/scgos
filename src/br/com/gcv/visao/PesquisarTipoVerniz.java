/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GCVPE0041
 */
package br.com.gcv.visao;

import br.com.gcv.controle.CTipoVerniz;
import br.com.gcv.dao.TipoVernizDAO;
import br.com.gcv.modelo.TipoVerniz;
import br.com.modelo.VerificarTecla;
import br.com.modelo.DataSistema;
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
 * @version 0.01_beta0917 created on 04/12/2017
 */
public class PesquisarTipoVerniz extends javax.swing.JDialog {

    private static Connection conexao;
    private String sql;
    private String cdTipoVerniz;
    private String nomeTipoVerniz;
    private TipoVernizDAO tvzdao;
    private CTipoVerniz ctvz;
    private TipoVerniz tvz;
    private int indexFinal;
    private int indexAtual = 0;
    private static String chamador;
    private VerificarTecla vt;
    private final DataSistema dat;
    private boolean salvar = false;
        

    /**
     * Creates new form PesquisaModelo
     */
    public PesquisarTipoVerniz(java.awt.Frame parent, boolean modal, String chamador, Connection conexao) {
        super(parent, modal);
        this.chamador = chamador;
        this.conexao = conexao;
        dat = new DataSistema();
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Tipo de Verniz");
        setaInicio();
        centralizarComponente();
        mudarLinha();
    }

    // setando as variáveis iniciais
    public void setaInicio() {
        tvz = new TipoVerniz();
        ctvz = new CTipoVerniz(conexao);
        sql = "select t.cd_tipoverniz as 'Cod.',"
                + " t.nome_tipoverniz as Nome,"
                + " t.situacao as 'Situação'"
                + " from gcvtipoverniz as t"
                + " order by t.nome_tipoverniz";
        buscarTodos();
        jTabTipoVerniz.setModel(tvzdao);
        ajustarTabela();
    }

    // criando um listener quando mudar de linha
    private void mudarLinha() {
        // verificar tecla para captuar quando a tecla entre for pressionada
        jTabTipoVerniz.addKeyListener(new KeyListener() {
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

        jTabTipoVerniz.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!salvar) {
                    indexAtual = jTabTipoVerniz.getSelectedRow();
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
            tvzdao = new TipoVernizDAO(conexao);
            tvzdao.setQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!Err: " + ex);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        jTabTipoVerniz.getColumnModel().getColumn(0).setMinWidth(50);
        jTabTipoVerniz.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTabTipoVerniz.getColumnModel().getColumn(1).setMinWidth(300);
        jTabTipoVerniz.getColumnModel().getColumn(1).setPreferredWidth(300);
        jTabTipoVerniz.getColumnModel().getColumn(2).setMinWidth(20);
        jTabTipoVerniz.getColumnModel().getColumn(2).setPreferredWidth(20);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
        jTexSelecao1.setText(String.format("%s", jTabTipoVerniz.getValueAt(indexAtual, 0)));
        jTexSelecao2.setText(String.format("%s", jTabTipoVerniz.getValueAt(indexAtual, 1)));
        cdTipoVerniz = String.format("%s", jTabTipoVerniz.getValueAt(indexAtual, 0));
        buscarCorrelatos();
    }

    // buscar tabelas correlatos
    private void buscarCorrelatos() {
        if (!cdTipoVerniz.isEmpty()) {
            String sql = "select * from gcvtipoverniz where cd_tipoverniz = '"
                    + cdTipoVerniz.trim()
                    + "'";
            try {
                ctvz.pesquisar(sql);
            } catch (SQLException ex) {
                Logger.getLogger(PesquisarTipoVerniz.class.getName()).log(Level.SEVERE, null, ex);
            }
            ctvz.mostrarPesquisa(tvz, 0);
            atualizarTela();
        }
    }

    // Atualizar registros correlatos
    private void atualizarTela() {
        jTexSelecao1.setText(tvz.getCdTipoVerniz());
        jTexSelecao2.setText(tvz.getNomeTipoVerniz());
        jTexCadastradoPor.setText(tvz.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(tvz.getDataCadastro())));
        if (tvz.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(tvz.getDataModificacao())));
        }
    }

    // salvar seleção
    private void salvarSelecao() {
        cdTipoVerniz = jTexSelecao1.getText();
        nomeTipoVerniz = jTexSelecao2.getText();
        dispose();
    }

    // método para retornar o tipo de operação pesquisado
    public String getCdTipoVerniz() {
        return cdTipoVerniz;
    }

    // Método para retorno o nome do tipo de operação pesquisado
    public String getNomeTipoVerniz() {
        return nomeTipoVerniz;
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
        jTabTipoVerniz = new JTable(tvzdao);
        jPanePesquisar = new javax.swing.JPanel();
        jLabPesquisar = new javax.swing.JLabel();
        jTexPesquisar = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabCdTipoVerniz = new javax.swing.JLabel();
        jTexSelecao1 = new javax.swing.JTextField();
        jTexSelecao2 = new javax.swing.JTextField();
        jPanBotoes = new javax.swing.JPanel();
        jLabDataCadastro = new javax.swing.JLabel();
        jForDataCadastro = new javax.swing.JFormattedTextField();
        jLabDataModificacao = new javax.swing.JLabel();
        jForDataModificacao = new javax.swing.JFormattedTextField();
        jTexRegAtual = new javax.swing.JTextField();
        jTexRegTotal = new javax.swing.JTextField();
        jLabReg = new javax.swing.JLabel();
        jLabCadastradoPor = new javax.swing.JLabel();
        jTexCadastradoPor = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Tipo de Verniz");
        setResizable(false);

        jPanTabela.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabTipoVerniz.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.SystemColor.activeCaptionBorder, null));
        jTabTipoVerniz.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabTipoVerniz.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cod.", "Nome", "Sit"
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
        jTabTipoVerniz.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabTipoVernizKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTabTipoVerniz);
        if (jTabTipoVerniz.getColumnModel().getColumnCount() > 0) {
            jTabTipoVerniz.getColumnModel().getColumn(0).setResizable(false);
            jTabTipoVerniz.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTabTipoVerniz.getColumnModel().getColumn(1).setResizable(false);
            jTabTipoVerniz.getColumnModel().getColumn(1).setPreferredWidth(300);
            jTabTipoVerniz.getColumnModel().getColumn(2).setResizable(false);
            jTabTipoVerniz.getColumnModel().getColumn(2).setPreferredWidth(20);
        }

        jPanePesquisar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabPesquisar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPesquisar.setText("Pesquisar Tipo de Verniz:");

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
                    .addComponent(jTexPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(13, Short.MAX_VALUE))
        );
        jPanePesquisarLayout.setVerticalGroup(
            jPanePesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanePesquisarLayout.createSequentialGroup()
                .addComponent(jLabPesquisar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabCdTipoVerniz.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdTipoVerniz.setText("Tp. Verniz:");

        jTexSelecao1.setEditable(false);
        jTexSelecao1.setEnabled(false);

        jTexSelecao2.setEditable(false);
        jTexSelecao2.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabCdTipoVerniz)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexSelecao2, javax.swing.GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCdTipoVerniz)
                    .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanTabelaLayout = new javax.swing.GroupLayout(jPanTabela);
        jPanTabela.setLayout(jPanTabelaLayout);
        jPanTabelaLayout.setHorizontalGroup(
            jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanBotoes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabDataCadastro.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDataCadastro.setText("Cadastro em:");

        jForDataCadastro.setEditable(false);
        jForDataCadastro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataCadastro.setEnabled(false);

        jLabDataModificacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDataModificacao.setText("Modificado em:");

        jForDataModificacao.setEditable(false);
        jForDataModificacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataModificacao.setEnabled(false);

        jTexRegAtual.setEditable(false);
        jTexRegAtual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexRegAtual.setEnabled(false);

        jTexRegTotal.setEditable(false);
        jTexRegTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexRegTotal.setEnabled(false);

        jLabReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabReg.setText("\\");

            jLabCadastradoPor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCadastradoPor.setText("Cadastrado por:");

            jTexCadastradoPor.setEditable(false);
            jTexCadastradoPor.setEnabled(false);

            javax.swing.GroupLayout jPanBotoesLayout = new javax.swing.GroupLayout(jPanBotoes);
            jPanBotoes.setLayout(jPanBotoesLayout);
            jPanBotoesLayout.setHorizontalGroup(
                jPanBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanBotoesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTexRegAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabReg, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexRegTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabCadastradoPor)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabDataCadastro)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabDataModificacao)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(97, 97, 97))
            );
            jPanBotoesLayout.setVerticalGroup(
                jPanBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanBotoesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabDataModificacao)
                        .addComponent(jLabDataCadastro)
                        .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexRegAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexRegTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabReg)
                        .addComponent(jLabCadastradoPor)
                        .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanBotoes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 627, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jTexPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexPesquisarKeyReleased
        if (!jTexPesquisar.getText().isEmpty()) {
            sql = "select t.cd_tipoverniz as 'Cod.',"
                    + " t.nome_tipoverniz as Nome,"
                    + " t.situacao as 'Situação'"
                    + " from gcvtipoverniz as t"
                    + " WHERE t.nome_tipoverniz LIKE '%"
                    + jTexPesquisar.getText().trim()
                    + "%' order by t.nome_tipoverniz";
            //indexAtual = 0;
            buscarTodos();
            jTabTipoVerniz.setModel(tvzdao);
            if (jTabTipoVerniz.getModel().getRowCount() > 0) {
                ajustarTabela();
            }
        } else {
            setaInicio();
        }
    }//GEN-LAST:event_jTexPesquisarKeyReleased

    private void jTabTipoVernizKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabTipoVernizKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("ENTER".equals(vt.VerificarTecla(evt))) {
            JOptionPane.showMessageDialog(null, vt.VerificarTecla(evt));
            salvarSelecao();
        }
    }//GEN-LAST:event_jTabTipoVernizKeyPressed

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
            java.util.logging.Logger.getLogger(PesquisarTipoVerniz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisarTipoVerniz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisarTipoVerniz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisarTipoVerniz.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PesquisarTipoVerniz dialog = new PesquisarTipoVerniz(new javax.swing.JFrame(), true, chamador, conexao);
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
    private javax.swing.JLabel jLabCdTipoVerniz;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabPesquisar;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JPanel jPanBotoes;
    private javax.swing.JPanel jPanTabela;
    private javax.swing.JPanel jPanePesquisar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabTipoVerniz;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexPesquisar;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JTextField jTexSelecao1;
    private javax.swing.JTextField jTexSelecao2;
    // End of variables declaration//GEN-END:variables

}
