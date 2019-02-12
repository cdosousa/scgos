/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 * Programa: GCVMO0040
 */
package br.com.gcv.visao;

// objetos do registro Pai
import br.com.gcv.modelo.Pedido;
import br.com.gcv.controle.CPedido;
import br.com.gcv.dao.PedidoDAO;

// objetos de Registro de Itens do Local de Proposta
import br.com.gcv.modelo.ItemPedido;
import br.com.gcv.controle.CItemPedido;

// Objetos para pesquisa de correlato
import br.com.modelo.EnderecoPostal;
import br.com.controle.CEnderecoPostal;
import br.com.gfc.visao.PesquisarCondicaoPagamento;
import br.com.gcs.visao.PesquisarMateriais;
import br.com.gcs.visao.PesquisarUnidadesMedida;
import br.com.gcv.modelo.Proposta;
import br.com.gcv.modelo.ItemProposta;
import br.com.gcv.modelo.LocalProposta;
import br.com.gfc.visao.PesquisarTipoPagamento;
import br.com.modelo.Empresa;
import br.com.controle.CEmpresa;

// Objetos de instância de parâmetros de ambiente
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;
import br.com.modelo.FormatarValor;
import br.com.modelo.HoraSistema;
import br.com.controle.CBuscarSequencia;
import br.com.gfc.visao.PesquisarTitulos;
import br.com.gsm.visao.PesquisarTecnicos;
import br.com.modelo.ParametrosGerais;

// Objetos de ambiente java
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 09/02/2017
 */
public class ManterPedido extends javax.swing.JFrame {

    // Variáveis de instancia de parâmetros de ambiente
    private final boolean ISBOTAO = true;
    private static Connection conexao;
    private static SessaoUsuario su;
    private static ParametrosGerais pg;
    private Empresa emp;
    private VerificarTecla vt;
    private NumberFormat formato;
    private NumberFormat ftq;
    private NumberFormat ftp;
    private NumberFormat ftv;
    private DataSistema dat;
    private String data = null;

    // Variáveis de instância de objetos da tabela Pedido da classe
    private Pedido regCorPedido;
    private List<Pedido> resultPedido;
    private Pedido ped; // objeto específico para salvar o registro no banco
    private CPedido cped;
    private Pedido modped; // objeto específico para carregar o registro na tela

    // Variáveis de Instância de objetos da Tabela itemPedido da classe
    private ItemPedido itped; //Objeto para gravar o registro no banco
    private CItemPedido citped;
    private PedidoDAO itpedao;
    private ItemPedido moditped;
    private DefaultTableModel itens;

    // Variáveis de instância da objetos correlatos classe
    private CEnderecoPostal cep;
    private EnderecoPostal ep;
    private String cdMunicipioIbge;
    private String cdUfIbge;
    private int numRegContrato;

    // Variáveis de instância da classe
    // == registro Pedido
    private int numRegPed;
    private int idxCorPed;
    private String sqlPed;
    private String operPed;
    private double txJuros;
    private final String tipoDoc = "PED";
    private String tipoLanc;
    private String tipoBuscLanc = "";  // Ti=Título, Co=Contrato or Ca=Cancelamento

    private double oldValorPedido;
    private final String DATASOURCE = "gcvpedido";

    // == registro itens do local da Pedido
    private int numRegItens;
    private int idxCorItens;
    private String sqlitped;
    private String operItens;
    private int sequenciaItens;
    private int linhaItem = 0;
    private double descAlcada = 0.000;
    private double valorUnitBrt = 0.00;
    private double valorUnitLiq = 0.00;
    private double valorServico = 0.00;
    private boolean esvaziarTabela = false;

    // == objetos do Pedido
    private Proposta modpro;
    private LocalProposta modlpr;
    private ItemProposta moditp;

    // variáveis de tabelas correlatos
    // == financeiro
    String sqlPesq;
    String sqlSelec;

    /**
     * Método para chamar o pedido através do menu inicial.
     *
     * @param su
     * @param conexao
     * @param pg
     */
    public ManterPedido(SessaoUsuario su, Connection conexao, ParametrosGerais pg) {
        this.su = su;
        this.conexao = conexao;
        this.pg = pg;
        formato = NumberFormat.getInstance(Locale.getDefault());
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        monitoraLinhaItens();
        formatarCampos();
        sqlPed = "select * from gcvpedido";
        dispose();
    }

    /**
     * Método para chamar o pedido através da proposta comercial
     *
     * @param su
     * @param conexao
     * @param pg
     * @param modpro
     */
    public ManterPedido(SessaoUsuario su, Connection conexao, ParametrosGerais pg, Proposta modpro) {
        this.su = su;
        this.conexao = conexao;
        this.pg = pg;
        formato = NumberFormat.getInstance(Locale.getDefault());
        this.modpro = modpro;
        //this.modlpr = modlpr;
        //this.moditp = moditp;
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setaPedido();
        setaItensPedido();
        formatarCampos();
        sqlPed = "select * from gcvpedido where cd_proposta = '" + this.modpro.getCdProposta()
                + "' and cd_revisao = '"
                + this.modpro.getCdRevisao()
                + "'";
        pesquisarPedido();
        dispose();
    }

    // método para setar objeto Pedido
    private void setaPedido() {
        monitoraJanela();
    }

    // método para setar objeto itensPedido
    private void setaItensPedido() {
        moditped = new ItemPedido();
        monitoraLinhaItens();
    }

    // Método para formatar compos na tela
    private void formatarCampos() {
        // formatos de numeros
        ftq.getInstance();
        ftq = new DecimalFormat("###,###,##0.0000");
        ftp = ftq;
        ftv = ftq;
        ftq.setMaximumFractionDigits(3);
        ftp.setMaximumFractionDigits(3);
        ftv.setMaximumFractionDigits(2);

        // campos Pedido
        jForValorTotalServico.setDocument(new DefineCampoDecimal());
        jForValorTotalMateriais.setDocument(new DefineCampoDecimal());
        jForValorTotalAdicionais.setDocument(new DefineCampoDecimal());
        jForValorTotalDescontos.setDocument(new DefineCampoDecimal());
        jForValorTotalOutrosDescontos.setDocument(new DefineCampoDecimal());
        jForValorTotalBruto.setDocument(new DefineCampoDecimal());
        jForValorTotalLiquido.setDocument(new DefineCampoDecimal());
        jForValorTotalBruto.setDocument(new DefineCampoDecimal());
        jForValorTotalLiquido.setDocument(new DefineCampoDecimal());

    }

    // método para limpar tela Pedido
    private void limparTelaPedido() {
        jForCdPedido.setText("");
        jForCdProposta.setText("");
        jTexCdRevisao.setText("");
        jForCdCpfCnpj.setText("");
        jTexNomeRazaoSocial.setText("");
        jComTipoPessoa.setSelectedIndex(0);
        jTexCdVendedor.setText("");
        jTexNomeVendedor.setText("");
        jTexCdTecnico.setText("");
        jTexNomeTecnico.setText("");
        jForDataInicio.setText("");
        jForHoraPedido.setText("");
        jComTipoPedido.setSelectedIndex(0);
        jTexCdTipoPagamento.setText("");
        jTexNomeTipoPagamento.setText("");
        jTexCdCondPagamento.setText("");
        jTexNomeCondPaamento.setText("");
        jTexCdOperVenda.setText("");
        jTexNomeOperVenda.setText("");
        jTexPrazoExecucao.setText("");
        jForValorTotalServico.setText("0.00");
        jForValorTotalMateriais.setText("0.00");
        jForValorTotalAdicionais.setText("0.00");
        jForValorTotalDescontos.setText("0.00");
        jForValorTotalOutrosDescontos.setText("0.00");
        jForValorTotalBruto.setText("0.00");
        jForValorTotalLiquido.setText("0.00");
        jComSituacao.setSelectedIndex(3);
        jTexCadPor.setText("");
        jForDataCad.setText("");
        jTexModifPor.setText("");
        jForDataModif.setText("");
        jTexRegAtual.setText("");
        jTexRegTotal.setText("");
        idxCorPed = 0;
        numRegPed = 0;
        resultPedido = null;
        regCorPedido = null;
        liberarCamposPedido();
    }

    // método para definir o tipo de pesquisa do Pedido
    private void pesquisarPedido() {
        cped = new CPedido(conexao, su);
        modped = new Pedido();
        try {
            numRegPed = cped.pesquisarPedido(sqlPed);
            idxCorPed = 1;
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Argumento inválido na buscaErr: " + iae.getCause());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Pesquisar os Registros!\nErr: " + ex);
        }
        if (numRegPed > 0) {
            upRegistrosPedido();
        } else {
            this.dispose();
        }
    }

    // método para pesquisar ItensPedido
    private void pesquisarItens() {
        sqlitped = "select *"
                + " from gcvitempedido as i"
                + " left outer join gcsmaterial as m on i.cd_material = m.cd_material"
                + " where i.cd_pedido = '" + modped.getCdPedido()
                + "' order by i.cd_pedido, i.sequencia";
        citped = new CItemPedido(conexao);
        itens = new DefaultTableModel();
        try {
            numRegItens = citped.pesquisar(sqlitped, modped.getCdProposta(), modped.getCdRevisao());
            idxCorItens += 1;
        } catch (SQLException ex) {
            Logger.getLogger(ManterPedido.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        if (numRegItens > 0) {
            sequenciaItens = numRegItens;
            jTabItensPedido.setModel(citped.carregarItens());
            ajustarTabelaItem();
            if (linhaItem < 0) {
                linhaItem = 0;
            }
            updateRegistroItem();
        } else {
            sequenciaItens = 0;
            if (esvaziarTabela) {
                esvaziarTabelaItem();
            }
            esvaziarTabela = false;
        }
    }

    // Método para atualizar os registros da Proposta
    private void upRegistrosPedido() {
        jTexRegTotal.setText(Integer.toString(numRegPed));
        jTexRegAtual.setText(Integer.toString(idxCorPed));
        modped = cped.mostrarPedido(idxCorPed - 1);
        DataSistema dat = new DataSistema();
        HoraSistema hs = new HoraSistema();
        jForCdPedido.setText(modped.getCdPedido());
        jForCdProposta.setText(modped.getCdProposta());
        jTexCdRevisao.setText(modped.getCdRevisao());
        jForCdCpfCnpj.setText(modped.getCdCpfCnpj());
        if (modped.getCdCpfCnpj() != null) {
            jTexNomeRazaoSocial.setText(modped.getNomeRazaoSocial().trim().toUpperCase());
        }
        if (modped.getDataInicio() != null) {
            jForDataInicio.setText(dat.getDataConv(Date.valueOf(modped.getDataInicio())));
        }
        jComTipoPessoa.setSelectedIndex(Integer.parseInt(modped.getTipoPessoa()));
        jTexCdVendedor.setText(modped.getCdVendedor());
        jTexNomeVendedor.setText(modped.getNomeVendedor());
        jTexCdTecnico.setText(modped.getCdTecnico());
        jTexNomeTecnico.setText(modped.getNomeTecnico());
        String tipoPedido = modped.getTipoPedido();
        switch (tipoPedido) {
            case "A":
                jComTipoPedido.setSelectedIndex(1);
                break;
            case "R":
                jComTipoPedido.setSelectedIndex(2);
                break;
            case "S":
                jComTipoPedido.setSelectedIndex(3);
                break;
            default:
                jComTipoPedido.setSelectedIndex(0);
                break;
        }
        jTexCdTipoPagamento.setText(modped.getCdTipoPagamento());
        jTexNomeTipoPagamento.setText(modped.getNomeTipoPagamento());
        jTexCdCondPagamento.setText(modped.getCdCondPagamento());
        jTexNomeCondPaamento.setText(modped.getNomeCondPag());
        jTexCdOperVenda.setText(modped.getCdOperVenda());
        jTexNomeOperVenda.setText(modped.getNomeOperVenda());
        jTexPrazoExecucao.setText(modped.getPrazoExecucao());
        jForValorTotalServico.setText(String.valueOf(modped.getValorServico()));
        jForValorTotalMateriais.setText(String.valueOf(modped.getValorProdutos()));
        jForValorTotalAdicionais.setText(String.valueOf(modped.getValorAdicionais()));
        jForValorTotalDescontos.setText(String.valueOf(modped.getValorDescontos()));
        jForValorTotalOutrosDescontos.setText(String.valueOf(modped.getValorOutrosDescontos()));
        jForValorTotalBruto.setText(String.valueOf(modped.getValorTotalBruto()));
        jForValorTotalLiquido.setText(String.valueOf(modped.getValorTotalLiquido()));
        jTexCadPor.setText(modped.getUsuarioCadastro());
        jForDataCad.setText(dat.getDataConv(Date.valueOf(modped.getDataCadastro())));
        jForHoraCad.setText(modped.getHoraCadastro());
        jTexModifPor.setText(modped.getUsuarioModificacao());
        jTextAreaObsPedido.setText(modped.getObs());
        if (modped.getDataModificacao() != null) {
            jForDataModif.setText(dat.getDataConv(Date.valueOf(modped.getDataModificacao())));
            jForHoraModif.setText(modped.getHoraModificacao());
        }
        switch (modped.getSituacao()) {
            case "CA":
                jComSituacao.setSelectedIndex(0);
                break;
            case "CO":
                jComSituacao.setSelectedIndex(1);
                break;
            case "FA":
                jComSituacao.setSelectedIndex(2);
                break;
            default:
                jComSituacao.setSelectedIndex(3);
                break;
        }
        if ("PE".equals(jComSituacao.getSelectedItem().toString().substring(0, 2))
                || "CA".equals(jComSituacao.getSelectedItem().toString().substring(0, 2))) {
            jButPrevisoes.setEnabled(true);
        } else {
            jButPrevisoes.setEnabled(false);
        }
        if ("CA".equals(jComSituacao.getSelectedItem().toString().substring(0, 2))) {
            jButCancelarPedido.setEnabled(!ISBOTAO);
        } else {
            jButCancelarPedido.setEnabled(ISBOTAO);
        }

        // Habilitando/Desabilitando botões de navegação de registros
        if (numRegPed > idxCorPed) {
            jButProximo.setEnabled(true);
        } else {
            jButProximo.setEnabled(false);
        }
        if (idxCorPed > 1) {
            jButAnterior.setEnabled(true);
        } else {
            jButAnterior.setEnabled(false);
        }
        pesquisarItens();
    }

    private void updateRegistroItem() {
        moditped = new ItemPedido();
        //    JOptionPane.showMessageDialog(null, "Pesquisa - Número da Linha: " + linhaItem);
        citped.mostrarPesquisa(moditped, linhaItem);
        jTexTipoVerniz.setText(moditped.getNomeTipoVerniz());
        jTexCdTipoVerniz.setText(moditped.getCdTipolVerniz());
    }

    //Método para Atualizar totais
    private void atualizarTotaisPedido(boolean upTela) {
        int numReg = 0;
        String sqlpro = "select p.cd_pedido as Pedido,"
                + " sum(p.valor_produtos) as 'Total Produtos',"
                + " sum(p.valor_servicos) as 'Total Serviços',"
                + " sum(p.valor_adicionais) as Adicionais,"
                + " sum(p.valor_descontos) as Descontos,"
                + " sum(p.valor_outros_descontos) as 'Outros Descontos',"
                + " sum(p.valor_total_bruto) as 'Total Proposta Bruto',"
                + " sum(p.valor_total_liquido) as 'Total Proposta Liquido'"
                + " from gcvlocalpedido as p"
                + " where p.cd_pedido = '" + modped.getCdPedido()
                + "'";
        try {
            numReg = cped.pesquisarTotaisPedido(sqlpro);

        } catch (SQLException ex) {
            Logger.getLogger(ManterPedido.class
                    .getName()).log(Level.SEVERE, null, ex);
        }
        if (numReg > 0) {
            cped.atualizarTotalPedido(modped);
            /*
            JOptionPane.showMessageDialog(null, "Atualizado Totais do Pediod:\nPedido: " + modped.getCdPedido() + "\nMateriais: " + modped.getValorProdutos()
                    + "\nServiço: " + modped.getValorServico() + "\nAdicionais: " + modped.getValorAdicionais()
                    + "\nDescontos: " + modped.getValorDescontos() + "\nOutros Descontos: "
                    + modped.getValorOutrosDescontos() + "\nTotal Bruto: " + modped.getValorTotalBruto()
                    + "\nTotal Liquido: " + modped.getValorTotalLiquido());
             */
            jForValorTotalServico.setText(String.valueOf(modped.getValorServico()));
            jForValorTotalMateriais.setText(String.valueOf(modped.getValorProdutos()));
            jForValorTotalAdicionais.setText(String.valueOf(modped.getValorAdicionais()));
            jForValorTotalDescontos.setText(String.valueOf(modped.getValorDescontos()));
            jForValorTotalOutrosDescontos.setText(String.valueOf(modped.getValorOutrosDescontos()));
            jForValorTotalBruto.setText(String.valueOf(modped.getValorTotalBruto()));
            jForValorTotalLiquido.setText(String.valueOf(modped.getValorTotalLiquido()));
        } else {
            JOptionPane.showMessageDialog(null, "Não foi possível encontrar registros para atualização!");
        }
    }

    //Método para ajustar tabela itemPedido
    private void ajustarTabelaItem() {
        jTabItensPedido.getColumnModel().getColumn(0).setWidth(5);
        jTabItensPedido.getColumnModel().getColumn(0).setPreferredWidth(5);
        jTabItensPedido.getColumnModel().getColumn(1).setWidth(60);
        jTabItensPedido.getColumnModel().getColumn(1).setPreferredWidth(60);
        jTabItensPedido.getColumnModel().getColumn(2).setWidth(220);
        jTabItensPedido.getColumnModel().getColumn(2).setPreferredWidth(220);
        jTabItensPedido.getColumnModel().getColumn(3).setWidth(10);
        jTabItensPedido.getColumnModel().getColumn(3).setPreferredWidth(10);
        jTabItensPedido.getColumnModel().getColumn(4).setWidth(30);
        jTabItensPedido.getColumnModel().getColumn(4).setPreferredWidth(30);
        jTabItensPedido.getColumnModel().getColumn(5).setWidth(40);
        jTabItensPedido.getColumnModel().getColumn(5).setPreferredWidth(40);
        jTabItensPedido.getColumnModel().getColumn(6).setWidth(40);
        jTabItensPedido.getColumnModel().getColumn(6).setPreferredWidth(40);
        jTabItensPedido.getColumnModel().getColumn(7).setWidth(40);
        jTabItensPedido.getColumnModel().getColumn(7).setPreferredWidth(40);
        jTabItensPedido.getColumnModel().getColumn(8).setWidth(40);
        jTabItensPedido.getColumnModel().getColumn(8).setPreferredWidth(40);
        jTabItensPedido.getColumnModel().getColumn(9).setWidth(40);
        jTabItensPedido.getColumnModel().getColumn(9).setPreferredWidth(40);
    }

    //Método para criar um listener na tabela
    private void monitoraLinhaItens() {
        VerificarTecla vt = new VerificarTecla();
        jTabItensPedido.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!esvaziarTabela) {
                    linhaItem = jTabItensPedido.getSelectedRow();
                    //JOptionPane.showMessageDialog(null, "Mudança - Número da Linha: " + linhaItem);
                    updateRegistroItem();
                }
            }
        });

        // capturando a tecla digitada
        jTabItensPedido.addKeyListener(new KeyListener() {
            boolean inclusao;

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                formato.setMaximumFractionDigits(2);
                double qtde = 0;
                double percPerda = 0;
                String tecla = vt.VerificarTecla(e).toUpperCase();
                if ("ABAIXO".equals(tecla) && !inclusao && "A".equals(operPed)) {
                    if (jTabItensPedido.getSelectedRow() == jTabItensPedido.getRowCount() - 1) {
                        moditped = new ItemPedido();
                        inclusao = true;
                        jTabItensPedido.setModel(citped.adicionarLinha());
                        linhaItem = jTabItensPedido.getSelectedRow() + 1;
                        upValue();
                        ajustarTabelaItem();
                    }
                }

                if ("F5".equals(tecla) && inclusao) {
                    if (jTabItensPedido.getSelectedColumn() == 1) {
                        PesquisarMateriais zoom = new PesquisarMateriais(new JFrame(), true, "P", "R", conexao, true);
                        zoom.setVisible(true);
                        moditped.setSequencia(Integer.parseInt(String.format("%s", jTabItensPedido.getValueAt(linhaItem, 0))));
                        moditped.setCdMaterial(zoom.getCdMaterial());
                        moditped.setNomeMaterial(zoom.getNomeMaterial());
                        moditped.setCdUnidmedida(zoom.cdUnidMedida());
                        moditped.setQuantidade(qtde * percPerda);
                        if ("4".equals(zoom.getTipoProduto())) {
                            moditped.setValorUnitBruto(zoom.getValorUnitBruto());
                            moditped.setValorUnitLiquido(zoom.getValorUnitLiquido());
                            moditped.setTipoItem("R");
                            if (zoom.getValorUnitBruto() == 0) {
                                JOptionPane.showMessageDialog(null, "Item sem preço cadastrado!");
                            }
                            moditped.setValorTotalItemBruto(moditped.getQuantidade() * moditped.getValorUnitBruto());
                            descAlcada = zoom.getDescAlcada();
                            valorUnitBrt = zoom.getValorUnitBruto();
                            valorUnitLiq = zoom.getValorUnitLiquido();
                        }
                        if ("5".equals(zoom.getTipoProduto())) {
                            moditped.setValorUnitBruto(zoom.getValorServico());
                            moditped.setTipoItem("S");
                            if (zoom.getValorServico() == 0) {
                                JOptionPane.showMessageDialog(null, "Item sem preço cadastrado!");
                            }
                            moditped.setValorTotalItemBruto(moditped.getQuantidade() * moditped.getValorUnitBruto());
                            valorServico = zoom.getValorServico();
                        }
                        upValue();
                    }
                    if (jTabItensPedido.getSelectedColumn() == 3) {
                        PesquisarUnidadesMedida zoom = new PesquisarUnidadesMedida(new JFrame(), true, "P", conexao);
                        zoom.setVisible(inclusao);
                        moditped.setCdUnidmedida(zoom.getSelec1());
                        upValue();
                    }
                    ajustarTabelaItem();
                }
                if ("ESCAPE".equals(tecla) && inclusao) {
                    jTabItensPedido.setModel(citped.excluirLinha(jTabItensPedido.getSelectedRow()));
                    jTabItensPedido.requestFocus();
                    inclusao = false;
                    ajustarTabelaItem();
                }
                if ("ENTER".equals(tecla) && (inclusao || "A".equals(operItens))) {
                    if (inclusao) {
                        inclusao = false;
                        operItens = "N";
                    } else {
                        operItens = "A";
                    }
                    salvarLinha();
                }
                if ("GUIA".equals(tecla) && "A".equals(operPed)) {
                    operItens = "A";
                    dowValue();
                    upValue();
                }

                if ("ACIMA".equals(tecla) && inclusao) {
                    inclusao = false;
                    operItens = "N";
                    salvarLinha();
                }
                if ("EXCLUIR".equals(tecla) && "A".equals(operPed) && !inclusao) {
                    if (JOptionPane.showConfirmDialog(null, "Confirma exclusão do Item?") == JOptionPane.OK_OPTION) {
                        int numReg = 0;
                        moditped = new ItemPedido();
                        dowValue();
                        CItemPedido citp = new CItemPedido(conexao);
                        String sq = "select * from gcvitempedido where cd_pedido = '" + modped.getCdPedido()
                                + "' and sequencia = '" + moditped.getSequencia()
                                + "'";
                        try {
                            numReg = citp.pesquisar(sq, "", "");
                        } catch (SQLException ex) {
                            Logger.getLogger(ManterPedido.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (numReg > 0) {
                            citp.mostrarPesquisa(moditped, 0);
                        }
                        itpedao = new PedidoDAO(conexao);
                        itpedao.excluirItemPedido(moditped);
                        if ("R".equals(moditped.getTipoItem())) {
                            modped.setValorProdutos(moditped.getValorProdutos() - moditped.getValorTotalItemBruto());
                        } else {
                            modped.setValorServico(moditped.getValorServico() - moditped.getValorTotalItemBruto());
                            //JOptionPane.showConfirmDialog(null, "Excluindo valor do Serviço!\nValor Serico Atual: " + modlpr.getValorServico()
                            //        + "\nValor Serviço a Excluir: " + moditped.getValorTotalItemBruto());
                        }
                        modped.setValorTotalBruto(modped.getValorTotalBruto() - moditped.getValorTotalItemBruto());
                        modped.setValorTotalLiquido(modped.getValorTotalLiquido() - moditped.getValorTotalItemLiquido());
                        modped.setValorDescontos(modped.getValorDescontos() - moditped.getValorDescontos());
                        esvaziarTabela = true;
                        pesquisarItens();
                        jForValorTotalServico.setText(String.valueOf(modped.getValorServico()));
                        jForValorTotalMateriais.setText(String.valueOf(modped.getValorProdutos()));
                        jForValorTotalAdicionais.setText(String.valueOf(modped.getValorAdicionais()));
                        jForValorTotalDescontos.setText(String.valueOf(modped.getValorDescontos()));
                        jForValorTotalOutrosDescontos.setText(String.valueOf(modped.getValorOutrosDescontos()));
                        jForValorTotalBruto.setText(String.valueOf(modped.getValorTotalBruto()));
                        jForValorTotalLiquido.setText(String.valueOf(modped.getValorTotalLiquido()));
                    }
                }
            }

            private void upValue() {
                jTabItensPedido.setModel(citped.upNovaLinha(moditped, linhaItem, 1, 2, 3, 4, 5, 6, 7, 8, 9, "I"));
            }

            private void dowValue() {
                moditped.setCdPedido(modped.getCdPedido());
                moditped.setSequencia(Integer.parseInt(String.format("%s", jTabItensPedido.getValueAt(linhaItem, 0))));
                moditped.setCdMaterial(String.format("%s", jTabItensPedido.getValueAt(linhaItem, 1)));
                moditped.setNomeMaterial(String.format("%s", jTabItensPedido.getValueAt(linhaItem, 2)));
                moditped.setCdUnidmedida(String.format("%s", jTabItensPedido.getValueAt(linhaItem, 3)));
                try {
                    moditped.setQuantidade(ftq.parse(String.format("%s", jTabItensPedido.getValueAt(linhaItem, 4))).doubleValue());
                    moditped.setValorUnitBruto(ftv.parse(String.format("%s", jTabItensPedido.getValueAt(linhaItem, 5))).doubleValue());
                    moditped.setPercDesconto(ftp.parse(String.format("%s", jTabItensPedido.getValueAt(linhaItem, 6))).doubleValue());
                    moditped.setValorDescontos(ftv.parse(String.format("%s", jTabItensPedido.getValueAt(linhaItem, 7))).doubleValue());
                    moditped.setValorTotalItemBruto(moditped.getQuantidade() * moditped.getValorUnitBruto());
                    moditped.setValorUnitLiquido(moditped.getValorUnitBruto() * (1 - moditped.getPercDesconto() / 100));
                    moditped.setValorTotalItemLiquido(moditped.getQuantidade() * moditped.getValorUnitLiquido());
                } catch (ParseException ex) {
                    Logger.getLogger(ManterPedido.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            private void salvarLinha() {
                salvarItem();
                pesquisarItens();
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }

        });
    }

    // Metodo para criar um listener na janela
    private void monitoraJanela() {
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                if ("A".equals(operPed)) {
                    if (JOptionPane.showConfirmDialog(null, "Confirma sair do Pedido?") == JOptionPane.OK_OPTION) {
                        salvarPedido(false);
                        dispose();
                    }
                } else {
                    dispose();
                }
            }
        });
    }

    //Bloquear os campos da tela Pedido
    private void bloquearCamposPedido() {
        jForCdPedido.setEditable(false);
        jForCdProposta.setEditable(false);
        jTexCdRevisao.setEditable(false);
        jForCdCpfCnpj.setEditable(false);
        jTexCdVendedor.setEditable(false);
        jTexCdTecnico.setEditable(false);
        jForDataInicio.setEditable(false);
        jComTipoPessoa.setEditable(false);
        jComTipoPedido.setEditable(false);
        jTexCdTipoPagamento.setEditable(false);
        jTexCdTipoPagamento.setEnabled(false);
        jTexCdCondPagamento.setEditable(false);
        jTexCdCondPagamento.setEnabled(false);
        jTexCdOperVenda.setEditable(false);
        jTexCdOperVenda.setEnabled(false);
        jTexPrazoExecucao.setEditable(false);
        jTexPrazoExecucao.setEnabled(false);
        jTextAreaObsPedido.setEditable(false);
        jForValorTotalOutrosDescontos.setEditable(false);
        jForValorTotalOutrosDescontos.setEnabled(false);
        jComSituacao.setEditable(false);
    }

    //Liberar os campos da tela Pedido para atualização
    private void liberarCamposPedido() {
        jForCdPedido.setEditable(true);
        jForCdCpfCnpj.setEditable(true);
        jTexNomeRazaoSocial.setEditable(true);
        jTexCdVendedor.setEditable(true);
        jTexCdVendedor.setEnabled(true);
        jTexCdTecnico.setEditable(true);
        jTexCdTecnico.setEnabled(true);
        jForDataInicio.setEnabled(true);
        jForDataInicio.setEditable(true);
        jComTipoPessoa.setEditable(true);
        jComTipoPedido.setEditable(true);
        jTexCdTipoPagamento.setEditable(true);
        jTexCdTipoPagamento.setEnabled(true);
        jTexCdCondPagamento.setEditable(true);
        jTexCdCondPagamento.setEnabled(true);
        jTexCdOperVenda.setEditable(true);
        jTexCdOperVenda.setEnabled(true);
        jTexPrazoExecucao.setEditable(true);
        jTexPrazoExecucao.setEnabled(true);
        jTextAreaObsPedido.setEditable(true);
        jForValorTotalOutrosDescontos.setEditable(true);
        jForValorTotalOutrosDescontos.setEnabled(true);
        jComSituacao.setEditable(true);
        jComSituacao.setEditable(true);
    }

    // método para dar zoom no campo TipoPagamento
    private void zoomTipoPagamento() {
        PesquisarTipoPagamento zoom = new PesquisarTipoPagamento(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdTipoPagamento.setText(zoom.getCdTipoPagamento());
        jTexNomeTipoPagamento.setText(zoom.getNomeTipoPagamento());
    }

    // método para dar zoom no campo CondPagamento
    private void zoomCondicaoPagamento() {
        PesquisarCondicaoPagamento zoom = new PesquisarCondicaoPagamento(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdCondPagamento.setText(zoom.getCdCondPag());
        jTexNomeCondPaamento.setText(zoom.getNomeCondPag());
    }

    // método para dar zoom no campo OperVend
    private void zoomOpercaoVenda() {
        PesquisarOperacaoVenda zoom = new PesquisarOperacaoVenda(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdOperVenda.setText(zoom.getTipoOperacaoVenda());
        jTexNomeOperVenda.setText(zoom.getNomeOperacaoVenda());
    }

    // método par dar zoom no campo Vendedor
    private void zoomVendedor() {
        PesquisarTecnicos zoom = new PesquisarTecnicos(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdVendedor.setText(zoom.getSelecao1());
        jTexNomeVendedor.setText(zoom.getSelecao2());
    }

    // método para dar zoom no campo Técnico
    private void zoomTecnico() {
        PesquisarTecnicos zoom = new PesquisarTecnicos(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdTecnico.setText(zoom.getSelecao1());
        jTexNomeTecnico.setText(zoom.getSelecao2());
    }

    /**
     * Metodo privado para buscar os lacamentos do pedido
     *
     * @param sqlPes query com a busca a ser realizada
     */
    private int LancFinanceiro() {
        int numLanc = 0;
        sqlSelec = "select l.cd_lancamento as Lancamento,"
                + "l.data_cadastro as Data,"
                + "l.cpf_cnpj as Cliente,"
                + "c.nome_razaosocial as Nome,"
                + "l.titulo as Titulo,"
                + "l.cd_parcela as Parcela,"
                + "l.data_emissao as Emissao,"
                + "l.data_vencimento as Vencimento,"
                + "l.data_liquidacao as Liquidaca,"
                + "l.valor_lancamento as Valor,"
                + "l.valor_saldo as Saldo,"
                + "l.situacao as Situacao"
                + " from gfclancamentos as l"
                + " left outer join gcvclientes as c on l.cpf_cnpj = c.cpf_cnpj"
                + " where l.titulo = '" + modped.getCdPedido()
                + "'";
        numLanc = cped.buscarLancFinan(sqlPesq);
        if (numLanc <= 0 && !"CA".equals(jComSituacao.getSelectedItem().toString().substring(0, 2))) {
            if (JOptionPane.showConfirmDialog(null, "Não foi encontrado lançamento para este pedido!\n\nDeseja gerar os lançamentos?") == JOptionPane.OK_OPTION) {
                // se for gerar título financeiro, verifica se existe previsção gerada.
                if (!"Pre".equals(tipoLanc)) {
                    String sql = "select * from gfclancamentos where tipo_lancamento = 'Pre' and titulo ='" + modped.getCdPedido()
                            + "'";
                    if (cped.buscarLancFinan(sql) > 0) {
                        cped.alterarFinanceiro(tipoDoc, tipoLanc);
                    } else {
                        cped.gerarFinanceiro(tipoDoc, tipoLanc);
                    }
                    jButPrevisoes.setEnabled(!ISBOTAO);
                } else {
                    cped.gerarFinanceiro(tipoDoc, tipoLanc);
                }
                numLanc = 1;
            }
        }
        return numLanc;
    }

    /**
     * Método para cancelar pedido
     */
    private void cancelarPedido() {
        sqlPed = "SELECT * FROM GCVPEDIDO WHERE CD_PEDIDO = '" + modped.getCdPedido()
                + "'";
        jComSituacao.setSelectedIndex(0);
        operPed = "A";
        salvarPedido(true);
        pesquisarPedido();
    }

    // método para dar zoom nas previsões
    private void zoomFinanceiro(String sql) {
        PesquisarTitulos zoom = new PesquisarTitulos(this, true, "P", conexao, sql, modped.getCdCondPagamento(), su);
        zoom.setVisible(true);
    }

    /**
     * Método para chamar a pesquisa de acabamento
     */
    private void zoomAcabamento() {
        if (!jTexCdTipoVerniz.getText().isEmpty()) {
            ManterAcabamentoItem zoom = new ManterAcabamentoItem(this, true, su, conexao, moditped, jTexCdTipoVerniz.getText());
            zoom.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(null, "Não Existe Tipo de Verniz cadastrado!");
        }
    }

    // metodo para salvar registro Pedido
    private void salvarPedido(boolean upTela) {
        ped = new Pedido();
        dat = new DataSistema();
        if (jForCdCpfCnpj.getText().isEmpty() || jComSituacao.getSelectedItem().toString().substring(0, 1) == " ") {
            JOptionPane.showMessageDialog(null, "Os campo CPF / CNPJ e Situacao precisam ser preenchidos corretamente!");
        } else {
            ped.setCdProposta(jForCdProposta.getText());
            ped.setCdRevisao(jTexCdRevisao.getText());
            ped.setCdCpfCnpj(jForCdCpfCnpj.getText());
            ped.setTipoPessoa(jComTipoPessoa.getSelectedItem().toString().substring(0, 1));
            ped.setCdVendedor(jTexCdVendedor.getText());
            ped.setCdTecnico(jTexCdTecnico.getText());
            ped.setDataInicio(dat.getDataConv(jForDataInicio.getText()));
            ped.setTipoPedido(jComTipoPedido.getSelectedItem().toString().substring(0, 1));
            ped.setCdTipoPagamento(jTexCdTipoPagamento.getText());
            ped.setCdCondPagamento(jTexCdCondPagamento.getText());
            ped.setCdOperVenda(jTexCdOperVenda.getText());
            ped.setValorServico(modped.getValorServico());
            ped.setValorProdutos(modped.getValorProdutos());
            ped.setValorAdicionais(modped.getValorAdicionais());
            ped.setValorDescontos(modped.getValorDescontos());
            ped.setValorOutrosDescontos(modped.getValorOutrosDescontos());
            ped.setValorTotalBruto(modped.getValorTotalBruto());
            ped.setValorTotalLiquido(modped.getValorTotalLiquido());
            ped.setPrazoExecucao(jTexPrazoExecucao.getText().trim());
            ped.setObs(jTextAreaObsPedido.getText());
            ped.setUsuarioCadastro(su.getUsuarioConectado());
            ped.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 2));
            gravarPedidoBanco();
            controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO, ISBOTAO);
        }
    }

    private void gravarPedidoBanco() {
        dat.setData(data);
        data = dat.getData();
        HoraSistema hs = new HoraSistema();
        PedidoDAO peddao = null;
        peddao = new PedidoDAO(conexao);
        if ("N".equals(operPed)) {
            CBuscarSequencia bs = new CBuscarSequencia(su, DATASOURCE, 8);
            ped.setCdPedido(bs.getRetorno());
            ped.setUsuarioCadastro(su.getUsuarioConectado());
            ped.setDataCadastro(data);
            ped.setHoraCadastro(hs.getHora());
            peddao.criarPedido(ped);
            modpro.setCdPedido(ped.getCdPedido());
            modpro.setAtualizacao(true);
        } else {
            ped.setCdPedido(jForCdPedido.getText());
            ped.setUsuarioModificacao(su.getUsuarioConectado());
            ped.setDataModificacao(data);
            ped.setHoraModificacao(hs.getHora());
            peddao.atualizarPedido(ped);
        }
        sqlPed = "SELECT * FROM GCVPEDIDO WHERE CD_PEDIDO = '" + ped.getCdPedido()
                + "'";
        operPed = "";
    }

    /**
     * Método para salvar o Item
     */
    private void salvarItem() {
        if (moditped.getSequencia() != 0 && moditped.getCdMaterial() != null) {
            dat = new DataSistema();
            itped = new ItemPedido();
            String data = null;
            itped.setCdPedido(modped.getCdPedido());
            itped.setSequencia(moditped.getSequencia());
            itped.setCdMaterial(moditped.getCdMaterial());
            itped.setCdUnidmedida(moditped.getCdUnidmedida());
            itped.setQuantidade(moditped.getQuantidade());
            itped.setValorUnitBruto(moditped.getValorUnitBruto());
            itped.setValorUnitLiquido(moditped.getValorUnitLiquido());
            itped.setPercDesconto(moditped.getPercDesconto());
            itped.setValorDescontos(moditped.getValorTotalItemBruto() / 100 * moditped.getPercDesconto());
            itped.setValorTotalItemBruto(moditped.getValorTotalItemBruto());
            itped.setValorTotalItemLiquido(moditped.getValorTotalItemBruto() * (1 - moditped.getPercDesconto() / 100));
            itped.setTipoItem(moditped.getTipoItem());
            itped.setObsItem(moditped.getObsItem());
            itped.setCdLocal(moditped.getCdLocal());
            itped.setSequenciaAtend(moditped.getSequenciaAtend());
            dat.setData(data);
            data = dat.getData();
            itped.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 2));
            gravarItemBanco();
        }
    }

    private void gravarItemBanco() {
        PedidoDAO itpdao = null;
        HoraSistema hs = new HoraSistema();
        itpdao = new PedidoDAO(conexao);
        if ("N".equals(operItens)) {
            itped.setUsuarioCadastro(su.getUsuarioConectado());
            itped.setDataCadastro(data);
            itped.setHoraCadastro(hs.getHora());
            itpdao.criarItemPedido(itped);
            if ("R".equals(moditped.getTipoItem())) {
                modped.setValorProdutos(modped.getValorProdutos() + moditped.getValorTotalItemBruto());
                //modped.setValorProdutos(modped.getValorProdutos() + moditped.getValorTotalItemBruto());
            } else if ("S".equals(moditped.getTipoItem())) {
                modped.setValorServico(modped.getValorServico() + moditped.getValorTotalItemBruto());
                //modped.setValorServico(modped.getValorServico() + moditped.getValorTotalItemBruto());
            }
            modped.setValorTotalBruto(modped.getValorProdutos() + modped.getValorServico());
            modped.setValorTotalLiquido(modped.getValorTotalBruto() - (modped.getValorDescontos() + modped.getValorOutrosDescontos()));
            //modped.setValorDescontos(modped.getValorDescontos() + moditped.getValorDescontos());
            //modped.setValorTotalBruto(modped.getValorProdutos() + modped.getValorServico());
        } else {
            itped.setUsuarioModificacao(su.getUsuarioConectado());
            itped.setDataModificacao(data);
            itped.setHoraModificacao(hs.getHora());
            itpdao.criarItemPedido(itped);
        }
        jForValorTotalMateriais.setText(String.valueOf(modped.getValorProdutos()));
        jForValorTotalServico.setText(String.valueOf(modped.getValorServico()));
        jForValorTotalAdicionais.setText(String.valueOf(modped.getValorAdicionais()));
        jForValorTotalDescontos.setText(String.valueOf(modped.getValorDescontos()));
        jForValorTotalOutrosDescontos.setText(String.valueOf(modped.getValorOutrosDescontos()));
        jForValorTotalBruto.setText(String.valueOf(modped.getValorTotalBruto()));
        jForValorTotalLiquido.setText(String.valueOf(modped.getValorTotalLiquido()));
        operItens = "";
    }

    // Método para Excluir Pedido
    private void excluirPedido() {
        if (!jForCdPedido.getText().isEmpty()) {
            try {
                Pedido cc = new Pedido();
                cc.setCdPedido(jForCdPedido.getText());
                PedidoDAO ccDAO = new PedidoDAO(conexao);
                ccDAO.excluirPedido(cc);
                limparTelaPedido();
                pesquisarPedido();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral na exclusão do registro!");
            }
        }
    }

    public boolean isCellEditable(int row, int column) {
        return false;
    }

    // méto para limpar a tabela de itens
    private void esvaziarTabelaItem() {
        citped = new CItemPedido(conexao);
        jTabItensPedido.setModel(new JTable().getModel());
        jTabItensPedido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabItensPedido.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{
                    {null, null, null, null, null, null, null, null, null, null}
                },
                new String[]{
                    "Seq.", "Cod.", "Descrição", "U.M", "Qtde", "Pr. Unit.", "Perc.Desc.", "Val.Desc.", "Tot.Item.Bruto", "Tot.Item.Liquido"
                }
        ) {
            Class[] types = new Class[]{
                java.lang.Integer.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.String.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Double.class,
                java.lang.Double.class

            };
            boolean[] canEdit = new boolean[]{
                false, false, false, false, false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit[columnIndex];
            }
        });
    }

    /**
     * Método privado para consultar o cadastro do Cliente
     *
     */
    private void consultarCliente() {
        String sql = "select * from gcvclientes where cpf_cnpj = '"
                + modped.getCdCpfCnpj()
                + "'";
        new ManterClientes(su, conexao, sql, false).setVisible(true);
    }

    /**
     * Método privato para consultar a Proposta Comercial vincualda ao Pedido
     */
    private void consultarProposta() {
        String sql = "select * from gcvproposta where cd_proposta = '"
                + modped.getCdProposta()
                + "' and cd_revisao = '"
                + modped.getCdRevisao()
                + "'";
        new ManterPropostaComercialRev1(su, conexao, pg, sql, false);
    }

    // Metodo para controlar os botoes
    private void controleBotoes(boolean bEd, boolean bSa, boolean bCa, boolean bEx, boolean bNo, boolean bCl) {
        jButEditar.setEnabled(bEd);
        jButSalvar.setEnabled(bSa);
        jButCancelar.setEnabled(bCa);
        jButExcluir.setEnabled(bEx);
        jButNovoPedido.setEnabled(bNo);
        jButSair.setEnabled(bCl);
    }

    // método para criar Pedido
    private void criarPedido() {
        try {
            cped.criarPedido(modpro);
        } catch (SQLException ex) {
            Logger.getLogger(ManterPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        pesquisarPedido();
    }

    // método para imprimir pedido comercial
    private void imprimirPedido() {
        emp = new Empresa();
        CEmpresa cemp = new CEmpresa(conexao);
        String sqlemp = "Select * from pgsempresa as e where e.situacao = 'A'";
        try {
            int numReg = cemp.pesquisar(sqlemp);
            if (numReg > 0) {
                cemp.mostrarPesquisa(emp, 0);
                //new CReportPropostaComercial().abrirRelatorio(modped.getCdProposta(), modped.getCdProposta(), modped.getCdRevisao(), su, conexao, emp, pg, "RELATORIO", modped);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManterPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    // método para enviar email pedido comercial
    private void enviarPedido() {
        if (modped.getDataEnvio() == null) {
            dat = new DataSistema();
            dat.setData(data);
            data = dat.getData();
            modped.setDataEnvio(data);
            operPed = "A";
            ped = modped;
            try {
                gravarPedidoBanco();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Não foi possível gravar o Pedido Comercial Para envio!");
            }
        }
        emp = new Empresa();
        CEmpresa cemp = new CEmpresa(conexao);
        String sqlemp = "Select * from pgsempresa as e where e.situacao = 'A'";
        try {
            int numReg = cemp.pesquisar(sqlemp);
            if (numReg > 0) {
                cemp.mostrarPesquisa(emp, 0);
                //new CReportPropostaComercial().abrirRelatorio(modped.getCdProposta(), modped.getCdProposta(), modped.getCdRevisao(), su, conexao, emp, pg, "EMAIL", modped);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ManterPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para buscar contrato do pedido
     */
    private void buscarContrato() {
        numRegContrato = cped.buscarContrato();
        if (numRegContrato > 0) {
            new ManterContrato(su, conexao, pg, modped, "P").setVisible(true);
        } else if (JOptionPane.showConfirmDialog(null, "Não existe contrato gerado para este pedido!\nDeseja gerar o contrato?") == JOptionPane.OK_OPTION
                && !"CA".equals(jComSituacao.getSelectedItem().toString().substring(0, 2))) {
            String dataInicio = jForDataInicio.getText().replace("/", "");
            if (!dataInicio.trim().isEmpty()) {
                String cdContrato = cped.gerarContrato();
                if (!cdContrato.isEmpty()) {
                    jComSituacao.setSelectedIndex(1);
                    jButPrevisoes.setEnabled(false);
                    JOptionPane.showMessageDialog(null, "Contrato: " + cdContrato + " Gerado com sucesso!");
                    salvarPedido(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Para gerar o Contrato é necessário informar a data de Início do Serviço.");
            }
        }
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
        jButImprimir = new javax.swing.JButton();
        jButEnviar = new javax.swing.JButton();
        jButSair = new javax.swing.JButton();
        jLabSituacao = new javax.swing.JLabel();
        jComSituacao = new javax.swing.JComboBox<>();
        jLabCdVendedor = new javax.swing.JLabel();
        jTexCdVendedor = new javax.swing.JTextField();
        jTexNomeVendedor = new javax.swing.JTextField();
        jLabCdTecnico = new javax.swing.JLabel();
        jTexCdTecnico = new javax.swing.JTextField();
        jTexNomeTecnico = new javax.swing.JTextField();
        jLabNomeRazaoSocial = new javax.swing.JLabel();
        jTexNomeRazaoSocial = new javax.swing.JTextField();
        jLabTipoPessoa = new javax.swing.JLabel();
        jComTipoPessoa = new javax.swing.JComboBox<>();
        jLabCdCpfCnpj = new javax.swing.JLabel();
        jForCdCpfCnpj = new javax.swing.JFormattedTextField();
        jLabCdPedido = new javax.swing.JLabel();
        jForCdPedido = new javax.swing.JFormattedTextField();
        jPanProposta = new javax.swing.JPanel();
        jLabCdProposta = new javax.swing.JLabel();
        jLabCdRevisao = new javax.swing.JLabel();
        jTexCdRevisao = new javax.swing.JTextField();
        jForCdProposta = new javax.swing.JFormattedTextField();
        jSepPedido = new javax.swing.JSeparator();
        jButCliente = new javax.swing.JButton();
        jLabDataPedido = new javax.swing.JLabel();
        jForDataInicio = new javax.swing.JFormattedTextField();
        jLabHoraPedido = new javax.swing.JLabel();
        jForHoraPedido = new javax.swing.JFormattedTextField();
        jSepCliente = new javax.swing.JSeparator();
        jLabTipoPedido = new javax.swing.JLabel();
        jComTipoPedido = new javax.swing.JComboBox<>();
        jLabTipoPagamento = new javax.swing.JLabel();
        jTexCdTipoPagamento = new javax.swing.JTextField();
        jTexNomeTipoPagamento = new javax.swing.JTextField();
        jTexCdCondPagamento = new javax.swing.JTextField();
        jTexNomeCondPaamento = new javax.swing.JTextField();
        jLabCondPag = new javax.swing.JLabel();
        jLabPrazoExecucao = new javax.swing.JLabel();
        jTexPrazoExecucao = new javax.swing.JTextField();
        jLabOperVenda = new javax.swing.JLabel();
        jTexCdOperVenda = new javax.swing.JTextField();
        jTexNomeOperVenda = new javax.swing.JTextField();
        jPanItens = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTabItensPedido = new javax.swing.JTable();
        jLabValorTotalServico = new javax.swing.JLabel();
        jForValorTotalServico = new FormatarValor(FormatarValor.NUMERO);
        jLabValorTotalMateriais = new javax.swing.JLabel();
        jForValorTotalMateriais = new FormatarValor(FormatarValor.NUMERO);
        jLabValorTotalAdicionais = new javax.swing.JLabel();
        jForValorTotalAdicionais = new FormatarValor(FormatarValor.NUMERO);
        jLabValTotalDescontos = new javax.swing.JLabel();
        jForValorTotalDescontos = new FormatarValor(FormatarValor.NUMERO);
        jLabValorTotalBruto = new javax.swing.JLabel();
        jForValorTotalBruto = new FormatarValor(FormatarValor.NUMERO);
        jForValorTotalLiquido = new FormatarValor(FormatarValor.NUMERO);
        jLabTotalLiquido = new javax.swing.JLabel();
        jForValorTotalOutrosDescontos = new FormatarValor(FormatarValor.NUMERO);
        jLabValorTotalOutrosDesc = new javax.swing.JLabel();
        jLabInformacaoComplementar = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextAreaObsPedido = new javax.swing.JTextArea();
        jPanRodape = new javax.swing.JPanel();
        jForDataCad = new javax.swing.JFormattedTextField();
        jForDataModif = new javax.swing.JFormattedTextField();
        jLabCadPor = new javax.swing.JLabel();
        jTexCadPor = new javax.swing.JTextField();
        jTexModifPor = new javax.swing.JTextField();
        jLabModifPor = new javax.swing.JLabel();
        jTexRegAtual = new javax.swing.JTextField();
        jLabReg = new javax.swing.JLabel();
        jTexRegTotal = new javax.swing.JTextField();
        jForHoraCad = new javax.swing.JFormattedTextField();
        jForHoraModif = new javax.swing.JFormattedTextField();
        jButNovoPedido = new javax.swing.JButton();
        jButContrato = new javax.swing.JButton();
        jButOrdemServico = new javax.swing.JButton();
        jButPrevisoes = new javax.swing.JButton();
        jButTitulos = new javax.swing.JButton();
        jButImprimirPedido = new javax.swing.JButton();
        jButEnvirPedido = new javax.swing.JButton();
        jButSairPedido = new javax.swing.JButton();
        jLabTipoVerniz = new javax.swing.JLabel();
        jTexTipoVerniz = new javax.swing.JTextField();
        jButAcabamento = new javax.swing.JButton();
        jTexCdTipoVerniz = new javax.swing.JTextField();
        jButCancelarPedido = new javax.swing.JButton();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Manter Pedido Comercial");
        setMinimumSize(new java.awt.Dimension(870, 763));
        setSize(new java.awt.Dimension(870, 763));

        jTooMenuFerramentas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTooMenuFerramentas.setRollover(true);

        jButNovo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Addthis-32.png"))); // NOI18N
        jButNovo.setText("Novo");
        jButNovo.setEnabled(false);
        jButNovo.setFocusable(false);
        jButNovo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButNovo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButNovo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButNovoActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButNovo);

        jButEditar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButEditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Edit-32.png"))); // NOI18N
        jButEditar.setText("Editar");
        jButEditar.setFocusable(false);
        jButEditar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButEditar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButEditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButEditarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButEditar);

        jButSalvar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Ok-32.PNG"))); // NOI18N
        jButSalvar.setText("Salvar");
        jButSalvar.setEnabled(false);
        jButSalvar.setFocusable(false);
        jButSalvar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButSalvar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButSalvar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButSalvarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButSalvar);

        jButCancelar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButCancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Cancel-32.png"))); // NOI18N
        jButCancelar.setText("Cancelar");
        jButCancelar.setEnabled(false);
        jButCancelar.setFocusable(false);
        jButCancelar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButCancelar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButCancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButCancelarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButCancelar);

        jButExcluir.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButExcluir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Delete-32.png"))); // NOI18N
        jButExcluir.setText("Excluir");
        jButExcluir.setEnabled(false);
        jButExcluir.setFocusable(false);
        jButExcluir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButExcluir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButExcluir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButExcluirActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButExcluir);

        jButPesquisar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButPesquisar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Search-32.png"))); // NOI18N
        jButPesquisar.setText("Pesquisar");
        jButPesquisar.setFocusable(false);
        jButPesquisar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButPesquisar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButPesquisar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButPesquisarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButPesquisar);

        jButAnterior.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButAnterior.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Back-32.png"))); // NOI18N
        jButAnterior.setText("Anterior");
        jButAnterior.setEnabled(false);
        jButAnterior.setFocusable(false);
        jButAnterior.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButAnterior.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButAnterior.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButAnteriorActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButAnterior);

        jButProximo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButProximo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Next-32.png"))); // NOI18N
        jButProximo.setText("Próximo");
        jButProximo.setEnabled(false);
        jButProximo.setFocusable(false);
        jButProximo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButProximo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButProximo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButProximoActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButProximo);

        jButImprimir.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButImprimir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Print_32.png"))); // NOI18N
        jButImprimir.setText("Imprimir");
        jButImprimir.setFocusable(false);
        jButImprimir.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButImprimir.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButImprimir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButImprimirActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButImprimir);

        jButEnviar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButEnviar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Mail-send-32.png"))); // NOI18N
        jButEnviar.setText("Enviar");
        jButEnviar.setFocusable(false);
        jButEnviar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButEnviar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButEnviarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButEnviar);

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

        jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabSituacao.setText("Situação:");

        jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CA-Cancelado", "CO-Contrato", "FA-Faturado", "PE-Pendente" }));

        jLabCdVendedor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdVendedor.setText("Vend.:");

        jTexCdVendedor.setEditable(false);
        jTexCdVendedor.setEnabled(false);
        jTexCdVendedor.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdVendedorKeyPressed(evt);
            }
        });

        jTexNomeVendedor.setEditable(false);
        jTexNomeVendedor.setEnabled(false);

        jLabCdTecnico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdTecnico.setText("Técnico:");

        jTexCdTecnico.setEditable(false);
        jTexCdTecnico.setEnabled(false);
        jTexCdTecnico.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdTecnicoKeyPressed(evt);
            }
        });

        jTexNomeTecnico.setEditable(false);
        jTexNomeTecnico.setEnabled(false);

        jLabNomeRazaoSocial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabNomeRazaoSocial.setText("Nome:");

        jLabTipoPessoa.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTipoPessoa.setText("Tp. Pessoa:");

        jComTipoPessoa.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Física", "Jurídica" }));
        jComTipoPessoa.setMaximumSize(new java.awt.Dimension(70, 40));
        jComTipoPessoa.setMinimumSize(new java.awt.Dimension(70, 20));
        jComTipoPessoa.setPreferredSize(new java.awt.Dimension(70, 20));

        jLabCdCpfCnpj.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdCpfCnpj.setText("CNPJ / C.P.F.:");

        jLabCdPedido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdPedido.setText("Pedido:");

        jPanProposta.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabCdProposta.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdProposta.setText("Proposta Comercial:");

        jLabCdRevisao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdRevisao.setText("Revisão:");

        jForCdProposta.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jForCdPropostaKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanPropostaLayout = new javax.swing.GroupLayout(jPanProposta);
        jPanProposta.setLayout(jPanPropostaLayout);
        jPanPropostaLayout.setHorizontalGroup(
            jPanPropostaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPropostaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanPropostaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabCdProposta)
                    .addComponent(jLabCdRevisao, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanPropostaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jTexCdRevisao)
                    .addComponent(jForCdProposta, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanPropostaLayout.setVerticalGroup(
            jPanPropostaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanPropostaLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanPropostaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCdProposta)
                    .addComponent(jForCdProposta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanPropostaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTexCdRevisao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabCdRevisao))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButCliente.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButCliente.setText("Cliente");
        jButCliente.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButClienteActionPerformed(evt);
            }
        });

        jLabDataPedido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDataPedido.setText("Data Início:");

        try {
            jForDataInicio.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##/##/####")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }

        jLabHoraPedido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabHoraPedido.setText("Hora:");

        jLabTipoPedido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTipoPedido.setText("Tipo Pedido:");

        jComTipoPedido.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ambos", "Revenda", "Serviço" }));

        jLabTipoPagamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTipoPagamento.setText("Tipo Pagamento:");

        jTexCdTipoPagamento.setEditable(false);
        jTexCdTipoPagamento.setEnabled(false);
        jTexCdTipoPagamento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdTipoPagamentoKeyPressed(evt);
            }
        });

        jTexNomeTipoPagamento.setEditable(false);
        jTexNomeTipoPagamento.setEnabled(false);

        jTexCdCondPagamento.setEditable(false);
        jTexCdCondPagamento.setEnabled(false);
        jTexCdCondPagamento.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdCondPagamentoKeyPressed(evt);
            }
        });

        jTexNomeCondPaamento.setEditable(false);
        jTexNomeCondPaamento.setEnabled(false);

        jLabCondPag.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCondPag.setText("Condição de Pagamento:");

        jLabPrazoExecucao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPrazoExecucao.setText("Prazo de Execução:");

        jTexPrazoExecucao.setEditable(false);
        jTexPrazoExecucao.setEnabled(false);

        jLabOperVenda.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabOperVenda.setText("Oper.Vend");

        jTexCdOperVenda.setEditable(false);
        jTexCdOperVenda.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdOperVendaKeyPressed(evt);
            }
        });

        jTexNomeOperVenda.setEditable(false);

        jPanItens.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), " Itens Serviço / Venda:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jTabItensPedido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jTabItensPedido.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Seq.", "Cod.", "Descrição", "U.M", "Qtde", "Pr. Unit.", "% Desc.", "Val. Desc.", "T. Ite.Brt", "T. Ite.Liq"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class, java.lang.Double.class
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
        jScrollPane1.setViewportView(jTabItensPedido);
        if (jTabItensPedido.getColumnModel().getColumnCount() > 0) {
            jTabItensPedido.getColumnModel().getColumn(0).setResizable(false);
            jTabItensPedido.getColumnModel().getColumn(0).setPreferredWidth(5);
            jTabItensPedido.getColumnModel().getColumn(1).setResizable(false);
            jTabItensPedido.getColumnModel().getColumn(1).setPreferredWidth(60);
            jTabItensPedido.getColumnModel().getColumn(2).setResizable(false);
            jTabItensPedido.getColumnModel().getColumn(2).setPreferredWidth(220);
            jTabItensPedido.getColumnModel().getColumn(3).setResizable(false);
            jTabItensPedido.getColumnModel().getColumn(3).setPreferredWidth(10);
            jTabItensPedido.getColumnModel().getColumn(4).setResizable(false);
            jTabItensPedido.getColumnModel().getColumn(4).setPreferredWidth(30);
            jTabItensPedido.getColumnModel().getColumn(5).setResizable(false);
            jTabItensPedido.getColumnModel().getColumn(5).setPreferredWidth(40);
            jTabItensPedido.getColumnModel().getColumn(6).setResizable(false);
            jTabItensPedido.getColumnModel().getColumn(6).setPreferredWidth(40);
            jTabItensPedido.getColumnModel().getColumn(7).setResizable(false);
            jTabItensPedido.getColumnModel().getColumn(7).setPreferredWidth(40);
            jTabItensPedido.getColumnModel().getColumn(8).setResizable(false);
            jTabItensPedido.getColumnModel().getColumn(8).setPreferredWidth(40);
            jTabItensPedido.getColumnModel().getColumn(9).setResizable(false);
            jTabItensPedido.getColumnModel().getColumn(9).setPreferredWidth(40);
        }

        javax.swing.GroupLayout jPanItensLayout = new javax.swing.GroupLayout(jPanItens);
        jPanItens.setLayout(jPanItensLayout);
        jPanItensLayout.setHorizontalGroup(
            jPanItensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1)
        );
        jPanItensLayout.setVerticalGroup(
            jPanItensLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanItensLayout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jLabValorTotalServico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValorTotalServico.setText("Val. Ser.:");

        jForValorTotalServico.setEditable(false);
        jForValorTotalServico.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jLabValorTotalMateriais.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValorTotalMateriais.setText("Val. Mat.:");

        jForValorTotalMateriais.setEditable(false);
        jForValorTotalMateriais.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jLabValorTotalAdicionais.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValorTotalAdicionais.setText("Val. Adic.:");

        jForValorTotalAdicionais.setEditable(false);
        jForValorTotalAdicionais.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jLabValTotalDescontos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValTotalDescontos.setText("Val. Desc.:");

        jForValorTotalDescontos.setEditable(false);
        jForValorTotalDescontos.setEnabled(false);

        jLabValorTotalBruto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValorTotalBruto.setText("Tot. Local Brt.:");

        jForValorTotalBruto.setEditable(false);
        jForValorTotalBruto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));

        jForValorTotalLiquido.setEditable(false);
        jForValorTotalLiquido.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForValorTotalLiquido.setEnabled(false);

        jLabTotalLiquido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTotalLiquido.setText("Tot. Local Liq.:");

        jForValorTotalOutrosDescontos.setEditable(false);
        jForValorTotalOutrosDescontos.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForValorTotalOutrosDescontos.setEnabled(false);

        jLabValorTotalOutrosDesc.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabValorTotalOutrosDesc.setText("Outros Desc.:");

        jLabInformacaoComplementar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabInformacaoComplementar.setText("Informação Complementar:");

        jScrollPane2.setMaximumSize(new java.awt.Dimension(166, 46));
        jScrollPane2.setMinimumSize(new java.awt.Dimension(166, 46));
        jScrollPane2.setPreferredSize(new java.awt.Dimension(166, 46));

        jTextAreaObsPedido.setColumns(20);
        jTextAreaObsPedido.setRows(5);
        jTextAreaObsPedido.setMaximumSize(new java.awt.Dimension(164, 90));
        jTextAreaObsPedido.setMinimumSize(new java.awt.Dimension(164, 90));
        jTextAreaObsPedido.setPreferredSize(new java.awt.Dimension(164, 90));
        jScrollPane2.setViewportView(jTextAreaObsPedido);

        jPanRodape.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jForDataCad.setEditable(false);
        jForDataCad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataCad.setEnabled(false);

        jForDataModif.setEditable(false);
        jForDataModif.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForDataModif.setEnabled(false);

        jLabCadPor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCadPor.setText("Cadastrado:");

        jTexCadPor.setEditable(false);
        jTexCadPor.setEnabled(false);

        jTexModifPor.setEditable(false);
        jTexModifPor.setEnabled(false);

        jLabModifPor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabModifPor.setText("Modificado:");

        jTexRegAtual.setEditable(false);
        jTexRegAtual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexRegAtual.setEnabled(false);

        jLabReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabReg.setText("\\");

            jTexRegTotal.setEditable(false);
            jTexRegTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            jTexRegTotal.setEnabled(false);

            jForHoraCad.setEditable(false);
            try {
                jForHoraCad.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForHoraCad.setEnabled(false);

            jForHoraModif.setEditable(false);
            try {
                jForHoraModif.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("##:##:##")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForHoraModif.setEnabled(false);

            javax.swing.GroupLayout jPanRodapeLayout = new javax.swing.GroupLayout(jPanRodape);
            jPanRodape.setLayout(jPanRodapeLayout);
            jPanRodapeLayout.setHorizontalGroup(
                jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanRodapeLayout.createSequentialGroup()
                    .addGap(13, 13, 13)
                    .addComponent(jTexRegAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(3, 3, 3)
                    .addComponent(jLabReg, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(2, 2, 2)
                    .addComponent(jTexRegTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jLabCadPor)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexCadPor, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataCad, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForHoraCad, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabModifPor)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexModifPor, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataModif, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForHoraModif, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanRodapeLayout.setVerticalGroup(
                jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanRodapeLayout.createSequentialGroup()
                    .addGap(11, 11, 11)
                    .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTexRegTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabReg)
                            .addComponent(jTexRegAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jForDataCad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabCadPor)
                            .addComponent(jTexCadPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabModifPor)
                            .addComponent(jTexModifPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForDataModif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForHoraCad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForHoraModif, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap())
            );

            jButNovoPedido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButNovoPedido.setText("Novo Pedido");

            jButContrato.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButContrato.setText("Contrato");
            jButContrato.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButContratoActionPerformed(evt);
                }
            });

            jButOrdemServico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButOrdemServico.setText("Ordem Serviço");

            jButPrevisoes.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButPrevisoes.setText("Previsões");
            jButPrevisoes.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButPrevisoesActionPerformed(evt);
                }
            });

            jButTitulos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButTitulos.setText("Títulos");
            jButTitulos.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButTitulosActionPerformed(evt);
                }
            });

            jButImprimirPedido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButImprimirPedido.setText("Imprimir");

            jButEnvirPedido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButEnvirPedido.setText("Enviar");

            jButSairPedido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButSairPedido.setText("Sair");

            jLabTipoVerniz.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabTipoVerniz.setText("Tipo Verniz:");

            jTexTipoVerniz.setEditable(false);
            jTexTipoVerniz.setEnabled(false);

            jButAcabamento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButAcabamento.setText("Acabamento");
            jButAcabamento.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButAcabamentoActionPerformed(evt);
                }
            });

            jTexCdTipoVerniz.setEditable(false);
            jTexCdTipoVerniz.setEnabled(false);

            jButCancelarPedido.setFont(new java.awt.Font("Tahoma", 1, 13)); // NOI18N
            jButCancelarPedido.setText("Cancelar Pedido");
            jButCancelarPedido.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButCancelarPedidoActionPerformed(evt);
                }
            });

            jMenuBar.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jMenu1.setText("Arquivo");

            jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItem1.setText("Novo");
            jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItem1ActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItem1);

            jMenuItemSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemSalvar.setText("Salvar");
            jMenuItemSalvar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemSalvarActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItemSalvar);

            jMenuItemSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemSair.setText("Sair");
            jMenuItemSair.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemSairActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItemSair);

            jMenuBar.add(jMenu1);

            jMenu2.setText("Editar");

            jMenuItemEditar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemEditar.setText("Editar");
            jMenuItemEditar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemEditarActionPerformed(evt);
                }
            });
            jMenu2.add(jMenuItemEditar);

            jMenuBar.add(jMenu2);

            setJMenuBar(jMenuBar);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createSequentialGroup()
                    .addGap(3, 3, 3)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jLabValorTotalServico)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForValorTotalServico, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabValorTotalMateriais)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForValorTotalMateriais, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jButCancelarPedido)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabTipoVerniz)))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jLabValorTotalAdicionais)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForValorTotalAdicionais, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jLabValTotalDescontos)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForValorTotalDescontos, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabValorTotalBruto)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForValorTotalBruto, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap())
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addComponent(jTexCdTipoVerniz)
                                    .addGap(11, 11, 11)
                                    .addComponent(jTexTipoVerniz, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jButAcabamento)
                                    .addGap(32, 32, 32))))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabInformacaoComplementar)
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addContainerGap())
                        .addComponent(jPanItens, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanRodape, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jSepCliente, javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabCdCpfCnpj)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jForCdCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jButCliente)
                                            .addGap(18, 18, 18)
                                            .addComponent(jLabTipoPessoa)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jComTipoPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabCdPedido)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jForCdPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addGap(63, 63, 63)
                                            .addComponent(jLabSituacao)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabNomeRazaoSocial)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jTexNomeRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabCdVendedor)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jTexCdVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jTexNomeVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGap(18, 18, 18)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jTexNomeTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout.createSequentialGroup()
                                                    .addComponent(jLabCdTecnico)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                    .addComponent(jTexCdTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addComponent(jSepPedido))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jPanProposta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                            .addComponent(jLabDataPedido)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jForDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jLabHoraPedido)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jForHoraPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                            .addGap(18, 18, 18))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(jTexPrazoExecucao, javax.swing.GroupLayout.PREFERRED_SIZE, 136, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(36, 36, 36))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabTipoPedido)
                                            .addComponent(jComTipoPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabTipoPagamento)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTexCdTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTexNomeTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addGap(5, 5, 5)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGap(1, 1, 1)
                                                .addComponent(jTexCdCondPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTexNomeCondPaamento, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addComponent(jLabCondPag))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabOperVenda)
                                                .addGap(109, 109, 109))
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jTexCdOperVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jTexNomeOperVenda, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)))
                                        .addComponent(jLabPrazoExecucao))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(539, 539, 539)
                                        .addComponent(jLabValorTotalOutrosDesc)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jForValorTotalOutrosDescontos, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabTotalLiquido)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jForValorTotalLiquido))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(jButNovoPedido)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButContrato, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButOrdemServico)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButPrevisoes, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButTitulos, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButImprimirPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButEnvirPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jButSairPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addContainerGap())))
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jPanProposta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabDataPedido)
                                .addComponent(jForDataInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabHoraPedido)
                                .addComponent(jForHoraPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabCdPedido)
                                .addComponent(jForCdPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabSituacao))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jSepPedido, javax.swing.GroupLayout.PREFERRED_SIZE, 1, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabCdCpfCnpj)
                                .addComponent(jForCdCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButCliente)
                                .addComponent(jLabTipoPessoa)
                                .addComponent(jComTipoPessoa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabNomeRazaoSocial)
                                .addComponent(jTexNomeRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabCdTecnico)
                                .addComponent(jTexCdTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTexNomeTecnico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabCdVendedor)
                                .addComponent(jTexCdVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexNomeVendedor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jSepCliente, javax.swing.GroupLayout.PREFERRED_SIZE, 2, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabTipoPedido)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jComTipoPedido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabPrazoExecucao)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexPrazoExecucao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createSequentialGroup()
                            .addComponent(jLabTipoPagamento)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTexCdTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexNomeTipoPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabCondPag)
                                .addComponent(jLabOperVenda))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jTexCdCondPagamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexNomeCondPaamento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexCdOperVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexNomeOperVenda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jButNovoPedido)
                        .addComponent(jButContrato)
                        .addComponent(jButOrdemServico)
                        .addComponent(jButPrevisoes)
                        .addComponent(jButTitulos)
                        .addComponent(jButImprimirPedido)
                        .addComponent(jButEnvirPedido)
                        .addComponent(jButSairPedido))
                    .addGap(7, 7, 7)
                    .addComponent(jPanItens, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(1, 1, 1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabTipoVerniz)
                        .addComponent(jTexTipoVerniz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButAcabamento)
                        .addComponent(jTexCdTipoVerniz, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButCancelarPedido))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabValTotalDescontos)
                            .addComponent(jForValorTotalDescontos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabValorTotalServico)
                            .addComponent(jForValorTotalServico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabValorTotalMateriais)
                            .addComponent(jForValorTotalMateriais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabValorTotalAdicionais)
                            .addComponent(jForValorTotalAdicionais, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabValorTotalBruto)
                            .addComponent(jForValorTotalBruto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabValorTotalOutrosDesc)
                            .addComponent(jForValorTotalOutrosDescontos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabTotalLiquido)
                            .addComponent(jForValorTotalLiquido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 19, Short.MAX_VALUE)
                    .addComponent(jLabInformacaoComplementar)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        jButNovoActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jMenuItemSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalvarActionPerformed
        // TODO add your handling code here:
        jButSalvarActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemSalvarActionPerformed

    private void jMenuItemSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jMenuItemSairActionPerformed

    private void jMenuItemEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEditarActionPerformed
        // TODO add your handling code here:
        jButEditarActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemEditarActionPerformed

    private void jButNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButNovoActionPerformed
        esvaziarTabelaItem();
        controleBotoes(!ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO);
    }//GEN-LAST:event_jButNovoActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        // TODO add your handling code here:
        operPed = "A";
        jButSalvar.setEnabled(true);
        liberarCamposPedido();
        esvaziarTabela = true;
        controleBotoes(!ISBOTAO, ISBOTAO, ISBOTAO, ISBOTAO, ISBOTAO, !ISBOTAO);
    }//GEN-LAST:event_jButEditarActionPerformed

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        operPed = "A";
        salvarPedido(true);
        pesquisarPedido();
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
        jForCdPedido.setEditable(true);
        jForCdPedido.setEnabled(true);
        jButProximo.setEnabled(!ISBOTAO);
        jButAnterior.setEnabled(!ISBOTAO);
        jButSair.setEnabled(ISBOTAO);
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO);
        limparTelaPedido();
        operPed = "N";         // se cancelar a ação atual na tela do sistema a operação do sistema será N  de novo Registro
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        controleBotoes(ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO, ISBOTAO);
        excluirPedido();
    }//GEN-LAST:event_jButExcluirActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        if (jForCdPedido.getText().isEmpty()) {
            sqlPed = "SELECT * FROM GCVPEDIDO";
        } else {
            sqlPed = "SELECT * FROM GCVPEDIDO WHERE CD_PEDIDO = " + jForCdPedido.getText().trim();
        }
        bloquearCamposPedido();
        pesquisarPedido();
        controleBotoes(ISBOTAO, !ISBOTAO, ISBOTAO, !ISBOTAO, !ISBOTAO, !ISBOTAO);
        jButSair.setEnabled(ISBOTAO);
    }//GEN-LAST:event_jButPesquisarActionPerformed

    private void jButAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAnteriorActionPerformed
        idxCorPed -= 1;
        esvaziarTabela = true;
        esvaziarTabelaItem();
        upRegistrosPedido();
        esvaziarTabela = false;
    }//GEN-LAST:event_jButAnteriorActionPerformed

    private void jButProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButProximoActionPerformed
        idxCorPed += 1;
        esvaziarTabela = true;
        esvaziarTabelaItem();
        upRegistrosPedido();
        esvaziarTabela = false;
    }//GEN-LAST:event_jButProximoActionPerformed

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jButImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButImprimirActionPerformed
        imprimirPedido();
    }//GEN-LAST:event_jButImprimirActionPerformed

    private void jTexCdVendedorKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdVendedorKeyPressed
        VerificarTecla vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomVendedor();
        }
    }//GEN-LAST:event_jTexCdVendedorKeyPressed

    private void jTexCdTecnicoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdTecnicoKeyPressed
        VerificarTecla vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomTecnico();
        }
    }//GEN-LAST:event_jTexCdTecnicoKeyPressed

    private void jButEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEnviarActionPerformed
        enviarPedido();
    }//GEN-LAST:event_jButEnviarActionPerformed

    private void jTexCdCondPagamentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdCondPagamentoKeyPressed
        VerificarTecla vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomCondicaoPagamento();
        }
    }//GEN-LAST:event_jTexCdCondPagamentoKeyPressed

    private void jTexCdTipoPagamentoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdTipoPagamentoKeyPressed
        VerificarTecla vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomTipoPagamento();
        }
    }//GEN-LAST:event_jTexCdTipoPagamentoKeyPressed

    private void jTexCdOperVendaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdOperVendaKeyPressed
        // TODO add your handling code here:
        VerificarTecla vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomOpercaoVenda();
        }
    }//GEN-LAST:event_jTexCdOperVendaKeyPressed

    private void jButClienteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButClienteActionPerformed
        consultarCliente();
    }//GEN-LAST:event_jButClienteActionPerformed

    private void jForCdPropostaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jForCdPropostaKeyPressed
        VerificarTecla vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            consultarProposta();
        }
    }//GEN-LAST:event_jForCdPropostaKeyPressed

    private void jButPrevisoesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPrevisoesActionPerformed
        tipoBuscLanc = "Ti"; // Ti=Título, Co=Contrato or Ca=Cancelamento
        tipoLanc = "Pre";
        sqlPesq = "select * from buscarfinanceiro"
                + " where tpDoc = '" + tipoDoc
                + "' and Titulo = '" + modped.getCdPedido()
                + "'";
        if (LancFinanceiro() > 0) {
            zoomFinanceiro(sqlSelec);
        } else {
            JOptionPane.showMessageDialog(null, "Não existe lancamentos para este pedido!");
        }
    }//GEN-LAST:event_jButPrevisoesActionPerformed

    private void jButContratoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButContratoActionPerformed
        tipoBuscLanc = "Co"; // Ti=Título, Co=Contrato or Ca=Cancelamento
        tipoLanc = "Tit";
        sqlPesq = "select * from buscarfinanceiro"
                + " where tpDoc = '" + tipoDoc
                + "' and tpLanc = '" + tipoLanc
                + "' and Titulo = '" + modped.getCdPedido()
                + "'";
        if (!"CA".equals(jComSituacao.getSelectedItem().toString().substring(0, 2))) {
            if (LancFinanceiro() > 0) {
                buscarContrato();
            }
        } else {
            buscarContrato();
        }
    }//GEN-LAST:event_jButContratoActionPerformed

    private void jButAcabamentoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAcabamentoActionPerformed
        zoomAcabamento();
    }//GEN-LAST:event_jButAcabamentoActionPerformed

    private void jButTitulosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButTitulosActionPerformed
        tipoBuscLanc = "Ti"; // Ti=Título, Co=Contrato or Ca=Cancelamento
        tipoLanc = "Tit";
        sqlPesq = "select * from buscarfinanceiro"
                + " where tpDoc = '" + tipoDoc
                + "' and tpLanc = '" + tipoLanc
                + "' and Titulo = '" + modped.getCdPedido()
                + "'";
        if (LancFinanceiro() > 0) {
            zoomFinanceiro(sqlSelec);
        } else {
            JOptionPane.showMessageDialog(null, "Não existe lancamentos para este pedido!");
        }
    }//GEN-LAST:event_jButTitulosActionPerformed

    private void jButCancelarPedidoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarPedidoActionPerformed
        sqlPesq = "select * from buscarfinanceiro"
                + " where tpDoc = '" + tipoDoc
                + "' and Titulo = '" + modped.getCdPedido()
                + "' and Situacao = 'AB'";
        int numParcelas = cped.buscarLancFinan(sqlPesq);
        if (numParcelas > 0) {
            if (cped.cancelarFinanceiro(numParcelas) == 0) {
                cancelarPedido();
            }
        } else {
            cancelarPedido();
        }
    }//GEN-LAST:event_jButCancelarPedidoActionPerformed

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
            java.util.logging.Logger.getLogger(ManterPedido.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterPedido.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterPedido.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterPedido.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterPedido(su, conexao, pg).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButAcabamento;
    private javax.swing.JButton jButAnterior;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButCancelarPedido;
    private javax.swing.JButton jButCliente;
    private javax.swing.JButton jButContrato;
    private javax.swing.JButton jButEditar;
    private javax.swing.JButton jButEnviar;
    private javax.swing.JButton jButEnvirPedido;
    private javax.swing.JButton jButExcluir;
    private javax.swing.JButton jButImprimir;
    private javax.swing.JButton jButImprimirPedido;
    private javax.swing.JButton jButNovo;
    private javax.swing.JButton jButNovoPedido;
    private javax.swing.JButton jButOrdemServico;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JButton jButPrevisoes;
    private javax.swing.JButton jButProximo;
    private javax.swing.JButton jButSair;
    private javax.swing.JButton jButSairPedido;
    private javax.swing.JButton jButSalvar;
    private javax.swing.JButton jButTitulos;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JComboBox<String> jComTipoPedido;
    private javax.swing.JComboBox<String> jComTipoPessoa;
    private javax.swing.JFormattedTextField jForCdCpfCnpj;
    private javax.swing.JFormattedTextField jForCdPedido;
    private javax.swing.JFormattedTextField jForCdProposta;
    private javax.swing.JFormattedTextField jForDataCad;
    private javax.swing.JFormattedTextField jForDataInicio;
    private javax.swing.JFormattedTextField jForDataModif;
    private javax.swing.JFormattedTextField jForHoraCad;
    private javax.swing.JFormattedTextField jForHoraModif;
    private javax.swing.JFormattedTextField jForHoraPedido;
    private javax.swing.JFormattedTextField jForValorTotalAdicionais;
    private javax.swing.JFormattedTextField jForValorTotalBruto;
    private javax.swing.JFormattedTextField jForValorTotalDescontos;
    private javax.swing.JFormattedTextField jForValorTotalLiquido;
    private javax.swing.JFormattedTextField jForValorTotalMateriais;
    private javax.swing.JFormattedTextField jForValorTotalOutrosDescontos;
    private javax.swing.JFormattedTextField jForValorTotalServico;
    private javax.swing.JLabel jLabCadPor;
    private javax.swing.JLabel jLabCdCpfCnpj;
    private javax.swing.JLabel jLabCdPedido;
    private javax.swing.JLabel jLabCdProposta;
    private javax.swing.JLabel jLabCdRevisao;
    private javax.swing.JLabel jLabCdTecnico;
    private javax.swing.JLabel jLabCdVendedor;
    private javax.swing.JLabel jLabCondPag;
    private javax.swing.JLabel jLabDataPedido;
    private javax.swing.JLabel jLabHoraPedido;
    private javax.swing.JLabel jLabInformacaoComplementar;
    private javax.swing.JLabel jLabModifPor;
    private javax.swing.JLabel jLabNomeRazaoSocial;
    private javax.swing.JLabel jLabOperVenda;
    private javax.swing.JLabel jLabPrazoExecucao;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabTipoPagamento;
    private javax.swing.JLabel jLabTipoPedido;
    private javax.swing.JLabel jLabTipoPessoa;
    private javax.swing.JLabel jLabTipoVerniz;
    private javax.swing.JLabel jLabTotalLiquido;
    private javax.swing.JLabel jLabValTotalDescontos;
    private javax.swing.JLabel jLabValorTotalAdicionais;
    private javax.swing.JLabel jLabValorTotalBruto;
    private javax.swing.JLabel jLabValorTotalMateriais;
    private javax.swing.JLabel jLabValorTotalOutrosDesc;
    private javax.swing.JLabel jLabValorTotalServico;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JPanel jPanItens;
    private javax.swing.JPanel jPanProposta;
    private javax.swing.JPanel jPanRodape;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSepCliente;
    private javax.swing.JSeparator jSepPedido;
    private javax.swing.JTable jTabItensPedido;
    private javax.swing.JTextField jTexCadPor;
    private javax.swing.JTextField jTexCdCondPagamento;
    private javax.swing.JTextField jTexCdOperVenda;
    private javax.swing.JTextField jTexCdRevisao;
    private javax.swing.JTextField jTexCdTecnico;
    private javax.swing.JTextField jTexCdTipoPagamento;
    private javax.swing.JTextField jTexCdTipoVerniz;
    private javax.swing.JTextField jTexCdVendedor;
    private javax.swing.JTextField jTexModifPor;
    private javax.swing.JTextField jTexNomeCondPaamento;
    private javax.swing.JTextField jTexNomeOperVenda;
    private javax.swing.JTextField jTexNomeRazaoSocial;
    private javax.swing.JTextField jTexNomeTecnico;
    private javax.swing.JTextField jTexNomeTipoPagamento;
    private javax.swing.JTextField jTexNomeVendedor;
    private javax.swing.JTextField jTexPrazoExecucao;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JTextField jTexTipoVerniz;
    private javax.swing.JTextArea jTextAreaObsPedido;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
