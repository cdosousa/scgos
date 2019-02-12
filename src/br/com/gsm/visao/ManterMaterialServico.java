/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa: GSMCA0041
 */
package br.com.gsm.visao;

import br.com.gcs.visao.PesquisarMateriais;
import br.com.gcs.visao.PesquisarUnidadesMedida;
import br.com.gsm.controle.CMateriaisServico;
import br.com.gsm.dao.MateriaisServicoDAO;
import br.com.gsm.modelo.MateriaisServico;
import br.com.gsm.modelo.Servicos;
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.DefineCampoInteiro;
import br.com.modelo.FormatarValor;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
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
 * @version 0.01_beta0917 created on 16/10/2017
 */
public class ManterMaterialServico extends javax.swing.JDialog {

    private static Connection conexao;
    private MateriaisServico regCorr;
    private List< MateriaisServico> resultado;
    private CMateriaisServico cms;
    private static MateriaisServico modms;
    private MateriaisServicoDAO msdao;
    private static Servicos modsrv;
    private VerificarTecla vt;
    private int numReg = 0;
    private int idxCorr = 0;
    private String sql;
    private String sqlta;
    private String oper;
    private static SessaoUsuario su;
    private final NumberFormat formato;
    private static String cdServico;
    private static int sequencia;
    private int indexAtual = 0;
    private double oldValorUnit = 0;
    private boolean changeValorUnit = false;
    private String oldSituacao = "";

    /**
     * Creates new form MaterTarefaAtividade
     */
    public ManterMaterialServico(java.awt.Frame parent, boolean modal, Servicos modsrv, int sequencia,
            SessaoUsuario su, Connection conexao) {
        super(parent, modal);
        this.modsrv = modsrv;
        this.su = su;
        this.conexao = conexao;
        this.cdServico = modsrv.getCdServico();
        this.sequencia = sequencia;
        formato = NumberFormat.getInstance();
        formato.setMinimumFractionDigits(2);
        cms = new CMateriaisServico(conexao);
        modms = new MateriaisServico();
        try {
            msdao = new MateriaisServicoDAO(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(ManterMaterialServico.class.getName()).log(Level.SEVERE, null, ex);
        }
        sql = "select ms.seq,"
                + "ms.cd_servico,"
                + "ms.cd_material,"
                + "mat.nome_material,"
                + "ms.cd_unidmedida,"
                + "um.nome_unidmedida, "
                + "ms.qtde_material,"
                + "ms.valor_unit,"
                + "ms.usuario_cadastro,"
                + "ms.data_cadastro,"
                + "ms.data_modificacao,"
                + "ms.situacao"
                + " from gsmmaterialservico as ms"
                + " left outer join gcsmaterial as mat on ms.cd_material = mat.cd_material"
                + " left outer join gcsunidmedida as um on ms.cd_unidmedida = um.cd_unidmedida"
                + " where ms.cd_servico = " + cdServico;
        oper = "N";
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formatarCampos();
        limparTela();
        setLocationRelativeTo(null);
        pesquisar();
        this.dispose();
    }

    // método para setar o formato do campo
    private void formatarCampos() {
        jTexCodigoMaterial.setDocument(new DefineCampoInteiro());
        jForQtde.setDocument(new DefineCampoDecimal());
        jForValorUnit.setDocument(new DefineCampoDecimal());
    }

    // método para limpar tela
    private void limparTela() {
        jTexCodigoMaterial.setText("");
        jTexNomeMaterial.setText("");
        jTexCodigoUnidMedida.setText("");
        jTexNomeUnidMedida.setText("");
        jForQtde.setText("0.00");
        jForValorUnit.setText("0.00");
        jComSituacao.setSelectedIndex(0);
        jTexCadastradoPor.setText("");
        jForDataCadastro.setText("");
        jForDataModificacao.setText("");
        jTexRegAtual.setText("");
        jTexRegTotal.setText("");
    }

    //Metodo para buscar todos
    public void buscarTodos() {
        sqlta = "select ms.seq as 'Seq.',"
                + "ms.cd_material as 'Cod.',"
                + "mat.nome_material as 'Descrição',"
                + "ms.cd_unidmedida as 'UM', "
                + "format(ms.qtde_material,3,'de_DE') as 'Qtd.', "
                + "format(ms.valor_unit,2,'de_DE') as Valor"
                + " from gsmmaterialservico as ms"
                + " left outer join gcsmaterial as mat on ms.cd_material = mat.cd_material "
                + " where ms.cd_servico = " + cdServico;
        try {
            msdao.setQuery(sqlta);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
        jTabMateriais.setModel(msdao);
        ajustarTabela();
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {
        try {
            numReg = cms.pesquisar(sql);
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
        cms.mostrarPesquisa(modms, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jTexCodigoMaterial.setText(modms.getCdMaterial().trim());
        jTexNomeMaterial.setText(modms.getNomeMaterial().trim());
        jTexCodigoUnidMedida.setText(modms.getCdUnidMedida().trim());
        jTexNomeUnidMedida.setText(modms.getNomeUnidMedida().trim());
        jForQtde.setText(String.valueOf(modms.getQtdeMaterial()));
        jForValorUnit.setText(String.valueOf(modms.getValorUnit()));
        oldValorUnit = modms.getValorUnit();
        jComSituacao.setSelectedIndex(Integer.parseInt(modms.getSituacao()));
        oldSituacao = modms.getSituacao();
        jTexCadastradoPor.setText(modms.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modms.getDataCadastro())));
        changeValorUnit = false;
        if (modms.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(modms.getDataModificacao())));
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
        jTexCodigoMaterial.setEnabled(false);
        jTexCodigoMaterial.setEditable(false);
        jTexCodigoUnidMedida.setEditable(false);
        jTexCodigoUnidMedida.setEnabled(false);
        jForQtde.setEditable(false);
        jForQtde.setEnabled(false);
        jForValorUnit.setEditable(false);
        jForValorUnit.setEnabled(false);
        jComSituacao.setEditable(false);
        jComSituacao.setEnabled(false);
    }

    // método para liberar campos
    private void liberarCampos() {
        jTexCodigoUnidMedida.setEnabled(true);
        jTexCodigoUnidMedida.setEditable(true);
        jForQtde.setEditable(true);
        jForQtde.setEnabled(true);
        jForValorUnit.setEditable(true);
        jForValorUnit.setEnabled(true);
        jComSituacao.setEditable(true);
        jComSituacao.setEnabled(true);
    }

    // método para criar novo registro
    private void novoRegistro() {
        limparTela();
        liberarCampos();
        jTexCodigoMaterial.setEditable(true);
        jTexCodigoMaterial.setEnabled(true);
        jTexCodigoMaterial.requestFocus();
        jButSalvar.setEnabled(true);
    }

    private void esvaziarTabelas() {
        jTabMateriais.setModel(new JTable().getModel());
    }

    // método para salvar registro
    private void salvarRegistro() {
        if (jTexCodigoMaterial.getText().isEmpty() || jTexCodigoUnidMedida.getText().isEmpty() || jComSituacao.getSelectedIndex() == 0
                || jForQtde.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Os campos Material, Unidade de Medida, Quantidade e Situação são obrigatórios.");
        } else {
            DataSistema dat = new DataSistema();
            MateriaisServico ms = new MateriaisServico();
            String data = null;
            ms.setCdServico(cdServico);
            ms.setCdMaterial(jTexCodigoMaterial.getText().trim());
            ms.setNomeMaterial(jTexNomeMaterial.getText().trim().toUpperCase());
            ms.setCdUnidMedida(jTexCodigoUnidMedida.getText().trim());
            ms.setNomeUnidMedida(jTexNomeUnidMedida.getText().trim());
            try {
                ms.setQtdeMaterial(formato.parse(jForQtde.getText()).doubleValue());
                ms.setValorUnit(formato.parse(jForValorUnit.getText()).doubleValue());
                ms.setValorMaterial(ms.getValorMaterial() + ms.getValorUnit());
            } catch (ParseException ex) {
                Logger.getLogger(ManterAtividades.class.getName()).log(Level.SEVERE, null, ex);
            }
            dat.setData(data);
            data = dat.getData();
            ms.setDataCadastro(data);
            ms.setUsuarioCadastro(su.getUsuarioConectado());
            ms.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1));
            MateriaisServicoDAO msdao = null;
            CMateriaisServico cms = new CMateriaisServico(conexao);
            try {
                msdao = new MateriaisServicoDAO(conexao);
                if ("N".equals(oper)) {
                    sequencia += 1;
                    ms.setSequencia(sequencia);
                    //sql = "SELECT * FROM GSMTAREFAATIVIDADE WHERE CD_ATIVIDADE = " + ms.getCdAtividade().trim();
                    msdao.adicionar(ms);
                    modms.setAtualizacao(true);
                    modms.setValTotalTarefa(modms.getValTotalTarefa() + ms.getValorUnit());
                    modms.setValTotalAtividade(modms.getValTotalEquipamento() + modms.getValTotalTarefa());
                    modsrv.setAtualizacao(true);
                } else {
                    ms.setSequencia(modms.getSequencia());
                    //sql = "SELECT * FROM GSMTAREFAATIVIDADE WHERE CD_ATIVIDADE = " + modta.getCdAtividade();
                    ms.setCdAtividade(modms.getCdAtividade());
                    ms.setDataModificacao(data);
                    msdao.atualizar(ms);
                    if (oldValorUnit != ms.getValorUnit()) {
                        changeValorUnit = true;
                        if (oldValorUnit > ms.getValorUnit() && "A".equals(ms.getSituacao()) && !"2".equals(oldSituacao)) {
                            modms.setValorMaterial(modms.getValorMaterial() - (oldValorUnit - ms.getValorUnit()));
                        } else if (oldValorUnit < ms.getValorUnit() && "A".equals(ms.getSituacao())) {
                            modms.setValorMaterial(modms.getValorMaterial() + (ms.getValorUnit() - oldValorUnit));
                        }
                        modms.setDataModificacao(data);
                        modms.setValorServico(modms.getValTotalAtividade() + modms.getValorMaterial());
                        modsrv.setAtualizacao(true);
                    }
                    if (oldSituacao != String.valueOf(jComSituacao.getSelectedIndex()) && !changeValorUnit) {
                        if ("A".equals(ms.getSituacao())) {
                            modms.setValorMaterial(modms.getValorMaterial() + ms.getValorUnit());
                        } else if ("I".equals(ms.getSituacao())) {
                            modms.setValorMaterial(modms.getValorMaterial() - ms.getValorUnit());
                        }
                        modms.setDataModificacao(data);
                        modms.setValorServico(modms.getValTotalAtividade() + modms.getValorMaterial());
                        modsrv.setAtualizacao(true);
                    }
                }
            } catch (SQLException sqlex) {
                JOptionPane.showMessageDialog(null, "Erro criar a tarefa no banco e dados!\nErr: " + sqlex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral para criar tarefa!\nErr: " + ex);
            }

            limparTela();
            bloquearCampos();
            pesquisar();
            jButSalvar.setEnabled(false);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        jTabMateriais.getColumnModel().getColumn(0).setMinWidth(10);
        jTabMateriais.getColumnModel().getColumn(0).setPreferredWidth(10);
        jTabMateriais.getColumnModel().getColumn(1).setMinWidth(20);
        jTabMateriais.getColumnModel().getColumn(1).setPreferredWidth(10);
        jTabMateriais.getColumnModel().getColumn(2).setMinWidth(100);
        jTabMateriais.getColumnModel().getColumn(2).setPreferredWidth(100);
        jTabMateriais.getColumnModel().getColumn(3).setMinWidth(10);
        jTabMateriais.getColumnModel().getColumn(3).setPreferredWidth(10);
        jTabMateriais.getColumnModel().getColumn(4).setMinWidth(20);
        jTabMateriais.getColumnModel().getColumn(4).setPreferredWidth(20);
        jTabMateriais.getColumnModel().getColumn(5).setMinWidth(20);
        jTabMateriais.getColumnModel().getColumn(5).setPreferredWidth(20);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
    }

    // método para dar zoom no campo material
    private void zoomMaterial() {
        PesquisarMateriais zoom = new PesquisarMateriais(new JFrame(), true, "P","I",conexao,false);
        zoom.setVisible(true);
        jTexCodigoMaterial.setText(zoom.getCdMaterial().trim());
        jTexNomeMaterial.setText(zoom.getNomeMaterial().trim());
    }

    //método para dar zoom no campo unidade medida
    private void zoomUnidMedida() {
        PesquisarUnidadesMedida zoom = new PesquisarUnidadesMedida(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCodigoUnidMedida.setText(zoom.getSelec1().trim());
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

        jPanTarefasAtividade = new javax.swing.JPanel();
        jLabMaterial = new javax.swing.JLabel();
        jLabUnidMedida = new javax.swing.JLabel();
        jLabQtde = new javax.swing.JLabel();
        jTexCodigoMaterial = new javax.swing.JTextField();
        jTexCodigoUnidMedida = new javax.swing.JTextField();
        jForQtde = new FormatarValor((FormatarValor.PESO));
        jTexNomeMaterial = new javax.swing.JTextField();
        jTexNomeUnidMedida = new javax.swing.JTextField();
        jComSituacao = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jLabValor = new javax.swing.JLabel();
        jForValorUnit = new FormatarValor((FormatarValor.MOEDA));
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
        jPanTabelaTarefas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabMateriais = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manter Tarefa Atividade");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setModal(true);

        jPanTarefasAtividade.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Incluir Tarefas x Atividades", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabMaterial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabMaterial.setText("Material:");

        jLabUnidMedida.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabUnidMedida.setText("Unid. Medida:");

        jLabQtde.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabQtde.setText("Qtde.:");

        jTexCodigoMaterial.setEnabled(false);
        jTexCodigoMaterial.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexCodigoMaterialKeyReleased(evt);
            }
        });

        jTexCodigoUnidMedida.setEnabled(false);
        jTexCodigoUnidMedida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexCodigoUnidMedidaKeyReleased(evt);
            }
        });

        jForQtde.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForQtde.setEnabled(false);

        jTexNomeMaterial.setEditable(false);
        jTexNomeMaterial.setEnabled(false);

        jTexNomeUnidMedida.setEditable(false);
        jTexNomeUnidMedida.setEnabled(false);

        jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ativo", "Inativo" }));
        jComSituacao.setEnabled(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Situação:");

        jLabValor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValor.setText("Valor:");

        jForValorUnit.setEditable(false);
        jForValorUnit.setEnabled(false);

        javax.swing.GroupLayout jPanTarefasAtividadeLayout = new javax.swing.GroupLayout(jPanTarefasAtividade);
        jPanTarefasAtividade.setLayout(jPanTarefasAtividadeLayout);
        jPanTarefasAtividadeLayout.setHorizontalGroup(
            jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTarefasAtividadeLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabQtde, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabUnidMedida, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabMaterial, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanTarefasAtividadeLayout.createSequentialGroup()
                        .addGroup(jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jForQtde, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTexCodigoMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanTarefasAtividadeLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jTexNomeMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 272, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanTarefasAtividadeLayout.createSequentialGroup()
                                .addGap(15, 15, 15)
                                .addComponent(jLabValor)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForValorUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanTarefasAtividadeLayout.createSequentialGroup()
                        .addComponent(jTexCodigoUnidMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexNomeUnidMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanTarefasAtividadeLayout.setVerticalGroup(
            jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTarefasAtividadeLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabMaterial)
                    .addComponent(jTexCodigoMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabUnidMedida)
                    .addComponent(jTexCodigoUnidMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeUnidMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabQtde)
                    .addComponent(jForQtde, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabValor)
                    .addComponent(jForValorUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(43, Short.MAX_VALUE))
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
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
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

            jPanTabelaTarefas.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Tarefas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jTabMateriais.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabMateriais.setModel(new javax.swing.table.DefaultTableModel(
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
                    "Seq.", "Cod.", "Descrição", "UM", "Qtde", "Valor"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class, java.lang.Double.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jScrollPane1.setViewportView(jTabMateriais);
            if (jTabMateriais.getColumnModel().getColumnCount() > 0) {
                jTabMateriais.getColumnModel().getColumn(0).setResizable(false);
                jTabMateriais.getColumnModel().getColumn(0).setPreferredWidth(10);
                jTabMateriais.getColumnModel().getColumn(1).setResizable(false);
                jTabMateriais.getColumnModel().getColumn(1).setPreferredWidth(20);
                jTabMateriais.getColumnModel().getColumn(2).setResizable(false);
                jTabMateriais.getColumnModel().getColumn(2).setPreferredWidth(100);
                jTabMateriais.getColumnModel().getColumn(3).setResizable(false);
                jTabMateriais.getColumnModel().getColumn(3).setPreferredWidth(10);
                jTabMateriais.getColumnModel().getColumn(4).setResizable(false);
                jTabMateriais.getColumnModel().getColumn(4).setPreferredWidth(20);
                jTabMateriais.getColumnModel().getColumn(5).setResizable(false);
                jTabMateriais.getColumnModel().getColumn(5).setPreferredWidth(20);
            }

            javax.swing.GroupLayout jPanTabelaTarefasLayout = new javax.swing.GroupLayout(jPanTabelaTarefas);
            jPanTabelaTarefas.setLayout(jPanTabelaTarefasLayout);
            jPanTabelaTarefasLayout.setHorizontalGroup(
                jPanTabelaTarefasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTabelaTarefasLayout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 563, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );
            jPanTabelaTarefasLayout.setVerticalGroup(
                jPanTabelaTarefasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanRodape, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanTabelaTarefas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanTarefasAtividade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanTarefasAtividade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanTabelaTarefas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        jTexCodigoMaterial.requestFocus();
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
        if (!jTexCodigoMaterial.getText().isEmpty()) {
            try {
                DataSistema dat = new DataSistema();
                String data = null;
                MateriaisServico ms = new MateriaisServico();
                ms.setCdServico(cdServico);
                ms.setCdMaterial(jTexCodigoMaterial.getText().toUpperCase());
                ms.setSequencia(modms.getSequencia());
                MateriaisServicoDAO msDAO = new MateriaisServicoDAO(conexao);
                msDAO.excluir(ms);
                dat.setData(data);
                data = dat.getData();
                modms.setDataModificacao(data);
                modms.setValorMaterial(modms.getValorMaterial() - oldValorUnit);
                modms.setValTotalAtividade(modms.getValorMaterial() + modms.getValTotalAtividade());
                modsrv.setAtualizacao(true);
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
        //sql = "SELECT * FROM GSMTAREFAATIVIDADE WHERE CD_ATIVIDADE = " + cdServico;
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

    private void jTexCodigoMaterialKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCodigoMaterialKeyReleased
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomMaterial();
        }
    }//GEN-LAST:event_jTexCodigoMaterialKeyReleased

    private void jTexCodigoUnidMedidaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCodigoUnidMedidaKeyReleased
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomUnidMedida();
        }
    }//GEN-LAST:event_jTexCodigoUnidMedidaKeyReleased

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
            java.util.logging.Logger.getLogger(ManterMaterialServico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterMaterialServico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterMaterialServico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterMaterialServico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ManterMaterialServico dialog = new ManterMaterialServico(new javax.swing.JFrame(), true,
                        modms, sequencia, su, conexao);
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
    private javax.swing.JFormattedTextField jForQtde;
    private javax.swing.JFormattedTextField jForValorUnit;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabMaterial;
    private javax.swing.JLabel jLabQtde;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabUnidMedida;
    private javax.swing.JLabel jLabValor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanRodape;
    private javax.swing.JPanel jPanTabelaTarefas;
    private javax.swing.JPanel jPanTarefasAtividade;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabMateriais;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexCodigoMaterial;
    private javax.swing.JTextField jTexCodigoUnidMedida;
    private javax.swing.JTextField jTexNomeMaterial;
    private javax.swing.JTextField jTexNomeUnidMedida;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
