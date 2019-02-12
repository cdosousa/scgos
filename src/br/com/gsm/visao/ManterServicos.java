/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GSMCA0070
 */
package br.com.gsm.visao;

import br.com.controle.CBuscarSequencia;
import br.com.gcs.visao.PesquisarMateriais;
import br.com.gcs.visao.PesquisarUnidadesMedida;
import br.com.gsm.controle.*;
import br.com.gsm.dao.*;
import br.com.gsm.modelo.*;
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.DefineCampoInteiro;
import br.com.modelo.FormatarValor;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;
import javax.swing.JOptionPane;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 27/10/2017
 */
public class ManterServicos extends javax.swing.JFrame {

    // Objetos do registro pai
    private Servicos regCorr;
    private List< Servicos> resultado;
    private CServicos csrv;
    private Servicos modsrv;

    // Objetos do registro filho Atividades
    private AtividadesServico modatsv;
    private CAtividadesServico catsv;
    private AtividadesServicoDAO atsvdao;

    // Objetos do registro filho Tarefas
    private TarefasAtividade ta;
    private CTarefasAtividades cta;
    private TarefasAtividadesDAO tadao;

    // Objetos do registro filho Equipamentos
    private EquipamentosAtividade ea;
    private CEquipamentosAtividade cea;
    private EquipamentosAtividadeDAO eadao;
    
    // Objetos do registro filho Materiais
    private MateriaisServico ms;
    private CMateriaisServico cms;
    private MateriaisServicoDAO msdao;

// Objetos de ambiente
    private VerificarTecla vt;
    private static SessaoUsuario su;
    private static Connection conexao;
    private NumberFormat formato;

    // Variáveis de instância registro pai
    private int numReg = 0;
    private int idxCorr = 0;
    private String sql;
    private String oper;
    private final String TABELA = "gsmservico";

    // Variáveis de instância registro filho atividades
    private String sqlatsv;
    private int sequenciaAtividade;
    private String cdAtividade;
    private int indexAtualAtividades = 0;
    private int numLinhaAtividades = 0;

    // Variáveis de instância registro filho tarefas
    private String sqlta;
    private int indexAtualTarefas = 0;
    private String cdTarefa;
    private int sequenciaTarefa;

    // Variáveis de instância registro filho equipamentos
    private String sqlea;
    private int indexAtualEquipamentos = 0;
    private String cdEquipamento;
    private int sequenciaEquipamento;
    
    // Variáveis de instância registro filho materiais
    private String sqlms;
    private int indexAtualMateriais = 0;
    private String cdMaterial;
    private int sequenciaMaterial;

    public ManterServicos(SessaoUsuario su, Connection conexao) {
        formato = NumberFormat.getInstance();
        this.su = su;
        this.conexao = conexao;
        oper = "N";
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formatarCampos();
        limparTela();
        setLocationRelativeTo(null);
        mudarLinhaAtividade();
        mudarLinhaTarefas();
        mudarLinhaEquipamentos();
        mudarLinhaMateriais();
        objServico();
        objAtividadesServico();
        objTarefasAtividades();
        objEquipamentosAtividades();
        objMateriasServico();
        this.dispose();
    }

    // construtor padrão
    public ManterServicos() {

    }

    // método para setar formato do campo
    private void formatarCampos() {
        jTexCdProdutoVinculado.setDocument(new DefineCampoInteiro());
        jForCdServico.setDocument(new DefineCampoInteiro());
        jForValorServico.setDocument(new DefineCampoDecimal());
        jForValorTotalAtividades.setDocument(new DefineCampoDecimal());
        jForValorTotalMateriais.setDocument(new DefineCampoDecimal());
    }

    // instancia objetos Servico
    private void objServico() {
        csrv = new CServicos(conexao);
        modsrv = new Servicos();
    }

    // instancia objetos AtividadesServico
    private void objAtividadesServico() {
        modatsv = new AtividadesServico();
        catsv = new CAtividadesServico(conexao);
        try {
            atsvdao = new AtividadesServicoDAO(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(ManterServicos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // instancia objetos tarefasAtividades
    private void objTarefasAtividades() {
        ta = new TarefasAtividade();
        cta = new CTarefasAtividades(conexao);
        try {
            tadao = new TarefasAtividadesDAO(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(ManterServicos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // instancia objetos equiapamentosAtividades
    private void objEquipamentosAtividades() {
        ea = new EquipamentosAtividade();
        cea = new CEquipamentosAtividade(conexao);
        try {
            eadao = new EquipamentosAtividadeDAO(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(ManterServicos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    // instancia objetos materiaisServico
    private void objMateriasServico(){
        ms = new MateriaisServico();
        cms = new CMateriaisServico(conexao);
        try {
            msdao = new MateriaisServicoDAO(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(ManterServicos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // método para limpar tela
    private void limparTela() {
        jForCdServico.setText("");
        jTexNomeServico.setText("");
        jTexCdProdutoVinculado.setText("");
        jTexNomeProdutoVinculado.setText("");
        jTexCdUnidMedida.setText("");
        jTexAreaDescComercial.setText("");
        jForValorServico.setText("0,00");
        jForValorTotalAtividades.setText("0,00");
        jForValorTotalMateriais.setText("0,00");
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
        jForCdServico.setEditable(true);
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {
        //csrv = new CServicos();
        //modsrv = new Servicos();
        jButEditar.setEnabled(true);
        jButExcluir.setEnabled(true);
        if (jTexNomeServico.getText().isEmpty()) {
            sql = "select s.cd_servico,"
                    + "    s.nome_servico,"
                    + "    s.cd_material,"
                    + "    m.nome_material,"
                    + "    s.cd_unidmedida,"
                    + "    s.descricao_comercial,"
                    + "    s.valor_servico,"
                    + "    s.valor_material,"
                    + "    s.valor_atividades,"
                    + "    s.usuario_cadastro,"
                    + "    s.data_cadastro,"
                    + "    s.data_modificacao,"
                    + "    s.situacao"
                    + " from gsmservico as s"
                    + " left outer join gcsmaterial as m on s.cd_material = m.cd_material";
        } else {
            sql = "select s.cd_servico,"
                    + "    s.nome_servico,"
                    + "    s.cd_material,"
                    + "    m.nome_material,"
                    + "    s.cd_unidmedida,"
                    + "    s.descricao_comercial,"
                    + "    s.valor_servico,"
                    + "    s.valor_material,"
                    + "    s.valor_atividades,"
                    + "    s.usuario_cadastro,"
                    + "    s.data_cadastro,"
                    + "    s.data_modificacao,"
                    + "    s.situacao"
                    + " from gsmservico as s"
                    + " left outer join gcsmaterial as m on s.cd_material = m.cd_material"
                    + " where s.nome_servico like '%" + jTexNomeServico.getText().trim() + "%'";
        }
        try {
            numReg = csrv.pesquisar(sql);
            idxCorr = +1;
            if (numReg > 0) {
                upRegistros();
            } else {
                JOptionPane.showMessageDialog(null, "Nenhum registro encontrado!");
            }
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Argumento inválido na busca\nErr: " + iae.getCause());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Pesquisar os Registro\nPrograma ManterServicos.\nErr: " + ex);
        }
    }

    // atualizar registros na tela
    private void upRegistros() {
        jTexRegTotal.setText(Integer.toString(numReg));
        jTexRegAtual.setText(Integer.toString(idxCorr));
        csrv.mostrarPesquisa(modsrv, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jForCdServico.setText(modsrv.getCdServico());
        jTexNomeServico.setText(modsrv.getNomeServico());
        jTexCdProdutoVinculado.setText(modsrv.getCdMaterial());
        jTexNomeProdutoVinculado.setText(modsrv.getNomeMaterial());
        jTexCdUnidMedida.setText(modsrv.getCdUnidMedida());
        jTexAreaDescComercial.setText(modsrv.getDescricaoComercial());
        jComSituacao.setSelectedIndex(Integer.parseInt(modsrv.getSituacao()));
        jForValorServico.setText(String.valueOf(modsrv.getValorServico()).trim());
        jForValorTotalAtividades.setText(String.valueOf(modsrv.getValTotalAtividade()).trim());
        jTexCadastradoPor.setText(modsrv.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modsrv.getDataCadastro())));
        if (modsrv.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(modsrv.getDataModificacao())));
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
        mostrarAtividades();
        mostrarMateriais();
        if(jForValorServico.getText().toString().trim().isEmpty()){
            jForValorServico.setText(String.valueOf(modsrv.getValorServico()).trim());
        }
    }

    //Bloquear os campos da tela
    private void bloquearCampos() {
        jForCdServico.setEditable(false);
        jTexNomeServico.setEditable(false);
        jTexCdProdutoVinculado.setEditable(false);
        jTexNomeProdutoVinculado.setEditable(false);
        jTexCdUnidMedida.setEditable(false);
        jTexAreaDescComercial.setEditable(false);
        jComSituacao.setEditable(false);
        jForValorServico.setEditable(false);
        jForValorTotalAtividades.setEditable(false);
    }

    //Loberar os campos da tela para atualização
    private void liberarCampos() {
        jTexNomeServico.setEditable(true);
        jTexCdProdutoVinculado.setEditable(true);
        jTexNomeProdutoVinculado.setEditable(true);
        jTexCdUnidMedida.setEditable(true);
        jTexAreaDescComercial.setEditable(true);
        jComSituacao.setEditable(true);
        jForValorServico.setEditable(true);
        jForValorTotalAtividades.setEditable(true);
    }

    // método para cria novo registro
    private void novoRegistro() {
        jButEditar.setEnabled(false);
        limparTela();
        liberarCampos();
        esvaziarTabelas();
        jForCdServico.setEditable(true);
        jForCdServico.requestFocus();
    }
    // método para limpar tabelas

    private void esvaziarTabelas() {
        jTabAtividades.setModel(new JTable().getModel());
        jTabTarefas.setModel(new JTable().getModel());
        jTabEquipamentos.setModel(new JTable().getModel());
    }

    // método para salvar registro
    private void salvarRegistro() {
        if (jTexNomeServico.getText().isEmpty() || jTexCdProdutoVinculado.getText().isEmpty() || jTexCdUnidMedida.getText().isEmpty()
                || jComSituacao.getSelectedItem().toString().substring(0, 1).toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Os campos Descrição e Situação, Produto Vinculado e Unidade de Medidas são obrigatórios.");
        } else {
            DataSistema dat = new DataSistema();
            Servicos srv = new Servicos();
            String data = null;
            srv.setNomeServico(jTexNomeServico.getText().trim().toUpperCase());
            srv.setCdMaterial(jTexCdProdutoVinculado.getText());
            srv.setCdUnidMedida(jTexCdUnidMedida.getText());
            srv.setDescricaoComercial(jTexAreaDescComercial.getText());
            srv.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1));
            try {
                srv.setValorServico(formato.parse(jForValorServico.getText()).doubleValue());
                srv.setValTotalAtividade(formato.parse(jForValorTotalAtividades.getText()).doubleValue());
                srv.setValorMaterial(formato.parse(jForValorTotalMateriais.getText()).doubleValue());
            } catch (ParseException ex) {
                Logger.getLogger(ManterServicos.class.getName()).log(Level.SEVERE, null, ex);
            }
            dat.setData(data);
            data = dat.getData();
            srv.setDataCadastro(data);
            srv.setUsuarioCadastro(su.getUsuarioConectado());
            ServicosDAO srvdao = null;
            CServicos csrv = new CServicos(conexao);
            try {
                srvdao = new ServicosDAO(conexao);
                if ("N".equals(oper)) {
                    //gera código do produto
                    CBuscarSequencia cb = new CBuscarSequencia(su, TABELA,8);
                    String sequencia = cb.getRetorno();
                    csrv.gerarCodigo(srv, sequencia);
                    sql = "SELECT * FROM GSMSERVICO WHERE CD_SERVICO = '" + srv.getCdServico().trim() + "'";
                    srvdao.adicionar(srv);
                } else {
                    sql = "SELECT * FROM GSMSERVICO WHERE CD_SERVICO = '" + modsrv.getCdServico() + "'";
                    srv.setCdServico(modsrv.getCdServico());
                    srv.setDataModificacao(data);
                    srvdao.atualizar(srv);
                }
            } catch (SQLException sqlex) {
                JOptionPane.showMessageDialog(null, "Erro criar o serviço no banco e dados!\nErr: " + sqlex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral para criar o serviço!\nErr: " + ex);
            }
            modsrv.setAtualizacao(false);
            limparTela();
            bloquearCampos();
            pesquisar();
        }
    }
    
    // incluir Atividade
    private void incluirAtividade() {
        ManterAtividadesServico mts = new ManterAtividadesServico(new JFrame(), true, modsrv, sequenciaAtividade, su, conexao);
        mts.setVisible(true);
        mostrarAtividades();
        if (modsrv.isAtualizacao()) {
            oper = "A";
            salvarRegistro();
        }
    }
    
    // incluir Materiais
    private void incluirMateriais() {
        ManterMaterialServico mat = new ManterMaterialServico(new JFrame(), true, modsrv, sequenciaMaterial,
                su, conexao);
        mat.setVisible(true);
        mostrarMateriais();
        if (modsrv.isAtualizacao()) {
            oper = "A";
            salvarRegistro();
        }
    }

    /**
     * INÍCIO SESSÃO PARA BUSCAR E ATUALIZAR ATIVIDADE DO SERVICO
     */
    // buscando tabela de atividades
    private void mostrarAtividades() {
        int numLinhas;
        sqlatsv = "select a.sequencia as 'Seq.', "
                + "a.cd_atividade as 'Cód.', "
                + "b.nome_atividade as Descrição, "
                + "format(a.valor_atividade,2,'de_DE')as Valor, "
                + "a.situacao as Situação "
                + "from gsmatividadesservico as a "
                + "left outer join gsmatividades as b on a.cd_atividade = b.cd_atividade "
                + "where a.cd_servico = " + modsrv.getCdServico()
                + " order by a.sequencia";
        buscarTodasAtividades();
        jTabAtividades.setModel(atsvdao);
        numLinhas = jTabAtividades.getModel().getRowCount();
        numLinhaAtividades = numLinhas;
        if (numLinhas > 0) {
            sequenciaAtividade = Integer.parseInt(String.format("%s", jTabAtividades.getValueAt(--numLinhas, 0)));
            ajustarTabelaAtividades();
        } else {
            sequenciaAtividade = 0;
            //jForValorTotalAtividades.setText("0.00");
            //jForValorServico.setText(String.valueOf(msdao.getTotalMaterial() + atsvdao.getTotalAtividade()));
        }
    }

    //Metodos para buscar atividades do serviço
    public void buscarTodasAtividades() {
        try {
            atsvdao.setQuery(sqlatsv);
            //jForValorTotalAtividades.setText(String.valueOf(atsvdao.getTotalAtividade()));
            //jForValorServico.setText(String.valueOf(msdao.getTotalMaterial()+atsvdao.getTotalAtividade()));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela tarefas
    public void ajustarTabelaAtividades() {
        jTabAtividades.getColumnModel().getColumn(0).setMinWidth(10);
        jTabAtividades.getColumnModel().getColumn(0).setPreferredWidth(10);
        jTabAtividades.getColumnModel().getColumn(1).setMinWidth(20);
        jTabAtividades.getColumnModel().getColumn(1).setPreferredWidth(20);
        jTabAtividades.getColumnModel().getColumn(2).setMinWidth(190);
        jTabAtividades.getColumnModel().getColumn(2).setPreferredWidth(190);
        jTabAtividades.getColumnModel().getColumn(3).setMinWidth(10);
        jTabAtividades.getColumnModel().getColumn(3).setPreferredWidth(10);
        jTabAtividades.getColumnModel().getColumn(4).setMinWidth(20);
        jTabAtividades.getColumnModel().getColumn(4).setPreferredWidth(20);
        if (indexAtualAtividades < 0) {
            indexAtualAtividades = 0;
        }
        cdAtividade = String.format("%s", jTabAtividades.getValueAt(indexAtualAtividades, 1));
        mostrarTarefas();
        mostrarEquipamentos();

    }

    // criando um listener quando mudar de linha na atividade
    private void mudarLinhaAtividade() {
        jTabAtividades.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtualAtividades = jTabAtividades.getSelectedRow();
                ajustarTabelaAtividades();
                mostrarTarefas();
                mostrarEquipamentos();
            }
        });

        jTabAtividades.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                vt = new VerificarTecla();
                if ("BAIXO".equals(vt.VerificarTecla(e).toUpperCase())) {
                    incluirAtividade();
                }
            }
        });
    }

    /**
     * FIM SESSÃO PARA BUSCAR E ATUALIZAR AS ATIVIDADE DO SERVICO
     */
    /*
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * INÍCIO SESSÃO PARA BUSCAR E ATUALIZAR A TAREFAS ATIVIDADE
     */
    // buscando tabela de tarefas
    private void mostrarTarefas() {
        int numLinhas;
        sqlta = "select ta.sequencia as 'Seq.', "
                + "t.nome_tarefa as Descrição, "
                + "e.nome_especialidade as Especialidade, "
                + "format(ta.valor_unit,2,'de_DE') as Valor, "
                + "ta.situacao as Situação "
                + "from gsmtarefaatividade as ta "
                + "left outer join gsmtarefas as t on ta.cd_tarefa = t.cd_tarefa "
                + "left outer join gsmespecialidades as e on ta.cd_especialidade = e.cd_especialidade "
                + "where ta.cd_atividade = " + cdAtividade
                + " order by ta.sequencia";
        buscarTodasTarefas();
        jTabTarefas.setModel(tadao);
        numLinhas = jTabTarefas.getModel().getRowCount();
        if (numLinhas > 0) {
            sequenciaTarefa = Integer.parseInt(String.format("%s", jTabTarefas.getValueAt(--numLinhas, 0)));
            ajustarTabelaTarefas();
        } else {
            sequenciaTarefa = 0;
        }
    }

    // criando um listener quando mudar de linha
    private void mudarLinhaTarefas() {
        jTabTarefas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtualTarefas = jTabTarefas.getSelectedRow();
                ajustarTabelaTarefas();
            }
        });

        jTabTarefas.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                vt = new VerificarTecla();
            }
        });
    }

    //Metodos para buscar tarefas da atividas
    public void buscarTodasTarefas() {
        try {
            tadao.setQuery(sqlta);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela tarefas
    public void ajustarTabelaTarefas() {
        jTabTarefas.getColumnModel().getColumn(0).setMinWidth(30);
        jTabTarefas.getColumnModel().getColumn(0).setPreferredWidth(30);
        jTabTarefas.getColumnModel().getColumn(1).setMinWidth(190);
        jTabTarefas.getColumnModel().getColumn(1).setPreferredWidth(190);
        jTabTarefas.getColumnModel().getColumn(2).setMinWidth(130);
        jTabTarefas.getColumnModel().getColumn(2).setPreferredWidth(130);
        jTabTarefas.getColumnModel().getColumn(3).setMinWidth(20);
        jTabTarefas.getColumnModel().getColumn(3).setPreferredWidth(20);
        jTabTarefas.getColumnModel().getColumn(4).setMinWidth(50);
        jTabTarefas.getColumnModel().getColumn(4).setPreferredWidth(50);
        if (indexAtualTarefas < 0) {
            indexAtualTarefas = 0;
        }
        cdTarefa = String.format("%s", jTabTarefas.getValueAt(indexAtualTarefas, 0));
    }

    /**
     * FIM SESSÃO PARA BUSCAR E ATUALIZAR OS TAREFAS DA ATIVIDADE
     */
    /*
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * INÍCIO SESSÃO PARA BUSCAR E ATUALIZAR OS EQUIPAMENTOS DA ATIVIDADE
     */
    // Método para mostrar equipamentos
    private void mostrarEquipamentos() {
        int numLinhas;
        sqlea = "select ea.sequencial as 'Seq.', "
                + "e.nome_equipamento as Descrição, "
                + "format(ea.valor_unit,2,'de_DE')as Valor, "
                + "ea.situacao as Situação "
                + "from gsmequipamentoatividade as ea "
                + "left outer join gsmequipamentos as e on ea.cd_equipamento = e.cd_equipamento "
                + "where ea.cd_atividade = " + cdAtividade
                + " order by ea.sequencial";
        buscarTodosEquipamentos();
        jTabEquipamentos.setModel(eadao);
        numLinhas = jTabEquipamentos.getModel().getRowCount();
        if (numLinhas > 0) {
            sequenciaEquipamento = Integer.parseInt(String.format("%s", jTabEquipamentos.getValueAt(--numLinhas, 0)));
            ajustarTabelaEquipamentos();
        } else {
            sequenciaEquipamento = 0;
        }
    }

    // criando um listener quando mudar de linha
    private void mudarLinhaEquipamentos() {
        jTabEquipamentos.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtualEquipamentos = jTabEquipamentos.getSelectedRow();
                ajustarTabelaEquipamentos();
            }
        });

        jTabEquipamentos.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                vt = new VerificarTecla();
            }
        });
    }

    //Metodos para buscar equipamento da atividas
    public void buscarTodosEquipamentos() {
        try {
            eadao.setQuery(sqlea);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela equipamento
    public void ajustarTabelaEquipamentos() {
        jTabEquipamentos.getColumnModel().getColumn(0).setMinWidth(30);
        jTabEquipamentos.getColumnModel().getColumn(0).setPreferredWidth(30);
        jTabEquipamentos.getColumnModel().getColumn(1).setMinWidth(200);
        jTabEquipamentos.getColumnModel().getColumn(1).setPreferredWidth(200);
        jTabEquipamentos.getColumnModel().getColumn(2).setMinWidth(60);
        jTabEquipamentos.getColumnModel().getColumn(2).setPreferredWidth(60);
        jTabEquipamentos.getColumnModel().getColumn(3).setMinWidth(40);
        jTabEquipamentos.getColumnModel().getColumn(3).setPreferredWidth(40);
        if (indexAtualEquipamentos < 0) {
            indexAtualEquipamentos = 0;
            cdEquipamento = String.format("%s", jTabEquipamentos.getValueAt(indexAtualEquipamentos, 0));
        }
    }
    /**
     * FIM SESSÃO PARA BUSCAR E ATUALIZAR OS EQUIPAMENTOS DA ATIVIDADE
     */
    /*
    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * INÍCIO SESSÃO PARA BUSCAR E ATUALIZAR OS MATERIAIS DO SERVICO
     */
    // Método para mostrar materiais
    private void mostrarMateriais() {
        int numLinhas;
        sqlms = "select ms.seq as 'Seq.',"
                + "ms.cd_material as 'Cod.',"
                + "mat.nome_material as 'Descrição',"
                + "ms.cd_unidmedida as 'UM', "
                + "format(ms.qtde_material,3,'de_DE') as 'Qtd.', "
                + "format(ms.valor_unit,2,'de_DE') as Valor"
                + " from gsmmaterialservico as ms"
                + " left outer join gcsmaterial as mat on ms.cd_material = mat.cd_material "
                + " where ms.cd_servico = " + jForCdServico.getText();
        buscarTodosMateriais();
        jTabMateriais.setModel(msdao);
        numLinhas = jTabMateriais.getModel().getRowCount();
        if (numLinhas > 0) {
            sequenciaMaterial = Integer.parseInt(String.format("%s", jTabMateriais.getValueAt(--numLinhas, 0)));
            ajustarTabelaMateriais();
        } else {
            sequenciaEquipamento = 0;
            jForValorTotalMateriais.setText("0.00");
            //jForValorServico.setText(String.valueOf(msdao.getTotalMaterial() + atsvdao.getTotalAtividade()));
        }
    }

    // criando um listener quando mudar de linha
    private void mudarLinhaMateriais() {
        jTabMateriais.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                indexAtualMateriais = jTabMateriais.getSelectedRow();
                ajustarTabelaMateriais();
            }
        });

        jTabMateriais.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                vt = new VerificarTecla();
            }
        });
    }

    //Metodos para buscar Materiais da Atividade
    public void buscarTodosMateriais() {
        try {
            msdao.setQuery(sqlms);
            jForValorTotalMateriais.setText(String.valueOf(msdao.getTotalMaterial()));
            //jForValorServico.setText(String.valueOf(msdao.getTotalMaterial()+atsvdao.getTotalAtividade()));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na Busca!\nErr: " + ex);
        }
    }

    // ajustar tamanho da tabela equipamento
    public void ajustarTabelaMateriais() {
        jTabMateriais.getColumnModel().getColumn(0).setMinWidth(10);
        jTabMateriais.getColumnModel().getColumn(0).setPreferredWidth(10);
        jTabMateriais.getColumnModel().getColumn(1).setMinWidth(20);
        jTabMateriais.getColumnModel().getColumn(1).setPreferredWidth(10);
        jTabMateriais.getColumnModel().getColumn(2).setMinWidth(100);
        jTabMateriais.getColumnModel().getColumn(2).setPreferredWidth(100);
        jTabMateriais.getColumnModel().getColumn(3).setMinWidth(10);
        jTabMateriais.getColumnModel().getColumn(3).setPreferredWidth(10);
        jTabMateriais.getColumnModel().getColumn(4).setMinWidth(20);
        jTabMateriais.getColumnModel().getColumn(4).setPreferredWidth(20);
        jTabMateriais.getColumnModel().getColumn(5).setMinWidth(20);
        jTabMateriais.getColumnModel().getColumn(5).setPreferredWidth(20);
        if (indexAtualEquipamentos < 0) {
            indexAtualEquipamentos = 0;
            cdEquipamento = String.format("%s", jTabEquipamentos.getValueAt(indexAtualEquipamentos, 0));
        }
    }

    // método para dar zoom no campo Produto Vinculado
    private void zoomMateriais() {
        PesquisarMateriais zoom = new PesquisarMateriais(new JFrame(), true, "P", "S",conexao,false);
        zoom.setVisible(true);
        jTexCdProdutoVinculado.setText(zoom.getCdMaterial().trim());
        jTexNomeProdutoVinculado.setText(zoom.getNomeMaterial().trim());
    }

    // método para dar zoom no campo Unidade de Medida
    private void zoomUnidMedida() {
        PesquisarUnidadesMedida zoom = new PesquisarUnidadesMedida(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdUnidMedida.setText(zoom.getSelec1().trim());
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
        jButImprimir = new javax.swing.JButton();
        jButSair = new javax.swing.JButton();
        jPanPrincipal = new javax.swing.JPanel();
        jPanGeral = new javax.swing.JPanel();
        jLabCdServico = new javax.swing.JLabel();
        jForCdServico = new javax.swing.JFormattedTextField();
        jLabNomeServico = new javax.swing.JLabel();
        jTexNomeServico = new javax.swing.JTextField();
        jLabSituacao = new javax.swing.JLabel();
        jComSituacao = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
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
        jPanTarefas = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabTarefas = new javax.swing.JTable();
        jPanEquipamentos = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabEquipamentos = new javax.swing.JTable();
        jLabValorTotalAtividades = new javax.swing.JLabel();
        jForValorTotalAtividades = new FormatarValor((FormatarValor.NUMERO));
        jLabCdProdutoVinculado = new javax.swing.JLabel();
        jTexCdProdutoVinculado = new javax.swing.JTextField();
        jTexNomeProdutoVinculado = new javax.swing.JTextField();
        jLabUnidMedida = new javax.swing.JLabel();
        jTexCdUnidMedida = new javax.swing.JTextField();
        jLabValorServico = new javax.swing.JLabel();
        jForValorServico = new FormatarValor((FormatarValor.NUMERO));
        jPanMateriais = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTabMateriais = new javax.swing.JTable();
        jPanAtividades = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTabAtividades = new javax.swing.JTable();
        jButAtividades = new javax.swing.JButton();
        jButMaterial = new javax.swing.JButton();
        jLabValorTotalMateriais = new javax.swing.JLabel();
        jForValorTotalMateriais = new FormatarValor((FormatarValor.NUMERO));
        jScrollPane5 = new javax.swing.JScrollPane();
        jTexAreaDescComercial = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuNovo = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Serviços");

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
        jButEditar.setEnabled(false);
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
        jButExcluir.setEnabled(false);
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
        jButProximo.setText("Proximo");
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

        jPanPrincipal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanGeral.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanGeral.setToolTipText("");

        jLabCdServico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdServico.setText("Código:");

        jForCdServico.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("########"))));

        jLabNomeServico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNomeServico.setText("Descrição:");

        jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabSituacao.setText("Situação:");

        jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ativo", "Inativo", "Pendente", "Obsoleto" }));

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

            jPanTarefas.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Sequência Tarefa:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jTabTarefas.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabTarefas.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null}
                },
                new String [] {
                    "Seq.", "Descrição", "Especialidade", "Valor", "Situação"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jTabTarefas.setColumnSelectionAllowed(true);
            jScrollPane1.setViewportView(jTabTarefas);
            jTabTarefas.getColumnModel().getSelectionModel().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
            if (jTabTarefas.getColumnModel().getColumnCount() > 0) {
                jTabTarefas.getColumnModel().getColumn(0).setResizable(false);
                jTabTarefas.getColumnModel().getColumn(0).setPreferredWidth(30);
                jTabTarefas.getColumnModel().getColumn(1).setResizable(false);
                jTabTarefas.getColumnModel().getColumn(1).setPreferredWidth(190);
                jTabTarefas.getColumnModel().getColumn(2).setResizable(false);
                jTabTarefas.getColumnModel().getColumn(2).setPreferredWidth(130);
                jTabTarefas.getColumnModel().getColumn(3).setResizable(false);
                jTabTarefas.getColumnModel().getColumn(3).setPreferredWidth(20);
                jTabTarefas.getColumnModel().getColumn(4).setResizable(false);
                jTabTarefas.getColumnModel().getColumn(4).setPreferredWidth(50);
            }

            javax.swing.GroupLayout jPanTarefasLayout = new javax.swing.GroupLayout(jPanTarefas);
            jPanTarefas.setLayout(jPanTarefasLayout);
            jPanTarefasLayout.setHorizontalGroup(
                jPanTarefasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanTarefasLayout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 550, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );
            jPanTarefasLayout.setVerticalGroup(
                jPanTarefasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTarefasLayout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 1, Short.MAX_VALUE))
            );

            jPanEquipamentos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Equipamentos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jTabEquipamentos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabEquipamentos.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null}
                },
                new String [] {
                    "Seq.", "Descrição", "Valor", "Situação"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jScrollPane2.setViewportView(jTabEquipamentos);
            if (jTabEquipamentos.getColumnModel().getColumnCount() > 0) {
                jTabEquipamentos.getColumnModel().getColumn(0).setResizable(false);
                jTabEquipamentos.getColumnModel().getColumn(0).setPreferredWidth(30);
                jTabEquipamentos.getColumnModel().getColumn(1).setResizable(false);
                jTabEquipamentos.getColumnModel().getColumn(1).setPreferredWidth(200);
                jTabEquipamentos.getColumnModel().getColumn(2).setResizable(false);
                jTabEquipamentos.getColumnModel().getColumn(2).setPreferredWidth(60);
                jTabEquipamentos.getColumnModel().getColumn(3).setResizable(false);
                jTabEquipamentos.getColumnModel().getColumn(3).setPreferredWidth(60);
            }

            javax.swing.GroupLayout jPanEquipamentosLayout = new javax.swing.GroupLayout(jPanEquipamentos);
            jPanEquipamentos.setLayout(jPanEquipamentosLayout);
            jPanEquipamentosLayout.setHorizontalGroup(
                jPanEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 495, Short.MAX_VALUE)
            );
            jPanEquipamentosLayout.setVerticalGroup(
                jPanEquipamentosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
            );

            jLabValorTotalAtividades.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorTotalAtividades.setText("Valor Total de Atividades:");

            jForValorTotalAtividades.setEditable(false);
            jForValorTotalAtividades.setEnabled(false);

            jLabCdProdutoVinculado.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdProdutoVinculado.setText("Produto Vinculado:");

            jTexCdProdutoVinculado.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCdProdutoVinculadoKeyPressed(evt);
                }
            });

            jLabUnidMedida.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabUnidMedida.setText("U.M:");

            jTexCdUnidMedida.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCdUnidMedidaKeyPressed(evt);
                }
            });

            jLabValorServico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorServico.setText("Valor do  Serviço:");

            jForValorServico.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

            jPanMateriais.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Relação Material:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jTabMateriais.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabMateriais.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null},
                    {null, null, null, null}
                },
                new String [] {
                    "Cód.", "Descrição", "U.M", "Val.Uni"
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
            jScrollPane3.setViewportView(jTabMateriais);
            if (jTabMateriais.getColumnModel().getColumnCount() > 0) {
                jTabMateriais.getColumnModel().getColumn(0).setResizable(false);
                jTabMateriais.getColumnModel().getColumn(0).setPreferredWidth(20);
                jTabMateriais.getColumnModel().getColumn(1).setResizable(false);
                jTabMateriais.getColumnModel().getColumn(1).setPreferredWidth(100);
                jTabMateriais.getColumnModel().getColumn(2).setResizable(false);
                jTabMateriais.getColumnModel().getColumn(2).setPreferredWidth(10);
                jTabMateriais.getColumnModel().getColumn(3).setResizable(false);
                jTabMateriais.getColumnModel().getColumn(3).setPreferredWidth(20);
            }

            javax.swing.GroupLayout jPanMateriaisLayout = new javax.swing.GroupLayout(jPanMateriais);
            jPanMateriais.setLayout(jPanMateriaisLayout);
            jPanMateriaisLayout.setHorizontalGroup(
                jPanMateriaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanMateriaisLayout.createSequentialGroup()
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanMateriaisLayout.setVerticalGroup(
                jPanMateriaisLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
            );

            jPanAtividades.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Atividades", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jTabAtividades.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabAtividades.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null},
                    {null, null, null, null, null}
                },
                new String [] {
                    "Seq.", "Cód.", "Descrição", "Valor", "Situação"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }
            });
            jScrollPane4.setViewportView(jTabAtividades);
            if (jTabAtividades.getColumnModel().getColumnCount() > 0) {
                jTabAtividades.getColumnModel().getColumn(0).setResizable(false);
                jTabAtividades.getColumnModel().getColumn(0).setPreferredWidth(10);
                jTabAtividades.getColumnModel().getColumn(1).setResizable(false);
                jTabAtividades.getColumnModel().getColumn(1).setPreferredWidth(20);
                jTabAtividades.getColumnModel().getColumn(2).setResizable(false);
                jTabAtividades.getColumnModel().getColumn(2).setPreferredWidth(190);
                jTabAtividades.getColumnModel().getColumn(3).setResizable(false);
                jTabAtividades.getColumnModel().getColumn(3).setPreferredWidth(20);
                jTabAtividades.getColumnModel().getColumn(4).setResizable(false);
                jTabAtividades.getColumnModel().getColumn(4).setPreferredWidth(40);
            }

            javax.swing.GroupLayout jPanAtividadesLayout = new javax.swing.GroupLayout(jPanAtividades);
            jPanAtividades.setLayout(jPanAtividadesLayout);
            jPanAtividadesLayout.setHorizontalGroup(
                jPanAtividadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanAtividadesLayout.createSequentialGroup()
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 1, Short.MAX_VALUE))
            );
            jPanAtividadesLayout.setVerticalGroup(
                jPanAtividadesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 153, Short.MAX_VALUE)
            );

            jButAtividades.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButAtividades.setText("Atividades");
            jButAtividades.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButAtividadesActionPerformed(evt);
                }
            });

            jButMaterial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButMaterial.setText("Material");
            jButMaterial.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButMaterialActionPerformed(evt);
                }
            });

            jLabValorTotalMateriais.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorTotalMateriais.setText("Valor Total de Materiais:");

            jForValorTotalMateriais.setEditable(false);
            jForValorTotalMateriais.setEnabled(false);

            jTexAreaDescComercial.setColumns(20);
            jTexAreaDescComercial.setLineWrap(true);
            jTexAreaDescComercial.setRows(20);
            jScrollPane5.setViewportView(jTexAreaDescComercial);

            jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel1.setText("Descrição Comercial:");

            javax.swing.GroupLayout jPanGeralLayout = new javax.swing.GroupLayout(jPanGeral);
            jPanGeral.setLayout(jPanGeralLayout);
            jPanGeralLayout.setHorizontalGroup(
                jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanGeralLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanRodape, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanGeralLayout.createSequentialGroup()
                            .addComponent(jPanAtividades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanEquipamentos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanGeralLayout.createSequentialGroup()
                            .addComponent(jPanTarefas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanMateriais, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanGeralLayout.createSequentialGroup()
                            .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabCdProdutoVinculado)
                                .addComponent(jLabCdServico))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jForCdServico, javax.swing.GroupLayout.DEFAULT_SIZE, 119, Short.MAX_VALUE)
                                .addComponent(jTexCdProdutoVinculado))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(jPanGeralLayout.createSequentialGroup()
                                    .addComponent(jLabNomeServico)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexNomeServico, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabSituacao)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanGeralLayout.createSequentialGroup()
                                    .addComponent(jTexNomeProdutoVinculado, javax.swing.GroupLayout.PREFERRED_SIZE, 287, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabUnidMedida)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexCdUnidMedida, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(37, 37, 37)
                                    .addComponent(jLabValorServico)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForValorServico, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanGeralLayout.createSequentialGroup()
                            .addComponent(jButAtividades, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButMaterial, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanGeralLayout.createSequentialGroup()
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jSeparator1))
                                .addGroup(jPanGeralLayout.createSequentialGroup()
                                    .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 504, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabValorTotalMateriais, javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabValorTotalAtividades, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jForValorTotalAtividades, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                                        .addComponent(jForValorTotalMateriais))))))
                    .addContainerGap())
            );
            jPanGeralLayout.setVerticalGroup(
                jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanGeralLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabCdServico)
                            .addComponent(jForCdServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabNomeServico)
                            .addComponent(jTexNomeServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabSituacao))
                        .addComponent(jComSituacao))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCdProdutoVinculado)
                        .addComponent(jTexCdProdutoVinculado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeProdutoVinculado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabUnidMedida)
                        .addComponent(jTexCdUnidMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabValorServico)
                        .addComponent(jForValorServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addGap(1, 1, 1)
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanGeralLayout.createSequentialGroup()
                            .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabValorTotalAtividades)
                                .addComponent(jForValorTotalAtividades, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButAtividades)
                                .addComponent(jButMaterial))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabValorTotalMateriais)
                                .addComponent(jForValorTotalMateriais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanGeralLayout.createSequentialGroup()
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanAtividades, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanEquipamentos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanTarefas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jPanMateriais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            javax.swing.GroupLayout jPanPrincipalLayout = new javax.swing.GroupLayout(jPanPrincipal);
            jPanPrincipal.setLayout(jPanPrincipalLayout);
            jPanPrincipalLayout.setHorizontalGroup(
                jPanPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanPrincipalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanGeral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanPrincipalLayout.setVerticalGroup(
                jPanPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanPrincipalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanGeral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jMenuBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jMenu1.setText("Arquivo");

            jMenuNovo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
            jMenuNovo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jMenuNovo.setText("Novo");
            jMenuNovo.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuNovoActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuNovo);

            jMenuItemSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemSalvar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jMenuItemSalvar.setText("Salvar");
            jMenuItemSalvar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemSalvarActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItemSalvar);

            jMenuItemSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemSair.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jMenuItemSair.setText("Sair");
            jMenuItemSair.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemSairActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItemSair);

            jMenuBar1.add(jMenu1);

            jMenu2.setText("Editar");
            jMenu2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenu2ActionPerformed(evt);
                }
            });

            jMenuItemEditar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemEditar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jMenuItemEditar.setText("Editar");
            jMenu2.add(jMenuItemEditar);

            jMenuBar1.add(jMenu2);

            setJMenuBar(jMenuBar1);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        // TODO add your handling code here:
        salvarRegistro();

    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:
        bloquearCampos();
        pesquisar();
    }//GEN-LAST:event_jButPesquisarActionPerformed

    private void jButProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButProximoActionPerformed
        // TODO add your handling code here:
        idxCorr += 1;
        esvaziarTabelas();
        upRegistros();
    }//GEN-LAST:event_jButProximoActionPerformed

    private void jButAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAnteriorActionPerformed
        // TODO add your handling code here:
        idxCorr -= 1;
        esvaziarTabelas();
        upRegistros();
    }//GEN-LAST:event_jButAnteriorActionPerformed

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
        // TODO add your handling code here:
        limparTela();
        esvaziarTabelas();
        oper = "N";
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        // TODO add your handling code here:
        liberarCampos();
        jTexNomeServico.requestFocus();
        oper = "A";

    }//GEN-LAST:event_jButEditarActionPerformed

    private void jButNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButNovoActionPerformed
        // TODO add your handling code here:
        novoRegistro();
        oper = "N";
    }//GEN-LAST:event_jButNovoActionPerformed

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jMenuNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNovoActionPerformed
        // TODO add your handling code here:
        jButNovoActionPerformed(evt);
    }//GEN-LAST:event_jMenuNovoActionPerformed

    private void jMenuItemSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalvarActionPerformed
        // TODO add your handling code here:
        jButSalvarActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemSalvarActionPerformed

    private void jMenuItemSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSairActionPerformed
        // TODO add your handling code here:
        jButSairActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemSairActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
        jButEditarActionPerformed(evt);
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        // TODO add your handling code here:
        if (!jForCdServico.getText().isEmpty()) {
            try {
                Servicos sv = new Servicos();
                sv.setCdServico(jForCdServico.getText());
                ServicosDAO svDAO = new ServicosDAO(conexao);
                svDAO.excluir(sv);
                limparTela();
                jButPesquisarActionPerformed(evt);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Não foi possível excluir o registro da tela");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral na exclusão do registro!");
            }
        }
    }//GEN-LAST:event_jButExcluirActionPerformed

    private void jTexCdProdutoVinculadoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdProdutoVinculadoKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomMateriais();
        }
    }//GEN-LAST:event_jTexCdProdutoVinculadoKeyPressed

    private void jTexCdUnidMedidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdUnidMedidaKeyPressed
        // TODO add your handling code here:
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomUnidMedida();
        }
    }//GEN-LAST:event_jTexCdUnidMedidaKeyPressed

    private void jButAtividadesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAtividadesActionPerformed
        // TODO add your handling code here:
        incluirAtividade();
    }//GEN-LAST:event_jButAtividadesActionPerformed

    private void jButMaterialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButMaterialActionPerformed
        // TODO add your handling code here:
        incluirMateriais();
    }//GEN-LAST:event_jButMaterialActionPerformed

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
            java.util.logging.Logger.getLogger(ManterServicos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterServicos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterServicos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterServicos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterServicos(su, conexao).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButAnterior;
    private javax.swing.JButton jButAtividades;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButEditar;
    private javax.swing.JButton jButExcluir;
    private javax.swing.JButton jButImprimir;
    private javax.swing.JButton jButMaterial;
    private javax.swing.JButton jButNovo;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JButton jButProximo;
    private javax.swing.JButton jButSair;
    private javax.swing.JButton jButSalvar;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JFormattedTextField jForCdServico;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JFormattedTextField jForValorServico;
    private javax.swing.JFormattedTextField jForValorTotalAtividades;
    private javax.swing.JFormattedTextField jForValorTotalMateriais;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabCdProdutoVinculado;
    private javax.swing.JLabel jLabCdServico;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabNomeServico;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabUnidMedida;
    private javax.swing.JLabel jLabValorServico;
    private javax.swing.JLabel jLabValorTotalAtividades;
    private javax.swing.JLabel jLabValorTotalMateriais;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JMenuItem jMenuNovo;
    private javax.swing.JPanel jPanAtividades;
    private javax.swing.JPanel jPanEquipamentos;
    private javax.swing.JPanel jPanGeral;
    private javax.swing.JPanel jPanMateriais;
    private javax.swing.JPanel jPanPrincipal;
    private javax.swing.JPanel jPanRodape;
    private javax.swing.JPanel jPanTarefas;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable jTabAtividades;
    private javax.swing.JTable jTabEquipamentos;
    private javax.swing.JTable jTabMateriais;
    private javax.swing.JTable jTabTarefas;
    private javax.swing.JTextArea jTexAreaDescComercial;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexCdProdutoVinculado;
    private javax.swing.JTextField jTexCdUnidMedida;
    private javax.swing.JTextField jTexNomeProdutoVinculado;
    private javax.swing.JTextField jTexNomeServico;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
