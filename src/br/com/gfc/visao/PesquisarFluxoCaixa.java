/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GFCPE0071
 */
package br.com.gfc.visao;

import br.com.DAO.ConsultaModelo;
import br.com.gfc.dao.ConsultaTitulos;
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.HoraSistema;
import br.com.modelo.SessaoUsuario;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import br.com.modelo.FormatarValor;
import br.com.modelo.VerificarTecla;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 *
 * @author Cristiano de Oliveira Sousa
 */
public class PesquisarFluxoCaixa extends javax.swing.JFrame {

    //objetos de instância de paramentros da clase
    private static Connection conexao;
    private static SessaoUsuario su;
    private DataSistema dat;
    private HoraSistema hs;
    DefaultTableCellRenderer alinTitulo;
    DefaultTableCellRenderer alinLinha;
    JTableHeader titulo;
    private NumberFormat ftv;

    //objetos da classe
    private ConsultaModelo fldao; //objeto fluxo
    private ConsultaTitulos ttdao; //objeto títulos
    private ConsultaModelo tcdao; //objeto clientes
    private ConsultaModelo fordao;//objeto fornecedores

    // Variáveis de instância da classe
    private String sqlFluxo;
    private String sqlTitulo;
    private String sqlTotalCliente;
    private String sqlTotalFornecedores;
    private String situacaoIni = "AA";
    private String situacaoFin = "ZZ";
    private String pagarReceberIni = "aa";
    private String pagarReceberFin = "ZZ";
    private String tipoLancametoIni = "aaa";
    private String tipoLancametoFin = "ZZZ";
    private int linhaFluxo;
    private int linhaClientes;
    private int linhaFornecedor;
    private int linhaTitulos;

    /**
     * Creates new form PesquisarFluxoCaixa
     */
    public PesquisarFluxoCaixa(Connection conexao, SessaoUsuario su) {
        this.conexao = conexao;
        this.su = su;
        initComponents();
        setaVariaveis();
        formatarCampos();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        monitoraLinhaTabelas();
        monitoraTeclaTabela();
        buscarFluxo();
        buscarTitulos();
        buscarTotalCliente();
        buscarTotalFornecedor();
    }

    private void setaVariaveis() {
        String data;
        String data1;
        dat = new DataSistema();
        DataSistema dat1 = new DataSistema();
        dat.setData("");
        dat1.setData("");
        jForDatEmissaoFin.setText(dat.getDataConv(Date.valueOf(dat.getData())));
        data = dat.getDataConv(jForDatEmissaoFin.getText());
        dat.setData(Date.valueOf(data), -30);
        jForDatEmissaoIni.setText(dat.getDataConv(Date.valueOf(dat.getData())));
        jForDatVencimentoIni.setText(dat1.getDataConv(Date.valueOf(dat1.getData())));
        data1 = dat1.getDataConv(jForDatVencimentoIni.getText());
        dat1.setData(Date.valueOf(data1), 30);
        jForDatVencimentoFin.setText(dat.getDataConv(Date.valueOf(dat1.getData())));
        sqlTitulo = "select * from buscartitulos where Situacao between '" + situacaoIni
                + "' and '" + situacaoFin
                + "' and Tipo between '" + pagarReceberIni
                + "' and '" + pagarReceberFin
                + "' and Emissao between '" + dat.getDataConv(jForDatEmissaoIni.getText())
                + "' and '" + dat.getDataConv(jForDatEmissaoFin.getText())
                + "' and Vencimento between '" + dat.getDataConv(jForDatVencimentoIni.getText())
                + "' and '" + dat.getDataConv(jForDatVencimentoFin.getText())
                + "' and TpLanc between '" + tipoLancametoIni
                + "' and '" + tipoLancametoFin
                + "'";
    }

    /**
     * Método para formatar campos
     */
    private void formatarCampos() {
        ftv.getInstance();
        ftv = new DecimalFormat("###,###,##0.0000");
        ftv.setMaximumFractionDigits(2);

        jForTotPrevReceber.setDocument(new DefineCampoDecimal());
        jForTotTitReceber.setDocument(new DefineCampoDecimal());
        jForTotPrevPagar.setDocument(new DefineCampoDecimal());
        jForTotTitPagar.setDocument(new DefineCampoDecimal());
    }

    /**
     * Método para gerar a tabela do fluxo
     */
    private void buscarFluxo() {
        linhaFluxo = 0;
        sqlFluxo = "select * from fluxocaixa";
        try {
            fldao = new ConsultaModelo(conexao);
            fldao.setQuery(sqlFluxo);
            jTabFluxo.setModel(fldao);
            alinharTabela(jTabFluxo, 1, 2, 3, 4);
            fldao.ajustarTabela(jTabFluxo, 3, 30, 30, 30, 30, 30);
        } catch (SQLException ex) {
            mensagem("Erro na busca do Fluxo Financeiro!\nErro: " + ex);
        }
    }

    /**
     * Método para buscar lancamentos financeiros
     */
    private void buscarTitulos() {
        linhaTitulos = 0;
        try {
            //mensagem(sqlTitulo);
            ttdao = new ConsultaTitulos(conexao);
            ttdao.setQuery(sqlTitulo);
            jTabTitulo.setModel(ttdao);
            alinharTabela(jTabTitulo, 8, 11, 12);
            ttdao.ajustarTabela(jTabTitulo, 30, 5, 5, 5, 30, 200, 20, 5, 20, 30, 30, 20, 20, 20);
            jForTotPrevReceber.setText(String.valueOf(ttdao.getTotPrevisaoRec()));
            jForTotTitReceber.setText(String.valueOf(ttdao.getTotTitulosRec()));
            jForTotPrevPagar.setText(String.valueOf(ttdao.getTotPrevisaoPag()));
            jForTotTitPagar.setText(String.valueOf(ttdao.getTotTitulosPag()));
        } catch (SQLException ex) {
            mensagem("Erro na busca dos Títulos Financeiro!\nErro: " + ex);
        } catch (Exception ex) {
            mensagem("Erro geral na busca dos Títulos!\nErro: " + ex);
        }
    }

    /**
     * Método para buscar total de títulos por clientes
     */
    private void buscarTotalCliente() {
        linhaClientes = 0;
        try {
            sqlTotalCliente = "select * from vw_gfctotalclientes";
            tcdao = new ConsultaModelo(conexao);
            tcdao.setQuery(sqlTotalCliente);
            jTabClientes.setModel(tcdao);
            alinharTabela(jTabClientes, 2);
            tcdao.ajustarTabela(jTabClientes, 30, 100, 20);
        } catch (SQLException ex) {
            mensagem("Erro na busca do total por Clientes!\nErro: " + ex);
        }
    }

    /**
     * Método para buscar total de títulos por fornecedor
     */
    private void buscarTotalFornecedor() {
        linhaFornecedor = 0;
        try {
            sqlTotalFornecedores = "select * from vw_gfctotalfornecedores";
            fordao = new ConsultaModelo(conexao);
            fordao.setQuery(sqlTotalFornecedores);
            jTabFornec.setModel(fordao);
            alinharTabela(jTabFornec, 2);
            fordao.ajustarTabela(jTabClientes, 30, 100, 20);
        } catch (SQLException ex) {
            mensagem("Erro na busca do total por Fornecedores!\nErro: " + ex);
        }
    }

    /**
     * Método para alinhas célula e título da tabela
     *
     * @param tabela
     * @param col Índice das culunas que serão alinhadas
     */
    private void alinharTabela(JTable tabela, int... col) {
        alinLinha = new DefaultTableCellRenderer();
        alinLinha.setHorizontalAlignment(SwingConstants.RIGHT);
        titulo = tabela.getTableHeader();
        alinTitulo = (DefaultTableCellRenderer) titulo.getDefaultRenderer();
        alinTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        alinTitulo.setFont(new java.awt.Font("Tahoma", 2, 14));
        int coluna = 0;
        for (int i : col) {
            coluna = i;
            tabela.getColumnModel().getColumn(coluna).setCellRenderer(alinLinha);
        }
    }

    /**
     * Método para liquidar o título selecionaldo
     */
    private void liquidarTitulo() {
        ManterLiquidacaoTitulo liquidar = new ManterLiquidacaoTitulo(this, rootPaneCheckingEnabled, conexao, su, String.format("%s", jTabTitulo.getValueAt(jTabTitulo.getSelectedRow(), 0)));
        liquidar.setVisible(true);
    }

    /**
     * Método para retornar mensagem na tela
     *
     * @param msg String com mensagem para ser mostrada
     */
    private void mensagem(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    /**
     * Método para monitorar a troca de linha das tabelas
     */
    private void monitoraLinhaTabelas() {
        //adiciona listener a tabela de fluxo
        jTabFluxo.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                linhaFluxo = jTabFluxo.getSelectedRow();
            }
        });

        //adiciona listener a tabela de clientes
        jTabClientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                linhaClientes = jTabClientes.getSelectedRow();
            }
        });

        //adicona listener a tabela de títulos
        jTabTitulo.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                linhaTitulos = jTabTitulo.getSelectedRow();
                if (linhaTitulos >= 0) {
                    controlaBotaoLiquidar();
                }
            }
        });
    }

    private void monitoraTeclaTabela() {
        jTabFluxo.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void keyPressed(KeyEvent e) {
                String sql;
                VerificarTecla vt = new VerificarTecla();
                if ("F5".equals(vt.VerificarTecla(e).toUpperCase())) {
                    String tpMov = "";
                    String tpLanc = "";
                    if (jTabFluxo.getSelectedRow() < 2) {
                        tpMov = "Re";
                        if (jTabFluxo.getSelectedRow() == 0) {
                            tpLanc = "Pre";
                        } else {
                            tpLanc = "Tit";
                        }
                    } else if (jTabFluxo.getSelectedRow() > 2) {
                        tpMov = "Pa";
                        if (jTabFluxo.getSelectedRow() == 3) {
                            tpLanc = "Pre";
                        } else {
                            tpLanc = "Tit";
                        }
                    }
                    //JOptionPane.showConfirmDialog(null, "coluna e posicionada: " + String.format("%s", jTabFluxo.getSelectedColumn()).toString() +
                    //        "\nTipo Movimento: " + tpMov + "\nTipo Lancamento: " + tpLanc);
                    switch (String.format("%s", jTabFluxo.getSelectedColumn()).toString()) {
                        case "1":
                            sql = "select * from buscartitulos where tipo = '" + tpMov
                                    + "' and TpLanc = '" + tpLanc
                                    + "' and Situacao = 'Aberto'";
                            zoomTitulos(sql);
                            break;
                        case "2":
                            sql = "select * from buscartitulos where tipo = '" + tpMov
                                    + "' and TpLanc = '" + tpLanc
                                    + "' and Situacao = 'Aberto'"
                                    + " and Vencimento <= (NOW() + INTERVAL 30 DAY)";
                            zoomTitulos(sql);
                            break;
                        case "3":
                            sql = "select * from buscartitulos where tipo = '" + tpMov
                                    + " 'and TpLanc = '" + tpLanc
                                    + " 'and Situacao = 'Aberto'"
                                    + " and Vencimento between (NOW() + INTERVAL 31 DAY)"
                                    + " and (NOW() + INTERVAL 60 DAY)";
                            zoomTitulos(sql);
                            break;
                        case "4":
                            sql = "select * from buscartitulos where tipo = '" + tpMov
                                    + "' and TpLanc = '" + tpLanc
                                    + "' and Situacao = 'Aberto'"
                                    + " and Vencimento between (NOW() + INTERVAL 61 DAY)"
                                    + " and (NOW() + INTERVAL 90 DAY)";
                            zoomTitulos(sql);
                            break;
                        case "5":
                            sql = "select * from buscartitulos where tipo = '"+ tpMov
                                    + "' and TpLanc = '" + tpLanc
                                    + "' and Situacao = 'Aberto'"
                                    + " and Vencimento > (NOW() + INTERVAL 90 DAY)";
                            zoomTitulos(sql);
                            break;
                    }

                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
    }

    /**
     * controla botão liquidar
     */
    private void controlaBotaoLiquidar() {
        //JOptionPane.showMessageDialog(null, "Conteúdo da Coluna: " + jTabTitulo.getColumnCount() + "\nConteúdo: " + jTabTitulo.getValueAt(linhaTitulos, jTabTitulo.getColumnCount() - 1));
        if ("Liquidado".equals(jTabTitulo.getValueAt(linhaTitulos, jTabTitulo.getColumnCount() - 1))) {
            jButLiquidar.setEnabled(false);
        } else {
            jButLiquidar.setEnabled(true);
        }
    }

    /**
     * Método para exportar títulos para excel
     */
    private void exportarTítulos() {
        try {
            ttdao.exportarExcel(new File(ttdao.gerarArquivo()), "Títulos");
        } catch (IOException io) {
            mensagem("Erro na gravação do arquivo!\nErro: " + io);
        }
    }

    /**
     * Método para dar zoom nos títulos
     */
    private void zoomTitulos(String sql) {
        PesquisarTitulosFiltro zoom = new PesquisarTitulosFiltro(this, rootPaneCheckingEnabled, conexao, su, sql);
        zoom.setVisible(rootPaneCheckingEnabled);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanGeral = new javax.swing.JPanel();
        jPanVisaoFinaceira = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabFluxo = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator1 = new javax.swing.JSeparator();
        jButAtualizarVisao = new javax.swing.JButton();
        jPanTitulos = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabTitulo = new javax.swing.JTable();
        jPanBotoesTitulo = new javax.swing.JPanel();
        jButLancamento = new javax.swing.JButton();
        jButLiquidar = new javax.swing.JButton();
        jButExportExcel = new javax.swing.JButton();
        jPanFiltros = new javax.swing.JPanel();
        jLabSituacao = new javax.swing.JLabel();
        jComSituacao = new javax.swing.JComboBox<>();
        jLabPagarReceber = new javax.swing.JLabel();
        jComPagarReceber = new javax.swing.JComboBox<>();
        jForDatEmissaoFin = new javax.swing.JFormattedTextField();
        jForDatVencimentoIni = new javax.swing.JFormattedTextField();
        jPanCliente = new javax.swing.JPanel();
        jLabNomeCliente = new javax.swing.JLabel();
        jTexNomeCliente = new javax.swing.JTextField();
        jForDatVencimentoFin = new javax.swing.JFormattedTextField();
        jLabTipoLancamento = new javax.swing.JLabel();
        jLabDataEmissao = new javax.swing.JLabel();
        jComTipoLancamento = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jForDatEmissaoIni = new javax.swing.JFormattedTextField();
        jButBuscar = new javax.swing.JButton();
        jPanTotaisTitulos = new javax.swing.JPanel();
        jLabTotPrevReceber = new javax.swing.JLabel();
        jLabTotTitReceber = new javax.swing.JLabel();
        jLabTotPrevPagar = new javax.swing.JLabel();
        jLabTotTitPagar = new javax.swing.JLabel();
        jForTotPrevReceber = new FormatarValor(FormatarValor.NUMERO);
        jForTotTitReceber = new FormatarValor(FormatarValor.NUMERO);
        jForTotPrevPagar = new FormatarValor(FormatarValor.NUMERO);
        jForTotTitPagar = new FormatarValor(FormatarValor.NUMERO);
        jPanel1 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTabClientes = new javax.swing.JTable();
        jPanForcedores = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTabFornec = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Movimento Financeiro");
        setMaximumSize(new java.awt.Dimension(1375, 750));
        setPreferredSize(new java.awt.Dimension(1340, 750));

        jPanGeral.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanVisaoFinaceira.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Visão Financeira - Títulos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel2.setMaximumSize(new java.awt.Dimension(155, 44));
        jPanel2.setMinimumSize(new java.awt.Dimension(155, 44));
        jPanel2.setName(""); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(155, 44));

        jTabFluxo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabFluxo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Tp", "Total", "30 Dias", "60 Dias", "90 Dias", "+ 90 Dias"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
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
        jTabFluxo.setToolTipText("");
        jTabFluxo.setRowHeight(22);
        jTabFluxo.setShowHorizontalLines(false);
        jTabFluxo.setShowVerticalLines(false);
        jScrollPane1.setViewportView(jTabFluxo);
        if (jTabFluxo.getColumnModel().getColumnCount() > 0) {
            jTabFluxo.getColumnModel().getColumn(0).setResizable(false);
            jTabFluxo.getColumnModel().getColumn(0).setPreferredWidth(3);
            jTabFluxo.getColumnModel().getColumn(1).setResizable(false);
            jTabFluxo.getColumnModel().getColumn(1).setPreferredWidth(30);
            jTabFluxo.getColumnModel().getColumn(2).setResizable(false);
            jTabFluxo.getColumnModel().getColumn(2).setPreferredWidth(30);
            jTabFluxo.getColumnModel().getColumn(3).setResizable(false);
            jTabFluxo.getColumnModel().getColumn(3).setPreferredWidth(30);
            jTabFluxo.getColumnModel().getColumn(4).setResizable(false);
            jTabFluxo.getColumnModel().getColumn(4).setPreferredWidth(30);
            jTabFluxo.getColumnModel().getColumn(5).setResizable(false);
            jTabFluxo.getColumnModel().getColumn(5).setPreferredWidth(30);
        }

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("À Receber");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Títulos à vencer em:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("À Pagar");

        jSeparator2.setAlignmentX(0.4F);

        jSeparator1.setAlignmentX(0.4F);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jLabel1)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(8, 8, 8)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(11, 11, 11)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 623, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );

        jButAtualizarVisao.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButAtualizarVisao.setText("Atualizar");
        jButAtualizarVisao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButAtualizarVisaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanVisaoFinaceiraLayout = new javax.swing.GroupLayout(jPanVisaoFinaceira);
        jPanVisaoFinaceira.setLayout(jPanVisaoFinaceiraLayout);
        jPanVisaoFinaceiraLayout.setHorizontalGroup(
            jPanVisaoFinaceiraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanVisaoFinaceiraLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanVisaoFinaceiraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 787, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButAtualizarVisao))
                .addContainerGap())
        );
        jPanVisaoFinaceiraLayout.setVerticalGroup(
            jPanVisaoFinaceiraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanVisaoFinaceiraLayout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButAtualizarVisao)
                .addContainerGap())
        );

        jPanTitulos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Títulos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jTabTitulo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Lancamento", "Tipo", "TpDoc", "TpLanc", "Emissão", "Cliente", "Titulo", "Parc", "Valor", "Vencimento", "Liquidação", "Valor Liq.", "Saldo", "Situação"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTabTitulo);
        if (jTabTitulo.getColumnModel().getColumnCount() > 0) {
            jTabTitulo.getColumnModel().getColumn(0).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTabTitulo.getColumnModel().getColumn(1).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(1).setPreferredWidth(5);
            jTabTitulo.getColumnModel().getColumn(2).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(2).setPreferredWidth(10);
            jTabTitulo.getColumnModel().getColumn(3).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(4).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(4).setPreferredWidth(30);
            jTabTitulo.getColumnModel().getColumn(5).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(5).setPreferredWidth(200);
            jTabTitulo.getColumnModel().getColumn(6).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(6).setPreferredWidth(20);
            jTabTitulo.getColumnModel().getColumn(7).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(7).setPreferredWidth(5);
            jTabTitulo.getColumnModel().getColumn(8).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(8).setPreferredWidth(20);
            jTabTitulo.getColumnModel().getColumn(9).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(9).setPreferredWidth(30);
            jTabTitulo.getColumnModel().getColumn(10).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(10).setPreferredWidth(30);
            jTabTitulo.getColumnModel().getColumn(11).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(11).setPreferredWidth(20);
            jTabTitulo.getColumnModel().getColumn(12).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(12).setPreferredWidth(20);
            jTabTitulo.getColumnModel().getColumn(13).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(13).setPreferredWidth(30);
        }

        jPanBotoesTitulo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButLancamento.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButLancamento.setText("Lançamento");
        jButLancamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButLancamentoActionPerformed(evt);
            }
        });

        jButLiquidar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButLiquidar.setText("Liquidar");
        jButLiquidar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButLiquidarActionPerformed(evt);
            }
        });

        jButExportExcel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButExportExcel.setText("Exportar p/ Excel");
        jButExportExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButExportExcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanBotoesTituloLayout = new javax.swing.GroupLayout(jPanBotoesTitulo);
        jPanBotoesTitulo.setLayout(jPanBotoesTituloLayout);
        jPanBotoesTituloLayout.setHorizontalGroup(
            jPanBotoesTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanBotoesTituloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButLiquidar, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButExportExcel)
                .addContainerGap())
        );
        jPanBotoesTituloLayout.setVerticalGroup(
            jPanBotoesTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanBotoesTituloLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanBotoesTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButLancamento)
                    .addComponent(jButLiquidar)
                    .addComponent(jButExportExcel))
                .addGap(5, 5, 5))
        );

        jPanFiltros.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filtros", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabSituacao.setText("Situação:");

        jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Aberto", "Cancelado", "Contabilizado", "Liquidado" }));
        jComSituacao.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComSituacaoItemStateChanged(evt);
            }
        });

        jLabPagarReceber.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPagarReceber.setText("Pagar / Receber:");

        jComPagarReceber.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Pagar", "Receber" }));
        jComPagarReceber.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComPagarReceberItemStateChanged(evt);
            }
        });

        try {
            jForDatEmissaoFin.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jForDatVencimentoIni.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jPanCliente.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabNomeCliente.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabNomeCliente.setText("Nome:");

        jTexNomeCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexNomeClienteKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanClienteLayout = new javax.swing.GroupLayout(jPanCliente);
        jPanCliente.setLayout(jPanClienteLayout);
        jPanClienteLayout.setHorizontalGroup(
            jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabNomeCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanClienteLayout.setVerticalGroup(
            jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanClienteLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeCliente)
                    .addComponent(jTexNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        try {
            jForDatVencimentoFin.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabTipoLancamento.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabTipoLancamento.setText("Tipo Lançamento:");

        jLabDataEmissao.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabDataEmissao.setText("Data Emissão:");

        jComTipoLancamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Adiantamento", "Previsão", "Titulo", "Transferência" }));
        jComTipoLancamento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComTipoLancamentoItemStateChanged(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Data Vencimento:");

        try {
            jForDatEmissaoIni.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jButBuscar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButBuscar.setText("Buscar");
        jButBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanFiltrosLayout = new javax.swing.GroupLayout(jPanFiltros);
        jPanFiltros.setLayout(jPanFiltrosLayout);
        jPanFiltrosLayout.setHorizontalGroup(
            jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanFiltrosLayout.createSequentialGroup()
                        .addComponent(jLabSituacao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabPagarReceber)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComPagarReceber, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabTipoLancamento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComTipoLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanFiltrosLayout.createSequentialGroup()
                        .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabDataEmissao))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jForDatEmissaoIni)
                            .addComponent(jForDatVencimentoIni, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jForDatEmissaoFin)
                            .addComponent(jForDatVencimentoFin, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanFiltrosLayout.setVerticalGroup(
            jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanFiltrosLayout.createSequentialGroup()
                .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabSituacao)
                        .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabPagarReceber)
                        .addComponent(jComPagarReceber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabTipoLancamento))
                    .addComponent(jComTipoLancamento, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButBuscar, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanFiltrosLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabDataEmissao)
                            .addComponent(jForDatEmissaoIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForDatEmissaoFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jForDatVencimentoIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForDatVencimentoFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanFiltrosLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanCliente, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );

        jPanTotaisTitulos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Total em lançamentos abertos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabTotPrevReceber.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabTotPrevReceber.setText("Total Prev. à Receber:");

        jLabTotTitReceber.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabTotTitReceber.setText("Total Títulos à Receber:");

        jLabTotPrevPagar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabTotPrevPagar.setText("Total Prev. à Pagar:");

        jLabTotTitPagar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabTotTitPagar.setText("Total Títulos à Pagar:");

        jForTotPrevReceber.setEditable(false);
        jForTotPrevReceber.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForTotPrevReceber.setEnabled(false);

        jForTotTitReceber.setEditable(false);
        jForTotTitReceber.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForTotTitReceber.setEnabled(false);

        jForTotPrevPagar.setEditable(false);
        jForTotPrevPagar.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForTotPrevPagar.setEnabled(false);

        jForTotTitPagar.setEditable(false);
        jForTotTitPagar.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForTotTitPagar.setEnabled(false);

        javax.swing.GroupLayout jPanTotaisTitulosLayout = new javax.swing.GroupLayout(jPanTotaisTitulos);
        jPanTotaisTitulos.setLayout(jPanTotaisTitulosLayout);
        jPanTotaisTitulosLayout.setHorizontalGroup(
            jPanTotaisTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTotaisTitulosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTotaisTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabTotPrevReceber, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabTotTitReceber, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabTotPrevPagar, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabTotTitPagar, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTotaisTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jForTotPrevReceber, javax.swing.GroupLayout.DEFAULT_SIZE, 128, Short.MAX_VALUE)
                    .addComponent(jForTotTitReceber)
                    .addComponent(jForTotPrevPagar)
                    .addComponent(jForTotTitPagar))
                .addContainerGap())
        );
        jPanTotaisTitulosLayout.setVerticalGroup(
            jPanTotaisTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTotaisTitulosLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanTotaisTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabTotPrevReceber)
                    .addComponent(jForTotPrevReceber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTotaisTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabTotTitReceber)
                    .addComponent(jForTotTitReceber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTotaisTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabTotPrevPagar)
                    .addComponent(jForTotPrevPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTotaisTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabTotTitPagar)
                    .addComponent(jForTotTitPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout jPanTitulosLayout = new javax.swing.GroupLayout(jPanTitulos);
        jPanTitulos.setLayout(jPanTitulosLayout);
        jPanTitulosLayout.setHorizontalGroup(
            jPanTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTitulosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanTitulosLayout.createSequentialGroup()
                        .addComponent(jPanFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanTotaisTitulos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanBotoesTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanTitulosLayout.setVerticalGroup(
            jPanTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanTitulosLayout.createSequentialGroup()
                .addGroup(jPanTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanTitulosLayout.createSequentialGroup()
                        .addComponent(jPanFiltros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(2, 2, 2))
                    .addGroup(jPanTitulosLayout.createSequentialGroup()
                        .addComponent(jPanTotaisTitulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5)
                .addComponent(jPanBotoesTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Clientes", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jTabClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "CPF / CNPJ", "Nome", "Valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class
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
        jScrollPane3.setViewportView(jTabClientes);
        if (jTabClientes.getColumnModel().getColumnCount() > 0) {
            jTabClientes.getColumnModel().getColumn(0).setResizable(false);
            jTabClientes.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTabClientes.getColumnModel().getColumn(1).setResizable(false);
            jTabClientes.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTabClientes.getColumnModel().getColumn(2).setResizable(false);
            jTabClientes.getColumnModel().getColumn(2).setPreferredWidth(20);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        jPanForcedores.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Fornecedores", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jTabFornec.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "CPF / CNPJ", "Nome", "Valor"
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
        jScrollPane4.setViewportView(jTabFornec);
        if (jTabFornec.getColumnModel().getColumnCount() > 0) {
            jTabFornec.getColumnModel().getColumn(0).setResizable(false);
            jTabFornec.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTabFornec.getColumnModel().getColumn(1).setResizable(false);
            jTabFornec.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTabFornec.getColumnModel().getColumn(2).setResizable(false);
            jTabFornec.getColumnModel().getColumn(2).setPreferredWidth(20);
        }

        javax.swing.GroupLayout jPanForcedoresLayout = new javax.swing.GroupLayout(jPanForcedores);
        jPanForcedores.setLayout(jPanForcedoresLayout);
        jPanForcedoresLayout.setHorizontalGroup(
            jPanForcedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanForcedoresLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanForcedoresLayout.setVerticalGroup(
            jPanForcedoresLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanForcedoresLayout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanGeralLayout = new javax.swing.GroupLayout(jPanGeral);
        jPanGeral.setLayout(jPanGeralLayout);
        jPanGeralLayout.setHorizontalGroup(
            jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanGeralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanGeralLayout.createSequentialGroup()
                        .addComponent(jPanVisaoFinaceira, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanForcedores, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanTitulos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanGeralLayout.setVerticalGroup(
            jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanGeralLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanGeralLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanForcedores, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanVisaoFinaceira, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanTitulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanGeral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanGeral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButBuscarActionPerformed
        sqlTitulo = "select * from buscartitulos where Situacao between '" + situacaoIni
                + "' and '" + situacaoFin
                + "' and Tipo between '" + pagarReceberIni
                + "' and '" + pagarReceberFin
                + "' and Emissao between '" + dat.getDataConv(jForDatEmissaoIni.getText())
                + "' and '" + dat.getDataConv(jForDatEmissaoFin.getText())
                + "' and Vencimento between '" + dat.getDataConv(jForDatVencimentoIni.getText())
                + "' and '" + dat.getDataConv(jForDatVencimentoFin.getText())
                + "' and TpLanc between '" + tipoLancametoIni
                + "' and '" + tipoLancametoFin
                + "'";
        buscarTitulos();
    }//GEN-LAST:event_jButBuscarActionPerformed

    private void jComSituacaoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComSituacaoItemStateChanged
        if ("T".equals(jComSituacao.getSelectedItem().toString().substring(0, 1))) {
            situacaoIni = "AA";
            situacaoFin = "ZZ";
        } else {
            situacaoIni = jComSituacao.getSelectedItem().toString();
            situacaoFin = jComSituacao.getSelectedItem().toString();
        }
    }//GEN-LAST:event_jComSituacaoItemStateChanged

    private void jComPagarReceberItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComPagarReceberItemStateChanged
        if ("T".equals(jComPagarReceber.getSelectedItem().toString().substring(0, 1))) {
            pagarReceberIni = "aa";
            pagarReceberFin = "ZZ";
        } else {
            pagarReceberIni = jComPagarReceber.getSelectedItem().toString().substring(0, 2);
            pagarReceberFin = jComPagarReceber.getSelectedItem().toString().substring(0, 2);
        }
    }//GEN-LAST:event_jComPagarReceberItemStateChanged

    private void jTexNomeClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexNomeClienteKeyReleased
        // TODO add your handling code here:
        if (!jTexNomeCliente.getText().isEmpty()) {
            sqlTitulo = "select * from buscartitulos where Situacao between '" + situacaoIni
                    + "' and '" + situacaoFin
                    + "' and Tipo between '" + pagarReceberIni
                    + "' and '" + pagarReceberFin
                    + "' and Emissao between '" + dat.getDataConv(jForDatEmissaoIni.getText())
                    + "' and '" + dat.getDataConv(jForDatEmissaoFin.getText())
                    + "' and Vencimento between '" + dat.getDataConv(jForDatVencimentoIni.getText())
                    + "' and '" + dat.getDataConv(jForDatVencimentoFin.getText())
                    + "' and TpLanc between '" + tipoLancametoIni
                    + "' and '" + tipoLancametoFin
                    + "' and Cliente like '%" + jTexNomeCliente.getText().trim()
                    + "%'";
        } else {
            jButBuscar.doClick();
        }
        buscarTitulos();
    }//GEN-LAST:event_jTexNomeClienteKeyReleased

    private void jButLancamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButLancamentoActionPerformed
        new ManterTitulos(su, conexao, String.format("%s", jTabTitulo.getValueAt(jTabTitulo.getSelectedRow(), 0))).setVisible(true);
    }//GEN-LAST:event_jButLancamentoActionPerformed

    private void jComTipoLancamentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComTipoLancamentoItemStateChanged
        if ("Tod".equals(jComTipoLancamento.getSelectedItem().toString().substring(0, 3))) {
            tipoLancametoIni = "aaa";
            tipoLancametoFin = "ZZZ";
        } else {
            switch (jComTipoLancamento.getSelectedItem().toString().substring(0, 3)) {
                case "Tra":
                    tipoLancametoIni = "Trf";
                    tipoLancametoFin = "Trf";
                    break;
                default:
                    tipoLancametoIni = jComTipoLancamento.getSelectedItem().toString().substring(0, 3);
                    tipoLancametoFin = jComTipoLancamento.getSelectedItem().toString().substring(0, 3);
                    break;
            }
        }
    }//GEN-LAST:event_jComTipoLancamentoItemStateChanged

    private void jButLiquidarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButLiquidarActionPerformed
        if (String.format("%s", jTabTitulo.getValueAt(jTabTitulo.getSelectedRow(), 0)) != null) {
            liquidarTitulo();
            jButAtualizarVisaoActionPerformed(evt);
            jButBuscarActionPerformed(evt);
        } else {
            mensagem("Nenhum título selecionado!\n\nClique em alguma linha da tabela para continuar!");
        }
    }//GEN-LAST:event_jButLiquidarActionPerformed

    private void jButAtualizarVisaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAtualizarVisaoActionPerformed
        buscarFluxo();
        buscarTotalCliente();
        buscarTotalFornecedor();
    }//GEN-LAST:event_jButAtualizarVisaoActionPerformed

    private void jButExportExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExportExcelActionPerformed
        exportarTítulos();
    }//GEN-LAST:event_jButExportExcelActionPerformed

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
            java.util.logging.Logger.getLogger(PesquisarFluxoCaixa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisarFluxoCaixa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisarFluxoCaixa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisarFluxoCaixa.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PesquisarFluxoCaixa(conexao, su).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButAtualizarVisao;
    private javax.swing.JButton jButBuscar;
    private javax.swing.JButton jButExportExcel;
    private javax.swing.JButton jButLancamento;
    private javax.swing.JButton jButLiquidar;
    private javax.swing.JComboBox<String> jComPagarReceber;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JComboBox<String> jComTipoLancamento;
    private javax.swing.JFormattedTextField jForDatEmissaoFin;
    private javax.swing.JFormattedTextField jForDatEmissaoIni;
    private javax.swing.JFormattedTextField jForDatVencimentoFin;
    private javax.swing.JFormattedTextField jForDatVencimentoIni;
    private javax.swing.JFormattedTextField jForTotPrevPagar;
    private javax.swing.JFormattedTextField jForTotPrevReceber;
    private javax.swing.JFormattedTextField jForTotTitPagar;
    private javax.swing.JFormattedTextField jForTotTitReceber;
    private javax.swing.JLabel jLabDataEmissao;
    private javax.swing.JLabel jLabNomeCliente;
    private javax.swing.JLabel jLabPagarReceber;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabTipoLancamento;
    private javax.swing.JLabel jLabTotPrevPagar;
    private javax.swing.JLabel jLabTotPrevReceber;
    private javax.swing.JLabel jLabTotTitPagar;
    private javax.swing.JLabel jLabTotTitReceber;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanBotoesTitulo;
    private javax.swing.JPanel jPanCliente;
    private javax.swing.JPanel jPanFiltros;
    private javax.swing.JPanel jPanForcedores;
    private javax.swing.JPanel jPanGeral;
    private javax.swing.JPanel jPanTitulos;
    private javax.swing.JPanel jPanTotaisTitulos;
    private javax.swing.JPanel jPanVisaoFinaceira;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTabClientes;
    private javax.swing.JTable jTabFluxo;
    private javax.swing.JTable jTabFornec;
    private javax.swing.JTable jTabTitulo;
    private javax.swing.JTextField jTexNomeCliente;
    // End of variables declaration//GEN-END:variables
}
