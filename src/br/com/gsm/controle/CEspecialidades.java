/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.controle;

import br.com.gsm.dao.EspecialidadesDAO;
import br.com.gsm.modelo.Especialidades;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author cristiano
 */
public class CEspecialidades {
    // variáveis de instancia
    private List<Especialidades> resultado = null;
    private Especialidades regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    
    // Construtor padrão
    public CEspecialidades(){
        
    }
    
    // método para acionar a pesquisa dos registro
    public  int pesquisar(String sql) throws SQLException{
        this.sql = sql;
        resultado =  new ArrayList<Especialidades>();
        EspecialidadesDAO espdao = new EspecialidadesDAO();
        espdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }
    
    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Especialidades esp, int idxAtual){
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        esp.setCdEspecialidade(regAtual.getCdEspecialidade());
        esp.setNomeEspecialidade(regAtual.getNomeEspecialidade());
        esp.setTxProdutividade(regAtual.getTxProdutividade());
        esp.setValorUnit(regAtual.getValorUnit());
        esp.setPagarIndicacao(regAtual.getPagarIndicacao());
        esp.setPagarObra(regAtual.getPagarObra());
        esp.setPagarComissao(regAtual.getPagarComissao());
        esp.setPercIndicacao(regAtual.getPercIndicacao());
        esp.setPercObra(regAtual.getPercObra());
        esp.setPercComissao(regAtual.getPercComissao());
        esp.setValorIndicacao(regAtual.getValorIndicacao());
        esp.setValorObra(regAtual.getValorObra());
        esp.setValorComissao(regAtual.getValorComissao());
        esp.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        esp.setDataCadastro(regAtual.getDataCadastro());
        esp.setDataModificacao(regAtual.getDataModificacao());
        char situacao = regAtual.getSituacao();
        switch(situacao){
            case 'A':
                esp.setSituacao('1');
                break;
            case 'I':
                esp.setSituacao('2');
                break;
        }
    }
    
}