/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.gcv.modelo.Clientes;
import br.com.gcv.modelo.Contrato;
import br.com.gcv.modelo.Pedido;
import br.com.modelo.Empresa;
import br.com.modelo.ParametrosGerais;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import br.com.modelo.SessaoUsuario;
import br.com.gcv.modelo.ReportUtils;
import br.com.gcv.modelo.ReportUtilsContrato;
import br.com.gfc.modelo.CondicaoPagamento;
import br.com.gfc.modelo.TipoPagamento;
import br.com.modelo.CurrencyWriter;
import br.com.modelo.DataSistema;
import br.com.modelo.EnviarEmail;
import br.com.modelo.Municipios;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 */
public class CReportContrato {

    private static String cdContratoIni;
    private static String cdContratoFim;
    private static String cdRevisao;
    private static Empresa emp;
    private static ParametrosGerais pg;
    private static SessaoUsuario su;
    private static Connection conexao;
    private static String acao;
    private static Contrato con;
    private double valor;

    private Clientes cli;
    private Pedido ped;
    private Municipios mun;
    private TipoPagamento tp;
    private CondicaoPagamento cp;
    private static CurrencyWriter cw = new CurrencyWriter();
    private String cpfCnpjCli;
    private DataSistema dat;
    private NumberFormat ftv;
    private String vencimentos = "";
    private JFormattedTextField jForCliCdCpfCnpj;
    private JFormattedTextField jForCliCep;
    private JFormattedTextField jForCliRespCdCpfCnpj;
    private JFormattedTextField jForEmpCdCpfCnpj;
    private JFormattedTextField jForEmpCep;
    private int numReg = 0;

    // Método para Abrir o Relatório
    public void abrirRelatorio(String cdContratoIni, String cdContratoFim, SessaoUsuario su,
            Connection conexao, Empresa emp, ParametrosGerais pg, String acao, Contrato con) {
        this.cdContratoIni = cdContratoIni;
        this.cdContratoFim = cdContratoFim;
        this.su = su;
        this.emp = emp;
        this.pg = pg;
        this.conexao = conexao;
        this.acao = acao;
        this.con = con;
        this.cpfCnpjCli = con.getCdCpfCnpj();
        dat = new DataSistema();
        ftv.getInstance();
        ftv = new DecimalFormat("###,###,##0.0000");
        ftv.setMaximumFractionDigits(2);
        jForCliCdCpfCnpj = new javax.swing.JFormattedTextField();
        jForCliCep = new javax.swing.JFormattedTextField();
        jForCliCep = new javax.swing.JFormattedTextField();
        jForCliRespCdCpfCnpj = new javax.swing.JFormattedTextField();
        jForCliRespCdCpfCnpj.setText(con.getCpfResponsavel());

        jForEmpCdCpfCnpj = new javax.swing.JFormattedTextField();
        jForEmpCep = new javax.swing.JFormattedTextField();
        try {
            jForEmpCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
            jForEmpCdCpfCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
        } catch (ParseException ex) {
            Logger.getLogger(CReportContrato.class.getName()).log(Level.SEVERE, null, ex);
        }

        jForEmpCdCpfCnpj.setText(emp.getCdCpfCnpj());
        jForEmpCep.setText(emp.getCdCep());
        /*
         * Obtendo o arquivo do relatório.
         * Note que estamos utilizando um InputStream para obter o arquivo que
         * está dentro do nosso projeto. Fazendo isso, não teremos problema
         * quando nosso projeto for empacotado em um .jar.
         *
         * Note que o caminho do .jasper inicia com /, ou seja, a raiz da
         * localização das classes compiladas do nosso projeto
         * (o pacote default).
         *
         * Utilize a aba Files (canto superior esquerdo) e veja que os arquivos
         * .jasper e .jrxml são copiados para o diretório /build/classes
         * e por consequencia para o .jar que for criado.
         *
         * Se não os estiver vendo, mande dar um Clean and Build no projeto
         * (botão direito no nó raiz do projeto, Clean and Build (Limpar e Construir)
         *
         */
        InputStream inputStream = getClass().getResourceAsStream("ImprimirContrato1.jasper");
        String relatorio = su.getLocalRelatorio() + "ImprimirContrato1.jasper";

        // mapa de parâmetros do relatório (ainda vamos aprender a usar)
        cpfCnpjCli = cpfCnpjCli.replace("/", "");
        cpfCnpjCli = cpfCnpjCli.replace(".", "");
        cpfCnpjCli = cpfCnpjCli.replace("-", "");

        Map parametros = new HashMap();
        buscarCliente(cpfCnpjCli);

        /**
         * Insere o parâmetro primeiroNome no mapa, com o valor F% ou seja,
         * todos os clientes que tenham primeiro nome começando com a letra F.
         */
        parametros.put("CD_CONTRATO_INI", con.getCdContrato());
        parametros.put("CD_CONTRATO_FIM", con.getCdContrato());
        parametros.put("LOGOTIPO", su.getLocalImagens().trim() + "Logo.png");
        parametros.put("RODAPE", su.getLocalImagens().trim() + "Rodape.png");
        parametros.put("SITE_EMPRESA", this.emp.getSite());
        parametros.put("EMAIL", this.emp.getEmail());
        parametros.put("TELEFONES", this.emp.getTelefone());
        parametros.put("LOCAL_RELATORIOS", this.pg.getLocalRelatorio());
        parametros.put("PGSEMPNOME", this.emp.getNomeRazaoSocial());
        parametros.put("PGSEMPCNPJ", jForEmpCdCpfCnpj.getText());
        parametros.put("GCVCLINOME", cli.getNomeRazaoSocial());
        /*
        parametros.put("PGSEMPENDERECO", String.format("%s", this.emp.getLogradouro() + ", " + this.emp.getNumero()
                + " - " + jForEmpCep.getText() + " - " + this.emp.getComplemento() + " - " + this.emp.getBairro() + " - " + this.emp.getCdMunicipioIbge()
                + " - " + this.emp.getSiglaUf()));
        JOptionPane.showMessageDialog(null, "Nome Cliente: " + cli.getNomeRazaoSocial()
                + "\nVencimentos: " + vencimentos);
        parametros.put("GCVCLICPFCNPJ", String.format("%s", jForCliCdCpfCnpj.getText()));
        parametros.put("GCVCLIENDERECO", String.format("%s", cli.getLogradouro() + " ,Nº " + cli.getNumero() + " C.E.P.: " + jForCliCep.getText() + " Bairro " + cli.getBairro()
                + " Cidade " + mun.getNomeMunicipio() + " Estadao " + cli.getSiglaUf()));
        if ("2".equals(cli.getTipoPessoa())) {
            parametros.put("GCVCLIRESPONSAVEL", String.format("%s", ", (neste ato representado pelo seu representante legal " + con.getNomeResponsavel()
                    + ", portador do CPF.nº " + jForCliRespCdCpfCnpj.getText() + ")"));
        } else {
            parametros.put("GCVCLIRESPONSAVEL", "");
        }
        if (ped.getCdPedido() != null) {
            parametros.put("GCVPEDTOTALLIQ", ftv.format(ped.getValorTotalLiquido()));
            parametros.put("GCVPEDTOTALLIQEXT", cw.write(BigDecimal.valueOf(ped.getValorTotalLiquido())));
        } else {
            parametros.put("GCVPEDTOTALLIQ", "1.000,00");
            parametros.put("GCVPEDTOTALLIQEXT", cw.write(new BigDecimal("1000.00")));
        }
        parametros.put("GCVPEDTIPOPGTO", tp.getNomeTipoPagamento());
        parametros.put("GCVPEDCONDPGTO", String.valueOf(cp.getNumParcelas()));
        parametros.put("GCVTITVENCTO", vencimentos.toString().trim());
        parametros.put("GCVTITTXJUROS", String.valueOf(tp.getTaxaJuros()) + "%");
        parametros.put("GCVTITTXMULTA", String.valueOf(tp.getTaxaMulta()) + "%");
        if (ped != null) {
            parametros.put("GCVPEDPRAZOEXECUCAO", ped.getPrazoExecucao());
            if (ped.getDataInicio() != null) {
                parametros.put("GCVPEDDATAINICIO", dat.getDataConv(Date.valueOf(ped.getDataInicio())));
            }
        } else {
            parametros.put("GCVPEDPRAZOEXECUCAO", "de 5 a 7 dias");
            parametros.put("GCVPEDDATAINICIO", "Amanhã");
        }
        parametros.put("GCVTITTXJUROSEXT", cw.write(BigDecimal.valueOf(tp.getTaxaJuros())));
        parametros.put("GCVTITTXMULTAEXT", cw.write(BigDecimal.valueOf(tp.getTaxaMulta())));
        /*
        JOptionPane.showMessageDialog(null, "Proposta Ini: " + cdContratoIni + "\nProposta fim: " + cdContratoFim
                + "\nLogotipo: " + pg.getLocalImagens() + "\nLocalRelatorios: " + pg.getLocalRelatorio());
         */
        if ("RELATORIO".equals(acao.toUpperCase())) {
            try {
                // abre o relatório
                ReportUtils.openReport("Contrato de Prestação de Serviço", relatorio, parametros, conexao);
            } catch (JRException ex) {
                Logger.getLogger(CReportContrato.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }

        if ("EMAIL".equals(acao)) {
            EnviarEmail email = new EnviarEmail();
            email.setProgramaPadrao("Outlook.Application");
            email.setDestinatario(cli.getEmail());
            email.setAssunto("Contrato de Prestação de Serviço");
            email.setCorpoMensagem("");
            email.setLocalAnexo(pg.getLocalContrato());
            try {
                ReportUtilsContrato.sendMail("Contrato", relatorio, parametros, conexao, email);
            } catch (JRException ex) {
                Logger.getLogger(CReportContrato.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CReportContrato.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        new CReportContrato().abrirRelatorio(cdContratoIni, cdContratoFim, su, conexao, emp, pg, acao, con);
    }

    /**
     * Método para buscar Dados do Cliente
     */
    private int buscarCliente(String cdCpfCnpj) {
        int numReg = 0;
        Clientes cli = new Clientes();
        try {
            CClientes ccli = new CClientes(conexao);
            String sqlcli = "SELECT * FROM GCVCLIENTES WHERE cpf_cnpj = '" + cdCpfCnpj + "'";
            numReg = ccli.pesquisar(sqlcli);
            if (numReg > 0) {
                ccli.mostrarPesquisa(cli, 0);
                try {
                    jForCliCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
                    if ("1".equals(cli.getTipoPessoa())) {
                        jForCliCdCpfCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("###.###.###-##")));
                    } else if ("2".equals(cli.getTipoPessoa())) {
                        jForCliCdCpfCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##.###.###/####-##")));
                    }
                } catch (ParseException ex) {
                    Logger.getLogger(CReportContrato.class.getName()).log(Level.SEVERE, null, ex);
                }
                jForCliCdCpfCnpj.setText(cli.getCdCpfCnpj());
                jForCliCep.setText(cli.getCdCep());
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Cliente!\nPrograma: CReportContrato.java\nErro: " + ex);
        }
        this.cli = cli;
        return numReg;
    }
}
