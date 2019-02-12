/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa: GFCCA0061
 */
package br.com.gfc.visao;

// Objetos da classe ManerParcelasCondiçãoPagamento
import br.com.gfc.modelo.ParcelasCondicaoPagamento;
import br.com.gfc.dao.ParcelasCondicaoPagamentoDAO;
import br.com.gfc.controle.CParcelasCondicaoPagamento;

// Objetos da Classe ManterCondiçãoPagamento
import br.com.gfc.modelo.CondicaoPagamento;

// Objetos de ambiente
import java.sql.Connection;
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.DefineCampoInteiro;
import br.com.modelo.FormatarValor;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;

// Objetos de api
import java.sql.Date;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 11/11/2017
 */
public class ManterParcelaCondicaoPagamento extends javax.swing.JDialog {

    // Variáveis de instância generalistas
    private final NumberFormat formato;
    private static Connection conexao;
    private static SessaoUsuario su;
    private VerificarTecla vt;
    private double oldPerRateio = 0;
    private boolean changePercRateio = false;
    private String oldSituacao = "";
    private final double maxRateio = 100;

    // Variáveis de instância da classe ManterParcelasCondicaoPagamento
    private static ParcelasCondicaoPagamento modpcp;
    private ParcelasCondicaoPagamento regCorr;
    private List< ParcelasCondicaoPagamento> resultado;
    private CParcelasCondicaoPagamento cpcp;
    private ParcelasCondicaoPagamentoDAO pcpdao;
    private int numReg = 0;
    private int idxCorr = 0;
    private static String cdCondPag;
    private static int sequencia;
    private static int numParcelas;
    private double totalRateio;
    private double residuo;

    // Variáeis de instância da Classe ManterCondicaoPagamento
    private static CondicaoPagamento modcp;
    private String sql;
    private String sqlatsv;
    private String oper;
    private int indexAtual = 0;

    /**
     * Creates new form MaterTarefaAtividade
     */
    public ManterParcelaCondicaoPagamento(java.awt.Frame parent, boolean modal, CondicaoPagamento modcp, int sequencia,
            int numParcelas, SessaoUsuario su, Connection conexao) {
        // carregando os objetos passador por referência
        super(parent, modal);
        this.modcp = modcp;
        this.su = su;
        this.conexao = conexao;
        this.cdCondPag = modcp.getCdCondpag();
        this.sequencia = sequencia;
        this.numParcelas = numParcelas;
        this.totalRateio = modcp.getTotalRateio();
        this.residuo = modcp.getResiduo();

        // instanciando os objetos da classe
        cpcp = new CParcelasCondicaoPagamento(conexao);
        modpcp = new ParcelasCondicaoPagamento();
        try {
            pcpdao = new ParcelasCondicaoPagamentoDAO(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(ManterParcelaCondicaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Instância as API´s de Sistema
        formato = NumberFormat.getInstance();
        formato.setMinimumFractionDigits(2);

        // Inicia as variáveis gerais
        sql = "SELECT * FROM GFCPARCELACONDPAG WHERE CD_CONDPAG = '"  + cdCondPag
                + "'";
        oper = "N";

        // Invoca métodos para carregar os registros
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formatarCampos();
        limparTela();
        setLocationRelativeTo(null);
        pesquisar();
        this.dispose();
    }

    // método para setar o formato do campo
    private void formatarCampos() {
        jForPrazoDias.setDocument(new DefineCampoInteiro());
        jForPercRateio.setDocument(new DefineCampoDecimal());
        jForResidual.setDocument(new DefineCampoDecimal());
        jForTotalRateio.setDocument(new DefineCampoDecimal());
        jTexNumParcelas.setDocument(new DefineCampoInteiro());
    }

    // método para limpar tela
    private void limparTela() {
        jForPrazoDias.setText("0");
        jForPercRateio.setText("0.00");
        jTexCadastradoPor.setText("");
        jForDataCadastro.setText("");
        jForDataModificacao.setText("");
        jTexRegAtual.setText("");
        jTexRegTotal.setText("");
        jForPrazoDias.requestFocus();
        jForPrazoDias.selectAll();
        //
        jTexNumParcelas.setText(String.valueOf(modcp.getNumParcelas()));
        jForResidual.setText(String.valueOf(residuo));
        jForTotalRateio.setText(String.valueOf(pcpdao.getTotalRateio()));
    }

    //Metodo para buscar todos
    public void buscarTodos() {
        sqlatsv = "select pcp.cd_parcela as Parcela,"
                + "pcp.prazo_dias as Prazo,"
                + "format(pcp.perc_rateio,3) as '% Rateio'"
                + " from gfcparcelacondpag as pcp "
                + "where pcp.cd_condpag = '" + modcp.getCdCondpag()
                + "' order by pcp.cd_parcela";
        try {
            pcpdao.setQuery(sqlatsv);
            jForTotalRateio.setText(String.valueOf(pcpdao.getTotalRateio()));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
        jTabParcelas.setModel(pcpdao);
        ajustarTabela();
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {
        try {
            numReg = cpcp.pesquisar(sql);
            idxCorr = +1;
            if (numReg > 0) {
                upRegistros();
                buscarTodos();
            } else {
                JOptionPane.showMessageDialog(null, "Nenhum registro encontrado!");
                esvaziarTabelas();
                jForTotalRateio.setText("0,00");
            }
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Argumento inválido na busca\nErr: " + iae.getCause());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Pesquisar os Registros\nErr: " + ex);
            ex.printStackTrace();
        }
    }

    // método para atualizar os registros na tela
    private void upRegistros() {
        jTexRegTotal.setText(Integer.toString(numReg));
        jTexRegAtual.setText(Integer.toString(idxCorr));
        cpcp.mostrarPesquisa(modpcp, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jForPrazoDias.setText(String.valueOf(modpcp.getPrazoDias()));
        jForPercRateio.setText(String.valueOf(modpcp.getPercRateio()));
        oldPerRateio = modpcp.getPercRateio();
        oldSituacao = modpcp.getSituacao();
        jTexCadastradoPor.setText(modpcp.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modpcp.getDataCadastro())));
        changePercRateio = false;
        if (modpcp.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(modpcp.getDataModificacao())));
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
        // Habilitando/Desabilitando botão novo
        if (numReg >= modcp.getNumParcelas()) {
            jButNovo.setEnabled(false);
        } else {
            jButNovo.setEnabled(true);
        }
    }

    // método para bloquear campos
    private void bloquearCampos() {
        jForPrazoDias.setEditable(false);
        jForPrazoDias.setEnabled(false);
        jForPercRateio.setEditable(false);
        jForPercRateio.setEnabled(false);
    }

    // método para liberar campos
    private void liberarCampos() {
        jForPrazoDias.setEditable(true);
        jForPrazoDias.setEnabled(true);
        jForPercRateio.setEditable(true);
        jForPercRateio.setEnabled(true);
    }

    // método para criar novo registro
    private void novoRegistro() {
        limparTela();
        liberarCampos();
        jButSalvar.setEnabled(true);
    }

    private void esvaziarTabelas() {
        jTabParcelas.setModel(new JTable().getModel());
    }

    // método para salvar registro
    private void salvarRegistro() {
        if (jForPrazoDias.getText().isEmpty() || jForPercRateio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Os campos Prazo e % Rateio são obrigatórios.");
        } else {
            DataSistema dat = new DataSistema();
            ParcelasCondicaoPagamento pcp = new ParcelasCondicaoPagamento();
            String data = null;
            pcp.setPrazoDias(Integer.parseInt(jForPrazoDias.getText()));
            pcp.setCdCondpag(cdCondPag);
            try {
                pcp.setPercRateio(formato.parse(jForPercRateio.getText()).doubleValue());
            } catch (ParseException ex) {
                Logger.getLogger(ManterParcelaCondicaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
            }
            dat.setData(data);
            data = dat.getData();
            pcp.setSituacao("S");
            pcp.setDataCadastro(data);
            pcp.setUsuarioCadastro(su.getUsuarioConectado());
            ParcelasCondicaoPagamentoDAO pcpdao = null;
            CParcelasCondicaoPagamento cpcp = new CParcelasCondicaoPagamento(conexao);
            try {
                pcpdao = new ParcelasCondicaoPagamentoDAO(conexao);
                if ("N".equals(oper)) {
                    residuo -= pcp.getPercRateio();
                    sequencia += 1;
                    pcp.setCdParcela(sequencia);
                    sql = "SELECT * FROM GFCPARCELACONDPAG WHERE CD_CONDPAG = " + pcp.getCdCondpag().trim();
                    pcpdao.adicionar(pcp);
                    modcp.setAtualizar(true);
                } else {
                    pcp.setCdParcela(modpcp.getCdParcela());
                    sql = "SELECT * FROM GFCPARCELACONDPAG WHERE CD_CONDPAG = " + modpcp.getCdCondpag();
                    pcp.setCdCondpag(modpcp.getCdCondpag());
                    pcp.setDataModificacao(data);
                    pcpdao.atualizar(pcp);
                    if (oldPerRateio != pcp.getPercRateio()) {
                        double percRateioAtual = pcp.getPercRateio();
                        changePercRateio = true;
                        if (oldPerRateio > pcp.getPercRateio()) {
                            residuo += (oldPerRateio - percRateioAtual);
                        } else if (oldPerRateio < pcp.getPercRateio()) {
                            residuo -= (pcp.getPercRateio() - oldPerRateio);
                        }
                        modpcp.setDataModificacao(data);
                        modcp.setAtualizar(true);
                    }
                }
            } catch (SQLException sqlex) {
                JOptionPane.showMessageDialog(null, "Erro criar a Atividade no banco e dados!\nErr: " + sqlex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral para criar Atividade!\nErr: " + ex);
            }

            limparTela();
            bloquearCampos();
            pesquisar();
            jButSalvar.setEnabled(false);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        jTabParcelas.getColumnModel().getColumn(0).setMinWidth(10);
        jTabParcelas.getColumnModel().getColumn(0).setPreferredWidth(10);
        jTabParcelas.getColumnModel().getColumn(1).setMinWidth(10);
        jTabParcelas.getColumnModel().getColumn(1).setPreferredWidth(10);
        jTabParcelas.getColumnModel().getColumn(2).setMinWidth(20);
        jTabParcelas.getColumnModel().getColumn(2).setPreferredWidth(20);
        if (indexAtual < 0) {
            indexAtual = 0;
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

        jPanParcelasPagamento = new javax.swing.JPanel();
        jLabPrazoDias = new javax.swing.JLabel();
        jLabPercRateio = new javax.swing.JLabel();
        jForPercRateio = new FormatarValor((FormatarValor.PORCENTAGEM));
        jForPrazoDias = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabTotalRateio = new javax.swing.JLabel();
        jLabResidual = new javax.swing.JLabel();
        jForResidual = new FormatarValor(FormatarValor.PORCENTAGEM);
        jLabel1 = new javax.swing.JLabel();
        jTexNumParcelas = new javax.swing.JTextField();
        jTexNumParcelas.setText(String.valueOf(modcp.getNumParcelas()));
        jForTotalRateio = new FormatarValor(FormatarValor.PORCENTAGEM)
        ;
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
        jPanTabelaParcelas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabParcelas = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manter Parcelas");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setModal(true);

        jPanParcelasPagamento.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Incluir Parcelas de Pagamento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabPrazoDias.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPrazoDias.setText("Prazo:");

        jLabPercRateio.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPercRateio.setText("% Rateiro:");

        jForPercRateio.setEditable(false);
        jForPercRateio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##0.000"))));
        jForPercRateio.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jForPercRateio.setEnabled(false);

        jForPrazoDias.setEditable(false);
        jForPrazoDias.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jForPrazoDias.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jForPrazoDias.setEnabled(false);
        jForPrazoDias.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                jForPrazoDiasFocusLost(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabTotalRateio.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTotalRateio.setText("Total Rateio:");

        jLabResidual.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabResidual.setText("Residual:");

        jForResidual.setEditable(false);
        jForResidual.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##0.000"))));
        jForResidual.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("N. Parc.:");

        jTexNumParcelas.setEditable(false);

        jForTotalRateio.setEditable(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabTotalRateio)
                    .addComponent(jLabResidual))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jForResidual, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                    .addComponent(jForTotalRateio))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexNumParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabTotalRateio)
                    .addComponent(jLabel1)
                    .addComponent(jTexNumParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jForTotalRateio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabResidual)
                    .addComponent(jForResidual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanParcelasPagamentoLayout = new javax.swing.GroupLayout(jPanParcelasPagamento);
        jPanParcelasPagamento.setLayout(jPanParcelasPagamentoLayout);
        jPanParcelasPagamentoLayout.setHorizontalGroup(
            jPanParcelasPagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanParcelasPagamentoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanParcelasPagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabPercRateio, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabPrazoDias, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanParcelasPagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jForPercRateio)
                    .addComponent(jForPrazoDias, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanParcelasPagamentoLayout.setVerticalGroup(
            jPanParcelasPagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanParcelasPagamentoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanParcelasPagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanParcelasPagamentoLayout.createSequentialGroup()
                        .addGroup(jPanParcelasPagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabPrazoDias)
                            .addComponent(jForPrazoDias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanParcelasPagamentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabPercRateio)
                            .addComponent(jForPercRateio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap())
        );

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
                    .addGap(22, 22, 22)
                    .addComponent(jLabCadastradoPor)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanRodapeLayout.createSequentialGroup()
                            .addComponent(jLabDataModificacao)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanRodapeLayout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addComponent(jLabDataCadastro)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForDataCadastro)))
                    .addGap(13, 13, 13))
            );
            jPanRodapeLayout.setVerticalGroup(
                jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanRodapeLayout.createSequentialGroup()
                    .addContainerGap(15, Short.MAX_VALUE)
                    .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabCadastradoPor)
                        .addGroup(jPanRodapeLayout.createSequentialGroup()
                            .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabDataCadastro)
                                .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabDataModificacao)
                                .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexRegAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexRegTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabReg))))
                    .addContainerGap())
            );

            jPanTabelaParcelas.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Parcelas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jTabParcelas.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabParcelas.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null},
                    {null, null, null}
                },
                new String [] {
                    "Parcela", "Prazo", "% Rateio"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.Integer.class, java.lang.Integer.class, java.lang.Double.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jScrollPane1.setViewportView(jTabParcelas);
            if (jTabParcelas.getColumnModel().getColumnCount() > 0) {
                jTabParcelas.getColumnModel().getColumn(0).setResizable(false);
                jTabParcelas.getColumnModel().getColumn(0).setPreferredWidth(10);
                jTabParcelas.getColumnModel().getColumn(1).setResizable(false);
                jTabParcelas.getColumnModel().getColumn(1).setPreferredWidth(10);
                jTabParcelas.getColumnModel().getColumn(2).setResizable(false);
                jTabParcelas.getColumnModel().getColumn(2).setPreferredWidth(20);
            }

            javax.swing.GroupLayout jPanTabelaParcelasLayout = new javax.swing.GroupLayout(jPanTabelaParcelas);
            jPanTabelaParcelas.setLayout(jPanTabelaParcelasLayout);
            jPanTabelaParcelasLayout.setHorizontalGroup(
                jPanTabelaParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTabelaParcelasLayout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );
            jPanTabelaParcelasLayout.setVerticalGroup(
                jPanTabelaParcelasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, 491, Short.MAX_VALUE)
                .addComponent(jPanParcelasPagamento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanTabelaParcelas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanRodape, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanParcelasPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(26, 26, 26)
                    .addComponent(jPanTabelaParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButNovoActionPerformed
        // TODO add your handling code here:
        novoRegistro();
        oper = "N";
        jForPrazoDias.requestFocus();
        jForPrazoDias.selectAll();
    }//GEN-LAST:event_jButNovoActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        // TODO add your handling code here:
        liberarCampos();
        jForPrazoDias.requestFocus();
        oper = "A";
        jButSalvar.setEnabled(true);
    }//GEN-LAST:event_jButEditarActionPerformed

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        // TODO add your handling code here:
        salvarRegistro();

    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
        // TODO add your handling code here:
        limparTela();
        oper = "N";
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        // TODO add your handling code here:
        if (!jForPrazoDias.getText().isEmpty()) {
            try {
                double percRaterioExcluido = modpcp.getPercRateio();
                DataSistema dat = new DataSistema();
                String data = null;
                ParcelasCondicaoPagamento xx = new ParcelasCondicaoPagamento();
                xx.setCdCondpag(cdCondPag);
                xx.setCdParcela(modpcp.getCdParcela());
                ParcelasCondicaoPagamentoDAO xxDAO = new ParcelasCondicaoPagamentoDAO(conexao);
                xxDAO.excluir(xx);
                dat.setData(data);
                data = dat.getData();
                residuo += percRaterioExcluido;
                modcp.setDataModificacao(data);
                modcp.setAtualizar(true);
                limparTela();
                jButPesquisarActionPerformed(evt);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Não foi possível excluir o registro da tela");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral na exclusão do registro!");
            }
        }
    }//GEN-LAST:event_jButExcluirActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:
        sql = "SELECT * FROM GFCPARCELACONDPAG WHERE CD_CONDPAG = " + cdCondPag;
        bloquearCampos();
        pesquisar();
    }//GEN-LAST:event_jButPesquisarActionPerformed

    private void jButAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAnteriorActionPerformed
        // TODO add your handling code here:
        idxCorr -= 1;
        upRegistros();
    }//GEN-LAST:event_jButAnteriorActionPerformed

    private void jButProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButProximoActionPerformed
        // TODO add your handling code here:
        idxCorr += 1;
        upRegistros();
    }//GEN-LAST:event_jButProximoActionPerformed

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jForPrazoDiasFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jForPrazoDiasFocusLost
        // TODO add your handling code here:
        jForPercRateio.setText(String.valueOf(residuo));
    }//GEN-LAST:event_jForPrazoDiasFocusLost

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
            java.util.logging.Logger.getLogger(ManterParcelaCondicaoPagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterParcelaCondicaoPagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterParcelaCondicaoPagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterParcelaCondicaoPagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ManterParcelaCondicaoPagamento dialog = new ManterParcelaCondicaoPagamento(new javax.swing.JFrame(), true, modcp, sequencia, numParcelas, su, conexao);
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
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JFormattedTextField jForPercRateio;
    private javax.swing.JFormattedTextField jForPrazoDias;
    private javax.swing.JFormattedTextField jForResidual;
    private javax.swing.JFormattedTextField jForTotalRateio;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabPercRateio;
    private javax.swing.JLabel jLabPrazoDias;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabResidual;
    private javax.swing.JLabel jLabTotalRateio;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanParcelasPagamento;
    private javax.swing.JPanel jPanRodape;
    private javax.swing.JPanel jPanTabelaParcelas;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabParcelas;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexNumParcelas;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
