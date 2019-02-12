/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GCSPE0091
 */
package br.com.gcs.visao;

import br.com.DAO.ConsultaModelo;
import br.com.gcs.controle.CMateriais;
import br.com.gcs.dao.MateriaisDAO;
import br.com.gcs.modelo.Materiais;
import br.com.modelo.FormatarValor;
import br.com.modelo.VerificarTecla;
import br.com.modelo.DataSistema;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.NumberFormat;
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
public class PesquisarMateriais extends javax.swing.JDialog {

    private String sql;
    private String cdMaterial;
    private ConsultaModelo matdao;
    private CMateriais cmat;
    private Materiais mat;
    private static Connection conexao;
    private int indexFinal;
    private int indexAtual = 0;
    private static String chamador;
    private VerificarTecla vt;
    private String selecao1; // cdMaterial
    private String nomeMaterial; // descricao
    private String selecao3; // tipoItem
    private String cdUnidMedida; // unidade de medida
    private double valorUnitBruto = 0;
    private double valorUnitLiquido = 0;
    private double descAlcada = 0;
    private double valorServico = 0;
    private static boolean preco = false;
    private static String tipoProduto;
    private final DataSistema dat;
    private NumberFormat formato;
    private boolean isSelecao = false;

    /**
     * Creates new form PesquisaModelo
     */
    public PesquisarMateriais(java.awt.Frame parent, boolean modal, String chamador, String tipoProduto, Connection conexao, boolean preco) {
        super(parent, modal);
        this.chamador = chamador;
        this.tipoProduto = tipoProduto;
        dat = new DataSistema();
        this.conexao = conexao;
        this.preco = preco;
        formato = NumberFormat.getInstance();
        setTitle("Manter Pesquisa de Materiais");
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
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
        if (!preco) {
            if ("S".equals(tipoProduto)) {
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
                        + " WHERE TIPO_PRODUTO = '"
                        + tipoProduto
                        + "'";

            } else {
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
            }
        } else {
            sql = "select * from buscarpreco";
        }
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
                if (!isSelecao) {
                    ajustarTabela();
                }
            }
        });

        // verificar tecla para captuar quando a tecla entre for pressionada
        jTabMat.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                vt = new VerificarTecla();
                if ("ENTER".equals(String.format("%s", vt.VerificarTecla(e)).toUpperCase()) && !"M".equals(chamador.toUpperCase())) {
                    isSelecao = true;
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
            matdao = new ConsultaModelo(conexao);
            matdao.setQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        matdao.ajustarTabela(jTabMat, 100, 250, 40, 60, 60, 40, 40, 60, 30, 60);
        /*
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
        */
        if (indexAtual < 0) {
            indexAtual = 0;
        }
        cdMaterial = String.format("%s", jTabMat.getValueAt(indexAtual, 0));
        jTexCdMaterial.setText(String.format("%s", jTabMat.getValueAt(indexAtual, 0)));
        jTexNomeMaterial.setText(String.format("%s", jTabMat.getValueAt(indexAtual, 1)));
        cdUnidMedida = String.format("%s", jTabMat.getValueAt(indexAtual, 8));
        buscarCorrelatos();
    }

    // buscar tabelas correlatos
    private void buscarCorrelatos() {
        if (!cdMaterial.isEmpty()) {
            String sql = "select * from gcsmaterial where cd_material = '"
                    + cdMaterial
                    + "'";
            try {
                cmat.pesquisar(sql, true);
            } catch (SQLException ex) {
                Logger.getLogger(PesquisarMateriais.class.getName()).log(Level.SEVERE, null, ex);
            }
            cmat.mostrarPesquisa(mat, 0);
            atualizarTela();
        }
    }

    // Atualizar registros correlatos
    private void atualizarTela() {
        jComCdTipoProduto.setSelectedIndex(Integer.parseInt(mat.getTipoProduto()));
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
        jTexEstoqMinimo.setText(String.valueOf(mat.getEstoqueMinimo()));
        jTexLoteMini.setText(String.valueOf(mat.getLoteMinimo()));
        jTexLoteMulti.setText(String.valueOf(mat.getLoteMultiplo()));
        jTexCcusto.setText(mat.getCdCcusto());
        jTexCtaContabil.setText(mat.getCdCtaContabeReduz());
        jTexOrigemProdudo.setText(mat.getCdOrigemCsta());
        jTexNCM.setText(mat.getCdNcm());
        jTexCadastradoPor.setText(mat.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(mat.getDataCadastro())));
        jForDataModificacao.setText(mat.getDataModificacao());
        tipoProduto = mat.getTipoProduto();
    }

    // salvar seleção
    private void salvarSelecao() {
        cdMaterial = jTexCdMaterial.getText().trim();
        nomeMaterial = jTexNomeMaterial.getText().trim();

        //atualizando preco
        if (!"4".equals(mat.getTipoProduto())) {
            valorServico = mat.getValorServico();
        } else {
            valorUnitBruto = mat.getValorUnitBruto();
            valorUnitLiquido = mat.getValorUnitLiquido();
        }
        tipoProduto = mat.getTipoProduto();
        dispose();
    }
    
    private void exportarMaterial(){
        try{
            matdao.exportarExcel(new File(matdao.gerarArquivo()),"Materiais");
        }catch(IOException io){
            JOptionPane.showMessageDialog(null, "Erro na exportação do arquivo!\nErro: " + io);
        }
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

        jButton1 = new javax.swing.JButton();
        jPanTabela = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabMat = new JTable(matdao);
        jPanePesquisar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTexPesquisar = new javax.swing.JTextField();
        jButExportarExcel = new javax.swing.JButton();
        jPanResumo = new javax.swing.JPanel();
        jPanInforProduto = new javax.swing.JPanel();
        jPanEstoque = new javax.swing.JPanel();
        jLabEstoqMinimo = new javax.swing.JLabel();
        jLabLoteMinimo = new javax.swing.JLabel();
        jLabLoteMultiplo = new javax.swing.JLabel();
        jTexEstoqMinimo = new FormatarValor((FormatarValor.NUMERO));
        jTexLoteMini = new FormatarValor((FormatarValor.NUMERO));
        jTexLoteMulti = new FormatarValor((FormatarValor.NUMERO));
        jLabCtaContabil = new javax.swing.JLabel();
        jLabCcusto = new javax.swing.JLabel();
        jTexCtaContabil = new javax.swing.JTextField();
        jTexCcusto = new javax.swing.JTextField();
        jPanCaracteristicas = new javax.swing.JPanel();
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
        jPanDimessoesPeso = new javax.swing.JPanel();
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
        jPanFiscal = new javax.swing.JPanel();
        jLabOrigemProduto = new javax.swing.JLabel();
        jLabNCM = new javax.swing.JLabel();
        jTexOrigemProdudo = new javax.swing.JTextField();
        jTexNCM = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabCadastradoPor = new javax.swing.JLabel();
        jLabDataCadastro = new javax.swing.JLabel();
        jLabDataModificacao = new javax.swing.JLabel();
        jForDataCadastro = new javax.swing.JFormattedTextField();
        jTexCadastradoPor = new javax.swing.JTextField();
        jForDataModificacao = new javax.swing.JFormattedTextField();
        jLabProduto = new javax.swing.JLabel();
        jTexCdMaterial = new javax.swing.JTextField();
        jTexNomeMaterial = new javax.swing.JTextField();
        jLabCdTipoProduto = new javax.swing.JLabel();
        jComCdTipoProduto = new javax.swing.JComboBox<>();

        jButton1.setText("jButton1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Materiais");
        setResizable(false);

        jPanTabela.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        jPanePesquisar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Pesquisar Produto:");

        jTexPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexPesquisarKeyReleased(evt);
            }
        });

        jButExportarExcel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButExportarExcel.setText("Exporta Excel");
        jButExportarExcel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButExportarExcelActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanePesquisarLayout = new javax.swing.GroupLayout(jPanePesquisar);
        jPanePesquisar.setLayout(jPanePesquisarLayout);
        jPanePesquisarLayout.setHorizontalGroup(
            jPanePesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanePesquisarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanePesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanePesquisarLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanePesquisarLayout.createSequentialGroup()
                        .addComponent(jTexPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 612, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 121, Short.MAX_VALUE)
                        .addComponent(jButExportarExcel)
                        .addGap(37, 37, 37))))
        );
        jPanePesquisarLayout.setVerticalGroup(
            jPanePesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanePesquisarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanePesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButExportarExcel)
                    .addGroup(jPanePesquisarLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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

        jPanInforProduto.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações do Produto", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jPanEstoque.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Estoque", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabEstoqMinimo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabEstoqMinimo.setText("Est. Min.:");

        jLabLoteMinimo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabLoteMinimo.setText("Lot.Min.:");

        jLabLoteMultiplo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabLoteMultiplo.setText("Lot.Mult.:");

        jTexEstoqMinimo.setEditable(false);
        jTexEstoqMinimo.setEnabled(false);

        jTexLoteMini.setEditable(false);
        jTexLoteMini.setEnabled(false);

        jTexLoteMulti.setEditable(false);
        jTexLoteMulti.setEnabled(false);

        jLabCtaContabil.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCtaContabil.setText("Cta.Contabil:");

        jLabCcusto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCcusto.setText("C.Custo:");

        jTexCtaContabil.setEditable(false);
        jTexCtaContabil.setEnabled(false);

        jTexCcusto.setEditable(false);
        jTexCcusto.setEnabled(false);

        javax.swing.GroupLayout jPanEstoqueLayout = new javax.swing.GroupLayout(jPanEstoque);
        jPanEstoque.setLayout(jPanEstoqueLayout);
        jPanEstoqueLayout.setHorizontalGroup(
            jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanEstoqueLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabEstoqMinimo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabLoteMinimo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabLoteMultiplo, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabCcusto, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabCtaContabil, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTexLoteMulti, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTexLoteMini, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTexEstoqMinimo, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jTexCtaContabil, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexCcusto, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(110, 110, 110))
        );
        jPanEstoqueLayout.setVerticalGroup(
            jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanEstoqueLayout.createSequentialGroup()
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabEstoqMinimo)
                    .addComponent(jTexEstoqMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabLoteMinimo)
                    .addComponent(jTexLoteMini, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTexLoteMulti, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabLoteMultiplo))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCcusto)
                    .addComponent(jTexCcusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCtaContabil)
                    .addComponent(jTexCtaContabil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanCaracteristicas.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Características", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

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

        javax.swing.GroupLayout jPanCaracteristicasLayout = new javax.swing.GroupLayout(jPanCaracteristicas);
        jPanCaracteristicas.setLayout(jPanCaracteristicasLayout);
        jPanCaracteristicasLayout.setHorizontalGroup(
            jPanCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanCaracteristicasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCaracteristicasLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTexNomeGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanCaracteristicasLayout.createSequentialGroup()
                        .addGroup(jPanCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabNomeUnidMedida)
                            .addComponent(jLabNomeEssencia)
                            .addComponent(jLabNomeClasse)
                            .addComponent(jLabNomeSubGrupo)
                            .addComponent(jLabNomeCategoria)
                            .addComponent(jLabNomeMarca))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTexNomeSubGrupo)
                            .addComponent(jTexNomeCategoria)
                            .addComponent(jTexNomeMarca)
                            .addComponent(jTexNomeClasse)
                            .addComponent(jTexNomeEssencia)
                            .addComponent(jTexNomeUnidMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanCaracteristicasLayout.setVerticalGroup(
            jPanCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanCaracteristicasLayout.createSequentialGroup()
                .addGroup(jPanCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jTexNomeGrupo, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeSubGrupo)
                    .addComponent(jTexNomeSubGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeCategoria)
                    .addComponent(jTexNomeCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeMarca)
                    .addComponent(jTexNomeMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeClasse)
                    .addComponent(jTexNomeClasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeEssencia)
                    .addComponent(jTexNomeEssencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanCaracteristicasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeUnidMedida)
                    .addComponent(jTexNomeUnidMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 9, Short.MAX_VALUE))
        );

        jPanDimessoesPeso.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Dimenssões / Peso", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

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

        javax.swing.GroupLayout jPanDimessoesPesoLayout = new javax.swing.GroupLayout(jPanDimessoesPeso);
        jPanDimessoesPeso.setLayout(jPanDimessoesPesoLayout);
        jPanDimessoesPesoLayout.setHorizontalGroup(
            jPanDimessoesPesoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanDimessoesPesoLayout.createSequentialGroup()
                .addGroup(jPanDimessoesPesoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanDimessoesPesoLayout.createSequentialGroup()
                        .addGroup(jPanDimessoesPesoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanDimessoesPesoLayout.createSequentialGroup()
                                .addComponent(jLabEspessura)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexEpessura, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanDimessoesPesoLayout.createSequentialGroup()
                                .addComponent(jLabLargura)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexLargura, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabComprimento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexComprimento, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanDimessoesPesoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanDimessoesPesoLayout.createSequentialGroup()
                            .addComponent(jLabPesoBruto)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexPesoBruto, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanDimessoesPesoLayout.createSequentialGroup()
                            .addComponent(jLabPesoLiquido)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexPesoLiquido, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanDimessoesPesoLayout.setVerticalGroup(
            jPanDimessoesPesoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanDimessoesPesoLayout.createSequentialGroup()
                .addGroup(jPanDimessoesPesoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabLargura)
                    .addComponent(jTexLargura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabComprimento)
                    .addComponent(jTexComprimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanDimessoesPesoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTexEpessura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabEspessura))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanDimessoesPesoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabPesoLiquido)
                    .addComponent(jTexPesoLiquido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanDimessoesPesoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabPesoBruto)
                    .addComponent(jTexPesoBruto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanFiscal.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Fiscal", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabOrigemProduto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabOrigemProduto.setText("Origem Produto:");

        jLabNCM.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNCM.setText("NCM:");

        jTexOrigemProdudo.setEditable(false);
        jTexOrigemProdudo.setEnabled(false);

        jTexNCM.setEditable(false);
        jTexNCM.setEnabled(false);

        javax.swing.GroupLayout jPanFiscalLayout = new javax.swing.GroupLayout(jPanFiscal);
        jPanFiscal.setLayout(jPanFiscalLayout);
        jPanFiscalLayout.setHorizontalGroup(
            jPanFiscalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanFiscalLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabOrigemProduto)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexOrigemProdudo, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabNCM)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTexNCM, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanFiscalLayout.setVerticalGroup(
            jPanFiscalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanFiscalLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanFiscalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabOrigemProduto)
                    .addComponent(jLabNCM)
                    .addComponent(jTexOrigemProdudo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNCM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
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
                .addComponent(jForDataModificacao)
                .addGap(187, 187, 187))
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

        javax.swing.GroupLayout jPanInforProdutoLayout = new javax.swing.GroupLayout(jPanInforProduto);
        jPanInforProduto.setLayout(jPanInforProdutoLayout);
        jPanInforProdutoLayout.setHorizontalGroup(
            jPanInforProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInforProdutoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanInforProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanInforProdutoLayout.createSequentialGroup()
                        .addComponent(jPanCaracteristicas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanInforProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanInforProdutoLayout.createSequentialGroup()
                                .addComponent(jPanDimessoesPeso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, 277, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanFiscal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(175, 175, 175))
        );
        jPanInforProdutoLayout.setVerticalGroup(
            jPanInforProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInforProdutoLayout.createSequentialGroup()
                .addGroup(jPanInforProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanCaracteristicas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanInforProdutoLayout.createSequentialGroup()
                        .addGroup(jPanInforProdutoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanDimessoesPeso, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanEstoque, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanFiscal, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabProduto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabProduto.setText("Produto:");

        jTexCdMaterial.setEditable(false);
        jTexCdMaterial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexCdMaterial.setEnabled(false);

        jTexNomeMaterial.setEditable(false);
        jTexNomeMaterial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexNomeMaterial.setEnabled(false);

        jLabCdTipoProduto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdTipoProduto.setText("Tipo Produto:");

        jComCdTipoProduto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Acabado", "Insumo", "Materia Prima", "Revenda", "Serviço" }));

        javax.swing.GroupLayout jPanResumoLayout = new javax.swing.GroupLayout(jPanResumo);
        jPanResumo.setLayout(jPanResumoLayout);
        jPanResumoLayout.setHorizontalGroup(
            jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanResumoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanResumoLayout.createSequentialGroup()
                        .addComponent(jLabProduto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexCdMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexNomeMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabCdTipoProduto)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComCdTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanInforProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 864, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(53, Short.MAX_VALUE))
        );
        jPanResumoLayout.setVerticalGroup(
            jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanResumoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabProduto)
                    .addComponent(jTexCdMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabCdTipoProduto)
                    .addComponent(jComCdTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanInforProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jPanResumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTexPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexPesquisarKeyReleased
        if (!jTexPesquisar.getText().isEmpty()) {
            if (!preco) {
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
            }else{
                sql = "select * from buscarpreco WHERE NOME_MATERIAL LIKE '%"
                        + jTexPesquisar.getText().trim()
                        + "%'";
            }
            //indexAtual = 0;
            buscarTodos();
            jTabMat.setModel(matdao);
            if (jTabMat.getModel().getRowCount() > 0) {
                ajustarTabela();
            }
        } else {
            setaInicio();
        }
    }//GEN-LAST:event_jTexPesquisarKeyReleased

    private void jTabMatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabMatKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("ENTER".equals(vt.VerificarTecla(evt))) {
            JOptionPane.showMessageDialog(null, vt.VerificarTecla(evt));
            salvarSelecao();
        }
    }//GEN-LAST:event_jTabMatKeyPressed

    private void jButExportarExcelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExportarExcelActionPerformed
        exportarMaterial();
    }//GEN-LAST:event_jButExportarExcelActionPerformed

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
            java.util.logging.Logger.getLogger(PesquisarMateriais.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisarMateriais.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisarMateriais.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisarMateriais.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PesquisarMateriais dialog = new PesquisarMateriais(new javax.swing.JFrame(), true, chamador, tipoProduto, conexao, preco);
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
    private javax.swing.JButton jButExportarExcel;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComCdTipoProduto;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabCcusto;
    private javax.swing.JLabel jLabCdTipoProduto;
    private javax.swing.JLabel jLabComprimento;
    private javax.swing.JLabel jLabCtaContabil;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabEspessura;
    private javax.swing.JLabel jLabEstoqMinimo;
    private javax.swing.JLabel jLabLargura;
    private javax.swing.JLabel jLabLoteMinimo;
    private javax.swing.JLabel jLabLoteMultiplo;
    private javax.swing.JLabel jLabNCM;
    private javax.swing.JLabel jLabNomeCategoria;
    private javax.swing.JLabel jLabNomeClasse;
    private javax.swing.JLabel jLabNomeEssencia;
    private javax.swing.JLabel jLabNomeMarca;
    private javax.swing.JLabel jLabNomeSubGrupo;
    private javax.swing.JLabel jLabNomeUnidMedida;
    private javax.swing.JLabel jLabOrigemProduto;
    private javax.swing.JLabel jLabPesoBruto;
    private javax.swing.JLabel jLabPesoLiquido;
    private javax.swing.JLabel jLabProduto;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanCaracteristicas;
    private javax.swing.JPanel jPanDimessoesPeso;
    private javax.swing.JPanel jPanEstoque;
    private javax.swing.JPanel jPanFiscal;
    private javax.swing.JPanel jPanInforProduto;
    private javax.swing.JPanel jPanResumo;
    private javax.swing.JPanel jPanTabela;
    private javax.swing.JPanel jPanePesquisar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabMat;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexCcusto;
    private javax.swing.JTextField jTexCdMaterial;
    private javax.swing.JTextField jTexComprimento;
    private javax.swing.JTextField jTexCtaContabil;
    private javax.swing.JTextField jTexEpessura;
    private javax.swing.JTextField jTexEstoqMinimo;
    private javax.swing.JTextField jTexLargura;
    private javax.swing.JTextField jTexLoteMini;
    private javax.swing.JTextField jTexLoteMulti;
    private javax.swing.JTextField jTexNCM;
    private javax.swing.JTextField jTexNomeCategoria;
    private javax.swing.JTextField jTexNomeClasse;
    private javax.swing.JTextField jTexNomeEssencia;
    private javax.swing.JTextField jTexNomeGrupo;
    private javax.swing.JTextField jTexNomeMarca;
    private javax.swing.JTextField jTexNomeMaterial;
    private javax.swing.JTextField jTexNomeSubGrupo;
    private javax.swing.JTextField jTexNomeUnidMedida;
    private javax.swing.JTextField jTexOrigemProdudo;
    private javax.swing.JTextField jTexPesoBruto;
    private javax.swing.JTextField jTexPesoLiquido;
    private javax.swing.JTextField jTexPesquisar;
    // End of variables declaration//GEN-END:variables

    // Métodos para retornar o produto selecionado
    /**
     * @return the valorUnitBruto retorna o valor unitário bruto do item
     */
    public double getValorUnitBruto() {
        return valorUnitBruto;
    }

    /**
     * @return the valorUnitLiquido retorna o valor unitário líquido do item
     * aplicando o desconto máximo de alçada
     */
    public double getValorUnitLiquido() {
        return valorUnitLiquido;
    }

    /**
     * @return the descAlcada retorna o % de alçada máximo para aplicação de
     * desconto
     */
    public double getDescAlcada() {
        return descAlcada;
    }

    /**
     * @return the selecao3 retorna o tipo de produto do item pesquisado
     */
    public String getTipoProduto() {
        return tipoProduto;
    }

    /**
     * @return the valorServico retorna o o valor do serviço se o item for do
     * tipo serviço
     */
    public double getValorServico() {
        return valorServico;
    }

    /**
     * @return the cdUnidMedida retorna a unidade de medida do item
     */
    public String cdUnidMedida() {
        return cdUnidMedida;
    }

    /**
     * @return the selecao1 retorna o códito do item
     */
    public String getCdMaterial() {
        return cdMaterial;
    }

    /**
     * @return the nomeMaterial retorna a descrição do item
     */
    public String getNomeMaterial() {
        return nomeMaterial;
    }
}
