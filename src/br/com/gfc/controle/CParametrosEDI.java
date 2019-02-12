/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.gfc.dao.ParametrosEDIDAO;
import br.com.gfc.modelo.ParametrosEDI;
import br.com.gfc.modelo.TipoCarteiraEDI;
import br.com.gfc.modelo.TipoPagamento;
import br.com.gfc.modelo.TipoServicoEDI;
import br.com.modelo.DataSistema;
import br.com.modelo.HoraSistema;
import br.com.modelo.SessaoUsuario;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 26/11/2018
 */
public class CParametrosEDI {

    // variáveis de instancia
    private Connection conexao;
    private SessaoUsuario su;
    private List<ParametrosEDI> resultado = null;
    private ParametrosEDI regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    private String cdBanco;
    private String cdConta;
    private String cdContaDig;
    private String cdAgencia;
    private String cdAgenciaDig;
    private String tipoConta;
    private String cdPortador;
    private String nomePortador;

    // Construtor padrão
    public CParametrosEDI(Connection conexao, SessaoUsuario su) {
        this.conexao = conexao;
        this.su = su;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<ParametrosEDI>();
        ParametrosEDIDAO pedidao = new ParametrosEDIDAO(conexao);
        pedidao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(ParametrosEDI pedi, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);

        pedi.setCdTipoPagamento(regAtual.getCdTipoPagamento());
        pedi.setNomeTipoPagamento(buscarTipoPagamento(regAtual.getCdTipoPagamento()));
        pedi.setCdPortador(cdPortador);
        pedi.setNomePortador(nomePortador);
        pedi.setCdCodigoBeneficiario(regAtual.getCdCodigoBeneficiario());
        pedi.setNumeroSequencial(regAtual.getNumeroSequencial());
        pedi.setSituacaoEdi(regAtual.getSituacaoEdi());
        pedi.setTipoArquvo(regAtual.getTipoArquvo());
        pedi.setCdTipoServico(String.format("%s%s%s", regAtual.getCdTipoServico(),"-",buscarTipoServico(regAtual.getCdTipoServico(), cdPortador).trim()));
        pedi.setNomeTipoServico(buscarTipoServico(regAtual.getCdTipoServico(), cdPortador));
        switch (regAtual.getUsarVersao()) {
            case "S":
                pedi.setUsarVersao("1");
                break;
            case "N":
                pedi.setUsarVersao("2");
                break;
            default:
                pedi.setUsarVersao("0");
        }
        switch (regAtual.getVersaoLayout()) {
            case "S":
                pedi.setVersaoLayout("1");
                break;
            case "N":
                pedi.setVersaoLayout("2");
                break;
            default:
                pedi.setVersaoLayout("0");
        }
        switch (regAtual.getUsarBoletoPersonalizado()) {
            case "S":
                pedi.setUsarBoletoPersonalizado("1");
                break;
            case "N":
                pedi.setUsarBoletoPersonalizado("2");
                break;
            default:
                pedi.setUsarBoletoPersonalizado("0");
                break;
        }
        pedi.setCdBoletoPerson(regAtual.getCdBoletoPerson());
        pedi.setCdCarteira(String.format("%s%s%s", regAtual.getCdCarteira(),"-",buscarTipoCarteira(regAtual.getCdCarteira().trim(), cdPortador)));
        pedi.setTipoEmissaoBoleto(regAtual.getTipoEmissaoBoleto());
        pedi.setTipoEntregaBoleto(String.valueOf(Integer.parseInt(regAtual.getTipoEntregaBoleto()) + 1));
        pedi.setCdJurosMora(regAtual.getCdJurosMora());
        pedi.setTipoJurosMoraDiaTx(regAtual.getTipoJurosMoraDiaTx());
        pedi.setCdDesconto(String.valueOf(Integer.valueOf(regAtual.getCdDesconto()) + 1));
        pedi.setCdBaixaDevolucao(regAtual.getCdBaixaDevolucao());
        pedi.setQtdeDiasBaixaDevol(regAtual.getQtdeDiasBaixaDevol());
        pedi.setMensagem1(regAtual.getMensagem1());
        pedi.setMensagem2(regAtual.getMensagem2());
        pedi.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        pedi.setDataCadastro(regAtual.getDataCadastro());
        pedi.setHoraCadastro(regAtual.getHoraCadastro());
        pedi.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        pedi.setDataModificacao(regAtual.getDataModificacao());
        pedi.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                pedi.setSituacao("1");
                break;
            case "I":
                pedi.setSituacao("2");
                break;
            default:
                pedi.setSituacao("0");
                break;
        }
    }

    /**
     * Método para gravar o serviço no banco
     *
     * @param pedi Objeto contendo as informações a serem gravadas
     * @param acao Tipo de ação a realizar sendo: A-Atualização ou N-Novo
     * @return
     * @throws SQLException
     */
    public int gravarRegistro(ParametrosEDI pedi, String acao) throws SQLException {
        DataSistema dat = new DataSistema();
        dat.setData("");
        HoraSistema hs = new HoraSistema();
        ParametrosEDIDAO pedidao = new ParametrosEDIDAO(conexao);
        if ("N".equals(acao)) {
            pedi.setUsuarioCadastro(su.getUsuarioConectado());
            pedi.setDataCadastro(dat.getData());
            pedi.setHoraCadastro(hs.getHora());
            if (pedidao.adicionar(pedi) > 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            pedi.setUsuarioModificacao(su.getUsuarioConectado());
            pedi.setDataModificacao(dat.getData());
            pedi.setHoraModificacao(hs.getHora());
            if (pedidao.atualizar(pedi) > 0) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    public int excluirRegistro(ParametrosEDI pedi) throws SQLException {
        ParametrosEDIDAO ccDAO = new ParametrosEDIDAO(conexao);
        ccDAO.excluir(pedi);
        return 1;
    }

    /**
     * Método para buscar nome do tipo de pagamento
     *
     * @param cdTipoPagamento Código do tipo de pagamentoa ser pesquisado
     */
    private String buscarTipoPagamento(String cdTipoPagamento) {
        TipoPagamento tp = new TipoPagamento();
        try {
            CTipoPagamento ctp = new CTipoPagamento(conexao);
            String sqltp = "SELECT * FROM GFCTIPOPAGAMENTO WHERE CD_TIPOPAGAMENTO = '" + cdTipoPagamento
                    + "'";
            if (ctp.pesquisar(sqltp) > 0) {
                ctp.mostrarPesquisa(tp, 0);
                cdPortador = tp.getCdPortador();
                nomePortador = tp.getNomePortador();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na buca do nome do Tipo de Pagamento!\nPrograma CParametrosEDI.java.\nErro: " + ex);
        }
        return tp.getNomeTipoPagamento();
    }

    /**
     * Método para buscar o tipo de serviço
     * @param cdTipoServico Código do tipo de serviço a ser pesquisado
     * @param cdPortador códito do portador vincualodo ao tipo de serviço
     * @return retorna uma stringo com o nome do tipo de serviço
     */
    private String buscarTipoServico(String cdTipoServico, String cdPortador) {
        TipoServicoEDI tedi = new TipoServicoEDI();
        try {
            CTipoServicoEDI ctedi = new CTipoServicoEDI(conexao, su);
            String sql = "select * from gfctiposervicoedi where cd_portador = '" + cdPortador
                    + "' and cd_tiposervico = '" + cdTipoServico
                    + "'";
            if (ctedi.pesquisar(sql) > 0) {
                ctedi.mostrarPesquisa(tedi, 0);
                return tedi.getNomeTipoServico();
            } else {
                return "";
            }
        } catch (SQLException ex) {
            mensagem("Erro na busca do tipo de servico! Programa CParametrosEDI.java\nErro: " + ex);
            return "";
        }
    }
    
    private String buscarTipoCarteira(String cdTipoCarteira, String cdPortador){
        TipoCarteiraEDI tcar = new TipoCarteiraEDI();
        try{
            CTipoCarteiraEDI ctcar = new CTipoCarteiraEDI(conexao, su);
            String sql = "select * from gfccarteiraedi where cd_portador = '" + cdPortador
                    + "' and cd_carteira = '" + cdTipoCarteira
                    + "'";
            if(ctcar.pesquisar(sql) > 0){
                ctcar.mostrarPesquisa(tcar, 0);
                return tcar.getNomeCarteira();
            }else
                return "";
        }catch(SQLException ex){
            mensagem("Erro na busca do tipo de carteira! Programa CParametrosEDI.java.\nErro: "+ex);
            return "";
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
}
