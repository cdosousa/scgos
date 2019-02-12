/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.controle;

import br.com.gcs.controle.CMateriais;
import br.com.gcs.modelo.Materiais;
import br.com.gsm.dao.ServicosDAO;
import br.com.gsm.modelo.Servicos;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author cristiano
 */
public class CServicos {

    //objetos da classe
    ServicosDAO srvdao;
 
    // variáveis de instancia
    private static Connection conexao;
    private List<Servicos> resultado = null;
    private Servicos regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    private String cdSequencia;
    private String cdServico;

    // Construtor padrão
    public CServicos(Connection conexao) {
        this.conexao = conexao;
    }

    // método para iniciar objetos
    public void iniObjetos() {
        try {
            srvdao = new ServicosDAO(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(CServicos.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        iniObjetos();
        this.sql = sql;
        resultado = new ArrayList<Servicos>();
        //ServicosDAO srvdao = new ServicosDAO();
        srvdao.selecionar(resultado, sql);
        numReg = resultado.size();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados
    public void mostrarPesquisa(Servicos srv, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        srv.setCdServico(regAtual.getCdServico());
        srv.setNomeServico(regAtual.getNomeServico());
        srv.setCdMaterial(regAtual.getCdMaterial());
        srv.setNomeMaterial(buscarMaterial(regAtual.getCdMaterial()));
        srv.setCdUnidMedida(regAtual.getCdUnidMedida());
        srv.setDescricaoComercial(regAtual.getDescricaoComercial());
        srv.setValorServico(regAtual.getValorServico());
        srv.setValTotalAtividade(regAtual.getValTotalAtividade());
        srv.setValorMaterial(regAtual.getValorMaterial());
        srv.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        srv.setDataCadastro(regAtual.getDataCadastro());
        srv.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                srv.setSituacao("1");
                break;
            case "I":
                srv.setSituacao("2");
                break;
        }
    }

    public void gerarCodigo(Servicos srv, String sequencia) {
        cdSequencia = sequencia;
        String ret = "C";
        cdServico = String.format("%s", cdSequencia.toString().trim());
        srv.setCdServico(cdSequencia);
    }
    
    // Método para buscar nome do produto
    private String buscarMaterial(String cdMaterial) {
        Materiais mat = new Materiais();
        try {
            CMateriais cmat = new CMateriais(conexao);
            String sqltb = "SELECT * FROM  GCSMATERIAL WHERE CD_MATERIAL = '" + cdMaterial + "'";
            cmat.pesquisar(sqltb, false);
            cmat.mostrarPesquisa(mat, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do nome do Material!\nPrograma: CItemTipoVerniz.java\nErro: " + ex);
        }
        return mat.getNomeMaterial();
    }
}