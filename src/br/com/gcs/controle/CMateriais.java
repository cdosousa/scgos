/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GCSROT0091
 */
package br.com.gcs.controle;

import br.com.gcs.dao.MateriaisDAO;
import br.com.gcs.modelo.Armazens;
import br.com.gcs.modelo.Categorias;
import br.com.gcs.modelo.ClasseProdutos;
import br.com.gcs.modelo.EssenciaProdutos;
import br.com.gcs.modelo.GrupoProdutos;
import br.com.gcs.modelo.MarcaProdutos;
import br.com.gcs.modelo.Materiais;
import br.com.gcs.modelo.SubGrupoProdutos;
import br.com.gcs.modelo.UnidadesMedida;
import br.com.gcv.controle.CItemLocal;
import br.com.gcv.controle.CItemTabela;
import br.com.gcv.modelo.ItemTabela;
import br.com.gfc.controle.CContasContabeis;
import br.com.gfc.modelo.ContasContabeis;
import br.com.gfr.controle.CCentroCustos;
import br.com.gfr.modelo.CentroCustos;
import br.com.gsm.controle.CServicos;
import br.com.gsm.modelo.Servicos;
import br.com.modelo.DataSistema;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 03/10/2017
 */
public class CMateriais {

    //Objetos da Classe
    MateriaisDAO matdao;

    // variáveis de instancia
    private Connection conexao;
    private List<Materiais> resultado = null;
    private Materiais regAtual;
    private int idxAtual;
    private int numReg;
    private String sql;
    private String cdSequencia;
    private String cdMaterial;

    private String grupo = "";
    private String subGrupo = "";
    private String categoria = "";
    private String essencia = "";

    private boolean buscarPreco = false;
    private double descAlcada = 0;
    private double valorUnitBruto = 0;
    private double valorUnitLiquido = 0;
    private double valorServico = 0;

    // Construtor padrão
    public CMateriais(Connection conexao) {
        this.conexao = conexao;
        iniObjetos();
    }

    // inicia objeto da classe
    public void iniObjetos() {
        try {
            matdao = new MateriaisDAO(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(CMateriais.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql, boolean buscarPreco) throws SQLException {
        this.sql = sql;
        this.buscarPreco = buscarPreco;
        resultado = new ArrayList<Materiais>();
        //MateriaisDAO matdao = new MateriaisDAO();
        matdao.selecionar(resultado, sql);
        numReg = resultado.size();
        //matdao.desconectar();
        return numReg;
    }

    // método para prencher a tela com os registros pesquisados da tabela
    public void mostrarPesquisa(Materiais mat, int idxAtual) {
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        mat.setCdSequencial(regAtual.getCdSequencial());
        mat.setCdMaterial(regAtual.getCdMaterial());
        mat.setNomeMaterial(regAtual.getNomeMaterial());
        mat.setCdGrupo(regAtual.getCdGrupo());
        mat.setNomeGrupo(buscarGrupo(regAtual.getCdGrupo(), "N"));
        mat.setCdSubGrupo(regAtual.getCdSubGrupo());
        mat.setNomeSubGrupo(buscarSubGrupo(regAtual.getCdSubGrupo(), regAtual.getCdGrupo(), "N"));
        mat.setCdCategoria(regAtual.getCdCategoria());
        mat.setNomeCategoria(buscarCategoria(regAtual.getCdCategoria(), "N"));
        mat.setCdMarca(regAtual.getCdMarca());
        if (!regAtual.getCdMarca().isEmpty()) {
            mat.setNomeMarca(buscarMarca(regAtual.getCdMarca()));
        } else {
            mat.setNomeMarca("");
        }
        mat.setCdClasse(regAtual.getCdClasse());
        if (!regAtual.getCdClasse().isEmpty()) {
            mat.setNomeClasse(buscarClasse(regAtual.getCdClasse()));
        } else {
            mat.setNomeClasse("");
        }
        mat.setCdEssencia(regAtual.getCdEssencia());
        if (!regAtual.getCdEssencia().isEmpty()) {
            mat.setNomeEssencia(buscarEssencia(regAtual.getCdEssencia(), "N"));
        } else {
            mat.setNomeEssencia("");
        }
        mat.setCdUnidMedida(regAtual.getCdUnidMedida());
        mat.setNomeUnidMedida(buscarUnidadesMedida(regAtual.getCdUnidMedida()));
        String tipoProduto = regAtual.getTipoProduto();
        switch (tipoProduto) {
            case "A":
                mat.setTipoProduto("1");
                break;
            case "I":
                mat.setTipoProduto("2");
                break;
            case "M":
                mat.setTipoProduto("3");
                break;
            case "R":
                mat.setTipoProduto("4");
                break;
            case "S":
                mat.setTipoProduto("5");
                break;
            default:
                mat.setTipoProduto("0");
                break;
        }
        mat.setCdArmazem(regAtual.getCdArmazem());
        mat.setNomeArmazPadrao(buscarLocalAramzem(regAtual.getCdArmazem()));
        mat.setPesoLiquido(regAtual.getPesoLiquido());
        mat.setPesoBruto(regAtual.getPesoBruto());
        mat.setUltPrecoCompra(regAtual.getUltPrecoCompra());
        mat.setCdCcusto(regAtual.getCdCcusto());
        mat.setNomeCCusto(buscarCentroCusto(regAtual.getCdCcusto()));
        mat.setCdCtaContabeReduz(regAtual.getCdCtaContabeReduz());
        mat.setNomeCtaContabil(regAtual.getCdCtaContabeReduz());
        mat.setUltCompra(regAtual.getUltCompra());
        mat.setEstoqueMinimo(regAtual.getEstoqueMinimo());
        mat.setLoteMinimo(regAtual.getLoteMinimo());
        mat.setLoteMultiplo(regAtual.getLoteMultiplo());
        mat.setLargura(regAtual.getLargura());
        mat.setComprimento(regAtual.getComprimento());
        mat.setEspessura(regAtual.getEspessura());
        mat.setCdOrigemCsta(regAtual.getCdOrigemCsta());
        mat.setCdNcm(regAtual.getCdNcm());
        mat.setDescricaoComercial(regAtual.getDescricaoComercial());
        mat.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        mat.setDataCadastro(regAtual.getDataCadastro());
        mat.setDataModificacao(regAtual.getDataModificacao());
        String situacao = regAtual.getSituacao();
        switch (situacao) {
            case "A":
                mat.setSituacao("1");
                break;
            case "I":
                mat.setSituacao("2");
                break;
        }
        if (buscarPreco) {
            if (!"S".equals(regAtual.getTipoProduto())) {
                gerarPrecoVenda();
                mat.setDescAlcada(descAlcada);
                if (!"R".equals(regAtual.getTipoProduto())) {
                    mat.setValorServico(valorUnitBruto);
                    mat.setDescAlcada(0.00);
                    mat.setValorUnitBruto(0.00);
                    mat.setValorUnitLiquido(0.00);
                } else {
                    mat.setValorServico(0.00);
                    mat.setValorUnitBruto(valorUnitBruto);
                    mat.setValorUnitLiquido(valorUnitBruto);
                }
            } else {
                gerarPrecoServico();
                mat.setValorServico(valorServico);
                mat.setDescAlcada(0.000);
                mat.setValorUnitBruto(0.00);
                mat.setValorUnitLiquido(0.00);
            }
        }
    }

    // Métodos para gerar código do material
    public void gerarCodigo(Materiais mat, String sequencia) {
        cdSequencia = sequencia;
        String ret = "C";
        if (!mat.getCdGrupo().isEmpty()) {
            if ("1".equals(buscarGrupo(mat.getCdGrupo(), ret))) {
                grupo = mat.getCdGrupo().trim();
                ret = "";
            }
        }
        if (!mat.getCdGrupo().isEmpty()) {
            if ("1".equals(buscarSubGrupo(mat.getCdSubGrupo(), mat.getCdGrupo(), ret))) {
                subGrupo = mat.getCdSubGrupo().trim();
                ret = "";
            }
        }
        if (!mat.getCdCategoria().isEmpty()) {
            if ("1".equals(buscarCategoria(mat.getCdCategoria(), ret))) {
                categoria = mat.getCdCategoria().trim();
                ret = "";
            }
        }
        if (!mat.getCdEssencia().isEmpty()) {
            if ("1".equals(buscarEssencia(mat.getCdEssencia(), ret))) {
                essencia = mat.getCdEssencia().trim();
                ret = "";
            }
        }
        cdMaterial = String.format("%s", grupo.toString().trim() + subGrupo.toString().trim() + categoria.toString().trim() + essencia.toString().trim() + cdSequencia.toString().trim());
        mat.setCdSequencial(cdSequencia);
        mat.setCdMaterial(cdMaterial);
        //JOptionPane.showMessageDialog(null, "Grupo: " + grupo + "\nSubGrupo: " + subGrupo + "\nCategoria: " +categoria + "\nEssencia: " + essencia +"\nMaterial Concatenado:"+
        //grupo.trim()+subGrupo.trim()+categoria.trim()+essencia.trim()+cdSequencia.trim());
    }

    // Método para gerar preco do item de Venda
    private void gerarPrecoVenda() {
        buscarPrecoItem(regAtual.getCdMaterial());
    }

    // Método para gerar preco do item de Venda
    private void gerarPrecoServico() {
        buscarPrecoServico(regAtual.getCdMaterial());
    }

    // Método para buscar nome do grupo e se forma código
    private String buscarGrupo(String cdGrupo, String ret) {
        //Busca nome Grupo
        GrupoProdutos gp = new GrupoProdutos();
        CGrupoProdutos cgp = new CGrupoProdutos(conexao);
        String sqlgp = "SELECT * FROM GCSGRUPOS WHERE CD_GRUPO = '" + cdGrupo + "'";
        try {
            cgp.pesquisar(sqlgp);
            cgp.mostrarPesquisa(gp, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Grupo de Produtos!\nErro: ex");
        }
        if ("N".equals(ret)) {
            return gp.getNomeGrupo();
        } else {
            ret = gp.getGerarCodigo();
            //JOptionPane.showMessageDialog(null, "Entrei no Else!\nGerarCódigo SubGrupo: "+ret);
            return ret;
        }
    }

    // Método par buscar nome do sub grupo e se forma código
    private String buscarSubGrupo(String cdSubGrupo, String cdGrupo, String ret) {
        //Busca nome Grupo
        SubGrupoProdutos sgp = new SubGrupoProdutos();
        CSubGrupoProdutos csgp = new CSubGrupoProdutos(conexao);
        String sqlsgp = "SELECT * FROM GCSSUBGRUPOS WHERE CD_GRUPO = '" + cdGrupo + "' AND CD_SUBGRUPO = '" + cdSubGrupo + "'";
        try {
            csgp.pesquisar(sqlsgp);
            csgp.mostrarPesquisa(sgp, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Grupo de Produtos!\nErro: ex");
        }
        if ("N".equals(ret)) {
            return sgp.getNomeSubGrupo();
        } else {
            ret = sgp.getGerarCodigo();
            //JOptionPane.showMessageDialog(null, "Entrei no Else!\nGerarCódigo SubGrupo: "+ret);
            return ret;
        }
    }

    // Método par buscar nome da Categoria e se forma código
    private String buscarCategoria(String cdCategoria, String ret) {
        Categorias cat = new Categorias();
        try {
            CCategorias pcat = new CCategorias(conexao);
            String sqlcat = "SELECT * FROM GCSCATEGORIA WHERE CD_CATEGORIA = '" + cdCategoria + "'";
            pcat.pesquisar(sqlcat);
            pcat.mostrarPesquisa(cat, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca da Categoria de Produtos!\nPrograma: GCSROT0091\nErro: " + ex);
        }
        if ("N".equals(ret)) {
            return cat.getNomeCategoria();
        } else {
            return cat.getGerarCodigo();
        }
    }

    // Método par buscar nome da essencia e se forma código
    private String buscarEssencia(String cdEssencia, String ret) {
        EssenciaProdutos ess = new EssenciaProdutos();
        try {
            CEssenciaProdutos pess = new CEssenciaProdutos(conexao);
            String sqless = "SELECT * FROM GCSESSENCIA WHERE CD_ESSENCIA = '" + cdEssencia + "'";
            pess.pesquisar(sqless);
            pess.mostrarPesquisa(ess, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca da Essencia de Produtos!\nPrograma: GCSROT0091\nErro: " + ex);
        }
        if ("N".equals(ret)) {
            return ess.getNomeEssencia();
        } else {
            return ess.getGerarCodigo();
        }
    }

    // Método par buscar nome da marca e se forma código
    private String buscarMarca(String cdMarca) {
        MarcaProdutos mar = new MarcaProdutos();
        try {
            CMarcaProdutos pmar = new CMarcaProdutos(conexao);
            String sqlmar = "SELECT * FROM GCSMARCA WHERE CD_MARCA = '" + cdMarca + "'";
            pmar.pesquisar(sqlmar);
            pmar.mostrarPesquisa(mar, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca da Essencia de Produtos!\nPrograma: GCSROT0091\nErro: " + ex);
        }
        return mar.getNomeMarca();
    }

    // Método par buscar nome da marca e se forma código
    private String buscarClasse(String cdClasse) {
        ClasseProdutos cla = new ClasseProdutos();
        try {
            CClasseProdutos pcla = new CClasseProdutos(conexao);
            String sqlcla = "SELECT * FROM GCSCLASSE WHERE CD_CLASSE = '" + cdClasse + "'";
            pcla.pesquisar(sqlcla);
            pcla.mostrarPesquisa(cla, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca da Essencia de Produtos!\nPrograma: GCSROT0091\nErro: " + ex);
        }
        return cla.getNomeClasse();
    }

    // Método par buscar nome da unidade de medida
    private String buscarUnidadesMedida(String cdUnidMedida) {
        UnidadesMedida um = new UnidadesMedida();
        try {
            CUnidadesMedida pum = new CUnidadesMedida(conexao);
            String sqlum = "SELECT * FROM GCSUNIDMEDIDA WHERE CD_UNIDMEDIDA = '" + cdUnidMedida + "'";
            pum.pesquisar(sqlum);
            pum.mostrarPesquisa(um, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca da Essencia de Produtos!\nPrograma: GCSROT0091\nErro: " + ex);
        }
        return um.getNomeUnidMedida();
    }

    // Método par buscar nome do Centro de Custo
    private String buscarCentroCusto(String cdCentroCustos) {
        CentroCustos cc = new CentroCustos();
        try {
            CCentroCustos pcc = new CCentroCustos(conexao);
            String sqlpcc = "SELECT * FROM GFRCCUSTO WHERE CD_CCUSTO = '" + cdCentroCustos + "'";
            pcc.pesquisar(sqlpcc);
            pcc.mostrarPesquisa(cc, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca da Essencia de Produtos!\nPrograma: GCSROT0091\nErro: " + ex);
        }
        return cc.getNomeCcusto();
    }

    // Método par buscar nome da Conta Contábil
    private String buscarContaContabil(String cdContaContabil) {
        ContasContabeis cc = new ContasContabeis();
        try {
            CContasContabeis pcc = new CContasContabeis();
            String sqlpcc = "SELECT * FROM  GFCPLANOCONTA WHERE REDUZIDO = '" + cdContaContabil + "'";
            pcc.pesquisar(sqlpcc);
            pcc.mostrarPesquisa(cc, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca da Essencia de Produtos!\nPrograma: GCSROT0091\nErro: " + ex);
        }
        return cc.getNomeConta();
    }

    // Método par buscar local do Armazém
    private String buscarLocalAramzem(String cdArmazem) {
        Armazens cd = new Armazens();
        try {
            CArmazens pcd = new CArmazens(conexao);
            String sqlcd = "SELECT * FROM  GCSARMAZEM WHERE CD_ARMAZEM = '" + cdArmazem + "'";
            pcd.pesquisar(sqlcd);
            pcd.mostrarPesquisa(cd, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca da Essencia de Produtos!\nPrograma: GCSROT0091\nErro: " + ex);
        }
        return cd.getNomeArmazem();
    }

    // Método para buscar o preco unitário o Material
    private void buscarPrecoItem(String cdMaterial) {
        String data = null;
        DataSistema dat = new DataSistema();
        dat.setData(data);
        data = dat.getData();
        ItemTabela itb = new ItemTabela();
        CItemTabela citb = new CItemTabela(conexao);
        String sqlitb = "select * from gcvitemtabela as itb"
                + " join gcvtabela as tab on itb.cd_tabela = tab.cd_tabela"
                + " and tab.situacao = 'A'"
                + " and tab.data_vigencia > '" + data
                + "' where itb.cd_material = '" + cdMaterial
                + "' and  itb.situacao = 'A'";
        try {
            int numReg = citb.pesquisar(sqlitb);

            if (numReg > 0) {
                citb.mostrarPesquisa(itb, 0, !buscarPreco);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CItemLocal.class.getName()).log(Level.SEVERE, null, ex);
        }
        valorUnitBruto = itb.getValorUnit();
        descAlcada = itb.getDescAlcada();
    }

    // Método para Buscar o preco unitario do Servico
    private void buscarPrecoServico(String cdMaterial) {
        Servicos srv = new Servicos();
        CServicos csrv = new CServicos(conexao);
        String sqlsrv = "select * from gsmservico as srv"
                + " where srv.cd_material = '" + cdMaterial
                + "' and srv.situacao = 'A'";
        try {
            int numreg = csrv.pesquisar(sqlsrv);
            if (numreg > 0) {
                csrv.mostrarPesquisa(srv, 0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CMateriais.class.getName()).log(Level.SEVERE, null, ex);
        }
        valorServico = srv.getValorServico();
    }
}
