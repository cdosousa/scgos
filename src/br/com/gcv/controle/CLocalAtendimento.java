/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.gcs.controle.CEssenciaProdutos;
import br.com.gcs.modelo.EssenciaProdutos;
import br.com.gcv.dao.LocalAtendimentoDAO;
import br.com.gcv.modelo.LocalAtendimento;
import br.com.gcv.modelo.TipoVerniz;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 23/11/2017
 */
public class CLocalAtendimento {

    // variáveis de instancia
    private Connection conexao;
    private List<LocalAtendimento> resultado = null;
    private LocalAtendimento regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    private int ultSequencia;

    // Construtor padrão
    public CLocalAtendimento(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<LocalAtendimento>();
        LocalAtendimentoDAO latdao = new LocalAtendimentoDAO(conexao);
        latdao.selecionar(resultado, sql);
        numReg = resultado.size();
        ultSequencia = latdao.getUltSequencia();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(LocalAtendimento ate, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        ate.setCdAtendimento(regAtual.getCdAtendimento());
        ate.setCdLocal(regAtual.getCdLocal());
        ate.setNomeLocal(regAtual.getNomeLocal());
        ate.setMetragemArea(regAtual.getMetragemArea());
        ate.setPercPerda(regAtual.getPercPerda());
        ate.setTipoPiso(regAtual.getTipoPiso());
        ate.setTipoRodape(regAtual.getTipoRodape());
        ate.setMetragemRodape(regAtual.getMetragemRodape());
        ate.setLargura(regAtual.getLargura());
        ate.setComprimento(regAtual.getComprimento());
        ate.setEspessura(regAtual.getEspessura());
        ate.setTingimento(regAtual.getTingimento());
        ate.setClareamento(regAtual.getClareamento());
        ate.setCdTipoVerniz(regAtual.getCdTipolVerniz());
        ate.setNomeTipoVerniz(buscarTipoVerniz(regAtual.getCdTipolVerniz()));
        ate.setCdEssencia(regAtual.getCdEssencia());
        if (!regAtual.getCdEssencia().isEmpty()) {
            ate.setNomeEssenciaMadeira(buscarEssencia(regAtual.getCdEssencia(), "N"));
        }
        ate.setObs(regAtual.getObs());
        ate.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        ate.setDataCadastro(regAtual.getDataCadastro());
        ate.setHoraCadastro(regAtual.getHoraCadastro());
        ate.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        ate.setDataModificacao(regAtual.getDataModificacao());
        ate.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = String.valueOf(regAtual.getSituacao());
        switch (situacao) {
            case "AA":
                ate.setSituacao("1");
                break;
            case "AV":
                ate.setSituacao("2");
                break;
            case "NI":
                ate.setSituacao("3");
                break;
            default:
                ate.setSituacao("0");
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
