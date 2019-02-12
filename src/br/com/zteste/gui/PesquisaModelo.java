/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GCSPE0091
 */
package br.com.zteste.gui;

import br.com.gcs.controle.CMateriais;
import br.com.gcs.dao.MateriaisDAO;
import br.com.gcs.modelo.Materiais;
import br.com.modelo.FormatarValor;
import br.com.modelo.VerificarTecla;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.SQLException;
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
public class PesquisaModelo extends javax.swing.JDialog {

    private String sql;
    private String cdMaterial;
    private MateriaisDAO matdao;
    private CMateriais cmat;
    private Materiais mat;
    private Connection conexao;
    private int indexFinal;
    private int indexAtual = 0;
    private static String chamador;
    private VerificarTecla vt;
    private String selecao1;
    private String selecao2;

    /**
     * Creates new form PesquisaModelo
     */
    public PesquisaModelo(java.awt.Frame parent, boolean modal, String chamador) {
        super(parent, modal);
        this.chamador = chamador;
        setTitle("Manter Pesquisa de Materiair");
        setLocationRelativeTo(null);
        initComponents();
        setTitle("Pesquisa de Materiais");
        setaInicio();
        centralizarComponente();
        mudarLinha();
        //buscarCorrelatos();
        //filtrarTabela();
    }

    // setando as variáveis iniciais
    public void setaInicio() {
        mat = new Materiais();
        cmat = new CMateriais(conexao);
        sql = "SELECT CD_MATERIAL AS Código,"
                + "NOME_MATERIAL AS Descrição,"
                + "CD_GRUPO AS Grupo,"
                + "CD_SUBGRUPO AS SubGrupo,"
                + "CD_CATEGORIA AS Categoria,"
                + "CD_MARCA AS Marca,"
                + "CD_CLASSE AS Classe,"
                + "CD_ESSENCIA AS Essencia,"
                + "CD_UNIDMEDIDA AS UM,"
                + "SITUACAO AS Situacao"
                + " FROM GCSMATERIAL";
        buscarTodos();
        jTabMat.setModel(matdao);
        ajustarTabela();
    }

    // criando um listener quando mudar de linha
    private void mudarLinha() {
        jTabMat.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtual = jTabMat.getSelectedRow();
                ajustarTabela();
            }
        });

        jTabMat.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                vt = new VerificarTecla();
                if ("ENTER".equals(String.format("%s", vt.VerificarTecla(e)).toUpperCase()) && "M".equals(chamador.toUpperCase())) {
                    salvarSelecao();
                }
            }

            @Override
            public void keyReleased(KeyEvent e
            ) {
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
            matdao = new MateriaisDAO(conexao);
            matdao.setQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        jTabMat.getColumnModel().getColumn(0).setMinWidth(100);
        jTabMat.getColumnModel().getColumn(0).setPreferredWidth(100);
        jTabMat.getColumnModel().getColumn(1).setMinWidth(250);
        jTabMat.getColumnModel().getColumn(1).setPreferredWidth(250);
        jTabMat.getColumnModel().getColumn(2).setMinWidth(40);
        jTabMat.getColumnModel().getColumn(2).setPreferredWidth(40);
        jTabMat.getColumnModel().getColumn(3).setMinWidth(60);
        jTabMat.getColumnModel().getColumn(3).setPreferredWidth(60);
        jTabMat.getColumnModel().getColumn(4).setMinWidth(60);
        jTabMat.getColumnModel().getColumn(4).setPreferredWidth(60);
        jTabMat.getColumnModel().getColumn(5).setMinWidth(40);
        jTabMat.getColumnModel().getColumn(5).setPreferredWidth(40);
        jTabMat.getColumnModel().getColumn(6).setMinWidth(40);
        jTabMat.getColumnModel().getColumn(6).setPreferredWidth(40);
        jTabMat.getColumnModel().getColumn(7).setMinWidth(60);
        jTabMat.getColumnModel().getColumn(7).setPreferredWidth(60);
        jTabMat.getColumnModel().getColumn(8).setMinWidth(30);
        jTabMat.getColumnModel().getColumn(8).setPreferredWidth(30);
        jTabMat.getColumnModel().getColumn(9).setMinWidth(60);
        jTabMat.getColumnModel().getColumn(9).setPreferredWidth(60);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
        cdMaterial = String.format("%s", jTabMat.getValueAt(indexAtual, 0));
        jTexSelecao1.setText(String.format("%s", jTabMat.getValueAt(indexAtual, 0)));
        jTexSelecao2.setText(String.format("%s", jTabMat.getValueAt(indexAtual, 1)));
        buscarCorrelatos();
    }

    // buscar tabelas correlatos
    private void buscarCorrelatos() {
        String sql = "select * from gcsmaterial where cd_material = "
                + cdMaterial
                + "";
        try {
            cmat.pesquisar(sql, false);
        } catch (SQLException ex) {
            Logger.getLogger(PesquisaModelo.class.getName()).log(Level.SEVERE, null, ex);
        }
        cmat.mostrarPesquisa(mat, 0);
        atualizarTela();
    }

    // Atualizar registros correlatos
    private void atualizarTela() {
        jTexNomeGrupo.setText(mat.getNomeGrupo());
        jTexNomeSubGrupo.setText(mat.getNomeSubGrupo());
        jTexNomeCategoria.setText(mat.getNomeCategoria());
        jTexNomeMarca.setText(mat.getNomeMarca());
        jTexNomeClasse.setText(mat.getNomeClasse());
        jTexNomeEssencia.setText(mat.getNomeEssencia());
        jTexNomeUnidMedida.setText(mat.getNomeUnidMedida());
        jTexLargura.setText(String.valueOf(mat.getLargura()));
        jTexComprimento.setText(String.valueOf(mat.getComprimento()));
        jTexEpessura.setText(String.valueOf(mat.getEspessura()));
        jTexPesoLiquido.setText(String.valueOf(mat.getPesoLiquido()));
        jTexPesoBruto.setText(String.valueOf(mat.getPesoBruto()));
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

        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabMat = new JTable(matdao);
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTexPesquisar = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabLargura = new javax.swing.JLabel();
        jLabComprimento = new javax.swing.JLabel();
        jLabEspessura = new javax.swing.JLabel();
        jTexLargura = new FormatarValor((FormatarValor.NUMERO));
        jTexComprimento = new FormatarValor((FormatarValor.NUMERO));
        jTexEpessura = new FormatarValor((FormatarValor.NUMERO));
        jLabPesoLiquido = new javax.swing.JLabel();
        jLabPesoBruto = new javax.swing.JLabel();
        jTexPesoLiquido = new FormatarValor((FormatarValor.PESO));
        jTexPesoBruto = new FormatarValor((FormatarValor.PESO));
        jPanel7 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTexNomeGrupo = new javax.swing.JTextField();
        jLabNomeSubGrupo = new javax.swing.JLabel();
        jTexNomeSubGrupo = new javax.swing.JTextField();
        jLabNomeCategoria = new javax.swing.JLabel();
        jTexNomeCategoria = new javax.swing.JTextField();
        jLabNomeMarca = new javax.swing.JLabel();
        jTexNomeMarca = new javax.swing.JTextField();
        jLabNomeClasse = new javax.swing.JLabel();
        jTexNomeClasse = new javax.swing.JTextField();
        jLabNomeEssencia = new javax.swing.JLabel();
        jTexNomeEssencia = new javax.swing.JTextField();
        jLabNomeUnidMedida = new javax.swing.JLabel();
        jTexNomeUnidMedida = new javax.swing.JTextField();
        jLabProduto = new javax.swing.JLabel();
        jTexSelecao1 = new javax.swing.JTextField();
        jTexSelecao2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Materiais");
        setResizable(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabMat.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.SystemColor.activeCaptionBorder, null));
        jTabMat.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabMat.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Descrição", "Grupo", "Sub Grupo", "Categoria", "Marca", "Classe", "Essencia", "U.M", "Situacao"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabMat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabMatKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTabMat);
        if (jTabMat.getColumnModel().getColumnCount() > 0) {
            jTabMat.getColumnModel().getColumn(0).setMinWidth(90);
            jTabMat.getColumnModel().getColumn(0).setPreferredWidth(90);
            jTabMat.getColumnModel().getColumn(1).setMinWidth(200);
            jTabMat.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTabMat.getColumnModel().getColumn(2).setMinWidth(50);
            jTabMat.getColumnModel().getColumn(2).setPreferredWidth(50);
            jTabMat.getColumnModel().getColumn(3).setMinWidth(70);
            jTabMat.getColumnModel().getColumn(3).setPreferredWidth(70);
            jTabMat.getColumnModel().getColumn(4).setMinWidth(70);
            jTabMat.getColumnModel().getColumn(4).setPreferredWidth(70);
            jTabMat.getColumnModel().getColumn(5).setMinWidth(70);
            jTabMat.getColumnModel().getColumn(5).setPreferredWidth(70);
            jTabMat.getColumnModel().getColumn(6).setMinWidth(70);
            jTabMat.getColumnModel().getColumn(6).setPreferredWidth(70);
            jTabMat.getColumnModel().getColumn(7).setMinWidth(70);
            jTabMat.getColumnModel().getColumn(7).setPreferredWidth(70);
            jTabMat.getColumnModel().getColumn(8).setMinWidth(30);
            jTabMat.getColumnModel().getColumn(8).setPreferredWidth(30);
            jTabMat.getColumnModel().getColumn(9).setMinWidth(30);
            jTabMat.getColumnModel().getColumn(9).setPreferredWidth(30);
        }

        jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Pesquisar Produto:");

        jTexPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexPesquisarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTexPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 829, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações do Produto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Dimenssões / Peso", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabLargura.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabLargura.setText("Larg.:");

        jLabComprimento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabComprimento.setText("Comp.:");

        jLabEspessura.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabEspessura.setText("Esp.:");

        jTexLargura.setEditable(false);
        jTexLargura.setEnabled(false);

        jTexComprimento.setEditable(false);
        jTexComprimento.setEnabled(false);
        jTexComprimento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTexComprimentoActionPerformed(evt);
            }
        });

        jTexEpessura.setEditable(false);
        jTexEpessura.setEnabled(false);

        jLabPesoLiquido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPesoLiquido.setText("Peso Liq.:");

        jLabPesoBruto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPesoBruto.setText("Peso Bru.:");

        jTexPesoLiquido.setEditable(false);
        jTexPesoLiquido.setEnabled(false);

        jTexPesoBruto.setEditable(false);
        jTexPesoBruto.setEnabled(false);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabEspessura)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexEpessura, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel6Layout.createSequentialGroup()
                                .addComponent(jLabLargura)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexLargura, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabComprimento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexComprimento, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabPesoBruto)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexPesoBruto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel6Layout.createSequentialGroup()
                            .addComponent(jLabPesoLiquido)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexPesoLiquido, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabLargura)
                    .addComponent(jTexLargura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabComprimento)
                    .addComponent(jTexComprimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTexEpessura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabEspessura))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabPesoLiquido)
                    .addComponent(jTexPesoLiquido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabPesoBruto)
                    .addComponent(jTexPesoBruto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel6Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jTexComprimento, jTexEpessura, jTexLargura});

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Características", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Grupo:");

        jTexNomeGrupo.setEditable(false);
        jTexNomeGrupo.setEnabled(false);

        jLabNomeSubGrupo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNomeSubGrupo.setText("Sub Grupo:");

        jTexNomeSubGrupo.setEditable(false);
        jTexNomeSubGrupo.setEnabled(false);

        jLabNomeCategoria.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNomeCategoria.setText("Categoria:");

        jTexNomeCategoria.setEditable(false);
        jTexNomeCategoria.setEnabled(false);

        jLabNomeMarca.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNomeMarca.setText("Marca:");

        jTexNomeMarca.setEditable(false);
        jTexNomeMarca.setEnabled(false);

        jLabNomeClasse.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNomeClasse.setText("Classe:");

        jTexNomeClasse.setEditable(false);
        jTexNomeClasse.setEnabled(false);

        jLabNomeEssencia.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNomeEssencia.setText("Essencia:");

        jTexNomeEssencia.setEditable(false);
        jTexNomeEssencia.setEnabled(false);

        jLabNomeUnidMedida.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNomeUnidMedida.setText("Unid. Medida:");

        jTexNomeUnidMedida.setEditable(false);
        jTexNomeUnidMedida.setEnabled(false);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTexNomeGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabNomeUnidMedida)
                            .addComponent(jLabNomeEssencia)
                            .addComponent(jLabNomeClasse)
                            .addComponent(jLabNomeSubGrupo)
                            .addComponent(jLabNomeCategoria)
                            .addComponent(jLabNomeMarca))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTexNomeSubGrupo)
                            .addComponent(jTexNomeCategoria)
                            .addComponent(jTexNomeMarca)
                            .addComponent(jTexNomeClasse)
                            .addComponent(jTexNomeEssencia)
                            .addComponent(jTexNomeUnidMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTexNomeGrupo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeSubGrupo)
                    .addComponent(jTexNomeSubGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeCategoria)
                    .addComponent(jTexNomeCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeMarca)
                    .addComponent(jTexNomeMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeClasse)
                    .addComponent(jTexNomeClasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeEssencia)
                    .addComponent(jTexNomeEssencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeUnidMedida)
                    .addComponent(jTexNomeUnidMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(294, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanel6, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabProduto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabProduto.setText("Produto:");

        jTexSelecao1.setEditable(false);
        jTexSelecao1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexSelecao1.setEnabled(false);

        jTexSelecao2.setEditable(false);
        jTexSelecao2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexSelecao2.setEnabled(false);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabProduto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabProduto)
                    .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTexPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexPesquisarKeyReleased
        if (!jTexPesquisar.getText().isEmpty()) {
            sql = "SELECT CD_MATERIAL AS Código,"
                    + "NOME_MATERIAL AS Descrição,"
                    + "CD_GRUPO AS Grupo,"
                    + "CD_SUBGRUPO AS SubGrupo,"
                    + "CD_CATEGORIA AS Categoria,"
                    + "CD_MARCA AS Marca,"
                    + "CD_CLASSE AS Classe,"
                    + "CD_ESSENCIA AS Essencia,"
                    + "CD_UNIDMEDIDA AS UM,"
                    + "SITUACAO AS Situacao"
                    + " FROM GCSMATERIAL"
                    + " WHERE NOME_MATERIAL LIKE '%"
                    + jTexPesquisar.getText().trim()
                    + "%'";
            //indexAtual = 0;
            buscarTodos();
            jTabMat.setModel(matdao);
            ajustarTabela();
        } else {
            setaInicio();
        }

    }//GEN-LAST:event_jTexPesquisarKeyReleased

    private void jTexComprimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTexComprimentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTexComprimentoActionPerformed

    private void jTabMatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabMatKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("ENTER".equals(vt.VerificarTecla(evt))) {
            JOptionPane.showMessageDialog(null, vt.VerificarTecla(evt));
            salvarSelecao();
        }
    }//GEN-LAST:event_jTabMatKeyPressed

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
            java.util.logging.Logger.getLogger(PesquisaModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisaModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisaModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisaModelo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PesquisaModelo dialog = new PesquisaModelo(new javax.swing.JFrame(), true, chamador);
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
    private javax.swing.JLabel jLabComprimento;
    private javax.swing.JLabel jLabEspessura;
    private javax.swing.JLabel jLabLargura;
    private javax.swing.JLabel jLabNomeCategoria;
    private javax.swing.JLabel jLabNomeClasse;
    private javax.swing.JLabel jLabNomeEssencia;
    private javax.swing.JLabel jLabNomeMarca;
    private javax.swing.JLabel jLabNomeSubGrupo;
    private javax.swing.JLabel jLabNomeUnidMedida;
    private javax.swing.JLabel jLabPesoBruto;
    private javax.swing.JLabel jLabPesoLiquido;
    private javax.swing.JLabel jLabProduto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabMat;
    private javax.swing.JTextField jTexComprimento;
    private javax.swing.JTextField jTexEpessura;
    private javax.swing.JTextField jTexLargura;
    private javax.swing.JTextField jTexNomeCategoria;
    private javax.swing.JTextField jTexNomeClasse;
    private javax.swing.JTextField jTexNomeEssencia;
    private javax.swing.JTextField jTexNomeGrupo;
    private javax.swing.JTextField jTexNomeMarca;
    private javax.swing.JTextField jTexNomeSubGrupo;
    private javax.swing.JTextField jTexNomeUnidMedida;
    private javax.swing.JTextField jTexPesoBruto;
    private javax.swing.JTextField jTexPesoLiquido;
    private javax.swing.JTextField jTexPesquisar;
    private javax.swing.JTextField jTexSelecao1;
    private javax.swing.JTextField jTexSelecao2;
    // End of variables declaration//GEN-END:variables
}
