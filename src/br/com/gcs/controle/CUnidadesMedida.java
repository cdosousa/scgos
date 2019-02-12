/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcs.controle;

import br.com.gcs.dao.UnidadesMedidaDAO;
import br.com.gcs.modelo.UnidadesMedida;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristiano
 */
public class CUnidadesMedida {
    // variáveis de instancia
    private List<UnidadesMedida> resultado = null;
    private UnidadesMedida regAtual;
    private Connection conexao;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CUnidadesMedida(Connection conexao){
        this.conexao = conexao;
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<UnidadesMedida>();
        UnidadesMedidaDAO umdao = new UnidadesMedidaDAO(conexao);
        umdao.selecionar(resultado, sql);
        numReg = resultado.size();
        //umdao.desconectar();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(UnidadesMedida um, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        um.setCdUnidMedida(regAtual.getCdUnidMedida());
        um.setNomeUnidMedida(regAtual.getNomeUnidMedida());
        um.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        um.setDataCadastro(regAtual.getDataCadastro());
        um.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch(situacao){
            case "A":
                um.setSituacao("1");
                break;
            case "I":
                um.setSituacao("2");
                break;
        }
    }
    
}