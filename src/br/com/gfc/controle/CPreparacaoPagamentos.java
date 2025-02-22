/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.DAO.ConsultaModelo;
import br.com.controle.CCodigoBarras;
import br.com.controle.CEmpresa;
import br.com.controle.CPosicionarArquivo;
import br.com.controle.CRelatorio;
import br.com.gfc.dao.LancamentosDAO;
import br.com.gfc.modelo.Portadores;
import br.com.gfc.dao.PreparacaoPagamentosDAO;
import br.com.gfc.modelo.ArquivoCNAB;
import br.com.gfc.modelo.Lancamentos;
import br.com.gfc.modelo.ParametrosEDI;
import br.com.gfc.modelo.PreparacaoPagamentos;
import br.com.gfc.modelo.PreparacaoTitulos;
import br.com.gfc.modelo.TipoMovimento;
import br.com.gfc.modelo.TipoPagamento;
import br.com.modelo.CodigoBarras;
import br.com.modelo.DataSistema;
import br.com.modelo.Empresa;
import br.com.modelo.HoraSistema;
import br.com.modelo.ParametrosGerais;
import br.com.modelo.SessaoUsuario;
import java.io.InputStream;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 17/09/2018
 */
public class CPreparacaoPagamentos {

    // variáveis de instancia
    private Connection conexao;
    private List<PreparacaoPagamentos> resultAgendamento = null;
    private List<PreparacaoTitulos> resultTitulos = null;
    private PreparacaoPagamentos regAtualAgend;
    private PreparacaoTitulos regAtualTit;
    private ArquivoCNAB cnab;
    private CodigoBarras cb;
    private CCodigoBarras ccb;
    private int idxAtual;
    private int numReg;
    private String sql;
    private String cdPortador;
    private NumberFormat formato;
    private String codigoBarras;
    private String dac2Of5;
    private JFormattedTextField linhaDigitavel = new JFormattedTextField();

    // variáveis correlatos;
    private String cdPor;
    private String cdBanco;
    private String nomeBanco;
    private String dacAgenciaContaNossoNumero;
    private String cdAgencia;
    private String cdAgenciaDig;
    private String cdConta;
    private String cdContaDig;
    private double txJuros;
    private double txMulta;
    private double txCorrecao;
    private int diasLiquidacao;
    private int diasCartorio;
    private boolean enviarArquivoCNAB = false;
    private boolean gerarBoleto = false;

    // variáveis para gerar lancamento financeiro
    private PreparacaoPagamentos modpp;
    private PreparacaoPagamentosDAO ppdao;
    private Lancamentos lan;
    private double valorTotal;
    private int numTitulos;
    private DataSistema dat;
    private String data = null;
    private SessaoUsuario su;
    private ParametrosGerais pg;
    private final String tableSource = "gfcpreparacaopagamentos";

    /**
     * Construtor padrão da classe
     *
     * @param conexao Objeto contendo os parâmetros de conexão do usuário
     * @param su Objeto contendo a sessão ativa do usuário
     */
    public CPreparacaoPagamentos(Connection conexao, SessaoUsuario su, ParametrosGerais pg) {
        this.conexao = conexao;
        this.su = su;
        this.pg = pg;
    }

    /**
     * Método para acionar a pesquisa dos registros de pagamentos agendados e
     * guardar em uma array de resultado
     *
     * @param sql Sentença sql para pesquisarPreparacao os registros
     * @return inteiro contendo a quantidade de registros pesquisadas
     * @throws SQLException lança uma exceção de erro
     */
    public int pesquisarPreparacao(String sql) throws SQLException {
        this.sql = sql;
        resultAgendamento = new ArrayList<PreparacaoPagamentos>();
        ppdao = new PreparacaoPagamentosDAO(conexao);
        ppdao.selecionarPreparacao(resultAgendamento, sql);
        numReg = resultAgendamento.size();
        return numReg;
    }

    /**
     * Método para acionar a pesquisa dos títulos agendados e guardar em uma
     * array de resultado
     *
     * @param sql Sentença sql para pesquisarPreparacao os registros
     * @return inteiro contendo a quantidade de registros pesquisadas
     * @throws SQLException lança uma exceção de erro
     */
    public int pesquisarTitulosPreparacao(String sql) throws SQLException {
        this.sql = sql;
        resultTitulos = new ArrayList<PreparacaoTitulos>();
        ppdao = new PreparacaoPagamentosDAO(conexao);
        ppdao.selecionarPreparacaoTitulos(resultTitulos, sql);
        numReg = resultTitulos.size();
        return numReg;
    }

    /**
     * método para prencher a tela com os registros pesquisados
     *
     * @param pp objeto contendo a preparacao de pagamento
     * @param idxAtual índice do registro para ser mostrado na tela da
     * preparacao de pagamento
     */
    public void mostrarPreparacao(PreparacaoPagamentos pp, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtualAgend = resultAgendamento.get(idxAtual);
        pp.setCdPreparacao(regAtualAgend.getCdPreparacao());
        pp.setDataLiquidacaoIni(regAtualAgend.getDataLiquidacaoIni());
        pp.setDataLiquidacaoFin(regAtualAgend.getDataLiquidacaoFin());
        pp.setCdPortador(regAtualAgend.getCdPortador());
        if (regAtualAgend.getCdPortador() != null) {
            pp.setNomePortador(buscarPortador(regAtualAgend.getCdPortador()));
            pp.setTaxaJuros(txJuros);
            pp.setTaxaMulta(txMulta);
        }
        pp.setCdTipoPagamento(regAtualAgend.getCdTipoPagamento());
        if (regAtualAgend.getCdTipoPagamento() != null) {
            pp.setNomeTipoPagamento(buscarTipoPagamento(regAtualAgend.getCdTipoPagamento()));
            pp.setTaxaJuros(txJuros);
            pp.setTaxaMulta(txMulta);
        }
        pp.setTipoMovimento(regAtualAgend.getTipoMovimento());
        pp.setQuantidadeTitulos(regAtualAgend.getQuantidadeTitulos());
        pp.setValorTotal(regAtualAgend.getValorTotal());
        pp.setCdBanco(cdBanco);
        pp.setNomeBanco(nomeBanco);
        pp.setCdAgencia(cdAgencia);
        pp.setCdAgenciaDig(cdAgenciaDig);
        pp.setCdConta(cdConta);
        pp.setCdContaDig(cdContaDig);
        pp.setTaxaCorrecao(txCorrecao);
        pp.setUsuarioCadastro(regAtualAgend.getUsuarioCadastro());
        pp.setDataCadastro(regAtualAgend.getDataCadastro());
        pp.setHoraCadastro(regAtualAgend.getHoraCadastro());
        pp.setUsuarioModificacao(regAtualAgend.getUsuarioModificacao());
        pp.setDataModificacao(regAtualAgend.getDataModificacao());
        pp.setHoraModificacao(regAtualAgend.getHoraModificacao());
        String situacao = regAtualAgend.getSituacao();
        switch (situacao) {
            case "AB":
                pp.setSituacao("1");
                break;
            case "CA":
                pp.setSituacao("2");
                break;
            case "CO":
                pp.setSituacao("3");
                break;
            case "LI":
                pp.setSituacao("4");
                break;
            default:
                pp.setSituacao("0");
                break;
        }
    }

    /**
     * método para prencher a tela com os registros pesquisados
     *
     * @param pt objeto contendo a preparacao de pagamento
     * @param idxAtual índice do registro para ser mostrado na tela da
     * preparacao de pagamento
     */
    public void mostrarPreparacaoTitulos(PreparacaoTitulos pt, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtualTit = resultTitulos.get(idxAtual);
        pt.setCdPreparacao(regAtualTit.getCdPreparacao());
        pt.setCdLancamento(regAtualTit.getCdLancamento());
        pt.setValorSaldo(regAtualTit.getValorSaldo());
        pt.setUsuarioCadastro(regAtualTit.getUsuarioCadastro());
        pt.setDataCadastro(regAtualTit.getDataCadastro());
        pt.setHoraCadastro(regAtualTit.getHoraCadastro());
        pt.setUsuarioModificacao(regAtualTit.getUsuarioModificacao());
        pt.setDataModificacao(regAtualTit.getDataModificacao());
        pt.setHoraModificacao(regAtualTit.getHoraModificacao());
        String situacao = regAtualTit.getSituacao();
        switch (situacao) {
            case "AB":
                pt.setSituacao("1");
                break;
            case "CA":
                pt.setSituacao("2");
                break;
            case "CO":
                pt.setSituacao("3");
                break;
            case "LI":
                pt.setSituacao("4");
                break;
            default:
                pt.setSituacao("0");
                break;
        }
    }

    /**
     * Método para gerar preparacao de pagamento de Contas a Pagar e Contas a
     * Receber, sendo do tipo Título.
     *
     * @param modpp objeto contendo os dados de cabecalho do lancamento
     * @param modcp objeto contendo a condição de pagamento do documento
     */
    public void gerarPreparacao(PreparacaoPagamentos modpp, String acao, JTable titulos) throws SQLException, ParseException {
        this.valorTotal = modpp.getValorTotal();
        this.modpp = modpp;
        dat = new DataSistema();
        ppdao = new PreparacaoPagamentosDAO(conexao);
        LancamentosDAO ldao = new LancamentosDAO(conexao);
        buscarTipoPagamento(modpp.getCdTipoPagamento());
        modpp.setCdPortador(cdPor);
        modpp.setTaxaJuros(txJuros);
        modpp.setTaxaMulta(txMulta);
        modpp.setDiasLiquidacao(diasLiquidacao);
        modpp.setDiasCartorio(diasCartorio);
        buscarPortador(cdPor);
        modpp.setTaxaCorrecao(txCorrecao);
        if (gravarPreparacaoPagamento(acao) == 1) {
            formato = NumberFormat.getInstance();
            PreparacaoTitulos pt = new PreparacaoTitulos();
            for (int i = 0; i < modpp.getQuantidadeTitulos(); i++) {
                pt.setCdPreparacao(modpp.getCdPreparacao());
                pt.setCdLancamento(String.format("%s", titulos.getValueAt(i, 1)));
                pt.setValorSaldo(formato.parse(String.format("%s", titulos.getValueAt(i, 13))).doubleValue());
                pt.setUsuarioCadastro(modpp.getUsuarioCadastro());
                pt.setDataCadastro(modpp.getDataCadastro());
                pt.setHoraCadastro(modpp.getHoraCadastro());
                ppdao.adicionarPreparacaoTitulos(pt);
                if (buscarCorrelatosTitulo(pt.getCdLancamento()) != null) {
                    lan.setPreparado("S");
                    lan.setUsuarioModificacao(modpp.getUsuarioCadastro());
                    lan.setDataModificacao(modpp.getDataCadastro());
                    lan.setHoraModificacao(modpp.getHoraCadastro());
                    String situacao = modpp.getSituacao();
                    switch (situacao) {
                        case "1":
                            lan.setSituacao("AB");
                            break;
                        case "2":
                            lan.setSituacao("CA");
                            break;
                        case "3":
                            lan.setSituacao("CO");
                            break;
                        case "4":
                            lan.setSituacao("LI");
                            break;
                        default:
                            lan.setSituacao(" ");
                            break;
                    }
                    ldao.atualizar(lan);
                }
            }
        }

        JOptionPane.showMessageDialog(null, "Registro gravado com sucesso!");
    }

    /**
     * Método para gerar a preparacao de acordo com o portador e tipo e
     * pagamento
     *
     * @param acao qual ação o usuário irá utilizar A=Alterar N=Novo
     */
    private int gravarPreparacaoPagamento(String acao) {
        dat = new DataSistema();
        dat.setData("");
        HoraSistema hs = new HoraSistema();
        if ("N".equals(acao)) {
            PreparacaoPagamentos pp = modpp;
            int seqAtual = ppdao.buscarUltimaPrep("select fun_gfc_busca_ult_prepPag_dia('"
                    + dat.getData()
                    + "') as Proximo");
            if (seqAtual < 10) {
                pp.setCdPreparacao(String.format("%s%s%s", dat.getData().replace("-", ""), "0", ++seqAtual));
            } else {
                pp.setCdPreparacao(String.format("%s%s", dat.getData().replace("-", ""), ++seqAtual));
            }
            pp.setUsuarioCadastro(su.getUsuarioConectado());
            pp.setDataCadastro(dat.getData());
            pp.setHoraCadastro(hs.getHora());
            pp.setSituacao("AB");
            return ppdao.adicionarPreparacao(pp);
        } else {
            dat.setData("");
            modpp.setUsuarioModificacao(su.getUsuarioConectado());
            modpp.setDataModificacao(dat.getData());
            modpp.setHoraModificacao(hs.getHora());
            return ppdao.atualizarPreparacao(modpp);
        }
    }

    /**
     * Método para gerar o relatório da preparacao
     *
     * @param cdPortador Cógido do portador do lancamento
     * @param cdTipoPgto Código do tipo de pagamento do lancamento
     * @param liquidacaoIni menor data de vencimento dos títulos envolvidos
     * @param liquidacaoFin maior data de vencimento dos títulos envolvidos
     * @param cdTipoLancamento código do tipo de lancamento
     * @param qtdTitulos quantidade de títulos relacionados a preparação
     * @param valorTotal valor total da preparação de pagamento
     * @param cdPreparacao código da preparação de pagamento
     */
    public void prepararRelatorio(String cdPortador, String cdTipoPgto, String liquidacaoIni, String liquidacaoFin, String cdTipoLancamento,
            String qtdTitulos, String valorTotal, String cdPreparacao) {
        InputStream inputStream = getClass().getResourceAsStream("PreparacaoPagamento.jasper");
        String relatorio = su.getLocalRelatorio() + "PreparacaoPagamento.jasper";
        Map parametros = new HashMap();
        parametros.put("LOGOTIPO", su.getLocalImagens().trim() + "Logo.png");
        parametros.put("pCdPortador", cdPortador);
        parametros.put("pNomePortador", buscarPortador(cdPortador));
        parametros.put("pCdTipoPgto", cdTipoPgto);
        parametros.put("pNomeTipoPgto", buscarTipoPagamento(cdTipoPgto));
        parametros.put("pCdBanco", cdBanco);
        parametros.put("pNomeBanco", nomeBanco);
        parametros.put("pLiquidacaoDe", liquidacaoIni);
        parametros.put("pLiquidacaoAte", liquidacaoFin);
        parametros.put("pTipoLancamento", buscarTipoMovimento(cdTipoLancamento));
        parametros.put("pQtdeTitulos", qtdTitulos);
        parametros.put("pValorTotal", valorTotal);
        parametros.put("pCdPreparacao", cdPreparacao);
        parametros.put("SUBREPORT_DIR", su.getLocalRelatorio());
        CRelatorio rel = new CRelatorio();
        try {
            rel.abrirRelatorio("Preparação de Pagamento", relatorio, parametros, conexao);

        } catch (JRException ex) {
            Logger.getLogger(CPreparacaoPagamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método responsável por preparar os dados necessários para a impressão do boleto
     * @param cdPreparacao numero da preparação de pagamento do lancamento financeiro
     * @param boleto nome do layout do boleto que será impresso
     * @throws SQLException retorna uma excessão de erro do SQL
     * @throws JRException retorna uma excessão de erro geral
     * @throws ParseException Retorna uma excessão de erro formatação de máscara
     */
    public void prepararBoleto(String cdPreparacao, String boleto) throws SQLException, JRException, ParseException {
        String sql = "select * from vw_gfc_emitirboleto where preparacao = '" + cdPreparacao
                + "'";
        ConsultaModelo bol = new ConsultaModelo(conexao);
        bol.setQuery(sql);
        if (bol.getRowCount() > 0) {
            InputStream is = getClass().getResourceAsStream(boleto);
            String boletoBanco = su.getLocalBoletoBanco() + boleto.toString();
            Map par = new HashMap();
            CRelatorio rel = new CRelatorio();
            for (int i = 0; i < bol.getRowCount(); i++) {
                par.put("pLocalPgto", "PAGÁVEL EM QUALQUER BANCO ATÉ O VENCIMENTO");
                par.put("pVencimento", bol.getValueAt(i, 2));
                par.put("pBeneficiario", bol.getValueAt(i, 3));
                par.put("pAgenciaCodigoBeneficiario", bol.getValueAt(i, 4));
                par.put("pEndBeneficiario", bol.getValueAt(i, 5));
                par.put("pDataDocumento", bol.getValueAt(i, 6));
                par.put("pNumDocumento", bol.getValueAt(i, 7));
                par.put("pEspecieDoc", bol.getValueAt(i, 8));
                par.put("pAceite", bol.getValueAt(i, 9));
                par.put("pDataProcessamento", bol.getValueAt(i, 10));
                par.put("pCarteira", bol.getValueAt(i, 11));
                par.put("pEspecie", bol.getValueAt(i, 12));
                par.put("pQuantidade", "");
                par.put("Valor", "");
                par.put("pValorDocumento", bol.getValueAt(i, 13));
                par.put("pDescricao1", bol.getValueAt(i, 15) + "    " + bol.getValueAt(i, 14) + "   AO DIA");
                par.put("pDescricao2", bol.getValueAt(i, 16) + "    " + bol.getValueAt(i, 21));
                par.put("pDescricao3", bol.getValueAt(i, 17));
                par.put("pDescontoAbatimento", "");
                par.put("pMouraMulta", bol.getValueAt(i, 14));
                par.put("pValorCobrado", "");
                par.put("pPagador", bol.getValueAt(i, 18));
                par.put("pEndPagador", bol.getValueAt(i, 19));
                par.put("pSacadorAvalista", "");
                //      mensagem("Vou preparar o código de barras!");
                codigoBarras = prepararCodigoBarras(i, bol);
                //      mensagem("Vou preparar a linhaDigitavel!");
                linhaDigitavel.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####.##### #####.###### #####.###### # ##############")));
                String linhaDig = prepararLinhaDigitavel(i, bol);
                //      mensagem("Codigo de Barras: " + codigoBarras + "\nLinhaDigitável: " + linhaDig + "\nDAC 2Of5: " + dac2Of5 + "\nTamanho String: "
                //              + codigoBarras.length() + "\nPosicao 1: " + codigoBarras.substring(1, 2) + "\nPosicao 41: " + codigoBarras.substring(41, 42)
                //              + "\nPosicao 0: " + codigoBarras.substring(0, 1));
                linhaDigitavel.setText(linhaDig);
                par.put("pNossoNumero", String.format("%s%s%s%s%s", bol.getValueAt(i, 11), "/", cb.getNossoNumero(),"-",cb.getDacAgenciaContaNossoNumero()));
                par.put("pCodigoBarras", codigoBarras);
                par.put("pLinhaDigitavel", linhaDigitavel.getText());
                par.put("pIconeBanco", pg.getLocalImagens() + "icone_itau.jpg");
                par.put("pCobrarJuros", "N");
                rel.abrirRelatorio("EmitirBoleto", boletoBanco, par, conexao);
            }
        }

    }

    /**
     * Método para criar o código de barras do boleto
     *
     * @param linha índice da linha do resultSet que o registro está posicionado
     * @param bol Objeto contendo o resultSet onde o registro será pesquisado
     * @return String contendo o código de barras criado
     */
    private String prepararCodigoBarras(int linha, ConsultaModelo bol) {
        CPosicionarArquivo cpa = new CPosicionarArquivo();
        DataSistema dat = new DataSistema();
        cb = new CodigoBarras();
        ccb = new CCodigoBarras(cb);
        buscarPortador(bol.getValueAt(linha, 1).toString());
        cb.setCdBanco(cdBanco); //Buscar o bando do beneficiario - OK
        cb.setCdMoeda("9");
        cb.setFatorVerncimento(String.valueOf(ccb.fatorVencimento(dat.getDataConv(bol.getValueAt(linha, 2).toString())))); //buscar fator de vencimento - OK
        cb.setValorTitulo(String.valueOf(bol.getValueAt(linha, 13)).replace(".", "").replace(",", ""));
        cb.setCdCarteira(bol.getValueAt(linha, 11).toString());
        cb.setNossoNumero(String.format("%s%s", cpa.gerarZeroEsquerda(8 - bol.getValueAt(linha, 20).toString().length()), bol.getValueAt(linha, 20).toString()));
        //mensagem("Buscando DAC modulo 10 da AgenciaContaNossoNumero!");
        cb.setDacAgenciaContaNossoNumero(String.valueOf(ccb.dacModulo10(String.format("%s%s%s%s", cdAgencia, cdConta, bol.getValueAt(linha, 11).toString(), cb.getNossoNumero())))); //buscar dac destes campo - OK
        cb.setCdAgenciaBenef(cdAgencia);   //buscar agencia deste beneciciario - OK
        cb.setContaCorrBenef(cdConta);  //buscar conta deste beneficiaro - OK
        //mensagem("Buscando DAC modulo 10 da AgenciaContaCorrente!");
        //dacAgenciaContaNossoNumero = ;
        //mensagem("DacAgenciaContaNossoNumero Gerado: \n"+dacAgenciaContaNossoNumero);
        cb.setDacAgenciConta(String.valueOf(ccb.dacModulo10(String.format("%s%s", cdAgencia, cdConta)))); //buscar dac da agencia conta - OK
        cb.setZeros("000");
        //mensagem("Código Gerado: \n" + ccb.dacCodigoBarras2of5());
        dac2Of5 = ccb.dacCodigoBarras2of5().substring(5, 6);
        return ccb.dacCodigoBarras2of5();
    }

    /**
     * Método para preparar a linha digigável do boleto
     * @param linha índice da linha do resultSet que contem o registro com os dados necessários para alinha digitável
     * @param bol Objeto contendo o resultSet com o registro dos dados que comporão a linha digitável
     * @return uma string contendo o valor da linha digitável
     */
    private String prepararLinhaDigitavel(int linha, ConsultaModelo bol) {
        String campo1 = String.format("%s%s%s%s", cdBanco, "9", bol.getValueAt(linha, 11).toString(), codigoBarras.substring(22, 24));
        String campo2 = String.format("%s%s%s", codigoBarras.substring(24, 30), cb.getDacAgenciaContaNossoNumero(), cdAgencia.substring(0, 3));
        String campo3 = String.format("%s%s%s%s", cdAgencia.substring(3, 4), cdConta, cdContaDig, "000");
        String campo4 = codigoBarras.substring(4, 5);
        String campo5 = String.format("%s%s", codigoBarras.substring(5, 9), codigoBarras.substring(9, 19));
        //mensagem("Campo1: " + campo1 + "\nCampo2: " + campo2 + "\nCampo3: " + campo3 + "\nCampo4: " + campo4 + "\nCampo5: " + campo5);
        //mensagem("Vou gerar o dac da linha digitável!");
        return ccb.dacLinhaDigitavel(campo1, campo2, campo3, campo4, campo5);
    }

    /**
     * Método para buscar as informações dos lançametos
     *
     * @throws SQLException
     */
    public Lancamentos buscarCorrelatosTitulo(String cdLancamento) throws SQLException {
        String sql = "select * from gfclancamentos where cd_lancamento = '" + cdLancamento
                + "'";
        lan = new Lancamentos();
        CLancamentos clan = new CLancamentos(conexao, su);
        if (clan.pesquisar(sql) != 0) {
            clan.mostrarPesquisa(lan, 0);
            return lan;
        }
        return null;
    }

    /**
     * Método par buscar nome do Portador. Este método além de retornar o nome
     * do portador, ele atualiza as váriáveis txCorrecao, cdBanco, nomeBanco,
     * cdAgencia, cdAgenciaDig, cdConta, cdContaDig, txJuros, txMulta,
     * diasLiquidacao e diasCartorio
     *
     * @param cdPortador código do portador
     * @return String contendo o nome do portador.
     */
    public String buscarPortador(String cdPortador) {
        Portadores por = new Portadores();
        try {
            CPortadores cpor = new CPortadores(conexao);
            String sqlcpor = "SELECT * FROM GFCPORTADOR WHERE CD_PORTADOR = '" + cdPortador + "'";
            cpor.pesquisar(sqlcpor);
            cpor.mostrarPesquisa(por, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome do Banco!\nPrograma CTecnicos.\nErro: " + ex);
        }
        txCorrecao = por.getTaxaCorrecao();
        cdBanco = por.getCdBanco();
        nomeBanco = por.getNomeBanco();
        cdAgencia = por.getCdAgencia();
        cdAgenciaDig = por.getCdAgenciaDig();
        cdConta = por.getCdConta();
        cdContaDig = por.getCdContaDig();
        txJuros = por.getTaxaJuros();
        txMulta = por.getTaxaMulta();
        diasLiquidacao = por.getDiasLiquidacao();
        diasCartorio = por.getDiasCartorio();
        return por.getNomePortador();
    }

    /**
     * Método para buscar dados da empresa
     *
     * @param cpfCnpj C.P.F ou C.N.P.J do cliente/fornecedor a ser pesquisado
     * @return String contendo o nome ou/a Razão Social do cliente/fornecedor
     */
    private String buscarClienteFornecedor(String cpfCnpj) {
        Empresa emp = new Empresa();
        try {
            CEmpresa cemp = new CEmpresa(conexao);
            String sqlcemp = "SELECT * FROM PGSEMPRESA WHERE CPF_CNPJ = '" + cpfCnpj + "'";
            cemp.pesquisar(sqlcemp);
            cemp.mostrarPesquisa(emp, 0);
        } catch (SQLException ex) {
            Logger.getLogger(CPreparacaoPagamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return emp.getNomeRazaoSocial();
    }

    /**
     * Método para buscar dados do Tipo de Pagamento
     *
     * @param cdTipoPagamento Código do tipo de pagamento a ser buscado
     * @return String com o nome do tipo de pagamento
     */
    public String buscarTipoPagamento(String cdTipoPagamento) {
        TipoPagamento tp = new TipoPagamento();
        try {
            CTipoPagamento ctp = new CTipoPagamento(conexao);
            String sqlctp = "SELECT * FROM GFCTIPOPAGAMENTO WHERE CD_TIPOPAGAMENTO = '" + cdTipoPagamento
                    + "'";
            ctp.pesquisar(sqlctp);
            ctp.mostrarPesquisa(tp, 0);
        } catch (SQLException ex) {
            Logger.getLogger(CPreparacaoPagamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
        cdPor = tp.getCdPortador();
        txJuros = tp.getTaxaJuros();
        txMulta = tp.getTaxaMulta();
        diasLiquidacao = tp.getDiasLiquidacao();
        diasCartorio = tp.getDiasCartorio();
        setGerarBoleto(tp.getEmiteBoleto());
        setEnviarArquivoCNAB(tp.getEnviarArqBanco());
        return tp.getNomeTipoPagamento();
    }

    /**
     * Método para buscar o nome do tipo de moviemtno
     *
     * @param cdTipoMovimento
     * @return
     */
    public String buscarTipoMovimento(String cdTipoMovimento) {
        TipoMovimento tm = new TipoMovimento();
        try {
            CTipoMovimento ctm = new CTipoMovimento(conexao);
            String sql = "select * from gfctipomovimento where cd_tipomovimento = '" + cdTipoMovimento
                    + "'";
            if (ctm.pesquisar(sql) > 0) {
                ctm.mostrarPesquisa(tm, 0);
                return tm.getNomeTipoMovimento();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do tipo de movimento!\nPrograma CPreparacaoPagamento.java\nErro: " + ex);
        }
        return "";
    }

    public String buscarEmpresa(String cpfCnpj) {
        Empresa em = new Empresa();
        try {
            CEmpresa cem = new CEmpresa(conexao);
            String sql = "select * from pgsempresa where cpf_cnpj = '" + cpfCnpj
                    + "'";
            if (cem.pesquisar(sql) > 0) {
                cem.mostrarPesquisa(em, 0);
                if ("F".equals(em.getTipoPessoa())) {
                    cnab.setTipoPessoaEmpresa("01");
                } else {
                    cnab.setTipoPessoaEmpresa("02");
                }
                cnab.setCpfCnpjEmpresa(cpfCnpj);
                return em.getNomeRazaoSocial();
            } else {
                return "";
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca dda empresa!\nPrograma CPreparacaoPagamento.java\nErro: " + ex);
            return "";
        }
    }

    /**
     * Método para buscar os parametros EDI para o tipo de pagamento
     *
     * @param cnab Objeto contendo os dados no parametro de Arquivo CNAB
     * @param cdTipoPagamento codido do tipo de pagamento a pesquisar
     * @return retorna 1 para verdadeiro ou 0 para falso.
     */
    public int buscarParametrosEDI(ArquivoCNAB cnab, String cdTipoPagamento) {
        this.cnab = cnab;
        ParametrosEDI pedi = new ParametrosEDI();
        try {
            CParametrosEDI cpedi = new CParametrosEDI(conexao, su);
            String sql = "select * from gfcparametrosedi where cd_tipopagamento = '" + cdTipoPagamento
                    + "'";
            if (cpedi.pesquisar(sql) > 0) {
                cpedi.mostrarPesquisa(pedi, 0);
                cnab.setNomeOperacao(pedi.getSituacaoEdi().trim().toUpperCase());
                cnab.setCdTipoServico(pedi.getCdTipoServico().substring(0, 2));
                cnab.setNomeTipoServico(pedi.getNomeTipoServico().trim().toUpperCase().substring(0, 8));
                cnab.setNomeEmpresa(buscarEmpresa(pedi.getCdCodigoBeneficiario()));
                buscarPortador(pedi.getCdPortador());
                cnab.setCdAgencia(cdAgencia);
                cnab.setCdAgenciaDig(cdAgenciaDig);
                cnab.setCdConta(cdConta);
                cnab.setCdContaDig(cdContaDig);
                cnab.setCdBanco(cdBanco);
                cnab.setNomeBanco(nomeBanco.trim().toUpperCase());
                cnab.setTaxaJuros(txJuros);
                cnab.setTaxaMulta(txMulta);
                cnab.setCdCarteira(pedi.getCdCarteira().trim().toString().substring(0, 3));
                cnab.setCdMulta(pedi.getCdJurosMora());
                mensagem("Código Carteira Pesquisada: " + pedi.getCdCarteira() + "\nCódigo Carteira Gravada: " + cnab.getCdCarteira());
                switch(pedi.getSituacao()){
                    case "1":
                        cnab.setParametroAtivo(true);
                        break;
                    default:
                        cnab.setParametroAtivo(false);
                        break;
                }
                return 1;
            } else {
                return 0;
            }
        } catch (SQLException ex) {
            mensagem("Erro na busca do nome da empresa!\nErro: " + ex);
            return 0;
        }
    }

    /**
     * Método para retornar mensagem na tela
     *
     * @param msg
     */
    private void mensagem(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    /**
     * @return the enviarArquivoCNAB
     */
    public boolean isEnviarArquivoCNAB() {
        return enviarArquivoCNAB;
    }

    /**
     * @param enviarArquivoCNAB the enviarArquivoCNAB to set
     */
    private void setEnviarArquivoCNAB(String enviarArquivo) {
        if ("S".equals(enviarArquivo)) {
            this.enviarArquivoCNAB = true;
        } else {
            this.enviarArquivoCNAB = false;
        }
    }

    /**
     * @return the gerarBoleto
     */
    public boolean isGerarBoleto() {
        return gerarBoleto;
    }

    /**
     * @param gerarBoleto the gerarBoleto to set
     */
    private void setGerarBoleto(String gerarBoleto) {
        if ("S".equals(gerarBoleto)) {
            this.gerarBoleto = true;
        } else {
            this.gerarBoleto = false;
        }
    }
}
