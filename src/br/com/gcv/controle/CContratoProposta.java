/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.gcv.dao.ContratoPropostaDAO;
import br.com.gcv.modelo.ContratoProposta;

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
public class CContratoProposta {

    // variáveis de instancia
    private Connection conexao;
    private List<ContratoProposta> resultado = null;
    private ContratoProposta regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CContratoProposta(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<ContratoProposta>();
        ContratoPropostaDAO cpdao = new ContratoPropostaDAO(conexao);
        cpdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(ContratoProposta cp, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        cp.setCdContrato(regAtual.getCdContrato());
        cp.setCdProposta(regAtual.getCdProposta());
        cp.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        cp.setDataCadastro(regAtual.getDataCadastro());
        cp.setHoraCadastro(regAtual.getHoraCadastro());
        cp.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        cp.setDataModificacao(regAtual.getDataModificacao());
        cp.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "P":
                cp.setSituacao("Pendente");
                break;
            case "E":
                cp.setSituacao("Aguardando Envio");
                break;
            case "C":
                cp.setSituacao("Aguardando Assinatura");
                break;
            case "A":
                cp.setSituacao("Assinado");
                break;
            default:
                cp.setSituacao("Pendente");
        }
    }
}