/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.controle;

import br.com.gsm.dao.MateriaisServicoDAO;
import br.com.gsm.modelo.MateriaisServico;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 31/10/2017
 */
public class CMateriaisServico {

    //objetos da classe
    //private MateriaisServicoDAO msdao;
 
    // variáveis de instancia
    private Connection conexao;
    private List<MateriaisServico> resultado = null;
    private MateriaisServico regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    private String cdSequencia;
    private String cdServico;

    // Construtor padrão
    public CMateriaisServico(Connection conexao) {
        this.conexao = conexao;
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        //iniObjetos();
        this.sql = sql;
        resultado = new ArrayList<MateriaisServico>();
        MateriaisServicoDAO msdao = new MateriaisServicoDAO(conexao);
        //ServicosDAO srvdao = new ServicosDAO();
        msdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(MateriaisServico ms, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        ms.setSequencia(regAtual.getSequencia());
        ms.setCdServico(regAtual.getCdServico());
        ms.setCdMaterial(regAtual.getCdMaterial());
        ms.setNomeMaterial(regAtual.getNomeMaterial());
        ms.setCdUnidMedida(regAtual.getCdUnidMedida());
        ms.setNomeUnidMedida(regAtual.getNomeUnidMedida());
        ms.setQtdeMaterial(regAtual.getQtdeMaterial());
        ms.setValorUnit(regAtual.getValorUnit());
        ms.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        ms.setDataCadastro(regAtual.getDataCadastro());
        ms.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                ms.setSituacao("1");
                break;
            case "I":
                ms.setSituacao("2");
                break;
        }
    }

    public void gerarCodigo(MateriaisServico ms, String sequencia) {
        cdSequencia = sequencia;
        String ret = "C";
        cdServico = String.format("%s", cdSequencia.toString().trim());
        ms.setCdServico(cdSequencia);
    }
}