/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GCVMO0041
 */
package br.com.gcv.visao;

// Objetos de instância da Classe
import br.com.DAO.ConsultaModelo;
import br.com.gcv.modelo.AcabamentoItemPed;
import br.com.gcv.controle.CPedido;
import br.com.gcv.dao.PedidoDAO;

// Objetos de instância dos registros filhos da classe
import br.com.gcv.modelo.ItemPedido;
import br.com.modelo.DataSistema;
import br.com.modelo.HoraSistema;

// Objetos de instância de parâmetros do ambiente
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;

// Objetos de parâmentros do sistema
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 04/12/2017
 */
public class ManterAcabamentoItem extends javax.swing.JDialog {

    // Variáveis de instância principal da Classe
    private AcabamentoItemPed regCorr;
    private CPedido caip;
    private static ItemPedido itp;
    private AcabamentoItemPed modaip;
    private List< AcabamentoItemPed> resultado;

    // Variáveis de instância de parâmetros da classe
    private static Connection conexao;
    private static SessaoUsuario su;
    private DataSistema dat;
    private HoraSistema hs;
    private VerificarTecla vt;
    private NumberFormat formato;

    // Variáveis de instância generalistas da classe
    private int numReg;
    private int idxCorr;
    private String sql;
    private String oper;
    private static String cdTipoVerniz;
    private int indexAtual = 0;

    // Variáveis de instância para registros filhos
    private String sqlaip;

    /**
     * Creates new form ManterProdutos
     *
     * @param parent
     * @param modal
     * @param su
     * @param conexao
     * @param itp
     * @param cdTipoVerniz
     */
    public ManterAcabamentoItem(java.awt.Frame parent, boolean modal, SessaoUsuario su, Connection conexao, ItemPedido itp, String cdTipoVerniz) {
        this.su = su;
        this.conexao = conexao;
        this.itp = itp;
        this.cdTipoVerniz = cdTipoVerniz;
        formato = NumberFormat.getInstance();
        oper = "N";
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        initComponents();
        setLocationRelativeTo(null);
        setaObjAcabamento();
        upItemPedido();
        this.dispose();
    }

    /**
     * Método para atualizar o item do pedido na tela
     */
    private void upItemPedido() {
        jForCdItemServico.setText(itp.getCdMaterial());
        jTexNomeItemServico.setText(itp.getNomeMaterial());
    }

    /**
     * Método para setar os objetos do acabamento do pedido
     */
    private void setaObjAcabamento() {
        modaip = new AcabamentoItemPed();
        caip = new CPedido(conexao, su);
        sqlaip = "select * from gcvacabamentoitemped where cd_pedido = '" + itp.getCdPedido()
                + "' and sequencia = " + itp.getSequencia();
        pesquisarAcabamento();
        buscarTodos();
    }

    /**
     * Método para pesquisar o Acabamento vinculado ao item de pedido
     */
    private void pesquisarAcabamento() {
        try {
            if (caip.pesquisarAcabamento(sqlaip) > 0) {
                atualizarAcabamento();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManterAcabamentoItem.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void buscarTodos() {
        sql = "select a.cd_material as 'Cod.',"
                + " m.nome_material as 'Descrição',"
                + " i.cd_unidmedida as 'U.M',"
                + " i.qtde as 'Qtde.'"
                + " from gcvacabamentoitemped as a"
                + " left join gcsmaterial as m on a.cd_material = m.cd_material"
                + " left join gcvitemtipoverniz as i on a.cd_material = i.cd_material and i.cd_tipoverniz = '" + cdTipoVerniz
                + "'"
                + " where a.cd_pedido = '" + itp.getCdPedido()
                + "' order by a.cd_material";
        try {
            ConsultaModelo cm = new ConsultaModelo(conexao);
            cm.setQuery(sql);
            jTabItens.setModel(cm);
            ajustarTabelaItem();
        } catch (SQLException ex) {
            Logger.getLogger(ManterAcabamentoItem.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /**
     * Método para atualizar a tela com o acabamento relacionado ao item
     */
    private void atualizarAcabamento() {
        dat = new DataSistema();
        modaip = caip.mostrarAcabamento(0);
        jTexCdAcabamento.setText(modaip.getCdMaterial());
        jTexNomeAcabamento.setText(modaip.getNomeMaterial());
        jTexCadastradoPor.setText(modaip.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modaip.getDataCadastro()).toString()));
        if (modaip.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(modaip.getDataModificacao())));
        }
    }

    /**
     * Método para habilitar ou desabilitar os campos de acabamento da tela
     *
     * @param permisao
     */
    private void bloqDesbloqCampos(boolean permisao) {
        jTexCdAcabamento.setEditable(permisao);
        jTexCdAcabamento.setEnabled(permisao);
        jTexNomeAcabamento.setEditable(permisao);
        jTexNomeAcabamento.setEnabled(permisao);
    }

    //Método para ajustar tabela itemPedido
    private void ajustarTabelaItem() {
        jTabItens.getColumnModel().getColumn(0).setWidth(10);
        jTabItens.getColumnModel().getColumn(0).setPreferredWidth(10);
        jTabItens.getColumnModel().getColumn(1).setWidth(200);
        jTabItens.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTabItens.getColumnModel().getColumn(2).setWidth(10);
        jTabItens.getColumnModel().getColumn(2).setPreferredWidth(10);
        jTabItens.getColumnModel().getColumn(3).setWidth(50);
        jTabItens.getColumnModel().getColumn(3).setPreferredWidth(50);
    }

    private void zoomAcabamento() {
        PesquisarAcabamento zoom = new PesquisarAcabamento(new JFrame(), rootPaneCheckingEnabled, "P", conexao, su, cdTipoVerniz);
        zoom.setVisible(true);
        jTexCdAcabamento.setText(zoom.getCdMaterial());
        jTexNomeAcabamento.setText(zoom.getNomeMaterial());
    }

    private void salvarAcabamento() {
        dat = new DataSistema();
        hs = new HoraSistema();
        dat.setData("");
        regCorr = new AcabamentoItemPed();
        regCorr.setCdPedido(itp.getCdPedido());
        regCorr.setSequencia(itp.getSequencia());
        regCorr.setCdMaterial(jTexCdAcabamento.getText());
        PedidoDAO aci = new PedidoDAO(conexao);
        if ("N".equals(oper.toUpperCase())) {
            regCorr.setUsuarioCadastro(su.getUsuarioConectado());
            regCorr.setDataCadastro(dat.getData());
            regCorr.setHoraCadastro(hs.getHora());
            aci.criarAcabamentoItem(regCorr);
        } else {
            regCorr.setUsuarioModificacao(su.getUsuarioConectado());
            regCorr.setDataModificacao(dat.getData());
            regCorr.setHoraModificacao(hs.getHora());
            aci.atualizarAcabamentoItem(regCorr);
        }
        oper = "";
        pesquisarAcabamento();
        buscarTodos();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTooMenuFerramentas = new javax.swing.JToolBar();
        jButEditar = new javax.swing.JButton();
        jButSalvar = new javax.swing.JButton();
        jButSair = new javax.swing.JButton();
        jPanSecundario = new javax.swing.JPanel();
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
        jLabCdItemServico = new javax.swing.JLabel();
        jForCdItemServico = new javax.swing.JFormattedTextField();
        jTexNomeItemServico = new javax.swing.JTextField();
        jSeparator2 = new javax.swing.JSeparator();
        jPanTipoVernizItens = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabItens = new javax.swing.JTable();
        jButItemTabela = new javax.swing.JButton();
        jLabCdAcabamento = new javax.swing.JLabel();
        jTexCdAcabamento = new javax.swing.JTextField();
        jTexNomeAcabamento = new javax.swing.JTextField();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manter Acabamento");

        jTooMenuFerramentas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTooMenuFerramentas.setRollover(true);

        jButEditar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Edit-32.png"))); // NOI18N
        jButEditar.setText("Editar");
        jButEditar.setFocusable(false);
        jButEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButEditar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButEditarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButEditar);

        jButSalvar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Ok-32.PNG"))); // NOI18N
        jButSalvar.setText("Salvar");
        jButSalvar.setEnabled(false);
        jButSalvar.setFocusable(false);
        jButSalvar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButSalvar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButSalvarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButSalvar);

        jButSair.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Exit-32.png"))); // NOI18N
        jButSair.setText("Sair");
        jButSair.setFocusable(false);
        jButSair.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButSair.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButSairActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButSair);

        jPanSecundario.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanSecundario.setToolTipText("");

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
                    .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabDataCadastro)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabDataModificacao)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
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

            jLabCdItemServico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdItemServico.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabCdItemServico.setText("Item/Serviço:");

            jForCdItemServico.setEditable(false);
            try {
                jForCdItemServico.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("********")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForCdItemServico.setEnabled(false);

            jTexNomeItemServico.setEditable(false);
            jTexNomeItemServico.setEnabled(false);

            jPanTipoVernizItens.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Itens", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jTabItens.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabItens.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null},
                    {null, null, null, null},
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
                    "Cod.", "Descrição", "U.M", "Qtde."
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
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
            jScrollPane1.setViewportView(jTabItens);
            if (jTabItens.getColumnModel().getColumnCount() > 0) {
                jTabItens.getColumnModel().getColumn(0).setResizable(false);
                jTabItens.getColumnModel().getColumn(0).setPreferredWidth(10);
                jTabItens.getColumnModel().getColumn(1).setResizable(false);
                jTabItens.getColumnModel().getColumn(1).setPreferredWidth(200);
                jTabItens.getColumnModel().getColumn(2).setResizable(false);
                jTabItens.getColumnModel().getColumn(2).setPreferredWidth(10);
                jTabItens.getColumnModel().getColumn(3).setResizable(false);
                jTabItens.getColumnModel().getColumn(3).setPreferredWidth(50);
            }

            jButItemTabela.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButItemTabela.setText("Itens");
            jButItemTabela.setEnabled(false);

            javax.swing.GroupLayout jPanTipoVernizItensLayout = new javax.swing.GroupLayout(jPanTipoVernizItens);
            jPanTipoVernizItens.setLayout(jPanTipoVernizItensLayout);
            jPanTipoVernizItensLayout.setHorizontalGroup(
                jPanTipoVernizItensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTipoVernizItensLayout.createSequentialGroup()
                    .addGap(479, 479, 479)
                    .addComponent(jButItemTabela)
                    .addGap(0, 127, Short.MAX_VALUE))
                .addGroup(jPanTipoVernizItensLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1)
                    .addContainerGap())
            );
            jPanTipoVernizItensLayout.setVerticalGroup(
                jPanTipoVernizItensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTipoVernizItensLayout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jButItemTabela)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jLabCdAcabamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdAcabamento.setText("Acabamento:");

            jTexCdAcabamento.setEditable(false);
            jTexCdAcabamento.setEnabled(false);
            jTexCdAcabamento.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCdAcabamentoKeyPressed(evt);
                }
            });

            jTexNomeAcabamento.setEditable(false);
            jTexNomeAcabamento.setEnabled(false);

            javax.swing.GroupLayout jPanSecundarioLayout = new javax.swing.GroupLayout(jPanSecundario);
            jPanSecundario.setLayout(jPanSecundarioLayout);
            jPanSecundarioLayout.setHorizontalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator2)
                        .addComponent(jPanBotoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanTipoVernizItens, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                    .addComponent(jLabCdItemServico)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jForCdItemServico, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexNomeItemServico, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                    .addComponent(jLabCdAcabamento)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexCdAcabamento, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexNomeAcabamento, javax.swing.GroupLayout.PREFERRED_SIZE, 390, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
            );
            jPanSecundarioLayout.setVerticalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCdItemServico)
                        .addComponent(jForCdItemServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeItemServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCdAcabamento)
                        .addComponent(jTexCdAcabamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeAcabamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(27, 27, 27)
                    .addComponent(jPanTipoVernizItens, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jMenuBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jMenu1.setText("Arquivo");

            jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItem1.setText("Novo");
            jMenu1.add(jMenuItem1);

            jMenuItemSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemSalvar.setText("Salvar");
            jMenu1.add(jMenuItemSalvar);

            jMenuItemSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemSair.setText("Sair");
            jMenu1.add(jMenuItemSair);

            jMenuBar.add(jMenu1);

            jMenu2.setText("Editar");

            jMenuItemEditar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemEditar.setText("Editar");
            jMenu2.add(jMenuItemEditar);

            jMenuBar.add(jMenu2);

            setJMenuBar(jMenuBar);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanSecundario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanSecundario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        if (jTexCdAcabamento.getText().isEmpty()) {
            oper = "N";
        } else {
            oper = "A";
        }
        jButEditar.setEnabled(false);
        jButSalvar.setEnabled(true);
        bloqDesbloqCampos(true);
    }//GEN-LAST:event_jButEditarActionPerformed

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        bloqDesbloqCampos(false);
        salvarAcabamento();
        jButSalvar.setEnabled(false);
        jButEditar.setEnabled(true);
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jTexCdAcabamentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdAcabamentoKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomAcabamento();
        }
    }//GEN-LAST:event_jTexCdAcabamentoKeyPressed

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
            java.util.logging.Logger.getLogger(ManterAcabamentoItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterAcabamentoItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterAcabamentoItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterAcabamentoItem.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ManterAcabamentoItem dialog = new ManterAcabamentoItem(new javax.swing.JFrame(), true, su, conexao, itp, cdTipoVerniz);
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
    private javax.swing.JButton jButEditar;
    private javax.swing.JButton jButItemTabela;
    private javax.swing.JButton jButSair;
    private javax.swing.JButton jButSalvar;
    private javax.swing.JFormattedTextField jForCdItemServico;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabCdAcabamento;
    private javax.swing.JLabel jLabCdItemServico;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JPanel jPanBotoes;
    private javax.swing.JPanel jPanSecundario;
    private javax.swing.JPanel jPanTipoVernizItens;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTabItens;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexCdAcabamento;
    private javax.swing.JTextField jTexNomeAcabamento;
    private javax.swing.JTextField jTexNomeItemServico;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
