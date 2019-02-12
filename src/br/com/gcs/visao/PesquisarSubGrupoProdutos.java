/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GSMPE0021
 */
package br.com.gcs.visao;
//imports de pacotes do projeto

import br.com.gcs.dao.SubGrupoProdutosDAO;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Toolkit;
// imports do pacote java
import javax.swing.JTable;
import javax.swing.JFrame;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cristiano de Oliveira Sousa Modelo de tabela para pesquisa de
 * usuários
 */
public class PesquisarSubGrupoProdutos extends JDialog {

    //Variáveis de instância privadas
    private SubGrupoProdutosDAO sgpDAO;
    private JPanel jPanTab;
    private JPanel jPanFiltro;
    private JTable tabelaPes;
    private JScrollPane jScrPane;
    private JLabel jLabPesqNome;
    private JLabel jLabSelecao1;
    private JLabel jLabSelecao2;
    private JTextField jTexPesqNome;
    private JTextField jTexSelecao1;
    private JTextField jTexSelecao2;
    private JTextField jTexSelecao3;
    private JTextField jTexSelecao4;
    private JButton jButPesquisar;
    private JButton jButSair;
    private static String chamador;
    private static Connection conexao;
    private String selec1;
    private String selec2;
    private int indexAtual = 0;

    //Contrutor padrão
    public PesquisarSubGrupoProdutos(Frame parent, boolean modal, String chamador, Connection conexao) {
        super(parent, modal);
        this.chamador = chamador;
        this.conexao = conexao;
        setLocationRelativeTo(null);
        if ("M".equals(chamador)) {
            buscarTodos();
        } else {
            buscarPorGrupo();
        }
        initComponentes();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        centralizarComponente();
    }

    // método para centralizar componente
    public void centralizarComponente() {
        Dimension ds = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension dw = getSize();
        setLocation((ds.width - dw.width) / 2, (ds.height - dw.height) / 2);
    }

    // método para criar os elementos da tela e abrigar a tabela
    public void initComponentes() {
        // iniciando meus objetos
        jScrPane = new javax.swing.JScrollPane();
        jLabPesqNome = new JLabel();
        jLabSelecao1 = new JLabel();
        jLabSelecao2 = new JLabel();
        jTexPesqNome = new JTextField();
        jButPesquisar = new JButton();
        jButSair = new JButton("Sair");
        jTexSelecao1 = new JTextField();
        jTexSelecao2 = new JTextField();
        jTexSelecao3 = new JTextField();
        jTexSelecao4 = new JTextField();
        jPanTab = new JPanel();
        jPanFiltro = new JPanel();
        tabelaPes = new JTable(sgpDAO);

        //Definindo o tamanho da Janela
        AjustarTamanhoTabela();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModal(true);
        setTitle("Pesquisar Sub Grupo de Produtos");
        setMaximumSize(new java.awt.Dimension(710, 320));
        setMinimumSize(new java.awt.Dimension(710, 320));
        setPreferredSize(new java.awt.Dimension(710, 320));
        setSize(new java.awt.Dimension(710, 320));

        //Definindo o tamanho da Tabela
        tabelaPes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        //tabelaPes.setAutoCreateRowSorter(true);
        tabelaPes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tabelaPes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtual = tabelaPes.getSelectedRow();
                AjustarTamanhoTabela();

            }
        });
        tabelaPes.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent arg0) {
                if (arg0.getKeyCode() == KeyEvent.VK_ENTER && !"M".equals(chamador)) {
                    arg0.consume();
                    SalvarSelecao();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                try {

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        //Definindo o Painel para colocar a tabela
        jScrPane.setViewportView(tabelaPes);

        jPanTab.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanTab.setMaximumSize(new java.awt.Dimension(705, 150));
        jPanTab.setMinimumSize(new java.awt.Dimension(705, 150));
        jPanTab.setPreferredSize(new java.awt.Dimension(705, 150));

        javax.swing.GroupLayout jPanTabLayout = new javax.swing.GroupLayout(jPanTab);
        jPanTab.setLayout(jPanTabLayout);
        jPanTabLayout.setHorizontalGroup(
                jPanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanTabLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrPane, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanTabLayout.setVerticalGroup(
                jPanTabLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanTabLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrPane, javax.swing.GroupLayout.PREFERRED_SIZE, 118, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(12, Short.MAX_VALUE))
        );

        jPanFiltro.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Filtro", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabPesqNome.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPesqNome.setText("Pesquisar Nome:");
        jTexPesqNome.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTexPesqNomeActionPerformed(evt);
            }
        });

        jButPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Search-32.png"))); // NOI18N
        jButPesquisar.setBorder(null);
        jButPesquisar.setMaximumSize(new java.awt.Dimension(48, 48));
        jButPesquisar.setMinimumSize(new java.awt.Dimension(48, 48));
        jButPesquisar.setPreferredSize(new java.awt.Dimension(48, 48));
        jButPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButPesquisarActionPerformed(evt);
            }
        });

        jButSair.setBorder(null);
        jButSair.setMaximumSize(new java.awt.Dimension(55, 23));
        jButSair.setMinimumSize(new java.awt.Dimension(55, 23));
        jButSair.setPreferredSize(new java.awt.Dimension(55, 23));
        jButSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanFiltroLayout = new javax.swing.GroupLayout(jPanFiltro);
        jPanFiltro.setLayout(jPanFiltroLayout);
        jPanFiltroLayout.setHorizontalGroup(
                jPanFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanFiltroLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jTexPesqNome, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabPesqNome))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanFiltroLayout.setVerticalGroup(
                jPanFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanFiltroLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanFiltroLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButPesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanFiltroLayout.createSequentialGroup()
                                                .addComponent(jLabPesqNome)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTexPesqNome, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabSelecao1.setText("Código:");

        jTexSelecao1.setEditable(false);
        jTexSelecao1.setEnabled(false);

        jLabSelecao2.setText("Nome:");

        jTexSelecao2.setEditable(false);
        jTexSelecao2.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanTab, javax.swing.GroupLayout.PREFERRED_SIZE, 670, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jPanFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(30, 30, 30)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabSelecao2))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jTexSelecao1)
                                                        .addComponent(jTexSelecao2))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING))
                                                .addComponent(jButSair)))
                                .addContainerGap(20, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jPanTab, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jPanFiltro, javax.swing.GroupLayout.PREFERRED_SIZE, 87, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabSelecao1)
                                                        .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jLabSelecao2)
                                                        .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(jButSair))))
                                .addContainerGap(39, Short.MAX_VALUE))
        );

        pack();
    }

    public void buscarTodos() {
        try {
            sgpDAO = new SubGrupoProdutosDAO(conexao);
            sgpDAO.setQuery("SELECT CD_SUBGRUPO AS SubGrupo,"
                    + "CD_GRUPO AS Grupo,"
                    + "NOME_SUBGRUPO AS Nome,"
                    + "GERAR_CODIGO AS Forma_Código,"
                    + "DATA_CADASTRO AS Cadastro,"
                    + "DATA_MODIFICACAO AS Modificação,"
                    + "SITUACAO AS Sit"
                    + " FROM GCSSUBGRUPOS");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro para buscar todos sub grupos!\nPrograma: GSMPE0021\nErr: " + ex);
        }
    }

    public void buscarPorNome() {
        String query = "SELECT CD_SUBGRUPO AS SubGrupo,"
                    + "CD_GRUPO AS Grupo,"
                    + "NOME_SUBGRUPO AS Nome,"
                    + "GERAR_CODIGO AS For_Código,"
                    + "DATA_CADASTRO AS Cadastro,"
                    + "DATA_MODIFICACAO AS Modificação,"
                    + "SITUACAO AS Sit"
                    + " FROM GCSSUBGRUPOS WHERE NOME_SUBGRUPO LIKE "
                    + "'%"
                    + jTexPesqNome.getText()
                    + "%'";
        try {
            sgpDAO = new SubGrupoProdutosDAO(conexao);
            sgpDAO.setQuery(query);
            jTexPesqNome.setText("");
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro geral na Busca por nome!\nPrograma: GSMPE0021\nErr: " + ex);
        }
    }

    public void buscarPorGrupo() {
        String query = "SELECT CD_SUBGRUPO AS SubGrupo,"
                    + "CD_GRUPO AS Grupo,"
                    + "NOME_SUBGRUPO AS Nome,"
                    + "GERAR_CODIGO AS For_Código,"
                    + "DATA_CADASTRO AS Cadastro,"
                    + "DATA_MODIFICACAO AS Modificação,"
                    + "SITUACAO AS Sit"
                    + " FROM GCSSUBGRUPOS WHERE CD_GRUPO = '"
                    + chamador.trim()
                    + "'";
        try {
            sgpDAO = new SubGrupoProdutosDAO(conexao);
            sgpDAO.setQuery(query);
            jTexPesqNome.setText("");
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + sqlex);
        } catch (Exception ex) {
            //JOptionPane.showMessageDialog(null, "Erro geral na busca por grupo!\nPrograma: GSMPE0021\nGrupo: "+chamador.trim()+"\nErr: " + ex+"\nQuery: "+query);
        }
    }

    public void AjustarTamanhoTabela() {
        tabelaPes.getColumnModel().getColumn(0).setMinWidth(70);
        tabelaPes.getColumnModel().getColumn(0).setMaxWidth(70);
        tabelaPes.getColumnModel().getColumn(1).setMinWidth(50);
        tabelaPes.getColumnModel().getColumn(1).setMaxWidth(50);
        tabelaPes.getColumnModel().getColumn(2).setMinWidth(250);
        tabelaPes.getColumnModel().getColumn(2).setMaxWidth(250);
        tabelaPes.getColumnModel().getColumn(3).setMinWidth(90);
        tabelaPes.getColumnModel().getColumn(3).setMaxWidth(90);
        tabelaPes.getColumnModel().getColumn(4).setMinWidth(75);
        tabelaPes.getColumnModel().getColumn(4).setMaxWidth(75);
        tabelaPes.getColumnModel().getColumn(5).setMinWidth(75);
        tabelaPes.getColumnModel().getColumn(5).setMaxWidth(75);
        tabelaPes.getColumnModel().getColumn(6).setMinWidth(30);
        tabelaPes.getColumnModel().getColumn(6).setMaxWidth(30);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
        jTexSelecao1.setText(String.format("%s", tabelaPes.getValueAt(indexAtual, 0)));
        jTexSelecao2.setText(String.format("%s", tabelaPes.getValueAt(indexAtual, 1)));
        jTexSelecao3.setText(String.format("%s", tabelaPes.getValueAt(indexAtual, 2)));
    }

    public void SalvarSelecao() {
        selec1 = jTexSelecao1.getText();
        selec2 = jTexSelecao3.getText();
        dispose();
    }

    public String getSelec1() {
        return selec1;
    }

    public String getSelec2() {
        return selec2;
    }

    public void jTexPesqNomeActionPerformed(java.awt.event.ActionEvent evt) {

    }

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
        buscarPorNome();
        AjustarTamanhoTabela();
    }

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    // método principal
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                PesquisarSubGrupoProdutos dialog = new PesquisarSubGrupoProdutos(new JFrame(), true, chamador, conexao);
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
}
