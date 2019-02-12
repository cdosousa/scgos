/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GCSCA0090
 */
package br.com.gcs.visao;

import br.com.controle.CBuscarSequencia;
import br.com.gcs.controle.CMateriais;
import br.com.gcs.dao.MateriaisDAO;
import br.com.gcs.modelo.Materiais;
import br.com.gfc.visao.PesquisarContasContabeis;
import br.com.gfr.visao.PesquisarCentroCustos;
import br.com.gfr.visao.PesquisarNCMProduto;
import br.com.gfr.visao.PesquisarOrigemProduto;
import br.com.modelo.DataSistema;
import br.com.modelo.DefineCampoData;
import br.com.modelo.DefineCampoDecimal;
import br.com.modelo.DefineCampoInteiro;
import br.com.modelo.FormatarValor;
import br.com.modelo.SessaoUsuario;
import java.awt.event.KeyEvent;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 04/10/2017
 */
public class ManterMateriais extends javax.swing.JFrame {

    private Materiais regCorr;
    private List< Materiais> resultado;
    private CMateriais pmat;
    private Materiais sqlmat;
    private static Connection conexao;
    private int numReg;
    private int idxCorr;
    private String sql;
    private String oper;
    private static SessaoUsuario su;
    private NumberFormat formato;

    public ManterMateriais(SessaoUsuario su, Connection conexao) {
        formato = NumberFormat.getInstance();
        formato.setMinimumFractionDigits(2);
        this.su = su;
        this.conexao = conexao;
        oper = "N";
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        formatarCampos();
        limparTela();
        setLocationRelativeTo(null);
        this.dispose();
    }

    // construtor padrão
    public ManterMateriais() {

    }

    // método para setar formato do campo
    public void formatarCampos() {
        jForPesoLiquido.setDocument(new DefineCampoDecimal());
        jForPesoBruto.setDocument(new DefineCampoDecimal());
        jForUltimoPreco.setDocument(new DefineCampoDecimal());
        jForEstoqueMinimo.setDocument(new DefineCampoDecimal());
        jForLoteMinimo.setDocument(new DefineCampoDecimal());
        jForLoteMultiplo.setDocument(new DefineCampoDecimal());
        jForComprimento.setDocument(new DefineCampoDecimal());
        jForLargura.setDocument(new DefineCampoDecimal());
        jForEspessura.setDocument(new DefineCampoDecimal());
        jForCdProduto.setDocument(new DefineCampoInteiro());
        jForCdSequencial.setDocument(new DefineCampoInteiro());
        jForUltimaCompra.setDocument(new DefineCampoData());
    }

    // método para limpar tela
    public void limparTela() {
        jForCdProduto.setText("");
        jForCdSequencial.setText("");
        jTexDescricao.setText("");
        jComSituacao.setSelectedIndex(0);
        jTexCdGrupo.setText("");
        jTexNomeGrupo.setText("");
        jTexCdSubGrupo.setText("");
        jTexNomeSubGrupo.setText("");
        jTexCdCategoria.setText("");
        jTexNomeCategoria.setText("");
        jTexCdMarca.setText("");
        jTexNomeMarca.setText("");
        jTexCdClasse.setText("");
        jTexNomeClasse.setText("");
        jTexCdEssencia.setText("");
        jTexNomeEssencia.setText("");
        jTexCdUnidadeMedida.setText("");
        jTexNomeUnidMedida.setText("");
        jComTipoProduto.setSelectedIndex(0);
        jTexCdArmPadrao.setText("");
        jTexNomeArmazPadrao.setText("");
        jForPesoLiquido.setText("0,00");
        jForPesoBruto.setText("0,00");
        jForUltimoPreco.setText("0,00");
        jForUltimaCompra.setText("");
        jTexCdCcusto.setText("");
        jTexNomeCCusto.setText("");
        jTexCdCtaContabil.setText("");
        jTexNomeCtaContabil.setText("");
        jForEstoqueMinimo.setText("0,00");
        jForLoteMinimo.setText("0,00");
        jForLoteMultiplo.setText("0,00");
        jForComprimento.setText("0,00");
        jForLargura.setText("0,00");
        jForEspessura.setText("0,00");
        jTexCdOrigemProduto.setText("");
        jTexCdNCM.setText("");
        jTexAreaDescricaoComercial.setText("");
        jTexCadastradoPor.setText("");
        jForDataCadastro.setText("");
        jForDataModificacao.setText("");
        jTexRegAtual.setText("");
        jTexRegTotal.setText("");
        idxCorr = 0;
        numReg = 0;
        resultado = null;
        regCorr = null;
        liberarCampos();
        jForCdProduto.setEditable(true);
        jForCdSequencial.setEditable(true);
    }

    // método para definir o tipo de pesquisa
    public void pesquisar() {
        pmat = new CMateriais(conexao);
        sqlmat = new Materiais();
        try {
            numReg = pmat.pesquisar(sql, false);
            idxCorr = +1;
            if (numReg > 0) {
                upRegistros();
            } else {
                JOptionPane.showMessageDialog(null, "Nenhum registro encontrado!");
            }
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Argumento inválido na busca\nErr: " + iae.getCause());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Pesquisar os Registro\nErr: " + ex);
        }
    }

    // atualizar registros na tela
    public void upRegistros() {
        jTexRegTotal.setText(Integer.toString(numReg));
        jTexRegAtual.setText(Integer.toString(idxCorr));
        pmat.mostrarPesquisa(sqlmat, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jForCdProduto.setText(sqlmat.getCdMaterial());
        jForCdSequencial.setText(sqlmat.getCdSequencial());
        jTexDescricao.setText(sqlmat.getNomeMaterial());
        jComSituacao.setSelectedIndex(Integer.parseInt(sqlmat.getSituacao()));
        jTexCdGrupo.setText(sqlmat.getCdGrupo());
        jTexNomeGrupo.setText(sqlmat.getNomeGrupo().trim());
        jTexCdSubGrupo.setText(sqlmat.getCdSubGrupo());
        jTexNomeSubGrupo.setText(sqlmat.getNomeSubGrupo().trim());
        jTexCdCategoria.setText(sqlmat.getCdCategoria());
        jTexNomeCategoria.setText(sqlmat.getNomeCategoria().trim());
        jTexCdMarca.setText(sqlmat.getCdMarca());
        jTexNomeMarca.setText(sqlmat.getNomeMarca().trim());
        jTexCdClasse.setText(sqlmat.getCdClasse());
        jTexNomeClasse.setText(sqlmat.getNomeClasse().trim());
        jTexCdEssencia.setText(sqlmat.getCdEssencia());
        jTexNomeEssencia.setText(sqlmat.getNomeEssencia().trim());
        jTexCdUnidadeMedida.setText(sqlmat.getCdUnidMedida());
        jTexNomeUnidMedida.setText(sqlmat.getNomeUnidMedida().trim());
        jComTipoProduto.setSelectedIndex(Integer.parseInt(sqlmat.getTipoProduto()));
        jTexCdArmPadrao.setText(sqlmat.getCdArmazem());
        jTexNomeArmazPadrao.setText(sqlmat.getNomeArmazPadrao().trim());
        jForPesoLiquido.setText(String.valueOf(sqlmat.getPesoLiquido()).trim());
        jForPesoBruto.setText(String.valueOf(sqlmat.getPesoBruto()).trim());
        jForUltimoPreco.setText(String.valueOf(sqlmat.getUltPrecoCompra()).trim());
        jForUltimaCompra.setText(sqlmat.getUltCompra());
        jTexCdCcusto.setText(sqlmat.getCdCcusto());
        jTexNomeCCusto.setText(sqlmat.getNomeCCusto().trim());
        jTexCdCtaContabil.setText(sqlmat.getCdCtaContabeReduz());
        jTexNomeCtaContabil.setText(sqlmat.getNomeCtaContabil().trim());
        jForEstoqueMinimo.setText(String.valueOf(sqlmat.getEstoqueMinimo()).trim());
        jForLoteMinimo.setText(String.valueOf(sqlmat.getLoteMinimo()).trim());
        jForLoteMultiplo.setText(String.valueOf(sqlmat.getLoteMultiplo()).trim());
        jForComprimento.setText(String.valueOf(sqlmat.getComprimento()).trim());
        jForLargura.setText(String.valueOf(sqlmat.getLargura()).trim());
        jForEspessura.setText(String.valueOf(sqlmat.getEspessura()).trim());
        jTexCdOrigemProduto.setText(sqlmat.getCdOrigemCsta());
        jTexCdNCM.setText(sqlmat.getCdNcm());
        jTexAreaDescricaoComercial.setText(sqlmat.getDescricaoComercial());
        jTexCadastradoPor.setText(sqlmat.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(sqlmat.getDataCadastro())));
        if (sqlmat.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(sqlmat.getDataModificacao())));
        }

        // Habilitando/Desabilitando botões de navegação de registros
        if (numReg > idxCorr) {
            jButProximo.setEnabled(true);
        } else {
            jButProximo.setEnabled(false);
        }
        if (idxCorr > 1) {
            jButAnterior.setEnabled(true);
        } else {
            jButAnterior.setEnabled(false);
        }
    }

    //Bloquear os campos da tela
    public void bloquearCampos() {
        jForCdProduto.setEditable(false);
        jForCdSequencial.setEditable(false);
        jTexDescricao.setEditable(false);
        jComSituacao.setEditable(false);
        jTexCdGrupo.setEditable(false);
        jTexCdSubGrupo.setEditable(false);
        jTexCdCategoria.setEditable(false);
        jTexCdMarca.setEditable(false);
        jTexCdClasse.setEditable(false);
        jTexCdEssencia.setEditable(false);
        jTexCdUnidadeMedida.setEditable(false);
        jComTipoProduto.setEditable(false);
        jTexCdArmPadrao.setEditable(false);
        jForPesoLiquido.setEditable(false);
        jForPesoBruto.setEditable(false);
        jForUltimoPreco.setEditable(false);
        jForUltimaCompra.setEditable(false);
        jTexCdCcusto.setEditable(false);
        jTexCdCtaContabil.setEditable(false);
        jForEstoqueMinimo.setEditable(false);
        jForLoteMinimo.setEditable(false);
        jForLoteMultiplo.setEditable(false);
        jForComprimento.setEditable(false);
        jForLargura.setEditable(false);
        jForEspessura.setEditable(false);
        jTexCdOrigemProduto.setEditable(false);
        jTexCdNCM.setEditable(false);
        jTexAreaDescricaoComercial.setEditable(false);
    }

    //Loberar os campos da tela para atualização
    public void liberarCampos() {
        jTexDescricao.setEditable(true);
        jComSituacao.setEditable(true);
        jTexCdMarca.setEditable(true);
        jTexCdClasse.setEditable(true);
        jTexNomeEssencia.setEditable(true);
        jTexCdUnidadeMedida.setEditable(true);
        jComTipoProduto.setEditable(true);
        jTexCdArmPadrao.setEditable(true);
        jForPesoLiquido.setEditable(true);
        jForPesoBruto.setEditable(true);
        jForUltimoPreco.setEditable(true);
        jForUltimaCompra.setEditable(true);
        jTexCdCcusto.setEditable(true);
        jTexCdCtaContabil.setEditable(true);
        jForEstoqueMinimo.setEditable(true);
        jForLoteMinimo.setEditable(true);
        jForLoteMultiplo.setEditable(true);
        jForComprimento.setEditable(true);
        jForLargura.setEditable(true);
        jForEspessura.setEditable(true);
        jTexCdOrigemProduto.setEditable(true);
        jTexCdNCM.setEditable(true);
        jTexAreaDescricaoComercial.setEditable(true);
    }

    // método para cria novo registro
    public void novoRegistro() {
        limparTela();
        liberarCampos();
        jForCdProduto.setEditable(true);
        jTexCdGrupo.setEditable(true);
        jTexCdSubGrupo.setEditable(true);
        jTexCdCategoria.setEditable(true);
        jTexCdEssencia.setEditable(true);
        jForCdProduto.requestFocus();
    }

    // metodo dar zoon no campo grupo de produto
    public void zoomGrupo() {
        PesquisarGrupoProdutos zoom = new PesquisarGrupoProdutos(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdGrupo.setText(zoom.getSelec1().trim());
        jTexNomeGrupo.setText(zoom.getSelec2().trim());
    }

    // metodo dar zoon no campo sub grupo de produto
    public void zoomSubGrupo() {
        PesquisarSubGrupoProdutos zoom = new PesquisarSubGrupoProdutos(new JFrame(), true, jTexCdGrupo.getText().trim(), conexao);
        zoom.setVisible(true);
        jTexCdSubGrupo.setText(zoom.getSelec1().trim());
        jTexNomeSubGrupo.setText(zoom.getSelec2().trim());
    }

    // metodo dar zoon no campo categoria de produto
    public void zoomCategoria() {
        PesquisarCategoriaProdutos zoom = new PesquisarCategoriaProdutos(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdCategoria.setText(zoom.getSelec1().trim());
        jTexNomeCategoria.setText(zoom.getSelec2().trim());
    }

    // metodo dar zoon no campo Marca de produto
    public void zoomMarca() {
        PesquisarMarcaProdutos zoom = new PesquisarMarcaProdutos(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdMarca.setText(zoom.getSelec1().trim());
        jTexNomeMarca.setText(zoom.getSelec2().trim());
    }

    // metodo dar zoon no campo Classe de produto
    public void zoomClasse() {
        PesquisarClasseProdutos zoom = new PesquisarClasseProdutos(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdClasse.setText(zoom.getSelec1().trim());
        jTexNomeClasse.setText(zoom.getSelec2().trim());
    }

    // metodo dar zoon no campo Essencia de produto
    public void zoomEssencia() {
        PesquisarEssenciaProdutos zoom = new PesquisarEssenciaProdutos(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdEssencia.setText(zoom.getSelec1().trim());
        jTexNomeEssencia.setText(zoom.getSelec2().trim());
    }

    // metodo dar zoon no campo Unidade de Medida de produto
    public void zoomUnidadesMedida() {
        PesquisarUnidadesMedida zoom = new PesquisarUnidadesMedida(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdUnidadeMedida.setText(zoom.getSelec1().trim());
        jTexNomeUnidMedida.setText(zoom.getSelec2().trim());
    }

    // metodo dar zoon no campo Local de Armazenagem de produto
    public void zoomArmazens() {
        PesquisarArmazens zoom = new PesquisarArmazens(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdArmPadrao.setText(zoom.getSelec1().trim());
        jTexNomeArmazPadrao.setText(zoom.getSelec2().trim());
    }

    // metodo dar zoon no campo Centro de Custo de produto
    public void zoomCentroCustos() {
        PesquisarCentroCustos zoom = new PesquisarCentroCustos(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdCcusto.setText(zoom.getSelec1().trim());
        jTexNomeCCusto.setText(zoom.getSelec2().trim());
    }

    // metodo dar zoon no campo Contas Contábeis de produto
    public void zoomContasContabeis() {
        PesquisarContasContabeis zoom = new PesquisarContasContabeis(new JFrame(), true, "P");
        zoom.setVisible(true);
        jTexCdCtaContabil.setText(zoom.getSelec1().trim());
        jTexNomeCtaContabil.setText(zoom.getSelec2().trim());
    }

    // metodo dar zoon no campo de Origem de produto
    public void zoomOrigemProduto() {
        PesquisarOrigemProduto zoom = new PesquisarOrigemProduto(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdOrigemProduto.setText(zoom.getSelec1().trim());
    }

    // metodo dar zoon no campo de NCM de produto
    public void zoomNCMProduto() {
        PesquisarNCMProduto zoom = new PesquisarNCMProduto(new JFrame(), true, "P", conexao);
        zoom.setVisible(true);
        jTexCdNCM.setText(zoom.getSelec1().trim());
    }

    // método para verificar tecla pressionada
    public String verificaTecla(KeyEvent evt) {
        KeyEvent ev = (KeyEvent) evt;
        return KeyEvent.getKeyText(evt.getKeyCode());
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
        jButCopiar = new javax.swing.JButton();
        jButSalvar = new javax.swing.JButton();
        jButCancelar = new javax.swing.JButton();
        jButExcluir = new javax.swing.JButton();
        jButPesquisar = new javax.swing.JButton();
        jButAnterior = new javax.swing.JButton();
        jButProximo = new javax.swing.JButton();
        jButImprimir = new javax.swing.JButton();
        jButSair = new javax.swing.JButton();
        jPanPrincipal = new javax.swing.JPanel();
        jPanGeral = new javax.swing.JPanel();
        jLabCdProduto = new javax.swing.JLabel();
        jForCdProduto = new javax.swing.JFormattedTextField();
        jLabDescricao = new javax.swing.JLabel();
        jTexDescricao = new javax.swing.JTextField();
        jLabSituacao = new javax.swing.JLabel();
        jComSituacao = new javax.swing.JComboBox<>();
        jSeparator1 = new javax.swing.JSeparator();
        jLabGrupo = new javax.swing.JLabel();
        jLabSuGrupo = new javax.swing.JLabel();
        jLabCategoria = new javax.swing.JLabel();
        jLabMarca = new javax.swing.JLabel();
        jLabClasse = new javax.swing.JLabel();
        jLabEssencia = new javax.swing.JLabel();
        jLabUnidMedida = new javax.swing.JLabel();
        jTexCdGrupo = new javax.swing.JTextField();
        jTexNomeGrupo = new javax.swing.JTextField();
        jTexCdSubGrupo = new javax.swing.JTextField();
        jTexNomeSubGrupo = new javax.swing.JTextField();
        jTexCdCategoria = new javax.swing.JTextField();
        jTexNomeCategoria = new javax.swing.JTextField();
        jTexCdMarca = new javax.swing.JTextField();
        jTexNomeMarca = new javax.swing.JTextField();
        jTexCdClasse = new javax.swing.JTextField();
        jTexNomeClasse = new javax.swing.JTextField();
        jTexCdEssencia = new javax.swing.JTextField();
        jTexNomeEssencia = new javax.swing.JTextField();
        jTexCdUnidadeMedida = new javax.swing.JTextField();
        jTexNomeUnidMedida = new javax.swing.JTextField();
        jPanEstoque = new javax.swing.JPanel();
        jLabLocalArmazPadrao = new javax.swing.JLabel();
        jTexCdArmPadrao = new javax.swing.JTextField();
        jTexNomeArmazPadrao = new javax.swing.JTextField();
        jLabPesoLiquido = new javax.swing.JLabel();
        jLabUltPreco = new javax.swing.JLabel();
        jLabCCusto = new javax.swing.JLabel();
        jLabCtaContabil = new javax.swing.JLabel();
        jForUltimoPreco = jForUltimoPreco = new FormatarValor((FormatarValor.MOEDA));
        jLabUltCompra = new javax.swing.JLabel();
        jForUltimaCompra = new javax.swing.JFormattedTextField();
        jTexCdCcusto = new javax.swing.JTextField();
        jTexCdCtaContabil = new javax.swing.JTextField();
        jTexNomeCCusto = new javax.swing.JTextField();
        jTexNomeCtaContabil = new javax.swing.JTextField();
        jLabEstoqueMinimo = new javax.swing.JLabel();
        jLabLoteMinimo = new javax.swing.JLabel();
        jLabLoteMultiplo = new javax.swing.JLabel();
        jForEstoqueMinimo = jForEstoqueMinimo = new FormatarValor((FormatarValor.NUMERO));
        jForLoteMinimo = jForLoteMinimo = new FormatarValor((FormatarValor.NUMERO));
        jForLoteMultiplo = jForLoteMultiplo = new FormatarValor((FormatarValor.NUMERO));
        jLabPesoBruto = new javax.swing.JLabel();
        jForPesoBruto = jForPesoBruto = new FormatarValor((FormatarValor.PESO));
        jForPesoLiquido = jForPesoLiquido = new FormatarValor((FormatarValor.PESO));
        jLabTipoProduto = new javax.swing.JLabel();
        jComTipoProduto = new javax.swing.JComboBox<>();
        jPanEngenharia = new javax.swing.JPanel();
        jPanDimenssoes = new javax.swing.JPanel();
        jLabComprimento = new javax.swing.JLabel();
        jForComprimento = new FormatarValor((FormatarValor.PESO));
        jLabLargura = new javax.swing.JLabel();
        jForLargura = new FormatarValor((FormatarValor.PESO));
        jLabEspessura = new javax.swing.JLabel();
        jForEspessura = new FormatarValor((FormatarValor.PESO));
        jPanRodape = new javax.swing.JPanel();
        jTexRegAtual = new javax.swing.JTextField();
        jLabReg = new javax.swing.JLabel();
        jTexRegTotal = new javax.swing.JTextField();
        jLabCadastradoPor = new javax.swing.JLabel();
        jTexCadastradoPor = new javax.swing.JTextField();
        jLabDataCadastro = new javax.swing.JLabel();
        jForDataCadastro = new javax.swing.JFormattedTextField();
        jLabDataModificacao = new javax.swing.JLabel();
        jForDataModificacao = new javax.swing.JFormattedTextField();
        jPanFiscal = new javax.swing.JPanel();
        jLabOrigemProduto = new javax.swing.JLabel();
        jTexCdOrigemProduto = new javax.swing.JTextField();
        jLabNCM = new javax.swing.JLabel();
        jTexCdNCM = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jForCdSequencial = new javax.swing.JFormattedTextField();
        jLabDescricaoComercial = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTexAreaDescricaoComercial = new javax.swing.JTextArea();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuNovo = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Materiais");

        jTooMenuFerramentas.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jTooMenuFerramentas.setRollover(true);

        jButNovo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButNovo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Addthis-32.png"))); // NOI18N
        jButNovo.setText("Novo");
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

        jButCopiar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButCopiar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Copy-32x32.png"))); // NOI18N
        jButCopiar.setText("Copiar");
        jButCopiar.setFocusable(false);
        jButCopiar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButCopiar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButCopiar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButCopiarActionPerformed(evt);
            }
        });
        jTooMenuFerramentas.add(jButCopiar);

        jButSalvar.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jButSalvar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/br/com/imagens/Ok-32.PNG"))); // NOI18N
        jButSalvar.setText("Salvar");
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
        jButProximo.setText("Proximo");
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

        jPanPrincipal.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jPanGeral.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanGeral.setToolTipText("");

        jLabCdProduto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCdProduto.setText("Código:");

        jForCdProduto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#################"))));

        jLabDescricao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabDescricao.setText("Descrição:");

        jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabSituacao.setText("Situação:");

        jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ativo", "Inativo", "Pendente", "Obsoleto" }));

        jLabGrupo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabGrupo.setText("Grupo:");

        jLabSuGrupo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabSuGrupo.setText("Sub Grupo:");

        jLabCategoria.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCategoria.setText("Categoria:");

        jLabMarca.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabMarca.setText("Marca:");

        jLabClasse.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabClasse.setText("Classe:");

        jLabEssencia.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabEssencia.setText("Essência:");

        jLabUnidMedida.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabUnidMedida.setText("Unidade de Medida:");

        jTexCdGrupo.setToolTipText("Pressione F-5 para pesquisar");
        jTexCdGrupo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdGrupoKeyPressed(evt);
            }
        });

        jTexNomeGrupo.setEditable(false);
        jTexNomeGrupo.setBackground(new java.awt.Color(255, 255, 255));
        jTexNomeGrupo.setEnabled(false);

        jTexCdSubGrupo.setToolTipText("Pressione F-5 para pesquisar");
        jTexCdSubGrupo.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdSubGrupoKeyPressed(evt);
            }
        });

        jTexNomeSubGrupo.setEditable(false);
        jTexNomeSubGrupo.setBackground(new java.awt.Color(255, 255, 255));
        jTexNomeSubGrupo.setEnabled(false);

        jTexCdCategoria.setToolTipText("Pressione F-5 para pesquisar");
        jTexCdCategoria.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdCategoriaKeyPressed(evt);
            }
        });

        jTexNomeCategoria.setEditable(false);
        jTexNomeCategoria.setBackground(new java.awt.Color(255, 255, 255));
        jTexNomeCategoria.setEnabled(false);

        jTexCdMarca.setToolTipText("Pressione F-5 para pesquisar");
        jTexCdMarca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdMarcaKeyPressed(evt);
            }
        });

        jTexNomeMarca.setEditable(false);
        jTexNomeMarca.setBackground(new java.awt.Color(255, 255, 255));
        jTexNomeMarca.setEnabled(false);

        jTexCdClasse.setToolTipText("Pressione F-5 para pesquisar");
        jTexCdClasse.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdClasseKeyPressed(evt);
            }
        });

        jTexNomeClasse.setEditable(false);
        jTexNomeClasse.setBackground(new java.awt.Color(255, 255, 255));
        jTexNomeClasse.setEnabled(false);

        jTexCdEssencia.setToolTipText("Pressione F-5 para pesquisar");
        jTexCdEssencia.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdEssenciaKeyPressed(evt);
            }
        });

        jTexNomeEssencia.setEditable(false);
        jTexNomeEssencia.setBackground(new java.awt.Color(255, 255, 255));
        jTexNomeEssencia.setEnabled(false);

        jTexCdUnidadeMedida.setToolTipText("Pressione F-5 para pesquisar");
        jTexCdUnidadeMedida.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdUnidadeMedidaKeyPressed(evt);
            }
        });

        jTexNomeUnidMedida.setEditable(false);
        jTexNomeUnidMedida.setBackground(new java.awt.Color(255, 255, 255));
        jTexNomeUnidMedida.setEnabled(false);

        jPanEstoque.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Estoque", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabLocalArmazPadrao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabLocalArmazPadrao.setText("Local Armaz. Padrão:");

        jTexCdArmPadrao.setToolTipText("Pressione F-5 para pesquisar");
        jTexCdArmPadrao.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdArmPadraoKeyPressed(evt);
            }
        });

        jTexNomeArmazPadrao.setEditable(false);
        jTexNomeArmazPadrao.setBackground(new java.awt.Color(255, 255, 255));
        jTexNomeArmazPadrao.setEnabled(false);

        jLabPesoLiquido.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPesoLiquido.setText("Peso Liquido:");

        jLabUltPreco.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabUltPreco.setText("Ultimo Preço Compra:");

        jLabCCusto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCCusto.setText("Centro Custo:");

        jLabCtaContabil.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabCtaContabil.setText("Conta Contábil:");

        jForUltimoPreco.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jForUltimoPreco.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabUltCompra.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabUltCompra.setText("Ultima Compra:");

        jForUltimaCompra.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
        jForUltimaCompra.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jTexCdCcusto.setToolTipText("Pressione F-5 para pesquisar");
        jTexCdCcusto.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdCcustoKeyPressed(evt);
            }
        });

        jTexCdCtaContabil.setToolTipText("Pressione F-5 para pesquisar");
        jTexCdCtaContabil.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jTexCdCtaContabilKeyPressed(evt);
            }
        });

        jTexNomeCCusto.setEditable(false);
        jTexNomeCCusto.setBackground(new java.awt.Color(255, 255, 255));
        jTexNomeCCusto.setEnabled(false);

        jTexNomeCtaContabil.setEditable(false);
        jTexNomeCtaContabil.setBackground(new java.awt.Color(255, 255, 255));
        jTexNomeCtaContabil.setEnabled(false);

        jLabEstoqueMinimo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabEstoqueMinimo.setText("Estoque Mínimo:");

        jLabLoteMinimo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabLoteMinimo.setText("Lote Mínimo:");

        jLabLoteMultiplo.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabLoteMultiplo.setText("Lote Multiplo:");

        jForEstoqueMinimo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jForEstoqueMinimo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jForLoteMinimo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jForLoteMinimo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jForLoteMultiplo.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jForLoteMultiplo.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabPesoBruto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabPesoBruto.setText("Peso Bruto:");

        jForPesoBruto.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#,##0.00"))));
        jForPesoBruto.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanEstoqueLayout = new javax.swing.GroupLayout(jPanEstoque);
        jPanEstoque.setLayout(jPanEstoqueLayout);
        jPanEstoqueLayout.setHorizontalGroup(
            jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanEstoqueLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabCtaContabil)
                    .addComponent(jLabCCusto)
                    .addComponent(jLabUltPreco)
                    .addComponent(jLabPesoLiquido)
                    .addComponent(jLabLocalArmazPadrao))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanEstoqueLayout.createSequentialGroup()
                        .addComponent(jTexCdCtaContabil, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTexNomeCtaContabil, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanEstoqueLayout.createSequentialGroup()
                        .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanEstoqueLayout.createSequentialGroup()
                                .addComponent(jTexCdCcusto, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTexNomeCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanEstoqueLayout.createSequentialGroup()
                                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanEstoqueLayout.createSequentialGroup()
                                        .addComponent(jTexCdArmPadrao, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTexNomeArmazPadrao, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanEstoqueLayout.createSequentialGroup()
                                        .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(jPanEstoqueLayout.createSequentialGroup()
                                                .addComponent(jForUltimoPreco, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                                            .addGroup(jPanEstoqueLayout.createSequentialGroup()
                                                .addComponent(jForPesoLiquido)
                                                .addGap(50, 50, 50)))
                                        .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanEstoqueLayout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addComponent(jLabPesoBruto)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jForPesoBruto, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanEstoqueLayout.createSequentialGroup()
                                                .addComponent(jLabUltCompra)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jForUltimaCompra)))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabEstoqueMinimo)
                                    .addComponent(jLabLoteMinimo)
                                    .addComponent(jLabLoteMultiplo))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jForEstoqueMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForLoteMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jForLoteMultiplo, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(188, Short.MAX_VALUE))
        );
        jPanEstoqueLayout.setVerticalGroup(
            jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanEstoqueLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabLocalArmazPadrao)
                    .addComponent(jTexCdArmPadrao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeArmazPadrao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabEstoqueMinimo)
                    .addComponent(jForEstoqueMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabPesoLiquido)
                    .addComponent(jLabLoteMinimo)
                    .addComponent(jForLoteMinimo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabPesoBruto)
                    .addComponent(jForPesoBruto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jForPesoLiquido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabUltPreco)
                    .addComponent(jForUltimoPreco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabUltCompra)
                    .addComponent(jForUltimaCompra, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabLoteMultiplo)
                    .addComponent(jForLoteMultiplo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCCusto)
                    .addComponent(jTexCdCcusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeCCusto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanEstoqueLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabCtaContabil)
                    .addComponent(jTexCdCtaContabil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTexNomeCtaContabil, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabTipoProduto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabTipoProduto.setText("Tipo Produto:");

        jComTipoProduto.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Acabado", "Insumo", "Materia Prima", "Revenda", "Serviço" }));

        jPanEngenharia.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Engenharia", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jPanDimenssoes.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Dimensões", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

        jLabComprimento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabComprimento.setText("Comprimento:");

        jForComprimento.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForComprimento.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabLargura.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabLargura.setText("Largura:");

        jForLargura.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForLargura.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        jLabEspessura.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabEspessura.setText("Espessura:");

        jForEspessura.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        jForEspessura.setHorizontalAlignment(javax.swing.JTextField.RIGHT);

        javax.swing.GroupLayout jPanDimenssoesLayout = new javax.swing.GroupLayout(jPanDimenssoes);
        jPanDimenssoes.setLayout(jPanDimenssoesLayout);
        jPanDimenssoesLayout.setHorizontalGroup(
            jPanDimenssoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanDimenssoesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabComprimento)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForComprimento, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabLargura)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jForLargura, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabEspessura)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jForEspessura, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanDimenssoesLayout.setVerticalGroup(
            jPanDimenssoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanDimenssoesLayout.createSequentialGroup()
                .addGap(1, 1, 1)
                .addGroup(jPanDimenssoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabComprimento)
                    .addComponent(jForComprimento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabLargura)
                    .addComponent(jForLargura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabEspessura)
                    .addComponent(jForEspessura, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(1, 1, 1))
        );

        javax.swing.GroupLayout jPanEngenhariaLayout = new javax.swing.GroupLayout(jPanEngenharia);
        jPanEngenharia.setLayout(jPanEngenhariaLayout);
        jPanEngenhariaLayout.setHorizontalGroup(
            jPanEngenhariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanEngenhariaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanDimenssoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanEngenhariaLayout.setVerticalGroup(
            jPanEngenhariaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanEngenhariaLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanDimenssoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanRodape.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jTexRegAtual.setEditable(false);
        jTexRegAtual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexRegAtual.setEnabled(false);

        jLabReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabReg.setText("\\");

            jTexRegTotal.setEditable(false);
            jTexRegTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
            jTexRegTotal.setEnabled(false);

            jLabCadastradoPor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCadastradoPor.setText("Cadastrado por:");

            jTexCadastradoPor.setEditable(false);
            jTexCadastradoPor.setEnabled(false);

            jLabDataCadastro.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabDataCadastro.setText("Cadastro em:");

            jForDataCadastro.setEditable(false);
            jForDataCadastro.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
            jForDataCadastro.setEnabled(false);

            jLabDataModificacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabDataModificacao.setText("Modificado em:");

            jForDataModificacao.setEditable(false);
            jForDataModificacao.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.DateFormatter()));
            jForDataModificacao.setEnabled(false);

            javax.swing.GroupLayout jPanRodapeLayout = new javax.swing.GroupLayout(jPanRodape);
            jPanRodape.setLayout(jPanRodapeLayout);
            jPanRodapeLayout.setHorizontalGroup(
                jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanRodapeLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTexRegAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabReg, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexRegTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabCadastradoPor)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(47, 47, 47)
                    .addComponent(jLabDataCadastro)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(18, 18, 18)
                    .addComponent(jLabDataModificacao)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(24, 24, 24))
            );
            jPanRodapeLayout.setVerticalGroup(
                jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanRodapeLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanRodapeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabDataModificacao)
                        .addComponent(jLabDataCadastro)
                        .addComponent(jForDataCadastro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jForDataModificacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexRegAtual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexRegTotal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabReg)
                        .addComponent(jLabCadastradoPor)
                        .addComponent(jTexCadastradoPor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jPanFiscal.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Fiscal", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jLabOrigemProduto.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabOrigemProduto.setText("Origem do Produto:");

            jTexCdOrigemProduto.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCdOrigemProdutoKeyPressed(evt);
                }
            });

            jLabNCM.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabNCM.setText("NCM:");

            jTexCdNCM.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexCdNCMKeyPressed(evt);
                }
            });

            javax.swing.GroupLayout jPanFiscalLayout = new javax.swing.GroupLayout(jPanFiscal);
            jPanFiscal.setLayout(jPanFiscalLayout);
            jPanFiscalLayout.setHorizontalGroup(
                jPanFiscalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanFiscalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanFiscalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabNCM)
                        .addComponent(jLabOrigemProduto))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanFiscalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jTexCdOrigemProduto)
                        .addComponent(jTexCdNCM, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanFiscalLayout.setVerticalGroup(
                jPanFiscalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanFiscalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanFiscalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabOrigemProduto)
                        .addComponent(jTexCdOrigemProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanFiscalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabNCM)
                        .addComponent(jTexCdNCM, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel1.setText("Sequencial:");

            jForCdSequencial.setEditable(false);
            jForCdSequencial.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("00000"))));
            jForCdSequencial.setEnabled(false);

            jLabDescricaoComercial.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabDescricaoComercial.setText("Descrição Comercial:");

            jTexAreaDescricaoComercial.setColumns(20);
            jTexAreaDescricaoComercial.setRows(3);
            jScrollPane1.setViewportView(jTexAreaDescricaoComercial);

            javax.swing.GroupLayout jPanGeralLayout = new javax.swing.GroupLayout(jPanGeral);
            jPanGeral.setLayout(jPanGeralLayout);
            jPanGeralLayout.setHorizontalGroup(
                jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanGeralLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanRodape, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanEstoque, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanGeralLayout.createSequentialGroup()
                            .addComponent(jPanEngenharia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jPanFiscal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(jPanGeralLayout.createSequentialGroup()
                            .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanGeralLayout.createSequentialGroup()
                                    .addComponent(jLabCdProduto)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jForCdProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jLabDescricao)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jTexDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, 381, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(33, 33, 33)
                                    .addComponent(jLabSituacao)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(jSeparator1)
                                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanGeralLayout.createSequentialGroup()
                                        .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabMarca, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabCategoria, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabSuGrupo, javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabGrupo, javax.swing.GroupLayout.Alignment.TRAILING))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTexCdGrupo, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                                            .addComponent(jTexCdSubGrupo)
                                            .addComponent(jTexCdCategoria)
                                            .addComponent(jTexCdMarca))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTexNomeGrupo, javax.swing.GroupLayout.DEFAULT_SIZE, 225, Short.MAX_VALUE)
                                            .addComponent(jTexNomeSubGrupo)
                                            .addComponent(jTexNomeCategoria)
                                            .addComponent(jTexNomeMarca))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabUnidMedida)
                                            .addComponent(jLabEssencia)
                                            .addComponent(jLabClasse)
                                            .addComponent(jLabTipoProduto))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanGeralLayout.createSequentialGroup()
                                                .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                    .addComponent(jTexCdUnidadeMedida, javax.swing.GroupLayout.DEFAULT_SIZE, 70, Short.MAX_VALUE)
                                                    .addComponent(jTexCdEssencia)
                                                    .addComponent(jTexCdClasse))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jTexNomeClasse, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jTexNomeEssencia, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addComponent(jTexNomeUnidMedida, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 225, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addGroup(jPanGeralLayout.createSequentialGroup()
                                                .addComponent(jComTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, 126, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jForCdSequencial))))))
                            .addGap(0, 0, Short.MAX_VALUE))
                        .addGroup(jPanGeralLayout.createSequentialGroup()
                            .addComponent(jLabDescricaoComercial)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jScrollPane1)))
                    .addContainerGap())
            );
            jPanGeralLayout.setVerticalGroup(
                jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanGeralLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jComSituacao, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabCdProduto)
                            .addComponent(jForCdProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabDescricao)
                            .addComponent(jTexDescricao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabSituacao)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabGrupo)
                        .addComponent(jTexCdGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabClasse)
                        .addComponent(jTexCdClasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeClasse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabSuGrupo)
                        .addComponent(jTexCdSubGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeSubGrupo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabEssencia)
                        .addComponent(jTexCdEssencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeEssencia, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCategoria)
                        .addComponent(jTexCdCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeCategoria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabUnidMedida)
                        .addComponent(jTexCdUnidadeMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexNomeUnidMedida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanGeralLayout.createSequentialGroup()
                            .addGap(15, 15, 15)
                            .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabMarca)
                                .addComponent(jTexCdMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexNomeMarca, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(jPanGeralLayout.createSequentialGroup()
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabTipoProduto)
                                .addComponent(jComTipoProduto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel1)
                                .addComponent(jForCdSequencial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanEstoque, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanEngenharia, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanFiscal, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanGeralLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabDescricaoComercial)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanRodape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

            javax.swing.GroupLayout jPanPrincipalLayout = new javax.swing.GroupLayout(jPanPrincipal);
            jPanPrincipal.setLayout(jPanPrincipalLayout);
            jPanPrincipalLayout.setHorizontalGroup(
                jPanPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanPrincipalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanGeral, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap())
            );
            jPanPrincipalLayout.setVerticalGroup(
                jPanPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanPrincipalLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jPanGeral, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            jMenuBar1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

            jMenu1.setText("Arquivo");

            jMenuNovo.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
            jMenuNovo.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jMenuNovo.setText("Novo");
            jMenuNovo.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuNovoActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuNovo);

            jMenuItemSalvar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemSalvar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jMenuItemSalvar.setText("Salvar");
            jMenuItemSalvar.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemSalvarActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItemSalvar);

            jMenuItemSair.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemSair.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jMenuItemSair.setText("Sair");
            jMenuItemSair.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenuItemSairActionPerformed(evt);
                }
            });
            jMenu1.add(jMenuItemSair);

            jMenuBar1.add(jMenu1);

            jMenu2.setText("Editar");
            jMenu2.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jMenu2ActionPerformed(evt);
                }
            });

            jMenuItemEditar.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
            jMenuItemEditar.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
            jMenuItemEditar.setText("Editar");
            jMenu2.add(jMenuItemEditar);

            jMenuBar1.add(jMenu2);

            setJMenuBar(jMenuBar1);

            javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        // TODO add your handling code here:
        if (jTexDescricao.getText().isEmpty() || jTexCdGrupo.getText().isEmpty() || jTexCdSubGrupo.getText().isEmpty() || jTexCdCategoria.getText().isEmpty()
                || jTexCdUnidadeMedida.getText().isEmpty() || jComTipoProduto.getSelectedItem().toString().substring(0, 1).toString().trim().isEmpty()
                || jComSituacao.getSelectedItem().toString().substring(0, 1).toString().trim().isEmpty() || jTexCdCtaContabil.getText().isEmpty()
                || jTexCdOrigemProduto.getText().isEmpty() || jTexCdNCM.getText().isEmpty() || jTexCdCcusto.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Os campos Descrição, Grupo, Sub Grupo Categoria, Unidade de Medida,"
                    + " Tipo de Produto, Centro de Custo, Conta Contábil, Origem do Produto NCM e Situação são obrigatórios.");
        } else {
            DataSistema dat = new DataSistema();
            Materiais mat = new Materiais();
            String data = null;
            mat.setNomeMaterial(jTexDescricao.getText().trim().toUpperCase());
            mat.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1));
            mat.setCdGrupo(jTexCdGrupo.getText().trim());
            mat.setCdSubGrupo(jTexCdSubGrupo.getText().trim());
            mat.setCdCategoria(jTexCdCategoria.getText().trim());
            mat.setCdMarca(jTexCdMarca.getText().trim());
            mat.setCdClasse(jTexCdClasse.getText().trim());
            mat.setCdEssencia(jTexCdEssencia.getText().trim());
            mat.setCdUnidMedida(jTexCdUnidadeMedida.getText().trim());
            mat.setTipoProduto(jComTipoProduto.getSelectedItem().toString().substring(0, 1));
            mat.setCdArmazem(jTexCdArmPadrao.getText().trim());
            try {
                mat.setPesoLiquido(formato.parse(jForPesoLiquido.getText()).doubleValue());
                mat.setPesoBruto(formato.parse(jForPesoBruto.getText()).doubleValue());
                mat.setUltPrecoCompra(formato.parse(jForUltimoPreco.getText()).doubleValue());
                mat.setEstoqueMinimo(formato.parse(jForEstoqueMinimo.getText()).doubleValue());
                mat.setLoteMinimo(formato.parse(jForLoteMinimo.getText()).doubleValue());
                mat.setComprimento(formato.parse(jForComprimento.getText()).doubleValue());
                mat.setLargura(formato.parse(jForLargura.getText()).doubleValue());
                mat.setEspessura(formato.parse(jForEspessura.getText()).doubleValue());
            } catch (ParseException ex) {
                Logger.getLogger(ManterMateriais.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (!jForUltimaCompra.getText().isEmpty()) {
                mat.setUltCompra(dat.getDataConv(Date.valueOf(jForUltimaCompra.getSelectedText().trim())));
            }
            mat.setCdCcusto(jTexCdCcusto.getText().trim());
            mat.setCdCtaContabeReduz(jTexCdCtaContabil.getText().trim());
            mat.setCdOrigemCsta(jTexCdOrigemProduto.getText().trim());
            mat.setCdNcm(jTexCdNCM.getText().trim());
            mat.setDescricaoComercial(jTexAreaDescricaoComercial.getText());
            dat.setData(data);
            data = dat.getData();
            mat.setDataCadastro(data);
            mat.setUsuarioCadastro(su.getUsuarioConectado());
            MateriaisDAO matdao = null;
            CMateriais cmat = new CMateriais(conexao);

            try {
                matdao = new MateriaisDAO(conexao);
                if ("N".equals(oper)) {
                    //gera código do produto
                    CBuscarSequencia cb = new CBuscarSequencia(su, "gcsmaterial",5);
                    String sequencia = cb.getRetorno();
                    cmat.gerarCodigo(mat, sequencia);
                    sql = "SELECT * FROM GCSMATERIAL WHERE CD_MATERIAL = '" + mat.getCdMaterial().trim() + "' AND CD_SEQUENCIAL = '" + mat.getCdSequencial().trim() + "'";
                    matdao.adicionar(mat);
                } else {
                    sql = "SELECT * FROM GCSMATERIAL WHERE CD_MATERIAL = '" + sqlmat.getCdMaterial() + "' AND CD_SEQUENCIAL = '" + sqlmat.getCdSequencial() + "'";
                    mat.setCdMaterial(sqlmat.getCdMaterial());
                    mat.setCdSequencial(sqlmat.getCdSequencial());
                    matdao.atualizar(mat);
                }
            } catch (SQLException sqlex) {
                JOptionPane.showMessageDialog(null, "Erro criar o produto no banco e dados!\nErr: " + sqlex);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral para criar o produto!\nErr: " + ex);
            }
            jButSalvar.setEnabled(false);
            limparTela();
            bloquearCampos();
            pesquisar();
        }
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:
        if (jTexDescricao.getText().isEmpty()) {
            sql = "SELECT * FROM GCSMATERIAL";
        } else {
            sql = "SELECT * FROM GCSMATERIAL WHERE NOME_MATERIAL LIKE '%" + jTexDescricao.getText().trim() + "%'";
        }
        bloquearCampos();
        pesquisar();
    }//GEN-LAST:event_jButPesquisarActionPerformed

    private void jButProximoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButProximoActionPerformed
        // TODO add your handling code here:
        idxCorr += 1;
        upRegistros();
    }//GEN-LAST:event_jButProximoActionPerformed

    private void jButAnteriorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButAnteriorActionPerformed
        // TODO add your handling code here:
        idxCorr -= 1;
        upRegistros();
    }//GEN-LAST:event_jButAnteriorActionPerformed

    private void jButCancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCancelarActionPerformed
        // TODO add your handling code here:
        limparTela();
        oper = "N";
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        // TODO add your handling code here:
        liberarCampos();
        jTexDescricao.requestFocus();
        oper = "A";
        jButSalvar.setEnabled(true);
    }//GEN-LAST:event_jButEditarActionPerformed

    private void jButNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButNovoActionPerformed
        // TODO add your handling code here:
        novoRegistro();
        oper = "N";
        jButSalvar.setEnabled(true);
    }//GEN-LAST:event_jButNovoActionPerformed

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jMenuNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuNovoActionPerformed
        // TODO add your handling code here:
        jButNovoActionPerformed(evt);
    }//GEN-LAST:event_jMenuNovoActionPerformed

    private void jMenuItemSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalvarActionPerformed
        // TODO add your handling code here:
        jButSalvarActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemSalvarActionPerformed

    private void jMenuItemSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSairActionPerformed
        // TODO add your handling code here:
        jButSairActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemSairActionPerformed

    private void jMenu2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu2ActionPerformed
        // TODO add your handling code here:
        jButEditarActionPerformed(evt);
    }//GEN-LAST:event_jMenu2ActionPerformed

    private void jTexCdGrupoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdGrupoKeyPressed
        // TODO add your handling code here:
        if ("F5".equals(verificaTecla(evt))) {
            if ("N".equals(oper)) {
                zoomGrupo();
            } else {
                JOptionPane.showMessageDialog(null, "Este campo não permite alteração!");
            }
        }
    }//GEN-LAST:event_jTexCdGrupoKeyPressed

    private void jTexCdSubGrupoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdSubGrupoKeyPressed
        // TODO add your handling code here:
        String key = verificaTecla(evt);
        if ("F5".equals(key)) {
            if ("N".equals(oper)) {
                zoomSubGrupo();
            } else {
                JOptionPane.showMessageDialog(null, "Este campo não permite alteração!");
            }
        }
    }//GEN-LAST:event_jTexCdSubGrupoKeyPressed

    private void jTexCdCategoriaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdCategoriaKeyPressed
        // TODO add your handling code here:
        if ("F5".equals(verificaTecla(evt))) {
            if ("N".equals(oper)) {
                zoomCategoria();
            } else {
                JOptionPane.showMessageDialog(null, "Este campo não permite alteração!");
            }
        }
    }//GEN-LAST:event_jTexCdCategoriaKeyPressed

    private void jTexCdMarcaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdMarcaKeyPressed
        // TODO add your handling code here:
        if ("F5".equals(verificaTecla(evt))) {
            zoomMarca();
        }
    }//GEN-LAST:event_jTexCdMarcaKeyPressed

    private void jTexCdClasseKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdClasseKeyPressed
        // TODO add your handling code here:
        if ("F5".equals(verificaTecla(evt))) {
            zoomClasse();
        }
    }//GEN-LAST:event_jTexCdClasseKeyPressed

    private void jTexCdEssenciaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdEssenciaKeyPressed
        // TODO add your handling code here:
        if ("F5".equals(verificaTecla(evt))) {
            if ("N".equals(oper)) {
                zoomEssencia();
            } else {
                JOptionPane.showMessageDialog(null, "Este campo não permite alteração!");
            }
        }
    }//GEN-LAST:event_jTexCdEssenciaKeyPressed

    private void jTexCdUnidadeMedidaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdUnidadeMedidaKeyPressed
        // TODO add your handling code here:
        if ("F5".equals(verificaTecla(evt))) {
            zoomUnidadesMedida();
        }
    }//GEN-LAST:event_jTexCdUnidadeMedidaKeyPressed

    private void jTexCdArmPadraoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdArmPadraoKeyPressed
        // TODO add your handling code here:
        if ("F5".equals(verificaTecla(evt))) {
            zoomArmazens();
        }
    }//GEN-LAST:event_jTexCdArmPadraoKeyPressed

    private void jTexCdCcustoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdCcustoKeyPressed
        // TODO add your handling code here:
        if ("F5".equals(verificaTecla(evt))) {
            zoomCentroCustos();
        }
    }//GEN-LAST:event_jTexCdCcustoKeyPressed

    private void jTexCdCtaContabilKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdCtaContabilKeyPressed
        // TODO add your handling code here:
        if ("F5".equals(verificaTecla(evt))) {
            zoomContasContabeis();
        }
    }//GEN-LAST:event_jTexCdCtaContabilKeyPressed

    private void jTexCdOrigemProdutoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdOrigemProdutoKeyPressed
        // TODO add your handling code here:
        if ("F5".equals(verificaTecla(evt))) {
            zoomOrigemProduto();
        }
    }//GEN-LAST:event_jTexCdOrigemProdutoKeyPressed

    private void jTexCdNCMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexCdNCMKeyPressed
        // TODO add your handling code here:
        if ("F5".equals(verificaTecla(evt))) {
            zoomNCMProduto();
        }
    }//GEN-LAST:event_jTexCdNCMKeyPressed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        // TODO add your handling code here:
        if (!jForCdProduto.getText().isEmpty()) {
            try {
                Materiais cc = new Materiais();
                cc.setCdMaterial(jForCdProduto.getText().trim());
                cc.setCdSequencial(jForCdSequencial.getText().trim());
                MateriaisDAO ccDAO = new MateriaisDAO(conexao);
                ccDAO.excluir(cc);
                limparTela();
                jButPesquisarActionPerformed(evt);
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null, "Não foi possível excluir o registro da tela");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro geral na exclusão do registro!");
            }
        }
    }//GEN-LAST:event_jButExcluirActionPerformed

    private void jButImprimirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButImprimirActionPerformed
        // TODO add your handling code here:
        ImprimirMateriais zoom = new ImprimirMateriais(new JFrame(), true,sqlmat.getCdMaterial(),sqlmat.getCdGrupo(),sqlmat.getCdSubGrupo(), su);
        zoom.setVisible(true);
    }//GEN-LAST:event_jButImprimirActionPerformed

    private void jButCopiarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButCopiarActionPerformed
        // TODO add your handling code here:
        jButSalvar.setEnabled(true);
        liberarCampos();
        jTexDescricao.requestFocus();
        oper = "N";
    }//GEN-LAST:event_jButCopiarActionPerformed

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
            java.util.logging.Logger.getLogger(ManterMateriais.class

.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        



} catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterMateriais.class

.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        



} catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterMateriais.class

.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        



} catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterMateriais.class

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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterMateriais(su, conexao).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButAnterior;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButCopiar;
    private javax.swing.JButton jButEditar;
    private javax.swing.JButton jButExcluir;
    private javax.swing.JButton jButImprimir;
    private javax.swing.JButton jButNovo;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JButton jButProximo;
    private javax.swing.JButton jButSair;
    private javax.swing.JButton jButSalvar;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JComboBox<String> jComTipoProduto;
    private javax.swing.JFormattedTextField jForCdProduto;
    private javax.swing.JFormattedTextField jForCdSequencial;
    private javax.swing.JFormattedTextField jForComprimento;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JFormattedTextField jForEspessura;
    private javax.swing.JFormattedTextField jForEstoqueMinimo;
    private javax.swing.JFormattedTextField jForLargura;
    private javax.swing.JFormattedTextField jForLoteMinimo;
    private javax.swing.JFormattedTextField jForLoteMultiplo;
    private javax.swing.JFormattedTextField jForPesoBruto;
    private javax.swing.JFormattedTextField jForPesoLiquido;
    private javax.swing.JFormattedTextField jForUltimaCompra;
    private javax.swing.JFormattedTextField jForUltimoPreco;
    private javax.swing.JLabel jLabCCusto;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabCategoria;
    private javax.swing.JLabel jLabCdProduto;
    private javax.swing.JLabel jLabClasse;
    private javax.swing.JLabel jLabComprimento;
    private javax.swing.JLabel jLabCtaContabil;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabDescricao;
    private javax.swing.JLabel jLabDescricaoComercial;
    private javax.swing.JLabel jLabEspessura;
    private javax.swing.JLabel jLabEssencia;
    private javax.swing.JLabel jLabEstoqueMinimo;
    private javax.swing.JLabel jLabGrupo;
    private javax.swing.JLabel jLabLargura;
    private javax.swing.JLabel jLabLocalArmazPadrao;
    private javax.swing.JLabel jLabLoteMinimo;
    private javax.swing.JLabel jLabLoteMultiplo;
    private javax.swing.JLabel jLabMarca;
    private javax.swing.JLabel jLabNCM;
    private javax.swing.JLabel jLabOrigemProduto;
    private javax.swing.JLabel jLabPesoBruto;
    private javax.swing.JLabel jLabPesoLiquido;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabSuGrupo;
    private javax.swing.JLabel jLabTipoProduto;
    private javax.swing.JLabel jLabUltCompra;
    private javax.swing.JLabel jLabUltPreco;
    private javax.swing.JLabel jLabUnidMedida;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JMenuItem jMenuNovo;
    private javax.swing.JPanel jPanDimenssoes;
    private javax.swing.JPanel jPanEngenharia;
    private javax.swing.JPanel jPanEstoque;
    private javax.swing.JPanel jPanFiscal;
    private javax.swing.JPanel jPanGeral;
    private javax.swing.JPanel jPanPrincipal;
    private javax.swing.JPanel jPanRodape;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea jTexAreaDescricaoComercial;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexCdArmPadrao;
    private javax.swing.JTextField jTexCdCategoria;
    private javax.swing.JTextField jTexCdCcusto;
    private javax.swing.JTextField jTexCdClasse;
    private javax.swing.JTextField jTexCdCtaContabil;
    private javax.swing.JTextField jTexCdEssencia;
    private javax.swing.JTextField jTexCdGrupo;
    private javax.swing.JTextField jTexCdMarca;
    private javax.swing.JTextField jTexCdNCM;
    private javax.swing.JTextField jTexCdOrigemProduto;
    private javax.swing.JTextField jTexCdSubGrupo;
    private javax.swing.JTextField jTexCdUnidadeMedida;
    private javax.swing.JTextField jTexDescricao;
    private javax.swing.JTextField jTexNomeArmazPadrao;
    private javax.swing.JTextField jTexNomeCCusto;
    private javax.swing.JTextField jTexNomeCategoria;
    private javax.swing.JTextField jTexNomeClasse;
    private javax.swing.JTextField jTexNomeCtaContabil;
    private javax.swing.JTextField jTexNomeEssencia;
    private javax.swing.JTextField jTexNomeGrupo;
    private javax.swing.JTextField jTexNomeMarca;
    private javax.swing.JTextField jTexNomeSubGrupo;
    private javax.swing.JTextField jTexNomeUnidMedida;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
