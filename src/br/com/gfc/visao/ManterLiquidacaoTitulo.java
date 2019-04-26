/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa: GFCMO0011
 */
package br.com.gfc.visao;

import br.com.gfc.controle.CLancamentos;
import br.com.gfc.modelo.Historico;
import br.com.gfc.modelo.Lancamentos;
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.DefineCampoInteiro;
import br.com.modelo.HoraSistema;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.FormatarValor;
import br.com.modelo.VerificarTecla;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa criado em 23/07/2018
 */
public class ManterLiquidacaoTitulo extends javax.swing.JDialog {

    // Objetos de instância da classe
    private static Connection conexao;
    private static SessaoUsuario su;
    private DataSistema dat;
    private HoraSistema hs;
    private NumberFormat formato;

    // Objetos da classe
    private Lancamentos modlan;
    private CLancamentos clan;

    // Variáveis de instância da classe
    private static String cdLancamento;
    private String sql;
    private String valorPago;
    private String dataLiquidacao;
    private boolean liquidacaoCnab = false;

    /**
     * Contrututor padrão da classe
     *
     * @param conexao objeto com parâmetro de conexao ativa do usuário
     * @param su objeto com sessão ativa do usuário
     * @param cdLancamento String com o lançamento que será liquidado
     */
    public ManterLiquidacaoTitulo(java.awt.Frame parent, boolean modal, Connection conexao, SessaoUsuario su, String cdLancamento) {
        super(parent, modal);
        this.conexao = conexao;
        this.su = su;
        this.cdLancamento = cdLancamento;
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        formato = NumberFormat.getInstance();
        formatarCampos();
        popularTipoMovimento();
        sql = "select * from gfclancamentos where cd_lancamento = '" + cdLancamento
                + "'";
        buscarLancamento();
    }

    /**
     * Construto padrão para gerar a liquidação do título via retorno de EDI
     *
     * @param parent
     * @param modal
     * @param conexao
     * @param su
     * @param cdLancamento
     * @param valorLiquidacao
     * @param dataLiquidacao
     */
    public ManterLiquidacaoTitulo(java.awt.Frame parent, boolean modal, Connection conexao, SessaoUsuario su, String cdLancamento, boolean liquidacaoCbab, double valorLiquidacao, String dataLiquidacao) {
        super(parent, modal);
        this.conexao = conexao;
        this.su = su;
        this.cdLancamento = cdLancamento;
        this.liquidacaoCnab = liquidacaoCbab;
        this.valorPago = String.valueOf(valorLiquidacao);
        this.dataLiquidacao = dataLiquidacao;
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        formato = NumberFormat.getInstance();
        formatarCampos();
        popularTipoMovimento();
        sql = "select * from gfclancamentos where cd_lancamento = '" + cdLancamento
                + "'";
        buscarLancamento();
        JOptionPane.showMessageDialog(null, "ValorLiquidacao: " + valorLiquidacao + "\nData Liquidacao: " + dataLiquidacao);
    }

    private void formatarCampos() {
        jForCdLancamento.setDocument(new DefineCampoInteiro());
        jForCdTitulo.setDocument(new DefineCampoInteiro());
        jForValorLancamento.setDocument(new DefineCampoDecimal());
        jForValorPago.setDocument(new DefineCampoDecimal());
        jForValorSaldo.setDocument(new DefineCampoDecimal());
    }

    /**
     * Método para buscar o lançamento que será liquidado
     */
    private void buscarLancamento() {
        modlan = new Lancamentos();
        clan = new CLancamentos(conexao, su);
        try {
            if (clan.pesquisar(sql) > 0) {
                clan.mostrarPesquisa(modlan, 0);
                upRegistros();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManterLiquidacaoTitulo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void upRegistros() {
        dat = new DataSistema();
        jForCdLancamento.setText(modlan.getCdLancamento());
        jForCdTitulo.setText(modlan.getTitulo());
        jComTipoMovimento.setSelectedItem(modlan.getTipoMovimento());
        jForDataEmissao.setText(dat.getDataConv(Date.valueOf(modlan.getDataEmissao())));
        jForDataVencimento.setText(dat.getDataConv(Date.valueOf(modlan.getDataVencimento())));
        jComSituacao.setSelectedIndex(Integer.parseInt(modlan.getSituacao()));
        jForCpfCnpj.setText(modlan.getCpfCnpj());
        jTexNomeCliente.setText(modlan.getNomeRazaoSocial());
        jForValorLancamento.setText(String.valueOf(modlan.getValorLancamento()));
        jForValorSaldo.setText(String.valueOf(modlan.getValorSaldo()));
        String tipoDoc = modlan.getTipoDocumento();
        switch (tipoDoc) {
            case "1":
                modlan.setTipoDocumento("PED");
                break;
            case "2":
                modlan.setTipoDocumento("OC");
                break;
            case "3":
                modlan.setTipoDocumento("OS");
                break;
            case "4":
                modlan.setTipoDocumento("NFE");
                break;
            case "5":
                modlan.setTipoDocumento("NFS");
                break;
            case "6":
                modlan.setTipoDocumento("O");
                break;
            default:
                modlan.setTipoDocumento("O");
                break;
        }
        String gerouArquivo = modlan.getGerouArquivo();
        switch (gerouArquivo) {
            case "1":
                modlan.setGerouArquivo("S");
                break;
            case "2":
                modlan.setGerouArquivo("N");
                break;
            default:
                modlan.setGerouArquivo("N");
                break;
        }
        String tipoLanc = modlan.getTipoLancamento();
        switch (tipoLanc) {
            case "1":
                modlan.setTipoLancamento("Adi");
                break;
            case "2":
                modlan.setTipoLancamento("Pre");
                break;
            case "3":
                modlan.setTipoLancamento("Tit");
                break;
            default:
                modlan.setTipoLancamento("Tit");
                break;
        }
        if (liquidacaoCnab) {
            jForValorPago.setText(valorPago.toString());
            jForDataLiquidacao.setText(dataLiquidacao.replace("-", "/"));
        }
    }

    /**
     * Método privado para popular o combobox TipoMovimento
     */
    private void popularTipoMovimento() {
        String prefixoLancamento;
        String sql = "select concat(a.cd_tipomovimento,'-',m.nome_tipomovimento) as TipoMovimento,"
                + " l.prefixo as prefixo"
                + " from gfctipolancmovi as a"
                + " left outer join gfctipomovimento as m on a.cd_tipomovimento = m.cd_tipomovimento"
                + " left outer join gfctipolancamento as l on a.cd_tipolancamento = l.cd_tipolancamento";
        PreparedStatement pstmt = null;
        try {
            pstmt = conexao.prepareCall(sql);
            ResultSet rs = pstmt.executeQuery();
            jComTipoMovimento.removeAllItems();
            jComTipoMovimento.addItem(" ");
            while (rs.next()) {
                jComTipoMovimento.addItem(rs.getString("TipoMovimento"));
                prefixoLancamento = rs.getString("prefixo");
            }
            pstmt.close();
        } catch (SQLException ex) {
            JOptionPane.showInputDialog(null, ex);
        }
    }

    /**
     * Método para fazer a chamar a liquidação do título
     */
    private void gerarLiquidacao() {
        modlan.setContraPartida(cdLancamento);
        modlan.setDataLiquidacao(jForDataLiquidacao.getText());
        modlan.setTipoMovimento(jComTipoMovimento.getSelectedItem().toString().substring(0, 2));
        Double valorPago = 0.00;
        Double valorAtual = 0.00;
        Double valorSaldo = 0.00;
        String situacaoParcela = "AB";
        String dataLiquidacao = jForDataLiquidacao.getText();
        try {
            valorPago = formato.parse(jForValorPago.getText()).doubleValue();
            valorAtual = formato.parse(jForValorSaldo.getText()).doubleValue();
            if (valorPago >= valorAtual) {
                valorSaldo = 0.00;
                situacaoParcela = "LI";
            } else {
                valorSaldo = (valorAtual - valorPago);
                situacaoParcela = "AB";
            }
        } catch (ParseException ex) {
            Logger.getLogger(ManterLiquidacaoTitulo.class.getName()).log(Level.SEVERE, null, ex);
        }
        modlan.setCdHistorico(jTexCdHistorico.getText());
        modlan.setComplemento(jTextAreaComplementoHistorico.getText());
        if (clan.liquidarTituto(modlan, valorPago, dataLiquidacao, valorSaldo, situacaoParcela) == 0) {
            JOptionPane.showMessageDialog(null, "Lancamento liquidado com sucesso!");
            buscarLancamento();
        }
    }

    /**
     * Método para pesquisar históricos financeiros
     */
    private void zoomHistorico() {
        PesquisarHistoricos zoom = new PesquisarHistoricos(new JFrame(), rootPaneCheckingEnabled, "P", conexao, su);
        zoom.setVisible(rootPaneCheckingEnabled);
        if (zoom.isSalvar()) {
            Historico hs = zoom.getHt();
            jTexCdHistorico.setText(hs.getCdHistorico());
            String ch = hs.getNomeHistorico();
            if ("1".equals(hs.getDocumentoComplementa())) {
                ch = String.format("%s", ch.trim() + " - Documento: " + jForCdTitulo.getText());
            }
            if ("1".equals(hs.getEmpresaComplementa())) {
                ch = String.format("%s", ch.trim() + " - Empresa: " + jTexNomeCliente.getText());
            }
            if ("1".equals(hs.getEmissaoComplementa())) {
                ch = String.format("%s", ch.trim() + " - Emissão: " + jForDataEmissao.getText());
            }
            jTextAreaComplementoHistorico.setText(ch.trim());
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

        jPanel1 = new javax.swing.JPanel();
        jLabLancamento = new javax.swing.JLabel();
        jLabTitulo = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jComTipoMovimento = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jLabDataEmissao = new javax.swing.JLabel();
        jForDataEmissao = new javax.swing.JFormattedTextField();
        jLabDataVencimento = new javax.swing.JLabel();
        jForDataVencimento = new javax.swing.JFormattedTextField();
        jLabSituacao = new javax.swing.JLabel();
        jComSituacao = new javax.swing.JComboBox<>();
        jPanLiquidacao = new javax.swing.JPanel();
        jLabValorTitulo = new javax.swing.JLabel();
        jForValorLancamento = new FormatarValor(FormatarValor.NUMERO);
        jLabValorPago = new javax.swing.JLabel();
        jLabSaldo = new javax.swing.JLabel();
        jForValorSaldo = new FormatarValor(FormatarValor.NUMERO);
        jLabDataLiquidacao = new javax.swing.JLabel();
        jForDataLiquidacao = new javax.swing.JFormattedTextField();
        jForValorPago = new FormatarValor(FormatarValor.NUMERO);
        jLabCdHistorico = new javax.swing.JLabel();
        jTexCdHistorico = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaComplementoHistorico = new javax.swing.JTextArea();
        jLabel2 = new javax.swing.JLabel();
        jTexNomeCliente = new javax.swing.JTextField();
        jForCpfCnpj = new javax.swing.JFormattedTextField();
        jForCdLancamento = new javax.swing.JFormattedTextField();
        jForCdTitulo = new javax.swing.JFormattedTextField();
        jPanBotoes = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Luidação de Título");

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabLancamento.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabLancamento.setText("Lançamento:");

        jLabTitulo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabTitulo.setText("Título:");

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("Tipo Movimento:");

        jComTipoMovimento.setEnabled(false);

        jLabDataEmissao.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabDataEmissao.setText("Data de Emissão:");

        jForDataEmissao.setEditable(false);
        try {
            jForDataEmissao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jForDataEmissao.setEnabled(false);

        jLabDataVencimento.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabDataVencimento.setText("Data de Vencimento:");

        jForDataVencimento.setEditable(false);
        try {
            jForDataVencimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jForDataVencimento.setEnabled(false);

        jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabSituacao.setText("Situação:");

        jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "AB-Aberto", "CA-Cancelado", "CO-Contabilizado", "LI-Liquidado" }));
        jComSituacao.setEnabled(false);

        jPanLiquidacao.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Liquidação", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabValorTitulo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabValorTitulo.setText("Valor do Título:");

        jForValorLancamento.setEditable(false);
        jForValorLancamento.setEnabled(false);

        jLabValorPago.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabValorPago.setText("Valor Pago:");

        jLabSaldo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabSaldo.setText("Saldo:");

        jForValorSaldo.setEditable(false);
        jForValorSaldo.setEnabled(false);

        jLabDataLiquidacao.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabDataLiquidacao.setText("Data Liquidação:");

        try {
            jForDataLiquidacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabCdHistorico.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabCdHistorico.setText("Hitórico de Liquidação:");

        jTexCdHistorico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdHistoricoKeyPressed(evt);
            }
        });

        jTextAreaComplementoHistorico.setColumns(20);
        jTextAreaComplementoHistorico.setLineWrap(true);
        jTextAreaComplementoHistorico.setRows(5);
        jTextAreaComplementoHistorico.setWrapStyleWord(true);
        jScrollPane1.setViewportView(jTextAreaComplementoHistorico);

        javax.swing.GroupLayout jPanLiquidacaoLayout = new javax.swing.GroupLayout(jPanLiquidacao);
        jPanLiquidacao.setLayout(jPanLiquidacaoLayout);
        jPanLiquidacaoLayout.setHorizontalGroup(
            jPanLiquidacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanLiquidacaoLayout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addGroup(jPanLiquidacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanLiquidacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanLiquidacaoLayout.createSequentialGroup()
                            .addComponent(jLabValorTitulo)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForValorLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanLiquidacaoLayout.createSequentialGroup()
                            .addComponent(jLabValorPago)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForValorPago, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanLiquidacaoLayout.createSequentialGroup()
                        .addComponent(jLabCdHistorico)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexCdHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(18, 18, 18)
                .addGroup(jPanLiquidacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanLiquidacaoLayout.createSequentialGroup()
                        .addComponent(jLabSaldo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForValorSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanLiquidacaoLayout.createSequentialGroup()
                        .addComponent(jLabDataLiquidacao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForDataLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanLiquidacaoLayout.createSequentialGroup()
                .addGap(174, 174, 174)
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanLiquidacaoLayout.setVerticalGroup(
            jPanLiquidacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanLiquidacaoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanLiquidacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabValorTitulo)
                    .addComponent(jForValorLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabDataLiquidacao)
                    .addComponent(jForDataLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanLiquidacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabValorPago)
                    .addComponent(jLabSaldo)
                    .addComponent(jForValorSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jForValorPago, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanLiquidacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCdHistorico)
                    .addComponent(jTexCdHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Cliente:");

        jTexNomeCliente.setEditable(false);
        jTexNomeCliente.setEnabled(false);

        jForCpfCnpj.setEditable(false);
        jForCpfCnpj.setEnabled(false);

        jForCdLancamento.setEditable(false);
        jForCdLancamento.setEnabled(false);

        jForCdTitulo.setEditable(false);
        jForCdTitulo.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanLiquidacao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabLancamento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForCdLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabTitulo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForCdTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComTipoMovimento, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabDataEmissao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForDataEmissao, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabDataVencimento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForDataVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabSituacao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComSituacao, 0, 149, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabLancamento)
                    .addComponent(jLabTitulo)
                    .addComponent(jLabel1)
                    .addComponent(jComTipoMovimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jForCdLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jForCdTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabDataEmissao)
                    .addComponent(jForDataEmissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabDataVencimento)
                    .addComponent(jForDataVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabSituacao)
                    .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTexNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jForCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanBotoes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton1.setText("Liquidar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanBotoesLayout = new javax.swing.GroupLayout(jPanBotoes);
        jPanBotoes.setLayout(jPanBotoesLayout);
        jPanBotoesLayout.setHorizontalGroup(
            jPanBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanBotoesLayout.setVerticalGroup(
            jPanBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanBotoesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanBotoes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        gerarLiquidacao();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jTexCdHistoricoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdHistoricoKeyPressed
        VerificarTecla vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt))) {
            zoomHistorico();
        }
    }//GEN-LAST:event_jTexCdHistoricoKeyPressed

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
            java.util.logging.Logger.getLogger(ManterLiquidacaoTitulo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterLiquidacaoTitulo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterLiquidacaoTitulo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterLiquidacaoTitulo.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ManterLiquidacaoTitulo dialog = new ManterLiquidacaoTitulo(new javax.swing.JFrame(), true, conexao, su, cdLancamento);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JComboBox<String> jComTipoMovimento;
    private javax.swing.JFormattedTextField jForCdLancamento;
    private javax.swing.JFormattedTextField jForCdTitulo;
    private javax.swing.JFormattedTextField jForCpfCnpj;
    private javax.swing.JFormattedTextField jForDataEmissao;
    private javax.swing.JFormattedTextField jForDataLiquidacao;
    private javax.swing.JFormattedTextField jForDataVencimento;
    private javax.swing.JFormattedTextField jForValorLancamento;
    private javax.swing.JFormattedTextField jForValorPago;
    private javax.swing.JFormattedTextField jForValorSaldo;
    private javax.swing.JLabel jLabCdHistorico;
    private javax.swing.JLabel jLabDataEmissao;
    private javax.swing.JLabel jLabDataLiquidacao;
    private javax.swing.JLabel jLabDataVencimento;
    private javax.swing.JLabel jLabLancamento;
    private javax.swing.JLabel jLabSaldo;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabTitulo;
    private javax.swing.JLabel jLabValorPago;
    private javax.swing.JLabel jLabValorTitulo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanBotoes;
    private javax.swing.JPanel jPanLiquidacao;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTexCdHistorico;
    private javax.swing.JTextField jTexNomeCliente;
    private javax.swing.JTextArea jTextAreaComplementoHistorico;
    // End of variables declaration//GEN-END:variables
}
