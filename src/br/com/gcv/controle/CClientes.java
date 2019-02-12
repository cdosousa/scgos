/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.controle.CMunicipios;
import br.com.controle.CUnidadeFederacao;
import br.com.gcv.dao.ClientesDAO;
import br.com.gcv.modelo.Clientes;
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
 * @author Cristiano de Oliveira Sousa created on 14/11/2017
 */
public class CClientes {

    // variáveis de instancia
    private Connection conexao;
    private List<Clientes> resultado = null;
    private Clientes regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CClientes(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<Clientes>();
        ClientesDAO clidao = new ClientesDAO(conexao);
        clidao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Clientes cli, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        cli.setCdCpfCnpj(regAtual.getCdCpfCnpj());
        cli.setCdInscEstadual(regAtual.getCdInscEstadual());
        String tipoPessoa = regAtual.getTipoPessoa();
        switch (tipoPessoa) {
            case "F":
                cli.setTipoPessoa("1");
                break;
            case "J":
                cli.setTipoPessoa("2");
                break;
            default:
                cli.setTipoPessoa("0");
                break;
        }
        cli.setNomeRazaoSocial(regAtual.getNomeRazaoSocial());
        cli.setApelido(regAtual.getApelido());
        String optanteSimples = regAtual.getOptanteSimples();
        switch (optanteSimples) {
            case "S":
                cli.setOptanteSimples("1");
                break;
            case "N":
                cli.setOptanteSimples("2");
                break;
            default:
                cli.setOptanteSimples("0");
                break;
        }
        String optanteSimbahia = regAtual.getOptanteSimbahia();
        switch (optanteSimbahia) {
            case "S":
                cli.setOptanteSimbahia("1");
                break;
            case "N":
                cli.setOptanteSimbahia("2");
                break;
            default:
                cli.setOptanteSimbahia("0");
                break;
        }
        String optanteSuframa = regAtual.getOptanteSuframa();
        switch (optanteSuframa) {
            case "S":
                cli.setOptanteSuframa("1");
                break;
            case "N":
                cli.setOptanteSuframa("2");
                break;
            default:
                cli.setOptanteSuframa("0");
                break;
        }
        cli.setTipoLogradouro(regAtual.getTipoLogradouro().trim());
        cli.setLogradouro(regAtual.getLogradouro());
        cli.setNumero(regAtual.getNumero());
        cli.setComplemento(regAtual.getComplemento());
        cli.setBairro(regAtual.getBairro());
        cli.setCdMunicipioIbge(regAtual.getCdMunicipioIbge());
        if (regAtual.getCdMunicipioIbge() != null) {
            cli.setNomeMunicipio(buscarMunicipio(regAtual.getCdMunicipioIbge()));
        }
        cli.setSiglaUf(regAtual.getSiglaUf());
        if (regAtual.getSiglaUf() != null) {
            cli.setUfIbge(Integer.parseInt(buscarUF(regAtual.getSiglaUf())));
        }
        cli.setCdCep(regAtual.getCdCep());
        cli.setNumBanco(regAtual.getNumBanco());
        cli.setNomeBanco(regAtual.getNomeBanco());
        cli.setAgenciaBanco(regAtual.getAgenciaBanco());
        cli.setNumContaBanco(regAtual.getNumContaBanco());
        cli.setCdPortador(regAtual.getCdPortador());
        if (regAtual.getCdPortador() != null) {
            cli.setNomePortador(buscarPortador(regAtual.getCdPortador()));
        }
        cli.setCdTipoPagamento(regAtual.getCdTipoPagamento());
        if (regAtual.getCdTipoPagamento() != null) {
            cli.setNomeTipoPagamento(buscarTipoPagamento(regAtual.getCdTipoPagamento()));
        }
        cli.setCdCondPagamento(regAtual.getCdCondPagamento());
        if (regAtual.getCdCondPagamento() != null) {
            cli.setNomeCondPag(buscarCondicaoPagamento(regAtual.getCdCondPagamento()));
        }
        cli.setNomeBanco(regAtual.getNomeBanco());
        cli.setNomeContato(regAtual.getNomeContato());
        cli.setTelefone(regAtual.getTelefone());
        cli.setCelular(regAtual.getCelular());
        cli.setEmail(regAtual.getEmail());
        cli.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        cli.setDataCadastro(regAtual.getDataCadastro());
        cli.setDataModificacao(regAtual.getDataModificacao());
        String situacao = String.valueOf(regAtual.getSituacao());
        switch (situacao) {
            case "A":
                cli.setSituacao("1");
                break;
            case "I":
                cli.setSituacao("2");
                break;
            default:
                cli.setSituacao("0");
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
