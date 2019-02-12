/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.gcs.controle.CEssenciaProdutos;
import br.com.gcs.modelo.EssenciaProdutos;
import br.com.gcv.dao.LocalPropostaDAO;
import br.com.gcv.modelo.LocalProposta;
import br.com.gcv.modelo.TipoVerniz;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 11/12/2017
 */
public class CLocalProposta {

    // variáveis de instancia
    private Connection conexao;
    private List<LocalProposta> resultado = null;
    private LocalProposta regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    private int ultSequencia;

    // Construtor padrão
    public CLocalProposta(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<LocalProposta>();
        LocalPropostaDAO lprdao = new LocalPropostaDAO(conexao);
        lprdao.selecionar(resultado, sql);
        numReg = resultado.size();
        ultSequencia = lprdao.getUltSequencia();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(LocalProposta lpr, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        lpr.setCdProposta(regAtual.getCdProposta());
        lpr.setCdRevisao(regAtual.getCdRevisao());
        lpr.setCdLocal(regAtual.getCdLocal());
        lpr.setCdLocalAtend(regAtual.getCdLocalAtend());
        lpr.setNomeLocal(regAtual.getNomeLocal());
        lpr.setMetragemArea(regAtual.getMetragemArea());
        lpr.setPercPerda(regAtual.getPercPerda());
        lpr.setTipoPiso(regAtual.getTipoPiso());
        lpr.setTipoRodape(regAtual.getTipoRodape());
        lpr.setMetragemRodape(regAtual.getMetragemRodape());
        lpr.setLargura(regAtual.getLargura());
        lpr.setComprimento(regAtual.getComprimento());
        lpr.setEspessura(regAtual.getEspessura());
        lpr.setTingimento(regAtual.getTingimento());
        lpr.setClareamento(regAtual.getClareamento());
        lpr.setCdTipoVerniz(regAtual.getCdTipolVerniz());
        lpr.setNomeTipoVerniz(buscarTipoVerniz(regAtual.getCdTipolVerniz()));
        lpr.setCdEssencia(regAtual.getCdEssencia());
        if (!regAtual.getCdEssencia().isEmpty()) {
            lpr.setNomeEssenciaMadeira(buscarEssencia(regAtual.getCdEssencia(), "N"));
        }
        lpr.setValorServico(regAtual.getValorServico());
        lpr.setValorProdutos(regAtual.getValorProdutos());
        lpr.setValorAdicionais(regAtual.getValorAdicionais());
        if (regAtual.getValorDescontos() != 0) {
            lpr.setValorDescontos(regAtual.getValorDescontos());
        }else
            lpr.setValorDescontos(0.00);
        if(regAtual.getValorOutrosDescontos() != 0){
            lpr.setValorOutrosDescontos(regAtual.getValorOutrosDescontos());
        }else{
            lpr.setValorOutrosDescontos(0.00);
        }
        lpr.setValorTotalBruto(regAtual.getValorTotalBruto());
        lpr.setValorTotalLiquido(regAtual.getValorTotalLiquido());
        lpr.setObs(regAtual.getObs());
        lpr.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        lpr.setDataCadastro(regAtual.getDataCadastro());
        lpr.setHoraCadastro(regAtual.getHoraCadastro());
        lpr.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        lpr.setDataModificacao(regAtual.getDataModificacao());
        lpr.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = String.valueOf(regAtual.getSituacao());
        switch (situacao) {
            case "AA":
                lpr.setSituacao("1");
                break;
            case "AV":
                lpr.setSituacao("2");
                break;
            case "NI":
                lpr.setSituacao("3");
                break;
            default:
                lpr.setSituacao("0");
        }
    }

    // Método para buscar nome da tabela de preco
    private String buscarTipoVerniz(String cdTipoVerniz) {
        TipoVerniz tv = new TipoVerniz();
        try {
            CTipoVerniz ctv = new CTipoVerniz(conexao);
            String sqltb = "SELECT * FROM  GCVTIPOVERNIZ WHERE CD_TIPOVERNIZ = '" + cdTipoVerniz + "'";
            int numReg = ctv.pesquisar(sqltb);
            if (numReg > 0) {
                ctv.mostrarPesquisa(tv, 0);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do nome do Tipo Verniz!\nPrograma: CItemTipoVerniz.java\nErro: " + ex);
        }
        return tv.getNomeTipoVerniz();
    }

    // Método par buscar nome da essencia e se forma código
    private String buscarEssencia(String cdEssencia, String ret) {
        EssenciaProdutos ess = new EssenciaProdutos();
        try {
            CEssenciaProdutos pess = new CEssenciaProdutos(conexao);
            String sqless = "SELECT * FROM GCSESSENCIA WHERE CD_ESSENCIA = '" + cdEssencia + "'";
            pess.pesquisar(sqless);
            pess.mostrarPesquisa(ess, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca da Essencia de Produtos!\nPrograma: GCSROT0091\nErro: " + ex);
        }
        if ("N".equals(ret)) {
            return ess.getNomeEssencia();
        } else {
            return ess.getGerarCodigo();
        }
    }

    /**
     * @return the ultSequencia
     */
    public int getUltSequencia() {
        return ultSequencia;
    }
}