/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GFRPE0041
 */
package br.com.gfr.visao;

import br.com.gfc.visao.*;
import br.com.gfr.controle.CTiposOperacoes;
import br.com.gfr.dao.TiposOperacoesDAO;
import br.com.gfr.modelo.TiposOperacoes;
import br.com.modelo.VerificarTecla;
import br.com.modelo.DataSistema;
import br.com.modelo.FormatarValor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 07/11/2017
 */
public class PesquisarTiposOperacoes extends javax.swing.JDialog {

    private static Connection conexao;
    private String sql;
    private String cdTipoOpercao;
    private String nomeTipoOperacao;
    private TiposOperacoesDAO topdao;
    private CTiposOperacoes ctop;
    private TiposOperacoes top;
    private int indexFinal;
    private int indexAtual = 0;
    private static String chamador;
    private VerificarTecla vt;
    private final DataSistema dat;

    /**
     * Creates new form PesquisaModelo
     */
    public PesquisarTiposOperacoes(java.awt.Frame parent, boolean modal, String chamador, Connection conexao) {
        super(parent, modal);
        this.chamador = chamador;
        this.conexao = conexao;
        dat = new DataSistema();
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisa de Tipo de Operação");
        setaInicio();
        centralizarComponente();
        mudarLinha();
    }

    // setando as variáveis iniciais
    public void setaInicio() {
        top = new TiposOperacoes();
        ctop = new CTiposOperacoes(conexao);
        sql = "select t.cd_tipooper as 'Tip.Oper',"
                + "t.nome_operacao,"
                + "t.cd_cfop as 'C.F.O.P',"
                + "t.situacao as Sit"
                + " from gfrtipooperacao as t "
                + " order by t.cd_tipooper";
        buscarTodos();
        jTabTipoOperacao.setModel(topdao);
        ajustarTabela();
    }

    // criando um listener quando mudar de linha
    private void mudarLinha() {
        jTabTipoOperacao.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtual = jTabTipoOperacao.getSelectedRow();
                ajustarTabela();
            }
        });

        // verificar tecla para captuar quando a tecla entre for pressionada
        jTabTipoOperacao.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                vt = new VerificarTecla();
                if ("ENTER".equals(String.format("%s", vt.VerificarTecla(e)).toUpperCase()) && !"M".equals(chamador.toUpperCase())) {
                    salvarSelecao();
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
            topdao = new TiposOperacoesDAO(conexao);
            topdao.setQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        jTabTipoOperacao.getColumnModel().getColumn(0).setMinWidth(30);
        jTabTipoOperacao.getColumnModel().getColumn(0).setPreferredWidth(30);
        jTabTipoOperacao.getColumnModel().getColumn(1).setMinWidth(300);
        jTabTipoOperacao.getColumnModel().getColumn(1).setPreferredWidth(300);
        jTabTipoOperacao.getColumnModel().getColumn(2).setMinWidth(30);
        jTabTipoOperacao.getColumnModel().getColumn(2).setPreferredWidth(30);
        jTabTipoOperacao.getColumnModel().getColumn(3).setMinWidth(20);
        jTabTipoOperacao.getColumnModel().getColumn(3).setPreferredWidth(20);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
        jTexSelecao1.setText(String.format("%s", jTabTipoOperacao.getValueAt(indexAtual, 2)));
        jTexSelecao2.setText(String.format("%s", jTabTipoOperacao.getValueAt(indexAtual, 1)));
        cdTipoOpercao = String.format("%s", jTabTipoOperacao.getValueAt(indexAtual, 0));
        cdTipoOpercao = cdTipoOpercao.replace(".", "");
        buscarCorrelatos();
    }

    // buscar tabelas correlatos
    private void buscarCorrelatos() {
        if (!cdTipoOpercao.isEmpty()) {
            String sql = "select * from gfrtipooperacao where cd_tipooper = "
                    + cdTipoOpercao
                    + "";
            try {
                ctop.pesquisar(sql);
            } catch (SQLException ex) {
                Logger.getLogger(PesquisarTiposOperacoes.class.getName()).log(Level.SEVERE, null, ex);
            }
            ctop.mostrarPesquisa(top, 0);
            atualizarTela();
        }
    }

    // Atualizar registros correlatos
    private void atualizarTela() {
        jForAliquotaPis.setText(String.valueOf(top.getAliquotaPis()));
        jForAliquotacCofins.setText(String.valueOf(top.getAliquotaCofins()));
        jForAliquotaSimples.setText(String.valueOf(top.getAliquotaSimples()));
        jForAliquotaIpi.setText(String.valueOf(top.getAliquotaIpi()));
        jForAliquotaIcms.setText(String.valueOf(top.getAliquotaIcms()));
        jForAliquotaSuframa.setText(String.valueOf(top.getAliquotaSuframa()));
        jForAliquotaSimbahia.setText(String.valueOf(top.getAliquotaSimbahia()));
        jComTributaIcms.setSelectedIndex(Integer.parseInt(top.getTributaIcms()));
        jComTributaIpi.setSelectedIndex(Integer.parseInt(top.getTributaIpi()));
        jComTributaSuframa.setSelectedIndex(Integer.parseInt(top.getTributaSuframa()));
        jComTributaSimbahia.setSelectedIndex(Integer.parseInt(top.getTributaSimbahia()));
        jForBaseCalIcmsOper.setText(String.valueOf(top.getBaseCalculoIcmsOp()));
        jForIcmsOpBaseRed.setText(String.valueOf(top.getIcmsOpBaseRed()));
        jForMva.setText(String.valueOf(top.getMva()));
        jForBaseIcmsStRed.setText(String.valueOf(top.getBaseIcmsStRed()));
        jForIcmsCadeiaSemRed.setText(String.valueOf(top.getIcmsCadeiaSemRed()));
        jForAliquotaIss.setText(String.valueOf(top.getAliquotaIss()));
        jTexCadastradoPor.setText(top.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(top.getDataCadastro())));
        if (top.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(top.getDataModificacao())));
        }
    }

    // salvar seleção
    private void salvarSelecao() {
        cdTipoOpercao = top.getCdTipoOper();
        nomeTipoOperacao = top.getNomeTipoOperacao();
        dispose();
    }
    
    // método para retornar o tipo de operação pesquisado
    public String getcdTipoOper(){
        return cdTipoOpercao;
    }
    // Método para retorno o nome do tipo de operação pesquisado
    public String getNomeTipoOper(){
        return nomeTipoOperacao;
    }

    //
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
        jTabTipoOperacao = new JTable(topdao);
        jPanePesquisar = new javax.swing.JPanel();
        jLabPesquisar = new javax.swing.JLabel();
        jTexPesquisar = new javax.swing.JTextField();
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
        jLabel1 = new javax.swing.JLabel();
        jTexSelecao1 = new javax.swing.JTextField();
        jTexSelecao2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Tipo de Operação");
        setResizable(false);

        jPanTabela.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabTipoOperacao.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.SystemColor.activeCaptionBorder, null));
        jTabTipoOperacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabTipoOperacao.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Tip.Oper", "Nome", "C.F.O.P", "Sit"
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
        jTabTipoOperacao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabTipoOperacaoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTabTipoOperacao);
        if (jTabTipoOperacao.getColumnModel().getColumnCount() > 0) {
            jTabTipoOperacao.getColumnModel().getColumn(0).setResizable(false);
            jTabTipoOperacao.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTabTipoOperacao.getColumnModel().getColumn(1).setResizable(false);
            jTabTipoOperacao.getColumnModel().getColumn(1).setPreferredWidth(300);
            jTabTipoOperacao.getColumnModel().getColumn(2).setResizable(false);
            jTabTipoOperacao.getColumnModel().getColumn(2).setPreferredWidth(30);
            jTabTipoOperacao.getColumnModel().getColumn(3).setResizable(false);
            jTabTipoOperacao.getColumnModel().getColumn(3).setPreferredWidth(20);
        }

        jPanePesquisar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabPesquisar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPesquisar.setText("Pesquisar Portador:");

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
                    .addComponent(jLabPesquisar)
                    .addComponent(jTexPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 576, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanePesquisarLayout.setVerticalGroup(
            jPanePesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanePesquisarLayout.createSequentialGroup()
                .addComponent(jLabPesquisar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 8, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanTabelaLayout = new javax.swing.GroupLayout(jPanTabela);
        jPanTabela.setLayout(jPanTabelaLayout);
        jPanTabelaLayout.setHorizontalGroup(
            jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanePesquisar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
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

        jPanTributos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Tributos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jPanFederais.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Federais", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabAliqPis.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabAliqPis.setText("PIS:");

        jForAliquotaPis.setEditable(false);
        jForAliquotaPis.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForAliquotaPis.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jForAliquotaPis.setEnabled(false);

        jLabAliqCofins.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabAliqCofins.setText("Cofins:");

        jForAliquotacCofins.setEditable(false);
        jForAliquotacCofins.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForAliquotacCofins.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jForAliquotacCofins.setEnabled(false);

        jLabAliqSimplesNacional.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabAliqSimplesNacional.setText("Simples Nacional:");

        jForAliquotaSimples.setEditable(false);
        jForAliquotaSimples.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForAliquotaSimples.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jForAliquotaSimples.setEnabled(false);

        jForAliquotaIpi.setEditable(false);
        jForAliquotaIpi.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForAliquotaIpi.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jForAliquotaIpi.setEnabled(false);

        jLabTributaIpi.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTributaIpi.setText("Tributa IPI:");

        jComTributaIpi.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não", "Regra" }));
        jComTributaIpi.setEnabled(false);

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
                .addGap(18, 18, 18)
                .addComponent(jLabTributaIpi)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jComTributaIpi, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForAliquotaIpi, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
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

        jForAliquotaIcms.setEditable(false);
        jForAliquotaIcms.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForAliquotaIcms.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jForAliquotaIcms.setEnabled(false);

        jForAliquotaSuframa.setEditable(false);
        jForAliquotaSuframa.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForAliquotaSuframa.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jForAliquotaSuframa.setEnabled(false);

        jForAliquotaSimbahia.setEditable(false);
        jForAliquotaSimbahia.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForAliquotaSimbahia.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jForAliquotaSimbahia.setEnabled(false);

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

        jForBaseCalIcmsOper.setEditable(false);
        jForBaseCalIcmsOper.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForBaseCalIcmsOper.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jForBaseCalIcmsOper.setEnabled(false);

        jForIcmsOpBaseRed.setEditable(false);
        jForIcmsOpBaseRed.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForIcmsOpBaseRed.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jForIcmsOpBaseRed.setEnabled(false);

        jForMva.setEditable(false);
        jForMva.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForMva.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jForMva.setEnabled(false);

        jForBaseIcmsStRed.setEditable(false);
        jForBaseIcmsStRed.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForBaseIcmsStRed.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jForBaseIcmsStRed.setEnabled(false);

        jForIcmsCadeiaSemRed.setEditable(false);
        jForIcmsCadeiaSemRed.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForIcmsCadeiaSemRed.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jForIcmsCadeiaSemRed.setEnabled(false);

        jComTributaIcms.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não", "Regra" }));
        jComTributaIcms.setEnabled(false);

        jComTributaSuframa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não", "Regra" }));
        jComTributaSuframa.setEnabled(false);

        jComTributaSimbahia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não", "Regra" }));
        jComTributaSimbahia.setEnabled(false);

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
                            .addComponent(jForIcmsOpBaseRed, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanEstaduaisLayout.createSequentialGroup()
                                .addComponent(jForBaseCalIcmsOper, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabMva)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForMva, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanEstaduaisLayout.createSequentialGroup()
                        .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanEstaduaisLayout.createSequentialGroup()
                                .addComponent(jLabBaseIcmsStRed)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForBaseIcmsStRed, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanEstaduaisLayout.createSequentialGroup()
                                .addComponent(jLabIcmsCadeiaSemRed)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForIcmsCadeiaSemRed, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(106, 106, 106)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanEstaduaisLayout.setVerticalGroup(
            jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanEstaduaisLayout.createSequentialGroup()
                .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabBaseCalIcmsOper)
                        .addComponent(jForBaseCalIcmsOper, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabMva)
                        .addComponent(jForMva, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                            .addComponent(jLabIcmsCadeiaSemRed)
                            .addComponent(jForIcmsCadeiaSemRed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanEstaduaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabBaseIcmsStRed)
                            .addComponent(jForBaseIcmsStRed, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
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
                .addContainerGap())
        );

        jPanMunicipais.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Municipais", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabAliqIss.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabAliqIss.setText("I.S.S.:");

        jForAliquotaIss.setEditable(false);
        jForAliquotaIss.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));
        jForAliquotaIss.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jForAliquotaIss.setEnabled(false);

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
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanTributosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanTributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanMunicipais, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanEstaduais, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanFederais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanTributosLayout.setVerticalGroup(
            jPanTributosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTributosLayout.createSequentialGroup()
                .addComponent(jPanFederais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanEstaduais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanMunicipais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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
                    .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabDataCadastro)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabDataModificacao)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(97, 97, 97))
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

            jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel1.setText("C.F.O.P:");

            jTexSelecao1.setEditable(false);
            jTexSelecao1.setEnabled(false);

            jTexSelecao2.setEditable(false);
            jTexSelecao2.setEnabled(false);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jPanTabela, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanTributos, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGap(0, 0, Short.MAX_VALUE))
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabel1)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel1)
                        .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 14, Short.MAX_VALUE)
                    .addComponent(jPanTributos, javax.swing.GroupLayout.PREFERRED_SIZE, 269, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jTexPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexPesquisarKeyReleased
        if (!jTexPesquisar.getText().isEmpty()) {
            sql = "select t.cd_tipooper as 'Tip.Oper',"
                    + "t.nome_operacao,"
                    + "t.cd_cfop as 'C.F.O.P',"
                    + "t.situacao as Sit"
                    + " from gfrtipooperacao as t "
                    + " WHERE t.nome_operacao LIKE '%"
                    + jTexPesquisar.getText().trim()
                    + "%' order by t.cd_tipooper";
            //indexAtual = 0;
            buscarTodos();
            jTabTipoOperacao.setModel(topdao);
            if (jTabTipoOperacao.getModel().getRowCount() > 0) {
                ajustarTabela();
            }
        } else {
            setaInicio();
        }
    }//GEN-LAST:event_jTexPesquisarKeyReleased

    private void jTabTipoOperacaoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabTipoOperacaoKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("ENTER".equals(vt.VerificarTecla(evt))) {
            JOptionPane.showMessageDialog(null, vt.VerificarTecla(evt));
            salvarSelecao();
        }
    }//GEN-LAST:event_jTabTipoOperacaoKeyPressed

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
            java.util.logging.Logger.getLogger(PesquisarTiposOperacoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisarTiposOperacoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisarTiposOperacoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisarTiposOperacoes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PesquisarTiposOperacoes dialog = new PesquisarTiposOperacoes(new javax.swing.JFrame(), true, chamador, conexao);
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
    private javax.swing.JLabel jLabMva;
    private javax.swing.JLabel jLabPesquisar;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabTributaIcms;
    private javax.swing.JLabel jLabTributaIpi;
    private javax.swing.JLabel jLabTributaSimbahia;
    private javax.swing.JLabel jLabTributaSuframa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanBotoes;
    private javax.swing.JPanel jPanEstaduais;
    private javax.swing.JPanel jPanFederais;
    private javax.swing.JPanel jPanMunicipais;
    private javax.swing.JPanel jPanTabela;
    private javax.swing.JPanel jPanTributos;
    private javax.swing.JPanel jPanePesquisar;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabTipoOperacao;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexPesquisar;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JTextField jTexSelecao1;
    private javax.swing.JTextField jTexSelecao2;
    // End of variables declaration//GEN-END:variables

}
