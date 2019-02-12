/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.controle.CEmpresa;
import br.com.gcv.dao.ContratoDAO;
import br.com.gcv.modelo.Clientes;
import br.com.gcv.modelo.Contrato;
import br.com.gcv.modelo.OperacaoVenda;
import br.com.modelo.SessaoUsuario;

//imports necessários para geração de um novo contrato através do pedido
import br.com.gcv.modelo.ContratoSequencia;
import br.com.gcv.modelo.ContratoSeqTexto;
import br.com.gcv.dao.ContratoSequenciaDAO;
import br.com.gcv.dao.ContratoSeqTextoDAO;
import br.com.gcv.controle.CContratoSequencia;
import br.com.gcv.controle.CContratoSeqTexto;
import br.com.gcv.modelo.ContratoImpresso;
import br.com.gcv.modelo.Pedido;
import br.com.gcv.visao.ManterPropostaComercialRev1;
import br.com.gfc.controle.CCondicaoPagamento;
import br.com.gfc.controle.CLancamentos;
import br.com.gfc.controle.CTipoPagamento;
import br.com.gfc.modelo.CondicaoPagamento;
import br.com.gfc.modelo.Lancamentos;
import br.com.gfc.modelo.TipoPagamento;
import br.com.modelo.CurrencyWriter;
import br.com.modelo.DataSistema;
import br.com.modelo.Empresa;
import java.math.BigDecimal;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 22/11/2017
 */
public class CContrato {

    // variáveis de instancia de conexão do usuário
    private Connection conexao;
    private SessaoUsuario su;

    // variáveis de instância dos objetos da classe
    private List<Contrato> resultado = null;
    private Contrato regAtual;
    private ContratoDAO condao;
    private ResultSet rsCon;
    private int idxAtual;
    private int numReg;
    private String sql;

    // variáveis responsáveis pela criação das cláusulas do contrato através do pedido
    private List<ContratoSequencia> resultadoSeq = null;
    private List<ContratoSeqTexto> resultadoSeqTexto = null;
    private ContratoSequencia regSeqAtual;
    private ContratoSeqTexto regSeqTexAtual;
    private ContratoSequenciaDAO conseqDAO;
    private ContratoSeqTextoDAO conseqTexDAO;
    private CContratoSequencia ccs;
    private CContratoSeqTexto ccst;
    private ResultSet rsConSeq;
    private ResultSet rsConSeqTex;
    private ContratoImpresso ci;
    private Empresa emp;
    private Clientes cli;
    private NumberFormat ftv;
    private static CurrencyWriter cw = new CurrencyWriter();
    private DataSistema dat;
    private String vencimentos = "";
    private int idxConSeq;
    private int idxConSeqTex;
    private int numRegSeq;
    private int numRegSeqTex;
    private String sqlConSeq;
    private String sqlConseqTex;
    private JFormattedTextField jForCliCdCpfCnpj;
    private JFormattedTextField jForCliCep;
    private JFormattedTextField jForCliRespCdCpfCnpj;
    private JFormattedTextField jForEmpCdCpfCnpj;
    private JFormattedTextField jForEmpCep;

    // Construtor padrão
    public CContrato(Connection conexao, SessaoUsuario su) throws SQLException {
        this.conexao = conexao;
        this.su = su;
        condao = new ContratoDAO(conexao);
        ftv.getInstance();
        ftv = new DecimalFormat("###,###,##0.0000");
        ftv.setMaximumFractionDigits(2);
        jForCliCdCpfCnpj = new javax.swing.JFormattedTextField();
        jForCliCep = new javax.swing.JFormattedTextField();
        jForCliCep = new javax.swing.JFormattedTextField();
        jForCliRespCdCpfCnpj = new javax.swing.JFormattedTextField();
        jForEmpCdCpfCnpj = new javax.swing.JFormattedTextField();
        jForEmpCep = new javax.swing.JFormattedTextField();
        try {
            jForEmpCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
            jForCliCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
            jForEmpCdCpfCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
            jForCliRespCdCpfCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
        } catch (ParseException ex) {
            Logger.getLogger(CReportContrato.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // método para acionar a pesquisa dos registro
    public int pesquisar(String sql) throws SQLException {
        resultado = new ArrayList<Contrato>();
        rsCon = condao.pesquisarContrato(sql);
        carregarRegistro();
        if (resultado.size() > 0) {
            //mostrarPesquisa(idxAtual);
            numReg = resultado.size();
        } else {
            numReg = 0;
        }
        return numReg;
    }

    // método para selecionar os registros no banco
    public void carregarRegistro() {
        try {
            while (rsCon.next()) {
                try {
                    resultado.add(new Contrato(
                            rsCon.getString("cd_contrato"),
                            rsCon.getString("cpf_cnpj"),
                            rsCon.getString("cd_pedido"),
                            rsCon.getString("data_emissao"),
                            rsCon.getString("data_envio"),
                            rsCon.getString("data_assinatura"),
                            rsCon.getString("nome_responsavel"),
                            rsCon.getString("cpf_responsavel"),
                            rsCon.getString("modelo"),
                            rsCon.getString("usuario_cadastro"),
                            rsCon.getString("data_cadastro"),
                            rsCon.getString("hora_cadastro"),
                            rsCon.getString("usuario_modificacao"),
                            rsCon.getString("data_modificacao"),
                            rsCon.getString("hora_modificacao"),
                            rsCon.getString("situacao")));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro na busca do Contrato!\nErr: " + ex);
                }
            }
        } catch (SQLException sqlex) {
            JOptionPane.showMessageDialog(null, "Erro de conexão com o banco de dados!\nPrograma: ContratoDAO.java\nErr: " + sqlex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro na Geral do Sistema!\nErr: " + ex);
        } finally {
            try {
                rsCon.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    // método para prencher a tela com os registros pesquisados
    public Contrato mostrarPesquisa(int idxAtual) {
        Contrato con = new Contrato();
        this.idxAtual = idxAtual;
        regAtual = resultado.get(idxAtual);
        con.setCdContrato(regAtual.getCdContrato());
        con.setCdCpfCnpj(regAtual.getCdCpfCnpj());
        con.setNomeRazaoSocial(buscarCliente(regAtual.getCdCpfCnpj()));
        con.setCdPedido(regAtual.getCdPedido());
        con.setDataEmissao(regAtual.getDataEmissao());
        con.setDataEnvio(regAtual.getDataEnvio());
        con.setDataAssinatura(regAtual.getDataAssinatura());
        con.setNomeResponsavel(regAtual.getNomeResponsavel());
        con.setCpfResponsavel(regAtual.getCpfResponsavel());
        switch (regAtual.getModelo()) {
            case "S":
                con.setModelo("1");
                break;
            case "N":
                con.setModelo("2");
                break;
            default:
                con.setModelo("0");
                break;
        }
        con.setUsuarioCadastro(regAtual.getUsuarioCadastro());
        con.setDataCadastro(regAtual.getDataCadastro());
        con.setHoraCadastro(regAtual.getHoraCadastro());
        con.setUsuarioModificacao(regAtual.getUsuarioModificacao());
        con.setDataModificacao(regAtual.getDataModificacao());
        con.setHoraModificacao(regAtual.getHoraModificacao());
        switch (regAtual.getSituacao()) {
            case "PE":
                con.setSituacao("1");
                break;
            case "AE":
                con.setSituacao("2");
                break;
            case "AA":
                con.setSituacao("3");
                break;
            case "AS":
                con.setSituacao("4");
                break;
            default:
                con.setSituacao("0");
                break;
        }
        return con;
    }

    /**
     * Método para gerar o contrato do cliente com base no pedido
     */
    public void gerarClausulasCliente(String cdOperVenda) {
        ci = new ContratoImpresso();
        buscarEmpresa();
        String cdContrato = buscarOperacaoVenda(cdOperVenda);
        if (cdContrato == null) {
            JOptionPane.showMessageDialog(null, "A Operação de Venda do Pedido não possui um contrato padrão vinculado!");
        } else {
            resultadoSeq = new ArrayList<ContratoSequencia>();
            sqlConSeq = "select * from gcvcontratosequencia as s"
                    + " where s.cd_contrato = '" + cdContrato
                    + "' order by s.cd_seqantecessora";
            sqlConseqTex = "select * from gcvcontratoseqtexto where cd_contrato = '" + cdContrato
                    + "'";
            try {
                conseqDAO = new ContratoSequenciaDAO(conexao);
                rsConSeq = conseqDAO.pesquisarSeqquenciaContrato(sqlConSeq);
                while (rsConSeq.next()) {
                    try {
                        resultadoSeq.add(new ContratoSequencia(
                                rsConSeq.getString("cd_contrato"),
                                rsConSeq.getString("cd_sequencia"),
                                rsConSeq.getString("cd_sequenciapai"),
                                rsConSeq.getString("tipo_sequencia"),
                                rsConSeq.getString("cd_seqantecessora"),
                                rsConSeq.getString("titulo"),
                                rsConSeq.getString("alinhamento_titulo"),
                                rsConSeq.getString("possui_texto"),
                                rsConSeq.getString("quebra_linha"),
                                rsConSeq.getString("usuario_cadastro"),
                                rsConSeq.getString("data_cadastro"),
                                rsConSeq.getString("hora_cadastro"),
                                rsConSeq.getString("usuario_modificacao"),
                                rsConSeq.getString("data_modificacao"),
                                rsConSeq.getString("hora_modificacao"),
                                rsConSeq.getString("situacao")));
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Erro na busca do ContratoXSequencia!\nErr: " + ex);
                    }
                }
            } catch (SQLException ex) {
                Logger.getLogger(CContrato.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (resultadoSeq.size() > 0) {
                numRegSeq = resultadoSeq.size();
                resultadoSeqTexto = new ArrayList<ContratoSeqTexto>();
                try {
                    conseqTexDAO = new ContratoSeqTextoDAO(conexao);
                    conseqTexDAO.selecionar(resultadoSeqTexto, sqlConseqTex);
                    numRegSeqTex = resultadoSeqTexto.size();
                } catch (SQLException ex) {
                    Logger.getLogger(CContrato.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            buscarCliente(regAtual.getCdCpfCnpj());
            prepararDadosCliente();
            for (int i = 0; i < numRegSeq; i++) {
                regSeqAtual = resultadoSeq.get(i);
                regSeqAtual.setCdContrato(regAtual.getCdContrato());
                conseqDAO.adicionar(regSeqAtual);
            }
            for (int j = 0; j < numRegSeqTex; j++) {
                regSeqTexAtual = resultadoSeqTexto.get(j);
                regSeqTexAtual.setCdContrato(regAtual.getCdContrato());
                regSeqTexAtual.setTextoLongo(mesclarClausula());
                conseqTexDAO.adicionar(regSeqTexAtual);
            }
        }
    }

    private void prepararDadosCliente() {
        dat = new DataSistema();
        ci = new ContratoImpresso();
        if ("J".equals(cli.getTipoPessoa())) {
            String nomeResp = "";
            String cpfResp = "";
            try {
                jForCliCdCpfCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
                if (regAtual.getNomeResponsavel() != null) {
                    nomeResp = regAtual.getNomeResponsavel();
                } else {
                    nomeResp = cli.getNomeRazaoSocial();
                }
                if (regAtual.getCpfResponsavel() != null) {
                    jForCliRespCdCpfCnpj.setText(regAtual.getCpfResponsavel());
                } else {
                    jForCliCdCpfCnpj.setText(cli.getCdCpfCnpj());
                }
                cpfResp = jForCliRespCdCpfCnpj.getText();
                ci.setGcvclicpfresponsavel(String.format("%s", ", (neste ato representado pelo seu representante legal " + nomeResp
                        + ", portador do CPF.nº " + cpfResp + ")"));
            } catch (ParseException ex) {
                Logger.getLogger(CReportContrato.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                jForCliCdCpfCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
                ci.setGcvcliresponsavel("");
                ci.setGcvclicpfresponsavel("");
            } catch (ParseException ex) {
                Logger.getLogger(CReportContrato.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        jForCliCdCpfCnpj.setText(cli.getCdCpfCnpj());
        jForCliCep.setText(cli.getCdCep());
        ci.setPgsempnome(emp.getNomeRazaoSocial());
        ci.setPgsempcnpj(jForEmpCdCpfCnpj.getText());
        ci.setPgsempendereco(String.format("%s", this.emp.getLogradouro() + ", " + this.emp.getNumero()
                + " - " + jForEmpCep.getText() + " - " + this.emp.getComplemento() + " - " + this.emp.getBairro() + " - " + this.emp.getNomeMunicipio()
                + " - " + this.emp.getSiglaUf()));
        ci.setGcvclicpfcnpj(jForCliCdCpfCnpj.getText());
        ci.setGcvclinome(cli.getNomeRazaoSocial());
        ci.setGcvcliendereco(String.format("%s", cli.getLogradouro() + ",Nº " + cli.getNumero() + ", " + cli.getComplemento() + " C.E.P.: " + jForCliCep.getText() + " Bairro " + cli.getBairro()
                + " Cidade " + cli.getNomeMunicipio() + " Estadao " + cli.getSiglaUf()));

        ci.setGcvclicpfresponsavel(jForCliRespCdCpfCnpj.getText());
        buscarPedido(regAtual.getCdPedido());
        ci.setGcvtitvencto(buscarVencimentos("Re", "Tit", cli.getCdCpfCnpj(), regAtual.getCdPedido(), regAtual.getCdPedido()));
    }

    private String mesclarClausula() {
        String texto = regSeqTexAtual.getTextoLongo();
        texto = texto.replace("<MG_PGSEMPNOME>", ci.getPgsempnome());
        texto = texto.replace("<MG_PGSEMPCNPJ>", ci.getPgsempcnpj());
        texto = texto.replace("<MG_PGSEMPENDERECO>", ci.getPgsempendereco());
        texto = texto.replace("<MG_GCVCLINOME>", ci.getGcvclinome());
        texto = texto.replace("<MG_GCVCLICPFCNPJ>", ci.getGcvclicpfcnpj());
        texto = texto.replace("<MG_GCVCLIENDERECO>", ci.getGcvcliendereco());
        texto = texto.replace("<MG_GCVCLIRESPONSAVEL>", ci.getGcvcliresponsavel());
        texto = texto.replace("<MG_GCVCLICPFRESPONSAVEL>", ci.getGcvclicpfresponsavel());
        texto = texto.replaceAll("<MG_GCVPEDTOTALLIQ>", ci.getGcvpedtotalliq());
        texto = texto.replaceAll("<MG_GCVPEDTOTALLIQEXT>", ci.getGcvpedtotalliqext());
        texto = texto.replaceAll("<MG_GCVPEDTIPOPGTO>", ci.getGcvpedtipopgto());
        texto = texto.replaceAll("<MG_GCVPEDCONDPGTO>", ci.getGcvpedcondpgto());
        texto = texto.replaceAll("<MG_GCVTITVENCTO>", ci.getGcvtitvencto());
        texto = texto.replaceAll("<MG_GCVTITTXJUROS>", ci.getGcvtittxjuros());
        texto = texto.replaceAll("<MG_GCVTITTXJUROSEXT>", ci.getGcvtittxjurosext());
        texto = texto.replaceAll("<MG_GCVTITTXMULTA>", ci.getGcvtittxmulta());
        texto = texto.replaceAll("<MG_GCVTITTXMULTAEXT>", ci.getGcvtittxmultaext());
        texto = texto.replaceAll("<MG_GCVPEDPRAZOEXECUCAO>", ci.getGcvpedprazoexecucao());
        texto = texto.replaceAll("<MG_GCVPEDDATAINICIO>", ci.getGcvpeddatainicio());
        return texto;
    }

    /**
     * Método para buscar nome do cliente
     *
     * @param cdCpfCnpj retorna o nome do cliente
     * @returm nomeRazaoSocial String com o nome do cliente
     */
    private String buscarCliente(String cdCpfCnpj) {
        cli = new Clientes();
        CClientes ccli = new CClientes(conexao);
        String sqlcli = "select * from gcvclientes where cpf_cnpj = '" + cdCpfCnpj
                + "'";
        try {
            ccli.pesquisar(sqlcli);
            ccli.mostrarPesquisa(cli, 0);
        } catch (SQLException ex) {
            Logger.getLogger(CContrato.class.getName()).log(Level.SEVERE, null, ex);
        }
        return cli.getNomeRazaoSocial();
    }

    /**
     * Busca o Contrato padrão vinculado a operação de venda do pedido
     *
     * @return cdContrato código do contrato padrão
     */
    private String buscarOperacaoVenda(String cdOperVenda) {
        int numReg;
        OperacaoVenda ov = new OperacaoVenda();
        COperacaoVenda cov = new COperacaoVenda(conexao);
        String sqlcov = "select * from gcvopervenda where cd_operacao = '" + cdOperVenda
                + "'";
        try {
            numReg = cov.pesquisar(sqlcov);
            if (numReg > 0) {
                cov.mostrarPesquisa(ov, 0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CContrato.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ov.getCdContrato();
    }

    private void buscarEmpresa() {
        emp = new Empresa();
        CEmpresa cemp = new CEmpresa(conexao);
        String sqlemp = "Select * from pgsempresa as e inner join pgsestabelecimento as et on e.cpf_cnpj = et.cpf_cnpj";
        try {
            int numReg = cemp.pesquisar(sqlemp);
            if (numReg > 0) {
                cemp.mostrarPesquisa(emp, 0);
                jForEmpCdCpfCnpj.setText(emp.getCdCpfCnpj());
                jForEmpCep.setText(emp.getCdCep());
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManterPropostaComercialRev1.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void buscarPedido(String cdPedido) {
        Pedido ped = new Pedido();
        try {
            CPedido cped = new CPedido(conexao, su);
            String sqlped = "select * from gcvpedido where cd_pedido = '" + cdPedido
                    + "'";
            cped.pesquisarPedido(sqlped);
            ped = cped.mostrarPedido(0);
            ci.setGcvpedtotalliq(ftv.format(ped.getValorTotalLiquido()));
            ci.setGcvpedtotalliqext(cw.write(BigDecimal.valueOf(ped.getValorTotalLiquido())));
            ci.setGcvpedtipopgto(buscarTipoPagamento(ped.getCdTipoPagamento()));
            buscarCondicaoPagamento(ped.getCdCondPagamento());
            ci.setGcvpedprazoexecucao(ped.getPrazoExecucao());
            ci.setGcvpeddatainicio(dat.getDataConv(Date.valueOf(ped.getDataInicio())));
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Pedido!\nPrograma CReportContrato.java\nErro: " + ex);
        }
    }

    /**
     * Método para Buscar tipo de pagamento
     */
    private String buscarTipoPagamento(String cdTipoPagamento) {
        TipoPagamento tp = new TipoPagamento();
        try {
            CTipoPagamento ctp = new CTipoPagamento(conexao);
            String sqlctp = "SELECT * FROM GFCTIPOPAGAMENTO WHERE CD_TIPOPAGAMENTO = '" + cdTipoPagamento + "'";
            int numReg = ctp.pesquisar(sqlctp);
            if (numReg > 0) {
                ctp.mostrarPesquisa(tp, 0);
                ci.setGcvtittxjuros("1%");
                ci.setGcvtittxjurosext("um por cento");
                ci.setGcvtittxmulta("2,00%");
                ci.setGcvtittxmultaext("dois por cento");
                
                /* precisa corrigir o valor por extenso para quando não for valor monetário.
                **
                if (tp.getTaxaJuros() > 0) {
                    ci.setGcvtittxjuros(String.format("%s", String.valueOf(tp.getTaxaJuros()) + "%"));
                    ci.setGcvtittxjurosext(String.format("%s", String.valueOf(tp.getTaxaJuros())+" por cento"));
                } else {
                    ci.setGcvtittxjuros("1%");
                    ci.setGcvtittxjurosext("um por cento");
                }
                if (tp.getTaxaMulta() > 0) {
                    ci.setGcvtittxmulta(String.format("%s", String.valueOf(tp.getTaxaMulta()) + "%"));
                    ci.setGcvtittxmultaext(String.format("%s", String.valueOf(tp.getTaxaMulta())+ " por cento"));
                }else{
                    ci.setGcvtittxmulta("2,00%");
                    ci.setGcvtittxmultaext("dois por cento");
                }
                 */
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome o Município!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return tp.getNomeTipoPagamento();
    }

    /**
     * Método para Buscar Condição de Pagamento
     */
    private String buscarCondicaoPagamento(String cdCondPag) {
        CondicaoPagamento cp = new CondicaoPagamento();
        try {
            CCondicaoPagamento ccp = new CCondicaoPagamento(conexao);
            String sqlctp = "SELECT * FROM GFCCONDICAOPAGAMENTO WHERE CD_CONDPAG = '" + cdCondPag + "'";
            int numReg = ccp.pesquisar(sqlctp);
            if (numReg > 0) {
                ccp.mostrarPesquisa(cp, 0);
                ci.setGcvpedcondpgto(String.valueOf(cp.getNumParcelas()));
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome o Município!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return cp.getNomeCondPag();
    }

    /**
     *
     * @param tpMov
     * @param tpLanc
     * @param cpfCnpj
     * @param titulo
     * @return String Títulos concatenados
     */
    private String buscarVencimentos(String tpMov, String tpLanc, String cpfCnpj, String titulo, String cdPedido) {
        String vctoConcat = "";
        Lancamentos l = new Lancamentos();
        try {
            CLancamentos cl = new CLancamentos(conexao,su);
            String sqllan = "select * from gfclancamentos where tipo_movimento = '" + tpMov
                    + "' and tipo_lancamento = '" + tpLanc
                    + "' and cpf_cnpj = '" + cpfCnpj
                    + "' and titulo = '" + cdPedido
                    + "'";
            int numReg = cl.pesquisar(sqllan);
            if (numReg > 0) {
                for (int i = 0; i < numReg; i++) {
                    if (i > 0) {
                        vctoConcat = String.format("%s", vctoConcat + " / ");
                    }
                    cl.mostrarPesquisa(l, i);
                    vctoConcat = String.format("%s", vctoConcat + dat.getDataConv(Date.valueOf(l.getDataVencimento())) + " ==> R\\$" + ftv.format(l.getValorLancamento()));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return vctoConcat;
    }
}
