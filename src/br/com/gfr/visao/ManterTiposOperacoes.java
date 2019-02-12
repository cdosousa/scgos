/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GFRCA0040
 */
package br.com.gfr.visao;

import br.com.gfr.visao.*;
import br.com.gfr.controle.CTiposOperacoes;
import br.com.modelo.DataSistema;
import br.com.modelo.SessaoUsuario;
import br.com.gfr.dao.TiposOperacoesDAO;
import br.com.gfr.modelo.TiposOperacoes;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.DefineCampoInteiro;
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
 * @author Cristiano de Oliveira Sousa created on 08/11/2017
 */
public class ManterTiposOperacoes extends javax.swing.JFrame {

    private static Connection conexao;
    private TiposOperacoes regCorr;
    private List< TiposOperacoes> resultado;
    private CTiposOperacoes ctop;
    private TiposOperacoes modtop;
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
    public ManterTiposOperacoes(SessaoUsuario su, Connection conexao) {
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
    public ManterTiposOperacoes() {

    }

    // método para setar formato do campo
    private void formatarCampos() {
        jForAliquotaPis.setDocument(new DefineCampoDecimal());
        jForAliquotacCofins.setDocument(new DefineCampoDecimal());
        jForAliquotaSimples.setDocument(new DefineCampoDecimal());
        jForAliquotaIpi.setDocument(new DefineCampoDecimal());
        jForAliquotaIcms.setDocument(new DefineCampoDecimal());
        jForAliquotaSuframa.setDocument(new DefineCampoDecimal());
        jForAliquotaSimbahia.setDocument(new DefineCampoDecimal());
        jForBaseCalIcmsOper.setDocument(new DefineCampoDecimal());
        jForIcmsOpBaseRed.setDocument(new DefineCampoDecimal());
        jForMva.setDocument(new DefineCampoDecimal());
        jForBaseIcmsStRed.setDocument(new DefineCampoDecimal());
        jForIcmsCadeiaSemRed.setDocument(new DefineCampoDecimal());
        jForAliquotaIss.setDocument(new DefineCampoDecimal());
    }

    // método para limpar tela
    private void limparTela() {
        jForCdTipoOperacao.setText("");
        jTexNomeTipoOperacao.setText("");
        jForCdCfop.setText("");
        jTexDescricaoCFOP.setText("");
        jComTipoEstoque.setSelectedIndex(3);
        jForAliquotaPis.setText("0,00");
        jForAliquotacCofins.setText("0,00");
        jForAliquotaSimples.setText("0,00");
        jForAliquotaIpi.setText("0,00");
        jComTributaIcms.setSelectedIndex(2);
        jComTributaIpi.setSelectedIndex(2);
        jComTributaSuframa.setSelectedIndex(2);
        jComTributaSimbahia.setSelectedIndex(2);
        jForAliquotaIcms.setText("0,00");
        jForAliquotaIpi.setText("0,00");
        jForAliquotaSuframa.setText("0,00");
        jForAliquotaSimbahia.setText("0,00");
        jForBaseCalIcmsOper.setText("0,00");
        jForBaseIcmsStRed.setText("0,00");
        jForIcmsOpBaseRed.setText("0,00");
        jForMva.setText("0,00");
        jForIcmsCadeiaSemRed.setText("0,00");
        jForAliquotaIss.setText("0,00");
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
        ctop = new CTiposOperacoes(conexao);
        modtop = new TiposOperacoes();
        try {
            numReg = ctop.pesquisar(sql);
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
        ctop.mostrarPesquisa(modtop, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jForCdTipoOperacao.setText(modtop.getCdTipoOper());
        jTexNomeTipoOperacao.setText(modtop.getNomeTipoOperacao());
        jForCdCfop.setText(modtop.getCdCfop().trim());
        jTexDescricaoCFOP.setText(modtop.getDescricaoCfop().trim());
        jComTipoEstoque.setSelectedIndex(Integer.parseInt(modtop.getTipoEstoque()));
        jForAliquotaPis.setText(String.valueOf(modtop.getAliquotaPis()));
        jForAliquotacCofins.setText(String.valueOf(modtop.getAliquotaCofins()));
        jForAliquotaSimples.setText(String.valueOf(modtop.getAliquotaSimples()));
        jForAliquotaIpi.setText(String.valueOf(modtop.getAliquotaIpi()));
        jForAliquotaIcms.setText(String.valueOf(modtop.getAliquotaIcms()));
        jForAliquotaSuframa.setText(String.valueOf(modtop.getAliquotaSuframa()));
        jForAliquotaSimbahia.setText(String.valueOf(modtop.getAliquotaSimbahia()));
        jComTributaIcms.setSelectedIndex(Integer.parseInt(modtop.getTributaIcms()));
        jComTributaIpi.setSelectedIndex(Integer.parseInt(modtop.getTributaIpi()));
        jComTributaSuframa.setSelectedIndex(Integer.parseInt(modtop.getTributaSuframa()));
        jComTributaSimbahia.setSelectedIndex(Integer.parseInt(modtop.getTributaSimbahia()));
        jForBaseCalIcmsOper.setText(String.valueOf(modtop.getBaseCalculoIcmsOp()));
        jForIcmsOpBaseRed.setText(String.valueOf(modtop.getIcmsOpBaseRed()));
        jForMva.setText(String.valueOf(modtop.getMva()));
        jForBaseIcmsStRed.setText(String.valueOf(modtop.getBaseIcmsStRed()));
        jForIcmsCadeiaSemRed.setText(String.valueOf(modtop.getIcmsCadeiaSemRed()));
        jForAliquotaIss.setText(String.valueOf(modtop.getAliquotaIss()));
        jTexCadastradoPor.setText(modtop.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modtop.getDataCadastro())));
        if (modtop.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(modtop.getDataModificacao())));
        }
        jComSituacao.setSelectedIndex(Integer.parseInt(String.valueOf(modtop.getSituacao())));

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
        jForCdTipoOperacao.setEditable(false);
        jTexNomeTipoOperacao.setEditable(false);
        jForCdCfop.setEditable(false);
        jTexDescricaoCFOP.setEditable(false);
        jComTipoEstoque.setEditable(false);
        jForAliquotaPis.setEditable(false);
        jForAliquotacCofins.setEditable(false);
        jForAliquotaSimples.setEditable(false);
        jForAliquotaIpi.setEditable(false);
        jForAliquotaIcms.setEditable(false);
        jForAliquotaSuframa.setEditable(false);
        jForAliquotaSimbahia.setEditable(false);
        jComTributaIcms.setEditable(false);
        jComTributaIpi.setEditable(false);
        jComTributaSuframa.setEditable(false);
        jComTributaSimbahia.setEditable(false);
        jForBaseCalIcmsOper.setEditable(false);
        jForIcmsOpBaseRed.setEditable(false);
        jForMva.setEditable(false);
        jForBaseIcmsStRed.setEditable(false);
        jForIcmsCadeiaSemRed.setEditable(false);
        jForAliquotaIss.setEditable(false);
        jComSituacao.setEditable(false);
    }

    //Loberar os campos da tela para atualização
    private void liberarCampos() {
        jTexNomeTipoOperacao.setEditable(true);
        jForCdCfop.setEditable(true);
        jTexDescricaoCFOP.setEditable(true);
        jComTipoEstoque.setEditable(true);
        jForAliquotaPis.setEditable(true);
        jForAliquotacCofins.setEditable(true);
        jForAliquotaSimples.setEditable(true);
        jForAliquotaIpi.setEditable(true);
        jForAliquotaIcms.setEditable(true);
        jForAliquotaSuframa.setEditable(true);
        jForAliquotaSimbahia.setEditable(true);
        jComTributaIcms.setEditable(true);
        jComTributaIpi.setEditable(true);
        jComTributaSuframa.setEditable(true);
        jComTributaSimbahia.setEditable(true);
        jForBaseCalIcmsOper.setEditable(true);
        jForIcmsOpBaseRed.setEditable(true);
        jForMva.setEditable(true);
        jForBaseIcmsStRed.setEditable(true);
        jForIcmsCadeiaSemRed.setEditable(true);
        jForAliquotaIss.setEditable(true);
        jComSituacao.setEditable(true);
    }

    private void novoRegistro() {
        limparTela();
        liberarCampos();
        jForCdTipoOperacao.setEditable(true);
        jForCdTipoOperacao.requestFocus();
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
        jLabSituacao = new javax.swing.JLabel();
        jForCdCfop = new javax.swing.JFormattedTextField();
        jPanTributos = new javax.swing.JPanel();
        jPanFederais = new javax.swing.JPanel();
        jLabAliqPis = new javax.swing.JLabel();
        jForAliquotaPis = new FormatarValor(FormatarValor.NUMERO);
        jLabAliqCofins = new javax.swing.JLabel();
        jForAliquotacCofins = new FormatarValor(FormatarValor.NUMERO)
        ;
        jLabAliqSimplesNacional = new javax.swing.JLabel();
        jForAliquotaSimples = new FormatarValor(FormatarValor.NUMERO);
        jForAliquotaIpi = new FormatarValor(FormatarValor.NUMERO);
        jLabTributaIpi = new javax.swing.JLabel();
        jComTributaIpi = new javax.swing.JComboBox<>();
        jPanEstaduais = new javax.swing.JPanel();
        jLabTributaIcms = new javax.swing.JLabel();
        jLabTributaSuframa = new javax.swing.JLabel();
        jLabTributaSimbahia = new javax.swing.JLabel();
        jForAliquotaIcms = new FormatarValor(FormatarValor.NUMERO);
        jForAliquotaSuframa = new FormatarValor(FormatarValor.NUMERO);
        jForAliquotaSimbahia = new FormatarValor(FormatarValor.NUMERO);
        jLabBaseCalIcmsOper = new javax.swing.JLabel();
        jLabIcmsOpBaseRed = new javax.swing.JLabel();
        jLabMva = new javax.swing.JLabel();
        jLabBaseIcmsStRed = new javax.swing.JLabel();
        jLabIcmsCadeiaSemRed = new javax.swing.JLabel();
        jForBaseCalIcmsOper = new FormatarValor(FormatarValor.NUMERO);
        jForIcmsOpBaseRed = new FormatarValor(FormatarValor.NUMERO);
        jForMva = new FormatarValor(FormatarValor.NUMERO);
        jForBaseIcmsStRed = new FormatarValor(FormatarValor.NUMERO);
        jForIcmsCadeiaSemRed = new FormatarValor(FormatarValor.NUMERO);
        jComTributaIcms = new javax.swing.JComboBox<>();
        jComTributaSuframa = new javax.swing.JComboBox<>();
        jComTributaSimbahia = new javax.swing.JComboBox<>();
        jPanMunicipais = new javax.swing.JPanel();
        jLabAliqIss = new javax.swing.JLabel();
        jForAliquotaIss = new FormatarValor(FormatarValor.NUMERO);
        jLabTipoOperacao = new javax.swing.JLabel();
        jForCdTipoOperacao = new javax.swing.JFormattedTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabMovimentoEtoque = new javax.swing.JLabel();
        jComTipoEstoque = new javax.swing.JComboBox<>();
        jTexNomeTipoOperacao = new javax.swing.JTextField();
        jTexDescricaoCFOP = new javax.swing.JTextField();
        jComSituacao = new javax.swing.JComboBox<>();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Tipo de Operação");

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
                    .addGap(53, 53, 53))
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
            jLabNumBanco.setText("C.F.O.P.:");

            jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabSituacao.setText("Situação:");

            try {
                jForCdCfop.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#.###")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }

            jPanTributos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Tributos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jPanFederais.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Federais", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jLabAliqPis.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabAliqPis.setText("PIS:");

            jForAliquotaPis.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
            jForAliquotaPis.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jLabAliqCofins.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabAliqCofins.setText("Cofins:");

            jForAliquotacCofins.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
            jForAliquotacCofins.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jLabAliqSimplesNacional.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabAliqSimplesNacional.setText("Simples Nacional:");

            jForAliquotaSimples.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
            jForAliquotaSimples.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jForAliquotaIpi.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
            jForAliquotaIpi.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jLabTributaIpi.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTributaIpi.setText("Tributa IPI:");

            jComTributaIpi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não", "Regra" }));

            javax.swing.GroupLayout jPanFederaisLayout = new javax.swing.GroupLayout(jPanFederais);
            jPanFederais.setLayout(jPanFederaisLayout);
            jPanFederaisLayout.setHorizontalGroup(
                jPanFederaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanFederaisLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabAliqPis)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForAliquotaPis, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabAliqCofins)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForAliquotacCofins, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabAliqSimplesNacional)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForAliquotaSimples, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(37, 37, 37)
                    .addComponent(jLabTributaIpi)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jComTributaIpi, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForAliquotaIpi, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanFederaisLayout.setVerticalGroup(
                jPanFederaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanFederaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabTributaIpi)
                    .addComponent(jForAliquotaIpi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComTributaIpi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanFederaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabAliqPis)
                    .addComponent(jForAliquotaPis, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabAliqCofins)
                    .addComponent(jForAliquotacCofins, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabAliqSimplesNacional)
                    .addComponent(jForAliquotaSimples, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            jPanEstaduais.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Estaduais", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jLabTributaIcms.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTributaIcms.setText("Tributa ICMS:");

            jLabTributaSuframa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTributaSuframa.setText("Suframa:");

            jLabTributaSimbahia.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTributaSimbahia.setText("Simbahia:");

            jForAliquotaIcms.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
            jForAliquotaIcms.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jForAliquotaSuframa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
            jForAliquotaSuframa.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jForAliquotaSimbahia.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
            jForAliquotaSimbahia.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jLabBaseCalIcmsOper.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabBaseCalIcmsOper.setText("Base Calc. Icms Oper.:");

            jLabIcmsOpBaseRed.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabIcmsOpBaseRed.setText("ICMS Operacional c/ Redução:");

            jLabMva.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabMva.setText("M.V.A:");

            jLabBaseIcmsStRed.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabBaseIcmsStRed.setText("Base ICMS ST c/ Redução:");

            jLabIcmsCadeiaSemRed.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabIcmsCadeiaSemRed.setText("ICMS Cadeia s/ Redução:");

            jForBaseCalIcmsOper.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
            jForBaseCalIcmsOper.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jForIcmsOpBaseRed.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
            jForIcmsOpBaseRed.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jForMva.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
            jForMva.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jForBaseIcmsStRed.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
            jForBaseIcmsStRed.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jForIcmsCadeiaSemRed.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
            jForIcmsCadeiaSemRed.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            jComTributaIcms.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não", "Regra" }));

            jComTributaSuframa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não", "Regra" }));

            jComTributaSimbahia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não", "Regra" }));

            javax.swing.GroupLayout jPanEstaduaisLayout = new javax.swing.GroupLayout(jPanEstaduais);
            jPanEstaduais.setLayout(jPanEstaduaisLayout);
            jPanEstaduaisLayout.setHorizontalGroup(
                jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanEstaduaisLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabTributaSuframa)
                        .addComponent(jLabTributaIcms)
                        .addComponent(jLabTributaSimbahia))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jComTributaSuframa, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComTributaIcms, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComTributaSimbahia, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jForAliquotaSimbahia, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jForAliquotaSuframa, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jForAliquotaIcms, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(22, 22, 22)
                    .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanEstaduaisLayout.createSequentialGroup()
                            .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabIcmsOpBaseRed, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabBaseCalIcmsOper, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanEstaduaisLayout.createSequentialGroup()
                                    .addComponent(jForBaseCalIcmsOper, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabIcmsCadeiaSemRed)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForIcmsCadeiaSemRed, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jForIcmsOpBaseRed, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanEstaduaisLayout.createSequentialGroup()
                            .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanEstaduaisLayout.createSequentialGroup()
                                    .addComponent(jLabBaseIcmsStRed)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForBaseIcmsStRed, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanEstaduaisLayout.createSequentialGroup()
                                    .addComponent(jLabMva)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForMva, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(204, 204, 204)))
                    .addContainerGap(12, Short.MAX_VALUE))
            );
            jPanEstaduaisLayout.setVerticalGroup(
                jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanEstaduaisLayout.createSequentialGroup()
                    .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabBaseCalIcmsOper)
                            .addComponent(jForBaseCalIcmsOper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabIcmsCadeiaSemRed)
                            .addComponent(jForIcmsCadeiaSemRed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jForAliquotaIcms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jComTributaIcms, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabTributaIcms)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanEstaduaisLayout.createSequentialGroup()
                            .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jForIcmsOpBaseRed)
                                .addComponent(jLabIcmsOpBaseRed, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabMva)
                                .addComponent(jForMva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanEstaduaisLayout.createSequentialGroup()
                                .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabTributaSuframa)
                                    .addComponent(jComTributaSuframa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabTributaSimbahia)
                                    .addComponent(jComTributaSimbahia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanEstaduaisLayout.createSequentialGroup()
                                .addComponent(jForAliquotaSuframa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForAliquotaSimbahia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabBaseIcmsStRed)
                        .addComponent(jForBaseIcmsStRed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            );

            jPanMunicipais.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Municipais", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jLabAliqIss.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabAliqIss.setText("I.S.S.:");

            jForAliquotaIss.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
            jForAliquotaIss.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

            javax.swing.GroupLayout jPanMunicipaisLayout = new javax.swing.GroupLayout(jPanMunicipais);
            jPanMunicipais.setLayout(jPanMunicipaisLayout);
            jPanMunicipaisLayout.setHorizontalGroup(
                jPanMunicipaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanMunicipaisLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabAliqIss)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForAliquotaIss, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanMunicipaisLayout.setVerticalGroup(
                jPanMunicipaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanMunicipaisLayout.createSequentialGroup()
                    .addGap(1, 1, 1)
                    .addGroup(jPanMunicipaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabAliqIss)
                        .addComponent(jForAliquotaIss, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout jPanTributosLayout = new javax.swing.GroupLayout(jPanTributos);
            jPanTributos.setLayout(jPanTributosLayout);
            jPanTributosLayout.setHorizontalGroup(
                jPanTributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTributosLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanTributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanFederais, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanEstaduais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanMunicipais, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanTributosLayout.setVerticalGroup(
                jPanTributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTributosLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanFederais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanEstaduais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanMunicipais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(24, Short.MAX_VALUE))
            );

            jLabTipoOperacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTipoOperacao.setText("Tipo Operação:");

            try {
                jForCdTipoOperacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#.###**")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }

            jLabMovimentoEtoque.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabMovimentoEtoque.setText("Movimento Estoque:");

            jComTipoEstoque.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Entrada", "Saída", "Nulo" }));

            jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ativo", "Inativo" }));

            javax.swing.GroupLayout jPanSecundarioLayout = new javax.swing.GroupLayout(jPanSecundario);
            jPanSecundario.setLayout(jPanSecundarioLayout);
            jPanSecundarioLayout.setHorizontalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                            .addGap(0, 0, Short.MAX_VALUE)
                            .addComponent(jLabMovimentoEtoque))
                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabTipoOperacao, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabNumBanco, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jForCdTipoOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jForCdCfop, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jTexDescricaoCFOP)
                                .addComponent(jTexNomeTipoOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabSituacao)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jComSituacao, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComTipoEstoque, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(52, 52, 52))
                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(jPanTributos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(10, 10, 10))
                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(jPanBotoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                    .addGap(10, 10, 10)
                    .addComponent(jSeparator1)
                    .addGap(10, 10, 10))
            );
            jPanSecundarioLayout.setVerticalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabSituacao)
                        .addComponent(jLabTipoOperacao)
                        .addComponent(jForCdTipoOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeTipoOperacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabNumBanco)
                        .addComponent(jForCdCfop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexDescricaoCFOP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabMovimentoEtoque)
                        .addComponent(jComTipoEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanTributos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        if (jForCdTipoOperacao.getText().isEmpty() || jTexNomeTipoOperacao.getText().isEmpty() || jForCdCfop.getText().isEmpty()
                || jTexDescricaoCFOP.getText().isEmpty() || jComSituacao.getSelectedItem().toString().substring(0, 1) == " ") {
            JOptionPane.showMessageDialog(null, "Os campos Tipo Operação, CFOP e Situacao precisam ser preenchidos corretamente!");
        } else {
            DataSistema dat = new DataSistema();
            TiposOperacoes top = new TiposOperacoes();
            String data = null;
            top.setCdTipoOper(jForCdTipoOperacao.getText());
            top.setNomeTipoOperacao(jTexNomeTipoOperacao.getText().trim().toUpperCase());
            top.setCdCfop(jForCdCfop.getText().trim());
            top.setDescricaoCfop(jTexDescricaoCFOP.getText().trim().toUpperCase());
            top.setTipoEstoque(jComTipoEstoque.getSelectedItem().toString().substring(0, 1));
            top.setTributaIcms(jComTributaIcms.getSelectedItem().toString().substring(0, 1));
            top.setTributaIpi(jComTributaIpi.getSelectedItem().toString().substring(0, 1));
            top.setTributaSuframa(jComTributaSuframa.getSelectedItem().toString().substring(0, 1));
            top.setTributaSimbahia(jComTributaSimbahia.getSelectedItem().toString().substring(0, 1));
            try {
                top.setAliquotaPis(formato.parse(jForAliquotaPis.getText()).doubleValue());
                top.setAliquotaCofins(formato.parse(jForAliquotacCofins.getText()).doubleValue());
                top.setAliquotaSimples(formato.parse(jForAliquotaSimples.getText()).doubleValue());
                top.setAliquotaIpi(formato.parse(jForAliquotaIpi.getText()).doubleValue());
                top.setAliquotaIcms(formato.parse(jForAliquotaIcms.getText()).doubleValue());
                top.setAliquotaSuframa(formato.parse(jForAliquotaSuframa.getText()).doubleValue());
                top.setAliquotaSimbahia(formato.parse(jForAliquotaSimbahia.getText()).doubleValue());
                top.setBaseCalculoIcmsOp(formato.parse(jForBaseCalIcmsOper.getText()).doubleValue());
                top.setIcmsOpBaseRed(formato.parse(jForIcmsOpBaseRed.getText()).doubleValue());
                top.setMva(formato.parse(jForMva.getText()).doubleValue());
                top.setBaseIcmsStRed(formato.parse(jForBaseIcmsStRed.getText()).doubleValue());
                top.setIcmsCadeiaSemRed(formato.parse(jForIcmsCadeiaSemRed.getText()).doubleValue());
                top.setAliquotaIss(formato.parse(jForAliquotaIss.getText()).doubleValue());
            } catch (ParseException ex) {
                Logger.getLogger(ManterTiposOperacoes.class.getName()).log(Level.SEVERE, null, ex);
            }

            top.setUsuarioCadastro(su.getUsuarioConectado());
            dat.setData(data);
            data = dat.getData();
            top.setDataCadastro(data);
            top.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1));
            TiposOperacoesDAO topdao = null;
            sql = "SELECT * FROM GFRTIPOOPERACAO WHERE CD_TIPOOPER = '" + jForCdTipoOperacao.getText().trim().replace(".", "")
                    + "'";
            try {
                topdao = new TiposOperacoesDAO(conexao);
                if ("N".equals(oper)) {
                    topdao.adicionar(top);
                } else {
                    top.setDataModificacao(data);
                    topdao.atualizar(top);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ManterTiposOperacoes.class.getName()).log(Level.SEVERE, null, ex);
            }
            limparTela();
            bloquearCampos();
            pesquisar();
        }
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:
        if (jTexNomeTipoOperacao.getText().isEmpty()) {
            sql = "SELECT * FROM GFRTIPOOPERACAO";
        } else {
            sql = "SELECT * FROM GFRTIPOOPERACAO WHERE NOME_OPERACAO LIKE '" + jTexNomeTipoOperacao.getText().trim() + "'";
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
                TiposOperacoes cc = new TiposOperacoes();
                cc.setCdTipoOper(jForCdTipoOperacao.getText().trim().replace(".", ""));
                TiposOperacoesDAO ccDAO = new TiposOperacoesDAO(conexao);
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
            java.util.logging.Logger.getLogger(ManterTiposOperacoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterTiposOperacoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterTiposOperacoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterTiposOperacoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new ManterTiposOperacoes(su, conexao).setVisible(true);
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
    private javax.swing.JComboBox<String> jComTipoEstoque;
    private javax.swing.JComboBox<String> jComTributaIcms;
    private javax.swing.JComboBox<String> jComTributaIpi;
    private javax.swing.JComboBox<String> jComTributaSimbahia;
    private javax.swing.JComboBox<String> jComTributaSuframa;
    private javax.swing.JFormattedTextField jForAliquotaIcms;
    private javax.swing.JFormattedTextField jForAliquotaIpi;
    private javax.swing.JFormattedTextField jForAliquotaIss;
    private javax.swing.JFormattedTextField jForAliquotaPis;
    private javax.swing.JFormattedTextField jForAliquotaSimbahia;
    private javax.swing.JFormattedTextField jForAliquotaSimples;
    private javax.swing.JFormattedTextField jForAliquotaSuframa;
    private javax.swing.JFormattedTextField jForAliquotacCofins;
    private javax.swing.JFormattedTextField jForBaseCalIcmsOper;
    private javax.swing.JFormattedTextField jForBaseIcmsStRed;
    private javax.swing.JFormattedTextField jForCdCfop;
    private javax.swing.JFormattedTextField jForCdTipoOperacao;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JFormattedTextField jForIcmsCadeiaSemRed;
    private javax.swing.JFormattedTextField jForIcmsOpBaseRed;
    private javax.swing.JFormattedTextField jForMva;
    private javax.swing.JLabel jLabAliqCofins;
    private javax.swing.JLabel jLabAliqIss;
    private javax.swing.JLabel jLabAliqPis;
    private javax.swing.JLabel jLabAliqSimplesNacional;
    private javax.swing.JLabel jLabBaseCalIcmsOper;
    private javax.swing.JLabel jLabBaseIcmsStRed;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabIcmsCadeiaSemRed;
    private javax.swing.JLabel jLabIcmsOpBaseRed;
    private javax.swing.JLabel jLabMovimentoEtoque;
    private javax.swing.JLabel jLabMva;
    private javax.swing.JLabel jLabNumBanco;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabTipoOperacao;
    private javax.swing.JLabel jLabTributaIcms;
    private javax.swing.JLabel jLabTributaIpi;
    private javax.swing.JLabel jLabTributaSimbahia;
    private javax.swing.JLabel jLabTributaSuframa;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JPanel jPanBotoes;
    private javax.swing.JPanel jPanEstaduais;
    private javax.swing.JPanel jPanFederais;
    private javax.swing.JPanel jPanMunicipais;
    private javax.swing.JPanel jPanSecundario;
    private javax.swing.JPanel jPanTributos;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexDescricaoCFOP;
    private javax.swing.JTextField jTexNomeTipoOperacao;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
