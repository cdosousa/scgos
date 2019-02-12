/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcs.controle;

import br.com.gcs.dao.ArmazensDAO;
import br.com.gcs.modelo.Armazens;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917
 * created on 29/09/2017
 */
public class CArmazens {
    // variáveis de instancia
    private List<Armazens> resultado = null;
    private Armazens regAtual;
    private Connection conexao;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CArmazens(Connection conexao){
        this.conexao = conexao;
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<Armazens>();
        ArmazensDAO cddao = new ArmazensDAO(conexao);
        cddao.selecionar(resultado, sql);
        numReg = resultado.size();
        //cddao.desconectar();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Armazens cd, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        cd.setCdArmazem(regAtual.getCdArmazem());
        cd.setNomeArmazem(regAtual.getNomeArmazem());
        String tipoArmazem = regAtual.getTipoArmazem();
        switch(tipoArmazem){
            case "P":
                cd.setTipoArmazem("1");
                break;
            case "T":
                cd.setTipoArmazem("2");
                break;
        }
        String permiteSeparacao = regAtual.getPermiteSeparacao();
        switch(permiteSeparacao){
            case "S":
                cd.setPermiteSeparacao("1");
                break;
            case "N":
                cd.setPermiteSeparacao("2");
                break;
        }
        cd.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        cd.setDataCadastro(regAtual.getDataCadastro());
        cd.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch(situacao){
            case "A":
                cd.setSituacao("1");
                break;
            case "I":
                cd.setSituacao("2");
                break;
        }
    }
}