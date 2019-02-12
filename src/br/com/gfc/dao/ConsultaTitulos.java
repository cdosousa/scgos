/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.dao;

import br.com.DAO.ConsultaModelo;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.JOptionPane;

/**
 * Esta classe extende a classe ConsultaModelo para, acrescentando os métodos getters/setters e modificando o método setQuery para totalização dos títulos
 * @author Cristiano de Oliveira Sousa criado em 20/08/2018
 */
public class ConsultaTitulos extends ConsultaModelo {

    // variáveis de instância local
    private double totTitulosRec;
    private double totTitulosPag;
    private double totPrevisaoRec;
    private double totPrevisaoPag;
    private NumberFormat ftv;

    /**
     * Construtor padrão para chamada da classe
     * @param conexao objeto contendo a instância de conexão do usuário com o banco de dados
     * @throws SQLException retorna exeção caso ocorra erro
     */
    public ConsultaTitulos(Connection conexao) throws SQLException {
        super(conexao);
        ftv.getInstance();
        ftv = new DecimalFormat("###,###,##0.0000");
        ftv.setMaximumFractionDigits(2);
    }

    /**
     * Método para fazer a consulta no banco de dados através de uma query qualquer feito pelo usuário e retorna o modela da tabela
     * @param query String contendo a select para ser execurada no banco de dados
     * @throws SQLException Retorna uma Execeção do SQL caso ocorra erro
     * @throws IllegalStateException Retorna uma exeção caso ocorra um erro geral
     */
    @Override
    public void setQuery(String query) throws SQLException, IllegalStateException {
        if (!statusConexao) {
            throw new IllegalStateException("Sem conexão com o Banco de Dados");
        }
        // especifica a consulta e executa
        resultSet = stmt.executeQuery(query);
        // obtém metadados para o resultSet
        metaData = resultSet.getMetaData();
        totalizaTitulos();
        // determina o número de linhas em resultSet
        resultSet.last(); // move para a última linha
        numberOfRows = resultSet.getRow(); //obtém o número da linha
        // notifica o Jtable que o modelo foi alterado
        fireTableStructureChanged();
    }

    /**
     * Método para totalizar os títulos por fornecedores
     * @throws SQLException retorna uma Execção do SQL caso ocorra erro na consulta
     */
    public void totalizaPortadores() throws SQLException {
        int numLinha = 0;
        String tipo;
        resultSet.first();
        do {
            tipo = String.format("%s", getValueAt(numLinha, 0).toString().substring(0, 2));
            try {
                switch (tipo) {
                    case "Pa":
                        totTitulosPag = getTotTitulosPag() + ftv.parse(String.format("%s", getValueAt(numLinha, 5))).doubleValue();
                        break;
                    case "Re":
                        totTitulosRec = getTotTitulosRec() + ftv.parse(String.format("%s", getValueAt(numLinha, 5))).doubleValue();
                        break;
                    default:
                        totTitulosPag = getTotTitulosPag();
                        totTitulosRec = getTotTitulosRec();
                        break;
                }
            } catch (ParseException px) {
                mensagem("Erro na totalização por portador!\nErro: " + px);
            }
            numLinha++;
        } while (resultSet.next());
    }

    /**
     * Método para os títulos totalizar por tipo de movimento e lancamento
     *
     * @throws SQLException
     */
    public void totalizaTitulos() throws SQLException {
        int numLinha = 0;
        String tipo;
        resultSet.first();
        do {
            tipo = String.format("%s", getValueAt(numLinha, 3).toString().toUpperCase() + getValueAt(numLinha, 1).toString().toUpperCase() + getValueAt(numLinha, 13).toString().toUpperCase());
            try {
                switch (tipo) {
                    case "PREPAABERTO":
                        totPrevisaoPag = getTotPrevisaoPag() + ftv.parse(String.format("%s", getValueAt(numLinha, 12))).doubleValue();
                        break;
                    case "PREREABERTO":
                        totPrevisaoRec = getTotPrevisaoRec() + ftv.parse(String.format("%s", getValueAt(numLinha, 12))).doubleValue();
                        break;
                    case "TITPAABERTO":
                        totTitulosPag = getTotTitulosPag() + ftv.parse(String.format("%s", getValueAt(numLinha, 12))).doubleValue();
                        break;
                    case "TITREABERTO":
                        totTitulosRec = getTotTitulosRec() + ftv.parse(String.format("%s", getValueAt(numLinha, 12))).doubleValue();
                        break;
                    default:
                        totPrevisaoPag = getTotPrevisaoPag();
                        totPrevisaoRec = getTotPrevisaoRec();
                        totTitulosPag = getTotTitulosPag();
                        totTitulosRec = getTotTitulosRec();
                        break;
                }
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(null, "Erro na conversão do número!\nErro: " + ex);
            }
            numLinha++;
        } while (resultSet.next());
    }

    /**
     * @return the totTitulosRec
     */
    public double getTotTitulosRec() {
        return totTitulosRec;
    }

    /**
     * @return the totTitulosPag
     */
    public double getTotTitulosPag() {
        return totTitulosPag;
    }

    /**
     * @return the totPrevisaoRec
     */
    public double getTotPrevisaoRec() {
        return totPrevisaoRec;
    }

    /**
     * @return the totPrevisaoPag
     */
    public double getTotPrevisaoPag() {
        return totPrevisaoPag;
    }
}