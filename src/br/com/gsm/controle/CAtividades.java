/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.controle;

import br.com.gsm.dao.AtividadesDAO;
import br.com.gsm.modelo.Atividades;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristiano
 */
public class CAtividades {
    // variáveis de instancia
    private Connection conexao;
    private List<Atividades> resultado = null;
    private Atividades regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    private String cdSequencia;
    private String cdAtividade;
    
    // Construtor padrão
    public CAtividades(Connection conexao){
        this.conexao = conexao;
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<Atividades>();
        AtividadesDAO atvdao = new AtividadesDAO(conexao);
        atvdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Atividades atv, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        atv.setCdAtividade(regAtual.getCdAtividade());
        atv.setNomeAtividade(regAtual.getNomeAtividade());
            atv.setValTotalTarefa(regAtual.getValTotalTarefa());
        atv.setValTotalEquipamento(regAtual.getValTotalEquipamento());
        atv.setValTotalAtividade(regAtual.getValTotalAtividade());
        atv.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        atv.setDataCadastro(regAtual.getDataCadastro());
        atv.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch(situacao){
            case "A":
                atv.setSituacao("1");
                break;
            case "I":
                atv.setSituacao("2");
                break;
        }
    }
    public void gerarCodigo(Atividades atv, String sequencia) {
        cdSequencia = sequencia;
        String ret = "C";
        cdAtividade = String.format("%s",cdSequencia.toString().trim());
        atv.setCdAtividade(cdSequencia);
    }
}
