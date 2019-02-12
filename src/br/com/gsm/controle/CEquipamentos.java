/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.controle;

import br.com.gsm.dao.EquipamentosDAO;
import br.com.gsm.modelo.Equipamentos;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristiano
 */
public class CEquipamentos {
    // variáveis de instancia
    private List<Equipamentos> resultado = null;
    private Equipamentos regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CEquipamentos(){
        
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<Equipamentos>();
        EquipamentosDAO eqpdao = new EquipamentosDAO();
        eqpdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Equipamentos eqp, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        eqp.setCdEquipamento(regAtual.getCdEquipamento());
        eqp.setNomeEquipamento(regAtual.getNomeEquipamento());
        eqp.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        eqp.setDataCadastro(regAtual.getDataCadastro());
        eqp.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch(situacao){
            case "A":
                eqp.setSituacao("1");
                break;
            case "I":
                eqp.setSituacao("2");
                break;
        }
    }
    
}
