/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.DAO.ConsultaModelo;
import br.com.controle.CPosicionarArquivo;
import br.com.gfc.modelo.ArquivoCNAB;
import br.com.gfc.modelo.EdiOcorrencia;
import br.com.modelo.DataSistema;
import br.com.modelo.ParametrosGerais;
import br.com.modelo.SessaoUsuario;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta_0917 criado em 30/11/2018
 */
public class CArquivoCNAB {

    /**
     * Objetos dependentes da classe
     */
    private ArquivoCNAB cnab;
    private ParametrosGerais pg;
    private Connection conexao;
    private SessaoUsuario su;
    private CPosicionarArquivo cpc;
    private static FileReader entrada;
    private Scanner ler;
    private NumberFormat ftv;
    private NumberFormat ftq;
    private List<ArquivoCNAB> listaArquivo;
    private ArquivoCNAB regAtual;

    /**
     * Variáveis de instância para geração do arquivo
     */
    private String cdPreparacao;
    private String io;
    private static FileWriter saida;
    private String[] arquivo;
    private int idxArq = 1;

    /**
     * Variáveis de instância para leitura do arquivo
     */
    private String nomeArquivo;
    private String registro;
    private String[] detalhe;
    private String[] head;
    private String[] trailer;
    private JTable tabela;
    private ConsultaModelo cm;

    /**
     * Construto padrão
     */
    public CArquivoCNAB() {

    }

    /**
     * Contrutor para ser utilizado para gerar arquivo no disto
     *
     * @param cnab Objeto contendo as informações padrão do arquivo
     * @param pg Objeto contedo os parametros gerais do sistema
     * @param conexao Objetoc contendo a instância de conexão do usuário com o
     * banco de dados
     * @param linhas Variáveis inteira contendo a quantidade de registro que
     * serão gravados
     * @param cdPreparacao variáveis contendo o códido da preparação de
     * pagamento
     * @throws FileNotFoundException Lançamento de erro de exeção
     * @throws IOException Lançamento de erro de exeção
     */
    public CArquivoCNAB(ArquivoCNAB cnab, ParametrosGerais pg, Connection conexao, SessaoUsuario su, int linhas, String cdPreparacao) throws FileNotFoundException, IOException {
        this.cnab = cnab;
        this.pg = pg;
        this.conexao = conexao;
        this.su = su;
        this.cdPreparacao = cdPreparacao;
        arquivo = new String[linhas];
        setaVariaveisEscrita();
    }

    /**
     * Construtor para ser utilzado para ler o arquivo de retorno do banco
     *
     * @param cnab
     * @param pg
     * @param conexao
     * @throws FileNotFoundException
     * @throws IOException
     */
    public CArquivoCNAB(ArquivoCNAB cnab, ParametrosGerais pg, Connection conexao, String nomeArquivo) throws FileNotFoundException, IOException {
        this.cnab = cnab;
        this.pg = pg;
        this.conexao = conexao;
        this.nomeArquivo = nomeArquivo;
        setaVariaveisLeitura();
    }

    /**
     * Método para setar as variáveis de ambiente para escrever no arquivo
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void setaVariaveisEscrita() throws FileNotFoundException, IOException {
        saida = new FileWriter(pg.getLocalBoletoBanco() + "remessa.txt");
    }

    /**
     * Método para setar as variáveis de leitura do arquivo
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private void setaVariaveisLeitura() throws FileNotFoundException, IOException {
        entrada = new FileReader(pg.getLocalBoletoBanco() + nomeArquivo);
        ler = new Scanner(entrada);
        listaArquivo = new ArrayList<ArquivoCNAB>();
        ftv.getInstance();
        ftq.getInstance();
        ftv = new DecimalFormat("###,###,##0.0000");
        ftq = new DecimalFormat("###,###,##0.0000");
        ftq.setMaximumFractionDigits(0);
        ftv.setMaximumFractionDigits(2);
    }

    /**
     * Método para preparar o arquivo que será gerado
     *
     * @return retorna se o arquivo foi gerado.
     */
    public int prepararArquivo() {
        DataSistema dat = new DataSistema();
        cpc = new CPosicionarArquivo();
        dat.setData("");
        String data = dat.getDataConv(Date.valueOf(dat.getData()));
        data = data.replaceAll("/", "");
        cnab.setCdTipoRegistro("0");
        cnab.setCdTipoOperacao("1");
        cnab.setDataGeracao(String.format("%s%s", data.substring(0, 4), data.substring(6)));
        cnab.setNumeroSequencial(1);
        if (criarHeaddItau(cnab) == 1) {
            cnab.setCdTipoRegistro("1");
            if (criarDetalheItau() == 1) {
                cnab.setCdTipoRegistro("9");
                if (criarTrailerItau() == 1) {
                    return 1;
                } else {
                    mensagem("Erro na geradacao do arquivo!");
                }
            } else {
                mensagem("Erro na geradacao do detalhe do arquivo!");
            }
        } else {
            mensagem("Erro na geradacao do cabecalho arquivo!");
        }
        return 0;
    }

    public int lerArquivo(JTable tabela) {
        this.tabela = tabela;
        int interacao = 1;
        try {
            cm = new ConsultaModelo(conexao);
            do {
                registro = ler.nextLine();
                cnab.setCdTipoRegistro(registro.substring(0, 1));
                if ("0".equals(cnab.getCdTipoRegistro())) {
                    if (lerHeadItau() == 0) {
                        return 0;
                    }
                } else if ("1".equals(cnab.getCdTipoRegistro())) {
                    if (lerDetalheItau() == 1) {
                        carregarTabelaDetalhe(interacao);
                        carregarListaDetalhe();
                    } else {
                        return 0;
                    }
                } else if ("9".equals(cnab.getCdTipoRegistro())) {
                    if (lerTrailerItau() == 0) {
                        return 0;
                    }
                }
                interacao++;
            } while (!"9".equals(cnab.getCdTipoRegistro()));
            return 1;
        } catch (Exception e) {
            mensagem("Erro na leitura do arquivo!\nErro: " + e);
            return 0;
        }
    }

    /**
     * Método para criar o cabecalho do arquivo
     *
     * @param cnab Objeto contendo as informções do arquivo EDI
     * @return retorna interiro (zero ou um) para dizer e o arquivo foi gerado
     * ou não
     */
    public int criarHeaddItau(ArquivoCNAB cnab) {
        String[] tipo = {"9", "9", "X", "9", "X", "9", "9", "9", "9", "X", "X", "9", "X", "9", "X", "9"};
        int[] tamanho = {1, 1, 7, 2, 15, 4, 2, 5, 1, 8, 30, 3, 15, 6, 294, 6};
        cnab.setCdAgenciaDig("00");
        String[] head = formatarString(cnab, "0");
        String cabecalho = "";
        arquivo[idxArq - 1] = cpc.posicionarCampos(head, tipo, tamanho);
        //mensagem("Arquivo: " + arquivo[idxArq]);
        idxArq++;
        return 1;
    }

    /**
     * Método para ler o cabecalho do arquivo
     *
     * @return returna 1 caso o arquivo tenha sido lido e 0 caso ocorra erro.
     */
    private int lerHeadItau() {
        int[] tamanho = {1, 1, 7, 2, 15, 4, 2, 5, 1, 8, 30, 3, 15, 6, 5, 3, 5, 6, 275, 6};
        head = lerRegistroDoArquivo(tamanho);
        if (head.length == tamanho.length) {
            cnab.setHead(head);
            return 1;
        } else {
            mensagem("Erro na leitura do cabecalho arquivo!");
            return 0;
        }
    }

    /**
     * Método para criar o detalhe do arquivo
     *
     * @return
     */
    public int criarDetalheItau() {
        String[] tipo = {"9", "9", "9", "9", "9", "9", "9", "X", "9", "X", "9", "9V", "9", "X", "X", "9", "X", "9", "9V", "9", "9", "X", "X", "9", "X", "X", "9V", "9", "9V", "9V", "9V", "9", "9", "X", "X", "X", "X",
            "9", "X", "X", "X", "X", "9", "9", "X", "9"};
        int[] tamanho = {1, 2, 14, 4, 2, 5, 1, 4, 4, 25, 8, 13, 3, 21, 1, 2, 10, 6, 13, 3, 5, 2, 1, 6, 2, 2, 13, 6, 13, 13, 13, 2, 14, 30, 10, 40, 12, 8, 15, 2, 30, 4, 6, 2, 1, 6};
        String[] detail = new String[tamanho.length];
        detail = formatarString(cnab, "1");
        try {
            String sql = "select * from vw_gfccnabitautitulos where preparacao = '" + cdPreparacao
                    + "'";
            ConsultaModelo cm = new ConsultaModelo(conexao);
            cm.setQuery(sql);
            //mensagem("Preparacao: " + cdPreparacao + "\nTítulos Encontrados: " + cm.getRowCount());
            if (cm.getRowCount() > 0) {
                DataSistema dat = new DataSistema();
                dat.setData("");
                for (int i = 0; i < cm.getRowCount(); i++) {
                    detail[9] = cm.getValueAt(i, 3).toString(); //identicicação do título
                    detail[10] = cm.getValueAt(i, 2).toString(); // nosso número
                    detail[16] = cm.getValueAt(i, 4).toString(); // Numero documento de Cobrança (Dupl, NP, Etc.)
                    String vcto = dat.getDataConv(Date.valueOf(cm.getValueAt(i, 5).toString())).replaceAll("/", "");
                    detail[17] = String.format("%s%s", vcto.substring(0, 4), vcto.substring(6)); // Vencimento
                    cnab.setDataVencimento(vcto);
                    String valor = cm.getValueAt(i, 6).toString().replace(",", "");
                    detail[18] = valor.replace(".", ""); // Valor nominal do Título
                    String emis = dat.getDataConv(Date.valueOf(cm.getValueAt(i, 7).toString())).replaceAll("/", "");
                    detail[23] = String.format("%s%s", emis.substring(0, 4), emis.substring(6)); // Data da emissao do título
                    String jurosdia = cm.getValueAt(i, 8).toString().replace(",", "");
                    detail[26] = jurosdia.replace(".", "");// juros de 1 dia
                    detail[31] = cm.getValueAt(i, 9).toString(); // identificação do tipo de inscrição do pagador (01-CPF / 02-CNPJ)
                    detail[32] = cm.getValueAt(i, 10).toString(); // Número de inscrição do pagador (CPF / CNPJ)
                    detail[33] = cm.getValueAt(i, 11).toString(); // Nome do Pagador
                    detail[35] = cm.getValueAt(i, 12).toString(); // Logradouro do Pagador
                    detail[36] = cm.getValueAt(i, 13).toString(); // Bairro do Pagador
                    detail[37] = cm.getValueAt(i, 14).toString(); // CEP do Pagador
                    detail[38] = cm.getValueAt(i, 15).toString(); // Cidade do Pagador
                    detail[39] = cm.getValueAt(i, 16).toString(); // UF do Pagador
                    detail[42] = detail[17];
                    detail[43] = "66";
                    detail[45] = String.valueOf(idxArq);
                    arquivo[idxArq - 1] = cpc.posicionarCampos(detail, tipo, tamanho);
                    idxArq++;
                    double valorMulta = (cnab.getTaxaMulta() * Double.valueOf(String.valueOf(cm.getValueAt(i, 6)).replace(".", "").replace(",", "."))) / 100;
                    if (cnab.getTaxaMulta() > 0) {
                        cnab.setValorMulta(String.valueOf(valorMulta).replace(".", ""));
                        cnab.setCdTipoRegistro("2");
                        if (criarDetalheMultaItau() != 1) {
                            return 0;
                        } else {

                        }
                    }
                }
                return 1;
            }
        } catch (SQLException ex) {
            Logger.getLogger(CArquivoCNAB.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
        return 0;
    }

    /**
     * Método para ler o detalhe do arquivo do itau
     *
     * @return returna 1 caso o arquivo tenha sido lido e 0 caso ocorra erro.
     */
    private int lerDetalheItau() {
        int[] tamanho = {1, 2, 14, 4, 2, 5, 1, 8, 25, 8, 12, 3, 8, 1, 13, 1, 2, 6, 10, 8, 12, 6, 13, 3, 4, 1, 2, 13, 26, 13, 13, 13, 13, 13, 13,
            1, 2, 6, 4, 6, 13, 30, 23, 8, 7, 2, 6};
        detalhe = lerRegistroDoArquivo(tamanho);
        if (detalhe.length == tamanho.length) {
            cnab.setDetalhe(detalhe);
            return 1;
        } else {
            mensagem("Erro na leitura do detalhe do arquivo!");
            return 0;
        }
    }

    /**
     * Método para criar o detalhe de multa do arquivo
     *
     * @return
     */
    public int criarDetalheMultaItau() {
        String[] tipo = {"9", "X", "9", "9", "X", "9"};
        int[] tamanho = {1, 1, 8, 13, 370, 6};
        String[] detail = new String[tamanho.length];
        detail = formatarString(cnab, cnab.getCdTipoRegistro());
        detail[3] = cnab.getValorMulta().substring(0, 4);
        detail[5] = String.valueOf(idxArq);
        arquivo[idxArq - 1] = cpc.posicionarCampos(detail, tipo, tamanho);
        idxArq++;
        return 1;
    }

    /**
     * Método para criar o registro Trailer do Itau
     *
     * @param campos vetor de String contendo os campos do registro
     * @param tipo vetor de String contendo o tipo do campos do registro
     * @param tamanho Vetor de inteiro contendo o tamanho dos campos do registro
     * @return retorna 1 para registro criado ou 0 para registro não criado
     */
    public int criarTrailerItau() {
        String[] tipo = {"9", "X", "9"};
        String[] campos = {"9", "", String.valueOf(idxArq)};
        int[] tamanho = {1, 393, 6};
        String trailer = "";
        arquivo[idxArq - 1] = cpc.posicionarCampos(campos, tipo, tamanho);
        return 1;
    }

    /**
     * Método para ler o trailer do arquivo do itau
     *
     * @return returna 1 caso o arquivo tenha sido lido e 0 caso ocorra erro.
     */
    private int lerTrailerItau() {
        int[] tamanho = {1, 1, 2, 3, 10, 8, 14, 8, 10, 8, 14, 8, 90, 8, 14, 8, 5, 8, 14, 160, 6};
        trailer = lerRegistroDoArquivo(tamanho);
        if (trailer.length == tamanho.length) {
            trailer[5] = String.format("%s", ftq.format(Integer.valueOf(trailer[5])));
            trailer[6] = String.format("%s", Double.valueOf(String.format("%s%s%s", trailer[6].substring(0, 12), ".", trailer[6].substring(12, 14))));
            trailer[9] = String.format("%s", ftq.format(Integer.valueOf(trailer[9])));
            trailer[10] = String.format("%s", Double.valueOf(String.format("%s%s%s", trailer[10].substring(0, 12), ".", trailer[10].substring(12, 14))));
            trailer[13] = String.format("%s", ftq.format(Integer.valueOf(trailer[13])));
            trailer[14] = String.format("%s", Double.valueOf(String.format("%s%s%s", trailer[14].substring(0, 12), ".", trailer[14].substring(12, 14))));
            trailer[17] = String.format("%s", ftq.format(Integer.valueOf(trailer[17])));
            trailer[18] = String.format("%s", Double.valueOf(String.format("%s%s%s", trailer[18].substring(0, 12), ".", trailer[18].substring(12, 14))));
            cnab.setTrailer(trailer);
            return 1;
        } else {
            mensagem("Erro na leitura do trailer do arquivo!");
            return 0;
        }
    }

    /**
     * Método para ler os registros do arquivo
     *
     * @param tamanho array contendo o tamanho de cada campo
     * @return retorna array de string com os campos lidos no arquivo
     */
    private String[] lerRegistroDoArquivo(int[] tamanho) {
        int posicao = 0;
        int campo = 0;
        String[] campos = new String[tamanho.length];
        for (int i = 0; i < tamanho.length; i++) {
            campos[campo] = registro.substring(posicao, tamanho[i] + posicao);
            //         mensagem("\nInteracao: " + campo + "\nPosicao: " + (posicao + 1) + "\nTamanho: " + tamanho[i] + "\nConteudo: " + campos[campo]);
            posicao += tamanho[i];
            campo++;
        }
        return campos;
    }

    /**
     * Gerar o arquivo no disco
     *
     * @return
     * @throws FileNotFoundException
     * @throws IOException
     */
    public int gerarArquivo() throws FileNotFoundException, IOException {
        int cont = 1;
        saida.write(arquivo[0] + System.lineSeparator());
        do {
            saida.write(arquivo[cont] + System.lineSeparator());
            cont++;
        } while (cont < idxArq);
        saida.close();
        return 1;
    }

    /**
     * Método para gerar a Stringo contendo os campos do cabecalho
     *
     * @param cnab objeto contendo o conteudo a ser gerado no arquivo
     * @return vetor de String contendo os campos do cabecalho
     */
    private String[] formatarString(ArquivoCNAB cnab, String registro) {
        String[] campos = new String[arquivo.length];
        if ("0".equals(registro)) {
            String[] head = new String[16];
            formatarHeadItau(head);
            campos = head;
        } else if ("1".equals(registro)) {
            String[] detail = new String[46];
            formatarDetalheItau(detail);
            campos = detail;
        } else {
            String[] detail = new String[6];
            formatarDetalheMultaItau(detail);
            campos = detail;
        }
        return campos;
    }

    /**
     * Método para formatar o cabecalho para o banco itau
     *
     * @param head
     */
    private void formatarHeadItau(String[] head) {
        head[0] = cnab.getCdTipoRegistro();
        head[1] = cnab.getCdTipoOperacao();
        head[2] = cnab.getNomeOperacao();
        head[3] = cnab.getCdTipoServico();
        head[4] = cnab.getNomeTipoServico();
        head[5] = cnab.getCdAgencia();
        head[6] = cnab.getCdAgenciaDig();
        head[7] = cnab.getCdConta();
        head[8] = cnab.getCdContaDig();
        head[9] = cpc.gerarBrancos(8);
        head[10] = cnab.getNomeEmpresa();
        head[11] = cnab.getCdBanco();
        head[12] = cnab.getNomeBanco();
        head[13] = cnab.getDataGeracao();
        head[13] = cnab.getDataGeracao();
        head[14] = cpc.gerarBrancos(294);
        head[15] = String.valueOf(cnab.getNumeroSequencial());
    }

    /**
     * -+ Método para formatar o detalhe para o banco itau
     *
     * @param detail
     */
    private void formatarDetalheItau(String[] detail) {
        detail[0] = cnab.getCdTipoRegistro();
        detail[1] = cnab.getTipoPessoaEmpresa();
        detail[2] = cnab.getCpfCnpjEmpresa();
        detail[3] = cnab.getCdAgencia();
        detail[4] = "00";
        detail[5] = cnab.getCdConta();
        detail[6] = cnab.getCdContaDig();
        detail[7] = cpc.gerarBrancos(4);
        detail[8] = cpc.gerarZeroEsquerda(4);
        detail[11] = cpc.gerarZeroEsquerda(13);
        detail[12] = cnab.getCdCarteira();
        detail[13] = cpc.gerarBrancos(21);
        detail[14] = "I";
        detail[15] = "01";
        detail[19] = cnab.getCdBanco();
        detail[20] = cpc.gerarZeroEsquerda(5);
        detail[21] = "06";
        detail[22] = "A";
        detail[24] = "05";
        detail[25] = "05";
        detail[27] = cpc.gerarZeroEsquerda(6);
        detail[28] = cpc.gerarZeroEsquerda(13);
        detail[29] = cpc.gerarZeroEsquerda(13);
        detail[30] = cpc.gerarZeroEsquerda(13);
        detail[34] = cpc.gerarBrancos(10);
        detail[40] = cpc.gerarBrancos(30);
        detail[41] = cpc.gerarBrancos(4);
        detail[43] = cpc.gerarZeroEsquerda(2);
        detail[44] = cpc.gerarBrancos(1);
    }

    /**
     * -+ Método para formatar o detalhe para o banco itau
     *
     * @param detail
     */
    private void formatarDetalheMultaItau(String[] detail) {
        detail[0] = cnab.getCdTipoRegistro();
        detail[1] = cnab.getCdMulta();
        detail[2] = cnab.getDataVencimento();
        detail[4] = " ";
    }

    /**
     * Método para carregar a tabela de títulos do detalhe do arquivo
     *
     * @param interacao o index da interação da leituro da arquivo para saber se
     * cria uma novo resultado
     * @throws SQLException lança uma excecão de erro
     */
    private void carregarTabelaDetalhe(int interacao) throws SQLException {
        String[] detalhe = new String[10];
        detalhe[0] = this.detalhe[9];
        detalhe[1] = this.detalhe[13];
        detalhe[2] = this.detalhe[11];
        detalhe[3] = this.detalhe[15];
        detalhe[4] = this.detalhe[17];
        detalhe[5] = this.detalhe[18];
        detalhe[6] = this.detalhe[8].trim();
        detalhe[7] = this.detalhe[19];
        detalhe[8] = String.format("%s%s%s%s%s", this.detalhe[21].substring(0, 2), "/", this.detalhe[21].substring(2, 4), "/20", this.detalhe[21].substring(4, 6));
        detalhe[9] = String.format("%s", ftv.format(Double.valueOf(String.format("%s%s%s", this.detalhe[22].substring(0, 11), ".", this.detalhe[22].substring(11, 13)))));
        tabela.setModel(cm.carregarConteudoTabela(tabela, detalhe, interacao));
    }

    /**
     * Método para carregar uma lista com os registros de detalhe do arquivo
     * Itau
     */
    private void carregarListaDetalhe() {
        listaArquivo.add(new ArquivoCNAB(
                detalhe[0],
                detalhe[1],
                detalhe[2],
                detalhe[3],
                detalhe[5],
                detalhe[6],
                detalhe[8],
                detalhe[9],
                detalhe[11],
                detalhe[13],
                detalhe[15],
                detalhe[16],
                detalhe[17],
                detalhe[18],
                detalhe[21],
                String.format("%s", Double.valueOf(String.format("%s%s%s", detalhe[22].substring(0, 11), ".", detalhe[22].substring(11, 13)))),
                detalhe[23],
                detalhe[24],
                detalhe[25],
                detalhe[26],
                String.format("%s", Double.valueOf(String.format("%s%s%s", detalhe[27].substring(0, 11), ".", detalhe[27].substring(11, 13)))),
                String.format("%s", Double.valueOf(String.format("%s%s%s", detalhe[29].substring(0, 11), ".", detalhe[29].substring(11, 13)))),
                String.format("%s", Double.valueOf(String.format("%s%s%s", detalhe[30].substring(0, 11), ".", detalhe[30].substring(11, 13)))),
                String.format("%s", Double.valueOf(String.format("%s%s%s", detalhe[31].substring(0, 11), ".", detalhe[31].substring(11, 13)))),
                String.format("%s", Double.valueOf(String.format("%s%s%s", detalhe[32].substring(0, 11), ".", detalhe[32].substring(11, 13)))),
                String.format("%s", Double.valueOf(String.format("%s%s%s", detalhe[33].substring(0, 11), ".", detalhe[33].substring(11, 13)))),
                String.format("%s", Double.valueOf(String.format("%s%s%s", detalhe[34].substring(0, 11), ".", detalhe[34].substring(11, 13)))),
                detalhe[35],
                detalhe[37],
                detalhe[38],
                detalhe[41],
                detalhe[43],
                detalhe[45],
                detalhe[46]));
    }

    /**
     * Método para selecionar o registro correlado de acordo com a movimentação
     * da tabela
     *
     * @param idxSequencia index da linha da tabela de registros CNAB
     * @return retorna o registro atual de acordo a linha posicionada da tabela
     */
    public ArquivoCNAB selecionarRegistroDetalhe(int idxSequencia) {
        regAtual = listaArquivo.get(idxSequencia);
        buscarOcorencia();
        return regAtual;
    }
    
    private void buscarOcorencia(){
        EdiOcorrencia eo = new EdiOcorrencia();
        try {
            CEdiOcorrencia ceo = new CEdiOcorrencia(conexao,su);
            String sqleo = "SELECT * FROM GFCEDIOCORRENCIA WHERE CD_BANCO = '" + regAtual.getCdBanco() + 
                    "' AND CD_OCORRENCIA = '" + regAtual.getCdOcorrencia() + 
                    "'";
            ceo.pesquisar(sqleo);
            ceo.mostrarPesquisa(eo, 0);
            regAtual.setDescricaoOcorrencia(eo.getNomeOcorrencia());
            if("S".equals(eo.getLiquidarTitulo())){
                regAtual.setOcorrenciaLiquidaTitulo(true);
            }else
                regAtual.setOcorrenciaLiquidaTitulo(false);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome do Banco!\nPrograma CEdiOcorrencia.\nErro: " + ex);
        }
    }

    /**
     * Método para zerar a tabela de registro detalhas cnab
     *
     * @param tamanho Array de Objeto contendo o tamanho de cada coluna da
     * tabela
     * @param campos Array de String contendo as colunas da tabela
     * @param tipos Array de tipo do campos da tabela
     * @param editavel Array de boolean informando se o campo é editável ou não
     */
    public void zerarTabela(Object[] tamanho, String[] campos, Class[] tipos, boolean[] editavel) {
        cm.zerarTabela(tabela, tamanho, campos, tipos, editavel);
    }

    /**
     * Método para gerar mensagem na tela
     *
     * @param msg String contendo a mensagem
     */
    private void mensagem(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }
}
