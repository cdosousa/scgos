/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GSMPE0041
 */
package br.com.gsm.visao;

import br.com.gsm.controle.CAtividades;
import br.com.gsm.dao.AtividadesDAO;
import br.com.gsm.dao.EquipamentosAtividadeDAO;
import br.com.gsm.dao.TarefasAtividadesDAO;
import br.com.gsm.modelo.Atividades;
import br.com.modelo.VerificarTecla;
import br.com.modelo.DataSistema;
import br.com.modelo.FormatarValor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 30/10/2017
 */
public class PesquisarAtividades extends javax.swing.JDialog {

    // objetos do registro pai
    private AtividadesDAO atvdao;
    private CAtividades catv;
    private Atividades modatv;

    // objetos do registro filho tarefas
    private TarefasAtividadesDAO tadao;

    // objetos do registro filho equipamentos
    private EquipamentosAtividadeDAO eadao;

    // objetos de ambiente
    private static Connection conexao;
    private VerificarTecla vt;
    private final DataSistema dat;
    private NumberFormat formato;

    // variáveis de instância do registro pai
    private String cdAtividade;
    private String sql;
    private int indexFinal;
    private int indexAtual = 0;
    private static String chamador;
    private String selecao1;
    private String selecao2;
    private double selecao3;

    // variáveis de instância do registro filho tarefas
    private String sqlta;
    private int indexAtualTarefas;
    private String cdTarefa;

    // variáveis de instância do registro filho equipamentos
    private String sqlea;
    private int indexAtualEquipamentos;
    private String cdEquipamento;

    /**
     * Creates new form PesquisaModelo
     */
    public PesquisarAtividades(java.awt.Frame parent, boolean modal, String chamador, Connection conexao) {
        super(parent, modal);
        formato = NumberFormat.getInstance();
        this.chamador = chamador;
        this.conexao = conexao;
        dat = new DataSistema();
        setLocationRelativeTo(null);
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisa de Atividades");
        setaInicio();
        centralizarComponente();
        mudarLinha();
        //buscarCorrelatos();
        //filtrarTabela();
    }

    // setando as variáveis iniciais
    public void setaInicio() {
        modatv = new Atividades();
        catv = new CAtividades(conexao);
        if ("M".equals(chamador)) {
            sql = "SELECT CD_ATIVIDADE AS 'Código',"
                    + "NOME_ATIVIDADE AS Descrição,"
                    + "format (VAL_TOTAL_TAREFAS,2,'de_DE') AS 'Valor Tot. Tarefas',"
                    + "format (VAL_TOTAL_EQUIPAMENTOS,2,'de_DE') AS 'Valor Tot. Equipamentos',"
                    + "format (VAL_TOTAL_ATIVIDADE,2,'de_DE') AS 'Valor Tot. Atividades',"
                    + "SITUACAO AS Sit"
                    + " FROM GSMATIVIDADES";
        }else{
            sql = "SELECT CD_ATIVIDADE AS 'Código',"
                    + "NOME_ATIVIDADE AS Descrição,"
                    + "format (VAL_TOTAL_TAREFAS,2,'de_DE') AS 'Valor Tot. Tarefas',"
                    + "format (VAL_TOTAL_EQUIPAMENTOS,2,'de_DE') AS 'Valor Tot. Equipamentos',"
                    + "format (VAL_TOTAL_ATIVIDADE,2,'de_DE') AS 'Valor Tot. Atividades',"
                    + "SITUACAO AS Sit"
                    + " FROM GSMATIVIDADES"
                    + " WHERE SITUACAO = 'A'";
        }
        buscarTodos();
        jTabAtividades.setModel(atvdao);
        ajustarTabela();
    }

    // criando um listener quando mudar de linha
    private void mudarLinha() {
        jTabAtividades.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtual = jTabAtividades.getSelectedRow();
                ajustarTabela();
            }
        });

        // verificar tecla para captuar quando a tecla entre for pressionada
        jTabAtividades.addKeyListener(new KeyListener() {
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
            atvdao = new AtividadesDAO(conexao);
            atvdao.setQuery(sql);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        jTabAtividades.getColumnModel().getColumn(0).setMinWidth(30);
        jTabAtividades.getColumnModel().getColumn(0).setPreferredWidth(30);
        jTabAtividades.getColumnModel().getColumn(1).setMinWidth(200);
        jTabAtividades.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTabAtividades.getColumnModel().getColumn(2).setMinWidth(60);
        jTabAtividades.getColumnModel().getColumn(2).setPreferredWidth(60);
        jTabAtividades.getColumnModel().getColumn(3).setMinWidth(60);
        jTabAtividades.getColumnModel().getColumn(3).setPreferredWidth(60);
        jTabAtividades.getColumnModel().getColumn(4).setMinWidth(60);
        jTabAtividades.getColumnModel().getColumn(4).setPreferredWidth(60);
        jTabAtividades.getColumnModel().getColumn(5).setMinWidth(30);
        jTabAtividades.getColumnModel().getColumn(5).setPreferredWidth(30);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
        cdAtividade = String.format("%s", jTabAtividades.getValueAt(indexAtual, 0));
        jTexSelecao1.setText(String.format("%s", jTabAtividades.getValueAt(indexAtual, 0)));
        jTexSelecao2.setText(String.format("%s", jTabAtividades.getValueAt(indexAtual, 1)));
        jForSelecao3.setText(String.format("%s", jTabAtividades.getValueAt(indexAtual, 4)));
        buscarCorrelatos();
    }

    // buscar tabelas correlatos
    private void buscarCorrelatos() {
        if (!cdAtividade.isEmpty()) {
            String sql = "select * from gsmatividades where cd_atividade = "
                    + cdAtividade
                    + "";
            try {
                catv.pesquisar(sql);
            } catch (SQLException ex) {
                Logger.getLogger(PesquisarAtividades.class.getName()).log(Level.SEVERE, null, ex);
            }
            catv.mostrarPesquisa(modatv, 0);
            mostrarTarefas();
            mostrarEquipamentos();
            atualizarTela();
        }
    }

    // Atualizar registros correlatos
    private void atualizarTela() {
        jTexCadastradoPor.setText(modatv.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modatv.getDataCadastro())));
        jForDataModificacao.setText(modatv.getDataModificacao());
    }

    // salvar seleção
    private void salvarSelecao() {
        selecao1 = jTexSelecao1.getText().trim();
        selecao2 = jTexSelecao2.getText().trim();
        try{
            selecao3 = formato.parse(jForSelecao3.getText()).doubleValue();
        }catch(ParseException ex){
            Logger.getLogger(ManterServicos.class.getName()).log(Level.SEVERE, null, ex);
        }
        dispose();
    }

    /**
     * SESSÃO PARA BUSCAR E ATUALIZAR A TAREFAS DA ATIVIDADE
     */
    // buscando tabela de tarefas
    private void mostrarTarefas() {
        int numLinhas;
        sqlta = "select ta.sequencia as 'Seq.', "
                + "t.nome_tarefa as Descrição, "
                + "e.nome_especialidade as Especialidade, "
                + "format(ta.valor_unit,2,'de_DE') as Valor "
                + "from gsmtarefaatividade as ta "
                + "left outer join gsmtarefas as t on ta.cd_tarefa = t.cd_tarefa "
                + "left outer join gsmespecialidades as e on ta.cd_especialidade = e.cd_especialidade "
                + "where ta.cd_atividade = " + modatv.getCdAtividade()
                + " and ta.situacao = 'A' "
                + "order by ta.sequencia";
        buscarTodasTarefas();
        jTabTarefas.setModel(tadao);
        numLinhas = jTabTarefas.getModel().getRowCount();
        ajustarTabelaTarefas();
    }

    //Metodos para buscar tarefas da atividas
    public void buscarTodasTarefas() {
        try {
            tadao = new TarefasAtividadesDAO(conexao);
            tadao.setQuery(sqlta);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela tarefas
    public void ajustarTabelaTarefas() {
        jTabTarefas.getColumnModel().getColumn(0).setMinWidth(20);
        jTabTarefas.getColumnModel().getColumn(0).setPreferredWidth(20);
        jTabTarefas.getColumnModel().getColumn(1).setMinWidth(200);
        jTabTarefas.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTabTarefas.getColumnModel().getColumn(2).setMinWidth(100);
        jTabTarefas.getColumnModel().getColumn(2).setPreferredWidth(100);
        jTabTarefas.getColumnModel().getColumn(3).setMinWidth(60);
        jTabTarefas.getColumnModel().getColumn(3).setPreferredWidth(60);
        if (indexAtualTarefas < 0) {
            indexAtualTarefas = 0;
            cdTarefa = String.format("%s", jTabTarefas.getValueAt(indexAtualTarefas, 0));
        }
    }

    /**
     * SESSÃO PARA BUSCAR E ATUALIZAR OS EQUIPAMENTOS DA ATIVIDADE
     */
    // Método para mostrar equipamentos
    private void mostrarEquipamentos() {
        int numLinhas;
        sqlea = "select ea.sequencial as 'Seq.', "
                + "e.nome_equipamento as Descrição, "
                + "format(ea.valor_unit,2,'de_DE')as Valor "
                + "from gsmequipamentoatividade as ea "
                + "left outer join gsmequipamentos as e on ea.cd_equipamento = e.cd_equipamento "
                + "where ea.cd_atividade = " + modatv.getCdAtividade()
                + " and ea.situacao = 'A' "
                + "order by ea.sequencial";
        buscarTodosEquipamentos();
        jTabEquipamentos.setModel(eadao);
        numLinhas = jTabEquipamentos.getModel().getRowCount();
        ajustarTabelaEquipamentos();
    }

    //Metodos para buscar equipamentos da atividas
    public void buscarTodosEquipamentos() {
        try {
            eadao = new EquipamentosAtividadeDAO(conexao);
            eadao.setQuery(sqlea);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela tarefas
    public void ajustarTabelaEquipamentos() {
        jTabEquipamentos.getColumnModel().getColumn(0).setMinWidth(20);
        jTabEquipamentos.getColumnModel().getColumn(0).setPreferredWidth(20);
        jTabEquipamentos.getColumnModel().getColumn(1).setMinWidth(200);
        jTabEquipamentos.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTabEquipamentos.getColumnModel().getColumn(2).setMinWidth(60);
        jTabEquipamentos.getColumnModel().getColumn(2).setPreferredWidth(60);
        if (indexAtualEquipamentos < 0) {
            indexAtualEquipamentos = 0;
            cdEquipamento = String.format("%s", jTabEquipamentos.getValueAt(indexAtualEquipamentos, 0));
        }
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
    
    /**
     * @return the selecao3
     */
    public double getSelecao3() {
        return selecao3;
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
        jTabAtividades = new JTable(atvdao);
        jPanePesquisar = new javax.swing.JPanel();
        jLabPesquisar = new javax.swing.JLabel();
        jTexPesquisar = new javax.swing.JTextField();
        jPanResumo = new javax.swing.JPanel();
        jPanInforAtividade = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabCadastradoPor = new javax.swing.JLabel();
        jLabDataCadastro = new javax.swing.JLabel();
        jLabDataModificacao = new javax.swing.JLabel();
        jForDataCadastro = new javax.swing.JFormattedTextField();
        jTexCadastradoPor = new javax.swing.JTextField();
        jForDataModificacao = new javax.swing.JFormattedTextField();
        jPanTarefas = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabTarefas = new javax.swing.JTable();
        jPanEquipamentos = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTabEquipamentos = new javax.swing.JTable();
        jLabAtividade = new javax.swing.JLabel();
        jTexSelecao1 = new javax.swing.JTextField();
        jTexSelecao2 = new javax.swing.JTextField();
        jForSelecao3 = new FormatarValor((FormatarValor.NUMERO));

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Pesquisar Tecnicos");
        setResizable(false);

        jPanTabela.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabAtividades.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED, java.awt.SystemColor.activeCaptionBorder, null));
        jTabAtividades.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabAtividades.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Código", "Descrição", "Valor Tarefas", "Valor Eqpto.", "Valor Ativid.", "Sit"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabAtividades.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTabAtividadesKeyPressed(evt);
            }
        });
        jScrollPane1.setViewportView(jTabAtividades);
        if (jTabAtividades.getColumnModel().getColumnCount() > 0) {
            jTabAtividades.getColumnModel().getColumn(0).setResizable(false);
            jTabAtividades.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTabAtividades.getColumnModel().getColumn(1).setResizable(false);
            jTabAtividades.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTabAtividades.getColumnModel().getColumn(2).setResizable(false);
            jTabAtividades.getColumnModel().getColumn(2).setPreferredWidth(60);
            jTabAtividades.getColumnModel().getColumn(3).setResizable(false);
            jTabAtividades.getColumnModel().getColumn(3).setPreferredWidth(60);
            jTabAtividades.getColumnModel().getColumn(4).setResizable(false);
            jTabAtividades.getColumnModel().getColumn(4).setPreferredWidth(60);
            jTabAtividades.getColumnModel().getColumn(5).setResizable(false);
            jTabAtividades.getColumnModel().getColumn(5).setPreferredWidth(30);
        }

        jPanePesquisar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabPesquisar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPesquisar.setText("Pesquisar Atividade:");

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
                    .addComponent(jPanePesquisar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanTabelaLayout.setVerticalGroup(
            jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTabelaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanePesquisar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanResumo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanInforAtividade.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações do Técnico", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

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

        jPanTarefas.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Tarefas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTabTarefas.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Seq.", "Descrição", "Especialidade", "Valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(jTabTarefas);
        if (jTabTarefas.getColumnModel().getColumnCount() > 0) {
            jTabTarefas.getColumnModel().getColumn(0).setResizable(false);
            jTabTarefas.getColumnModel().getColumn(0).setPreferredWidth(20);
            jTabTarefas.getColumnModel().getColumn(1).setResizable(false);
            jTabTarefas.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTabTarefas.getColumnModel().getColumn(2).setResizable(false);
            jTabTarefas.getColumnModel().getColumn(2).setPreferredWidth(100);
            jTabTarefas.getColumnModel().getColumn(3).setResizable(false);
            jTabTarefas.getColumnModel().getColumn(3).setPreferredWidth(60);
        }

        javax.swing.GroupLayout jPanTarefasLayout = new javax.swing.GroupLayout(jPanTarefas);
        jPanTarefas.setLayout(jPanTarefasLayout);
        jPanTarefasLayout.setHorizontalGroup(
            jPanTarefasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTarefasLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 504, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanTarefasLayout.setVerticalGroup(
            jPanTarefasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        jPanEquipamentos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Equipamentos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTabEquipamentos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Seq.", "Descrição", "Valor"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        jScrollPane3.setViewportView(jTabEquipamentos);
        if (jTabEquipamentos.getColumnModel().getColumnCount() > 0) {
            jTabEquipamentos.getColumnModel().getColumn(0).setResizable(false);
            jTabEquipamentos.getColumnModel().getColumn(0).setPreferredWidth(20);
            jTabEquipamentos.getColumnModel().getColumn(1).setResizable(false);
            jTabEquipamentos.getColumnModel().getColumn(1).setPreferredWidth(200);
            jTabEquipamentos.getColumnModel().getColumn(2).setResizable(false);
            jTabEquipamentos.getColumnModel().getColumn(2).setPreferredWidth(60);
        }

        javax.swing.GroupLayout jPanEquipamentosLayout = new javax.swing.GroupLayout(jPanEquipamentos);
        jPanEquipamentos.setLayout(jPanEquipamentosLayout);
        jPanEquipamentosLayout.setHorizontalGroup(
            jPanEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanEquipamentosLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 367, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );
        jPanEquipamentosLayout.setVerticalGroup(
            jPanEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanEquipamentosLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanInforAtividadeLayout = new javax.swing.GroupLayout(jPanInforAtividade);
        jPanInforAtividade.setLayout(jPanInforAtividadeLayout);
        jPanInforAtividadeLayout.setHorizontalGroup(
            jPanInforAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInforAtividadeLayout.createSequentialGroup()
                .addGroup(jPanInforAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanInforAtividadeLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanInforAtividadeLayout.createSequentialGroup()
                        .addComponent(jPanTarefas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanEquipamentos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanInforAtividadeLayout.setVerticalGroup(
            jPanInforAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInforAtividadeLayout.createSequentialGroup()
                .addGroup(jPanInforAtividadeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanTarefas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanEquipamentos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabAtividade.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabAtividade.setText("Atividade:");

        jTexSelecao1.setEditable(false);
        jTexSelecao1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexSelecao1.setEnabled(false);

        jTexSelecao2.setEditable(false);
        jTexSelecao2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTexSelecao2.setEnabled(false);

        jForSelecao3.setEditable(false);
        jForSelecao3.setEnabled(false);

        javax.swing.GroupLayout jPanResumoLayout = new javax.swing.GroupLayout(jPanResumo);
        jPanResumo.setLayout(jPanResumoLayout);
        jPanResumoLayout.setHorizontalGroup(
            jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanResumoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanInforAtividade, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanResumoLayout.createSequentialGroup()
                        .addComponent(jLabAtividade)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, 396, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForSelecao3, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanResumoLayout.setVerticalGroup(
            jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanResumoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanResumoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabAtividade)
                    .addComponent(jTexSelecao1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexSelecao2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jForSelecao3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jPanInforAtividade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jTexPesquisarKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexPesquisarKeyReleased
        if (!jTexPesquisar.getText().isEmpty()) {
            sql = "SELECT CD_ATIVIDADE AS 'Código',"
                    + "NOME_ATIVIDADE AS Descrição,"
                    + "format (VAL_TOTAL_TAREFAS,2,'de_DE') AS 'Valor Tot. Tarefas',"
                    + "format (VAL_TOTAL_EQUIPAMENTOS,2,'de_DE') AS 'Valor Tot. Equipamentos',"
                    + "format (VAL_TOTAL_ATIVIDADE,2,'de_DE') AS 'Valor Tot. Atividades',"
                    + "SITUACAO AS Sit"
                    + " FROM GSMATIVIDADES"
                    + " WHERE NOME_ATIVIDADE LIKE '%"
                    + jTexPesquisar.getText().trim()
                    + "%'";
            //indexAtual = 0;
            buscarTodos();
            jTabAtividades.setModel(atvdao);
            if (jTabAtividades.getModel().getRowCount() > 0) {
                ajustarTabela();
            }
        } else {
            setaInicio();
        }
    }//GEN-LAST:event_jTexPesquisarKeyReleased

    private void jTabAtividadesKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTabAtividadesKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("ENTER".equals(vt.VerificarTecla(evt))) {
            JOptionPane.showMessageDialog(null, vt.VerificarTecla(evt));
            salvarSelecao();
        }
    }//GEN-LAST:event_jTabAtividadesKeyPressed

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
            java.util.logging.Logger.getLogger(PesquisarAtividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PesquisarAtividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PesquisarAtividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PesquisarAtividades.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                PesquisarAtividades dialog = new PesquisarAtividades(new javax.swing.JFrame(), true, chamador, conexao);
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
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JFormattedTextField jForSelecao3;
    private javax.swing.JLabel jLabAtividade;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabPesquisar;
    private javax.swing.JPanel jPanEquipamentos;
    private javax.swing.JPanel jPanInforAtividade;
    private javax.swing.JPanel jPanResumo;
    private javax.swing.JPanel jPanTabela;
    private javax.swing.JPanel jPanTarefas;
    private javax.swing.JPanel jPanePesquisar;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTabAtividades;
    private javax.swing.JTable jTabEquipamentos;
    private javax.swing.JTable jTabTarefas;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexPesquisar;
    private javax.swing.JTextField jTexSelecao1;
    private javax.swing.JTextField jTexSelecao2;
    // End of variables declaration//GEN-END:variables
}
