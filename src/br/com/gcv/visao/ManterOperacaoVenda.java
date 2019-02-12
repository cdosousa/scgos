/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GCVCA0010
 */
package br.com.gcv.visao;

import br.com.gcv.controle.COperacaoVenda;
import br.com.modelo.DataSistema;
import br.com.modelo.SessaoUsuario;
import br.com.gcv.dao.OperacaoVendaDAO;
import br.com.gcv.modelo.OperacaoVenda;
import br.com.gfr.visao.PesquisarCentroCustos;
import br.com.gfr.visao.PesquisarTiposOperacoes;
import br.com.modelo.VerificarTecla;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 10/11/2017
 */
public class ManterOperacaoVenda extends javax.swing.JFrame {

    private static Connection conexao;
    private OperacaoVenda regCorr;
    private List< OperacaoVenda> resultado;
    private COperacaoVenda cove;
    private OperacaoVenda modove;
    private VerificarTecla vt;
    private NumberFormat formato;
    private int numReg;
    private int idxCorr;
    private String sql;
    private String oper;
    private static SessaoUsuario su;

    /**
     * Creates new form ManterProdutos
     */
    public ManterOperacaoVenda(SessaoUsuario su, Connection conexao) {
        this.su = su;
        this.conexao = conexao;
        formato = NumberFormat.getInstance();
        oper = "N";
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        this.dispose();
    }

    // construturo padrão
    public ManterOperacaoVenda() {

    }

    // método para limpar tela
    private void limparTela() {
        jForCdOperacao.setText("");
        jTexNomeOperacao.setText("");
        jForCdTipoOperacao.setText("");
        jTexNomeTipoOperacao.setText("");
        jComTipoFinalidade.setSelectedIndex(0);
        jCheEmiteNfeVenda.setSelected(false);
        jCheEmiteNfeServico.setSelected(false);
        jCheGeraCobranca.setSelected(false);
        jTexCdContrato.setText("");
        jTexCdCCusto.setText("");
        jTexNomeCCusto.setText("");
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
        jForCdTipoOperacao.setEditable(true);
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {
        cove = new COperacaoVenda(conexao);
        modove = new OperacaoVenda();
        try {
            numReg = cove.pesquisar(sql);
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

    private void upRegistros() {
        jTexRegTotal.setText(Integer.toString(numReg));
        jTexRegAtual.setText(Integer.toString(idxCorr));
        cove.mostrarPesquisa(modove, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jForCdOperacao.setText(modove.getCdOperacaoVenda().trim());
        jTexNomeOperacao.setText(modove.getNomeOperacaoVenda().trim().toUpperCase());
        jForCdTipoOperacao.setText(modove.getCdTipoOper());
        jTexNomeTipoOperacao.setText(modove.getNomeTipoOperacao());
        jComTipoFinalidade.setSelectedIndex(Integer.parseInt(modove.getTipoFinalidade()));
        if("S".equals(modove.getEmiteNfeVenda()))
            jCheEmiteNfeVenda.setSelected(true);
        else
            jCheEmiteNfeVenda.setSelected(false);
        if("S".equals(modove.getEmiteNfeServico()))
            jCheEmiteNfeServico.setSelected(true);
        else
            jCheEmiteNfeServico.setSelected(false);
        if("S".equals(modove.getGeraCobranca()))
            jCheGeraCobranca.setSelected(true);
        else
            jCheGeraCobranca.setSelected(false);
        jTexCdContrato.setText(modove.getCdContrato());
        jTexCdCCusto.setText(modove.getCdCCusto());
        jTexNomeCCusto.setText(modove.getNomeCCusto());
        jTexCadastradoPor.setText(modove.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modove.getDataCadastro())));
        if (modove.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(modove.getDataModificacao())));
        }
        jComSituacao.setSelectedIndex(Integer.parseInt(String.valueOf(modove.getSituacao())));

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

    //Bloquear os campos da tela
    private void bloquearCampos() {
        jForCdOperacao.setEditable(false);
        jTexNomeOperacao.setEditable(false);
        jForCdTipoOperacao.setEditable(false);
        jTexNomeTipoOperacao.setEditable(false);
        jComTipoFinalidade.setEditable(false);
        jCheEmiteNfeVenda.setEnabled(false);
        jCheEmiteNfeServico.setEnabled(false);
        jCheGeraCobranca.setEnabled(false);
        jTexCdContrato.setEditable(false);
        jComSituacao.setEditable(false);
    }

    //Loberar os campos da tela para atualização
    private void liberarCampos() {
        jTexNomeTipoOperacao.setEditable(true);
        jForCdOperacao.setEditable(true);
        jTexNomeOperacao.setEditable(true);
        jComTipoFinalidade.setEditable(true);
        jCheEmiteNfeVenda.setEnabled(true);
        jCheEmiteNfeServico.setEnabled(true);
        jCheGeraCobranca.setEnabled(true);
        jTexCdContrato.setEditable(true);
        jComSituacao.setEditable(true);
    }

    private void novoRegistro() {
        limparTela();
        liberarCampos();
        jForCdOperacao.setEditable(true);
        jForCdOperacao.requestFocus();
    }
    
    // metodo para dar zoon no campo Tipo de Operacao
    private void zoomTipoOperacao() {
        PesquisarTiposOperacoes zoom = new PesquisarTiposOperacoes(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jForCdTipoOperacao.setText(zoom.getcdTipoOper());
        jTexNomeTipoOperacao.setText(zoom.getNomeTipoOper().trim());
    }
    
    /**
     * 
     */
    private void zoomCCusto(){
        PesquisarCentroCustos zoom = new PesquisarCentroCustos(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdCCusto.setText(zoom.getSelec1());
        jTexNomeCCusto.setText(zoom.getSelec2());
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
        jLabDataCadastro = new javax.swing.JLabel();
        jForDataCadastro = new javax.swing.JFormattedTextField();
        jLabDataModificacao = new javax.swing.JLabel();
        jForDataModificacao = new javax.swing.JFormattedTextField();
        jTexRegAtual = new javax.swing.JTextField();
        jTexRegTotal = new javax.swing.JTextField();
        jLabReg = new javax.swing.JLabel();
        jLabCadastradoPor = new javax.swing.JLabel();
        jTexCadastradoPor = new javax.swing.JTextField();
        jLabOperacaVenda = new javax.swing.JLabel();
        jLabSituacao = new javax.swing.JLabel();
        jForCdOperacao = new javax.swing.JFormattedTextField();
        jLabTipoOperacao = new javax.swing.JLabel();
        jForCdTipoOperacao = new javax.swing.JFormattedTextField();
        jLabFinalidade = new javax.swing.JLabel();
        jComTipoFinalidade = new javax.swing.JComboBox<>();
        jTexNomeTipoOperacao = new javax.swing.JTextField();
        jTexNomeOperacao = new javax.swing.JTextField();
        jComSituacao = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jCheEmiteNfeVenda = new javax.swing.JCheckBox();
        jCheEmiteNfeServico = new javax.swing.JCheckBox();
        jCheGeraCobranca = new javax.swing.JCheckBox();
        jSeparator2 = new javax.swing.JSeparator();
        jLabCdContrato = new javax.swing.JLabel();
        jTexCdContrato = new javax.swing.JTextField();
        jLabCdCCusto = new javax.swing.JLabel();
        jTexCdCCusto = new javax.swing.JTextField();
        jTexNomeCCusto = new javax.swing.JTextField();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Operação de Venda");

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

        jPanBotoes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        jTexRegAtual.setEditable(false);
        jTexRegAtual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexRegAtual.setEnabled(false);

        jTexRegTotal.setEditable(false);
        jTexRegTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexRegTotal.setEnabled(false);

        jLabReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabReg.setText("\\");

            jLabCadastradoPor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCadastradoPor.setText("Cadastrado por:");

            jTexCadastradoPor.setEditable(false);
            jTexCadastradoPor.setEnabled(false);

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
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabCadastradoPor)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabDataCadastro)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabDataModificacao)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            jPanBotoesLayout.setVerticalGroup(
                jPanBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanBotoesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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

            jLabOperacaVenda.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabOperacaVenda.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabOperacaVenda.setText("Código:");

            jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabSituacao.setText("Situação:");

            try {
                jForCdOperacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }

            jLabTipoOperacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTipoOperacao.setText("Tp. Oper.:");

            try {
                jForCdTipoOperacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#.###**")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForCdTipoOperacao.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jForCdTipoOperacaoKeyPressed(evt);
                }
            });

            jLabFinalidade.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabFinalidade.setText("Finalidade:");

            jComTipoFinalidade.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "GA-Garantia", "RV-Revenda", "RS-Revenda / Serviço", "SE-Serviço", "VE-Venda" }));

            jTexNomeTipoOperacao.setEditable(false);
            jTexNomeTipoOperacao.setEnabled(false);

            jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ativo", "Inativo" }));

            jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel1.setText("Descrição:");

            jCheEmiteNfeVenda.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jCheEmiteNfeVenda.setText("Emitir NF Venda");

            jCheEmiteNfeServico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jCheEmiteNfeServico.setText("Emitir NF Serviço");

            jCheGeraCobranca.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jCheGeraCobranca.setText("Gerar Cobrança");

            jLabCdContrato.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdContrato.setText("Contrato Padrão:");

            jLabCdCCusto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdCCusto.setText("C.Custo:");

            jTexCdCCusto.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCdCCustoKeyPressed(evt);
                }
            });

            jTexNomeCCusto.setEditable(false);
            jTexNomeCCusto.setEnabled(false);

            javax.swing.GroupLayout jPanSecundarioLayout = new javax.swing.GroupLayout(jPanSecundario);
            jPanSecundario.setLayout(jPanSecundarioLayout);
            jPanSecundarioLayout.setHorizontalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanBotoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanSecundarioLayout.createSequentialGroup()
                                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabOperacaVenda))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTexNomeOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jForCdOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabSituacao)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                            .addComponent(jLabFinalidade)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jCheEmiteNfeVenda)
                                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                    .addComponent(jComTipoFinalidade, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabTipoOperacao)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jForCdTipoOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexNomeTipoOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, 335, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanSecundarioLayout.createSequentialGroup()
                                            .addComponent(jCheGeraCobranca)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabCdCCusto))
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanSecundarioLayout.createSequentialGroup()
                                            .addComponent(jCheEmiteNfeServico)
                                            .addGap(74, 74, 74)
                                            .addComponent(jLabCdContrato)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                            .addComponent(jTexCdContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 259, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                            .addComponent(jTexCdCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTexNomeCCusto)))))))
                    .addContainerGap())
            );
            jPanSecundarioLayout.setVerticalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabOperacaVenda)
                        .addComponent(jForCdOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabSituacao)
                        .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jTexNomeOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabTipoOperacao, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jForCdTipoOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexNomeTipoOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabFinalidade)
                            .addComponent(jComTipoFinalidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(7, 7, 7)
                    .addComponent(jCheEmiteNfeVenda)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jCheEmiteNfeServico)
                        .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabCdContrato)
                            .addComponent(jTexCdContrato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jCheGeraCobranca)
                        .addComponent(jLabCdCCusto)
                        .addComponent(jTexCdCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(9, 9, 9))
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
                .addComponent(jPanSecundario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
        if (jForCdTipoOperacao.getText().isEmpty() || jTexNomeOperacao.getText().isEmpty() || jForCdOperacao.getText().isEmpty()
                || jComTipoFinalidade.getSelectedItem().toString().substring(0, 1) == " " || jComSituacao.getSelectedItem().toString().substring(0, 1) == " ") {
            JOptionPane.showMessageDialog(null, "Os campos Operação, Descrição, Tipo Operação, Finalidade e Situacao precisam ser preenchidos corretamente!");
        } else {
            DataSistema dat = new DataSistema();
            OperacaoVenda ove = new OperacaoVenda();
            String data = null;
            ove.setCdOperacaoVenda(jForCdOperacao.getText());
            ove.setNomeOperacaoVenda(jTexNomeOperacao.getText().trim().toLowerCase());
            ove.setCdTipoOper(jForCdTipoOperacao.getText());
            ove.setNomeTipoOperacao(jTexNomeTipoOperacao.getText().trim().toUpperCase());
            ove.setTipoFinalidade(jComTipoFinalidade.getSelectedItem().toString().substring(0, 2).toUpperCase());
            if(jCheEmiteNfeVenda.isSelected())
                ove.setEmiteNfeVenda("S");
            else
                ove.setEmiteNfeVenda("N");
            if(jCheEmiteNfeServico.isSelected())
                ove.setEmiteNfeServico("S");
            else
                ove.setEmiteNfeServico("N");
            if(jCheGeraCobranca.isSelected())
                ove.setGeraCobranca("S");
            else
                ove.setGeraCobranca("N");
            ove.setCdContrato(jTexCdContrato.getText());
            ove.setCdCCusto(jTexCdCCusto.getText());
            ove.setUsuarioCadastro(su.getUsuarioConectado());
            dat.setData(data);
            data = dat.getData();
            ove.setDataCadastro(data);
            ove.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1));
            OperacaoVendaDAO ovedao = null;
            sql = "SELECT * FROM GCVOPERVENDA WHERE CD_OPERACAO = '" + jForCdOperacao.getText().trim()
                    + "'";
            try {
                ovedao = new OperacaoVendaDAO(conexao);
                if ("N".equals(oper)) {
                    ovedao.adicionar(ove);
                } else {
                    ove.setDataModificacao(data);
                    ovedao.atualizar(ove);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ManterOperacaoVenda.class.getName()).log(Level.SEVERE, null, ex);
            }
            limparTela();
            bloquearCampos();
            pesquisar();
        }
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:
        if (jTexNomeOperacao.getText().isEmpty()) {
            sql = "SELECT * FROM GCVOPERVENDA";
        } else {
            sql = "SELECT * FROM GCVOPERVENDA WHERE NOME_OPERACAO LIKE '" + jTexNomeOperacao.getText().trim() + "'";
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
        jTexNomeTipoOperacao.requestFocus();
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
        if (!jForCdTipoOperacao.getText().isEmpty()) {
            try {
                OperacaoVenda cc = new OperacaoVenda();
                cc.setCdOperacaoVenda(jForCdOperacao.getText().trim());
                OperacaoVendaDAO ccDAO = new OperacaoVendaDAO(conexao);
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

    private void jForCdTipoOperacaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jForCdTipoOperacaoKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomTipoOperacao();
        }
    }//GEN-LAST:event_jForCdTipoOperacaoKeyPressed

    private void jTexCdCCustoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdCCustoKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if("F5".equals(vt.VerificarTecla(evt).toUpperCase())){
            zoomCCusto();
        }
        
    }//GEN-LAST:event_jTexCdCCustoKeyPressed

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
            java.util.logging.Logger.getLogger(ManterOperacaoVenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterOperacaoVenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterOperacaoVenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterOperacaoVenda.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterOperacaoVenda(su, conexao).setVisible(true);
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
    private javax.swing.JCheckBox jCheEmiteNfeServico;
    private javax.swing.JCheckBox jCheEmiteNfeVenda;
    private javax.swing.JCheckBox jCheGeraCobranca;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JComboBox<String> jComTipoFinalidade;
    private javax.swing.JFormattedTextField jForCdOperacao;
    private javax.swing.JFormattedTextField jForCdTipoOperacao;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabCdCCusto;
    private javax.swing.JLabel jLabCdContrato;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabFinalidade;
    private javax.swing.JLabel jLabOperacaVenda;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabTipoOperacao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JPanel jPanBotoes;
    private javax.swing.JPanel jPanSecundario;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexCdCCusto;
    private javax.swing.JTextField jTexCdContrato;
    private javax.swing.JTextField jTexNomeCCusto;
    private javax.swing.JTextField jTexNomeOperacao;
    private javax.swing.JTextField jTexNomeTipoOperacao;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
