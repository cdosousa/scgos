/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GSMCA0060
 */
package br.com.gsm.visao;

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
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;
import javax.swing.JOptionPane;
import java.text.NumberFormat;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 11/10/2017
 */
public class ManterEquipes extends javax.swing.JFrame {

    // objetos do registro pai
    private Equipes regCorr;
    private List< Equipes> resultado;
    private CEquipes ceqp;
    private Equipes modeqp;
    // objetos do registro filho
    private TecnicosEquipe te;
    private CTecnicosEquipe cte;
    private TecnicosEquipeDAO tedao;
    // objetos de ambiente
    private static SessaoUsuario su;
    private NumberFormat formato;
    private VerificarTecla vt;
    // variáveis de instância
    private static Connection conexao;
    private int numReg = 0;
    private int idxCorr = 0;
    private String sql;
    private String sqlte;
    private String oper;
    private int indexAtualTecnicos = 0;
    private String cdEquipe;
    private String cpfTecnico;
    private int numLinhasTecnico = 0;

    public ManterEquipes(SessaoUsuario su, Connection conexao) {
        formato = NumberFormat.getInstance();
        this.su = su;
        this.conexao = conexao;
        oper = "N";
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formatarCampos();
        limparTela();
        setLocationRelativeTo(null);
        mudarLinhaTecnicos();
        objTecnicosEquipe();
        this.dispose();
    }

    // construtor padrão
    public ManterEquipes() {

    }

    // método para setar formato do campo
    private void formatarCampos() {
        jForPercObra.setDocument(new DefineCampoDecimal());
        jForValSobrObra.setDocument(new DefineCampoDecimal());
        jForPercIndicacao.setDocument(new DefineCampoDecimal());
        jForValorIndicacao.setDocument(new DefineCampoDecimal());
        jForPercComissao.setDocument(new DefineCampoDecimal());
        jForCdEquipe.setDocument(new DefineCampoInteiro());
    }

    // instancia objetos TecnicosEquipes
    private void objTecnicosEquipe() {
        te = new TecnicosEquipe();
        cte = new CTecnicosEquipe(conexao);
    }

    // método para limpar tela
    private void limparTela() {
        jForCdEquipe.setText("");
        jTexNomeEquipe.setText("");
        jForPercObra.setText("0,00");
        jForValSobrObra.setText("0,00");
        jForPercIndicacao.setText("0,00");
        jForValorIndicacao.setText("0,00");
        jForPercComissao.setText("0,00");
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
        jForCdEquipe.setEditable(true);
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {
        ceqp = new CEquipes();
        modeqp = new Equipes();
        try {
            numReg = ceqp.pesquisar(sql);
            idxCorr = +1;
            if (numReg > 0) {
                upRegistros();
                jButTecnicos.setEnabled(true);
            } else {
                JOptionPane.showMessageDialog(null, "Nenhum registro encontrado!");
                jButTecnicos.setEnabled(false);
            }
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Argumento inválido na busca\nErr: " + iae.getCause());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Pesquisar os Registro\nErr: " + ex);
        }
        jButEditar.setEnabled(true);
        jButExcluir.setEnabled(true);
    }

    // atualizar registros na tela
    private void upRegistros() {
        jTexRegTotal.setText(Integer.toString(numReg));
        jTexRegAtual.setText(Integer.toString(idxCorr));
        ceqp.mostrarPesquisa(modeqp, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jForCdEquipe.setText(modeqp.getCdEquipe());
        cdEquipe = modeqp.getCdEquipe();
        jTexNomeEquipe.setText(modeqp.getNomeEquipe());
        jComSituacao.setSelectedIndex(Integer.parseInt(String.valueOf(modeqp.getSituacao())));
        jTexCadastradoPor.setText(modeqp.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modeqp.getDataCadastro())));
        if (modeqp.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(modeqp.getDataModificacao())));
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
        mostrarTecnicos();
    }

    //Bloquear os campos da tela
    private void bloquearCampos() {
        jForCdEquipe.setEditable(false);
        jTexNomeEquipe.setEditable(false);
        jComSituacao.setEditable(false);
        jForPercObra.setEditable(false);
        jForValSobrObra.setEditable(false);
        jForPercIndicacao.setEditable(false);
        jForValorIndicacao.setEditable(false);
        jForPercComissao.setEditable(false);
    }

    //Loberar os campos da tela para atualização
    private void liberarCampos() {
        jTexNomeEquipe.setEditable(true);
        jComSituacao.setEditable(true);
        jForPercObra.setEditable(true);
        jForValSobrObra.setEditable(true);
        jForPercIndicacao.setEditable(true);
        jForValorIndicacao.setEditable(true);
        jForPercComissao.setEditable(true);
    }

    // método para cria novo registro
    private void novoRegistro() {
        jButEditar.setEnabled(false);
        limparTela();
        liberarCampos();
        esvaziarTabelas();
        jForCdEquipe.setEditable(true);
        jForCdEquipe.requestFocus();
    }
    // método para limpar tabelas

    private void esvaziarTabelas() {
        jTabTecnicos.setModel(new JTable().getModel());
    }

    // método para salvar registro
    private void salvarRegistro() {
        if (jTexNomeEquipe.getText().isEmpty()
                || jComSituacao.getSelectedItem().toString().substring(0, 1).toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Os campos Descrição e Situação são obrigatórios.");
        } else {
            DataSistema dat = new DataSistema();
            Equipes eqp = new Equipes();
            String data = null;
            eqp.setCdEquipe(jForCdEquipe.getText());
            eqp.setNomeEquipe(jTexNomeEquipe.getText().trim().toUpperCase());
            eqp.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1).toString().charAt(0));
            dat.setData(data);
            data = dat.getData();
            eqp.setDataCadastro(data);
            eqp.setUsuarioCadastro(su.getUsuarioConectado());
            EquipesDAO eqpdao = null;
            CEquipes ceqp = new CEquipes();
            try {
                eqpdao = new EquipesDAO();
                if ("N".equals(oper)) {
                    //gera código do produto
                    sql = "SELECT * FROM GSMEQUIPE WHERE CD_EQUIPE = '" + eqp.getCdEquipe().trim() + "'";
                    eqpdao.adicionar(eqp);
                } else {
                    sql = "SELECT * FROM GSMEQUIPE WHERE CD_EQUIPE = '" + modeqp.getCdEquipe() + "'";
                    eqp.setCdEquipe(modeqp.getCdEquipe());
                    eqp.setDataModificacao(data);
                    eqpdao.atualizar(eqp);
                }
            } catch (SQLException sqlex) {
                JOptionPane.showMessageDialog(null, "Erro criar a equipe no banco e dados!\nErr: " + sqlex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral para criar a equipe!\nErr: " + ex);
            }
            limparTela();
            bloquearCampos();
            pesquisar();
        }
    }

    /**
     * SESSÃO PARA BUSCAR E ATUALIZAR OS TECNICOS
     */
    // buscando tabela de tecnicos
    private void mostrarTecnicos() {
        int numLinhas;
        sqlte = "select te.cpf_tecnico as 'C.P..F',"
                + "t.nome_tecnico as Nome,"
                + "e.nome_especialidade as Especialidade,"
                + "te.pagar_obra as 'Pgto. Obra',"
                + "te.pagar_indicacao as 'Pgto. Indic',"
                + "te.pagar_comissao as 'Pgto. Comissão',"
                + "te.situacao as Sit "
                + "from gsmtecnicoequipe te "
                + "left outer join gsmtecnicos as t on te.cpf_tecnico = t.cpf "
                + "left outer join gsmespecialidades as e on te.cd_especialidade = e.cd_especialidade "
                + "where te.cd_equipe = '" + modeqp.getCdEquipe()
                + "' order by te.cpf_tecnico";
        buscarTodosTecnicos();
        jTabTecnicos.setModel(tedao);
        numLinhas = jTabTecnicos.getModel().getRowCount();
        numLinhasTecnico = numLinhas;
        ajustarTabelaTecnicos();
    }

    // criando um listener quando mudar de linha
    private void mudarLinhaTecnicos() {
        jTabTecnicos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtualTecnicos = jTabTecnicos.getSelectedRow();
                ajustarTabelaTecnicos();
            }
        });

        jTabTecnicos.addKeyListener(new KeyListener() {
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
                    incluirTecnicos();
                }
            }
        });
    }

    //Metodos para buscar tecnicos da equipe
    public void buscarTodosTecnicos() {
        try {
            tedao = new TecnicosEquipeDAO();
            tedao.setQuery(sqlte);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela tarefas
    public void ajustarTabelaTecnicos() {
        jTabTecnicos.getColumnModel().getColumn(0).setMinWidth(90);
        jTabTecnicos.getColumnModel().getColumn(0).setPreferredWidth(90);
        jTabTecnicos.getColumnModel().getColumn(1).setMinWidth(200);
        jTabTecnicos.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTabTecnicos.getColumnModel().getColumn(2).setMinWidth(150);
        jTabTecnicos.getColumnModel().getColumn(2).setPreferredWidth(150);
        jTabTecnicos.getColumnModel().getColumn(3).setMinWidth(30);
        jTabTecnicos.getColumnModel().getColumn(3).setPreferredWidth(30);
        jTabTecnicos.getColumnModel().getColumn(4).setMinWidth(30);
        jTabTecnicos.getColumnModel().getColumn(4).setPreferredWidth(30);
        jTabTecnicos.getColumnModel().getColumn(5).setMinWidth(50);
        jTabTecnicos.getColumnModel().getColumn(5).setPreferredWidth(50);
        jTabTecnicos.getColumnModel().getColumn(6).setMinWidth(10);
        jTabTecnicos.getColumnModel().getColumn(6).setPreferredWidth(10);
        if (indexAtualTecnicos < 0)
            indexAtualTecnicos = 0;
        cpfTecnico = String.format("%s", jTabTecnicos.getValueAt(indexAtualTecnicos, 0));
        buscarCorrelatos();
    }
    
    // buscar tabelas correlatos
    private void buscarCorrelatos() {
        if (!cdEquipe.isEmpty()) {
            String sql = "select * from gsmtecnicoequipe where cd_equipe = '"
                    + cdEquipe
                    + "' and cpf_tecnico = '"
                    + cpfTecnico
                    + "'";
            try {
                cte.pesquisar(sql);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Erro na pesquisa do Técnico!");
            }
            cte.mostrarPesquisa(te, 0);
            atualizarTela();
        }
    }
    
    // Atualizar registros correlatos
    private void atualizarTela() {
        jForPercObra.setText(String.valueOf(te.getPercObra()));
        jForValSobrObra.setText(String.valueOf(te.getValorObra()));
        jForPercIndicacao.setText(String.valueOf(te.getPercIndicacao()));
        jForValorIndicacao.setText(String.valueOf(te.getValorIndicacao()));
        jForPercComissao.setText(String.valueOf(te.getPercComissao()));
    }
    

    // incluir tarefa
    private void incluirTecnicos() {
        ManterTecnicosEquipe mte = new ManterTecnicosEquipe(new JFrame(), true, modeqp, su, conexao);
        mte.setVisible(true);
        mostrarTecnicos();
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
        jLabCdEquipe = new javax.swing.JLabel();
        jForCdEquipe = new javax.swing.JFormattedTextField();
        jLabNomeEquipe = new javax.swing.JLabel();
        jTexNomeEquipe = new javax.swing.JTextField();
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
        jPanTecnicos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabTecnicos = new javax.swing.JTable();
        jPanBotoesTarefa = new javax.swing.JPanel();
        jButTecnicos = new javax.swing.JButton();
        jPanParametroObra = new javax.swing.JPanel();
        jLabPercObra = new javax.swing.JLabel();
        jLabValorEquipamentos = new javax.swing.JLabel();
        jLabPercIndicacao = new javax.swing.JLabel();
        jForPercObra = new FormatarValor((FormatarValor.PORCENTAGEM));
        jForValSobrObra = new FormatarValor((FormatarValor.MOEDA));
        jForPercIndicacao = new FormatarValor((FormatarValor.PORCENTAGEM));
        jLabValorIndicacao = new javax.swing.JLabel();
        jForValorIndicacao = new FormatarValor((FormatarValor.MOEDA));
        jLabPercComissao = new javax.swing.JLabel();
        jForPercComissao = new FormatarValor((FormatarValor.PORCENTAGEM));
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuNovo = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Equipes");

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

        jLabCdEquipe.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdEquipe.setText("Código:");

        jForCdEquipe.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("########"))));

        jLabNomeEquipe.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNomeEquipe.setText("Descrição:");

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

            jPanTecnicos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Tecnicos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jTabTecnicos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabTecnicos.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null}
                },
                new String [] {
                    "C.P.F", "Nome", "Especialidade", "Pgto. Obra", "Pgto. Indic.", "Pgto. Comissão", "Sit"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jTabTecnicos.setColumnSelectionAllowed(true);
            jScrollPane1.setViewportView(jTabTecnicos);
            jTabTecnicos.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            if (jTabTecnicos.getColumnModel().getColumnCount() > 0) {
                jTabTecnicos.getColumnModel().getColumn(0).setResizable(false);
                jTabTecnicos.getColumnModel().getColumn(0).setPreferredWidth(90);
                jTabTecnicos.getColumnModel().getColumn(1).setResizable(false);
                jTabTecnicos.getColumnModel().getColumn(1).setPreferredWidth(200);
                jTabTecnicos.getColumnModel().getColumn(2).setResizable(false);
                jTabTecnicos.getColumnModel().getColumn(2).setPreferredWidth(150);
                jTabTecnicos.getColumnModel().getColumn(3).setResizable(false);
                jTabTecnicos.getColumnModel().getColumn(3).setPreferredWidth(30);
                jTabTecnicos.getColumnModel().getColumn(4).setResizable(false);
                jTabTecnicos.getColumnModel().getColumn(4).setPreferredWidth(30);
                jTabTecnicos.getColumnModel().getColumn(5).setResizable(false);
                jTabTecnicos.getColumnModel().getColumn(5).setPreferredWidth(50);
                jTabTecnicos.getColumnModel().getColumn(6).setResizable(false);
                jTabTecnicos.getColumnModel().getColumn(6).setPreferredWidth(10);
            }

            jPanBotoesTarefa.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jButTecnicos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButTecnicos.setText("Técnicos");
            jButTecnicos.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButTecnicosActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanBotoesTarefaLayout = new javax.swing.GroupLayout(jPanBotoesTarefa);
            jPanBotoesTarefa.setLayout(jPanBotoesTarefaLayout);
            jPanBotoesTarefaLayout.setHorizontalGroup(
                jPanBotoesTarefaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanBotoesTarefaLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButTecnicos)
                    .addContainerGap())
            );
            jPanBotoesTarefaLayout.setVerticalGroup(
                jPanBotoesTarefaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanBotoesTarefaLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButTecnicos)
                    .addContainerGap())
            );

            javax.swing.GroupLayout jPanTecnicosLayout = new javax.swing.GroupLayout(jPanTecnicos);
            jPanTecnicos.setLayout(jPanTecnicosLayout);
            jPanTecnicosLayout.setHorizontalGroup(
                jPanTecnicosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jPanBotoesTarefa, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1)
            );
            jPanTecnicosLayout.setVerticalGroup(
                jPanTecnicosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTecnicosLayout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanBotoesTarefa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jPanParametroObra.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pagamento Obras", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jLabPercObra.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabPercObra.setText("% sobre obra:");

            jLabValorEquipamentos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorEquipamentos.setText("Valor sobre obra:");

            jLabPercIndicacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabPercIndicacao.setText("% indicação:");

            jLabValorIndicacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorIndicacao.setText("Valor Indicação:");

            jLabPercComissao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabPercComissao.setText("% Comissão:");

            javax.swing.GroupLayout jPanParametroObraLayout = new javax.swing.GroupLayout(jPanParametroObra);
            jPanParametroObra.setLayout(jPanParametroObraLayout);
            jPanParametroObraLayout.setHorizontalGroup(
                jPanParametroObraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanParametroObraLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanParametroObraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanParametroObraLayout.createSequentialGroup()
                            .addComponent(jLabValorEquipamentos)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForValSobrObra))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanParametroObraLayout.createSequentialGroup()
                            .addGap(17, 17, 17)
                            .addComponent(jLabPercObra)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForPercObra, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)))
                    .addGap(26, 26, 26)
                    .addGroup(jPanParametroObraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabPercIndicacao)
                        .addComponent(jLabValorIndicacao))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanParametroObraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanParametroObraLayout.createSequentialGroup()
                            .addComponent(jForPercIndicacao, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabPercComissao)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForPercComissao, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jForValorIndicacao, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanParametroObraLayout.setVerticalGroup(
                jPanParametroObraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanParametroObraLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanParametroObraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabPercObra)
                        .addComponent(jForPercObra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jForPercIndicacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabPercIndicacao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jForPercComissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabPercComissao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanParametroObraLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabValorEquipamentos)
                        .addComponent(jForValSobrObra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabValorIndicacao)
                        .addComponent(jForValorIndicacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout jPanGeralLayout = new javax.swing.GroupLayout(jPanGeral);
            jPanGeral.setLayout(jPanGeralLayout);
            jPanGeralLayout.setHorizontalGroup(
                jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanGeralLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanGeralLayout.createSequentialGroup()
                            .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanGeralLayout.createSequentialGroup()
                                    .addComponent(jLabCdEquipe)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForCdEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabNomeEquipe)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexNomeEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(182, 182, 182))
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 799, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabSituacao)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanRodape, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanParametroObra, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanTecnicos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanGeralLayout.setVerticalGroup(
                jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanGeralLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jComSituacao, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabCdEquipe)
                            .addComponent(jForCdEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabNomeEquipe)
                            .addComponent(jTexNomeEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabSituacao)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanTecnicos, javax.swing.GroupLayout.PREFERRED_SIZE, 234, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(7, 7, 7)
                    .addComponent(jPanParametroObra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addGap(0, 0, 0))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        // TODO add your handling code here:
        salvarRegistro();

    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:
        if (jTexNomeEquipe.getText().isEmpty()) {
            sql = "SELECT * FROM GSMEQUIPE";
        } else {
            sql = "SELECT * FROM GSMEQUIPE WHERE NOME_EQUIPE LIKE '%" + jTexNomeEquipe.getText().trim() + "%'";
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
        jTexNomeEquipe.requestFocus();
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
        if (!jForCdEquipe.getText().isEmpty()) {
            try {
                Equipes eq = new Equipes();
                eq.setCdEquipe(jForCdEquipe.getText().toUpperCase());
                EquipesDAO eqDAO = new EquipesDAO();
                eqDAO.excluir(eq);
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

    private void jButTecnicosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButTecnicosActionPerformed
        // TODO add your handling code here:
        incluirTecnicos();
    }//GEN-LAST:event_jButTecnicosActionPerformed

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
            java.util.logging.Logger.getLogger(ManterEquipes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterEquipes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterEquipes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterEquipes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterEquipes(su,conexao).setVisible(true);
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
    private javax.swing.JButton jButTecnicos;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JFormattedTextField jForCdEquipe;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JFormattedTextField jForPercComissao;
    private javax.swing.JFormattedTextField jForPercIndicacao;
    private javax.swing.JFormattedTextField jForPercObra;
    private javax.swing.JFormattedTextField jForValSobrObra;
    private javax.swing.JFormattedTextField jForValorIndicacao;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabCdEquipe;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabNomeEquipe;
    private javax.swing.JLabel jLabPercComissao;
    private javax.swing.JLabel jLabPercIndicacao;
    private javax.swing.JLabel jLabPercObra;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabValorEquipamentos;
    private javax.swing.JLabel jLabValorIndicacao;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JMenuItem jMenuNovo;
    private javax.swing.JPanel jPanBotoesTarefa;
    private javax.swing.JPanel jPanGeral;
    private javax.swing.JPanel jPanParametroObra;
    private javax.swing.JPanel jPanPrincipal;
    private javax.swing.JPanel jPanRodape;
    private javax.swing.JPanel jPanTecnicos;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTabTecnicos;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexNomeEquipe;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
