/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.gcv.dao.ContatoDAO;
import br.com.gcv.modelo.Contato;

import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 22/11/2017
 */
public class CContato {

    // variáveis de instancia
    private Connection conexao;
    private List<Contato> resultado = null;
    private Contato regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;

    // Construtor padrão
    public CContato(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        this.sql = sql;
        resultado = new ArrayList<Contato>();
        ContatoDAO condao = new ContatoDAO(conexao);
        condao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Contato con, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        con.setCdAtendimento(regAtual.getCdAtendimento());
        con.setDataAtendimento(regAtual.getDataAtendimento());
        con.setHoraAtendimento(regAtual.getHoraAtendimento());
        con.setNomeRazaoSocial(regAtual.getNomeRazaoSocial());
        String tipoPessoa = regAtual.getTipoPessoa();
        switch (tipoPessoa) {
            case "F":
                con.setTipoPessoa("1");
                break;
            case "J":
                con.setTipoPessoa("2");
                break;
            default:
                con.setTipoPessoa("0");
                break;
        }
        con.setTelefone(regAtual.getTelefone());
        con.setEmail(regAtual.getEmail());
        con.setCdVistoria(regAtual.getCdVistoria());
        con.setCdProposta(regAtual.getCdProposta());
        con.setObs(regAtual.getObs());
        con.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        con.setDataCadastro(regAtual.getDataCadastro());
        con.setHoraCadastro(regAtual.getHoraCadastro());
        con.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        con.setDataModificacao(regAtual.getDataModificacao());
        con.setHoraModificacao(regAtual.getHoraModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "AA":
                con.setSituacao("Aguardando Agendamento");
                break;
            case "AV":
                con.setSituacao("Aguardando Vistoria");
                break;
            case "NI":
                con.setSituacao("Não Iniciado");
                break;
            case "PG":
                con.setSituacao("Proposta Gerada");
                break;
            default:
                con.setSituacao("Pendente");
        }
    }
}
