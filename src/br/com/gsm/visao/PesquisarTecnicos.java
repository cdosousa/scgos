/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GSMPE0051
 */
package br.com.gsm.visao;

import br.com.gsm.controle.CTecnicos;
import br.com.gsm.dao.TecnicosDAO;
import br.com.gsm.modelo.Tecnicos;
import br.com.modelo.FormatarValor;
import br.com.modelo.VerificarTecla;
import br.com.modelo.DataSistema;
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
 * @version 0.01_beta0917 created on 10/10/2017
 */
public class PesquisarTecnicos extends javax.swing.JDialog {

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

    private static Connection conexao;
    private String sql;
    private String cdCpf;
    private TecnicosDAO tecdao;
    private CTecnicos ctec;
    private Tecnicos tec;
    private int indexFinal;
    private int indexAtual = 0;
    private static String chamador;
    private VerificarTecla vt;
    private String selecao1;
    private String selecao2;
    private final DataSistema dat;

    /**
     * Creates new form PesquisaModelo
     */
    public PesquisarTecnicos(java.awt.Frame parent, boolean modal, String chamador, Connection conexao) {
        super(parent, modal);
        this.chamador = chamador;
        this.conexao = conexao;
        dat = new DataSistema();
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisa de Técnicos");
        setaInicio();
        centralizarComponente();
        mudarLinha();
        //buscarCorrelatos();
        //filtrarTabela();
    }

    // setando as variáveis iniciais
    public void setaInicio() {
        tec = new Tecnicos();
        ctec = new CTecnicos(conexao);
        sql = "SELECT CPF AS 'C.P.F',"
                + "NOME_TECNICO AS Nome,"
                + "RG AS 'R.G',"
                + "EMAIL AS Email,"
                + "TELEFONE AS Telefone,"
                + "CELULAR AS Celular,"
                + "SITUACAO AS Sit"
                + " FROM GSMTECNICOS";
        buscarTodos();
        jTabTecnico.setModel(tecdao);
        ajustarTabela();
    }

    // criando um listener quando mudar de linha
    private void mudarLinha() {
        jTabTecnico.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtual = jTabTecnico.getSelectedRow();
                ajustarTabela();
            }
        });

        // verificar tecla para captuar quando a tecla entre for pressionada
        jTabTecnico.addKeyListener(new KeyListener() {
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
            tecdao = new TecnicosDAO(conexao);
            tecdao.setQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        jTabTecnico.getColumnModel().getColumn(0).setMinWidth(90);
        jTabTecnico.getColumnModel().getColumn(0).setPreferredWidth(90);
        jTabTecnico.getColumnModel().getColumn(1).setMinWidth(250);
        jTabTecnico.getColumnModel().getColumn(1).setPreferredWidth(250);
        jTabTecnico.getColumnModel().getColumn(2).setMinWidth(90);
        jTabTecnico.getColumnModel().getColumn(2).setPreferredWidth(90);
        jTabTecnico.getColumnModel().getColumn(3).setMinWidth(200);
        jTabTecnico.getColumnModel().getColumn(3).setPreferredWidth(200);
        jTabTecnico.getColumnModel().getColumn(4).setMinWidth(90);
        jTabTecnico.getColumnModel().getColumn(4).setPreferredWidth(90);
        jTabTecnico.getColumnModel().getColumn(5).setMinWidth(90);
        jTabTecnico.getColumnModel().getColumn(5).setPreferredWidth(90);
        jTabTecnico.getColumnModel().getColumn(6).setMinWidth(30);
        jTabTecnico.getColumnModel().getColumn(6).setPreferredWidth(30);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
        cdCpf = String.format("%s", jTabTecnico.getValueAt(indexAtual, 0));
        jTexSelecao1.setText(String.format("%s", jTabTecnico.getValueAt(indexAtual, 0)));
        jTexSelecao2.setText(String.format("%s", jTabTecnico.getValueAt(indexAtual, 1)));
        buscarCorrelatos();
    }

    // buscar tabelas correlatos
    private void buscarCorrelatos() {
        if (!cdCpf.isEmpty()) {
            String sql = "select * from gsmtecnicos where cpf = "
                    + cdCpf
                    + "";
            try {
                ctec.pesquisar(sql);
            } catch (SQLException ex) {
                Logger.getLogger(PesquisarTecnicos.class.getName()).log(Level.SEVERE, null, ex);
            }
            ctec.mostrarPesquisa(tec, 0);
            atualizarTela();
        }
    }

    // Atualizar registros correlatos
    private void atualizarTela() {
        jTexTipoLogradouro.setText(tec.getTipoLogradouro());
        jTexLogradouro.setText(tec.getLogradouro());
        jTexNumero.setText(tec.getNumero());
        jTexComplemento.setText(tec.getComplemento());
        jTexBairro.setText(tec.getBairro());
        jTexUF.setText(tec.getCdSiglaUf());
        jTexMunicipio.setText(tec.getNomeMunicipio());
        String possuiCnh = String.valueOf(tec.getPossuiHabilitacao());
        switch (possuiCnh) {
            case "1":
                jTexPossuiCnh.setText("Sim");
                break;
            case "2":
                jTexPossuiCnh.setText("Não");
                break;
            default:
                jTexPossuiCnh.setText("Não Informado");
                break;
        }
        String categoriaCnh = String.valueOf(tec.getCategoriaCnh());
        switch (categoriaCnh) {
            case "1":
                jTexCategoriaCnh.setText("A");
                break;
            case "2":
                jTexCategoriaCnh.setText("B");
                break;
            case "3":
                jTexCategoriaCnh.setText("AB");
                break;
            case "4":
                jTexCategoriaCnh.setText("C");
                break;
            case "5":
                jTexCategoriaCnh.setText("D");
                break;
            case "6":
                jTexCategoriaCnh.setText("E");
                break;
            default:
                jTexCategoriaCnh.setText("N/A");
                break;
        }
        jTexNumeroCnh.setText(String.valueOf(tec.getNumCnh()));
        jTexCadastradoPor.setText(tec.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(tec.getDataCadastro())));
        jForDataModificacao.setText(tec.getDataModificacao());
    }

    // salvar seleção
    private void salvarSelecao() {
        selecao1 = jTexSelecao1.getText().trim();
        selecao2 = jTexSelecao2.getText().trim();
        dispose();
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
        jTabTecnico = new JTable(tecdao);
        jPanePesquisar = new javax.swing.JPanel();
        jLabPesquisar = new javax.swing.JLabel();
        jTexPesquisar = new javax.swing.JTextField();
        jPanResumo = new javax.swing.JPanel();
        jPanInforTecnico = new javax.swing.JPanel();
        jPanEstoque = new javax.swing.JPanel();
        jLabPossuiCNH = new javax.swing.JLabel();
        jLabCategoriaCnh = new javax.swing.JLabel();
        jLabNumeroCnh = new javax.swing.JLabel();
        jTexPossuiCnh = new javax.swing.JTextField();
        jTexCategoriaCnh = new javax.swing.JTextField();
        jTexNumeroCnh = new javax.swing.JTextField();
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
        jPanel1 = new javax.swing.JPanel();
        jLabCadastradoPor = new javax.swing.JLabel();
        jLabDataCadastro = new javax.swing.JLabel();
        jLabDataModificacao = new javax.swing.JLabel();
        jForDataCadastro = new javax.swing.JFormattedTextField();
        jTexCadastradoPor = new javax.swing.JTextField();
        jForDataModificacao = new javax.swing.JFormattedTextField();
        jLabTecnico = new javax.swing.JLabel();
        jTexSelecao1 = new javax.swing.JTextField();
        jTexSelecao2 = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Tecnicos");
        setResizable(false);

        jPanTabela.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabTecnico.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.SystemColor.activeCaptionBorder, null));
        jTabTecnico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabTecnico.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CPF", "Nome", "RG", "email", "Telefone", "Celular", "Sit"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabTecnico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabTecnicoKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTabTecnico);
        if (jTabTecnico.getColumnModel().getColumnCount() > 0) {
            jTabTecnico.getColumnModel().getColumn(0).setResizable(false);
            jTabTecnico.getColumnModel().getColumn(0).setPreferredWidth(90);
            jTabTecnico.getColumnModel().getColumn(1).setResizable(false);
            jTabTecnico.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTabTecnico.getColumnModel().getColumn(2).setResizable(false);
            jTabTecnico.getColumnModel().getColumn(2).setPreferredWidth(90);
            jTabTecnico.getColumnModel().getColumn(3).setResizable(false);
            jTabTecnico.getColumnModel().getColumn(3).setPreferredWidth(200);
            jTabTecnico.getColumnModel().getColumn(4).setResizable(false);
            jTabTecnico.getColumnModel().getColumn(4).setPreferredWidth(90);
            jTabTecnico.getColumnModel().getColumn(5).setResizable(false);
            jTabTecnico.getColumnModel().getColumn(5).setPreferredWidth(90);
            jTabTecnico.getColumnModel().getColumn(6).setResizable(false);
            jTabTecnico.getColumnModel().getColumn(6).setPreferredWidth(30);
        }

        jPanePesquisar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabPesquisar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPesquisar.setText("Pesquisar Técnico:");

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

        jPanInforTecnico.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações do Técnico", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jPanEstoque.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "CNH", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabPossuiCNH.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPossuiCNH.setText("Possui CNH?");

        jLabCategoriaCnh.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCategoriaCnh.setText("Categoria:");

        jLabNumeroCnh.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNumeroCnh.setText("Num. Reg.:");

        jTexPossuiCnh.setEditable(false);
        jTexPossuiCnh.setEnabled(false);

        jTexCategoriaCnh.setEditable(false);
        jTexCategoriaCnh.setEnabled(false);

        jTexNumeroCnh.setEditable(false);
        jTexNumeroCnh.setEnabled(false);

        javax.swing.GroupLayout jPanEstoqueLayout = new javax.swing.GroupLayout(jPanEstoque);
        jPanEstoque.setLayout(jPanEstoqueLayout);
        jPanEstoqueLayout.setHorizontalGroup(
            jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanEstoqueLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabPossuiCNH, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabCategoriaCnh, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabNumeroCnh, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanEstoqueLayout.createSequentialGroup()
                        .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTexCategoriaCnh, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jTexPossuiCnh, javax.swing.GroupLayout.Alignment.LEADING))
                        .addGap(110, 110, 110))
                    .addGroup(jPanEstoqueLayout.createSequentialGroup()
                        .addComponent(jTexNumeroCnh, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())))
        );
        jPanEstoqueLayout.setVerticalGroup(
            jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanEstoqueLayout.createSequentialGroup()
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabPossuiCNH)
                    .addComponent(jTexPossuiCnh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCategoriaCnh)
                    .addComponent(jTexCategoriaCnh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTexNumeroCnh, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabNumeroCnh))
                .addGap(52, 52, 52))
        );

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

        javax.swing.GroupLayout jPanInforTecnicoLayout = new javax.swing.GroupLayout(jPanInforTecnico);
        jPanInforTecnico.setLayout(jPanInforTecnicoLayout);
        jPanInforTecnicoLayout.setHorizontalGroup(
            jPanInforTecnicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInforTecnicoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanInforTecnicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanInforTecnicoLayout.createSequentialGroup()
                        .addComponent(jPanEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanEstoque, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanInforTecnicoLayout.setVerticalGroup(
            jPanInforTecnicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInforTecnicoLayout.createSequentialGroup()
                .addGroup(jPanInforTecnicoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanEndereco, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanEstoque, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabTecnico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTecnico.setText("Técnico:");

        jTexSelecao1.setEditable(false);
        jTexSelecao1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexSelecao1.setEnabled(false);

        jTexSelecao2.setEditable(false);
        jTexSelecao2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexSelecao2.setEnabled(false);

        javax.swing.GroupLayout jPanResumoLayout = new javax.swing.GroupLayout(jPanResumo);
        jPanResumo.setLayout(jPanResumoLayout);
        jPanResumoLayout.setHorizontalGroup(
            jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanResumoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanInforTecnico, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanResumoLayout.createSequentialGroup()
                        .addComponent(jLabTecnico)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 325, Short.MAX_VALUE)))
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
                .addGap(18, 18, 18)
                .addComponent(jPanInforTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanResumo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
            sql = "SELECT CPF AS 'C.P.F',"
                    + "NOME_TECNICO AS Nome,"
                    + "RG AS 'R.G',"
                    + "EMAIL AS Email,"
                    + "TELEFONE AS Telefone,"
                    + "CELULAR AS Celular,"
                    + "SITUACAO AS Sit"
                    + " FROM GSMTECNICOS"
                    + " WHERE NOME_TECNICO LIKE '%"
                    + jTexPesquisar.getText().trim()
                    + "%'";
            //indexAtual = 0;
            buscarTodos();
            jTabTecnico.setModel(tecdao);
            if (jTabTecnico.getModel().getRowCount() > 0) {
                ajustarTabela();
            }
        } else {
            setaInicio();
        }
    }//GEN-LAST:event_jTexPesquisarKeyReleased

    private void jTabTecnicoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabTecnicoKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("ENTER".equals(vt.VerificarTecla(evt))) {
            JOptionPane.showMessageDialog(null, vt.VerificarTecla(evt));
            salvarSelecao();
        }
    }//GEN-LAST:event_jTabTecnicoKeyPressed

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
            java.util.logging.Logger.getLogger(PesquisarTecnicos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisarTecnicos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisarTecnicos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisarTecnicos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PesquisarTecnicos dialog = new PesquisarTecnicos(new javax.swing.JFrame(), true, chamador, conexao);
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
    private javax.swing.JFormattedTextField jForCep;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JLabel jLabBairro;
    private javax.swing.JLabel jLabCEP;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabCategoriaCnh;
    private javax.swing.JLabel jLabComplemento;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabMunicipio;
    private javax.swing.JLabel jLabNomeCategoria;
    private javax.swing.JLabel jLabNumeroCnh;
    private javax.swing.JLabel jLabPesquisar;
    private javax.swing.JLabel jLabPossuiCNH;
    private javax.swing.JLabel jLabTecnico;
    private javax.swing.JLabel jLabUF;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanEndereco;
    private javax.swing.JPanel jPanEstoque;
    private javax.swing.JPanel jPanInforTecnico;
    private javax.swing.JPanel jPanResumo;
    private javax.swing.JPanel jPanTabela;
    private javax.swing.JPanel jPanePesquisar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabTecnico;
    private javax.swing.JTextField jTexBairro;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexCategoriaCnh;
    private javax.swing.JTextField jTexComplemento;
    private javax.swing.JTextField jTexLogradouro;
    private javax.swing.JTextField jTexMunicipio;
    private javax.swing.JTextField jTexNumero;
    private javax.swing.JTextField jTexNumeroCnh;
    private javax.swing.JTextField jTexPesquisar;
    private javax.swing.JTextField jTexPossuiCnh;
    private javax.swing.JTextField jTexSelecao1;
    private javax.swing.JTextField jTexSelecao2;
    private javax.swing.JTextField jTexTipoLogradouro;
    private javax.swing.JTextField jTexUF;
    // End of variables declaration//GEN-END:variables
}
