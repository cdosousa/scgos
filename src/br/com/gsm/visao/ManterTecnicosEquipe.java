/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa: GSMCA0061
 */
package br.com.gsm.visao;

import br.com.gsm.controle.CTecnicosEquipe;
import br.com.gsm.dao.TecnicosEquipeDAO;
import br.com.gsm.modelo.Equipes;
import br.com.gsm.modelo.TecnicosEquipe;
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.DefineCampoInteiro;
import br.com.modelo.FormatarValor;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 26/10/2017
 */
public class ManterTecnicosEquipe extends javax.swing.JDialog {

    // objetos do registro pai
    private TecnicosEquipe regCorr;
    private List< TecnicosEquipe> resultado;
    private CTecnicosEquipe cte;
    // objetos o registro filho(tabela)
    private static TecnicosEquipe modte;
    private TecnicosEquipeDAO tedao;
    private static Equipes modeqp;
    // objetos de ambiente 
    private VerificarTecla vt;
    private static SessaoUsuario su;
    private final NumberFormat formato;
    // variáveis de instancia
    private static Connection conexao;
    private int numReg = 0;
    private int idxCorr = 0;
    private String sql;
    private String sqlte;
    private String oper;
    private static String cdEquipe;
    private static int sequencia;
    private int indexAtual = 0;
    private double oldValorUnit = 0;
    private boolean changeValorUnit = false;
    private String oldSituacao = "";

    /**
     * Creates new form MaterTarefaAtividade
     */
    public ManterTecnicosEquipe(java.awt.Frame parent, boolean modal, Equipes modeqp, SessaoUsuario su,
            Connection conexao) {
        super(parent, modal);
        this.modeqp = modeqp;
        this.su = su;
        this.conexao = conexao;
        this.cdEquipe = modeqp.getCdEquipe();
        formato = NumberFormat.getInstance();
        formato.setMinimumFractionDigits(2);
        cte = new CTecnicosEquipe(conexao);
        modte = new TecnicosEquipe();
        try {
            tedao = new TecnicosEquipeDAO();
        } catch (SQLException ex) {
            Logger.getLogger(ManterTecnicosEquipe.class.getName()).log(Level.SEVERE, null, ex);
        }
        sql = "SELECT * FROM GSMTECNICOEQUIPE WHERE CD_EQUIPE = " + cdEquipe;
        oper = "N";
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
        jTexCodigoTecnico.setDocument(new DefineCampoInteiro());
        jTexCodigoEspecialidade.setDocument(new DefineCampoInteiro());
        jForPercObra.setDocument(new DefineCampoDecimal());
        jForValorObra.setDocument(new DefineCampoDecimal());
        jForPercIndicacao.setDocument(new DefineCampoDecimal());
        jForValorIndicacao.setDocument(new DefineCampoDecimal());
        jForPercComissao.setDocument(new DefineCampoDecimal());
    }

    // método para limpar tela
    private void limparTela() {
        jTexCodigoTecnico.setText("");
        jTexNomeTecnico.setText("");
        jTexCodigoEspecialidade.setText("");
        jTexNomeEspecialidade.setText("");
        jCheHabilitaPgtoObra.setSelected(false);
        jForPercObra.setText("0.00");
        jForValorObra.setText("0.00");
        jCheHabilitaPgtoIndicacao.setSelected(false);
        jForPercIndicacao.setText("0.00");
        jForValorIndicacao.setText("0.00");
        jCheHabilitaPgtoComissao.setSelected(false);
        jForPercComissao.setText("0.00");
        jComSituacao.setSelectedIndex(0);
        jTexCadastradoPor.setText("");
        jForDataCadastro.setText("");
        jForDataModificacao.setText("");
        jTexRegAtual.setText("");
        jTexRegTotal.setText("");
    }

    //Metodo para buscar todos
    public void buscarTodos() {
        sqlte = "select te.cpf_tecnico as 'C.P..F',"
                + "t.nome_tecnico as Nome,"
                + "e.nome_especialidade as Especialidade,"
                + "te.pagar_obra as 'Pgto. Obra',"
                + "te.pagar_indicacao as 'Pgto. Indic',"
                + "te.pagar_comissao as 'Pgto. Comissão',"
                + "te.situacao as Sit "
                + "from gsmtecnicoequipe te "
                + "left outer join gsmtecnicos as t on te.cpf_tecnico = t.cpf "
                + "left outer join gsmespecialidades as e on te.cd_especialidade = e.cd_especialidade "
                + "where te.cd_equipe = '" + modeqp.getCdEquipe()
                + "' order by te.cpf_tecnico";
        try {
            tedao.setQuery(sqlte);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
        jTabTecnicos.setModel(tedao);
        ajustarTabela();
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {
        try {
            numReg = cte.pesquisar(sql);
            idxCorr = +1;
            if (numReg > 0) {
                upRegistros();
                buscarTodos();
            } else {
                JOptionPane.showMessageDialog(null, "Nenhum registro encontrado!");
                esvaziarTabelas();
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
        cte.mostrarPesquisa(modte, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jTexCodigoTecnico.setText(modte.getCpfTecnico().trim());
        jTexNomeTecnico.setText(modte.getNomeTecnico().trim());
        jTexCodigoEspecialidade.setText(modte.getCdEspecialidade().trim());
        jTexNomeEspecialidade.setText(modte.getNomeEspecialidade().trim());
        if("S".equals(String.valueOf(modte.getPagarObra())))
            jCheHabilitaPgtoObra.setSelected(true);
        jForPercObra.setText(String.valueOf(modte.getPercObra()));
        jForValorObra.setText(String.valueOf(modte.getValorObra()));
        if("S".equals(String.valueOf(modte.getPagarIndicacao())))
            jCheHabilitaPgtoIndicacao.setSelected(true);
        jForPercIndicacao.setText(String.valueOf(modte.getPercIndicacao()));
        jForValorIndicacao.setText(String.valueOf(modte.getValorIndicacao()));
        if("S".equals(String.valueOf(modte.getPercComissao())))
            jCheHabilitaPgtoComissao.setSelected(true);
        jForPercComissao.setText(String.valueOf(modte.getPercComissao()));
        jComSituacao.setSelectedIndex(Integer.parseInt(String.valueOf(modte.getSituacao())));
        oldSituacao = String.valueOf(modte.getSituacao());
        jTexCadastradoPor.setText(modte.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modte.getDataCadastro())));
        changeValorUnit = false;
        if (modte.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(modte.getDataModificacao())));
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
    }

    // método para bloquear campos
    private void bloquearCampos() {
        jTexCodigoTecnico.setEnabled(false);
        jTexCodigoTecnico.setEditable(false);
        jTexCodigoEspecialidade.setEditable(false);
        jTexCodigoEspecialidade.setEnabled(false);
        jCheHabilitaPgtoObra.setEnabled(false);
        jForPercObra.setEditable(false);
        jForPercObra.setEnabled(false);
        jForValorObra.setEditable(false);
        jForValorObra.setEnabled(false);
        jCheHabilitaPgtoIndicacao.setEnabled(false);
        jForPercIndicacao.setEditable(false);
        jForPercIndicacao.setEnabled(false);
        jForValorIndicacao.setEditable(false);
        jForValorIndicacao.setEnabled(false);
        jCheHabilitaPgtoComissao.setEnabled(false);
        jForPercComissao.setEditable(false);
        jForPercComissao.setEnabled(false);
        jComSituacao.setEditable(false);
        jComSituacao.setEnabled(false);
    }

    // método para liberar campos
    private void liberarCampos() {
        jTexCodigoEspecialidade.setEnabled(true);
        jTexCodigoEspecialidade.setEditable(true);
        jCheHabilitaPgtoObra.setEnabled(true);
        jForPercObra.setEditable(true);
        jForPercObra.setEnabled(true);
        jForValorObra.setEditable(true);
        jForValorObra.setEnabled(true);
        jCheHabilitaPgtoIndicacao.setEnabled(true);
        jForPercIndicacao.setEditable(true);
        jForPercIndicacao.setEnabled(true);
        jForValorIndicacao.setEditable(true);
        jForValorIndicacao.setEnabled(true);
        jCheHabilitaPgtoComissao.setEnabled(true);
        jForPercComissao.setEditable(true);
        jForPercComissao.setEnabled(true);
        jComSituacao.setEditable(true);
        jComSituacao.setEnabled(true);
    }

    // método para criar novo registro
    private void novoRegistro() {
        limparTela();
        liberarCampos();
        jTexCodigoTecnico.setEditable(true);
        jTexCodigoTecnico.setEnabled(true);
        jTexCodigoTecnico.requestFocus();
        jButSalvar.setEnabled(true);
    }

    private void esvaziarTabelas() {
        jTabTecnicos.setModel(new JTable().getModel());
    }

    // método para salvar registro
    private void salvarRegistro() {
        if (jTexCodigoTecnico.getText().isEmpty() || jTexCodigoEspecialidade.getText().isEmpty() || jComSituacao.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(null, "Os campos Técnico, Especialidade e Situação são obrigatórios.");
        } else {
            DataSistema dat = new DataSistema();
            TecnicosEquipe eqp = new TecnicosEquipe();
            String data = null;
            eqp.setCdEquipe(cdEquipe);
            eqp.setCpfTecnico(jTexCodigoTecnico.getText().trim());
            eqp.setNomeTecnico(jTexNomeTecnico.getText().trim().toUpperCase());
            eqp.setCdEspecialidade(jTexCodigoEspecialidade.getText().trim());
            eqp.setNomeEspecialidade(jTexNomeEspecialidade.getText().trim());
            char pagarObra = 'N';
            if(jCheHabilitaPgtoObra.isSelected())
                pagarObra = 'S';
            eqp.setPagarObra(pagarObra);
            char pagarIndicacao = 'N';
            if(jCheHabilitaPgtoIndicacao.isSelected())
                pagarIndicacao = 'S';
            eqp.setPagarIndicacao(pagarIndicacao);
            char pagarComissao = 'N';
            if(jCheHabilitaPgtoComissao.isSelected())
                pagarComissao = 'S';
            eqp.setPagarComissao(pagarComissao);
            try {
                eqp.setPercObra(formato.parse(jForPercObra.getText()).doubleValue());
                eqp.setValorObra(formato.parse(jForValorObra.getText()).doubleValue());
                eqp.setPercIndicacao(formato.parse(jForPercIndicacao.getText()).doubleValue());
                eqp.setValorIndicacao(formato.parse(jForValorIndicacao.getText()).doubleValue());
                eqp.setPercComissao(formato.parse(jForPercComissao.getText()).doubleValue());
            } catch (ParseException ex) {
                Logger.getLogger(ManterAtividades.class.getName()).log(Level.SEVERE, null, ex);
            }
            dat.setData(data);
            data = dat.getData();
            eqp.setDataCadastro(data);
            eqp.setUsuarioCadastro(su.getUsuarioConectado());
            eqp.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1).toString().charAt(0));
            TecnicosEquipeDAO eqpdao = null;
            CTecnicosEquipe ceqp = new CTecnicosEquipe(conexao);
            try {
                eqpdao = new TecnicosEquipeDAO();
                if ("N".equals(oper)) {
                    sql = "SELECT * FROM GSMTECNICOEQUIPE WHERE CD_EQUIPE = " + eqp.getCdEquipe().trim();
                    eqpdao.adicionar(eqp);
                    modeqp.setAtualizacao(true);
                } else {
                    sql = "SELECT * FROM GSMTECNICOEQUIPE WHERE CD_EQUIPE = " + modte.getCdEquipe();
                    eqp.setCdEquipe(modte.getCdEquipe());
                    eqp.setDataModificacao(data);
                    eqpdao.atualizar(eqp);
                }
            } catch (SQLException sqlex) {
                JOptionPane.showMessageDialog(null, "Erro criar a tarefa no banco e dados!\nErr: " + sqlex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral para criar tarefa!\nErr: " + ex);
            }

            limparTela();
            bloquearCampos();
            pesquisar();
            jButSalvar.setEnabled(false);
        }
    }

    // ajustar tamanho da tabela
    public void ajustarTabela() {
        jTabTecnicos.getColumnModel().getColumn(0).setMinWidth(90);
        jTabTecnicos.getColumnModel().getColumn(0).setPreferredWidth(90);
        jTabTecnicos.getColumnModel().getColumn(1).setMinWidth(200);
        jTabTecnicos.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTabTecnicos.getColumnModel().getColumn(2).setMinWidth(150);
        jTabTecnicos.getColumnModel().getColumn(2).setPreferredWidth(150);
        jTabTecnicos.getColumnModel().getColumn(3).setMinWidth(30);
        jTabTecnicos.getColumnModel().getColumn(3).setPreferredWidth(30);
        jTabTecnicos.getColumnModel().getColumn(4).setMinWidth(30);
        jTabTecnicos.getColumnModel().getColumn(4).setPreferredWidth(30);
        jTabTecnicos.getColumnModel().getColumn(5).setMinWidth(55);
        jTabTecnicos.getColumnModel().getColumn(5).setPreferredWidth(55);
        jTabTecnicos.getColumnModel().getColumn(6).setMinWidth(5);
        jTabTecnicos.getColumnModel().getColumn(6).setPreferredWidth(5);
        if (indexAtual < 0) {
            indexAtual = 0;
        }
    }

    // método para dar zoom no campo tarefa
    private void zoomTecnicos() {
        PesquisarTecnicos zoom = new PesquisarTecnicos(new JFrame(), true, "P",conexao);
        zoom.setVisible(true);
        jTexCodigoTecnico.setText(zoom.getSelecao1().trim());
        jTexNomeTecnico.setText(zoom.getSelecao2().trim());
    }

    //método para dar zoom no campo especialidade
    private void zoomEspecialidade() {
        PesquisarEspecialidade zoom = new PesquisarEspecialidade(new JFrame(), true, "P");
        zoom.setVisible(true);
        jTexCodigoEspecialidade.setText(zoom.getSelec1().trim());
        jTexNomeEspecialidade.setText(zoom.getSelec2().trim());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanTecnicosEquipe = new javax.swing.JPanel();
        jLabTecnico = new javax.swing.JLabel();
        jLabEspecialidade = new javax.swing.JLabel();
        jTexCodigoTecnico = new javax.swing.JTextField();
        jTexCodigoEspecialidade = new javax.swing.JTextField();
        jTexNomeTecnico = new javax.swing.JTextField();
        jTexNomeEspecialidade = new javax.swing.JTextField();
        jComSituacao = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jPanPgtoIndicacao = new javax.swing.JPanel();
        jCheHabilitaPgtoIndicacao = new javax.swing.JCheckBox();
        jLabPercIndicacao = new javax.swing.JLabel();
        jForPercIndicacao = new FormatarValor((FormatarValor.PORCENTAGEM));
        jLabValorIndicacao = new javax.swing.JLabel();
        jForValorIndicacao = new FormatarValor((FormatarValor.MOEDA));
        jPanPgtoComissao = new javax.swing.JPanel();
        jCheHabilitaPgtoComissao = new javax.swing.JCheckBox();
        jLabPercComissao = new javax.swing.JLabel();
        jForPercComissao = new FormatarValor((FormatarValor.PORCENTAGEM));
        jPanel1 = new javax.swing.JPanel();
        jCheHabilitaPgtoObra = new javax.swing.JCheckBox();
        jLabel2 = new javax.swing.JLabel();
        jForPercObra = new FormatarValor((FormatarValor.PORCENTAGEM));
        jLabel3 = new javax.swing.JLabel();
        jForValorObra = new FormatarValor((FormatarValor.MOEDA));
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
        jPanTabelaTecnicos = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabTecnicos = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manter Técnicos Equipe");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setModal(true);

        jPanTecnicosEquipe.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Incluir Técnicos X Equipe", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabTecnico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTecnico.setText("Técnico:");

        jLabEspecialidade.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabEspecialidade.setText("Especialidade:");

        jTexCodigoTecnico.setEnabled(false);
        jTexCodigoTecnico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexCodigoTecnicoKeyReleased(evt);
            }
        });

        jTexCodigoEspecialidade.setEnabled(false);
        jTexCodigoEspecialidade.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexCodigoEspecialidadeKeyReleased(evt);
            }
        });

        jTexNomeTecnico.setEditable(false);
        jTexNomeTecnico.setEnabled(false);

        jTexNomeEspecialidade.setEditable(false);
        jTexNomeEspecialidade.setEnabled(false);

        jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ativo", "Inativo" }));
        jComSituacao.setEnabled(false);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("Situação:");

        jPanPgtoIndicacao.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jCheHabilitaPgtoIndicacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheHabilitaPgtoIndicacao.setText("Habilita Pgto de Indicação");
        jCheHabilitaPgtoIndicacao.setEnabled(false);

        jLabPercIndicacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPercIndicacao.setText("% Indicação:");

        jForPercIndicacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForPercIndicacao.setEnabled(false);
        jForPercIndicacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jForPercIndicacaoActionPerformed(evt);
            }
        });

        jLabValorIndicacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValorIndicacao.setText("Val. Indicação:");

        jForValorIndicacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForValorIndicacao.setEnabled(false);

        javax.swing.GroupLayout jPanPgtoIndicacaoLayout = new javax.swing.GroupLayout(jPanPgtoIndicacao);
        jPanPgtoIndicacao.setLayout(jPanPgtoIndicacaoLayout);
        jPanPgtoIndicacaoLayout.setHorizontalGroup(
            jPanPgtoIndicacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPgtoIndicacaoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanPgtoIndicacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheHabilitaPgtoIndicacao, javax.swing.GroupLayout.PREFERRED_SIZE, 241, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanPgtoIndicacaoLayout.createSequentialGroup()
                        .addComponent(jLabPercIndicacao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForPercIndicacao, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabValorIndicacao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForValorIndicacao, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanPgtoIndicacaoLayout.setVerticalGroup(
            jPanPgtoIndicacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPgtoIndicacaoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheHabilitaPgtoIndicacao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanPgtoIndicacaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabPercIndicacao)
                    .addComponent(jForPercIndicacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabValorIndicacao)
                    .addComponent(jForValorIndicacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanPgtoComissao.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jCheHabilitaPgtoComissao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheHabilitaPgtoComissao.setText("Habilita Pgto de Comissão");
        jCheHabilitaPgtoComissao.setEnabled(false);

        jLabPercComissao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPercComissao.setText("% Sobre Comissão:");

        jForPercComissao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForPercComissao.setEnabled(false);
        jForPercComissao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jForPercComissaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanPgtoComissaoLayout = new javax.swing.GroupLayout(jPanPgtoComissao);
        jPanPgtoComissao.setLayout(jPanPgtoComissaoLayout);
        jPanPgtoComissaoLayout.setHorizontalGroup(
            jPanPgtoComissaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPgtoComissaoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanPgtoComissaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jCheHabilitaPgtoComissao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanPgtoComissaoLayout.createSequentialGroup()
                        .addComponent(jLabPercComissao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForPercComissao, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanPgtoComissaoLayout.setVerticalGroup(
            jPanPgtoComissaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPgtoComissaoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheHabilitaPgtoComissao)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanPgtoComissaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabPercComissao)
                    .addComponent(jForPercComissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jCheHabilitaPgtoObra.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jCheHabilitaPgtoObra.setText("Habilita Pgto de Obra");
        jCheHabilitaPgtoObra.setEnabled(false);

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("% Sobre Obra:");

        jForPercObra.setEnabled(false);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Valor sobre Obra:");

        jForValorObra.setEnabled(false);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForPercObra, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3))
                    .addComponent(jCheHabilitaPgtoObra, javax.swing.GroupLayout.PREFERRED_SIZE, 203, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jForValorObra, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jCheHabilitaPgtoObra)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jForPercObra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jForValorObra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanTecnicosEquipeLayout = new javax.swing.GroupLayout(jPanTecnicosEquipe);
        jPanTecnicosEquipe.setLayout(jPanTecnicosEquipeLayout);
        jPanTecnicosEquipeLayout.setHorizontalGroup(
            jPanTecnicosEquipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTecnicosEquipeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTecnicosEquipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanTecnicosEquipeLayout.createSequentialGroup()
                        .addGroup(jPanTecnicosEquipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabEspecialidade, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabTecnico, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanTecnicosEquipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanTecnicosEquipeLayout.createSequentialGroup()
                                .addComponent(jTexCodigoEspecialidade, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexNomeEspecialidade, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanTecnicosEquipeLayout.createSequentialGroup()
                                .addComponent(jTexCodigoTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexNomeTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanTecnicosEquipeLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jPanPgtoIndicacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jPanPgtoComissao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(10, 10, 10))
        );
        jPanTecnicosEquipeLayout.setVerticalGroup(
            jPanTecnicosEquipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTecnicosEquipeLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTecnicosEquipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabTecnico)
                    .addComponent(jTexCodigoTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addGroup(jPanTecnicosEquipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabEspecialidade)
                    .addComponent(jTexCodigoEspecialidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeEspecialidade, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanTecnicosEquipeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanPgtoComissao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanPgtoIndicacao, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        jButImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButImprimirActionPerformed(evt);
            }
        });
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
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabCadastradoPor)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(47, 47, 47)
                    .addComponent(jLabDataCadastro)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabDataModificacao)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(24, 24, 24))
            );
            jPanRodapeLayout.setVerticalGroup(
                jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanRodapeLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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

            jPanTabelaTecnicos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Tarefas", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jTabTecnicos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabTecnicos.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null},
                    {null, null, null, null, null, null, null}
                },
                new String [] {
                    "C.P.F", "Nome", "Especialidade", "Pgto. Obra", "Pgto. Indic.", "Pgto. Comissão", "Sit"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jScrollPane1.setViewportView(jTabTecnicos);
            if (jTabTecnicos.getColumnModel().getColumnCount() > 0) {
                jTabTecnicos.getColumnModel().getColumn(0).setResizable(false);
                jTabTecnicos.getColumnModel().getColumn(0).setPreferredWidth(90);
                jTabTecnicos.getColumnModel().getColumn(1).setResizable(false);
                jTabTecnicos.getColumnModel().getColumn(1).setPreferredWidth(200);
                jTabTecnicos.getColumnModel().getColumn(2).setResizable(false);
                jTabTecnicos.getColumnModel().getColumn(2).setPreferredWidth(150);
                jTabTecnicos.getColumnModel().getColumn(3).setResizable(false);
                jTabTecnicos.getColumnModel().getColumn(3).setPreferredWidth(30);
                jTabTecnicos.getColumnModel().getColumn(4).setResizable(false);
                jTabTecnicos.getColumnModel().getColumn(4).setPreferredWidth(30);
                jTabTecnicos.getColumnModel().getColumn(5).setResizable(false);
                jTabTecnicos.getColumnModel().getColumn(5).setPreferredWidth(50);
                jTabTecnicos.getColumnModel().getColumn(6).setResizable(false);
                jTabTecnicos.getColumnModel().getColumn(6).setPreferredWidth(10);
            }

            javax.swing.GroupLayout jPanTabelaTecnicosLayout = new javax.swing.GroupLayout(jPanTabelaTecnicos);
            jPanTabelaTecnicos.setLayout(jPanTabelaTecnicosLayout);
            jPanTabelaTecnicosLayout.setHorizontalGroup(
                jPanTabelaTecnicosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTabelaTecnicosLayout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 957, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(13, Short.MAX_VALUE))
            );
            jPanTabelaTecnicosLayout.setVerticalGroup(
                jPanTabelaTecnicosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 178, javax.swing.GroupLayout.PREFERRED_SIZE)
            );

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanRodape, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanTecnicosEquipe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanTabelaTecnicos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanTecnicosEquipe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanTabelaTecnicos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButNovoActionPerformed
        // TODO add your handling code here:
        novoRegistro();
        oper = "N";
    }//GEN-LAST:event_jButNovoActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        // TODO add your handling code here:
        liberarCampos();
        jTexCodigoTecnico.requestFocus();
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
        if (!jTexCodigoTecnico.getText().isEmpty()) {
            try {
                DataSistema dat = new DataSistema();
                String data = null;
                TecnicosEquipe tt = new TecnicosEquipe();
                tt.setCdEquipe(cdEquipe);
                tt.setCpfTecnico(jTexCodigoTecnico.getText().toUpperCase());
                TecnicosEquipeDAO ttDAO = new TecnicosEquipeDAO();
                ttDAO.excluir(tt);
                dat.setData(data);
                data = dat.getData();
                modeqp.setDataModificacao(data);
                modeqp.setAtualizacao(true);
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
        sql = "SELECT * FROM GSMTECNICOEQUIPE WHERE CD_EQUIPE = " + cdEquipe;
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

    private void jButImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButImprimirActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButImprimirActionPerformed

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jTexCodigoTecnicoKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCodigoTecnicoKeyReleased
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomTecnicos();
        }
    }//GEN-LAST:event_jTexCodigoTecnicoKeyReleased

    private void jTexCodigoEspecialidadeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCodigoEspecialidadeKeyReleased
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomEspecialidade();
        }
    }//GEN-LAST:event_jTexCodigoEspecialidadeKeyReleased

    private void jForPercComissaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jForPercComissaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jForPercComissaoActionPerformed

    private void jForPercIndicacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jForPercIndicacaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jForPercIndicacaoActionPerformed

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
            java.util.logging.Logger.getLogger(ManterTecnicosEquipe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterTecnicosEquipe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterTecnicosEquipe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterTecnicosEquipe.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                ManterTecnicosEquipe dialog = new ManterTecnicosEquipe(new javax.swing.JFrame(), true, modeqp, su,conexao);
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
    private javax.swing.JCheckBox jCheHabilitaPgtoComissao;
    private javax.swing.JCheckBox jCheHabilitaPgtoIndicacao;
    private javax.swing.JCheckBox jCheHabilitaPgtoObra;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JFormattedTextField jForPercComissao;
    private javax.swing.JFormattedTextField jForPercIndicacao;
    private javax.swing.JFormattedTextField jForPercObra;
    private javax.swing.JFormattedTextField jForValorIndicacao;
    private javax.swing.JFormattedTextField jForValorObra;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabEspecialidade;
    private javax.swing.JLabel jLabPercComissao;
    private javax.swing.JLabel jLabPercIndicacao;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabTecnico;
    private javax.swing.JLabel jLabValorIndicacao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanPgtoComissao;
    private javax.swing.JPanel jPanPgtoIndicacao;
    private javax.swing.JPanel jPanRodape;
    private javax.swing.JPanel jPanTabelaTecnicos;
    private javax.swing.JPanel jPanTecnicosEquipe;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTabTecnicos;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexCodigoEspecialidade;
    private javax.swing.JTextField jTexCodigoTecnico;
    private javax.swing.JTextField jTexNomeEspecialidade;
    private javax.swing.JTextField jTexNomeTecnico;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
