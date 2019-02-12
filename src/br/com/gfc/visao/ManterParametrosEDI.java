/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GFCCA0100
 */
package br.com.gfc.visao;

// Objetos de Sessão de Usuário e Conexão com o Banco
import br.com.modelo.SessaoUsuario;
import java.sql.Connection;

// Objetos Principais da Classe
import br.com.gfc.modelo.ParametrosEDI;
import br.com.gfc.controle.CParametrosEDI;

// Objetos correlatos da classe
// Objetos gerais da classe
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoInteiro;
import br.com.modelo.Empresa;
import br.com.modelo.FormatarValor;
import br.com.modelo.VerificarTecla;
import br.com.visao.PesquisarEmpresa;
import java.awt.Dialog;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.NumberFormat;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.List;
import javax.swing.JComboBox;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 26/11/2018
 * @version 0.01Beta_0917
 */
public class ManterParametrosEDI extends javax.swing.JFrame {

    // variáveis de sessão do usuário e banco de dados
    private static Connection conexao;
    private static SessaoUsuario su;

    // variáveis de objeto principal da classe
    private ParametrosEDI regCorr;
    private List< ParametrosEDI> resultado;
    private CParametrosEDI cpedi;
    private ParametrosEDI modpedi;
    private ParametrosEDI pedi;

    // variáveis de instância da classe
    private VerificarTecla vt;
    private NumberFormat formato;
    private DataSistema dat;
    private String data = null;
    private int numReg;
    private int idxCorr;
    private String sql;
    private String oper;
    private final String TABELA = "gfcparametrosedi";
    private final boolean ISBOTAO = true;

    /**
     * Método para abrir tela através do menu do programa
     *
     * @param su Objeto contendo a sessão ativa do usuário
     * @param conexao Objeto contendo a conexao ativa do usuario
     */
    public ManterParametrosEDI(SessaoUsuario su, Connection conexao) {
        this.su = su;
        this.conexao = conexao;
        setaParametros();
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
        sql = "SELECT * FROM GFCPARAMETROSEDI";
    }

    /**
     * Construtor para abrir a tela através de pesquisa do tipo de pagamento
     *
     * @param su Objeto contendo a sessão ativa do usuário
     * @param conexao Objeto contendo a conexao ativa do usuario
     * @param cdTipoPagamento String contendo o código do tipo de pagamento a
     * ser pesquisado
     */
    public ManterParametrosEDI(SessaoUsuario su, Connection conexao, String cdTipoPagamento) {
        this.su = su;
        this.conexao = conexao;
        setaParametros();
        sql = "SELECT * FROM GFCPARAMETROSEDI WHERE cd_tipopagamento = '" + cdTipoPagamento
                + "'";
        LiberarBloquearCampos(false);
        pesquisar();
        controleBotoes(!ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO);
        setModalExclusionType(Dialog.ModalExclusionType.APPLICATION_EXCLUDE);
    }

    /**
     * Método para setar os parametros comuns da classe
     */
    private void setaParametros() {
        formato = NumberFormat.getInstance();
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formatarCampos();
        LiberarBloquearCampos(false);
        controlaBoletoPersonalizado();
        controlaCodDesconto();
        controlaDiasBaixaDevolucao();
        controlaVersaoLayout();
        setLocationRelativeTo(null);
        this.dispose();
    }

    // método para setar formato do campo
    private void formatarCampos() {
        jForSequencial.setDocument(new DefineCampoInteiro());
        jForQtdeDiasBaixaDevol.setDocument(new DefineCampoInteiro());
        jForCdTipoPagamento.setDocument(new DefineCampoInteiro());
    }

    /**
     * Método privado para popular o combobox Tipo de Servico
     */
    private void popularTipoServico() {
        String sql = "select CONCAT(cd_tiposervico,'-',nome_tiposervico) as TipoServico"
                + " from gfctiposervicoedi"
                + " where cd_portador = '" + jForCdPortador.getText()
                + "' and situacao <> 'I'";
        PreparedStatement pstmt = null;
        try {
            pstmt = conexao.prepareCall(sql);
            ResultSet rs = pstmt.executeQuery();
            jComCdTipoServico.removeAllItems();
            jComCdTipoServico.addItem(" ");
            while (rs.next()) {
                jComCdTipoServico.addItem(rs.getString("TipoServico"));
            }
            pstmt.close();
        } catch (SQLException ex) {
            JOptionPane.showInputDialog(null, ex);
        }
    }

    /**
     * Método privado para popular o combobox Tipo de Carteira
     */
    private void popularTipoCarteira() {
        String sql = "select CONCAT(cd_carteira,'-',nome_carteira) as TipoCarteira"
                + " from gfccarteiraedi"
                + " where cd_portador = '" + jForCdPortador.getText()
                + "' and situacao <> 'I'";
        PreparedStatement pstmt = null;
        try {
            pstmt = conexao.prepareCall(sql);
            ResultSet rs = pstmt.executeQuery();
            jComCdCarteira.removeAllItems();
            jComCdCarteira.addItem(" ");
            while (rs.next()) {
                jComCdCarteira.addItem(rs.getString("TipoCarteira"));
            }
            pstmt.close();
        } catch (SQLException ex) {
            JOptionPane.showInputDialog(null, ex);
        }
    }

    // método para limpar tela
    private void limparTela() {
        jForCdTipoPagamento.setText("");
        jTexNomeTipoPagamento.setText("");
        jForCdPortador.setText("");
        jTexNomePortador.setText("");
        jTexCdCodigoBeneficiario.setText("");
        jForSequencial.setText("");
        jComTipoArquivo.setSelectedIndex(0);
        jTexSituacaoEdi.setText("");
        jComCdTipoServico.setSelectedIndex(0);
        jComUsarVersao.setSelectedIndex(0);
        jTexVersaoLayout.setText("");
        jComUsarBoletPerson.setSelectedIndex(0);
        jTexCodBoletoPerson.setText("");
        jComCdCarteira.setSelectedIndex(0);
        jComTpEmissaoBoleto.setSelectedIndex(0);
        jComTpEntregaBoleto.setSelectedIndex(0);
        jComCdJurosMora.setSelectedIndex(0);
        jComTpJurosMoraDiaTx.setSelectedIndex(0);
        jComCdDesconto.setSelectedIndex(0);
        jComCdBaixaDevolucao.setSelectedIndex(0);
        jForQtdeDiasBaixaDevol.setText("");
        jTextAreaMensagem1.setText("");
        jTextAreaMensagem2.setText("");
        jComSituacao.setSelectedIndex(0);
        jTexCadPor.setText("");
        jForDataCad.setText("");
        jForHoraCad.setText("");
        jTexModifPor.setText("");
        jForDataModif.setText("");
        jForHoraModif.setText("");
        jTexRegAtual.setText("");
        jTexRegTotal.setText("");
        idxCorr = 0;
        numReg = 0;
        resultado = null;
        regCorr = null;
        LiberarBloquearCampos(true);
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {
        cpedi = new CParametrosEDI(conexao, su);
        modpedi = new ParametrosEDI();
        try {
            numReg = cpedi.pesquisar(sql);
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
        modpedi = new ParametrosEDI();
        jTexRegTotal.setText(Integer.toString(numReg));
        jTexRegAtual.setText(Integer.toString(idxCorr));
        cpedi.mostrarPesquisa(modpedi, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jForCdTipoPagamento.setText(modpedi.getCdTipoPagamento());
        jTexNomeTipoPagamento.setText(modpedi.getNomeTipoPagamento());
        jForCdPortador.setText(modpedi.getCdPortador());
        jTexNomePortador.setText(modpedi.getNomePortador());
        jTexCdCodigoBeneficiario.setText(modpedi.getCdCodigoBeneficiario());
        jForSequencial.setText(String.valueOf(modpedi.getNumeroSequencial()));
        jComTipoArquivo.setSelectedItem(modpedi.getTipoArquvo());
        jTexSituacaoEdi.setText(modpedi.getSituacaoEdi());
        jComCdTipoServico.setSelectedItem(modpedi.getCdTipoServico());
        jComUsarVersao.setSelectedIndex(Integer.parseInt(modpedi.getUsarVersao()));
        jTexVersaoLayout.setText(modpedi.getVersaoLayout());
        jComUsarBoletPerson.setSelectedIndex(Integer.parseInt(modpedi.getUsarBoletoPersonalizado()));
        jTexCodBoletoPerson.setText(modpedi.getCdBoletoPerson());
        jComCdCarteira.setSelectedItem(modpedi.getCdCarteira());
        jComTpEmissaoBoleto.setSelectedIndex(Integer.parseInt(modpedi.getTipoEmissaoBoleto()));
        jComTpEntregaBoleto.setSelectedIndex(Integer.parseInt(modpedi.getTipoEntregaBoleto()));
        jComCdJurosMora.setSelectedIndex(Integer.parseInt(modpedi.getCdJurosMora()));
        jComTpJurosMoraDiaTx.setSelectedIndex(Integer.parseInt(modpedi.getTipoJurosMoraDiaTx()));
        jComCdDesconto.setSelectedIndex(Integer.parseInt(modpedi.getCdDesconto()));
        jComCdBaixaDevolucao.setSelectedIndex(Integer.parseInt(modpedi.getCdBaixaDevolucao()));
        jForQtdeDiasBaixaDevol.setText(String.valueOf(modpedi.getQtdeDiasBaixaDevol()));
        jComSituacao.setSelectedIndex(Integer.parseInt(String.valueOf(modpedi.getSituacao())));
        jTextAreaMensagem1.setText(modpedi.getMensagem1());
        jTextAreaMensagem2.setText(modpedi.getMensagem2());
        jTexCadPor.setText(modpedi.getUsuarioCadastro());
        if (modpedi.getDataCadastro() != null) {
            jForDataCad.setText(dat.getDataConv(Date.valueOf(modpedi.getDataCadastro())));
        }
        jForHoraCad.setText(modpedi.getHoraCadastro());
        jTexModifPor.setText(modpedi.getUsuarioModificacao());
        if (modpedi.getDataModificacao() != null) {
            jForDataModif.setText(dat.getDataConv(Date.valueOf(modpedi.getDataModificacao())));
            jForHoraModif.setText(modpedi.getHoraModificacao());
        }

        // Habilitando / Desabilitando botões de navegação de registros
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

    /**
     * Método para liberar ou Bloquear os campos da tela
     *
     * @param acao
     */
    private void LiberarBloquearCampos(boolean acao) {
        jForCdTipoPagamento.setEditable(acao);
        jTexCdCodigoBeneficiario.setEditable(acao);
        jForSequencial.setEditable(acao);
        jComTipoArquivo.setEditable(acao);
        jComUsarVersao.setEditable(acao);
        jTexVersaoLayout.setEditable(acao);
        jTexSituacaoEdi.setEditable(acao);
        jComCdTipoServico.setEditable(acao);
        jComUsarVersao.setEditable(acao);
        jTexVersaoLayout.setEditable(acao);
        jComUsarBoletPerson.setEditable(acao);
        jTexCodBoletoPerson.setEditable(acao);
        jComCdCarteira.setEditable(acao);
        jComTpEmissaoBoleto.setEditable(acao);
        jComTpEntregaBoleto.setEditable(acao);
        jComCdJurosMora.setEditable(acao);
        jComTpJurosMoraDiaTx.setEditable(acao);
        jComCdDesconto.setEditable(acao);
        jComCdBaixaDevolucao.setEditable(acao);
        jForQtdeDiasBaixaDevol.setEditable(acao);
        jTextAreaMensagem1.setEditable(acao);
        jTextAreaMensagem2.setEditable(acao);
        jComSituacao.setEditable(acao);
    }

    private void novoRegistro() {
        limparTela();

    }

    /**
     * Método para salvar a tela para gravação no banco de dados
     */
    private void salvarRegistro() {
        dat = new DataSistema();
        pedi = new ParametrosEDI();
        pedi.setCdTipoPagamento(jForCdTipoPagamento.getText());
        pedi.setCdPortador(jForCdPortador.getText());
        pedi.setCdCodigoBeneficiario(jTexCdCodigoBeneficiario.getText());
        pedi.setNumeroSequencial(Integer.parseInt(jForSequencial.getText()));
        pedi.setTipoArquvo(jComTipoArquivo.getSelectedItem().toString());
        pedi.setSituacaoEdi(jTexSituacaoEdi.getText());
        pedi.setCdTipoServico(jComCdTipoServico.getSelectedItem().toString().substring(0, 2));
        pedi.setUsarVersao(jComUsarVersao.getSelectedItem().toString().substring(0, 1));
        pedi.setVersaoLayout(jTexVersaoLayout.getText());
        pedi.setUsarBoletoPersonalizado(jComUsarBoletPerson.getSelectedItem().toString().substring(0, 1));
        pedi.setCdBoletoPerson(jTexCodBoletoPerson.getText());
        pedi.setCdCarteira(jComCdCarteira.getSelectedItem().toString().substring(0, 3));
        pedi.setTipoEmissaoBoleto(jComTpEmissaoBoleto.getSelectedItem().toString().substring(0, 1));
        pedi.setTipoEntregaBoleto(jComTpEntregaBoleto.getSelectedItem().toString().substring(0, 1));
        pedi.setCdJurosMora(jComCdJurosMora.getSelectedItem().toString().substring(0, 1));
        pedi.setTipoJurosMoraDiaTx(jComTpJurosMoraDiaTx.getSelectedItem().toString().substring(0, 1));
        pedi.setCdDesconto(jComCdDesconto.getSelectedItem().toString().substring(0, 1));
        pedi.setCdBaixaDevolucao(jComCdBaixaDevolucao.getSelectedItem().toString().substring(0, 1));
        if(Integer.valueOf(jComCdBaixaDevolucao.getSelectedItem().toString().substring(0, 1)) == 1){
            pedi.setQtdeDiasBaixaDevol(Integer.valueOf(jForQtdeDiasBaixaDevol.getText()));
        }
        pedi.setMensagem1(jTextAreaMensagem1.getText());
        pedi.setMensagem2(jTextAreaMensagem2.getText());
        pedi.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1));
        gravarLancamentoBanco();
    }

    /**
     * Método para gravar o lancamento no banco de dados
     *
     */
    private void gravarLancamentoBanco() {
        cpedi = new CParametrosEDI(conexao, su);
        try {
            if (cpedi.gravarRegistro(pedi, oper) > 0) {
                mensagem("Registro gravado com sucesso!");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManterParametrosEDI.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Método para controla a edição do campo data para desconto
     */
    private void controlaCodDesconto(){
        jComCdBaixaDevolucao.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(jComCdDesconto.getSelectedIndex() > 1){
                    jForDataParaDesconto.setEditable(true);
                    jForDataParaDesconto.setEnabled(true);
                }else{
                    jForDataParaDesconto.setEditable(false);
                    jForDataParaDesconto.setEnabled(false);
                }
            }
        });
    }
    
    /**
     * Método para controlar a edição do campo código do layout
     */
    private void controlaVersaoLayout(){
        jComUsarVersao.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(jComUsarVersao.getSelectedIndex() == 1){
                    jTexVersaoLayout.setEditable(true);
                    jTexVersaoLayout.setEnabled(true);
                }else{
                    jTexVersaoLayout.setEditable(false);
                    jTexVersaoLayout.setEnabled(false);
                }
            }
        });
    }
    
    /**
     * Método para controlar a edição do campo código boleto
     */
    private void controlaBoletoPersonalizado(){
        jComUsarBoletPerson.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(jComUsarBoletPerson.getSelectedIndex() == 1){
                    jTexCodBoletoPerson.setEditable(true);
                    jTexCodBoletoPerson.setEnabled(true);
                }else{
                    jTexCodBoletoPerson.setEditable(false);
                    jTexCodBoletoPerson.setEnabled(false);
                }
            }
        });
    }
    
    private void controlaDiasBaixaDevolucao(){
        jComCdBaixaDevolucao.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(jComCdBaixaDevolucao.getSelectedIndex() == 1){
                    jForQtdeDiasBaixaDevol.setEditable(true);
                    jForQtdeDiasBaixaDevol.setEnabled(true);
                }else{
                    jForQtdeDiasBaixaDevol.setEditable(false);
                    jForQtdeDiasBaixaDevol.setEnabled(false);
                }
            }
        });
    }

    /**
     * Método para dar zoom no campo Tipo de pagamento
     */
    private void zoomTipoPagamento() {
        PesquisarTipoPagamento zoom = new PesquisarTipoPagamento(this, rootPaneCheckingEnabled, "P", conexao);
        zoom.setVisible(rootPaneCheckingEnabled);
        jForCdTipoPagamento.setText(zoom.getCdTipoPagamento());
        jTexNomeTipoPagamento.setText(zoom.getNomeTipoPagamento());
        jForCdPortador.setText(zoom.getCdPortador());
        jTexNomePortador.setText(zoom.getNomePortador());
    }
    
    /**
     * Método para dar zoom no campo Código de benficiário. Irá buscar o CNPJ da empresa do grupo
     */
    private void zoomEmpresaGrupo(){
        PesquisarEmpresa zoom = new PesquisarEmpresa(this, rootPaneCheckingEnabled, "P", "EMP", conexao, su);
        zoom.setVisible(true);
        Empresa emp = zoom.getEmp();
        jTexCdCodigoBeneficiario.setText(emp.getCdCpfCnpj());
    }

    /**
     * Metodo para controlar os botoes
     *
     * @param bNo Botão novo lancamento
     * @param bEd Botão editar lancamento
     * @param bSa Botão salvar lancamento
     * @param bCa Botão cancelar ação na tela
     * @param bEx Botão excluir lancamento
     * @param bPe Botão pesquisar lancamento
     * @param bCl Botão sair da tela de lancamento
     */
    private void controleBotoes(boolean bNo, boolean bEd, boolean bSa, boolean bCa, boolean bEx, boolean bPe, boolean bCl) {
        jButNovo.setEnabled(bNo);
        jButEditar.setEnabled(bEd);
        jButSalvar.setEnabled(bSa);
        jButCancelar.setEnabled(bCa);
        jButExcluir.setEnabled(bEx);
        jButPesquisar.setEnabled(bPe);
        jButSair.setEnabled(bCl);
    }

    private void desabilitaBotNavegarcao() {
        jButProximo.setEnabled(false);
        jButAnterior.setEnabled(false);
    }

    /**
     * Método para gerar mensagem na tela
     *
     * @param msg
     */
    private void mensagem(String msg) {
        JOptionPane.showMessageDialog(null, msg);
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
        jPanRodape = new javax.swing.JPanel();
        jForDataCad = new javax.swing.JFormattedTextField();
        jForDataModif = new javax.swing.JFormattedTextField();
        jLabCadPor = new javax.swing.JLabel();
        jTexCadPor = new javax.swing.JTextField();
        jTexModifPor = new javax.swing.JTextField();
        jLabModifPor = new javax.swing.JLabel();
        jTexRegAtual = new javax.swing.JTextField();
        jLabReg = new javax.swing.JLabel();
        jTexRegTotal = new javax.swing.JTextField();
        jForHoraCad = new javax.swing.JFormattedTextField();
        jForHoraModif = new javax.swing.JFormattedTextField();
        jPanel1 = new javax.swing.JPanel();
        jSepLancamento = new javax.swing.JSeparator();
        jLabSitucao = new javax.swing.JLabel();
        jComSituacao = new javax.swing.JComboBox<>();
        jLabPortador = new javax.swing.JLabel();
        jForCdPortador = new javax.swing.JFormattedTextField();
        jTexNomePortador = new javax.swing.JTextField();
        jLabCdTipoPagamento = new javax.swing.JLabel();
        jForCdTipoPagamento = new javax.swing.JFormattedTextField();
        jTexNomeTipoPagamento = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabSequencial = new javax.swing.JLabel();
        jForSequencial = new FormatarValor(FormatarValor.INTEIRO);
        jLabCdCodigoBeneficiario = new javax.swing.JLabel();
        jTexCdCodigoBeneficiario = new javax.swing.JTextField();
        jLabTipoArquivo = new javax.swing.JLabel();
        jComTipoArquivo = new javax.swing.JComboBox<>();
        jLabSituacaoEdi = new javax.swing.JLabel();
        jTexSituacaoEdi = new javax.swing.JTextField();
        jLabCdTipoServico = new javax.swing.JLabel();
        jComCdTipoServico = new javax.swing.JComboBox<>();
        jComUsarVersao = new javax.swing.JComboBox<>();
        jLabUsarVersao = new javax.swing.JLabel();
        jLabUsarBoletPerson = new javax.swing.JLabel();
        jComUsarBoletPerson = new javax.swing.JComboBox<>();
        jLabVersaoLayout = new javax.swing.JLabel();
        jTexVersaoLayout = new javax.swing.JTextField();
        jLabCodBoletoPerson = new javax.swing.JLabel();
        jTexCodBoletoPerson = new javax.swing.JTextField();
        jLabCdCarteira = new javax.swing.JLabel();
        jComCdCarteira = new javax.swing.JComboBox<>();
        jLabTpEmissaoBoleto = new javax.swing.JLabel();
        jComTpEmissaoBoleto = new javax.swing.JComboBox<>();
        jLabel1 = new javax.swing.JLabel();
        jComTpEntregaBoleto = new javax.swing.JComboBox<>();
        jLabCdJurosMora = new javax.swing.JLabel();
        jComCdJurosMora = new javax.swing.JComboBox<>();
        jLabTpJurosMoraDiaTx = new javax.swing.JLabel();
        jComTpJurosMoraDiaTx = new javax.swing.JComboBox<>();
        jLabCdDesconto = new javax.swing.JLabel();
        jComCdDesconto = new javax.swing.JComboBox<>();
        jLabDataParaDesconto = new javax.swing.JLabel();
        jForDataParaDesconto = new javax.swing.JFormattedTextField();
        jLabCdBaixaDevolucao = new javax.swing.JLabel();
        jComCdBaixaDevolucao = new javax.swing.JComboBox<>();
        jLabQtdeDiasBaixaDevol = new javax.swing.JLabel();
        jForQtdeDiasBaixaDevol = new javax.swing.JFormattedTextField();
        jLabMensagem1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextAreaMensagem1 = new javax.swing.JTextArea();
        jLabMensagem2 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaMensagem2 = new javax.swing.JTextArea();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Parâmetros EDI");

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

        jPanRodape.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jForDataCad.setEditable(false);
        jForDataCad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataCad.setEnabled(false);

        jForDataModif.setEditable(false);
        jForDataModif.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataModif.setEnabled(false);

        jLabCadPor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCadPor.setText("Cadastrado:");

        jTexCadPor.setEditable(false);
        jTexCadPor.setEnabled(false);

        jTexModifPor.setEditable(false);
        jTexModifPor.setEnabled(false);

        jLabModifPor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabModifPor.setText("Modificado:");

        jTexRegAtual.setEditable(false);
        jTexRegAtual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexRegAtual.setEnabled(false);

        jLabReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabReg.setText("\\");

            jTexRegTotal.setEditable(false);
            jTexRegTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            jTexRegTotal.setEnabled(false);

            jForHoraCad.setEditable(false);
            try {
                jForHoraCad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForHoraCad.setEnabled(false);

            jForHoraModif.setEditable(false);
            try {
                jForHoraModif.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForHoraModif.setEnabled(false);

            javax.swing.GroupLayout jPanRodapeLayout = new javax.swing.GroupLayout(jPanRodape);
            jPanRodape.setLayout(jPanRodapeLayout);
            jPanRodapeLayout.setHorizontalGroup(
                jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanRodapeLayout.createSequentialGroup()
                    .addGap(13, 13, 13)
                    .addComponent(jTexRegAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(3, 3, 3)
                    .addComponent(jLabReg, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(2, 2, 2)
                    .addComponent(jTexRegTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabCadPor)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexCadPor, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataCad, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForHoraCad, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabModifPor)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexModifPor, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataModif, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForHoraModif, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(122, 122, 122))
            );
            jPanRodapeLayout.setVerticalGroup(
                jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanRodapeLayout.createSequentialGroup()
                    .addGap(11, 11, 11)
                    .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTexRegTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabReg)
                            .addComponent(jTexRegAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jForDataCad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabCadPor)
                            .addComponent(jTexCadPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabModifPor)
                            .addComponent(jTexModifPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForDataModif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForHoraCad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForHoraModif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
            );

            javax.swing.GroupLayout jPanSecundarioLayout = new javax.swing.GroupLayout(jPanSecundario);
            jPanSecundario.setLayout(jPanSecundarioLayout);
            jPanSecundarioLayout.setHorizontalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );
            jPanSecundarioLayout.setVerticalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabSitucao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabSitucao.setText("Situacão:");

            jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "A-Ativo", "I-Inativo" }));

            jLabPortador.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabPortador.setText("Portador:");

            jForCdPortador.setEditable(false);
            try {
                jForCdPortador.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForCdPortador.setEnabled(false);
            jForCdPortador.addCaretListener(new javax.swing.event.CaretListener() {
                public void caretUpdate(javax.swing.event.CaretEvent evt) {
                    jForCdPortadorCaretUpdate(evt);
                }
            });

            jTexNomePortador.setEditable(false);
            jTexNomePortador.setEnabled(false);

            jLabCdTipoPagamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdTipoPagamento.setText("Tipo Pagamento:");

            jForCdTipoPagamento.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jForCdTipoPagamentoKeyPressed(evt);
                }
            });

            jTexNomeTipoPagamento.setEditable(false);
            jTexNomeTipoPagamento.setEnabled(false);

            jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabSequencial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabSequencial.setText("N.Seq. Arquivo:");

            jForSequencial.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

            jLabCdCodigoBeneficiario.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdCodigoBeneficiario.setText("Cód. Beneficiário:");

            jTexCdCodigoBeneficiario.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCdCodigoBeneficiarioKeyPressed(evt);
                }
            });

            jLabTipoArquivo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTipoArquivo.setText("Tipo Arquivo:");

            jComTipoArquivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "CNAB240", "CNAB400" }));

            jLabSituacaoEdi.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabSituacaoEdi.setText("Situação EDI:");

            javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
            jPanel3.setLayout(jPanel3Layout);
            jPanel3Layout.setHorizontalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabCdCodigoBeneficiario)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexCdCodigoBeneficiario, javax.swing.GroupLayout.PREFERRED_SIZE, 191, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jLabSequencial)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForSequencial, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(85, 85, 85)
                            .addComponent(jLabTipoArquivo)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComTipoArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 127, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(1, 1, 1))
                        .addGroup(jPanel3Layout.createSequentialGroup()
                            .addComponent(jLabSituacaoEdi)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexSituacaoEdi, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(64, Short.MAX_VALUE))
            );
            jPanel3Layout.setVerticalGroup(
                jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel3Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabSequencial)
                        .addComponent(jForSequencial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabCdCodigoBeneficiario)
                        .addComponent(jTexCdCodigoBeneficiario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabTipoArquivo)
                        .addComponent(jComTipoArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabSituacaoEdi)
                        .addComponent(jTexSituacaoEdi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
            );

            jLabCdTipoServico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdTipoServico.setText("Tipo Serviço:");

            jComCdTipoServico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", " " }));

            jComUsarVersao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não" }));

            jLabUsarVersao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabUsarVersao.setText("Usar versão de layout:");

            jLabUsarBoletPerson.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabUsarBoletPerson.setText("Usuar Boleto Pernosalizado:");

            jComUsarBoletPerson.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não" }));
            jComUsarBoletPerson.setMaximumSize(new java.awt.Dimension(49, 22));

            jLabVersaoLayout.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabVersaoLayout.setText("Versão Layout:");

            jTexVersaoLayout.setEditable(false);
            jTexVersaoLayout.setEnabled(false);

            jLabCodBoletoPerson.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCodBoletoPerson.setText("Cód. Boleto Person.:");

            jTexCodBoletoPerson.setEditable(false);
            jTexCodBoletoPerson.setEnabled(false);

            jLabCdCarteira.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdCarteira.setText("Cód. Carteira:");

            jComCdCarteira.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", " " }));

            jLabTpEmissaoBoleto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTpEmissaoBoleto.setText("Tipo de emissão do boleto:");

            jComTpEmissaoBoleto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "1-Banco Emite", "2-Cliente Emite", "4-Banco Reemite", "5-Banco não Emite" }));

            jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel1.setText("Entrega Boleto:");

            jComTpEntregaBoleto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "0-Postagem Beneficiario", "1-Postagem Banco", "2-Envio p/ Agencia", "3-Pagador via E-mail", "4-Pagador via SMS", " " }));

            jLabCdJurosMora.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdJurosMora.setText("Cód. Juros Mora:");

            jComCdJurosMora.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "1-Valor dia", "2-Taxa Mensal", "3-Isento" }));

            jLabTpJurosMoraDiaTx.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTpJurosMoraDiaTx.setText("Juros Mora/Dia taxa:");

            jComTpJurosMoraDiaTx.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "1-Informar Valor", "2-Informar Percentual", "3-Informar zeros" }));

            jLabCdDesconto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdDesconto.setText("Cód. Desc.:");

            jComCdDesconto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "0-Sem desconto", "1-Valor fixo até Data:", "2-Valor Perc. até Data:" }));

            jLabDataParaDesconto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabDataParaDesconto.setText("Data p/ Desconto:");

            jForDataParaDesconto.setEditable(false);
            try {
                jForDataParaDesconto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForDataParaDesconto.setEnabled(false);

            jLabCdBaixaDevolucao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdBaixaDevolucao.setText("Cód. p/ baixar devolução:");

            jComCdBaixaDevolucao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "1-Baixar e Devolver", "2-Não Baixar e Não Devolver" }));

            jLabQtdeDiasBaixaDevol.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabQtdeDiasBaixaDevol.setText("Num. dias p/ baixar devolução:");

            jLabMensagem1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabMensagem1.setText("Mensagem1:");

            jTextAreaMensagem1.setColumns(20);
            jTextAreaMensagem1.setRows(5);
            jTextAreaMensagem1.setMaximumSize(new java.awt.Dimension(775, 44));
            jTextAreaMensagem1.setMinimumSize(new java.awt.Dimension(775, 44));
            jScrollPane1.setViewportView(jTextAreaMensagem1);

            jLabMensagem2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabMensagem2.setText("Mensagem1:");

            jTextAreaMensagem2.setColumns(20);
            jTextAreaMensagem2.setRows(5);
            jTextAreaMensagem2.setMaximumSize(new java.awt.Dimension(775, 44));
            jTextAreaMensagem2.setMinimumSize(new java.awt.Dimension(775, 44));
            jScrollPane2.setViewportView(jTextAreaMensagem2);

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(53, 53, 53)
                            .addComponent(jLabPortador)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForCdPortador, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexNomePortador, javax.swing.GroupLayout.PREFERRED_SIZE, 495, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addGap(262, 262, 262)
                            .addComponent(jLabTpEmissaoBoleto)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComTpEmissaoBoleto, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComTpEntregaBoleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabCdTipoServico)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jComCdTipoServico, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabUsarVersao)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jComUsarVersao, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabCdCarteira)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jComCdCarteira, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabVersaoLayout)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTexVersaoLayout, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addComponent(jLabUsarBoletPerson)
                                            .addGap(5, 5, 5)
                                            .addComponent(jComUsarBoletPerson, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(0, 0, Short.MAX_VALUE)
                                            .addComponent(jLabCodBoletoPerson)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTexCodBoletoPerson, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(2, 2, 2))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabMensagem2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane2))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabMensagem1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jScrollPane1)
                                        .addGap(4, 4, 4))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabCdJurosMora)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComCdJurosMora, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabTpJurosMoraDiaTx)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComTpJurosMoraDiaTx, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabCdDesconto)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jComCdDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabDataParaDesconto)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jForDataParaDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabSitucao)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jSepLancamento)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabCdBaixaDevolucao)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jComCdBaixaDevolucao, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabQtdeDiasBaixaDevol)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jForQtdeDiasBaixaDevol, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabCdTipoPagamento)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jForCdTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTexNomeTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                    .addContainerGap())
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addGap(4, 4, 4)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabSitucao)
                        .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabCdTipoPagamento)
                        .addComponent(jForCdTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(3, 3, 3)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabPortador)
                        .addComponent(jForCdPortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomePortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jSepLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCdTipoServico)
                        .addComponent(jComCdTipoServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabUsarVersao)
                        .addComponent(jComUsarVersao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabUsarBoletPerson)
                        .addComponent(jComUsarBoletPerson, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabCdCarteira)
                            .addComponent(jComCdCarteira, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabVersaoLayout)
                            .addComponent(jTexVersaoLayout, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabCodBoletoPerson)
                            .addComponent(jTexCodBoletoPerson, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabTpEmissaoBoleto)
                        .addComponent(jComTpEmissaoBoleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1)
                        .addComponent(jComTpEntregaBoleto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCdJurosMora)
                        .addComponent(jComCdJurosMora, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabTpJurosMoraDiaTx)
                        .addComponent(jComTpJurosMoraDiaTx, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabCdDesconto)
                        .addComponent(jComCdDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabDataParaDesconto)
                        .addComponent(jForDataParaDesconto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCdBaixaDevolucao)
                        .addComponent(jComCdBaixaDevolucao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabQtdeDiasBaixaDevol)
                        .addComponent(jForQtdeDiasBaixaDevol, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabMensagem1)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabMensagem2)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE))
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
                .addComponent(jPanSecundario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanSecundario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        if ("N".equals(oper) && jForCdTipoPagamento.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Para novo parâmetro, é necessário informar o tipo de pagamento!");
        } else {
            salvarRegistro();
            limparTela();
            LiberarBloquearCampos(false);
            controleBotoes(ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
            pesquisar();
        }
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:

        sql = "SELECT * FROM GFCPARAMETROSEDI WHERE CD_TIPOPAGAMENTO LIKE '%" + jForCdTipoPagamento.getText()
                + "%'";
        LiberarBloquearCampos(false);
        controleBotoes(ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
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
        // TODO add your handling code here:
        limparTela();
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
        desabilitaBotNavegarcao();
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        controleBotoes(!ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO);
        desabilitaBotNavegarcao();
        oper = "A";         // se estiver editando um registro do sistema, a operação do sistema será A de alteração de Registro
        LiberarBloquearCampos(true);
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
        controleBotoes(!ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO);
        desabilitaBotNavegarcao();
        novoRegistro();
    }//GEN-LAST:event_jButNovoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        jButNovoActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        // TODO add your handling code here:
        controleBotoes(ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
        if (!jForCdTipoPagamento.getText().isEmpty()) {
            try {
                ParametrosEDI cc = new ParametrosEDI();
                cc.setCdTipoPagamento(jForCdTipoPagamento.getText());
                if (cpedi.excluirRegistro(cc) > 0) {
                    limparTela();
                    jButPesquisarActionPerformed(evt);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Não foi possível excluir o registro da tela");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral na exclusão do registro!");
            }
        }
    }//GEN-LAST:event_jButExcluirActionPerformed

    private void jForCdTipoPagamentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jForCdTipoPagamentoKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomTipoPagamento();
        }
    }//GEN-LAST:event_jForCdTipoPagamentoKeyPressed

    private void jForCdPortadorCaretUpdate(javax.swing.event.CaretEvent evt) {//GEN-FIRST:event_jForCdPortadorCaretUpdate
        popularTipoServico();
        popularTipoCarteira();
    }//GEN-LAST:event_jForCdPortadorCaretUpdate

    private void jTexCdCodigoBeneficiarioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdCodigoBeneficiarioKeyPressed
        vt = new VerificarTecla();
        if("F5".equals(vt.VerificarTecla(evt).toUpperCase()))
            zoomEmpresaGrupo();
    }//GEN-LAST:event_jTexCdCodigoBeneficiarioKeyPressed

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
            java.util.logging.Logger.getLogger(ManterParametrosEDI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterParametrosEDI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterParametrosEDI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterParametrosEDI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterParametrosEDI(su, conexao).setVisible(true);
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
    private javax.swing.JComboBox<String> jComCdBaixaDevolucao;
    private javax.swing.JComboBox<String> jComCdCarteira;
    private javax.swing.JComboBox<String> jComCdDesconto;
    private javax.swing.JComboBox<String> jComCdJurosMora;
    private javax.swing.JComboBox<String> jComCdTipoServico;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JComboBox<String> jComTipoArquivo;
    private javax.swing.JComboBox<String> jComTpEmissaoBoleto;
    private javax.swing.JComboBox<String> jComTpEntregaBoleto;
    private javax.swing.JComboBox<String> jComTpJurosMoraDiaTx;
    private javax.swing.JComboBox<String> jComUsarBoletPerson;
    private javax.swing.JComboBox<String> jComUsarVersao;
    private javax.swing.JFormattedTextField jForCdPortador;
    private javax.swing.JFormattedTextField jForCdTipoPagamento;
    private javax.swing.JFormattedTextField jForDataCad;
    private javax.swing.JFormattedTextField jForDataModif;
    private javax.swing.JFormattedTextField jForDataParaDesconto;
    private javax.swing.JFormattedTextField jForHoraCad;
    private javax.swing.JFormattedTextField jForHoraModif;
    private javax.swing.JFormattedTextField jForQtdeDiasBaixaDevol;
    private javax.swing.JFormattedTextField jForSequencial;
    private javax.swing.JLabel jLabCadPor;
    private javax.swing.JLabel jLabCdBaixaDevolucao;
    private javax.swing.JLabel jLabCdCarteira;
    private javax.swing.JLabel jLabCdCodigoBeneficiario;
    private javax.swing.JLabel jLabCdDesconto;
    private javax.swing.JLabel jLabCdJurosMora;
    private javax.swing.JLabel jLabCdTipoPagamento;
    private javax.swing.JLabel jLabCdTipoServico;
    private javax.swing.JLabel jLabCodBoletoPerson;
    private javax.swing.JLabel jLabDataParaDesconto;
    private javax.swing.JLabel jLabMensagem1;
    private javax.swing.JLabel jLabMensagem2;
    private javax.swing.JLabel jLabModifPor;
    private javax.swing.JLabel jLabPortador;
    private javax.swing.JLabel jLabQtdeDiasBaixaDevol;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSequencial;
    private javax.swing.JLabel jLabSituacaoEdi;
    private javax.swing.JLabel jLabSitucao;
    private javax.swing.JLabel jLabTipoArquivo;
    private javax.swing.JLabel jLabTpEmissaoBoleto;
    private javax.swing.JLabel jLabTpJurosMoraDiaTx;
    private javax.swing.JLabel jLabUsarBoletPerson;
    private javax.swing.JLabel jLabUsarVersao;
    private javax.swing.JLabel jLabVersaoLayout;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JPanel jPanRodape;
    private javax.swing.JPanel jPanSecundario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSepLancamento;
    private javax.swing.JTextField jTexCadPor;
    private javax.swing.JTextField jTexCdCodigoBeneficiario;
    private javax.swing.JTextField jTexCodBoletoPerson;
    private javax.swing.JTextField jTexModifPor;
    private javax.swing.JTextField jTexNomePortador;
    private javax.swing.JTextField jTexNomeTipoPagamento;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JTextField jTexSituacaoEdi;
    private javax.swing.JTextField jTexVersaoLayout;
    private javax.swing.JTextArea jTextAreaMensagem1;
    private javax.swing.JTextArea jTextAreaMensagem2;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
