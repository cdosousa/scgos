/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GCSCA0100
 */
package br.com.gcs.visao;

// objetos do registro Pai
// Objetos para pesquisa de correlato
import br.com.modelo.EnderecoPostal;
import br.com.controle.CEnderecoPostal;
import br.com.gcs.controle.CFornecedores;
import br.com.gcs.dao.FornecedoresDAO;
import br.com.gcs.modelo.Fornecedores;
import br.com.gfc.visao.PesquisarCondicaoPagamento;
import br.com.gfc.visao.PesquisarPortadores;
import br.com.gfc.visao.PesquisarTipoPagamento;
import br.com.visao.PesquisarMunicipios;
import br.com.visao.PesquisarUnidadeFederacao;

// Objetos de instância de parâmetros de ambiente
import br.com.modelo.DataSistema;
import br.com.modelo.Empresa;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;
import br.com.visao.PesquisarEmpresa;

// Objetos de ambiente java
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 30/07/2018
 * @version 0.01beta_0917
 */
public class ManterFornecedores extends javax.swing.JFrame {

    // Variáveis de instancia de parâmetros de ambiente
    private static Connection conexao;
    private static SessaoUsuario su;
    private VerificarTecla vt;

    // Variáveis de instância de objetos da classe
    private Fornecedores regCorr;
    private List< Fornecedores> resultado;
    private CFornecedores cfor;
    private Fornecedores modfor;
    private Empresa emp;

    // Variáveis de instância da objetos correlatos classe
    private CEnderecoPostal cep;
    private EnderecoPostal ep;

    // Variáveis de instância da classe
    private int numReg;
    private int idxCorr;
    private String sql;
    private String oper;
    private final boolean ISBOTAO = true;

    /**
     * Contrutor padrão para iniciar a tela com um fornecedor fixo
     *
     * @param su Sessao Atual do Usuario
     * @param conexao Objeto de conexão aberta com o bando de dados
     * @param sql Sentença SQL para buscar o cliente no banco de dados e Salvar
     */
    public ManterFornecedores(SessaoUsuario su, Connection conexao, String sql) {
        this.su = su;
        this.conexao = conexao;
        this.sql = sql;
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        controleBotoes(!ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO);
        bloquearCampos();
        pesquisar();
        this.dispose();
    }

    /**
     * Método para chamada do programa via menu inicil
     *
     * @param su
     * @param conexao
     */
    public ManterFornecedores(SessaoUsuario su, Connection conexao) {
        this.su = su;
        this.conexao = conexao;
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO);
        this.dispose();
    }

    // método para limpar tela
    private void limparTela() {
        jForCdCpfCnpj.setText("");
        jForInscEstadual.setText("");
        jComTipoPessoa.setSelectedIndex(0);
        jTexNomeRazaoSocial.setText("");
        jTexApelido.setText("");
        jComOptanteSimples.setSelectedIndex(0);
        jComTipoLogradouro.setSelectedIndex(0);
        jTexLogradouro.setText("");
        jTexNumero.setText("");
        jTexComplemento.setText("");
        jTexBairro.setText("");
        jTexCdMunicipioIbge.setText("");
        jTexSiglaUF.setText("");
        jForCep.setText("");
        jTexCdUfIbge.setText("");
        jTexMuncipio.setText("");
        jTexNumBanco.setText("");
        jTexNomeBanco.setText("");
        jTexAgenciaBanco.setText("");
        jTexNumContaBanco.setText("");
        jTexCdPortador.setText("");
        jTexNomePortador.setText("");
        jTexCdTipoPagamento.setText("");
        jTexNomeTipoPagamento.setText("");
        jTexCdCondPag.setText("");
        jTexNomeCondPag.setText("");
        jTexNomeContato.setText("");
        jForTelefone.setText("");
        jForCelular.setText("");
        jTexEmail.setText("");
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
        jForCdCpfCnpj.setEditable(true);
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {

        cfor = new CFornecedores(conexao);
        modfor = new Fornecedores();
        try {
            numReg = cfor.pesquisar(sql);
            idxCorr = +1;
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Argumento inválido na busca\nErr: " + iae.getCause());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Pesquisar os Registro\nErr: " + ex);
        }
        if (numReg > 0) {
            upRegistros();
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum registro encontrado!");
        }
    }

    private void upRegistros() {
        jTexRegTotal.setText(Integer.toString(numReg));
        jTexRegAtual.setText(Integer.toString(idxCorr));
        cfor.mostrarPesquisa(modfor, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jForInscEstadual.setText(modfor.getCdInscEstadual());
        jComTipoPessoa.setSelectedIndex(Integer.parseInt(modfor.getTipoPessoa()));
        jTexNomeRazaoSocial.setText(modfor.getNomeRazaoSocial().trim().toUpperCase());
        jTexApelido.setText(modfor.getApelido().trim().toUpperCase());
        jComOptanteSimples.setSelectedIndex(Integer.parseInt(modfor.getOptanteSimples()));
        jComTipoLogradouro.setSelectedItem(modfor.getTipoLogradouro());
        jTexLogradouro.setText(modfor.getLogradouro());
        jTexNumero.setText(modfor.getNumero());
        jTexComplemento.setText(modfor.getComplemento());
        jTexBairro.setText(modfor.getBairro());
        jTexCdMunicipioIbge.setText(modfor.getCdMunicipioIbge());
        jTexMuncipio.setText(modfor.getNomeMunicipio());
        jTexSiglaUF.setText(modfor.getSiglaUf());
        jTexCdUfIbge.setText(String.valueOf(modfor.getUfIbge()));
        jForCep.setText(modfor.getCdCep());
        jTexNumBanco.setText(modfor.getNumBanco());
        jTexNomeBanco.setText(modfor.getNomeBanco());
        jTexAgenciaBanco.setText(modfor.getAgenciaBanco());
        jTexNumContaBanco.setText(modfor.getNumContaBanco());
        jTexCdPortador.setText(modfor.getCdPortador());
        jTexNomePortador.setText(modfor.getNomePortador());
        jTexCdTipoPagamento.setText(modfor.getCdTipoPagamento());
        jTexNomeTipoPagamento.setText(modfor.getNomeTipoPagamento());
        jTexCdCondPag.setText(modfor.getCdCondPagamento());
        jTexNomeCondPag.setText(modfor.getNomeCondPag());
        jTexNomeContato.setText(modfor.getNomeContato());
        jForCelular.setText(modfor.getCelular());
        jForTelefone.setText(modfor.getTelefone());
        jTexEmail.setText(modfor.getEmail());
        jForCdCpfCnpj.setText(modfor.getCdCpfCnpj().trim());
        jTexCadastradoPor.setText(modfor.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modfor.getDataCadastro())));
        if (modfor.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(modfor.getDataModificacao())));
        }
        jComSituacao.setSelectedIndex(Integer.parseInt(String.valueOf(modfor.getSituacao())));

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

    //Bloquear os campos da tela
    private void bloquearCampos() {
        jForCdCpfCnpj.setEditable(false);
        //jForInscEstadual.setEditable(false);
        //jComTipoPessoa.setEditable(false);
        //jTexNomeRazaoSocial.setEditable(false);
        //jTexApelido.setEditable(false);
        jComOptanteSimples.setEditable(false);
        jComTipoLogradouro.setEditable(false);
        jTexLogradouro.setEditable(false);
        jTexNumero.setEditable(false);
        jTexComplemento.setEditable(false);
        jTexBairro.setEditable(false);
        //jTexMuncipio.setEditable(false);
        //jTexSiglaUF.setEditable(false);
        jForCep.setEditable(false);
        jTexNumBanco.setEditable(false);
        jTexNomeBanco.setEditable(false);
        jTexAgenciaBanco.setEditable(false);
        jTexNumContaBanco.setEditable(false);
        jTexCdPortador.setEditable(false);
        jTexCdTipoPagamento.setEditable(false);
        jTexCdCondPag.setEditable(false);
        jTexNomeContato.setEditable(false);
        jForTelefone.setEditable(false);
        jForCelular.setEditable(false);
        jTexEmail.setEditable(false);
        jComSituacao.setEditable(false);
    }

    //Loberar os campos da tela para atualização
    private void liberarCampos() {
        //jForInscEstadual.setEditable(true);
        //jForInscEstadual.setEditable(true);
        //jComTipoPessoa.setEditable(true);
        //jTexNomeRazaoSocial.setEditable(true);
        //jTexApelido.setEditable(true);
        jComOptanteSimples.setEditable(true);
        jComTipoLogradouro.setEditable(true);
        jTexLogradouro.setEditable(true);
        jTexNumero.setEditable(true);
        jTexComplemento.setEditable(true);
        jTexBairro.setEditable(true);
        //jTexMuncipio.setEditable(true);
        //jTexSiglaUF.setEditable(true);
        //jForCep.setEditable(true);
        jTexNumBanco.setEditable(true);
        jTexNomeBanco.setEditable(true);
        jTexAgenciaBanco.setEditable(true);
        jTexNumContaBanco.setEditable(true);
        jTexCdPortador.setEditable(true);
        jTexCdTipoPagamento.setEditable(true);
        jTexCdCondPag.setEditable(true);
        jTexNomeContato.setEditable(true);
        jForTelefone.setEditable(true);
        jForCelular.setEditable(true);
        jTexEmail.setEditable(true);
        jComSituacao.setEditable(false);
    }

    private void novoRegistro() {
        limparTela();
        liberarCampos();
        jForCdCpfCnpj.setEditable(true);
        jForCdCpfCnpj.requestFocus();
    }

    // metodo para dar zoon no campo UF
    private void zoomUF() {
        PesquisarUnidadeFederacao zoom = new PesquisarUnidadeFederacao(new JFrame(), true, conexao, "P");
        zoom.setVisible(true);
        jTexSiglaUF.setText(zoom.getSelec1().trim());
        jTexCdUfIbge.setText(zoom.getSelec3().trim());
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
                + jTexCdUfIbge.getText().toUpperCase().trim()
                + "' ORDER BY MU.NOME_MUNICIPIO";
        PesquisarMunicipios zoom = new PesquisarMunicipios(new JFrame(), true, "P", sql, conexao);
        zoom.setVisible(true);
        jTexCdMunicipioIbge.setText(zoom.getSelec1().trim());
        jTexMuncipio.setText(zoom.getSelec3().trim());
    }

    // metodo para dar zoom no campo portador
    private void zoomPortador() {
        PesquisarPortadores zoom = new PesquisarPortadores(this, rootPaneCheckingEnabled, "P", conexao);
        zoom.setVisible(rootPaneCheckingEnabled);
        jTexCdPortador.setText(zoom.getCdPortador());
        jTexNomePortador.setText(zoom.getNomePortador());
    }

    // metodo para dar zoom no campo tipo pagamento
    private void zoomTipPagamento() {
        PesquisarTipoPagamento zoom = new PesquisarTipoPagamento(this, rootPaneCheckingEnabled, "P", conexao);
        zoom.setVisible(rootPaneCheckingEnabled);
        jTexCdTipoPagamento.setText(zoom.getCdTipoPagamento());
        jTexNomeTipoPagamento.setText(zoom.getNomeTipoPagamento());
    }

    // metodo para dar zoom no campo condição de pagamento
    private void zoomCondicaoPagamento() {
        PesquisarCondicaoPagamento zoom = new PesquisarCondicaoPagamento(this, rootPaneCheckingEnabled, "P", conexao);
        zoom.setVisible(rootPaneCheckingEnabled);
        jTexCdCondPag.setText(zoom.getCdCondPag());
        jTexNomeCondPag.setText(zoom.getNomeCondPag());
    }

    /**
     * Método para pesquisar empresa
     */
    private void zoomEmpresas() {
        PesquisarEmpresa zoom = new PesquisarEmpresa(this, true, "P", conexao, su);
        zoom.setVisible(true);
        if (zoom.isEscolheu()) {
            emp = zoom.getEmp();
            upEmpresa();
        }
    }

    /**
     * Método para atualizar os dados da empresa
     */
    private void upEmpresa() {
        jComTipoPessoa.setSelectedIndex(Integer.parseInt(emp.getTipoPessoa()));
        jForCdCpfCnpj.setText(emp.getCdCpfCnpj());
        jForInscEstadual.setText(emp.getCdInscEstadual());
        jTexNomeRazaoSocial.setText(emp.getNomeRazaoSocial());
        jComSituacao.setSelectedIndex(Integer.parseInt(emp.getSituacao()));
        jTexApelido.setText(emp.getApelido());
        jComTipoLogradouro.setSelectedItem(emp.getTipoLogradouro());
        jTexLogradouro.setText(emp.getLogradouro());
        jTexNumero.setText(emp.getNumero());
        jTexComplemento.setText(emp.getComplemento());
        jTexMuncipio.setText(emp.getNomeMunicipio());
        jForCep.setText(emp.getCdCep());
        jTexBairro.setText(emp.getBairro());
        jTexSiglaUF.setText(emp.getSiglaUf());
        jTexCdUfIbge.setText(emp.getCdUfIbge());
        jTexCdMunicipioIbge.setText(emp.getCdMunicipioIbge());
        jTexEmail.setText(emp.getEmail());
        jForCelular.setText(emp.getCelular());
        jForTelefone.setText(emp.getTelefone());
    }

// metodo para buscar CEP
    private void buscarCep() throws IOException {
        cep = new CEnderecoPostal();
        ep = new EnderecoPostal();
        if (!jForCep.getText().trim().replace("-", "").isEmpty()) {
            cep.pesquisar(conexao, jForCep.getText().replace("-", ""), su.getCharSet());
            upEndereco();
        } else if (jTexSiglaUF.getText().isEmpty() || jTexLogradouro.getText().isEmpty() || jTexMuncipio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Você deve informar o CEP para busca, ou, na falta dele,\n informe UF, Cidade e Logradouro para buscar o CEP");
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
        jTexCdUfIbge.setText(String.valueOf(ep.getUfIbge()));
        jTexCdMunicipioIbge.setText(ep.getCdIbge());
        jTexMuncipio.setText(ep.getMunicipioLocalidade());
        jForCep.setText(ep.getCep());
    }

    /**
     * Metodo para controlar os botoes
     *
     * @param bNo Habilita ou desabilita o botão Novo
     * @param bEd Habilita ou desabilita o botão Editar
     * @param bSa Habilita ou desabilita o botão Salvar
     * @param bCa Habilita ou desabilita o botão Cancelar
     * @param bEx Habilita ou desabilita o botão Excluir
     * @param bPe Habilita ou desabilita o botão Pesquisar
     */
    private void controleBotoes(boolean bNo, boolean bEd, boolean bSa, boolean bCa, boolean bEx, boolean bPe) {
        jButNovo.setEnabled(bNo);
        jButEditar.setEnabled(bEd);
        jButSalvar.setEnabled(bSa);
        jButCancelar.setEnabled(bCa);
        jButExcluir.setEnabled(bEx);
        jButPesquisar.setEnabled(bPe);
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
        jPanSecundario = new javax.swing.JPanel();
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
        jLabCpfCnpj = new javax.swing.JLabel();
        jLabNomeTecnico = new javax.swing.JLabel();
        jLabSituacao = new javax.swing.JLabel();
        jForCdCpfCnpj = new javax.swing.JFormattedTextField();
        jTexNomeRazaoSocial = new javax.swing.JTextField();
        jComSituacao = new javax.swing.JComboBox<>();
        jForInscEstadual = new javax.swing.JFormattedTextField();
        jLabInscEstadual = new javax.swing.JLabel();
        jLabOrgaoExpedidor = new javax.swing.JLabel();
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
        jTexCdMunicipioIbge = new javax.swing.JTextField();
        jLabCep = new javax.swing.JLabel();
        jForCep = new javax.swing.JFormattedTextField();
        jTexCdUfIbge = new javax.swing.JTextField();
        jButValidarCep = new javax.swing.JButton();
        jLabTipoPessoa = new javax.swing.JLabel();
        jComTipoPessoa = new javax.swing.JComboBox<>();
        jLabOptanteSimples = new javax.swing.JLabel();
        jComOptanteSimples = new javax.swing.JComboBox<>();
        jTexApelido = new javax.swing.JTextField();
        jPanContato = new javax.swing.JPanel();
        jLabTelefone = new javax.swing.JLabel();
        jForTelefone = new javax.swing.JFormattedTextField();
        jLabCelular = new javax.swing.JLabel();
        jLabEmail = new javax.swing.JLabel();
        jForCelular = new javax.swing.JFormattedTextField();
        jTexEmail = new javax.swing.JTextField();
        jLabNomeContato = new javax.swing.JLabel();
        jTexNomeContato = new javax.swing.JTextField();
        jPanInformacoesBancarias = new javax.swing.JPanel();
        jLabNumBanco = new javax.swing.JLabel();
        jTexNumBanco = new javax.swing.JTextField();
        jLabNomeBanco = new javax.swing.JLabel();
        jTexNomeBanco = new javax.swing.JTextField();
        jLabAgenciaBanco = new javax.swing.JLabel();
        jTexAgenciaBanco = new javax.swing.JTextField();
        jLabNumContaBanco = new javax.swing.JLabel();
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
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Fornecedores");

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

        jPanSecundario.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanSecundario.setToolTipText("");

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
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
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
                    .addGap(108, 108, 108))
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

            jLabCpfCnpj.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCpfCnpj.setText("C.P.F. / C.N.P.J.:");

            jLabNomeTecnico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabNomeTecnico.setText("Nome / Razão Social:");

            jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabSituacao.setText("Situação:");

            jForCdCpfCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat(""))));
            jForCdCpfCnpj.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jForCdCpfCnpjKeyPressed(evt);
                }
            });

            jTexNomeRazaoSocial.setEditable(false);
            jTexNomeRazaoSocial.setEnabled(false);
            jTexNomeRazaoSocial.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jTexNomeRazaoSocialActionPerformed(evt);
                }
            });

            jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ativo", "Inativo" }));

            jForInscEstadual.setEditable(false);
            try {
                jForInscEstadual.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*******************")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForInscEstadual.setEnabled(false);

            jLabInscEstadual.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabInscEstadual.setText("I.E.:");

            jLabOrgaoExpedidor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabOrgaoExpedidor.setText("Fantasia / Apelido:");

            jPanEndereco.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Endereço", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel1.setText("Tipo Logradouro:");
            jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            jLabel1.setAutoscrolls(true);

            jComTipoLogradouro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Aeroporto", "Alameda", "Área", "Avenida", "Campo", "Chácara", "Colônia", "Condomínio", "Conjunto", "Distrito", "Esplanada", "Estação", "Estrada", "Favela", "Feira", "Jardim", "Ladeira", "Lago", "Lagoa", "Largo", "Loteamento", "Morro", "Núcleo", "Parque", "Passarela", "Pátio", "Praça", "Quadra", "Recanto", "Residencial", "Rodovia", "Rua", "Setor", "Sítio", "Travessa", "Trecho", "Trevo", "Vale", "Vereda", "Via", "Viaduto", "Viela", "Vila" }));

            jLabNumero.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabNumero.setText("Número:");

            jLabComplemento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabComplemento.setText("Complemento:");

            jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel3.setText("Bairro:");

            jLabCidade.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCidade.setText("U.F.:");

            jLabMunicipio.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabMunicipio.setText("Município:");

            jTexMuncipio.setEditable(false);
            jTexMuncipio.setEnabled(false);
            jTexMuncipio.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexMuncipioKeyPressed(evt);
                }
            });

            jTexSiglaUF.setEditable(false);
            jTexSiglaUF.setEnabled(false);
            jTexSiglaUF.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexSiglaUFKeyPressed(evt);
                }
            });

            jTexCdMunicipioIbge.setEditable(false);
            jTexCdMunicipioIbge.setEnabled(false);

            jLabCep.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCep.setText("C.E.P:");

            jForCep.setEditable(false);
            try {
                jForCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForCep.setEnabled(false);

            jTexCdUfIbge.setEditable(false);
            jTexCdUfIbge.setEnabled(false);

            jButValidarCep.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButValidarCep.setText("Validar CEP");
            jButValidarCep.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButValidarCepActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanEnderecoLayout = new javax.swing.GroupLayout(jPanEndereco);
            jPanEndereco.setLayout(jPanEnderecoLayout);
            jPanEnderecoLayout.setHorizontalGroup(
                jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanEnderecoLayout.createSequentialGroup()
                    .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanEnderecoLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabComplemento))
                        .addComponent(jLabCidade, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanEnderecoLayout.createSequentialGroup()
                            .addComponent(jComTipoLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabNumero)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanEnderecoLayout.createSequentialGroup()
                            .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTexCdUfIbge, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                .addComponent(jTexSiglaUF))
                            .addGap(16, 16, 16)
                            .addComponent(jLabMunicipio)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTexMuncipio, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexCdMunicipioIbge, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanEnderecoLayout.createSequentialGroup()
                                    .addComponent(jLabCep)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jForCep, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButValidarCep, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addGroup(jPanEnderecoLayout.createSequentialGroup()
                            .addComponent(jTexComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jTexBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanEnderecoLayout.setVerticalGroup(
                jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanEnderecoLayout.createSequentialGroup()
                    .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanEnderecoLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComTipoLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabNumero)
                                .addComponent(jTexNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexLogradouro)))
                        .addGroup(jPanEnderecoLayout.createSequentialGroup()
                            .addGap(14, 14, 14)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabComplemento)
                        .addComponent(jTexComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jTexBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCidade)
                        .addComponent(jLabMunicipio)
                        .addComponent(jTexMuncipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabCep)
                        .addComponent(jForCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexSiglaUF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTexCdMunicipioIbge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexCdUfIbge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButValidarCep))
                    .addContainerGap())
            );

            jLabTipoPessoa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTipoPessoa.setText("Tipo Pessoa:");

            jComTipoPessoa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Física", "Jurídica" }));
            jComTipoPessoa.setEnabled(false);
            jComTipoPessoa.setMaximumSize(new java.awt.Dimension(70, 40));
            jComTipoPessoa.setMinimumSize(new java.awt.Dimension(70, 20));
            jComTipoPessoa.setPreferredSize(new java.awt.Dimension(70, 20));
            jComTipoPessoa.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    jComTipoPessoaItemStateChanged(evt);
                }
            });

            jLabOptanteSimples.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabOptanteSimples.setText("Optante Simples:");

            jComOptanteSimples.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não" }));
            jComOptanteSimples.setMaximumSize(new java.awt.Dimension(70, 40));
            jComOptanteSimples.setMinimumSize(new java.awt.Dimension(70, 20));
            jComOptanteSimples.setPreferredSize(new java.awt.Dimension(70, 20));

            jTexApelido.setEditable(false);
            jTexApelido.setEnabled(false);

            jPanContato.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Contato", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

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

            jLabNomeContato.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabNomeContato.setText("Contato:");

            javax.swing.GroupLayout jPanContatoLayout = new javax.swing.GroupLayout(jPanContato);
            jPanContato.setLayout(jPanContatoLayout);
            jPanContatoLayout.setHorizontalGroup(
                jPanContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanContatoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabEmail)
                        .addComponent(jLabNomeContato))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanContatoLayout.createSequentialGroup()
                            .addComponent(jTexEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 487, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanContatoLayout.createSequentialGroup()
                            .addComponent(jTexNomeContato, javax.swing.GroupLayout.PREFERRED_SIZE, 379, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabTelefone)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabCelular)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForCelular)))
                    .addContainerGap())
            );
            jPanContatoLayout.setVerticalGroup(
                jPanContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanContatoLayout.createSequentialGroup()
                    .addGroup(jPanContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabTelefone)
                        .addComponent(jForTelefone, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabCelular)
                        .addComponent(jForCelular, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabNomeContato)
                        .addComponent(jTexNomeContato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanContatoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabEmail)
                        .addComponent(jTexEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(0, 8, Short.MAX_VALUE))
            );

            jPanInformacoesBancarias.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações Bancárias", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jLabNumBanco.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabNumBanco.setText("Num. Banco:");

            jLabNomeBanco.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabNomeBanco.setText("Nome Banco:");

            jLabAgenciaBanco.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabAgenciaBanco.setText("Agência:");

            jLabNumContaBanco.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabNumContaBanco.setText("Conta:");

            javax.swing.GroupLayout jPanInformacoesBancariasLayout = new javax.swing.GroupLayout(jPanInformacoesBancarias);
            jPanInformacoesBancarias.setLayout(jPanInformacoesBancariasLayout);
            jPanInformacoesBancariasLayout.setHorizontalGroup(
                jPanInformacoesBancariasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanInformacoesBancariasLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jLabNumBanco)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexNumBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabNomeBanco)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexNomeBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 184, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabAgenciaBanco)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexAgenciaBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabNumContaBanco)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexNumContaBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanInformacoesBancariasLayout.setVerticalGroup(
                jPanInformacoesBancariasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanInformacoesBancariasLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanInformacoesBancariasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabNumBanco)
                        .addComponent(jTexNumBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabNomeBanco)
                        .addComponent(jTexNomeBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabAgenciaBanco)
                        .addComponent(jTexAgenciaBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabNumContaBanco)
                        .addComponent(jTexNumContaBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jPanInformacoesFinanceiras.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informações Financeiras", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jLabCdPortador.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdPortador.setText("Portador:");

            jTexCdPortador.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCdPortadorKeyPressed(evt);
                }
            });

            jLabCdTipoPagamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdTipoPagamento.setText("Tipo Pagamento:");

            jTexNomePortador.setEnabled(false);

            jTexCdTipoPagamento.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCdTipoPagamentoKeyPressed(evt);
                }
            });

            jTexNomeTipoPagamento.setEnabled(false);

            jLabel7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel7.setText("Condição Pagamento:");

            jTexCdCondPag.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCdCondPagKeyPressed(evt);
                }
            });

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
                    .addGroup(jPanInformacoesFinanceirasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(jTexNomeTipoPagamento, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTexNomePortador, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTexNomeCondPag, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE))
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

            javax.swing.GroupLayout jPanSecundarioLayout = new javax.swing.GroupLayout(jPanSecundario);
            jPanSecundario.setLayout(jPanSecundarioLayout);
            jPanSecundarioLayout.setHorizontalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabNomeTecnico, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabOrgaoExpedidor, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                    .addComponent(jTexNomeRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabSituacao)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(373, 373, 373))
                                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                    .addComponent(jTexApelido, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                            .addGap(8, 8, 8)
                            .addComponent(jLabOptanteSimples)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComOptanteSimples, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jPanEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jPanContato, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanInformacoesBancarias, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanInformacoesFinanceiras, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jPanBotoes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                    .addGap(47, 47, 47)
                    .addComponent(jLabCpfCnpj)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForCdCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabInscEstadual)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForInscEstadual, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(91, 91, 91)
                    .addComponent(jLabTipoPessoa)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jComTipoPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );
            jPanSecundarioLayout.setVerticalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabTipoPessoa)
                            .addComponent(jComTipoPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabCpfCnpj)
                            .addComponent(jForCdCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabInscEstadual)
                            .addComponent(jForInscEstadual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGap(9, 9, 9)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabSituacao)
                        .addComponent(jLabNomeTecnico)
                        .addComponent(jTexNomeRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabOrgaoExpedidor)
                        .addComponent(jTexApelido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabOptanteSimples)
                        .addComponent(jComOptanteSimples, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanContato, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanInformacoesBancarias, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanInformacoesFinanceiras, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

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
                .addComponent(jPanSecundario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 867, javax.swing.GroupLayout.PREFERRED_SIZE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanSecundario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(2, 2, 2))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        controleBotoes(ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO);
        if (jForCdCpfCnpj.getText().isEmpty() || jTexNomeRazaoSocial.getText().isEmpty() || jComSituacao.getSelectedItem().toString().substring(0, 1) == " "
                || jForInscEstadual.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Os campos CPF / CNPJ, Nome/Razão Social e Situacao precisam ser preenchidos corretamente!");
        } else {
            DataSistema dat = new DataSistema();
            Fornecedores fornec = new Fornecedores();
            String data = null;
            String cpfCnpj = jForCdCpfCnpj.getText().trim();
            cpfCnpj = cpfCnpj.replace(".", "");
            cpfCnpj = cpfCnpj.replace("-", "");
            cpfCnpj = cpfCnpj.replace("/", "");
            fornec.setCdCpfCnpj(cpfCnpj);
            fornec.setCdInscEstadual(jForInscEstadual.getText().trim().toUpperCase());
            fornec.setTipoPessoa(jComTipoPessoa.getSelectedItem().toString().substring(0, 1));
            fornec.setNomeRazaoSocial(jTexNomeRazaoSocial.getText().trim().toUpperCase());
            fornec.setApelido(jTexApelido.getText().trim().toUpperCase());
            fornec.setOptanteSimples(jComOptanteSimples.getSelectedItem().toString().substring(0, 1));
            fornec.setTipoLogradouro(jComTipoLogradouro.getSelectedItem().toString().trim());
            fornec.setLogradouro(jTexLogradouro.getText().trim().toUpperCase());
            fornec.setNumero(jTexNumero.getText().trim().toUpperCase());
            fornec.setComplemento(jTexComplemento.getText().trim().toUpperCase());
            fornec.setBairro(jTexBairro.getText().trim().toUpperCase());
            fornec.setCdMunicipioIbge(jTexCdMunicipioIbge.getText().trim());
            fornec.setSiglaUf(jTexSiglaUF.getText().toUpperCase().toString().substring(jTexSiglaUF.getText().trim().length() - 2, 2));
            fornec.setCdCep(jForCep.getText().toString().trim());
            fornec.setNumBanco(jTexNumBanco.getText().trim().toUpperCase());
            fornec.setNomeBanco(jTexNomeBanco.getText().trim().toUpperCase());
            fornec.setAgenciaBanco(jTexAgenciaBanco.getText());
            fornec.setNumContaBanco(jTexNumContaBanco.getText());
            fornec.setCdPortador(jTexCdPortador.getText());
            fornec.setCdTipoPagamento(jTexCdTipoPagamento.getText());
            fornec.setCdCondPagamento(jTexCdCondPag.getText());
            fornec.setNomeContato(jTexNomeContato.getText().toUpperCase());
            fornec.setTelefone(jForTelefone.getText());
            fornec.setCelular(jForCelular.getText());
            fornec.setEmail(jTexEmail.getText().trim());
            fornec.setUsuarioCadastro(su.getUsuarioConectado());
            dat.setData(data);
            data = dat.getData();
            fornec.setDataCadastro(data);
            fornec.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1));
            FornecedoresDAO fordao = null;
            sql = "SELECT * FROM GCSFORNECEDORES WHERE CPF_CNPJ = '" + cpfCnpj
                    + "'";
            try {
                fordao = new FornecedoresDAO(conexao);
                if ("N".equals(oper)) {
                    fordao.adicionar(fornec);
                } else {
                    fordao.atualizar(fornec);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ManterFornecedores.class.getName()).log(Level.SEVERE, null, ex);
            }
            limparTela();
            bloquearCampos();
            pesquisar();
        }
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:
        controleBotoes(ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO);
        if (jTexNomeRazaoSocial.getText().isEmpty()) {
            sql = "SELECT * FROM GCSFORNECEDORES";
        } else {
            sql = "SELECT * FROM GCSFORNECEDORES WHERE NOME_RAZAOSOCIAL LIKE '" + jTexNomeRazaoSocial.getText().trim() + "'";
        }
        bloquearCampos();
        pesquisar();

    }//GEN-LAST:event_jButPesquisarActionPerformed

    private void jButProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButProximoActionPerformed
        // TODO add your handling code here:
        idxCorr += 1;
        upRegistros();
    }//GEN-LAST:event_jButProximoActionPerformed

    private void jButAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAnteriorActionPerformed
        // TODO add your handling code here:
        idxCorr -= 1;
        upRegistros();
    }//GEN-LAST:event_jButAnteriorActionPerformed

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO);
        limparTela();
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        oper = "A";         // se estiver editando um registro do sistema, a operação do sistema será A de alteração de Registro
        controleBotoes(!ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO);
        liberarCampos();
        jForInscEstadual.requestFocus();
        jForInscEstadual.selectAll();
    }//GEN-LAST:event_jButEditarActionPerformed

    private void jMenuItemSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jMenuItemSairActionPerformed

    private void jMenuItemSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalvarActionPerformed
        // TODO add your handling code here:
        jButSalvarActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemSalvarActionPerformed

    private void jMenuItemEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEditarActionPerformed
        // TODO add your handling code here:
        jButEditarActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemEditarActionPerformed

    private void jButNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButNovoActionPerformed
        // TODO add your handling code here:
        oper = "N";
        controleBotoes(!ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO);
        novoRegistro();
    }//GEN-LAST:event_jButNovoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        jButNovoActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        controleBotoes(ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO);
        if (!jForCdCpfCnpj.getText().isEmpty()) {
            try {
                Fornecedores ff = new Fornecedores();
                String cpfCnpj = jForCdCpfCnpj.getText().trim();
                cpfCnpj = cpfCnpj.replace(".", "");
                cpfCnpj = cpfCnpj.replace("-", "");
                cpfCnpj = cpfCnpj.replace("/", "");
                ff.setCdCpfCnpj(cpfCnpj);
                FornecedoresDAO ffDAO = new FornecedoresDAO(conexao);
                ffDAO.excluir(ff);
                limparTela();
                jButPesquisarActionPerformed(evt);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Não foi possível excluir o registro da tela");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral na exclusão do registro!");
            }
        }
    }//GEN-LAST:event_jButExcluirActionPerformed

    private void jTexNomeRazaoSocialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTexNomeRazaoSocialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTexNomeRazaoSocialActionPerformed

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

    private void jButValidarCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButValidarCepActionPerformed
        try {
            buscarCep();
        } catch (IOException ex) {
            Logger.getLogger(ManterFornecedores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButValidarCepActionPerformed

    private void jComTipoPessoaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComTipoPessoaItemStateChanged
        // TODO add your handling code here:
        String tipoPessoa = jComTipoPessoa.getSelectedItem().toString().substring(0, 1);
        try {
            switch (tipoPessoa) {
                case "F":
                    jForCdCpfCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
                    break;
                case "J":
                    jForCdCpfCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
                    break;
                default:
                    break;
            }

        } catch (ParseException ex) {
            Logger.getLogger(ManterFornecedores.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jComTipoPessoaItemStateChanged

    private void jTexCdPortadorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdPortadorKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomPortador();
        }
    }//GEN-LAST:event_jTexCdPortadorKeyPressed

    private void jTexCdTipoPagamentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdTipoPagamentoKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomTipPagamento();
        }
    }//GEN-LAST:event_jTexCdTipoPagamentoKeyPressed

    private void jTexCdCondPagKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdCondPagKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomCondicaoPagamento();
        }
    }//GEN-LAST:event_jTexCdCondPagKeyPressed

    private void jForCdCpfCnpjKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jForCdCpfCnpjKeyPressed
        if ("N".equals(oper)) {
            vt = new VerificarTecla();
            if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
                emp = new Empresa();
                zoomEmpresas();
            }
        }
    }//GEN-LAST:event_jForCdCpfCnpjKeyPressed

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
            java.util.logging.Logger.getLogger(ManterFornecedores.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterFornecedores.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterFornecedores.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterFornecedores.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterFornecedores(su, conexao).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButAnterior;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButEditar;
    private javax.swing.JButton jButExcluir;
    private javax.swing.JButton jButNovo;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JButton jButProximo;
    private javax.swing.JButton jButSair;
    private javax.swing.JButton jButSalvar;
    private javax.swing.JButton jButValidarCep;
    private javax.swing.JComboBox<String> jComOptanteSimples;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JComboBox<String> jComTipoLogradouro;
    private javax.swing.JComboBox<String> jComTipoPessoa;
    private javax.swing.JFormattedTextField jForCdCpfCnpj;
    private javax.swing.JFormattedTextField jForCelular;
    private javax.swing.JFormattedTextField jForCep;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JFormattedTextField jForInscEstadual;
    private javax.swing.JFormattedTextField jForTelefone;
    private javax.swing.JLabel jLabAgenciaBanco;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabCdPortador;
    private javax.swing.JLabel jLabCdTipoPagamento;
    private javax.swing.JLabel jLabCelular;
    private javax.swing.JLabel jLabCep;
    private javax.swing.JLabel jLabCidade;
    private javax.swing.JLabel jLabComplemento;
    private javax.swing.JLabel jLabCpfCnpj;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabEmail;
    private javax.swing.JLabel jLabInscEstadual;
    private javax.swing.JLabel jLabMunicipio;
    private javax.swing.JLabel jLabNomeBanco;
    private javax.swing.JLabel jLabNomeContato;
    private javax.swing.JLabel jLabNomeTecnico;
    private javax.swing.JLabel jLabNumBanco;
    private javax.swing.JLabel jLabNumContaBanco;
    private javax.swing.JLabel jLabNumero;
    private javax.swing.JLabel jLabOptanteSimples;
    private javax.swing.JLabel jLabOrgaoExpedidor;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabTelefone;
    private javax.swing.JLabel jLabTipoPessoa;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JPanel jPanBotoes;
    private javax.swing.JPanel jPanContato;
    private javax.swing.JPanel jPanEndereco;
    private javax.swing.JPanel jPanInformacoesBancarias;
    private javax.swing.JPanel jPanInformacoesFinanceiras;
    private javax.swing.JPanel jPanSecundario;
    private javax.swing.JTextField jTexAgenciaBanco;
    private javax.swing.JTextField jTexApelido;
    private javax.swing.JTextField jTexBairro;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexCdCondPag;
    private javax.swing.JTextField jTexCdMunicipioIbge;
    private javax.swing.JTextField jTexCdPortador;
    private javax.swing.JTextField jTexCdTipoPagamento;
    private javax.swing.JTextField jTexCdUfIbge;
    private javax.swing.JTextField jTexComplemento;
    private javax.swing.JTextField jTexEmail;
    private javax.swing.JTextField jTexLogradouro;
    private javax.swing.JTextField jTexMuncipio;
    private javax.swing.JTextField jTexNomeBanco;
    private javax.swing.JTextField jTexNomeCondPag;
    private javax.swing.JTextField jTexNomeContato;
    private javax.swing.JTextField jTexNomePortador;
    private javax.swing.JTextField jTexNomeRazaoSocial;
    private javax.swing.JTextField jTexNomeTipoPagamento;
    private javax.swing.JTextField jTexNumBanco;
    private javax.swing.JTextField jTexNumContaBanco;
    private javax.swing.JTextField jTexNumero;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JTextField jTexSiglaUF;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
