/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GFCCA0030
 */
package br.com.gfc.visao;

import br.com.gfc.controle.CContasBancarias;
import br.com.modelo.DataSistema;
import br.com.modelo.SessaoUsuario;
import br.com.gfc.dao.ContasBancariasDAO;
import br.com.gfc.modelo.ContasBancarias;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.FormatarValor;
import br.com.modelo.VerificarTecla;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 06/11/2017
 */
public class ManterContasBancarias extends javax.swing.JFrame {

    private static Connection conexao;
    private ContasBancarias regCorr;
    private List< ContasBancarias> resultado;
    private CContasBancarias ccb;
    private ContasBancarias modcb;
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
    public ManterContasBancarias(SessaoUsuario su, Connection conexao) {
        this.su = su;
        this.conexao = conexao;
        formato = NumberFormat.getInstance();
        oper = "N";
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formatarCampos();
        setLocationRelativeTo(null);
        this.dispose();
    }

    // construturo padrão
    public ManterContasBancarias() {

    }

    // método para setar formato do campo
    private void formatarCampos() {
        jForLimite.setDocument(new DefineCampoDecimal());
        jForLimite.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jForSaldo.setDocument(new DefineCampoDecimal());
        jForSaldo.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jForTarifaAdm.setDocument(new DefineCampoDecimal());
        jForTarifaAdm.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jForTxJuros.setDocument(new DefineCampoDecimal());
        jForTxJuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);

    }

    // método para limpar tela
    private void limparTela() {
        jForCdBanco.setText("");
        jTexNomeBanco.setText("");
        jForAgencia.setText("");
        jForAgenciaDigito.setText("");
        jComTipoConta.setSelectedIndex(0);
        jForConta.setText("");
        jForContaDigito.setText("");
        jForLimite.setText("0,00");
        jForSaldo.setText("0,00");
        jForTarifaAdm.setText("0,00");
        jForTxJuros.setText("0,00");
        jForDataAbertura.setText("");
        jForDataEncerramento.setText("");
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
        jForCdBanco.setEditable(true);
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {

        ccb = new CContasBancarias(conexao);
        modcb = new ContasBancarias();
        try {
            numReg = ccb.pesquisar(sql);
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
        ccb.mostrarPesquisa(modcb, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jForCdBanco.setText(modcb.getCdBanco().trim());
        jTexNomeBanco.setText(modcb.getNomeBanco().trim());
        jForAgencia.setText(modcb.getCdAgencia());
        jForAgenciaDigito.setText(modcb.getCdAgenciaDig());
        jComTipoConta.setSelectedItem(modcb.getTipoConta());
        jForConta.setText(modcb.getCdConta());
        jForContaDigito.setText(modcb.getCdContaDig());
        jForLimite.setText(String.valueOf(modcb.getLimite()));
        jForSaldo.setText(String.valueOf(modcb.getSaldo()));
        jForTarifaAdm.setText(String.valueOf(modcb.getTarifaAdm()));
        jForTxJuros.setText(String.valueOf(modcb.getTaxaJuros()));
        if (modcb.getDataAbertura() != null) {
            jForDataAbertura.setText(dat.getDataConv(Date.valueOf(modcb.getDataAbertura())));
        }
        if (modcb.getDataEncerrametno() != null) {
            jForDataEncerramento.setText(dat.getDataConv(Date.valueOf(modcb.getDataEncerrametno())));
        }
        jTexCadastradoPor.setText(modcb.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modcb.getDataCadastro())));
        if (modcb.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(modcb.getDataModificacao())));
        }
        jComSituacao.setSelectedIndex(Integer.parseInt(String.valueOf(modcb.getSituacao())));

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
        jForCdBanco.setEditable(false);
        jForAgencia.setEditable(false);
        jForAgenciaDigito.setEditable(false);
        jComTipoConta.setEditable(false);
        jForConta.setEditable(false);
        jForContaDigito.setEditable(false);
        jForLimite.setEditable(false);
        jForSaldo.setEditable(false);
        jForTarifaAdm.setEditable(false);
        jForTxJuros.setEditable(false);
        jForDataAbertura.setEditable(false);
        jForDataEncerramento.setEditable(false);
        jComSituacao.setEditable(false);
    }

    //Loberar os campos da tela para atualização
    private void liberarCampos() {
        jForAgenciaDigito.setEditable(true);
        jComTipoConta.setEditable(true);
        jForContaDigito.setEditable(true);
        jForLimite.setEditable(true);
        jForSaldo.setEditable(true);
        jForTarifaAdm.setEditable(true);
        jForTxJuros.setEditable(true);
        jForDataAbertura.setEditable(true);
        jForDataEncerramento.setEditable(true);
        jComSituacao.setEditable(true);
    }

    private void novoRegistro() {
        limparTela();
        liberarCampos();
        jForCdBanco.setEditable(true);
        jForAgencia.setEditable(true);
        jForConta.setEditable(true);
        jForCdBanco.requestFocus();
    }

    // metodo para dar zoon no campo UF
    private void zoomBanco() {
        PesquisarBancos zoom = new PesquisarBancos(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jForCdBanco.setText(zoom.getSelecao1().trim());
        jTexNomeBanco.setText(zoom.getSelecao2().trim());
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
        jLabNumBanco = new javax.swing.JLabel();
        jLabNomeBanco = new javax.swing.JLabel();
        jLabSituacao = new javax.swing.JLabel();
        jForCdBanco = new javax.swing.JFormattedTextField();
        jTexNomeBanco = new javax.swing.JTextField();
        jComSituacao = new javax.swing.JComboBox<>();
        jPanInformacoes = new javax.swing.JPanel();
        jLabLimite = new javax.swing.JLabel();
        jForLimite = new FormatarValor(FormatarValor.NUMERO)
        ;
        jLabSaldo = new javax.swing.JLabel();
        jForSaldo = new FormatarValor(FormatarValor.NUMERO);
        jLabTarifaAdm = new javax.swing.JLabel();
        jForTarifaAdm = new FormatarValor(FormatarValor.NUMERO);
        jLabTxJuros = new javax.swing.JLabel();
        jForTxJuros = new FormatarValor(FormatarValor.NUMERO);
        jLabDataAbertura = new javax.swing.JLabel();
        jForDataAbertura = new javax.swing.JFormattedTextField();
        jLabDataEncerramento = new javax.swing.JLabel();
        jForDataEncerramento = new javax.swing.JFormattedTextField();
        jLabAgencia = new javax.swing.JLabel();
        jForAgencia = new javax.swing.JFormattedTextField();
        jLabCelular = new javax.swing.JLabel();
        jForAgenciaDigito = new javax.swing.JFormattedTextField();
        jLabConta = new javax.swing.JLabel();
        jForConta = new javax.swing.JFormattedTextField();
        jLabDigito = new javax.swing.JLabel();
        jForContaDigito = new javax.swing.JFormattedTextField();
        jLabTipoConta = new javax.swing.JLabel();
        jComTipoConta = new javax.swing.JComboBox<>();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Contas Bancárias");

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
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 38, Short.MAX_VALUE)
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

            jLabNumBanco.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabNumBanco.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabNumBanco.setText("Núm. Banco:");

            jLabNomeBanco.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabNomeBanco.setText("Nome:");

            jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabSituacao.setText("Situação:");

            jForCdBanco.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
            jForCdBanco.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jForCdBancoKeyPressed(evt);
                }
            });

            jTexNomeBanco.setEditable(false);
            jTexNomeBanco.setEnabled(false);
            jTexNomeBanco.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jTexNomeBancoActionPerformed(evt);
                }
            });

            jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ativo", "Inativo" }));

            jPanInformacoes.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações da Conta", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jLabLimite.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabLimite.setText("Limite:");

            jForLimite.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

            jLabSaldo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabSaldo.setText("Saldo:");

            jForSaldo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

            jLabTarifaAdm.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTarifaAdm.setText("Tarifa Adm:.");

            jForTarifaAdm.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

            jLabTxJuros.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTxJuros.setText("Tx. Juros:");

            jForTxJuros.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

            jLabDataAbertura.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabDataAbertura.setText("Data Abertura:");

            try {
                jForDataAbertura.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }

            jLabDataEncerramento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabDataEncerramento.setText("Data Encerramento:");

            try {
                jForDataEncerramento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }

            javax.swing.GroupLayout jPanInformacoesLayout = new javax.swing.GroupLayout(jPanInformacoes);
            jPanInformacoes.setLayout(jPanInformacoesLayout);
            jPanInformacoesLayout.setHorizontalGroup(
                jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanInformacoesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanInformacoesLayout.createSequentialGroup()
                            .addComponent(jLabLimite)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForLimite, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabSaldo)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabTarifaAdm)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForTarifaAdm, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabTxJuros)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForTxJuros, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanInformacoesLayout.createSequentialGroup()
                            .addComponent(jLabDataAbertura)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForDataAbertura, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabDataEncerramento)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForDataEncerramento, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(62, Short.MAX_VALUE))
            );
            jPanInformacoesLayout.setVerticalGroup(
                jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanInformacoesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabLimite)
                        .addComponent(jForLimite, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabSaldo)
                        .addComponent(jForSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabTarifaAdm)
                        .addComponent(jForTarifaAdm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabTxJuros)
                        .addComponent(jForTxJuros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanInformacoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabDataAbertura)
                        .addComponent(jForDataAbertura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabDataEncerramento)
                        .addComponent(jForDataEncerramento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
            );

            jLabAgencia.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabAgencia.setText("Agência:");

            jForAgencia.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

            jLabCelular.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCelular.setText("Dígito:");

            jForAgenciaDigito.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

            jLabConta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabConta.setText("Conta:");

            jForConta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

            jLabDigito.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabDigito.setText("Dígito:");

            jForContaDigito.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

            jLabTipoConta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTipoConta.setText("Tipo Conta:");
            jLabTipoConta.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            jLabTipoConta.setAutoscrolls(true);

            jComTipoConta.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Aplicação", "Caixa", "Carteira", "Corrente", "Crédito", "Investimento", "Poupança" }));

            javax.swing.GroupLayout jPanSecundarioLayout = new javax.swing.GroupLayout(jPanSecundario);
            jPanSecundario.setLayout(jPanSecundarioLayout);
            jPanSecundarioLayout.setHorizontalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                            .addComponent(jPanBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                    .addComponent(jPanInformacoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 0, Short.MAX_VALUE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabNumBanco)
                                        .addComponent(jLabTipoConta))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                            .addComponent(jForCdBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabNomeBanco)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTexNomeBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabSituacao)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                            .addComponent(jComTipoConta, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addComponent(jLabAgencia)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jForAgencia, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabCelular)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jForAgenciaDigito, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabConta)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jForConta, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(19, 19, 19)
                                            .addComponent(jLabDigito)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jForContaDigito, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addContainerGap())))
            );
            jPanSecundarioLayout.setVerticalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabNumBanco)
                            .addComponent(jForCdBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabNomeBanco))
                        .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabSituacao))
                        .addComponent(jTexNomeBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabAgencia)
                            .addComponent(jForAgencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabCelular)
                            .addComponent(jForAgenciaDigito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabConta)
                            .addComponent(jForConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabDigito)
                            .addComponent(jForContaDigito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabTipoConta, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComTipoConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanInformacoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanSecundario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 771, Short.MAX_VALUE)
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
        if (jForCdBanco.getText().isEmpty() || jForAgencia.getText().isEmpty() || jForConta.getText().isEmpty()
                || jComSituacao.getSelectedItem().toString().substring(0, 1) == " ") {
            JOptionPane.showMessageDialog(null, "Os campos Num.Banco, Agencia, Conta e Situacao precisam ser preenchidos corretamente!");
        } else {
            DataSistema dat = new DataSistema();
            ContasBancarias cb = new ContasBancarias();
            String data = null;
            cb.setCdBanco(jForCdBanco.getText().trim());
            cb.setNomeBanco(jTexNomeBanco.getText().trim().toUpperCase());
            cb.setCdAgencia(jForAgencia.getText());
            cb.setCdAgenciaDig(jForAgenciaDigito.getText());
            cb.setTipoConta(jComTipoConta.getSelectedItem().toString().trim());
            cb.setCdConta(jForConta.getText());
            cb.setCdContaDig(jForContaDigito.getText());
            try {
                cb.setLimite(formato.parse(jForLimite.getText()).doubleValue());
                cb.setSaldo(formato.parse(jForSaldo.getText()).doubleValue());
                cb.setTarifaAdm(formato.parse(jForTarifaAdm.getText()).doubleValue());
                cb.setTaxaJuros(formato.parse(jForTxJuros.getText()).doubleValue());
            } catch (ParseException ex) {
                Logger.getLogger(ManterContasBancarias.class.getName()).log(Level.SEVERE, null, ex);
            }
            String dataAbertura = jForDataAbertura.getText();
            dataAbertura = dataAbertura.replace("/", "");
            if (!dataAbertura.trim().isEmpty()) {
                cb.setDataAbertura(dat.getDataConv(jForDataAbertura.getText()));
            }
            String dataEncerramento = jForDataEncerramento.getText();
            dataEncerramento = dataEncerramento.replace("/", "");
            if (!dataEncerramento.trim().isEmpty()) {
                cb.setDataEncerrametno(dat.getDataConv(jForDataEncerramento.getText()));
            }
            cb.setUsuarioCadastro(su.getUsuarioConectado());
            dat.setData(data);
            data = dat.getData();
            cb.setDataCadastro(data);
            cb.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1));
            ContasBancariasDAO cbdao = null;
            sql = "SELECT * FROM GFCCONTAS WHERE CD_BANCO = '" + jForCdBanco.getText().trim()
                    + "' AND CD_AGENCIA = '" + jForAgencia.getText().trim()
                    + "' AND CD_CONTA = '" + jForConta.getText().trim()
                    + "'";
            try {
                cbdao = new ContasBancariasDAO(conexao);
                if ("N".equals(oper)) {
                    cbdao.adicionar(cb);
                } else {
                    cbdao.atualizar(cb);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ManterContasBancarias.class.getName()).log(Level.SEVERE, null, ex);
            }
            limparTela();
            bloquearCampos();
            pesquisar();
        }
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:
        if (jForConta.getText().isEmpty()) {
            sql = "SELECT * FROM GFCCONTAS";
        } else {
            sql = "SELECT * FROM GFCCONTAS WHERE CD_CONTA LIKE '" + jForConta.getText().trim() + "'";
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
        jForAgenciaDigito.requestFocus();
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
        if (!jForConta.getText().isEmpty()) {
            try {
                ContasBancarias cc = new ContasBancarias();
                String cdBanco = jForCdBanco.getText().trim();
                String cdAgencia = jForAgencia.getText().trim();
                String cdConta = jForConta.getText().trim();
                cc.setCdBanco(cdBanco);
                cc.setCdAgencia(cdAgencia);
                cc.setCdConta(cdConta);
                ContasBancariasDAO ccDAO = new ContasBancariasDAO(conexao);
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

    private void jTexNomeBancoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTexNomeBancoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTexNomeBancoActionPerformed

    private void jForCdBancoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jForCdBancoKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomBanco();
        }
    }//GEN-LAST:event_jForCdBancoKeyPressed

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
            java.util.logging.Logger.getLogger(ManterContasBancarias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterContasBancarias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterContasBancarias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterContasBancarias.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterContasBancarias(su, conexao).setVisible(true);
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
    private javax.swing.JComboBox<String> jComTipoConta;
    private javax.swing.JFormattedTextField jForAgencia;
    private javax.swing.JFormattedTextField jForAgenciaDigito;
    private javax.swing.JFormattedTextField jForCdBanco;
    private javax.swing.JFormattedTextField jForConta;
    private javax.swing.JFormattedTextField jForContaDigito;
    private javax.swing.JFormattedTextField jForDataAbertura;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataEncerramento;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JFormattedTextField jForLimite;
    private javax.swing.JFormattedTextField jForSaldo;
    private javax.swing.JFormattedTextField jForTarifaAdm;
    private javax.swing.JFormattedTextField jForTxJuros;
    private javax.swing.JLabel jLabAgencia;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabCelular;
    private javax.swing.JLabel jLabConta;
    private javax.swing.JLabel jLabDataAbertura;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataEncerramento;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabDigito;
    private javax.swing.JLabel jLabLimite;
    private javax.swing.JLabel jLabNomeBanco;
    private javax.swing.JLabel jLabNumBanco;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSaldo;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabTarifaAdm;
    private javax.swing.JLabel jLabTipoConta;
    private javax.swing.JLabel jLabTxJuros;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JPanel jPanBotoes;
    private javax.swing.JPanel jPanInformacoes;
    private javax.swing.JPanel jPanSecundario;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexNomeBanco;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
