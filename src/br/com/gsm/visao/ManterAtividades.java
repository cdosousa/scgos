/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GSMCA0040
 */
package br.com.gsm.visao;

import br.com.controle.CBuscarSequencia;
import br.com.gsm.controle.*;
import br.com.gsm.dao.*;
import br.com.gsm.modelo.*;
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.DefineCampoInteiro;
import br.com.modelo.FormatarValor;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 11/10/2017
 */
public class ManterAtividades extends javax.swing.JFrame {

    private Atividades regCorr;
    private List< Atividades> resultado;
    private CAtividades patv;
    private Atividades modatv;
    private VerificarTecla vt;
    private TarefasAtividade ta;
    private CTarefasAtividades cta;
    private TarefasAtividadesDAO tadao;
    private EquipamentosAtividade ea;
    private CEquipamentosAtividade cea;
    private EquipamentosAtividadeDAO eadao;
    private int numReg = 0;
    private int idxCorr = 0;
    private String sql;
    private String sqlta;
    private String sqlea;
    private String oper;
    private static SessaoUsuario su;
    private static Connection conexao;
    private NumberFormat formato;
    private final String TABELA = "gsmatividades";
    private int indexAtualTarefas = 0;
    private int indexAtualEquipamentos = 0;
    private String cdTarefa;
    private String cdEquipamento;
    private int sequenciaTarefa;
    private int sequenciaEquipamento;

    public ManterAtividades(SessaoUsuario su, Connection conexao) {
        formato = NumberFormat.getInstance();
        this.su = su;
        this.conexao = conexao;
        oper = "N";
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formatarCampos();
        limparTela();
        setLocationRelativeTo(null);
        mudarLinhaTarefas();
        mudarLinhaEquipamentos();
        objTarefasAtividades();
        objEquipamentosAtividades();
        this.dispose();
    }

    // construtor padrão
    public ManterAtividades() {

    }

    // método para setar formato do campo
    private void formatarCampos() {
        jForValTarefas.setDocument(new DefineCampoDecimal());
        jForValEquipamentos.setDocument(new DefineCampoDecimal());
        jForValAtividades.setDocument(new DefineCampoDecimal());
        jForCdAtividade.setDocument(new DefineCampoInteiro());
    }

    // instancia objetos tarefasAtividades
    private void objTarefasAtividades() {
        ta = new TarefasAtividade();
        cta = new CTarefasAtividades(conexao);
    }

    // instancia objetos equiapamentosAtividades
    private void objEquipamentosAtividades() {
        ea = new EquipamentosAtividade();
        cea = new CEquipamentosAtividade(conexao);
    }

    // método para limpar tela
    private void limparTela() {
        jForCdAtividade.setText("");
        jTexNomeAtividade.setText("");
        jForValTarefas.setText("0,00");
        jForValEquipamentos.setText("0,00");
        jForValAtividades.setText("0,00");
        jComSituacao.setSelectedIndex(0);
        jTexCadastradoPor.setText("");
        jForDataCadastro.setText("");
        jForDataModificacao.setText("");
        jTexRegAtual.setText("");
        jTexRegTotal.setText("");
        idxCorr = 0;
        numReg = 0;
        resultado = null;
        regCorr = null;
        liberarCampos();
        jForCdAtividade.setEditable(true);
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {
        patv = new CAtividades(conexao);
        modatv = new Atividades();
        try {
            numReg = patv.pesquisar(sql);
            idxCorr = +1;
            if (numReg > 0) {
                upRegistros();
                jButTarefas.setEnabled(true);
                jButEquipamentos.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(null, "Nenhum registro encontrado!");
                jButTarefas.setEnabled(false);
                jButEquipamentos.setEnabled(false);
            }
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Argumento inválido na busca\nErr: " + iae.getCause());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Pesquisar os Registro\nErr: " + ex);
        }
    }

    // atualizar registros na tela
    private void upRegistros() {
        jTexRegTotal.setText(Integer.toString(numReg));
        jTexRegAtual.setText(Integer.toString(idxCorr));
        patv.mostrarPesquisa(modatv, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jForCdAtividade.setText(modatv.getCdAtividade());
        jTexNomeAtividade.setText(modatv.getNomeAtividade());
        jComSituacao.setSelectedIndex(Integer.parseInt(modatv.getSituacao()));
        jForValTarefas.setText(String.valueOf(modatv.getValTotalTarefa()).trim());
        jForValEquipamentos.setText(String.valueOf(modatv.getValTotalEquipamento()).trim());
        jForValAtividades.setText(String.valueOf(modatv.getValTotalAtividade()).trim());
        jTexCadastradoPor.setText(modatv.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modatv.getDataCadastro())));
        if (modatv.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(modatv.getDataModificacao())));
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
        mostrarTarefas();
        mostrarEquipamentos();
    }

    //Bloquear os campos da tela
    private void bloquearCampos() {
        jForCdAtividade.setEditable(false);
        jTexNomeAtividade.setEditable(false);
        jComSituacao.setEditable(false);
        jForValTarefas.setEditable(false);
        jForValEquipamentos.setEditable(false);
        jForValAtividades.setEditable(false);
    }

    //Loberar os campos da tela para atualização
    private void liberarCampos() {
        jTexNomeAtividade.setEditable(true);
        jComSituacao.setEditable(true);
        jForValTarefas.setEditable(true);
        jForValEquipamentos.setEditable(true);
        jForValAtividades.setEditable(true);
    }

    // método para cria novo registro
    private void novoRegistro() {
        jButEditar.setEnabled(false);
        limparTela();
        liberarCampos();
        esvaziarTabelas();
        jForCdAtividade.setEditable(true);
        jForCdAtividade.requestFocus();
    }
    // método para limpar tabelas

    private void esvaziarTabelas() {
        jTabTarefas.setModel(new JTable().getModel());
        jTabEquipamentos.setModel(new JTable().getModel());
    }
    
    // método para salvar registro
    private void salvarRegistro(){
        if (jTexNomeAtividade.getText().isEmpty()
                || jComSituacao.getSelectedItem().toString().substring(0, 1).toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Os campos Descrição e Situação são obrigatórios.");
        } else {
            DataSistema dat = new DataSistema();
            Atividades atv = new Atividades();
            String data = null;
            atv.setNomeAtividade(jTexNomeAtividade.getText().trim().toUpperCase());
            atv.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1));
            try {
                atv.setValTotalTarefa(formato.parse(jForValTarefas.getText()).doubleValue());
                atv.setValTotalEquipamento(formato.parse(jForValEquipamentos.getText()).doubleValue());
                atv.setValTotalAtividade(formato.parse(jForValAtividades.getText()).doubleValue());
            } catch (ParseException ex) {
                Logger.getLogger(ManterAtividades.class.getName()).log(Level.SEVERE, null, ex);
            }
            dat.setData(data);
            data = dat.getData();
            atv.setDataCadastro(data);
            atv.setUsuarioCadastro(su.getUsuarioConectado());
            AtividadesDAO atvdao = null;
            CAtividades catv = new CAtividades(conexao);
            try {
                atvdao = new AtividadesDAO(conexao);
                if ("N".equals(oper)) {
                    //gera código do produto
                    CBuscarSequencia cb = new CBuscarSequencia(su, TABELA,8);
                    String sequencia = cb.getRetorno();
                    catv.gerarCodigo(atv, sequencia);
                    sql = "SELECT * FROM GSMATIVIDADES WHERE CD_ATIVIDADE = '" + atv.getCdAtividade().trim() + "'";
                    atvdao.adicionar(atv);
                } else {
                    sql = "SELECT * FROM GSMATIVIDADES WHERE CD_ATIVIDADE = '" + modatv.getCdAtividade() + "'";
                    atv.setCdAtividade(modatv.getCdAtividade());
                    atv.setDataModificacao(data);
                    atvdao.atualizar(atv);
                }
            } catch (SQLException sqlex) {
                JOptionPane.showMessageDialog(null, "Erro criar o produto no banco e dados!\nErr: " + sqlex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral para criar o produto!\nErr: " + ex);
            }
            limparTela();
            bloquearCampos();
            pesquisar();
        }
    }

    /**
     * SESSÃO PARA BUSCAR E ATUALIZAR A TAREFAS DA ATIVIDADE
     */
    // buscando tabela de tarefas
    private void mostrarTarefas() {
        int numLinhas;
        sqlta = "select ta.sequencia as 'Seq.', "
                + "t.nome_tarefa as Descrição, "
                + "e.nome_especialidade as Especialidade, "
                + "format(ta.valor_unit,2,'de_DE') as Valor, "
                + "ta.situacao as Situação "
                + "from gsmtarefaatividade as ta "
                + "left outer join gsmtarefas as t on ta.cd_tarefa = t.cd_tarefa "
                + "left outer join gsmespecialidades as e on ta.cd_especialidade = e.cd_especialidade "
                + "where ta.cd_atividade = " + modatv.getCdAtividade()
                + " order by ta.sequencia";
        buscarTodasTarefas();
        jTabTarefas.setModel(tadao);
        numLinhas = jTabTarefas.getModel().getRowCount();
        if (numLinhas > 0) {
            sequenciaTarefa = Integer.parseInt(String.format("%s", jTabTarefas.getValueAt(--numLinhas, 0)));
        } else {
            sequenciaTarefa = 0;
        }
        ajustarTabelaTarefas();
    }

    // criando um listener quando mudar de linha
    private void mudarLinhaTarefas() {
        jTabTarefas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtualTarefas = jTabTarefas.getSelectedRow();
                ajustarTabelaTarefas();
            }
        });

        jTabTarefas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                vt = new VerificarTecla();
                if ("BAIXO".equals(vt.VerificarTecla(e).toUpperCase())) {
                    incluirTarefa();
                }
            }
        });
    }

    //Metodos para buscar tarefas da atividas
    public void buscarTodasTarefas() {
        try {
            tadao = new TarefasAtividadesDAO(conexao);
            tadao.setQuery(sqlta);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela tarefas
    public void ajustarTabelaTarefas() {
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
        if (indexAtualTarefas < 0) {
            indexAtualTarefas = 0;
            cdTarefa = String.format("%s", jTabTarefas.getValueAt(indexAtualTarefas, 0));
        }
    }

    // incluir tarefa
    private void incluirTarefa() {
        ManterTarefaAtividade mta = new ManterTarefaAtividade(new JFrame(), true, modatv, sequenciaTarefa, su, conexao);
        mta.setVisible(true);
        mostrarTarefas();
        jForValAtividades.setText(String.valueOf(modatv.getValTotalAtividade()));
        jForValTarefas.setText(String.valueOf(modatv.getValTotalTarefa()));
        if (modatv.isAtualizacao()) {
            oper = "A";
            salvarRegistro();
            
        }
    }

    /**
     * SESSÃO PARA BUSCAR E ATUALIZAR OS EQUIPAMENTOS DA ATIVIDADE
     */
    // Método para mostrar equipamentos
    private void mostrarEquipamentos() {
        int numLinhas;
        sqlea = "select ea.sequencial as 'Seq.', "
                + "e.nome_equipamento as Descrição, "
                + "format(ea.valor_unit,2,'de_DE')as Valor, "
                + "ea.situacao as Situação "
                + "from gsmequipamentoatividade as ea "
                + "left outer join gsmequipamentos as e on ea.cd_equipamento = e.cd_equipamento "
                + "where ea.cd_atividade = " + modatv.getCdAtividade()
                + " order by ea.sequencial";
        buscarTodosEquipamentos();
        jTabEquipamentos.setModel(eadao);
        numLinhas = jTabEquipamentos.getModel().getRowCount();
        if (numLinhas > 0) {
            sequenciaEquipamento = Integer.parseInt(String.format("%s", jTabEquipamentos.getValueAt(--numLinhas, 0)));
        } else {
            sequenciaEquipamento = 0;
        }
        ajustarTabelaEquipamentos();
    }

    // criando um listener quando mudar de linha
    private void mudarLinhaEquipamentos() {
        jTabEquipamentos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtualEquipamentos = jTabEquipamentos.getSelectedRow();
                ajustarTabelaEquipamentos();
            }
        });

        jTabEquipamentos.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                vt = new VerificarTecla();
                if ("BAIXO".equals(vt.VerificarTecla(e).toUpperCase())) {
                    incluirEquipamento();
                }
            }
        });
    }

    //Metodos para buscar tarefas da atividas
    public void buscarTodosEquipamentos() {
        try {
            eadao = new EquipamentosAtividadeDAO(conexao);
            eadao.setQuery(sqlea);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela tarefas
    public void ajustarTabelaEquipamentos() {
        jTabEquipamentos.getColumnModel().getColumn(0).setMinWidth(30);
        jTabEquipamentos.getColumnModel().getColumn(0).setPreferredWidth(30);
        jTabEquipamentos.getColumnModel().getColumn(1).setMinWidth(200);
        jTabEquipamentos.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTabEquipamentos.getColumnModel().getColumn(2).setMinWidth(60);
        jTabEquipamentos.getColumnModel().getColumn(2).setPreferredWidth(60);
        jTabEquipamentos.getColumnModel().getColumn(3).setMinWidth(60);
        jTabEquipamentos.getColumnModel().getColumn(3).setPreferredWidth(60);
        if (indexAtualEquipamentos < 0) {
            indexAtualEquipamentos = 0;
            cdEquipamento = String.format("%s", jTabEquipamentos.getValueAt(indexAtualEquipamentos, 0));
        }
    }

    // incluir Equipamento
    private void incluirEquipamento() {
        ManterEquipamentoAtividade mea = new ManterEquipamentoAtividade(new JFrame(), true, modatv,
                sequenciaEquipamento, su, conexao);
        mea.setVisible(true);
        mostrarEquipamentos();
        jForValAtividades.setText(String.valueOf(modatv.getValTotalAtividade()));
        jForValEquipamentos.setText(String.valueOf(modatv.getValTotalEquipamento()));
        if (modatv.isAtualizacao()) {
            oper = "A";
            salvarRegistro();
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
        jButImprimir = new javax.swing.JButton();
        jButSair = new javax.swing.JButton();
        jPanPrincipal = new javax.swing.JPanel();
        jPanGeral = new javax.swing.JPanel();
        jLabCdAtividade = new javax.swing.JLabel();
        jForCdAtividade = new javax.swing.JFormattedTextField();
        jLabNomeAtividade = new javax.swing.JLabel();
        jTexNomeAtividade = new javax.swing.JTextField();
        jLabSituacao = new javax.swing.JLabel();
        jComSituacao = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
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
        jPanTarefas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabTarefas = new javax.swing.JTable();
        jPanBotoesTarefa = new javax.swing.JPanel();
        jButTarefas = new javax.swing.JButton();
        jPanEquipamentos = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabEquipamentos = new javax.swing.JTable();
        jPanBotoesEquipamentos = new javax.swing.JPanel();
        jButEquipamentos = new javax.swing.JButton();
        jPanTotais = new javax.swing.JPanel();
        jLabValorTarefas = new javax.swing.JLabel();
        jLabValorEquipamentos = new javax.swing.JLabel();
        jLabValorTotalAtividades = new javax.swing.JLabel();
        jForValTarefas = new FormatarValor((FormatarValor.MOEDA));
        jForValEquipamentos = new FormatarValor((FormatarValor.MOEDA));
        jForValAtividades = new FormatarValor((FormatarValor.MOEDA));
        jSeparator4 = new javax.swing.JSeparator();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuNovo = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Atividades");

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
        jButEditar.setEnabled(false);
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
        jButProximo.setText("Proximo");
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

        jPanPrincipal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanGeral.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanGeral.setToolTipText("");

        jLabCdAtividade.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdAtividade.setText("Código:");

        jForCdAtividade.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("########"))));

        jLabNomeAtividade.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNomeAtividade.setText("Descrição:");

        jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabSituacao.setText("Situação:");

        jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ativo", "Inativo", "Pendente", "Obsoleto" }));

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

            jPanTarefas.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Tarefas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

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
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jTabTarefas.setColumnSelectionAllowed(true);
            jScrollPane1.setViewportView(jTabTarefas);
            jTabTarefas.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
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

            jPanBotoesTarefa.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jButTarefas.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButTarefas.setText("Tarefas");
            jButTarefas.setEnabled(false);
            jButTarefas.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButTarefasActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanBotoesTarefaLayout = new javax.swing.GroupLayout(jPanBotoesTarefa);
            jPanBotoesTarefa.setLayout(jPanBotoesTarefaLayout);
            jPanBotoesTarefaLayout.setHorizontalGroup(
                jPanBotoesTarefaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanBotoesTarefaLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButTarefas)
                    .addContainerGap())
            );
            jPanBotoesTarefaLayout.setVerticalGroup(
                jPanBotoesTarefaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanBotoesTarefaLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButTarefas)
                    .addContainerGap())
            );

            javax.swing.GroupLayout jPanTarefasLayout = new javax.swing.GroupLayout(jPanTarefas);
            jPanTarefas.setLayout(jPanTarefasLayout);
            jPanTarefasLayout.setHorizontalGroup(
                jPanTarefasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanBotoesTarefa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanTarefasLayout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );
            jPanTarefasLayout.setVerticalGroup(
                jPanTarefasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTarefasLayout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanBotoesTarefa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jPanEquipamentos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Equipamentos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jTabEquipamentos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabEquipamentos.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
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
            jScrollPane2.setViewportView(jTabEquipamentos);
            if (jTabEquipamentos.getColumnModel().getColumnCount() > 0) {
                jTabEquipamentos.getColumnModel().getColumn(0).setResizable(false);
                jTabEquipamentos.getColumnModel().getColumn(0).setPreferredWidth(30);
                jTabEquipamentos.getColumnModel().getColumn(1).setResizable(false);
                jTabEquipamentos.getColumnModel().getColumn(1).setPreferredWidth(200);
                jTabEquipamentos.getColumnModel().getColumn(2).setResizable(false);
                jTabEquipamentos.getColumnModel().getColumn(2).setPreferredWidth(60);
                jTabEquipamentos.getColumnModel().getColumn(3).setResizable(false);
                jTabEquipamentos.getColumnModel().getColumn(3).setPreferredWidth(60);
            }

            jPanBotoesEquipamentos.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jButEquipamentos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButEquipamentos.setText("Equipamentos");
            jButEquipamentos.setEnabled(false);
            jButEquipamentos.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButEquipamentosActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanBotoesEquipamentosLayout = new javax.swing.GroupLayout(jPanBotoesEquipamentos);
            jPanBotoesEquipamentos.setLayout(jPanBotoesEquipamentosLayout);
            jPanBotoesEquipamentosLayout.setHorizontalGroup(
                jPanBotoesEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanBotoesEquipamentosLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButEquipamentos)
                    .addContainerGap())
            );
            jPanBotoesEquipamentosLayout.setVerticalGroup(
                jPanBotoesEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanBotoesEquipamentosLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jButEquipamentos)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout jPanEquipamentosLayout = new javax.swing.GroupLayout(jPanEquipamentos);
            jPanEquipamentos.setLayout(jPanEquipamentosLayout);
            jPanEquipamentosLayout.setHorizontalGroup(
                jPanEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addComponent(jPanBotoesEquipamentos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            jPanEquipamentosLayout.setVerticalGroup(
                jPanEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanEquipamentosLayout.createSequentialGroup()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanBotoesEquipamentos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jPanTotais.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabValorTarefas.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorTarefas.setText("Valor Tarefas:");

            jLabValorEquipamentos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorEquipamentos.setText("Valor Equipamentos:");

            jLabValorTotalAtividades.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorTotalAtividades.setText("Valor Total de Atividades:");

            javax.swing.GroupLayout jPanTotaisLayout = new javax.swing.GroupLayout(jPanTotais);
            jPanTotais.setLayout(jPanTotaisLayout);
            jPanTotaisLayout.setHorizontalGroup(
                jPanTotaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTotaisLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanTotaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 258, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanTotaisLayout.createSequentialGroup()
                            .addGroup(jPanTotaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabValorTotalAtividades, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabValorEquipamentos, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabValorTarefas, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanTotaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jForValEquipamentos, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                .addComponent(jForValAtividades, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                .addComponent(jForValTarefas, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE))))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanTotaisLayout.setVerticalGroup(
                jPanTotaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTotaisLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanTotaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabValorTarefas)
                        .addComponent(jForValTarefas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanTotaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabValorEquipamentos)
                        .addComponent(jForValEquipamentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(10, 10, 10)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanTotaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabValorTotalAtividades)
                        .addComponent(jForValAtividades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(52, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout jPanGeralLayout = new javax.swing.GroupLayout(jPanGeral);
            jPanGeral.setLayout(jPanGeralLayout);
            jPanGeralLayout.setHorizontalGroup(
                jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanGeralLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanRodape, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanTotais, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanGeralLayout.createSequentialGroup()
                            .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanGeralLayout.createSequentialGroup()
                                    .addComponent(jLabCdAtividade)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForCdAtividade, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabNomeAtividade)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexNomeAtividade, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(33, 33, 33)
                                    .addComponent(jLabSituacao)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 799, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(41, 151, Short.MAX_VALUE))
                        .addGroup(jPanGeralLayout.createSequentialGroup()
                            .addComponent(jPanTarefas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanEquipamentos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addContainerGap())
            );
            jPanGeralLayout.setVerticalGroup(
                jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanGeralLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jComSituacao, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabCdAtividade)
                            .addComponent(jForCdAtividade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabNomeAtividade)
                            .addComponent(jTexNomeAtividade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabSituacao)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanTarefas, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanEquipamentos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanTotais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            javax.swing.GroupLayout jPanPrincipalLayout = new javax.swing.GroupLayout(jPanPrincipal);
            jPanPrincipal.setLayout(jPanPrincipalLayout);
            jPanPrincipalLayout.setHorizontalGroup(
                jPanPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanPrincipalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanGeral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanPrincipalLayout.setVerticalGroup(
                jPanPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanPrincipalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanGeral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jMenuBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jMenu1.setText("Arquivo");

            jMenuNovo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
            jMenuNovo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jMenuNovo.setText("Novo");
            jMenuNovo.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuNovoActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuNovo);

            jMenuItemSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemSalvar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jMenuItemSalvar.setText("Salvar");
            jMenuItemSalvar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemSalvarActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItemSalvar);

            jMenuItemSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemSair.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jMenuItemSair.setText("Sair");
            jMenuItemSair.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemSairActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItemSair);

            jMenuBar1.add(jMenu1);

            jMenu2.setText("Editar");
            jMenu2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenu2ActionPerformed(evt);
                }
            });

            jMenuItemEditar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemEditar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jMenuItemEditar.setText("Editar");
            jMenu2.add(jMenuItemEditar);

            jMenuBar1.add(jMenu2);

            setJMenuBar(jMenuBar1);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        // TODO add your handling code here:
        salvarRegistro();
        
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:
        jButEditar.setEnabled(true);
        jButExcluir.setEnabled(true);
        if (jTexNomeAtividade.getText().isEmpty()) {
            sql = "SELECT * FROM GSMATIVIDADES";
        } else {
            sql = "SELECT * FROM GSMATIVIDADES WHERE NOME_ATIVIDADE LIKE '%" + jTexNomeAtividade.getText().trim() + "%'";
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
        esvaziarTabelas();
        oper = "N";
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        // TODO add your handling code here:
        liberarCampos();
        jTexNomeAtividade.requestFocus();
        oper = "A";

    }//GEN-LAST:event_jButEditarActionPerformed

    private void jButNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButNovoActionPerformed
        // TODO add your handling code here:
        novoRegistro();
        oper = "N";
    }//GEN-LAST:event_jButNovoActionPerformed

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jMenuNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNovoActionPerformed
        // TODO add your handling code here:
        jButNovoActionPerformed(evt);
    }//GEN-LAST:event_jMenuNovoActionPerformed

    private void jMenuItemSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalvarActionPerformed
        // TODO add your handling code here:
        jButSalvarActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemSalvarActionPerformed

    private void jMenuItemSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSairActionPerformed
        // TODO add your handling code here:
        jButSairActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemSairActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
        jButEditarActionPerformed(evt);
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        // TODO add your handling code here:
        if (!jForCdAtividade.getText().isEmpty()) {
            try {
                Atividades at = new Atividades();
                at.setCdAtividade(jForCdAtividade.getText().toUpperCase());
                AtividadesDAO atDAO = new AtividadesDAO(conexao);
                atDAO.excluir(at);
                limparTela();
                jButPesquisarActionPerformed(evt);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Não foi possível excluir o registro da tela");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral na exclusão do registro!");
            }
        }
    }//GEN-LAST:event_jButExcluirActionPerformed

    private void jButImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButImprimirActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_jButImprimirActionPerformed

    private void jButTarefasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButTarefasActionPerformed
        // TODO add your handling code here:
        incluirTarefa();
    }//GEN-LAST:event_jButTarefasActionPerformed

    private void jButEquipamentosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEquipamentosActionPerformed
        // TODO add your handling code here:
        incluirEquipamento();
    }//GEN-LAST:event_jButEquipamentosActionPerformed

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
            java.util.logging.Logger.getLogger(ManterAtividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterAtividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterAtividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterAtividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterAtividades(su, conexao).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButAnterior;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButEditar;
    private javax.swing.JButton jButEquipamentos;
    private javax.swing.JButton jButExcluir;
    private javax.swing.JButton jButImprimir;
    private javax.swing.JButton jButNovo;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JButton jButProximo;
    private javax.swing.JButton jButSair;
    private javax.swing.JButton jButSalvar;
    private javax.swing.JButton jButTarefas;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JFormattedTextField jForCdAtividade;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JFormattedTextField jForValAtividades;
    private javax.swing.JFormattedTextField jForValEquipamentos;
    private javax.swing.JFormattedTextField jForValTarefas;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabCdAtividade;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabNomeAtividade;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabValorEquipamentos;
    private javax.swing.JLabel jLabValorTarefas;
    private javax.swing.JLabel jLabValorTotalAtividades;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JMenuItem jMenuNovo;
    private javax.swing.JPanel jPanBotoesEquipamentos;
    private javax.swing.JPanel jPanBotoesTarefa;
    private javax.swing.JPanel jPanEquipamentos;
    private javax.swing.JPanel jPanGeral;
    private javax.swing.JPanel jPanPrincipal;
    private javax.swing.JPanel jPanRodape;
    private javax.swing.JPanel jPanTarefas;
    private javax.swing.JPanel jPanTotais;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JTable jTabEquipamentos;
    private javax.swing.JTable jTabTarefas;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexNomeAtividade;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
