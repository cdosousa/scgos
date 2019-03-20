/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GFCCA0110
 */
package br.com.gfc.visao;

// Objetos de Sessão de Usuário e Conexão com o Banco
import br.com.DAO.ConsultaModelo;
import br.com.gfc.controle.CBancos;
import br.com.modelo.SessaoUsuario;
import java.sql.Connection;

// Objetos Principais da Classe
import br.com.gfc.modelo.EdiOcorrencia;
import br.com.gfc.controle.CEdiOcorrencia;
import br.com.gfc.dao.EdiOcorrenciaDAO;
import br.com.gfc.modelo.Bancos;

// Objetos correlatos da classe
// Objetos gerais da classe
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoInteiro;
import br.com.modelo.HoraSistema;
import br.com.modelo.VerificarTecla;
import java.awt.Dialog;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 13/03/2018
 * @version 0.01Beta_0917
 */
public class ManterEdiOcorrencia extends javax.swing.JFrame {

    // variáveis de sessão do usuário e banco de dados
    private static Connection conexao;
    private static SessaoUsuario su;

    // variáveis de objeto principal da classe
    private EdiOcorrencia regCorr;
    private List< EdiOcorrencia> resultado;
    private CBancos cb;
    private Bancos modb;
    private EdiOcorrencia modEo;
    private ConsultaModelo cmEo;

    // variáveis de instância da classe
    private VerificarTecla vt;
    private DataSistema dat;
    private String data = null;
    private int numReg;
    private int idxCorr;
    private String sql;
    private String oper;
    private final String TABELA = "gfcediocorrencia";
    private final boolean ISBOTAO = true;
    private int linhaEdiOcorr;
    private boolean moverLinhaTpServ = false;
    private String sqlEdiOcorr;
    private boolean inclusaoOcorrencia = false;

    /**
     * Método para abrir tela através do menu do programa
     *
     * @param su Objeto contendo a sessão ativa do usuário
     * @param conexao Objeto contendo a conexao ativa do usuario
     */
    public ManterEdiOcorrencia(SessaoUsuario su, Connection conexao) {
        this.su = su;
        this.conexao = conexao;
        setaParametros();
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
        sql = "SELECT * FROM GFCBANCOS";
    }

    /**
     * Construtor para abrir a tela através de pesquisa do tipo de pagamento
     *
     * @param su Objeto contendo a sessão ativa do usuário
     * @param conexao Objeto contendo a conexao ativa do usuario
     * @param cdBanco String contendo o código do banco a ser pesquisado
     */
    public ManterEdiOcorrencia(SessaoUsuario su, Connection conexao, String cdBanco) {
        this.su = su;
        this.conexao = conexao;
        setaParametros();
        sql = "SELECT * FROM GFCBANCOS WHERE CD_BANCO = '" + cdBanco
                + "'";
        LiberarBloquearCampos(false);
        pesquisar();
        controleBotoes(!ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO);
        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
    }

    /**
     * Método para setar os parametros comuns da classe
     */
    private void setaParametros() {
        initComponents();
        try {
            cmEo = new ConsultaModelo(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(ManterEdiOcorrencia.class.getName()).log(Level.SEVERE, null, ex);
        }
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formatarCampos();
        LiberarBloquearCampos(false);
        setLocationRelativeTo(null);
        buscarOcorrencias();
        monitoraLinhaTabela();
        this.dispose();
    }

    // método para setar formato do campo
    private void formatarCampos() {
        jForCdBanco.setDocument(new DefineCampoInteiro());
    }

    // método para limpar tela
    private void limparTela() {
        jForCdBanco.setText("");
        jTexNomeBanco.setText("");
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
        LiberarBloquearCampos(true);
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {
        cb = new CBancos(conexao);
        modb = new Bancos();
        try {
            numReg = cb.pesquisar(sql);
            idxCorr = +1;
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Argumento inválido na busca\nErr: " + iae.getCause());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Pesquisar os Registro\nErr: " + ex);
        }
        if (numReg > 0) {
            upRegistros();
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum registro encontrado!");
        }
    }

    /**
     * Método para buscar ocorrências edi
     */
    private void buscarOcorrencias() {
        zerarTabela(jTabOcorrencia);
        modEo = new EdiOcorrencia();
        linhaEdiOcorr = 0;
        sqlEdiOcorr = "{call pr_OcorreciaEdi(?)}";
        try {
            cmEo = new ConsultaModelo(conexao);
            if (cmEo.callProcedure(sqlEdiOcorr, jForCdBanco.getText()) > 0) {
                jTabOcorrencia.setModel(cmEo.carregarConteudoTabela(jTabOcorrencia));
                ajustarColunasTabelaOcorrencias();
            }
        } catch (SQLException ex) {
            mensagem("Erro na busca do SQL!\nErro: " + ex);
        }
    }

    /**
     * Método para atualizar registro na tela
     */
    private void upRegistros() {
        modb = new Bancos();
        jTexRegTotal.setText(Integer.toString(numReg));
        jTexRegAtual.setText(Integer.toString(idxCorr));
        cb.mostrarPesquisa(modb, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jForCdBanco.setText(modb.getCdBanco());
        jTexNomeBanco.setText(modb.getNomeBanco());
        jTexCadPor.setText(modb.getUsuarioCadastro());
        if (modb.getDataCadastro() != null) {
            jForDataCad.setText(dat.getDataConv(Date.valueOf(modb.getDataCadastro())));
        }
        jForHoraCad.setText(modb.getHoraCadastro());
        jTexModifPor.setText(modb.getUsuarioModificacao());
        if (modb.getDataModificacao() != null) {
            jForDataModif.setText(dat.getDataConv(Date.valueOf(modb.getDataModificacao())));
            jForHoraModif.setText(modb.getHoraModificacao());
        }
        buscarOcorrencias();
        // Habilitando / Desabilitando botões de navegação de registros
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

    /**
     * Método para liberar ou Bloquear os campos da tela
     *
     * @param acao
     */
    private void LiberarBloquearCampos(boolean acao) {
        //jForCdBanco.setEditable(acao);
    }

    private void novoRegistro() {
        limparTela();

    }

    /**
     * Método para dar zoom no campo Banco
     */
    private void zoomBanco() {
        PesquisarBancos zoom = new PesquisarBancos(this, rootPaneCheckingEnabled, "P", conexao);
        zoom.setVisible(rootPaneCheckingEnabled);
        jForCdBanco.setText(zoom.getSelecao1());
        jTexNomeBanco.setText(zoom.getSelecao2());
    }

    /**
     * Metodo para controlar os botoes
     *
     * @param bNo Botão novo lancamento
     * @param bEd Botão editar lancamento
     * @param bSa Botão salvar lancamento
     * @param bCa Botão cancelar ação na tela
     * @param bEx Botão excluir lancamento
     * @param bPe Botão pesquisar lancamento
     * @param bCl Botão sair da tela de lancamento
     */
    private void controleBotoes(boolean bNo, boolean bEd, boolean bSa, boolean bCa, boolean bEx, boolean bPe, boolean bCl) {
        jButNovo.setEnabled(bNo);
        jButEditar.setEnabled(bEd);
        jButSalvar.setEnabled(bSa);
        jButCancelar.setEnabled(bCa);
        jButExcluir.setEnabled(bEx);
        jButPesquisar.setEnabled(bPe);
        jButSair.setEnabled(bCl);
    }

    private void desabilitaBotNavegarcao() {
        jButProximo.setEnabled(false);
        jButAnterior.setEnabled(false);
    }

    private void monitoraLinhaTabela() {
        vt = new VerificarTecla();
        // Adiciona listener na tabela para controlar mudança de linha
        jTabOcorrencia.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (moverLinhaTpServ) {
                    linhaEdiOcorr = jTabOcorrencia.getSelectedRow();
                }
            }
        });

        // capitura tecla pressionada na tabela
        jTabOcorrencia.addKeyListener(new KeyListener() {
            String[] dados = new String[jTabOcorrencia.getColumnCount()];

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                String tecla = vt.VerificarTecla(e).toUpperCase();
                if ("ABAIXO".equals(tecla) && !inclusaoOcorrencia && "A".equals(oper)) {
                    if (jTabOcorrencia.getSelectedRow() == jTabOcorrencia.getRowCount() - 1) {
                        controleBotoes(!ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO);
                        modEo = new EdiOcorrencia();
                        inclusaoOcorrencia = true;
                        Object[] linha = new Object[]{"", "", "", "0"};
                        jTabOcorrencia.setModel(cmEo.novaLinha(jTabOcorrencia, linha));
                        ajustarColunasTabelaOcorrencias();
                        linhaEdiOcorr = jTabOcorrencia.getSelectedRow() + 1;
                        upValue();
                    }
                } else if ("ESCAPE".equals(tecla) && inclusaoOcorrencia) {
                    try {
                        jTabOcorrencia.setModel(cmEo.excluirLinha(jTabOcorrencia, jTabOcorrencia.getSelectedRow()));
                        jTabOcorrencia.requestFocus();
                        inclusaoOcorrencia = false;
                        ajustarColunasTabelaOcorrencias();
                    } catch (SQLException ex) {
                        Logger.getLogger(ManterPortadoresParamEDI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if ("ENTER".equals(tecla) && (inclusaoOcorrencia || "A".equals(oper))) {
                    if (inclusaoOcorrencia) {
                        inclusaoOcorrencia = false;
                        oper = "N";
                    } else {
                        oper = "A";
                    }
                    salvarLinha();
                } else if ("GUIA".equals(tecla) && inclusaoOcorrencia) {
                    oper = "A";
                    dowValue();
                    upValue();
                } else if ("ACIMA".equals(tecla) && inclusaoOcorrencia) {
                    inclusaoOcorrencia = false;
                    oper = "N";
                    salvarLinha();
                }

            }

            private void upValue() {
                dados[0] = modEo.getCdOcorrencia();
                dados[1] = modEo.getNomeOcorrencia();
                dados[2] = modEo.getLiquidarTitulo();
                dados[3] = modEo.getSituacao();
                try {
                    jTabOcorrencia.setModel(cmEo.upNovaLinha(jTabOcorrencia, linhaEdiOcorr, dados, 0, 1, 2, 3));
                } catch (SQLException ex) {
                    Logger.getLogger(ManterPortadoresParamEDI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private void dowValue() {
                modEo.setCdBanco(modb.getCdBanco());
                modEo.setCdOcorrencia(String.format("%s", jTabOcorrencia.getValueAt(linhaEdiOcorr, 0)));
                modEo.setNomeOcorrencia(String.format("%s", jTabOcorrencia.getValueAt(linhaEdiOcorr, 1)).toUpperCase());
                modEo.setLiquidarTitulo(String.format("%s", jTabOcorrencia.getValueAt(linhaEdiOcorr, 2)).substring(0, 1));
                modEo.setSituacao(String.format("%s", jTabOcorrencia.getValueAt(linhaEdiOcorr, 3)).substring(0, 1));
            }

            private void salvarLinha() {
                salvarOcorrencia();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    /**
     * Método para salvar a ocorrencia no banco de dados
     *
     * @return
     */
    private int salvarOcorrencia() {
        EdiOcorrencia ediOcorr = new EdiOcorrencia();
        ediOcorr.setCdBanco(modEo.getCdBanco());
        ediOcorr.setCdOcorrencia(modEo.getCdOcorrencia());
        ediOcorr.setNomeOcorrencia(modEo.getNomeOcorrencia());
        ediOcorr.setLiquidarTitulo(modEo.getLiquidarTitulo());
        ediOcorr.setSituacao(modEo.getSituacao());
        CEdiOcorrencia cEo = new CEdiOcorrencia(conexao, su);
        return cEo.gravarOcorrencia(modEo, oper);
    }

    /**
     * Método para ajustar os campos da tabela de ocorrências
     */
    private void ajustarColunasTabelaOcorrencias() {
        cmEo.ajustarTabela(jTabOcorrencia, 5, 400, 5, 20);
        ajustarCamposTabela(jTabOcorrencia, 2, new String[]{" ", "Sim", "Não"});
        ajustarCamposTabela(jTabOcorrencia, 3, new String[]{" ", "Ativo", "Inativo"});
    }

    /**
     * Método para ajustar tipo do campo da tabela
     *
     * @param tabela Objeto contendo a tabela a ser ajustada
     * @param coluna numero da coluna a ser ajustada
     * @param conteudo conteúdo do combo box para ser adicionado
     */
    private void ajustarCamposTabela(JTable tabela, int coluna, String[] conteudo) {
        TableColumn campo = tabela.getColumnModel().getColumn(coluna);
        JComboBox comboBox = new JComboBox();
        for (int i = 0; i < conteudo.length; i++) {
            comboBox.addItem(conteudo[i]);
        }
        campo.setCellEditor(new DefaultCellEditor(comboBox));
        DefaultTableCellRenderer rd = new DefaultTableCellRenderer();
        rd.setToolTipText("Clique para ver opções");
        campo.setCellRenderer(rd);
    }

    private void zerarTabela(JTable tabela) {
        cmEo.zerarTabela(tabela, new Object[]{null, null, null, null},
                new String[]{"Código", "Ocorrência", "Liquidar", "Situação"},
                new Class[]{String.class, String.class, String.class, String.class},
                new boolean[]{false, false, false, false});
    }

    /**
     * Método para gerar mensagem na tela
     *
     * @param msg
     */
    private void mensagem(String msg) {
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
        jButImportarArq = new javax.swing.JButton();
        jPanSecundario = new javax.swing.JPanel();
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
        jSepLancamento = new javax.swing.JSeparator();
        jLabCdBanco = new javax.swing.JLabel();
        jForCdBanco = new javax.swing.JFormattedTextField();
        jTexNomeBanco = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabOcorrencia = new javax.swing.JTable();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Parâmetros EDI");

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

        jButImportarArq.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButImportarArq.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/fileimport-32.png"))); // NOI18N
        jButImportarArq.setText("Importar Arquivo");
        jButImportarArq.setFocusable(false);
        jButImportarArq.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButImportarArq.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButImportarArq.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButImportarArqActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButImportarArq);

        jPanSecundario.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanSecundario.setToolTipText("");

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
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanRodapeLayout.createSequentialGroup()
                    .addGap(13, 13, 13)
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
                    .addGap(122, 122, 122))
            );
            jPanRodapeLayout.setVerticalGroup(
                jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanRodapeLayout.createSequentialGroup()
                    .addGap(11, 11, 11)
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

            javax.swing.GroupLayout jPanSecundarioLayout = new javax.swing.GroupLayout(jPanSecundario);
            jPanSecundario.setLayout(jPanSecundarioLayout);
            jPanSecundarioLayout.setHorizontalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            jPanSecundarioLayout.setVerticalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabCdBanco.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdBanco.setText("Banco:");

            jForCdBanco.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jForCdBancoKeyPressed(evt);
                }
            });

            jTexNomeBanco.setEditable(false);
            jTexNomeBanco.setEnabled(false);

            jTabOcorrencia.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null}
                },
                new String [] {
                    "Código", "Ocorrencia", "Liquidar", "Situacao"
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
            jScrollPane1.setViewportView(jTabOcorrencia);
            if (jTabOcorrencia.getColumnModel().getColumnCount() > 0) {
                jTabOcorrencia.getColumnModel().getColumn(0).setResizable(false);
                jTabOcorrencia.getColumnModel().getColumn(0).setPreferredWidth(5);
                jTabOcorrencia.getColumnModel().getColumn(1).setResizable(false);
                jTabOcorrencia.getColumnModel().getColumn(1).setPreferredWidth(400);
                jTabOcorrencia.getColumnModel().getColumn(2).setResizable(false);
                jTabOcorrencia.getColumnModel().getColumn(2).setPreferredWidth(5);
                jTabOcorrencia.getColumnModel().getColumn(3).setResizable(false);
                jTabOcorrencia.getColumnModel().getColumn(3).setPreferredWidth(20);
            }

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSepLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 848, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jLabCdBanco)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForCdBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexNomeBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 673, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(4, 4, 4)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCdBanco)
                        .addComponent(jForCdBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(5, 5, 5)
                    .addComponent(jSepLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(2, 2, 2)
                    .addComponent(jPanSecundario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        if ("N".equals(oper) && jForCdBanco.getText().trim().isEmpty()) {
            sql = "SELECT * FROM GFCBANCOS WHERE CD_BANCO = '" + jForCdBanco.getText()
                    + "'";
            JOptionPane.showMessageDialog(null, "Para novo parâmetro, é necessário informar o tipo de pagamento!");
        } else {
            limparTela();
            LiberarBloquearCampos(false);
            controleBotoes(ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
            pesquisar();
        }
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:

        sql = "SELECT * FROM GFCBANCOS WHERE CD_BANCO LIKE '%" + jForCdBanco.getText()
                + "%'";
        LiberarBloquearCampos(false);
        controleBotoes(ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
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
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
        desabilitaBotNavegarcao();
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        controleBotoes(!ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO);
        desabilitaBotNavegarcao();
        oper = "A";         // se estiver editando um registro do sistema, a operação do sistema será A de alteração de Registro
        LiberarBloquearCampos(true);
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
        oper = "N";
        controleBotoes(!ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO);
        desabilitaBotNavegarcao();
        novoRegistro();
    }//GEN-LAST:event_jButNovoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        jButNovoActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButExcluirActionPerformed

    private void jForCdBancoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jForCdBancoKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomBanco();
        }
    }//GEN-LAST:event_jForCdBancoKeyPressed

    private void jButImportarArqActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButImportarArqActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser();
        int option = fc.showOpenDialog(jTooMenuFerramentas);
        if (option == fc.APPROVE_OPTION) {
            File arq = fc.getSelectedFile();
            String caminhoArquivo = arq.getParent() + File.separator + arq.getName();
            CEdiOcorrencia cEdiOco = new CEdiOcorrencia(conexao, su);
            cEdiOco.importarOcorrencia(caminhoArquivo);
        }
    }//GEN-LAST:event_jButImportarArqActionPerformed

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
            java.util.logging.Logger.getLogger(ManterEdiOcorrencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterEdiOcorrencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterEdiOcorrencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterEdiOcorrencia.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterEdiOcorrencia(su, conexao).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButAnterior;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButEditar;
    private javax.swing.JButton jButExcluir;
    private javax.swing.JButton jButImportarArq;
    private javax.swing.JButton jButNovo;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JButton jButProximo;
    private javax.swing.JButton jButSair;
    private javax.swing.JButton jButSalvar;
    private javax.swing.JFormattedTextField jForCdBanco;
    private javax.swing.JFormattedTextField jForDataCad;
    private javax.swing.JFormattedTextField jForDataModif;
    private javax.swing.JFormattedTextField jForHoraCad;
    private javax.swing.JFormattedTextField jForHoraModif;
    private javax.swing.JLabel jLabCadPor;
    private javax.swing.JLabel jLabCdBanco;
    private javax.swing.JLabel jLabModifPor;
    private javax.swing.JLabel jLabReg;
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
    private javax.swing.JSeparator jSepLancamento;
    private javax.swing.JTable jTabOcorrencia;
    private javax.swing.JTextField jTexCadPor;
    private javax.swing.JTextField jTexModifPor;
    private javax.swing.JTextField jTexNomeBanco;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
