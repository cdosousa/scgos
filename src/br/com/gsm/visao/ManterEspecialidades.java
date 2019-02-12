/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GSMCA0020
 */
package br.com.gsm.visao;

import br.com.gsm.controle.CEspecialidades;
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoData;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.FormatarValor;
import br.com.modelo.SessaoUsuario;
import br.com.gsm.dao.EspecialidadesDAO;
import br.com.gsm.modelo.Especialidades;
import java.sql.Date;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.List;

/**
 *
 * @author cristiano
 */
public class ManterEspecialidades extends javax.swing.JFrame {

    private Especialidades regCorr;
    private List< Especialidades> resultado;
    private CEspecialidades pesq;
    private Especialidades sqlesp;
    private int numReg;
    private int idxCorr;
    private String sql;
    private String oper;
    private static SessaoUsuario su;
    private NumberFormat formato;

    /**
     * Creates new form ManterProdutos
     */
    public ManterEspecialidades(SessaoUsuario su) {
        formato = NumberFormat.getInstance();
        formato.setMinimumFractionDigits(2);
        this.su = su;
        oper = "N";
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formatarCampos();
        limparTela();
        setLocationRelativeTo(null);
        this.dispose();
    }

    public ManterEspecialidades() {

    }

    // método para setar formato do campo
    public void formatarCampos() {
        jForTxProdutividade.setDocument(new DefineCampoDecimal());
        jForValorUnit.setDocument(new DefineCampoDecimal());
        jForPercIndicacao.setDocument(new DefineCampoDecimal());
        jForPercObra.setDocument(new DefineCampoDecimal());
        jForPercComissao.setDocument(new DefineCampoDecimal());
        jForValorIndicacao.setDocument(new DefineCampoDecimal());
        jForValorObra.setDocument(new DefineCampoDecimal());
        jForValorComissao.setDocument(new DefineCampoDecimal());
    }

    // método para limpar tela
    public void limparTela() {
        jForCodigoEspecialidade.setText("");
        jTexNomeEspecialidade.setText("");
        jForTxProdutividade.setText("0,00");
        jForValorUnit.setText("0,00");
        jChePagarIndicacao.setSelected(false);
        jChePagarObra.setSelected(false);
        jChePagarComissao.setSelected(false);
        jForPercIndicacao.setText("0,00");
        jForPercObra.setText("0,00");
        jForPercComissao.setText("0,00");
        jForValorIndicacao.setText("0,00");
        jForValorObra.setText("0,00");
        jForValorComissao.setText("0,00");
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
        jForCodigoEspecialidade.setEditable(true);
    }

    // método para definir o tipo de pesquisa
    public void pesquisar() {

        pesq = new CEspecialidades();
        sqlesp = new Especialidades();
        try {
            numReg = pesq.pesquisar(sql);
            idxCorr = +1;
            if (numReg > 0) {
                upRegistros();
            } else {
                JOptionPane.showMessageDialog(null, "Nenhum registro encontrado!");
            }
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Argumento inválido na busca\nErr: " + iae.getCause());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Pesquisar os Registro\nErr: " + ex);
        }
    }

    public void upRegistros() {
        jTexRegTotal.setText(Integer.toString(numReg));
        jTexRegAtual.setText(Integer.toString(idxCorr));
        pesq.mostrarPesquisa(sqlesp, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jForCodigoEspecialidade.setText(sqlesp.getCdEspecialidade().trim());
        jTexNomeEspecialidade.setText(sqlesp.getNomeEspecialidade().trim());
        jForTxProdutividade.setText(String.valueOf(sqlesp.getTxProdutividade()));
        jForValorUnit.setText(String.valueOf(sqlesp.getValorUnit()));
        if ("S".equals(String.valueOf(sqlesp.getPagarIndicacao()))) {
            jChePagarIndicacao.setSelected(true);
        } else {
            jChePagarIndicacao.setSelected(false);
        }
        if ("S".equals(String.valueOf(sqlesp.getPagarObra()))) {
            jChePagarObra.setSelected(true);
        } else {
            jChePagarObra.setSelected(false);
        }
        if ("S".equals(String.valueOf(sqlesp.getPagarComissao()))) {
            jChePagarComissao.setSelected(true);
        } else {
            jChePagarComissao.setSelected(false);
        }
        jForPercIndicacao.setText(String.valueOf(sqlesp.getPercIndicacao()));
        jForPercObra.setText(String.valueOf(sqlesp.getPercObra()));
        jForPercComissao.setText(String.valueOf(sqlesp.getPercComissao()));
        jForValorIndicacao.setText(String.valueOf(sqlesp.getValorIndicacao()));
        jForValorObra.setText(String.valueOf(sqlesp.getValorObra()));
        jForValorComissao.setText(String.valueOf(sqlesp.getValorComissao()));
        jTexCadastradoPor.setText(sqlesp.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(sqlesp.getDataCadastro())));
        if (sqlesp.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(sqlesp.getDataModificacao())));
        }
        jComSituacao.setSelectedIndex(Integer.parseInt(String.valueOf(sqlesp.getSituacao())));

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
    public void bloquearCampos() {
        jForCodigoEspecialidade.setEditable(false);
        jTexNomeEspecialidade.setEditable(false);
        jComSituacao.setEditable(false);
    }

    //Loberar os campos da tela para atualização
    public void liberarCampos() {
        jTexNomeEspecialidade.setEditable(true);
        jComSituacao.setEditable(true);
    }

    // Criar um novo registro
    public void novoRegistro() {
        limparTela();
        liberarCampos();
        jForCodigoEspecialidade.setEditable(true);
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
        jPanPricipal = new javax.swing.JPanel();
        jPanSecundario = new javax.swing.JPanel();
        jPanBotoes = new javax.swing.JPanel();
        jLabDataCadastro = new javax.swing.JLabel();
        jForDataCadastro = new javax.swing.JFormattedTextField();
        jLabDataModificacao = new javax.swing.JLabel();
        jForDataModificacao = new javax.swing.JFormattedTextField();
        jTexRegAtual = new javax.swing.JTextField();
        jTexRegTotal = new javax.swing.JTextField();
        jLabReg = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jTexCadastradoPor = new javax.swing.JTextField();
        jLabCodigo = new javax.swing.JLabel();
        jLabDescricao = new javax.swing.JLabel();
        jLabSituacao = new javax.swing.JLabel();
        jForCodigoEspecialidade = new javax.swing.JFormattedTextField();
        jTexNomeEspecialidade = new javax.swing.JTextField();
        jComSituacao = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jForTxProdutividade = jForTxProdutividade = new FormatarValor((FormatarValor.PESO));
        jLabValor = new javax.swing.JLabel();
        jForValorUnit = jForValorUnit = new FormatarValor((FormatarValor.MOEDA));
        jSeparator1 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jLabPercIndicacao = new javax.swing.JLabel();
        jLabPercObra = new javax.swing.JLabel();
        jLabPercComissao = new javax.swing.JLabel();
        jForPercIndicacao = new FormatarValor((FormatarValor.PORCENTAGEM));
        jForPercObra = new FormatarValor((FormatarValor.PORCENTAGEM));
        jForPercComissao = new FormatarValor((FormatarValor.PORCENTAGEM));
        jLabValorIndicacao = new javax.swing.JLabel();
        jLabValorObra = new javax.swing.JLabel();
        jLabValorComissao = new javax.swing.JLabel();
        jForValorIndicacao = new FormatarValor((FormatarValor.MOEDA));
        jForValorObra = new FormatarValor((FormatarValor.MOEDA));
        jForValorComissao = new FormatarValor((FormatarValor.MOEDA));
        jChePagarIndicacao = new javax.swing.JCheckBox();
        jChePagarObra = new javax.swing.JCheckBox();
        jChePagarComissao = new javax.swing.JCheckBox();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Especialidades");

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

        jPanPricipal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

            jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel2.setText("Cadastrado por:");

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
                    .addComponent(jLabel2)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(52, 52, 52)
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
                        .addComponent(jLabel2)
                        .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jLabCodigo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCodigo.setText("Código:");

            jLabDescricao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabDescricao.setText("Descrição:");

            jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabSituacao.setText("Situação:");

            try {
                jForCodigoEspecialidade.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("********")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }

            jTexNomeEspecialidade.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jTexNomeEspecialidadeActionPerformed(evt);
                }
            });

            jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ativo", "Inativo" }));

            jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel1.setText("Qtde./ Produtivade:");

            jForTxProdutividade.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
            jForTxProdutividade.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jLabValor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValor.setText("Valor:");

            jForValorUnit.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
            jForValorUnit.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jSeparator1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

            jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabPercIndicacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabPercIndicacao.setText("%");

            jLabPercObra.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabPercObra.setText("%");

            jLabPercComissao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabPercComissao.setText("%");

            jForPercIndicacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
            jForPercIndicacao.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jForPercObra.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
            jForPercObra.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jForPercComissao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
            jForPercComissao.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jLabValorIndicacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorIndicacao.setText("Valor(R$):");

            jLabValorObra.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorObra.setText("Valor(R$):");

            jLabValorComissao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorComissao.setText("Valor(R$):");

            jForValorIndicacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
            jForValorIndicacao.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jForValorObra.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
            jForValorObra.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jForValorComissao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
            jForValorComissao.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jChePagarIndicacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jChePagarIndicacao.setText("Habilita Pgto. Indicação");

            jChePagarObra.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jChePagarObra.setText("Habiita Pgto. Obra");

            jChePagarComissao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jChePagarComissao.setText("Habilita Pgto. Comissão");

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jChePagarIndicacao)
                        .addComponent(jChePagarObra)
                        .addComponent(jChePagarComissao))
                    .addGap(20, 20, 20)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabPercComissao)
                        .addComponent(jLabPercObra)
                        .addComponent(jLabPercIndicacao))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jForPercComissao, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabValorComissao)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForValorComissao, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jForPercObra, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabValorObra)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForValorObra, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jForPercIndicacao, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabValorIndicacao)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForValorIndicacao, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabPercIndicacao)
                        .addComponent(jForPercIndicacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabValorIndicacao)
                        .addComponent(jForValorIndicacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jChePagarIndicacao))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabPercObra)
                        .addComponent(jForPercObra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabValorObra)
                        .addComponent(jForValorObra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jChePagarObra))
                    .addGap(18, 18, 18)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabPercComissao)
                        .addComponent(jForPercComissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabValorComissao)
                        .addComponent(jForValorComissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jChePagarComissao))
                    .addContainerGap(17, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout jPanSecundarioLayout = new javax.swing.GroupLayout(jPanSecundario);
            jPanSecundario.setLayout(jPanSecundarioLayout);
            jPanSecundarioLayout.setHorizontalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabCodigo, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabDescricao, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTexNomeEspecialidade)
                                        .addComponent(jForCodigoEspecialidade, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabSituacao)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(35, 35, 35))
                                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                    .addComponent(jForTxProdutividade, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(27, 27, 27)
                                    .addComponent(jLabValor)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForValorUnit, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(89, 89, 89)
                                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addContainerGap())))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanBotoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addContainerGap())))
            );
            jPanSecundarioLayout.setVerticalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCodigo)
                        .addComponent(jLabSituacao)
                        .addComponent(jForCodigoEspecialidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabDescricao)
                        .addComponent(jTexNomeEspecialidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jForTxProdutividade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabValor)
                            .addComponent(jForValorUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(92, 92, 92)
                    .addComponent(jPanBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout jPanPricipalLayout = new javax.swing.GroupLayout(jPanPricipal);
            jPanPricipal.setLayout(jPanPricipalLayout);
            jPanPricipalLayout.setHorizontalGroup(
                jPanPricipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanPricipalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanSecundario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanPricipalLayout.setVerticalGroup(
                jPanPricipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanPricipalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanSecundario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jPanPricipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanPricipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jTexNomeEspecialidadeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTexNomeEspecialidadeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTexNomeEspecialidadeActionPerformed

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        // TODO add your handling code here:
        if (jForCodigoEspecialidade.getText().isEmpty() || jTexNomeEspecialidade.getText().isEmpty() || jComSituacao.getSelectedItem().toString().substring(0, 1) == " ") {
            JOptionPane.showMessageDialog(null, "Os campos Código, Descrição e Situacao precisam ser preenchidos corretamente!");
        } else {
            DataSistema dat = new DataSistema();
            Especialidades esp = new Especialidades();
            String data = null;
            esp.setCdEspecialidade(jForCodigoEspecialidade.getText().toUpperCase());
            esp.setNomeEspecialidade(jTexNomeEspecialidade.getText().toUpperCase());
            char pagarIndicacao = 'N';
            if (jChePagarIndicacao.isSelected()) {
                pagarIndicacao = 'S';
            }

            char pagarObra = 'N';
            if (jChePagarObra.isSelected()) {
                pagarObra = 'S';
            }

            char pagarComissao = 'N';
            if (jChePagarComissao.isSelected()) {
                pagarComissao = 'S';
            }
            esp.setPagarIndicacao(pagarIndicacao);
            esp.setPagarObra(pagarObra);
            esp.setPagarComissao(pagarComissao);
            try {
                esp.setTxProdutividade(formato.parse(jForTxProdutividade.getText()).doubleValue());
                esp.setValorUnit(formato.parse(jForValorUnit.getText()).doubleValue());
                esp.setPercIndicacao(formato.parse(jForPercIndicacao.getText()).doubleValue());
                esp.setPercObra(formato.parse(jForPercObra.getText()).doubleValue());
                esp.setPercComissao(formato.parse(jForPercComissao.getText()).doubleValue());
                esp.setValorIndicacao(formato.parse(jForValorIndicacao.getText()).doubleValue());
                esp.setValorObra(formato.parse(jForValorObra.getText()).doubleValue());
                esp.setValorComissao(formato.parse(jForValorComissao.getText()).doubleValue());
            } catch (ParseException pe) {
                Logger.getLogger(ManterEspecialidades.class.getName()).log(Level.SEVERE, null, pe);
            }
            esp.setUsuarioCadastro(su.getUsuarioConectado());
            dat.setData(data);
            data = dat.getData();
            esp.setDataCadastro(data);
            esp.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1).charAt(0));
            EspecialidadesDAO espdao = null;
            sql = "SELECT * FROM GSMESPECIALIDADES WHERE CD_ESPECIALIDADE =" + jForCodigoEspecialidade.getText().toUpperCase().trim();
            try {
                espdao = new EspecialidadesDAO();
                if ("N".equals(oper)) {
                    espdao.adicionar(esp);
                } else {
                    esp.setDataModificacao(data);
                    espdao.atualizar(esp);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ManterEspecialidades.class.getName()).log(Level.SEVERE, null, ex);
            }
            limparTela();
            bloquearCampos();
            pesquisar();
        }

    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:
        if (jTexNomeEspecialidade.getText().isEmpty()) {
            sql = "SELECT * FROM GSMESPECIALIDADES";
        } else {
            sql = "SELECT * FROM GSMESPECIALIDADES WHERE NOME_ESPECIALIDADE LIKE '" + jTexNomeEspecialidade.getText().trim() + "'";
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
        jForCodigoEspecialidade.requestFocus();
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
        jForCodigoEspecialidade.requestFocus();
    }//GEN-LAST:event_jButNovoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        jButNovoActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        // TODO add your handling code here:
        if (!jForCodigoEspecialidade.getText().isEmpty()) {
            try {
                Especialidades cc = new Especialidades();
                cc.setCdEspecialidade(jForCodigoEspecialidade.getText().toUpperCase());
                EspecialidadesDAO ccDAO = new EspecialidadesDAO();
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
            java.util.logging.Logger.getLogger(ManterEspecialidades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterEspecialidades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterEspecialidades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterEspecialidades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new ManterEspecialidades(su).setVisible(true);
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
    private javax.swing.JCheckBox jChePagarComissao;
    private javax.swing.JCheckBox jChePagarIndicacao;
    private javax.swing.JCheckBox jChePagarObra;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JFormattedTextField jForCodigoEspecialidade;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JFormattedTextField jForPercComissao;
    private javax.swing.JFormattedTextField jForPercIndicacao;
    private javax.swing.JFormattedTextField jForPercObra;
    private javax.swing.JFormattedTextField jForTxProdutividade;
    private javax.swing.JFormattedTextField jForValorComissao;
    private javax.swing.JFormattedTextField jForValorIndicacao;
    private javax.swing.JFormattedTextField jForValorObra;
    private javax.swing.JFormattedTextField jForValorUnit;
    private javax.swing.JLabel jLabCodigo;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabDescricao;
    private javax.swing.JLabel jLabPercComissao;
    private javax.swing.JLabel jLabPercIndicacao;
    private javax.swing.JLabel jLabPercObra;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabValor;
    private javax.swing.JLabel jLabValorComissao;
    private javax.swing.JLabel jLabValorIndicacao;
    private javax.swing.JLabel jLabValorObra;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JPanel jPanBotoes;
    private javax.swing.JPanel jPanPricipal;
    private javax.swing.JPanel jPanSecundario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexNomeEspecialidade;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
