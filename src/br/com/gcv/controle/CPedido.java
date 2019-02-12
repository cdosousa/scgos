/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.DAO.ConsultaModelo;
import br.com.controle.CBuscarSequencia;
import br.com.gcs.controle.CMateriais;
import br.com.gcs.modelo.Materiais;
import br.com.gcv.dao.ContratoDAO;
import br.com.gcv.dao.ItemPropostaDAO;
import br.com.gcv.dao.PedidoDAO;
import br.com.gcv.modelo.AcabamentoItemPed;
import br.com.gcv.modelo.Clientes;
import br.com.gcv.modelo.Contrato;
import br.com.gcv.modelo.Proposta;
import br.com.gcv.modelo.ItemProposta;
import br.com.gcv.modelo.ItemPedido;
import br.com.gcv.modelo.LocalProposta;
import br.com.gcv.modelo.OperacaoVenda;
import br.com.gcv.modelo.Pedido;
import br.com.gcv.modelo.TipoVerniz;
import br.com.gcv.visao.PesquisarClientes;
import br.com.gfc.controle.CCondicaoPagamento;
import br.com.gfc.controle.CLancamentos;
import br.com.gfc.controle.CTipoMovimento;
import br.com.gfc.controle.CTipoPagamento;
import br.com.gfc.modelo.CondicaoPagamento;
import br.com.gfc.modelo.Lancamentos;
import br.com.gfc.modelo.TipoMovimento;
import br.com.gfc.modelo.TipoPagamento;
import br.com.gsm.controle.CTecnicos;
import br.com.gsm.modelo.Tecnicos;
import br.com.modelo.DataSistema;
import br.com.modelo.HoraSistema;
import br.com.modelo.SessaoUsuario;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Unificação dos controles para criação, altaração e manutenção do Pedido
 * Comerecial
 *
 * @author Cristiano de Oliveira Sousa
 * @version 20180115_alpha criado em 08/02/2017
 * @param conexao
 */
public class CPedido {

    // variáveis de instância da classe
    private Connection conexao;
    private SessaoUsuario su;

    // variáveis do Pedido
    private List<Pedido> todosPed;
    private List<Pedido> totaisPed;
    private List<ItemPedido> todosItped;
    private List<AcabamentoItemPed> todosAcab;

    private Pedido regPedAtual;
    private ItemPedido regItpedAtual;
    private AcabamentoItemPed regAcabAtual;
    private ResultSet rsPed;
    private ResultSet rsItped;
    private ResultSet rsAcab;
    private int idxPedAtual;
    private int idxItpedAtual;
    private int idxAcabAtual;
    private int numRegPed;
    private int numRegLpr;
    private int numRegTotPed;
    private int numRegItped;
    private int numRegAcab;
    private String sqlPed;
    private String sqlItped;
    private String sqlAcab;
    private int ultSeqItp;
    private final String TABELA = "GCVPEDIDO";

    // Variáveis da Proposta que irá gerar o Pedido
    private Proposta modpro;
    private List<LocalProposta> localpro;
    private List<ItemProposta> itempro;
    private String sqlPro;
    private String sqlLpr;
    private String sqlItp;

    // Variáveis de instância gerais da Classe
    private String data;

    // Objetos do Contrato do Pedido
    private Contrato ct;
    private OperacaoVenda ov;

    // Objetos dos lançamentos financeiros
    private TipoMovimento tpMov;

    // construtor padrão da class
    public CPedido(Connection conexao, SessaoUsuario su) {
        this.conexao = conexao;
        this.su = su;
    }

    /**
     * Método para pesquisar o Pedido no sistema.
     *
     * @param sql
     * @return numRegPed
     */
    public int pesquisarPedido(String sql) throws SQLException {
        todosPed = new ArrayList<Pedido>();
        PedidoDAO peddao = new PedidoDAO(conexao);
        rsPed = peddao.pesquisarPedido(sql);
        carregarRegistrosPedido();
        if (todosPed.size() > 0) {
            //JOptionPane.showMessageDialog(null, "Total de Registros: " + todosPed.size() + "\nIdxPedAtual: " + idxPedAtual);
            mostrarPedido(idxPedAtual);
            pesquisarItemPedido();
            numRegPed = todosPed.size();
        } else {
            numRegPed = 0;
        }
        rsPed.close();
        return numRegPed;
    }

    /**
     * Método para pesquisar os Itens do Pedido no sistema e Retorna os itens
     * cadastradas para o Pedido
     */
    private void pesquisarItemPedido() {
        todosItped = new ArrayList<ItemPedido>();
        PedidoDAO peddao = new PedidoDAO(conexao);
        sqlItped = "select * from gcvitempedido where cd_pedido = '"
                + regPedAtual.getCdPedido()
                + "'";
        rsItped = peddao.pesquisarItemPedido(sqlItped);
        CarregarRegistrosItemPedido();
        try {
            rsItped.close();
        } catch (SQLException ex) {
            Logger.getLogger(CPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para pesquisar os totais do Pedido e atualizar no objeto da do
     * Pedido
     *
     * @param sqlped
     */
    public int pesquisarTotaisPedido(String sql) throws SQLException {
        totaisPed = new ArrayList<Pedido>();
        PedidoDAO peddao = new PedidoDAO(conexao);
        peddao.totalizarItens(totaisPed, sql);
        numRegTotPed = totaisPed.size();
        return numRegTotPed;
    }

    /**
     * Método para pesquisar os Itens do da Proposta para geração do Pedido no
     * Sistema
     */
    private void pesquisarItensProposta() throws SQLException {
        sqlLpr = "Select * from gcvitemproposta where cd_proposta = '"
                + modpro.getCdProposta()
                + "' and cd_revisao = '" + modpro.getCdRevisao()
                + "'";
        ItemPropostaDAO itpdao = new ItemPropostaDAO(conexao);
        itempro = new ArrayList<ItemProposta>();
        itpdao.selecionar(itempro, sqlLpr);
    }

    /**
     * Método para pesquisar Acabamento
     */
    public int pesquisarAcabamento(String sql) throws SQLException {
        todosAcab = new ArrayList<AcabamentoItemPed>();
        int numReg = 0;
        PedidoDAO peddao = new PedidoDAO(conexao);
        rsAcab = peddao.pesquisarAcabamento(sql);
        carregarRegistrosAcabamento();
        numReg = todosAcab.size();
        try {
            rsAcab.close();
        } catch (SQLException ex) {
            Logger.getLogger(CPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numReg;
    }

    /**
     * Método privado para carregar os registro do resultSet para a arrayList do
     * Pedido
     *
     */
    private void carregarRegistrosPedido() throws SQLException {
        while (rsPed.next()) {
            try {
                todosPed.add(new Pedido(
                        rsPed.getString("cd_pedido"),
                        rsPed.getString("cd_proposta"),
                        rsPed.getString("cd_revisao"),
                        rsPed.getString("cd_vendedor"),
                        rsPed.getString("cd_tecnico"),
                        rsPed.getString("cpf_cnpj"),
                        rsPed.getString("tipo_pedido"),
                        rsPed.getString("cd_tipopagamento"),
                        rsPed.getString("cd_condpag"),
                        rsPed.getString("cd_opervenda"),
                        rsPed.getDouble("valor_servicos"),
                        rsPed.getDouble("valor_produtos"),
                        rsPed.getDouble("valor_adicionais"),
                        rsPed.getDouble("valor_descontos"),
                        rsPed.getDouble("valor_outros_descontos"),
                        rsPed.getDouble("valor_total_bruto"),
                        rsPed.getDouble("valor_total_liquido"),
                        rsPed.getString("prazo_execucao"),
                        rsPed.getString("data_inicio"),
                        rsPed.getString("obs"),
                        rsPed.getString("usuario_cadastro"),
                        rsPed.getString("data_cadastro"),
                        rsPed.getString("hora_cadastro"),
                        rsPed.getString("usuario_modificacao"),
                        rsPed.getString("data_modificacao"),
                        rsPed.getString("hora_modificacao"),
                        rsPed.getString("situacao")));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro no Carregamento do Pedido!\nPrograma: CPedido.java");
            }
        }
    }

    /**
     * Método criado para carregar o conteúdo do resultset do Item da proposta
     * para a arrayLista do Item da Proposta
     */
    private void CarregarRegistrosItemPedido() {
        try {
            while (rsItped.next()) {
                try {
                    todosItped.add(new ItemPedido(
                            rsItped.getString("cd_pedido"),
                            rsItped.getInt("sequencia"),
                            rsItped.getString("cd_material"),
                            rsItped.getString("cd_unidmedida"),
                            rsItped.getDouble("quantidade"),
                            rsItped.getDouble("valor_unit_bruto"),
                            rsItped.getDouble("valor_unit_liquido"),
                            rsItped.getDouble("perc_desc"),
                            rsItped.getDouble("valor_desc"),
                            rsItped.getDouble("total_item_bruto"),
                            rsItped.getDouble("total_item_liquido"),
                            rsItped.getString("tipo_item"),
                            rsItped.getString("obs_item"),
                            rsItped.getInt("cd_local_proposta"),
                            rsItped.getInt("sequencia_proposta"),
                            rsItped.getString("usuario_cadastro"),
                            rsItped.getString("data_cadastro"),
                            rsItped.getString("hora_cadastro"),
                            rsItped.getString("usuario_modificacao"),
                            rsItped.getString("data_modificacao"),
                            rsItped.getString("hora_modificacao"),
                            rsItped.getString("situacao")));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro na busca do Registro!\nPrograma: CPedido.java\nErr: " + ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método criado para carregar o conteúdo do resultset do Acabamento do Item
     * para a arrayLista do Acabameto
     */
    private void carregarRegistrosAcabamento() {
        try {
            while (rsAcab.next()) {
                try {
                    todosAcab.add(new AcabamentoItemPed(
                            rsAcab.getString("cd_pedido"),
                            rsAcab.getInt("sequencia"),
                            rsAcab.getString("cd_material"),
                            rsAcab.getString("usuario_cadastro"),
                            rsAcab.getString("data_cadastro"),
                            rsAcab.getString("hora_cadastro"),
                            rsAcab.getString("usuario_modificacao"),
                            rsAcab.getString("data_modificacao"),
                            rsAcab.getString("hora_modificacao")));
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Erro na busca do Registro!\nPrograma: CPedido.java\nErr: " + ex);
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(CPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para mostrar a pedido na tela para o usuário
     *
     * @param idxAtualPed Index atual para ser exibido na tela para o usuario
     */
    public Pedido mostrarPedido(int idxAtualPed) {
        regPedAtual = todosPed.get(idxAtualPed);
        //JOptionPane.showMessageDialog(null, "Pedido: " + regPedAtual.getCdPedido());
        if (regPedAtual.getCdCpfCnpj() != null) {
            regPedAtual.setNomeRazaoSocial(buscarCliente(regPedAtual.getCdCpfCnpj()));
        }
        if (regPedAtual.getCdVendedor() != null) {
            regPedAtual.setNomeVendedor(buscarTecnico(regPedAtual.getCdVendedor()));
        }
        if (regPedAtual.getCdTecnico() != null) {
            regPedAtual.setNomeTecnico(buscarTecnico(regPedAtual.getCdTecnico()));
        }
        if (regPedAtual.getCdTipoPagamento() != null) {
            regPedAtual.setNomeTipoPagamento(buscarTipoPagamento(regPedAtual.getCdTipoPagamento()));
        }
        if (regPedAtual.getCdCondPagamento() != null) {
            regPedAtual.setNomeCondPag(buscarCondicaoPagamento(regPedAtual.getCdCondPagamento()));
        }
        if (regPedAtual.getCdOperVenda() != null) {
            regPedAtual.setNomeOperVenda(buscarOperVenda(regPedAtual.getCdOperVenda()));
        }
        buscarProposta();
        return regPedAtual;
    }

    /**
     * Método para mostrar o Item do pedido na tela para o usuário
     *
     * @param idxItpedAtual Index atual para ser exibido na tela para o usuario
     */
    public ItemPedido mostrarItemPedido(int idxItpedAtual) {
        regItpedAtual = todosItped.get(idxItpedAtual);
        return regItpedAtual;
    }

    /**
     * Método para mostear o Acabamento do item do pedido para o usuário
     *
     */
    public AcabamentoItemPed mostrarAcabamento(int idxAcabAtual) {
        regAcabAtual = todosAcab.get(idxAcabAtual);
        regAcabAtual.setNomeMaterial(buscarMaterial());
        return regAcabAtual;
    }

    /**
     * Método para pesquisar a Proposta que gerou o Pedido
     */
    private void buscarProposta() {
        //JOptionPane.showMessageDialog(null, "Buscando proposta!");
        CNewProposta cpro = new CNewProposta(conexao, su);
        Proposta pro = new Proposta();
        String sql = "Select * from gcvproposta where cd_proposta = '"
                + regPedAtual.getCdProposta()
                + "' and cd_revisao = '"
                + regPedAtual.getCdRevisao()
                + "'";
        int numReg = 0;
        try {
            numReg = cpro.pesquisarProposta(sql);
        } catch (SQLException ex) {
            Logger.getLogger(CPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (numReg > 0) {
            pro = cpro.mostrarProposta(0);
            /*
            JOptionPane.showMessageDialog(null, "Mostrando a Pesquisa!\nSql: " + sql
                    +"\nData do Atendimento: "+ate.getDataAtendimento()
                    +"\nHora do Atendimento: "+ate.getHoraAtendimento());
             */
        }
    }

    /**
     * Método para atualizar os tatais pesquisado da Proposta Comercial
     *
     * @param modped
     */
    public void atualizarTotalPedido(Proposta modped) {
        Pedido regAtual = totaisPed.get(0);
        modped.setValorProdutos(regAtual.getValorProdutos());
        modped.setValorServico(regAtual.getValorServico());
        modped.setValorAdicionais(regAtual.getValorAdicionais());
        modped.setValorDescontos(regAtual.getValorDescontos());
        modped.setValorOutrosDescontos(regAtual.getValorOutrosDescontos());
        modped.setValorTotalBruto(regAtual.getValorTotalBruto());
        modped.setValorTotalLiquido(regAtual.getValorTotalLiquido());
    }

    /**
     * Aqui começão a sessão para gerar uma nova proposta comercial no sistema.
     */
    /**
     * Método para preparar os dados para gerar um novo pedido no sistema a
     * partir de uma proposta confirmada
     *
     * @param modpro Recebe o objeto que contém a proposta que será usada para
     * cria o Pedido
     */
    public String criarPedido(Proposta modpro) throws SQLException {
        this.modpro = modpro;
        pesquisarItensProposta();
        return gerarDadosPedido();
    }

    /**
     * Método para Migrar os dados dos objetos da Proposta para os objetos da
     * Pedido
     */
    private String gerarDadosPedido() throws SQLException {
        DataSistema dat = new DataSistema();
        dat.setData(data);
        data = dat.getData();
        Pedido ped = new Pedido();
        List<ItemPedido> itped = new ArrayList<ItemPedido>();
        //JOptionPane.showMessageDialog(null, "Vou pesquisar o Cliente!");
        PesquisarClientes zoomCli = new PesquisarClientes(new JFrame(), true, "P", conexao);
        zoomCli.setVisible(true);
        ped.setCdCpfCnpj(zoomCli.getSelecao1());
        if (ped.getCdCpfCnpj() != null) {
            //JOptionPane.showMessageDialog(null, "Cliente Pesquisado: " + ped.getCdCpfCnpj());
            migraPedido(ped);
            migrarItemPedido(itped, ped.getCdPedido());
            PedidoDAO peddao = new PedidoDAO(conexao);
            peddao.criarPedido(ped, itped);
            JOptionPane.showMessageDialog(null, "Pedido Criado com Suscesso!");
            return ped.getCdPedido();
        } else {
            return "N";
        }
    }

    /**
     * Método para prepara o objeto do Pedido
     *
     * @param ped
     */
    private void migraPedido(Pedido ped) {
        HoraSistema hs = new HoraSistema();
        CBuscarSequencia bs = new CBuscarSequencia(su, TABELA, 8);
        ped.setCdPedido(bs.getRetorno());
        ped.setCdProposta(modpro.getCdProposta());
        ped.setCdRevisao(modpro.getCdRevisao());
        ped.setCdVendedor(modpro.getCdVendedor());
        ped.setCdTecnico(modpro.getCdTecnico());
        ped.setTipoPedido(modpro.getTipoPedido());
        ped.setCdTipoPagamento(modpro.getCdTipoPagamento());
        ped.setCdCondPagamento(modpro.getCdCondPagamento());
        ped.setValorServico(modpro.getValorServico());
        ped.setValorProdutos(modpro.getValorProdutos());
        ped.setValorAdicionais(modpro.getValorAdicionais());
        ped.setValorDescontos(modpro.getValorDescontos());
        ped.setValorOutrosDescontos(modpro.getValorOutrosDescontos());
        ped.setValorTotalBruto(modpro.getValorTotalBruto());
        ped.setValorTotalLiquido(modpro.getValorTotalLiquido());
        ped.setPrazoExecucao(modpro.getPrazoExecucao());
        ped.setObs(modpro.getObs());
        ped.setUsuarioCadastro(su.getUsuarioConectado());
        ped.setDataCadastro(data);
        ped.setHoraCadastro(hs.getHora());
        ped.setSituacao("PE");
    }

    /**
     * Método para preparar o objeto dos itens do Pedido
     *
     * @param itped
     * @param cdPedido
     */
    private void migrarItemPedido(List<ItemPedido> itped, String cdPedido) {
        HoraSistema hs = new HoraSistema();
        ItemProposta moditp = new ItemProposta();
        ItemPedido moditped;
        int sequenciaItens = 1;
        for (int j = 0; j < itempro.size(); j++) {
            moditp = itempro.get(j);
            moditped = new ItemPedido();
            moditped.setCdPedido(cdPedido);
            moditped.setSequencia(sequenciaItens);
            moditped.setCdMaterial(moditp.getCdMaterial());
            moditped.setCdUnidmedida(moditp.getCdUnidmedida());
            moditped.setQuantidade(moditp.getQuantidade());
            moditped.setValorUnitBruto(moditp.getValorUnitBruto());
            moditped.setValorUnitLiquido(moditp.getValorUnitLiquido());
            moditped.setPercDesconto(moditp.getPercDesconto());
            moditped.setValorDescontos(moditp.getValorDescontos());
            moditped.setValorTotalItemBruto(moditp.getValorTotalItemBruto());
            moditped.setValorTotalItemLiquido(moditp.getValorTotalItemLiquido());
            moditped.setTipoItem(moditp.getTipoItem());
            moditped.setObsItem(moditp.getObsItem());
            moditped.setCdLocal(moditp.getCdLocal());
            moditped.setSequenciaAtend(moditp.getSequencia());
            moditped.setUsuarioCadastro(su.getUsuarioConectado());
            moditped.setDataCadastro(data);
            moditped.setHoraCadastro(hs.getHora());
            moditped.setSituacao("PE");
            switch (moditp.getTipoItem()) {
                case "1":
                    moditped.setTipoItem("R");
                    break;
                case "2":
                    moditped.setTipoItem("S");
                    break;
                default:
                    moditped.setTipoItem(" ");
                    break;
            }
            sequenciaItens += 1;
            itped.add(moditped);
        }
    }

    /**
     * Método para gerar o Contrato do Cliente através do Pedido
     *
     * @return cdContrato
     */
    public String gerarContrato() {
        ct = new Contrato();
        DataSistema dat = new DataSistema();
        String data = null;
        dat.setData(data);
        data = dat.getData();
        HoraSistema hs = new HoraSistema();
        CBuscarSequencia cb = new CBuscarSequencia(su, "gcvcontrato", 9);
        ct.setCdContrato(String.format("%s", data.replace("-", "").substring(0, 6) + cb.getRetorno()));
        ct.setCdCpfCnpj(regPedAtual.getCdCpfCnpj());
        ct.setCdPedido(regPedAtual.getCdPedido());
        ct.setDataEmissao(regPedAtual.getDataInicio());
        ct.setModelo("N");
        ct.setUsuarioCadastro(su.getUsuarioConectado());
        ct.setDataCadastro(data);
        ct.setHoraCadastro(hs.getHora());
        ct.setSituacao("PE");
        try {
            ContratoDAO ctDAO = new ContratoDAO(conexao);
            ctDAO.adicionar(ct);
        } catch (SQLException ex) {
            Logger.getLogger(CPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ct.getCdContrato();
    }

    /**
     * Método para gerar o lançamento financeiro no pedido
     *
     * @param tipoDoc tipo de Documento a ser gerado
     * @param tipoLanc tipo de lançamento a ser gerado
     */
    public void gerarFinanceiro(String tipoDoc, String tipoLanc) {
        DataSistema dat = new DataSistema();
        dat.setData(data);
        HoraSistema hs = new HoraSistema();
        Lancamentos modlan = new Lancamentos();
        modlan.setTitulo(regPedAtual.getCdPedido());
        modlan.setValorLancamento(regPedAtual.getValorTotalLiquido());
        modlan.setValorSaldo(regPedAtual.getValorTotalLiquido());
        modlan.setTipoLancamento(tipoLanc);
        modlan.setTipoMovimento("Re");
        modlan.setDataEmissao(regPedAtual.getDataInicio());
        modlan.setCpfCnpj(regPedAtual.getCdCpfCnpj());
        modlan.setTipoDocumento(tipoDoc);
        modlan.setDocumento(regPedAtual.getCdPedido());
        modlan.setGerouArquivo("N");
        modlan.setCdTipoPagamento(regPedAtual.getCdTipoPagamento());
        modlan.setCdContaReduzida(buscarTipoMovimento());
        modlan.setPreparado("N");
        modlan.setSituacao(tpMov.getSituacaoLancamento());
        modlan.setCdCCusto(ov.getCdCCusto());
        modlan.setUsuarioCadastro(su.getUsuarioConectado());
        modlan.setDataCadastro(dat.getData());
        modlan.setHoraCadastro(hs.getHora());
        CLancamentos clan = new CLancamentos(conexao, su);
        try {
            clan.gerarLancamento(modlan, regPedAtual.getCdCondPagamento());
        } catch (SQLException ex) {
            Logger.getLogger(CPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para alterar o lancamento financeiro do pedido
     */
    public void alterarFinanceiro(String tipoDoc, String tipoLanc) {
        CLancamentos clan = new CLancamentos(conexao, su);
        clan.alterarLancamento(regPedAtual.getCdPedido(), tipoLanc, "Re", regPedAtual.getCdCondPagamento());
    }

    /**
     * Método para cancelar os lancamentos financeiro no pedido
     *
     * @param nunLanc número de parcelas para serem canceladas
     * @return retorna zero se as parcelas foram canceladas com sucesso.
     */
    public int cancelarFinanceiro(int nunLanc) {
        CLancamentos clan = new CLancamentos(conexao, su);
        return clan.cancelarLancamentos("PED", regPedAtual.getCdPedido(), "Re", "AB", nunLanc);
    }

    /**
     * Método para voltar status da proposta comercial
     * @param cdProposta
     * @return 
     */
    public int reabrirProposta(String cdProposta) {
        DataSistema dat = new DataSistema();
        dat.setData(data);
        HoraSistema hs = new HoraSistema();
        data = dat.getData();
        String rp = "UPDATE GCVPROPOSTA SET CD_PEDIDO = ?,"
                + "USUARIO_MODIFICACAO = ?,"
                + "DATA_MODIFICACAO = ?,"
                + "HORA_MODIFICACAO = ?,"
                + "SITUACAO = ?"
                + " WHERE CD_PROPOSTA = ?";
        PedidoDAO pdao = new PedidoDAO(conexao);
        try {
            return pdao.reabrirProposta(rp, cdProposta, su.getUsuarioConectado(), data, hs.getHora());
        } catch (SQLException ex) {
            Logger.getLogger(CPedido.class.getName()).log(Level.SEVERE, null, ex);
            return 0;
        }
    }

    /**
     * Método que retornar o número de Registros de Itens do Pedido
     *
     * @return the numRegItped
     */
    public int getNumRegItped() {
        return numRegItped;
    }

    // Método par buscar nome do Tipo de Pagamento
    private String buscarTipoPagamento(String cdTipoPagamento) {
        TipoPagamento tp = new TipoPagamento();
        try {
            CTipoPagamento ctp = new CTipoPagamento(conexao);
            String sqlctp = "SELECT * FROM GFCTIPOPAGAMENTO WHERE CD_TIPOPAGAMENTO = '" + cdTipoPagamento + "'";
            int numReg = ctp.pesquisar(sqlctp);
            if (numReg > 0) {
                ctp.mostrarPesquisa(tp, 0);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome o Município!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return tp.getNomeTipoPagamento();
    }

    // Método par buscar nome da Condição de Pagamento
    private String buscarCondicaoPagamento(String cdCondPag) {
        CondicaoPagamento cp = new CondicaoPagamento();
        try {
            CCondicaoPagamento ccp = new CCondicaoPagamento(conexao);
            String sqlctp = "SELECT * FROM GFCCONDICAOPAGAMENTO WHERE CD_CONDPAG = '" + cdCondPag + "'";
            int numReg = ccp.pesquisar(sqlctp);
            if (numReg > 0) {
                ccp.mostrarPesquisa(cp, 0);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome o Município!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return cp.getNomeCondPag();
    }

    // Método para buscar nome do técnico
    private String buscarTecnico(String cdTecnico) {
        Tecnicos tec = new Tecnicos();
        CTecnicos ctec = new CTecnicos(conexao);
        String sqltec = "SELECT * FROM GSMTECNICOS WHERE CPF = '" + cdTecnico + "'";
        try {
            int numReg = ctec.pesquisar(sqltec);
            if (numReg > 0) {
                ctec.mostrarPesquisa(tec, 0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CProposta.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tec.getNomeTecnico();
    }

    // Método para buscar nome da tabela de preco
    private String buscarTipoVerniz(String cdTipoVerniz) {
        TipoVerniz tv = new TipoVerniz();
        try {
            CTipoVerniz ctv = new CTipoVerniz(conexao);
            String sqltb = "SELECT * FROM  GCVTIPOVERNIZ WHERE CD_TIPOVERNIZ = '" + cdTipoVerniz + "'";
            int numReg = ctv.pesquisar(sqltb);
            if (numReg > 0) {
                ctv.mostrarPesquisa(tv, 0);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do nome do Tipo Verniz!\nPrograma: CItemTipoVerniz.java\nErro: " + ex);
        }
        return tv.getNomeTipoVerniz();
    }

    // Método para buscar nome do cliente
    private String buscarCliente(String cdCpfCnpj) {
        Clientes cli = new Clientes();
        try {
            CClientes ccli = new CClientes(conexao);
            String sqlcli = "SELECT * FROM GCVCLIENTES WHERE cpf_cnpj = '" + cdCpfCnpj + "'";
            ccli.pesquisar(sqlcli);
            ccli.mostrarPesquisa(cli, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca da Essencia de Produtos!\nPrograma: GCSROT0091\nErro: " + ex);
        }
        regPedAtual.setTipoPessoa(cli.getTipoPessoa());
        return cli.getNomeRazaoSocial();
    }

    /**
     * Método privado para buscar o nome da operação de venda
     *
     * @param cdOperVenda Códido do tipo de operação de venda
     */
    private String buscarOperVenda(String cdOperVenda) {
        ov = new OperacaoVenda();
        try {
            COperacaoVenda cov = new COperacaoVenda(conexao);
            String sqlov = "Select * from gcvopervenda where cd_operacao = '"
                    + cdOperVenda
                    + "'";
            cov.pesquisar(sqlov);
            cov.mostrarPesquisa(ov, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca da Operação de Venda!\nPrograma: CPedido.java\nErro: " + ex);
        }
        return ov.getNomeOperacaoVenda();
    }

    /**
     * Método para buscar contrato do cliente
     *
     * @return numReg retorna a quantidade de registros encontrada
     */
    public int buscarContrato() {
        int numReg = 0;
        String sqlCt = "Select * from gcvcontrato where cd_pedido = '" + regPedAtual.getCdPedido()
                + "' and situacao <> 'C'";
        try {
            CContrato cct = new CContrato(conexao, su);
            numReg = cct.pesquisar(sqlCt);
            if (numReg > 0) {
                ct = cct.mostrarPesquisa(0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numReg;
    }

    /**
     * Método para buscar os lancamentos financeiros do pedido
     *
     * @param sql query para buscar os lançamentos no banco
     * @return numReg retorna a quantidade de registros encontrados
     */
    public int buscarLancFinan(String sql) {
        int numReg = 0;
        Lancamentos lan = new Lancamentos();
        try {
            ConsultaModelo cdao = new ConsultaModelo(conexao);
            cdao.setQuery(sql);
            numReg = cdao.getRowCount();
        } catch (SQLException ex) {
            Logger.getLogger(CPedido.class.getName()).log(Level.SEVERE, null, ex);
        }
        return numReg;
    }

    /**
     * Método para buscar nome do material
     */
    private String buscarMaterial() {
        Materiais mat = new Materiais();
        int numReg = 0;
        String sql = "select * from gcsmaterial where cd_material = '" + regAcabAtual.getCdMaterial()
                + "'";
        try {
            CMateriais cmat = new CMateriais(conexao);
            numReg = cmat.pesquisar(sql, false);
            if (numReg > 0) {
                cmat.mostrarPesquisa(mat, 0);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível buscar o nome do material! Programa: CPedido.java.\nErr: " + ex);
        }
        return mat.getNomeMaterial();
    }

    /**
     * Método para buscar o tipo de movimento do lancamento no banco de dados
     *
     * @return cdConta
     */
    private String buscarTipoMovimento() {
        tpMov = new TipoMovimento();
        int numReg = 0;
        String cdConta = "";
        String sql = "select * from gfctipomovimento where cd_tipomovimento = 'Re'";
        try {
            CTipoMovimento ctpm = new CTipoMovimento(conexao);
            numReg = ctpm.pesquisar(sql);
            if (numReg > 0) {
                tpMov = ctpm.selecionarPesquisa(0);
                cdConta = tpMov.getCdContaReduzida();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível buscar o tipo de movimento! Programa: CPedido.java\nErr: " + ex);
        }
        return cdConta;
    }
}
