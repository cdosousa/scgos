/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GFCMO0010
 */
package br.com.gfc.visao;

// Objetos de Sessão de Usuário e Conexão com o Banco
import br.com.gcs.visao.PesquisarFornecedores;
import br.com.gcv.visao.PesquisarClientes;
import br.com.modelo.SessaoUsuario;
import java.sql.Connection;

// Objetos Principais da Classe
import br.com.gfc.modelo.Lancamentos;
import br.com.gfc.dao.LancamentosDAO;
import br.com.gfc.controle.CLancamentos;
import br.com.gfc.modelo.Historico;
import br.com.gfr.visao.PesquisarCentroCustos;
import br.com.gsm.visao.PesquisarTecnicos;

// Objetos correlatos da classe
// Objetos gerais da classe
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.DefineCampoInteiro;
import br.com.modelo.Empresa;
import br.com.modelo.FormatarValor;
import br.com.modelo.HoraSistema;
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
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 16/02/2018
 */
public class ManterTitulos extends javax.swing.JFrame {

    // variáveis de sessão do usuário e banco de dados
    private static Connection conexao;
    private static SessaoUsuario su;

    // variáveis de objeto principal da classe
    private Lancamentos regCorr;
    private List< Lancamentos> resultado;
    private CLancamentos clan;
    private Lancamentos modlan;
    private Lancamentos lan;

    // variáveis de instância da classe
    private VerificarTecla vt;
    private NumberFormat formato;
    private DataSistema dat;
    private String data = null;
    private int numReg;
    private int idxCorr;
    private String sql;
    private String oper;
    private final String TABELA = "gfclancamentos";
    private final boolean ISBOTAO = true;
    private String prefixoLancamento;

    /**
     * Método para abrir tela através do menu do programa
     *
     * @param su Objeto contendo a sessão ativa do usuário
     * @param conexao Objeto contendo a conexao ativa do usuario
     */
    public ManterTitulos(SessaoUsuario su, Connection conexao) {
        this.su = su;
        this.conexao = conexao;
        setaParametros();
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
        sql = "SELECT * FROM GFCLANCAMENTOS";
    }

    /**
     * Construtor para abrir a tela através de pesquisa do lancamento
     *
     * @param su Objeto contendo a sessão ativa do usuário
     * @param conexao Objeto contendo a conexao ativa do usuario
     * @param cdLancamento String contendo o número do lancamento a ser
     * pesquisado
     */
    public ManterTitulos(SessaoUsuario su, Connection conexao, String cdLancamento) {
        this.su = su;
        this.conexao = conexao;
        setaParametros();
        sql = "SELECT * FROM GFCLANCAMENTOS WHERE cd_lancamento = '" + cdLancamento
                + "'";
        bloquearCampos();
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
        popularTipoLancamento();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formatarCampos();
        bloquearCampos();
        monitoraTipoLancamento();
        setLocationRelativeTo(null);
        this.dispose();
    }

    // método para setar formato do campo
    private void formatarCampos() {
        jForCdLancamento.setDocument(new DefineCampoInteiro());
        jForSequencial.setDocument(new DefineCampoInteiro());
        jForTitulo.setDocument(new DefineCampoInteiro());
        jForParcela.setDocument(new DefineCampoInteiro());
        jForValorLancamento.setDocument(new DefineCampoDecimal());
        jForValorSaldo.setDocument(new DefineCampoDecimal());
        jForValorAtualizado.setDocument(new DefineCampoDecimal());
        jForCdContraPartida.setDocument(new DefineCampoInteiro());
        jForCdTipoPagamento.setDocument(new DefineCampoInteiro());
        jForTxJuros.setDocument(new DefineCampoDecimal());
        jForTxJuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jForTxMulta.setDocument(new DefineCampoDecimal());
        jForTxMulta.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jForTxCorrecao.setDocument(new DefineCampoDecimal());
        jForTxCorrecao.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jForDiasLiquidacao.setDocument(new DefineCampoInteiro());
        jForDiasLiquidacao.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jForDiasCartorio.setDocument(new DefineCampoInteiro());
        jForDiasCartorio.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jForValorJuros.setDocument(new DefineCampoDecimal());
        jForValorJuros.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jForValorMulta.setDocument(new DefineCampoDecimal());
        jForValorMulta.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
    }

    /**
     * Método para monitorar a alteração do combo tipo lancamento
     */
    private void monitoraTipoLancamento() {
        jComTipoLancamento.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (jComTipoLancamento.getSelectedIndex() != 0) {
                    popularTipoMovimento();
                }
            }
        });
    }

    /**
     * Método privado para popular o combobox TipoLancamento
     */
    private void popularTipoLancamento() {
        String sql = "select CONCAT(cd_tipolancamento,'-',nome_tipolancamento) as TipoLancamento"
                + " from gfctipolancamento";
        PreparedStatement pstmt = null;
        try {
            pstmt = conexao.prepareCall(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                jComTipoLancamento.addItem(rs.getString("TipoLancamento"));
            }
            pstmt.close();
        } catch (SQLException ex) {
            JOptionPane.showInputDialog(null, ex);
        }
    }

    /**
     * Método privado para popular o combobox TipoMovimento
     */
    private void popularTipoMovimento() {
        String sql = "select concat(a.cd_tipomovimento,'-',m.nome_tipomovimento) as TipoMovimento,"
                + " l.prefixo as prefixo"
                + " from gfctipolancmovi as a"
                + " left outer join gfctipomovimento as m on a.cd_tipomovimento = m.cd_tipomovimento"
                + " left outer join gfctipolancamento as l on a.cd_tipolancamento = l.cd_tipolancamento"
                + " where a.cd_tipolancamento = '"
                + jComTipoLancamento.getSelectedItem().toString().substring(0, 3)
                + "'";
        PreparedStatement pstmt = null;
        try {
            pstmt = conexao.prepareCall(sql);
            ResultSet rs = pstmt.executeQuery();
            jComTipoMovimento.removeAllItems();
            jComTipoMovimento.addItem(" ");
            while (rs.next()) {
                jComTipoMovimento.addItem(rs.getString("TipoMovimento"));
                prefixoLancamento = rs.getString("prefixo");
            }
            pstmt.close();
        } catch (SQLException ex) {
            JOptionPane.showInputDialog(null, ex);
        }
    }

    // método para limpar tela
    private void limparTela() {
        jForCdLancamento.setText("");
        jForSequencial.setText("");
        jComTipoLancamento.setSelectedIndex(0);
        jComTipoMovimento.setSelectedIndex(0);
        jComSituacao.setSelectedIndex(0);
        jForTitulo.setText("");
        jForParcela.setText("");
        jForDataEmissao.setText("");
        jForDataVencimento.setText("");
        jForDataLiquidacao.setText("");
        jForValorLancamento.setText("0.00");
        jForValorSaldo.setText("0.00");
        jForValorAtualizado.setText("0.00");
        jForCpfCnpj.setText("");
        jTexNomeRazaoSocial.setText("");
        jComTipoDocumento.setSelectedIndex(0);
        jTexNumDocumento.setText("");
        jForCdTipoPagamento.setText("");
        jTexCondPgto.setText("");
        jTexNomeCondPgto.setText("");
        jTexCdHistorico.setText("");
        jTexNomeHistorico.setText("");
        jTexNomeTipoPagamento.setText("");
        jForCdContraPartida.setText("");
        jComGerouArquivo.setSelectedIndex(0);
        jTexNumeroRemessa.setText("");
        jForCdPortador.setText("");
        jTexNomePortador.setText("");
        jForConta.setText("");
        jForContaDigito.setText("");
        jForAgencia.setText("");
        jForAgenciaDigito.setText("");
        jForCdBanco.setText("");
        jTexNomeBanco.setText("");
        jForTxJuros.setText("0,00");
        jForTxMulta.setText("0,00");
        jForTxCorrecao.setText("0,00");
        jForDiasLiquidacao.setText("0");
        jForDiasCartorio.setText("0");
        jForValorJuros.setText("0,00");
        jForValorMulta.setText("0,00");
        jTexCdContaReduzida.setText("");
        jTexGerouPreparacao.setText("");
        jTexCdCCusto.setText("");
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
        liberarCampos();
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {
        clan = new CLancamentos(conexao, su);
        modlan = new Lancamentos();
        try {
            numReg = clan.pesquisar(sql);
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
        modlan = new Lancamentos();
        jTexRegTotal.setText(Integer.toString(numReg));
        jTexRegAtual.setText(Integer.toString(idxCorr));
        clan.mostrarPesquisa(modlan, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jForCdLancamento.setText(modlan.getCdLancamento());
        jForSequencial.setText(String.valueOf(modlan.getSequencial()));
        jComTipoLancamento.setSelectedIndex(Integer.parseInt(modlan.getTipoLancamento()));
        jComTipoMovimento.setSelectedItem(modlan.getTipoMovimento());
        jComSituacao.setSelectedIndex(Integer.parseInt(String.valueOf(modlan.getSituacao())));
        jForTitulo.setText(modlan.getTitulo());
        jForParcela.setText(String.valueOf(modlan.getCdParcela()));
        jForDataEmissao.setText(dat.getDataConv(Date.valueOf(modlan.getDataEmissao())));
        jForDataVencimento.setText(dat.getDataConv(Date.valueOf(modlan.getDataVencimento())));
        if (modlan.getDataLiquidacao() != null) {
            jForDataLiquidacao.setText(dat.getDataConv(Date.valueOf(modlan.getDataLiquidacao())));
        } else {
            jForDataLiquidacao.setText("");
        }
        jForValorLancamento.setText(String.valueOf(modlan.getValorLancamento()));
        jForValorSaldo.setText(String.valueOf(modlan.getValorSaldo()));
        jForValorAtualizado.setText(String.valueOf(modlan.getValorAtualizado()));
        jForCpfCnpj.setText(modlan.getCpfCnpj());
        jTexNomeRazaoSocial.setText(modlan.getNomeRazaoSocial());
        jComTipoDocumento.setSelectedIndex(Integer.parseInt(modlan.getTipoDocumento()));
        jTexNumDocumento.setText(modlan.getDocumento());
        jForCdTipoPagamento.setText(modlan.getCdTipoPagamento());
        jTexNomeTipoPagamento.setText(modlan.getNomeTipoPagamento());
        jTexCdHistorico.setText(modlan.getCdHistorico());
        jTexNomeHistorico.setText(modlan.getComplementoHistorico());
        jForCdContraPartida.setText(modlan.getContraPartida());
        jTexGerouPreparacao.setText(modlan.getPreparado());
        jComGerouArquivo.setSelectedIndex(Integer.parseInt(modlan.getGerouArquivo()));
        jTexNumeroRemessa.setText(modlan.getNossoNumeroBanco());
        jForCdPortador.setText(modlan.getCdPortador());
        jTexNomePortador.setText(modlan.getNomePortador());
        jForConta.setText(modlan.getCdConta());
        jForContaDigito.setText(modlan.getCdContaDig());
        jForAgencia.setText(modlan.getCdAgencia());
        jForAgenciaDigito.setText(modlan.getCdAgenciaDig());
        jForCdBanco.setText(modlan.getCdBanco().trim());
        jTexNomeBanco.setText(modlan.getNomeBanco().trim());
        jForTxJuros.setText(String.valueOf(modlan.getTaxaJuros()));
        jForTxMulta.setText(String.valueOf(modlan.getTaxaMulta()));
        jForTxCorrecao.setText(String.valueOf(modlan.getTaxaCorrecao()));
        jForDiasLiquidacao.setText(String.valueOf(modlan.getDiasLiquidacao()));
        jForDiasCartorio.setText(String.valueOf(modlan.getDiasCartorio()));
        jForValorJuros.setText(String.valueOf(modlan.getValorJuros()));
        jForValorMulta.setText(String.valueOf(modlan.getValorMulta()));
        jTexCdContaReduzida.setText(modlan.getCdContaReduzida());
        jTexCdCCusto.setText(modlan.getCdCCusto());
        jTexCadPor.setText(modlan.getUsuarioCadastro());
        if (modlan.getDataCadastro() != null) {
            jForDataCad.setText(dat.getDataConv(Date.valueOf(modlan.getDataCadastro())));
        }
        jForHoraCad.setText(modlan.getHoraCadastro());
        jTexModifPor.setText(modlan.getUsuarioModificacao());
        if (modlan.getDataModificacao() != null) {
            jForDataModif.setText(dat.getDataConv(Date.valueOf(modlan.getDataModificacao())));
            jForHoraModif.setText(modlan.getHoraModificacao());
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
        if (jComSituacao.getSelectedIndex() == 1) {
            jButLiquidar.setEnabled(true);
        } else {
            jButLiquidar.setEnabled(false);
        }
    }

    //Bloquear os campos da tela
    private void bloquearCampos() {
        jForCdLancamento.setEditable(false);
        jForSequencial.setEditable(false);
        jComTipoLancamento.setEditable(false);
        jComTipoMovimento.setEditable(false);
        jForTitulo.setEditable(false);
        jForParcela.setEditable(false);
        jForDataEmissao.setEditable(false);
        jForDataVencimento.setEditable(false);
        jForDataLiquidacao.setEditable(false);
        jForValorLancamento.setEditable(false);
        jForValorSaldo.setEditable(false);
        jForValorAtualizado.setEditable(false);
        jForCpfCnpj.setEditable(false);
        jComTipoDocumento.setEditable(false);
        jTexNumDocumento.setEditable(false);
        jForCdTipoPagamento.setEditable(false);
        jTexCdHistorico.setEditable(false);
        jTexNomeHistorico.setEditable(false);
        jForCdContraPartida.setEditable(false);
        jTexGerouPreparacao.setEditable(false);
        jComGerouArquivo.setEditable(false);
        jTexNumeroRemessa.setEditable(false);
        jForCdPortador.setEditable(false);
        jTexCdContaReduzida.setEditable(false);
        jTexCdCCusto.setEditable(false);
        jForTxJuros.setEditable(false);
        jForTxMulta.setEditable(false);
        jForTxCorrecao.setEditable(false);
        jForDiasLiquidacao.setEditable(false);
        jForDiasCartorio.setEditable(false);
        jForValorJuros.setEditable(false);
        jForValorMulta.setEditable(false);
        jComSituacao.setEditable(false);
        jTexCondPgto.setEditable(false);
        jTexCondPgto.setEnabled(false);
    }

    //Loberar os campos da tela para atualização
    private void liberarCampos() {
        jForCdLancamento.setEditable(true);
        jForSequencial.setEditable(true);
        jComTipoLancamento.setEditable(true);
        jComTipoMovimento.setEditable(true);
        jForTitulo.setEditable(true);
        jForParcela.setEditable(true);
        jForDataEmissao.setEditable(true);
        jForDataVencimento.setEditable(true);
        jForDataLiquidacao.setEditable(true);
        jForValorLancamento.setEditable(true);
        jForValorSaldo.setEditable(true);
        jForCpfCnpj.setEditable(true);
        jComTipoDocumento.setEditable(true);
        jTexNumDocumento.setEditable(true);
        jForCdTipoPagamento.setEditable(true);
        jTexCdHistorico.setEditable(true);
        jTexNomeHistorico.setEditable(true);
        jForCdPortador.setEditable(true);
        jTexCdContaReduzida.setEditable(true);
        jTexCdCCusto.setEditable(true);
        jForTxJuros.setEditable(true);
        jForTxMulta.setEditable(true);
        jForTxCorrecao.setEditable(true);
        jForDiasLiquidacao.setEditable(true);
        jForDiasCartorio.setEditable(true);
        jForValorJuros.setEditable(true);
        jForValorMulta.setEditable(true);
        jComSituacao.setEditable(true);
    }

    private void novoRegistro() {
        limparTela();
        jComSituacao.setSelectedIndex(1);
        jComGerouArquivo.setSelectedIndex(2);
        jTexGerouPreparacao.setText("N");
        jForCdLancamento.setEditable(false);
        jComTipoLancamento.requestFocus();
        jTexCondPgto.setEditable(true);
        jTexCondPgto.setEnabled(true);
    }

    /**
     * Método para fazer a liquidação do título
     */
    private void liquidarTitulo() {
        ManterLiquidacaoTitulo liquidar = new ManterLiquidacaoTitulo(this, rootPaneCheckingEnabled, conexao, su, jForCdLancamento.getText());
        liquidar.setVisible(true);
        pesquisar();
    }

    /**
     * Método para salvar a tela para gravação no banco de dados
     */
    private void salvarLancamento() {
        if (jComTipoLancamento.getSelectedItem().toString().substring(0, 1) == " "
                || jComTipoMovimento.getSelectedItem().toString().substring(0, 1) == " "
                || jForValorLancamento.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Os campos precisam ser preenchidos corretamente!");
        } else {
            dat = new DataSistema();
            lan = new Lancamentos();
            lan.setTipoLancamento(jComTipoLancamento.getSelectedItem().toString().substring(0, 3));
            lan.setTipoMovimento(jComTipoMovimento.getSelectedItem().toString().substring(0, 2));
            lan.setTitulo(jForTitulo.getText());
            lan.setCdParcela(Integer.parseInt(jForParcela.getText()));
            lan.setDataEmissao(dat.getDataConv(jForDataEmissao.getText()));
            lan.setDataVencimento(dat.getDataConv(jForDataVencimento.getText()));
            if (!jForDataLiquidacao.getText().replace("/", "").trim().isEmpty()) {
                JOptionPane.showMessageDialog(null, "Data de Liquidação: " + jForDataLiquidacao.getText().replace("/", ""));
                lan.setDataLiquidacao(dat.getDataConv(jForDataLiquidacao.getText().replace("/", "").trim()));
            }
            try {
                lan.setValorLancamento(formato.parse(jForValorLancamento.getText()).doubleValue());
                lan.setValorSaldo(formato.parse(jForValorSaldo.getText()).doubleValue());
                lan.setValorAtualizado(formato.parse(jForValorAtualizado.getText()).doubleValue());
                lan.setTaxaJuros(formato.parse(jForTxJuros.getText()).doubleValue());
                lan.setTaxaMulta(formato.parse(jForTxMulta.getText()).doubleValue());
                lan.setTaxaCorrecao(formato.parse(jForTxCorrecao.getText()).doubleValue());
                lan.setValorJuros(formato.parse(jForValorJuros.getText()).doubleValue());
                lan.setValorMulta(formato.parse(jForValorMulta.getText()).doubleValue());
            } catch (ParseException ex) {
                Logger.getLogger(ManterTitulos.class.getName()).log(Level.SEVERE, null, ex);
            }
            lan.setCpfCnpj(jForCpfCnpj.getText());
            lan.setTipoDocumento(jComTipoDocumento.getSelectedItem().toString().substring(0, 3).trim());
            lan.setDocumento(jTexNumDocumento.getText().toUpperCase().trim());
            lan.setCdTipoPagamento(jForCdTipoPagamento.getText());
            lan.setCdHistorico(jTexCdHistorico.getText());
            lan.setComplementoHistorico(jTexNomeHistorico.getText());
            lan.setContraPartida(jForCdContraPartida.getText());
            lan.setPreparado(jTexGerouPreparacao.getText());
            lan.setGerouArquivo(jComGerouArquivo.getSelectedItem().toString().substring(0, 1));
            lan.setNossoNumeroBanco(jTexNumeroRemessa.getText());
            lan.setCdPortador(jForCdPortador.getText());
            lan.setCdContaReduzida(jTexCdContaReduzida.getText());
            lan.setCdCCusto(jTexCdCCusto.getText());
            if (!jForDiasLiquidacao.getText().trim().isEmpty()) {
                lan.setDiasLiquidacao(Integer.parseInt(jForDiasLiquidacao.getText()));
            }
            if (!jForDiasCartorio.getText().trim().isEmpty()) {
                lan.setDiasCartorio(Integer.parseInt(jForDiasCartorio.getText()));
            }
            lan.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 2));
            gravarLancamentoBanco();
        }
    }

    /**
     * Método para gravar o lancamento no banco de dados
     *
     */
    private void gravarLancamentoBanco() {
        dat.setData(data);
        data = dat.getData();
        HoraSistema hs = new HoraSistema();
        if ("N".equals(oper)) {
            //CBuscarSequencia bs = new CBuscarSequencia(su, TABELA, 8);
            //lan.setSequencial(Integer.parseInt(bs.getRetorno()));
            //lan.setCdLancamento(prefixoLancamento + bs.getRetorno());
            clan = new CLancamentos(conexao, su);
            lan.setUsuarioCadastro(su.getUsuarioConectado());
            lan.setDataCadastro(data);
            lan.setHoraCadastro(hs.getHora());
            try {
                clan.gerarLancamento(lan, jTexCondPgto.getText());
                sql = "SELECT * FROM GFCLANCAMENTOS WHERE CPF_CNPJ = '" + jForCpfCnpj.getText().replace(".", "").replace("/", "").replace("-", "")
                        + "' and titulo = '" + jForTitulo.getText()
                        + "' and tipo_lancamento = '" + jComTipoLancamento.getSelectedItem().toString().substring(0, 3)
                        + "' and tipo_movimento = '" + jComTipoMovimento.getSelectedItem().toString().substring(0, 2)
                        + "' and tipo_documento = '" + jComTipoDocumento.getSelectedItem().toString().substring(0, 3)
                        + "'";
            } catch (SQLException ex) {
                Logger.getLogger(ManterTitulos.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            sql = "SELECT * FROM GFCLANCAMENTOS WHERE CD_LANCAMENTO = '" + jForCdLancamento.getText().trim()
                    + "'";
            try {
                LancamentosDAO landao = new LancamentosDAO(conexao);
                lan.setCdLancamento(jForCdLancamento.getText().trim().toString());
                lan.setUsuarioModificacao(su.getUsuarioConectado());
                lan.setDataModificacao(data);
                lan.setHoraModificacao(hs.getHora());
                landao.atualizar(lan);
            } catch (SQLException ex) {
                Logger.getLogger(ManterTitulos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void selecionarCredor() {
        switch (jComTipoDocumento.getSelectedIndex()) {
            case 2:
            case 4:
                zoomFornecedores();
                break;
            case 3:
                zoomTecnicos();
                break;
            case 6:
                zoomEpresas();
                break;
        }
    }

    /**
     * Método para dar zoom no campo de Clientes clientes
     */
    private void zoomClientes() {
        PesquisarClientes zoom = new PesquisarClientes(new JFrame(), rootPaneCheckingEnabled, "P", conexao);
        zoom.setVisible(rootPaneCheckingEnabled);
        jForCpfCnpj.setText(zoom.getSelecao1());
        jTexNomeRazaoSocial.setText(zoom.getSelecao2());
    }

    /**
     * Método para dar zoom no campo de Clientes e pesquisar fornecedores
     */
    private void zoomFornecedores() {
        PesquisarFornecedores zoom = new PesquisarFornecedores(new JFrame(), rootPaneCheckingEnabled, "P", conexao);
        zoom.setVisible(rootPaneCheckingEnabled);
        jForCpfCnpj.setText(zoom.getSelecao1());
        jTexNomeRazaoSocial.setText(zoom.getSelecao2());
    }

    /**
     * Método para dar zoom no campo clientes e pesquisar técnicos
     */
    private void zoomTecnicos() {
        PesquisarTecnicos zoom = new PesquisarTecnicos(new JFrame(), rootPaneCheckingEnabled, "P", conexao);
        zoom.setVisible(true);
        jForCpfCnpj.setText(zoom.getSelecao1());
        jTexNomeRazaoSocial.setText(zoom.getSelecao2());
    }

    /**
     * Método para dar zoom no campo clientes e pesquisar empresas
     */
    private void zoomEpresas() {
        PesquisarEmpresa zoom = new PesquisarEmpresa(new JFrame(), rootPaneCheckingEnabled, "P", conexao, su);
        zoom.setVisible(true);
        if (zoom.isEscolheu()) {
            Empresa x = zoom.getEmp();
            jForCpfCnpj.setText(x.getCdCpfCnpj());
            jTexNomeRazaoSocial.setText(x.getNomeRazaoSocial());
        }
    }

    /**
     * Método para dar zoom no campo Portador
     */
    private void zoomPortador() {
        PesquisarPortadores zoom = new PesquisarPortadores(new JFrame(), rootPaneCheckingEnabled, "P", conexao);
        zoom.setVisible(rootPaneCheckingEnabled);
        jForCdPortador.setText(zoom.getCdPortador());
        jTexNomePortador.setText(zoom.getNomePortador());
        jForCdBanco.setText(zoom.getCdBanco());
        jTexNomeBanco.setText(zoom.getNomeBanco());
        jForAgencia.setText(zoom.getCdAgencia());
        jForAgenciaDigito.setText(zoom.getCdAgenciaDig());
        jForConta.setText(zoom.getCdConta());
        jForContaDigito.setText(zoom.getCdContaDig());
        jForTxJuros.setText(String.valueOf(zoom.getTxJuros()));
        jForTxMulta.setText(String.valueOf(zoom.getTxMulta()));
        jForTxCorrecao.setText(String.valueOf(zoom.getTxCorrecao()));
        jForDiasLiquidacao.setText(String.valueOf(zoom.getDiasLiquidacao()));
        jForDiasCartorio.setText(String.valueOf(zoom.getDiasCartorio()));
    }

    /**
     * Método para dar zoom no campo Tipo de pagamento
     */
    private void zoomTipoPagamento() {
        PesquisarTipoPagamento zoom = new PesquisarTipoPagamento(this, rootPaneCheckingEnabled, "P", conexao);
        zoom.setVisible(rootPaneCheckingEnabled);
        jForCdTipoPagamento.setText(zoom.getCdTipoPagamento());
        jTexNomeTipoPagamento.setText(zoom.getNomeTipoPagamento());
    }

    /**
     * Método para dar zoom no campo Conta Reduzida
     */
    private void zoomContaReduzida() {
        PesquisarContasContabeis zoom = new PesquisarContasContabeis(new JFrame(), rootPaneCheckingEnabled, "P");
        zoom.setVisible(rootPaneCheckingEnabled);
        jTexCdContaReduzida.setText(zoom.getSelec1());
    }

    /**
     * Metodo para dar zoom no campo CCusto
     */
    private void zoomCCusto() {
        PesquisarCentroCustos zoom = new PesquisarCentroCustos(new JFrame(), rootPaneCheckingEnabled, "P", conexao);
        zoom.setVisible(rootPaneCheckingEnabled);
        jTexCdCCusto.setText(zoom.getSelec1());
    }

    /**
     * Método para pesquisar a condição de pagamento
     */
    private void zoomCondPagamento() {
        PesquisarCondicaoPagamento zoom = new PesquisarCondicaoPagamento(this, rootPaneCheckingEnabled, "P", conexao);
        zoom.setVisible(rootPaneCheckingEnabled);
        jTexCondPgto.setText(zoom.getCdCondPag());
        jTexNomeCondPgto.setText(zoom.getNomeCondPag());
    }
    
    /**
     * Método para pesquisar históricos financeiros
     */
    private void zoomHistorico(){
        PesquisarHistoricos zoom = new PesquisarHistoricos(this, rootPaneCheckingEnabled, "P", conexao, su);
        zoom.setVisible(rootPaneCheckingEnabled);
        if(zoom.isSalvar()){
            Historico hs = zoom.getHt();
            jTexCdHistorico.setText(hs.getCdHistorico());
            String ch = hs.getNomeHistorico();
            if("1".equals(hs.getDocumentoComplementa()))
                ch = String.format("%s", ch.trim() + " - Documento: " + jTexNumDocumento.getText());
            if("1".equals(hs.getEmpresaComplementa()))
                ch = String.format("%s", ch.trim() + " - Empresa: " + jTexNomeRazaoSocial.getText());
            if("1".equals(hs.getEmissaoComplementa()))
                ch = String.format("%s", ch.trim() + " - Emissão: " + jForDataEmissao.getText());
            jTexNomeHistorico.setText(ch.trim());
        }
    }

    /**
     * Método para pesquisar as liquidações dos títulos
     */
    private void zoomLiquidacoes() {
        String sqlLiq = "select l.cd_lancamento as Lancamento,"
                + "l.data_cadastro as Data,"
                + "l.cpf_cnpj as Cliente,"
                + "c.nome_razaosocial as Nome,"
                + "l.titulo as Titulo,"
                + "l.cd_parcela as Parcela,"
                + "l.data_emissao as Emissao,"
                + "l.data_vencimento as Vencimento,"
                + "l.data_liquidacao as Liquidacao,"
                + "l.valor_lancamento as Valor,"
                + "l.valor_saldo as Saldo,"
                + "l.situacao as Situacao"
                + " from gfclancamentos as l"
                + " left outer join pgsempresa as c on l.cpf_cnpj = c.cpf_cnpj"
                + " where l.cd_contra_partida = '" + jForCdLancamento.getText()
                + "'";
        PesquisarTitulos zoom = new PesquisarTitulos(this, rootPaneCheckingEnabled, "R", conexao, sqlLiq, data, su);
        zoom.setVisible(true);
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
        jLabCdLancamento = new javax.swing.JLabel();
        jLabTipoLancamento = new javax.swing.JLabel();
        jComTipoLancamento = new javax.swing.JComboBox<>();
        jLabTipoMovimento = new javax.swing.JLabel();
        jComTipoMovimento = new javax.swing.JComboBox<>();
        jSepLancamento = new javax.swing.JSeparator();
        jPanJuros_Cartorio = new javax.swing.JPanel();
        jLabTxJuros = new javax.swing.JLabel();
        jForTxJuros = new FormatarValor(FormatarValor.NUMERO);
        jLabTxMulta = new javax.swing.JLabel();
        jForTxMulta = new FormatarValor(FormatarValor.NUMERO)
        ;
        jLabTxCorrecao = new javax.swing.JLabel();
        jForTxCorrecao = new FormatarValor(FormatarValor.NUMERO);
        jLabDiasLiquidacao = new javax.swing.JLabel();
        jForDiasLiquidacao = new javax.swing.JFormattedTextField();
        jLabDiasCartorio = new javax.swing.JLabel();
        jForDiasCartorio = new javax.swing.JFormattedTextField();
        jLabValorJuros = new javax.swing.JLabel();
        jForValorJuros = new FormatarValor(FormatarValor.NUMERO);
        jLabValorMulta = new javax.swing.JLabel();
        jForValorMulta = new FormatarValor(FormatarValor.NUMERO);
        jLabSitucao = new javax.swing.JLabel();
        jComSituacao = new javax.swing.JComboBox<>();
        jPanTitulo = new javax.swing.JPanel();
        jLabTitulo = new javax.swing.JLabel();
        jLabParcela = new javax.swing.JLabel();
        jLabDataEmissao = new javax.swing.JLabel();
        jForDataEmissao = new javax.swing.JFormattedTextField();
        jLabDataVencimento = new javax.swing.JLabel();
        jForDataVencimento = new javax.swing.JFormattedTextField();
        jLabLiquidacao = new javax.swing.JLabel();
        jForDataLiquidacao = new javax.swing.JFormattedTextField();
        jLabValorLancamento = new javax.swing.JLabel();
        jForValorLancamento = new FormatarValor(FormatarValor.NUMERO);
        jLabValorSaldo = new javax.swing.JLabel();
        jLabValorAtualizado = new javax.swing.JLabel();
        jSepTitulo = new javax.swing.JSeparator();
        jLabCpfCnpj = new javax.swing.JLabel();
        jForCpfCnpj = new javax.swing.JFormattedTextField();
        jTexNomeRazaoSocial = new javax.swing.JTextField();
        jLabTipoDocumento = new javax.swing.JLabel();
        jComTipoDocumento = new javax.swing.JComboBox<>();
        jLabNumDocumento = new javax.swing.JLabel();
        jTexNumDocumento = new javax.swing.JTextField();
        jPanRemessaBanco = new javax.swing.JPanel();
        jLabNumeroRemessa = new javax.swing.JLabel();
        jTexNumeroRemessa = new javax.swing.JTextField();
        jLabGerouArquivo = new javax.swing.JLabel();
        jComGerouArquivo = new javax.swing.JComboBox<>();
        jLabCdContraPartida = new javax.swing.JLabel();
        jForCdContraPartida = new javax.swing.JFormattedTextField();
        jSepContraPartida = new javax.swing.JSeparator();
        jForTitulo = new javax.swing.JFormattedTextField();
        jForParcela = new javax.swing.JFormattedTextField();
        jForValorSaldo = new FormatarValor(FormatarValor.NUMERO)
        ;
        jForValorAtualizado = new FormatarValor(FormatarValor.NUMERO);
        jLabCdTipoPagamento = new javax.swing.JLabel();
        jForCdTipoPagamento = new javax.swing.JFormattedTextField();
        jTexNomeTipoPagamento = new javax.swing.JTextField();
        jLabCondPgto = new javax.swing.JLabel();
        jTexCondPgto = new javax.swing.JTextField();
        jTexNomeCondPgto = new javax.swing.JTextField();
        jLabGerouPreparacao = new javax.swing.JLabel();
        jTexGerouPreparacao = new javax.swing.JTextField();
        jLabCdHistorico = new javax.swing.JLabel();
        jTexCdHistorico = new javax.swing.JTextField();
        jTexNomeHistorico = new javax.swing.JTextField();
        jLabPortador = new javax.swing.JLabel();
        jForCdPortador = new javax.swing.JFormattedTextField();
        jTexNomePortador = new javax.swing.JTextField();
        jLabConta = new javax.swing.JLabel();
        jForConta = new javax.swing.JFormattedTextField();
        jLabDigito = new javax.swing.JLabel();
        jForContaDigito = new javax.swing.JFormattedTextField();
        jLabAgencia = new javax.swing.JLabel();
        jForAgencia = new javax.swing.JFormattedTextField();
        jForCdBanco = new javax.swing.JFormattedTextField();
        jLabNumBanco = new javax.swing.JLabel();
        jTexNomeBanco = new javax.swing.JTextField();
        jForAgenciaDigito = new javax.swing.JFormattedTextField();
        jSeparator1 = new javax.swing.JSeparator();
        jLabCdContaReduzida = new javax.swing.JLabel();
        jTexCdContaReduzida = new javax.swing.JTextField();
        jLabCdCCusto = new javax.swing.JLabel();
        jTexCdCCusto = new javax.swing.JTextField();
        jForCdLancamento = new javax.swing.JFormattedTextField();
        jLabSequencial = new javax.swing.JLabel();
        jForSequencial = new FormatarValor(FormatarValor.INTEIRO);
        jPanel2 = new javax.swing.JPanel();
        jButLiquidar = new javax.swing.JButton();
        jButLiquidações = new javax.swing.JButton();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Títulos Financeiros");

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
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanSecundarioLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(14, Short.MAX_VALUE)))
            );
            jPanSecundarioLayout.setVerticalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGap(0, 76, Short.MAX_VALUE)
                .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanSecundarioLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            );

            jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabCdLancamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdLancamento.setText("Lançamento:");

            jLabTipoLancamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTipoLancamento.setText("Tp. Lançamento:");

            jComTipoLancamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

            jLabTipoMovimento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTipoMovimento.setText("Tp. Movimento:");

            jComTipoMovimento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " " }));

            jPanJuros_Cartorio.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jLabTxJuros.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTxJuros.setText("Tx. Juros:");

            jForTxJuros.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

            jLabTxMulta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTxMulta.setText("Tx. Multa:");

            jForTxMulta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

            jLabTxCorrecao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTxCorrecao.setText("Tx. Correção:");

            jForTxCorrecao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0.00"))));

            jLabDiasLiquidacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabDiasLiquidacao.setText("Dias p/ Liquidação:");

            jForDiasLiquidacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

            jLabDiasCartorio.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabDiasCartorio.setText("Dias p/ Cartório:");

            jForDiasCartorio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));

            jLabValorJuros.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorJuros.setText("Juros:");

            jForValorJuros.setEditable(false);
            jForValorJuros.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
            jForValorJuros.setEnabled(false);

            jLabValorMulta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorMulta.setText("Multa:");

            jForValorMulta.setEditable(false);
            jForValorMulta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
            jForValorMulta.setEnabled(false);

            javax.swing.GroupLayout jPanJuros_CartorioLayout = new javax.swing.GroupLayout(jPanJuros_Cartorio);
            jPanJuros_Cartorio.setLayout(jPanJuros_CartorioLayout);
            jPanJuros_CartorioLayout.setHorizontalGroup(
                jPanJuros_CartorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanJuros_CartorioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanJuros_CartorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanJuros_CartorioLayout.createSequentialGroup()
                            .addComponent(jLabTxJuros)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForTxJuros, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabTxMulta)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForTxMulta, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jLabTxCorrecao)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForTxCorrecao, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanJuros_CartorioLayout.createSequentialGroup()
                            .addComponent(jLabDiasLiquidacao)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForDiasLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabDiasCartorio)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForDiasCartorio, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanJuros_CartorioLayout.createSequentialGroup()
                            .addComponent(jLabValorJuros)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForValorJuros, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabValorMulta)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForValorMulta, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanJuros_CartorioLayout.setVerticalGroup(
                jPanJuros_CartorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanJuros_CartorioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanJuros_CartorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabTxJuros)
                        .addComponent(jForTxJuros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabTxMulta)
                        .addComponent(jForTxMulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabTxCorrecao)
                        .addComponent(jForTxCorrecao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanJuros_CartorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabDiasLiquidacao)
                        .addComponent(jForDiasLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabDiasCartorio)
                        .addComponent(jForDiasCartorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                    .addGroup(jPanJuros_CartorioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabValorJuros)
                        .addComponent(jForValorJuros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabValorMulta)
                        .addComponent(jForValorMulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
            );

            jLabSitucao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabSitucao.setText("Situacão:");

            jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "AB-Aberto", "CA-Cancelado", "CO-Contabilizado", "LI-Liquidado" }));

            jPanTitulo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Título:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jLabTitulo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTitulo.setText("Número/ Parcela:");

            jLabParcela.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabParcela.setText("/");

            jLabDataEmissao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabDataEmissao.setText("Emissão:");

            try {
                jForDataEmissao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }

            jLabDataVencimento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabDataVencimento.setText("Vencimento:");

            try {
                jForDataVencimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }

            jLabLiquidacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabLiquidacao.setText("Liquidação:");

            try {
                jForDataLiquidacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }

            jLabValorLancamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorLancamento.setText("Valor Liquido:");

            jLabValorSaldo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorSaldo.setText("Valor Saldo:");

            jLabValorAtualizado.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabValorAtualizado.setText("Valor Atualizado:");

            jLabCpfCnpj.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCpfCnpj.setText("CPF/ CNPJ:");

            jForCpfCnpj.setEditable(false);
            jForCpfCnpj.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jForCpfCnpjKeyPressed(evt);
                }
            });

            jTexNomeRazaoSocial.setEditable(false);
            jTexNomeRazaoSocial.setEnabled(false);

            jLabTipoDocumento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTipoDocumento.setText("Tipo Documento:");

            jComTipoDocumento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "PED-Pedido", "OC-Ordem Compra", "OS-Ordem Serviço", "NFE-Nota Fiscal Entrada", "NFS-Nota Fiscal Saída", "O-Outros", " " }));
            jComTipoDocumento.addItemListener(new java.awt.event.ItemListener() {
                public void itemStateChanged(java.awt.event.ItemEvent evt) {
                    jComTipoDocumentoItemStateChanged(evt);
                }
            });

            jLabNumDocumento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabNumDocumento.setText("Num. Documento:");

            jPanRemessaBanco.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Remessa Banco:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jLabNumeroRemessa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabNumeroRemessa.setText("Nosso Número:");

            jLabGerouArquivo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabGerouArquivo.setText("Gerou Arquivo:");

            jComGerouArquivo.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Sim", "Não" }));

            javax.swing.GroupLayout jPanRemessaBancoLayout = new javax.swing.GroupLayout(jPanRemessaBanco);
            jPanRemessaBanco.setLayout(jPanRemessaBancoLayout);
            jPanRemessaBancoLayout.setHorizontalGroup(
                jPanRemessaBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanRemessaBancoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanRemessaBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabGerouArquivo)
                        .addComponent(jLabNumeroRemessa))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanRemessaBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jTexNumeroRemessa, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jComGerouArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanRemessaBancoLayout.setVerticalGroup(
                jPanRemessaBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanRemessaBancoLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanRemessaBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabGerouArquivo)
                        .addComponent(jComGerouArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanRemessaBancoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabNumeroRemessa)
                        .addComponent(jTexNumeroRemessa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap())
            );

            jLabCdContraPartida.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdContraPartida.setText("Contra-Partida:");

            jForCdContraPartida.setEditable(false);
            jForCdContraPartida.setEnabled(false);

            jLabCdTipoPagamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdTipoPagamento.setText("Tipo Pagamento:");

            jForCdTipoPagamento.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jForCdTipoPagamentoKeyPressed(evt);
                }
            });

            jTexNomeTipoPagamento.setEditable(false);
            jTexNomeTipoPagamento.setEnabled(false);

            jLabCondPgto.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
            jLabCondPgto.setText("Condição Pagamento:");
            jLabCondPgto.setEnabled(false);
            jLabCondPgto.setFocusable(false);

            jTexCondPgto.setEnabled(false);
            jTexCondPgto.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCondPgtoKeyPressed(evt);
                }
            });

            jTexNomeCondPgto.setEnabled(false);

            jLabGerouPreparacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabGerouPreparacao.setText("Preparado para liquidação?");

            jTexGerouPreparacao.setEditable(false);

            jLabCdHistorico.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
            jLabCdHistorico.setText("Histórico:");

            jTexCdHistorico.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCdHistoricoKeyPressed(evt);
                }
            });

            javax.swing.GroupLayout jPanTituloLayout = new javax.swing.GroupLayout(jPanTitulo);
            jPanTitulo.setLayout(jPanTituloLayout);
            jPanTituloLayout.setHorizontalGroup(
                jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTituloLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jSepTitulo)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanTituloLayout.createSequentialGroup()
                            .addComponent(jLabCdContraPartida)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForCdContraPartida, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabValorLancamento)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForValorLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabValorSaldo)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForValorSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabValorAtualizado)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jForValorAtualizado, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanTituloLayout.createSequentialGroup()
                            .addGroup(jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanTituloLayout.createSequentialGroup()
                                    .addGroup(jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanTituloLayout.createSequentialGroup()
                                            .addComponent(jLabTipoDocumento)
                                            .addGap(12, 12, 12)
                                            .addComponent(jComTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabNumDocumento)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTexNumDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanTituloLayout.createSequentialGroup()
                                            .addComponent(jLabCpfCnpj)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jForCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTexNomeRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jSepContraPartida, javax.swing.GroupLayout.PREFERRED_SIZE, 490, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanTituloLayout.createSequentialGroup()
                                            .addComponent(jLabCdTipoPagamento)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jForCdTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTexNomeTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(jPanTituloLayout.createSequentialGroup()
                                            .addComponent(jLabCondPgto)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTexCondPgto, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTexNomeCondPgto, javax.swing.GroupLayout.PREFERRED_SIZE, 293, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGap(18, 18, 18)
                                    .addGroup(jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanTituloLayout.createSequentialGroup()
                                            .addComponent(jLabGerouPreparacao)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jTexGerouPreparacao, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jPanRemessaBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanTituloLayout.createSequentialGroup()
                                    .addComponent(jLabTitulo)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabParcela)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForParcela, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabDataEmissao)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForDataEmissao, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabDataVencimento)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForDataVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabLiquidacao)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForDataLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanTituloLayout.createSequentialGroup()
                                    .addComponent(jLabCdHistorico)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexCdHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexNomeHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, 656, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGap(0, 0, Short.MAX_VALUE)))
                    .addContainerGap())
            );
            jPanTituloLayout.setVerticalGroup(
                jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTituloLayout.createSequentialGroup()
                    .addGroup(jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabTitulo)
                        .addComponent(jLabParcela)
                        .addComponent(jLabDataEmissao)
                        .addComponent(jForDataEmissao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabDataVencimento)
                        .addComponent(jForDataVencimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabLiquidacao)
                        .addComponent(jForDataLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jForTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jForParcela, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabValorLancamento)
                        .addComponent(jForValorLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabValorSaldo)
                        .addComponent(jLabValorAtualizado)
                        .addComponent(jForValorAtualizado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabCdContraPartida)
                        .addComponent(jForCdContraPartida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jForValorSaldo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jSepTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanTituloLayout.createSequentialGroup()
                            .addGroup(jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabTipoDocumento)
                                .addComponent(jComTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabNumDocumento)
                                .addComponent(jTexNumDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabGerouPreparacao)
                                .addComponent(jTexGerouPreparacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabCpfCnpj)
                                .addComponent(jForCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexNomeRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(18, 18, 18)
                            .addComponent(jSepContraPartida, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabCdTipoPagamento)
                                .addComponent(jForCdTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexNomeTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabCondPgto)
                                .addComponent(jTexCondPgto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexNomeCondPgto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addComponent(jPanRemessaBanco, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCdHistorico)
                        .addComponent(jTexCdHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeHistorico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGap(2, 2, 2))
            );

            jLabPortador.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabPortador.setText("Portador:");

            try {
                jForCdPortador.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForCdPortador.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jForCdPortadorKeyPressed(evt);
                }
            });

            jTexNomePortador.setEditable(false);
            jTexNomePortador.setEnabled(false);

            jLabConta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabConta.setText("Conta:");

            jForConta.setEditable(false);
            jForConta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
            jForConta.setEnabled(false);

            jLabDigito.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabDigito.setText("Dígito:");

            jForContaDigito.setEditable(false);
            jForContaDigito.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
            jForContaDigito.setEnabled(false);

            jLabAgencia.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabAgencia.setText("Agência:");

            jForAgencia.setEditable(false);
            jForAgencia.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
            jForAgencia.setEnabled(false);

            jForCdBanco.setEditable(false);
            jForCdBanco.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
            jForCdBanco.setEnabled(false);

            jLabNumBanco.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabNumBanco.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
            jLabNumBanco.setText("Núm. Banco:");

            jTexNomeBanco.setEditable(false);
            jTexNomeBanco.setEnabled(false);

            jForAgenciaDigito.setEditable(false);
            jForAgenciaDigito.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#0"))));
            jForAgenciaDigito.setEnabled(false);

            jLabCdContaReduzida.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdContaReduzida.setText("Cta Contábil Red.:");

            jTexCdContaReduzida.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCdContaReduzidaKeyPressed(evt);
                }
            });

            jLabCdCCusto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCdCCusto.setText("C.Custo:");

            jTexCdCCusto.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCdCCustoKeyPressed(evt);
                }
            });

            jLabSequencial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabSequencial.setText("Sequencial:");

            jForSequencial.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

            jPanel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jButLiquidar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
            jButLiquidar.setText("Liquidar");
            jButLiquidar.setEnabled(false);
            jButLiquidar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButLiquidarActionPerformed(evt);
                }
            });

            jButLiquidações.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
            jButLiquidações.setText("Liquidações");
            jButLiquidações.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButLiquidaçõesActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
            jPanel2.setLayout(jPanel2Layout);
            jPanel2Layout.setHorizontalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jButLiquidar)
                    .addGap(18, 18, 18)
                    .addComponent(jButLiquidações)
                    .addContainerGap(632, Short.MAX_VALUE))
            );
            jPanel2Layout.setVerticalGroup(
                jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel2Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButLiquidar)
                        .addComponent(jButLiquidações))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
            jPanel1.setLayout(jPanel1Layout);
            jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jSepLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 856, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabCdLancamento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForCdLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabTipoLancamento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComTipoLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabTipoMovimento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComTipoMovimento, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabSitucao)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabSequencial)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jForSequencial, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jPanTitulo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabPortador)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jForCdPortador, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jTexNomePortador, javax.swing.GroupLayout.PREFERRED_SIZE, 278, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jSeparator1)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabConta)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jForConta, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabDigito)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jForContaDigito, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(jLabAgencia)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jForAgencia, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jForAgenciaDigito, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabNumBanco)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jForCdBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTexNomeBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(jLabCdContaReduzida)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTexCdContaReduzida, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(jLabCdCCusto)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTexCdCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jPanJuros_Cartorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(249, 249, 249))))
                    .addGap(0, 0, Short.MAX_VALUE))
            );
            jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanel1Layout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCdLancamento)
                        .addComponent(jLabTipoLancamento)
                        .addComponent(jComTipoLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabTipoMovimento)
                        .addComponent(jComTipoMovimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabSitucao)
                        .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jForCdLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSepLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabSequencial)
                        .addComponent(jForSequencial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabPortador)
                                .addComponent(jForCdPortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexNomePortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabConta)
                                .addComponent(jForConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabDigito)
                                .addComponent(jForContaDigito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabAgencia)
                                .addComponent(jForAgencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jForAgenciaDigito, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabNumBanco)
                                .addComponent(jForCdBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexNomeBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabCdContaReduzida)
                                .addComponent(jTexCdContaReduzida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabCdCCusto)
                                .addComponent(jTexCdCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGap(16, 16, 16))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                            .addComponent(jPanJuros_Cartorio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, 888, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jPanSecundario, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanSecundario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        if ("N".equals(oper) && jTexCondPgto.getText().toString().trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Para novo lançamento, é necessário informar a condição de pagamento!");
        } else {
            salvarLancamento();
            limparTela();
            bloquearCampos();
            controleBotoes(ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
            pesquisar();
        }
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:

        sql = "SELECT * FROM GFCLANCAMENTOS WHERE CD_LANCAMENTO LIKE '%" + jForCdLancamento.getText()
                + "%' AND TITULO LIKE '%" + jForTitulo.getText().trim() + ""
                + "%' AND CPF_CNPJ LIKE '%" + jForCpfCnpj.getText().replace("/", "").replace(".", "").replace("-", "")
                + "%'";
        bloquearCampos();
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
        if ("N".equals(jTexGerouPreparacao.getText().trim().toUpperCase())) {
            controleBotoes(!ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO);
            desabilitaBotNavegarcao();
            oper = "A";         // se estiver editando um registro do sistema, a operação do sistema será A de alteração de Registro
            liberarCampos();
            if (jComSituacao.getSelectedIndex() == 1) {
                jButLiquidar.setEnabled(true);
            } else {
                jButLiquidar.setEnabled(false);
            }
        } else {
            JOptionPane.showMessageDialog(null, "O título está preparado para pagamento e não pode ser editado, cancelado ou excluído!"
                    + "\nExclua a prepação de pagamento para realizar qualquer modificação.");
        }
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
        if (!jForCdLancamento.getText().isEmpty()) {
            try {
                Lancamentos cc = new Lancamentos();
                cc.setCdLancamento(jForCdLancamento.getText());
                LancamentosDAO ccDAO = new LancamentosDAO(conexao);
                ccDAO.excluir(cc);
                limparTela();
                jButPesquisarActionPerformed(evt);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Não foi possível excluir o registro da tela");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral na exclusão do registro!");
            }
        }
    }//GEN-LAST:event_jButExcluirActionPerformed

    private void jForCpfCnpjKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jForCpfCnpjKeyPressed
        vt = new VerificarTecla();
        if (jComTipoMovimento.getSelectedItem().toString().length() > 1) {
            if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
                switch (jComTipoMovimento.getSelectedItem().toString().substring(0, 2).toUpperCase()) {
                    case "AD":
                    case "EN":
                    case "RE":
                    case "RR":
                        zoomClientes();
                        break;
                    case "PA":
                    case "PR":
                    case "SA":
                        selecionarCredor();
                        break;
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "É necessário escolher um tipo de movimento!");
        }
    }//GEN-LAST:event_jForCpfCnpjKeyPressed

    private void jForCdPortadorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jForCdPortadorKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomPortador();
        }
    }//GEN-LAST:event_jForCdPortadorKeyPressed

    private void jTexCdContaReduzidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdContaReduzidaKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomContaReduzida();
        }
    }//GEN-LAST:event_jTexCdContaReduzidaKeyPressed

    private void jTexCdCCustoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdCCustoKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomCCusto();
        }
    }//GEN-LAST:event_jTexCdCCustoKeyPressed

    private void jForCdTipoPagamentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jForCdTipoPagamentoKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomTipoPagamento();
        }
    }//GEN-LAST:event_jForCdTipoPagamentoKeyPressed

    private void jTexCondPgtoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCondPgtoKeyPressed
        if ("N".equals(oper)) {
            vt = new VerificarTecla();
            if ("F5".equals(vt.VerificarTecla(evt))) {
                zoomCondPagamento();
            }
        }
    }//GEN-LAST:event_jTexCondPgtoKeyPressed

    private void jButLiquidarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButLiquidarActionPerformed
        liquidarTitulo();
    }//GEN-LAST:event_jButLiquidarActionPerformed

    private void jButLiquidaçõesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButLiquidaçõesActionPerformed
        zoomLiquidacoes();
    }//GEN-LAST:event_jButLiquidaçõesActionPerformed

    private void jComTipoDocumentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComTipoDocumentoItemStateChanged
        if (jComTipoDocumento.getSelectedIndex() != 0) {
            jForCpfCnpj.setEditable(true);
            jForCpfCnpj.setEnabled(true);
        } else {
            jForCpfCnpj.setEditable(false);
            jForCpfCnpj.setEnabled(false);
        }
    }//GEN-LAST:event_jComTipoDocumentoItemStateChanged

    private void jTexCdHistoricoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdHistoricoKeyPressed
        vt = new VerificarTecla();
        if("F5".equals(vt.VerificarTecla(evt)))
            zoomHistorico();
    }//GEN-LAST:event_jTexCdHistoricoKeyPressed

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
            java.util.logging.Logger.getLogger(ManterTitulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterTitulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterTitulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterTitulos.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterTitulos(su, conexao).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButAnterior;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButEditar;
    private javax.swing.JButton jButExcluir;
    private javax.swing.JButton jButLiquidar;
    private javax.swing.JButton jButLiquidações;
    private javax.swing.JButton jButNovo;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JButton jButProximo;
    private javax.swing.JButton jButSair;
    private javax.swing.JButton jButSalvar;
    private javax.swing.JComboBox<String> jComGerouArquivo;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JComboBox<String> jComTipoDocumento;
    private javax.swing.JComboBox<String> jComTipoLancamento;
    private javax.swing.JComboBox<String> jComTipoMovimento;
    private javax.swing.JFormattedTextField jForAgencia;
    private javax.swing.JFormattedTextField jForAgenciaDigito;
    private javax.swing.JFormattedTextField jForCdBanco;
    private javax.swing.JFormattedTextField jForCdContraPartida;
    private javax.swing.JFormattedTextField jForCdLancamento;
    private javax.swing.JFormattedTextField jForCdPortador;
    private javax.swing.JFormattedTextField jForCdTipoPagamento;
    private javax.swing.JFormattedTextField jForConta;
    private javax.swing.JFormattedTextField jForContaDigito;
    private javax.swing.JFormattedTextField jForCpfCnpj;
    private javax.swing.JFormattedTextField jForDataCad;
    private javax.swing.JFormattedTextField jForDataEmissao;
    private javax.swing.JFormattedTextField jForDataLiquidacao;
    private javax.swing.JFormattedTextField jForDataModif;
    private javax.swing.JFormattedTextField jForDataVencimento;
    private javax.swing.JFormattedTextField jForDiasCartorio;
    private javax.swing.JFormattedTextField jForDiasLiquidacao;
    private javax.swing.JFormattedTextField jForHoraCad;
    private javax.swing.JFormattedTextField jForHoraModif;
    private javax.swing.JFormattedTextField jForParcela;
    private javax.swing.JFormattedTextField jForSequencial;
    private javax.swing.JFormattedTextField jForTitulo;
    private javax.swing.JFormattedTextField jForTxCorrecao;
    private javax.swing.JFormattedTextField jForTxJuros;
    private javax.swing.JFormattedTextField jForTxMulta;
    private javax.swing.JFormattedTextField jForValorAtualizado;
    private javax.swing.JFormattedTextField jForValorJuros;
    private javax.swing.JFormattedTextField jForValorLancamento;
    private javax.swing.JFormattedTextField jForValorMulta;
    private javax.swing.JFormattedTextField jForValorSaldo;
    private javax.swing.JLabel jLabAgencia;
    private javax.swing.JLabel jLabCadPor;
    private javax.swing.JLabel jLabCdCCusto;
    private javax.swing.JLabel jLabCdContaReduzida;
    private javax.swing.JLabel jLabCdContraPartida;
    private javax.swing.JLabel jLabCdHistorico;
    private javax.swing.JLabel jLabCdLancamento;
    private javax.swing.JLabel jLabCdTipoPagamento;
    private javax.swing.JLabel jLabCondPgto;
    private javax.swing.JLabel jLabConta;
    private javax.swing.JLabel jLabCpfCnpj;
    private javax.swing.JLabel jLabDataEmissao;
    private javax.swing.JLabel jLabDataVencimento;
    private javax.swing.JLabel jLabDiasCartorio;
    private javax.swing.JLabel jLabDiasLiquidacao;
    private javax.swing.JLabel jLabDigito;
    private javax.swing.JLabel jLabGerouArquivo;
    private javax.swing.JLabel jLabGerouPreparacao;
    private javax.swing.JLabel jLabLiquidacao;
    private javax.swing.JLabel jLabModifPor;
    private javax.swing.JLabel jLabNumBanco;
    private javax.swing.JLabel jLabNumDocumento;
    private javax.swing.JLabel jLabNumeroRemessa;
    private javax.swing.JLabel jLabParcela;
    private javax.swing.JLabel jLabPortador;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSequencial;
    private javax.swing.JLabel jLabSitucao;
    private javax.swing.JLabel jLabTipoDocumento;
    private javax.swing.JLabel jLabTipoLancamento;
    private javax.swing.JLabel jLabTipoMovimento;
    private javax.swing.JLabel jLabTitulo;
    private javax.swing.JLabel jLabTxCorrecao;
    private javax.swing.JLabel jLabTxJuros;
    private javax.swing.JLabel jLabTxMulta;
    private javax.swing.JLabel jLabValorAtualizado;
    private javax.swing.JLabel jLabValorJuros;
    private javax.swing.JLabel jLabValorLancamento;
    private javax.swing.JLabel jLabValorMulta;
    private javax.swing.JLabel jLabValorSaldo;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JPanel jPanJuros_Cartorio;
    private javax.swing.JPanel jPanRemessaBanco;
    private javax.swing.JPanel jPanRodape;
    private javax.swing.JPanel jPanSecundario;
    private javax.swing.JPanel jPanTitulo;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JSeparator jSepContraPartida;
    private javax.swing.JSeparator jSepLancamento;
    private javax.swing.JSeparator jSepTitulo;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTexCadPor;
    private javax.swing.JTextField jTexCdCCusto;
    private javax.swing.JTextField jTexCdContaReduzida;
    private javax.swing.JTextField jTexCdHistorico;
    private javax.swing.JTextField jTexCondPgto;
    private javax.swing.JTextField jTexGerouPreparacao;
    private javax.swing.JTextField jTexModifPor;
    private javax.swing.JTextField jTexNomeBanco;
    private javax.swing.JTextField jTexNomeCondPgto;
    private javax.swing.JTextField jTexNomeHistorico;
    private javax.swing.JTextField jTexNomePortador;
    private javax.swing.JTextField jTexNomeRazaoSocial;
    private javax.swing.JTextField jTexNomeTipoPagamento;
    private javax.swing.JTextField jTexNumDocumento;
    private javax.swing.JTextField jTexNumeroRemessa;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
