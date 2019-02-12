/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa: GSMCA0041
 */
package br.com.gsm.visao;

import br.com.gsm.controle.CTarefasAtividades;
import br.com.gsm.dao.TarefasAtividadesDAO;
import br.com.gsm.modelo.Atividades;
import br.com.gsm.modelo.TarefasAtividade;
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.DefineCampoInteiro;
import br.com.modelo.FormatarValor;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;
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
 * @version 0.01_beta0917 created on 16/10/2017
 */
public class ManterTarefaAtividade extends javax.swing.JDialog {

    private TarefasAtividade regCorr;
    private List< TarefasAtividade> resultado;
    private CTarefasAtividades pta;
    private static TarefasAtividade modta;
    private TarefasAtividadesDAO tadao;
    private static Atividades modatv;
    private VerificarTecla vt;
    private int numReg = 0;
    private int idxCorr = 0;
    private String sql;
    private String sqlta;
    private String oper;
    private static SessaoUsuario su;
    private static Connection conexao;
    private final NumberFormat formato;
    private static String cdAtividade;
    private static int sequencia;
    private int indexAtual = 0;
    private double oldValorUnit = 0;
    private boolean changeValorUnit = false;
    private String oldSituacao = "";

    /**
     * Creates new form MaterTarefaAtividade
     */
    public ManterTarefaAtividade(java.awt.Frame parent, boolean modal, Atividades modatv, int sequencia,
            SessaoUsuario su, Connection conexao) {
        super(parent, modal);
        this.modatv = modatv;
        this.su = su;
        this.conexao = conexao;
        this.cdAtividade = modatv.getCdAtividade();
        this.sequencia = sequencia;
        formato = NumberFormat.getInstance();
        formato.setMinimumFractionDigits(2);
        pta = new CTarefasAtividades(conexao);
        modta = new TarefasAtividade();
        try {
            tadao = new TarefasAtividadesDAO(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(ManterTarefaAtividade.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        sql = "SELECT * FROM GSMTAREFAATIVIDADE WHERE CD_ATIVIDADE = " + cdAtividade;
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
        jTexCodigoTarefa.setDocument(new DefineCampoInteiro());
        jTexCodigoEspecialidade.setDocument(new DefineCampoInteiro());
        jForValorTarefa.setDocument(new DefineCampoDecimal());
    }

    // método para limpar tela
    private void limparTela() {
        jTexCodigoTarefa.setText("");
        jTexNomeTarefa.setText("");
        jTexCodigoEspecialidade.setText("");
        jTexNomeEspecialidade.setText("");
        jForValorTarefa.setText("0.00");
        jComSituacao.setSelectedIndex(0);
        jTexCadastradoPor.setText("");
        jForDataCadastro.setText("");
        jForDataModificacao.setText("");
        jTexRegAtual.setText("");
        jTexRegTotal.setText("");
    }

    //Metodo para buscar todos
    public void buscarTodos() {
        sqlta = "select ta.sequencia as 'Seq.', "
                + "t.nome_tarefa as Descrição, "
                + "e.nome_especialidade as Especialidade, "
                + "format(ta.valor_unit,2,'de_DE')as Valor, "
                + "ta.situacao as Situação "
                + "from gsmtarefaatividade as ta "
                + "left outer join gsmtarefas as t on ta.cd_tarefa = t.cd_tarefa "
                + "left outer join gsmespecialidades as e on ta.cd_especialidade = e.cd_especialidade "
                + "where ta.cd_atividade = " + cdAtividade;
        try {
            tadao.setQuery(sqlta);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
        jTabTarefas.setModel(tadao);
        ajustarTabela();
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {
        try {
            numReg = pta.pesquisar(sql);
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
        pta.mostrarPesquisa(modta, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jTexCodigoTarefa.setText(modta.getCdTarefa().trim());
        jTexNomeTarefa.setText(modta.getNomeTarefa().trim());
        jTexCodigoEspecialidade.setText(modta.getCdEspecialidade().trim());
        jTexNomeEspecialidade.setText(modta.getNomeEspecialidade().trim());
        jForValorTarefa.setText(String.valueOf(modta.getValorUnit()));
        oldValorUnit = modta.getValorUnit();
        jComSituacao.setSelectedIndex(Integer.parseInt(modta.getSituacao()));
        oldSituacao = modta.getSituacao();
        jTexCadastradoPor.setText(modta.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modta.getDataCadastro())));
        changeValorUnit = false;
        if (modta.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(modta.getDataModificacao())));
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
        jTexCodigoTarefa.setEnabled(false);
        jTexCodigoTarefa.setEditable(false);
        jTexCodigoEspecialidade.setEditable(false);
        jTexCodigoEspecialidade.setEnabled(false);
        jForValorTarefa.setEditable(false);
        jForValorTarefa.setEnabled(false);
        jComSituacao.setEditable(false);
        jComSituacao.setEnabled(false);
    }

    // método para liberar campos
    private void liberarCampos() {
        jTexCodigoEspecialidade.setEnabled(true);
        jTexCodigoEspecialidade.setEditable(true);
        jForValorTarefa.setEditable(true);
        jForValorTarefa.setEnabled(true);
        jComSituacao.setEditable(true);
        jComSituacao.setEnabled(true);
    }

    // método para criar novo registro
    private void novoRegistro() {
        limparTela();
        liberarCampos();
        jTexCodigoTarefa.setEditable(true);
        jTexCodigoTarefa.setEnabled(true);
        jTexCodigoTarefa.requestFocus();
        jButSalvar.setEnabled(true);
    }
    
    private void esvaziarTabelas() {
        jTabTarefas.setModel(new JTable().getModel());
    }

    // método para salvar registro
    private void salvarRegistro() {
        if (jTexCodigoTarefa.getText().isEmpty() || jTexCodigoEspecialidade.getText().isEmpty() || jComSituacao.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Os campos Tarefa, Especialidade e Situação são obrigatórios.");
        } else {
            DataSistema dat = new DataSistema();
            TarefasAtividade atv = new TarefasAtividade();
            String data = null;
            atv.setCdAtividade(cdAtividade);
            atv.setCdTarefa(jTexCodigoTarefa.getText().trim());
            atv.setNomeTarefa(jTexNomeTarefa.getText().trim().toUpperCase());
            atv.setCdEspecialidade(jTexCodigoEspecialidade.getText().trim());
            atv.setNomeEspecialidade(jTexNomeEspecialidade.getText().trim());
            try {
                atv.setValorUnit(formato.parse(jForValorTarefa.getText()).doubleValue());
                atv.setValTotalTarefa(formato.parse(jForValorTarefa.getText()).doubleValue());
                atv.setValTotalAtividade(atv.getValTotalAtividade() + atv.getValTotalTarefa());
            } catch (ParseException ex) {
                Logger.getLogger(ManterAtividades.class.getName()).log(Level.SEVERE, null, ex);
            }
            dat.setData(data);
            data = dat.getData();
            atv.setDataCadastro(data);
            atv.setUsuarioCadastro(su.getUsuarioConectado());
            atv.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1));
            TarefasAtividadesDAO atvdao = null;
            CTarefasAtividades catv = new CTarefasAtividades(conexao);
            try {
                atvdao = new TarefasAtividadesDAO(conexao);
                if ("N".equals(oper)) {
                    sequencia += 1;
                    atv.setSequencia(String.valueOf(sequencia));
                    sql = "SELECT * FROM GSMTAREFAATIVIDADE WHERE CD_ATIVIDADE = " + atv.getCdAtividade().trim();
                    atvdao.adicionar(atv);
                    modatv.setAtualizacao(true);
                    modatv.setValTotalTarefa(modatv.getValTotalTarefa() + atv.getValorUnit());
                    modatv.setValTotalAtividade(modatv.getValTotalEquipamento() + modatv.getValTotalTarefa());
                    modatv.setAtualizacao(true);
                } else {
                    atv.setSequencia(modta.getSequencia());
                    sql = "SELECT * FROM GSMTAREFAATIVIDADE WHERE CD_ATIVIDADE = " + modta.getCdAtividade();
                    atv.setCdAtividade(modta.getCdAtividade());
                    atv.setDataModificacao(data);
                    atvdao.atualizar(atv);
                    if (oldValorUnit != atv.getValorUnit()) {
                        changeValorUnit = true;
                        if (oldValorUnit > atv.getValorUnit()&& "A".equals(atv.getSituacao()) && !"2".equals(oldSituacao)) {
                            modatv.setValTotalTarefa(modatv.getValTotalTarefa() - (oldValorUnit - atv.getValorUnit()));
                        } else if (oldValorUnit < atv.getValorUnit()&& "A".equals(atv.getSituacao())) {
                            modatv.setValTotalTarefa(modatv.getValTotalTarefa() + (atv.getValorUnit() - oldValorUnit));
                        }
                        modatv.setDataModificacao(data);
                        modatv.setValTotalAtividade(modatv.getValTotalEquipamento() + modatv.getValTotalTarefa());
                        modatv.setAtualizacao(true);
                    }
                    if (oldSituacao != String.valueOf(jComSituacao.getSelectedIndex()) && !changeValorUnit) {
                        if ("A".equals(atv.getSituacao())) {
                            modatv.setValTotalTarefa(modatv.getValTotalTarefa() + atv.getValorUnit());
                        } else if ("I".equals(atv.getSituacao())) {
                            modatv.setValTotalTarefa(modatv.getValTotalTarefa() - atv.getValorUnit());
                        }
                        modatv.setDataModificacao(data);
                        modatv.setValTotalAtividade(modatv.getValTotalEquipamento() + modatv.getValTotalTarefa());
                        modatv.setAtualizacao(true);
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
        jTabTarefas.getColumnModel().getColumn(0).setMinWidth(30);
        jTabTarefas.getColumnModel().getColumn(0).setPreferredWidth(30);
        jTabTarefas.getColumnModel().getColumn(1).setMinWidth(200);
        jTabTarefas.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTabTarefas.getColumnModel().getColumn(2).setMinWidth(150);
        jTabTarefas.getColumnModel().getColumn(2).setPreferredWidth(150);
        jTabTarefas.getColumnModel().getColumn(3).setMinWidth(60);
        jTabTarefas.getColumnModel().getColumn(3).setPreferredWidth(60);
        jTabTarefas.getColumnModel().getColumn(4).setMinWidth(60);
        jTabTarefas.getColumnModel().getColumn(4).setPreferredWidth(60);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
    }

    // método para dar zoom no campo tarefa
    private void zoomTarefa() {
        PesquisarTarefas zoom = new PesquisarTarefas(new JFrame(), true, "P");
        zoom.setVisible(true);
        jTexCodigoTarefa.setText(zoom.getSelec1().trim());
        jTexNomeTarefa.setText(zoom.getSelec2().trim());
    }

    //método para dar zoom no campo especialidade
    private void zoomEspecialidade() {
        PesquisarEspecialidade zoom = new PesquisarEspecialidade(new JFrame(), true, "P");
        zoom.setVisible(true);
        jTexCodigoEspecialidade.setText(zoom.getSelec1().trim());
        jTexNomeEspecialidade.setText(zoom.getSelec2().trim());
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
        jLabTarefa = new javax.swing.JLabel();
        jLabEspecialidade = new javax.swing.JLabel();
        jLabValor = new javax.swing.JLabel();
        jTexCodigoTarefa = new javax.swing.JTextField();
        jTexCodigoEspecialidade = new javax.swing.JTextField();
        jForValorTarefa = jForValorTarefa = new FormatarValor((FormatarValor.MOEDA));
        jTexNomeTarefa = new javax.swing.JTextField();
        jTexNomeEspecialidade = new javax.swing.JTextField();
        jComSituacao = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
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
        jTabTarefas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manter Tarefa Atividade");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setModal(true);

        jPanTarefasAtividade.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Incluir Tarefas x Atividades", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabTarefa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTarefa.setText("Tarefa:");

        jLabEspecialidade.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabEspecialidade.setText("Especialidade:");

        jLabValor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValor.setText("Valor:");

        jTexCodigoTarefa.setEnabled(false);
        jTexCodigoTarefa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexCodigoTarefaKeyReleased(evt);
            }
        });

        jTexCodigoEspecialidade.setEnabled(false);
        jTexCodigoEspecialidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexCodigoEspecialidadeKeyReleased(evt);
            }
        });

        jForValorTarefa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForValorTarefa.setEnabled(false);

        jTexNomeTarefa.setEditable(false);
        jTexNomeTarefa.setEnabled(false);

        jTexNomeEspecialidade.setEditable(false);
        jTexNomeEspecialidade.setEnabled(false);

        jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ativo", "Inativo" }));
        jComSituacao.setEnabled(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Situação:");

        javax.swing.GroupLayout jPanTarefasAtividadeLayout = new javax.swing.GroupLayout(jPanTarefasAtividade);
        jPanTarefasAtividade.setLayout(jPanTarefasAtividadeLayout);
        jPanTarefasAtividadeLayout.setHorizontalGroup(
            jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTarefasAtividadeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabValor, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabEspecialidade, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabTarefa, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanTarefasAtividadeLayout.createSequentialGroup()
                        .addGroup(jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jTexCodigoEspecialidade, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                            .addComponent(jTexCodigoTarefa))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTexNomeTarefa, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTexNomeEspecialidade, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanTarefasAtividadeLayout.createSequentialGroup()
                        .addComponent(jForValorTarefa, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComSituacao, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanTarefasAtividadeLayout.setVerticalGroup(
            jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTarefasAtividadeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabTarefa)
                    .addComponent(jTexCodigoTarefa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeTarefa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabEspecialidade)
                    .addComponent(jTexCodigoEspecialidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeEspecialidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addGroup(jPanTarefasAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabValor)
                    .addComponent(jForValorTarefa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
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

            jPanTabelaTarefas.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Tarefas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jTabTarefas.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabTarefas.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null}
                },
                new String [] {
                    "Seq.", "Descrição", "Especialidade", "Valor", "Situação"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Double.class, java.lang.Object.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jScrollPane1.setViewportView(jTabTarefas);
            if (jTabTarefas.getColumnModel().getColumnCount() > 0) {
                jTabTarefas.getColumnModel().getColumn(0).setResizable(false);
                jTabTarefas.getColumnModel().getColumn(0).setPreferredWidth(30);
                jTabTarefas.getColumnModel().getColumn(1).setResizable(false);
                jTabTarefas.getColumnModel().getColumn(1).setPreferredWidth(200);
                jTabTarefas.getColumnModel().getColumn(2).setResizable(false);
                jTabTarefas.getColumnModel().getColumn(2).setPreferredWidth(150);
                jTabTarefas.getColumnModel().getColumn(3).setResizable(false);
                jTabTarefas.getColumnModel().getColumn(3).setPreferredWidth(60);
                jTabTarefas.getColumnModel().getColumn(4).setResizable(false);
                jTabTarefas.getColumnModel().getColumn(4).setPreferredWidth(60);
            }

            javax.swing.GroupLayout jPanTabelaTarefasLayout = new javax.swing.GroupLayout(jPanTabelaTarefas);
            jPanTabelaTarefas.setLayout(jPanTabelaTarefasLayout);
            jPanTabelaTarefasLayout.setHorizontalGroup(
                jPanTabelaTarefasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
            );
            jPanTabelaTarefasLayout.setVerticalGroup(
                jPanTabelaTarefasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
                .addComponent(jPanTarefasAtividade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanRodape, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanTabelaTarefas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
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
        jTexCodigoTarefa.requestFocus();
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
        if (!jTexCodigoTarefa.getText().isEmpty()) {
            try {
                DataSistema dat = new DataSistema();
                String data = null;
                TarefasAtividade at = new TarefasAtividade();
                at.setCdAtividade(cdAtividade);
                at.setCdTarefa(jTexCodigoTarefa.getText().toUpperCase());
                at.setSequencia(modta.getSequencia().trim());
                TarefasAtividadesDAO atDAO = new TarefasAtividadesDAO(conexao);
                atDAO.excluir(at);
                dat.setData(data);
                data = dat.getData();
                modatv.setDataModificacao(data);
                modatv.setValTotalTarefa(modatv.getValTotalTarefa() - oldValorUnit);
                modatv.setValTotalAtividade(modatv.getValTotalEquipamento() + modatv.getValTotalTarefa());
                modatv.setAtualizacao(true);
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
        sql = "SELECT * FROM GSMTAREFAATIVIDADE WHERE CD_ATIVIDADE = " + cdAtividade;
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

    private void jTexCodigoTarefaKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCodigoTarefaKeyReleased
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomTarefa();
        }
    }//GEN-LAST:event_jTexCodigoTarefaKeyReleased

    private void jTexCodigoEspecialidadeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCodigoEspecialidadeKeyReleased
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomEspecialidade();
        }
    }//GEN-LAST:event_jTexCodigoEspecialidadeKeyReleased

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
            java.util.logging.Logger.getLogger(ManterTarefaAtividade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterTarefaAtividade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterTarefaAtividade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterTarefaAtividade.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ManterTarefaAtividade dialog = new ManterTarefaAtividade(new javax.swing.JFrame(), true, modatv, sequencia, su, conexao);
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
    private javax.swing.JFormattedTextField jForValorTarefa;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabEspecialidade;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabTarefa;
    private javax.swing.JLabel jLabValor;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanRodape;
    private javax.swing.JPanel jPanTabelaTarefas;
    private javax.swing.JPanel jPanTarefasAtividade;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabTarefas;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexCodigoEspecialidade;
    private javax.swing.JTextField jTexCodigoTarefa;
    private javax.swing.JTextField jTexNomeEspecialidade;
    private javax.swing.JTextField jTexNomeTarefa;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
