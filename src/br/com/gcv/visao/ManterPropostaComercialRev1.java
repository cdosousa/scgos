/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa: GCVMO0030
 */
package br.com.gcv.visao;

// objetos do registro Pai
import br.com.obsoleto.ManterPropostaComercial;
import br.com.gcv.modelo.Proposta;

// Objetos do Registro de Local de Proposta
import br.com.gcv.modelo.LocalProposta;
import br.com.gcv.dao.LocalPropostaDAO;
import br.com.gcv.controle.CLocalProposta;

// objetos de Registro de Itens do Local de Proposta
import br.com.gcv.modelo.ItemProposta;
import br.com.gcv.dao.ItemPropostaDAO;
import br.com.gcv.controle.CItemProposta;

// Objetos para pesquisa de correlato
import br.com.modelo.EnderecoPostal;
import br.com.controle.CEnderecoPostal;
import br.com.gcs.visao.PesquisarEssenciaProdutos;
import br.com.gfc.visao.PesquisarCondicaoPagamento;
import br.com.gcs.visao.PesquisarMateriais;
import br.com.gcs.visao.PesquisarUnidadesMedida;
import br.com.gcv.modelo.Atendimento;
import br.com.gfc.visao.PesquisarTipoPagamento;
import br.com.visao.PesquisarMunicipios;
import br.com.visao.PesquisarUnidadeFederacao;
import br.com.modelo.Empresa;
import br.com.controle.CEmpresa;

// Objetos de instância de parâmetros de ambiente
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;
import br.com.modelo.FormatarValor;
import br.com.modelo.HoraSistema;
import br.com.controle.CBuscarSequencia;
import br.com.gcv.controle.CNewProposta;
import br.com.gcv.controle.CPedido;
import br.com.gcv.controle.CReportPropostaComercial;
import br.com.gcv.dao.NewPropostaDAO;
import br.com.gcv.dao.PropostaDAO;
import br.com.gsm.visao.PesquisarTecnicos;
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
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 11/12/2017
 */
public class ManterPropostaComercialRev1 extends javax.swing.JFrame {

    // Variáveis de instancia de parâmetros de ambiente
    private final boolean ISBOTAO = true;
    private static Connection conexao;
    private static SessaoUsuario su;
    private static ParametrosGerais pg;
    private Empresa emp;
    private VerificarTecla vt;
    private NumberFormat formato;
    private NumberFormat ftq;
    private NumberFormat ftp;
    private NumberFormat ftv;
    private DataSistema dat;
    private String data = null;

    // Variáveis de instância de objetos da tabela Proposta da classe
    private Proposta regCorProposta;
    private List< Proposta> resultProposta;
    private Proposta pro; // objeto específico para salvar o registro no banco
    private CNewProposta cpro;
    private Proposta modpro; // objeto específico para carregar o registro na tela

    // Variáveis de instância de objetos da tabela localProposta da classe
    private LocalProposta regCorLocal;
    private List< LocalProposta> resultLocal;
    private LocalProposta lpr; // Objeto específico para salvar o registro no banco
    private CLocalProposta clpr;
    private LocalProposta modlpr;

    // Variáveis de Instância de objetos da Tabela itemProposta da classe
    private ItemProposta itp; //Objeto para gravar o registro no banco
    private CItemProposta citp;
    private ItemPropostaDAO itpdao;
    private ItemProposta moditp;
    private DefaultTableModel itens;

    // Variáveis de instância da objetos correlatos classe
    private CEnderecoPostal cep;
    private EnderecoPostal ep;
    private String cdMunicipioIbge = "";
    private String cdUfIbge;

    // Variáveis de instância da classe
    // == registro Proposta
    private int numRegProp;
    private int idxCorProp;
    private String sqlProp;
    private String operProp;
    private double txJuros;
    private int numRegPedido = 0;
    private boolean fecharProposta = false;

    // == registro local da Proposta
    private int numRegLocal;
    private int idxCorLocal;
    private int idxCorLocalAtualizado = 0;
    private int idxNovoLocal = 0;
    private String sqlLocal;
    private String operLocal;
    private double oldValorProposta;
    private int sequenciaLocal;
    private final String DATASOURCE = "gcvproposta";

    // == registro itens do local da Proposta
    private int numRegItens;
    private int idxCorItens;
    private String sqlitp;
    private String operItens;
    private int sequenciaItens;
    private int linhaItem = 0;
    private double descAlcada = 0.000;
    private double valorUnitBrt = 0.00;
    private double valorUnitLiq = 0.00;
    private double valorServico = 0.00;
    private boolean esvaziarTabela = false;

    // == objetos do atendimento
    private Atendimento modate;

    /**
     * Construtor padrão para abertura do programa via Menu do Sistema, permite
     * a pesquisa de todas as propostas do banco de dados
     *
     * @param su Sessão ativa do usuário
     * @param conexao Objeto de conexão ativa com o banco de dados
     * @param pg Objeto de Parametros Gerais do Sistema
     */
    public ManterPropostaComercialRev1(SessaoUsuario su, Connection conexao, ParametrosGerais pg) {
        this.su = su;
        this.conexao = conexao;
        this.pg = pg;
        formato = NumberFormat.getInstance(Locale.getDefault());
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setaProposta();
        setaLocalProposta();
        setaItensProposta();
        monitoraLinhaItens();
        formatarCampos();
        sqlProp = "select * from gcvproposta";
        dispose();
    }

    /**
     * Construtor sobrecarregado da Classe para chamada do programa através do
     * Programa de Atendimento Permite abrir o programa com a Proposta Vinculada
     * ao Atendimento e, na ausência da Proposta, permite a geração da Proposta
     * Comercial
     *
     * @param su Objeto contendo a Sessão ativa do usuário
     * @param conexao Objeto contendo a conexão ativa com o banco de dados
     * @param pg Objeto contendo os parametros gerais do sistema
     * @param modate Objeto contendo o Atendimento para geração/consulta da Proposta
     */
    public ManterPropostaComercialRev1(SessaoUsuario su, Connection conexao, ParametrosGerais pg, Atendimento modate) {
        this.su = su;
        this.conexao = conexao;
        this.pg = pg;
        formato = NumberFormat.getInstance(Locale.getDefault());
        this.modate = modate;
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setaProposta();
        setaLocalProposta();
        setaItensProposta();
        formatarCampos();
        sqlProp = "select * from gcvproposta where cd_atendimento = '" + modate.getCdAtendimento()
                + "'";
        pesquisarProposta();
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO);
        dispose();
    }

    /**
     * Construtor padrão para abertura do programa via Pedido Comercial, permite
     * a pesquisa da Proposta Comercial vinculada ao Pedido comercial
     *
     * @param su Sessão ativa do usuário
     * @param conexao Objeto de conexão ativa com o banco de dados
     * @param pg Objeto de Parametros Gerais do Sistema
     * @param sqlPro Sentença SQL para buscar a Proposta Comercial vinculada ao pedido
     * @param isBotao controla o estado dos botões na tela
     */
    public ManterPropostaComercialRev1(SessaoUsuario su, Connection conexao, ParametrosGerais pg, String sqlPro, boolean isBotao) {
        this.su = su;
        this.conexao = conexao;
        this.pg = pg;
        this.sqlProp = sqlPro;
        formato = NumberFormat.getInstance(Locale.getDefault());
        //operProp = "N";
        //operLocal = "N";
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        monitoraLinhaItens();
        formatarCampos();
        controleBotoes(isBotao, isBotao, isBotao, isBotao, isBotao, !isBotao);
        jButCopiar.setEnabled(isBotao);
        jButPesquisar.setEnabled(isBotao);
        bloquearCamposProposta();
        bloquearCamposLocal();
        setaProposta();
        pesquisarProposta();
        dispose();
    }

    // método para setar Proposta
    private void setaProposta() {
        cpro = new CNewProposta(conexao, su);
        if (jTabbedPanProposta.getSelectedIndex() == 0) {
            jButSalvar.setEnabled(true);
        }
        monitoraJanela();
    }

    // método para setar LocalProposta
    private void setaLocalProposta() {
        modlpr = new LocalProposta();
    }

    // método para setar itensProposta
    private void setaItensProposta() {
        moditp = new ItemProposta();
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
        jForValorServicoLocal.setDocument(new DefineCampoDecimal());
        jForValorMateriaisLocal.setDocument(new DefineCampoDecimal());
        jForValorAdicionaisLocal.setDocument(new DefineCampoDecimal());
        jForValorDescontosLocal.setDocument(new DefineCampoDecimal());
        //jForValorOutrosDescLocal.setDocument(new DefineCampoDecimal());
        jForValorTotalBrutoLocal.setDocument(new DefineCampoDecimal());
        jForValorTotalLiquidoLocal.setDocument(new DefineCampoDecimal());
        jForTotalServico.setDocument(new DefineCampoDecimal());
        jForTotalMateriais.setDocument(new DefineCampoDecimal());
        jForTotalAdicionais.setDocument(new DefineCampoDecimal());
        jForTotalDescontos.setDocument(new DefineCampoDecimal());
        jForTotalOutrosDescontos.setDocument(new DefineCampoDecimal());
        jForValorTotalBruto.setDocument(new DefineCampoDecimal());
        jForValorTotalLiquido.setDocument(new DefineCampoDecimal());

    }

    // método para limpar tela Proposta
    private void limparTelaProposta() {
        jForCdProposta.setText("");
        jTexCdRevisao.setText("");
        jForCdAtendimento.setText("");
        jForDataAtendimento.setText("");
        jForHoraAtendimento.setText("");
        jForCdVistoria.setText("");
        jTexCdVendedor.setText("");
        jTexNomeVendedor.setText("");
        jTexCdTecnico.setText("");
        jTexNomeTecnico.setText("");
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
        jComTipoPedido.setSelectedIndex(0);
        jTexCdTipoPagamento.setText("");
        jTexNomeTipoPagamento.setText("");
        jTexCdCondPagamento.setText("");
        jTexNomeCondPaamento.setText("");
        jForCdPedido.setText("");
        jForTotalMateriais.setText("0.00");
        jForTotalServico.setText("0.00");
        jForTotalAdicionais.setText("0.00");
        jForTotalDescontos.setText("0.00");
        jForTotalOutrosDescontos.setText("0.00");
        jForValorTotalBruto.setText("0.00");
        jForValorTotalLiquido.setText("0.00");
        jTexPrazoExecucao.setText("");
        jTextAreaObsCliente.setText("");
        jForDataEnvio.setText("");
        jComSituacao.setSelectedIndex(0);
        jTexCadPorAtend.setText("");
        jForDataCadAtend.setText("");
        jTexModifPorAtend.setText("");
        jForDataModifiAtend.setText("");
        jTexRegAtualAtend.setText("");
        jTexRegTotalAtend.setText("");
        idxCorProp = 0;
        numRegProp = 0;
        resultProposta = null;
        regCorProposta = null;
        limparTelaLocal();
        liberarCamposProposta();
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
        jForValorDescontosLocal.setText("0,00");
        jForValorOutrosDescLocal.setText("0,00");
        jForValorTotalBrutoLocal.setText("0,00");
        jForValorTotalLiquidoLocal.setText("0,00");
        jTextAreaObsLocal.setText("");
        jForDataEnvio.setText("");
    }

    // método para definir o tipo de pesquisa da proposta
    private void pesquisarProposta() {
        modpro = new Proposta();
        try {
            numRegProp = cpro.pesquisarProposta(sqlProp);
            idxCorProp = 1;
            idxCorLocal = 1;
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Argumento inválido na buscaErr: " + iae.getCause());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Pesquisar os RegistroErr: " + ex);
        }
        if (numRegProp > 0) {
            upRegistrosProposta();
        } else if (JOptionPane.showConfirmDialog(null, "Não há Proposta gerada! Confirma gerar Proposta?")
                == JOptionPane.OK_OPTION) { // senão encontrar proposta cadastrado, inicia os campos da tela com as informações do contato
            operProp = "N";
            criarProposta();
        } else {
            this.dispose();
        }
    }

    // método para pesquisar ItensProposta
    private void pesquisarItens() {
        sqlitp = "select *"
                + " from gcvitemproposta as i"
                + " left outer join gcsmaterial as m on i.cd_material = m.cd_material"
                + " where i.cd_proposta = '" + modpro.getCdProposta()
                + "' and i.cd_revisao = '" + modpro.getCdRevisao()
                + "' and i.cd_local = '" + modlpr.getCdLocal()
                + "' order by i.cd_proposta, i.cd_local, i.sequencia";
        citp = new CItemProposta(conexao);
        itens = new DefaultTableModel();
        try {
            numRegItens = citp.pesquisar(sqlitp);
            idxCorItens += 1;

        } catch (SQLException ex) {
            Logger.getLogger(ManterPropostaComercialRev1.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        if (numRegItens > 0) {
            sequenciaItens = numRegItens;
            jTabItensProposta.setModel(citp.carregarItens());
            jForValorMateriaisLocal.setText(String.valueOf(citp.getTotalMaterial()));
            jForValorServicoLocal.setText(String.valueOf(citp.getTotalServico()));
            jForValorDescontosLocal.setText(String.valueOf(citp.getTotalDesconto()));
            jForValorTotalBrutoLocal.setText(String.valueOf(citp.getTotalMaterial() + citp.getTotalServico()));
            jForValorTotalLiquidoLocal.setText(String.valueOf(modlpr.getValorTotalLiquido()));
            ajustarTabelaItem();
        } else {
            sequenciaItens = 0;
            if (esvaziarTabela) {
                esvaziarTabelaItem();
            }
            esvaziarTabela = false;
        }
    }

    // Método para atualizar os registros da Proposta
    private void upRegistrosProposta() {
        jButPedido.setEnabled(false);
        modpro = new Proposta();
        jTexRegTotalAtend.setText(Integer.toString(numRegProp));
        jTexRegAtualAtend.setText(Integer.toString(idxCorProp));
        modpro = cpro.mostrarProposta(idxCorProp - 1);
        DataSistema dat = new DataSistema();
        HoraSistema hs = new HoraSistema();
        jForCdProposta.setText(modpro.getCdProposta());
        jTexCdRevisao.setText(modpro.getCdRevisao());
        jForCdAtendimento.setText(modpro.getCdAtendimento());
        if (modpro.getCdAtendimento() != "") {
            jForDataAtendimento.setText(dat.getDataConv(Date.valueOf(modpro.getDataAtendimento())));
            jForHoraAtendimento.setText(modpro.getHoraAtendimento());
        }
        jForCdVistoria.setText(modpro.getCdVistoria());
        jTexCdVendedor.setText(modpro.getCdVendedor());
        jTexNomeVendedor.setText(modpro.getNomeVendedor());
        jTexCdTecnico.setText(modpro.getCdTecnico());
        jTexNomeTecnico.setText(modpro.getNomeTecnico());
        jTexNomeRazaoSocial.setText(modpro.getNomeRazaoSocial().trim().toUpperCase());
        String tipoPessoa = modpro.getTipoPessoa();
        switch (tipoPessoa) {
            case "F":
                jComTipoPessoa.setSelectedIndex(1);
                break;
            case "J":
                jComTipoPessoa.setSelectedIndex(1);
                break;
            default:
                jComTipoPessoa.setSelectedIndex(0);
                break;
        }
        jForTelefone.setText(modpro.getTelefone());
        jForCelular.setText(modpro.getCelular());
        jTexEmail.setText(modpro.getEmail());
        jComTipoLogradouro.setSelectedItem(modpro.getTipoLogradouro());
        jTexLogradouro.setText(modpro.getLogradouro());
        jTexNumero.setText(modpro.getNumero());
        jTexComplemento.setText(modpro.getComplemento());
        jTexBairro.setText(modpro.getBairro());
        cdMunicipioIbge = modpro.getCdMunicipioIbge();
        jTexMuncipio.setText(modpro.getNomeMunicipio());
        jTexSiglaUF.setText(modpro.getSiglaUf());
        cdUfIbge = String.valueOf(modpro.getUfIbge());
        jForCep.setText(modpro.getCdCep());
        String tipoEndereco = modpro.getTipoEndereco();
        switch (tipoEndereco) {
            case "C":
                jComTipoEndereco.setSelectedIndex(1);
                break;
            case "R":
                jComTipoEndereco.setSelectedIndex(2);
                break;
            default:
                jComTipoEndereco.setSelectedIndex(0);
                break;
        }
        String tipoPedido = modpro.getTipoPedido();
        switch (tipoPedido) {
            case "A":
                jComTipoPedido.setSelectedIndex(1);
                break;
            case "R":
                jComTipoPedido.setSelectedIndex(2);
                break;
            case "S":
                jComTipoPedido.setSelectedIndex(3);
                break;
            default:
                jComTipoPedido.setSelectedIndex(0);
                break;
        }
        jTexCdTipoPagamento.setText(modpro.getCdTipoPagamento());
        jTexNomeTipoPagamento.setText(modpro.getNomeTipoPagamento());
        jTexCdCondPagamento.setText(modpro.getCdCondPagamento());
        jTexNomeCondPaamento.setText(modpro.getNomeCondPag());
        jForCdPedido.setText(modpro.getCdPedido());
        jTexCadPorAtend.setText(modpro.getUsuarioCadastro());
        jForDataCadAtend.setText(dat.getDataConv(Date.valueOf(modpro.getDataCadastro())));
        jForHoraCadAtend.setText(modpro.getHoraCadastro());
        jTexModifPorAtend.setText(modpro.getUsuarioModificacao());
        jForTotalMateriais.setText(String.valueOf(modpro.getValorProdutos()));
        jForTotalServico.setText(String.valueOf(modpro.getValorServico()));
        jForTotalAdicionais.setText(String.valueOf(modpro.getValorAdicionais()));
        jForTotalDescontos.setText(String.valueOf(modpro.getValorDescontos()));
        jForTotalOutrosDescontos.setText(String.valueOf(modpro.getValorOutrosDescontos()));
        jForValorTotalBruto.setText(String.valueOf(modpro.getValorTotalBruto()));
        jForValorTotalLiquido.setText(String.valueOf(modpro.getValorTotalLiquido()));
        jTexPrazoExecucao.setText(modpro.getPrazoExecucao());
        jTextAreaObsCliente.setText(modpro.getObs());
        if (modpro.getDataEnvio() != null) {
            jForDataEnvio.setText(dat.getDataConv(Date.valueOf(modpro.getDataEnvio())));
        }
        if (modpro.getDataModificacao() != null) {
            jForDataModifiAtend.setText(dat.getDataConv(Date.valueOf(modpro.getDataModificacao())));
            jForHoraAtendimento.setText(modpro.getHoraModificacao());
        }
        String situacao = modpro.getSituacao();
        switch (situacao) {
            case "AP":
                jComSituacao.setSelectedIndex(1);
                break;
            case "CA":
                jComSituacao.setSelectedIndex(2);
                break;
            case "EN":
                jComSituacao.setSelectedIndex(3);
                break;
            case "NF":
                jComSituacao.setSelectedIndex(4);
                break;
            case "NE":
                jComSituacao.setSelectedIndex(5);
                break;
            case "NA":
                jComSituacao.setSelectedIndex(6);
                break;
            default:
                jComSituacao.setSelectedIndex(0);
        }
        if ("EN".equals(modpro.getSituacao()) || "AP".equals(modpro.getSituacao())) {
            jButPedido.setEnabled(true);
        }
        // Habilitando/Desabilitando botões de navegação de registros
        if (numRegProp > idxCorProp) {
            jButProximo.setEnabled(true);
        } else {
            jButProximo.setEnabled(false);
        }
        if (idxCorProp > 1) {
            jButAnterior.setEnabled(true);
        } else {
            jButAnterior.setEnabled(false);
        }
        if (cpro.getNumRegLpr() > 0) {
            if (idxCorLocalAtualizado > 0) {
                idxCorLocal = idxCorLocalAtualizado;
                idxCorLocalAtualizado = 0;
            } else {
                idxCorLocal = 1;
            }
            numRegLocal = cpro.getNumRegLpr();
            upRegistrosLocal();
        }
    }

    // Método para atualizar os registros de proposta
    private void upRegistrosLocal() {
        jTexRegTotalLocal.setText(Integer.toString(numRegLocal));
        jTexRegAtualLocal.setText(Integer.toString(idxCorLocal));
        modlpr = cpro.mostrarLocalProposta(idxCorLocal - 1);
        jTexNomeLocal.setText(modlpr.getNomeLocal());
        jForMetragemArea.setText(String.valueOf(modlpr.getMetragemArea()));
        jForPercPerda.setText(String.valueOf(modlpr.getPercPerda()));
        jComTipoPiso.setSelectedItem(modlpr.getTipoPiso());
        jTexEssenciaMadeira.setText(modlpr.getCdEssencia());
        jTexNomeEssenciaMadeira.setText(modlpr.getNomeEssenciaMadeira());
        jComTipoRodape.setSelectedItem(modlpr.getTipoRodape());
        jForMetragemRodape.setText(String.valueOf(modlpr.getMetragemRodape()));
        jForLargura.setText(String.valueOf(modlpr.getLargura()));
        jTexComprimento.setText(String.valueOf(modlpr.getComprimento()));
        jForEspessura.setText(String.valueOf(modlpr.getEspessura()));
        if ("S".equals(modlpr.getTingimento())) {
            jCheTingimento.setSelected(true);
        } else {
            jCheTingimento.setSelected(false);
        }
        if ("S".equals(modlpr.getClareamento())) {
            jCheClareamento.setSelected(true);
        } else {
            jCheClareamento.setSelected(false);
        }
        jTexCdTipoVerniz.setText(modlpr.getCdTipolVerniz());
        jTexNomeTipoVerniz.setText(modlpr.getNomeTipoVerniz());
        jForValorAdicionaisLocal.setText(String.valueOf(modlpr.getValorAdicionais()));
        jForValorOutrosDescLocal.setText(String.valueOf(modlpr.getValorOutrosDescontos()));
        DataSistema dat = new DataSistema();
        jTexCadPorLocal.setText(modlpr.getUsuarioCadastro());
        jForDataCadLocal.setText(dat.getDataConv(Date.valueOf(modlpr.getDataCadastro())));
        jForHoraCadLocal.setText(modlpr.getHoraCadastro());
        jTexModifPorLocal.setText(modlpr.getUsuarioModificacao());
        if (modlpr.getDataModificacao() != null) {
            jForDataModifLocal.setText(dat.getDataConv(Date.valueOf(modlpr.getDataModificacao())));
            jForHoraModifLocal.setText(modlpr.getHoraModificacao());
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

    //Método para Atualizar totais
    private void atualizarTotaisProposta(boolean upTela) {
        int numReg = 0;
        String sqlpro = "select p.cd_proposta as Proposta,"
                + " p.cd_revisao as Revisao,"
                + " sum(p.valor_produtos) as 'Total Produtos',"
                + " sum(p.valor_servicos) as 'Total Serviços',"
                + " sum(p.valor_adicionais) as Adicionais,"
                + " sum(p.valor_descontos) as Descontos,"
                + " sum(p.valor_outros_descontos) as 'Outros Descontos',"
                + " sum(p.valor_total_bruto) as 'Total Proposta Bruto',"
                + " sum(p.valor_total_liquido) as 'Total Proposta Liquido'"
                + " from gcvlocalproposta as p"
                + " where p.cd_proposta = '" + modpro.getCdProposta()
                + "' and p.cd_revisao = '" + modpro.getCdRevisao()
                + "'";
        try {
            numReg = cpro.pesquisarTotaisProposta(sqlpro);

        } catch (SQLException ex) {
            Logger.getLogger(ManterPropostaComercial.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        if (numReg > 0) {
            cpro.atualizarTotalProposta(modpro);
            /*
            JOptionPane.showMessageDialog(null, "Atualizado Totais da Proposta:\nProposta: " + modpro.getCdProposta() + "\nMateriais: " + modpro.getValorProdutos()
                    + "\nServiço: " + modpro.getValorServico() + "\nAdicionais: " + modpro.getValorAdicionais()
                    + "\nDescontos: " + modpro.getValorDescontos() + "\nOutros Descontos: "
                    + modpro.getValorOutrosDescontos() + "\nTotal Bruto: " + modpro.getValorTotalBruto()
                    + "\nTotal Liquido: " + modpro.getValorTotalLiquido());
             */
            jForTotalMateriais.setText(String.valueOf(modpro.getValorProdutos()));
            jForTotalServico.setText(String.valueOf(modpro.getValorServico()));
            jForTotalMateriais.setText(String.valueOf(modpro.getValorProdutos()));
            jForTotalAdicionais.setText(String.valueOf(modpro.getValorAdicionais()));
            jForTotalDescontos.setText(String.valueOf(modpro.getValorDescontos()));
            jForTotalOutrosDescontos.setText(String.valueOf(modpro.getValorOutrosDescontos()));
            jForValorTotalBruto.setText(String.valueOf(modpro.getValorTotalBruto()));
            jForValorTotalLiquido.setText(String.valueOf(modpro.getValorTotalLiquido()));
        } else {
            JOptionPane.showMessageDialog(null, "Não foi possível encontrar registros para atualização!");
        }
    }

    //Método para ajustar tabela itemProposta
    private void ajustarTabelaItem() {
        jTabItensProposta.getColumnModel().getColumn(0).setWidth(5);
        jTabItensProposta.getColumnModel().getColumn(0).setPreferredWidth(5);
        jTabItensProposta.getColumnModel().getColumn(1).setWidth(60);
        jTabItensProposta.getColumnModel().getColumn(1).setPreferredWidth(60);
        jTabItensProposta.getColumnModel().getColumn(2).setWidth(220);
        jTabItensProposta.getColumnModel().getColumn(2).setPreferredWidth(220);
        jTabItensProposta.getColumnModel().getColumn(3).setWidth(10);
        jTabItensProposta.getColumnModel().getColumn(3).setPreferredWidth(10);
        jTabItensProposta.getColumnModel().getColumn(4).setWidth(30);
        jTabItensProposta.getColumnModel().getColumn(4).setPreferredWidth(30);
        jTabItensProposta.getColumnModel().getColumn(5).setWidth(40);
        jTabItensProposta.getColumnModel().getColumn(5).setPreferredWidth(40);
        jTabItensProposta.getColumnModel().getColumn(6).setWidth(40);
        jTabItensProposta.getColumnModel().getColumn(6).setPreferredWidth(40);
        jTabItensProposta.getColumnModel().getColumn(7).setWidth(40);
        jTabItensProposta.getColumnModel().getColumn(7).setPreferredWidth(40);
        jTabItensProposta.getColumnModel().getColumn(8).setWidth(40);
        jTabItensProposta.getColumnModel().getColumn(8).setPreferredWidth(40);
        jTabItensProposta.getColumnModel().getColumn(9).setWidth(40);
        jTabItensProposta.getColumnModel().getColumn(9).setPreferredWidth(40);
    }

    //Método para criar um listener na tabela
    private void monitoraLinhaItens() {
        VerificarTecla vt = new VerificarTecla();
        jTabItensProposta.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                linhaItem = jTabItensProposta.getSelectedRow();
            }
        });

        // capturando a tecla digitada
        jTabItensProposta.addKeyListener(new KeyListener() {
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
                    Logger.getLogger(ManterPropostaComercialRev1.class.getName()).log(Level.SEVERE, null, ex);
                }
                String tecla = vt.VerificarTecla(e).toUpperCase();
                if ("ABAIXO".equals(tecla) && !inclusao && "A".equals(operLocal)) {
                    if (jTabItensProposta.getSelectedRow() == jTabItensProposta.getRowCount() - 1) {
                        jButNovoLocal.setEnabled(!ISBOTAO);
                        jButLocalAnterior.setEnabled(!ISBOTAO);
                        jButLocalProximo.setEnabled(!ISBOTAO);
                        moditp = new ItemProposta();
                        inclusao = true;
                        jTabItensProposta.setModel(citp.adicionarLinha());
                        linhaItem = jTabItensProposta.getSelectedRow() + 1;
                        upValue();
                        ajustarTabelaItem();
                    }
                }

                if ("F5".equals(tecla) && inclusao) {
                    if (jTabItensProposta.getSelectedColumn() == 1) {
                        PesquisarMateriais zoom = new PesquisarMateriais(new JFrame(), true, "P", "R", conexao, true);
                        zoom.setVisible(true);
                        moditp.setSequencia(Integer.parseInt(String.format("%s", jTabItensProposta.getValueAt(linhaItem, 0))));
                        moditp.setCdMaterial(zoom.getCdMaterial());
                        moditp.setNomeMaterial(zoom.getNomeMaterial());
                        moditp.setCdUnidmedida(zoom.cdUnidMedida());
                        moditp.setQuantidade(qtde * percPerda);
                        if ("4".equals(zoom.getTipoProduto())) {
                            moditp.setValorUnitBruto(zoom.getValorUnitBruto());
                            moditp.setValorUnitLiquido(zoom.getValorUnitLiquido());
                            moditp.setTipoItem("R");
                            if (zoom.getValorUnitBruto() == 0) {
                                JOptionPane.showMessageDialog(null, "Item sem preço cadastrado!");
                            }
                            moditp.setValorTotalItemBruto(moditp.getQuantidade() * moditp.getValorUnitBruto());
                            descAlcada = zoom.getDescAlcada();
                            valorUnitBrt = zoom.getValorUnitBruto();
                            valorUnitLiq = zoom.getValorUnitLiquido();
                        } else {
                            moditp.setValorUnitBruto(zoom.getValorServico());
                            moditp.setTipoItem("S");
                            if (zoom.getValorServico() == 0) {
                                JOptionPane.showMessageDialog(null, "Item sem preço cadastrado!");
                            }
                            moditp.setValorTotalItemBruto(moditp.getQuantidade() * moditp.getValorUnitBruto());
                            valorServico = zoom.getValorServico();
                        }
                        upValue();
                    }
                    if (jTabItensProposta.getSelectedColumn() == 3) {
                        PesquisarUnidadesMedida zoom = new PesquisarUnidadesMedida(new JFrame(), true, "P", conexao);
                        zoom.setVisible(inclusao);
                        moditp.setCdUnidmedida(zoom.getSelec1());
                        upValue();
                    }
                    ajustarTabelaItem();
                }
                if ("ESCAPE".equals(tecla) && inclusao) {
                    jTabItensProposta.setModel(citp.excluirLinha(jTabItensProposta.getSelectedRow()));
                    jTabItensProposta.requestFocus();
                    inclusao = false;
                    ajustarTabelaItem();
                }
                if ("ENTER".equals(tecla) && (inclusao || "A".equals(operItens))) {
                    if (inclusao) {
                        inclusao = false;
                        operItens = "N";
                    } else {
                        operItens = "A";
                    }
                    salvarLinha();
                }
                if ("GUIA".equals(tecla) && "A".equals(operLocal)) {
                    operItens = "A";
                    dowValue();
                    upValue();
                }

                if ("ACIMA".equals(tecla) && inclusao) {
                    inclusao = false;
                    operItens = "N";
                    salvarLinha();
                }
                if ("EXCLUIR".equals(tecla) && "A".equals(operLocal) && !inclusao) {
                    if (JOptionPane.showConfirmDialog(null, "Confirma exclusão do Item?") == JOptionPane.OK_OPTION) {
                        int numReg = 0;
                        moditp = new ItemProposta();
                        dowValue();
                        CItemProposta citp = new CItemProposta(conexao);
                        String sq = "select * from gcvitemproposta where cd_proposta = '" + modpro.getCdProposta()
                                + "' and cd_revisao = '" + moditp.getCdRevisao()
                                + "' and cd_local = '" + moditp.getCdLocal()
                                + "' and sequencia = '" + moditp.getSequencia()
                                + "'";
                        try {
                            numReg = citp.pesquisar(sq);
                        } catch (SQLException ex) {
                            Logger.getLogger(ManterPropostaComercialRev1.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (numReg > 0) {
                            citp.mostrarPesquisa(moditp, 0);
                        }
                        try {
                            itpdao = new ItemPropostaDAO(conexao);
                            itpdao.excluir(moditp);
                            if ("R".equals(moditp.getTipoItem())) {
                                modlpr.setValorProdutos(modlpr.getValorProdutos() - moditp.getValorTotalItemBruto());
                            } else {
                                modlpr.setValorServico(modlpr.getValorServico() - moditp.getValorTotalItemBruto());
                                //JOptionPane.showConfirmDialog(null, "Excluindo valor do Serviço!\nValor Serico Atual: " + modlpr.getValorServico()
                                //        + "\nValor Serviço a Excluir: " + moditp.getValorTotalItemBruto());
                            }
                            modlpr.setValorTotalBruto(modlpr.getValorTotalBruto() - moditp.getValorTotalItemBruto());
                            modlpr.setValorTotalLiquido(modlpr.getValorTotalLiquido() - moditp.getValorTotalItemLiquido());
                            modlpr.setValorDescontos(modlpr.getValorDescontos() - moditp.getValorDescontos());
                            lpr = modlpr;
                            operLocal = "A";
                            gravarLocalBanco(false);
                            esvaziarTabela = true;
                            pesquisarItens();
                            jForValorServicoLocal.setText(String.valueOf(modlpr.getValorServico()));
                            jForValorMateriaisLocal.setText(String.valueOf(modlpr.getValorProdutos()));
                            jForValorAdicionaisLocal.setText(String.valueOf(modlpr.getValorAdicionais()));
                            jForValorDescontosLocal.setText(String.valueOf(modlpr.getValorDescontos()));
                            jForValorOutrosDescLocal.setText(String.valueOf(modlpr.getValorOutrosDescontos()));
                            jForValorTotalBrutoLocal.setText(String.valueOf(modlpr.getValorTotalBruto()));
                            jForValorTotalLiquidoLocal.setText(String.valueOf(modlpr.getValorTotalLiquido()));
                        } catch (SQLException ex) {
                            Logger.getLogger(ManterPropostaComercialRev1.class
                                    .getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }

            private void upValue() {
                jTabItensProposta.setModel(citp.upNovaLinha(moditp, linhaItem, 1, 2, 3, 4, 5, 6, 7, 8, 9, "I"));
            }

            private void dowValue() {
                moditp.setCdProposta(modpro.getCdProposta());
                moditp.setCdRevisao(modpro.getCdRevisao());
                moditp.setCdLocal(modlpr.getCdLocal());
                moditp.setSequencia(Integer.parseInt(String.format("%s", jTabItensProposta.getValueAt(linhaItem, 0))));
                moditp.setCdMaterial(String.format("%s", jTabItensProposta.getValueAt(linhaItem, 1)));
                moditp.setNomeMaterial(String.format("%s", jTabItensProposta.getValueAt(linhaItem, 2)));
                moditp.setCdUnidmedida(String.format("%s", jTabItensProposta.getValueAt(linhaItem, 3)));
                try {
                    moditp.setQuantidade(ftq.parse(String.format("%s", jTabItensProposta.getValueAt(linhaItem, 4))).doubleValue());
                    moditp.setValorUnitBruto(ftv.parse(String.format("%s", jTabItensProposta.getValueAt(linhaItem, 5))).doubleValue());
                    moditp.setPercDesconto(ftp.parse(String.format("%s", jTabItensProposta.getValueAt(linhaItem, 6))).doubleValue());
                    moditp.setValorDescontos(ftv.parse(String.format("%s", jTabItensProposta.getValueAt(linhaItem, 7))).doubleValue());
                    moditp.setValorTotalItemBruto(moditp.getQuantidade() * moditp.getValorUnitBruto());
                    moditp.setValorUnitLiquido(moditp.getValorUnitBruto() * (1 - moditp.getPercDesconto() / 100));
                    moditp.setValorTotalItemLiquido(moditp.getQuantidade() * moditp.getValorUnitLiquido());
                } catch (ParseException ex) {
                    Logger.getLogger(ManterPropostaComercialRev1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private void salvarLinha() {
                salvarItem();
                pesquisarItens();
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
                    if (JOptionPane.showConfirmDialog(null, "Confirma sair da Proposta?") == JOptionPane.OK_OPTION) {
                        salvarLocal(false);
                        salvarProposta(false);
                        dispose();
                    }
                } else {
                    dispose();
                }
            }
        });
    }

    //Bloquear os campos da tela Proposta
    private void bloquearCamposProposta() {
        jForCdProposta.setEditable(false);
        jTexCdVendedor.setEditable(false);
        jTexCdTecnico.setEditable(false);
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
        jComTipoPedido.setEditable(false);
        jTexCdTipoPagamento.setEditable(false);
        jTexCdTipoPagamento.setEnabled(false);
        jTexCdCondPagamento.setEditable(false);
        jTexCdCondPagamento.setEnabled(false);
        jTexPrazoExecucao.setEditable(false);
        jTexPrazoExecucao.setEnabled(false);
        jTextAreaObsCliente.setEditable(false);
        jForTotalOutrosDescontos.setEditable(false);
        jForTotalOutrosDescontos.setEnabled(false);
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
        jForValorOutrosDescLocal.setEditable(false);
        jForValorOutrosDescLocal.setEnabled(false);
    }

    //Liberar os campos da tela Proposta para atualização
    private void liberarCamposProposta() {
        jTexCdVendedor.setEditable(true);
        jTexCdVendedor.setEnabled(true);
        jTexCdTecnico.setEditable(true);
        jTexCdTecnico.setEnabled(true);
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
        jComTipoEndereco.setEditable(true);
        jComTipoPedido.setEditable(true);
        jTexCdTipoPagamento.setEditable(true);
        jTexCdTipoPagamento.setEnabled(true);
        jTexCdCondPagamento.setEditable(true);
        jTexCdCondPagamento.setEnabled(true);
        jTexPrazoExecucao.setEditable(true);
        jTexPrazoExecucao.setEnabled(true);
        jTextAreaObsCliente.setEditable(true);
        jForTotalOutrosDescontos.setEditable(true);
        jForTotalOutrosDescontos.setEnabled(true);
        jComSituacao.setEditable(true);
        jComSituacao.setEditable(true);
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
        jForValorOutrosDescLocal.setEditable(true);
        jForValorOutrosDescLocal.setEnabled(true);
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
        jTexCdTipoPagamento.setText(zoom.getCdTipoPagamento());
        jTexNomeTipoPagamento.setText(zoom.getNomeTipoPagamento());
    }

    // método para dar zoom no campo CondPagamento
    private void zoomCondicaoPagamento() {
        PesquisarCondicaoPagamento zoom = new PesquisarCondicaoPagamento(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdCondPagamento.setText(zoom.getCdCondPag());
        jTexNomeCondPaamento.setText(zoom.getNomeCondPag());
    }

    // método par dar zoom no campo Vendedor
    private void zoomVendedor() {
        PesquisarTecnicos zoom = new PesquisarTecnicos(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdVendedor.setText(zoom.getSelecao1());
        jTexNomeVendedor.setText(zoom.getSelecao2());
    }

    // método para dar zoom no campo Técnico
    private void zoomTecnico() {
        PesquisarTecnicos zoom = new PesquisarTecnicos(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdTecnico.setText(zoom.getSelecao1());
        jTexNomeTecnico.setText(zoom.getSelecao2());
    }

    // metodo para salvar registro Proposta
    private void salvarProposta(boolean upTela) {
        pro = new Proposta();
        dat = new DataSistema();
        if (jTexNomeRazaoSocial.getText().isEmpty() || jComSituacao.getSelectedItem().toString().substring(0, 1) == " ") {
            JOptionPane.showMessageDialog(null, "Os campo Nome/Razão Social e Situacao precisam ser preenchidos corretamente!");
        } else {
            pro.setCdProposta(jForCdProposta.getText());
            pro.setCdRevisao(jTexCdRevisao.getText());
            pro.setCdAtendimento(jForCdAtendimento.getText());
            pro.setDataAtendimento(dat.getDataConv(jForDataAtendimento.getText()));
            pro.setHoraAtendimento(jForHoraAtendimento.getText());
            pro.setCdVistoria(jForCdVistoria.getText());
            pro.setCdVendedor(jTexCdVendedor.getText());
            pro.setCdTecnico(jTexCdTecnico.getText());
            pro.setNomeRazaoSocial(jTexNomeRazaoSocial.getText().trim().toUpperCase());
            pro.setTipoPessoa(jComTipoPessoa.getSelectedItem().toString().substring(0, 1));
            pro.setTelefone(jForTelefone.getText());
            pro.setCelular(jForCelular.getText());
            pro.setEmail(jTexEmail.getText().trim());
            pro.setTipoLogradouro(jComTipoLogradouro.getSelectedItem().toString().trim());
            pro.setLogradouro(jTexLogradouro.getText().trim().toUpperCase());
            pro.setNumero(jTexNumero.getText().trim().toUpperCase());
            pro.setComplemento(jTexComplemento.getText().trim().toUpperCase());
            pro.setBairro(jTexBairro.getText().trim().toUpperCase());
            pro.setCdMunicipioIbge(cdMunicipioIbge.trim());
            if (!jTexSiglaUF.getText().isEmpty()) {
                pro.setSiglaUf(jTexSiglaUF.getText().toUpperCase().toString().substring(jTexSiglaUF.getText().trim().length() - 2, 2));
            }
            pro.setCdCep(jForCep.getText().toString().trim());
            pro.setTipoEndereco(jComTipoEndereco.getSelectedItem().toString().substring(0, 1));
            pro.setTipoPedido(jComTipoPedido.getSelectedItem().toString().substring(0, 1));
            pro.setCdTipoPagamento(jTexCdTipoPagamento.getText());
            pro.setCdCondPagamento(jTexCdCondPagamento.getText());
            pro.setCdPedido(jForCdPedido.getText());
            pro.setValorServico(modpro.getValorServico());
            pro.setValorProdutos(modpro.getValorProdutos());
            pro.setValorAdicionais(modpro.getValorAdicionais());
            pro.setValorDescontos(modpro.getValorDescontos());
            pro.setValorOutrosDescontos(modpro.getValorOutrosDescontos());
            pro.setValorTotalBruto(modpro.getValorTotalBruto());
            pro.setValorTotalLiquido(modpro.getValorTotalLiquido());
            pro.setPrazoExecucao(jTexPrazoExecucao.getText().trim());
            pro.setObs(jTextAreaObsCliente.getText());
            String dataEnvio = jForDataEnvio.getText().replace("/", "");
            if (!dataEnvio.trim().isEmpty()) {
                pro.setDataEnvio(dat.getDataConv(jForDataEnvio.getText()));
            }
            pro.setUsuarioCadastro(su.getUsuarioConectado());
            pro.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 2));
            gravarPropostaBanco();
            controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
        }
    }

    private void gravarPropostaBanco() {
        dat.setData(data);
        data = dat.getData();
        HoraSistema hs = new HoraSistema();
        sqlProp = "SELECT * FROM GCVPROPOSTA WHERE CD_PROPOSTA = '" + pro.getCdProposta()
                + "' AND CD_REVISAO = " + pro.getCdRevisao();
        try {
            PropostaDAO prodao = new PropostaDAO(conexao);
            if ("N".equals(operProp)) {
                CBuscarSequencia bs = new CBuscarSequencia(su, DATASOURCE, 8);
                pro.setCdProposta(bs.getRetorno());
                pro.setUsuarioCadastro(su.getUsuarioConectado());
                pro.setDataCadastro(data);
                pro.setHoraCadastro(hs.getHora());
                prodao.adicionar(pro);
                modate.setCdProposta(pro.getCdProposta());
                modate.setAtualizacao(true);
            } else {
                pro.setUsuarioModificacao(su.getUsuarioConectado());
                pro.setDataModificacao(data);
                pro.setHoraModificacao(hs.getHora());
                prodao.atualizar(pro);
            }
            if (fecharProposta) {
                NewPropostaDAO nprodao = new NewPropostaDAO(conexao);
                nprodao.atualizarProposta(pro);
                fecharProposta = false;
            }
            //modate.setAtualizacao(true);
        } catch (SQLException ex) {
            Logger.getLogger(ManterPropostaComercialRev1.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        operProp = "";
    }

    // Método para salvar registro Local
    private void salvarLocal(boolean upTela) {
        dat = new DataSistema();
        lpr = new LocalProposta();
        lpr.setNomeLocal(jTexNomeLocal.getText().trim().toUpperCase());
        try {
            lpr.setMetragemArea(formato.parse(jForMetragemArea.getText()).doubleValue());
            lpr.setPercPerda(formato.parse(jForPercPerda.getText()).doubleValue());
            lpr.setMetragemRodape(formato.parse(jForMetragemRodape.getText()).doubleValue());
            lpr.setLargura(formato.parse(jForLargura.getText()).doubleValue());
            lpr.setComprimento(jTexComprimento.getText());
            lpr.setEspessura(formato.parse(jForEspessura.getText()).doubleValue());
            lpr.setValorServico(formato.parse(jForValorServicoLocal.getText()).doubleValue());
            lpr.setValorProdutos(formato.parse(jForValorMateriaisLocal.getText()).doubleValue());
            lpr.setValorAdicionais(formato.parse(jForValorAdicionaisLocal.getText()).doubleValue());
            lpr.setValorDescontos(formato.parse(jForValorDescontosLocal.getText()).doubleValue());
            lpr.setValorOutrosDescontos(formato.parse(jForValorOutrosDescLocal.getText()).doubleValue());
            lpr.setValorTotalBruto(formato.parse(jForValorTotalBrutoLocal.getText()).doubleValue());
            lpr.setValorTotalLiquido(lpr.getValorTotalLiquido());

        } catch (ParseException ex) {
            Logger.getLogger(ManterPropostaComercialRev1.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        lpr.setTipoPiso(jComTipoPiso.getSelectedItem().toString());
        lpr.setTipoRodape(jComTipoRodape.getSelectedItem().toString());
        if (jCheTingimento.isSelected()) {
            lpr.setTingimento("S");
        } else {
            lpr.setTingimento("N");
        }
        if (jCheClareamento.isSelected()) {
            lpr.setClareamento("S");
        } else {
            lpr.setClareamento("N");
        }
        lpr.setCdTipoVerniz(jTexCdTipoVerniz.getText());
        lpr.setCdEssencia(jTexEssenciaMadeira.getText());
        lpr.setObs(jTextAreaObsLocal.getText());
        dat.setData(data);
        data = dat.getData();
        lpr.setSituacao("AA");
        gravarLocalBanco(true);
        bloquearCamposProposta();
        atualizarTotaisProposta(upTela);
        pro = modpro;
        operProp = "A";
        gravarPropostaBanco();
        operProp = "";
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
    }

    private void gravarLocalBanco(boolean upTela) {
        LocalPropostaDAO ltrdao = null;
        sqlLocal = "SELECT * FROM GCVLOCALPROPOSTA WHERE CD_PROPOSTA = '" + modpro.getCdProposta()
                + "' AND CD_REVISAO = '" + modpro.getCdRevisao()
                + "'";
        try {
            HoraSistema hs = new HoraSistema();
            ltrdao = new LocalPropostaDAO(conexao);
            if ("N".equals(operLocal)) {
                lpr.setCdAtendimento(modpro.getCdAtendimento());
                lpr.setCdProposta(modpro.getCdProposta());
                lpr.setCdRevisao(modpro.getCdRevisao());
                lpr.setCdLocal(numRegLocal + 1);
                lpr.setUsuarioCadastro(su.getUsuarioConectado());
                lpr.setDataCadastro(data);
                lpr.setHoraCadastro(hs.getHora());
                ltrdao.adicionar(lpr);
            } else {
                lpr.setCdAtendimento(jForCdAtendimento.getText());
                lpr.setCdProposta(jForCdProposta.getText());
                lpr.setCdRevisao(jTexCdRevisao.getText());
                lpr.setCdLocal(modlpr.getCdLocal());
                lpr.setUsuarioModificacao(su.getUsuarioConectado());
                lpr.setDataModificacao(data);
                lpr.setHoraModificacao(hs.getHora());
                ltrdao.atualizar(lpr);
                //Pane.showMessageDialog(null, "Atualizando Local!\n cdProposta: " + lpr.getCdProposta()
                //        + "\ncdLocal: " + lpr.getCdLocal() + "\nValor Serviço: " + lpr.getValorServico());
            }
            operLocal = "";
            esvaziarTabela = false;
        } catch (SQLException ex) {
            Logger.getLogger(ManterPropostaComercialRev1.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para salvar ItemProposta
    private void salvarItem() {
        if (moditp.getSequencia() != 0 && moditp.getCdMaterial() != null) {
            dat = new DataSistema();
            itp = new ItemProposta();
            String data = null;
            itp.setCdAtendimento(modpro.getCdAtendimento());
            itp.setCdProposta(modpro.getCdProposta());
            itp.setCdRevisao(modpro.getCdRevisao());
            itp.setCdLocal(modlpr.getCdLocal());
            itp.setSequencia(moditp.getSequencia());
            itp.setCdMaterial(moditp.getCdMaterial());
            itp.setCdUnidmedida(moditp.getCdUnidmedida());
            itp.setQuantidade(moditp.getQuantidade());
            itp.setValorUnitBruto(moditp.getValorUnitBruto());
            itp.setValorUnitLiquido(moditp.getValorUnitLiquido());
            itp.setPercDesconto(moditp.getPercDesconto());
            itp.setValorDescontos(moditp.getValorTotalItemBruto() / 100 * moditp.getPercDesconto());
            itp.setValorTotalItemBruto(moditp.getValorTotalItemBruto());
            itp.setValorTotalItemLiquido(moditp.getValorTotalItemBruto() * (1 - moditp.getPercDesconto() / 100));
            itp.setTipoItem(moditp.getTipoItem());
            itp.setObsItem(moditp.getObsItem());
            dat.setData(data);
            data = dat.getData();
            itp.setSituacao("AA");
            gravarItemBanco();
        }
    }

    private void gravarItemBanco() {
        ItemPropostaDAO itpdao = null;
        try {
            HoraSistema hs = new HoraSistema();
            itpdao = new ItemPropostaDAO(conexao);
            if ("N".equals(operItens)) {
                itp.setUsuarioCadastro(su.getUsuarioConectado());
                itp.setDataCadastro(data);
                itp.setHoraCadastro(hs.getHora());
                itpdao.adicionar(itp);
                if ("R".equals(moditp.getTipoItem())) {
                    modlpr.setValorProdutos(modlpr.getValorProdutos() + moditp.getValorTotalItemBruto());
                    //modpro.setValorProdutos(modpro.getValorProdutos() + moditp.getValorTotalItemBruto());
                } else if ("S".equals(moditp.getTipoItem())) {
                    modlpr.setValorServico(modlpr.getValorServico() + moditp.getValorTotalItemBruto());
                    //modpro.setValorServico(modpro.getValorServico() + moditp.getValorTotalItemBruto());
                }
                modlpr.setValorTotalBruto(modlpr.getValorProdutos() + modlpr.getValorServico());
                modlpr.setValorTotalLiquido(modlpr.getValorTotalBruto() - (modlpr.getValorDescontos() + modlpr.getValorOutrosDescontos()));
                //modpro.setValorDescontos(modpro.getValorDescontos() + moditp.getValorDescontos());
                //modpro.setValorTotalBruto(modpro.getValorProdutos() + modpro.getValorServico());
            } else {
                itp.setUsuarioModificacao(su.getUsuarioConectado());
                itp.setDataModificacao(data);
                itp.setHoraModificacao(hs.getHora());
                itpdao.atualizar(itp);
            }
            lpr = modlpr;
            operLocal = "A";
            jForValorMateriaisLocal.setText(String.valueOf(modlpr.getValorProdutos()));
            jForValorServicoLocal.setText(String.valueOf(modlpr.getValorServico()));
            jForValorAdicionaisLocal.setText(String.valueOf(modlpr.getValorAdicionais()));
            jForValorDescontosLocal.setText(String.valueOf(modlpr.getValorDescontos()));
            jForValorOutrosDescLocal.setText(String.valueOf(modlpr.getValorOutrosDescontos()));
            jForValorTotalBrutoLocal.setText(String.valueOf(modlpr.getValorTotalBruto()));
            jForValorTotalLiquidoLocal.setText(String.valueOf(modlpr.getValorTotalLiquido()));
            operItens = "";
        } catch (SQLException ex) {
            Logger.getLogger(ManterPropostaComercialRev1.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }

    // Método para Excluir Proposta
    private void excluirAtend() {
        if (!jForCdProposta.getText().isEmpty()) {
            try {
                Proposta cc = new Proposta();
                cc.setCdProposta(jForCdProposta.getText());
                cc.setCdRevisao(jTexCdRevisao.getText());
                PropostaDAO ccDAO = new PropostaDAO(conexao);
                ccDAO.excluir(cc);
                limparTelaProposta();
                pesquisarProposta();
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
                LocalProposta cc = new LocalProposta();
                cc.setCdProposta(jForCdProposta.getText());
                cc.setCdRevisao(jTexCdRevisao.getText());
                cc.setCdLocal(modlpr.getCdLocal());
                LocalPropostaDAO ccDAO = new LocalPropostaDAO(conexao);
                ccDAO.excluir(cc);
                limparTelaLocal();
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Não foi possível excluir o registro da tela");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral na exclusão do registro!");
            }
        }
    }

    // Método para calcular pagamento
    private void calcularPagamento() {

    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    // méto para limpara a tabela de itens
    private void esvaziarTabelaItem() {
        citp = new CItemProposta(conexao);
        jTabItensProposta.setModel(new JTable().getModel());
        jTabItensProposta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabItensProposta
                .setModel(new javax.swing.table.DefaultTableModel(
                        new Object[][]{
                            {null, null, null, null, null, null, null, null, null, null}
                        },
                        new String[]{
                            "Seq.", "Cod.", "Descrição", "U.M", "Qtde", "Pr. Unit.", "Perc.Desc.", "Val.Desc.", "Tot.Item.Bruto", "Tot.Item.Liquido"
                        }
                ) {
                    Class[] types = new Class[]{
                        java.lang.Integer.class,
                        java.lang.String.class,
                        java.lang.String.class,
                        java.lang.String.class,
                        java.lang.Double.class,
                        java.lang.Double.class,
                        java.lang.Double.class,
                        java.lang.Double.class,
                        java.lang.Double.class,
                        java.lang.Double.class

                    };
                    boolean[] canEdit = new boolean[]{
                        false, false, false, false, false, false, false, false, false, false, false
                    };

                    public Class getColumnClass(int columnIndex) {
                        return types[columnIndex];
                    }

                    public boolean isCellEditable(int rowIndex, int columnIndex) {
                        return canEdit[columnIndex];
                    }
                });
    }

    /**
     * Metodo para controlar os botoes da tela
     *
     * @param bEd Botão Editar
     * @param bSa Botão Salvar
     * @param bCa Botão Cancelar
     * @param bEx Botão Excluir
     * @param bNo Botão Novo Local
     * @param bCl Botão Sair
     */
    private void controleBotoes(boolean bEd, boolean bSa, boolean bCa, boolean bEx, boolean bNo, boolean bCl) {
        jButEditar.setEnabled(bEd);
        jButSalvar.setEnabled(bSa);
        jButCancelar.setEnabled(bCa);
        jButExcluir.setEnabled(bEx);
        jButNovoLocal.setEnabled(bNo);
        jButSair.setEnabled(bCl);
    }

    // método para criar propotaComercial
    private void criarProposta() {
        try {
            cpro.criarProposta(modate);
        } catch (SQLException ex) {
            Logger.getLogger(ManterPropostaComercialRev1.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisarProposta();
    }

    // método para imprimir proposta comercial
    private void imprimirProposta() {
        emp = new Empresa();
        CEmpresa cemp = new CEmpresa(conexao);
        String sqlemp = "Select * from pgsempresa as e where e.situacao = 'A' and e.tipo_empresa = 'Emp'";
        try {
            int numReg = cemp.pesquisar(sqlemp);
            if (numReg > 0) {
                cemp.mostrarPesquisa(emp, 0);
                new CReportPropostaComercial().abrirRelatorio(modpro.getCdProposta(), modpro.getCdProposta(), modpro.getCdRevisao(), su, conexao, emp, pg, "RELATORIO", modpro);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManterPropostaComercialRev1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // método para enviar email proposta comercial
    private void enviarProposta() {
        if (modpro.getDataEnvio() == null) {
            dat = new DataSistema();
            dat.setData(data);
            data = dat.getData();
            modpro.setDataEnvio(data);
            modpro.setSituacao("EN");
            operProp = "A";
            pro = modpro;
            sqlProp = "select * from gcvproposta where cd_proposta = '" + modpro.getCdProposta()
                    + "' and cd_revisao = " + modpro.getCdRevisao();
            try {
                gravarPropostaBanco();
                jForDataEnvio.setText(modpro.getDataEnvio());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Não foi possível gravar a Proposta Comercial Para envio!");
            }
        }
        emp = new Empresa();
        CEmpresa cemp = new CEmpresa(conexao);
        String sqlemp = "Select * from pgsempresa as e where e.situacao = 'A' and e.tipo_empresa = 'Emp'";
        try {
            int numReg = cemp.pesquisar(sqlemp);
            if (numReg > 0) {
                cemp.mostrarPesquisa(emp, 0);
                new CReportPropostaComercial().abrirRelatorio(modpro.getCdProposta(), modpro.getCdProposta(), modpro.getCdRevisao(), su, conexao, emp, pg, "EMAIL", modpro);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManterPropostaComercialRev1.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisarProposta();
    }

    // Método para fazer a cópia da proposta para uma nova versao
    private void copiarProposta() throws SQLException {
        cpro.criarProposta();
    }

    //Método para Entar no Pedido
    private void buscarPedido() {
        String sql = "select * from gcvpedido where cd_proposta = '" + modpro.getCdProposta()
                + "' and cd_revisao = '" + modpro.getCdRevisao()
                + "'";
        try {
            CPedido cped = new CPedido(conexao, su);
            numRegPedido = cped.pesquisarPedido(sql);
            if (numRegPedido > 0) {
                new ManterPedido(su, conexao, pg, modpro).setVisible(true);
            } else if (JOptionPane.showConfirmDialog(null, "Não há Pedido gerado! Confirma gerar Pedido?\nAtenção! Este processo irá confirmar a revisão atual da Proposta "
                    + "e fechar as demais revisões!")
                    == JOptionPane.OK_OPTION) {
                String cdPedido = cped.criarPedido(modpro);
                if (!"N".equals(cdPedido)) {
                    jForCdPedido.setText(cdPedido);
                    jComSituacao.setSelectedIndex(1);
                    fecharProposta = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Geração de pedido cancelada!");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManterPropostaComercialRev1.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (fecharProposta) {
            operProp = "A";
            salvarProposta(ISBOTAO);
            pesquisarProposta();
            new ManterPedido(su, conexao, pg, modpro).setVisible(true);
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
        jButCopiar = new javax.swing.JButton();
        jButNovo = new javax.swing.JButton();
        jButEditar = new javax.swing.JButton();
        jButSalvar = new javax.swing.JButton();
        jButCancelar = new javax.swing.JButton();
        jButExcluir = new javax.swing.JButton();
        jButPesquisar = new javax.swing.JButton();
        jButAnterior = new javax.swing.JButton();
        jButProximo = new javax.swing.JButton();
        jButImprimir = new javax.swing.JButton();
        jButEnviar = new javax.swing.JButton();
        jButSair = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jForCdAtendimento = new javax.swing.JFormattedTextField();
        jLabel4 = new javax.swing.JLabel();
        jForDataAtendimento = new javax.swing.JFormattedTextField();
        jForHoraAtendimento = new javax.swing.JFormattedTextField();
        jLabSituacao = new javax.swing.JLabel();
        jComSituacao = new javax.swing.JComboBox<>();
        jTabbedPanProposta = new javax.swing.JTabbedPane();
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
        jLabValorTotalBruto = new javax.swing.JLabel();
        jForValorTotalBruto = new FormatarValor(FormatarValor.NUMERO);
        jLabTotalDescontos = new javax.swing.JLabel();
        jForTotalDescontos = new FormatarValor(FormatarValor.NUMERO);
        ;
        jLabel8 = new javax.swing.JLabel();
        jForValorTotalLiquido = new FormatarValor(FormatarValor.NUMERO);
        jLabValorOutrosDescontos = new javax.swing.JLabel();
        jForTotalOutrosDescontos = new FormatarValor(FormatarValor.NUMERO)
        ;
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
        jButDespesasAdicionais = new javax.swing.JButton();
        jButPedido = new javax.swing.JButton();
        jLabTipoPedido = new javax.swing.JLabel();
        jComTipoPedido = new javax.swing.JComboBox<>();
        jLabTipoPagamento = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jTexCdTipoPagamento = new javax.swing.JTextField();
        jTexNomeTipoPagamento = new javax.swing.JTextField();
        jTexCdCondPagamento = new javax.swing.JTextField();
        jTexNomeCondPaamento = new javax.swing.JTextField();
        jLabCdPedido = new javax.swing.JLabel();
        jForCdPedido = new javax.swing.JFormattedTextField();
        jTexPrazoExecucao = new javax.swing.JTextField();
        jLabPrazoExecucao = new javax.swing.JLabel();
        jForDataEnvio = new javax.swing.JFormattedTextField();
        jLabDataEnvioProposta = new javax.swing.JLabel();
        jLabObsCliente = new javax.swing.JLabel();
        jPanAmbiente = new javax.swing.JPanel();
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
        jTabItensProposta = new javax.swing.JTable();
        jLabValorServicoLocal = new javax.swing.JLabel();
        jForValorServicoLocal = new FormatarValor(FormatarValor.NUMERO);
        jLabValorMateriaisLocal = new javax.swing.JLabel();
        jForValorMateriaisLocal = new FormatarValor(FormatarValor.NUMERO);
        jLabValorAdicionaisLocal = new javax.swing.JLabel();
        jForValorAdicionaisLocal = new FormatarValor(FormatarValor.NUMERO);
        jLabValorTotalBrutoLocal = new javax.swing.JLabel();
        jForValorTotalBrutoLocal = new FormatarValor(FormatarValor.NUMERO);
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
        jLabValDescontosLocal = new javax.swing.JLabel();
        jForValorDescontosLocal = new FormatarValor(FormatarValor.NUMERO);
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaObsLocal = new javax.swing.JTextArea();
        jLabInformacaoComplementar = new javax.swing.JLabel();
        jLabTotalLiquidoLocal = new javax.swing.JLabel();
        jForValorTotalLiquidoLocal = new FormatarValor(FormatarValor.NUMERO);
        jLabValorOutrosDescLocal = new javax.swing.JLabel();
        jForValorOutrosDescLocal = new FormatarValor(FormatarValor.NUMERO)
        ;
        jLabCdProposta = new javax.swing.JLabel();
        jForCdProposta = new javax.swing.JFormattedTextField();
        jLabCdVistoria = new javax.swing.JLabel();
        jForCdVistoria = new javax.swing.JFormattedTextField();
        jLabCdVendedor = new javax.swing.JLabel();
        jTexCdVendedor = new javax.swing.JTextField();
        jTexNomeVendedor = new javax.swing.JTextField();
        jLabCdTecnico = new javax.swing.JLabel();
        jTexCdTecnico = new javax.swing.JTextField();
        jTexNomeTecnico = new javax.swing.JTextField();
        jTexCdRevisao = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manter Proposta Comercial");
        setMaximumSize(new java.awt.Dimension(880, 746));
        setMinimumSize(new java.awt.Dimension(880, 746));
        setPreferredSize(new java.awt.Dimension(880, 746));
        setSize(new java.awt.Dimension(880, 746));

        jTooMenuFerramentas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTooMenuFerramentas.setRollover(true);

        jButCopiar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButCopiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Copy-32x32.png"))); // NOI18N
        jButCopiar.setText("Copiar");
        jButCopiar.setFocusable(false);
        jButCopiar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButCopiar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButCopiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButCopiarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButCopiar);

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

        jButEnviar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButEnviar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Mail-send-32.png"))); // NOI18N
        jButEnviar.setText("Enviar");
        jButEnviar.setFocusable(false);
        jButEnviar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButEnviar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButEnviarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButEnviar);

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

        jForCdAtendimento.setEditable(false);
        jForCdAtendimento.setEnabled(false);

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Data Atend.:");

        jForDataAtendimento.setEditable(false);
        try {
            jForDataAtendimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jForDataAtendimento.setEnabled(false);

        jForHoraAtendimento.setEditable(false);
        try {
            jForHoraAtendimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jForHoraAtendimento.setEnabled(false);

        jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabSituacao.setText("Situação:");

        jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "AP-Aprovada", "CA-Cancelada", "EN-Enviada", "NF-Negociação Fechada", "NE-Não Enviada", "NA-Não Aprovada", " " }));

        jTabbedPanProposta.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTabbedPanProposta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabbedPanProposta.setMaximumSize(new java.awt.Dimension(880, 535));
        jTabbedPanProposta.setMinimumSize(new java.awt.Dimension(880, 535));
        jTabbedPanProposta.setPreferredSize(new java.awt.Dimension(880, 535));

        jPanCliente.setMaximumSize(new java.awt.Dimension(730, 510));
        jPanCliente.setMinimumSize(new java.awt.Dimension(730, 510));
        jPanCliente.setPreferredSize(new java.awt.Dimension(730, 510));

        jLabTipoPessoa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTipoPessoa.setText("Tp. Pessoa:");

        jComTipoPessoa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Física", "Jurídica" }));
        jComTipoPessoa.setMaximumSize(new java.awt.Dimension(70, 40));
        jComTipoPessoa.setMinimumSize(new java.awt.Dimension(70, 20));
        jComTipoPessoa.setPreferredSize(new java.awt.Dimension(70, 20));

        jLabTelefone.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTelefone.setText("Tel.:");

        try {
            jForTelefone.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) ####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabCelular.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCelular.setText("Cel.:");

        jLabEmail.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabEmail.setText("email:");

        try {
            jForCelular.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("(##) #####-####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabNomeRazaoSocial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNomeRazaoSocial.setText("Nome/Razão Soc.:");

        jPanEndereco.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Endereço", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel1.setText("End.:");
        jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel1.setAutoscrolls(true);

        jComTipoLogradouro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Aeroporto", "Alameda", "Área", "Avenida", "Campo", "Chácara", "Colônia", "Condomínio", "Conjunto", "Distrito", "Esplanada", "Estação", "Estrada", "Favela", "Feira", "Jardim", "Ladeira", "Lago", "Lagoa", "Largo", "Loteamento", "Morro", "Núcleo", "Parque", "Passarela", "Pátio", "Praça", "Quadra", "Recanto", "Residencial", "Rodovia", "Rua", "Setor", "Sítio", "Travessa", "Trecho", "Trevo", "Vale", "Vereda", "Via", "Viaduto", "Viela", "Vila" }));

        jLabNumero.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNumero.setText("Número:");

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
        jLabel5.setText("Tp End.:");

        jComTipoEndereco.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Comercial", "Residencial" }));

        javax.swing.GroupLayout jPanEnderecoLayout = new javax.swing.GroupLayout(jPanEndereco);
        jPanEndereco.setLayout(jPanEnderecoLayout);
        jPanEnderecoLayout.setHorizontalGroup(
            jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanEnderecoLayout.createSequentialGroup()
                        .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabCidade)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexSiglaUF, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabMunicipio)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexMuncipio, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jComTipoEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jLabComplemento)
                                .addGap(2, 2, 2)
                                .addComponent(jTexComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(9, 9, 9)
                        .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jButValidarCep)
                            .addGroup(jPanEnderecoLayout.createSequentialGroup()
                                .addComponent(jLabCep)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForCep, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanEnderecoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel1)
                        .addGap(8, 8, 8)
                        .addComponent(jComTipoLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jTexLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 346, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabNumero)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTexNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        jLabTotalServico.setText("Tot. Ser.:");

        jForTotalServico.setEditable(false);
        jForTotalServico.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jLabTotalMateriais.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTotalMateriais.setText("Tot. Prod:");

        jForTotalMateriais.setEditable(false);
        jForTotalMateriais.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jLabTotalAdicionais.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTotalAdicionais.setText("Desp. Adic.:");

        jForTotalAdicionais.setEditable(false);
        jForTotalAdicionais.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jLabValorTotalBruto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValorTotalBruto.setText("Tot. Bruto:");

        jForValorTotalBruto.setEditable(false);
        jForValorTotalBruto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jLabTotalDescontos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTotalDescontos.setText("Descontos:");

        jForTotalDescontos.setEditable(false);
        jForTotalDescontos.setEnabled(false);

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel8.setText("Tot. Liquido:");

        jForValorTotalLiquido.setEditable(false);
        jForValorTotalLiquido.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForValorTotalLiquido.setEnabled(false);

        jLabValorOutrosDescontos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValorOutrosDescontos.setText("Outros Desc.:");

        jForTotalOutrosDescontos.setEditable(false);
        jForTotalOutrosDescontos.setEnabled(false);

        javax.swing.GroupLayout jPanTotalAtendimentoLayout = new javax.swing.GroupLayout(jPanTotalAtendimento);
        jPanTotalAtendimento.setLayout(jPanTotalAtendimentoLayout);
        jPanTotalAtendimentoLayout.setHorizontalGroup(
            jPanTotalAtendimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanTotalAtendimentoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabTotalServico)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForTotalServico, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 17, Short.MAX_VALUE)
                .addComponent(jLabTotalMateriais)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForTotalMateriais, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabTotalAdicionais)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForTotalAdicionais, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanTotalAtendimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanTotalAtendimentoLayout.createSequentialGroup()
                        .addComponent(jLabValorOutrosDescontos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jForTotalOutrosDescontos, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel8))
                    .addGroup(jPanTotalAtendimentoLayout.createSequentialGroup()
                        .addComponent(jLabTotalDescontos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForTotalDescontos, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabValorTotalBruto)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTotalAtendimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jForValorTotalBruto, javax.swing.GroupLayout.DEFAULT_SIZE, 80, Short.MAX_VALUE)
                    .addComponent(jForValorTotalLiquido))
                .addContainerGap())
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
                    .addComponent(jLabValorTotalBruto)
                    .addComponent(jForValorTotalBruto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabTotalDescontos)
                    .addComponent(jForTotalDescontos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTotalAtendimentoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jForValorTotalLiquido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabValorOutrosDescontos)
                    .addComponent(jForTotalOutrosDescontos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jTextAreaObsCliente.setColumns(20);
        jTextAreaObsCliente.setRows(5);
        jScrollPane3.setViewportView(jTextAreaObsCliente);

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

            jButDespesasAdicionais.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButDespesasAdicionais.setText("Desp. Adic.");

            jButPedido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButPedido.setText("Pedido");
            jButPedido.setEnabled(false);
            jButPedido.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButPedidoActionPerformed(evt);
                }
            });

            jLabTipoPedido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTipoPedido.setText("Tipo Pedido:");

            jComTipoPedido.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ambos", "Revenda", "Serviço" }));

            jLabTipoPagamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTipoPagamento.setText("Tipo Pagamento:");

            jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel7.setText("Condição de Pagamento:");

            jTexCdTipoPagamento.setEditable(false);
            jTexCdTipoPagamento.setEnabled(false);
            jTexCdTipoPagamento.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCdTipoPagamentoKeyPressed(evt);
                }
            });

            jTexNomeTipoPagamento.setEditable(false);
            jTexNomeTipoPagamento.setEnabled(false);

            jTexCdCondPagamento.setEditable(false);
            jTexCdCondPagamento.setEnabled(false);
            jTexCdCondPagamento.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCdCondPagamentoKeyPressed(evt);
                }
            });

            jTexNomeCondPaamento.setEditable(false);
            jTexNomeCondPaamento.setEnabled(false);

            jLabCdPedido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdPedido.setText("Pedido:");

            jForCdPedido.setEditable(false);
            jForCdPedido.setEnabled(false);

            jTexPrazoExecucao.setEditable(false);
            jTexPrazoExecucao.setEnabled(false);

            jLabPrazoExecucao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabPrazoExecucao.setText("Prazo de Execução:");

            jForDataEnvio.setEditable(false);
            try {
                jForDataEnvio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }

            jLabDataEnvioProposta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabDataEnvioProposta.setText("Data de Envio da Proposta:");

            jLabObsCliente.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabObsCliente.setText("Observação do Cliente:");

            javax.swing.GroupLayout jPanClienteLayout = new javax.swing.GroupLayout(jPanCliente);
            jPanCliente.setLayout(jPanClienteLayout);
            jPanClienteLayout.setHorizontalGroup(
                jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanClienteLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane3)
                        .addComponent(jPanTotalAtendimento, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanClienteLayout.createSequentialGroup()
                            .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabTipoPedido)
                                .addComponent(jComTipoPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(24, 24, 24)
                            .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanClienteLayout.createSequentialGroup()
                                    .addComponent(jTexCdTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexNomeTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jTexCdCondPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexNomeCondPaamento, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanClienteLayout.createSequentialGroup()
                                    .addComponent(jLabTipoPagamento)
                                    .addGap(147, 147, 147)
                                    .addComponent(jLabel7)))
                            .addGap(18, 18, 18)
                            .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanClienteLayout.createSequentialGroup()
                                    .addComponent(jLabPrazoExecucao)
                                    .addGap(0, 37, Short.MAX_VALUE))
                                .addComponent(jTexPrazoExecucao))
                            .addGap(18, 18, 18)
                            .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jForCdPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabCdPedido)))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanClienteLayout.createSequentialGroup()
                            .addComponent(jLabObsCliente)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabDataEnvioProposta)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForDataEnvio, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanClienteLayout.createSequentialGroup()
                            .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jPanEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(jPanClienteLayout.createSequentialGroup()
                                    .addComponent(jLabNomeRazaoSocial)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexNomeRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabTipoPessoa)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComTipoPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanClienteLayout.createSequentialGroup()
                                    .addComponent(jLabTelefone)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabCelular)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForCelular, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabEmail)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButPedido, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButDespesasAdicionais, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(0, 0, Short.MAX_VALUE))
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
                        .addComponent(jComTipoPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButDespesasAdicionais))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabTelefone)
                        .addComponent(jForTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabCelular)
                        .addComponent(jForCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabEmail)
                        .addComponent(jTexEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButPedido))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(1, 1, 1)
                    .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabTipoPagamento)
                        .addComponent(jLabTipoPedido)
                        .addComponent(jLabel7)
                        .addComponent(jLabCdPedido)
                        .addComponent(jLabPrazoExecucao))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComTipoPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexCdTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexCdCondPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeCondPaamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jForCdPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexPrazoExecucao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanTotalAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jForDataEnvio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabDataEnvioProposta)
                        .addComponent(jLabObsCliente))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 78, Short.MAX_VALUE)
                    .addGap(2, 2, 2)
                    .addComponent(jPanBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            jTabbedPanProposta.addTab("Cliente", jPanCliente);

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
                                    .addComponent(jComTipoPiso, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanDetalhesLayout.createSequentialGroup()
                                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanDetalhesLayout.createSequentialGroup()
                                            .addComponent(jLabTipoVerniz)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTexCdTipoVerniz, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanDetalhesLayout.createSequentialGroup()
                                            .addGap(33, 33, 33)
                                            .addGroup(jPanDetalhesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jCheTingimento)
                                                .addComponent(jCheClareamento))))))
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
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
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

            jTabItensProposta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jTabItensProposta.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                    {null, null, null, null, null, null, null, null, null, null}
                },
                new String [] {
                    "Seq.", "Cod.", "Descrição", "U.M", "Qtde", "Pr. Unit.", "% Desc.", "Val. Desc.", "T. Ite.Brt", "T. Ite.Liq"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
                };
                boolean[] canEdit = new boolean [] {
                    false, false, false, false, false, false, false, false, false, false
                };

                public Class getColumnClass(int columnIndex) {
                    return types [columnIndex];
                }

                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return canEdit [columnIndex];
                }
            });
            jScrollPane1.setViewportView(jTabItensProposta);
            if (jTabItensProposta.getColumnModel().getColumnCount() > 0) {
                jTabItensProposta.getColumnModel().getColumn(0).setResizable(false);
                jTabItensProposta.getColumnModel().getColumn(0).setPreferredWidth(5);
                jTabItensProposta.getColumnModel().getColumn(1).setResizable(false);
                jTabItensProposta.getColumnModel().getColumn(1).setPreferredWidth(60);
                jTabItensProposta.getColumnModel().getColumn(2).setResizable(false);
                jTabItensProposta.getColumnModel().getColumn(2).setPreferredWidth(220);
                jTabItensProposta.getColumnModel().getColumn(3).setResizable(false);
                jTabItensProposta.getColumnModel().getColumn(3).setPreferredWidth(10);
                jTabItensProposta.getColumnModel().getColumn(4).setResizable(false);
                jTabItensProposta.getColumnModel().getColumn(4).setPreferredWidth(30);
                jTabItensProposta.getColumnModel().getColumn(5).setResizable(false);
                jTabItensProposta.getColumnModel().getColumn(5).setPreferredWidth(40);
                jTabItensProposta.getColumnModel().getColumn(6).setResizable(false);
                jTabItensProposta.getColumnModel().getColumn(6).setPreferredWidth(40);
                jTabItensProposta.getColumnModel().getColumn(7).setResizable(false);
                jTabItensProposta.getColumnModel().getColumn(7).setPreferredWidth(40);
                jTabItensProposta.getColumnModel().getColumn(8).setResizable(false);
                jTabItensProposta.getColumnModel().getColumn(8).setPreferredWidth(40);
                jTabItensProposta.getColumnModel().getColumn(9).setResizable(false);
                jTabItensProposta.getColumnModel().getColumn(9).setPreferredWidth(40);
            }

            javax.swing.GroupLayout jPanItensLayout = new javax.swing.GroupLayout(jPanItens);
            jPanItens.setLayout(jPanItensLayout);
            jPanItensLayout.setHorizontalGroup(
                jPanItensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1)
            );
            jPanItensLayout.setVerticalGroup(
                jPanItensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanItensLayout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );

            jLabValorServicoLocal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorServicoLocal.setText("Val. Ser.:");

            jForValorServicoLocal.setEditable(false);
            jForValorServicoLocal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
            jForValorServicoLocal.setEnabled(false);
            jForValorServicoLocal.setFocusable(false);
            jForValorServicoLocal.setRequestFocusEnabled(false);
            jForValorServicoLocal.setVerifyInputWhenFocusTarget(false);

            jLabValorMateriaisLocal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorMateriaisLocal.setText("Val. Mat.:");

            jForValorMateriaisLocal.setEditable(false);
            jForValorMateriaisLocal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
            jForValorMateriaisLocal.setEnabled(false);
            jForValorMateriaisLocal.setFocusable(false);
            jForValorMateriaisLocal.setRequestFocusEnabled(false);
            jForValorMateriaisLocal.setVerifyInputWhenFocusTarget(false);

            jLabValorAdicionaisLocal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorAdicionaisLocal.setText("Val. Adic.:");

            jForValorAdicionaisLocal.setEditable(false);
            jForValorAdicionaisLocal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
            jForValorAdicionaisLocal.setEnabled(false);
            jForValorAdicionaisLocal.setFocusable(false);
            jForValorAdicionaisLocal.setRequestFocusEnabled(false);
            jForValorAdicionaisLocal.setVerifyInputWhenFocusTarget(false);

            jLabValorTotalBrutoLocal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorTotalBrutoLocal.setText("Tot. Local Brt.:");

            jForValorTotalBrutoLocal.setEditable(false);
            jForValorTotalBrutoLocal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
            jForValorTotalBrutoLocal.setEnabled(false);
            jForValorTotalBrutoLocal.setFocusable(false);
            jForValorTotalBrutoLocal.setRequestFocusEnabled(false);
            jForValorTotalBrutoLocal.setVerifyInputWhenFocusTarget(false);

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
                        .addContainerGap(112, Short.MAX_VALUE))
                );
                jPanBotoes1Layout.setVerticalGroup(
                    jPanBotoes1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanBotoes1Layout.createSequentialGroup()
                        .addGap(11, 11, 11)
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

                jLabValDescontosLocal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                jLabValDescontosLocal.setText("Val. Desc.:");

                jForValorDescontosLocal.setEditable(false);
                jForValorDescontosLocal.setEnabled(false);
                jForValorDescontosLocal.setFocusable(false);
                jForValorDescontosLocal.setRequestFocusEnabled(false);
                jForValorDescontosLocal.setVerifyInputWhenFocusTarget(false);

                jScrollPane2.setMaximumSize(new java.awt.Dimension(166, 46));
                jScrollPane2.setMinimumSize(new java.awt.Dimension(166, 46));
                jScrollPane2.setPreferredSize(new java.awt.Dimension(166, 46));

                jTextAreaObsLocal.setColumns(20);
                jTextAreaObsLocal.setRows(5);
                jTextAreaObsLocal.setMinimumSize(new java.awt.Dimension(4, 90));
                jScrollPane2.setViewportView(jTextAreaObsLocal);

                jLabInformacaoComplementar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                jLabInformacaoComplementar.setText("Informação Complementar:");

                jLabTotalLiquidoLocal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                jLabTotalLiquidoLocal.setText("Tot. Local Liq.:");

                jForValorTotalLiquidoLocal.setEditable(false);
                jForValorTotalLiquidoLocal.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
                jForValorTotalLiquidoLocal.setEnabled(false);
                jForValorTotalLiquidoLocal.setFocusable(false);
                jForValorTotalLiquidoLocal.setRequestFocusEnabled(false);
                jForValorTotalLiquidoLocal.setVerifyInputWhenFocusTarget(false);

                jLabValorOutrosDescLocal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                jLabValorOutrosDescLocal.setText("Outros Desc.:");

                jForValorOutrosDescLocal.setEditable(false);

                javax.swing.GroupLayout jPanAmbienteLayout = new javax.swing.GroupLayout(jPanAmbiente);
                jPanAmbiente.setLayout(jPanAmbienteLayout);
                jPanAmbienteLayout.setHorizontalGroup(
                    jPanAmbienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanAmbienteLayout.createSequentialGroup()
                        .addGroup(jPanAmbienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanItens, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanAmbienteLayout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(jPanAmbienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanAmbienteLayout.createSequentialGroup()
                                        .addGroup(jPanAmbienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanAmbienteLayout.createSequentialGroup()
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
                                                .addComponent(jLabValDescontosLocal))
                                            .addGroup(jPanAmbienteLayout.createSequentialGroup()
                                                .addComponent(jLabInformacaoComplementar)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabValorOutrosDescLocal)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanAmbienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jForValorDescontosLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jForValorOutrosDescLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGap(34, 34, 34)
                                        .addGroup(jPanAmbienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanAmbienteLayout.createSequentialGroup()
                                                .addComponent(jLabTotalLiquidoLocal)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jForValorTotalLiquidoLocal))
                                            .addGroup(jPanAmbienteLayout.createSequentialGroup()
                                                .addComponent(jLabValorTotalBrutoLocal)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jForValorTotalBrutoLocal, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(0, 0, Short.MAX_VALUE))))
                            .addGroup(jPanAmbienteLayout.createSequentialGroup()
                                .addComponent(jPanDetalhes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanAmbienteLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jPanBotoes1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );
                jPanAmbienteLayout.setVerticalGroup(
                    jPanAmbienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanAmbienteLayout.createSequentialGroup()
                        .addComponent(jPanDetalhes, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanItens, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)
                        .addGroup(jPanAmbienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanAmbienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabValDescontosLocal)
                                .addComponent(jForValorDescontosLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabValorTotalBrutoLocal)
                                .addComponent(jForValorTotalBrutoLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanAmbienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabValorServicoLocal)
                                .addComponent(jForValorServicoLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabValorMateriaisLocal)
                                .addComponent(jForValorMateriaisLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabValorAdicionaisLocal)
                                .addComponent(jForValorAdicionaisLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanAmbienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanAmbienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabTotalLiquidoLocal)
                                .addComponent(jForValorTotalLiquidoLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jForValorOutrosDescLocal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabValorOutrosDescLocal))
                            .addComponent(jLabInformacaoComplementar, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanBotoes1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                );

                jTabbedPanProposta.addTab("Ambientes", jPanAmbiente);

                jLabCdProposta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                jLabCdProposta.setText("Proposta Comercial:");

                jLabCdVistoria.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                jLabCdVistoria.setText("Núm. Vistoria:");

                jForCdVistoria.setEditable(false);
                jForCdVistoria.setEnabled(false);

                jLabCdVendedor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                jLabCdVendedor.setText("Vend.:");

                jTexCdVendedor.setEditable(false);
                jTexCdVendedor.setEnabled(false);
                jTexCdVendedor.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        jTexCdVendedorKeyPressed(evt);
                    }
                });

                jTexNomeVendedor.setEditable(false);
                jTexNomeVendedor.setEnabled(false);

                jLabCdTecnico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                jLabCdTecnico.setText("Técnico:");

                jTexCdTecnico.setEditable(false);
                jTexCdTecnico.setEnabled(false);
                jTexCdTecnico.addKeyListener(new java.awt.event.KeyAdapter() {
                    public void keyPressed(java.awt.event.KeyEvent evt) {
                        jTexCdTecnicoKeyPressed(evt);
                    }
                });

                jTexNomeTecnico.setEditable(false);
                jTexNomeTecnico.setEnabled(false);

                jLabel9.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
                jLabel9.setText("Revisão:");

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
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTabbedPanProposta, javax.swing.GroupLayout.PREFERRED_SIZE, 861, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jForCdAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabCdVistoria))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jForDataAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jForHoraAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(24, 24, 24)))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jForCdVistoria, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabCdVendedor)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTexCdVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTexNomeVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabCdProposta))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabCdTecnico)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTexCdTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTexNomeTecnico)
                                        .addGap(1, 1, 1)
                                        .addComponent(jLabel9)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTexCdRevisao)
                                    .addComponent(jForCdProposta, javax.swing.GroupLayout.DEFAULT_SIZE, 69, Short.MAX_VALUE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabSituacao)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())
                );
                layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel2)
                                    .addComponent(jForCdAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabCdVistoria)
                                    .addComponent(jForCdVistoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabCdVendedor)
                                    .addComponent(jTexCdVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTexNomeVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabCdTecnico)
                                    .addComponent(jTexCdTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jTexNomeTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(1, 1, 1)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabCdProposta)
                                    .addComponent(jForCdProposta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel4)
                                        .addComponent(jForDataAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jForHoraAtendimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jTexCdRevisao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel9)))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabSituacao))
                        .addGap(1, 1, 1)
                        .addComponent(jTabbedPanProposta, javax.swing.GroupLayout.PREFERRED_SIZE, 517, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(29, Short.MAX_VALUE))
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
        if ("AP".equals(jComSituacao.getSelectedItem().toString().substring(0, 2).toUpperCase())) {
            JOptionPane.showMessageDialog(null, "Proposta aprovada! Alteração não permitida!");
        } else if ("NF".equals(jComSituacao.getSelectedItem().toString().substring(0, 2).toUpperCase())) {
            JOptionPane.showMessageDialog(null, "Proposta aprovada! Alteração não permitida!");
        } else if ("NA".equals(jComSituacao.getSelectedItem().toString().substring(0, 2).toUpperCase())) {
            JOptionPane.showMessageDialog(null, "Proposta aprovada! Alteração não permitida!");
        } else if ("EN".equals(jComSituacao.getSelectedItem().toString().substring(0, 2).toUpperCase())) {
            if (JOptionPane.showConfirmDialog(null, "Proposta já enviada! Deseja realmente alterar?") == JOptionPane.OK_OPTION) {
                if (jTabbedPanProposta.getSelectedIndex() == 0) {
                    liberarCamposProposta();
                    operProp = "A";         // se estiver editando um registro do sistema, a operação do sistema será A de alteração de Registro
                } else if (jTabbedPanProposta.getSelectedIndex() == 1) {
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
            }
        }else{
            if (jTabbedPanProposta.getSelectedIndex() == 0) {
                    liberarCamposProposta();
                    operProp = "A";         // se estiver editando um registro do sistema, a operação do sistema será A de alteração de Registro
                } else if (jTabbedPanProposta.getSelectedIndex() == 1) {
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
        }
    }//GEN-LAST:event_jButEditarActionPerformed

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        if (jTabbedPanProposta.getSelectedIndex() == 0) {
            salvarProposta(true);
            pesquisarProposta();
        } else if (jTabbedPanProposta.getSelectedIndex() == 1) {
            salvarLocal(true);
            idxCorLocalAtualizado = modlpr.getCdLocal();
            pesquisarProposta();
            jTabbedPanProposta.setSelectedIndex(1);
        } else {
            JOptionPane.showMessageDialog(null, "Não foi possível detectar a Guia");
        }
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
        // TODO add your handling code here:
        jForCdProposta.setEditable(true);
        jForCdProposta.setEnabled(true);
        jButProximo.setEnabled(!ISBOTAO);
        jButAnterior.setEnabled(!ISBOTAO);
        jButSair.setEnabled(ISBOTAO);
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO);
        limparTelaProposta();
        operProp = "N";         // se cancelar a ação atual na tela do sistema a operação do sistema será N  de novo Registro
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO);
        if (jTabbedPanProposta.getSelectedIndex() == 0) {
            excluirAtend();
        } else if (jTabbedPanProposta.getSelectedIndex() == 1) {
            excluirLocal();
        } else {
            JOptionPane.showMessageDialog(null, "Não foi possível detectar a Guia!");
        }
    }//GEN-LAST:event_jButExcluirActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:
        if (jForCdProposta.getText().isEmpty()) {
            sqlProp = "SELECT * FROM GCVPROPOSTA";
        } else {
            sqlProp = "SELECT * FROM GCVPROPOSTA WHERE CD_PROPOSTA = " + jForCdProposta.getText().trim();
        }
        bloquearCamposProposta();
        pesquisarProposta();
        controleBotoes(ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO);
        jButSair.setEnabled(ISBOTAO);
    }//GEN-LAST:event_jButPesquisarActionPerformed

    private void jButAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAnteriorActionPerformed
        // TODO add your handling code here:
        idxCorProp -= 1;
        limparTelaLocal();
        esvaziarTabelaItem();
        upRegistrosProposta();
    }//GEN-LAST:event_jButAnteriorActionPerformed

    private void jButProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButProximoActionPerformed
        // TODO add your handling code here:
        idxCorProp += 1;
        limparTelaLocal();
        esvaziarTabelaItem();
        upRegistrosProposta();
    }//GEN-LAST:event_jButProximoActionPerformed

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        //modate.setVoltar(true);
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

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

    private void jButImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButImprimirActionPerformed
        imprimirProposta();
    }//GEN-LAST:event_jButImprimirActionPerformed

    private void jTexCdVendedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdVendedorKeyPressed
        VerificarTecla vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomVendedor();
        }
    }//GEN-LAST:event_jTexCdVendedorKeyPressed

    private void jTexCdTecnicoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdTecnicoKeyPressed
        VerificarTecla vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomTecnico();
        }
    }//GEN-LAST:event_jTexCdTecnicoKeyPressed

    private void jButEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEnviarActionPerformed
        enviarProposta();
    }//GEN-LAST:event_jButEnviarActionPerformed

    private void jTexCdCondPagamentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdCondPagamentoKeyPressed
        VerificarTecla vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomCondicaoPagamento();
        }
    }//GEN-LAST:event_jTexCdCondPagamentoKeyPressed

    private void jTexCdTipoPagamentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdTipoPagamentoKeyPressed
        VerificarTecla vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomTipoPagamento();
        }
    }//GEN-LAST:event_jTexCdTipoPagamentoKeyPressed

    private void jButValidarCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButValidarCepActionPerformed
        try {
            buscarCep();

        } catch (IOException ex) {
            Logger.getLogger(ManterPropostaComercialRev1.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButValidarCepActionPerformed

    private void jTexSiglaUFKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexSiglaUFKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomUF();
        }
    }//GEN-LAST:event_jTexSiglaUFKeyPressed

    private void jTexMuncipioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexMuncipioKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomMunicipio();
        }
    }//GEN-LAST:event_jTexMuncipioKeyPressed

    private void jButCopiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCopiarActionPerformed
        if (JOptionPane.showConfirmDialog(null, "Deseja criar uma nova versão da Proposta atual?") == JOptionPane.OK_OPTION) {
            try {
                copiarProposta();
            } catch (SQLException ex) {
                Logger.getLogger(ManterPropostaComercialRev1.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButCopiarActionPerformed

    private void jButPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPedidoActionPerformed
        buscarPedido();
    }//GEN-LAST:event_jButPedidoActionPerformed

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
            java.util.logging.Logger.getLogger(ManterPropostaComercialRev1.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterPropostaComercialRev1.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterPropostaComercialRev1.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterPropostaComercialRev1.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new ManterPropostaComercialRev1(su, conexao, pg).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButAnterior;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButCopiar;
    private javax.swing.JButton jButDespesasAdicionais;
    private javax.swing.JButton jButEditar;
    private javax.swing.JButton jButEnviar;
    private javax.swing.JButton jButExcluir;
    private javax.swing.JButton jButImprimir;
    private javax.swing.JButton jButLocalAnterior;
    private javax.swing.JButton jButLocalProximo;
    private javax.swing.JButton jButNovo;
    private javax.swing.JButton jButNovoLocal;
    private javax.swing.JButton jButPedido;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JButton jButProximo;
    private javax.swing.JButton jButSair;
    private javax.swing.JButton jButSalvar;
    private javax.swing.JButton jButValidarCep;
    private javax.swing.JCheckBox jCheClareamento;
    private javax.swing.JCheckBox jCheTingimento;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JComboBox<String> jComTipoEndereco;
    private javax.swing.JComboBox<String> jComTipoLogradouro;
    private javax.swing.JComboBox<String> jComTipoPedido;
    private javax.swing.JComboBox<String> jComTipoPessoa;
    private javax.swing.JComboBox<String> jComTipoPiso;
    private javax.swing.JComboBox<String> jComTipoRodape;
    private javax.swing.JFormattedTextField jForCdAtendimento;
    private javax.swing.JFormattedTextField jForCdPedido;
    private javax.swing.JFormattedTextField jForCdProposta;
    private javax.swing.JFormattedTextField jForCdVistoria;
    private javax.swing.JFormattedTextField jForCelular;
    private javax.swing.JFormattedTextField jForCep;
    private javax.swing.JFormattedTextField jForDataAtendimento;
    private javax.swing.JFormattedTextField jForDataCadAtend;
    private javax.swing.JFormattedTextField jForDataCadLocal;
    private javax.swing.JFormattedTextField jForDataEnvio;
    private javax.swing.JFormattedTextField jForDataModifLocal;
    private javax.swing.JFormattedTextField jForDataModifiAtend;
    private javax.swing.JFormattedTextField jForEspessura;
    private javax.swing.JFormattedTextField jForHoraAtendimento;
    private javax.swing.JFormattedTextField jForHoraCadAtend;
    private javax.swing.JFormattedTextField jForHoraCadLocal;
    private javax.swing.JFormattedTextField jForHoraModifAtend;
    private javax.swing.JFormattedTextField jForHoraModifLocal;
    private javax.swing.JFormattedTextField jForLargura;
    private javax.swing.JFormattedTextField jForMetragemArea;
    private javax.swing.JFormattedTextField jForMetragemRodape;
    private javax.swing.JFormattedTextField jForPercPerda;
    private javax.swing.JFormattedTextField jForTelefone;
    private javax.swing.JFormattedTextField jForTotalAdicionais;
    private javax.swing.JFormattedTextField jForTotalDescontos;
    private javax.swing.JFormattedTextField jForTotalMateriais;
    private javax.swing.JFormattedTextField jForTotalOutrosDescontos;
    private javax.swing.JFormattedTextField jForTotalServico;
    private javax.swing.JFormattedTextField jForValorAdicionaisLocal;
    private javax.swing.JFormattedTextField jForValorDescontosLocal;
    private javax.swing.JFormattedTextField jForValorMateriaisLocal;
    private javax.swing.JFormattedTextField jForValorOutrosDescLocal;
    private javax.swing.JFormattedTextField jForValorServicoLocal;
    private javax.swing.JFormattedTextField jForValorTotalBruto;
    private javax.swing.JFormattedTextField jForValorTotalBrutoLocal;
    private javax.swing.JFormattedTextField jForValorTotalLiquido;
    private javax.swing.JFormattedTextField jForValorTotalLiquidoLocal;
    private javax.swing.JLabel jLabAmbiente;
    private javax.swing.JLabel jLabCadPorAtend;
    private javax.swing.JLabel jLabCadPorLocal;
    private javax.swing.JLabel jLabCdPedido;
    private javax.swing.JLabel jLabCdProposta;
    private javax.swing.JLabel jLabCdTecnico;
    private javax.swing.JLabel jLabCdVendedor;
    private javax.swing.JLabel jLabCdVistoria;
    private javax.swing.JLabel jLabCelular;
    private javax.swing.JLabel jLabCep;
    private javax.swing.JLabel jLabCidade;
    private javax.swing.JLabel jLabComplemento;
    private javax.swing.JLabel jLabComprimento;
    private javax.swing.JLabel jLabDataEnvioProposta;
    private javax.swing.JLabel jLabDataModifiAtend;
    private javax.swing.JLabel jLabEmail;
    private javax.swing.JLabel jLabEspessura;
    private javax.swing.JLabel jLabInformacaoComplementar;
    private javax.swing.JLabel jLabLargura;
    private javax.swing.JLabel jLabMetragemArea;
    private javax.swing.JLabel jLabMetragemRodape;
    private javax.swing.JLabel jLabModifPorLocal;
    private javax.swing.JLabel jLabMunicipio;
    private javax.swing.JLabel jLabNomeRazaoSocial;
    private javax.swing.JLabel jLabNumero;
    private javax.swing.JLabel jLabObsCliente;
    private javax.swing.JLabel jLabPercPerda;
    private javax.swing.JLabel jLabPrazoExecucao;
    private javax.swing.JLabel jLabRegAtend;
    private javax.swing.JLabel jLabRegLocal;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabTelefone;
    private javax.swing.JLabel jLabTipoPagamento;
    private javax.swing.JLabel jLabTipoPedido;
    private javax.swing.JLabel jLabTipoPessoa;
    private javax.swing.JLabel jLabTipoPiso;
    private javax.swing.JLabel jLabTipoRodape;
    private javax.swing.JLabel jLabTipoVerniz;
    private javax.swing.JLabel jLabTotalAdicionais;
    private javax.swing.JLabel jLabTotalDescontos;
    private javax.swing.JLabel jLabTotalLiquidoLocal;
    private javax.swing.JLabel jLabTotalMateriais;
    private javax.swing.JLabel jLabTotalServico;
    private javax.swing.JLabel jLabValDescontosLocal;
    private javax.swing.JLabel jLabValorAdicionaisLocal;
    private javax.swing.JLabel jLabValorMateriaisLocal;
    private javax.swing.JLabel jLabValorOutrosDescLocal;
    private javax.swing.JLabel jLabValorOutrosDescontos;
    private javax.swing.JLabel jLabValorServicoLocal;
    private javax.swing.JLabel jLabValorTotalBruto;
    private javax.swing.JLabel jLabValorTotalBrutoLocal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JPanel jPanAmbiente;
    private javax.swing.JPanel jPanBotoes;
    private javax.swing.JPanel jPanBotoes1;
    private javax.swing.JPanel jPanCliente;
    private javax.swing.JPanel jPanDetalhes;
    private javax.swing.JPanel jPanEndereco;
    private javax.swing.JPanel jPanItens;
    private javax.swing.JPanel jPanTotalAtendimento;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTabItensProposta;
    private javax.swing.JTabbedPane jTabbedPanProposta;
    private javax.swing.JTabbedPane jTabbedPaneDetalhes;
    private javax.swing.JTextField jTexBairro;
    private javax.swing.JTextField jTexCadPorAtend;
    private javax.swing.JTextField jTexCadPorLocal;
    private javax.swing.JTextField jTexCdCondPagamento;
    private javax.swing.JTextField jTexCdRevisao;
    private javax.swing.JTextField jTexCdTecnico;
    private javax.swing.JTextField jTexCdTipoPagamento;
    private javax.swing.JTextField jTexCdTipoVerniz;
    private javax.swing.JTextField jTexCdVendedor;
    private javax.swing.JTextField jTexComplemento;
    private javax.swing.JTextField jTexComprimento;
    private javax.swing.JTextField jTexEmail;
    private javax.swing.JTextField jTexEssenciaMadeira;
    private javax.swing.JTextField jTexLogradouro;
    private javax.swing.JTextField jTexModifPorAtend;
    private javax.swing.JTextField jTexModifPorLocal;
    private javax.swing.JTextField jTexMuncipio;
    private javax.swing.JTextField jTexNomeCondPaamento;
    private javax.swing.JTextField jTexNomeEssenciaMadeira;
    private javax.swing.JTextField jTexNomeLocal;
    private javax.swing.JTextField jTexNomeRazaoSocial;
    private javax.swing.JTextField jTexNomeTecnico;
    private javax.swing.JTextField jTexNomeTipoPagamento;
    private javax.swing.JTextField jTexNomeTipoVerniz;
    private javax.swing.JTextField jTexNomeVendedor;
    private javax.swing.JTextField jTexNumero;
    private javax.swing.JTextField jTexPrazoExecucao;
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
