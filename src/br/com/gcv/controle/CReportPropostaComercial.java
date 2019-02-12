/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.gcv.modelo.Proposta;
import br.com.modelo.Empresa;
import br.com.modelo.ParametrosGerais;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import br.com.modelo.SessaoUsuario;
import br.com.gcv.modelo.ReportUtils;
import br.com.modelo.EnviarEmail;
import java.io.IOException;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 */
public class CReportPropostaComercial {

    private static String cdPropostaIni;
    private static String cdPropostaFim;
    private static String cdRevisao;
    private static Empresa emp;
    private static ParametrosGerais pg;
    private static SessaoUsuario su;
    private static Connection conexao;
    private static String acao;
    private static Proposta pro;

    // Método para Abrir o Relatório
    public void abrirRelatorio(String cdPropostaIni, String cdPropostaFim, String cdRevisao, SessaoUsuario su,
            Connection conexao, Empresa emp, ParametrosGerais pg, String acao, Proposta pro) {
        this.cdPropostaIni = cdPropostaIni;
        this.cdPropostaFim = cdPropostaFim;
        this.cdRevisao = cdRevisao;
        this.su = su;
        this.emp = emp;
        this.pg = pg;
        this.conexao = conexao;
        this.acao = acao;
        this.pro = pro;
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
        InputStream inputStream = getClass().getResourceAsStream("ImprimirProposta.jasper");
        String relatorio = su.getLocalRelatorio() + "ImprimirProposta.jasper";

        // mapa de parâmetros do relatório (ainda vamos aprender a usar)
        Map parametros = new HashMap();

        /**
         * Insere o parâmetro primeiroNome no mapa, com o valor F% ou seja,
         * todos os clientes que tenham primeiro nome começando com a letra F.
         */
        parametros.put("CD_PROPOSTA_INI", this.cdPropostaIni);
        parametros.put("CD_PROPOSTA_FIM", this.cdPropostaFim);
        parametros.put("CD_REVISAO", this.cdRevisao);
        parametros.put("LOGOTIPO", su.getLocalImagens().trim() + "Logo.png");
        parametros.put("RODAPE", su.getLocalImagens().trim() + "Rodape.png");
        parametros.put("SITE_EMPRESA", this.emp.getSite());
        parametros.put("EMAIL", this.emp.getEmail());
        parametros.put("TELEFONES", this.emp.getTelefone());
        parametros.put("RAZAOSOCIAL", this.emp.getNomeRazaoSocial());
        parametros.put("CNPJ", this.emp.getCdCpfCnpj());
        parametros.put("LOCAL_RELATORIOS", this.pg.getLocalRelatorio());
        /*
        JOptionPane.showMessageDialog(null, "Proposta Ini: " + cdPropostaIni + "\nProposta fim: " + cdPropostaFim
                + "\nLogotipo: " + pg.getLocalImagens() + "\nLocalRelatorios: " + pg.getLocalRelatorio());
         */
        if ("RELATORIO".equals(acao.toUpperCase())) {
            try {
                // abre o relatório
                ReportUtils.openReport("Proposta Comercial", relatorio, parametros, conexao);
            } catch (JRException ex) {
                Logger.getLogger(CReportPropostaComercial.class.getName()).log(Level.SEVERE, null, ex);
                ex.printStackTrace();
            }
        }

        if ("EMAIL".equals(acao)) {
            EnviarEmail email = new EnviarEmail();
            email.setProgramaPadrao("Outlook.Application");
            email.setDestinatario(pro.getEmail());
            email.setAssunto("Proposta Comercial");
            email.setCorpoMensagem("");
            email.setLocalAnexo(pg.getLocalPropostaComercial());
            try {
                ReportUtils.sendMail("PropostaComercial", relatorio, parametros, conexao, email);
            } catch (JRException ex) {
                Logger.getLogger(CReportPropostaComercial.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(CReportPropostaComercial.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public static void main(String[] args) {
        new CReportPropostaComercial().abrirRelatorio(cdPropostaIni, cdPropostaFim, cdRevisao, su, conexao, emp, pg, acao, pro);
    }
}
