/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GFCPE0012
 */
package br.com.gfc.visao;

import br.com.gfc.controle.CLancamentos;
import br.com.gfc.dao.LancamentosDAO;
import br.com.gfc.modelo.Lancamentos;
import br.com.modelo.VerificarTecla;
import br.com.modelo.DataSistema;
import br.com.modelo.FormatarValor;
import br.com.modelo.SessaoUsuario;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 22/02/2018
 */
public class PesquisarTitulos extends javax.swing.JDialog {

    private static String sql;
    private String cdLancamento;
    private LancamentosDAO landao;
    private CLancamentos clan;
    private Lancamentos lan;
    private static Connection conexao;
    private int indexFinal;
    private int indexAtual = 0;
    private static String chamador;
    private VerificarTecla vt;
    private final DataSistema dat;
    private NumberFormat formato;
    private int numLinhas;

    // variáveis para criar novo título
    Lancamentos modlan;
    SessaoUsuario su;
    String cdCondPag;

    /**
     * Metodo para Pesquisar todos os títulos
     */
    public PesquisarTitulos(java.awt.Frame parent, boolean modal, String chamador, Connection conexao) {
        super(parent, modal);
        this.chamador = chamador;
        dat = new DataSistema();
        this.conexao = conexao;
        formato = NumberFormat.getInstance();
        setTitle("Pesquisar Títulos");
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setaInicio();
        centralizarComponente();
        mudarLinha();
    }

    /**
     * Metodo para Pesquisar todos os títulos
     */
    public PesquisarTitulos(java.awt.Frame parent, boolean modal, String chamador, Connection conexao, String sql, String cdCondpag, SessaoUsuario su) {
        super(parent, modal);
        this.chamador = chamador;
        dat = new DataSistema();
        this.conexao = conexao;
        this.sql = sql;
        this.modlan = modlan;
        this.cdCondPag = cdCondpag;
        this.su = su;
        formato = NumberFormat.getInstance();
        setTitle("Pesquisar Títulos");
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setaInicio();
        centralizarComponente();
        mudarLinha();
    }

    // setando as variáveis iniciais
    public void setaInicio() {
        lan = new Lancamentos();
        clan = new CLancamentos(conexao,su);
        if ("M".equals(chamador)) {
            sql = "select l.cd_lancamento as Lancamento,"
                    + "l.data_cadastro as Data,"
                    + "l.cpf_cnpj as Cliente,"
                    + "c.nome_razaosocial as Nome,"
                    + "l.titulo as Titulo,"
                    + "l.cd_parcela as Parcela,"
                    + "l.data_emissao as Emissao,"
                    + "l.data_vencimento as Vencimento,"
                    + "l.data_liquidacao as Liquidacao,"
                    + "l.valor_lancamento as Valor,"
                    + "l.valor_saldo as Saldo,"
                    + "l.situacao as Situacao"
                    + " from gfclancamentos as l"
                    + " left outer join pgsempresa as c on l.cpf_cnpj = c.cpf_cnpj";
        }
        buscarTodos();
        if (numLinhas > 0) {
            jTabTitulos.setModel(landao);
            ajustarTabela();
        } else {
            dispose();
        }
    }

    // criando um listener quando mudar de linha
    private void mudarLinha() {
        jTabTitulos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtual = jTabTitulos.getSelectedRow();
                ajustarTabela();
            }
        });

        // verificar tecla para captuar quando a tecla entre for pressionada
        jTabTitulos.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                vt = new VerificarTecla();
                if ("ENTER".equals(String.format("%s", vt.VerificarTecla(e)).toUpperCase()) && !"M".equals(chamador.toUpperCase())) {
                    salvarSelecao();
                }
                if("F5".equals(String.format("%s", vt.VerificarTecla(e)).toUpperCase())){
                    manutencaoTituto();
                }
            }

            @Override
            public void keyReleased(KeyEvent e
            ) {
            }
        });

    }

    // centralizar no formulario
    // método para centralizar componente
    public void centralizarComponente() {
        Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dw = getSize();
        setLocation((ds.width - dw.width) / 2, (ds.height - dw.height) / 2);
    }

    //Metodo para buscar todos
    public void buscarTodos() {
        try {
            landao = new LancamentosDAO(conexao);
            landao.setQuery(sql);
            numLinhas = landao.getRowCount();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        jTabTitulos.getColumnModel().getColumn(0).setMinWidth(70);
        jTabTitulos.getColumnModel().getColumn(0).setPreferredWidth(70);
        jTabTitulos.getColumnModel().getColumn(1).setMinWidth(70);
        jTabTitulos.getColumnModel().getColumn(1).setPreferredWidth(70);
        jTabTitulos.getColumnModel().getColumn(2).setMinWidth(110);
        jTabTitulos.getColumnModel().getColumn(2).setPreferredWidth(110);
        jTabTitulos.getColumnModel().getColumn(3).setMinWidth(240);
        jTabTitulos.getColumnModel().getColumn(3).setPreferredWidth(240);
        jTabTitulos.getColumnModel().getColumn(4).setMinWidth(60);
        jTabTitulos.getColumnModel().getColumn(4).setPreferredWidth(60);
        jTabTitulos.getColumnModel().getColumn(5).setMinWidth(25);
        jTabTitulos.getColumnModel().getColumn(5).setPreferredWidth(25);
        jTabTitulos.getColumnModel().getColumn(6).setMinWidth(70);
        jTabTitulos.getColumnModel().getColumn(6).setPreferredWidth(70);
        jTabTitulos.getColumnModel().getColumn(7).setMinWidth(70);
        jTabTitulos.getColumnModel().getColumn(7).setPreferredWidth(70);
        jTabTitulos.getColumnModel().getColumn(8).setMinWidth(70);
        jTabTitulos.getColumnModel().getColumn(8).setPreferredWidth(70);
        jTabTitulos.getColumnModel().getColumn(9).setMinWidth(60);
        jTabTitulos.getColumnModel().getColumn(9).setPreferredWidth(60);
        jTabTitulos.getColumnModel().getColumn(11).setMinWidth(50);
        jTabTitulos.getColumnModel().getColumn(11).setPreferredWidth(50);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
        cdLancamento = String.format("%s", jTabTitulos.getValueAt(indexAtual, 0));
        buscarCorrelatos();
    }

    // buscar tabelas correlatos
    private void buscarCorrelatos() {
        if (!cdLancamento.isEmpty()) {
            String sql = "select * from gfclancamentos where cd_lancamento = '"
                    + cdLancamento
                    + "'";
            try {
                clan.pesquisar(sql);
            } catch (SQLException ex) {
                Logger.getLogger(PesquisarTitulos.class.getName()).log(Level.SEVERE, null, ex);
            }
            clan.mostrarPesquisa(lan, 0);
            atualizarTela();
        }
    }

    // Atualizar registros correlatos
    private void atualizarTela() {
        jComTipoLancamento.setSelectedIndex(Integer.parseInt(lan.getTipoLancamento()));
        jComTipoMovimento.setSelectedItem(lan.getTipoMovimento());
        jForCdContraPartida.setText(lan.getContraPartida());
        jComTipoDocumento.setSelectedIndex(Integer.parseInt(lan.getTipoDocumento()));
        jTexNumDocumento.setText(lan.getDocumento());
        jForValorAtualizado.setText(String.valueOf(lan.getValorAtualizado()));
        jForCdTipoPagamento.setText(lan.getCdTipoPagamento());
        jTexNomeTipoPagamento.setText(lan.getNomeTipoPagamento());
        jComGerouArquivo.setSelectedIndex(Integer.parseInt(lan.getGerouArquivo()));
        jTexNumeroRemessa.setText(lan.getNossoNumeroBanco());
        jForCdPortador.setText(lan.getCdPortador());
        jTexNomePortador.setText(lan.getNomePortador());
        jForConta.setText(lan.getCdConta());
        jForContaDigito.setText(lan.getCdContaDig());
        jForAgencia.setText(lan.getCdAgencia());
        jForAgenciaDigito.setText(lan.getCdAgenciaDig());
        jForCdBanco.setText(lan.getCdBanco());
        jTexNomeBanco.setText(lan.getNomeBanco());
        jForTxJuros.setText(String.valueOf(lan.getTaxaJuros()));
        jForTxMulta.setText(String.valueOf(lan.getTaxaMulta()));
        jForTxCorrecao.setText(String.valueOf(lan.getTaxaCorrecao()));
        jForDiasLiquidacao.setText(String.valueOf(lan.getDiasLiquidacao()));
        jForDiasCartorio.setText(String.valueOf(lan.getDiasCartorio()));
        jForValorJuros.setText(String.valueOf(lan.getValorJuros()));
        jForValorMulta.setText(String.valueOf(lan.getValorMulta()));
        jTexCdContaReduzida.setText(lan.getCdContaReduzida());
        jTexCdCCusto.setText(lan.getCdCCusto());
    }

    // Limpar registros correlatos
    private void limparTela() {
        jComTipoLancamento.setSelectedIndex(0);
        jComTipoMovimento.setSelectedIndex(0);
        jForCdContraPartida.setText("");
        jComTipoDocumento.setSelectedIndex(0);
        jTexNumDocumento.setText("");
        jForValorAtualizado.setText("");
        jForCdTipoPagamento.setText("");
        jTexNomeTipoPagamento.setText("");
        jComGerouArquivo.setSelectedIndex(0);
        jTexNumeroRemessa.setText("");
        jForCdPortador.setText("");
        jTexNomePortador.setText("");
        jForConta.setText("");
        jForContaDigito.setText("");
        jForAgencia.setText("");
        jForAgenciaDigito.setText("");
        jForCdBanco.setText("");
        jTexNomeBanco.setText("");
        jForTxJuros.setText("");
        jForTxMulta.setText("");
        jForTxCorrecao.setText("");
        jForDiasLiquidacao.setText("");
        jForDiasCartorio.setText("");
        jForValorJuros.setText("");
        jForValorMulta.setText("");
        jTexCdContaReduzida.setText("");
        jTexCdCCusto.setText("");
    }

    // salvar seleção
    private void salvarSelecao() {
        dispose();
    }
    
    /**
     * Abrir tela do título
     */
    private void manutencaoTituto(){
        new ManterTitulos(su, conexao, cdLancamento).setVisible(true);
    }

    // capiturando o valor da linha
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanTabela = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabTitulos = new JTable(landao);
        jPanePesquisar = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jTexPesquisar = new javax.swing.JTextField();
        jPanResumo = new javax.swing.JPanel();
        jPanTitulo = new javax.swing.JPanel();
        jLabValorAtualizado = new javax.swing.JLabel();
        jSepTitulo = new javax.swing.JSeparator();
        jLabTipoDocumento = new javax.swing.JLabel();
        jComTipoDocumento = new javax.swing.JComboBox<>();
        jLabNumDocumento = new javax.swing.JLabel();
        jTexNumDocumento = new javax.swing.JTextField();
        jLabCdContraPartida = new javax.swing.JLabel();
        jForCdContraPartida = new javax.swing.JFormattedTextField();
        jForValorAtualizado = new FormatarValor(FormatarValor.NUMERO);
        jLabCdTipoPagamento = new javax.swing.JLabel();
        jForCdTipoPagamento = new javax.swing.JFormattedTextField();
        jTexNomeTipoPagamento = new javax.swing.JTextField();
        jPanRemessaBanco = new javax.swing.JPanel();
        jLabNumeroRemessa = new javax.swing.JLabel();
        jTexNumeroRemessa = new javax.swing.JTextField();
        jLabGerouArquivo = new javax.swing.JLabel();
        jComGerouArquivo = new javax.swing.JComboBox<>();
        jForCdPortador = new javax.swing.JFormattedTextField();
        jTexNomePortador = new javax.swing.JTextField();
        jLabConta = new javax.swing.JLabel();
        jForConta = new javax.swing.JFormattedTextField();
        jLabDigito = new javax.swing.JLabel();
        jForContaDigito = new javax.swing.JFormattedTextField();
        jLabAgencia = new javax.swing.JLabel();
        jForAgencia = new javax.swing.JFormattedTextField();
        jForCdBanco = new javax.swing.JFormattedTextField();
        jLabNumBanco = new javax.swing.JLabel();
        jPanJuros_Cartorio = new javax.swing.JPanel();
        jLabTxJuros = new javax.swing.JLabel();
        jForTxJuros = new FormatarValor(FormatarValor.NUMERO);
        jLabTxMulta = new javax.swing.JLabel();
        jForTxMulta = new FormatarValor(FormatarValor.NUMERO)
        ;
        jLabTxCorrecao = new javax.swing.JLabel();
        jForTxCorrecao = new FormatarValor(FormatarValor.NUMERO);
        jLabDiasLiquidacao = new javax.swing.JLabel();
        jForDiasLiquidacao = new javax.swing.JFormattedTextField();
        jLabDiasCartorio = new javax.swing.JLabel();
        jForDiasCartorio = new javax.swing.JFormattedTextField();
        jLabValorJuros = new javax.swing.JLabel();
        jForValorJuros = new FormatarValor(FormatarValor.NUMERO);
        jLabValorMulta = new javax.swing.JLabel();
        jForValorMulta = new FormatarValor(FormatarValor.NUMERO);
        jTexNomeBanco = new javax.swing.JTextField();
        jForAgenciaDigito = new javax.swing.JFormattedTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabCdContaReduzida = new javax.swing.JLabel();
        jTexCdContaReduzida = new javax.swing.JTextField();
        jLabCdCCusto = new javax.swing.JLabel();
        jTexCdCCusto = new javax.swing.JTextField();
        jLabPortador = new javax.swing.JLabel();
        jLabTipoLancamento = new javax.swing.JLabel();
        jComTipoLancamento = new javax.swing.JComboBox<>();
        jLabTipoMovimento = new javax.swing.JLabel();
        jComTipoMovimento = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Títulos");
        setResizable(false);

        jPanTabela.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabTitulos.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.SystemColor.activeCaptionBorder, null));
        jTabTitulos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabTitulos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Lancamento", "Data", "Cliente", "Nome", "Título", "Parcela", "Emissao", "Vencimento", "Liquidacao", "Valor", "Situacao"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabTitulos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabTitulosKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTabTitulos);
        if (jTabTitulos.getColumnModel().getColumnCount() > 0) {
            jTabTitulos.getColumnModel().getColumn(0).setResizable(false);
            jTabTitulos.getColumnModel().getColumn(0).setPreferredWidth(60);
            jTabTitulos.getColumnModel().getColumn(1).setResizable(false);
            jTabTitulos.getColumnModel().getColumn(1).setPreferredWidth(70);
            jTabTitulos.getColumnModel().getColumn(2).setResizable(false);
            jTabTitulos.getColumnModel().getColumn(2).setPreferredWidth(110);
            jTabTitulos.getColumnModel().getColumn(3).setResizable(false);
            jTabTitulos.getColumnModel().getColumn(3).setPreferredWidth(250);
            jTabTitulos.getColumnModel().getColumn(4).setResizable(false);
            jTabTitulos.getColumnModel().getColumn(4).setPreferredWidth(60);
            jTabTitulos.getColumnModel().getColumn(5).setResizable(false);
            jTabTitulos.getColumnModel().getColumn(5).setPreferredWidth(25);
            jTabTitulos.getColumnModel().getColumn(6).setResizable(false);
            jTabTitulos.getColumnModel().getColumn(6).setPreferredWidth(70);
            jTabTitulos.getColumnModel().getColumn(7).setResizable(false);
            jTabTitulos.getColumnModel().getColumn(7).setPreferredWidth(70);
            jTabTitulos.getColumnModel().getColumn(8).setResizable(false);
            jTabTitulos.getColumnModel().getColumn(8).setPreferredWidth(70);
            jTabTitulos.getColumnModel().getColumn(9).setResizable(false);
            jTabTitulos.getColumnModel().getColumn(9).setPreferredWidth(60);
            jTabTitulos.getColumnModel().getColumn(10).setResizable(false);
            jTabTitulos.getColumnModel().getColumn(10).setPreferredWidth(50);
        }

        jPanePesquisar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Pesquisar Produto:");

        jTexPesquisar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexPesquisarKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanePesquisarLayout = new javax.swing.GroupLayout(jPanePesquisar);
        jPanePesquisar.setLayout(jPanePesquisarLayout);
        jPanePesquisarLayout.setHorizontalGroup(
            jPanePesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanePesquisarLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanePesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTexPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 829, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanePesquisarLayout.setVerticalGroup(
            jPanePesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanePesquisarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanTabelaLayout = new javax.swing.GroupLayout(jPanTabela);
        jPanTabela.setLayout(jPanTabelaLayout);
        jPanTabelaLayout.setHorizontalGroup(
            jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(jPanePesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanTabelaLayout.setVerticalGroup(
            jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanePesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanResumo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanTitulo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Título:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabValorAtualizado.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValorAtualizado.setText("Valor Atualizado:");

        jLabTipoDocumento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTipoDocumento.setText("Tipo Documento:");

        jComTipoDocumento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "PED-Pedido", "OC-Ordem Compra", "OS-Ordem Serviço", "NFE-Nota Fiscal Entrada", "NFS-Nota Fiscal Saída", "O-Outros", " " }));

        jLabNumDocumento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNumDocumento.setText("Num. Documento:");

        jTexNumDocumento.setEditable(false);
        jTexNumDocumento.setEnabled(false);

        jLabCdContraPartida.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdContraPartida.setText("Contra-Partida:");

        jForCdContraPartida.setEditable(false);
        jForCdContraPartida.setEnabled(false);

        jForValorAtualizado.setEditable(false);
        jForValorAtualizado.setEnabled(false);

        jLabCdTipoPagamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdTipoPagamento.setText("Tipo Pagamento:");

        jForCdTipoPagamento.setEditable(false);
        jForCdTipoPagamento.setEnabled(false);

        jTexNomeTipoPagamento.setEditable(false);
        jTexNomeTipoPagamento.setEnabled(false);

        javax.swing.GroupLayout jPanTituloLayout = new javax.swing.GroupLayout(jPanTitulo);
        jPanTitulo.setLayout(jPanTituloLayout);
        jPanTituloLayout.setHorizontalGroup(
            jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTituloLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSepTitulo)
                    .addGroup(jPanTituloLayout.createSequentialGroup()
                        .addGroup(jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanTituloLayout.createSequentialGroup()
                                .addComponent(jLabCdContraPartida)
                                .addGap(1, 1, 1)
                                .addComponent(jForCdContraPartida, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabTipoDocumento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabNumDocumento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexNumDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanTituloLayout.createSequentialGroup()
                                .addComponent(jLabValorAtualizado)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForValorAtualizado, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabCdTipoPagamento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForCdTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexNomeTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 137, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanTituloLayout.setVerticalGroup(
            jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTituloLayout.createSequentialGroup()
                .addGroup(jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCdContraPartida)
                    .addComponent(jForCdContraPartida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabTipoDocumento)
                    .addComponent(jComTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabNumDocumento)
                    .addComponent(jTexNumDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabValorAtualizado)
                    .addComponent(jForValorAtualizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabCdTipoPagamento)
                    .addComponent(jForCdTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSepTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanRemessaBanco.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Remessa Banco:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabNumeroRemessa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNumeroRemessa.setText("Nosso Número:");

        jTexNumeroRemessa.setEditable(false);
        jTexNumeroRemessa.setEnabled(false);

        jLabGerouArquivo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabGerouArquivo.setText("Gerou Arquivo:");

        jComGerouArquivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não" }));

        javax.swing.GroupLayout jPanRemessaBancoLayout = new javax.swing.GroupLayout(jPanRemessaBanco);
        jPanRemessaBanco.setLayout(jPanRemessaBancoLayout);
        jPanRemessaBancoLayout.setHorizontalGroup(
            jPanRemessaBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanRemessaBancoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanRemessaBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabGerouArquivo)
                    .addComponent(jLabNumeroRemessa))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanRemessaBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jComGerouArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNumeroRemessa, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2))
        );
        jPanRemessaBancoLayout.setVerticalGroup(
            jPanRemessaBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanRemessaBancoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanRemessaBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabGerouArquivo)
                    .addComponent(jComGerouArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanRemessaBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNumeroRemessa)
                    .addComponent(jTexNumeroRemessa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jForCdPortador.setEditable(false);
        try {
            jForCdPortador.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jForCdPortador.setEnabled(false);

        jTexNomePortador.setEditable(false);
        jTexNomePortador.setEnabled(false);

        jLabConta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabConta.setText("Conta:");

        jForConta.setEditable(false);
        jForConta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jForConta.setEnabled(false);

        jLabDigito.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDigito.setText("Dígito:");

        jForContaDigito.setEditable(false);
        jForContaDigito.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jForContaDigito.setEnabled(false);

        jLabAgencia.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabAgencia.setText("Agência:");

        jForAgencia.setEditable(false);
        jForAgencia.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jForAgencia.setEnabled(false);

        jForCdBanco.setEditable(false);
        jForCdBanco.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jForCdBanco.setEnabled(false);

        jLabNumBanco.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNumBanco.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabNumBanco.setText("Núm. Banco:");

        jPanJuros_Cartorio.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabTxJuros.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTxJuros.setText("Tx. Juros:");

        jForTxJuros.setEditable(false);
        jForTxJuros.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForTxJuros.setEnabled(false);

        jLabTxMulta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTxMulta.setText("Tx. Multa:");

        jForTxMulta.setEditable(false);
        jForTxMulta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForTxMulta.setEnabled(false);

        jLabTxCorrecao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTxCorrecao.setText("Tx. Correção:");

        jForTxCorrecao.setEditable(false);
        jForTxCorrecao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForTxCorrecao.setEnabled(false);

        jLabDiasLiquidacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDiasLiquidacao.setText("Dias p/ Liquidação:");

        jForDiasLiquidacao.setEditable(false);
        jForDiasLiquidacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jForDiasLiquidacao.setEnabled(false);

        jLabDiasCartorio.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDiasCartorio.setText("Dias p/ Cartório:");

        jForDiasCartorio.setEditable(false);
        jForDiasCartorio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jForDiasCartorio.setEnabled(false);

        jLabValorJuros.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValorJuros.setText("Juros:");

        jForValorJuros.setEditable(false);
        jForValorJuros.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForValorJuros.setEnabled(false);

        jLabValorMulta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValorMulta.setText("Multa:");

        jForValorMulta.setEditable(false);
        jForValorMulta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForValorMulta.setEnabled(false);

        javax.swing.GroupLayout jPanJuros_CartorioLayout = new javax.swing.GroupLayout(jPanJuros_Cartorio);
        jPanJuros_Cartorio.setLayout(jPanJuros_CartorioLayout);
        jPanJuros_CartorioLayout.setHorizontalGroup(
            jPanJuros_CartorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanJuros_CartorioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanJuros_CartorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanJuros_CartorioLayout.createSequentialGroup()
                        .addComponent(jLabTxJuros)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForTxJuros, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabTxMulta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForTxMulta, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabTxCorrecao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForTxCorrecao, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanJuros_CartorioLayout.createSequentialGroup()
                        .addComponent(jLabDiasLiquidacao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForDiasLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabDiasCartorio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForDiasCartorio, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanJuros_CartorioLayout.createSequentialGroup()
                        .addComponent(jLabValorJuros)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForValorJuros, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabValorMulta)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForValorMulta, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanJuros_CartorioLayout.setVerticalGroup(
            jPanJuros_CartorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanJuros_CartorioLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanJuros_CartorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabTxJuros)
                    .addComponent(jForTxJuros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabTxMulta)
                    .addComponent(jForTxMulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabTxCorrecao)
                    .addComponent(jForTxCorrecao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanJuros_CartorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabDiasLiquidacao)
                    .addComponent(jForDiasLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabDiasCartorio)
                    .addComponent(jForDiasCartorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(jPanJuros_CartorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabValorJuros)
                    .addComponent(jForValorJuros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabValorMulta)
                    .addComponent(jForValorMulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTexNomeBanco.setEditable(false);
        jTexNomeBanco.setEnabled(false);

        jForAgenciaDigito.setEditable(false);
        jForAgenciaDigito.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
        jForAgenciaDigito.setEnabled(false);

        jLabCdContaReduzida.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdContaReduzida.setText("Cta Contábil Red.:");

        jTexCdContaReduzida.setEditable(false);
        jTexCdContaReduzida.setEnabled(false);

        jLabCdCCusto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdCCusto.setText("C.Custo:");

        jTexCdCCusto.setEditable(false);
        jTexCdCCusto.setEnabled(false);

        jLabPortador.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPortador.setText("Portador:");

        jLabTipoLancamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTipoLancamento.setText("Tipo Lançamento:");

        jComTipoLancamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Adiantamento", "Previsao", "Titulo" }));

        jLabTipoMovimento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTipoMovimento.setText("Tipo Movimento:");

        jComTipoMovimento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Adiantamento", "Entrada", "Pagar", "pRecebido", "Receber", "rRecebido", "Saida", "Transferência" }));

        javax.swing.GroupLayout jPanResumoLayout = new javax.swing.GroupLayout(jPanResumo);
        jPanResumo.setLayout(jPanResumoLayout);
        jPanResumoLayout.setHorizontalGroup(
            jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanResumoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanResumoLayout.createSequentialGroup()
                        .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanResumoLayout.createSequentialGroup()
                                .addComponent(jLabTipoLancamento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComTipoLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabTipoMovimento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComTipoMovimento, javax.swing.GroupLayout.PREFERRED_SIZE, 115, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanRemessaBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanResumoLayout.createSequentialGroup()
                        .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(jPanResumoLayout.createSequentialGroup()
                                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanResumoLayout.createSequentialGroup()
                                        .addComponent(jLabPortador)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jForCdPortador, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTexNomePortador, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanResumoLayout.createSequentialGroup()
                                        .addComponent(jLabConta)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jForConta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabDigito)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jForContaDigito, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabAgencia)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jForAgencia, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jForAgenciaDigito, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanResumoLayout.createSequentialGroup()
                                        .addComponent(jLabNumBanco)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jForCdBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTexNomeBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanResumoLayout.createSequentialGroup()
                                        .addComponent(jLabCdContaReduzida)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTexCdContaReduzida, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabCdCCusto)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTexCdCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanJuros_Cartorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanResumoLayout.setVerticalGroup(
            jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanResumoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabTipoLancamento)
                    .addComponent(jComTipoLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabTipoMovimento)
                    .addComponent(jComTipoMovimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanRemessaBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanJuros_Cartorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanResumoLayout.createSequentialGroup()
                        .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabPortador)
                            .addComponent(jForCdPortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTexNomePortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabConta)
                            .addComponent(jForConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabDigito)
                            .addComponent(jForContaDigito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabAgencia)
                            .addComponent(jForAgencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForAgenciaDigito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabNumBanco)
                            .addComponent(jForCdBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTexNomeBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabCdContaReduzida)
                            .addComponent(jTexCdContaReduzida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabCdCCusto)
                            .addComponent(jTexCdCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanResumo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(1, 1, 1))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanResumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTexPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexPesquisarKeyReleased
        if (!jTexPesquisar.getText().isEmpty()) {
            sql = "SELECT CD_MATERIAL AS Código,"
                    + "NOME_MATERIAL AS Descrição,"
                    + "CD_GRUPO AS Grupo,"
                    + "CD_SUBGRUPO AS SubGrupo,"
                    + "CD_CATEGORIA AS Categoria,"
                    + "CD_MARCA AS Marca,"
                    + "CD_CLASSE AS Classe,"
                    + "CD_ESSENCIA AS Essencia,"
                    + "CD_UNIDMEDIDA AS UM,"
                    + "SITUACAO AS Situacao"
                    + " FROM GCSMATERIAL"
                    + " WHERE NOME_MATERIAL LIKE '%"
                    + jTexPesquisar.getText().trim()
                    + "%'";
            //indexAtual = 0;
            buscarTodos();
            jTabTitulos.setModel(landao);
            if (jTabTitulos.getModel().getRowCount() > 0) {
                ajustarTabela();
            }
        } else {
            setaInicio();
        }
    }//GEN-LAST:event_jTexPesquisarKeyReleased

    private void jTabTitulosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabTitulosKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("ENTER".equals(vt.VerificarTecla(evt))) {
            JOptionPane.showMessageDialog(null, vt.VerificarTecla(evt));
            salvarSelecao();
        }
    }//GEN-LAST:event_jTabTitulosKeyPressed

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
            java.util.logging.Logger.getLogger(PesquisarTitulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisarTitulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisarTitulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisarTitulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PesquisarTitulos dialog = new PesquisarTitulos(new javax.swing.JFrame(), true, chamador, conexao);
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
    private javax.swing.JComboBox<String> jComGerouArquivo;
    private javax.swing.JComboBox<String> jComTipoDocumento;
    private javax.swing.JComboBox<String> jComTipoLancamento;
    private javax.swing.JComboBox<String> jComTipoMovimento;
    private javax.swing.JFormattedTextField jForAgencia;
    private javax.swing.JFormattedTextField jForAgenciaDigito;
    private javax.swing.JFormattedTextField jForCdBanco;
    private javax.swing.JFormattedTextField jForCdContraPartida;
    private javax.swing.JFormattedTextField jForCdPortador;
    private javax.swing.JFormattedTextField jForCdTipoPagamento;
    private javax.swing.JFormattedTextField jForConta;
    private javax.swing.JFormattedTextField jForContaDigito;
    private javax.swing.JFormattedTextField jForDiasCartorio;
    private javax.swing.JFormattedTextField jForDiasLiquidacao;
    private javax.swing.JFormattedTextField jForTxCorrecao;
    private javax.swing.JFormattedTextField jForTxJuros;
    private javax.swing.JFormattedTextField jForTxMulta;
    private javax.swing.JFormattedTextField jForValorAtualizado;
    private javax.swing.JFormattedTextField jForValorJuros;
    private javax.swing.JFormattedTextField jForValorMulta;
    private javax.swing.JLabel jLabAgencia;
    private javax.swing.JLabel jLabCdCCusto;
    private javax.swing.JLabel jLabCdContaReduzida;
    private javax.swing.JLabel jLabCdContraPartida;
    private javax.swing.JLabel jLabCdTipoPagamento;
    private javax.swing.JLabel jLabConta;
    private javax.swing.JLabel jLabDiasCartorio;
    private javax.swing.JLabel jLabDiasLiquidacao;
    private javax.swing.JLabel jLabDigito;
    private javax.swing.JLabel jLabGerouArquivo;
    private javax.swing.JLabel jLabNumBanco;
    private javax.swing.JLabel jLabNumDocumento;
    private javax.swing.JLabel jLabNumeroRemessa;
    private javax.swing.JLabel jLabPortador;
    private javax.swing.JLabel jLabTipoDocumento;
    private javax.swing.JLabel jLabTipoLancamento;
    private javax.swing.JLabel jLabTipoMovimento;
    private javax.swing.JLabel jLabTxCorrecao;
    private javax.swing.JLabel jLabTxJuros;
    private javax.swing.JLabel jLabTxMulta;
    private javax.swing.JLabel jLabValorAtualizado;
    private javax.swing.JLabel jLabValorJuros;
    private javax.swing.JLabel jLabValorMulta;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanJuros_Cartorio;
    private javax.swing.JPanel jPanRemessaBanco;
    private javax.swing.JPanel jPanResumo;
    private javax.swing.JPanel jPanTabela;
    private javax.swing.JPanel jPanTitulo;
    private javax.swing.JPanel jPanePesquisar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSepTitulo;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTabTitulos;
    private javax.swing.JTextField jTexCdCCusto;
    private javax.swing.JTextField jTexCdContaReduzida;
    private javax.swing.JTextField jTexNomeBanco;
    private javax.swing.JTextField jTexNomePortador;
    private javax.swing.JTextField jTexNomeTipoPagamento;
    private javax.swing.JTextField jTexNumDocumento;
    private javax.swing.JTextField jTexNumeroRemessa;
    private javax.swing.JTextField jTexPesquisar;
    // End of variables declaration//GEN-END:variables

}
