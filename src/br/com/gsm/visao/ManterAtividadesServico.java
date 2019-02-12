/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa: GSMCA0071
 */
package br.com.gsm.visao;

// Objetos do registro pais
import br.com.gsm.modelo.AtividadesServico;
import br.com.gsm.dao.AtividadesServicoDAO;
import br.com.gsm.controle.CAtividadesServico;

// Objetos do registro filho
import br.com.gsm.modelo.Servicos;

// Objetos de ambiente
import java.sql.Connection;
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.DefineCampoInteiro;
import br.com.modelo.FormatarValor;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;

// Objetos de api
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
 * @version 0.01_beta0917 created on 30/10/2017
 */
public class ManterAtividadesServico extends javax.swing.JDialog {

    private AtividadesServico regCorr;
    private List< AtividadesServico> resultado;
    private CAtividadesServico catsv;
    private static Connection conexao;
    private static AtividadesServico modatsv;
    private AtividadesServicoDAO atsvdao;
    private static Servicos modsrv;
    private VerificarTecla vt;
    private int numReg = 0;
    private int idxCorr = 0;
    private String sql;
    private String sqlatsv;
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
    public ManterAtividadesServico(java.awt.Frame parent, boolean modal, Servicos modsrv, int sequencia,
            SessaoUsuario su, Connection conexao) {
        super(parent, modal);
        this.modsrv = modsrv;
        this.su = su;
        this.conexao = conexao;
        this.cdServico = modsrv.getCdServico();
        this.sequencia = sequencia;
        formato = NumberFormat.getInstance();
        formato.setMinimumFractionDigits(2);
        catsv = new CAtividadesServico(conexao);
        modatsv = new AtividadesServico();
        try {
            atsvdao = new AtividadesServicoDAO(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(ManterAtividadesServico.class.getName()).log(Level.SEVERE, null, ex);
        }
        sql = "SELECT * FROM GSMATIVIDADESSERVICO WHERE CD_SERVICO = " + cdServico;
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
        jTexCdAtividade.setDocument(new DefineCampoInteiro());
        jForValorAtividade.setDocument(new DefineCampoDecimal());
    }

    // método para limpar tela
    private void limparTela() {
        jTexCdAtividade.setText("");
        jTexNomeAtividade.setText("");
        jForValorAtividade.setText("0.00");
        jComSituacao.setSelectedIndex(0);
        jTexCadastradoPor.setText("");
        jForDataCadastro.setText("");
        jForDataModificacao.setText("");
        jTexRegAtual.setText("");
        jTexRegTotal.setText("");
    }

    //Metodo para buscar todos
    public void buscarTodos() {
        sqlatsv = "select a.sequencia as 'Seq.', "
                + "b.nome_atividade as Descrição, "
                + "format(a.valor_atividade,2,'de_DE')as Valor, "
                + "a.situacao as Situação "
                + "from gsmatividadesservico as a "
                + "left outer join gsmatividades as b on a.cd_atividade = b.cd_atividade "
                + "where a.cd_servico = " + modatsv.getCdServico();
        try {
            atsvdao.setQuery(sqlatsv);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
        jTabAtividades.setModel(atsvdao);
        ajustarTabela();
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {
        try {
            numReg = catsv.pesquisar(sql);
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
        catsv.mostrarPesquisa(modatsv, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jTexCdAtividade.setText(modatsv.getCdAtividade().trim());
        jTexNomeAtividade.setText(modatsv.getNomeAtividade().trim());
        jForValorAtividade.setText(String.valueOf(modatsv.getValorAtividade()));
        oldValorUnit = modatsv.getValorAtividade();
        jComSituacao.setSelectedIndex(Integer.parseInt(modatsv.getSituacao()));
        oldSituacao = modatsv.getSituacao();
        jTexCadastradoPor.setText(modatsv.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modatsv.getDataCadastro())));
        changeValorUnit = false;
        if (modatsv.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(modatsv.getDataModificacao())));
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
        jTexCdAtividade.setEnabled(false);
        jTexCdAtividade.setEditable(false);
        jForValorAtividade.setEditable(false);
        jForValorAtividade.setEnabled(false);
        jComSituacao.setEditable(false);
        jComSituacao.setEnabled(false);
    }

    // método para liberar campos
    private void liberarCampos() {
        jForValorAtividade.setEditable(true);
        jForValorAtividade.setEnabled(true);
        jComSituacao.setEditable(true);
        jComSituacao.setEnabled(true);
    }

    // método para criar novo registro
    private void novoRegistro() {
        limparTela();
        liberarCampos();
        jTexCdAtividade.setEditable(true);
        jTexCdAtividade.setEnabled(true);
        jTexCdAtividade.requestFocus();
        jButSalvar.setEnabled(true);
    }

    private void esvaziarTabelas() {
        jTabAtividades.setModel(new JTable().getModel());
    }

    // método para salvar registro
    private void salvarRegistro() {
        if (jTexCdAtividade.getText().isEmpty() || jComSituacao.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Os campos Atividade e Situação são obrigatórios.");
        } else {
            DataSistema dat = new DataSistema();
            AtividadesServico atsv = new AtividadesServico();
            String data = null;
            atsv.setCdServico(cdServico);
            atsv.setCdAtividade(jTexCdAtividade.getText().trim());
            atsv.setNomeAtividade(jTexNomeAtividade.getText().trim().toUpperCase());
            try {
                atsv.setValorAtividade(formato.parse(jForValorAtividade.getText()).doubleValue());
                atsv.setValTotalAtividade(atsv.getValTotalAtividade() + atsv.getValorAtividade());
            } catch (ParseException ex) {
                Logger.getLogger(ManterAtividades.class.getName()).log(Level.SEVERE, null, ex);
            }
            dat.setData(data);
            data = dat.getData();
            atsv.setDataCadastro(data);
            atsv.setUsuarioCadastro(su.getUsuarioConectado());
            atsv.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1));
            AtividadesServicoDAO atsvdao = null;
            CAtividadesServico catsv = new CAtividadesServico(conexao);
            try {
                atsvdao = new AtividadesServicoDAO(conexao);
                if ("N".equals(oper)) {
                    sequencia += 1;
                    atsv.setSequencia(String.valueOf(sequencia));
                    sql = "SELECT * FROM GSMATIVIDADESSERVICO WHERE CD_SERVICO = " + atsv.getCdServico().trim();
                    atsvdao.adicionar(atsv);
                    modsrv.setAtualizacao(true);
                } else {
                    atsv.setSequencia(modatsv.getSequencia());
                    sql = "SELECT * FROM GSMATIVIDADESSERVICO WHERE CD_SERVICO = " + modatsv.getCdServico();
                    atsv.setCdServico(modatsv.getCdServico());
                    atsv.setDataModificacao(data);
                    atsvdao.atualizar(atsv);
                    if (oldValorUnit != atsv.getValorAtividade()) {
                        changeValorUnit = true;
                        if (oldValorUnit > atsv.getValorAtividade() && "A".equals(atsv.getSituacao()) && !"2".equals(oldSituacao)) {
                            modatsv.setValTotalAtividade(modatsv.getValTotalAtividade() - (oldValorUnit - atsv.getValorAtividade()));
                        } else if (oldValorUnit < atsv.getValorAtividade() && "A".equals(atsv.getSituacao())) {
                            modatsv.setValTotalAtividade(modatsv.getValTotalAtividade() + (atsv.getValorAtividade() - oldValorUnit));
                        }
                        modatsv.setDataModificacao(data);
                        modsrv.setAtualizacao(true);
                    }
                    if (oldSituacao != String.valueOf(jComSituacao.getSelectedIndex()) && !changeValorUnit) {
                        if ("A".equals(atsv.getSituacao())) {
                            modatsv.setValTotalAtividade(modatsv.getValTotalAtividade() + atsv.getValorAtividade());
                        } else if ("I".equals(atsv.getSituacao())) {
                            modatsv.setValTotalAtividade(modatsv.getValTotalAtividade() - atsv.getValorAtividade());
                        }
                        modatsv.setDataModificacao(data);
                        modsrv.setAtualizacao(true);
                    }
                }
            } catch (SQLException sqlex) {
                JOptionPane.showMessageDialog(null, "Erro criar a Atividade no banco e dados!\nErr: " + sqlex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral para criar Atividade!\nErr: " + ex);
            }

            limparTela();
            bloquearCampos();
            pesquisar();
            jButSalvar.setEnabled(false);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        jTabAtividades.getColumnModel().getColumn(0).setMinWidth(10);
        jTabAtividades.getColumnModel().getColumn(0).setPreferredWidth(10);
        jTabAtividades.getColumnModel().getColumn(1).setMinWidth(200);
        jTabAtividades.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTabAtividades.getColumnModel().getColumn(2).setMinWidth(60);
        jTabAtividades.getColumnModel().getColumn(2).setPreferredWidth(60);
        jTabAtividades.getColumnModel().getColumn(3).setMinWidth(10);
        jTabAtividades.getColumnModel().getColumn(3).setPreferredWidth(10);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
    }

    // método para dar zoom no campo tarefa
    private void zoomAtividade() {
        PesquisarAtividades zoom = new PesquisarAtividades(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdAtividade.setText(zoom.getSelecao1());
        jTexNomeAtividade.setText(zoom.getSelecao2());
        jForValorAtividade.setText(String.valueOf(zoom.getSelecao3()));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanAtividadesServico = new javax.swing.JPanel();
        jLabAtividade = new javax.swing.JLabel();
        jLabValorAtividade = new javax.swing.JLabel();
        jTexCdAtividade = new javax.swing.JTextField();
        jForValorAtividade = jForValorAtividade = new FormatarValor((FormatarValor.MOEDA));
        jTexNomeAtividade = new javax.swing.JTextField();
        jComSituacao = new javax.swing.JComboBox<>();
        jLabSituacao = new javax.swing.JLabel();
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
        jPanTabelaAtividades = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabAtividades = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manter Tarefa Atividade");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setModal(true);

        jPanAtividadesServico.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Incluir Atividades x Servico", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabAtividade.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabAtividade.setText("Atividade:");

        jLabValorAtividade.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValorAtividade.setText("Valor:");

        jTexCdAtividade.setEnabled(false);
        jTexCdAtividade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexCdAtividadeKeyReleased(evt);
            }
        });

        jForValorAtividade.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForValorAtividade.setEnabled(false);

        jTexNomeAtividade.setEditable(false);
        jTexNomeAtividade.setEnabled(false);

        jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ativo", "Inativo" }));
        jComSituacao.setEnabled(false);

        jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabSituacao.setText("Situação:");

        javax.swing.GroupLayout jPanAtividadesServicoLayout = new javax.swing.GroupLayout(jPanAtividadesServico);
        jPanAtividadesServico.setLayout(jPanAtividadesServicoLayout);
        jPanAtividadesServicoLayout.setHorizontalGroup(
            jPanAtividadesServicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanAtividadesServicoLayout.createSequentialGroup()
                .addGap(49, 49, 49)
                .addGroup(jPanAtividadesServicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabValorAtividade, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabAtividade, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanAtividadesServicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanAtividadesServicoLayout.createSequentialGroup()
                        .addComponent(jTexCdAtividade, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexNomeAtividade, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanAtividadesServicoLayout.createSequentialGroup()
                        .addComponent(jForValorAtividade, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(81, 81, 81)
                        .addComponent(jLabSituacao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComSituacao, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(246, Short.MAX_VALUE))
        );
        jPanAtividadesServicoLayout.setVerticalGroup(
            jPanAtividadesServicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanAtividadesServicoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanAtividadesServicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabAtividade)
                    .addComponent(jTexCdAtividade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeAtividade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanAtividadesServicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabValorAtividade)
                    .addComponent(jForValorAtividade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabSituacao))
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

            jPanTabelaAtividades.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Atividades", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jTabAtividades.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabAtividades.setModel(new javax.swing.table.DefaultTableModel(
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
                    "Seq.", "Descrição", "Valor", "Situação"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jScrollPane1.setViewportView(jTabAtividades);
            if (jTabAtividades.getColumnModel().getColumnCount() > 0) {
                jTabAtividades.getColumnModel().getColumn(0).setResizable(false);
                jTabAtividades.getColumnModel().getColumn(0).setPreferredWidth(10);
                jTabAtividades.getColumnModel().getColumn(1).setResizable(false);
                jTabAtividades.getColumnModel().getColumn(1).setPreferredWidth(200);
                jTabAtividades.getColumnModel().getColumn(2).setResizable(false);
                jTabAtividades.getColumnModel().getColumn(2).setPreferredWidth(60);
                jTabAtividades.getColumnModel().getColumn(3).setResizable(false);
                jTabAtividades.getColumnModel().getColumn(3).setPreferredWidth(10);
            }

            javax.swing.GroupLayout jPanTabelaAtividadesLayout = new javax.swing.GroupLayout(jPanTabelaAtividades);
            jPanTabelaAtividades.setLayout(jPanTabelaAtividadesLayout);
            jPanTabelaAtividadesLayout.setHorizontalGroup(
                jPanTabelaAtividadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
            );
            jPanTabelaAtividadesLayout.setVerticalGroup(
                jPanTabelaAtividadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, 734, Short.MAX_VALUE)
                .addComponent(jPanAtividadesServico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanRodape, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanTabelaAtividades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanAtividadesServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanTabelaAtividades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
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
        jTexCdAtividade.requestFocus();
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
        if (!jTexCdAtividade.getText().isEmpty()) {
            try {
                DataSistema dat = new DataSistema();
                String data = null;
                AtividadesServico as = new AtividadesServico();
                as.setCdServico(cdServico);
                as.setCdAtividade(jTexCdAtividade.getText().toUpperCase());
                as.setSequencia(modatsv.getSequencia().trim());
                AtividadesServicoDAO asDAO = new AtividadesServicoDAO(conexao);
                asDAO.excluir(as);
                dat.setData(data);
                data = dat.getData();
                modatsv.setDataModificacao(data);
                modatsv.setValTotalAtividade(modatsv.getValTotalAtividade() - oldValorUnit);
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
        sql = "SELECT * FROM GSMATIVIDADESSERVICO WHERE CD_SERVICO = " + cdServico;
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

    private void jTexCdAtividadeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdAtividadeKeyReleased
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomAtividade();
        }
    }//GEN-LAST:event_jTexCdAtividadeKeyReleased

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
            java.util.logging.Logger.getLogger(ManterAtividadesServico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterAtividadesServico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterAtividadesServico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterAtividadesServico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ManterAtividadesServico dialog = new ManterAtividadesServico(new javax.swing.JFrame(), true, modatsv, sequencia, su, conexao);
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
    private javax.swing.JFormattedTextField jForValorAtividade;
    private javax.swing.JLabel jLabAtividade;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabValorAtividade;
    private javax.swing.JPanel jPanAtividadesServico;
    private javax.swing.JPanel jPanRodape;
    private javax.swing.JPanel jPanTabelaAtividades;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabAtividades;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexCdAtividade;
    private javax.swing.JTextField jTexNomeAtividade;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
