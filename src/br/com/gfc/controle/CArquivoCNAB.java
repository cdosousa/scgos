/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.controle;

import br.com.DAO.ConsultaModelo;
import br.com.gfc.modelo.ArquivoCNAB;
import br.com.modelo.DataSistema;
import br.com.modelo.ParametrosGerais;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta_0917
 */
public class CArquivoCNAB {

    private ArquivoCNAB cnab;
    private ParametrosGerais pg;
    private Connection conexao;
    private String cdPreparacao;
    private String io;
    private static FileWriter saida;
    private static FileReader entrada;
    private String[] arquivo;
    private int idxArq = 1;

    /**
     * Construto padrão
     */
    public CArquivoCNAB() {

    }

    /**
     * Contrutor para ser utilizado para gerar arquivo no disto
     * @param cnab Objeto contendo as informações padrão do arquivo
     * @param pg Objeto contedo os parametros gerais do sistema
     * @param conexao Objetoc contendo a instância de conexão do usuário com o banco de dados
     * @param linhas Variáveis inteira contendo a quantidade de registro que serão gravados
     * @param cdPreparacao variáveis contendo o códido da preparação de pagamento
     * @throws FileNotFoundException Lançamento de erro de exeção
     * @throws IOException Lançamento de erro de exeção
     */
    public CArquivoCNAB(ArquivoCNAB cnab, ParametrosGerais pg, Connection conexao, int linhas, String cdPreparacao) throws FileNotFoundException, IOException {
        this.cnab = cnab;
        this.pg = pg;
        this.conexao = conexao;
        this.cdPreparacao = cdPreparacao;
        arquivo = new String[linhas];
        setaVariaveisEscrita();
    }

    /**
     * Método para setar as variáveis de ambiente para escrever no arquivo
     * @throws FileNotFoundException
     * @throws IOException 
     */
    private void setaVariaveisEscrita() throws FileNotFoundException, IOException {
        saida = new FileWriter(pg.getLocalBoletoBanco() + "remessaItau.txt");
    }
    
    private void setaVariaveisLeitura() throws FileNotFoundException{
        entrada = new FileReader(pg.getLocalBoletoBanco() + "retornoItau.txt");
       
    }

    /**
     * Método para preparar o arquivo que será gerado
     * @return retorna se o arquivo foi gerado.
     */
    public int prepararArquivo() {
        DataSistema dat = new DataSistema();
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

    /**
     * Método para criar o cabecalho do arquivo
     *
     * @param cnab Objeto contendo as informções do arquivo EDI
     * @param tipo vetor de string conteno o tipo de cada campo do arquivo
     * @param tamanho vetor de inteiros contendo o tamanho de cada campo do
     * arquivo
     * @return retorna interiro (zero ou um) para dizer e o arquivo foi gerado
     * ou não
     */
    public int criarHeaddItau(ArquivoCNAB cnab) {
        String[] tipo = {"9", "9", "X", "9", "X", "9", "9", "9", "9", "X", "X", "9", "X", "9", "X", "9"};
        int[] tamanho = {1, 1, 7, 2, 15, 4, 2, 5, 1, 8, 30, 3, 15, 6, 294, 6};
        cnab.setCdAgenciaDig("00");
        String[] head = formatarString(cnab, "0");
        String cabecalho = "";
        arquivo[idxArq - 1] = posicionarCampos(head, tipo, tamanho);
        //mensagem("Arquivo: " + arquivo[idxArq]);
        idxArq++;
        return 1;
    }

    /**
     * Método para criar o detalhe do arquivo
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
                    detail[17] = String.format("%s%s", vcto.substring(0, 4),vcto.substring(6)); // Vencimento
                    String valor = cm.getValueAt(i, 6).toString().replace(",", "");
                    detail[18] = valor.replace(".", ""); // Valor nominal do Título
                    String emis = dat.getDataConv(Date.valueOf(cm.getValueAt(i, 7).toString())).replaceAll("/", ""); 
                    detail[23] = String.format("%s%s", emis.substring(0, 4),emis.substring(6)); // Data da emissao do título
                    String jurosdia = cm.getValueAt(i, 8).toString().replace(",", "");
                    detail[26] =  jurosdia.replace(".", "");// juros de 1 dia
                    detail[31] = cm.getValueAt(i, 9).toString(); // identificação do tipo de inscrição do pagador (01-CPF / 02-CNPJ)
                    detail[32] = cm.getValueAt(i, 10).toString(); // Número de inscrição do pagador (CPF / CNPJ)
                    detail[33] = cm.getValueAt(i, 11).toString(); // Nome do Pagador
                    detail[35] = cm.getValueAt(i, 12).toString(); // Logradouro do Pagador
                    detail[36] = cm.getValueAt(i, 13).toString(); // Bairro do Pagador
                    detail[37] = cm.getValueAt(i, 14).toString(); // CEP do Pagador
                    detail[38] = cm.getValueAt(i, 15).toString(); // Cidade do Pagador
                    detail[39] = cm.getValueAt(i, 16).toString(); // UF do Pagador
                    detail[45] = String.valueOf(idxArq);
                    arquivo[idxArq - 1] = posicionarCampos(detail, tipo, tamanho);
                    idxArq++;
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
        arquivo[idxArq - 1] = posicionarCampos(campos, tipo, tamanho);
        return 1;
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
        saida.write(arquivo[0]);
        do {
            saida.write("\n" + arquivo[cont]);
            cont++;
        } while (cont < idxArq);
        saida.close();
        return 1;
    }
    
    public int lerArquivo(){
        int cont = 1;
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
        head[9] = gerarBrancos(8);
        head[10] = cnab.getNomeEmpresa();
        head[11] = cnab.getCdBanco();
        head[12] = cnab.getNomeBanco();
        head[13] = cnab.getDataGeracao();
        head[13] = cnab.getDataGeracao();
        head[14] = gerarBrancos(294);
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
        detail[7] = gerarBrancos(4);
        detail[8] = gerarZeroEsquerda(4);
        detail[11] = gerarZeroEsquerda(13);
        detail[12] = cnab.getCdCarteira();
        detail[13] = gerarBrancos(21);
        detail[14] = "I";
        detail[15] = "01";
        detail[19] = cnab.getCdBanco();
        detail[20] = gerarZeroEsquerda(5);
        detail[21] = "06";
        detail[22] = "A";
        detail[24] = "05";
        detail[25] = "05";
        detail[27] = gerarZeroEsquerda(6);
        detail[28] = gerarZeroEsquerda(13);
        detail[29] = gerarZeroEsquerda(13);
        detail[30] = gerarZeroEsquerda(13);
        detail[34] = gerarBrancos(10);
        detail[40] = gerarBrancos(30);
        detail[41] = gerarBrancos(4);
        detail[42] = gerarZeroEsquerda(6);
        detail[43] = gerarZeroEsquerda(2);
        detail[44] = gerarBrancos(1);
    }

    /**
     * Método para fazer o posicionamento dos campos dentro do arquivo
     * @param campos Vetor de String contendo os campos a serem posicionados
     * @param tipo Vetor de String contendo o formato de cada campo do registro
     * @param tamanho Vetor de inteiro contendo o tamanho de cada campo do registro
     * @return retorna um String concatenada com os campos posicionados no registro
     */
    private String posicionarCampos(String[] campos, String[] tipo, int[] tamanho) {
        int x = 0;
        int y = 0;
        String linha = "";
        do {
            //mensagem("Conteudo: " + campos[x] + "\nTamanho: " + tamanho[y]);
            if ("X".equals(tipo[x].toString().toUpperCase())) {
                if (campos[x].length() < tamanho[y]) {
                    if (campos[x].isEmpty()) {
                        linha = String.format("%s%s", linha, gerarBrancos(tamanho[y]));
                    } else {
                        int qtd = Integer.valueOf(tamanho[y]) - campos[x].trim().length();
                        int tam = campos[x].length();
                        //mensagem("Tamanho do conteudo: " + tam + "\nQtde Brancos a buscar: " + qtd);
                        linha = String.format("%s%s%s", linha, campos[x].toString().substring(0, tam), gerarBrancos(qtd));
                    }
                } else {
                    linha = String.format("%s%s", linha, campos[x].toString().substring(0, tamanho[y]));
                }
            } else {
                if (campos[x].length() < tamanho[y]) {
                    if (campos[x].isEmpty()) {
                        linha = String.format("%s%s", linha, gerarZeroEsquerda(tamanho[y]));
                    } else {
                        linha = String.format("%s%s%s", linha, gerarZeroEsquerda(Integer.valueOf(tamanho[y]) - campos[x].length()).trim(), campos[x].substring(0, campos[x].length()).trim());
                    }
                } else {
                    linha = String.format("%s%s", linha, campos[x].trim().substring(0, tamanho[y]));
                }
            }
            x++;
            y++;
        } while (x < tipo.length);
        return linha;
    }

    /**
     * Método para gerar complemento branco para campos do arquivo
     *
     * @param tamanho tamanho de espaco em beanco a ser gerado
     * @return retorna String com espaços em branco solicitado
     */
    private String gerarBrancos(int tamanho) {
        String brancos = "";
        for (int i = 0; i < tamanho; i++) {
            brancos = String.format("%s%s", brancos, " ");
        }
        return brancos;
    }

    /**
     * Método para gerar zero a esquerda para campos numéricos
     *
     * @param qtde quantidade de zeros a serem complementados
     * @return retorna String contendo a quantidade e zeros soliciadas
     */
    private String gerarZeroEsquerda(int qtde) {
        String zeroEsquerda = "";
        for (int i = 0; i < qtde; i++) {
            zeroEsquerda = String.format("%s%s", zeroEsquerda, "0");
        }
        return zeroEsquerda;
    }

    private void mensagem(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }
}
