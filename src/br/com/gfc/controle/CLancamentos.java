/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.controle.CBuscarSequencia;
import br.com.controle.CEmpresa;
import br.com.gfc.modelo.Portadores;
import br.com.gfc.dao.LancamentosDAO;
import br.com.gfc.modelo.CondicaoPagamento;
import br.com.gfc.modelo.Lancamentos;
import br.com.gfc.modelo.ParcelasCondicaoPagamento;
import br.com.gfc.modelo.TipoLancamento;
import br.com.gfc.modelo.TipoMovimento;
import br.com.gfc.modelo.TipoPagamento;
import br.com.modelo.DataSistema;
import br.com.modelo.Empresa;
import br.com.modelo.HoraSistema;
import br.com.modelo.SessaoUsuario;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 14/02/2018
 */
public class CLancamentos {

    // variáveis de instancia
    private Connection conexao;
    private List<Lancamentos> resultado = null;
    private Lancamentos regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    private String cdPortador;
    private String cpfCnpj;

    // variáveis correlatos;
    private String cdPor;
    private String cdBanco;
    private String nomeBanco;
    private String cdAgencia;
    private String cdAgenciaDig;
    private String cdConta;
    private String cdContaDig;
    private double txJuros;
    private double txMulta;
    private double txCorrecao;
    private int diasLiquidacao;
    private int diasCartorio;

    // variáveis para gerar lancamento financeiro
    private Lancamentos modlan;
    private LancamentosDAO ldao;
    private CondicaoPagamento modcp;
    private ParcelasCondicaoPagamento modpcp;
    private CParcelasCondicaoPagamento cpcp;
    private double valorLancamento;
    private int numParcelas;
    private DataSistema dat;
    private String data = null;
    private SessaoUsuario su;

    // variáveis para gerar liquidação do título
    private String sitContraPart;
    private String cdLancamentoOriginal;

    /**
     * Construtor padrão da classe
     *
     * @param conexao Objeto contendo os parâmetros de conexão do usuário
     * @param su Objeto contendo a sessão ativa do usuário
     */
    public CLancamentos(Connection conexao, SessaoUsuario su) {
        this.conexao = conexao;
        this.su = su;
    }

    /**
     * Método para acionar a pesquisa dos registros de lançamentos e guardar em
     * uma array de resultado
     *
     * @param sql Sentença sql para pesquisar os registros
     * @return inteiro contendo a quantidade de registros pesquisadas
     * @throws SQLException lança uma exceção de erro
     */
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<Lancamentos>();
        LancamentosDAO landao = new LancamentosDAO(conexao);
        landao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    /**
     * método para prencher a tela com os registros pesquisados
     *
     * @param lan objeto contendo o lançamento financeiro
     * @param idxAtual índice do registro para ser mostrado na tela do
     * lancamento
     */
    public void mostrarPesquisa(Lancamentos lan, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        lan.setCdLancamento(regAtual.getCdLancamento());
        lan.setSequencial(regAtual.getSequencial());
        String tipoLanc = regAtual.getTipoLancamento();
        switch (tipoLanc) {
            case "Adi":
                lan.setTipoLancamento("1");
                break;
            case "Pre":
                lan.setTipoLancamento("2");
                break;
            case "Tit":
                lan.setTipoLancamento("3");
                break;
            default:
                lan.setTipoLancamento("3");
                break;
        }

        lan.setTipoMovimento(buscarTipoMovimento(regAtual.getTipoMovimento()));
        lan.setTitulo(regAtual.getTitulo());
        lan.setCdParcela(regAtual.getCdParcela());
        lan.setCpfCnpj(regAtual.getCpfCnpj());
        lan.setNomeRazaoSocial(buscarClienteFornecedor(regAtual.getCpfCnpj()));
        lan.setDataEmissao(regAtual.getDataEmissao());
        lan.setDataVencimento(regAtual.getDataVencimento());
        if (regAtual.getDataLiquidacao() != null) {
            lan.setDataLiquidacao(regAtual.getDataLiquidacao());
        }
        lan.setValorLancamento(regAtual.getValorLancamento());
        lan.setValorSaldo(regAtual.getValorSaldo());
        lan.setCdPortador(regAtual.getCdPortador());
        if (regAtual.getCdPortador() != null) {
            lan.setNomePortador(buscarPortador(regAtual.getCdPortador()));
            lan.setTaxaJuros(txJuros);
            lan.setTaxaMulta(txMulta);
        }
        lan.setCdBanco(cdBanco);
        lan.setNomeBanco(nomeBanco);
        lan.setCdAgencia(cdAgencia);
        lan.setCdAgenciaDig(cdAgenciaDig);
        lan.setCdConta(cdConta);
        lan.setCdContaDig(cdContaDig);
        lan.setTaxaCorrecao(txCorrecao);
        String tipoDoc = regAtual.getTipoDocumento().trim();
        switch (tipoDoc) {
            case "PED":
                lan.setTipoDocumento("1");
                break;
            case "OC-":
                lan.setTipoDocumento("2");
                break;
            case "OS-":
                lan.setTipoDocumento("3");
                break;
            case "NFE":
                lan.setTipoDocumento("4");
                break;
            case "NFS":
                lan.setTipoDocumento("5");
                break;
            case "O":
                lan.setTipoDocumento("6");
                break;
            default:
                lan.setTipoDocumento("0");
                break;
        }
        lan.setDocumento(regAtual.getDocumento());
        lan.setCdTipoPagamento(regAtual.getCdTipoPagamento());
        if (regAtual.getCdTipoPagamento() != null) {
            lan.setNomeTipoPagamento(buscarTipoPagamento(regAtual.getCdTipoPagamento()));
            lan.setTaxaJuros(txJuros);
            lan.setTaxaMulta(txMulta);
        }
        lan.setNossoNumeroBanco(regAtual.getNossoNumeroBanco());
        lan.setValorJuros(regAtual.getValorJuros());
        lan.setValorMulta(regAtual.getValorMulta());
        lan.setValorAtualizado(regAtual.getValorAtualizado());
        lan.setDiasCartorio(regAtual.getDiasCartorio());
        lan.setDiasLiquidacao(regAtual.getDiasLiquidacao());
        String gerouArquivo = regAtual.getGerouArquivo();
        switch (gerouArquivo) {
            case "S":
                lan.setGerouArquivo("1");
                break;
            case "N":
                lan.setGerouArquivo("2");
                break;
            default:
                lan.setGerouArquivo("0");
                break;
        }
        lan.setCdContaReduzida(regAtual.getCdContaReduzida());
        lan.setCdCCusto(regAtual.getCdCCusto());
        lan.setContraPartida(regAtual.getContraPartida());
        lan.setPreparado(regAtual.getPreparado());
        lan.setCdHistorico(regAtual.getCdHistorico());
        lan.setComplementoHistorico(regAtual.getComplementoHistorico());
        lan.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        lan.setDataCadastro(regAtual.getDataCadastro());
        lan.setHoraCadastro(regAtual.getHoraCadastro());
        lan.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        lan.setDataModificacao(regAtual.getDataModificacao());
        lan.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "AB":
                lan.setSituacao("1");
                break;
            case "CA":
                lan.setSituacao("2");
                break;
            case "CO":
                lan.setSituacao("3");
                break;
            case "LI":
                lan.setSituacao("4");
                break;
            default:
                lan.setSituacao("0");
                break;
        }
    }

    /**
     * Método para gerar Lancamentos Financeiros de Contas a Pagar e Contas a
     * Receber, sendo do tipo Previsão ou Título.
     *
     * @param modlan objeto contendo os dados de cabecalho do lancamento
     * @param modcp objeto contendo a condição de pagamento do documento
     */
    public void gerarLancamento(Lancamentos modlan, String cdCondPag) throws SQLException {
        this.valorLancamento = modlan.getValorLancamento();
        this.modlan = modlan;
        dat = new DataSistema();
        modcp = buscarCondicaoPagamento(cdCondPag);
        numParcelas = modcp.getNumParcelas();
        modpcp = new ParcelasCondicaoPagamento();
        cpcp = new CParcelasCondicaoPagamento(conexao);
        ldao = new LancamentosDAO(conexao);
        buscarTipoPagamento(modlan.getCdTipoPagamento());
        modlan.setCdPortador(cdPor);
        modlan.setTaxaJuros(txJuros);
        modlan.setTaxaMulta(txMulta);
        modlan.setDiasLiquidacao(diasLiquidacao);
        modlan.setDiasCartorio(diasCartorio);
        buscarPortador(cdPor);
        modlan.setTaxaCorrecao(txCorrecao);
        pesquisarParcelas();
        for (int i = 1; i <= numParcelas; i++) {
            gerarParcelas(i, "N");
        }
        JOptionPane.showMessageDialog(null, "Registro gravado com sucesso!");
    }

    /**
     * Método para alterar o lancamento Financeiro de previsão para título
     *
     * @param titulo Número do título que será alterado
     * @param tpLanc código do tipo de lancamento
     * @param tpMov código do tipo de movimento
     * @param cdCondPag códito da condição de pagamenteo
     */
    public void alterarLancamento(String titulo, String tpLanc, String tpMov, String cdCondPag) {
        String sql = "select * from gfclancamentos where tipo_lancamento = 'Pre' and tipo_movimento = '" + tpMov
                + "' and titulo = '" + titulo
                + "'";
        try {
            int numReg = pesquisar(sql);
            if (numReg > 0) {
                ldao = new LancamentosDAO(conexao);
                modcp = buscarCondicaoPagamento(cdCondPag);
                modpcp = new ParcelasCondicaoPagamento();
                cpcp = new CParcelasCondicaoPagamento(conexao);
                pesquisarParcelas();
                for (int i = 0; i < numReg; i++) {
                    modlan = resultado.get(i);
                    modlan.setTipoLancamento(tpLanc);
                    gerarParcelas(i + 1, "A");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CLancamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para cancelar os lancamentos financeiros
     *
     * @param tipoDocumento Tipo de documento que gerou o lancamento financeiro
     * @param titulo número do título financeiro que será cancelado
     * @param tipoMovimento tipo de movimento financeiro (Re-Receber ou
     * Pa-Pagar)
     * @param situacao situação do lancamento financeiro
     * @param parcelas número de parcelas que serão canceladas
     * @return zero para cancelamento realizado
     */
    public int cancelarLancamentos(String tipoDocumento, String titulo, String tipoMovimento, String situacao, int parcelas) {
        dat = new DataSistema();
        String data = "";
        dat.setData(data);
        data = dat.getData();
        HoraSistema hs = new HoraSistema();
        String sql = "select * "
                + "from gfclancamentos "
                + "where tipo_documento = '" + tipoDocumento
                + "' and titulo = '" + titulo
                + "' and tipo_movimento = '" + tipoMovimento
                + "' and situacao = '" + situacao
                + "'";
        try {
            resultado = new ArrayList<Lancamentos>();
            ldao = new LancamentosDAO(conexao);
            ldao.selecionar(resultado, sql);
            for (int i = 0; i < parcelas; i++) {
                modlan = resultado.get(i);
                modlan.setSituacao("CA");
                modlan.setUsuarioModificacao(su.getUsuarioConectado());
                modlan.setDataModificacao(data);
                modlan.setHoraModificacao(hs.getHora());
                ldao.atualizar(modlan);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro no cancelamento dos lancamentos financeiros! Contate o administrador do sistema.");
            return 1;
        }
        return 0;
    }

    /**
     * Método para gerar as parcelas de acordo com a condição de pagamento
     *
     * @param idxAtual
     * @param acao qual ação o usuário irá utilizar A=Alterar N=Novo
     */
    private void gerarParcelas(int idxAtual, String acao) {
        dat = new DataSistema();
        HoraSistema hs = new HoraSistema();
        cpcp.mostrarPesquisa(modpcp, idxAtual - 1);
        dat.setData(Date.valueOf(modlan.getDataEmissao()), modpcp.getPrazoDias());
        modlan.setDataVencimento(dat.getData());
        if ("N".equals(acao)) {
            CBuscarSequencia cb = new CBuscarSequencia(su, "gfclancamentos", 8);
            Lancamentos lan = modlan;
            lan.setCdLancamento(String.format("%s", buscarTipoLancamento(lan.getTipoLancamento()) + cb.getRetorno()));
            lan.setSequencial(Integer.parseInt(cb.getRetorno()));
            if (modlan.getCdParcela() <= idxAtual) {
                lan.setCdParcela(idxAtual);
            }
            lan.setValorLancamento(valorLancamento / 100 * modpcp.getPercRateio());
            lan.setValorSaldo(lan.getValorLancamento());
            ldao.adicionar(lan);
        } else {
            String cdLancOriginal = modlan.getCdLancamento();
            modlan.setCdLancamento(String.format("%s", buscarTipoLancamento(modlan.getTipoLancamento()) + cdLancOriginal.substring(1)));
            dat.setData("");
            modlan.setUsuarioModificacao(su.getUsuarioConectado());
            modlan.setDataModificacao(dat.getData());
            modlan.setHoraModificacao(hs.getHora());
            ldao.mudarTipoLancamento(modlan, cdLancOriginal);
        }
    }

    /**
     * Método para gerar a liquidação do título
     *
     * @param lan Objeto contendo a parcela a ser liquidada
     * @param valorPago Valor do pagamento realizado
     * @param dataLiquidacao data da liquidação da parcela
     * @return retorna 0 para sucesso e 1 para insucesso
     */
    public int liquidarTituto(Lancamentos lan, Double valorPago, String dataLiquidacao, Double valorSaldoParcela, String situacaoParcela) {
        dat = new DataSistema();
        dat.setData(data);
        data = dat.getData();
        HoraSistema hs = new HoraSistema();
        CBuscarSequencia cb = new CBuscarSequencia(su, "gfclancamentos", 8);
        cdLancamentoOriginal = lan.getCdLancamento();
        buscarTipoMovimento(lan.getTipoMovimento());
        lan.setContraPartida(cdLancamentoOriginal);
        lan.setCdLancamento(String.format("%s", buscarTipoLancamento(lan.getTipoLancamento()) + cb.getRetorno()));
        lan.setSequencial(Integer.parseInt(cb.getRetorno()));
        lan.setValorLancamento(valorPago);
        lan.setValorSaldo(0.00);
        lan.setValorAtualizado(valorPago);
        lan.setSituacao("LI");
        lan.setTipoMovimento("rR");
        lan.setDataLiquidacao(dat.getDataConv(dataLiquidacao));
        lan.setUsuarioCadastro(su.getUsuarioConectado());
        lan.setDataCadastro(data);
        lan.setHoraCadastro(hs.getHora());
        try {
            ldao = new LancamentosDAO(conexao);
            if (ldao.adicionar(lan) == 0) {
                sql = "select * from gfclancamentos where cd_lancamento = '" + cdLancamentoOriginal
                        + "'";
                if (pesquisar(sql) > 0) {
                    modlan = new Lancamentos();
                    modlan = resultado.get(0);
                    modlan.setDataLiquidacao(lan.getDataLiquidacao());
                    modlan.setValorSaldo(valorSaldoParcela);
                    modlan.setSituacao(situacaoParcela);
                    modlan.setUsuarioModificacao(su.getUsuarioConectado());
                    modlan.setDataModificacao(lan.getDataCadastro());
                    modlan.setHoraModificacao(lan.getHoraCadastro());
                    ldao.atualizar(modlan);
                }
                return 0;
            } else {
                return 1;
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na inclusão da liquidação!\nErro: " + ex);
            return 1;
        }
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
    private String buscarPortador(String cdPortador) {
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
            Logger.getLogger(CLancamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return emp.getNomeRazaoSocial();
    }

    /**
     * Método para buscar Condicao de Pgamento
     *
     * @param cdCondPag código da condição de pagamento
     * @return retorna o objeto contendo a condição de pagamento
     */
    private CondicaoPagamento buscarCondicaoPagamento(String cdCondPag) {
        CondicaoPagamento cp = new CondicaoPagamento();
        try {
            CCondicaoPagamento ccp = new CCondicaoPagamento(conexao);
            String sqlccp = "SELECT * FROM GFCCONDICAOPAGAMENTO WHERE CD_CONDPAG = '" + cdCondPag + "'";
            if (ccp.pesquisar(sqlccp) > 0) {
                ccp.mostrarPesquisa(cp, 0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CLancamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cp;
    }

    /**
     * Método para buscar tipo de movimento
     *
     * @param cdTipoMovimento código com o tipo de movimento a ser pesquisado
     * @return String nome concatenado do tipo de movimento
     */
    private String buscarTipoMovimento(String cdTipoMovimento) {
        TipoMovimento tm = new TipoMovimento();
        CTipoMovimento ctm = new CTipoMovimento(conexao);
        String sqltm = "select * from gfctipomovimento where cd_tipomovimento = '" + cdTipoMovimento
                + "'";
        try {
            if (ctm.pesquisar(sqltm) > 0) {
                ctm.mostrarPesquisa(tm, 0);
                String retorno = String.format("%s", tm.getCdTipoMovimento() + "-" + tm.getNomeTipoMovimento());
                switch (tm.getSituacaoContraPartida()) {
                    case "1":
                        sitContraPart = "AB";
                        break;
                    case "2":
                        sitContraPart = "CA";
                        break;
                    case "3":
                        sitContraPart = "CO";
                        break;
                    case "4":
                        sitContraPart = "LI";
                        break;
                    default:
                        tm.setSituacaoContraPartida("AB");
                        break;
                }
                return retorno;
            } else {
                return "";
            }
        } catch (SQLException ex) {
            Logger.getLogger(CLancamentos.class.getName()).log(Level.SEVERE, null, ex);
            return "Erro";
        }
    }

    /**
     * Método para buscar tipo de lancçamento
     *
     * @param cdTipoLancamento
     * @return String Retorna o Prefixo para o tipo de lancamento
     */
    private String buscarTipoLancamento(String cdTipoLancamento) {
        TipoLancamento tl = new TipoLancamento();
        CTipoLancamento ctl = new CTipoLancamento(conexao);
        String sqltl = "select * from gfctipolancamento where cd_tipolancamento = '" + cdTipoLancamento
                + "'";
        try {
            ctl.pesquisar(sqltl);
            ctl.mostrarPesquisa(tl, 0);
        } catch (SQLException ex) {
            Logger.getLogger(CLancamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
        return String.valueOf(tl.getPrefixo()).trim();
    }

    /**
     * Método para buscar dados do Tipo de Pagamento
     *
     * @param cdTipoPagamento Código do tipo de pagamento a ser buscado
     * @return String com o nome do tipo de pagamento
     */
    private String buscarTipoPagamento(String cdTipoPagamento) {
        TipoPagamento tp = new TipoPagamento();
        try {
            CTipoPagamento ctp = new CTipoPagamento(conexao);
            String sqlctp = "SELECT * FROM GFCTIPOPAGAMENTO WHERE CD_TIPOPAGAMENTO = '" + cdTipoPagamento + "'";
            ctp.pesquisar(sqlctp);
            ctp.mostrarPesquisa(tp, 0);
        } catch (SQLException ex) {
            Logger.getLogger(CLancamentos.class.getName()).log(Level.SEVERE, null, ex);
        }
        cdPor = tp.getCdPortador();
        txJuros = tp.getTaxaJuros();
        txMulta = tp.getTaxaMulta();
        diasLiquidacao = tp.getDiasLiquidacao();
        diasCartorio = tp.getDiasCartorio();
        return tp.getNomeTipoPagamento();
    }

    /**
     * Método para pesquisar as parcelas da condicao de pagamento
     *
     * @return modpcp
     */
    private void pesquisarParcelas() throws SQLException {
        String sql = "select * from gfcparcelacondpag as p"
                + " where p.cd_condpag = '"
                + modcp.getCdCondpag()
                + "'";
        cpcp.pesquisar(sql);
    }
}
