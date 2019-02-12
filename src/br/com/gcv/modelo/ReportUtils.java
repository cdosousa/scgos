/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

import br.com.controle.CEnviarEmail;
import br.com.modelo.EnviarEmail;
import java.awt.BorderLayout;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Map;
import javax.swing.JFrame;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.swing.JRViewer;

/**
 * Classe com métodos e utilitários para abrir um relatório
 *
 * @author cristiano
 */
public class ReportUtils {

    private static EnviarEmail modemail;

    /**
     * Abre um relatório usando uma conexão como datasource.
     *
     * @param titulo Título usado na janela do relatório.
     * @param inputStream InputStream que contém o relatório.
     * @param parametros Parâmetros utilizados pelo relatório.
     * @param conexao Conexão utilizada para a execução da query.
     * @
     */
    public static void openReport(String titulo, String relatorio, Map parametros, Connection conexao) throws JRException {
        /*
         * Cria um JasperPrint, que é a versão preenchida do relatório,
         * usando uma conexão.
         */

        JasperPrint print = JasperFillManager.fillReport(relatorio, parametros, conexao);

        //JOptionPane.showMessageDialog(null, "Gerando Relatório Proposta: " + parametros.get("CD_PROPOSTA_INI"));
        //JasperExportManager.exportReportToPdfFile(print, "C://Proposta Comercial "+parametros.get("CD_PROPOSTA_INI")+".pdf");
        // abre o JasperPrint em um JFrame
        viewReportFrame(titulo, print);
    }

    /**
     * Abre um relatório usando um datasource genérico.
     *
     * @param titulo Título usado na janela do relatório.
     * @param inputStream InputStream que contém o relatório.
     * @param parametros Parâmetros utilizados pelo relatório.
     * @param dataSource Datasource a ser utilizado pelo relatório.
     * @throws JRException Caso ocorra algum problema na execução do relatório
     */
    public static void openReport(String titulo, InputStream inputStream, Map parametros, JRDataSource dataSource) throws JRException {
        /*
         * Cria um JasperPrint, que é a versão preenchida do relatório,
         * usando um datasource genérico.
         */
        JasperPrint print = JasperFillManager.fillReport(inputStream, parametros, dataSource);
        // abre o JasperPrint em um JFrame
        viewReportFrame(titulo, print);
    }

    /**
     * Cria um JFrame para exibir o relatório representado pelo JasperPrint.
     *
     * @param titulo Título do JFrame.
     * @param print JasperPrint do relatório.
     */
    private static void viewReportFrame(String titulo, JasperPrint print) {

        /*
         * Cria um JRViewer para exibir o relatório.
         * Um JRViewer é uma JPanel.
         */
        JRViewer viewer = new JRViewer(print);

        // cria o JFrame
        JFrame frameRelatorio = new JFrame(titulo);

        // adiciona o JRViewer no JFrame
        frameRelatorio.add(viewer, BorderLayout.CENTER);

        // configura o tamanho padrão do JFrame
        frameRelatorio.setSize(595, 842);

        // maximiza o JFrame para ocupar a tela toda.
        frameRelatorio.setExtendedState(JFrame.MAXIMIZED_BOTH);

        // configura a operação padrão quando o JFrame for fechado.
        frameRelatorio.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // exibe o JFrame
        frameRelatorio.setVisible(true);
    }

    public static void sendMail(String titulo, String relatorio, Map parametros, Connection conexao, EnviarEmail email) throws JRException, IOException {
        modemail = email;

        /*
         * Cria um JasperPrint, que é a versão preenchida do relatório,
         * usando uma conexão.
         */
        JasperPrint print = JasperFillManager.fillReport(relatorio, parametros, conexao);
        /*
        JOptionPane.showMessageDialog(null, "Gerando Relatório Proposta: " + parametros.get("CD_PROPOSTA_INI"));
        */
        String nomeArquivo = "";
        String assunto = "";
        if(!"0".equals(parametros.get("CD_REVISAO"))){
            nomeArquivo = String.format("%s%s%s%s%s", "Proposta Comercial ",parametros.get("CD_PROPOSTA_INI").toString().trim(),
                    "-",parametros.get("CD_REVISAO").toString().trim(),".pdf");
            assunto = String.format("%s%s%s%s", "Proposta Comercial ",parametros.get("CD_PROPOSTA_INI").toString().trim(),
                    "-",parametros.get("CD_REVISAO").toString().trim());
        }else{
            nomeArquivo = String.format("%s%s%s", "Proposta Comercial ",parametros.get("CD_PROPOSTA_INI").toString().trim(),".pdf");
            assunto = String.format("%s%s", "Proposta Comercial ",parametros.get("CD_PROPOSTA_INI").toString().trim());
        }
        JasperExportManager.exportReportToPdfFile(print, email.getLocalAnexo() + nomeArquivo.trim());
        
        modemail.setAssunto(assunto);
        modemail.setAnexo(email.getLocalAnexo() + nomeArquivo);
        enviarEmail();
        //String comando = "rundll32 url.dll,FileProtocolHandler "+"mailto:cristiano.sousa@oici.com.br?subject=TesteEmail&body=TEstando&attachments="+"C:"+File.separator+"PropostaComercial.pdf";
        //String comando = "rundll32 url.dll,FileProtocolHandler "+"mailto:cristiano.sousa@oici.com.br?subject=TesteEmail&body=TEstando&attachments=C:\\PropostaComercial.pdf";
        //Runtime.getRuntime().exec(comando);
    }

    private static void enviarEmail() {
        CEnviarEmail cem = new CEnviarEmail(modemail);

        /*
        ActiveXComponent axcOutlook = new ActiveXComponent("Outlook.Application");
        Dispatch criacaoEmail = Dispatch.invoke(axcOutlook.getObject(), "CreateItem", Dispatch.Get,
                new Object[]{"0"},new int[0]).toDispatch();
        String destinatario = "cristiano.sousa@gmail.com";
        String assunto = "Proposta Comercial";
        String corpoMensagem = "Segue Proposta Comercial";
        Object anexo1 = new Object();
        anexo1 = "C:"+File.separator+"PropostaComercial.pdf";
        Dispatch.put(criacaoEmail, "to", destinatario);
        Dispatch.put(criacaoEmail, "subject", assunto);
        Dispatch.put(criacaoEmail, "body", corpoMensagem);
        Dispatch.put(criacaoEmail, "ReadReceiptRequested", "false");
        Dispatch attachs = Dispatch.get(criacaoEmail, "Attachments").toDispatch();
        Dispatch.call(attachs, "add",anexo1);
        Dispatch.call(criacaoEmail, "Display");
         */
    }
}
