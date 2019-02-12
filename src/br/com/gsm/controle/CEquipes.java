/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.controle;

import br.com.gsm.dao.EquipesDAO;
import br.com.gsm.modelo.Equipes;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 26/10/2017
 */
public class CEquipes {
    // variáveis de instancia
    private List<Equipes> resultado = null;
    private Equipes regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    private String cdSequencia;
    private String cdEquipe;
    
    // Construtor padrão
    public CEquipes(){
        
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<Equipes>();
        EquipesDAO eqpdao = new EquipesDAO();
        eqpdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Equipes eqp, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        eqp.setCdEquipe(regAtual.getCdEquipe());
        eqp.setNomeEquipe(regAtual.getNomeEquipe());
        eqp.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        eqp.setDataCadastro(regAtual.getDataCadastro());
        eqp.setDataModificacao(regAtual.getDataModificacao());
        String situacao = String.valueOf(regAtual.getSituacao());
        switch(situacao){
            case "A":
                eqp.setSituacao('1');
                break;
            case "I":
                eqp.setSituacao('2');
                break;
        }
    }
    public void gerarCodigo(Equipes eqp, String sequencia) {
        cdSequencia = sequencia;
        String ret = "C";
        cdEquipe = String.format("%s",cdSequencia.toString().trim());
        eqp.setCdEquipe(cdSequencia);
    }
}