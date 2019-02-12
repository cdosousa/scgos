/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.controle;

import br.com.controle.CMunicipios;
import br.com.controle.CUnidadeFederacao;
import br.com.gsm.dao.TecnicosDAO;
import br.com.gsm.modelo.Tecnicos;
import br.com.modelo.Municipios;
import br.com.modelo.UnidadeFederacao;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 23/10/2017
 */
public class CTecnicos {
    // variáveis de instancia
    private Connection conexao;
    private List<Tecnicos> resultado = null;
    private Tecnicos regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CTecnicos(Connection conexao){
        this.conexao = conexao;
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<Tecnicos>();
        TecnicosDAO tecdao = new TecnicosDAO(conexao);
        tecdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Tecnicos tec, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        tec.setCpf(regAtual.getCpf());
        tec.setNomeTecnico(regAtual.getNomeTecnico());
        tec.setRg(regAtual.getRg());
        tec.setDataEmissaoRg(regAtual.getDataEmissaoRg());
        tec.setOrgaoExpedidorRg(regAtual.getOrgaoExpedidorRg());
        tec.setTipoLogradouro(regAtual.getTipoLogradouro().trim());
        tec.setLogradouro(regAtual.getLogradouro());
        tec.setNumero(regAtual.getNumero());
        tec.setComplemento(regAtual.getComplemento());
        tec.setBairro(regAtual.getBairro());
        tec.setCdMunicipioIbge(regAtual.getCdMunicipioIbge());
        tec.setNomeMunicipio(buscarMunicipio(regAtual.getCdMunicipioIbge()));
        tec.setCdSiglaUf(regAtual.getCdSiglaUf());
        tec.setCdSiglaIbge(buscarUF(regAtual.getCdSiglaUf()));
        tec.setCep(regAtual.getCep());
        tec.setTelefone(regAtual.getTelefone());
        tec.setCelular(regAtual.getCelular());
        tec.setEmail(regAtual.getEmail());
        String possuiCNH = regAtual.getPossuiHabilitacao();
        switch(possuiCNH){
            case "S":
                tec.setPossuiHabilitacao("1");
                break;
            case "N":
                tec.setPossuiHabilitacao("2");
                break;
            default:
                tec.setPossuiHabilitacao("0");
                break;
        }
        String categoriaCNH = regAtual.getCategoriaCnh();
        switch(categoriaCNH){
            case "A":
                tec.setCategoriaCnh("1");
                break;
            case "B":
                tec.setCategoriaCnh("2");
                break;
            case "AB":
                tec.setCategoriaCnh("3");
                break;
            case "C":
                tec.setCategoriaCnh("4");
                break;
            case "D":
                tec.setCategoriaCnh("5");
                break;
            case "E":
                tec.setCategoriaCnh("6");
                break;
            default:
                tec.setCategoriaCnh("0");
                break;
        }
        tec.setNumCnh(regAtual.getNumCnh());
        tec.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        tec.setDataCadastro(regAtual.getDataCadastro());
        tec.setDataModificacao(regAtual.getDataModificacao());
        String situacao = String.valueOf(regAtual.getSituacao());
        switch(situacao){
            case "A":
                tec.setSituacao('1');
                break;
            case "I":
                tec.setSituacao('2');
                break;
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