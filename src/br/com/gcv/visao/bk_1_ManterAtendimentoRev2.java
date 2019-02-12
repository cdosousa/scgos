/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa: GCVMO0020
 */
package br.com.gcv.visao;

// objetos do registro Pai
import br.com.gcv.modelo.Atendimento;
import br.com.gcv.dao.AtendimentoDAO;
import br.com.gcv.controle.CAtendimento;

// Objetos do Registro de Local de Atendimento
import br.com.gcv.modelo.LocalAtendimento;
import br.com.gcv.dao.LocalAtendimentoDAO;
import br.com.gcv.controle.CLocalAtendimento;

// objetos de Registro de Itens do Local de Atendimento
import br.com.gcv.modelo.ItemLocal;
import br.com.gcv.dao.ItemLocalDAO;
import br.com.gcv.controle.CItemLocal;

// Objetos para pesquisa de correlato
import br.com.modelo.EnderecoPostal;
import br.com.controle.CEnderecoPostal;
import br.com.gcs.visao.PesquisarEssenciaProdutos;
import br.com.gcs.visao.PesquisarMateriais;
import br.com.gcs.visao.PesquisarUnidadesMedida;
import br.com.gcv.controle.CContato;
import br.com.gcv.dao.ContatoDAO;
import br.com.gcv.modelo.Contato;
import br.com.gfc.visao.PesquisarTipoPagamento;
import br.com.visao.PesquisarMunicipios;
import br.com.visao.PesquisarUnidadeFederacao;

// Objetos de instância de parâmetros de ambiente
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;
import br.com.modelo.FormatarValor;
import br.com.modelo.HoraSistema;
import br.com.modelo.ParametrosGerais;

// Objetos de ambiente java
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 *
 * @author Cristiano de Oliveira Sousa create on 23/11/2017
 */
public class bk_1_ManterAtendimentoRev2 extends javax.swing.JDialog {

    // Variáveis de instancia de parâmetros de ambiente
    private final boolean ISBOTAO = true;
    private static Connection conexao;
    private static SessaoUsuario su;
    private static ParametrosGerais pg;
    private VerificarTecla vt;
    private GregorianCalendar gc;
    private NumberFormat formato;
    private NumberFormat ftq;
    private NumberFormat ftp;
    private NumberFormat ftv;

    // Variáveis de instância de objetos da tabela Atendimento da classe
    private Atendimento regCorAtend;
    private List< Atendimento> resultAtend;
    private Atendimento ate; // objeto específico para salvar o registro no banco
    private CAtendimento cate;
    private Atendimento modate; // objeto específico para carregar o registro na tela

    // Variáveis de instância de objetos da tabela localAtendimento da classe
    //private LocalAtendimento regCorLocal;
    //private List< LocalAtendimento> resultLocal;
    private CLocalAtendimento clat;
    private LocalAtendimento modlat;

    // Variáveis de Instância de objetos da Tabela itemLocal da classe
    private CItemLocal citl;
    private ItemLocalDAO itldao;
    private ItemLocal moditl;
    //private DefaultTableModel itens;

    // Variáveis de instância da objetos correlatos classe
    private CEnderecoPostal cep;
    private EnderecoPostal ep;
    private Contato modcon;
    private String cdMunicipioIbge = "";
    private String cdUfIbge;

    // Variáveis de instância da classe
    // == registro atendimento
    private int numRegAtend;
    private int idxCorAtend;
    private String sqlAtend;
    private String operAnted;
    //private double txJuros;

    // == registro local de antendimento
    private int numRegLocal;
    private int idxCorLocal;
    private int idxNovoLocal = 0;
    private String sqlLocal;
    private String operLocal;
    //private double oldValorAtendimento;
    private int sequenciaLocal;

    // == registro itens do local de atendimento
    private int numRegItens;
    private int idxCorItens;
    private String sqlitl;
    private String operItens;
    private int sequenciaItens;
    private int linhaItem = 0;
    private double descAlcada = 0.000;
    private double valorUnitBrt = 0.00;
    private double valorUnitLiq = 0.00;
    private double valorServico = 0.00;
    private boolean esvaziarTabela = false;
    private int numRegProp = 0;

    public bk_1_ManterAtendimentoRev2(java.awt.Frame parent, boolean modal, SessaoUsuario su, Connection conexao, ParametrosGerais pg) {
        super(parent, modal);
        this.su = su;
        this.conexao = conexao;
        this.pg = pg;
        formato = NumberFormat.getInstance(Locale.getDefault());
        operAnted = "N";
        operLocal = "N";
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setModal(false);
        dispose();
        setaItensLocal();
    }

    public bk_1_ManterAtendimentoRev2(java.awt.Frame parent, boolean modal, SessaoUsuario su, Connection conexao, ParametrosGerais pg, Contato modcon) {
        super(parent, modal);
        this.su = su;
        this.conexao = conexao;
        this.pg = pg;
        formato = NumberFormat.getInstance(Locale.getDefault());
        this.modcon = modcon;
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setModal(false);
        dispose();
        setaAtendimento();
        setaLocalAtendimento();
        setaItensLocal();
        formatarCampos();
        sqlAtend = "select * from gcvatendimento where cd_atendimento = '" + modcon.getCdAtendimento()
                + "'";
        sqlLocal = "select * from gcvlocalatendimento where cd_atendimento = '" + modcon.getCdAtendimento()
                + "'";
        pesquisarAtendimento();
    }

    // método para setar Atendimento
    private void setaAtendimento() {
        if (jTabbedPanAtendimento.getSelectedIndex() == 0) {
            jButSalvar.setEnabled(true);
        }
        monitoraJanela();
    }

    // método para setar LocalAtendimento
    private void setaLocalAtendimento() {
        modlat = new LocalAtendimento();
    }

    // método para setar itenslocal
    private void setaItensLocal() {
        moditl = new ItemLocal();
        monitoraLinhaItens();
    }

    // Método para formatar compos na tela
    private void formatarCampos() {
        // formatos de numeros
        ftq.getInstance();
        ftq = new DecimalFormat("###,###,##0.0000");
        ftp = ftq;
        ftv = ftq;
        ftq.setMaximumFractionDigits(3);
        ftp.setMaximumFractionDigits(3);
        ftv.setMaximumFractionDigits(2);

        // campos Proposta
        jForMetragemArea.setDocument(new DefineCampoDecimal());
        jForPercPerda.setDocument(new DefineCampoDecimal());
        jForMetragemRodape.setDocument(new DefineCampoDecimal());
        jForLargura.setDocument(new DefineCampoDecimal());
        jForEspessura.setDocument(new DefineCampoDecimal());
    }

    // Método para setar o atendimento com base no Contato
    // método para limpar tela Atendimento
    private void limparTelaAtendimento() {
        jForCdAtendimento.setText("");
        jForDataAtendimento.setText("");
        jForHoraAtendimento.setText("");
        jTexNomeRazaoSocial.setText("");
        jComTipoPessoa.setSelectedIndex(0);
        jForTelefone.setText("");
        jForCelular.setText("");
        jTexEmail.setText("");
        jComTipoLogradouro.setSelectedIndex(0);
        jTexLogradouro.setText("");
        jTexNumero.setText("");
        jTexComplemento.setText("");
        jTexBairro.setText("");
        cdMunicipioIbge = "";
        jTexSiglaUF.setText("");
        jForCep.setText("");
        cdUfIbge = "";
        jTexMuncipio.setText("");
        jForTotalMateriais.setText("0.00");
        jForTotalServico.setText("0.00");
        jForTotalAdicionais.setText("0.00");
        jForValorTotal.setText("0.00");
        jComSituacao.setSelectedIndex(0);
        jTexCadPorAtend.setText("");
        jForDataCadAtend.setText("");
        jTexModifPorAtend.setText("");
        jForDataModifiAtend.setText("");
        jTexRegAtualAtend.setText("");
        jTexRegTotalAtend.setText("");
        idxCorAtend = 0;
        numRegAtend = 0;
        resultAtend = null;
        regCorAtend = null;
        limparTelaLocal();
        liberarCamposAtend();
    }

    // Metodo para Limpar Tela Local
    private void limparTelaLocal() {
        jTexNomeLocal.setText("");
        jForMetragemArea.setText("0,000");
        jForPercPerda.setText("0,000");
        jComTipoPiso.setSelectedIndex(0);
        jComTipoRodape.setSelectedIndex(0);
        jForMetragemRodape.setText("0,000");
        jForLargura.setText("0,000");
        jTexComprimento.setText("0,000");
        jForEspessura.setText("0,000");
        jCheTingimento.setSelected(false);
        jCheClareamento.setSelected(false);
        jTexCdTipoVerniz.setText("");
        jTexNomeTipoVerniz.setText("");
        jTexEssenciaMadeira.setText("");
        jTexNomeEssenciaMadeira.setText("");
        jForValorServicoLocal.setText("0,00");
        jForValorMateriaisLocal.setText("0,00");
        jForValorAdicionaisLocal.setText("0,00");
        jForValorTotalLocal.setText("0,00");
        jTextAreaObsLocal.setText("");
    }

    // método para definir o tipo de pesquisa do atendimento
    private void pesquisarAtendimento() {
        cate = new CAtendimento(conexao, su);
        modate = new Atendimento();
        try {
            numRegAtend = cate.pesquisar(sqlAtend);
            idxCorAtend = +1;
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Argumento inválido na buscaErr: " + iae.getCause());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Pesquisar os RegistroErr: " + ex);
        }
        if (numRegAtend > 0) {
            upRegistrosAtendimento();
        } else { // senão encontrar atendimento cadastrado, inicia os campos da tela com as informações do contato
            operAnted = "N";
            DataSistema dat = new DataSistema();
            jForCdAtendimento.setText(modcon.getCdAtendimento());
            jForDataAtendimento.setText(dat.getDataConv(Date.valueOf(modcon.getDataAtendimento())));
            jForHoraAtendimento.setText(modcon.getHoraAtendimento());
            jTexNomeRazaoSocial.setText(modcon.getNomeRazaoSocial());
            jComTipoPessoa.setSelectedIndex(Integer.parseInt(modcon.getTipoPessoa()));
            jForTelefone.setText(modcon.getTelefone());
            jForCelular.setText(modcon.getCelular());
            jTexEmail.setText(modcon.getEmail());
            jTextAreaObsCliente.setText(modcon.getObs());
        }
    }

    // método para definir o tipo de pesquisa do Ambiente
    private void pesquisarLocal(boolean upTela) {
        clat = new CLocalAtendimento(conexao);
        modlat = new LocalAtendimento();
        try {
            numRegLocal = clat.pesquisar(sqlLocal);
            if (idxNovoLocal != numRegLocal) {
                idxCorLocal = +1;
            } else {
                idxCorLocal = idxNovoLocal;
            }

        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Argumento inválido na buscaErr: " + iae.getCause());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Pesquisar os RegistroErr: " + ex);
        }
        if (numRegLocal > 0) {
            sequenciaLocal = clat.getUltSequencia();
            upRegistrosLocal();
        } else {
            sequenciaLocal = 0;
        }
    }

    // método para pesquisar ItensLocal
    private void pesquisarItens() {
        sqlitl = "select *"
                + " from gcvitemlocal as i"
                + " left outer join gcsmaterial as m on i.cd_material = m.cd_material"
                + " where i.cd_atendimento = '" + modcon.getCdAtendimento()
                + "' and i.cd_local = '" + modlat.getCdLocal()
                + "' order by i.cd_atendimento, i.cd_local, i.sequencia";
        citl = new CItemLocal(conexao);
        //itens = new DefaultTableModel();
        try {
            numRegItens = citl.pesquisar(sqlitl);
            idxCorItens += 1;
        } catch (SQLException ex) {
            Logger.getLogger(bk_1_ManterAtendimentoRev2.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (numRegItens > 0) {
            sequenciaItens = numRegItens;
            jTabItensLocal.setModel(citl.carregarItens());
            jForValorMateriaisLocal.setText(String.valueOf(citl.getTotalMaterial()));
            jForValorServicoLocal.setText(String.valueOf(citl.getTotalServico()));
            jForValorTotalLocal.setText(String.valueOf(citl.getTotalMaterial() + citl.getTotalServico()));
            ajustarTabelaItem();
        } else {
            sequenciaItens = 0;
            if (esvaziarTabela) {
                esvaziarTabelaItem();
            }
            esvaziarTabela = false;
        }
    }

    // Método para atualizar os registros de atendimento
    private void upRegistrosAtendimento() {
        jTexRegTotalAtend.setText(Integer.toString(numRegAtend));
        jTexRegAtualAtend.setText(Integer.toString(idxCorAtend));
        cate.mostrarPesquisa(modate, idxCorAtend - 1);
        DataSistema dat = new DataSistema();
        HoraSistema hs = new HoraSistema();
        jForCdAtendimento.setText(modate.getCdAtendimento());
        jForDataAtendimento.setText(dat.getDataConv(Date.valueOf(modate.getDataAtendimento())));
        jForHoraAtendimento.setText(modate.getHoraAtendimento());
        jTexNomeRazaoSocial.setText(modate.getNomeRazaoSocial().trim().toUpperCase());
        jComTipoPessoa.setSelectedIndex(Integer.parseInt(modate.getTipoPessoa()));
        jForTelefone.setText(modate.getTelefone());
        jForCelular.setText(modate.getCelular());
        jTexEmail.setText(modate.getEmail());
        jComTipoLogradouro.setSelectedItem(modate.getTipoLogradouro());
        jTexLogradouro.setText(modate.getLogradouro());
        jTexNumero.setText(modate.getNumero());
        jTexComplemento.setText(modate.getComplemento());
        jTexBairro.setText(modate.getBairro());
        cdMunicipioIbge = modate.getCdMunicipioIbge();
        jTexMuncipio.setText(modate.getNomeMunicipio());
        jTexSiglaUF.setText(modate.getSiglaUf());
        cdUfIbge = String.valueOf(modate.getUfIbge());
        jForCep.setText(modate.getCdCep());
        jComTipoEndereco.setSelectedIndex(Integer.parseInt(modate.getTipoEndereco()));
        jTexCadPorAtend.setText(modate.getUsuarioCadastro());
        jForDataCadAtend.setText(dat.getDataConv(Date.valueOf(modate.getDataCadastro())));
        jForHoraCadAtend.setText(modate.getHoraCadastro());
        jTexModifPorAtend.setText(modate.getUsuarioModificacao());
        jForTotalMateriais.setText(String.valueOf(modate.getValorProdutos()));
        jForTotalServico.setText(String.valueOf(modate.getValorServico()));
        jForTotalAdicionais.setText(String.valueOf(modate.getValorAdicionais()));
        jForValorTotal.setText(String.valueOf(modate.getValorTotalBruto()));
        if (modate.getDataModificacao() != null) {
            jForDataModifiAtend.setText(dat.getDataConv(Date.valueOf(modate.getDataModificacao())));
            jForHoraAtendimento.setText(modate.getHoraModificacao());
        }
        jComSituacao.setSelectedIndex(Integer.parseInt(String.valueOf(modate.getSituacao())));

        // Habilitando/Desabilitando botões de navegação de registros
        if (numRegAtend > idxCorAtend) {
            jButProximo.setEnabled(true);
        } else {
            jButProximo.setEnabled(false);
        }
        if (idxCorAtend > 1) {
            jButAnterior.setEnabled(true);
        } else {
            jButAnterior.setEnabled(false);
        }
        pesquisarLocal(true);
    }

    //Método para Atualizar totais
    private void atualizarTotaisAtendimento(boolean upTela) {
        int numReg = 0;
        //cate = new CAtendimento(conexao);
        String sqlate = "select l.cd_atendimento as Atendimento,"
                + " sum(l.valor_produtos) as 'Total Produtos',"
                + " sum(l.valor_servicos) as 'Total Serviços',"
                + " sum(l.valor_adicionais) as Adicionais,"
                + " sum(l.valor_total) as 'Total Atendimento'"
                + " from gcvlocalatendimento as l"
                + " where l.cd_atendimento = '" + modcon.getCdAtendimento()
                + "'";
        try {
            numReg = cate.pesquisarTotais(sqlate);
        } catch (SQLException ex) {
            Logger.getLogger(bk_1_ManterAtendimentoRev2.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (numReg > 0) {
            cate.upTotalAtendiment(modate);
            /*
            JOptionPane.showMessageDialog(null, "Atualizado Totais:\nMateriais: " + modate.getValorProdutos()
                    + "\nServiço: " + modate.getValorServico() + "\nAdicionais: " + modate.getValorAdicionais()
                    + "\nTotal: " + modate.getValorTotalBruto());
             */
            jForTotalMateriais.setText(String.valueOf(modate.getValorProdutos()));
            jForTotalServico.setText(String.valueOf(modate.getValorServico()));
            jForTotalMateriais.setText(String.valueOf(modate.getValorProdutos()));
            jForTotalAdicionais.setText(String.valueOf(modate.getValorAdicionais()));
            jForValorTotal.setText(String.valueOf(modate.getValorTotalBruto()));
            operAnted = "A";
            //salvarAtendimento(upTela);
            operAnted = "";
        }
    }

    // Método para atualizar os registros de local
    private void upRegistrosLocal() {
        jTexRegTotalLocal.setText(Integer.toString(numRegLocal));
        jTexRegAtualLocal.setText(Integer.toString(idxCorLocal));
        clat.mostrarPesquisa(modlat, idxCorLocal - 1);
        jTexNomeLocal.setText(modlat.getNomeLocal());
        jForMetragemArea.setText(String.valueOf(modlat.getMetragemArea()));
        jForPercPerda.setText(String.valueOf(modlat.getPercPerda()));
        jComTipoPiso.setSelectedItem(modlat.getTipoPiso());
        jTexEssenciaMadeira.setText(modlat.getCdEssencia());
        jTexNomeEssenciaMadeira.setText(modlat.getNomeEssenciaMadeira());
        jComTipoRodape.setSelectedItem(modlat.getTipoRodape());
        jForMetragemRodape.setText(String.valueOf(modlat.getMetragemRodape()));
        jForLargura.setText(String.valueOf(modlat.getLargura()));
        jTexComprimento.setText(String.valueOf(modlat.getComprimento()));
        jForEspessura.setText(String.valueOf(modlat.getEspessura()));
        if ("S".equals(modlat.getTingimento())) {
            jCheTingimento.setSelected(true);
        } else {
            jCheTingimento.setSelected(false);
        }
        if ("S".equals(modlat.getClareamento())) {
            jCheClareamento.setSelected(true);
        } else {
            jCheClareamento.setSelected(false);
        }
        jTexCdTipoVerniz.setText(modlat.getCdTipolVerniz());
        jTexNomeTipoVerniz.setText(modlat.getNomeTipoVerniz());
        DataSistema dat = new DataSistema();
        jTexCadPorLocal.setText(modlat.getUsuarioCadastro());
        jForDataCadLocal.setText(dat.getDataConv(Date.valueOf(modlat.getDataCadastro())));
        jForHoraCadLocal.setText(modlat.getHoraCadastro());
        jTexModifPorLocal.setText(modlat.getUsuarioModificacao());
        if (modlat.getDataModificacao() != null) {
            jForDataModifLocal.setText(dat.getDataConv(Date.valueOf(modlat.getDataModificacao())));
            jForHoraModifLocal.setText(modlat.getHoraModificacao());
        }
        // Habilitando/Desabilitando botões de navegação de registros
        if (numRegLocal > idxCorLocal) {
            jButLocalProximo.setEnabled(true);
        } else {
            jButLocalProximo.setEnabled(false);
        }
        if (idxCorLocal > 1) {
            jButLocalAnterior.setEnabled(true);
        } else {
            jButLocalAnterior.setEnabled(false);
        }
        bloquearCamposLocal();
        pesquisarItens();
    }

    //Método para ajustar tabela itemlocal
    private void ajustarTabelaItem() {
        jTabItensLocal.getColumnModel().getColumn(0).setWidth(10);
        jTabItensLocal.getColumnModel().getColumn(0).setPreferredWidth(10);
        jTabItensLocal.getColumnModel().getColumn(1).setWidth(40);
        jTabItensLocal.getColumnModel().getColumn(1).setPreferredWidth(40);
        jTabItensLocal.getColumnModel().getColumn(2).setWidth(300);
        jTabItensLocal.getColumnModel().getColumn(2).setPreferredWidth(300);
        jTabItensLocal.getColumnModel().getColumn(3).setWidth(10);
        jTabItensLocal.getColumnModel().getColumn(3).setPreferredWidth(10);
        jTabItensLocal.getColumnModel().getColumn(4).setWidth(30);
        jTabItensLocal.getColumnModel().getColumn(4).setPreferredWidth(30);
        jTabItensLocal.getColumnModel().getColumn(5).setWidth(40);
        jTabItensLocal.getColumnModel().getColumn(5).setPreferredWidth(40);
        jTabItensLocal.getColumnModel().getColumn(6).setWidth(40);
        jTabItensLocal.getColumnModel().getColumn(6).setPreferredWidth(40);
    }

    //Método para criar um listener na tabela
    private void monitoraLinhaItens() {
        VerificarTecla vt = new VerificarTecla();
        jTabItensLocal.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                linhaItem = jTabItensLocal.getSelectedRow();
            }
        });

        // capturando a tecla digitada
        jTabItensLocal.addKeyListener(new KeyListener() {
            boolean inclusao;

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                formato.setMaximumFractionDigits(2);
                double qtde = 0;
                double percPerda = 0;
                try {
                    qtde = formato.parse(jForMetragemArea.getText()).doubleValue();
                    percPerda = 1 + (formato.parse(jForPercPerda.getText()).doubleValue()) / 100;
                } catch (ParseException ex) {
                    Logger.getLogger(bk_1_ManterAtendimentoRev2.class.getName()).log(Level.SEVERE, null, ex);
                }
                String tecla = vt.VerificarTecla(e).toUpperCase();
                if ("ABAIXO".equals(tecla) && !inclusao && "A".equals(operLocal)) {
                    if (jTabItensLocal.getSelectedRow() == jTabItensLocal.getRowCount() - 1) {
                        jButNovoLocal.setEnabled(!ISBOTAO);
                        jButLocalAnterior.setEnabled(!ISBOTAO);
                        jButLocalProximo.setEnabled(!ISBOTAO);
                        moditl = new ItemLocal();
                        inclusao = true;
                        operItens = "N";
                        jTabItensLocal.setModel(citl.adicionarLinha());
                        linhaItem = jTabItensLocal.getSelectedRow() + 1;
                        upValue();
                        ajustarTabelaItem();
                    }
                }

                if ("F5".equals(tecla) && inclusao) {
                    if (jTabItensLocal.getSelectedColumn() == 1) {
                        PesquisarMateriais zoom = new PesquisarMateriais(new JFrame(), true, "P", "R", conexao, true);
                        zoom.setVisible(true);
                        moditl.setSequencia(Integer.parseInt(String.format("%s", jTabItensLocal.getValueAt(linhaItem, 0))));
                        moditl.setCdMaterial(zoom.getCdMaterial());
                        moditl.setNomeMaterial(zoom.getNomeMaterial());
                        moditl.setCdUnidmedida(zoom.cdUnidMedida());
                        moditl.setQuantidade(qtde * percPerda);
                        if ("4".equals(zoom.getTipoProduto())) {
                            moditl.setValorUnitBruto(zoom.getValorUnitBruto());
                            moditl.setTipoItem("R");
                            if (zoom.getValorUnitBruto() == 0) {
                                JOptionPane.showMessageDialog(null, "Item sem preço cadastrado!");
                            }
                            moditl.setValorTotalItemBruto(moditl.getQuantidade() * moditl.getValorUnitBruto());
                            descAlcada = zoom.getDescAlcada();
                            valorUnitBrt = zoom.getValorUnitBruto();
                            valorUnitLiq = zoom.getValorUnitLiquido();
                        } else {
                            moditl.setValorUnitBruto(zoom.getValorServico());
                            moditl.setTipoItem("S");
                            if (zoom.getValorServico() == 0) {
                                JOptionPane.showMessageDialog(null, "Serviço sem preço cadastrado!");
                            }
                            moditl.setValorTotalItemBruto(moditl.getQuantidade() * moditl.getValorUnitBruto());
                            valorServico = zoom.getValorServico();
                        }
                        upValue();
                    }
                    if (jTabItensLocal.getSelectedColumn() == 3) {
                        PesquisarUnidadesMedida zoom = new PesquisarUnidadesMedida(new JFrame(), true, "P", conexao);
                        zoom.setVisible(inclusao);
                        moditl.setCdUnidmedida(zoom.getSelec1());
                        upValue();
                    }
                    ajustarTabelaItem();
                }
                if ("ESCAPE".equals(tecla) && inclusao) {
                    jTabItensLocal.setModel(citl.excluirLinha(jTabItensLocal.getSelectedRow()));
                    jTabItensLocal.requestFocus();
                    inclusao = false;
                    ajustarTabelaItem();
                }
                if ("ENTER".equals(tecla)) {
                    if (inclusao) {
                        inclusao = false;
                    } else {
                        operItens = "A";
                    }
                    preparaItem();
                    upValue();
                    salvarItem();
                    pesquisarItens();
                }
                if ("GUIA".equals(tecla)) {
                    dowValue();
                    upValue();
                }

                if ("ACIMA".equals(tecla)) {
                    if (inclusao) {
                        inclusao = false;
                        operItens = "N";
                    } else {
                        operItens = "A";
                    }
                    preparaItem();
                    salvarItem();
                    pesquisarItens();
                }

                if ("EXCLUIR".equals(tecla) && "A".equals(operLocal) && !inclusao) {
                    moditl = new ItemLocal();
                    moditl.setCdAtendimento(modcon.getCdAtendimento());
                    moditl.setCdLocal(modlat.getCdLocal());
                    moditl.setSequencia(Integer.parseInt(String.format("%s", jTabItensLocal.getValueAt(linhaItem, 0))));
                    try {
                        itldao = new ItemLocalDAO(conexao);
                        itldao.excluir(moditl);
                        if ("R".equals(moditl.getTipoItem())) {
                            modlat.setValorProdutos(modlat.getValorProdutos() - moditl.getValorTotalItemBruto());
                        } else {
                            modlat.setValorServico(modlat.getValorServico() - moditl.getValorTotalItemBruto());
                        }
                        modlat.setValorTotalBruto(modlat.getValorTotalBruto() - moditl.getValorTotalItemBruto());
                        esvaziarTabela = true;
                        pesquisarItens();
                        jForValorServicoLocal.setText(String.valueOf(modlat.getValorServico()));
                        jForValorMateriaisLocal.setText(String.valueOf(modlat.getValorProdutos()));
                        jForValorAdicionaisLocal.setText(String.valueOf(modlat.getValorAdicionais()));
                        jForValorTotalLocal.setText(String.valueOf(modlat.getValorTotalBruto()));
                    } catch (SQLException ex) {
                        Logger.getLogger(bk_1_ManterAtendimentoRev2.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }

            private void upValue() {
                jTabItensLocal.setModel(citl.upNovaLinha(moditl, linhaItem, 1, 2, 3, 4, 5, 6, "I"));
            }

            private void preparaItem() {
                citl.buscarMaterial(moditl.getCdMaterial());
                switch (citl.getCdTipoItem()) {
                    case "4":
                        moditl.setTipoItem("R");
                        break;
                    default:
                        moditl.setTipoItem("S");
                        break;
                }
                moditl.setValorTotalItemBruto(moditl.getQuantidade() * moditl.getValorUnitBruto());
            }

            private void dowValue() {
                moditl.setSequencia(Integer.parseInt(String.format("%s", jTabItensLocal.getValueAt(linhaItem, 0))));
                moditl.setCdMaterial(String.format("%s", jTabItensLocal.getValueAt(linhaItem, 1)));
                moditl.setNomeMaterial(String.format("%s", jTabItensLocal.getValueAt(linhaItem, 2)));
                moditl.setCdUnidmedida(String.format("%s", jTabItensLocal.getValueAt(linhaItem, 3)));
                try {
                    moditl.setQuantidade(ftq.parse(String.format("%s", jTabItensLocal.getValueAt(linhaItem, 4))).doubleValue());
                    //JOptionPane.showMessageDialog(null, "Quantidade: " + ftq.parse(String.format("%s", jTabItensLocal.getValueAt(linhaItem, 4))).doubleValue());
                    moditl.setValorUnitBruto(ftv.parse(String.format("%s", jTabItensLocal.getValueAt(linhaItem, 5))).doubleValue());
                    //JOptionPane.showMessageDialog(null, "Preço Unit.: " + ftv.parse(String.format("%s", jTabItensLocal.getValueAt(linhaItem, 5))).doubleValue());
                } catch (ParseException ex) {
                    Logger.getLogger(ManterPropostaComercialRev1.class.getName()).log(Level.SEVERE, null, ex);
                }
                moditl.setValorTotalItemBruto(moditl.getQuantidade() * moditl.getValorUnitBruto());
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

        });
    }

    // Metodo para criar um listener na janela
    private void monitoraJanela() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                if ("A".equals(operLocal)) {
                    if (JOptionPane.showConfirmDialog(null, "Confirma sair do Atendimento?") == JOptionPane.OK_OPTION) {
                        salvarLocal(false);
                        atualizarTotaisAtendimento(false);
                        salvarAtendimento(false);
                        dispose();
                    }
                } else {
                    dispose();
                }
            }
        });
    }

    //Bloquear os campos da tela Atendimento
    private void bloquearCamposAtend() {
        jForCdAtendimento.setEditable(false);
        jForDataAtendimento.setEditable(false);
        jForHoraAtendimento.setEditable(false);
        jTexNomeRazaoSocial.setEditable(false);
        jComTipoPessoa.setEditable(false);
        jForTelefone.setEditable(false);
        jForCelular.setEditable(false);
        jTexEmail.setEditable(false);
        jComTipoLogradouro.setEditable(false);
        jTexLogradouro.setEditable(false);
        jTexNumero.setEditable(false);
        jTexComplemento.setEditable(false);
        jTexBairro.setEditable(false);
        jTexMuncipio.setEditable(false);
        jTexSiglaUF.setEditable(false);
        jForCep.setEditable(false);
        jComTipoEndereco.setEditable(false);
        jComSituacao.setEditable(false);
        bloquearCamposLocal();
    }

    //Bloquear os campos da tela Local
    private void bloquearCamposLocal() {
        jTexNomeLocal.setEditable(false);
        jForMetragemArea.setEditable(false);
        jForPercPerda.setEditable(false);
        jComTipoPiso.setEditable(false);
        jTexEssenciaMadeira.setEditable(false);
        jComTipoRodape.setEditable(false);
        jForMetragemRodape.setEditable(false);
        jForLargura.setEditable(false);
        jTexComprimento.setEditable(false);
        jForEspessura.setEditable(false);
        jCheTingimento.setEnabled(false);
        jCheClareamento.setEnabled(false);
        jTexCdTipoVerniz.setEditable(false);
        jTextAreaObsLocal.setEditable(false);
    }

    //Liberar os campos da tela Atendimento para atualização
    private void liberarCamposAtend() {
        jTexNomeRazaoSocial.setEditable(true);
        jComTipoPessoa.setEditable(true);
        jForTelefone.setEditable(true);
        jForCelular.setEditable(true);
        jTexEmail.setEditable(true);
        jComTipoLogradouro.setEditable(true);
        jTexLogradouro.setEditable(true);
        jTexNumero.setEditable(true);
        jTexComplemento.setEditable(true);
        jTexBairro.setEditable(true);
        jTexMuncipio.setEditable(true);
        jTexSiglaUF.setEditable(true);
        jForCep.setEditable(true);
        jComTipoEndereco.setEditable(false);
        jComSituacao.setEditable(false);
        liberarCamposLocal();
    }

    //Liberar os campos da tela Local para atualização
    private void liberarCamposLocal() {
        jForMetragemArea.setEditable(true);
        jForPercPerda.setEditable(true);
        jComTipoPiso.setEditable(true);
        jTexEssenciaMadeira.setEditable(true);
        jComTipoRodape.setEditable(true);
        jForMetragemRodape.setEditable(true);
        jForLargura.setEditable(true);
        jTexComprimento.setEditable(true);
        jForEspessura.setEditable(true);
        jCheTingimento.setEnabled(true);
        jCheClareamento.setEnabled(true);
        jTexCdTipoVerniz.setEditable(true);
        jTextAreaObsLocal.setEditable(true);
    }

    // metodo para dar zoon no campo UF
    private void zoomUF() {
        PesquisarUnidadeFederacao zoom = new PesquisarUnidadeFederacao(new JFrame(), true, conexao, "P");
        zoom.setVisible(true);
        jTexSiglaUF.setText(zoom.getSelec1().trim());
        cdUfIbge = zoom.getSelec3().trim();
    }

    // metodo para dar zoon no campo Município
    private void zoomMunicipio() {
        String sql = "SELECT MU.CD_MUNICIPIO_IBGE AS Cod_Ibge,"
                + "MU.CD_MUNICIPIO AS Cod_Mun,"
                + "MU.NOME_MUNICIPIO AS Município,"
                + "MU.CD_UF_IBGE AS UF,"
                + "MU.DATA_CADASTRO AS Cadastro,"
                + "MU.DATA_MODIFICACAO AS Modificao,"
                + "MU.SITUACAO AS Sit"
                + " FROM PGSMUNICIPIO AS MU"
                + " LEFT JOIN PGSUF AS UF ON MU.CD_UF_IBGE = UF.CD_UF_IBGE"
                + " WHERE UF.CD_UF_IBGE = '"
                + cdUfIbge.toUpperCase().trim()
                + "' ORDER BY MU.NOME_MUNICIPIO";
        PesquisarMunicipios zoom = new PesquisarMunicipios(new JFrame(), true, "P", sql, conexao);
        zoom.setVisible(true);
        cdMunicipioIbge = zoom.getSelec1().trim();
        jTexMuncipio.setText(zoom.getSelec3().trim());
    }

    // metodo para buscar CEP
    private void buscarCep() throws IOException {
        cep = new CEnderecoPostal();
        ep = new EnderecoPostal();
        if (!jForCep.getText().trim().replace("-", "").isEmpty()) {
            cep.pesquisar(conexao, jForCep.getText().replace("-", ""), su.getCharSet());
            upEndereco();
        } else if (jTexSiglaUF.getText().isEmpty() || jTexLogradouro.getText().isEmpty() || jTexMuncipio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Você deve informar o CEP para busca, ou, na falta dele, informe UF, Cidade e Logradouro para buscar o CEP");
        } else {
            cep.pesquisar(conexao, jTexSiglaUF.getText().toUpperCase().trim() + "/" + jTexMuncipio.getText().toUpperCase().trim() + "/" + jTexLogradouro.getText().toUpperCase().trim(), su.getCharSet());
            upEndereco();
        }
    }

    private void upEndereco() {
        cep.mostrarPesquisa(ep);
        jComTipoLogradouro.setSelectedItem(ep.getTipoLogradouro());
        jTexLogradouro.setText(ep.getLogradouro());
        jTexComplemento.setText(ep.getComplemento());
        jTexBairro.setText(ep.getBairro());
        jTexSiglaUF.setText(ep.getSiglaUf());
        cdUfIbge = String.valueOf(ep.getUfIbge());
        cdMunicipioIbge = ep.getCdIbge();
        jTexMuncipio.setText(ep.getMunicipioLocalidade());
        jForCep.setText(ep.getCep());
    }

    // metodo para dar zoom no campo Tipo Verniz
    private void zoomTipoVerniz() {
        PesquisarTipoVerniz zoom = new PesquisarTipoVerniz(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdTipoVerniz.setText(zoom.getCdTipoVerniz());
        jTexNomeTipoVerniz.setText(zoom.getNomeTipoVerniz());
    }

    // metodo para dar zoon no Campo Essencia
    private void zoomEssencia() {
        PesquisarEssenciaProdutos zoom = new PesquisarEssenciaProdutos(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexEssenciaMadeira.setText(zoom.getSelec1().trim());
        jTexNomeEssenciaMadeira.setText(zoom.getSelec2());
    }

    // método para dar zoom no campo TipoPagamento
    private void zoomTipoPagamento() {
        PesquisarTipoPagamento zoom = new PesquisarTipoPagamento(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        //    txJuros = zoom.getTxJuros();
    }

    // metodo para salvar registro atendimento
    private void salvarAtendimento(boolean upTela) {
        ate = new Atendimento();
        if (jTexNomeRazaoSocial.getText().isEmpty() || jComSituacao.getSelectedItem().toString().substring(0, 1) == " ") {
            JOptionPane.showMessageDialog(null, "Os campo Nome/Razão Social, Tipo de Logradouro, Tip de Endereço"
                    + " e \nSituacao precisam ser preenchidos corretamente!");
        } else {
            DataSistema dat = new DataSistema();
            String data = null;
            ate.setCdAtendimento(jForCdAtendimento.getText());
            ate.setDataAtendimento(dat.getDataConv(jForDataAtendimento.getText()));
            ate.setHoraAtendimento(jForHoraAtendimento.getText());
            ate.setNomeRazaoSocial(jTexNomeRazaoSocial.getText().trim().toUpperCase());
            ate.setTipoPessoa(jComTipoPessoa.getSelectedItem().toString().substring(0, 1));
            ate.setTelefone(jForTelefone.getText());
            ate.setCelular(jForCelular.getText());
            ate.setEmail(jTexEmail.getText().trim());
            ate.setTipoLogradouro(jComTipoLogradouro.getSelectedItem().toString().trim());
            ate.setLogradouro(jTexLogradouro.getText().trim().toUpperCase());
            ate.setNumero(jTexNumero.getText().trim().toUpperCase());
            ate.setComplemento(jTexComplemento.getText().trim().toUpperCase());
            ate.setBairro(jTexBairro.getText().trim().toUpperCase());
            ate.setCdMunicipioIbge(cdMunicipioIbge.trim());
            if (!jTexSiglaUF.getText().isEmpty()) {
                ate.setSiglaUf(jTexSiglaUF.getText().toUpperCase().toString().substring(jTexSiglaUF.getText().trim().length() - 2, 2));
            }
            ate.setCdCep(jForCep.getText().toString().trim());
            ate.setTipoEndereco(jComTipoEndereco.getSelectedItem().toString().substring(0, 1));
            ate.setValorServico(modate.getValorServico());
            ate.setValorProdutos(modate.getValorProdutos());
            ate.setValorAdicionais(modate.getValorAdicionais());
            ate.setValorTotalBruto(modate.getValorTotalBruto());
            ate.setCdProposta(modate.getCdProposta());
            ate.setUsuarioCadastro(su.getUsuarioConectado());
            dat.setData(data);
            data = dat.getData();
            HoraSistema hs = new HoraSistema();
            ate.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 2));
            AtendimentoDAO atedao = null;
            sqlAtend = "SELECT * FROM GCVATENDIMENTO WHERE CD_ATENDIMENTO = '" + ate.getCdAtendimento()
                    + "'";
            try {
                atedao = new AtendimentoDAO(conexao);
                if ("N".equals(operAnted)) {
                    ate.setDataCadastro(data);
                    ate.setHoraCadastro(hs.getHora());
                    atedao.adicionar(ate);
                } else {
                    ate.setUsuarioModificacao(su.getUsuarioConectado());
                    ate.setDataModificacao(data);
                    ate.setHoraModificacao(hs.getHora());
                    atedao.atualizar(ate);
                }
                modcon.setSituacao("PG");
                modcon.setAtualizacao(true);
            } catch (SQLException ex) {
                Logger.getLogger(bk_1_ManterAtendimentoRev2.class.getName()).log(Level.SEVERE, null, ex);
            }
            operAnted = "";
            if (upTela) {
                pesquisarAtendimento();
            }
            controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
        }
    }

    // Método para salvar registro Local
    private void salvarLocal(boolean upTela) {
        DataSistema dat = new DataSistema();
        LocalAtendimento lat = modlat;
        String data = null;
        lat.setNomeLocal(jTexNomeLocal.getText().trim().toUpperCase());
        try {
            lat.setMetragemArea(formato.parse(jForMetragemArea.getText()).doubleValue());
            lat.setPercPerda(formato.parse(jForPercPerda.getText()).doubleValue());
            lat.setMetragemRodape(formato.parse(jForMetragemRodape.getText()).doubleValue());
            lat.setLargura(formato.parse(jForLargura.getText()).doubleValue());
            lat.setComprimento(jTexComprimento.getText());
            lat.setEspessura(formato.parse(jForEspessura.getText()).doubleValue());
            lat.setValorServico(formato.parse(jForValorServicoLocal.getText()).doubleValue());
            lat.setValorProdutos(formato.parse(jForValorMateriaisLocal.getText()).doubleValue());
            lat.setValorAdicionais(formato.parse(jForValorAdicionaisLocal.getText()).doubleValue());
            lat.setValorTotalBruto(formato.parse(jForValorTotalLocal.getText()).doubleValue());
        } catch (ParseException ex) {
            Logger.getLogger(bk_1_ManterAtendimentoRev2.class.getName()).log(Level.SEVERE, null, ex);
        }
        lat.setTipoPiso(jComTipoPiso.getSelectedItem().toString());
        lat.setTipoRodape(jComTipoRodape.getSelectedItem().toString());
        if (jCheTingimento.isSelected()) {
            lat.setTingimento("S");
        } else {
            lat.setTingimento("N");
        }
        if (jCheClareamento.isSelected()) {
            lat.setClareamento("S");
        } else {
            lat.setClareamento("N");
        }
        lat.setCdTipoVerniz(jTexCdTipoVerniz.getText());
        lat.setCdEssencia(jTexEssenciaMadeira.getText());
        lat.setObs(jTextAreaObsLocal.getText());
        dat.setData(data);
        data = dat.getData();
        lat.setSituacao("AA");
        LocalAtendimentoDAO latdao = null;
        sqlLocal = "SELECT * FROM GCVLOCALATENDIMENTO WHERE CD_ATENDIMENTO = '" + jForCdAtendimento.getText()
                + "'";
        try {
            HoraSistema hs = new HoraSistema();
            latdao = new LocalAtendimentoDAO(conexao);
            if ("N".equals(operLocal)) {
                lat.setCdAtendimento(jForCdAtendimento.getText());
                lat.setCdLocal(sequenciaLocal + 1);
                lat.setUsuarioCadastro(su.getUsuarioConectado());
                lat.setDataCadastro(data);
                lat.setHoraCadastro(hs.getHora());
                latdao.adicionar(lat);
            } else {
                lat.setCdAtendimento(jForCdAtendimento.getText());
                lat.setCdLocal(modlat.getCdLocal());
                lat.setUsuarioModificacao(su.getUsuarioConectado());
                lat.setDataModificacao(data);
                lat.setHoraModificacao(hs.getHora());
                latdao.atualizar(lat);
            }
            atualizarTotaisAtendimento(upTela);
            operLocal = "";
            esvaziarTabela = false;
            controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
            liberarCamposLocal();
            pesquisarLocal(true);
        } catch (SQLException ex) {
            Logger.getLogger(bk_1_ManterAtendimentoRev2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para salvar Itemlocal
    private void salvarItem() {
        if (moditl.getSequencia() != 0 && moditl.getCdMaterial() != null) {
            DataSistema dat = new DataSistema();
            ItemLocal itl = new ItemLocal();
            String data = null;
            itl.setCdAtendimento(modcon.getCdAtendimento());
            itl.setCdLocal(modlat.getCdLocal());
            itl.setSequencia(moditl.getSequencia());
            itl.setCdMaterial(moditl.getCdMaterial());
            itl.setCdUnidmedida(moditl.getCdUnidmedida());
            itl.setQuantidade(moditl.getQuantidade());
            itl.setValorUnitBruto(moditl.getValorUnitBruto());
            itl.setValorTotalItemBruto(moditl.getValorTotalItemBruto());
            itl.setTipoItem(moditl.getTipoItem());
            itl.setObsItem(moditl.getObsItem());
            dat.setData(data);
            data = dat.getData();
            itl.setSituacao("AA");
            ItemLocalDAO itldao = null;
            try {
                HoraSistema hs = new HoraSistema();
                itldao = new ItemLocalDAO(conexao);
                if ("N".equals(operItens)) {
                    itl.setUsuarioCadastro(su.getUsuarioConectado());
                    itl.setDataCadastro(data);
                    itl.setHoraCadastro(hs.getHora());
                    itldao.adicionar(itl);
                    if ("R".equals(moditl.getTipoItem())) {
                        modlat.setValorProdutos(modlat.getValorProdutos() + moditl.getValorTotalItemBruto());
                        //modate.setValorProdutos(modate.getValorProdutos() + moditl.getValorTotalItemBruto());
                    } else if ("S".equals(moditl.getTipoItem())) {
                        modlat.setValorServico(modlat.getValorServico() + moditl.getValorTotalItemBruto());
                        //modate.setValorServico(modate.getValorServico() + moditl.getValorTotalItemBruto());
                    }
                    modlat.setValorTotalBruto(modlat.getValorProdutos() + modlat.getValorServico());
                    //modate.setValorTotalBruto(modate.getValorProdutos() + modate.getValorServico());
                } else {
                    itl.setUsuarioModificacao(su.getUsuarioConectado());
                    itl.setDataModificacao(data);
                    itl.setHoraModificacao(hs.getHora());
                    itldao.atualizar(itl);
                }
                ate = modate;
                operAnted = "A";
                jForValorMateriaisLocal.setText(String.valueOf(modlat.getValorProdutos()));
                jForValorServicoLocal.setText(String.valueOf(modlat.getValorServico()));
                jForValorAdicionaisLocal.setText(String.valueOf(modlat.getValorAdicionais()));
                jForValorTotalLocal.setText(String.valueOf(modlat.getValorTotalBruto()));
                operItens = "";
            } catch (SQLException ex) {
                Logger.getLogger(bk_1_ManterAtendimentoRev2.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    // Método para Excluir Atendimento
    private void excluirAtend() {
        if (!jForCdAtendimento.getText().isEmpty()) {
            try {
                Atendimento cc = new Atendimento();
                cc.setCdAtendimento(jForCdAtendimento.getText());
                AtendimentoDAO ccDAO = new AtendimentoDAO(conexao);
                ccDAO.excluir(cc);
                limparTelaAtendimento();
                pesquisarAtendimento();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Não foi possível excluir o registro da tela");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral na exclusão do registro!");
            }
        }
    }

    // Método para Excluir Ambiente
    private void excluirLocal() {
        if (!jTexNomeLocal.getText().trim().isEmpty()) {
            try {
                LocalAtendimento cc = new LocalAtendimento();
                cc.setCdAtendimento(jForCdAtendimento.getText());
                cc.setCdLocal(modlat.getCdLocal());
                LocalAtendimentoDAO ccDAO = new LocalAtendimentoDAO(conexao);
                ccDAO.excluir(cc);
                limparTelaLocal();
                pesquisarLocal(true);
                atualizarTotaisAtendimento(true);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Não foi possível excluir o registro da tela");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral na exclusão do registro!");
            }
        }
    }

    // Método para calcular pagamento
    private void calcularPagamento() {
        try {
            double parcelas = formato.parse(jForNumParcelas.getText()).doubleValue();
            double totalAtendimento = formato.parse(jForValorTotal.getText()).doubleValue();
            double desconto = formato.parse(jForDesc.getText()).doubleValue();
            double totalComDesconto = (totalAtendimento - (totalAtendimento / 100 * desconto));
            jForValorParcelas.setText(String.valueOf(totalComDesconto / parcelas));
        } catch (ParseException ex) {
            Logger.getLogger(bk_1_ManterAtendimentoRev2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    // méto para limpara a tabela de itens
    private void esvaziarTabelaItem() {
        citl = new CItemLocal(conexao);
        jTabItensLocal.setModel(new JTable().getModel());
        jTabItensLocal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabItensLocal.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null, null, null, null, null}
                },
                new String[]{
                    "Seq.", "Cod.", "Descrição", "U.M", "Qtde", "Pr. Unit.", "Tot. Item"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
    }

    // Metodo para controlar os botoes
    private void controleBotoes(boolean bEd, boolean bSa, boolean bCa, boolean bEx, boolean bNo, boolean bCl) {
        jButEditar.setEnabled(bEd);
        jButSalvar.setEnabled(bSa);
        jButCancelar.setEnabled(bCa);
        jButExcluir.setEnabled(bEx);
        jButNovoLocal.setEnabled(bNo);
        jButSair.setEnabled(bCl);
    }

    // Método para Gerar Proposta
    private void propostaComercial() {
        //new ManterPropostaComercialRev1(su, conexao, pg, modate, modlat, moditl).setVisible(true);
        numRegProp = cate.buscarProposta();
        if (numRegProp > 0) {
            new ManterPropostaComercialRev1(su, conexao, pg, modate).setVisible(true);
        } else if (JOptionPane.showConfirmDialog(null, "Ainda não foi gerada Proposta Comercial para este atendimento!"
                + "\nDeseja gerar uma Proposta Agora?") == JOptionPane.OK_OPTION) {
            try {
                modate.setCdProposta(cate.criarProposta(modate));
                jComSituacao.setSelectedIndex(2);
                //modate.setCdProposta(modcon.getCdProposta());
                operAnted = "A";
                modcon.setCdProposta(modate.getCdProposta());
                modcon.setSituacao("AA");
                modcon.setAtualizacao(true);
                salvarAtendimento(true);
                ContatoDAO condao = new ContatoDAO(conexao);
                condao.atualizar(modcon);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            new ManterPropostaComercialRev1(su, conexao, pg, modate).setVisible(true);
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

        jTooMenuFerramentas = new javax.swing.JToolBar();
        jButNovo = new javax.swing.JButton();
        jButEditar = new javax.swing.JButton();
        jButSalvar = new javax.swing.JButton();
        jButCancelar = new javax.swing.JButton();
        jButExcluir = new javax.swing.JButton();
        jButPesquisar = new javax.swing.JButton();
        jButAnterior = new javax.swing.JButton();
        jButProximo = new javax.swing.JButton();
        jButSair = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jForCdAtendimento = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        jForDataAtendimento = new javax.swing.JFormattedTextField();
        jLabHoraAtendimento = new javax.swing.JLabel();
        jForHoraAtendimento = new javax.swing.JFormattedTextField();
        jLabSituacao = new javax.swing.JLabel();
        jComSituacao = new javax.swing.JComboBox<>();
        jTabbedPanAtendimento = new javax.swing.JTabbedPane();
        jPanCliente = new javax.swing.JPanel();
        jTexNomeRazaoSocial = new javax.swing.JTextField();
        jLabTipoPessoa = new javax.swing.JLabel();
        jComTipoPessoa = new javax.swing.JComboBox<>();
        jLabTelefone = new javax.swing.JLabel();
        jForTelefone = new javax.swing.JFormattedTextField();
        jLabCelular = new javax.swing.JLabel();
        jLabEmail = new javax.swing.JLabel();
        jForCelular = new javax.swing.JFormattedTextField();
        jTexEmail = new javax.swing.JTextField();
        jLabNomeRazaoSocial = new javax.swing.JLabel();
        jPanEndereco = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComTipoLogradouro = new javax.swing.JComboBox<>();
        jTexLogradouro = new javax.swing.JTextField();
        jLabNumero = new javax.swing.JLabel();
        jTexNumero = new javax.swing.JTextField();
        jLabComplemento = new javax.swing.JLabel();
        jTexComplemento = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTexBairro = new javax.swing.JTextField();
        jLabCidade = new javax.swing.JLabel();
        jLabMunicipio = new javax.swing.JLabel();
        jTexMuncipio = new javax.swing.JTextField();
        jTexSiglaUF = new javax.swing.JTextField();
        jLabCep = new javax.swing.JLabel();
        jForCep = new javax.swing.JFormattedTextField();
        jButValidarCep = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        jComTipoEndereco = new javax.swing.JComboBox<>();
        jPanTotalAtendimento = new javax.swing.JPanel();
        jLabTotalServico = new javax.swing.JLabel();
        jForTotalServico = new FormatarValor(FormatarValor.NUMERO);
        jLabTotalMateriais = new javax.swing.JLabel();
        jForTotalMateriais = new FormatarValor(FormatarValor.NUMERO);
        jLabTotalAdicionais = new javax.swing.JLabel();
        jForTotalAdicionais = new FormatarValor(FormatarValor.NUMERO);
        jLabValorTotal = new javax.swing.JLabel();
        jForValorTotal = new FormatarValor(FormatarValor.NUMERO);
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextAreaObsCliente = new javax.swing.JTextArea();
        jPanBotoes = new javax.swing.JPanel();
        jForDataCadAtend = new javax.swing.JFormattedTextField();
        jLabDataModifiAtend = new javax.swing.JLabel();
        jForDataModifiAtend = new javax.swing.JFormattedTextField();
        jTexRegAtualAtend = new javax.swing.JTextField();
        jTexRegTotalAtend = new javax.swing.JTextField();
        jLabRegAtend = new javax.swing.JLabel();
        jLabCadPorAtend = new javax.swing.JLabel();
        jTexCadPorAtend = new javax.swing.JTextField();
        jTexModifPorAtend = new javax.swing.JTextField();
        jForHoraCadAtend = new javax.swing.JFormattedTextField();
        jForHoraModifAtend = new javax.swing.JFormattedTextField();
        jButVistoria = new javax.swing.JButton();
        jButProposta = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanDetalhes = new javax.swing.JPanel();
        jTabbedPaneDetalhes = new javax.swing.JTabbedPane();
        jSeparator2 = new javax.swing.JSeparator();
        jLabTipoVerniz = new javax.swing.JLabel();
        jLabMetragemArea = new javax.swing.JLabel();
        jForMetragemArea = new FormatarValor(FormatarValor.PORCENTAGEM)
        ;
        jLabel6 = new javax.swing.JLabel();
        jLabPercPerda = new javax.swing.JLabel();
        jForPercPerda = new FormatarValor(FormatarValor.PORCENTAGEM)
        ;
        jSeparator3 = new javax.swing.JSeparator();
        jLabTipoPiso = new javax.swing.JLabel();
        jComTipoPiso = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        jLabTipoRodape = new javax.swing.JLabel();
        jComTipoRodape = new javax.swing.JComboBox<>();
        jLabMetragemRodape = new javax.swing.JLabel();
        jForMetragemRodape = new FormatarValor(FormatarValor.PORCENTAGEM)
        ;
        jCheTingimento = new javax.swing.JCheckBox();
        jCheClareamento = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jLabLargura = new javax.swing.JLabel();
        jForLargura = new FormatarValor(FormatarValor.PORCENTAGEM)
        ;
        jLabComprimento = new javax.swing.JLabel();
        jLabEspessura = new javax.swing.JLabel();
        jForEspessura = new FormatarValor(FormatarValor.PORCENTAGEM)
        ;
        jTexComprimento = new javax.swing.JTextField();
        jLabAmbiente = new javax.swing.JLabel();
        jTexNomeLocal = new javax.swing.JTextField();
        jButLocalAnterior = new javax.swing.JButton();
        jButLocalProximo = new javax.swing.JButton();
        jButNovoLocal = new javax.swing.JButton();
        jTexNomeEssenciaMadeira = new javax.swing.JTextField();
        jTexCdTipoVerniz = new javax.swing.JTextField();
        jTexNomeTipoVerniz = new javax.swing.JTextField();
        jTexEssenciaMadeira = new javax.swing.JTextField();
        jPanItens = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabItensLocal = new javax.swing.JTable();
        jLabValorServicoLocal = new javax.swing.JLabel();
        jForValorServicoLocal = new FormatarValor(FormatarValor.NUMERO);
        jLabValorMateriaisLocal = new javax.swing.JLabel();
        jForValorMateriaisLocal = new FormatarValor(FormatarValor.NUMERO);
        jLabValorAdicionaisLocal = new javax.swing.JLabel();
        jForValorAdicionaisLocal = new FormatarValor(FormatarValor.NUMERO);
        jLabValorTotalLocal = new javax.swing.JLabel();
        jForValorTotalLocal = new FormatarValor(FormatarValor.NUMERO);
        jPanInfoCompl = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaObsLocal = new javax.swing.JTextArea();
        jPanBotoes1 = new javax.swing.JPanel();
        jForDataCadLocal = new javax.swing.JFormattedTextField();
        jForDataModifLocal = new javax.swing.JFormattedTextField();
        jLabCadPorLocal = new javax.swing.JLabel();
        jTexCadPorLocal = new javax.swing.JTextField();
        jTexModifPorLocal = new javax.swing.JTextField();
        jLabModifPorLocal = new javax.swing.JLabel();
        jTexRegAtualLocal = new javax.swing.JTextField();
        jLabRegLocal = new javax.swing.JLabel();
        jTexRegTotalLocal = new javax.swing.JTextField();
        jForHoraCadLocal = new javax.swing.JFormattedTextField();
        jForHoraModifLocal = new javax.swing.JFormattedTextField();
        jPanCondPag = new javax.swing.JPanel();
        jLabTipoPagamento = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jForNumParcelas = new FormatarValor(FormatarValor.NUMERO)
        ;
        jLabel8 = new javax.swing.JLabel();
        jForValorParcelas = new FormatarValor(FormatarValor.MOEDA)
        ;
        jButCalcularParcela = new javax.swing.JButton();
        jForDesc = new FormatarValor(FormatarValor.PORCENTAGEM);
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manter Atendimento Cliente");
        setMinimumSize(new java.awt.Dimension(810, 695));
        setResizable(false);

        jTooMenuFerramentas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTooMenuFerramentas.setRollover(true);

        jButNovo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Addthis-32.png"))); // NOI18N
        jButNovo.setText("Novo");
        jButNovo.setEnabled(false);
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
        jButCancelar.setEnabled(false);
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
        jButPesquisar.setEnabled(false);
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
        jButProximo.setText("Próximo");
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

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Núm. Atend.:");

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Data Atend.:");

        try {
            jForDataAtendimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabHoraAtendimento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabHoraAtendimento.setText("Hora Atend.:");

        try {
            jForHoraAtendimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabSituacao.setText("Situação:");

        jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AA-Aguardando Agendamento", "AV-Aguardando Vistoria", "PG-Proposta Gerada", "NI-Não Iniciado" }));

        jTabbedPanAtendimento.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanCliente.setMaximumSize(new java.awt.Dimension(730, 240));
        jPanCliente.setMinimumSize(new java.awt.Dimension(730, 240));
        jPanCliente.setPreferredSize(new java.awt.Dimension(730, 234));

        jLabTipoPessoa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTipoPessoa.setText("Tipo Pessoa:");

        jComTipoPessoa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Física", "Jurídica" }));
        jComTipoPessoa.setMaximumSize(new java.awt.Dimension(70, 40));
        jComTipoPessoa.setMinimumSize(new java.awt.Dimension(70, 20));
        jComTipoPessoa.setPreferredSize(new java.awt.Dimension(70, 20));

        jLabTelefone.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTelefone.setText("Telefone:");

        try {
            jForTelefone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabCelular.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCelular.setText("Celular:");

        jLabEmail.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabEmail.setText("email:");

        try {
            jForCelular.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) #####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabNomeRazaoSocial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNomeRazaoSocial.setText("Nome / Razão Social:");

        jPanEndereco.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Endereço", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("End.:");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setAutoscrolls(true);

        jComTipoLogradouro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Aeroporto", "Alameda", "Área", "Avenida", "Campo", "Chácara", "Colônia", "Condomínio", "Conjunto", "Distrito", "Esplanada", "Estação", "Estrada", "Favela", "Feira", "Jardim", "Ladeira", "Lago", "Lagoa", "Largo", "Loteamento", "Morro", "Núcleo", "Parque", "Passarela", "Pátio", "Praça", "Quadra", "Recanto", "Residencial", "Rodovia", "Rua", "Setor", "Sítio", "Travessa", "Trecho", "Trevo", "Vale", "Vereda", "Via", "Viaduto", "Viela", "Vila" }));

        jLabNumero.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNumero.setText("Núm.:");

        jLabComplemento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabComplemento.setText("Compl.:");

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel3.setText("Bairro:");

        jLabCidade.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCidade.setText("U.F.:");

        jLabMunicipio.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabMunicipio.setText("Município:");

        jTexMuncipio.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexMuncipioKeyPressed(evt);
            }
        });

        jTexSiglaUF.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexSiglaUFKeyPressed(evt);
            }
        });

        jLabCep.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCep.setText("C.E.P:");

        try {
            jForCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jButValidarCep.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButValidarCep.setText("Validar CEP");
        jButValidarCep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButValidarCepActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Tp. End.:");

        jComTipoEndereco.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Comercial", "Residencial" }));

        javax.swing.GroupLayout jPanEnderecoLayout = new javax.swing.GroupLayout(jPanEndereco);
        jPanEndereco.setLayout(jPanEnderecoLayout);
        jPanEnderecoLayout.setHorizontalGroup(
            jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanEnderecoLayout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComTipoLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 378, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanEnderecoLayout.createSequentialGroup()
                        .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabCidade)
                            .addComponent(jLabComplemento))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                                .addComponent(jTexComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                                .addComponent(jTexSiglaUF, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabMunicipio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexMuncipio, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComTipoEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanEnderecoLayout.createSequentialGroup()
                        .addComponent(jLabCep)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForCep, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanEnderecoLayout.createSequentialGroup()
                        .addComponent(jLabNumero)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jButValidarCep))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanEnderecoLayout.setVerticalGroup(
            jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComTipoLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabNumero)
                    .addComponent(jTexNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexLogradouro)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabComplemento)
                    .addComponent(jTexComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTexBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabCep)
                    .addComponent(jForCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCidade)
                    .addComponent(jLabMunicipio)
                    .addComponent(jTexMuncipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexSiglaUF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButValidarCep)
                    .addComponent(jLabel5)
                    .addComponent(jComTipoEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        jPanTotalAtendimento.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabTotalServico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTotalServico.setText("Tot. Serviço.:");

        jForTotalServico.setEditable(false);
        jForTotalServico.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jLabTotalMateriais.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTotalMateriais.setText("Tot. Materiais:");

        jForTotalMateriais.setEditable(false);
        jForTotalMateriais.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jLabTotalAdicionais.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTotalAdicionais.setText("Tot. Adicionais:");

        jForTotalAdicionais.setEditable(false);
        jForTotalAdicionais.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jLabValorTotal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValorTotal.setText("Total:");

        jForValorTotal.setEditable(false);
        jForValorTotal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        javax.swing.GroupLayout jPanTotalAtendimentoLayout = new javax.swing.GroupLayout(jPanTotalAtendimento);
        jPanTotalAtendimento.setLayout(jPanTotalAtendimentoLayout);
        jPanTotalAtendimentoLayout.setHorizontalGroup(
            jPanTotalAtendimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTotalAtendimentoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabTotalServico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForTotalServico, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabTotalMateriais)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForTotalMateriais, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabTotalAdicionais)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForTotalAdicionais, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabValorTotal)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(106, Short.MAX_VALUE))
        );
        jPanTotalAtendimentoLayout.setVerticalGroup(
            jPanTotalAtendimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTotalAtendimentoLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTotalAtendimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabTotalServico)
                    .addComponent(jForTotalServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabTotalMateriais)
                    .addComponent(jForTotalMateriais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabTotalAdicionais)
                    .addComponent(jForTotalAdicionais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabValorTotal)
                    .addComponent(jForValorTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Observação do Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        jTextAreaObsCliente.setColumns(20);
        jTextAreaObsCliente.setRows(5);
        jScrollPane3.setViewportView(jTextAreaObsCliente);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanBotoes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jForDataCadAtend.setEditable(false);
        jForDataCadAtend.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataCadAtend.setEnabled(false);

        jLabDataModifiAtend.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDataModifiAtend.setText("Modificado:");

        jForDataModifiAtend.setEditable(false);
        jForDataModifiAtend.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataModifiAtend.setEnabled(false);

        jTexRegAtualAtend.setEditable(false);
        jTexRegAtualAtend.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexRegAtualAtend.setEnabled(false);

        jTexRegTotalAtend.setEditable(false);
        jTexRegTotalAtend.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexRegTotalAtend.setEnabled(false);

        jLabRegAtend.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabRegAtend.setText("\\");

            jLabCadPorAtend.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCadPorAtend.setText("Cadastrado:");

            jTexCadPorAtend.setEditable(false);
            jTexCadPorAtend.setEnabled(false);

            jTexModifPorAtend.setEditable(false);
            jTexModifPorAtend.setEnabled(false);

            jForHoraCadAtend.setEditable(false);
            try {
                jForHoraCadAtend.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForHoraCadAtend.setEnabled(false);

            jForHoraModifAtend.setEditable(false);
            try {
                jForHoraModifAtend.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForHoraModifAtend.setEnabled(false);

            javax.swing.GroupLayout jPanBotoesLayout = new javax.swing.GroupLayout(jPanBotoes);
            jPanBotoes.setLayout(jPanBotoesLayout);
            jPanBotoesLayout.setHorizontalGroup(
                jPanBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanBotoesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTexRegAtualAtend, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabRegAtend, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexRegTotalAtend, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabCadPorAtend)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexCadPorAtend, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataCadAtend, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForHoraCadAtend, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabDataModifiAtend)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexModifPorAtend, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataModifiAtend, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForHoraModifAtend, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanBotoesLayout.setVerticalGroup(
                jPanBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanBotoesLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jForDataCadAtend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexRegAtualAtend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexRegTotalAtend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabRegAtend)
                        .addComponent(jLabCadPorAtend)
                        .addComponent(jTexCadPorAtend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexModifPorAtend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabDataModifiAtend)
                        .addComponent(jForDataModifiAtend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jForHoraCadAtend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jForHoraModifAtend, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
            );

            jButVistoria.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButVistoria.setText("Vistoria");

            jButProposta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButProposta.setText("Proposta");
            jButProposta.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButPropostaActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanClienteLayout = new javax.swing.GroupLayout(jPanCliente);
            jPanCliente.setLayout(jPanClienteLayout);
            jPanClienteLayout.setHorizontalGroup(
                jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanClienteLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanTotalAtendimento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanClienteLayout.createSequentialGroup()
                            .addComponent(jLabNomeRazaoSocial)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexNomeRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabTipoPessoa)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComTipoPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanClienteLayout.createSequentialGroup()
                            .addComponent(jLabTelefone)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabCelular)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabEmail)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanClienteLayout.createSequentialGroup()
                            .addComponent(jPanEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButProposta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButVistoria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanBotoes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addContainerGap())
            );
            jPanClienteLayout.setVerticalGroup(
                jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanClienteLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabNomeRazaoSocial)
                        .addComponent(jTexNomeRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabTipoPessoa)
                        .addComponent(jComTipoPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabEmail)
                            .addComponent(jTexEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabTelefone)
                            .addComponent(jForTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabCelular)
                            .addComponent(jForCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanClienteLayout.createSequentialGroup()
                            .addComponent(jButVistoria)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButProposta)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanTotalAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(2, 2, 2))
            );

            jTabbedPanAtendimento.addTab("Cliente", jPanCliente);

            jLabTipoVerniz.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTipoVerniz.setText("Tp. Verniz:");

            jLabMetragemArea.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabMetragemArea.setText("Área(m2):");

            jForMetragemArea.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##0.###"))));

            jLabel6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel6.setText("Essen.: Madeira:");

            jLabPercPerda.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabPercPerda.setText("% Perda:");

            jForPercPerda.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##0.###"))));

            jLabTipoPiso.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTipoPiso.setText("Tp. Piso:");

            jComTipoPiso.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ambos", "Assoalho", "Deck", "Taco" }));

            jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações Rodapé", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jLabTipoRodape.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTipoRodape.setText("Tipo:");

            jComTipoRodape.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Madeira", "MDF" }));

            jLabMetragemRodape.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabMetragemRodape.setText("Metragem:");

            jForMetragemRodape.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##0.###"))));

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabTipoRodape)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComTipoRodape, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(jLabMetragemRodape)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForMetragemRodape, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabTipoRodape)
                        .addComponent(jComTipoRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabMetragemRodape)
                        .addComponent(jForMetragemRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jCheTingimento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jCheTingimento.setText("Tingimento");

            jCheClareamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jCheClareamento.setText("Clareamento");

            jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Dimenssões da Peça", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jLabLargura.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabLargura.setText("Larg.:");

            jForLargura.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##0.###"))));

            jLabComprimento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabComprimento.setText("Comp.:");

            jLabEspessura.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabEspessura.setText("Espess.:");

            jForEspessura.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("##0.###"))));

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addComponent(jLabLargura)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForLargura, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabComprimento)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexComprimento, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabEspessura)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForEspessura, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabLargura)
                        .addComponent(jForLargura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabComprimento)
                        .addComponent(jLabEspessura)
                        .addComponent(jForEspessura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexComprimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 0, Short.MAX_VALUE))
            );

            jLabAmbiente.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabAmbiente.setText("Ambiente:");

            jButLocalAnterior.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButLocalAnterior.setText("<<");
            jButLocalAnterior.setEnabled(false);
            jButLocalAnterior.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButLocalAnteriorActionPerformed(evt);
                }
            });

            jButLocalProximo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButLocalProximo.setText(">>");
            jButLocalProximo.setEnabled(false);
            jButLocalProximo.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButLocalProximoActionPerformed(evt);
                }
            });

            jButNovoLocal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButNovoLocal.setText("Novo");
            jButNovoLocal.setEnabled(false);
            jButNovoLocal.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButNovoLocalActionPerformed(evt);
                }
            });

            jTexNomeEssenciaMadeira.setEditable(false);
            jTexNomeEssenciaMadeira.setEnabled(false);

            jTexCdTipoVerniz.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCdTipoVernizKeyPressed(evt);
                }
            });

            jTexNomeTipoVerniz.setEditable(false);
            jTexNomeTipoVerniz.setEnabled(false);

            jTexEssenciaMadeira.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexEssenciaMadeiraKeyPressed(evt);
                }
            });

            javax.swing.GroupLayout jPanDetalhesLayout = new javax.swing.GroupLayout(jPanDetalhes);
            jPanDetalhes.setLayout(jPanDetalhesLayout);
            jPanDetalhesLayout.setHorizontalGroup(
                jPanDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanDetalhesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSeparator2)
                        .addComponent(jSeparator3)
                        .addGroup(jPanDetalhesLayout.createSequentialGroup()
                            .addGroup(jPanDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTabbedPaneDetalhes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanDetalhesLayout.createSequentialGroup()
                                    .addComponent(jLabMetragemArea)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForMetragemArea, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabPercPerda)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForPercPerda, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabTipoPiso)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComTipoPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jCheTingimento))
                                .addGroup(jPanDetalhesLayout.createSequentialGroup()
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanDetalhesLayout.createSequentialGroup()
                                            .addGap(33, 33, 33)
                                            .addComponent(jCheClareamento))
                                        .addGroup(jPanDetalhesLayout.createSequentialGroup()
                                            .addComponent(jLabTipoVerniz)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTexCdTipoVerniz, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGap(18, 18, Short.MAX_VALUE)
                            .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanDetalhesLayout.createSequentialGroup()
                            .addGroup(jPanDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanDetalhesLayout.createSequentialGroup()
                                    .addComponent(jLabel6)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jTexEssenciaMadeira, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexNomeEssenciaMadeira, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(30, 30, 30)
                                    .addComponent(jTexNomeTipoVerniz, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanDetalhesLayout.createSequentialGroup()
                                    .addComponent(jLabAmbiente)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexNomeLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jButLocalAnterior)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButLocalProximo)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jButNovoLocal)))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
            );
            jPanDetalhesLayout.setVerticalGroup(
                jPanDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanDetalhesLayout.createSequentialGroup()
                    .addComponent(jTabbedPaneDetalhes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabAmbiente)
                        .addComponent(jTexNomeLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButLocalAnterior)
                        .addComponent(jButLocalProximo)
                        .addComponent(jButNovoLocal))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanDetalhesLayout.createSequentialGroup()
                            .addGroup(jPanDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jForMetragemArea, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabMetragemArea)
                                .addComponent(jLabPercPerda)
                                .addComponent(jForPercPerda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabTipoPiso)
                                .addComponent(jComTipoPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jCheTingimento))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanDetalhesLayout.createSequentialGroup()
                                    .addComponent(jCheClareamento)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabTipoVerniz)
                                        .addComponent(jTexCdTipoVerniz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 0, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTexNomeTipoVerniz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(jTexNomeEssenciaMadeira, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTexEssenciaMadeira, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jPanItens.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Itens", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jTabItensLocal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabItensLocal.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null, null, null}
                },
                new String [] {
                    "Seq.", "Cod.", "Descrição", "U.M", "Qtde", "Pr. Unit.", "Tot. Item"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            jScrollPane1.setViewportView(jTabItensLocal);
            if (jTabItensLocal.getColumnModel().getColumnCount() > 0) {
                jTabItensLocal.getColumnModel().getColumn(0).setResizable(false);
                jTabItensLocal.getColumnModel().getColumn(0).setPreferredWidth(10);
                jTabItensLocal.getColumnModel().getColumn(1).setResizable(false);
                jTabItensLocal.getColumnModel().getColumn(1).setPreferredWidth(40);
                jTabItensLocal.getColumnModel().getColumn(2).setResizable(false);
                jTabItensLocal.getColumnModel().getColumn(2).setPreferredWidth(300);
                jTabItensLocal.getColumnModel().getColumn(3).setResizable(false);
                jTabItensLocal.getColumnModel().getColumn(3).setPreferredWidth(10);
                jTabItensLocal.getColumnModel().getColumn(4).setResizable(false);
                jTabItensLocal.getColumnModel().getColumn(4).setPreferredWidth(30);
                jTabItensLocal.getColumnModel().getColumn(5).setResizable(false);
                jTabItensLocal.getColumnModel().getColumn(5).setPreferredWidth(40);
                jTabItensLocal.getColumnModel().getColumn(6).setResizable(false);
                jTabItensLocal.getColumnModel().getColumn(6).setPreferredWidth(40);
            }

            javax.swing.GroupLayout jPanItensLayout = new javax.swing.GroupLayout(jPanItens);
            jPanItens.setLayout(jPanItensLayout);
            jPanItensLayout.setHorizontalGroup(
                jPanItensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanItensLayout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanItensLayout.setVerticalGroup(
                jPanItensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanItensLayout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );

            jLabValorServicoLocal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorServicoLocal.setText("Val. Serv.:");

            jForValorServicoLocal.setEditable(false);
            jForValorServicoLocal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

            jLabValorMateriaisLocal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorMateriaisLocal.setText("Val. Mat.:");

            jForValorMateriaisLocal.setEditable(false);
            jForValorMateriaisLocal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

            jLabValorAdicionaisLocal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorAdicionaisLocal.setText("Val. Adic.:");

            jForValorAdicionaisLocal.setEditable(false);
            jForValorAdicionaisLocal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

            jLabValorTotalLocal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorTotalLocal.setText("Tot. Local:");

            jForValorTotalLocal.setEditable(false);
            jForValorTotalLocal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

            jPanInfoCompl.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações Complementares", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

            jTextAreaObsLocal.setColumns(20);
            jTextAreaObsLocal.setRows(5);
            jScrollPane2.setViewportView(jTextAreaObsLocal);

            javax.swing.GroupLayout jPanInfoComplLayout = new javax.swing.GroupLayout(jPanInfoCompl);
            jPanInfoCompl.setLayout(jPanInfoComplLayout);
            jPanInfoComplLayout.setHorizontalGroup(
                jPanInfoComplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanInfoComplLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 749, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanInfoComplLayout.setVerticalGroup(
                jPanInfoComplLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanInfoComplLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(39, 39, 39))
            );

            jPanBotoes1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jForDataCadLocal.setEditable(false);
            jForDataCadLocal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
            jForDataCadLocal.setEnabled(false);

            jForDataModifLocal.setEditable(false);
            jForDataModifLocal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
            jForDataModifLocal.setEnabled(false);

            jLabCadPorLocal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCadPorLocal.setText("Cadastrado:");

            jTexCadPorLocal.setEditable(false);
            jTexCadPorLocal.setEnabled(false);

            jTexModifPorLocal.setEditable(false);
            jTexModifPorLocal.setEnabled(false);

            jLabModifPorLocal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabModifPorLocal.setText("Modificado:");

            jTexRegAtualLocal.setEditable(false);
            jTexRegAtualLocal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            jTexRegAtualLocal.setEnabled(false);

            jLabRegLocal.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
            jLabRegLocal.setText("\\");

                jTexRegTotalLocal.setEditable(false);
                jTexRegTotalLocal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
                jTexRegTotalLocal.setEnabled(false);

                jForHoraCadLocal.setEditable(false);
                try {
                    jForHoraCadLocal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
                } catch (java.text.ParseException ex) {
                    ex.printStackTrace();
                }
                jForHoraCadLocal.setEnabled(false);

                jForHoraModifLocal.setEditable(false);
                try {
                    jForHoraModifLocal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
                } catch (java.text.ParseException ex) {
                    ex.printStackTrace();
                }
                jForHoraModifLocal.setEnabled(false);

                javax.swing.GroupLayout jPanBotoes1Layout = new javax.swing.GroupLayout(jPanBotoes1);
                jPanBotoes1.setLayout(jPanBotoes1Layout);
                jPanBotoes1Layout.setHorizontalGroup(
                    jPanBotoes1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanBotoes1Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addComponent(jTexRegAtualLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(3, 3, 3)
                        .addComponent(jLabRegLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(2, 2, 2)
                        .addComponent(jTexRegTotalLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabCadPorLocal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexCadPorLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForDataCadLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForHoraCadLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabModifPorLocal)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexModifPorLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForDataModifLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForHoraModifLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                );
                jPanBotoes1Layout.setVerticalGroup(
                    jPanBotoes1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanBotoes1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanBotoes1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanBotoes1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTexRegTotalLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabRegLocal)
                                .addComponent(jTexRegAtualLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanBotoes1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jForDataCadLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabCadPorLocal)
                                .addComponent(jTexCadPorLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabModifPorLocal)
                                .addComponent(jTexModifPorLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jForDataModifLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jForHoraCadLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jForHoraModifLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );

                javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
                jPanel4.setLayout(jPanel4Layout);
                jPanel4Layout.setHorizontalGroup(
                    jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanDetalhes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jPanBotoes1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addGroup(jPanel4Layout.createSequentialGroup()
                                            .addComponent(jLabValorServicoLocal)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jForValorServicoLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabValorMateriaisLocal)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jForValorMateriaisLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabValorAdicionaisLocal)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jForValorAdicionaisLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabValorTotalLocal)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jForValorTotalLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jPanInfoCompl, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(jPanItens, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())
                );
                jPanel4Layout.setVerticalGroup(
                    jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jPanDetalhes, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addComponent(jPanItens, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabValorServicoLocal)
                            .addComponent(jForValorServicoLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabValorMateriaisLocal)
                            .addComponent(jForValorMateriaisLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabValorAdicionaisLocal)
                            .addComponent(jForValorAdicionaisLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabValorTotalLocal)
                            .addComponent(jForValorTotalLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanInfoCompl, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanBotoes1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap())
                );

                jTabbedPanAtendimento.addTab("Ambientes", jPanel4);

                jLabTipoPagamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                jLabTipoPagamento.setText("Desconto:");

                jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                jLabel7.setText("Parcelas:");

                jForNumParcelas.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

                jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                jLabel8.setText("Valor Parcelas:");

                jForValorParcelas.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));

                jButCalcularParcela.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                jButCalcularParcela.setText("Calcular");
                jButCalcularParcela.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jButCalcularParcelaActionPerformed(evt);
                    }
                });

                jForDesc.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

                javax.swing.GroupLayout jPanCondPagLayout = new javax.swing.GroupLayout(jPanCondPag);
                jPanCondPag.setLayout(jPanCondPagLayout);
                jPanCondPagLayout.setHorizontalGroup(
                    jPanCondPagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCondPagLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanCondPagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabTipoPagamento)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanCondPagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jForNumParcelas, javax.swing.GroupLayout.DEFAULT_SIZE, 50, Short.MAX_VALUE)
                            .addComponent(jForDesc))
                        .addGap(16, 16, 16)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForValorParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(22, 22, 22)
                        .addComponent(jButCalcularParcela)
                        .addContainerGap())
                );
                jPanCondPagLayout.setVerticalGroup(
                    jPanCondPagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanCondPagLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanCondPagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabTipoPagamento)
                            .addComponent(jForDesc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanCondPagLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel7)
                            .addComponent(jForNumParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel8)
                            .addComponent(jForValorParcelas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButCalcularParcela))
                        .addGap(411, 411, 411))
                );

                jTabbedPanAtendimento.addTab("Pagamento", jPanCondPag);

                jMenuBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

                jMenu1.setText("Arquivo");

                jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
                jMenuItem1.setText("Novo");
                jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jMenuItem1ActionPerformed(evt);
                    }
                });
                jMenu1.add(jMenuItem1);

                jMenuItemSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
                jMenuItemSalvar.setText("Salvar");
                jMenuItemSalvar.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jMenuItemSalvarActionPerformed(evt);
                    }
                });
                jMenu1.add(jMenuItemSalvar);

                jMenuItemSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
                jMenuItemSair.setText("Sair");
                jMenuItemSair.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jMenuItemSairActionPerformed(evt);
                    }
                });
                jMenu1.add(jMenuItemSair);

                jMenuBar.add(jMenu1);

                jMenu2.setText("Editar");

                jMenuItemEditar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
                jMenuItemEditar.setText("Editar");
                jMenuItemEditar.addActionListener(new java.awt.event.ActionListener() {
                    public void actionPerformed(java.awt.event.ActionEvent evt) {
                        jMenuItemEditarActionPerformed(evt);
                    }
                });
                jMenu2.add(jMenuItemEditar);

                jMenuBar.add(jMenu2);

                setJMenuBar(jMenuBar);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
                getContentPane().setLayout(layout);
                layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, 810, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForCdAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForDataAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabHoraAtendimento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForHoraAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabSituacao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jTabbedPanAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                );
                layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabSituacao)
                            .addComponent(jLabel2)
                            .addComponent(jForCdAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4)
                            .addComponent(jForDataAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabHoraAtendimento)
                            .addComponent(jForHoraAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTabbedPanAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                );

                pack();
            }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        jButNovoActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItemSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalvarActionPerformed
        // TODO add your handling code here:
        jButSalvarActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemSalvarActionPerformed

    private void jMenuItemSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jMenuItemSairActionPerformed

    private void jMenuItemEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEditarActionPerformed
        // TODO add your handling code here:
        jButEditarActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemEditarActionPerformed

    private void jButNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButNovoActionPerformed
        operLocal = "N";
        idxNovoLocal = numRegLocal + 1;
        esvaziarTabelaItem();
        limparTelaLocal();
        liberarCamposLocal();
        jTexNomeLocal.setEditable(true);
        jTexNomeLocal.requestFocus();
        jTexNomeLocal.selectAll();
        controleBotoes(!ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO);
    }//GEN-LAST:event_jButNovoActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        // TODO add your handling code here:
        if (jTabbedPanAtendimento.getSelectedIndex() == 0) {
            liberarCamposAtend();
            operAnted = "A";         // se estiver editando um registro do sistema, a operação do sistema será A de alteração de Registro
        } else if (jTabbedPanAtendimento.getSelectedIndex() == 1) {
            liberarCamposLocal();
            jButSalvar.setEnabled(true);
            esvaziarTabela = true;
            jTexNomeLocal.setEditable(true);
            jTexNomeLocal.requestFocus();
            jTexNomeLocal.selectAll();
            operLocal = "A";
        } else {
            JOptionPane.showMessageDialog(null, "Não foi possível detectar a Guia!");
        }
        controleBotoes(!ISBOTAO, ISBOTAO, ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO);
    }//GEN-LAST:event_jButEditarActionPerformed

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        if (jTabbedPanAtendimento.getSelectedIndex() == 0) {
            salvarAtendimento(true);
        } else if (jTabbedPanAtendimento.getSelectedIndex() == 1) {
            salvarLocal(true);
        } else {
            JOptionPane.showMessageDialog(null, "Não foi possível detectar a Guia");
        }
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
        // TODO add your handling code here:
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO);
        limparTelaAtendimento();
        pesquisarLocal(true);
        operAnted = "N";         // se cancelar a ação atual na tela do sistema a operação do sistema será N  de novo Registro
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO);
        if (jTabbedPanAtendimento.getSelectedIndex() == 0) {
            excluirAtend();
        } else if (jTabbedPanAtendimento.getSelectedIndex() == 1) {
            excluirLocal();
        } else {
            JOptionPane.showMessageDialog(null, "Não foi possível detectar a Guia!");
        }
    }//GEN-LAST:event_jButExcluirActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:
        if (jTexNomeRazaoSocial.getText().isEmpty()) {
            sqlAtend = "SELECT * FROM GCVATENDIMENTO";
        } else {
            sqlAtend = "SELECT * FROM GCVATENDIMENTO WHERE NOME_CLIENTE LIKE '" + jTexNomeRazaoSocial.getText().trim() + "'";
        }
        bloquearCamposAtend();
        pesquisarAtendimento();
    }//GEN-LAST:event_jButPesquisarActionPerformed

    private void jButAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAnteriorActionPerformed
        // TODO add your handling code here:
        idxCorAtend -= 1;
        upRegistrosAtendimento();
    }//GEN-LAST:event_jButAnteriorActionPerformed

    private void jButProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButProximoActionPerformed
        // TODO add your handling code here:
        idxCorAtend += 1;
        upRegistrosAtendimento();
    }//GEN-LAST:event_jButProximoActionPerformed

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        modcon.setVoltar(true);
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jTexMuncipioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexMuncipioKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomMunicipio();
        }
    }//GEN-LAST:event_jTexMuncipioKeyPressed

    private void jTexSiglaUFKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexSiglaUFKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomUF();
        }
    }//GEN-LAST:event_jTexSiglaUFKeyPressed

    private void jButValidarCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButValidarCepActionPerformed
        try {
            buscarCep();
        } catch (IOException ex) {
            Logger.getLogger(bk_1_ManterAtendimentoRev2.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButValidarCepActionPerformed

    private void jButLocalAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButLocalAnteriorActionPerformed
        idxCorLocal -= 1;
        limparTelaLocal();
        esvaziarTabelaItem();
        upRegistrosLocal();
    }//GEN-LAST:event_jButLocalAnteriorActionPerformed

    private void jButLocalProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButLocalProximoActionPerformed
        idxCorLocal += 1;
        limparTelaLocal();
        esvaziarTabelaItem();
        upRegistrosLocal();
    }//GEN-LAST:event_jButLocalProximoActionPerformed

    private void jButNovoLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButNovoLocalActionPerformed
        jButNovoActionPerformed(evt);
    }//GEN-LAST:event_jButNovoLocalActionPerformed

    private void jTexCdTipoVernizKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdTipoVernizKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomTipoVerniz();
        }
    }//GEN-LAST:event_jTexCdTipoVernizKeyPressed

    private void jTexEssenciaMadeiraKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexEssenciaMadeiraKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomEssencia();
        }
    }//GEN-LAST:event_jTexEssenciaMadeiraKeyPressed

    private void jButCalcularParcelaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCalcularParcelaActionPerformed
        calcularPagamento();
    }//GEN-LAST:event_jButCalcularParcelaActionPerformed

    private void jButPropostaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPropostaActionPerformed
        propostaComercial();
    }//GEN-LAST:event_jButPropostaActionPerformed

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
            java.util.logging.Logger.getLogger(bk_1_ManterAtendimentoRev2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(bk_1_ManterAtendimentoRev2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(bk_1_ManterAtendimentoRev2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(bk_1_ManterAtendimentoRev2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                bk_1_ManterAtendimentoRev2 dialog = new bk_1_ManterAtendimentoRev2(new javax.swing.JFrame(), true, su, conexao, pg);
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
    private javax.swing.JButton jButCalcularParcela;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButEditar;
    private javax.swing.JButton jButExcluir;
    private javax.swing.JButton jButLocalAnterior;
    private javax.swing.JButton jButLocalProximo;
    private javax.swing.JButton jButNovo;
    private javax.swing.JButton jButNovoLocal;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JButton jButProposta;
    private javax.swing.JButton jButProximo;
    private javax.swing.JButton jButSair;
    private javax.swing.JButton jButSalvar;
    private javax.swing.JButton jButValidarCep;
    private javax.swing.JButton jButVistoria;
    private javax.swing.JCheckBox jCheClareamento;
    private javax.swing.JCheckBox jCheTingimento;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JComboBox<String> jComTipoEndereco;
    private javax.swing.JComboBox<String> jComTipoLogradouro;
    private javax.swing.JComboBox<String> jComTipoPessoa;
    private javax.swing.JComboBox<String> jComTipoPiso;
    private javax.swing.JComboBox<String> jComTipoRodape;
    private javax.swing.JFormattedTextField jForCdAtendimento;
    private javax.swing.JFormattedTextField jForCelular;
    private javax.swing.JFormattedTextField jForCep;
    private javax.swing.JFormattedTextField jForDataAtendimento;
    private javax.swing.JFormattedTextField jForDataCadAtend;
    private javax.swing.JFormattedTextField jForDataCadLocal;
    private javax.swing.JFormattedTextField jForDataModifLocal;
    private javax.swing.JFormattedTextField jForDataModifiAtend;
    private javax.swing.JFormattedTextField jForDesc;
    private javax.swing.JFormattedTextField jForEspessura;
    private javax.swing.JFormattedTextField jForHoraAtendimento;
    private javax.swing.JFormattedTextField jForHoraCadAtend;
    private javax.swing.JFormattedTextField jForHoraCadLocal;
    private javax.swing.JFormattedTextField jForHoraModifAtend;
    private javax.swing.JFormattedTextField jForHoraModifLocal;
    private javax.swing.JFormattedTextField jForLargura;
    private javax.swing.JFormattedTextField jForMetragemArea;
    private javax.swing.JFormattedTextField jForMetragemRodape;
    private javax.swing.JFormattedTextField jForNumParcelas;
    private javax.swing.JFormattedTextField jForPercPerda;
    private javax.swing.JFormattedTextField jForTelefone;
    private javax.swing.JFormattedTextField jForTotalAdicionais;
    private javax.swing.JFormattedTextField jForTotalMateriais;
    private javax.swing.JFormattedTextField jForTotalServico;
    private javax.swing.JFormattedTextField jForValorAdicionaisLocal;
    private javax.swing.JFormattedTextField jForValorMateriaisLocal;
    private javax.swing.JFormattedTextField jForValorParcelas;
    private javax.swing.JFormattedTextField jForValorServicoLocal;
    private javax.swing.JFormattedTextField jForValorTotal;
    private javax.swing.JFormattedTextField jForValorTotalLocal;
    private javax.swing.JLabel jLabAmbiente;
    private javax.swing.JLabel jLabCadPorAtend;
    private javax.swing.JLabel jLabCadPorLocal;
    private javax.swing.JLabel jLabCelular;
    private javax.swing.JLabel jLabCep;
    private javax.swing.JLabel jLabCidade;
    private javax.swing.JLabel jLabComplemento;
    private javax.swing.JLabel jLabComprimento;
    private javax.swing.JLabel jLabDataModifiAtend;
    private javax.swing.JLabel jLabEmail;
    private javax.swing.JLabel jLabEspessura;
    private javax.swing.JLabel jLabHoraAtendimento;
    private javax.swing.JLabel jLabLargura;
    private javax.swing.JLabel jLabMetragemArea;
    private javax.swing.JLabel jLabMetragemRodape;
    private javax.swing.JLabel jLabModifPorLocal;
    private javax.swing.JLabel jLabMunicipio;
    private javax.swing.JLabel jLabNomeRazaoSocial;
    private javax.swing.JLabel jLabNumero;
    private javax.swing.JLabel jLabPercPerda;
    private javax.swing.JLabel jLabRegAtend;
    private javax.swing.JLabel jLabRegLocal;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabTelefone;
    private javax.swing.JLabel jLabTipoPagamento;
    private javax.swing.JLabel jLabTipoPessoa;
    private javax.swing.JLabel jLabTipoPiso;
    private javax.swing.JLabel jLabTipoRodape;
    private javax.swing.JLabel jLabTipoVerniz;
    private javax.swing.JLabel jLabTotalAdicionais;
    private javax.swing.JLabel jLabTotalMateriais;
    private javax.swing.JLabel jLabTotalServico;
    private javax.swing.JLabel jLabValorAdicionaisLocal;
    private javax.swing.JLabel jLabValorMateriaisLocal;
    private javax.swing.JLabel jLabValorServicoLocal;
    private javax.swing.JLabel jLabValorTotal;
    private javax.swing.JLabel jLabValorTotalLocal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JPanel jPanBotoes;
    private javax.swing.JPanel jPanBotoes1;
    private javax.swing.JPanel jPanCliente;
    private javax.swing.JPanel jPanCondPag;
    private javax.swing.JPanel jPanDetalhes;
    private javax.swing.JPanel jPanEndereco;
    private javax.swing.JPanel jPanInfoCompl;
    private javax.swing.JPanel jPanItens;
    private javax.swing.JPanel jPanTotalAtendimento;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTabItensLocal;
    private javax.swing.JTabbedPane jTabbedPanAtendimento;
    private javax.swing.JTabbedPane jTabbedPaneDetalhes;
    private javax.swing.JTextField jTexBairro;
    private javax.swing.JTextField jTexCadPorAtend;
    private javax.swing.JTextField jTexCadPorLocal;
    private javax.swing.JTextField jTexCdTipoVerniz;
    private javax.swing.JTextField jTexComplemento;
    private javax.swing.JTextField jTexComprimento;
    private javax.swing.JTextField jTexEmail;
    private javax.swing.JTextField jTexEssenciaMadeira;
    private javax.swing.JTextField jTexLogradouro;
    private javax.swing.JTextField jTexModifPorAtend;
    private javax.swing.JTextField jTexModifPorLocal;
    private javax.swing.JTextField jTexMuncipio;
    private javax.swing.JTextField jTexNomeEssenciaMadeira;
    private javax.swing.JTextField jTexNomeLocal;
    private javax.swing.JTextField jTexNomeRazaoSocial;
    private javax.swing.JTextField jTexNomeTipoVerniz;
    private javax.swing.JTextField jTexNumero;
    private javax.swing.JTextField jTexRegAtualAtend;
    private javax.swing.JTextField jTexRegAtualLocal;
    private javax.swing.JTextField jTexRegTotalAtend;
    private javax.swing.JTextField jTexRegTotalLocal;
    private javax.swing.JTextField jTexSiglaUF;
    private javax.swing.JTextArea jTextAreaObsCliente;
    private javax.swing.JTextArea jTextAreaObsLocal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
