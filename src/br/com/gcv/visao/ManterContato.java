/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GCVMO0010
 */
package br.com.gcv.visao;

// objetos do registro Pai da classe
import br.com.controle.CBuscarSequencia;
import br.com.gcv.modelo.Contato;
import br.com.gcv.dao.ContatoDAO;
import br.com.gcv.controle.CContato;

// Objetos para pesquisa de correlato
// Objetos de instância de parâmetros de ambiente
import br.com.modelo.DataSistema;
import br.com.modelo.HoraSistema;
import br.com.modelo.ParametrosGerais;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;

// Objetos de ambiente java
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 14/11/2017
 */
public class ManterContato extends javax.swing.JFrame {

    // Variáveis de instancia de parâmetros de ambiente
    private static Connection conexao;
    private static SessaoUsuario su;
    private static ParametrosGerais pg;
    private DataSistema dat;
    private VerificarTecla vt;

    // Variáveis de instância e objetos da classe para tabela data
    private ContatoDAO dcodat;
    private String sqldat;
    private int indexAtualData = 0;
    private String dataSelec;

    // Variáveis de Instancia e objetos da classe para a tabela atendimento
    private ContatoDAO dcoate;
    private String sqlatend;
    private int indexAtualAtend = 0;
    private String cdAtendimento;

    // Variáveis de Instâncioa e Objetos da Classe para os registro correlato
    private Contato modcon;
    private CContato ccon;

    private Contato regCorr;
    private List< Contato> resultado;

    // Variáveis de instância da classe
    private int numReg;
    private int idxCorr;
    private String sql;
    private String oper;
    private String datasource = "gcvcontato";
    private final boolean ISBOTAO = true;

    // construturo padrão
    public ManterContato() {

    }

    public ManterContato(SessaoUsuario su, Connection conexao, ParametrosGerais pg) {
        this.su = su;
        this.conexao = conexao;
        this.pg = pg;
        oper = "N";
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setaInicio();
        mudarLinhaTabData();
        mudarLinhaTabAtendimento();
        controleBotoes(ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO, ISBOTAO);
        //this.dispose();
    }

    // setando as variáveis de início
    private void setaInicio() {
        modcon = new Contato();
        ccon = new CContato(conexao);
        sqldat = "select c.data_atendimento as Data"
                + " from gcvcontato as c"
                + " group by c.data_atendimento"
                + " order by c.data_atendimento desc";
        buscarTodos();
        jTabData.setModel(dcodat);
        ajustarTabelaData();
    }

    // criando um listiner para quando mudar de linha na tabela Data
    private void mudarLinhaTabData() {
        jTabData.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtualData = jTabData.getSelectedRow();
                ajustarTabelaData();
            }
        });
    }

    // método para buscar todos os atendimentos
    private void buscarTodos() {
        try {
            dcodat = new ContatoDAO(conexao);
            dcodat.setQuery(sqldat);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca dos Atendimentos!\nPrograma GCVMO0010-Manter Contato Cliente");
        }
    }

    // método para ajustar o tamanho da tabela data
    private void ajustarTabelaData() {
        jTabData.getColumnModel().getColumn(0).setMinWidth(30);
        jTabData.getColumnModel().getColumn(0).setPreferredWidth(30);
        if (indexAtualData < 0) {
            indexAtualData = 0;
        }
        dataSelec = String.format("%s", jTabData.getValueAt(indexAtualData, 0));
        buscarAtendimentos();
    }

    // Buscando Atendimento
    private void buscarAtendimentos() {
        if (jTexPesquisar1.getText().trim().isEmpty()) {
            sqlatend = "select c.cd_atendimento as Atendimento,"
                    + " c.cd_vistoria as Vistoria,"
                    + " c.cd_proposta as Proposta,"
                    + " case situacao"
                    + " when 'AA' then 'Aguardando Agendamento'"
                    + " when 'AV' then 'Aguardando Vistoria'"
                    + " when 'NI' then 'Não Iniciado'"
                    + " when 'PG' then 'Proposta Gerada'"
                    + " else 'Pendente'"
                    + " end as 'Situação'"
                    + " from gcvcontato as c"
                    + " where c.data_atendimento = '" + dataSelec
                    + "' order by c.data_atendimento desc, c.cd_atendimento desc;";
        } else {
            sqlatend = "select c.cd_atendimento as Atendimento,"
                    + " c.cd_vistoria as Vistoria,"
                    + " c.cd_proposta as Proposta,"
                    + " case situacao"
                    + " when 'AA' then 'Aguardando Agendamento'"
                    + " when 'AV' then 'Aguardando Vistoria'"
                    + " when 'NI' then 'Não Iniciado'"
                    + " when 'PG' then 'Proposta Gerada'"
                    + " else 'Pendente'"
                    + " end as 'Situação'"
                    + " from gcvcontato as c"
                    + " where c.data_atendimento = '" + dataSelec
                    + "' and nome_cliente LIKE '%"
                    + jTexPesquisar1.getText().trim()
                    + "%' order by c.data_atendimento desc, c.cd_atendimento desc;";
        }

        buscarSelecao();
        jTabAtendimento.setModel(dcoate);
        ajustarTabelaAtend();
    }

    // criando um listiner para quando mudar de linha na tabela Atendimento
    private void mudarLinhaTabAtendimento() {
        jTabAtendimento.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtualAtend = jTabAtendimento.getSelectedRow();
                ajustarTabelaAtend();
            }
        });
    }

    // método para buscar os atendimentos conforme a data
    private void buscarSelecao() {
        try {
            dcoate = new ContatoDAO(conexao);
            dcoate.setQuery(sqlatend);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca dos Atendimentos!\nPrograma GCVMO0010-Manter Contato Cliente");
        }
    }

    // método para ajustar o tamanho da tabela Atendimento
    private void ajustarTabelaAtend() {
        jTabAtendimento.getColumnModel().getColumn(0).setMinWidth(15);
        jTabAtendimento.getColumnModel().getColumn(0).setPreferredWidth(15);
        jTabAtendimento.getColumnModel().getColumn(1).setMinWidth(10);
        jTabAtendimento.getColumnModel().getColumn(1).setPreferredWidth(10);
        jTabAtendimento.getColumnModel().getColumn(2).setMinWidth(10);
        jTabAtendimento.getColumnModel().getColumn(2).setPreferredWidth(10);
        jTabAtendimento.getColumnModel().getColumn(3).setMinWidth(90);
        jTabAtendimento.getColumnModel().getColumn(3).setPreferredWidth(90);
        if (indexAtualAtend < 0) {
            indexAtualAtend = 0;
        }
        cdAtendimento = String.format("%s", jTabAtendimento.getValueAt(indexAtualAtend, 0));
        buscarCorrelatos();
    }

    //metodo para buscar correlatos
    private void buscarCorrelatos() {
        if (!cdAtendimento.isEmpty()) {
            String sql = "select * from gcvcontato where cd_atendimento = " + cdAtendimento;
            try {
                ccon.pesquisar(sql);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro na busca do atendimento!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro Geral na busca do Atendimento!\nErr:" + ex);
            }
            ccon.mostrarPesquisa(modcon, 0);
            atualizarTela();
        }
    }

    // método para atualizar tela
    private void atualizarTela() {
        dat = new DataSistema();
        jTexNomeRazaoSocial.setText(modcon.getNomeRazaoSocial());
        jComTipoPessoa.setSelectedIndex(Integer.parseInt(modcon.getTipoPessoa()));
        jForTelefone.setText(modcon.getTelefone());
        jTexEmail.setText(modcon.getEmail());
        jTextAreaObservacao.setText(modcon.getObs());
        jTexCadastradoPor.setText(modcon.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modcon.getDataCadastro())));
        jForHoraCad.setText(modcon.getHoraCadastro());
        if (modcon.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(modcon.getDataModificacao())));
        }
    }

    // método para limpar tela
    private void limparTela() {
        jTexNomeRazaoSocial.setText("");
        jComTipoPessoa.setSelectedIndex(0);
        jForTelefone.setText("");
        jTexEmail.setText("");
        jTextAreaObservacao.setText("");
        jTexCadastradoPor.setText("");
        jForDataCadastro.setText("");
        jForDataModificacao.setText("");
        jTexRegAtual.setText("");
        jTexRegTotal.setText("");
        idxCorr = 0;
        numReg = 0;
        resultado = null;
        regCorr = null;
    }

    //Bloquear os campos da tela
    private void bloquearCampos() {
        jComTipoPessoa.setEditable(false);
        jTexNomeRazaoSocial.setEditable(false);
        jForTelefone.setEditable(false);
        jTexEmail.setEditable(false);
    }

    //Loberar os campos da tela para atualização
    private void liberarCampos() {
        jComTipoPessoa.setEditable(true);
        jTexNomeRazaoSocial.setEditable(true);
        jForTelefone.setEditable(true);
        jTexEmail.setEditable(true);
    }

    private void novoRegistro() {
        limparTela();
        liberarCampos();
        modcon = new Contato();
        jComTipoPessoa.setSelectedIndex(1);
        jTexNomeRazaoSocial.requestFocus();
        jTexNomeRazaoSocial.selectAll();
        jButEditar.setEnabled(false);
    }

    private void manterAtendimento() {
        new ManterAtendimentoRev2(su, conexao, pg, modcon).setVisible(true);
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
     * @param bAte Botão Atendimento
     */
    private void controleBotoes(boolean bNo, boolean bEd, boolean bSa, boolean bCa, boolean bEx, boolean bPe, boolean bCl, boolean bAte) {
        jButNovo.setEnabled(bNo);
        jButEditar.setEnabled(bEd);
        jButSalvar.setEnabled(bSa);
        jButCancelar.setEnabled(bCa);
        jButExcluir.setEnabled(bEx);
        jButPesquisar.setEnabled(bPe);
        jButSair.setEnabled(bCl);
        jButAtendimento.setEnabled(bAte);
    }

    // método para salvar contato
    private void salvarContato() {
        HoraSistema hs = new HoraSistema();
        if (jTexNomeRazaoSocial.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "No mínimo o Nome/Razão Social precisa ser preenchido corretamente!");
        } else {

            DataSistema dat = new DataSistema();
            Contato con = new Contato();
            String data = null;
            con.setNomeRazaoSocial(jTexNomeRazaoSocial.getText().trim().toUpperCase());
            con.setTipoPessoa(jComTipoPessoa.getSelectedItem().toString().substring(0, 1));
            con.setTelefone(jForTelefone.getText());
            con.setEmail(jTexEmail.getText().trim());
            con.setObs(jTextAreaObservacao.getText());
            con.setCdProposta(modcon.getCdProposta());
            dat.setData(data);
            data = dat.getData();
            ContatoDAO condao = null;
            try {
                condao = new ContatoDAO(conexao);
                if ("N".equals(oper)) {
                    CBuscarSequencia bs = new CBuscarSequencia(su, datasource, 8);
                    con.setCdAtendimento(bs.getRetorno());
                    con.setDataAtendimento(data);
                    con.setHoraAtendimento(hs.getHora());
                    con.setUsuarioCadastro(su.getUsuarioConectado());
                    con.setDataCadastro(data);
                    con.setHoraCadastro(hs.getHora());
                    con.setSituacao("NI");
                    condao.adicionar(con);
                } else {
                    con.setCdAtendimento(cdAtendimento);
                    switch (modcon.getSituacao().toString().substring(0, 2)) {
                        case ("Ag"):
                            con.setSituacao("AA");
                            break;
                        case ("Pr"):
                            con.setSituacao("PG");
                            break;
                        default:
                            con.setSituacao("NI");
                            break;
                    }
                    con.setUsuarioModificacao(su.getUsuarioConectado());
                    con.setDataModificacao(data);
                    con.setHoraModificacao(hs.getHora());
                    condao.atualizar(con);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ManterContato.class.getName()).log(Level.SEVERE, null, ex);
            }
            modcon.setAtualizacao(false);
            oper = "";
            limparTela();
            bloquearCampos();
            setaInicio();
        }
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
        jPanBotoes = new javax.swing.JPanel();
        jForDataCadastro = new javax.swing.JFormattedTextField();
        jLabDataModificacao = new javax.swing.JLabel();
        jForDataModificacao = new javax.swing.JFormattedTextField();
        jTexRegAtual = new javax.swing.JTextField();
        jTexRegTotal = new javax.swing.JTextField();
        jLabReg = new javax.swing.JLabel();
        jLabCadastradoPor = new javax.swing.JLabel();
        jTexCadastradoPor = new javax.swing.JTextField();
        jForHoraModif = new javax.swing.JFormattedTextField();
        jForHoraCad = new javax.swing.JFormattedTextField();
        jTexModificadoPor = new javax.swing.JTextField();
        jPanContato = new javax.swing.JPanel();
        jLabTelefone = new javax.swing.JLabel();
        jForTelefone = new javax.swing.JFormattedTextField();
        jLabEmail = new javax.swing.JLabel();
        jTexEmail = new javax.swing.JTextField();
        jLabNomeCliente = new javax.swing.JLabel();
        jTexNomeRazaoSocial = new javax.swing.JTextField();
        jLabTipoPessoa = new javax.swing.JLabel();
        jComTipoPessoa = new javax.swing.JComboBox<>();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaObservacao = new javax.swing.JTextArea();
        jPanAtendimento = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTabAtendimento = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabData = new javax.swing.JTable();
        jCalendar1 = new com.toedter.calendar.JCalendar();
        jPanePesquisar1 = new javax.swing.JPanel();
        jLabPesquisar1 = new javax.swing.JLabel();
        jTexPesquisar1 = new javax.swing.JTextField();
        jButAtendimento = new javax.swing.JButton();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Contato");

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
        jButCancelar.setEnabled(false);
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
        jButExcluir.setEnabled(false);
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

        jPanBotoes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jForDataCadastro.setEditable(false);
        jForDataCadastro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataCadastro.setEnabled(false);

        jLabDataModificacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDataModificacao.setText("Modificado:");

        jForDataModificacao.setEditable(false);
        jForDataModificacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataModificacao.setEnabled(false);

        jTexRegAtual.setEditable(false);
        jTexRegAtual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexRegAtual.setEnabled(false);

        jTexRegTotal.setEditable(false);
        jTexRegTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexRegTotal.setEnabled(false);

        jLabReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabReg.setText("\\");

            jLabCadastradoPor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCadastradoPor.setText("Cadastrado:");

            jTexCadastradoPor.setEditable(false);
            jTexCadastradoPor.setEnabled(false);

            jForHoraModif.setEditable(false);
            try {
                jForHoraModif.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForHoraModif.setEnabled(false);

            jForHoraCad.setEditable(false);
            try {
                jForHoraCad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForHoraCad.setEnabled(false);

            jTexModificadoPor.setEditable(false);
            jTexModificadoPor.setEnabled(false);

            javax.swing.GroupLayout jPanBotoesLayout = new javax.swing.GroupLayout(jPanBotoes);
            jPanBotoes.setLayout(jPanBotoesLayout);
            jPanBotoesLayout.setHorizontalGroup(
                jPanBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanBotoesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTexRegAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabReg, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexRegTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabCadastradoPor)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForHoraCad, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabDataModificacao)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexModificadoPor, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForHoraModif, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            jPanBotoesLayout.setVerticalGroup(
                jPanBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanBotoesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabDataModificacao)
                        .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexRegAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexRegTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabReg)
                        .addComponent(jLabCadastradoPor)
                        .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jForHoraModif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jForHoraCad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexModificadoPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jPanContato.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Contato", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jLabTelefone.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTelefone.setText("Telefone:");

            try {
                jForTelefone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) *####-####")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }

            jLabEmail.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabEmail.setText("email:");

            jLabNomeCliente.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabNomeCliente.setText("Nome / Razão Social:");

            jLabTipoPessoa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTipoPessoa.setText("Tipo Pessoa:");

            jComTipoPessoa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Física", "Jurídica" }));
            jComTipoPessoa.setMaximumSize(new java.awt.Dimension(70, 40));
            jComTipoPessoa.setMinimumSize(new java.awt.Dimension(70, 20));
            jComTipoPessoa.setPreferredSize(new java.awt.Dimension(70, 20));

            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.MatteBorder(null), "Informações Complementares", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 14))); // NOI18N

            jTextAreaObservacao.setColumns(20);
            jTextAreaObservacao.setRows(5);
            jScrollPane1.setViewportView(jTextAreaObservacao);

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
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout jPanContatoLayout = new javax.swing.GroupLayout(jPanContato);
            jPanContato.setLayout(jPanContatoLayout);
            jPanContatoLayout.setHorizontalGroup(
                jPanContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanContatoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanContatoLayout.createSequentialGroup()
                            .addGroup(jPanContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanContatoLayout.createSequentialGroup()
                                    .addComponent(jLabNomeCliente)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexNomeRazaoSocial)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabTipoPessoa)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComTipoPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addContainerGap())
                        .addGroup(jPanContatoLayout.createSequentialGroup()
                            .addComponent(jLabTelefone)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabEmail)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))))
            );
            jPanContatoLayout.setVerticalGroup(
                jPanContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanContatoLayout.createSequentialGroup()
                    .addGroup(jPanContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabNomeCliente)
                        .addComponent(jTexNomeRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabTipoPessoa)
                        .addComponent(jComTipoPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabTelefone)
                        .addComponent(jForTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabEmail)
                        .addComponent(jTexEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jPanAtendimento.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Atendimentos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 18))); // NOI18N

            jTabAtendimento.setBorder(new javax.swing.border.MatteBorder(null));
            jTabAtendimento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabAtendimento.setModel(new javax.swing.table.DefaultTableModel(
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
                    "Atendimento", "Vistoria", "Proposta", "Situação"
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
            jScrollPane3.setViewportView(jTabAtendimento);
            if (jTabAtendimento.getColumnModel().getColumnCount() > 0) {
                jTabAtendimento.getColumnModel().getColumn(0).setResizable(false);
                jTabAtendimento.getColumnModel().getColumn(0).setPreferredWidth(15);
                jTabAtendimento.getColumnModel().getColumn(1).setResizable(false);
                jTabAtendimento.getColumnModel().getColumn(1).setPreferredWidth(10);
                jTabAtendimento.getColumnModel().getColumn(2).setResizable(false);
                jTabAtendimento.getColumnModel().getColumn(2).setPreferredWidth(10);
                jTabAtendimento.getColumnModel().getColumn(3).setResizable(false);
                jTabAtendimento.getColumnModel().getColumn(3).setPreferredWidth(90);
            }

            jTabData.setBorder(new javax.swing.border.MatteBorder(null));
            jTabData.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabData.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null},
                    {null}
                },
                new String [] {
                    "Data"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class
                };
                boolean[] canEdit = new boolean [] {
                    false
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            jScrollPane2.setViewportView(jTabData);
            if (jTabData.getColumnModel().getColumnCount() > 0) {
                jTabData.getColumnModel().getColumn(0).setResizable(false);
                jTabData.getColumnModel().getColumn(0).setPreferredWidth(30);
            }

            javax.swing.GroupLayout jPanAtendimentoLayout = new javax.swing.GroupLayout(jPanAtendimento);
            jPanAtendimento.setLayout(jPanAtendimentoLayout);
            jPanAtendimentoLayout.setHorizontalGroup(
                jPanAtendimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanAtendimentoLayout.createSequentialGroup()
                    .addGap(1, 1, 1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            jPanAtendimentoLayout.setVerticalGroup(
                jPanAtendimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanAtendimentoLayout.createSequentialGroup()
                    .addGroup(jPanAtendimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 169, Short.MAX_VALUE)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGap(0, 0, Short.MAX_VALUE))
            );

            jCalendar1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

            jPanePesquisar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabPesquisar1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabPesquisar1.setText("Pesquisar Clientes:");

            jTexPesquisar1.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    jTexPesquisar1KeyReleased(evt);
                }
            });

            javax.swing.GroupLayout jPanePesquisar1Layout = new javax.swing.GroupLayout(jPanePesquisar1);
            jPanePesquisar1.setLayout(jPanePesquisar1Layout);
            jPanePesquisar1Layout.setHorizontalGroup(
                jPanePesquisar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanePesquisar1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanePesquisar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanePesquisar1Layout.createSequentialGroup()
                            .addComponent(jLabPesquisar1)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addComponent(jTexPesquisar1))
                    .addContainerGap())
            );
            jPanePesquisar1Layout.setVerticalGroup(
                jPanePesquisar1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanePesquisar1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabPesquisar1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexPesquisar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jButAtendimento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButAtendimento.setText("Atendimento");
            jButAtendimento.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButAtendimentoActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanSecundarioLayout = new javax.swing.GroupLayout(jPanSecundario);
            jPanSecundario.setLayout(jPanSecundarioLayout);
            jPanSecundarioLayout.setHorizontalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanePesquisar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanContato, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanBotoes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                            .addComponent(jPanAtendimento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jButAtendimento)
                                .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanSecundarioLayout.setVerticalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                            .addComponent(jCalendar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButAtendimento)))
                    .addGap(4, 4, 4)
                    .addComponent(jPanePesquisar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanContato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, 824, Short.MAX_VALUE)
                .addComponent(jPanSecundario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                    .addComponent(jPanSecundario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        // TODO add your handling code here:
        salvarContato();
        controleBotoes(ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO, ISBOTAO);
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:
        jButEditar.setEnabled(true);
        if (jTexNomeRazaoSocial.getText().isEmpty()) {
            sql = "SELECT * FROM GCVCLIENTES";
        } else {
            sql = "SELECT * FROM GCVCLIENTES WHERE NOME_RAZAOSOCIAL LIKE '" + jTexNomeRazaoSocial.getText().trim() + "'";
        }
        bloquearCampos();
        controleBotoes(ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO, ISBOTAO);
        // pesquisar();

    }//GEN-LAST:event_jButPesquisarActionPerformed

    private void jButProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButProximoActionPerformed
        // TODO add your handling code here:
        idxCorr += 1;
        //upRegistros();
    }//GEN-LAST:event_jButProximoActionPerformed

    private void jButAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAnteriorActionPerformed
        // TODO add your handling code here:
        idxCorr -= 1;
        //upRegistros();
    }//GEN-LAST:event_jButAnteriorActionPerformed

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
        // TODO add your handling code here:
        limparTela();
        jButPesquisarActionPerformed(evt);
        oper = "N";         // se cancelar a ação atual na tela do sistema a operação do sistema será N  de novo Registro
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        // TODO add your handling code here:
        liberarCampos();
        controleBotoes(!ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO);
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
        controleBotoes(!ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO);
        oper = "N";
    }//GEN-LAST:event_jButNovoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        jButNovoActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        // TODO add your handling code here:
        if (!cdAtendimento.isEmpty()) {
            try {
                Contato cc = new Contato();
                cc.setCdAtendimento(cdAtendimento);
                ContatoDAO ccDAO = new ContatoDAO(conexao);
                ccDAO.excluir(cc);
                limparTela();
                setaInicio();
                jButPesquisarActionPerformed(evt);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Não foi possível excluir o registro da tela");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral na exclusão do registro!");
            }
        }
    }//GEN-LAST:event_jButExcluirActionPerformed

    private void jTexPesquisar1KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexPesquisar1KeyReleased
        if (!jTexPesquisar1.getText().isEmpty()) {
            sqldat = "select c.data_atendimento as Data"
                    + " from gcvcontato as c"
                    + " WHERE nome_cliente LIKE '%"
                    + jTexPesquisar1.getText().trim()
                    + "%'";
            //indexAtualData = 0;
            buscarTodos();
            jTabData.setModel(dcodat);
            if (jTabData.getModel().getRowCount() > 0) {
                ajustarTabelaData();
            }
        } else {
            setaInicio();
        }
    }//GEN-LAST:event_jTexPesquisar1KeyReleased

    private void jButAtendimentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAtendimentoActionPerformed
        // TODO add your handling code here:
        manterAtendimento();
        if (modcon.isAtualizacao()) {
            oper = "A";
            salvarContato();
        }
    }//GEN-LAST:event_jButAtendimentoActionPerformed

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
            java.util.logging.Logger.getLogger(ManterContato.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterContato.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterContato.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterContato.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterContato(su, conexao, pg).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButAnterior;
    private javax.swing.JButton jButAtendimento;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButEditar;
    private javax.swing.JButton jButExcluir;
    private javax.swing.JButton jButNovo;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JButton jButProximo;
    private javax.swing.JButton jButSair;
    private javax.swing.JButton jButSalvar;
    private com.toedter.calendar.JCalendar jCalendar1;
    private javax.swing.JComboBox<String> jComTipoPessoa;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JFormattedTextField jForHoraCad;
    private javax.swing.JFormattedTextField jForHoraModif;
    private javax.swing.JFormattedTextField jForTelefone;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabEmail;
    private javax.swing.JLabel jLabNomeCliente;
    private javax.swing.JLabel jLabPesquisar1;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabTelefone;
    private javax.swing.JLabel jLabTipoPessoa;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JPanel jPanAtendimento;
    private javax.swing.JPanel jPanBotoes;
    private javax.swing.JPanel jPanContato;
    private javax.swing.JPanel jPanSecundario;
    private javax.swing.JPanel jPanePesquisar1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTabAtendimento;
    private javax.swing.JTable jTabData;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexEmail;
    private javax.swing.JTextField jTexModificadoPor;
    private javax.swing.JTextField jTexNomeRazaoSocial;
    private javax.swing.JTextField jTexPesquisar1;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JTextArea jTextAreaObservacao;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
