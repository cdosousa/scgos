/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GFCPE0081
 */
package br.com.gfc.visao;

import br.com.gfc.controle.CTipoMovimento;
import br.com.gfc.dao.TipoMovimentoDAO;
import br.com.gfc.modelo.TipoMovimento;
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
 * @version 0.01_beta0917 created on 13/03/2018
 */
public class PesquisarTipoMovimento extends javax.swing.JDialog {

    private static Connection conexao;
    private String sql;
    private TipoMovimentoDAO tmdao;
    private CTipoMovimento ctm;
    private TipoMovimento tm;
    private int indexFinal;
    private int indexAtual = 0;
    private static String chamador;
    private VerificarTecla vt;
    private String cdTipoMovimento;
    private String nomeTipoMovimento;
    private final DataSistema dat;
    private boolean salvar = false;

    /**
     * Creates new form PesquisaModelo
     */
    public PesquisarTipoMovimento(java.awt.Frame parent, boolean modal, String chamador, Connection conexao) {
        super(parent, modal);
        this.chamador = chamador;
        this.conexao = conexao;
        dat = new DataSistema();
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisa de Tipos de Movimento");
        setaInicio();
        centralizarComponente();
        mudarLinha();
    }

    // setando as variáveis iniciais
    public void setaInicio() {
        tm = new TipoMovimento();
        ctm = new CTipoMovimento(conexao);
        sql = "select p.cd_tipomovimento,"
                + "p.nome_tipomovimento,"
                + "p.cd_contareduzida,"
                + "p.sit_lancamento,"
                + "p.sit_contrapartida,"
                + "p.situacao "
                + "from gfctipomovimento as p "
                + "order by p.cd_tipomovimento";
        buscarTodos();
        jTabTipoMovimento.setModel(tmdao);
        ajustarTabela();
    }

    // criando um listener quando mudar de linha
    private void mudarLinha() {
        // verificar tecla para captuar quando a tecla entre for pressionada
        jTabTipoMovimento.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                vt = new VerificarTecla();
                if ("ENTER".equals(String.format("%s", vt.VerificarTecla(e)).toUpperCase()) && !"M".equals(chamador.toUpperCase())) {
                    salvar = true;
                    //salvarSelecao();
                    dispose();
                }
            }

            @Override
            public void keyReleased(KeyEvent e
            ) {
            }
        });

        jTabTipoMovimento.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtual = jTabTipoMovimento.getSelectedRow();
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
            tmdao = new TipoMovimentoDAO(conexao);
            tmdao.setQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        jTabTipoMovimento.getColumnModel().getColumn(0).setMinWidth(30);
        jTabTipoMovimento.getColumnModel().getColumn(0).setPreferredWidth(30);
        jTabTipoMovimento.getColumnModel().getColumn(1).setMinWidth(200);
        jTabTipoMovimento.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTabTipoMovimento.getColumnModel().getColumn(2).setMinWidth(30);
        jTabTipoMovimento.getColumnModel().getColumn(2).setPreferredWidth(30);
        jTabTipoMovimento.getColumnModel().getColumn(3).setMinWidth(60);
        jTabTipoMovimento.getColumnModel().getColumn(3).setPreferredWidth(60);
        jTabTipoMovimento.getColumnModel().getColumn(4).setMinWidth(20);
        jTabTipoMovimento.getColumnModel().getColumn(4).setPreferredWidth(20);
        jTabTipoMovimento.getColumnModel().getColumn(5).setMinWidth(20);
        jTabTipoMovimento.getColumnModel().getColumn(5).setPreferredWidth(20);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
        cdTipoMovimento = String.format("%s", jTabTipoMovimento.getValueAt(indexAtual, 0));
        nomeTipoMovimento = String.format("%s", jTabTipoMovimento.getValueAt(indexAtual, 1));
        buscarCorrelatos();
    }

    // buscar tabelas correlatos
    private void buscarCorrelatos() {
        if (!cdTipoMovimento.isEmpty()) {
            String sql = "select * from gfctipomovimento where cd_tipomovimento = '"
                    + cdTipoMovimento
                    + "'";
            try {
                ctm.pesquisar(sql);
            } catch (SQLException ex) {
                Logger.getLogger(PesquisarTipoMovimento.class.getName()).log(Level.SEVERE, null, ex);
            }
            ctm.mostrarPesquisa(tm, 0);
        }
    }

    // salvar seleção
    private void salvarSelecao() {
        this.cdTipoMovimento = tm.getCdTipoMovimento();
        this.nomeTipoMovimento = tm.getNomeTipoMovimento();
    }

    /**
     * @return the CdTipoMovimento
     */
    public String getCdTipoMovimento() {
        return this.cdTipoMovimento;
    }

    /**
     * @return the nomeTipoMovimento
     */
    public String getNomeTipoMovimento() {
        return nomeTipoMovimento;
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
        jTabTipoMovimento = new JTable(tmdao);
        jPanePesquisar = new javax.swing.JPanel();
        jLabPesquisar = new javax.swing.JLabel();
        jTexPesquisar = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Portadores");
        setResizable(false);

        jPanTabela.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabTipoMovimento.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.SystemColor.activeCaptionBorder, null));
        jTabTipoMovimento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabTipoMovimento.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cód.", "Descrição", "Conta Reduzida", "Sit Lançamento", "Sit ContraPartida", "Sit"
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
        jTabTipoMovimento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabTipoMovimentoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTabTipoMovimento);
        if (jTabTipoMovimento.getColumnModel().getColumnCount() > 0) {
            jTabTipoMovimento.getColumnModel().getColumn(0).setResizable(false);
            jTabTipoMovimento.getColumnModel().getColumn(0).setPreferredWidth(8);
            jTabTipoMovimento.getColumnModel().getColumn(1).setResizable(false);
            jTabTipoMovimento.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTabTipoMovimento.getColumnModel().getColumn(2).setResizable(false);
            jTabTipoMovimento.getColumnModel().getColumn(2).setPreferredWidth(20);
            jTabTipoMovimento.getColumnModel().getColumn(3).setResizable(false);
            jTabTipoMovimento.getColumnModel().getColumn(3).setPreferredWidth(30);
            jTabTipoMovimento.getColumnModel().getColumn(4).setResizable(false);
            jTabTipoMovimento.getColumnModel().getColumn(4).setPreferredWidth(30);
            jTabTipoMovimento.getColumnModel().getColumn(5).setResizable(false);
            jTabTipoMovimento.getColumnModel().getColumn(5).setPreferredWidth(20);
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanTabela, javax.swing.GroupLayout.PREFERRED_SIZE, 722, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTexPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexPesquisarKeyReleased
        if (!jTexPesquisar.getText().isEmpty()) {
            sql = "select p.cd_tipomovimento,"
                    + "p.nome_tipomovimento,"
                    + "p.cd_contareduzida,"
                    + "p.sit_lancamento,"
                    + "c.sit_contrapartida,"
                    + "p.situacao "
                    + "from gfctipomovimento as p  "
                    + " WHERE p.nome_tipomovimento LIKE '%"
                    + jTexPesquisar.getText().trim()
                    + "%' order by p.cd_movimento";
            //indexAtual = 0;
            buscarTodos();
            jTabTipoMovimento.setModel(tmdao);
            if (jTabTipoMovimento.getModel().getRowCount() > 0) {
                ajustarTabela();
            }
        } else {
            setaInicio();
        }
    }//GEN-LAST:event_jTexPesquisarKeyReleased

    private void jTabTipoMovimentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabTipoMovimentoKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("ENTER".equals(vt.VerificarTecla(evt))) {
            JOptionPane.showMessageDialog(null, vt.VerificarTecla(evt));
            salvarSelecao();
        }
    }//GEN-LAST:event_jTabTipoMovimentoKeyPressed

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
            java.util.logging.Logger.getLogger(PesquisarTipoMovimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisarTipoMovimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisarTipoMovimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisarTipoMovimento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PesquisarTipoMovimento dialog = new PesquisarTipoMovimento(new javax.swing.JFrame(), true, chamador, conexao);
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
    private javax.swing.JLabel jLabPesquisar;
    private javax.swing.JPanel jPanTabela;
    private javax.swing.JPanel jPanePesquisar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabTipoMovimento;
    private javax.swing.JTextField jTexPesquisar;
    // End of variables declaration//GEN-END:variables
}
