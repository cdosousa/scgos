/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcs.controle;

import br.com.controle.CMunicipios;
import br.com.controle.CUnidadeFederacao;
import br.com.gcs.dao.FornecedoresDAO;
import br.com.gcs.modelo.Fornecedores;
import br.com.gfc.controle.CCondicaoPagamento;
import br.com.gfc.controle.CPortadores;
import br.com.gfc.controle.CTipoPagamento;
import br.com.gfc.modelo.CondicaoPagamento;
import br.com.gfc.modelo.Portadores;
import br.com.gfc.modelo.TipoPagamento;
import br.com.modelo.Municipios;
import br.com.modelo.UnidadeFederacao;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 30/07/2018
 * @version 0.01beta_0917
*/
public class CFornecedores {

    // variáveis de instancia
    private Connection conexao;
    private List<Fornecedores> resultado = null;
    private Fornecedores regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CFornecedores(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<Fornecedores>();
        FornecedoresDAO fordao = new FornecedoresDAO(conexao);
        fordao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Fornecedores fornec, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        fornec.setCdCpfCnpj(regAtual.getCdCpfCnpj());
        fornec.setCdInscEstadual(regAtual.getCdInscEstadual());
        String tipoPessoa = regAtual.getTipoPessoa();
        switch (tipoPessoa) {
            case "F":
                fornec.setTipoPessoa("1");
                break;
            case "J":
                fornec.setTipoPessoa("2");
                break;
            default:
                fornec.setTipoPessoa("0");
                break;
        }
        fornec.setNomeRazaoSocial(regAtual.getNomeRazaoSocial());
        fornec.setApelido(regAtual.getApelido());
        String optanteSimples = regAtual.getOptanteSimples();
        switch (optanteSimples) {
            case "S":
                fornec.setOptanteSimples("1");
                break;
            case "N":
                fornec.setOptanteSimples("2");
                break;
            default:
                fornec.setOptanteSimples("0");
                break;
        }
        fornec.setTipoLogradouro(regAtual.getTipoLogradouro().trim());
        fornec.setLogradouro(regAtual.getLogradouro());
        fornec.setNumero(regAtual.getNumero());
        fornec.setComplemento(regAtual.getComplemento());
        fornec.setBairro(regAtual.getBairro());
        fornec.setCdMunicipioIbge(regAtual.getCdMunicipioIbge());
        if (regAtual.getCdMunicipioIbge() != null) {
            fornec.setNomeMunicipio(buscarMunicipio(regAtual.getCdMunicipioIbge()));
        }
        fornec.setSiglaUf(regAtual.getSiglaUf());
        if (regAtual.getSiglaUf() != null) {
            fornec.setUfIbge(Integer.parseInt(buscarUF(regAtual.getSiglaUf())));
        }
        fornec.setCdCep(regAtual.getCdCep());
        fornec.setNumBanco(regAtual.getNumBanco());
        fornec.setNomeBanco(regAtual.getNomeBanco());
        fornec.setAgenciaBanco(regAtual.getAgenciaBanco());
        fornec.setNumContaBanco(regAtual.getNumContaBanco());
        fornec.setCdPortador(regAtual.getCdPortador());
        if (regAtual.getCdPortador() != null) {
            fornec.setNomePortador(buscarPortador(regAtual.getCdPortador()));
        }
        fornec.setCdTipoPagamento(regAtual.getCdTipoPagamento());
        if (regAtual.getCdTipoPagamento() != null) {
            fornec.setNomeTipoPagamento(buscarTipoPagamento(regAtual.getCdTipoPagamento()));
        }
        fornec.setCdCondPagamento(regAtual.getCdCondPagamento());
        if (regAtual.getCdCondPagamento() != null) {
            fornec.setNomeCondPag(buscarCondicaoPagamento(regAtual.getCdCondPagamento()));
        }
        fornec.setNomeBanco(regAtual.getNomeBanco());
        fornec.setNomeContato(regAtual.getNomeContato());
        fornec.setTelefone(regAtual.getTelefone());
        fornec.setCelular(regAtual.getCelular());
        fornec.setEmail(regAtual.getEmail());
        fornec.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        fornec.setDataCadastro(regAtual.getDataCadastro());
        fornec.setDataModificacao(regAtual.getDataModificacao());
        String situacao = String.valueOf(regAtual.getSituacao());
        switch (situacao) {
            case "A":
                fornec.setSituacao("1");
                break;
            case "I":
                fornec.setSituacao("2");
                break;
            default:
                fornec.setSituacao("0");
        }
    }

    // Método par buscar nome do Municipio
    private String buscarMunicipio(String cdMunicipioIbge) {
        Municipios mu = new Municipios();
        try {
            CMunicipios cmu = new CMunicipios(conexao);
            String sqlcmu = "SELECT * FROM PGSMUNICIPIO WHERE CD_MUNICIPIO_IBGE = '" + cdMunicipioIbge + "'";
            cmu.pesquisar(sqlcmu);
            cmu.mostrarPesquisa(mu, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome o Município!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return mu.getNomeMunicipio();
    }

    // Método par buscar nome da UF
    private String buscarUF(String cdSiglaUF) {
        UnidadeFederacao uf = new UnidadeFederacao();
        try {
            CUnidadeFederacao cuf = new CUnidadeFederacao(conexao);
            String sqlcuf = "SELECT * FROM PGSUF WHERE SIGLA_UF = '" + cdSiglaUF + "'";
            cuf.pesquisar(sqlcuf);
            cuf.mostrarPesquisa(uf, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome da UF!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return String.valueOf(uf.getUfIbge());
    }

    // Método par buscar nome do Portador
    private String buscarPortador(String cdPortador) {
        Portadores por = new Portadores();
        try {
            CPortadores cpor = new CPortadores(conexao);
            String sqlcpor = "SELECT * FROM GFCPORTADOR WHERE CD_PORTADOR = '" + cdPortador + "'";
            cpor.pesquisar(sqlcpor);
            cpor.mostrarPesquisa(por, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome o Município!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return por.getNomePortador();
    }

    // Método par buscar nome do Tipo de Pagamento
    private String buscarTipoPagamento(String cdTipoPagamento) {
        TipoPagamento tp = new TipoPagamento();
        try {
            CTipoPagamento ctp = new CTipoPagamento(conexao);
            String sqlctp = "SELECT * FROM GFCTIPOPAGAMENTO WHERE CD_TIPOPAGAMENTO = '" + cdTipoPagamento + "'";
            ctp.pesquisar(sqlctp);
            ctp.mostrarPesquisa(tp, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome o Município!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return tp.getNomeTipoPagamento();
    }

    // Método par buscar nome da Condição de Pagamento
    private String buscarCondicaoPagamento(String cdCondPag) {
        CondicaoPagamento cp = new CondicaoPagamento();
        try {
            CCondicaoPagamento ccp = new CCondicaoPagamento(conexao);
            String sqlctp = "SELECT * FROM GFCCONDICAOPAGAMENTO WHERE CD_CONDPAG = '" + cdCondPag + "'";
            ccp.pesquisar(sqlctp);
            ccp.mostrarPesquisa(cp, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome o Município!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return cp.getNomeCondPag();
    }
}