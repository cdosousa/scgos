/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.controle.CMunicipios;
import br.com.controle.CUnidadeFederacao;
import br.com.gcv.dao.AtendimentoDAO;
import br.com.gcv.modelo.Atendimento;
import br.com.modelo.Municipios;
import br.com.modelo.SessaoUsuario;
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
 * @author Cristiano de Oliveira Sousa created on 23/11/2017
 */
public class CAtendimento {

    // variáveis de instancia
    private Connection conexao;
    private SessaoUsuario su;
    private List<Atendimento> resultado = null;
    private Atendimento regAtual;
    //private AtendimentoDAO atedao;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CAtendimento(Connection conexao, SessaoUsuario su) {
        this.conexao = conexao;
        this.su = su;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<Atendimento>();
        AtendimentoDAO atedao = new AtendimentoDAO(conexao);
        atedao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para pesquisar totais dos registro
    public int pesquisarTotais(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<Atendimento>();
        AtendimentoDAO atedao = new AtendimentoDAO(conexao);
        atedao.totalizarItens(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Atendimento ate, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        ate.setCdAtendimento(regAtual.getCdAtendimento());
        ate.setDataAtendimento(regAtual.getDataAtendimento());
        ate.setHoraAtendimento(regAtual.getHoraAtendimento());
        ate.setNomeRazaoSocial(regAtual.getNomeRazaoSocial());
        String tipoPessoa = regAtual.getTipoPessoa();
        switch (tipoPessoa) {
            case "F":
                ate.setTipoPessoa("1");
                break;
            case "J":
                ate.setTipoPessoa("2");
                break;
            default:
                ate.setTipoPessoa("0");
                break;
        }
        ate.setTelefone(regAtual.getTelefone());
        ate.setCelular(regAtual.getCelular());
        ate.setEmail(regAtual.getEmail());
        ate.setTipoLogradouro(regAtual.getTipoLogradouro().trim());
        ate.setLogradouro(regAtual.getLogradouro());
        ate.setNumero(regAtual.getNumero());
        ate.setComplemento(regAtual.getComplemento());
        ate.setBairro(regAtual.getBairro());
        ate.setCdMunicipioIbge(regAtual.getCdMunicipioIbge());
        if (!regAtual.getCdMunicipioIbge().trim().isEmpty()) {
            ate.setNomeMunicipio(buscarMunicipio(regAtual.getCdMunicipioIbge()));
        }
        ate.setSiglaUf(regAtual.getSiglaUf());
        if (regAtual.getSiglaUf() != null) {
            ate.setUfIbge(Integer.parseInt(buscarUF(regAtual.getSiglaUf())));
        }
        ate.setCdCep(regAtual.getCdCep());
        String tipoEndereo = regAtual.getTipoEndereco();
        switch (tipoEndereo) {
            case "C":
                ate.setTipoEndereco("1");
                break;
            case "R":
                ate.setTipoEndereco("2");
                break;
            default:
                ate.setTipoEndereco("1");
                break;
        }
        ate.setValorServico(regAtual.getValorServico());
        ate.setValorProdutos(regAtual.getValorProdutos());
        ate.setValorAdicionais(regAtual.getValorAdicionais());
        ate.setValorTotalBruto(regAtual.getValorTotalBruto());
        ate.setCdProposta(regAtual.getCdProposta());
        ate.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        ate.setDataCadastro(regAtual.getDataCadastro());
        ate.setHoraCadastro(regAtual.getHoraCadastro());
        ate.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        ate.setDataModificacao(regAtual.getDataModificacao());
        ate.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = String.valueOf(regAtual.getSituacao());
        switch (situacao) {
            case "AA":
                ate.setSituacao("0");
                break;
            case "AV":
                ate.setSituacao("1");
                break;
            case "PG":
                ate.setSituacao("2");
                break;
            default:
                ate.setSituacao("3");
                break;
        }
    }

    // método para atualizar total do ambiente
    public void upTotalAtendiment(Atendimento ate) {
        Atendimento regAtual = resultado.get(0);
        ate.setCdAtendimento(regAtual.getCdAtendimento());
        ate.setValorProdutos(regAtual.getValorProdutos());
        ate.setValorServico(regAtual.getValorServico());
        ate.setValorAdicionais(regAtual.getValorAdicionais());
        ate.setValorTotalBruto(regAtual.getValorTotalBruto());
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

    /**
     * Método para pesquisar se existe Proposta Comercial gerada para o
     * atendimento.
     *
     * @return int Número de Registro encontrado
     */
    public int buscarProposta() {
        int numReg = 0;
        String sql = "select * from gcvproposta where cd_atendimento = '" + regAtual.getCdAtendimento()
                + "'";
        try {
            CNewProposta cp = new CNewProposta(conexao,su);
            numReg = cp.pesquisarProposta(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return numReg;
    }

    public String criarProposta(Atendimento ate) {
        String cdProposta = "";
        CNewProposta cp = new CNewProposta(conexao, su);
        try {
            cdProposta = cp.criarProposta(ate);
        } catch (SQLException ex) {
            Logger.getLogger(CAtendimento.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cdProposta;
    }

}
