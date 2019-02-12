/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GFCCA0070
 */
package br.com.gfc.visao;

// objetos do registro pai
import br.com.gfc.modelo.TipoLancamento;
import br.com.gfc.dao.TipoLancamentoDAO;
import br.com.gfc.controle.CTipoLancamento;

// objetos do registro filho
import br.com.gfc.modelo.TipoLancamentoMovimento;
import br.com.gfc.dao.TipoLancamentoMovimentoDAO;
import br.com.gfc.controle.CTipoLancamentoMovimento;

//objetos para intância de parâmetros de ambiente
import br.com.modelo.DataSistema;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.HoraSistema;
import br.com.modelo.VerificarTecla;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0919 created on 28/02/2018
 */
public class ManterTipoLancamento extends javax.swing.JFrame {

    // variáveis de instância de conexão do usuário
    private static Connection conexao;
    private static SessaoUsuario su;

    // variáveis de instância de objetos do registro pai
    private TipoLancamento regCorr;
    private List< TipoLancamento> resultado;
    private CTipoLancamento ctl;
    private TipoLancamento modtl;
    private int numReg;
    private int idxCorr;
    private String sql;
    private String oper;

    // variáveis de instância de objetos do registro filho
    private TipoLancamentoMovimento modtlm;
    private CTipoLancamentoMovimento ctlm;
    private TipoLancamentoMovimentoDAO tlmDAO;
    private TipoLancamentoMovimento tlm;
    private DefaultTableModel tipoMovimentos;
    private int linhaTipoMovim;
    private int numRegTipoMovimento;
    private int idxCorTipoMovimento;
    private String sqlTipoMovimento;
    private String operTlm;
    private boolean esvaziarTabela;

    // variáveis de parâmetros de ambiente
    private VerificarTecla vt;
    private NumberFormat formato;
    private NumberFormat ftq;
    private NumberFormat ftp;
    private NumberFormat ftv;
    private DataSistema dat;

    /**
     * Creates new form ManterProdutos
     */
    public ManterTipoLancamento(SessaoUsuario su, Connection conexao) {
        this.su = su;
        this.conexao = conexao;
        oper = "N";
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        monitoraLinhaTipoMovimento();
        this.dispose();
    }

    // construturo padrão
    public ManterTipoLancamento() {

    }

    /**
     * Método privado para setar os objetos do registro filho
     */
    private void setaTipoMovimento() {
        modtlm = new TipoLancamentoMovimento();
        monitoraLinhaTipoMovimento();
    }

    // método para limpar tela
    private void limparTela() {
        jForCdTipoLancamento.setText("");
        jTexNomeTipoLancamento.setText("");
        jTexPrefixo.setText("");
        jTexCdPortador.setText("");
        jTexNomePortador.setText("");
        jComSituacao.setSelectedIndex(0);
        jTexCadPor.setText("");
        jForDataCad.setText("");
        jForHoraCad.setText("");
        jTexModifPor.setText("");
        jForDataModif.setText("");
        jForHoraModif.setText("");
        jTexRegAtual.setText("");
        jTexRegTotal.setText("");
        idxCorr = 0;
        numReg = 0;
        resultado = null;
        regCorr = null;
        liberarCampos();
        jForCdTipoLancamento.setEditable(true);
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {
        ctl = new CTipoLancamento(conexao);
        modtl = new TipoLancamento();
        try {
            numReg = ctl.pesquisar(sql);
            idxCorr = +1;
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Argumento inválido na busca\nErr: " + iae.getCause());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Pesquisar os Registro\nErr: " + ex);
        }
        if (numReg > 0) {
            upRegistros();
            pesquisarTipoMovimento();
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum registro encontrado!");
        }
    }

    // método para pesquisar o tipoMovimento
    private void pesquisarTipoMovimento() {
        sqlTipoMovimento = "select * from gfctipolancmovi as tplm "
                + "where tplm.cd_tipolancamento = '" + modtl.getCdTipoLancamento()
                + "' order by tplm.cd_tipomovimento";
        ctlm = new CTipoLancamentoMovimento(conexao);
        tipoMovimentos = new DefaultTableModel();
        try {
            numRegTipoMovimento = ctlm.pesquisar(sqlTipoMovimento);
            idxCorTipoMovimento = +1;
        } catch (SQLException ex) {
            Logger.getLogger(ManterTipoLancamento.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (numRegTipoMovimento > 0) {
            jTabTipoMovimentos.setModel(ctlm.carregarMovimentos());
            ajustarTabelaTipoMovimento();
        } else if (esvaziarTabela) {
            esvaziarTabela = false;
        }
    }

    private void upRegistros() {
        jTexRegTotal.setText(Integer.toString(numReg));
        jTexRegAtual.setText(Integer.toString(idxCorr));
        ctl.mostrarPesquisa(modtl, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jForCdTipoLancamento.setText(modtl.getCdTipoLancamento().trim());
        jTexNomeTipoLancamento.setText(modtl.getNomeTipoLancamento().trim());
        jTexPrefixo.setText(String.valueOf(modtl.getPrefixo()));
        jTexCdPortador.setText(modtl.getCdPortador());
        jTexNomePortador.setText(modtl.getNomePortador());
        jTexCadPor.setText(modtl.getUsuarioCadastro());
        jForDataCad.setText(dat.getDataConv(Date.valueOf(modtl.getDataCadastro())));
        jForHoraCad.setText(modtl.getHoraCadastro());
        jTexModifPor.setText(modtl.getUsuarioModificacao());
        if (modtl.getDataModificacao() != null) {
            jForDataModif.setText(dat.getDataConv(Date.valueOf(modtl.getDataModificacao())));
            jForHoraModif.setText(modtl.getHoraModificacao());
        }
        jComSituacao.setSelectedIndex(Integer.parseInt(String.valueOf(modtl.getSituacao())));

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
        esvaziarTabelaTipoMovimento();
        pesquisarTipoMovimento();
    }

    //Método para ajustar tabela tipoMovimento
    private void ajustarTabelaTipoMovimento() {
        jTabTipoMovimentos.getColumnModel().getColumn(0).setWidth(8);
        jTabTipoMovimentos.getColumnModel().getColumn(0).setPreferredWidth(8);
        jTabTipoMovimentos.getColumnModel().getColumn(1).setWidth(100);
        jTabTipoMovimentos.getColumnModel().getColumn(1).setPreferredWidth(100);
    }

    //Bloquear os campos da tela
    private void bloquearCampos() {
        jForCdTipoLancamento.setEditable(false);
        jTexNomeTipoLancamento.setEditable(false);
        jTexPrefixo.setEditable(false);
        jTexCdPortador.setEditable(false);
        jTexNomePortador.setEditable(false);
        jComSituacao.setEditable(false);
    }

    //Loberar os campos da tela para atualização
    private void liberarCampos() {
        jTexNomeTipoLancamento.setEditable(true);
        jTexPrefixo.setEditable(true);
        jTexCdPortador.setEditable(true);
        jComSituacao.setEditable(true);
    }

    private void novoRegistro() {
        limparTela();
        liberarCampos();
        jForCdTipoLancamento.setEditable(true);
        jForCdTipoLancamento.requestFocus();
    }

    /**
     * Método para monitorar as linhas do registro filho
     */
    private void monitoraLinhaTipoMovimento() {
        VerificarTecla vt = new VerificarTecla();
        jTabTipoMovimentos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                linhaTipoMovim = jTabTipoMovimentos.getSelectedRow();
            }
        });

        // capturando a tecla digitada
        jTabTipoMovimentos.addKeyListener(new KeyListener() {
            boolean inclusao = false;

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                String tecla = vt.VerificarTecla(e).toUpperCase();
                if ("ABAIXO".equals(tecla) && !inclusao && "A".equals(oper)) {
                    if (jTabTipoMovimentos.getSelectedRow() == jTabTipoMovimentos.getRowCount() - 1) {
                        modtlm = new TipoLancamentoMovimento();
                        inclusao = true;
                        jTabTipoMovimentos.setModel(ctlm.adicionarLinha());
                        linhaTipoMovim = jTabTipoMovimentos.getSelectedRow() + 1;
                        ajustarTabelaTipoMovimento();
                    }
                }

                if ("F5".equals(tecla) && inclusao) {
                    if (jTabTipoMovimentos.getSelectedColumn() == 0) {
                        PesquisarTipoMovimento zoom = new PesquisarTipoMovimento(new JFrame(), true, "P", conexao);
                        zoom.setVisible(true);
                        modtlm.setCdTipoMovimento(zoom.getCdTipoMovimento());
                        modtlm.setNomeTipoMovimento(zoom.getNomeTipoMovimento());
                        upValue();
                        ajustarTabelaTipoMovimento();
                    }
                }
                if ("ESCAPE".equals(tecla) && inclusao) {
                    jTabTipoMovimentos.setModel(ctlm.excluirLinha(jTabTipoMovimentos.getSelectedRow()));
                    jTabTipoMovimentos.requestFocus();
                    inclusao = false;
                    ajustarTabelaTipoMovimento();
                }
                if ("ENTER".equals(tecla) && (inclusao || "A".equals(oper))) {
                    if (inclusao) {
                        inclusao = false;
                        operTlm = "N";
                    } else {
                        operTlm = "A";
                    }
                    salvarLinha();
                }
                if ("GUIA".equals(tecla) && "A".equals(oper)) {
                    operTlm = "A";
                    dowValue();
                    upValue();
                }
                if ("ACIMA".equals(tecla) && inclusao) {
                    inclusao = false;
                    operTlm = "N";
                    salvarLinha();
                }
                if ("EXCLUIR".equals(tecla) && "A".equals(oper) && !inclusao) {
                    if (JOptionPane.showConfirmDialog(null, "Confirma exclusão do Movimento?") == JOptionPane.OK_OPTION) {
                        int numReg = 0;
                        modtlm = new TipoLancamentoMovimento();
                        dowValue();
                        CTipoLancamentoMovimento ctlm = new CTipoLancamentoMovimento(conexao);
                        String sql = "select gfctipolancmov where cd_tipolancamento = '" + modtl.getCdTipoLancamento()
                                + "' and cd_tipomovimento = '" + modtlm.getCdTipoMovimento()
                                + "'";
                        try {
                            numReg = ctlm.pesquisar(sql);
                        } catch (SQLException ex) {
                            Logger.getLogger(ManterTipoLancamento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (numReg > 0) {
                            ctlm.mostrarPesquisa(modtlm, 0);
                        }
                        try {
                            tlmDAO = new TipoLancamentoMovimentoDAO(conexao);
                        } catch (SQLException ex) {
                            Logger.getLogger(ManterTipoLancamento.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        tlmDAO.excluir(modtlm);
                        esvaziarTabela = true;
                        pesquisarTipoMovimento();
                    }
                }
            }

            private void upValue() {
                jTabTipoMovimentos.setModel(ctlm.upNovaLinha(modtlm, linhaTipoMovim, 0, 1));
            }

            private void dowValue() {
                modtlm.setCdTipoLancamento(modtl.getCdTipoLancamento());
                modtlm.setCdTipoMovimento(String.format("%s", jTabTipoMovimentos.getValueAt(linhaTipoMovim, 0)));
                modtlm.setNomeTipoMovimento(String.format("%s", jTabTipoMovimentos.getValueAt(linhaTipoMovim, 1)));
            }

            private void salvarLinha() {
                modtlm.setCdTipoLancamento(modtl.getCdTipoLancamento());
                salvarTipoMovimento();
                pesquisarTipoMovimento();
            }

            @Override
            public void keyReleased(KeyEvent e
            ) {
            }
        });
    }

    // Método para salvar o tipo de movimento
    private void salvarTipoMovimento() {
        if (modtlm.getCdTipoMovimento() != null) {
            dat = new DataSistema();
            tlm = new TipoLancamentoMovimento();
            tlm.setCdTipoMovimento(modtlm.getCdTipoMovimento());
            tlm.setCdTipoLancamento(modtlm.getCdTipoLancamento());
            tlm.setSituacao("A");
            gravarTipoMovimentoBanco();
        }
    }

    // método para gravar o tipo de movimento no banco
    private void gravarTipoMovimentoBanco() {
        String data = null;
        dat.setData(data);
        data = dat.getData();
        HoraSistema hs = new HoraSistema();
        try {
            tlmDAO = new TipoLancamentoMovimentoDAO(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(ManterTipoLancamento.class.getName()).log(Level.SEVERE, null, ex);
        }
        if ("N".equals(operTlm)) {
            tlm.setUsuarioCadastro(su.getUsuarioConectado());
            tlm.setDataCadastro(data);
            tlm.setHoraCadastro(hs.getHora());
            tlmDAO.adicionar(tlm);
        } else {
            tlm.setUsuarioModificacao(su.getUsuarioConectado());
            tlm.setDataModificacao(data);
            tlm.setHoraModificacao(hs.getHora());
            tlmDAO.atualizar(tlm);
        }
        operTlm = "";
    }

    /**
     * Método para pesquisar um portador
     */
    private void zoomPortador() {
        PesquisarPortadores zoom = new PesquisarPortadores(new JFrame(), rootPaneCheckingEnabled, "P", conexao);
        zoom.setVisible(rootPaneCheckingEnabled);
        jTexCdPortador.setText(zoom.getCdPortador());
        jTexNomePortador.setText(zoom.getNomePortador());
    }

    // méto para limpar a tabela de itens
    private void esvaziarTabelaTipoMovimento() {
        ctlm = new CTipoLancamentoMovimento(conexao);
        jTabTipoMovimentos.setModel(new JTable().getModel());
        jTabTipoMovimentos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabTipoMovimentos
                .setModel(new javax.swing.table.DefaultTableModel(
                        new Object[][]{
                            {null, null}
                        },
                        new String[]{
                            "Cód.", "Descrição"
                        }
                ) {
                    Class[] types = new Class[]{
                        java.lang.String.class,
                        java.lang.String.class,};
                    boolean[] canEdit = new boolean[]{
                        false, false
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types[columnIndex];
                    }

                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit[columnIndex];
                    }
                });
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
        jButNovo = new javax.swing.JButton();
        jButEditar = new javax.swing.JButton();
        jButSalvar = new javax.swing.JButton();
        jButCancelar = new javax.swing.JButton();
        jButExcluir = new javax.swing.JButton();
        jButPesquisar = new javax.swing.JButton();
        jButAnterior = new javax.swing.JButton();
        jButProximo = new javax.swing.JButton();
        jButSair = new javax.swing.JButton();
        jPanSecundario = new javax.swing.JPanel();
        jLabTipoLancamento = new javax.swing.JLabel();
        jLabSituacao = new javax.swing.JLabel();
        jForCdTipoLancamento = new javax.swing.JFormattedTextField();
        jTexNomeTipoLancamento = new javax.swing.JTextField();
        jComSituacao = new javax.swing.JComboBox<>();
        jLabPrefixo = new javax.swing.JLabel();
        jTexPrefixo = new javax.swing.JTextField();
        jLabCdPortador = new javax.swing.JLabel();
        jTexCdPortador = new javax.swing.JTextField();
        jTexNomePortador = new javax.swing.JTextField();
        jPanRodape = new javax.swing.JPanel();
        jForDataCad = new javax.swing.JFormattedTextField();
        jForDataModif = new javax.swing.JFormattedTextField();
        jLabCadPor = new javax.swing.JLabel();
        jTexCadPor = new javax.swing.JTextField();
        jTexModifPor = new javax.swing.JTextField();
        jLabModifPor = new javax.swing.JLabel();
        jTexRegAtual = new javax.swing.JTextField();
        jLabReg = new javax.swing.JLabel();
        jTexRegTotal = new javax.swing.JTextField();
        jForHoraCad = new javax.swing.JFormattedTextField();
        jForHoraModif = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabTipoMovimentos = new javax.swing.JTable();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Tipos Lançamentos");

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
        jButAnterior.setEnabled(false);
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
        jButProximo.setText("Próximo");
        jButProximo.setEnabled(false);
        jButProximo.setFocusable(false);
        jButProximo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButProximo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButProximo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButProximoActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButProximo);

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

        jLabTipoLancamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTipoLancamento.setText("Tipo Lanc.:");

        jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabSituacao.setText("Situação:");

        try {
            jForCdTipoLancamento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("***")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jTexNomeTipoLancamento.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTexNomeTipoLancamentoActionPerformed(evt);
            }
        });

        jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ativo", "Inativo" }));

        jLabPrefixo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPrefixo.setText("Prefixo:");

        jLabCdPortador.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdPortador.setText("Portador:");

        jTexCdPortador.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdPortadorKeyPressed(evt);
            }
        });

        jPanRodape.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jForDataCad.setEditable(false);
        jForDataCad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataCad.setEnabled(false);

        jForDataModif.setEditable(false);
        jForDataModif.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataModif.setEnabled(false);

        jLabCadPor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCadPor.setText("Cadastrado:");

        jTexCadPor.setEditable(false);
        jTexCadPor.setEnabled(false);

        jTexModifPor.setEditable(false);
        jTexModifPor.setEnabled(false);

        jLabModifPor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabModifPor.setText("Modificado:");

        jTexRegAtual.setEditable(false);
        jTexRegAtual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexRegAtual.setEnabled(false);

        jLabReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabReg.setText("\\");

            jTexRegTotal.setEditable(false);
            jTexRegTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            jTexRegTotal.setEnabled(false);

            jForHoraCad.setEditable(false);
            try {
                jForHoraCad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForHoraCad.setEnabled(false);

            jForHoraModif.setEditable(false);
            try {
                jForHoraModif.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForHoraModif.setEnabled(false);

            javax.swing.GroupLayout jPanRodapeLayout = new javax.swing.GroupLayout(jPanRodape);
            jPanRodape.setLayout(jPanRodapeLayout);
            jPanRodapeLayout.setHorizontalGroup(
                jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanRodapeLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTexRegAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(3, 3, 3)
                    .addComponent(jLabReg, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(2, 2, 2)
                    .addComponent(jTexRegTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabCadPor)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexCadPor, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataCad, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForHoraCad, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabModifPor)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexModifPor, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataModif, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForHoraModif, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanRodapeLayout.setVerticalGroup(
                jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanRodapeLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTexRegTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabReg)
                            .addComponent(jTexRegAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jForDataCad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabCadPor)
                            .addComponent(jTexCadPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabModifPor)
                            .addComponent(jTexModifPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForDataModif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForHoraCad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForHoraModif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
            );

            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Tipos de Movimentos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jTabTipoMovimentos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabTipoMovimentos.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null}
                },
                new String [] {
                    "Cód.", "Descrição"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            jScrollPane1.setViewportView(jTabTipoMovimentos);
            if (jTabTipoMovimentos.getColumnModel().getColumnCount() > 0) {
                jTabTipoMovimentos.getColumnModel().getColumn(0).setResizable(false);
                jTabTipoMovimentos.getColumnModel().getColumn(0).setPreferredWidth(8);
                jTabTipoMovimentos.getColumnModel().getColumn(1).setResizable(false);
                jTabTipoMovimentos.getColumnModel().getColumn(1).setPreferredWidth(100);
            }

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1)
                    .addContainerGap())
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout jPanSecundarioLayout = new javax.swing.GroupLayout(jPanSecundario);
            jPanSecundario.setLayout(jPanSecundarioLayout);
            jPanSecundarioLayout.setHorizontalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabCdPortador)
                                .addComponent(jLabTipoLancamento)
                                .addComponent(jLabPrefixo))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                    .addComponent(jForCdTipoLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexNomeTipoLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(107, 107, 107)
                                    .addComponent(jLabSituacao)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                    .addComponent(jTexCdPortador, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jTexNomePortador, javax.swing.GroupLayout.PREFERRED_SIZE, 279, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jTexPrefixo, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jPanRodape, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanSecundarioLayout.setVerticalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                            .addGap(12, 12, 12)
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabTipoLancamento)
                                    .addComponent(jForCdTipoLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabSituacao))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jTexNomeTipoLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCdPortador)
                        .addComponent(jTexCdPortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomePortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabPrefixo)
                        .addComponent(jTexPrefixo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jMenuBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jMenu1.setText("Arquivo");

            jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItem1.setText("Novo");
            jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem1ActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItem1);

            jMenuItemSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemSalvar.setText("Salvar");
            jMenuItemSalvar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemSalvarActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItemSalvar);

            jMenuItemSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemSair.setText("Sair");
            jMenuItemSair.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemSairActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItemSair);

            jMenuBar.add(jMenu1);

            jMenu2.setText("Editar");

            jMenuItemEditar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemEditar.setText("Editar");
            jMenuItemEditar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemEditarActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItemEditar);

            jMenuBar.add(jMenu2);

            setJMenuBar(jMenuBar);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanSecundario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanSecundario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(1, 1, 1))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        // TODO add your handling code here:
        if (jForCdTipoLancamento.getText().isEmpty() || jTexNomeTipoLancamento.getText().isEmpty()
                || jComSituacao.getSelectedItem().toString().substring(0, 1) == " ") {
            JOptionPane.showMessageDialog(null, "Os campos Tipo Lancamento, Nome e Situacao precisam ser preenchidos corretamente!");
        } else {
            DataSistema dat = new DataSistema();
            HoraSistema hs = new HoraSistema();
            TipoLancamento tl = new TipoLancamento();
            String data = null;
            tl.setCdTipoLancamento(jForCdTipoLancamento.getText().trim());
            tl.setNomeTipoLancamento(jTexNomeTipoLancamento.getText().trim().toUpperCase());
            tl.setPrefixo(Integer.parseInt(jTexPrefixo.getText()));
            tl.setCdPortador(jTexCdPortador.getText());
            dat.setData(data);
            data = dat.getData();
            tl.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1));
            TipoLancamentoDAO tldao = null;
            sql = "SELECT * FROM GFCTIPOLANCAMENTO WHERE CD_TIPOLANCAMENTO = '" + jForCdTipoLancamento.getText().trim()
                    + "'";
            try {
                tldao = new TipoLancamentoDAO(conexao);
                if ("N".equals(oper)) {
                    tl.setUsuarioCadastro(su.getUsuarioConectado());
                    tl.setDataCadastro(data);
                    tl.setHoraCadastro(hs.getHora());
                    tldao.adicionar(tl);
                } else {
                    tl.setUsuarioModificacao(su.getUsuarioConectado());
                    tl.setDataModificacao(data);
                    tl.setHoraModificacao(hs.getHora());
                    tldao.atualizar(tl);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ManterTipoLancamento.class.getName()).log(Level.SEVERE, null, ex);
            }
            limparTela();
            bloquearCampos();
            pesquisar();
        }
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:
        if (jTexNomeTipoLancamento.getText().isEmpty()) {
            sql = "SELECT * FROM GFCTIPOLANCAMENTO";
        } else {
            sql = "SELECT * FROM GFCTIPOLANCAMENTO WHERE NOME_TIPOLANCAMENTO LIKE '" + jTexNomeTipoLancamento.getText().trim() + "'";
        }
        bloquearCampos();
        pesquisar();

    }//GEN-LAST:event_jButPesquisarActionPerformed

    private void jButProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButProximoActionPerformed
        // TODO add your handling code here:
        idxCorr += 1;
        upRegistros();
    }//GEN-LAST:event_jButProximoActionPerformed

    private void jButAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAnteriorActionPerformed
        // TODO add your handling code here:
        idxCorr -= 1;
        upRegistros();
    }//GEN-LAST:event_jButAnteriorActionPerformed

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
        // TODO add your handling code here:
        limparTela();
        oper = "N";         // se cancelar a ação atual na tela do sistema a operação do sistema será N  de novo Registro
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        // TODO add your handling code here:
        liberarCampos();
        jTexNomeTipoLancamento.requestFocus();
        oper = "A";         // se estiver editando um registro do sistema, a operação do sistema será A de alteração de Registro
    }//GEN-LAST:event_jButEditarActionPerformed

    private void jMenuItemSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jMenuItemSairActionPerformed

    private void jMenuItemSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalvarActionPerformed
        // TODO add your handling code here:
        jButSalvarActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemSalvarActionPerformed

    private void jMenuItemEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEditarActionPerformed
        // TODO add your handling code here:
        jButEditarActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemEditarActionPerformed

    private void jButNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButNovoActionPerformed
        // TODO add your handling code here:
        novoRegistro();
        oper = "N";
    }//GEN-LAST:event_jButNovoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        jButNovoActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        // TODO add your handling code here:
        if (!jForCdTipoLancamento.getText().isEmpty()) {
            try {
                TipoLancamento cc = new TipoLancamento();
                String cdTipoLancamento = jForCdTipoLancamento.getText().trim();
                cc.setCdTipoLancamento(cdTipoLancamento);
                TipoLancamentoDAO ccDAO = new TipoLancamentoDAO(conexao);
                ccDAO.excluir(cc);
                limparTela();
                jButPesquisarActionPerformed(evt);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Não foi possível excluir o registro da tela");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral na exclusão do registro!");
            }
        }
    }//GEN-LAST:event_jButExcluirActionPerformed

    private void jTexNomeTipoLancamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTexNomeTipoLancamentoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTexNomeTipoLancamentoActionPerformed

    private void jTexCdPortadorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdPortadorKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomPortador();
        }
    }//GEN-LAST:event_jTexCdPortadorKeyPressed

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
            java.util.logging.Logger.getLogger(ManterTipoLancamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterTipoLancamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterTipoLancamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterTipoLancamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterTipoLancamento(su, conexao).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButAnterior;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButEditar;
    private javax.swing.JButton jButExcluir;
    private javax.swing.JButton jButNovo;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JButton jButProximo;
    private javax.swing.JButton jButSair;
    private javax.swing.JButton jButSalvar;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JFormattedTextField jForCdTipoLancamento;
    private javax.swing.JFormattedTextField jForDataCad;
    private javax.swing.JFormattedTextField jForDataModif;
    private javax.swing.JFormattedTextField jForHoraCad;
    private javax.swing.JFormattedTextField jForHoraModif;
    private javax.swing.JLabel jLabCadPor;
    private javax.swing.JLabel jLabCdPortador;
    private javax.swing.JLabel jLabModifPor;
    private javax.swing.JLabel jLabPrefixo;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabTipoLancamento;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JPanel jPanRodape;
    private javax.swing.JPanel jPanSecundario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabTipoMovimentos;
    private javax.swing.JTextField jTexCadPor;
    private javax.swing.JTextField jTexCdPortador;
    private javax.swing.JTextField jTexModifPor;
    private javax.swing.JTextField jTexNomePortador;
    private javax.swing.JTextField jTexNomeTipoLancamento;
    private javax.swing.JTextField jTexPrefixo;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
