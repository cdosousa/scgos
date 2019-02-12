/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.controle;

import br.com.controle.CBuscarSequencia;
import br.com.controle.CMunicipios;
import br.com.controle.CUnidadeFederacao;
import br.com.gcs.controle.CEssenciaProdutos;
import br.com.gcs.modelo.EssenciaProdutos;
import br.com.gcv.dao.ItemLocalDAO;
import br.com.gcv.dao.LocalAtendimentoDAO;
import br.com.gcv.dao.NewPropostaDAO;
import br.com.gcv.modelo.Atendimento;
import br.com.gcv.modelo.ItemLocal;
import br.com.gcv.modelo.ItemProposta;
import br.com.gcv.modelo.LocalAtendimento;
import br.com.gcv.modelo.LocalProposta;
import br.com.gcv.modelo.Proposta;
import br.com.gcv.modelo.TipoVerniz;
import br.com.gfc.controle.CCondicaoPagamento;
import br.com.gfc.controle.CTipoPagamento;
import br.com.gfc.modelo.CondicaoPagamento;
import br.com.gfc.modelo.TipoPagamento;
import br.com.gsm.controle.CTecnicos;
import br.com.gsm.modelo.Tecnicos;
import br.com.modelo.DataSistema;
import br.com.modelo.HoraSistema;
import br.com.modelo.Municipios;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.UnidadeFederacao;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Unificação dos controles para criação, altaração e manutenção da Proposta
 * Comerecial
 *
 * @author Cristiano de Oliveira Sousa
 * @version 20180115_alpha criado em 15/01/2017
 * @param conexao
 */
public class CNewProposta {

    // variáveis de instância da classe
    private Connection conexao;
    private SessaoUsuario su;

// variáveis da Proposta Comercial
    private List<Proposta> todosPro;
    private List<Proposta> totaisPro;
    private List<LocalProposta> todosLpr;
    private List<ItemProposta> todosItp;
    private int ultRevisaoPro;

    private Proposta regProAtual;
    private LocalProposta regLprAtual;
    private ItemProposta regItpLocal;
    //private NewPropostaDAO prodao;
    private ResultSet rsPro;
    private ResultSet rsLpr;
    private ResultSet rsItp;
    private int idxProAtual;
    private int idxLprAtual;
    private int idxItpAtual;
    private int numRegPro;
    private int numRegTotPro;
    private int numRegLpr;
    private int numRegItp;
    private String sqlPro;
    private String sqlLpr;
    private String sqlItp;
    private int ultSeqLpr;
    private int ultSeqItp;
    private final String TABELA = "GCVPROPOSTA";

// Variáveis do Atendimento que irá gerar a Proposta Comercial
    private Atendimento modate;
    private List<LocalAtendimento> localAte;
    private List<ItemLocal> itemAte;
    private String sqlAte;
    private String sqlLat;
    private String sqlIta;

// Variáveis de instância gerais da Classe
    private String data;

    // construtor padrão da class
    public CNewProposta(Connection conexao, SessaoUsuario su) {
        this.conexao = conexao;
        this.su = su;
        //prodao = new NewPropostaDAO(conexao);
    }

    /**
     * Método para pesquisar a Proposta no sistema.
     *
     * @param sql
     */
    public int pesquisarProposta(String sql) throws SQLException {
        todosPro = new ArrayList<Proposta>();
        NewPropostaDAO prodao = new NewPropostaDAO(conexao);
        rsPro = prodao.pesquisarProposta(sql);
        carregarRegistrosProposta();
        numRegPro = todosPro.size();
        rsPro.close();
        return numRegPro;
    }

    /**
     * Método privado para carregar os registro do resultSet para a arrayList da
     * Proposta Comercial
     *
     */
    private void carregarRegistrosProposta() throws SQLException {
        while (rsPro.next()) {
            //        JOptionPane.showMessageDialog(null, "Carregando Propostas!");
            try {
                todosPro.add(new Proposta(
                        rsPro.getString("cd_proposta"),
                        rsPro.getString("cd_revisao"),
                        rsPro.getString("cd_atendimento"),
                        rsPro.getString("cd_vistoria"),
                        rsPro.getString("cd_vendedor"),
                        rsPro.getString("cd_tecnico"),
                        rsPro.getString("nome_cliente"),
                        rsPro.getString("tipo_pessoa"),
                        rsPro.getString("telefone"),
                        rsPro.getString("celular"),
                        rsPro.getString("email"),
                        rsPro.getString("tipo_logradouro"),
                        rsPro.getString("logradouro"),
                        rsPro.getString("numero"),
                        rsPro.getString("complemento"),
                        rsPro.getString("Bairro"),
                        rsPro.getString("cd_municipio_ibge"),
                        rsPro.getString("sigla_uf"),
                        rsPro.getString("cep"),
                        rsPro.getString("tipo_endereco"),
                        rsPro.getString("tipo_pedido"),
                        rsPro.getString("cd_tipopagamento"),
                        rsPro.getString("cd_condpag"),
                        rsPro.getString("cd_pedido"),
                        rsPro.getDouble("valor_servicos"),
                        rsPro.getDouble("valor_produtos"),
                        rsPro.getDouble("valor_adicionais"),
                        rsPro.getDouble("valor_descontos"),
                        rsPro.getDouble("valor_outros_descontos"),
                        rsPro.getDouble("valor_total_bruto"),
                        rsPro.getDouble("valor_total_liquido"),
                        rsPro.getString("prazo_execucao"),
                        rsPro.getString("obs"),
                        rsPro.getString("data_envio"),
                        rsPro.getString("usuario_cadastro"),
                        rsPro.getString("data_cadastro"),
                        rsPro.getString("hora_cadastro"),
                        rsPro.getString("usuario_modificacao"),
                        rsPro.getString("data_modificacao"),
                        rsPro.getString("hora_modificacao"),
                        rsPro.getString("situacao")));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro no Carregamento da Proposta Comercial!\nPrograma: CNewProposta.java\nErr: " + ex);
            }
            //        JOptionPane.showMessageDialog(null, "Proposta: "+rsPro.getString("cd_proposta"));
            ultRevisaoPro = Integer.parseInt(rsPro.getString("cd_revisao"));
        }
        //    JOptionPane.showMessageDialog(null, "Proposta Carregada!");
        //    JOptionPane.showMessageDialog(null, "Total de Registros: "+todosPro.size());
    }

    /**
     * Método para mostrar a proposta na tela para o usuário
     *
     * @param idxAtualPro Index atual para ser exibido na tela para o usuario
     */
    public Proposta mostrarProposta(int idxAtualPro) {
        regProAtual = todosPro.get(idxAtualPro);
        regProAtual.setNomeVendedor(buscarTecnico(regProAtual.getCdVendedor()));
        regProAtual.setNomeTecnico(buscarTecnico(regProAtual.getCdTecnico()));
        if (regProAtual.getCdMunicipioIbge() != null) {
            regProAtual.setNomeMunicipio(buscarMunicipio(regProAtual.getCdMunicipioIbge()));
        }
        regProAtual.setNomeTipoPagamento(buscarTipoPagamento(regProAtual.getCdTipoPagamento()));
        regProAtual.setNomeCondPag(buscarCondicaoPagamento(regProAtual.getCdCondPagamento()));
        buscarAtendimento();
        pesquisarLocalProposta();
        pesquisarItemProposta();
        numRegLpr = todosLpr.size();
        return regProAtual;
    }

    /**
     * Método para pesquisar o Atendimento que gerou a proposta comercial
     */
    private void buscarAtendimento() {
        CAtendimento cate = new CAtendimento(conexao, su);
        Atendimento ate = new Atendimento();
        String sql = "Select * from gcvatendimento where cd_atendimento = '"
                + regProAtual.getCdAtendimento()
                + "'";
        int numReg = 0;
        try {
            numReg = cate.pesquisar(sql);
        } catch (SQLException ex) {
            Logger.getLogger(CNewProposta.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (numReg > 0) {
            cate.mostrarPesquisa(ate, 0);
            /*
            JOptionPane.showMessageDialog(null, "Mostrando a Pesquisa!\nSql: " + sql
                    +"\nData do Atendimento: "+ate.getDataAtendimento()
                    +"\nHora do Atendimento: "+ate.getHoraAtendimento());
             */
        }
        regProAtual.setDataAtendimento(ate.getDataAtendimento());
        regProAtual.setHoraAtendimento(ate.getHoraAtendimento());
    }

    /**
     * Método para pesquisar os Locais da Proposta Comercial no sistema Retorna
     * os Locais cadastradas para a Proposta Comercial
     */
    private void pesquisarLocalProposta() {
        todosLpr = new ArrayList<LocalProposta>();
        NewPropostaDAO prodao = new NewPropostaDAO(conexao);
        sqlLpr = "select * from gcvlocalproposta where cd_proposta = '"
                + regProAtual.getCdProposta()
                + "' and cd_revisao = '"
                + regProAtual.getCdRevisao()
                + "'";
        rsLpr = prodao.pesquisarLocalProposta(sqlLpr);
        CarregarRegistrosLocalProposta();
    }

    /**
     * Método criado para carregar o conteúdo do resultset do local da proposta
     * para a arrayLista do Local da Proposta
     */
    private void CarregarRegistrosLocalProposta() {
        try {
            while (rsLpr.next()) {
                try {
                    todosLpr.add(new LocalProposta(
                            rsLpr.getString("cd_proposta"),
                            rsLpr.getString("cd_revisao"),
                            rsLpr.getInt("cd_local"),
                            rsLpr.getInt("cd_local_atend"),
                            rsLpr.getString("nome_local"),
                            rsLpr.getDouble("metrage_area"),
                            rsLpr.getDouble("perc_perda"),
                            rsLpr.getString("tipo_piso"),
                            rsLpr.getString("tipo_rodape"),
                            rsLpr.getDouble("metragem_rodape"),
                            rsLpr.getDouble("largura"),
                            rsLpr.getString("comprimento"),
                            rsLpr.getDouble("espessura"),
                            rsLpr.getString("tingimento"),
                            rsLpr.getString("clareamento"),
                            rsLpr.getString("cd_tipoverniz"),
                            rsLpr.getString("cd_essencia"),
                            rsLpr.getDouble("valor_servicos"),
                            rsLpr.getDouble("valor_produtos"),
                            rsLpr.getDouble("valor_adicionais"),
                            rsLpr.getDouble("valor_descontos"),
                            rsLpr.getDouble("valor_outros_descontos"),
                            rsLpr.getDouble("valor_total_bruto"),
                            rsLpr.getDouble("valor_total_liquido"),
                            rsLpr.getString("obs_local"),
                            rsLpr.getString("usuario_cadastro"),
                            rsLpr.getString("data_cadastro"),
                            rsLpr.getString("hora_cadastro"),
                            rsLpr.getString("usuario_modificacao"),
                            rsLpr.getString("data_modificacao"),
                            rsLpr.getString("hora_modificacao"),
                            rsLpr.getString("situacao")));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro na busca do Registro!\nPrograma: LocalPropostaDAO.java\nErr: " + ex);
                }
                ultSeqLpr = rsLpr.getInt("cd_local");
                numRegLpr = todosLpr.size();
            }
            rsLpr.close();
        } catch (SQLException ex) {
            Logger.getLogger(CNewProposta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para mostrar o Local da Proposta na tela para o usuário
     *
     * @param idxAtualLpr Index atual para ser exibido na tela para o usuario
     */
    public LocalProposta mostrarLocalProposta(int idxAtualLpr) {
        regLprAtual = todosLpr.get(idxAtualLpr);
        regLprAtual.setNomeTipoVerniz(buscarTipoVerniz(regLprAtual.getCdTipolVerniz()));
        if (!regLprAtual.getCdEssencia().isEmpty()) {
            regLprAtual.setNomeEssenciaMadeira(buscarEssencia(regLprAtual.getCdEssencia(), "N"));
        }
        return regLprAtual;
    }

    /**
     * Método para pesquisar os Itens da Proposta Comercial no sistema Retorna
     * os itens cadastradas para a Proposta Comercial
     */
    private void pesquisarItemProposta() {
        todosItp = new ArrayList<ItemProposta>();
        NewPropostaDAO prodao = new NewPropostaDAO(conexao);
        sqlItp = "select * from gcvitemproposta where cd_proposta = '"
                + regProAtual.getCdProposta()
                + "' and cd_revisao = '"
                + regProAtual.getCdRevisao()
                + "'";
        rsItp = prodao.pesquisarItemProposta(sqlItp);
        CarregarRegistrosItemProposta();
    }

    /**
     * Método criado para carregar o conteúdo do resultset do Item da proposta
     * para a arrayLista do Item da Proposta
     */
    private void CarregarRegistrosItemProposta() {
        try {
            while (rsItp.next()) {
                try {
                    todosItp.add(new ItemProposta(
                            rsItp.getString("cd_proposta"),
                            rsItp.getString("cd_revisao"),
                            rsItp.getInt("cd_local"),
                            rsItp.getInt("sequencia"),
                            rsItp.getInt("sequencia_atend"),
                            rsItp.getString("cd_material"),
                            rsItp.getString("cd_unidmedida"),
                            rsItp.getDouble("quantidade"),
                            rsItp.getDouble("valor_unit_bruto"),
                            rsItp.getDouble("valor_unit_liquido"),
                            rsItp.getDouble("perc_desc"),
                            rsItp.getDouble("valor_desc"),
                            rsItp.getDouble("total_item_bruto"),
                            rsItp.getDouble("total_item_liquido"),
                            rsItp.getString("tipo_item"),
                            rsItp.getString("obs_item"),
                            rsItp.getString("usuario_cadastro"),
                            rsItp.getString("data_cadastro"),
                            rsItp.getString("hora_cadastro"),
                            rsItp.getString("usuario_modificacao"),
                            rsItp.getString("data_modificacao"),
                            rsItp.getString("hora_modificacao"),
                            rsItp.getString("situacao")));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Erro na busca do Registro!\nPrograma: LocalPropostaDAO.java\nErr: " + ex);
                }
            }
            rsItp.close();
        } catch (SQLException ex) {
            Logger.getLogger(CNewProposta.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Método para pesquisar os totais da proposta e atualizar no objeto da
     * Proposta Comercial
     *
     * @param sqlpro
     */
    public int pesquisarTotaisProposta(String sql) throws SQLException {
        totaisPro = new ArrayList<Proposta>();
        NewPropostaDAO prodao = new NewPropostaDAO(conexao);
        prodao.totalizarItens(totaisPro, sql);
        numRegTotPro = totaisPro.size();
        return numRegTotPro;
    }

    /**
     * Método para atualizar os tatais pesquisado da Proposta Comercial
     *
     * @param modpro
     */
    public void atualizarTotalProposta(Proposta pro) {
        Proposta regAtual = totaisPro.get(0);
        pro.setValorProdutos(regAtual.getValorProdutos());
        pro.setValorServico(regAtual.getValorServico());
        pro.setValorAdicionais(regAtual.getValorAdicionais());
        pro.setValorDescontos(regAtual.getValorDescontos());
        pro.setValorOutrosDescontos(regAtual.getValorOutrosDescontos());
        pro.setValorTotalBruto(regAtual.getValorTotalBruto());
        pro.setValorTotalLiquido(regAtual.getValorTotalLiquido());
    }

    /*
    Aqui começão a sessão para gerar uma nova proposta comercial no sistema.
     */
    /**
     * Método para preparar os dados para gerar uma nova proposta no sistema a
     * partir de uma atendimento realizado
     *
     * @param modate Recebe o objeto que contem o atendimento que será usado
     * para cria a Proposta Comercial
     */
    public String criarProposta(Atendimento modate) throws SQLException {
        this.modate = modate;
        pesquiarLocalAtendimento();
        gerarDadosProposta();
        return this.modate.getCdProposta();
    }

    /**
     * Método para preparar os dados para gerar uma nova proposta no sistema a
     * partir de uma proposta atual, ou seja, duplica a proposta comercial no
     * sistema.
     *
     * @param modpro recebe o objeto da proposta comercial corrente que será
     * duplicada
     */
    public void criarProposta() throws SQLException {
        gerarVersaoProposta();
    }

    /**
     * Método para pesquisar o Local de Atendimento para gerar a Proposta
     * Comercial
     *
     * @param localAte - Objeto array list que irá armazenar os registros dos
     * locais de atendimento.
     */
    private void pesquiarLocalAtendimento() throws SQLException {
        sqlLat = "Select * from gcvlocalatendimento where cd_atendimento = '"
                + modate.getCdAtendimento()
                + "'";
        LocalAtendimentoDAO latdao = new LocalAtendimentoDAO(conexao);
        localAte = new ArrayList<LocalAtendimento>();
        latdao.selecionar(localAte, sqlLat);
    }

    /**
     * Método para pesquisar os Itens do Atendimento para geração da Proposta
     * Comercial
     *
     */
    private void pesquisarItensAtendimento(int localAtendimento) throws SQLException {
        sqlIta = "Select * from gcvitemlocal where cd_atendimento = '"
                + modate.getCdAtendimento()
                + "' and cd_local = "
                + localAtendimento
                + "";
        ItemLocalDAO itadao = new ItemLocalDAO(conexao);
        itemAte = new ArrayList<ItemLocal>();
        itadao.selecionar(itemAte, sqlIta);
    }

    /**
     * Método para Migrar os dados dos objetos de Atendimento para os objetos da
     * Proposta Comercial
     */
    private void gerarDadosProposta() throws SQLException {
        DataSistema dat = new DataSistema();
        NewPropostaDAO prodao = new NewPropostaDAO(conexao);
        dat.setData(data);
        data = dat.getData();
        Proposta pro = new Proposta();
        List<LocalProposta> lpr = new ArrayList<LocalProposta>();
        List<ItemProposta> itp = new ArrayList<ItemProposta>();
        migraProposta(pro);
        migrarLocalProposta(lpr, itp, pro.getCdProposta(), pro.getCdRevisao());
        prodao.criarProposta(pro, lpr, itp);
        modate.setCdProposta(pro.getCdProposta());
        JOptionPane.showMessageDialog(null, "Proposta Criada com Suscesso!\n" + modate.getCdProposta());
    }

    /**
     * Método para criar uma versão nova da proposta comercial corrente
     */
    private void gerarVersaoProposta() throws SQLException {
        DataSistema dat = new DataSistema();
        NewPropostaDAO prodao = new NewPropostaDAO(conexao);
        dat.setData(data);
        data = dat.getData();
        Proposta pro = new Proposta();
        pro = regProAtual;
        pro.setDataEnvio(null);
        pro.setSituacao("NE");
        ultRevisaoPro++;
        pesquisarItemProposta();
        pro.setCdRevisao(String.valueOf(ultRevisaoPro));
        prodao.criarProposta(pro, todosLpr, todosItp);
        JOptionPane.showMessageDialog(null, "Proposta Copiada com Sucesso agora!");
    }

    /**
     * Método para prepara o objeto da Proposta Comercial
     *
     * @param pro
     */
    private void migraProposta(Proposta pro) {
        HoraSistema hs = new HoraSistema();
        CBuscarSequencia bs = new CBuscarSequencia(su, TABELA, 8);
        pro.setCdProposta(bs.getRetorno());
        pro.setCdRevisao("0");
        pro.setCdAtendimento(modate.getCdAtendimento());
        pro.setNomeRazaoSocial(modate.getNomeRazaoSocial());
        String tipoPessoa = modate.getTipoPessoa();
        switch (tipoPessoa) {
            case "1":
                pro.setTipoPessoa("F");
                break;
            case "2":
                pro.setTipoPessoa("J");
                break;
            default:
                pro.setTipoPessoa(" ");
                break;
        }
        pro.setTelefone(modate.getTelefone());
        pro.setCelular(modate.getCelular());
        pro.setEmail(modate.getEmail());
        pro.setTipoLogradouro(modate.getTipoLogradouro());
        pro.setLogradouro(modate.getLogradouro());
        pro.setNumero(modate.getNumero());
        pro.setComplemento(modate.getComplemento());
        pro.setBairro(modate.getBairro());
        pro.setCdMunicipioIbge(modate.getCdMunicipioIbge());
        pro.setSiglaUf(modate.getSiglaUf());
        pro.setCdCep(modate.getCdCep());
        String tipoEndereco = modate.getTipoEndereco();
        switch (tipoEndereco) {
            case "1":
                pro.setTipoEndereco("C");
                break;
            case "2":
                pro.setTipoEndereco("R");
                break;
            default:
                pro.setTipoEndereco(" ");
                break;
        }
        pro.setTipoPedido("0");
        pro.setCdTipoPagamento("");
        pro.setCdCondPagamento("");
        pro.setCdPedido("");
        pro.setValorServico(modate.getValorServico());
        pro.setValorProdutos(modate.getValorProdutos());
        pro.setValorAdicionais(modate.getValorAdicionais());
        pro.setValorDescontos(0.00);
        pro.setValorOutrosDescontos(0.00);
        pro.setValorTotalBruto(modate.getValorTotalBruto());
        pro.setValorTotalLiquido(modate.getValorTotalBruto());
        pro.setUsuarioCadastro(su.getUsuarioConectado());
        pro.setDataCadastro(data);
        pro.setHoraCadastro(hs.getHora());
        pro.setSituacao("NE");
    }

    /**
     * Método para preparar o objeto dos Locais da Proposta Comercial
     *
     * @param lpr
     * @param cdProposta
     */
    private void migrarLocalProposta(List<LocalProposta> lpr, List<ItemProposta> itp, String cdProposta, String cdRevisao) throws SQLException {
        HoraSistema hs = new HoraSistema();
        int sequenciaLocal = 1;
        LocalAtendimento modlat = new LocalAtendimento();
        LocalProposta modlpr;
        for (int i = 0; i < localAte.size(); i++) {
            modlat = localAte.get(i);
            /*
            JOptionPane.showMessageDialog(null, "Sequencia Local: " + modlat.getCdLocal()
                    + "\nLocal: " + modlat.getNomeLocal());
             */
            modlpr = new LocalProposta();
            modlpr.setCdProposta(cdProposta);
            modlpr.setCdRevisao(cdRevisao);
            modlpr.setCdAtendimento(modlat.getCdAtendimento());
            modlpr.setCdLocal(sequenciaLocal);
            modlpr.setCdLocalAtend(modlat.getCdLocal());
            modlpr.setNomeLocal(modlat.getNomeLocal());
            modlpr.setMetragemArea(modlat.getMetragemArea());
            modlpr.setPercPerda(modlat.getPercPerda());
            modlpr.setTipoPiso(modlat.getTipoPiso());
            modlpr.setTipoRodape(modlat.getTipoRodape());
            modlpr.setMetragemRodape(modlat.getMetragemRodape());
            modlpr.setLargura(modlat.getLargura());
            modlpr.setComprimento(modlat.getComprimento());
            modlpr.setEspessura(modlat.getEspessura());
            modlpr.setTingimento(modlat.getTingimento());
            modlpr.setClareamento(modlat.getClareamento());
            modlpr.setCdTipoVerniz(modlat.getCdTipolVerniz());
            modlpr.setCdEssencia(modlat.getCdEssencia());
            modlpr.setValorServico(modlat.getValorServico());
            modlpr.setValorProdutos(modlat.getValorProdutos());
            modlpr.setValorAdicionais(modlat.getValorAdicionais());
            modlpr.setValorDescontos(0.000);
            modlpr.setValorTotalBruto(modlat.getValorTotalBruto());
            modlpr.setObs(modlat.getObs());
            modlpr.setUsuarioCadastro(su.getUsuarioConectado());
            modlpr.setDataCadastro(data);
            modlpr.setHoraCadastro(hs.getHora());
            modlpr.setSituacao("NE");
            lpr.add(modlpr);
            pesquisarItensAtendimento(modlat.getCdLocal());
            migrarItemProposta(itp, cdProposta, cdRevisao, sequenciaLocal);
            sequenciaLocal += 1;
        }
    }

    /**
     * Método para prepara o objeto dos itens da Proposta Comercial
     *
     * @param itp
     * @param cdProposta
     * @param cdLocal
     */
    private void migrarItemProposta(List<ItemProposta> itp, String cdProposta, String cdRevisao, int cdLocal) {
        HoraSistema hs = new HoraSistema();
        ItemLocal moditl = new ItemLocal();
        ItemProposta moditp;
        int sequenciaItens = 1;
        for (int j = 0; j < itemAte.size(); j++) {
            moditl = itemAte.get(j);
            moditp = new ItemProposta();
            moditp.setCdProposta(cdProposta);
            moditp.setCdRevisao(cdRevisao);
            moditp.setCdLocal(cdLocal);
            moditp.setSequencia(sequenciaItens);
            moditp.setSequenciaAtend(moditl.getSequencia());
            moditp.setCdMaterial(moditl.getCdMaterial());
            moditp.setCdUnidmedida(moditl.getCdUnidmedida());
            moditp.setQuantidade(moditl.getQuantidade());
            moditp.setValorUnitBruto(moditl.getValorUnitBruto());
            moditp.setPercDesconto(0.000);
            moditp.setValorDescontos(0.00);
            moditp.setValorTotalItemBruto(moditl.getValorTotalItemBruto());
            moditp.setValorTotalItemLiquido(moditl.getValorTotalItemBruto());
            moditp.setUsuarioCadastro(su.getUsuarioConectado());
            moditp.setDataCadastro(data);
            moditp.setHoraCadastro(hs.getHora());
            moditp.setSituacao("NE");
            moditp.setTipoItem(moditl.getTipoItem());
            moditp.setObsItem(moditl.getObsItem());
            sequenciaItens += 1;
            itp.add(moditp);
        }
    }

    /**
     * Método que retorna o número de locais cadastrado para a proposta
     *
     * @return the numRegLpr
     */
    public int getNumRegLpr() {
        return numRegLpr;
    }

    /**
     * Método que retornar o número de Registros de Itens da Proposta
     *
     * @return the numRegItp
     */
    public int getNumRegItp() {
        return numRegItp;
    }

    // Método par buscar nome do Municipio
    private String buscarMunicipio(String cdMunicipioIbge) {
        Municipios mu = new Municipios();
        try {
            CMunicipios cmu = new CMunicipios(conexao);
            String sqlcmu = "SELECT * FROM PGSMUNICIPIO WHERE CD_MUNICIPIO_IBGE = '" + cdMunicipioIbge + "'";

            if (cmu.pesquisar(sqlcmu) > 0) {
                cmu.mostrarPesquisa(mu, 0);
            }else{
                return "";
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome o Município!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return mu.getNomeMunicipio();
    }

    // Método par buscar nome da UF
    private String buscarUF(String cdSiglaUF) {
        UnidadeFederacao uf = new UnidadeFederacao();
        try {
            CUnidadeFederacao cuf = new CUnidadeFederacao(conexao);
            String sqlcuf = "SELECT * FROM PGSUF WHERE SIGLA_UF = '" + cdSiglaUF + "'";
            cuf.pesquisar(sqlcuf);
            cuf.mostrarPesquisa(uf, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca do Nome da UF!\nPrograma CTecnicos.\nErro: " + ex);
        }
        return String.valueOf(uf.getUfIbge());
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

    // Método par buscar nome da essencia e se forma código
    private String buscarEssencia(String cdEssencia, String ret) {
        EssenciaProdutos ess = new EssenciaProdutos();
        try {
            CEssenciaProdutos pess = new CEssenciaProdutos(conexao);
            String sqless = "SELECT * FROM GCSESSENCIA WHERE CD_ESSENCIA = '" + cdEssencia + "'";
            pess.pesquisar(sqless);
            pess.mostrarPesquisa(ess, 0);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Erro na busca da Essencia de Produtos!\nPrograma: GCSROT0091\nErro: " + ex);
        }
        if ("N".equals(ret)) {
            return ess.getNomeEssencia();
        } else {
            return ess.getGerarCodigo();
        }
    }
}
