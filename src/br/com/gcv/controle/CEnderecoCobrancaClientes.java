/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.controle.CMunicipios;
import br.com.controle.CUnidadeFederacao;
import br.com.gcv.dao.EnderecoCobrancaClientesDAO;
import br.com.gcv.modelo.EnderecoCobrancaCliente;
import br.com.modelo.Municipios;
import br.com.modelo.UnidadeFederacao;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 17/11/2017
 */
public class CEnderecoCobrancaClientes {

    // variáveis de instancia
    private Connection conexao;
    private List<EnderecoCobrancaCliente> resultado = null;
    private EnderecoCobrancaCliente regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CEnderecoCobrancaClientes(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<EnderecoCobrancaCliente>();
        EnderecoCobrancaClientesDAO eccdao = new EnderecoCobrancaClientesDAO(conexao);
        eccdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(EnderecoCobrancaCliente ecc, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        ecc.setCdCpfCnpj(regAtual.getCdCpfCnpj());
        ecc.setSequencia(regAtual.getSequencia());
        ecc.setTipoLogradouro(regAtual.getTipoLogradouro().trim());
        ecc.setLogradouro(regAtual.getLogradouro());
        ecc.setNumero(regAtual.getNumero());
        ecc.setComplemento(regAtual.getComplemento());
        ecc.setBairro(regAtual.getBairro());
        ecc.setCdMunicipioIbge(regAtual.getCdMunicipioIbge());
        ecc.setNomeMunicipio(buscarMunicipio(regAtual.getCdMunicipioIbge()));
        ecc.setSiglaUf(regAtual.getSiglaUf());
        ecc.setUfIbge(Integer.parseInt(buscarUF(regAtual.getSiglaUf())));
        ecc.setCdCep(regAtual.getCdCep());
        ecc.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        ecc.setDataCadastro(regAtual.getDataCadastro());
        ecc.setDataModificacao(regAtual.getDataModificacao());
        String situacao = String.valueOf(regAtual.getSituacao());
        switch (situacao) {
            case "A":
                ecc.setSituacao("1");
                break;
            case "I":
                ecc.setSituacao("2");
                break;
            default:
                ecc.setSituacao("0");
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
}
