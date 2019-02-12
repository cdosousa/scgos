/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.visao;

import br.com.DAO.ConsultaModelo;
import br.com.DAO.EmpresaDAO;
import br.com.gsm.controle.CTecnicos;
import br.com.gsm.modelo.Tecnicos;
import br.com.modelo.DataSistema;
import br.com.modelo.Empresa;
import br.com.modelo.HoraSistema;
import br.com.modelo.SessaoUsuario;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @created on 24/09/2018
 */
public class ExportarTecnicosParaEmpresa extends javax.swing.JFrame {

    /**
     * Classe criada para poder exportar os funcionáios já cadastrados para a
     * tabela de empresas. Creates new form ExportarClientesParaEmpresa
     */
    //Objetos de instância da classe
    static Connection conexao;
    static SessaoUsuario su;
    DataSistema dat;
    HoraSistema hs;

    // Objetos da classe
    Tecnicos tec;
    CTecnicos ctec;
    Empresa emp;
    ConsultaModelo con;

    //Variáveis de controle da classe
    int numtec;
    int numExp;
    int idxAtual;
    String sqlTec;

    public ExportarTecnicosParaEmpresa(Connection conexao, SessaoUsuario su) {
        this.conexao = conexao;
        this.su = su;
        initComponents();
    }

    /**
     * Método para pesquisar os clientes e retornar na tela
     */
    private void pesquisarTecnicos() {
        numtec = 0;
        numExp = 0;
        sqlTec = "select t.cpf as Codigo,"
                + "t.nome_tecnico as Nome,"
                + "t.rg as Documento"
                + " from gsmtecnicos as t"
                + " where not exists (select p.cpf_cnpj"
                + " from pgsempresa as p"
                + " where p.cpf_cnpj = t.cpf)";
        try {
            con = new ConsultaModelo(conexao);
            con.setQuery(sqlTec);
            numtec = con.getRowCount();
            if (numtec > 0) {
                jTabTecnicos.setModel(con);
                upTela();
            }
        } catch (SQLException ex) {
            messagem("Erro na busca dos clientes!\nErro: " + ex);
        }
    }

    /**
     * Médodo para atualizar a tela
     */
    private void upTela() {
        jForTecEncontrados.setText(String.valueOf(numtec));
        jForTecExportados.setText(String.valueOf(numExp));
    }

    /**
     * Método para gravar os clientes na tabalea de empresa
     */
    private void exportarTecnicos() {
        sqlTec = "select * "
                + " from gsmtecnicos as t"
                + " where not exists (select p.cpf_cnpj"
                + " from pgsempresa as p"
                + " where p.cpf_cnpj = t.cpf)";
        try {
            tec = new Tecnicos();
            ctec = new CTecnicos(conexao);
            numtec = ctec.pesquisar(sqlTec);
            String data = null;
            dat = new DataSistema();
            dat.setData(data);
            hs = new HoraSistema();
            if (numtec > 0) {
                for (idxAtual = 0; idxAtual < numtec; idxAtual++) {
                    ctec.mostrarPesquisa(tec, idxAtual);
                    gravarEmpresa();
                    numExp++;
                }
                upTela();
            }
        } catch (SQLException ex) {
            messagem("Erra na busca do cliente!\nPrograma ExportarTecnicosParaEmpresa.java\nErro: " + ex);
        }
    }

    /**
     * método para gravar empresa no banco
     */
    private void gravarEmpresa() {
        emp = new Empresa();
        emp.setCdCpfCnpj(tec.getCpf());
        emp.setCdInscEstadual("Isento");
        emp.setNomeRazaoSocial(tec.getNomeTecnico());
        emp.setApelido(tec.getNomeTecnico());
        emp.setTipoEmpresa("Fun");
        emp.setTipoPessoa("F");
        emp.setNumero(tec.getNumero());
        emp.setTipoLogradouro(tec.getTipoLogradouro());
        emp.setLogradouro(tec.getLogradouro());
        emp.setComplemento(tec.getComplemento());
        emp.setBairro(tec.getBairro());
        emp.setCdMunicipioIbge(tec.getCdMunicipioIbge());
        emp.setSiglaUf(tec.getCdSiglaUf());
        emp.setCdCep(tec.getCep());
        emp.setTelefone(tec.getTelefone());
        emp.setCelular(tec.getCelular());
        emp.setEmail(tec.getEmail());
        emp.setUsuarioCadastro(tec.getUsuarioCadastro());
        emp.setDataCadastro(dat.getData());
        emp.setHoraCadastro(hs.getHora());
        emp.setSituacao("A");
        try {
            EmpresaDAO dao = new EmpresaDAO(conexao);
            dao.adicionar(emp);
        } catch (SQLException ex) {
            messagem("Erro na gravaçao da empresa no banco!\nPrograma: exportarTecnicosParaEmpresa.java\nErro: " + ex);
        }
    }

    /**
     * Método para mostrar a mensagem na tela
     *
     * @param msg
     */
    private void messagem(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTabTecnicos = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jForTecEncontrados = new javax.swing.JFormattedTextField();
        jForTecExportados = new javax.swing.JFormattedTextField();
        jButPesquisar = new javax.swing.JButton();
        jButGravar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabTecnicos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabTecnicos.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTabTecnicos.setEnabled(false);
        jScrollPane1.setViewportView(jTabTecnicos);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Clientes Encontrados:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Clientes Expotados");

        jForTecEncontrados.setEditable(false);
        jForTecEncontrados.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForTecEncontrados.setEnabled(false);
        jForTecEncontrados.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N

        jForTecExportados.setEditable(false);
        jForTecExportados.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForTecExportados.setEnabled(false);
        jForTecExportados.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N

        jButPesquisar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButPesquisar.setText("Pesquisar");
        jButPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButPesquisarActionPerformed(evt);
            }
        });

        jButGravar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButGravar.setText("Gravar");
        jButGravar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButGravarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForTecEncontrados, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(123, 123, 123)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForTecExportados, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButPesquisar)
                                .addGap(63, 63, 63)
                                .addComponent(jButGravar)))
                        .addGap(0, 381, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButPesquisar)
                    .addComponent(jButGravar))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jForTecEncontrados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jForTecExportados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        pesquisarTecnicos();
    }//GEN-LAST:event_jButPesquisarActionPerformed

    private void jButGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButGravarActionPerformed
        exportarTecnicos();
    }//GEN-LAST:event_jButGravarActionPerformed

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
            java.util.logging.Logger.getLogger(ExportarTecnicosParaEmpresa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExportarTecnicosParaEmpresa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExportarTecnicosParaEmpresa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExportarTecnicosParaEmpresa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExportarTecnicosParaEmpresa(conexao, su).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButGravar;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JFormattedTextField jForTecEncontrados;
    private javax.swing.JFormattedTextField jForTecExportados;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabTecnicos;
    // End of variables declaration//GEN-END:variables
}
