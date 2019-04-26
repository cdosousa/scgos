/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa GFCMO0020
 */
package br.com.gfc.visao;

import br.com.DAO.ConsultaModelo;
import br.com.gfc.controle.CArquivoCNAB;
import br.com.gfc.controle.CPreparacaoPagamentos;
import br.com.modelo.SessaoUsuario;
import java.sql.Connection;
import java.sql.Date;

import br.com.gfc.dao.ConsultaTitulos;
import br.com.gfc.modelo.ArquivoCNAB;
import br.com.gfc.modelo.Lancamentos;
import br.com.gfc.modelo.PreparacaoPagamentos;
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoData;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.DefineCampoInteiro;
import br.com.modelo.HoraSistema;
import br.com.modelo.FormatarValor;
import br.com.modelo.ParametrosGerais;
import br.com.modelo.VerificarMouse;
import br.com.modelo.VerificarTecla;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.text.NumberFormat;
import java.text.ParseException;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JTable;
import net.sf.jasperreports.engine.JRException;

/**
 *
 * @author Cristiano de Oliveira Sousa criado em 22/08/2018
 * @version teste
 */
public class ManterPreparacaoPagamento extends javax.swing.JFrame {

    /**
     * Objetos de instância da classe
     */
    private static Connection conexao;
    private static SessaoUsuario su;
    private static ParametrosGerais pg;
    private DataSistema dat;
    private HoraSistema hs;
    private NumberFormat formato;

    /**
     * objetos de instância dos registros da classe
     */
    private ConsultaTitulos ctPortadores;
    private ConsultaTitulos ctTitulos;
    private ConsultaModelo ctAgendamentos;
    private ConsultaModelo ctAgendTitulos;
    private PreparacaoPagamentos modpp;
    private CPreparacaoPagamentos cpp;
    private Lancamentos modlan;
    private ArquivoCNAB cnab;
    private CArquivoCNAB ccnab;
    private ArquivoCNAB cb;

    /**
     * variáveis de instância da classe
     */
    private String sqlPortadores;
    private String sqlTitulo;
    private String sqlAgendamento;
    private String sqlAgendamentoTitulos;
    private String dtEmisIni;
    private String dtEmisFin;
    private String dtVctoIni;
    private String dtVctoFin;
    private String situacaoIni = "AA";
    private String situacaoFin = "ZZ";
    private String pagarReceberIni = "aa";
    private String pagarReceberFin = "ZZ";
    private String tipoLancametoIni = "aaa";
    private String tipoLancametoFin = "ZZZ";
    private String nome = "";
    private String cdPorador;
    private String cdTipoPagamento;
    private String cdTipoMovimento;
    private int linhaPortadores;
    private int linhaTitulos;
    private int linhaPagamAgend;
    private int linhaTitAgend;
    private int linhaDetalheCNAB;
    private boolean moverLinha = true;
    private boolean moverLinhaTitulosAgendados = true;
    private boolean botaoMarcarDesmarcar = false;
    private boolean marcarLinha = false;
    private boolean buscaCNAB = false;
    private boolean[] intervaloTit;
    private int contIntervTit = 0;
    private int[] linhaSelecTitulo;
    private double valTotalSelec;
    private String peridoLiquDe;
    private String peridoLiquAte;
    private String operAgendamento;
    private String nomeArquivoRetorno;

    /**
     * Creates new form ManterPreparacaoPagamento
     */
    public ManterPreparacaoPagamento(Connection conexao, SessaoUsuario su, ParametrosGerais pg) {
        this.conexao = conexao;
        this.su = su;
        this.pg = pg;
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setaVariaveis();
        monitoraLinhaTabelas();
        setLocationRelativeTo(null);
        monitorCliqueTabela();
        this.dispose();
    }

    /**
     * Método para setar as variáveis de filtro da tela
     */
    private void setaVariaveis() {
        formato = NumberFormat.getInstance();
        String data;
        String data1;
        dat = new DataSistema();
        DataSistema dat1 = new DataSistema();
        dat.setData("");
        dat1.setData("");
        jForDatEmissaoFin.setText(dat.getDataConv(Date.valueOf(dat.getData())));
        data = dat.getDataConv(jForDatEmissaoFin.getText());
        dat.setData(Date.valueOf(data), -30);
        jForDatEmissaoIni.setText(dat.getDataConv(Date.valueOf(dat.getData())));
        jForDatVencimentoIni.setText(dat1.getDataConv(Date.valueOf(dat1.getData())));
        data1 = dat1.getDataConv(jForDatVencimentoIni.getText());
        dat1.setData(Date.valueOf(data1), 30);
        jForDatVencimentoFin.setText(dat.getDataConv(Date.valueOf(dat1.getData())));
        jForQtdeTitulos.setDocument(new DefineCampoInteiro());
        jForValTotTitulos.setDocument(new DefineCampoDecimal());
        jForNumPreparacao.setDocument(new DefineCampoInteiro());
        jForDataLiquidacaoDe.setDocument(new DefineCampoData());
        jForDataLiquidacaoAte.setDocument(new DefineCampoData());
        carregaControles();
        buscarTotalPortadores();
        buscarAgendamentos();
    }

    /**
     * Método para instanciar controladores
     */
    private void carregaControles() {
        cpp = new CPreparacaoPagamentos(conexao, su, pg);
        try {
            ctPortadores = new ConsultaTitulos(conexao);
            ctTitulos = new ConsultaTitulos(conexao);
        } catch (SQLException ex) {
            Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * método para buscar os valores por portadores
     */
    private void buscarTotalPortadores() {
        linhaPortadores = 0;
        sqlPortadores = "{call pr_total_portadores_x_tipopgamento(?,?,?,?,?,?,?,?,?,?)}";
        dtEmisIni = dat.getDataConv(jForDatEmissaoIni.getText());
        dtEmisFin = dat.getDataConv(jForDatEmissaoFin.getText());
        dtVctoIni = dat.getDataConv(jForDatVencimentoIni.getText());
        dtVctoFin = dat.getDataConv(jForDatVencimentoFin.getText());
        if (!jTexNomeCliente.getText().isEmpty()) {
            nome = jTexNomeCliente.getText();
        } else {
            nome = "";
        }

        try {
            ctPortadores.callProcedure(sqlPortadores, dtVctoIni, dtVctoFin, dtEmisIni, dtEmisFin, pagarReceberIni, pagarReceberFin, tipoLancametoIni, tipoLancametoFin, "AB", nome);
            if (ctPortadores.getRowCount() > 0) {
                jTabPortadores.setModel(ctPortadores);
                ctPortadores.ajustarTabela(jTabPortadores, 5, 5, 100, 5, 100, 30);
                moverLinha = true;
                ctPortadores.totalizaPortadores();
                jForTotTitReceber.setText(String.valueOf(ctPortadores.getTotTitulosRec()));
                jForTotTitPagar.setText(String.valueOf(ctPortadores.getTotTitulosPag()));
                buscarTitulos();
            } else {
                zerarTabelaTitulos(jTabTitulo);
            }
        } catch (SQLException ex) {
            mensagemTela(String.format("%s", "Erro na busca do total por portadores!!\n") + ex);
        }
    }

    /**
     * Método para buscar lancamentos financeiros
     */
    private void buscarTitulos() {
        cdPorador = String.format("%s", jTabPortadores.getValueAt(linhaPortadores, 1));
        cdTipoPagamento = String.format("%s", jTabPortadores.getValueAt(linhaPortadores, 3));
        cdTipoMovimento = String.format("%s", jTabPortadores.getValueAt(linhaPortadores, 0).toString().substring(0, 2));
        sqlTitulo = "call pr_titulosXportadorXtipoPagamento(?,?,?,?,?,?,?,?,?,?,?,?,?)";
        linhaTitulos = 0;
        try {
            ctTitulos.callProcedure(sqlTitulo, dtVctoIni, dtVctoFin, dtEmisIni, dtEmisFin, pagarReceberIni, pagarReceberFin, tipoLancametoIni, tipoLancametoFin, "AB", nome, cdPorador, cdTipoPagamento, cdTipoMovimento);
            jTabTitulo.setModel(ctTitulos);
            intervaloTit = new boolean[jTabTitulo.getRowCount()];
            ctTitulos.alinharTabela(jTabTitulo, 8, 11, 12);
            ctTitulos.ajustarTabela(jTabTitulo, 30, 5, 5, 5, 30, 200, 20, 5, 20, 30, 30, 20, 20, 20);
            jTabTitulo.setRowSelectionAllowed(true);
            jTabTitulo.setFocusTraversalKeysEnabled(false);
            ctTitulos.totalizaTitulos();
            contIntervTit = 0;
            linhaSelecTitulo = new int[contIntervTit];

        } catch (SQLException ex) {
            mensagemTela("Erro na busca dos Títulos Financeiro!\nErro: " + ex);
        } catch (Exception ex) {
            mensagemTela("Erro geral na busca dos Títulos!\nErro: " + ex);
        }
    }

    /**
     * Método para buscar os agendamentos realizados
     */
    private void buscarAgendamentos() {
        linhaPagamAgend = 0;
        sqlAgendamento = "select * from vw_gfcpreparacaopagamento";
        try {
            ctAgendamentos = new ConsultaModelo(conexao);
            ctAgendamentos.setQuery(sqlAgendamento);
            jTabPagamentosAgendados.setModel(ctAgendamentos);
            if (jTabPagamentosAgendados.getRowCount() > 0) {
                ctAgendamentos.ajustarTabela(jTabPagamentosAgendados, 50, 60, 60, 20, 20, 20, 30, 20, 30);
                buscarTitulosAgendados();
            }
        } catch (SQLException ex) {
            mensagemTela("Erro na busca de Pagamentos Agendados");
        }
    }

    private void buscarTitulosAgendados() {
        sqlAgendamentoTitulos = "SELECT  p.cd_lancamento AS Lancamento,"
                + " l.titulo AS Titulo,"
                + " p.valor_saldo AS Valor,"
                + " l.data_vencimento AS Vencimento,"
                + " CASE (l.situacao)"
                + "    WHEN 'AB' THEN 'Aberto'"
                + "    WHEN 'LI' THEN 'Liquidado'"
                + "    WHEN 'CO' THEN 'Contabilizado'"
                + "    WHEN 'CA' THEN 'Cancelado'"
                + " END AS Situacao"
                + " FROM gfcpreparacaotitulos p"
                + " LEFT JOIN gfclancamentos l ON p.cd_lancamento = l.cd_lancamento"
                + " where p.cd_preparacao = '" + String.format("%s", jTabPagamentosAgendados.getValueAt(linhaPagamAgend, 0))
                + "'";
        try {
            ctAgendTitulos = new ConsultaModelo(conexao);
            ctAgendTitulos.setQuery(sqlAgendamentoTitulos);
            jTabTitulosAgendados.setModel(ctAgendTitulos);
            if (jTabTitulosAgendados.getRowCount() > 0) {
                //linhaTitAgend = 0;
                ctAgendTitulos.ajustarTabela(jTabTitulosAgendados, 40, 5, 30, 30, 20);
                if (linhaTitAgend >= 0) {
                    buscarCorrelatosTitulo(jTabTitulosAgendados.getValueAt(linhaTitAgend, 0).toString());
                }
            }
        } catch (SQLException ex) {
            mensagemTela("Errona busca dos títulos agendados!\nErr: " + ex);
        }
    }

    /**
     * método para monitorar a linha da tabela
     */
    private void monitoraLinhaTabelas() {
        //Adiciona listener para a tabela de portadores
        jTabPortadores.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (moverLinha) {
                    linhaPortadores = jTabPortadores.getSelectedRow();
                    if (jTabPortadores.getRowCount() > 0) {
                        jButPreparar.setEnabled(true);
                        jButMarcarDesmarcar.setEnabled(false);
                        verificaBotaoMarcarTodos();
                        botaoMarcarDesmarcar = false;
                        buscarTitulos();
                    } else {
                        zerarTabelaTitulos(jTabTitulo);
                    }
                }
            }
        });

        //adiciona listener para a tabela de títulos
        jTabTitulo.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                linhaTitulos = jTabTitulo.getSelectedRow();
            }
        });

        jTabTitulo.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {

            }

            @Override
            public void keyReleased(KeyEvent e) {
                VerificarTecla vt = new VerificarTecla();
                String tecla = vt.VerificarTecla(e).toUpperCase();
                if ("ACIMA".equals(tecla)) {
                    if (contIntervTit > 0) {
                        //mensagemTela("pressionei seta: " + tecla);
                        listarSelecionados();
                    }
                    //mensagemTela("Listei os selecionados");
                }
                if ("ESPAÇO".equals(tecla)) {
                    //marcarDesmarcarLinha();
                }

            }
        });
        // Adiciona listener para a tabela de agendamento
        jTabPagamentosAgendados.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                limparTelaTitulos();
                linhaPagamAgend = jTabPagamentosAgendados.getSelectedRow();
                limparTelaAgendamento();
                buscarCorrelatosAgendamento();
                buscarTitulosAgendados();
            }
        });

        //Adiciona listener para a tabela de títulos agendados
        jTabTitulosAgendados.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    if (moverLinhaTitulosAgendados) {
                        if (jTabTitulosAgendados.getRowCount() > 0) {
                            System.out.println("\n");
                            System.out.println("Linha da Tabela de titulos agendados: " + jTabTitulosAgendados.getSelectedRow());
                            linhaTitAgend = jTabTitulosAgendados.getSelectedRow();
                            System.out.println("Variável linhaTitAgend: " + linhaTitAgend);
                            if (linhaTitAgend >= 0) {
                                buscarCorrelatosTitulo(jTabTitulosAgendados.getValueAt(linhaTitAgend, 0).toString());
                            }
                        }
                    }
                    moverLinhaTitulosAgendados = true;
                } catch (SQLException ex) {
                    Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        jTabTitulosAgendados.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                VerificarTecla vt = new VerificarTecla();
                if ("F5".equals(String.format("%s", vt.VerificarTecla(e)).toUpperCase())) {
                    zoomTitulo();
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

        //Adiciona listener para a tabela de detalhes do cnab
        jTabDetalheCNAB.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                try {
                    if (jTabbPrincipal.getSelectedIndex() == 2 && buscaCNAB && jTabDetalheCNAB.getRowCount() > 0) {
                        linhaDetalheCNAB = jTabDetalheCNAB.getSelectedRow();
                        limparDetalheTelaCNAB();
                        atualizarDetail();
                        if (linhaDetalheCNAB >= 0) {
                            if (buscarCorrelatosTitulo(String.format("%s%s", "1", jTabDetalheCNAB.getValueAt(linhaDetalheCNAB, 0).toString())) == 1) {
                                atualizarCorrelatosTabelaCnab();
                            }
                        }
                    }
                } catch (Exception ex) {
                    mensagemTela("Erro na busca dos registros correlados da tabela de detalhe do arquivo CNAB!\nErro: " + ex);
                }
            }
        });

        //Adiciona um keylistener para atabela de detalhes do cnab
        jTabDetalheCNAB.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                VerificarTecla vt = new VerificarTecla();
                if ("F5".equals(String.format("%s", vt.VerificarTecla(e)).toUpperCase())) {
                    new ManterTitulos(su, conexao, String.format("%s%s", "1", jTabDetalheCNAB.getValueAt(linhaDetalheCNAB, 0))).setVisible(true);
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });

    }

    /**
     * Método para monitorar o clique na tabela
     */
    private void monitorCliqueTabela() {
    }

    /**
     * Método para zerar a tabela de títulos
     */
    private void zerarTabelaTitulos(JTable tabela) {
        ctTitulos.zerarTabela(tabela, new Object[]{null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                new String[]{"Lancamento", "Tipo", "TpDoc", "TpLanc", "Emissao", "Cliente", "Titulo", "Parc", "Valor", "Vencimento", "Liquidacao", "ValorLiq", "Saldo", "Situacao"},
                new Class[]{String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, Double.class, String.class,
                    String.class, Double.class, Double.class, String.class},
                new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false});
    }

    private void zerarTabelaDetalheCNAB() {
        ccnab.zerarTabela(new Object[]{null, null, null, null, null, null, null, null, null, null},
                new String[]{"Nosso Num.", "DAC", "Carteira", "Cod.Cart.", "Dt.Ocorr.", "Doc", "Esp.Doc", "Num.Banco", "Dt.Vencimento", "Valor Titulo"},
                new Class[]{String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, String.class, Double.class},
                new boolean[]{false, false, false, false, false, false, false, false, false, false});
    }

    /**
     * Método para marcar e desmarcar títulos
     *
     * @param tipoGeracao tipo de geração da nova tabela (N-Nova tabela /
     * S-Tabela Seleção)
     */
    private void marcarDesmarcarTitulos() throws SQLException {
        ctTitulos.zerarTabela(jTabTitulo, new Object[]{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                new String[]{"Sel.", "Lancamento", "Tipo", "TpDoc", "TpLanc", "Emissao", "Cliente", "Titulo", "Parc", "Valor", "Vencimento", "Liquidacao", "ValorLiq", "Saldo", "Situacao"},
                new Class[]{Boolean.class, String.class, String.class, String.class, String.class, Date.class, String.class, String.class, String.class, String.class, Date.class,
                    Date.class, String.class, String.class, String.class},
                new boolean[]{true, false, false, false, false, false, false, false, false, false, false, false, false, false, false});
        jTabTitulo.setModel(ctTitulos.gerarNovaTabela(jTabTitulo, botaoMarcarDesmarcar));
        jTabTitulo.setRowSelectionInterval(0, 0);
        intervaloTit = new boolean[jTabTitulo.getRowCount()];

    }

    /**
     * Método selecionar títulos marcados
     */
    private void selecionarTitulosMarcados() throws SQLException {
        ctTitulos.zerarTabela(jTabTituloSelecionados, new Object[]{null, null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                new String[]{"Sel.", "Lancamento", "Tipo", "TpDoc", "TpLanc", "Emissao", "Cliente", "Titulo", "Parc", "Valor", "Vencimento", "Liquidacao", "ValorLiq", "Saldo", "Situacao"},
                new Class[]{Boolean.class, String.class, String.class, String.class, String.class, Date.class, String.class, String.class, String.class, String.class, Date.class,
                    Date.class, String.class, String.class, String.class},
                new boolean[]{true, false, false, false, false, false, false, false, false, false, false, false, false, false, false});
        jTabTituloSelecionados.setModel(ctTitulos.gerarNovaTabela(jTabTituloSelecionados, linhaSelecTitulo));
        jTabTituloSelecionados.setRowSelectionInterval(0, 0);
        //intervaloTit = new boolean[jTabTituloSelecionados.getRowCount()];
    }

    /**
     * mètodo para zerar a tabela portadores
     */
    private void zerarTabelaPortadores() {
        ctPortadores.zerarTabela(jTabTitulo, new Object[][]{null, null, null, null, null, null},
                new String[]{"Tipo", "Portador", "Nome Portador", "Tipo Pagamento", "Nome Tipo Pagamento", "Valor Total"},
                new Class[]{String.class, String.class, String.class, String.class, String.class, Double.class},
                new boolean[]{false, false, false, false, false, false});
    }

    private void verificaBotaoMarcarTodos() {
        if (!botaoMarcarDesmarcar) {
            jButMarcarDesmarcar.setText("Marcar Todos");
            botaoMarcarDesmarcar = true;
        } else {
            jButMarcarDesmarcar.setText("Desmarcar Todos");
            botaoMarcarDesmarcar = false;
        }
    }

    private void marcarDesmarcarLinha() {
        if (jTabTitulo.getSelectedColumn() == 0) {
            intervaloTit[linhaTitulos] = (Boolean) jTabTitulo.getValueAt(linhaTitulos, 0);
            if ((Boolean) jTabTitulo.getValueAt(linhaTitulos, 0)) {
                contIntervTit++;
                if (Date.valueOf(peridoLiquDe).after(Date.valueOf(String.format("%s", jTabTitulo.getValueAt(linhaTitulos, 10))))) {
                    peridoLiquDe = String.format("%s", jTabTitulo.getValueAt(linhaTitulos, 10));
                }
                if (Date.valueOf(peridoLiquAte).before(Date.valueOf(String.format("%s", jTabTitulo.getValueAt(linhaTitulos, 10))))) {
                    peridoLiquAte = String.format("%s", jTabTitulo.getValueAt(linhaTitulos, 10));
                }
                try {
                    valTotalSelec = valTotalSelec + formato.parse(String.format("%s", jTabTitulo.getValueAt(linhaTitulos, 13))).doubleValue();
                } catch (ParseException ex) {
                    Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
                }
                listarSelecionados();

            } else if (contIntervTit > 0) {
                contIntervTit--;
                try {
                    valTotalSelec = valTotalSelec - formato.parse(String.format("%s", jTabTitulo.getValueAt(linhaTitulos, 13))).doubleValue();
                } catch (ParseException ex) {
                    Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
                }
                listarSelecionados();
            }
        }
    }

    /**
     * Método para listar os títulos que foram selecionados
     */
    private void listarSelecionados() {
        if (!botaoMarcarDesmarcar) {
            jTabTitulo.setRowSelectionInterval(0, jTabTitulo.getRowCount() - 1);
        } else {
            //        mensagemTela("Adicionar linha selecionada\nContador: " + contIntervTit);
            linhaSelecTitulo = new int[contIntervTit];
            int idx = 0;
            for (int i = 0; i < jTabTitulo.getRowCount(); i++) {
                if (intervaloTit[i]) {
                    linhaSelecTitulo[idx] = i;
                    //mensagemTela("Gravei index: " + idx + "\nLinha: " + i);
                    idx++;
                }
            }
            jTabTitulo.removeRowSelectionInterval(0, jTabTitulo.getRowCount() - 1);
            for (int j : linhaSelecTitulo) {
                //          mensagemTela("Marcando linhas: " + j);
                jTabTitulo.addRowSelectionInterval(j, j);
            }
        }
    }

    /**
     * Método para salvar a preparação no banco de dados
     *
     * @throws ParseException
     * @throws SQLException
     */
    private void salvarPreparacao() throws ParseException, SQLException {
        modpp = new PreparacaoPagamentos();
        modpp.setDataLiquidacaoIni(dat.getDataConv(jForDataLiquidacaoDe.getText()));
        modpp.setDataLiquidacaoFin(dat.getDataConv(jForDataLiquidacaoAte.getText()));
        modpp.setCdPortador(jTexCdInfoPortador.getText());
        modpp.setCdTipoPagamento(jTexCdInfoTipoPagamento.getText());
        modpp.setTipoMovimento(jTexCdInfoTipoMovimento.getText());
        modpp.setQuantidadeTitulos(Integer.valueOf(jForQtdeTitulos.getText()));
        modpp.setValorTotal(formato.parse(jForValTotTitulos.getText()).doubleValue());
        cpp.gerarPreparacao(modpp, operAgendamento, jTabTituloSelecionados);
    }

    /**
     * Método para buscar as informações dos lançametos
     *
     * @throws SQLException
     */
    private int buscarCorrelatosTitulo(String titulo) throws SQLException {
        modlan = cpp.buscarCorrelatosTitulo(titulo);
        if (modlan != null) {
            jTexNomeRazaoSocial.setText(modlan.getNomeRazaoSocial());
            jTexDocumento.setText(modlan.getDocumento());
            jTexTipoDocumento.setText(modlan.getTipoDocumento());
            jForEmissaoTitulo.setText(dat.getDataConv(Date.valueOf(modlan.getDataEmissao())));
            return 1;
        }
        return 0;
    }

    /**
     * Método para buscar o tipo de movimento
     */
    private void buscarCorrelatosAgendamento() {
        jTexCdInfoPortador.setText(jTabPagamentosAgendados.getValueAt(linhaPagamAgend, 3).toString());
        jTexNomeInfoPortador.setText(cpp.buscarPortador(jTexCdInfoPortador.getText()));
        jTexCdInfoTipoPagamento.setText(jTabPagamentosAgendados.getValueAt(linhaPagamAgend, 4).toString());
        jTexNomeInfoTipoPagamento.setText(cpp.buscarTipoPagamento(jTexCdInfoTipoPagamento.getText()));
        jTexCdInfoTipoMovimento.setText(jTabPagamentosAgendados.getValueAt(linhaPagamAgend, 5).toString());
        jTexNomeInfoTipoMovimento.setText(cpp.buscarTipoMovimento(jTexCdInfoTipoMovimento.getText()));
        if ("Re".equals(jTexCdInfoTipoMovimento.getText())) {
            if (cpp.isEnviarArquivoCNAB()) {
                jButArquivoCnab.setEnabled(true);
            } else {
                jButArquivoCnab.setEnabled(false);
            }

            if (cpp.isGerarBoleto()) {
                jButBoleto.setEnabled(true);
            } else {
                jButBoleto.setEnabled(false);
            }
        }
    }

    /**
     * Método para limpar tela de títulos selecionados para agendamento
     */
    private void limparTelaAgendamento() {
        jTexCdInfoPortador.setText("");
        jTexNomeInfoPortador.setText("");
        jTexCdInfoTipoPagamento.setText("");
        jTexNomeInfoTipoPagamento.setText("");
        jTexCdInfoTipoMovimento.setText("");
        jTexNomeInfoTipoMovimento.setText("");
    }

    /**
     * Método para limpar tela de correlato de títulos
     */
    private void limparTelaTitulos() {
        jTexNomeRazaoSocial.setText("");
        jTexDocumento.setText("");
        jTexTipoDocumento.setText("");
        jForEmissaoTitulo.setText("");
    }

    /**
     * Método para limpar os campos de head e trailer da tala de CNAB
     */
    private void limparHeadTrailerTelaCNAB() {
        //registro de head
        jTexCdMovimento.setText("");
        jTexNomeMovimento.setText("");
        jTexCdTipoServico.setText("");
        jTexNomeTipoServico.setText("");
        jForDataArquivo.setText("");
        jTexNomeEmpresa.setText("");
        jTexCdBanco.setText("");
        jTexNomeBanco.setText("");
        jTexCdAgencia.setText("");
        jTexCdConta.setText("");
        jTexCdDigConta.setText("");
        jForDataCreditoArquivo.setText("");

        //registro do trailer
        jTexQtdTitulosCobSimples.setText("0");
        jForValorTituloCobSimples.setText("0.00");
        jTexAvisoBcoCobSimples.setText("");
        jTexQtdTituloCobVinculada.setText("0");
        jForValorTituloCobVinculada.setText("0.00");
        jTexAvisoBcoCobVinculada.setText("");
        jTexQtdTituloCobDirEscriturada.setText("0");
        jForValTituloCobDirEscriturada.setText("0.00");
        jTexAvisoBcoCobDirEscriturada.setText("");
        jTexQtdDetalhes.setText("0");
        jForValorTotalInformado.setText("0.00");
    }

    /**
     * Metodo para limpar tela do arquivo CNAB
     */
    private void limparDetalheTelaCNAB() {
        //registro do detail
        jForValorDespCobranca.setText("0.00");
        jForValorAbatConcedido.setText("0.00");
        jForValorDescConcedido.setText("0.00");
        jForValorLiqEmConta.setText("0.00");
        jForValorJurosMoraMulta.setText("0.00");
        jForValorOutrosCred.setText("0.00");
        jForValorIOF.setText("0.00");
        jTexCdOcorrencia.setText("");
        jTexCdIntrCancelamento.setText("");
        jTexPagador.setText("");
        jTexCdErroMensInformacao.setText("");
        jTexCdLiquidacao.setText("");
        jForDataCredLiquidacao.setText("");
        jTexSeqRegistro.setText("");
    }

    /**
     * Método criado para gerar a liquidação do título manual
     */
    private void liquidarTitulo(String cdLancamento) {
        ManterLiquidacaoTitulo liquidar = new ManterLiquidacaoTitulo(this, rootPaneCheckingEnabled, conexao, su, cdLancamento);
        liquidar.setVisible(true);
        moverLinhaTitulosAgendados = false;
    }

    /**
     * Método criado para gerar a liquidação do título pelo edi
     */
    private void liquidarTitulo(String cdLancamento, double valorLiquidacao, String dataLiquidacao) {
        ManterLiquidacaoTitulo liquidar = new ManterLiquidacaoTitulo(this, rootPaneCheckingEnabled, conexao, su, cdLancamento, true, valorLiquidacao, dataLiquidacao);
        liquidar.setVisible(true);
    }

    /**
     * Método para imprimir a preparação de pagamento
     */
    private void imprimirPreparacao() {
        JFormattedTextField valorTotal = new FormatarValor(FormatarValor.MOEDA);
        valorTotal.setText(jTabPagamentosAgendados.getValueAt(linhaPagamAgend, 6).toString());
        cpp.prepararRelatorio(jTabPagamentosAgendados.getValueAt(linhaTitAgend, 3).toString(),
                jTabPagamentosAgendados.getValueAt(linhaTitAgend, 4).toString(),
                dat.getDataConv(Date.valueOf(jTabPagamentosAgendados.getValueAt(linhaPagamAgend, 1).toString())),
                dat.getDataConv(Date.valueOf(jTabPagamentosAgendados.getValueAt(linhaTitAgend, 2).toString())),
                jTabPagamentosAgendados.getValueAt(linhaTitAgend, 5).toString(),
                jTabPagamentosAgendados.getValueAt(linhaPagamAgend, 7).toString(),
                valorTotal.getText(),
                jTabPagamentosAgendados.getValueAt(linhaPagamAgend, 0).toString());
    }

    /**
     * Método para gerar arquivo CNAB
     *
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void gerarArquivoCNAB() throws FileNotFoundException, IOException {
        cnab = new ArquivoCNAB();
        cpp.buscarParametrosEDI(cnab, jTexCdInfoTipoPagamento.getText());
        if (cnab.isParametroAtivo() == true) {
            //mensagemTela("Linha Posicionada: " + linhaPagamAgend);
            ccnab = new CArquivoCNAB(cnab, pg, conexao, su, jTabTitulosAgendados.getRowCount() * 2 + 2, jTabPagamentosAgendados.getValueAt(linhaPagamAgend, 0).toString());
            if (ccnab.prepararArquivo() == 1) {
                if (ccnab.gerarArquivo() == 1) {
                    ccnab.atualizarLancamento();
                }
            }
        } else {
            mensagemTela("Este banco não está habilitado para envio de arquivo CNAB");
        }

    }

    private void imprimirBoleto() throws SQLException, JRException, ParseException {
        cpp.prepararBoleto(jTabPagamentosAgendados.getValueAt(linhaPagamAgend, 0).toString(), "BoletoItau.jasper");
    }

    private void lerArquivoCNAB() throws FileNotFoundException, IOException {
        cnab = new ArquivoCNAB();
        ccnab = new CArquivoCNAB(cnab, pg, conexao, nomeArquivoRetorno);
        buscaCNAB = true;
        ccnab.lerArquivo(jTabDetalheCNAB);
        atualizarDetail();
        atualizarHead();
        atualizarTrailer();
    }

    /**
     * Método para buscar o aquivo de retorno do banco
     */
    private void buscarArquivoCNAB() {
        limparHeadTrailerTelaCNAB();
        limparDetalheTelaCNAB();
        linhaDetalheCNAB = 0;
        if (buscaCNAB) {
            zerarTabelaDetalheCNAB();
        }
        buscaCNAB = false;
        JFileChooser fc = new JFileChooser();
        int option = fc.showOpenDialog(jPanel6);
        if (option == fc.APPROVE_OPTION) {
            File arq = fc.getSelectedFile();
            try {
                jTexArquivoRetorno.setText(arq.getCanonicalPath());
                nomeArquivoRetorno = arq.getName();
            } catch (IOException ex) {
                Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Método para atualizar os campos do registro head na tela
     */
    private void atualizarHead() {
        String[] head = cnab.getHead();
        jTexCdMovimento.setText(head[1]);
        jTexNomeMovimento.setText(head[2]);
        jTexCdTipoServico.setText(head[3]);
        jTexNomeTipoServico.setText(head[4]);
        jForDataArquivo.setText(String.format("%s%s%s%s%s", head[13].substring(0, 2), "/", head[13].substring(2, 4), "/20", head[13].substring(4, 6)));
        jTexNomeEmpresa.setText(head[10]);
        jTexCdBanco.setText(head[11]);
        jTexNomeBanco.setText(head[12]);
        jTexCdAgencia.setText(head[5]);
        jTexCdConta.setText(head[7]);
        jTexCdDigConta.setText(head[8]);
        jForDataCreditoArquivo.setText(String.format("%s%s%s%s%s", head[17].substring(0, 2), "/", head[17].substring(2, 4), "/20", head[17].substring(4, 6)));
    }

    /**
     * Método para atualizar os campos do registro detalhe na tela
     */
    private void atualizarDetail() {
        dat = new DataSistema();
        cb = ccnab.selecionarRegistroDetalhe(linhaDetalheCNAB);
        jForValorDespCobranca.setText(cb.getValorTarifaCobranca());
        jForValorAbatConcedido.setText(cb.getValorAbatimento());
        jForValorDescConcedido.setText(cb.getValorDesconto());
        jForValorLiqEmConta.setText(cb.getValorPrincipal());
        jForValorJurosMoraMulta.setText(cb.getValorMulta());
        jForValorOutrosCred.setText(cb.getValorOutrosCreditos());
        jForValorIOF.setText(cb.getValorIof());
        jTexCdOcorrencia.setText(cb.getCdOcorrencia());
        jTexNomeOcorrencia.setText(cb.getDescricaoOcorrencia());
        jTextAreaOcorrencia.setText(cb.getDescricaoOcorrencia());
        jTexCdIntrCancelamento.setText(cb.getCdInstrucaoCancelada());
        jTexPagador.setText(cb.getNomeCliente());
        jTexCdErroMensInformacao.setText(cb.getMensagemErrosInfor());
        jTexCdLiquidacao.setText(cb.getCdLiquidacao());
        jForDataCredLiquidacao.setText(cb.getDataCredito());
        jTexSeqRegistro.setText(cb.getSequencialRegistro());
    }

    private void atualizarCorrelatosTabelaCnab() {
        if ("109".equals(jTabDetalheCNAB.getValueAt(linhaDetalheCNAB, 2).toString())) {
            try {
                if (linhaDetalheCNAB >= 0) {
                    buscarCorrelatosTitulo(String.format("%s%s", "1", jTabDetalheCNAB.getValueAt(linhaDetalheCNAB, 0).toString()));
                }
            } catch (SQLException ex) {
                Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
            }
            jTexCdLancamentoVinc.setText(modlan.getCdLancamento());
            jForDataEmissaVinc.setText(dat.getDataConv(Date.valueOf(modlan.getDataEmissao())));
            jForDataVenctoVinc.setText(dat.getDataConv(Date.valueOf(modlan.getDataVencimento())));
            jForValorVinc.setText(String.valueOf(modlan.getValorLancamento()));
            jForSaldoVinc.setText(String.valueOf(modlan.getValorSaldo()));
            jForCpfCnpjVinc.setText(modlan.getCpfCnpj());
            jTexNomeClienteVinc.setText(modlan.getNomeRazaoSocial());
            if (cb.isOcorrenciaLiquidaTitulo()) {
                jButLiquidarViaEdi.setEnabled(true);
            } else {
                jButLiquidarViaEdi.setEnabled(false);
            }
        }
    }

    /**
     * Método para atualuzar os campos do registro trailer na tela
     */
    private void atualizarTrailer() {
        String[] trailer = cnab.getTrailer();
        jTexQtdTitulosCobSimples.setText(trailer[5]);
        jForValorTituloCobSimples.setText(trailer[6]);
        jTexAvisoBcoCobSimples.setText(trailer[7]);
        jTexQtdTituloCobVinculada.setText(trailer[9]);
        jForValorTituloCobVinculada.setText(trailer[10]);
        jTexAvisoBcoCobVinculada.setText(trailer[11]);
        jTexQtdTituloCobDirEscriturada.setText(trailer[13]);
        jForValTituloCobDirEscriturada.setText(trailer[14]);
        jTexAvisoBcoCobDirEscriturada.setText(trailer[15]);
        jTexQtdDetalhes.setText(trailer[17]);
        jForValorTotalInformado.setText(trailer[18]);
    }

    private void zoomTitulo() {
        String cdLancamento = jTabTitulosAgendados.getValueAt(linhaTitAgend, 0).toString();
        new ManterTitulos(su, conexao, cdLancamento).setVisible(true);
    }

    /**
     * Método para retornar mensagem na tela
     *
     * @param msg Mensagem a ser exibida
     */
    private void mensagemTela(String msg) {
        JOptionPane.showMessageDialog(null, msg);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTooMenuFerramentas = new javax.swing.JToolBar();
        jButNovo = new javax.swing.JButton();
        jButEditar = new javax.swing.JButton();
        jButSalvar = new javax.swing.JButton();
        jButCancelar = new javax.swing.JButton();
        jButExcluir = new javax.swing.JButton();
        jButPesquisar = new javax.swing.JButton();
        jButAnterior = new javax.swing.JButton();
        jButProximo = new javax.swing.JButton();
        jButSair = new javax.swing.JButton();
        jPanGeral = new javax.swing.JPanel();
        jTabbPrincipal = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanTotaisTitulos = new javax.swing.JPanel();
        jLabTotTitReceber = new javax.swing.JLabel();
        jLabTotTitPagar = new javax.swing.JLabel();
        jForTotTitReceber = new FormatarValor(FormatarValor.NUMERO);
        jForTotTitPagar = new FormatarValor(FormatarValor.NUMERO);
        jPanFiltros = new javax.swing.JPanel();
        jLabSituacao = new javax.swing.JLabel();
        jComSituacao = new javax.swing.JComboBox<>();
        jLabPagarReceber = new javax.swing.JLabel();
        jComPagarReceber = new javax.swing.JComboBox<>();
        jForDatEmissaoFin = new javax.swing.JFormattedTextField();
        jForDatVencimentoIni = new javax.swing.JFormattedTextField();
        jPanCliente = new javax.swing.JPanel();
        jLabNomeCliente = new javax.swing.JLabel();
        jTexNomeCliente = new javax.swing.JTextField();
        jForDatVencimentoFin = new javax.swing.JFormattedTextField();
        jLabTipoLancamento = new javax.swing.JLabel();
        jLabDataEmissao = new javax.swing.JLabel();
        jComTipoLancamento = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jForDatEmissaoIni = new javax.swing.JFormattedTextField();
        jButBuscar = new javax.swing.JButton();
        jButPreparar = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabPortadores = new javax.swing.JTable();
        jPanTitulos = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTabTitulo = new javax.swing.JTable();
        jPanBotoesTitulo = new javax.swing.JPanel();
        jButSelecionarMarcados = new javax.swing.JButton();
        jButMarcarDesmarcar = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanSelecionados = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTabTituloSelecionados = new javax.swing.JTable();
        jPanTotaisSelecao = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabNumPreparacao = new javax.swing.JLabel();
        jLabDataLiquidacao = new javax.swing.JLabel();
        jForNumPreparacao = new FormatarValor(FormatarValor.INTEIRO);
        jForDataLiquidacaoDe = new javax.swing.JFormattedTextField();
        jForDataLiquidacaoAte = new javax.swing.JFormattedTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabQtdeTitulos = new javax.swing.JLabel();
        jLabValTotTitulos = new javax.swing.JLabel();
        jForValTotTitulos = new FormatarValor(FormatarValor.NUMERO);
        jForQtdeTitulos = new FormatarValor(FormatarValor.INTEIRO);
        jButSalvarPreparacao = new javax.swing.JButton();
        jPanPagamentosAgendados = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTabPagamentosAgendados = new javax.swing.JTable();
        jPanInfoPortador = new javax.swing.JPanel();
        jLabInfoPortador = new javax.swing.JLabel();
        jLabInfoTipoPagamento = new javax.swing.JLabel();
        jLabInfoTipoMovimento = new javax.swing.JLabel();
        jTexCdInfoPortador = new javax.swing.JTextField();
        jTexNomeInfoPortador = new javax.swing.JTextField();
        jTexCdInfoTipoPagamento = new javax.swing.JTextField();
        jTexNomeInfoTipoPagamento = new javax.swing.JTextField();
        jTexCdInfoTipoMovimento = new javax.swing.JTextField();
        jTexNomeInfoTipoMovimento = new javax.swing.JTextField();
        jPanTitulosAgendados = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTabTitulosAgendados = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabDocumento = new javax.swing.JLabel();
        jTexNomeRazaoSocial = new javax.swing.JTextField();
        jTexDocumento = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTexTipoDocumento = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jForEmissaoTitulo = new javax.swing.JFormattedTextField();
        jButLiquidar = new javax.swing.JButton();
        jButImprimirPrep = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jButBoleto = new javax.swing.JButton();
        jButArquivoCnab = new javax.swing.JButton();
        jButRetornoCNAB = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        jLabArquivoRetorno = new javax.swing.JLabel();
        jTexArquivoRetorno = new javax.swing.JTextField();
        jButArquivo = new javax.swing.JButton();
        jPanRetornoDeArquivo = new javax.swing.JPanel();
        jLabCodigoMovimento = new javax.swing.JLabel();
        jTexCdMovimento = new javax.swing.JTextField();
        jTexNomeMovimento = new javax.swing.JTextField();
        jLabTipoServico = new javax.swing.JLabel();
        jTexCdTipoServico = new javax.swing.JTextField();
        jTexNomeTipoServico = new javax.swing.JTextField();
        jLabDataArquivo = new javax.swing.JLabel();
        jForDataArquivo = new javax.swing.JFormattedTextField();
        jLabNomeEmpresa = new javax.swing.JLabel();
        jTexNomeEmpresa = new javax.swing.JTextField();
        jLabCdBanco = new javax.swing.JLabel();
        jTexCdBanco = new javax.swing.JTextField();
        jTexNomeBanco = new javax.swing.JTextField();
        jLabAgenciaConta = new javax.swing.JLabel();
        jTexCdAgencia = new javax.swing.JTextField();
        jTexCdConta = new javax.swing.JTextField();
        jLabContaDigito = new javax.swing.JLabel();
        jTexCdDigConta = new javax.swing.JTextField();
        jLabDataCreditoArquivo = new javax.swing.JLabel();
        jForDataCreditoArquivo = new javax.swing.JFormattedTextField();
        jLabContaDigito1 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        jTabDetalheCNAB = new javax.swing.JTable();
        jPanDetalheArquivo = new javax.swing.JPanel();
        jLabValorDespCobr = new javax.swing.JLabel();
        jLabValorAbatConced = new javax.swing.JLabel();
        jLabValorDescConced = new javax.swing.JLabel();
        jLabValorLiqEmConta = new javax.swing.JLabel();
        jLabValorJurosMoraMulta = new javax.swing.JLabel();
        jLabValorOutrosCred = new javax.swing.JLabel();
        jForValorDespCobranca = new FormatarValor(FormatarValor.NUMERO);
        jForValorAbatConcedido = new FormatarValor(FormatarValor.NUMERO);
        jForValorDescConcedido = new FormatarValor(FormatarValor.NUMERO);
        jForValorLiqEmConta = new FormatarValor(FormatarValor.NUMERO);
        jForValorJurosMoraMulta = new FormatarValor(FormatarValor.NUMERO);
        jForValorOutrosCred = new FormatarValor(FormatarValor.NUMERO);
        jLabValorIOF = new javax.swing.JLabel();
        jForValorIOF = new FormatarValor(FormatarValor.NUMERO);
        jSeparator2 = new javax.swing.JSeparator();
        jLabCdOcorrencia = new javax.swing.JLabel();
        jLabCdIntrCancel = new javax.swing.JLabel();
        jLabPagador = new javax.swing.JLabel();
        jLabCdErroMensInfor = new javax.swing.JLabel();
        jLabCdLiquidacao = new javax.swing.JLabel();
        jLabDataCredLiquid = new javax.swing.JLabel();
        jTexCdOcorrencia = new javax.swing.JTextField();
        jTexNomeOcorrencia = new javax.swing.JTextField();
        jTexCdIntrCancelamento = new javax.swing.JTextField();
        jTexNomeIntrCancel = new javax.swing.JTextField();
        jTexPagador = new javax.swing.JTextField();
        jTexCdErroMensInformacao = new javax.swing.JTextField();
        jTexNomeErroMensInfor = new javax.swing.JTextField();
        jTexCdLiquidacao = new javax.swing.JTextField();
        jTexNomeLiquidacao = new javax.swing.JTextField();
        jForDataCredLiquidacao = new javax.swing.JFormattedTextField();
        jLabSeqRegistro = new javax.swing.JLabel();
        jTexSeqRegistro = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabQtdTitulosCobSimples = new javax.swing.JLabel();
        jLabValorTituloCobSimples = new javax.swing.JLabel();
        jLabAvisoBcoCobSimples = new javax.swing.JLabel();
        jTexQtdTitulosCobSimples = new javax.swing.JTextField();
        jTexAvisoBcoCobSimples = new javax.swing.JTextField();
        jLabQtdTituloCobVinculada = new javax.swing.JLabel();
        jTexQtdTituloCobVinculada = new javax.swing.JTextField();
        jLabValorTituloCobVinculada = new javax.swing.JLabel();
        jLabAvisoBcoCobVinculada = new javax.swing.JLabel();
        jTexAvisoBcoCobVinculada = new javax.swing.JTextField();
        jLabQtdTituloCobDirEscriturada = new javax.swing.JLabel();
        jTexQtdTituloCobDirEscriturada = new javax.swing.JTextField();
        jLabValTituloCobDirEscriturada = new javax.swing.JLabel();
        jLabAvisoBcoCobDirEscriturada = new javax.swing.JLabel();
        jTexAvisoBcoCobDirEscriturada = new javax.swing.JTextField();
        jLabValorTotalInformado = new javax.swing.JLabel();
        jLabQtdDetalhes = new javax.swing.JLabel();
        jForValorTituloCobSimples = new FormatarValor(FormatarValor.NUMERO);
        jForValorTituloCobVinculada = new FormatarValor(FormatarValor.NUMERO);
        jForValorTotalInformado = new FormatarValor(FormatarValor.NUMERO);
        jTexQtdDetalhes = new javax.swing.JTextField();
        jForValTituloCobDirEscriturada = new FormatarValor(FormatarValor.NUMERO)
        ;
        jButLiquidarViaEdi = new javax.swing.JButton();
        jPanTituloVinculado = new javax.swing.JPanel();
        jLabCdLancamentoVinc = new javax.swing.JLabel();
        jLabDataEmissaVinc = new javax.swing.JLabel();
        jLabDataVenctoVinc = new javax.swing.JLabel();
        jLabValorVinc = new javax.swing.JLabel();
        jLabSaldoVinc = new javax.swing.JLabel();
        jLabCpfCnpjVinc = new javax.swing.JLabel();
        jTexCdLancamentoVinc = new javax.swing.JTextField();
        jForDataEmissaVinc = new javax.swing.JFormattedTextField();
        jForDataVenctoVinc = new javax.swing.JFormattedTextField();
        jForValorVinc = new FormatarValor(FormatarValor.NUMERO);
        jForSaldoVinc = new FormatarValor(FormatarValor.NUMERO);
        jForCpfCnpjVinc = new javax.swing.JFormattedTextField();
        jTexNomeClienteVinc = new javax.swing.JTextField();
        jPanOcorrencias = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTextAreaOcorrencia = new javax.swing.JTextArea();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTextArea3 = new javax.swing.JTextArea();
        jScrollPane10 = new javax.swing.JScrollPane();
        jTextArea4 = new javax.swing.JTextArea();
        jLabel9 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Preparação para Pagamentos");
        setMinimumSize(new java.awt.Dimension(1350, 830));
        setResizable(false);

        jTooMenuFerramentas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTooMenuFerramentas.setRollover(true);

        jButNovo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Addthis-32.png"))); // NOI18N
        jButNovo.setText("Novo");
        jButNovo.setFocusable(false);
        jButNovo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButNovo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTooMenuFerramentas.add(jButNovo);

        jButEditar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Edit-32.png"))); // NOI18N
        jButEditar.setText("Editar");
        jButEditar.setFocusable(false);
        jButEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButEditar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTooMenuFerramentas.add(jButEditar);

        jButSalvar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Ok-32.PNG"))); // NOI18N
        jButSalvar.setText("Salvar");
        jButSalvar.setFocusable(false);
        jButSalvar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButSalvar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTooMenuFerramentas.add(jButSalvar);

        jButCancelar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Cancel-32.png"))); // NOI18N
        jButCancelar.setText("Cancelar");
        jButCancelar.setFocusable(false);
        jButCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTooMenuFerramentas.add(jButCancelar);

        jButExcluir.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Delete-32.png"))); // NOI18N
        jButExcluir.setText("Excluir");
        jButExcluir.setFocusable(false);
        jButExcluir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButExcluir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTooMenuFerramentas.add(jButExcluir);

        jButPesquisar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Search-32.png"))); // NOI18N
        jButPesquisar.setText("Pesquisar");
        jButPesquisar.setFocusable(false);
        jButPesquisar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButPesquisar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTooMenuFerramentas.add(jButPesquisar);

        jButAnterior.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButAnterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Back-32.png"))); // NOI18N
        jButAnterior.setText("Anterior");
        jButAnterior.setEnabled(false);
        jButAnterior.setFocusable(false);
        jButAnterior.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButAnterior.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTooMenuFerramentas.add(jButAnterior);

        jButProximo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButProximo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Next-32.png"))); // NOI18N
        jButProximo.setText("Próximo");
        jButProximo.setEnabled(false);
        jButProximo.setFocusable(false);
        jButProximo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButProximo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jTooMenuFerramentas.add(jButProximo);

        jButSair.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButSair.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Exit-32.png"))); // NOI18N
        jButSair.setText("Sair");
        jButSair.setFocusable(false);
        jButSair.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButSair.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButSairActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButSair);

        jPanGeral.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTabbPrincipal.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N

        jPanel2.setMaximumSize(new java.awt.Dimension(1321, 634));

        jPanTotaisTitulos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Total em lançamentos abertos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabTotTitReceber.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabTotTitReceber.setText("Total Títulos à Receber:");

        jLabTotTitPagar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabTotTitPagar.setText("Total Títulos à Pagar:");

        jForTotTitReceber.setEditable(false);
        jForTotTitReceber.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForTotTitReceber.setEnabled(false);

        jForTotTitPagar.setEditable(false);
        jForTotTitPagar.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForTotTitPagar.setEnabled(false);

        javax.swing.GroupLayout jPanTotaisTitulosLayout = new javax.swing.GroupLayout(jPanTotaisTitulos);
        jPanTotaisTitulos.setLayout(jPanTotaisTitulosLayout);
        jPanTotaisTitulosLayout.setHorizontalGroup(
            jPanTotaisTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTotaisTitulosLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanTotaisTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabTotTitReceber, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabTotTitPagar, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTotaisTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jForTotTitPagar, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(jForTotTitReceber))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanTotaisTitulosLayout.setVerticalGroup(
            jPanTotaisTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTotaisTitulosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTotaisTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabTotTitReceber)
                    .addComponent(jForTotTitReceber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTotaisTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabTotTitPagar)
                    .addComponent(jForTotTitPagar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39))
        );

        jPanFiltros.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Filtros", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabSituacao.setText("Situação:");

        jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Aberto", "Cancelado", "Contabilizado", "Liquidado" }));
        jComSituacao.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComSituacaoItemStateChanged(evt);
            }
        });

        jLabPagarReceber.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPagarReceber.setText("Pagar / Receber:");

        jComPagarReceber.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Pagar", "Receber" }));
        jComPagarReceber.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComPagarReceberItemStateChanged(evt);
            }
        });

        try {
            jForDatEmissaoFin.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        try {
            jForDatVencimentoIni.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jPanCliente.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Cliente", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabNomeCliente.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabNomeCliente.setText("Nome:");

        jTexNomeCliente.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTexNomeClienteKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanClienteLayout = new javax.swing.GroupLayout(jPanCliente);
        jPanCliente.setLayout(jPanClienteLayout);
        jPanClienteLayout.setHorizontalGroup(
            jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanClienteLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabNomeCliente)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTexNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );
        jPanClienteLayout.setVerticalGroup(
            jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanClienteLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanClienteLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeCliente)
                    .addComponent(jTexNomeCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        try {
            jForDatVencimentoFin.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabTipoLancamento.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabTipoLancamento.setText("Tipo Lançamento:");

        jLabDataEmissao.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabDataEmissao.setText("Data Emissão:");

        jComTipoLancamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Todos", "Adiantamento", "Previsão", "Titulo", "Transferência" }));
        jComTipoLancamento.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                jComTipoLancamentoItemStateChanged(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel5.setText("Data Vencimento:");

        try {
            jForDatEmissaoIni.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jButBuscar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButBuscar.setText("Buscar");
        jButBuscar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButBuscarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanFiltrosLayout = new javax.swing.GroupLayout(jPanFiltros);
        jPanFiltros.setLayout(jPanFiltrosLayout);
        jPanFiltrosLayout.setHorizontalGroup(
            jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanFiltrosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanFiltrosLayout.createSequentialGroup()
                        .addComponent(jLabSituacao)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabPagarReceber)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jComPagarReceber, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabTipoLancamento)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComTipoLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButBuscar, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanFiltrosLayout.createSequentialGroup()
                        .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel5)
                            .addComponent(jLabDataEmissao))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jForDatEmissaoIni)
                            .addComponent(jForDatVencimentoIni, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jForDatEmissaoFin)
                            .addComponent(jForDatVencimentoFin, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 15, Short.MAX_VALUE)))
                .addContainerGap(28, Short.MAX_VALUE))
        );
        jPanFiltrosLayout.setVerticalGroup(
            jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanFiltrosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanFiltrosLayout.createSequentialGroup()
                        .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabSituacao)
                            .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabPagarReceber)
                            .addComponent(jComPagarReceber, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabTipoLancamento))
                        .addGap(3, 3, 3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComTipoLancamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButBuscar)))
                .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanFiltrosLayout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabDataEmissao)
                            .addComponent(jForDatEmissaoIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForDatEmissaoFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanFiltrosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jForDatVencimentoIni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForDatVencimentoFin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanFiltrosLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanCliente, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jButPreparar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButPreparar.setText("Preparar");
        jButPreparar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButPrepararActionPerformed(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Valores para Pagamentos por Portador e Tipo de Pagamento", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jTabPortadores.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jTabPortadores.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Portador", "Nome Portador", "Tipo Pagamento", "Nome Tipo Pagamento", "Valor Total"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane1.setViewportView(jTabPortadores);
        if (jTabPortadores.getColumnModel().getColumnCount() > 0) {
            jTabPortadores.getColumnModel().getColumn(0).setResizable(false);
            jTabPortadores.getColumnModel().getColumn(0).setPreferredWidth(10);
            jTabPortadores.getColumnModel().getColumn(1).setResizable(false);
            jTabPortadores.getColumnModel().getColumn(1).setPreferredWidth(100);
            jTabPortadores.getColumnModel().getColumn(2).setResizable(false);
            jTabPortadores.getColumnModel().getColumn(2).setPreferredWidth(10);
            jTabPortadores.getColumnModel().getColumn(3).setResizable(false);
            jTabPortadores.getColumnModel().getColumn(3).setPreferredWidth(100);
            jTabPortadores.getColumnModel().getColumn(4).setResizable(false);
            jTabPortadores.getColumnModel().getColumn(4).setPreferredWidth(60);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanTitulos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Títulos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jTabTitulo.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Lancamento", "Tipo", "TpDoc", "TpLanc", "Emissão", "Cliente", "Titulo", "Parc", "Valor", "Vencimento", "Liquidação", "Valor Liq.", "Saldo", "Situação"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabTitulo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabTituloMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(jTabTitulo);
        if (jTabTitulo.getColumnModel().getColumnCount() > 0) {
            jTabTitulo.getColumnModel().getColumn(0).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTabTitulo.getColumnModel().getColumn(1).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(1).setPreferredWidth(5);
            jTabTitulo.getColumnModel().getColumn(2).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(2).setPreferredWidth(5);
            jTabTitulo.getColumnModel().getColumn(3).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(3).setPreferredWidth(5);
            jTabTitulo.getColumnModel().getColumn(4).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(4).setPreferredWidth(30);
            jTabTitulo.getColumnModel().getColumn(5).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(5).setPreferredWidth(200);
            jTabTitulo.getColumnModel().getColumn(6).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(6).setPreferredWidth(20);
            jTabTitulo.getColumnModel().getColumn(7).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(7).setPreferredWidth(5);
            jTabTitulo.getColumnModel().getColumn(8).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(8).setPreferredWidth(20);
            jTabTitulo.getColumnModel().getColumn(9).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(9).setPreferredWidth(30);
            jTabTitulo.getColumnModel().getColumn(10).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(10).setPreferredWidth(30);
            jTabTitulo.getColumnModel().getColumn(11).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(11).setPreferredWidth(20);
            jTabTitulo.getColumnModel().getColumn(12).setResizable(false);
            jTabTitulo.getColumnModel().getColumn(12).setPreferredWidth(20);
            jTabTitulo.getColumnModel().getColumn(13).setResizable(false);
        }

        jPanBotoesTitulo.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jButSelecionarMarcados.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButSelecionarMarcados.setText("Selecionar Marcados");
        jButSelecionarMarcados.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButSelecionarMarcadosActionPerformed(evt);
            }
        });

        jButMarcarDesmarcar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButMarcarDesmarcar.setText("Marcar Todos");
        jButMarcarDesmarcar.setEnabled(false);
        jButMarcarDesmarcar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButMarcarDesmarcarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanBotoesTituloLayout = new javax.swing.GroupLayout(jPanBotoesTitulo);
        jPanBotoesTitulo.setLayout(jPanBotoesTituloLayout);
        jPanBotoesTituloLayout.setHorizontalGroup(
            jPanBotoesTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanBotoesTituloLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jButMarcarDesmarcar)
                .addGap(910, 910, 910)
                .addComponent(jButSelecionarMarcados)
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanBotoesTituloLayout.setVerticalGroup(
            jPanBotoesTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanBotoesTituloLayout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addGroup(jPanBotoesTituloLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButSelecionarMarcados)
                    .addComponent(jButMarcarDesmarcar))
                .addGap(6, 6, 6))
        );

        javax.swing.GroupLayout jPanTitulosLayout = new javax.swing.GroupLayout(jPanTitulos);
        jPanTitulos.setLayout(jPanTitulosLayout);
        jPanTitulosLayout.setHorizontalGroup(
            jPanTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTitulosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanBotoesTitulo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanTitulosLayout.setVerticalGroup(
            jPanTitulosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanTitulosLayout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 174, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanBotoesTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10))
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanTitulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanFiltros, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButPreparar)
                            .addComponent(jPanTotaisTitulos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanFiltros, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanTotaisTitulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButPreparar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanTitulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbPrincipal.addTab("Preparação", jPanel2);

        jPanSelecionados.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Títulos Selecionados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jTabTituloSelecionados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Lancamento", "Tipo", "TpDoc", "TpLanc", "Emissão", "Cliente", "Titulo", "Parc", "Valor", "Vencimento", "Liquidação", "Valor Liq.", "Saldo", "Situação"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jTabTituloSelecionados.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTabTituloSelecionadosMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTabTituloSelecionados);
        if (jTabTituloSelecionados.getColumnModel().getColumnCount() > 0) {
            jTabTituloSelecionados.getColumnModel().getColumn(0).setResizable(false);
            jTabTituloSelecionados.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTabTituloSelecionados.getColumnModel().getColumn(1).setResizable(false);
            jTabTituloSelecionados.getColumnModel().getColumn(1).setPreferredWidth(5);
            jTabTituloSelecionados.getColumnModel().getColumn(2).setResizable(false);
            jTabTituloSelecionados.getColumnModel().getColumn(2).setPreferredWidth(5);
            jTabTituloSelecionados.getColumnModel().getColumn(3).setResizable(false);
            jTabTituloSelecionados.getColumnModel().getColumn(3).setPreferredWidth(5);
            jTabTituloSelecionados.getColumnModel().getColumn(4).setResizable(false);
            jTabTituloSelecionados.getColumnModel().getColumn(4).setPreferredWidth(20);
            jTabTituloSelecionados.getColumnModel().getColumn(5).setResizable(false);
            jTabTituloSelecionados.getColumnModel().getColumn(5).setPreferredWidth(200);
            jTabTituloSelecionados.getColumnModel().getColumn(6).setResizable(false);
            jTabTituloSelecionados.getColumnModel().getColumn(6).setPreferredWidth(20);
            jTabTituloSelecionados.getColumnModel().getColumn(7).setResizable(false);
            jTabTituloSelecionados.getColumnModel().getColumn(7).setPreferredWidth(5);
            jTabTituloSelecionados.getColumnModel().getColumn(8).setResizable(false);
            jTabTituloSelecionados.getColumnModel().getColumn(8).setPreferredWidth(20);
            jTabTituloSelecionados.getColumnModel().getColumn(9).setResizable(false);
            jTabTituloSelecionados.getColumnModel().getColumn(9).setPreferredWidth(30);
            jTabTituloSelecionados.getColumnModel().getColumn(10).setResizable(false);
            jTabTituloSelecionados.getColumnModel().getColumn(10).setPreferredWidth(30);
            jTabTituloSelecionados.getColumnModel().getColumn(11).setResizable(false);
            jTabTituloSelecionados.getColumnModel().getColumn(11).setPreferredWidth(20);
            jTabTituloSelecionados.getColumnModel().getColumn(12).setResizable(false);
            jTabTituloSelecionados.getColumnModel().getColumn(12).setPreferredWidth(20);
            jTabTituloSelecionados.getColumnModel().getColumn(13).setResizable(false);
        }

        javax.swing.GroupLayout jPanSelecionadosLayout = new javax.swing.GroupLayout(jPanSelecionados);
        jPanSelecionados.setLayout(jPanSelecionadosLayout);
        jPanSelecionadosLayout.setHorizontalGroup(
            jPanSelecionadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanSelecionadosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanSelecionadosLayout.setVerticalGroup(
            jPanSelecionadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanSelecionadosLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(5, 5, 5))
        );

        jPanTotaisSelecao.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder()));

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Agendamento:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabNumPreparacao.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabNumPreparacao.setText("Num. Preparação:");

        jLabDataLiquidacao.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabDataLiquidacao.setText("Período para Liquidação:");

        try {
            jForDataLiquidacaoDe.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jForDataLiquidacaoDe.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        try {
            jForDataLiquidacaoAte.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jForDataLiquidacaoAte.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel1.setText("/");

        jLabQtdeTitulos.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabQtdeTitulos.setText("Qtde. Títulos:");

        jLabValTotTitulos.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabValTotTitulos.setText("Val. Total:");

        jForValTotTitulos.setEditable(false);
        jForValTotTitulos.setEnabled(false);

        jForQtdeTitulos.setEditable(false);
        jForQtdeTitulos.setEnabled(false);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabDataLiquidacao)
                    .addComponent(jLabNumPreparacao))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jForNumPreparacao, javax.swing.GroupLayout.PREFERRED_SIZE, 138, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jForDataLiquidacaoDe, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 12, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForDataLiquidacaoAte, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabValTotTitulos)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForValTotTitulos, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addComponent(jLabQtdeTitulos)
                        .addGap(18, 18, 18)
                        .addComponent(jForQtdeTitulos, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabQtdeTitulos)
                            .addComponent(jForQtdeTitulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jForValTotTitulos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabValTotTitulos)))
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabNumPreparacao)
                            .addComponent(jForNumPreparacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabDataLiquidacao)
                            .addComponent(jForDataLiquidacaoDe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForDataLiquidacaoAte, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jButSalvarPreparacao.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButSalvarPreparacao.setText("Salvar Preparação");
        jButSalvarPreparacao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButSalvarPreparacaoActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanTotaisSelecaoLayout = new javax.swing.GroupLayout(jPanTotaisSelecao);
        jPanTotaisSelecao.setLayout(jPanTotaisSelecaoLayout);
        jPanTotaisSelecaoLayout.setHorizontalGroup(
            jPanTotaisSelecaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTotaisSelecaoLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButSalvarPreparacao)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanTotaisSelecaoLayout.setVerticalGroup(
            jPanTotaisSelecaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTotaisSelecaoLayout.createSequentialGroup()
                .addGroup(jPanTotaisSelecaoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanTotaisSelecaoLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanTotaisSelecaoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButSalvarPreparacao)))
                .addGap(5, 5, 5))
        );

        jPanPagamentosAgendados.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Pagamentos Agendados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jTabPagamentosAgendados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Preparacao", "Liquidacao De", "Liquidacao Ate", "Portador", "Tp. Pgto", "Tipo", "Valor", "Qtd Tit.", "Situacao"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(jTabPagamentosAgendados);
        if (jTabPagamentosAgendados.getColumnModel().getColumnCount() > 0) {
            jTabPagamentosAgendados.getColumnModel().getColumn(0).setResizable(false);
            jTabPagamentosAgendados.getColumnModel().getColumn(0).setPreferredWidth(50);
            jTabPagamentosAgendados.getColumnModel().getColumn(1).setResizable(false);
            jTabPagamentosAgendados.getColumnModel().getColumn(1).setPreferredWidth(60);
            jTabPagamentosAgendados.getColumnModel().getColumn(2).setResizable(false);
            jTabPagamentosAgendados.getColumnModel().getColumn(2).setPreferredWidth(60);
            jTabPagamentosAgendados.getColumnModel().getColumn(3).setResizable(false);
            jTabPagamentosAgendados.getColumnModel().getColumn(3).setPreferredWidth(20);
            jTabPagamentosAgendados.getColumnModel().getColumn(4).setResizable(false);
            jTabPagamentosAgendados.getColumnModel().getColumn(4).setPreferredWidth(20);
            jTabPagamentosAgendados.getColumnModel().getColumn(5).setResizable(false);
            jTabPagamentosAgendados.getColumnModel().getColumn(5).setPreferredWidth(20);
            jTabPagamentosAgendados.getColumnModel().getColumn(6).setResizable(false);
            jTabPagamentosAgendados.getColumnModel().getColumn(6).setPreferredWidth(30);
            jTabPagamentosAgendados.getColumnModel().getColumn(7).setResizable(false);
            jTabPagamentosAgendados.getColumnModel().getColumn(7).setPreferredWidth(20);
            jTabPagamentosAgendados.getColumnModel().getColumn(8).setResizable(false);
            jTabPagamentosAgendados.getColumnModel().getColumn(8).setPreferredWidth(30);
        }

        jPanInfoPortador.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Informação do Portador", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabInfoPortador.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabInfoPortador.setText("Portador:");

        jLabInfoTipoPagamento.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabInfoTipoPagamento.setText("Tipo Pagamento:");

        jLabInfoTipoMovimento.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabInfoTipoMovimento.setText("Tipo de Movimento:");

        jTexCdInfoPortador.setEditable(false);
        jTexCdInfoPortador.setEnabled(false);
        jTexCdInfoPortador.setOpaque(false);

        jTexNomeInfoPortador.setEditable(false);
        jTexNomeInfoPortador.setEnabled(false);
        jTexNomeInfoPortador.setOpaque(false);

        jTexCdInfoTipoPagamento.setEditable(false);
        jTexCdInfoTipoPagamento.setEnabled(false);
        jTexCdInfoTipoPagamento.setOpaque(false);

        jTexNomeInfoTipoPagamento.setEditable(false);
        jTexNomeInfoTipoPagamento.setEnabled(false);
        jTexNomeInfoTipoPagamento.setOpaque(false);

        jTexCdInfoTipoMovimento.setEditable(false);
        jTexCdInfoTipoMovimento.setEnabled(false);
        jTexCdInfoTipoMovimento.setOpaque(false);

        jTexNomeInfoTipoMovimento.setEditable(false);
        jTexNomeInfoTipoMovimento.setEnabled(false);
        jTexNomeInfoTipoMovimento.setOpaque(false);

        javax.swing.GroupLayout jPanInfoPortadorLayout = new javax.swing.GroupLayout(jPanInfoPortador);
        jPanInfoPortador.setLayout(jPanInfoPortadorLayout);
        jPanInfoPortadorLayout.setHorizontalGroup(
            jPanInfoPortadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInfoPortadorLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanInfoPortadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabInfoPortador, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabInfoTipoPagamento, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabInfoTipoMovimento, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanInfoPortadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTexCdInfoTipoPagamento, javax.swing.GroupLayout.DEFAULT_SIZE, 42, Short.MAX_VALUE)
                    .addComponent(jTexCdInfoPortador)
                    .addComponent(jTexCdInfoTipoMovimento))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanInfoPortadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jTexNomeInfoTipoPagamento, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                    .addComponent(jTexNomeInfoPortador)
                    .addComponent(jTexNomeInfoTipoMovimento))
                .addContainerGap(47, Short.MAX_VALUE))
        );
        jPanInfoPortadorLayout.setVerticalGroup(
            jPanInfoPortadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanInfoPortadorLayout.createSequentialGroup()
                .addGroup(jPanInfoPortadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabInfoPortador)
                    .addComponent(jTexCdInfoPortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeInfoPortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanInfoPortadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabInfoTipoPagamento)
                    .addComponent(jTexCdInfoTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeInfoTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanInfoPortadorLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabInfoTipoMovimento)
                    .addComponent(jTexCdInfoTipoMovimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeInfoTipoMovimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(4, 4, 4))
        );

        jPanTitulosAgendados.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Títulos", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jTabTitulosAgendados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Título", "Parc.", "Valor", "Vencimento", "Situação"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.Double.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(jTabTitulosAgendados);
        if (jTabTitulosAgendados.getColumnModel().getColumnCount() > 0) {
            jTabTitulosAgendados.getColumnModel().getColumn(0).setResizable(false);
            jTabTitulosAgendados.getColumnModel().getColumn(0).setPreferredWidth(40);
            jTabTitulosAgendados.getColumnModel().getColumn(1).setResizable(false);
            jTabTitulosAgendados.getColumnModel().getColumn(1).setPreferredWidth(5);
            jTabTitulosAgendados.getColumnModel().getColumn(2).setResizable(false);
            jTabTitulosAgendados.getColumnModel().getColumn(2).setPreferredWidth(30);
            jTabTitulosAgendados.getColumnModel().getColumn(3).setResizable(false);
            jTabTitulosAgendados.getColumnModel().getColumn(3).setPreferredWidth(30);
            jTabTitulosAgendados.getColumnModel().getColumn(4).setResizable(false);
            jTabTitulosAgendados.getColumnModel().getColumn(4).setPreferredWidth(20);
        }

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel2.setText("Nome/Razão Social:");

        jLabDocumento.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabDocumento.setText("Documento:");

        jTexNomeRazaoSocial.setEditable(false);
        jTexNomeRazaoSocial.setEnabled(false);

        jTexDocumento.setEditable(false);
        jTexDocumento.setEnabled(false);

        jLabel3.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel3.setText("Tipo:");

        jTexTipoDocumento.setEditable(false);
        jTexTipoDocumento.setEnabled(false);

        jLabel6.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel6.setText("Emissão:");

        jForEmissaoTitulo.setEditable(false);
        try {
            jForEmissaoTitulo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        jForEmissaoTitulo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);
        jForEmissaoTitulo.setEnabled(false);

        jButLiquidar.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButLiquidar.setText("Liquidar");
        jButLiquidar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButLiquidarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanTitulosAgendadosLayout = new javax.swing.GroupLayout(jPanTitulosAgendados);
        jPanTitulosAgendados.setLayout(jPanTitulosAgendadosLayout);
        jPanTitulosAgendadosLayout.setHorizontalGroup(
            jPanTitulosAgendadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTitulosAgendadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanTitulosAgendadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanTitulosAgendadosLayout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexNomeRazaoSocial))
                    .addGroup(jPanTitulosAgendadosLayout.createSequentialGroup()
                        .addGroup(jPanTitulosAgendadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanTitulosAgendadosLayout.createSequentialGroup()
                                .addComponent(jLabDocumento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForEmissaoTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButLiquidar))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanTitulosAgendadosLayout.setVerticalGroup(
            jPanTitulosAgendadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTitulosAgendadosLayout.createSequentialGroup()
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTitulosAgendadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jTexNomeRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTitulosAgendadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabDocumento)
                    .addComponent(jTexDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTexTipoDocumento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel6)
                    .addComponent(jForEmissaoTitulo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButLiquidar)
                .addContainerGap())
        );

        jButImprimirPrep.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButImprimirPrep.setText("Imprimir");
        jButImprimirPrep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButImprimirPrepActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Boleto/CNAB", javax.swing.border.TitledBorder.CENTER, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jButBoleto.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButBoleto.setText("Boleto");
        jButBoleto.setEnabled(false);
        jButBoleto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButBoletoActionPerformed(evt);
            }
        });

        jButArquivoCnab.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButArquivoCnab.setText("CNAB");
        jButArquivoCnab.setEnabled(false);
        jButArquivoCnab.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButArquivoCnabActionPerformed(evt);
            }
        });

        jButRetornoCNAB.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButRetornoCNAB.setText("Retorno");
        jButRetornoCNAB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButRetornoCNABActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButRetornoCNAB))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButArquivoCnab, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jButBoleto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(24, 24, 24))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jButBoleto)
                .addGap(2, 2, 2)
                .addComponent(jButArquivoCnab)
                .addGap(2, 2, 2)
                .addComponent(jButRetornoCNAB)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanPagamentosAgendadosLayout = new javax.swing.GroupLayout(jPanPagamentosAgendados);
        jPanPagamentosAgendados.setLayout(jPanPagamentosAgendadosLayout);
        jPanPagamentosAgendadosLayout.setHorizontalGroup(
            jPanPagamentosAgendadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPagamentosAgendadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanPagamentosAgendadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 710, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanPagamentosAgendadosLayout.createSequentialGroup()
                        .addComponent(jPanInfoPortador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButImprimirPrep)
                        .addGap(18, 18, 18)
                        .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addComponent(jPanTitulosAgendados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanPagamentosAgendadosLayout.setVerticalGroup(
            jPanPagamentosAgendadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPagamentosAgendadosLayout.createSequentialGroup()
                .addGroup(jPanPagamentosAgendadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanPagamentosAgendadosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addGroup(jPanPagamentosAgendadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanInfoPortador, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(jPanPagamentosAgendadosLayout.createSequentialGroup()
                                .addGap(9, 9, 9)
                                .addComponent(jButImprimirPrep))
                            .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
                    .addComponent(jPanTitulosAgendados, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanPagamentosAgendados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanSelecionados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPanTotaisSelecao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(5, 5, 5)
                .addComponent(jPanSelecionados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanTotaisSelecao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanPagamentosAgendados, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbPrincipal.addTab("Preparados", jPanel3);

        jLabArquivoRetorno.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabArquivoRetorno.setText("Arquivo retorno:");

        jTexArquivoRetorno.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexArquivoRetornoKeyPressed(evt);
            }
        });

        jButArquivo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButArquivo.setText("arquivo");
        jButArquivo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButArquivoActionPerformed(evt);
            }
        });

        jPanRetornoDeArquivo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Retorno Bancário", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabCodigoMovimento.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabCodigoMovimento.setText("Código/Movimento:");

        jLabTipoServico.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabTipoServico.setText("Tipo Serviço:");

        jLabDataArquivo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabDataArquivo.setText("Data do Arquivo:");

        try {
            jForDataArquivo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabNomeEmpresa.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabNomeEmpresa.setText("Empresa:");

        jLabCdBanco.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabCdBanco.setText("Banco:");

        jLabAgenciaConta.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabAgenciaConta.setText("Agência/Conta:");

        jLabContaDigito.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabContaDigito.setText("-");

        jLabDataCreditoArquivo.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabDataCreditoArquivo.setText("Data Crédito:");

        try {
            jForDataCreditoArquivo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabContaDigito1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabContaDigito1.setText("/");

        jTabDetalheCNAB.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Nosso Num.", "DAC", "Carteira", "Cod.Cart.", "Dt.Ocorr.", "Doc", "Esp.Doc", "Num.Banco", "Dt.Vencimento", "Valor Titulo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane6.setViewportView(jTabDetalheCNAB);
        if (jTabDetalheCNAB.getColumnModel().getColumnCount() > 0) {
            jTabDetalheCNAB.getColumnModel().getColumn(0).setResizable(false);
            jTabDetalheCNAB.getColumnModel().getColumn(0).setPreferredWidth(30);
            jTabDetalheCNAB.getColumnModel().getColumn(1).setResizable(false);
            jTabDetalheCNAB.getColumnModel().getColumn(1).setPreferredWidth(10);
            jTabDetalheCNAB.getColumnModel().getColumn(2).setResizable(false);
            jTabDetalheCNAB.getColumnModel().getColumn(2).setPreferredWidth(10);
            jTabDetalheCNAB.getColumnModel().getColumn(3).setResizable(false);
            jTabDetalheCNAB.getColumnModel().getColumn(3).setPreferredWidth(10);
            jTabDetalheCNAB.getColumnModel().getColumn(4).setResizable(false);
            jTabDetalheCNAB.getColumnModel().getColumn(4).setPreferredWidth(30);
            jTabDetalheCNAB.getColumnModel().getColumn(5).setResizable(false);
            jTabDetalheCNAB.getColumnModel().getColumn(5).setPreferredWidth(30);
            jTabDetalheCNAB.getColumnModel().getColumn(6).setResizable(false);
            jTabDetalheCNAB.getColumnModel().getColumn(6).setPreferredWidth(20);
            jTabDetalheCNAB.getColumnModel().getColumn(7).setResizable(false);
            jTabDetalheCNAB.getColumnModel().getColumn(7).setPreferredWidth(30);
            jTabDetalheCNAB.getColumnModel().getColumn(8).setResizable(false);
            jTabDetalheCNAB.getColumnModel().getColumn(8).setPreferredWidth(30);
            jTabDetalheCNAB.getColumnModel().getColumn(9).setResizable(false);
            jTabDetalheCNAB.getColumnModel().getColumn(9).setPreferredWidth(60);
        }

        jPanDetalheArquivo.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Registro Detalhe:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabValorDespCobr.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabValorDespCobr.setText("Desp. Cobrança (R$):");

        jLabValorAbatConced.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabValorAbatConced.setText("Abat. Concedido (R$):");

        jLabValorDescConced.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabValorDescConced.setText("Desc. Concedido (R$):");

        jLabValorLiqEmConta.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabValorLiqEmConta.setText("Líquido em conta (R$):");

        jLabValorJurosMoraMulta.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabValorJurosMoraMulta.setText("Juro Mora/Multa (R$):");

        jLabValorOutrosCred.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabValorOutrosCred.setText("Outros Créditos (R$):");

        jForValorDespCobranca.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jForValorAbatConcedido.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jForValorDescConcedido.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jForValorLiqEmConta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jForValorJurosMoraMulta.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jForValorOutrosCred.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jLabValorIOF.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabValorIOF.setText("IOF (R$):");

        jForValorIOF.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jSeparator2.setBorder(javax.swing.BorderFactory.createEtchedBorder(java.awt.Color.gray, null));

        jLabCdOcorrencia.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabCdOcorrencia.setText("Cod. Ocorr.:");

        jLabCdIntrCancel.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabCdIntrCancel.setText("Cod. Intr. Canc.:");

        jLabPagador.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabPagador.setText("Pagador:");

        jLabCdErroMensInfor.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabCdErroMensInfor.setText("Cod.Erro/Msg Inf.:");

        jLabCdLiquidacao.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabCdLiquidacao.setText("Cod. Liq.:");

        jLabDataCredLiquid.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabDataCredLiquid.setText("Dt Créd. Liq.");

        jLabSeqRegistro.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabSeqRegistro.setText("Seq. Registro:");

        javax.swing.GroupLayout jPanDetalheArquivoLayout = new javax.swing.GroupLayout(jPanDetalheArquivo);
        jPanDetalheArquivo.setLayout(jPanDetalheArquivoLayout);
        jPanDetalheArquivoLayout.setHorizontalGroup(
            jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanDetalheArquivoLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabValorDespCobr)
                    .addComponent(jLabValorAbatConced)
                    .addComponent(jLabValorDescConced)
                    .addComponent(jLabValorLiqEmConta)
                    .addComponent(jLabValorJurosMoraMulta)
                    .addComponent(jLabValorOutrosCred))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jForValorJurosMoraMulta, javax.swing.GroupLayout.DEFAULT_SIZE, 110, Short.MAX_VALUE)
                        .addComponent(jForValorLiqEmConta)
                        .addComponent(jForValorOutrosCred))
                    .addGroup(jPanDetalheArquivoLayout.createSequentialGroup()
                        .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jForValorAbatConcedido, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                            .addComponent(jForValorDespCobranca, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jForValorDescConcedido, javax.swing.GroupLayout.Alignment.LEADING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabValorIOF)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jForValorIOF)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabCdErroMensInfor, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabPagador, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabCdIntrCancel, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabCdOcorrencia, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabCdLiquidacao, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabDataCredLiquid, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanDetalheArquivoLayout.createSequentialGroup()
                        .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jTexCdErroMensInformacao, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTexCdLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexNomeLiquidacao))
                    .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jTexNomeErroMensInfor, javax.swing.GroupLayout.PREFERRED_SIZE, 389, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jTexPagador, javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanDetalheArquivoLayout.createSequentialGroup()
                                .addComponent(jTexCdIntrCancelamento, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexNomeIntrCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanDetalheArquivoLayout.createSequentialGroup()
                                .addComponent(jTexCdOcorrencia, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexNomeOcorrencia, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanDetalheArquivoLayout.createSequentialGroup()
                                .addComponent(jForDataCredLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(127, 127, 127)
                                .addComponent(jLabSeqRegistro)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexSeqRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap())
        );
        jPanDetalheArquivoLayout.setVerticalGroup(
            jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanDetalheArquivoLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanDetalheArquivoLayout.createSequentialGroup()
                        .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabValorDespCobr, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jForValorDespCobranca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabValorIOF)
                            .addComponent(jForValorIOF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabValorAbatConced, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jForValorAbatConcedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabValorDescConced, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jForValorDescConcedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabValorLiqEmConta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jForValorLiqEmConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabValorJurosMoraMulta, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jForValorJurosMoraMulta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabValorOutrosCred, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jForValorOutrosCred, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanDetalheArquivoLayout.createSequentialGroup()
                        .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabCdOcorrencia)
                            .addComponent(jTexCdOcorrencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTexNomeOcorrencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabCdIntrCancel)
                            .addComponent(jTexCdIntrCancelamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTexNomeIntrCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabPagador)
                            .addComponent(jTexPagador, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabCdErroMensInfor)
                            .addComponent(jTexCdErroMensInformacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTexNomeErroMensInfor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabCdLiquidacao)
                            .addComponent(jTexCdLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTexNomeLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(2, 2, 2)
                        .addGroup(jPanDetalheArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabDataCredLiquid)
                            .addComponent(jForDataCredLiquidacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabSeqRegistro)
                            .addComponent(jTexSeqRegistro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(2, 2, 2))
            .addGroup(jPanDetalheArquivoLayout.createSequentialGroup()
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Registro Trailer", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabQtdTitulosCobSimples.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabQtdTitulosCobSimples.setText("Qt. Tit. Cob. Simples:");

        jLabValorTituloCobSimples.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabValorTituloCobSimples.setText("Val. Tit. Cob. Simples:");

        jLabAvisoBcoCobSimples.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabAvisoBcoCobSimples.setText("Ref.aviso Bco:");

        jLabQtdTituloCobVinculada.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabQtdTituloCobVinculada.setText("Qt.Tit.Cob. Vinculada:");

        jLabValorTituloCobVinculada.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabValorTituloCobVinculada.setText("Val.Tit.Cob.Vinculada:");

        jLabAvisoBcoCobVinculada.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabAvisoBcoCobVinculada.setText("Ref.aviso Bco:");

        jLabQtdTituloCobDirEscriturada.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabQtdTituloCobDirEscriturada.setText("Qt.Tit.Cob.Dir.Escrit:");

        jLabValTituloCobDirEscriturada.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabValTituloCobDirEscriturada.setText("Val.Tit.Cob.Dir.Escrit:");

        jLabAvisoBcoCobDirEscriturada.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabAvisoBcoCobDirEscriturada.setText("Ref.aviso Bco:");

        jLabValorTotalInformado.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabValorTotalInformado.setText("Vl. Tot. Infor.:");

        jLabQtdDetalhes.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabQtdDetalhes.setText("Qt. detalhes:");

        jForValorTituloCobSimples.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jForValorTituloCobVinculada.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jForValorTotalInformado.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jButLiquidarViaEdi.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButLiquidarViaEdi.setText("Liquidar");
        jButLiquidarViaEdi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButLiquidarViaEdiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabAvisoBcoCobSimples, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabValorTituloCobSimples, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabQtdTitulosCobSimples, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jForValorTituloCobSimples)
                            .addComponent(jTexQtdTitulosCobSimples, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jTexAvisoBcoCobSimples, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabValorTituloCobVinculada)
                            .addComponent(jLabAvisoBcoCobVinculada)
                            .addComponent(jLabQtdTituloCobVinculada))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jForValorTituloCobVinculada)
                            .addComponent(jTexAvisoBcoCobVinculada, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                            .addComponent(jTexQtdTituloCobVinculada, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabValTituloCobDirEscriturada)
                            .addComponent(jLabQtdTituloCobDirEscriturada)
                            .addComponent(jLabAvisoBcoCobDirEscriturada))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jTexAvisoBcoCobDirEscriturada, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
                                    .addComponent(jForValTituloCobDirEscriturada))
                                .addGap(33, 33, 33)
                                .addComponent(jLabQtdDetalhes)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexQtdDetalhes))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addComponent(jTexQtdTituloCobDirEscriturada, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabValorTotalInformado)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForValorTotalInformado, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jButLiquidarViaEdi))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabQtdTituloCobDirEscriturada)
                                    .addComponent(jTexQtdTituloCobDirEscriturada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabValorTotalInformado)
                                    .addComponent(jForValorTotalInformado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(2, 2, 2)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabValTituloCobDirEscriturada)
                                    .addComponent(jLabQtdDetalhes)
                                    .addComponent(jTexQtdDetalhes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jForValTituloCobDirEscriturada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(2, 2, 2)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabAvisoBcoCobDirEscriturada)
                                    .addComponent(jTexAvisoBcoCobDirEscriturada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel7Layout.createSequentialGroup()
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabQtdTitulosCobSimples)
                                    .addComponent(jTexQtdTitulosCobSimples, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabQtdTituloCobVinculada)
                                    .addComponent(jTexQtdTituloCobVinculada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(2, 2, 2)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabValorTituloCobSimples)
                                    .addComponent(jForValorTituloCobSimples, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabValorTituloCobVinculada)
                                    .addComponent(jForValorTituloCobVinculada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(2, 2, 2)
                                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabAvisoBcoCobSimples)
                                    .addComponent(jTexAvisoBcoCobSimples, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabAvisoBcoCobVinculada)
                        .addComponent(jTexAvisoBcoCobVinculada, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(2, 2, 2)
                .addComponent(jButLiquidarViaEdi)
                .addContainerGap())
        );

        jPanTituloVinculado.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Título Vinculado", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabCdLancamentoVinc.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabCdLancamentoVinc.setText("Lcto.:");

        jLabDataEmissaVinc.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabDataEmissaVinc.setText("Emissão:");

        jLabDataVenctoVinc.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabDataVenctoVinc.setText("Vcto:");

        jLabValorVinc.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabValorVinc.setText("Valor:");

        jLabSaldoVinc.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabSaldoVinc.setText("Saldo:");

        jLabCpfCnpjVinc.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabCpfCnpjVinc.setText("Cliente:");

        jTexCdLancamentoVinc.setEnabled(false);

        jForDataEmissaVinc.setEnabled(false);

        jForDataVenctoVinc.setEnabled(false);

        jForValorVinc.setEnabled(false);

        jForSaldoVinc.setEnabled(false);

        jForCpfCnpjVinc.setEnabled(false);

        jTexNomeClienteVinc.setEnabled(false);

        javax.swing.GroupLayout jPanTituloVinculadoLayout = new javax.swing.GroupLayout(jPanTituloVinculado);
        jPanTituloVinculado.setLayout(jPanTituloVinculadoLayout);
        jPanTituloVinculadoLayout.setHorizontalGroup(
            jPanTituloVinculadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTituloVinculadoLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanTituloVinculadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabValorVinc)
                    .addComponent(jLabDataEmissaVinc)
                    .addComponent(jLabCdLancamentoVinc)
                    .addComponent(jLabCpfCnpjVinc))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanTituloVinculadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTexNomeClienteVinc)
                    .addGroup(jPanTituloVinculadoLayout.createSequentialGroup()
                        .addGroup(jPanTituloVinculadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jForCpfCnpjVinc, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanTituloVinculadoLayout.createSequentialGroup()
                                .addGroup(jPanTituloVinculadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jForDataEmissaVinc, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jForValorVinc, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanTituloVinculadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanTituloVinculadoLayout.createSequentialGroup()
                                        .addGap(18, 18, 18)
                                        .addComponent(jLabSaldoVinc)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jForSaldoVinc, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanTituloVinculadoLayout.createSequentialGroup()
                                        .addGap(23, 23, 23)
                                        .addComponent(jLabDataVenctoVinc)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jForDataVenctoVinc, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(jTexCdLancamentoVinc, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 11, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanTituloVinculadoLayout.setVerticalGroup(
            jPanTituloVinculadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanTituloVinculadoLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanTituloVinculadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCdLancamentoVinc)
                    .addComponent(jTexCdLancamentoVinc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanTituloVinculadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabDataEmissaVinc)
                    .addComponent(jLabDataVenctoVinc)
                    .addComponent(jForDataEmissaVinc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jForDataVenctoVinc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanTituloVinculadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabValorVinc)
                    .addComponent(jLabSaldoVinc)
                    .addComponent(jForValorVinc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jForSaldoVinc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanTituloVinculadoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCpfCnpjVinc)
                    .addComponent(jForCpfCnpjVinc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addComponent(jTexNomeClienteVinc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanOcorrencias.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Mensagens do Arquivo", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 13))); // NOI18N

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel4.setText("Ocorrência.:");

        jTextAreaOcorrencia.setColumns(20);
        jTextAreaOcorrencia.setLineWrap(true);
        jTextAreaOcorrencia.setRows(3);
        jTextAreaOcorrencia.setEnabled(false);
        jScrollPane7.setViewportView(jTextAreaOcorrencia);

        jTextArea2.setColumns(20);
        jTextArea2.setLineWrap(true);
        jTextArea2.setRows(3);
        jTextArea2.setEnabled(false);
        jScrollPane8.setViewportView(jTextArea2);

        jLabel7.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel7.setText("Int.Cancel.:");

        jLabel8.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel8.setText("Erro/Mensag.:");

        jTextArea3.setColumns(20);
        jTextArea3.setLineWrap(true);
        jTextArea3.setRows(3);
        jTextArea3.setEnabled(false);
        jScrollPane9.setViewportView(jTextArea3);

        jTextArea4.setColumns(20);
        jTextArea4.setLineWrap(true);
        jTextArea4.setRows(3);
        jTextArea4.setEnabled(false);
        jScrollPane10.setViewportView(jTextArea4);

        jLabel9.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jLabel9.setText("Liquidação:");

        javax.swing.GroupLayout jPanOcorrenciasLayout = new javax.swing.GroupLayout(jPanOcorrencias);
        jPanOcorrencias.setLayout(jPanOcorrenciasLayout);
        jPanOcorrenciasLayout.setHorizontalGroup(
            jPanOcorrenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanOcorrenciasLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanOcorrenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanOcorrenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9)
                    .addComponent(jScrollPane7)
                    .addComponent(jScrollPane8)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        jPanOcorrenciasLayout.setVerticalGroup(
            jPanOcorrenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanOcorrenciasLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addGroup(jPanOcorrenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel4)
                    .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanOcorrenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanOcorrenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanOcorrenciasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane10, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanRetornoDeArquivoLayout = new javax.swing.GroupLayout(jPanRetornoDeArquivo);
        jPanRetornoDeArquivo.setLayout(jPanRetornoDeArquivoLayout);
        jPanRetornoDeArquivoLayout.setHorizontalGroup(
            jPanRetornoDeArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanRetornoDeArquivoLayout.createSequentialGroup()
                .addGroup(jPanRetornoDeArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanRetornoDeArquivoLayout.createSequentialGroup()
                        .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 961, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanTituloVinculado, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanRetornoDeArquivoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanRetornoDeArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanRetornoDeArquivoLayout.createSequentialGroup()
                                .addComponent(jLabCodigoMovimento)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexCdMovimento, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexNomeMovimento, javax.swing.GroupLayout.PREFERRED_SIZE, 295, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabTipoServico)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexCdTipoServico, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexNomeTipoServico, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabDataArquivo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForDataArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanRetornoDeArquivoLayout.createSequentialGroup()
                                .addComponent(jLabNomeEmpresa)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexNomeEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabCdBanco)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexCdBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexNomeBanco, javax.swing.GroupLayout.PREFERRED_SIZE, 230, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabAgenciaConta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexCdAgencia, javax.swing.GroupLayout.PREFERRED_SIZE, 55, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabContaDigito1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexCdConta, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabContaDigito)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexCdDigConta, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(jLabDataCreditoArquivo)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jForDataCreditoArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(0, 0, 0))
                    .addGroup(jPanRetornoDeArquivoLayout.createSequentialGroup()
                        .addGroup(jPanRetornoDeArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jPanDetalheArquivo, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jPanel7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanOcorrencias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap())
        );
        jPanRetornoDeArquivoLayout.setVerticalGroup(
            jPanRetornoDeArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanRetornoDeArquivoLayout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanRetornoDeArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCodigoMovimento)
                    .addComponent(jTexCdMovimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeMovimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabTipoServico)
                    .addComponent(jTexCdTipoServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeTipoServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabDataArquivo)
                    .addComponent(jForDataArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(2, 2, 2)
                .addGroup(jPanRetornoDeArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabNomeEmpresa)
                    .addComponent(jTexNomeEmpresa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabCdBanco)
                    .addComponent(jTexCdBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeBanco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabAgenciaConta)
                    .addComponent(jTexCdAgencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexCdConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabContaDigito)
                    .addComponent(jTexCdDigConta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabDataCreditoArquivo)
                    .addComponent(jForDataCreditoArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabContaDigito1))
                .addGap(1, 1, 1)
                .addGroup(jPanRetornoDeArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanTituloVinculado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1)
                .addGroup(jPanRetornoDeArquivoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanRetornoDeArquivoLayout.createSequentialGroup()
                        .addComponent(jPanDetalheArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(4, 4, 4)
                        .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(jPanOcorrencias, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        jButton1.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
        jButton1.setText("Processar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanRetornoDeArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(jLabArquivoRetorno)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexArquivoRetorno, javax.swing.GroupLayout.PREFERRED_SIZE, 608, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButArquivo)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(2, 2, 2)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabArquivoRetorno)
                    .addComponent(jTexArquivoRetorno, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButArquivo)
                    .addComponent(jButton1))
                .addGap(2, 2, 2)
                .addComponent(jPanRetornoDeArquivo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jTabbPrincipal.addTab("CNAB", jPanel6);

        javax.swing.GroupLayout jPanGeralLayout = new javax.swing.GroupLayout(jPanGeral);
        jPanGeral.setLayout(jPanGeralLayout);
        jPanGeralLayout.setHorizontalGroup(
            jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanGeralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanGeralLayout.setVerticalGroup(
            jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanGeralLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jTabbPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, 663, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jMenuBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jMenu1.setText("Arquivo");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setText("Novo");
        jMenu1.add(jMenuItem1);

        jMenuItemSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSalvar.setText("Salvar");
        jMenu1.add(jMenuItemSalvar);

        jMenuItemSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemSair.setText("Sair");
        jMenu1.add(jMenuItemSair);

        jMenuBar.add(jMenu1);

        jMenu2.setText("Editar");

        jMenuItemEditar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItemEditar.setText("Editar");
        jMenu2.add(jMenuItemEditar);

        jMenuBar.add(jMenu2);

        setJMenuBar(jMenuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanGeral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanGeral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jTabTituloSelecionadosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabTituloSelecionadosMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jTabTituloSelecionadosMouseClicked

    private void jButMarcarDesmarcarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButMarcarDesmarcarActionPerformed
        // TODO add your handling code here:
        try {
            marcarDesmarcarTitulos();
            verificaBotaoMarcarTodos();
            listarSelecionados();
            ctTitulos.alinharTabela(jTabTitulo, 9, 12, 13);
            ctTitulos.ajustarTabela(jTabTitulo, 5, 30, 5, 5, 5, 30, 200, 20, 5, 20, 30, 30, 20, 20, 20);
        } catch (SQLException ex) {
            Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButMarcarDesmarcarActionPerformed

    private void jButSelecionarMarcadosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSelecionarMarcadosActionPerformed
        try {
            selecionarTitulosMarcados();
            ctTitulos.alinharTabela(jTabTituloSelecionados, 9, 12, 13);
            ctTitulos.ajustarTabela(jTabTituloSelecionados, 5, 30, 5, 5, 5, 30, 200, 20, 5, 20, 30, 30, 20, 20, 20);
            jTabbPrincipal.setSelectedIndex(1);
            jForQtdeTitulos.setText(String.valueOf(contIntervTit));
            jForValTotTitulos.setText(String.valueOf(valTotalSelec));
            jTexCdInfoPortador.setText(String.format("%s", jTabPortadores.getValueAt(linhaPortadores, 1)));
            jTexNomeInfoPortador.setText(String.format("%s", jTabPortadores.getValueAt(linhaPortadores, 2)));
            jTexCdInfoTipoPagamento.setText(String.format("%s", jTabPortadores.getValueAt(linhaPortadores, 3)));
            jTexNomeInfoTipoPagamento.setText(String.format("%s", jTabPortadores.getValueAt(linhaPortadores, 4)));
            jTexCdInfoTipoMovimento.setText(String.format("%s", jTabPortadores.getValueAt(linhaPortadores, 0)).substring(0, 2));
            jTexNomeInfoTipoMovimento.setText(String.format("%s", jTabPortadores.getValueAt(linhaPortadores, 0)));
            jForDataLiquidacaoDe.setText(dat.getDataConv(Date.valueOf(peridoLiquDe)));
            jForDataLiquidacaoAte.setText(dat.getDataConv(Date.valueOf(peridoLiquAte)));
        } catch (SQLException ex) {
            Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButSelecionarMarcadosActionPerformed

    private void jTabTituloMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTabTituloMouseClicked
        VerificarMouse vm = new VerificarMouse();
        if ("SHIFT".equals(vm.botaoMouse(evt).toUpperCase())) {
            marcarDesmarcarLinha();
        }
    }//GEN-LAST:event_jTabTituloMouseClicked

    private void jButPrepararActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPrepararActionPerformed
        try {
            marcarDesmarcarTitulos();
            ctTitulos.alinharTabela(jTabTitulo, 9, 12, 13);
            ctTitulos.ajustarTabela(jTabTitulo, 5, 30, 5, 5, 5, 30, 200, 20, 5, 20, 30, 30, 20, 20, 20);
            jButMarcarDesmarcar.setEnabled(true);
            jButPreparar.setEnabled(false);
            verificaBotaoMarcarTodos();
            dat.setData("");
            peridoLiquDe = String.format("%s", jTabTitulo.getValueAt(linhaTitulos, 10));
            peridoLiquAte = String.format("%s", jTabTitulo.getValueAt(linhaTitulos, 10));
        } catch (SQLException ex) {
            Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButPrepararActionPerformed

    private void jButBuscarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButBuscarActionPerformed
        moverLinha = false;
        zerarTabelaTitulos(jTabTitulo);
        zerarTabelaPortadores();
        linhaPortadores = 0;
        linhaTitulos = 0;
        sqlTitulo = "select * from buscartitulos where Situacao between '" + situacaoIni
                + "' and '" + situacaoFin
                + "' and Tipo between '" + pagarReceberIni
                + "' and '" + pagarReceberFin
                + "' and Emissao between '" + dat.getDataConv(jForDatEmissaoIni.getText())
                + "' and '" + dat.getDataConv(jForDatEmissaoFin.getText())
                + "' and Vencimento between '" + dat.getDataConv(jForDatVencimentoIni.getText())
                + "' and '" + dat.getDataConv(jForDatVencimentoFin.getText())
                + "' and TpLanc between '" + tipoLancametoIni
                + "' and '" + tipoLancametoFin
                + "'";
        buscarTotalPortadores();
    }//GEN-LAST:event_jButBuscarActionPerformed

    private void jComTipoLancamentoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComTipoLancamentoItemStateChanged
        if ("Tod".equals(jComTipoLancamento.getSelectedItem().toString().substring(0, 3))) {
            tipoLancametoIni = "aaa";
            tipoLancametoFin = "ZZZ";
        } else {
            switch (jComTipoLancamento.getSelectedItem().toString().substring(0, 3)) {
                case "Tra":
                    tipoLancametoIni = "Trf";
                    tipoLancametoFin = "Trf";
                    break;
                default:
                    tipoLancametoIni = jComTipoLancamento.getSelectedItem().toString().substring(0, 3);
                    tipoLancametoFin = jComTipoLancamento.getSelectedItem().toString().substring(0, 3);
                    break;
            }
        }
    }//GEN-LAST:event_jComTipoLancamentoItemStateChanged

    private void jTexNomeClienteKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexNomeClienteKeyReleased

        buscarTotalPortadores();
    }//GEN-LAST:event_jTexNomeClienteKeyReleased

    private void jComPagarReceberItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComPagarReceberItemStateChanged
        if ("T".equals(jComPagarReceber.getSelectedItem().toString().substring(0, 1))) {
            pagarReceberIni = "aa";
            pagarReceberFin = "ZZ";
        } else {
            pagarReceberIni = jComPagarReceber.getSelectedItem().toString().substring(0, 2);
            pagarReceberFin = jComPagarReceber.getSelectedItem().toString().substring(0, 2);
        }
    }//GEN-LAST:event_jComPagarReceberItemStateChanged

    private void jComSituacaoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_jComSituacaoItemStateChanged
        if ("T".equals(jComSituacao.getSelectedItem().toString().substring(0, 1))) {
            situacaoIni = "AA";
            situacaoFin = "ZZ";
        } else {
            situacaoIni = jComSituacao.getSelectedItem().toString();
            situacaoFin = jComSituacao.getSelectedItem().toString();
        }
    }//GEN-LAST:event_jComSituacaoItemStateChanged

    private void jButSalvarPreparacaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarPreparacaoActionPerformed
        operAgendamento = "N";
        try {
            salvarPreparacao();
            zerarTabelaTitulos(jTabTituloSelecionados);
            jForNumPreparacao.setText("");
            jForQtdeTitulos.setText("");
            jForDataLiquidacaoDe.setText("");
            jForDataLiquidacaoAte.setText("");
            jForValTotTitulos.setText("");
            jButBuscarActionPerformed(evt);
        } catch (ParseException ex) {
            Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
        }
        operAgendamento = "A";
        buscarAgendamentos();
    }//GEN-LAST:event_jButSalvarPreparacaoActionPerformed

    private void jButLiquidarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButLiquidarActionPerformed
        liquidarTitulo(modlan.getCdLancamento());
        buscarTitulosAgendados();

    }//GEN-LAST:event_jButLiquidarActionPerformed

    private void jButImprimirPrepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButImprimirPrepActionPerformed
        imprimirPreparacao();
    }//GEN-LAST:event_jButImprimirPrepActionPerformed

    private void jButArquivoCnabActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButArquivoCnabActionPerformed
        try {
            gerarArquivoCNAB();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButArquivoCnabActionPerformed

    private void jButBoletoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButBoletoActionPerformed
        try {
            try {
                imprimirBoleto();
            } catch (ParseException ex) {
                Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JRException ex) {
            Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButBoletoActionPerformed

    private void jButRetornoCNABActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButRetornoCNABActionPerformed
        try {
            lerArquivoCNAB();
        } catch (IOException ex) {
            Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButRetornoCNABActionPerformed

    private void jTexArquivoRetornoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexArquivoRetornoKeyPressed
        VerificarTecla vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            buscarArquivoCNAB();
        }
    }//GEN-LAST:event_jTexArquivoRetornoKeyPressed

    private void jButArquivoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButArquivoActionPerformed
        linhaDetalheCNAB = 0;
        buscarArquivoCNAB();
    }//GEN-LAST:event_jButArquivoActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        try {
            lerArquivoCNAB();
        } catch (IOException ex) {
            Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception f) {
            mensagemTela("Erro na leitura do arquivo!\nErro: " + f);
        }
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButLiquidarViaEdiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButLiquidarViaEdiActionPerformed
        if (cb.isOcorrenciaLiquidaTitulo()) {
            try {
                liquidarTitulo(modlan.getCdLancamento(), formato.parse(jForValorLiqEmConta.getText()).doubleValue(), jForDataCredLiquidacao.getText().replace("/", "-"));
            } catch (ParseException ex) {
                Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_jButLiquidarViaEdiActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterPreparacaoPagamento.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterPreparacaoPagamento(conexao, su, pg).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButAnterior;
    private javax.swing.JButton jButArquivo;
    private javax.swing.JButton jButArquivoCnab;
    private javax.swing.JButton jButBoleto;
    private javax.swing.JButton jButBuscar;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButEditar;
    private javax.swing.JButton jButExcluir;
    private javax.swing.JButton jButImprimirPrep;
    private javax.swing.JButton jButLiquidar;
    private javax.swing.JButton jButLiquidarViaEdi;
    private javax.swing.JButton jButMarcarDesmarcar;
    private javax.swing.JButton jButNovo;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JButton jButPreparar;
    private javax.swing.JButton jButProximo;
    private javax.swing.JButton jButRetornoCNAB;
    private javax.swing.JButton jButSair;
    private javax.swing.JButton jButSalvar;
    private javax.swing.JButton jButSalvarPreparacao;
    private javax.swing.JButton jButSelecionarMarcados;
    private javax.swing.JButton jButton1;
    private javax.swing.JComboBox<String> jComPagarReceber;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JComboBox<String> jComTipoLancamento;
    private javax.swing.JFormattedTextField jForCpfCnpjVinc;
    private javax.swing.JFormattedTextField jForDatEmissaoFin;
    private javax.swing.JFormattedTextField jForDatEmissaoIni;
    private javax.swing.JFormattedTextField jForDatVencimentoFin;
    private javax.swing.JFormattedTextField jForDatVencimentoIni;
    private javax.swing.JFormattedTextField jForDataArquivo;
    private javax.swing.JFormattedTextField jForDataCredLiquidacao;
    private javax.swing.JFormattedTextField jForDataCreditoArquivo;
    private javax.swing.JFormattedTextField jForDataEmissaVinc;
    private javax.swing.JFormattedTextField jForDataLiquidacaoAte;
    private javax.swing.JFormattedTextField jForDataLiquidacaoDe;
    private javax.swing.JFormattedTextField jForDataVenctoVinc;
    private javax.swing.JFormattedTextField jForEmissaoTitulo;
    private javax.swing.JFormattedTextField jForNumPreparacao;
    private javax.swing.JFormattedTextField jForQtdeTitulos;
    private javax.swing.JFormattedTextField jForSaldoVinc;
    private javax.swing.JFormattedTextField jForTotTitPagar;
    private javax.swing.JFormattedTextField jForTotTitReceber;
    private javax.swing.JFormattedTextField jForValTituloCobDirEscriturada;
    private javax.swing.JFormattedTextField jForValTotTitulos;
    private javax.swing.JFormattedTextField jForValorAbatConcedido;
    private javax.swing.JFormattedTextField jForValorDescConcedido;
    private javax.swing.JFormattedTextField jForValorDespCobranca;
    private javax.swing.JFormattedTextField jForValorIOF;
    private javax.swing.JFormattedTextField jForValorJurosMoraMulta;
    private javax.swing.JFormattedTextField jForValorLiqEmConta;
    private javax.swing.JFormattedTextField jForValorOutrosCred;
    private javax.swing.JFormattedTextField jForValorTituloCobSimples;
    private javax.swing.JFormattedTextField jForValorTituloCobVinculada;
    private javax.swing.JFormattedTextField jForValorTotalInformado;
    private javax.swing.JFormattedTextField jForValorVinc;
    private javax.swing.JLabel jLabAgenciaConta;
    private javax.swing.JLabel jLabArquivoRetorno;
    private javax.swing.JLabel jLabAvisoBcoCobDirEscriturada;
    private javax.swing.JLabel jLabAvisoBcoCobSimples;
    private javax.swing.JLabel jLabAvisoBcoCobVinculada;
    private javax.swing.JLabel jLabCdBanco;
    private javax.swing.JLabel jLabCdErroMensInfor;
    private javax.swing.JLabel jLabCdIntrCancel;
    private javax.swing.JLabel jLabCdLancamentoVinc;
    private javax.swing.JLabel jLabCdLiquidacao;
    private javax.swing.JLabel jLabCdOcorrencia;
    private javax.swing.JLabel jLabCodigoMovimento;
    private javax.swing.JLabel jLabContaDigito;
    private javax.swing.JLabel jLabContaDigito1;
    private javax.swing.JLabel jLabCpfCnpjVinc;
    private javax.swing.JLabel jLabDataArquivo;
    private javax.swing.JLabel jLabDataCredLiquid;
    private javax.swing.JLabel jLabDataCreditoArquivo;
    private javax.swing.JLabel jLabDataEmissaVinc;
    private javax.swing.JLabel jLabDataEmissao;
    private javax.swing.JLabel jLabDataLiquidacao;
    private javax.swing.JLabel jLabDataVenctoVinc;
    private javax.swing.JLabel jLabDocumento;
    private javax.swing.JLabel jLabInfoPortador;
    private javax.swing.JLabel jLabInfoTipoMovimento;
    private javax.swing.JLabel jLabInfoTipoPagamento;
    private javax.swing.JLabel jLabNomeCliente;
    private javax.swing.JLabel jLabNomeEmpresa;
    private javax.swing.JLabel jLabNumPreparacao;
    private javax.swing.JLabel jLabPagador;
    private javax.swing.JLabel jLabPagarReceber;
    private javax.swing.JLabel jLabQtdDetalhes;
    private javax.swing.JLabel jLabQtdTituloCobDirEscriturada;
    private javax.swing.JLabel jLabQtdTituloCobVinculada;
    private javax.swing.JLabel jLabQtdTitulosCobSimples;
    private javax.swing.JLabel jLabQtdeTitulos;
    private javax.swing.JLabel jLabSaldoVinc;
    private javax.swing.JLabel jLabSeqRegistro;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabTipoLancamento;
    private javax.swing.JLabel jLabTipoServico;
    private javax.swing.JLabel jLabTotTitPagar;
    private javax.swing.JLabel jLabTotTitReceber;
    private javax.swing.JLabel jLabValTituloCobDirEscriturada;
    private javax.swing.JLabel jLabValTotTitulos;
    private javax.swing.JLabel jLabValorAbatConced;
    private javax.swing.JLabel jLabValorDescConced;
    private javax.swing.JLabel jLabValorDespCobr;
    private javax.swing.JLabel jLabValorIOF;
    private javax.swing.JLabel jLabValorJurosMoraMulta;
    private javax.swing.JLabel jLabValorLiqEmConta;
    private javax.swing.JLabel jLabValorOutrosCred;
    private javax.swing.JLabel jLabValorTituloCobSimples;
    private javax.swing.JLabel jLabValorTituloCobVinculada;
    private javax.swing.JLabel jLabValorTotalInformado;
    private javax.swing.JLabel jLabValorVinc;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JPanel jPanBotoesTitulo;
    private javax.swing.JPanel jPanCliente;
    private javax.swing.JPanel jPanDetalheArquivo;
    private javax.swing.JPanel jPanFiltros;
    private javax.swing.JPanel jPanGeral;
    private javax.swing.JPanel jPanInfoPortador;
    private javax.swing.JPanel jPanOcorrencias;
    private javax.swing.JPanel jPanPagamentosAgendados;
    private javax.swing.JPanel jPanRetornoDeArquivo;
    private javax.swing.JPanel jPanSelecionados;
    private javax.swing.JPanel jPanTituloVinculado;
    private javax.swing.JPanel jPanTitulos;
    private javax.swing.JPanel jPanTitulosAgendados;
    private javax.swing.JPanel jPanTotaisSelecao;
    private javax.swing.JPanel jPanTotaisTitulos;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JTable jTabDetalheCNAB;
    private javax.swing.JTable jTabPagamentosAgendados;
    private javax.swing.JTable jTabPortadores;
    private javax.swing.JTable jTabTitulo;
    private javax.swing.JTable jTabTituloSelecionados;
    private javax.swing.JTable jTabTitulosAgendados;
    private javax.swing.JTabbedPane jTabbPrincipal;
    private javax.swing.JTextField jTexArquivoRetorno;
    private javax.swing.JTextField jTexAvisoBcoCobDirEscriturada;
    private javax.swing.JTextField jTexAvisoBcoCobSimples;
    private javax.swing.JTextField jTexAvisoBcoCobVinculada;
    private javax.swing.JTextField jTexCdAgencia;
    private javax.swing.JTextField jTexCdBanco;
    private javax.swing.JTextField jTexCdConta;
    private javax.swing.JTextField jTexCdDigConta;
    private javax.swing.JTextField jTexCdErroMensInformacao;
    private javax.swing.JTextField jTexCdInfoPortador;
    private javax.swing.JTextField jTexCdInfoTipoMovimento;
    private javax.swing.JTextField jTexCdInfoTipoPagamento;
    private javax.swing.JTextField jTexCdIntrCancelamento;
    private javax.swing.JTextField jTexCdLancamentoVinc;
    private javax.swing.JTextField jTexCdLiquidacao;
    private javax.swing.JTextField jTexCdMovimento;
    private javax.swing.JTextField jTexCdOcorrencia;
    private javax.swing.JTextField jTexCdTipoServico;
    private javax.swing.JTextField jTexDocumento;
    private javax.swing.JTextField jTexNomeBanco;
    private javax.swing.JTextField jTexNomeCliente;
    private javax.swing.JTextField jTexNomeClienteVinc;
    private javax.swing.JTextField jTexNomeEmpresa;
    private javax.swing.JTextField jTexNomeErroMensInfor;
    private javax.swing.JTextField jTexNomeInfoPortador;
    private javax.swing.JTextField jTexNomeInfoTipoMovimento;
    private javax.swing.JTextField jTexNomeInfoTipoPagamento;
    private javax.swing.JTextField jTexNomeIntrCancel;
    private javax.swing.JTextField jTexNomeLiquidacao;
    private javax.swing.JTextField jTexNomeMovimento;
    private javax.swing.JTextField jTexNomeOcorrencia;
    private javax.swing.JTextField jTexNomeRazaoSocial;
    private javax.swing.JTextField jTexNomeTipoServico;
    private javax.swing.JTextField jTexPagador;
    private javax.swing.JTextField jTexQtdDetalhes;
    private javax.swing.JTextField jTexQtdTituloCobDirEscriturada;
    private javax.swing.JTextField jTexQtdTituloCobVinculada;
    private javax.swing.JTextField jTexQtdTitulosCobSimples;
    private javax.swing.JTextField jTexSeqRegistro;
    private javax.swing.JTextField jTexTipoDocumento;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextArea jTextArea3;
    private javax.swing.JTextArea jTextArea4;
    private javax.swing.JTextArea jTextAreaOcorrencia;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
