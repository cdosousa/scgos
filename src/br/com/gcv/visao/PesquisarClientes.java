/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GCVPE0021
 */
package br.com.gcv.visao;

// Objetos do Registro Pai da Classe
import br.com.gcv.controle.CClientes;
import br.com.gcv.dao.ClientesDAO;
import br.com.gcv.modelo.Clientes;

// Objetos de instância de parâmetros de ambiente
import br.com.modelo.VerificarTecla;
import br.com.modelo.DataSistema;

// Objetos de API´s do sistema
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 17/11/2017
 */
public class PesquisarClientes extends javax.swing.JDialog {

    // Variávies de instância dos objetos de parâmetros da classe
    private static Connection conexao;
    private final DataSistema dat;
    private VerificarTecla vt;

    // Variáveis de instância dos objetos de registro da classe
    private Clientes cli;
    private ClientesDAO clidao;
    private CClientes ccli;

    // Variáveis de instância gerais da classe
    private static String chamador;
    private String sql;
    private String cdCpfCnpj;
    private int indexFinal;
    private int indexAtual = 0;
    private String selecao1;
    private String selecao2;

    /**
     * Creates new form PesquisaModelo
     */
    public PesquisarClientes(java.awt.Frame parent, boolean modal, String chamador, Connection conexao) {
        super(parent, modal);
        this.chamador = chamador;
        this.conexao = conexao;
        dat = new DataSistema();
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisa de Clientes");
        setaInicio();
        centralizarComponente();
        mudarLinha();
    }

    // setando as variáveis iniciais
    private void setaInicio() {
        cli = new Clientes();
        ccli = new CClientes(conexao);
        sql = "select c.cpf_cnpj as 'CPF / CNPJ',"
                + "c.nome_razaosocial as 'Nome / Razão Social',"
                + "c.apelido as Apelido,"
                + "c.contato as 'Nome Contato',"
                + "c.email as 'E-mail',"
                + "c.telefone as Telefone,"
                + "c.celular as Celular,"
                + "c.situacao as Sit"
                + " from gcvclientes as c"
                + " order by c.nome_razaosocial";
        buscarTodos();
        jTabClientes.setModel(clidao);
        ajustarTabela();
    }

    // criando um listener quando mudar de linha
    private void mudarLinha() {
        jTabClientes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtual = jTabClientes.getSelectedRow();
                ajustarTabela();
            }
        });

        // verificar tecla para captuar quando a tecla entre for pressionada
        jTabClientes.addKeyListener(new KeyListener() {
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
    private void centralizarComponente() {
        Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dw = getSize();
        setLocation((ds.width - dw.width) / 2, (ds.height - dw.height) / 2);
    }

    //Metodo para buscar todos
    private void buscarTodos() {
        try {
            clidao = new ClientesDAO(conexao);
            clidao.setQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela
    private void ajustarTabela() {
        jTabClientes.getColumnModel().getColumn(0).setMinWidth(90);
        jTabClientes.getColumnModel().getColumn(0).setPreferredWidth(90);
        jTabClientes.getColumnModel().getColumn(1).setMinWidth(300);
        jTabClientes.getColumnModel().getColumn(1).setPreferredWidth(300);
        jTabClientes.getColumnModel().getColumn(2).setMinWidth(150);
        jTabClientes.getColumnModel().getColumn(2).setPreferredWidth(150);
        jTabClientes.getColumnModel().getColumn(3).setMinWidth(100);
        jTabClientes.getColumnModel().getColumn(3).setPreferredWidth(100);
        jTabClientes.getColumnModel().getColumn(4).setMinWidth(200);
        jTabClientes.getColumnModel().getColumn(4).setPreferredWidth(200);
        jTabClientes.getColumnModel().getColumn(5).setMinWidth(90);
        jTabClientes.getColumnModel().getColumn(5).setPreferredWidth(90);
        jTabClientes.getColumnModel().getColumn(6).setMinWidth(90);
        jTabClientes.getColumnModel().getColumn(6).setPreferredWidth(90);
        jTabClientes.getColumnModel().getColumn(7).setMinWidth(20);
        jTabClientes.getColumnModel().getColumn(7).setPreferredWidth(20);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
        cdCpfCnpj = String.format("%s", jTabClientes.getValueAt(indexAtual, 0));
        jTexSelecao1.setText(String.format("%s", jTabClientes.getValueAt(indexAtual, 0)));
        jTexSelecao2.setText(String.format("%s", jTabClientes.getValueAt(indexAtual, 1)));
        buscarCorrelatos();
    }

    // buscar tabelas correlatos
    private void buscarCorrelatos() {
        if (!cdCpfCnpj.isEmpty()) {
            String sql = "select * from gcvclientes where cpf_cnpj = "
                    + cdCpfCnpj
                    + "";
            try {
                ccli.pesquisar(sql);
            } catch (SQLException ex) {
                Logger.getLogger(PesquisarClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
            ccli.mostrarPesquisa(cli, 0);
            atualizarTela();
        }
    }

    // Atualizar registros correlatos
    private void atualizarTela() {
        jTexTipoLogradouro.setText(cli.getTipoLogradouro());
        jTexLogradouro.setText(cli.getLogradouro());
        jTexNumero.setText(cli.getNumero());
        jTexComplemento.setText(cli.getComplemento());
        jTexBairro.setText(cli.getBairro());
        jForCep.setText(cli.getCdCep());
        jTexUF.setText(cli.getSiglaUf());
        jTexMunicipio.setText(cli.getNomeMunicipio());
        jComOptanteSimples.setSelectedIndex(Integer.parseInt(cli.getOptanteSimples()));
        jComOptanteSimbahia.setSelectedIndex(Integer.parseInt(cli.getOptanteSimbahia()));
        jComOptanteSuframa.setSelectedIndex(Integer.parseInt(cli.getOptanteSuframa()));
        jTexNumBanco.setText(cli.getNumBanco());
        jTexNomeBanco.setText(cli.getNomeBanco());
        jTexAgenciaBanco.setText(cli.getAgenciaBanco());
        jTexNumContaBanco.setText(cli.getNumContaBanco());
        jTexCdPortador.setText(cli.getCdPortador());
        jTexNomePortador.setText(cli.getNomePortador());
        jTexCdTipoPagamento.setText(cli.getCdTipoPagamento());
        jTexNomeTipoPagamento.setText(cli.getNomeTipoPagamento());
        jTexCdCondPag.setText(cli.getCdCondPagamento());
        jTexNomeCondPag.setText(cli.getNomeCondPag());
        jTexCadastradoPor.setText(cli.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(cli.getDataCadastro())));
        jForDataModificacao.setText(cli.getDataModificacao());
    }

    // salvar seleção
    private void salvarSelecao() {
        selecao1 = jTexSelecao1.getText().trim();
        selecao2 = jTexSelecao2.getText().trim();
        dispose();
    }

    /**
     * @return the selecao1
     */
    public String getSelecao1() {
        return selecao1;
    }

    /**
     * @return the selecao2
     */
    public String getSelecao2() {
        return selecao2;
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
        jTabClientes = new JTable(clidao);
        jPanePesquisar = new javax.swing.JPanel();
        jLabPesquisar = new javax.swing.JLabel();
        jTexPesquisar = new javax.swing.JTextField();
        jPanResumo = new javax.swing.JPanel();
        jPanInforTecnico = new javax.swing.JPanel();
        jPanEndereco = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jTexTipoLogradouro = new javax.swing.JTextField();
        jTexLogradouro = new javax.swing.JTextField();
        jLabNomeCategoria = new javax.swing.JLabel();
        jTexNumero = new javax.swing.JTextField();
        jLabComplemento = new javax.swing.JLabel();
        jTexComplemento = new javax.swing.JTextField();
        jLabBairro = new javax.swing.JLabel();
        jTexBairro = new javax.swing.JTextField();
        jLabUF = new javax.swing.JLabel();
        jTexUF = new javax.swing.JTextField();
        jLabMunicipio = new javax.swing.JLabel();
        jTexMunicipio = new javax.swing.JTextField();
        jLabCEP = new javax.swing.JLabel();
        jForCep = new javax.swing.JFormattedTextField();
        jLabTecnico = new javax.swing.JLabel();
        jTexSelecao1 = new javax.swing.JTextField();
        jTexSelecao2 = new javax.swing.JTextField();
        jPanel1 = new javax.swing.JPanel();
        jLabCadastradoPor = new javax.swing.JLabel();
        jLabDataCadastro = new javax.swing.JLabel();
        jLabDataModificacao = new javax.swing.JLabel();
        jForDataCadastro = new javax.swing.JFormattedTextField();
        jTexCadastradoPor = new javax.swing.JTextField();
        jForDataModificacao = new javax.swing.JFormattedTextField();
        jPanInformacoesBancarias = new javax.swing.JPanel();
        jLabNumBanco = new javax.swing.JLabel();
        jTexNumBanco = new javax.swing.JTextField();
        jTexNomeBanco = new javax.swing.JTextField();
        jLabAgenciaBanco = new javax.swing.JLabel();
        jTexAgenciaBanco = new javax.swing.JTextField();
        jTexNumContaBanco = new javax.swing.JTextField();
        jPanInformacoesFinanceiras = new javax.swing.JPanel();
        jLabCdPortador = new javax.swing.JLabel();
        jTexCdPortador = new javax.swing.JTextField();
        jLabCdTipoPagamento = new javax.swing.JLabel();
        jTexNomePortador = new javax.swing.JTextField();
        jTexCdTipoPagamento = new javax.swing.JTextField();
        jTexNomeTipoPagamento = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jTexCdCondPag = new javax.swing.JTextField();
        jTexNomeCondPag = new javax.swing.JTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabOptanteSimples = new javax.swing.JLabel();
        jComOptanteSimples = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        jComOptanteSimbahia = new javax.swing.JComboBox<>();
        jLabel4 = new javax.swing.JLabel();
        jComOptanteSuframa = new javax.swing.JComboBox<>();
        jLabCdSuframa = new javax.swing.JLabel();
        jTexCdSuframa = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Clientes");
        setResizable(false);

        jPanTabela.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabClientes.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.SystemColor.activeCaptionBorder, null));
        jTabClientes.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabClientes.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CPF / CNPJ", "Nome / Razão Social ", "Apelido", "Nome Contato", "email", "Telefone", "Celular", "Sit"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabClientes.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabClientesKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTabClientes);
        if (jTabClientes.getColumnModel().getColumnCount() > 0) {
            jTabClientes.getColumnModel().getColumn(0).setResizable(false);
            jTabClientes.getColumnModel().getColumn(0).setPreferredWidth(90);
            jTabClientes.getColumnModel().getColumn(1).setResizable(false);
            jTabClientes.getColumnModel().getColumn(1).setPreferredWidth(300);
            jTabClientes.getColumnModel().getColumn(2).setResizable(false);
            jTabClientes.getColumnModel().getColumn(2).setPreferredWidth(150);
            jTabClientes.getColumnModel().getColumn(3).setResizable(false);
            jTabClientes.getColumnModel().getColumn(3).setPreferredWidth(100);
            jTabClientes.getColumnModel().getColumn(4).setResizable(false);
            jTabClientes.getColumnModel().getColumn(4).setPreferredWidth(200);
            jTabClientes.getColumnModel().getColumn(5).setResizable(false);
            jTabClientes.getColumnModel().getColumn(5).setPreferredWidth(90);
            jTabClientes.getColumnModel().getColumn(6).setResizable(false);
            jTabClientes.getColumnModel().getColumn(6).setPreferredWidth(90);
            jTabClientes.getColumnModel().getColumn(7).setResizable(false);
            jTabClientes.getColumnModel().getColumn(7).setPreferredWidth(20);
        }

        jPanePesquisar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabPesquisar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPesquisar.setText("Pesquisar Clientes:");

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
                    .addComponent(jLabPesquisar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanePesquisarLayout.setVerticalGroup(
            jPanePesquisarLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanePesquisarLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabPesquisar)
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

        jPanInforTecnico.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações do Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jPanEndereco.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Endereço", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Tipo:");

        jTexTipoLogradouro.setEditable(false);
        jTexTipoLogradouro.setEnabled(false);

        jTexLogradouro.setEditable(false);
        jTexLogradouro.setEnabled(false);

        jLabNomeCategoria.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNomeCategoria.setText("Num.:");

        jTexNumero.setEditable(false);
        jTexNumero.setEnabled(false);

        jLabComplemento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabComplemento.setText("Complemento:");

        jTexComplemento.setEditable(false);
        jTexComplemento.setEnabled(false);

        jLabBairro.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabBairro.setText("Bairro:");

        jTexBairro.setEditable(false);
        jTexBairro.setEnabled(false);

        jLabUF.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabUF.setText("U.F.:");

        jTexUF.setEditable(false);
        jTexUF.setEnabled(false);

        jLabMunicipio.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabMunicipio.setText("Munic.:");

        jTexMunicipio.setEditable(false);
        jTexMunicipio.setEnabled(false);

        jLabCEP.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCEP.setText("CEP:");

        jForCep.setEditable(false);
        try {
            jForCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jForCep.setEnabled(false);

        javax.swing.GroupLayout jPanEnderecoLayout = new javax.swing.GroupLayout(jPanEndereco);
        jPanEndereco.setLayout(jPanEnderecoLayout);
        jPanEnderecoLayout.setHorizontalGroup(
            jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanEnderecoLayout.createSequentialGroup()
                        .addComponent(jLabBairro)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTexBairro))
                    .addGroup(jPanEnderecoLayout.createSequentialGroup()
                        .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabNomeCategoria))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                                .addComponent(jTexNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabComplemento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexComplemento))
                            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                                .addComponent(jTexTipoLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 399, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanEnderecoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabUF)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTexUF, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabMunicipio)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTexMunicipio)))
                .addContainerGap())
            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabCEP)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jForCep, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanEnderecoLayout.setVerticalGroup(
            jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTexTipoLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(jTexLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeCategoria)
                    .addComponent(jTexNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabComplemento)
                    .addComponent(jTexComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabBairro)
                    .addComponent(jTexBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabUF)
                    .addComponent(jTexUF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabMunicipio)
                    .addComponent(jTexMunicipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCEP)
                    .addComponent(jForCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanInforTecnicoLayout = new javax.swing.GroupLayout(jPanInforTecnico);
        jPanInforTecnico.setLayout(jPanInforTecnicoLayout);
        jPanInforTecnicoLayout.setHorizontalGroup(
            jPanInforTecnicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInforTecnicoLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanInforTecnicoLayout.setVerticalGroup(
            jPanInforTecnicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInforTecnicoLayout.createSequentialGroup()
                .addComponent(jPanEndereco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jLabTecnico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTecnico.setText("Cliente:");

        jTexSelecao1.setEditable(false);
        jTexSelecao1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexSelecao1.setEnabled(false);

        jTexSelecao2.setEditable(false);
        jTexSelecao2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexSelecao2.setEnabled(false);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabCadastradoPor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCadastradoPor.setText("Cadastrado por:");

        jLabDataCadastro.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDataCadastro.setText("Data Cadastro:");

        jLabDataModificacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDataModificacao.setText("Data Modificação:");

        jForDataCadastro.setEditable(false);
        jForDataCadastro.setEnabled(false);

        jTexCadastradoPor.setEditable(false);
        jTexCadastradoPor.setEnabled(false);

        jForDataModificacao.setEditable(false);
        jForDataModificacao.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabCadastradoPor)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabDataCadastro)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(jLabDataModificacao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCadastradoPor)
                    .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabDataCadastro)
                    .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabDataModificacao)
                    .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(17, Short.MAX_VALUE))
        );

        jPanInformacoesBancarias.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações Bancárias", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabNumBanco.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNumBanco.setText("Banco:");

        jLabAgenciaBanco.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabAgenciaBanco.setText("Agência / Conta:");

        javax.swing.GroupLayout jPanInformacoesBancariasLayout = new javax.swing.GroupLayout(jPanInformacoesBancarias);
        jPanInformacoesBancarias.setLayout(jPanInformacoesBancariasLayout);
        jPanInformacoesBancariasLayout.setHorizontalGroup(
            jPanInformacoesBancariasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInformacoesBancariasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanInformacoesBancariasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanInformacoesBancariasLayout.createSequentialGroup()
                        .addComponent(jLabNumBanco)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexNumBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexNomeBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanInformacoesBancariasLayout.createSequentialGroup()
                        .addComponent(jLabAgenciaBanco)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexAgenciaBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexNumContaBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanInformacoesBancariasLayout.setVerticalGroup(
            jPanInformacoesBancariasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInformacoesBancariasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanInformacoesBancariasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNumBanco)
                    .addComponent(jTexNumBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanInformacoesBancariasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabAgenciaBanco)
                    .addComponent(jTexAgenciaBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNumContaBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanInformacoesFinanceiras.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações Financeiras", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabCdPortador.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdPortador.setText("Portador:");

        jLabCdTipoPagamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdTipoPagamento.setText("Tipo Pgto.:");

        jTexNomePortador.setEnabled(false);

        jTexNomeTipoPagamento.setEnabled(false);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel7.setText("Cond. Pgto.:");

        jTexNomeCondPag.setEnabled(false);

        javax.swing.GroupLayout jPanInformacoesFinanceirasLayout = new javax.swing.GroupLayout(jPanInformacoesFinanceiras);
        jPanInformacoesFinanceiras.setLayout(jPanInformacoesFinanceirasLayout);
        jPanInformacoesFinanceirasLayout.setHorizontalGroup(
            jPanInformacoesFinanceirasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInformacoesFinanceirasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanInformacoesFinanceirasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabCdTipoPagamento, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabCdPortador, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanInformacoesFinanceirasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTexCdCondPag, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                    .addComponent(jTexCdTipoPagamento)
                    .addComponent(jTexCdPortador))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanInformacoesFinanceirasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTexNomeTipoPagamento, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                    .addComponent(jTexNomePortador)
                    .addComponent(jTexNomeCondPag))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanInformacoesFinanceirasLayout.setVerticalGroup(
            jPanInformacoesFinanceirasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInformacoesFinanceirasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanInformacoesFinanceirasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCdPortador)
                    .addComponent(jTexCdPortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomePortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanInformacoesFinanceirasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCdTipoPagamento)
                    .addComponent(jTexCdTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanInformacoesFinanceirasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jTexCdCondPag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeCondPag, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabOptanteSimples.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabOptanteSimples.setText("Optante Simples:");

        jComOptanteSimples.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não" }));
        jComOptanteSimples.setMaximumSize(new java.awt.Dimension(70, 40));
        jComOptanteSimples.setMinimumSize(new java.awt.Dimension(70, 20));
        jComOptanteSimples.setPreferredSize(new java.awt.Dimension(70, 20));

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Optante Simbahia:");

        jComOptanteSimbahia.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não" }));
        jComOptanteSimbahia.setEnabled(false);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Optante Suframa:");

        jComOptanteSuframa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não" }));
        jComOptanteSuframa.setEnabled(false);

        jLabCdSuframa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdSuframa.setText("Cód. Suframa:");

        jTexCdSuframa.setEnabled(false);

        javax.swing.GroupLayout jPanResumoLayout = new javax.swing.GroupLayout(jPanResumo);
        jPanResumo.setLayout(jPanResumoLayout);
        jPanResumoLayout.setHorizontalGroup(
            jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanResumoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanResumoLayout.createSequentialGroup()
                        .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanResumoLayout.createSequentialGroup()
                                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanResumoLayout.createSequentialGroup()
                                        .addComponent(jLabTecnico)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTexSelecao2))
                                    .addComponent(jPanInforTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jPanInformacoesBancarias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jPanInformacoesFinanceiras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanResumoLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabOptanteSimples)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComOptanteSimples, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComOptanteSimbahia, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComOptanteSuframa, 0, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabCdSuframa)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexCdSuframa, javax.swing.GroupLayout.PREFERRED_SIZE, 102, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanResumoLayout.setVerticalGroup(
            jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanResumoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabTecnico)
                    .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabOptanteSimples)
                    .addComponent(jComOptanteSimples, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jComOptanteSimbahia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jComOptanteSuframa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabCdSuframa)
                    .addComponent(jTexCdSuframa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanInforTecnico, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanResumoLayout.createSequentialGroup()
                        .addComponent(jPanInformacoesBancarias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanInformacoesFinanceiras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanResumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanTabela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanResumo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTexPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexPesquisarKeyReleased
        if (!jTexPesquisar.getText().isEmpty()) {
            sql = "select c.cpf_cnpj as 'CPF / CNPJ',"
                    + "c.nome_razaosocial as 'Nome / Razão Social',"
                    + "c.apelido as Apelido,"
                    + "c.contato as 'Nome Contato',"
                    + "c.email as 'E-mail',"
                    + "c.telefone as Telefone,"
                    + "c.celular as Celular,"
                    + "c.situacao as Sit"
                    + " from gcvclientes as c"
                    + " WHERE NOME_RAZAOSOCIAL LIKE '%"
                    + jTexPesquisar.getText().trim()
                    + "%'";
            //indexAtual = 0;
            buscarTodos();
            jTabClientes.setModel(clidao);
            if (jTabClientes.getModel().getRowCount() > 0) {
                ajustarTabela();
            }
        } else {
            setaInicio();
        }
    }//GEN-LAST:event_jTexPesquisarKeyReleased

    private void jTabClientesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabClientesKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("ENTER".equals(vt.VerificarTecla(evt))) {
            JOptionPane.showMessageDialog(null, vt.VerificarTecla(evt));
            salvarSelecao();
        }
    }//GEN-LAST:event_jTabClientesKeyPressed

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
            java.util.logging.Logger.getLogger(PesquisarClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisarClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisarClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisarClientes.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PesquisarClientes dialog = new PesquisarClientes(new javax.swing.JFrame(), true, chamador, conexao);
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
    private javax.swing.JComboBox<String> jComOptanteSimbahia;
    private javax.swing.JComboBox<String> jComOptanteSimples;
    private javax.swing.JComboBox<String> jComOptanteSuframa;
    private javax.swing.JFormattedTextField jForCep;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JLabel jLabAgenciaBanco;
    private javax.swing.JLabel jLabBairro;
    private javax.swing.JLabel jLabCEP;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabCdPortador;
    private javax.swing.JLabel jLabCdSuframa;
    private javax.swing.JLabel jLabCdTipoPagamento;
    private javax.swing.JLabel jLabComplemento;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabMunicipio;
    private javax.swing.JLabel jLabNomeCategoria;
    private javax.swing.JLabel jLabNumBanco;
    private javax.swing.JLabel jLabOptanteSimples;
    private javax.swing.JLabel jLabPesquisar;
    private javax.swing.JLabel jLabTecnico;
    private javax.swing.JLabel jLabUF;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanEndereco;
    private javax.swing.JPanel jPanInforTecnico;
    private javax.swing.JPanel jPanInformacoesBancarias;
    private javax.swing.JPanel jPanInformacoesFinanceiras;
    private javax.swing.JPanel jPanResumo;
    private javax.swing.JPanel jPanTabela;
    private javax.swing.JPanel jPanePesquisar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTabClientes;
    private javax.swing.JTextField jTexAgenciaBanco;
    private javax.swing.JTextField jTexBairro;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexCdCondPag;
    private javax.swing.JTextField jTexCdPortador;
    private javax.swing.JTextField jTexCdSuframa;
    private javax.swing.JTextField jTexCdTipoPagamento;
    private javax.swing.JTextField jTexComplemento;
    private javax.swing.JTextField jTexLogradouro;
    private javax.swing.JTextField jTexMunicipio;
    private javax.swing.JTextField jTexNomeBanco;
    private javax.swing.JTextField jTexNomeCondPag;
    private javax.swing.JTextField jTexNomePortador;
    private javax.swing.JTextField jTexNomeTipoPagamento;
    private javax.swing.JTextField jTexNumBanco;
    private javax.swing.JTextField jTexNumContaBanco;
    private javax.swing.JTextField jTexNumero;
    private javax.swing.JTextField jTexPesquisar;
    private javax.swing.JTextField jTexSelecao1;
    private javax.swing.JTextField jTexSelecao2;
    private javax.swing.JTextField jTexTipoLogradouro;
    private javax.swing.JTextField jTexUF;
    // End of variables declaration//GEN-END:variables
}
