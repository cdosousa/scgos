/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.gcv.dao.ContratoSeqTextoDAO;
import br.com.gcv.modelo.ContratoSeqTexto;

import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristiano de Oliveira Sousa 
 * @version 0.01_092017beta
 * @created on 27/03/2018
 */
public class CContratoSeqTexto {

    // variáveis de instancia
    private Connection conexao;
    private List<ContratoSeqTexto> resultado = null;
    private ContratoSeqTexto regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CContratoSeqTexto(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<ContratoSeqTexto>();
        ContratoSeqTextoDAO cstdao = new ContratoSeqTextoDAO(conexao);
        cstdao.selecionar(getResultado(), sql);
        numReg = getResultado().size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(ContratoSeqTexto cst, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = getResultado().get(idxAtual);
        cst.setCdContrato(regAtual.getCdContrato());
        cst.setCdSequencia(regAtual.getCdSequencia());
        cst.setTextoLongo(regAtual.getTextoLongo());
        cst.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        cst.setDataCadastro(regAtual.getDataCadastro());
        cst.setHoraCadastro(regAtual.getHoraCadastro());
        cst.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        cst.setDataModificacao(regAtual.getDataModificacao());
        cst.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "P":
                cst.setSituacao("Pendente");
                break;
            case "E":
                cst.setSituacao("Aguardando Envio");
                break;
            case "C":
                cst.setSituacao("Aguardando Assinatura");
                break;
            case "A":
                cst.setSituacao("Assinado");
                break;
            default:
                cst.setSituacao("Pendente");
        }
    }

    /**
     * @return the resultado
     */
    public List<ContratoSeqTexto> getResultado() {
        return resultado;
    }
}