/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.controle.CMunicipios;
import br.com.gfc.dao.BancosDAO;
import br.com.gfc.modelo.Bancos;
import br.com.modelo.Municipios;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917
 * created on 03/11/2017
 */
public class CBancos {
    // variáveis de instancia
    private Connection conexao;
    private List<Bancos> resultado = null;
    private Bancos regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CBancos(Connection conexao){
        this.conexao = conexao;
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<Bancos>();
        BancosDAO bcdao = new BancosDAO(conexao);
        bcdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Bancos bc, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        bc.setCdBanco(regAtual.getCdBanco());
        bc.setNomeBanco(regAtual.getNomeBanco());
        bc.setTipoLogradouro(regAtual.getTipoLogradouro());
        bc.setLogradouro(regAtual.getLogradouro());
        bc.setNumero(regAtual.getNumero());
        bc.setComplemento(regAtual.getComplemento());
        bc.setBairro(regAtual.getBairro());
        bc.setCdMunicipioIbge(regAtual.getCdMunicipioIbge());
        bc.setNomeMunicipio(buscarMunicipio(regAtual.getCdMunicipioIbge()));
        bc.setCdUfSigla(regAtual.getCdUfSigla());
        bc.setCep(regAtual.getCep());
        bc.setContato(regAtual.getContato());
        bc.setTelefone(regAtual.getTelefone());
        bc.setCelular(regAtual.getCelular());
        bc.setEmail(regAtual.getEmail());
        bc.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        bc.setDataCadastro(regAtual.getDataCadastro());
        bc.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch(situacao){
            case "A":
                bc.setSituacao("1");
                break;
            case "I":
                bc.setSituacao("2");
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
}