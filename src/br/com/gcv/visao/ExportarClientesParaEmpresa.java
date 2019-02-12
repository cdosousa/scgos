/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.visao;

import br.com.DAO.ConsultaModelo;
import br.com.DAO.EmpresaDAO;
import br.com.gcv.controle.CClientes;
import br.com.gcv.modelo.Clientes;
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
 * @created on 13/08/2018
 */
public class ExportarClientesParaEmpresa extends javax.swing.JFrame {

    /**
     * Classe criada para poder exportar os clientes já cadastrados para a
     * tabela de empresas. Creates new form ExportarClientesParaEmpresa
     */
    //Objetos de instância da classe
    static Connection conexao;
    static SessaoUsuario su;
    DataSistema dat;
    HoraSistema hs;

    // Objetos da classe
    Clientes cli;
    CClientes ccli;
    Empresa emp;
    ConsultaModelo con;

    //Variáveis de controle da classe
    int numCli;
    int numExp;
    int idxAtual;
    String sqlCli;

    public ExportarClientesParaEmpresa(Connection conexao, SessaoUsuario su) {
        this.conexao = conexao;
        this.su = su;
        initComponents();
    }

    /**
     * Método para pesquisar os clientes e retornar na tela
     */
    private void pesquisarClientes() {
        numCli = 0;
        numExp = 0;
        sqlCli = "select c.cpf_cnpj as Codigo,"
                + "c.nome_razaosocial as Nome,"
                + "c.apelido as Fantasia,"
                + "c.tipo_pessoa as Pessoa"
                + " from gcvclientes as c"
                + " where not exists (select p.cpf_cnpj"
                + " from pgsempresa as p"
                + " where p.cpf_cnpj = c.cpf_cnpj)";
        try {
            con = new ConsultaModelo(conexao);
            con.setQuery(sqlCli);
            numCli = con.getRowCount();
            if (numCli > 0) {
                jTabClientes.setModel(con);
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
        jForCliEncontrados.setText(String.valueOf(numCli));
        jForCliExportados.setText(String.valueOf(numExp));
    }

    /**
     * Método para gravar os clientes na tabalea de empresa
     */
    private void exportarCliente() {
        sqlCli = "select * "
                + " from gcvclientes as c"
                + " where not exists (select p.cpf_cnpj"
                + " from pgsempresa as p"
                + " where p.cpf_cnpj = c.cpf_cnpj)";
        try {
            cli = new Clientes();
            ccli = new CClientes(conexao);
            numCli = ccli.pesquisar(sqlCli);
            String data = null;
            dat = new DataSistema();
            dat.setData(data);
            hs = new HoraSistema();
            if (numCli > 0) {
                for (idxAtual = 0; idxAtual < numCli; idxAtual++) {
                    ccli.mostrarPesquisa(cli, idxAtual);
                    gravarEmpresa();
                    numExp++;
                }
                upTela();
            }
        } catch (SQLException ex) {
            messagem("Erra na busca do cliente!\nPrograma ExportarClientesParaEmpresa.java\nErro: " + ex);
        }
    }

    /**
     * método para gravar empresa no banco
     */
    private void gravarEmpresa() {
        emp = new Empresa();
        emp.setCdCpfCnpj(cli.getCdCpfCnpj());
        emp.setCdInscEstadual(cli.getCdInscEstadual());
        if ("2".equals(cli.getTipoPessoa())) {
            emp.setTipoPessoa("J");
        } else {
            emp.setTipoPessoa("F");
        }
        emp.setNomeRazaoSocial(cli.getNomeRazaoSocial());
        emp.setApelido(cli.getApelido());
        emp.setTipoEmpresa("Cli");
        emp.setNumero(cli.getNumero());
        emp.setTipoLogradouro(cli.getTipoLogradouro());
        emp.setLogradouro(cli.getLogradouro());
        emp.setComplemento(cli.getComplemento());
        emp.setBairro(cli.getBairro());
        emp.setCdMunicipioIbge(cli.getCdMunicipioIbge());
        emp.setSiglaUf(cli.getSiglaUf());
        emp.setCdCep(cli.getCdCep());
        emp.setTelefone(cli.getTelefone());
        emp.setCelular(cli.getCelular());
        emp.setEmail(cli.getEmail());
        emp.setUsuarioCadastro(cli.getUsuarioCadastro());
        emp.setDataCadastro(dat.getData());
        emp.setHoraCadastro(hs.getHora());
        if ("1".equals(cli.getSituacao())) {
            emp.setSituacao("A");
        } else {
            emp.setSituacao("I");
        }
        try {
            EmpresaDAO dao = new EmpresaDAO(conexao);
            dao.adicionar(emp);
        } catch (SQLException ex) {
            messagem("Erro na gravaçao da empresa no banco!\nPrograma: exportarClientesParaEmpresa.java\nErro: " + ex);
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
        jTabClientes = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jForCliEncontrados = new javax.swing.JFormattedTextField();
        jForCliExportados = new javax.swing.JFormattedTextField();
        jButPesquisar = new javax.swing.JButton();
        jButGravar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jTabClientes.setModel(new javax.swing.table.DefaultTableModel(
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
        jTabClientes.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_ALL_COLUMNS);
        jTabClientes.setEnabled(false);
        jScrollPane1.setViewportView(jTabClientes);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Clientes Encontrados:");

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Clientes Expotados");

        jForCliEncontrados.setEditable(false);
        jForCliEncontrados.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForCliEncontrados.setEnabled(false);
        jForCliEncontrados.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N

        jForCliExportados.setEditable(false);
        jForCliExportados.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForCliExportados.setEnabled(false);
        jForCliExportados.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N

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
                                .addComponent(jForCliEncontrados, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(123, 123, 123)
                                .addComponent(jLabel2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForCliExportados, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                    .addComponent(jForCliEncontrados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jForCliExportados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        pesquisarClientes();
    }//GEN-LAST:event_jButPesquisarActionPerformed

    private void jButGravarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButGravarActionPerformed
        exportarCliente();
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
            java.util.logging.Logger.getLogger(ExportarClientesParaEmpresa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ExportarClientesParaEmpresa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ExportarClientesParaEmpresa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ExportarClientesParaEmpresa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ExportarClientesParaEmpresa(conexao, su).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButGravar;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JFormattedTextField jForCliEncontrados;
    private javax.swing.JFormattedTextField jForCliExportados;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabClientes;
    // End of variables declaration//GEN-END:variables
}
