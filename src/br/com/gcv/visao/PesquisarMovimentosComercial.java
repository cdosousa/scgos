    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GCVPE0051
 */
package br.com.gcv.visao;

import br.com.DAO.ConsultaModelo;
import br.com.gcv.controle.CAtendimento;
import br.com.gcv.controle.CProposta;
import br.com.gcv.modelo.Atendimento;
import br.com.gcv.modelo.Proposta;

import br.com.modelo.DataSistema;
import br.com.modelo.HoraSistema;
import br.com.modelo.ParametrosGerais;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 17/11/2017
 */
public class PesquisarMovimentosComercial extends javax.swing.JFrame {

    /**
     * variáveis de instância de parâmetros de conexão e do usuario
     */
    private static Connection conexao;
    private static SessaoUsuario su;
    private static ParametrosGerais pg;
    private VerificarTecla vt;
    private DataSistema dat;
    private HoraSistema hs;
    /**
     * variáveis de instância para buscar proposta
     */
    private Atendimento ate;
    private CAtendimento cate;
    private Proposta pro;
    private CProposta cpro;
    private ConsultaModelo daoPro;
    private ConsultaModelo daoPed;
    private ConsultaModelo daoItp;
    private ConsultaModelo daoCli;
    private ConsultaModelo daoAcab;
    private String cliente;
    private String sqlpro;
    private String sqlped;
    private String sqlitp;
    private String sqlcli;
    private String sqlacab;
    private int linhaPro = 0;
    private int linhaPed = 0;
    private int linhaCli = 0;
    private int linhaVerniz = 0;
    private String movimento;

    /**
     * Construtro padrão da classe. Carregado os objetos de conexao e usuario
     */
    public PesquisarMovimentosComercial(Connection conexao, SessaoUsuario su, ParametrosGerais pg) {
        this.conexao = conexao;
        this.su = su;
        this.pg = pg;
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        monitoraLinha();
        capturandoTecla(jTabProposta, jTabPedidos, jTabPedidosCli, jTabVerniz);
        capturandoClick(jTabProposta, jTabPedidos, jTabPedidosCli, jTabVerniz);
        movimento = "";
        buscarTodos();
        monitoraGuia();
    }

    private void buscarTodos() {
        linhaCli = 0;
        linhaPro = 0;
        linhaPed = 0;
        linhaVerniz = 0;
        sqlpro = "select * from buscarproposta";
        sqlped = "select * from buscarpedidos";
        sqlcli = "select * from buscarclientes";
        sqlacab = "select * from buscaracabamentos";
        pesquisarPropostas();
        pesquisarPedidos();
        pesquisarClientes();
        pesquisarAcabamento();
    }

    /**
     * Método para pesquisar as Propostas
     */
    private void pesquisarPropostas() {
        try {
            daoPro = new ConsultaModelo(conexao);
            daoPro.setQuery(sqlpro);
            if (daoPro.getRowCount() > 0) {
                jTabProposta.setModel(daoPro);
                ajustarTabela(jTabProposta, 15, 10, 10, 200, 20, 100);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca da Proposta Comercial!\nErro: " + ex);
        }
    }

    /**
     * Método para pesquisar os Pedidos
     */
    private void pesquisarPedidos() {
        try {
            daoPed = new ConsultaModelo(conexao);
            daoPed.setQuery(sqlped);
            if (daoPed.getRowCount() > 0) {
                jTabPedidos.setModel(daoPed);
                ajustarTabela(jTabPedidos, 15, 20, 300, 30, 30, 30, 30);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Pedido Comercial!\nErro: " + ex);
        }
    }

    /**
     * Método para pesquisar os Clientes
     */
    private void pesquisarClientes() {
        try {
            daoCli = new ConsultaModelo(conexao);
            daoCli.setQuery(sqlcli);
            if (daoCli.getRowCount() > 0) {
                jTabClientes.setModel(daoCli);
                ajustarTabela(jTabClientes, 250, 100, 5, 70, 100, 40, 40, 200);
            }
        } catch (SQLException ex) {
            messagemPopUp("Erro na busca do Pedido Comercial!\nErro: " + ex);
        }
    }

    /**
     * Metodo par buscar pedidos por cliente
     */
    private void pesquisarPedidosCliente() {
        String cli = String.format("%s", jTabClientes.getValueAt(linhaCli, 1));
        cli = cli.replace(".", "");
        cli = cli.replace("-", "");
        cli = cli.replace("/", "");
        sqlped = "select p.cd_pedido as Pedido,"
                + "	p.data_cadastro as Emissao,"
                + "	case p.situacao\n"
                + "     WHEN 'A' THEN 'Pendente'"
                + "     WHEN 'AA' THEN 'Pendente'"
                + "     WHEN 'CA' THEN 'Cancelado'"
                + "     WHEN 'CO' THEN 'Contrato'"
                + "     WHEN 'FA' THEN 'Faturado'"
                + "     WHEN 'PE' THEN 'Pendente'"
                + "     WHEN 'P' THEN 'Pendente'"
                + " end as Situacao,"
                + " c.cd_contrato as Contrato"
                + " from gcvpedido as p\n"
                + " left outer join gcvcontrato as c on p.cd_pedido = c.cd_pedido"
                + " where p.cpf_cnpj = '" + cli
                + "'";
        try {
            daoPed = new ConsultaModelo(conexao);
            daoPed.zerarTabela(jTabPedidosCli, new Object[]{null, null, null, null}, new String[]{"Pedido", "Emissao", "Situacao", "Contrato"}, new Class[]{String.class, String.class, String.class, String.class}, new boolean[]{false, false, false, false});
            daoPed.zerarTabela(jTabItensPedido, new Object[]{null, null, null, null, null, null}, new String[]{"Material", "Descricao", "Qtde", "Val.Unit", "Val.Tot", "Desc."}, new Class[]{String.class, String.class, Double.class, Double.class, Double.class, Double.class}, new boolean[]{false, false, false, false, false, false});
            daoPed.setQuery(sqlped);
            if (daoPed.getRowCount() > 0) {
                jTabPedidosCli.setModel(daoPed);
                ajustarTabela(jTabPedidosCli, 20, 20, 20, 30);
                pesquisarItemPedido();
            }
        } catch (SQLException ex) {
            messagemPopUp("Erro na busca dos Pedidos do Cliente!\nErro: " + ex);
        }
    }

    /**
     * Método para buscar os itens do pedido
     */
    private void pesquisarItemPedido() {
        sqlitp = "select i.cd_material as Material,"
                + " m.nome_material as Decricao,"
                + " format(i.quantidade,2,'De_de') as Qtde,"
                + " format(i.valor_unit_bruto,2, 'De_de') as 'Val.Unit',"
                + " format(i.total_item_bruto,2, 'De_de') as 'Val.Tot',"
                + " format(i.valor_desc, 2,'De_de') as 'Desc.'"
                + " from gcvitempedido as i"
                + " left outer join gcsmaterial as m on i.cd_material = m.cd_material"
                + " where i.cd_pedido = '" + String.format("%s", jTabPedidosCli.getValueAt(linhaPed, 0))
                + "'";
        try {
            daoItp = new ConsultaModelo(conexao);
            daoItp.setQuery(sqlitp);
            if (daoItp.getRowCount() > 0) {
                jTabItensPedido.setModel(daoItp);
                ajustarTabela(jTabItensPedido, 10, 120, 5, 5, 5, 5);
            }
        } catch (SQLException ex) {
            messagemPopUp("Erro na busca dos Itens do Pedido do Cliente!\nErro: " + ex);
        }

    }
    
    /**
     * Método para buscar os acabamentos
     */
    private void pesquisarAcabamento(){
        try{
            daoAcab = new ConsultaModelo(conexao);
            daoAcab.setQuery(sqlacab);
            if(daoAcab.getRowCount() > 0){
                jTabVerniz.setModel(daoAcab);
                daoAcab.ajustarTabela(jTabVerniz, 10,80,10,5,10,10,3);
            }
        }catch (SQLException ex) {
            messagemPopUp("Erro na busca dos Acabamentos!\nErro: " + ex);
        }
    }

    /**
     * Método para ajustar o tamanho das colunas da tabela
     *
     * @param tabela tabela que será ajustada
     * @param size array com o tamanho dos campos que serão ajustados
     */
    private void ajustarTabela(JTable tabela, int... size) {
        int tamanho = 0;
        int coluna = 0;
        for (int i : size) {
            tamanho = i;
            tabela.getColumnModel().getColumn(coluna).setMinWidth(tamanho);
            tabela.getColumnModel().getColumn(coluna).setPreferredWidth(tamanho);
            coluna++;
        }
    }

    /**
     * Método para monitorar a mudança de linha nas tabelas
     */
    private void monitoraLinha() {
        // adiciona um listener à tabela de Proposta Comercial
        linhaPro = 0;
        jTabProposta.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                linhaPro = jTabProposta.getSelectedRow();
                movimento = "Propostas";
            }

        });
        //Adiciona um Listener á tabela de Pedido Comercial
        linhaPed = 0;
        jTabPedidos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                linhaPed = jTabPedidos.getSelectedRow();
                movimento = "Pedidos";
            }
        });
        //Adicionar um Listener à tabela de Clientes
        linhaCli = 0;
        jTabClientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                linhaCli = jTabClientes.getSelectedRow();
                linhaPed = 0;
                pesquisarPedidosCliente();

            }
        });
        //Adiciona um Listener à tabela pedidos de clientes
        jTabPedidosCli.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                linhaPed = jTabPedidosCli.getSelectedRow();
                pesquisarItemPedido();
                movimento = "Pedidos";
            }
        });
        //Adiciona um Listener à tabela Verniz
        jTabVerniz.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                linhaVerniz = jTabVerniz.getSelectedRow();
                movimento = "verniz";
            }
        });
    }

    /**
     * Método para monitorar a troca de guia
     */
    private void monitoraGuia() {
        // adiciona um listener ao paine de guia para monitorar a troca da guia
        jTabbedPanel.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (jTabbedPanel.getSelectedIndex() == 0) {
                    buscarTodos();
                } else {
                    pesquisarPedidosCliente();
                }
            }
        });
    }

    /**
     * Método para capiturar a tecla da tabela selecionada
     *
     * @param tabela
     */
    private void capturandoTecla(JTable... tabela) {
        for (int i = 0; i < tabela.length; i++) {
            vt = new VerificarTecla();
            tabela[i].addKeyListener(new KeyListener() {
                @Override
                public void keyTyped(KeyEvent e) {
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    String tecla = vt.VerificarTecla(e).toUpperCase();
                    if ("F5".equals(tecla)) {
                        if ("PEDIDOS".equals(movimento.toUpperCase())) {
                            String cdPedido;
                            if (jTabbedPanel.getSelectedIndex() == 0) {
                               cdPedido = String.format("%s", jTabPedidos.getValueAt(linhaPed, 0));
                            }else{
                                cdPedido = String.format("%s", jTabPedidosCli.getValueAt(linhaPed, 0));
                            }
                            if (buscarProposta(cdPedido) > 0) {
                                new ManterPedido(su, conexao, pg, pro).setVisible(true);
                            }
                        } else if ("PROPOSTAS".equals(movimento.toUpperCase())) {
                            String cdProposta = String.format("%s", jTabProposta.getValueAt(linhaPro, 0));
                            if (buscarAtendimento(cdProposta) > 0) {
                                new ManterPropostaComercialRev1(su, conexao, pg, ate).setVisible(true);
                            }
                        } else if("VERNIZ".equals(movimento.toUpperCase())){
                            String cdVerniz = String.format("%s", jTabVerniz.getValueAt(linhaVerniz, 0));
                        }
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {
                }
            });
        }
    }

    private void capturandoClick(JTable... tabela) {
        for (int i = 0; i < tabela.length; i++) {
            tabela[i].addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    monitoraLinha();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                }
            });
        }
    }

    /**
     * Método para buscar atendimento
     *
     * @param cdProposta
     * @return a quantidade de registros pesquisados
     */
    private int buscarAtendimento(String cdProposta) {
        ate = new Atendimento();
        cate = new CAtendimento(conexao, su);
        String sql = "select * from gcvatendimento where cd_proposta = '" + cdProposta
                + "'";
        int numReg = 0;
        try {
            numReg = cate.pesquisar(sql);
            if (numReg > 0) {
                cate.mostrarPesquisa(ate, 0);
            }
        } catch (SQLException ex) {
            messagemPopUp("Atendimento não encontrado!\nErro:" + ex);
        }
        return numReg;
    }

    /**
     * Método para buscar a proposta comercial
     *
     * @param cdPedido
     * @return retorna a quantidade de registros pesquisados
     */
    private int buscarProposta(String cdPedido) {
        int numReg = 0;
        pro = new Proposta();
        cpro = new CProposta(conexao);
        String sql = "select * from gcvproposta where cd_pedido = '" + cdPedido
                + "'";
        try {
            numReg = cpro.pesquisar(sql);
            if (numReg > 0) {
                cpro.mostrarPesquisa(pro, 0);
            }
        } catch (SQLException ex) {
            messagemPopUp("Pedido não encontrado!\nErro: " + ex);
        }
        return numReg;
    }

    private void messagemPopUp(String msg) {
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

        jTabbedPanel = new javax.swing.JTabbedPane();
        jPanProposta = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabProposta = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();
        jForCdProposta = new javax.swing.JFormattedTextField();
        jLabel2 = new javax.swing.JLabel();
        jTexNomeCliente = new javax.swing.JTextField();
        jPanPedidos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabPedidos = new javax.swing.JTable();
        jLabCdPedido = new javax.swing.JLabel();
        jForCdPedido = new javax.swing.JFormattedTextField();
        jLabClientePedido = new javax.swing.JLabel();
        jTexNomeClientePedido = new javax.swing.JTextField();
        jPanClientes = new javax.swing.JPanel();
        jPanListaClientes = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabNomeCliente = new javax.swing.JLabel();
        jTextNomeCliente1 = new javax.swing.JTextField();
        jLabCpfCnpj = new javax.swing.JLabel();
        jTexCdCpfCnpj = new javax.swing.JTextField();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTabClientes = new javax.swing.JTable();
        jPanMovimentos = new javax.swing.JPanel();
        jPanPedidosCliente = new javax.swing.JPanel();
        jPanTitulos = new javax.swing.JPanel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTabPedidosCli = new javax.swing.JTable();
        jPanItensPedido = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTabItensPedido = new javax.swing.JTable();
        jPanVerniz = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTabVerniz = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pesquisar Movimentos Comercial");
        setMaximumSize(new java.awt.Dimension(1375, 810));
        setMinimumSize(new java.awt.Dimension(1375, 810));
        setPreferredSize(new java.awt.Dimension(1375, 810));

        jTabbedPanel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTabbedPanel.setMinimumSize(new java.awt.Dimension(1335, 48));
        jTabbedPanel.setName(""); // NOI18N
        jTabbedPanel.setPreferredSize(new java.awt.Dimension(1335, 100));

        jPanProposta.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Proposta Comercial", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jTabProposta.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Proposta", "Revisao", "Data", "Cliente", "Valor", "Situacao"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
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
        jScrollPane2.setViewportView(jTabProposta);
        if (jTabProposta.getColumnModel().getColumnCount() > 0) {
            jTabProposta.getColumnModel().getColumn(0).setResizable(false);
            jTabProposta.getColumnModel().getColumn(0).setPreferredWidth(15);
            jTabProposta.getColumnModel().getColumn(1).setResizable(false);
            jTabProposta.getColumnModel().getColumn(1).setPreferredWidth(10);
            jTabProposta.getColumnModel().getColumn(2).setResizable(false);
            jTabProposta.getColumnModel().getColumn(2).setPreferredWidth(10);
            jTabProposta.getColumnModel().getColumn(3).setPreferredWidth(200);
            jTabProposta.getColumnModel().getColumn(4).setPreferredWidth(20);
            jTabProposta.getColumnModel().getColumn(5).setPreferredWidth(100);
        }

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Proposta:");

        jForCdProposta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jForCdProposta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jForCdPropostaKeyReleased(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Cliente:");

        jTexNomeCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexNomeClienteKeyReleased(evt);
            }
        });

        jPanPedidos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pedido Comercial", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jTabPedidos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Pedido", "Emissao", "Cliente", "Bruto", "Descontos", "Liquido", "Situacao"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTabPedidos);
        if (jTabPedidos.getColumnModel().getColumnCount() > 0) {
            jTabPedidos.getColumnModel().getColumn(0).setPreferredWidth(15);
            jTabPedidos.getColumnModel().getColumn(1).setPreferredWidth(20);
            jTabPedidos.getColumnModel().getColumn(2).setPreferredWidth(300);
            jTabPedidos.getColumnModel().getColumn(3).setPreferredWidth(30);
            jTabPedidos.getColumnModel().getColumn(4).setPreferredWidth(30);
            jTabPedidos.getColumnModel().getColumn(5).setPreferredWidth(30);
            jTabPedidos.getColumnModel().getColumn(6).setPreferredWidth(30);
        }

        jLabCdPedido.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabCdPedido.setText("Pedido:");

        jForCdPedido.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jForCdPedido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jForCdPedidoKeyReleased(evt);
            }
        });

        jLabClientePedido.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabClientePedido.setText("Cliente:");

        jTexNomeClientePedido.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexNomeClientePedidoKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanPedidosLayout = new javax.swing.GroupLayout(jPanPedidos);
        jPanPedidos.setLayout(jPanPedidosLayout);
        jPanPedidosLayout.setHorizontalGroup(
            jPanPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
            .addGroup(jPanPedidosLayout.createSequentialGroup()
                .addComponent(jLabCdPedido)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForCdPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabClientePedido)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexNomeClientePedido, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 154, Short.MAX_VALUE))
        );
        jPanPedidosLayout.setVerticalGroup(
            jPanPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPedidosLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85)
                .addGroup(jPanPedidosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCdPedido)
                    .addComponent(jForCdPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabClientePedido)
                    .addComponent(jTexNomeClientePedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 42, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanPropostaLayout = new javax.swing.GroupLayout(jPanProposta);
        jPanProposta.setLayout(jPanPropostaLayout);
        jPanPropostaLayout.setHorizontalGroup(
            jPanPropostaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPropostaLayout.createSequentialGroup()
                .addGroup(jPanPropostaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 925, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanPropostaLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForCdProposta, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 506, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 409, Short.MAX_VALUE))
        );
        jPanPropostaLayout.setVerticalGroup(
            jPanPropostaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPropostaLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanPropostaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jForCdProposta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTexNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanPedidos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPanel.addTab("Geral", jPanProposta);

        jPanClientes.setMinimumSize(new java.awt.Dimension(1235, 0));

        jPanListaClientes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pesquisar", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabNomeCliente.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabNomeCliente.setText("Nome Cliente:");

        jTextNomeCliente1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextNomeCliente1KeyReleased(evt);
            }
        });

        jLabCpfCnpj.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabCpfCnpj.setText("C.P.F / C.N.P.J:");

        jTexCdCpfCnpj.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexCdCpfCnpjKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabNomeCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextNomeCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, 352, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabCpfCnpj)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexCdCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeCliente)
                    .addComponent(jTextNomeCliente1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabCpfCnpj)
                    .addComponent(jTexCdCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTabClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nome", "C.P.F / C.N.P.J", "UF", "Municipio", "Contato", "Telefone", "Celular", "Email"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        jTabClientes.setMaximumSize(new java.awt.Dimension(655, 128));
        jTabClientes.setMinimumSize(new java.awt.Dimension(655, 128));
        jTabClientes.setPreferredSize(new java.awt.Dimension(655, 128));
        jScrollPane7.setViewportView(jTabClientes);
        if (jTabClientes.getColumnModel().getColumnCount() > 0) {
            jTabClientes.getColumnModel().getColumn(0).setResizable(false);
            jTabClientes.getColumnModel().getColumn(0).setPreferredWidth(250);
            jTabClientes.getColumnModel().getColumn(1).setResizable(false);
            jTabClientes.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTabClientes.getColumnModel().getColumn(2).setResizable(false);
            jTabClientes.getColumnModel().getColumn(2).setPreferredWidth(5);
            jTabClientes.getColumnModel().getColumn(3).setResizable(false);
            jTabClientes.getColumnModel().getColumn(3).setPreferredWidth(70);
            jTabClientes.getColumnModel().getColumn(4).setResizable(false);
            jTabClientes.getColumnModel().getColumn(4).setPreferredWidth(100);
            jTabClientes.getColumnModel().getColumn(5).setResizable(false);
            jTabClientes.getColumnModel().getColumn(5).setPreferredWidth(40);
            jTabClientes.getColumnModel().getColumn(6).setResizable(false);
            jTabClientes.getColumnModel().getColumn(6).setPreferredWidth(40);
            jTabClientes.getColumnModel().getColumn(7).setResizable(false);
            jTabClientes.getColumnModel().getColumn(7).setPreferredWidth(40);
        }

        javax.swing.GroupLayout jPanListaClientesLayout = new javax.swing.GroupLayout(jPanListaClientes);
        jPanListaClientes.setLayout(jPanListaClientesLayout);
        jPanListaClientesLayout.setHorizontalGroup(
            jPanListaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanListaClientesLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanListaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane7))
                .addContainerGap())
        );
        jPanListaClientesLayout.setVerticalGroup(
            jPanListaClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanListaClientesLayout.createSequentialGroup()
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1))
        );

        jPanMovimentos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Movimentos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jPanPedidosCliente.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pedidos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanPedidosCliente.setPreferredSize(new java.awt.Dimension(960, 300));

        jPanTitulos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Títulos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Titulo", "Emissao", "Vecto", "Valor"
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
        jScrollPane6.setViewportView(jTable2);
        if (jTable2.getColumnModel().getColumnCount() > 0) {
            jTable2.getColumnModel().getColumn(0).setResizable(false);
            jTable2.getColumnModel().getColumn(0).setPreferredWidth(20);
            jTable2.getColumnModel().getColumn(1).setResizable(false);
            jTable2.getColumnModel().getColumn(1).setPreferredWidth(20);
            jTable2.getColumnModel().getColumn(2).setResizable(false);
            jTable2.getColumnModel().getColumn(2).setPreferredWidth(20);
            jTable2.getColumnModel().getColumn(3).setResizable(false);
            jTable2.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        javax.swing.GroupLayout jPanTitulosLayout = new javax.swing.GroupLayout(jPanTitulos);
        jPanTitulos.setLayout(jPanTitulosLayout);
        jPanTitulosLayout.setHorizontalGroup(
            jPanTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTitulosLayout.createSequentialGroup()
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanTitulosLayout.setVerticalGroup(
            jPanTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 102, Short.MAX_VALUE)
        );

        jTabPedidosCli.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Pedido", "Emissao", "Situacao", "Contrato"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.String.class
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
        jScrollPane3.setViewportView(jTabPedidosCli);
        if (jTabPedidosCli.getColumnModel().getColumnCount() > 0) {
            jTabPedidosCli.getColumnModel().getColumn(0).setResizable(false);
            jTabPedidosCli.getColumnModel().getColumn(0).setPreferredWidth(20);
            jTabPedidosCli.getColumnModel().getColumn(1).setResizable(false);
            jTabPedidosCli.getColumnModel().getColumn(1).setPreferredWidth(20);
            jTabPedidosCli.getColumnModel().getColumn(2).setResizable(false);
            jTabPedidosCli.getColumnModel().getColumn(2).setPreferredWidth(20);
            jTabPedidosCli.getColumnModel().getColumn(3).setResizable(false);
            jTabPedidosCli.getColumnModel().getColumn(3).setPreferredWidth(30);
        }

        jPanItensPedido.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Itens", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N
        jPanItensPedido.setPreferredSize(new java.awt.Dimension(430, 20));

        jTabItensPedido.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Material", "Descricao", "Qtde", "Val.Unit", "Val.Tot", "Desc."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
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
        jScrollPane5.setViewportView(jTabItensPedido);
        if (jTabItensPedido.getColumnModel().getColumnCount() > 0) {
            jTabItensPedido.getColumnModel().getColumn(0).setResizable(false);
            jTabItensPedido.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTabItensPedido.getColumnModel().getColumn(1).setResizable(false);
            jTabItensPedido.getColumnModel().getColumn(1).setPreferredWidth(120);
            jTabItensPedido.getColumnModel().getColumn(2).setResizable(false);
            jTabItensPedido.getColumnModel().getColumn(2).setPreferredWidth(5);
            jTabItensPedido.getColumnModel().getColumn(3).setResizable(false);
            jTabItensPedido.getColumnModel().getColumn(3).setPreferredWidth(5);
            jTabItensPedido.getColumnModel().getColumn(4).setResizable(false);
            jTabItensPedido.getColumnModel().getColumn(4).setPreferredWidth(5);
            jTabItensPedido.getColumnModel().getColumn(5).setResizable(false);
            jTabItensPedido.getColumnModel().getColumn(5).setPreferredWidth(5);
        }

        javax.swing.GroupLayout jPanItensPedidoLayout = new javax.swing.GroupLayout(jPanItensPedido);
        jPanItensPedido.setLayout(jPanItensPedidoLayout);
        jPanItensPedidoLayout.setHorizontalGroup(
            jPanItensPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 782, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        jPanItensPedidoLayout.setVerticalGroup(
            jPanItensPedidoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanPedidosClienteLayout = new javax.swing.GroupLayout(jPanPedidosCliente);
        jPanPedidosCliente.setLayout(jPanPedidosClienteLayout);
        jPanPedidosClienteLayout.setHorizontalGroup(
            jPanPedidosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPedidosClienteLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(6, 6, 6)
                .addComponent(jPanTitulos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(jPanPedidosClienteLayout.createSequentialGroup()
                .addComponent(jPanItensPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 794, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanPedidosClienteLayout.setVerticalGroup(
            jPanPedidosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPedidosClienteLayout.createSequentialGroup()
                .addGroup(jPanPedidosClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanTitulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanItensPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(135, Short.MAX_VALUE))
        );

        jPanVerniz.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Verniz", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jTabVerniz.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Codigo", "Descricao", "Area", "UM.Area", "Rend.", "Qt.Nec.", "UM"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTabVerniz);
        if (jTabVerniz.getColumnModel().getColumnCount() > 0) {
            jTabVerniz.getColumnModel().getColumn(0).setResizable(false);
            jTabVerniz.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTabVerniz.getColumnModel().getColumn(1).setResizable(false);
            jTabVerniz.getColumnModel().getColumn(1).setPreferredWidth(80);
            jTabVerniz.getColumnModel().getColumn(2).setResizable(false);
            jTabVerniz.getColumnModel().getColumn(2).setPreferredWidth(10);
            jTabVerniz.getColumnModel().getColumn(3).setResizable(false);
            jTabVerniz.getColumnModel().getColumn(3).setPreferredWidth(3);
            jTabVerniz.getColumnModel().getColumn(4).setResizable(false);
            jTabVerniz.getColumnModel().getColumn(4).setPreferredWidth(10);
            jTabVerniz.getColumnModel().getColumn(5).setResizable(false);
            jTabVerniz.getColumnModel().getColumn(5).setPreferredWidth(10);
            jTabVerniz.getColumnModel().getColumn(6).setResizable(false);
            jTabVerniz.getColumnModel().getColumn(6).setPreferredWidth(3);
        }

        javax.swing.GroupLayout jPanVernizLayout = new javax.swing.GroupLayout(jPanVerniz);
        jPanVerniz.setLayout(jPanVernizLayout);
        jPanVernizLayout.setHorizontalGroup(
            jPanVernizLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 515, Short.MAX_VALUE)
        );
        jPanVernizLayout.setVerticalGroup(
            jPanVernizLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanVernizLayout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanMovimentosLayout = new javax.swing.GroupLayout(jPanMovimentos);
        jPanMovimentos.setLayout(jPanMovimentosLayout);
        jPanMovimentosLayout.setHorizontalGroup(
            jPanMovimentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanMovimentosLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(jPanPedidosCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 807, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanVerniz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanMovimentosLayout.setVerticalGroup(
            jPanMovimentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanMovimentosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanMovimentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanPedidosCliente, javax.swing.GroupLayout.DEFAULT_SIZE, 475, Short.MAX_VALUE)
                    .addComponent(jPanVerniz, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanClientesLayout = new javax.swing.GroupLayout(jPanClientes);
        jPanClientes.setLayout(jPanClientesLayout);
        jPanClientesLayout.setHorizontalGroup(
            jPanClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanMovimentos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanListaClientes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanClientesLayout.setVerticalGroup(
            jPanClientesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanClientesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanListaClientes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanMovimentos, javax.swing.GroupLayout.PREFERRED_SIZE, 514, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPanel.addTab("Clientes", jPanClientes);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 1363, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );

        jTabbedPanel.getAccessibleContext().setAccessibleName("Clientes");

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTexNomeClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexNomeClienteKeyReleased
        sqlpro = "select * from buscarproposta as p"
                + " where p.Cliente like '%" + jTexNomeCliente.getText().trim()
                + "%'"
                + " order by p.Proposta";
        pesquisarPropostas();
    }//GEN-LAST:event_jTexNomeClienteKeyReleased

    private void jForCdPropostaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jForCdPropostaKeyReleased
        sqlpro = "select * from buscarproposta as p"
                + " where p.Proposta like '%" + jForCdProposta.getText().trim()
                + "%'"
                + " order by p.Proposta";
        pesquisarPropostas();
    }//GEN-LAST:event_jForCdPropostaKeyReleased

    private void jForCdPedidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jForCdPedidoKeyReleased
        sqlped = "select * from buscarpedidos as p"
                + " where p.Pedido like '%" + jForCdPedido.getText().trim()
                + "%' order by p.Pedido";
        pesquisarPedidos();
    }//GEN-LAST:event_jForCdPedidoKeyReleased

    private void jTexNomeClientePedidoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexNomeClientePedidoKeyReleased
        sqlped = "select * from buscarpedidos as p"
                + " where p.Cliente like '%" + jTexNomeClientePedido.getText().trim()
                + "%' order by p.Pedido";
        pesquisarPedidos();
    }//GEN-LAST:event_jTexNomeClientePedidoKeyReleased

    private void jTextNomeCliente1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextNomeCliente1KeyReleased
        sqlcli = "select * from buscarclientes as c"
                + " where c.Nome like '%" + jTextNomeCliente1.getText().trim()
                + "%' order by c.Nome";
        pesquisarClientes();
    }//GEN-LAST:event_jTextNomeCliente1KeyReleased

    private void jTexCdCpfCnpjKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdCpfCnpjKeyReleased
        sqlcli = "select * from buscarclientes"
                + " where CPF_CNPJ like '%" + jTexCdCpfCnpj.getText().trim()
                + "%' order by CPF_CNPJ";
        pesquisarClientes();
    }//GEN-LAST:event_jTexCdCpfCnpjKeyReleased

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
            java.util.logging.Logger.getLogger(PesquisarMovimentosComercial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisarMovimentosComercial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisarMovimentosComercial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisarMovimentosComercial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PesquisarMovimentosComercial(conexao, su, pg).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFormattedTextField jForCdPedido;
    private javax.swing.JFormattedTextField jForCdProposta;
    private javax.swing.JLabel jLabCdPedido;
    private javax.swing.JLabel jLabClientePedido;
    private javax.swing.JLabel jLabCpfCnpj;
    private javax.swing.JLabel jLabNomeCliente;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanClientes;
    private javax.swing.JPanel jPanItensPedido;
    private javax.swing.JPanel jPanListaClientes;
    private javax.swing.JPanel jPanMovimentos;
    private javax.swing.JPanel jPanPedidos;
    private javax.swing.JPanel jPanPedidosCliente;
    private javax.swing.JPanel jPanProposta;
    private javax.swing.JPanel jPanTitulos;
    private javax.swing.JPanel jPanVerniz;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable jTabClientes;
    private javax.swing.JTable jTabItensPedido;
    private javax.swing.JTable jTabPedidos;
    private javax.swing.JTable jTabPedidosCli;
    private javax.swing.JTable jTabProposta;
    private javax.swing.JTable jTabVerniz;
    private javax.swing.JTabbedPane jTabbedPanel;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTexCdCpfCnpj;
    private javax.swing.JTextField jTexNomeCliente;
    private javax.swing.JTextField jTexNomeClientePedido;
    private javax.swing.JTextField jTextNomeCliente1;
    // End of variables declaration//GEN-END:variables
}
