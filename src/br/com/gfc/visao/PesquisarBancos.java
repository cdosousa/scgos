/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GFCPE0021
 */
package br.com.gfc.visao;

import br.com.gfc.controle.CBancos;
import br.com.gfc.dao.BancosDAO;
import br.com.gfc.modelo.Bancos;
import br.com.modelo.VerificarTecla;
import br.com.modelo.DataSistema;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 10/10/2017
 */
public class PesquisarBancos extends javax.swing.JDialog {

    /**
     * @return the selecao1
     */
    public String getSelecao1() {
        return selecao1;
    }

    /**
     * @return the selecao2
     */
    public String getSelecao2() {
        return selecao2;
    }

    private static Connection conexao;
    private String sql;
    private String cdBanco;
    private BancosDAO bcdao;
    private CBancos cbc;
    private Bancos bc;
    private int indexFinal;
    private int indexAtual = 0;
    private static String chamador;
    private VerificarTecla vt;
    private String selecao1;
    private String selecao2;
    private final DataSistema dat;
    private boolean salvar = false;

    /**
     * Creates new form PesquisaModelo
     */
    public PesquisarBancos(java.awt.Frame parent, boolean modal, String chamador, Connection conexao) {
        super(parent, modal);
        this.chamador = chamador;
        this.conexao = conexao;
        dat = new DataSistema();
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Bancos");
        setaInicio();
        centralizarComponente();
        mudarLinha();
        //buscarCorrelatos();
        //filtrarTabela();
    }

    // setando as variáveis iniciais
    public void setaInicio() {
        bc = new Bancos();
        cbc = new CBancos(conexao);
        sql = "select cd_banco as 'Código',"
                + "nome_banco as Nome,"
                + "email as Email,"
                + "telefone as Telefone,"
                + "celular as Celular,"
                + "situacao as Sit"
                + " from gfcbancos";
        buscarTodos();
        jTabBancos.setModel(bcdao);
        ajustarTabela();
    }

    // criando um listener quando mudar de linha
    private void mudarLinha() {
        // verificar tecla para captuar quando a tecla entre for pressionada
        jTabBancos.addKeyListener(new KeyListener() {
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
        
        jTabBancos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtual = jTabBancos.getSelectedRow();
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
            bcdao = new BancosDAO(conexao);
            bcdao.setQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        jTabBancos.getColumnModel().getColumn(0).setMinWidth(30);
        jTabBancos.getColumnModel().getColumn(0).setPreferredWidth(30);
        jTabBancos.getColumnModel().getColumn(1).setMinWidth(200);
        jTabBancos.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTabBancos.getColumnModel().getColumn(2).setMinWidth(200);
        jTabBancos.getColumnModel().getColumn(2).setPreferredWidth(200);
        jTabBancos.getColumnModel().getColumn(3).setMinWidth(90);
        jTabBancos.getColumnModel().getColumn(3).setPreferredWidth(90);
        jTabBancos.getColumnModel().getColumn(4).setMinWidth(90);
        jTabBancos.getColumnModel().getColumn(4).setPreferredWidth(90);
        jTabBancos.getColumnModel().getColumn(5).setMinWidth(30);
        jTabBancos.getColumnModel().getColumn(5).setPreferredWidth(30);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
        cdBanco = String.format("%s", jTabBancos.getValueAt(indexAtual, 0));
        jTexSelecao1.setText(String.format("%s", jTabBancos.getValueAt(indexAtual, 0)));
        jTexSelecao2.setText(String.format("%s", jTabBancos.getValueAt(indexAtual, 1)));
        buscarCorrelatos();
    }

    // buscar tabelas correlatos
    private void buscarCorrelatos() {
        if (!cdBanco.isEmpty()) {
            String sql = "select * from gfcbancos where cd_banco = "
                    + cdBanco
                    + "";
            try {
                cbc.pesquisar(sql);
            } catch (SQLException ex) {
                Logger.getLogger(PesquisarBancos.class.getName()).log(Level.SEVERE, null, ex);
            }
            cbc.mostrarPesquisa(bc, 0);
            atualizarTela();
        }
    }

    // Atualizar registros correlatos
    private void atualizarTela() {
        jTexTipoLogradouro.setText(bc.getTipoLogradouro());
        jTexLogradouro.setText(bc.getLogradouro());
        jTexNumero.setText(bc.getNumero());
        jTexComplemento.setText(bc.getComplemento());
        jTexBairro.setText(bc.getBairro());
        jTexUF.setText(bc.getCdUfSigla());
        jTexMunicipio.setText(bc.getNomeMunicipio());
        jTexCadastradoPor.setText(bc.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(bc.getDataCadastro())));
        jForDataModificacao.setText(bc.getDataModificacao());
    }

    // salvar seleção
    private void salvarSelecao() {
        selecao1 = jTexSelecao1.getText().trim();
        selecao2 = jTexSelecao2.getText().trim();
        dispose();
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
        jTabBancos = new JTable(bcdao);
        jPanePesquisar = new javax.swing.JPanel();
        jLabPesquisar = new javax.swing.JLabel();
        jTexPesquisar = new javax.swing.JTextField();
        jPanResumo = new javax.swing.JPanel();
        jPanInforBanco = new javax.swing.JPanel();
        jPanEndereco = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTexTipoLogradouro = new javax.swing.JTextField();
        jTexLogradouro = new javax.swing.JTextField();
        jLabNomeCategoria = new javax.swing.JLabel();
        jTexNumero = new javax.swing.JTextField();
        jLabComplemento = new javax.swing.JLabel();
        jTexComplemento = new javax.swing.JTextField();
        jLabBairro = new javax.swing.JLabel();
        jTexBairro = new javax.swing.JTextField();
        jLabUF = new javax.swing.JLabel();
        jTexUF = new javax.swing.JTextField();
        jLabMunicipio = new javax.swing.JLabel();
        jTexMunicipio = new javax.swing.JTextField();
        jLabCEP = new javax.swing.JLabel();
        jForCep = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabCadastradoPor = new javax.swing.JLabel();
        jLabDataCadastro = new javax.swing.JLabel();
        jLabDataModificacao = new javax.swing.JLabel();
        jForDataCadastro = new javax.swing.JFormattedTextField();
        jTexCadastradoPor = new javax.swing.JTextField();
        jForDataModificacao = new javax.swing.JFormattedTextField();
        jLabBanco = new javax.swing.JLabel();
        jTexSelecao1 = new javax.swing.JTextField();
        jTexSelecao2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Bancos");
        setResizable(false);

        jPanTabela.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabBancos.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.SystemColor.activeCaptionBorder, null));
        jTabBancos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabBancos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Nome", "email", "Telefone", "Celular", "Sit"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabBancos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabBancosKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTabBancos);
        if (jTabBancos.getColumnModel().getColumnCount() > 0) {
            jTabBancos.getColumnModel().getColumn(0).setResizable(false);
            jTabBancos.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTabBancos.getColumnModel().getColumn(1).setResizable(false);
            jTabBancos.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTabBancos.getColumnModel().getColumn(2).setResizable(false);
            jTabBancos.getColumnModel().getColumn(2).setPreferredWidth(200);
            jTabBancos.getColumnModel().getColumn(3).setResizable(false);
            jTabBancos.getColumnModel().getColumn(3).setPreferredWidth(90);
            jTabBancos.getColumnModel().getColumn(4).setResizable(false);
            jTabBancos.getColumnModel().getColumn(4).setPreferredWidth(90);
            jTabBancos.getColumnModel().getColumn(5).setResizable(false);
            jTabBancos.getColumnModel().getColumn(5).setPreferredWidth(30);
        }

        jPanePesquisar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabPesquisar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPesquisar.setText("Pesquisar Banco:");

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
                    .addComponent(jTexPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 829, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabPesquisar))
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

        javax.swing.GroupLayout jPanTabelaLayout = new javax.swing.GroupLayout(jPanTabela);
        jPanTabela.setLayout(jPanTabelaLayout);
        jPanTabelaLayout.setHorizontalGroup(
            jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanePesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
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

        jPanInforBanco.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações do Banco", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jPanEndereco.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Endereço", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Tipo:");

        jTexTipoLogradouro.setEditable(false);
        jTexTipoLogradouro.setEnabled(false);

        jTexLogradouro.setEditable(false);
        jTexLogradouro.setEnabled(false);

        jLabNomeCategoria.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNomeCategoria.setText("Num.:");

        jTexNumero.setEditable(false);
        jTexNumero.setEnabled(false);

        jLabComplemento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabComplemento.setText("Complemento:");

        jTexComplemento.setEditable(false);
        jTexComplemento.setEnabled(false);

        jLabBairro.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabBairro.setText("Bairro:");

        jTexBairro.setEditable(false);
        jTexBairro.setEnabled(false);

        jLabUF.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabUF.setText("U.F.:");

        jTexUF.setEditable(false);
        jTexUF.setEnabled(false);

        jLabMunicipio.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabMunicipio.setText("Munic.:");

        jTexMunicipio.setEditable(false);
        jTexMunicipio.setEnabled(false);

        jLabCEP.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCEP.setText("CEP:");

        jForCep.setEditable(false);
        try {
            jForCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jForCep.setEnabled(false);

        javax.swing.GroupLayout jPanEnderecoLayout = new javax.swing.GroupLayout(jPanEndereco);
        jPanEndereco.setLayout(jPanEnderecoLayout);
        jPanEnderecoLayout.setHorizontalGroup(
            jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanEnderecoLayout.createSequentialGroup()
                        .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                                .addComponent(jLabComplemento)
                                .addGap(14, 14, 14)
                                .addComponent(jTexComplemento))
                            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(8, 8, 8)
                                .addComponent(jTexTipoLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jTexLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addComponent(jLabNomeCategoria)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanEnderecoLayout.createSequentialGroup()
                        .addComponent(jLabBairro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(10, 10, 10)
                        .addComponent(jLabMunicipio)
                        .addGap(2, 2, 2)
                        .addComponent(jTexMunicipio, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabUF)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexUF, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabCEP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jForCep, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanEnderecoLayout.setVerticalGroup(
            jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTexTipoLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTexLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabNomeCategoria)
                    .addComponent(jTexNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabComplemento)
                    .addComponent(jTexComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCEP)
                        .addComponent(jForCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabBairro)
                        .addComponent(jTexBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabMunicipio)
                        .addComponent(jTexMunicipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabUF)
                        .addComponent(jTexUF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

        javax.swing.GroupLayout jPanInforBancoLayout = new javax.swing.GroupLayout(jPanInforBanco);
        jPanInforBanco.setLayout(jPanInforBancoLayout);
        jPanInforBancoLayout.setHorizontalGroup(
            jPanInforBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInforBancoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanInforBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanEndereco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanInforBancoLayout.setVerticalGroup(
            jPanInforBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInforBancoLayout.createSequentialGroup()
                .addComponent(jPanEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabBanco.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabBanco.setText("Banco:");

        jTexSelecao1.setEditable(false);
        jTexSelecao1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexSelecao1.setEnabled(false);

        jTexSelecao2.setEditable(false);
        jTexSelecao2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexSelecao2.setEnabled(false);

        javax.swing.GroupLayout jPanResumoLayout = new javax.swing.GroupLayout(jPanResumo);
        jPanResumo.setLayout(jPanResumoLayout);
        jPanResumoLayout.setHorizontalGroup(
            jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanResumoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanInforBanco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanResumoLayout.createSequentialGroup()
                        .addComponent(jLabBanco)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanResumoLayout.setVerticalGroup(
            jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanResumoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabBanco)
                    .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanInforBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanResumo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanResumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTexPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexPesquisarKeyReleased
        if (!jTexPesquisar.getText().isEmpty()) {
            sql = "select cd_banco as 'Código',"
                    + "nome_banco as Nome,"
                    + "email as Email,"
                    + "telefone as Telefone,"
                    + "celular as Celular,"
                    + "situacao as Sit"
                    + " from gfcbancos"
                    + " WHERE NOME_BANCO LIKE '%"
                    + jTexPesquisar.getText().trim()
                    + "%'";
            //indexAtual = 0;
            buscarTodos();
            jTabBancos.setModel(bcdao);
            if (jTabBancos.getModel().getRowCount() > 0) {
                ajustarTabela();
            }
        } else {
            setaInicio();
        }
    }//GEN-LAST:event_jTexPesquisarKeyReleased

    private void jTabBancosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabBancosKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("ENTER".equals(vt.VerificarTecla(evt))) {
            JOptionPane.showMessageDialog(null, vt.VerificarTecla(evt));
            salvarSelecao();
        }
    }//GEN-LAST:event_jTabBancosKeyPressed

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
            java.util.logging.Logger.getLogger(PesquisarBancos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisarBancos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisarBancos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisarBancos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
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
                PesquisarBancos dialog = new PesquisarBancos(new javax.swing.JFrame(), true, chamador, conexao);
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
    private javax.swing.JFormattedTextField jForCep;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JLabel jLabBairro;
    private javax.swing.JLabel jLabBanco;
    private javax.swing.JLabel jLabCEP;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabComplemento;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabMunicipio;
    private javax.swing.JLabel jLabNomeCategoria;
    private javax.swing.JLabel jLabPesquisar;
    private javax.swing.JLabel jLabUF;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanEndereco;
    private javax.swing.JPanel jPanInforBanco;
    private javax.swing.JPanel jPanResumo;
    private javax.swing.JPanel jPanTabela;
    private javax.swing.JPanel jPanePesquisar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabBancos;
    private javax.swing.JTextField jTexBairro;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexComplemento;
    private javax.swing.JTextField jTexLogradouro;
    private javax.swing.JTextField jTexMunicipio;
    private javax.swing.JTextField jTexNumero;
    private javax.swing.JTextField jTexPesquisar;
    private javax.swing.JTextField jTexSelecao1;
    private javax.swing.JTextField jTexSelecao2;
    private javax.swing.JTextField jTexTipoLogradouro;
    private javax.swing.JTextField jTexUF;
    // End of variables declaration//GEN-END:variables
}
