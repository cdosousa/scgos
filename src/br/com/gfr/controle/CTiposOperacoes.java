/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfr.controle;

import br.com.gfr.dao.TiposOperacoesDAO;
import br.com.gfr.modelo.TiposOperacoes;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 08/11/2017
 */
public class CTiposOperacoes {

    // variáveis de instancia
    private Connection conexao;
    private List<TiposOperacoes> resultado = null;
    private TiposOperacoes regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CTiposOperacoes(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<TiposOperacoes>();
        TiposOperacoesDAO topdao = new TiposOperacoesDAO(conexao);
        topdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(TiposOperacoes top, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        top.setCdTipoOper(regAtual.getCdTipoOper());
        top.setCdCfop(regAtual.getCdCfop());
        top.setNomeTipoOperacao(regAtual.getNomeTipoOperacao());
        top.setDescricaoCfop(regAtual.getDescricaoCfop());
        String tipoEstoque = regAtual.getTipoEstoque();
        switch(tipoEstoque){
            case "E":
                top.setTipoEstoque("1");
                break;
            case "S":
                top.setTipoEstoque("2");
                break;
            case "N":
                top.setTipoEstoque("3");
                break;
            default:
                top.setTipoEstoque("0");
                break;
        }
        top.setAliquotaPis(regAtual.getAliquotaPis());
        top.setAliquotaCofins(regAtual.getAliquotaCofins());
        top.setAliquotaSimples(regAtual.getAliquotaSimples());
        top.setAliquotaIpi(regAtual.getAliquotaIpi());
        top.setAliquotaIcms(regAtual.getAliquotaIcms());
        top.setAliquotaSuframa(regAtual.getAliquotaSuframa());
        top.setAliquotaSimbahia(regAtual.getAliquotaSimbahia());
        String tributaIcms = regAtual.getTributaIcms();
        switch(tributaIcms){
            case "S":
                top.setTributaIcms("1");
                break;
            case "N":
                top.setTributaIcms("2");
                break;
            case "R":
                top.setTributaIcms("3");
                break;
            default:
                top.setTributaIcms("0");
                break;
        }
        String tributaIpi = regAtual.getTributaIpi();
        switch(tributaIpi){
            case "S":
                top.setTributaIpi("1");
                break;
            case "N":
                top.setTributaIpi("2");
                break;
            case "R":
                top.setTributaIpi("3");
                break;
            default:
                top.setTributaIpi("0");
                break;
        }
        String tributaSuframa = regAtual.getTributaSuframa();
        switch(tributaSuframa){
            case "S":
                top.setTributaSuframa("1");
                break;
            case "N":
                top.setTributaSuframa("2");
                break;
            case "R":
                top.setTributaSuframa("3");
                break;
            default:
                top.setTributaSuframa("0");
                break;
        }
        String tributaSimbahia = regAtual.getTributaSimbahia();
        switch(tributaSimbahia){
            case "S":
                top.setTributaSimbahia("1");
                break;
            case "N":
                top.setTributaSimbahia("2");
                break;
            case "R":
                top.setTributaSimbahia("3");
                break;
            default:
                top.setTributaSimbahia("0");
                break;
        }
        top.setBaseCalculoIcmsOp(regAtual.getBaseCalculoIcmsOp());
        top.setIcmsOpBaseRed(regAtual.getIcmsOpBaseRed());
        top.setMva(regAtual.getMva());
        top.setBaseIcmsStRed(regAtual.getBaseIcmsStRed());
        top.setIcmsCadeiaSemRed(regAtual.getIcmsCadeiaSemRed());
        top.setAliquotaIss(regAtual.getAliquotaIss());
        top.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        top.setDataCadastro(regAtual.getDataCadastro());
        top.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                top.setSituacao("1");
                break;
            case "I":
                top.setSituacao("2");
                break;
        }
    }
}
