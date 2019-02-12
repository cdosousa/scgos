/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GFCCA0041
 */
package br.com.gfc.visao;

import br.com.DAO.ConsultaModelo;
import br.com.gfc.controle.CTipoCarteiraEDI;
import br.com.gfc.controle.CTipoServicoEDI;
import br.com.gfc.modelo.Portadores;
import br.com.gfc.modelo.TipoServicoEDI;
import br.com.gfc.modelo.TipoCarteiraEDI;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;
import java.awt.Dialog;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableColumn;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 criado em 21/11/2018
 *
 */
public class ManterPortadoresParamEDI extends javax.swing.JFrame {

    /**
     * objetos de instância dos registros da classe
     */
    private static SessaoUsuario su;
    private static Connection conexao;
    private VerificarTecla vt;

    /**
     * Objetos de instâncias dos registros da classe
     */
    private static Portadores por;
    private TipoServicoEDI modtedi;
    private TipoCarteiraEDI modcedi;
    private ConsultaModelo cTp;
    private ConsultaModelo cCar;

    /**
     * variáveis de instância da classe
     */
    private int numRegPor;
    private int idxCorrPor;
    private String sqlTpServ;
    private String sqlTpCart;
    private int linhaTpServ;
    private int linhaTpCart;
    private boolean moverLinhaTpServ;
    private boolean moverLinhaTpCart;
    private String operPor;
    private String operServ;
    private String operCart;
    boolean inclusaoServ;
    boolean inclusaoCart;
    private final boolean ISBOTAO = true;

    /**
     * Construtor padrão da classe
     */
    public ManterPortadoresParamEDI() {

    }

    /**
     * Construtor sobrecarregado da classe para chamada através da tela de
     * portador.
     *
     * @param su Objeto contento da sessão do usuario
     * @param conexao Objetoc contendo a intância de conexão do usuário ao banco
     * @param por Objeto contendo as informações do portador
     */
    public ManterPortadoresParamEDI(SessaoUsuario su, Connection conexao, Portadores por) {
        this.su = su;
        this.conexao = conexao;
        this.por = por;
        operPor = "N";
        setModalExclusionType(Dialog.ModalExclusionType.NO_EXCLUDE);
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setaVariaveis();
        this.dispose();
    }

    /**
     * Método para setar variáveis
     */
    private void setaVariaveis() {
        jForCdPortador.setText(por.getCdPortador());
        jTexNomePortador.setText(por.getNomePortador());
        buscarTipoServico();
        buscarTipoCarteira();
        monitoraLinhaServico();
        monitoraLinhaCarteira();
    }

    /**
     * método para limpar tela
     */
    private void limparTela() {
        jForCdPortador.setText("");
        jTexNomePortador.setText("");
        jTexCadPor.setText("");
        jForDataCad.setText("");
        jForHoraCad.setText("");
        jTexModifPor.setText("");
        jForDataModif.setText("");
        jForHoraModif.setText("");
        jTexRegAtual.setText("");
        jTexRegTotal.setText("");
        idxCorrPor = 0;
        numRegPor = 0;
    }

    /**
     * Método para buscar os tipos de serviços por portadores
     */
    private int buscarTipoServico() {
        modtedi = new TipoServicoEDI();
        linhaTpServ = 0;
        sqlTpServ = "{call pr_TipoServicosEdi(?)}";
        try {
            cTp = new ConsultaModelo(conexao);
            if (cTp.callProcedure(sqlTpServ, por.getCdPortador()) > 0) {
                jTabServicos.setModel(cTp.carregarConteudoTabela(jTabServicos));
                cTp.ajustarTabela(jTabServicos, 20, 200, 30);
                ajustarCamposTabela(jTabServicos, 2);
            }
        } catch (SQLException ex) {
            mensagemTela("Erro na busca dos Serviços!\nErro: " + ex);
        }
        if (cTp.getRowCount() > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Método para buscar as carteiras por portador
     */
    private int buscarTipoCarteira() {
        modcedi = new TipoCarteiraEDI();
        linhaTpCart = 0;
        sqlTpCart = "{call pr_TipoCarteiraEdi(?)}";
        try {
            cCar = new ConsultaModelo(conexao);
            if (cCar.callProcedure(sqlTpCart, por.getCdPortador()) > 0) {
                jTabCarteira.setModel(cCar.carregarConteudoTabela(jTabCarteira));
                cCar.ajustarTabela(jTabCarteira, 20, 200, 30);
                ajustarCamposTabela(jTabCarteira, 2);
            }
        } catch (SQLException ex) {
            mensagemTela(String.format("%s", "Erro na busca da carteira!\nErro:" + ex));
        }
        if (cCar.getRowCount() > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Método para controlar mudanças nas tabelas
     */
    private void monitoraLinhaServico() {
        VerificarTecla vt = new VerificarTecla();
        //adiciona listener para a tabela TipoServico
        jTabServicos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (moverLinhaTpServ) {
                    linhaTpServ = jTabServicos.getSelectedRow();
                }
            }
        });

        // captura tecla digitada na tabela
        jTabServicos.addKeyListener(new KeyListener() {
            String[] dados = new String[jTabServicos.getColumnCount()];

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                String tecla = vt.VerificarTecla(e).toUpperCase();
                if ("ABAIXO".equals(tecla) && !inclusaoServ && "A".equals(operPor)) {
                    if (jTabServicos.getSelectedRow() == jTabServicos.getRowCount() - 1) {
                        controleBotoes(!ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO);
                        modtedi = new TipoServicoEDI();
                        inclusaoServ = true;
                        Object[] linha = new Object[]{"", "", "0"};
                        jTabServicos.setModel(cTp.novaLinha(jTabServicos, linha));
                        linhaTpServ = jTabServicos.getSelectedRow() + 1;
                        cTp.ajustarTabela(jTabServicos, 20, 200, 30);
                        ajustarCamposTabela(jTabServicos, 2);
                        upValue();
                    }
                }
                if ("ESCAPE".equals(tecla) && inclusaoServ) {
                    try {
                        jTabServicos.setModel(cTp.excluirLinha(jTabServicos, jTabServicos.getSelectedRow()));
                        jTabServicos.requestFocus();
                        inclusaoServ = false;
                        cTp.ajustarTabela(jTabServicos, 20, 200, 30);
                        ajustarCamposTabela(jTabServicos, 2);
                    } catch (SQLException ex) {
                        Logger.getLogger(ManterPortadoresParamEDI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if ("ENTER".equals(tecla) && (inclusaoServ || "A".equals(operServ))) {
                    if (inclusaoServ) {
                        inclusaoServ = false;
                        operServ = "N";
                    } else {
                        operServ = "A";
                    }
                    salvarLinha();
                }
                if ("GUIA".equals(tecla) && "A".equals(operPor)) {
                    operServ = "A";
                    dowValue();
                    upValue();
                }
                if ("ACIMA".equals(tecla) && inclusaoServ) {
                    inclusaoServ = false;
                    operServ = "N";
                    salvarLinha();
                }
            }

            private void upValue() {
                dados[0] = modtedi.getCdTipoServico();
                dados[1] = modtedi.getNomeTipoServico();
                dados[2] = modtedi.getSituacao();
                try {
                    jTabServicos.setModel(cTp.upNovaLinha(jTabServicos, linhaTpServ, dados, 0, 1, 2));
                } catch (SQLException ex) {
                    Logger.getLogger(ManterPortadoresParamEDI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private void dowValue() {
                modtedi.setCdPortador(por.getCdPortador());
                modtedi.setCdTipoServico(String.format("%s", jTabServicos.getValueAt(linhaTpServ, 0)));
                modtedi.setNomeTipoServico(String.format("%s", jTabServicos.getValueAt(linhaTpServ, 1)));
                modtedi.setSituacao(String.format("%s", jTabServicos.getValueAt(linhaTpServ, 2)));
                //mensagemTela("Portador: " + modtedi.getCdPortador() + "\nCod.Serv.: " + modtedi.getCdTipoServico() + "\nNome Serv.: " + modtedi.getNomeTipoServico()
                //        + "\nSituacao: " + modtedi.getSituacao());
            }

            private void salvarLinha() {
                salvarTipoServico();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    /**
     * Método para controlar mudanças nas tabelas
     */
    private void monitoraLinhaCarteira() {
        VerificarTecla vt = new VerificarTecla();
        //adiciona listener para a tabela TipoCarteira
        jTabCarteira.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (moverLinhaTpCart) {
                    linhaTpCart = jTabCarteira.getSelectedRow();
                }
            }
        });

        // captura tecla digitada na tabela
        jTabCarteira.addKeyListener(new KeyListener() {
            String[] dados = new String[jTabCarteira.getColumnCount()];

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                String tecla = vt.VerificarTecla(e).toUpperCase();
                if ("ABAIXO".equals(tecla) && !inclusaoCart && "A".equals(operPor)) {
                    if (jTabCarteira.getSelectedRow() == jTabCarteira.getRowCount() - 1) {
                        controleBotoes(!ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO);
                        modcedi = new TipoCarteiraEDI();
                        inclusaoCart = true;
                        Object[] linha = new Object[]{"", "", "0"};
                        jTabCarteira.setModel(cCar.novaLinha(jTabCarteira, linha));
                        linhaTpCart = jTabCarteira.getSelectedRow() + 1;
                        cCar.ajustarTabela(jTabCarteira, 20, 200, 30);
                        ajustarCamposTabela(jTabCarteira, 2);
                        upValue();
                    }
                }
                if ("ESCAPE".equals(tecla) && inclusaoCart) {
                    try {
                        jTabCarteira.setModel(cCar.excluirLinha(jTabCarteira, jTabCarteira.getSelectedRow()));
                        jTabCarteira.requestFocus();
                        inclusaoCart = false;
                        cCar.ajustarTabela(jTabCarteira, 20, 200, 30);
                        ajustarCamposTabela(jTabCarteira, 2);
                    } catch (SQLException ex) {
                        Logger.getLogger(ManterPortadoresParamEDI.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if ("ENTER".equals(tecla) && (inclusaoCart || "A".equals(operCart))) {
                    if (inclusaoCart) {
                        inclusaoCart = false;
                        operCart = "N";
                    } else {
                        operCart = "A";
                    }
                    salvarLinha();
                }
                if ("GUIA".equals(tecla) && "A".equals(operPor)) {
                    operCart = "A";
                    dowValue();
                    upValue();
                }
                if ("ACIMA".equals(tecla) && inclusaoCart) {
                    inclusaoCart = false;
                    operCart = "N";
                    salvarLinha();
                }
            }

            private void upValue() {
                dados[0] = modcedi.getCdCarteira();
                dados[1] = modcedi.getNomeCarteira();
                dados[2] = modcedi.getSituacao();
                try {
                    jTabCarteira.setModel(cCar.upNovaLinha(jTabCarteira, linhaTpCart, dados, 0, 1, 2));
                } catch (SQLException ex) {
                    Logger.getLogger(ManterPortadoresParamEDI.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private void dowValue() {
                modcedi.setCdPortador(por.getCdPortador());
                modcedi.setCdCarteira(String.format("%s", jTabCarteira.getValueAt(linhaTpCart, 0)));
                modcedi.setNomeCarteira(String.format("%s", jTabCarteira.getValueAt(linhaTpCart, 1)));
                modcedi.setSituacao(String.format("%s", jTabCarteira.getValueAt(linhaTpCart, 2)));
                //mensagemTela("Portador: " + modtedi.getCdPortador() + "\nCod.Serv.: " + modtedi.getCdTipoServico() + "\nNome Serv.: " + modtedi.getNomeTipoServico()
                //        + "\nSituacao: " + modtedi.getSituacao());
            }

            private void salvarLinha() {
                salvarTipoCarteira();
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

    /**
     * Método para salvar o tipo de serviço
     */
    private void salvarTipoServico() {
        TipoServicoEDI tpedi = new TipoServicoEDI();
        tpedi.setCdPortador(modtedi.getCdPortador());
        tpedi.setCdTipoServico(modtedi.getCdTipoServico());
        tpedi.setNomeTipoServico(modtedi.getNomeTipoServico());
        tpedi.setSituacao(modtedi.getSituacao().substring(0, 1));
        CTipoServicoEDI ctpedi = new CTipoServicoEDI(conexao, su);
        try {
            if (ctpedi.gravarServico(tpedi, operServ) > 0) {
                buscarTipoServico();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManterPortadoresParamEDI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para salvar o tipo de carteira
     */
    private void salvarTipoCarteira() {
        TipoCarteiraEDI tcedi = new TipoCarteiraEDI();
        tcedi.setCdPortador(modcedi.getCdPortador());
        tcedi.setCdCarteira(modcedi.getCdCarteira());
        tcedi.setNomeCarteira(modcedi.getNomeCarteira());
        tcedi.setSituacao(modcedi.getSituacao().substring(0, 1));
        CTipoCarteiraEDI ctcedi = new CTipoCarteiraEDI(conexao, su);
        try {
            if (ctcedi.gravarCarteira(tcedi, operCart) > 0) {
                buscarTipoCarteira();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManterPortadoresParamEDI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para retornar mensagem na tela
     *
     * @param msg Mensagem a ser exibida
     */
    private void mensagemTela(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    /**
     * Metodo para controlar os botoes da tela
     *
     * @param bNo Botão Novo
     * @param bEd Botão Editar
     * @param bSa Botão Salvar
     * @param bCa Botão Cancelar
     * @param bEx Botão Excluir
     * @param bPe Botão Pesquisar
     * @param bCl Botão Sair
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

    /**
     * Método para ajustar tipo do campo da tabela
     *
     * @param tabela Objeto contendo a tabela a ser ajustada
     * @param coluna numero da coluna a ser ajustada
     */
    private void ajustarCamposTabela(JTable tabela, int coluna) {
        TableColumn situacao = tabela.getColumnModel().getColumn(coluna);
        JComboBox sit = new JComboBox();
        sit.addItem("Ativo");
        sit.addItem("Inativo");
        situacao.setCellEditor(new DefaultCellEditor(sit));
        DefaultTableCellRenderer rd = new DefaultTableCellRenderer();
        rd.setToolTipText("Clique para ver opções");
        situacao.setCellRenderer(rd);
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
        jLabPortador = new javax.swing.JLabel();
        jForCdPortador = new javax.swing.JFormattedTextField();
        jLabNomePortador = new javax.swing.JLabel();
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
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanTabela = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabServicos = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabCarteira = new javax.swing.JTable();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Cadastros EDI");

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

        jLabPortador.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPortador.setText("Portador:");

        jForCdPortador.setEditable(false);
        try {
            jForCdPortador.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jForCdPortador.setEnabled(false);

        jLabNomePortador.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNomePortador.setText("Nome:");

        jTexNomePortador.setEditable(false);
        jTexNomePortador.setEnabled(false);

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
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

            jPanTabela.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jTabServicos.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null}
                },
                new String [] {
                    "Código", "Serviço", "Situação"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            jScrollPane1.setViewportView(jTabServicos);
            if (jTabServicos.getColumnModel().getColumnCount() > 0) {
                jTabServicos.getColumnModel().getColumn(0).setResizable(false);
                jTabServicos.getColumnModel().getColumn(0).setPreferredWidth(20);
                jTabServicos.getColumnModel().getColumn(1).setResizable(false);
                jTabServicos.getColumnModel().getColumn(1).setPreferredWidth(200);
                jTabServicos.getColumnModel().getColumn(2).setResizable(false);
                jTabServicos.getColumnModel().getColumn(2).setPreferredWidth(30);
            }

            javax.swing.GroupLayout jPanTabelaLayout = new javax.swing.GroupLayout(jPanTabela);
            jPanTabela.setLayout(jPanTabelaLayout);
            jPanTabelaLayout.setHorizontalGroup(
                jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTabelaLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            jPanTabelaLayout.setVerticalGroup(
                jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTabelaLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jTabbedPane1.addTab("Cod. Serviço", jPanTabela);

            jTabCarteira.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null}
                },
                new String [] {
                    "Código", "Carteira", "Situação"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            jScrollPane2.setViewportView(jTabCarteira);
            if (jTabCarteira.getColumnModel().getColumnCount() > 0) {
                jTabCarteira.getColumnModel().getColumn(0).setResizable(false);
                jTabCarteira.getColumnModel().getColumn(0).setPreferredWidth(20);
                jTabCarteira.getColumnModel().getColumn(1).setResizable(false);
                jTabCarteira.getColumnModel().getColumn(1).setPreferredWidth(200);
                jTabCarteira.getColumnModel().getColumn(2).setResizable(false);
                jTabCarteira.getColumnModel().getColumn(2).setPreferredWidth(30);
            }

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jTabbedPane1.addTab("Cod. Carteira", jPanel1);

            javax.swing.GroupLayout jPanSecundarioLayout = new javax.swing.GroupLayout(jPanSecundario);
            jPanSecundario.setLayout(jPanSecundarioLayout);
            jPanSecundarioLayout.setHorizontalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanSecundarioLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jTabbedPane1))
                            .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addComponent(jLabPortador)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForCdPortador, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabNomePortador)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexNomePortador, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(187, 187, 187))))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanSecundarioLayout.setVerticalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabPortador)
                        .addComponent(jForCdPortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabNomePortador)
                        .addComponent(jTexNomePortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 171, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
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
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanSecundario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButPesquisarActionPerformed

    private void jButProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButProximoActionPerformed
        // TODO add your handling code here:
        idxCorrPor += 1;
    }//GEN-LAST:event_jButProximoActionPerformed

    private void jButAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAnteriorActionPerformed
        // TODO add your handling code here:
        idxCorrPor -= 1;
    }//GEN-LAST:event_jButAnteriorActionPerformed

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButCancelarActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        // TODO add your handling code here:
        operPor = "A";
        operCart = "A";
        controleBotoes(!ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO);

    }//GEN-LAST:event_jButEditarActionPerformed

    private void jMenuItemSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jMenuItemSairActionPerformed

    private void jMenuItemSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalvarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jMenuItemSalvarActionPerformed

    private void jMenuItemEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEditarActionPerformed
        // TODO add your handling code here:
        jButEditarActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemEditarActionPerformed

    private void jButNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButNovoActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButNovoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        jButNovoActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButExcluirActionPerformed

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        // TODO add your handling code here:
        controleBotoes(!ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
        inclusaoServ = false;
        operServ = "A";
        operPor = "A";
    }//GEN-LAST:event_jButSalvarActionPerformed

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
            java.util.logging.Logger.getLogger(ManterPortadoresParamEDI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterPortadoresParamEDI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterPortadoresParamEDI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterPortadoresParamEDI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterPortadoresParamEDI(su, conexao, por).setVisible(true);
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
    private javax.swing.JFormattedTextField jForCdPortador;
    private javax.swing.JFormattedTextField jForDataCad;
    private javax.swing.JFormattedTextField jForDataModif;
    private javax.swing.JFormattedTextField jForHoraCad;
    private javax.swing.JFormattedTextField jForHoraModif;
    private javax.swing.JLabel jLabCadPor;
    private javax.swing.JLabel jLabModifPor;
    private javax.swing.JLabel jLabNomePortador;
    private javax.swing.JLabel jLabPortador;
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
    private javax.swing.JPanel jPanTabela;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTabCarteira;
    private javax.swing.JTable jTabServicos;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTexCadPor;
    private javax.swing.JTextField jTexModifPor;
    private javax.swing.JTextField jTexNomePortador;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
