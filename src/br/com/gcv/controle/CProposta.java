/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.controle.CMunicipios;
import br.com.controle.CUnidadeFederacao;
import br.com.gcv.dao.PropostaDAO;
import br.com.gcv.modelo.Proposta;
import br.com.gfc.controle.CCondicaoPagamento;
import br.com.gfc.controle.CTipoPagamento;
import br.com.gfc.modelo.CondicaoPagamento;
import br.com.gfc.modelo.TipoPagamento;
import br.com.gsm.controle.CTecnicos;
import br.com.gsm.modelo.Tecnicos;
import br.com.modelo.Municipios;
import br.com.modelo.UnidadeFederacao;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 11/12/2017
 */
public class CProposta {

    // variáveis de instancia
    private Connection conexao;
    private List<Proposta> resultado = null;
    private Proposta regAtual;
    PropostaDAO prodao;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CProposta(Connection conexao) {
        this.conexao = conexao;
        try {
            prodao = new PropostaDAO(this.conexao);
        } catch (SQLException ex) {
            Logger.getLogger(CProposta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<Proposta>();
        prodao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para pesquisar totais dos registro
    public int pesquisarTotais(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<Proposta>();
        prodao.totalizarItens(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Proposta pro, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        pro.setCdProposta(regAtual.getCdProposta());
        pro.setCdRevisao(regAtual.getCdRevisao());
        pro.setCdAtendimento(regAtual.getCdAtendimento());
        pro.setCdVistoria(regAtual.getCdVistoria());
        pro.setCdVendedor(regAtual.getCdVendedor());
        pro.setNomeVendedor(buscarTecnico(regAtual.getCdVendedor()));
        pro.setCdTecnico(regAtual.getCdTecnico());
        pro.setNomeTecnico(buscarTecnico(regAtual.getCdTecnico()));
        pro.setNomeRazaoSocial(regAtual.getNomeRazaoSocial());
        String tipoPessoa = regAtual.getTipoPessoa();
        switch (tipoPessoa) {
            case "F":
                pro.setTipoPessoa("1");
                break;
            case "J":
                pro.setTipoPessoa("2");
                break;
            default:
                pro.setTipoPessoa("0");
                break;
        }
        pro.setTelefone(regAtual.getTelefone());
        pro.setCelular(regAtual.getCelular());
        pro.setEmail(regAtual.getEmail());
        pro.setTipoLogradouro(regAtual.getTipoLogradouro().trim());
        pro.setLogradouro(regAtual.getLogradouro());
        pro.setNumero(regAtual.getNumero());
        pro.setComplemento(regAtual.getComplemento());
        pro.setBairro(regAtual.getBairro());
        pro.setCdMunicipioIbge(regAtual.getCdMunicipioIbge());
        pro.setNomeMunicipio(buscarMunicipio(regAtual.getCdMunicipioIbge()));
        pro.setSiglaUf(regAtual.getSiglaUf());
        pro.setUfIbge(Integer.parseInt(buscarUF(regAtual.getSiglaUf())));
        pro.setCdCep(regAtual.getCdCep());
        String tipoEndereco = regAtual.getTipoEndereco();
        switch (tipoEndereco) {
            case "C":
                pro.setTipoEndereco("1");
                break;
            case "R":
                pro.setTipoEndereco("2");
                break;
            default:
                pro.setTipoEndereco("0");
                break;
        }
        String tipoPedido = regAtual.getTipoPedido();
        switch (tipoPedido) {
            case "A":
                pro.setTipoPedido("1");
                break;
            case "R":
                pro.setTipoPedido("2");
                break;
            case "S":
                pro.setTipoPedido("3");
                break;
            default:
                pro.setTipoPedido("0");
                break;
        }
        pro.setCdTipoPagamento(regAtual.getCdTipoPagamento());
        pro.setNomeTipoPagamento(buscarTipoPagamento(regAtual.getCdTipoPagamento()));
        pro.setCdCondPagamento(regAtual.getCdCondPagamento());
        pro.setNomeCondPag(buscarCondicaoPagamento(regAtual.getCdCondPagamento()));
        pro.setCdPedido(regAtual.getCdPedido());
        pro.setValorServico(regAtual.getValorServico());
        pro.setValorProdutos(regAtual.getValorProdutos());
        pro.setValorAdicionais(regAtual.getValorAdicionais());
        pro.setValorDescontos(regAtual.getValorDescontos());
        pro.setValorOutrosDescontos(regAtual.getValorOutrosDescontos());
        pro.setValorTotalBruto(regAtual.getValorTotalBruto());
        pro.setValorTotalLiquido(regAtual.getValorTotalLiquido());
        pro.setPrazoExecucao(regAtual.getPrazoExecucao());
        pro.setObs(regAtual.getObs());
        pro.setDataEnvio(regAtual.getDataEnvio());
        pro.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        pro.setDataCadastro(regAtual.getDataCadastro());
        pro.setHoraCadastro(regAtual.getHoraCadastro());
        pro.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        pro.setDataModificacao(regAtual.getDataModificacao());
        pro.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = String.valueOf(regAtual.getSituacao());
        switch (situacao) {
            case "AA":
                pro.setSituacao("1");
                break;
            case "AV":
                pro.setSituacao("2");
                break;
            case "NI":
                pro.setSituacao("3");
                break;
            default:
                pro.setSituacao("0");
        }
    }

    // método para atualizar total do ambiente
    public void upTotalProposta(Proposta pro) {
        Proposta regAtual = resultado.get(0);
        pro.setCdProposta(regAtual.getCdProposta());
        pro.setCdRevisao(regAtual.getCdRevisao());
        pro.setValorProdutos(regAtual.getValorProdutos());
        pro.setValorServico(regAtual.getValorServico());
        pro.setValorAdicionais(regAtual.getValorAdicionais());
        pro.setValorDescontos(regAtual.getValorDescontos());
        pro.setValorOutrosDescontos(regAtual.getValorOutrosDescontos());
        pro.setValorTotalBruto(regAtual.getValorTotalBruto());
        pro.setValorTotalLiquido(regAtual.getValorTotalLiquido());
    }

    // Método par buscar nome do Municipio
    private String buscarMunicipio(String cdMunicipioIbge) {
        Municipios mu = new Municipios();
        try {
            CMunicipios cmu = new CMunicipios(conexao);
            String sqlcmu = "SELECT * FROM PGSMUNICIPIO WHERE CD_MUNICIPIO_IBGE = '" + cdMunicipioIbge + "'";
            if (cmu.pesquisar(sqlcmu) > 0) {
                cmu.mostrarPesquisa(mu, 0);
            }
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
            if (cuf.pesquisar(sqlcuf) > 0) {
                cuf.mostrarPesquisa(uf, 0);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome da UF!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return String.valueOf(uf.getUfIbge());
    }

    // Método par buscar nome do Tipo de Pagamento
    private String buscarTipoPagamento(String cdTipoPagamento) {
        TipoPagamento tp = new TipoPagamento();
        try {
            CTipoPagamento ctp = new CTipoPagamento(conexao);
            String sqlctp = "SELECT * FROM GFCTIPOPAGAMENTO WHERE CD_TIPOPAGAMENTO = '" + cdTipoPagamento + "'";
            int numReg = ctp.pesquisar(sqlctp);
            if (numReg > 0) {
                ctp.mostrarPesquisa(tp, 0);
            }
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
            int numReg = ccp.pesquisar(sqlctp);
            if (numReg > 0) {
                ccp.mostrarPesquisa(cp, 0);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome o Município!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return cp.getNomeCondPag();
    }

    // Método para buscar nome do técnico
    private String buscarTecnico(String cdTecnico) {
        Tecnicos tec = new Tecnicos();
        CTecnicos ctec = new CTecnicos(conexao);
        String sqltec = "SELECT * FROM GSMTECNICOS WHERE CPF = '" + cdTecnico + "'";
        try {
            int numReg = ctec.pesquisar(sqltec);
            if (numReg > 0) {
                ctec.mostrarPesquisa(tec, 0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CProposta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tec.getNomeTecnico();
    }
}
