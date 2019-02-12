/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GCVCA0031
 */
package br.com.gcv.visao;

// Objetos de instância da Classe
import br.com.gcs.visao.PesquisarMateriais;
import br.com.gcs.visao.PesquisarUnidadesMedida;
import br.com.gcv.modelo.ItemTabela;
import br.com.gcv.dao.ItemTabelaDAO;
import br.com.gcv.controle.CItemTabela;
import br.com.gcv.modelo.Tabela;

// Objetos de instância de parâmetros de ambiente
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.DefineCampoInteiro;
import br.com.modelo.FormatarValor;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;

// Objetos de Parâmetros de Sistema
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 21/11/2017
 */
public class ManterItemTabela extends javax.swing.JDialog {

    // Variáveis de intância de objetos principal da classe
    private ItemTabela regCorr;
    private List< ItemTabela> resultado;
    private ItemTabelaDAO itadao;
    private CItemTabela pita;

    // Variáveis de instância de objetos de registros correlatos
    private static Tabela modtab;
    private static ItemTabela modita;

    // Variáveis de instância de objetos de parâmetros da classe
    private static Connection conexao;
    private static SessaoUsuario su;
    private final NumberFormat formato;
    private VerificarTecla vt;

    // Variáveis de instância generalistas da classe
    private static String cdTabela;
    private int numReg = 0;
    private int idxCorr = 0;
    private String sql;
    private String sqlita;
    private String oper;
    private int indexAtual = 0;
    private double oldValorUnit = 0;
    private double oldDescAlcada = 0;
    private boolean changeValorUnit = false;
    private String oldSituacao = "";

    /**
     * Creates new form MaterTarefaAtividade
     */
    public ManterItemTabela(java.awt.Frame parent, boolean modal, Tabela modtab, SessaoUsuario su, Connection conexao) {
        super(parent, modal);
        this.modtab = modtab;
        this.su = su;
        this.conexao = conexao;
        this.cdTabela = modtab.getCdTabela();
        formato = NumberFormat.getInstance();
        formato.setMinimumFractionDigits(2);
        pita = new CItemTabela(conexao);
        modita = new ItemTabela();
        try {
            itadao = new ItemTabelaDAO(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(ManterItemTabela.class.getName()).log(Level.SEVERE, null, ex);
        }

        sql = "SELECT * FROM GCVITEMTABELA WHERE CD_TABELA = " + cdTabela;
        oper = "N";
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formatarCampos();
        limparTela();
        setLocationRelativeTo(null);
        pesquisar();
        buscarTodos();
        this.dispose();
    }

    // método para setar o formato do campo
    private void formatarCampos() {
        jTexCdMaterial.setDocument(new DefineCampoInteiro());
        jForValorUnit.setDocument(new DefineCampoDecimal());
        jForDescAlcada.setDocument(new DefineCampoDecimal());
    }

    // método para limpar tela
    private void limparTela() {
        jTexCdMaterial.setText("");
        jTexNomeMaterial.setText("");
        jTexCdUnidMedida.setText("");
        jTexNomeUnidMedida.setText("");
        jForValorUnit.setText("0.00");
        jForDescAlcada.setText("0.00");
        jComSituacao.setSelectedIndex(0);
        jTexCadastradoPor.setText("");
        jForDataCadastro.setText("");
        jForDataModificacao.setText("");
        jTexRegAtual.setText("");
        jTexRegTotal.setText("");
    }

    //Metodo para buscar todos
    public void buscarTodos() {
        sqlita = "select i.cd_material as 'Cód.',"
                + " m.nome_material as 'Descrição',"
                + " i.cd_unidmedida as 'U.M',"
                + " format(i.valor_unit,2,'de_DE') as Valor,"
                + " format(i.desc_alcada,3,'de_DE') as 'Desc.Alçada',"
                + " i.situacao as 'Situação'"
                + " from gcvitemtabela as i"
                + " left outer join gcsmaterial as m on i.cd_material = m.cd_material "
                + " where i.cd_tabela = " + cdTabela;
        try {
            itadao.setQuery(sqlita);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
        jTabItens.setModel(itadao);
        ajustarTabela();
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {
        try {
            numReg = pita.pesquisar(sql);
            idxCorr = +1;
            if (numReg > 0) {
                upRegistros();
                buscarTodos();
            } else {
                JOptionPane.showMessageDialog(null, "Nenhum registro encontrado!");
                esvaziarTabelas();
            }
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Argumento inválido na busca\nErr: " + iae.getCause());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Pesquisar os Registros\nErr: " + ex);
            ex.printStackTrace();
        }
    }

    // método para atualizar os registros na tela
    private void upRegistros() {
        jTexRegTotal.setText(Integer.toString(numReg));
        jTexRegAtual.setText(Integer.toString(idxCorr));
        pita.mostrarPesquisa(modita, idxCorr - 1,true);
        DataSistema dat = new DataSistema();
        jTexCdMaterial.setText(modita.getCdMaterial().trim());
        jTexNomeMaterial.setText(modita.getNomeMaterial().trim());
        jTexCdUnidMedida.setText(modita.getCdUnidMedida());
        jTexNomeUnidMedida.setText(modita.getNomeUnidMedia());
        jForValorUnit.setText(String.valueOf(modita.getValorUnit()));
        oldValorUnit = modita.getValorUnit();
        jForDescAlcada.setText(String.valueOf(modita.getDescAlcada()));
        oldDescAlcada = modita.getDescAlcada();
        jComSituacao.setSelectedIndex(Integer.parseInt(modita.getSituacao()));
        oldSituacao = modita.getSituacao();
        jTexCadastradoPor.setText(modita.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modita.getDataCadastro())));
        changeValorUnit = false;
        if (modita.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(modita.getDataModificacao())));
        }
        // Habilitando/Desabilitando botões de navegação de registros
        if (numReg > idxCorr) {
            jButProximo.setEnabled(true);
        } else {
            jButProximo.setEnabled(false);
        }
        if (idxCorr > 1) {
            jButAnterior.setEnabled(true);
        } else {
            jButAnterior.setEnabled(false);
        }
    }

    // método para bloquear campos
    private void bloquearCampos() {
        jTexCdMaterial.setEditable(false);
        jTexCdMaterial.setEnabled(false);
        jTexCdUnidMedida.setEditable(false);
        jTexCdUnidMedida.setEnabled(false);
        jForValorUnit.setEditable(false);
        jForValorUnit.setEnabled(false);
        jForDescAlcada.setEditable(false);
        jForDescAlcada.setEnabled(false);
        jComSituacao.setEditable(false);
        jComSituacao.setEnabled(false);
    }

    // método para liberar campos
    private void liberarCampos() {
        jForValorUnit.setEditable(true);
        jForValorUnit.setEnabled(true);
        jForDescAlcada.setEditable(true);
        jForDescAlcada.setEnabled(true);
        jComSituacao.setEditable(true);
        jComSituacao.setEnabled(true);
    }

    // método para criar novo registro
    private void novoRegistro() {
        limparTela();
        liberarCampos();
        jTexCdMaterial.setEditable(true);
        jTexCdMaterial.setEnabled(true);
        jTexCdUnidMedida.setEditable(true);
        jTexCdUnidMedida.setEnabled(true);
        jTexCdMaterial.requestFocus();
        jButSalvar.setEnabled(true);
    }

    private void esvaziarTabelas() {
        jTabItens.setModel(new JTable().getModel());
    }

    private void salvarRegistro() {
        if (jTexCdMaterial.getText().isEmpty() || jTexCdUnidMedida.getText().isEmpty()
                || jForValorUnit.getText().isEmpty() || jComSituacao.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Os campos Material, Unid. Med. Valor e Situação são obrigatórios.");
        } else {
            DataSistema dat = new DataSistema();
            ItemTabela ita = new ItemTabela();
            String data = null;
            ita.setCdTabela(cdTabela);
            ita.setCdMaterial(jTexCdMaterial.getText().trim());
            ita.setCdUnidMedida(jTexCdUnidMedida.getText());
            try {
                ita.setValorUnit(formato.parse(jForValorUnit.getText()).doubleValue());
                String descAlcada = jForDescAlcada.getText().trim().toString();
                if (descAlcada != null) {
                    ita.setDescAlcada(formato.parse(jForDescAlcada.getText()).doubleValue());
                }
            } catch (ParseException ex) {
                Logger.getLogger(ManterTabela.class.getName()).log(Level.SEVERE, null, ex);
            }
            dat.setData(data);
            data = dat.getData();
            ita.setDataCadastro(data);
            ita.setUsuarioCadastro(su.getUsuarioConectado());
            ita.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1));
            ItemTabelaDAO itadao = null;
            CItemTabela cita = new CItemTabela(conexao);
            try {
                itadao = new ItemTabelaDAO(conexao);
                if ("N".equals(oper)) {
                    sql = "SELECT * FROM GCVITEMTABELA WHERE CD_TABELA = " + ita.getCdTabela().trim();
                    itadao.adicionar(ita);
                    modtab.setAtualizacao(true);
                } else {
                    sql = "SELECT * FROM GCVITEMTABELA WHERE CD_TABELA = " + modita.getCdTabela();
                    ita.setDataModificacao(data);
                    itadao.atualizar(ita);
                    if (oldValorUnit != ita.getValorUnit()) {
                        modtab.setDataModificacao(data);
                        modtab.setAtualizacao(true);
                    }
                    if (oldSituacao != String.valueOf(jComSituacao.getSelectedIndex()) && !changeValorUnit) {
                        modtab.setDataModificacao(data);
                        modtab.setAtualizacao(true);
                    }
                }
            } catch (SQLException sqlex) {
                JOptionPane.showMessageDialog(null, "Erro ao criar o item no banco e dados!\nErr: " + sqlex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral para criar item!\nErr: " + ex);
            }

            limparTela();
            bloquearCampos();
            pesquisar();
            jButSalvar.setEnabled(false);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        jTabItens.getColumnModel().getColumn(0).setMinWidth(90);
        jTabItens.getColumnModel().getColumn(0).setPreferredWidth(90);
        jTabItens.getColumnModel().getColumn(1).setMinWidth(200);
        jTabItens.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTabItens.getColumnModel().getColumn(2).setMinWidth(10);
        jTabItens.getColumnModel().getColumn(2).setPreferredWidth(10);
        jTabItens.getColumnModel().getColumn(3).setMinWidth(50);
        jTabItens.getColumnModel().getColumn(3).setPreferredWidth(50);
        jTabItens.getColumnModel().getColumn(4).setMinWidth(50);
        jTabItens.getColumnModel().getColumn(4).setPreferredWidth(50);
        jTabItens.getColumnModel().getColumn(5).setMinWidth(30);
        jTabItens.getColumnModel().getColumn(5).setPreferredWidth(30);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
    }

    // método para dar zoom no campo tarefa
    private void zoomMaterial() {
        PesquisarMateriais zoom = new PesquisarMateriais(new JFrame(), true, "P","R",conexao,false);
        zoom.setVisible(true);
        jTexCdMaterial.setText(zoom.getCdMaterial().trim());
        jTexNomeMaterial.setText(zoom.getNomeMaterial().trim());
    }
    
    // método para dar zoom no campo tarefa
    private void zoomUnidMedida() {
        PesquisarUnidadesMedida zoom = new PesquisarUnidadesMedida(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdUnidMedida.setText(zoom.getSelec1().trim());
        jTexNomeUnidMedida.setText(zoom.getSelec2().trim());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanItemTabela = new javax.swing.JPanel();
        jLabCdMaterial = new javax.swing.JLabel();
        jLabValor = new javax.swing.JLabel();
        jTexCdMaterial = new javax.swing.JTextField();
        jForValorUnit = jForValorUnit = new FormatarValor((FormatarValor.MOEDA));
        jTexNomeMaterial = new javax.swing.JTextField();
        jComSituacao = new javax.swing.JComboBox<>();
        jLabSituacao = new javax.swing.JLabel();
        jLabCdUnidMedida = new javax.swing.JLabel();
        jTexCdUnidMedida = new javax.swing.JTextField();
        jTexNomeUnidMedida = new javax.swing.JTextField();
        jLabDescAlcada = new javax.swing.JLabel();
        jForDescAlcada = new FormatarValor(FormatarValor.PORCENTAGEM);
        jTooMenuFerramentas = new javax.swing.JToolBar();
        jButNovo = new javax.swing.JButton();
        jButEditar = new javax.swing.JButton();
        jButSalvar = new javax.swing.JButton();
        jButCancelar = new javax.swing.JButton();
        jButExcluir = new javax.swing.JButton();
        jButPesquisar = new javax.swing.JButton();
        jButAnterior = new javax.swing.JButton();
        jButProximo = new javax.swing.JButton();
        jButImprimir = new javax.swing.JButton();
        jButSair = new javax.swing.JButton();
        jPanRodape = new javax.swing.JPanel();
        jTexRegAtual = new javax.swing.JTextField();
        jLabReg = new javax.swing.JLabel();
        jTexRegTotal = new javax.swing.JTextField();
        jLabCadastradoPor = new javax.swing.JLabel();
        jTexCadastradoPor = new javax.swing.JTextField();
        jLabDataCadastro = new javax.swing.JLabel();
        jForDataCadastro = new javax.swing.JFormattedTextField();
        jLabDataModificacao = new javax.swing.JLabel();
        jForDataModificacao = new javax.swing.JFormattedTextField();
        jPanTabelaItens = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabItens = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manter Equipamento Atividade");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setModal(true);

        jPanItemTabela.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Incluir Item Tabela", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabCdMaterial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdMaterial.setText("Material:");

        jLabValor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValor.setText("Valor:");

        jTexCdMaterial.setEditable(false);
        jTexCdMaterial.setEnabled(false);
        jTexCdMaterial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexCdMaterialKeyReleased(evt);
            }
        });

        jForValorUnit.setEditable(false);
        jForValorUnit.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForValorUnit.setEnabled(false);

        jTexNomeMaterial.setEditable(false);
        jTexNomeMaterial.setEnabled(false);

        jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ativo", "Inativo" }));
        jComSituacao.setEnabled(false);

        jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabSituacao.setText("Situação:");

        jLabCdUnidMedida.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdUnidMedida.setText("Unid. Med.:");

        jTexCdUnidMedida.setEditable(false);
        jTexCdUnidMedida.setEnabled(false);
        jTexCdUnidMedida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdUnidMedidaKeyPressed(evt);
            }
        });

        jTexNomeUnidMedida.setEditable(false);
        jTexNomeUnidMedida.setEnabled(false);

        jLabDescAlcada.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDescAlcada.setText("Desc. Alçada:");

        jForDescAlcada.setEditable(false);
        jForDescAlcada.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##0.000"))));
        jForDescAlcada.setEnabled(false);

        javax.swing.GroupLayout jPanItemTabelaLayout = new javax.swing.GroupLayout(jPanItemTabela);
        jPanItemTabela.setLayout(jPanItemTabelaLayout);
        jPanItemTabelaLayout.setHorizontalGroup(
            jPanItemTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanItemTabelaLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanItemTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabValor)
                    .addComponent(jLabCdUnidMedida)
                    .addComponent(jLabCdMaterial))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanItemTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanItemTabelaLayout.createSequentialGroup()
                        .addComponent(jTexCdMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexNomeMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabSituacao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComSituacao, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(38, 38, 38))
                    .addGroup(jPanItemTabelaLayout.createSequentialGroup()
                        .addGroup(jPanItemTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanItemTabelaLayout.createSequentialGroup()
                                .addComponent(jForValorUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabDescAlcada)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForDescAlcada, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanItemTabelaLayout.createSequentialGroup()
                                .addComponent(jTexCdUnidMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexNomeUnidMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        jPanItemTabelaLayout.setVerticalGroup(
            jPanItemTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanItemTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanItemTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCdMaterial)
                    .addComponent(jTexCdMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabSituacao))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanItemTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCdUnidMedida)
                    .addComponent(jTexCdUnidMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeUnidMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanItemTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabValor)
                    .addComponent(jForValorUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabDescAlcada)
                    .addComponent(jForDescAlcada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTooMenuFerramentas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTooMenuFerramentas.setRollover(true);

        jButNovo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Addthis-32.png"))); // NOI18N
        jButNovo.setText("Novo");
        jButNovo.setFocusable(false);
        jButNovo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButNovo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButNovoActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButNovo);

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

        jButCancelar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Cancel-32.png"))); // NOI18N
        jButCancelar.setText("Cancelar");
        jButCancelar.setFocusable(false);
        jButCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButCancelarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButCancelar);

        jButExcluir.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Delete-32.png"))); // NOI18N
        jButExcluir.setText("Excluir");
        jButExcluir.setFocusable(false);
        jButExcluir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButExcluir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButExcluirActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButExcluir);

        jButPesquisar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Search-32.png"))); // NOI18N
        jButPesquisar.setText("Pesquisar");
        jButPesquisar.setFocusable(false);
        jButPesquisar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButPesquisar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButPesquisarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButPesquisar);

        jButAnterior.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButAnterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Back-32.png"))); // NOI18N
        jButAnterior.setText("Anterior");
        jButAnterior.setFocusable(false);
        jButAnterior.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButAnterior.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButAnteriorActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButAnterior);

        jButProximo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButProximo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Next-32.png"))); // NOI18N
        jButProximo.setText("Proximo");
        jButProximo.setFocusable(false);
        jButProximo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButProximo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButProximo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButProximoActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButProximo);

        jButImprimir.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Print_32.png"))); // NOI18N
        jButImprimir.setText("Imprimir");
        jButImprimir.setFocusable(false);
        jButImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButImprimirActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButImprimir);

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

        jPanRodape.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTexRegAtual.setEditable(false);
        jTexRegAtual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexRegAtual.setEnabled(false);

        jLabReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabReg.setText("\\");

            jTexRegTotal.setEditable(false);
            jTexRegTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            jTexRegTotal.setEnabled(false);

            jLabCadastradoPor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCadastradoPor.setText("Cadastrado por:");

            jTexCadastradoPor.setEditable(false);
            jTexCadastradoPor.setEnabled(false);

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

            javax.swing.GroupLayout jPanRodapeLayout = new javax.swing.GroupLayout(jPanRodape);
            jPanRodape.setLayout(jPanRodapeLayout);
            jPanRodapeLayout.setHorizontalGroup(
                jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanRodapeLayout.createSequentialGroup()
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
                    .addGap(47, 47, 47)
                    .addComponent(jLabDataCadastro)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabDataModificacao)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(24, 24, 24))
            );
            jPanRodapeLayout.setVerticalGroup(
                jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanRodapeLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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

            jPanTabelaItens.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Itens", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jTabItens.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabItens.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null, null},
                    {null, null, null, null, null, null},
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
                    "Cod.", "Descrição", "U.M", "Valor", "Desc. Alçada", "Situação"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    true, true, false, true, false, true
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
                jTabItens.getColumnModel().getColumn(0).setPreferredWidth(90);
                jTabItens.getColumnModel().getColumn(1).setResizable(false);
                jTabItens.getColumnModel().getColumn(1).setPreferredWidth(200);
                jTabItens.getColumnModel().getColumn(2).setResizable(false);
                jTabItens.getColumnModel().getColumn(2).setPreferredWidth(10);
                jTabItens.getColumnModel().getColumn(3).setResizable(false);
                jTabItens.getColumnModel().getColumn(3).setPreferredWidth(50);
                jTabItens.getColumnModel().getColumn(4).setResizable(false);
                jTabItens.getColumnModel().getColumn(4).setPreferredWidth(50);
                jTabItens.getColumnModel().getColumn(5).setResizable(false);
                jTabItens.getColumnModel().getColumn(5).setPreferredWidth(30);
            }

            javax.swing.GroupLayout jPanTabelaItensLayout = new javax.swing.GroupLayout(jPanTabelaItens);
            jPanTabelaItens.setLayout(jPanTabelaItensLayout);
            jPanTabelaItensLayout.setHorizontalGroup(
                jPanTabelaItensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1)
            );
            jPanTabelaItensLayout.setVerticalGroup(
                jPanTabelaItensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanItemTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanRodape, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, 734, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
                .addComponent(jPanTabelaItens, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanItemTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanTabelaItens, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButNovoActionPerformed
        // TODO add your handling code here:
        novoRegistro();
        oper = "N";
    }//GEN-LAST:event_jButNovoActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        // TODO add your handling code here:
        liberarCampos();
        jTexCdMaterial.requestFocus();
        oper = "A";
        jButSalvar.setEnabled(true);
    }//GEN-LAST:event_jButEditarActionPerformed

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        // TODO add your handling code here:
        salvarRegistro();

    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
        // TODO add your handling code here:
        limparTela();
        oper = "N";
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        // TODO add your handling code here:
        if (!jTexCdMaterial.getText().isEmpty()) {
            try {
                DataSistema dat = new DataSistema();
                String data = null;
                ItemTabela ea = new ItemTabela();
                ea.setCdTabela(cdTabela);
                ea.setCdMaterial(jTexCdMaterial.getText().toUpperCase());
                ea.setCdUnidMedida(jTexCdUnidMedida.getText());
                ItemTabelaDAO eaDAO = new ItemTabelaDAO(conexao);
                eaDAO.excluir(ea);
                dat.setData(data);
                data = dat.getData();
                modtab.setDataModificacao(data);
                modtab.setAtualizacao(true);
                limparTela();
                jButPesquisarActionPerformed(evt);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Não foi possível excluir o registro da tela");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral na exclusão do registro!");
            }
        }
    }//GEN-LAST:event_jButExcluirActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:
        sql = "SELECT * FROM GCVITEMTABELA WHERE CD_TABELA = " + cdTabela;
        bloquearCampos();
        pesquisar();
    }//GEN-LAST:event_jButPesquisarActionPerformed

    private void jButAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAnteriorActionPerformed
        // TODO add your handling code here:
        idxCorr -= 1;
        upRegistros();
    }//GEN-LAST:event_jButAnteriorActionPerformed

    private void jButProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButProximoActionPerformed
        // TODO add your handling code here:
        idxCorr += 1;
        upRegistros();
    }//GEN-LAST:event_jButProximoActionPerformed

    private void jButImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButImprimirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButImprimirActionPerformed

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jTexCdMaterialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdMaterialKeyReleased
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomMaterial();
        }
    }//GEN-LAST:event_jTexCdMaterialKeyReleased

    private void jTexCdUnidMedidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdUnidMedidaKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomUnidMedida();
        }
    }//GEN-LAST:event_jTexCdUnidMedidaKeyPressed

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
            java.util.logging.Logger.getLogger(ManterItemTabela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterItemTabela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterItemTabela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterItemTabela.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                ManterItemTabela dialog = new ManterItemTabela(new javax.swing.JFrame(),
                        true, modtab, su, conexao);
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
    private javax.swing.JButton jButAnterior;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButEditar;
    private javax.swing.JButton jButExcluir;
    private javax.swing.JButton jButImprimir;
    private javax.swing.JButton jButNovo;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JButton jButProximo;
    private javax.swing.JButton jButSair;
    private javax.swing.JButton jButSalvar;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JFormattedTextField jForDescAlcada;
    private javax.swing.JFormattedTextField jForValorUnit;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabCdMaterial;
    private javax.swing.JLabel jLabCdUnidMedida;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabDescAlcada;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabValor;
    private javax.swing.JPanel jPanItemTabela;
    private javax.swing.JPanel jPanRodape;
    private javax.swing.JPanel jPanTabelaItens;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabItens;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexCdMaterial;
    private javax.swing.JTextField jTexCdUnidMedida;
    private javax.swing.JTextField jTexNomeMaterial;
    private javax.swing.JTextField jTexNomeUnidMedida;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
