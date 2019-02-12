/**
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *
 * Programa: GCVCA0021
 */
package br.com.gcv.visao;

// objetos do registro Pai
import br.com.gcv.modelo.Clientes;
import br.com.gcv.dao.ClientesDAO;
import br.com.gcv.controle.CClientes;

// Objetos para pesquisa de correlato
import br.com.modelo.EnderecoPostal;
import br.com.controle.CEnderecoPostal;
import br.com.gcv.dao.EnderecoCobrancaClientesDAO;
import br.com.gcv.modelo.EnderecoCobrancaCliente;
import br.com.gfc.visao.PesquisarCondicaoPagamento;
import br.com.gfc.visao.PesquisarPortadores;
import br.com.gfc.visao.PesquisarTipoPagamento;
import br.com.visao.PesquisarMunicipios;
import br.com.visao.PesquisarUnidadeFederacao;

// Objetos de instância de parâmetros de ambiente
import br.com.modelo.DataSistema;
import br.com.modelo.SessaoUsuario;
import br.com.modelo.VerificarTecla;

// Objetos de ambiente java
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Connection;
import java.text.ParseException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JFrame;

/**
 *
 * @author Cristiano de Oliveira Sousa created on 17/11/2017
 */
public class ManterEnderecoCobrancaClientes extends javax.swing.JFrame {

    // Variáveis de instancia de parâmetros de ambiente
    private static Connection conexao;
    private static SessaoUsuario su;
    private VerificarTecla vt;

    // Variáveis de instância de objetos da classe
    private Clientes regCorr;
    private List< Clientes> resultado;
    private CClientes ccli;
    private Clientes modcli;

    // Variáveis de instância da objetos correlatos classe
    private CEnderecoPostal cep;
    private EnderecoPostal ep;

    // Variáveis de instância da classe
    private int numReg;
    private int idxCorr;
    private String sql;
    private String oper;

    // construturo padrão
    public ManterEnderecoCobrancaClientes() {

    }

    public ManterEnderecoCobrancaClientes(SessaoUsuario su, Connection conexao) {
        this.su = su;
        this.conexao = conexao;
        oper = "N";
        initComponents();
        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        this.dispose();
    }

    // método para limpar tela
    private void limparTela() {
        jForCdCpfCnpj.setText("");
        jForInscEstadual.setText("");
        jTexNomeRazaoSocial.setText("");
        jTexApelido.setText("");
        jComTipoLogradouro.setSelectedIndex(0);
        jTexLogradouro.setText("");
        jTexNumero.setText("");
        jTexComplemento.setText("");
        jTexBairro.setText("");
        jTexCdMunicipioIbge.setText("");
        jTexSiglaUF.setText("");
        jForCep.setText("");
        jTexCdUfIbge.setText("");
        jTexMuncipio.setText("");
        jComSituacao.setSelectedIndex(0);
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
        jForCdCpfCnpj.setEditable(true);
    }

    // método para definir o tipo de pesquisa
    private void pesquisar() {

        ccli = new CClientes(conexao);
        modcli = new Clientes();
        try {
            numReg = ccli.pesquisar(sql);
            idxCorr = +1;
        } catch (IllegalArgumentException iae) {
            JOptionPane.showMessageDialog(null, "Argumento inválido na busca\nErr: " + iae.getCause());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "Erro ao Pesquisar os Registro\nErr: " + ex);
        }
        if (numReg > 0) {
            upRegistros();
        } else {
            JOptionPane.showMessageDialog(null, "Nenhum registro encontrado!");
        }
    }

    private void upRegistros() {
        jTexRegTotal.setText(Integer.toString(numReg));
        jTexRegAtual.setText(Integer.toString(idxCorr));
        ccli.mostrarPesquisa(modcli, idxCorr - 1);
        DataSistema dat = new DataSistema();
        jForInscEstadual.setText(modcli.getCdInscEstadual());
        jTexNomeRazaoSocial.setText(modcli.getNomeRazaoSocial().trim().toUpperCase());
        jTexApelido.setText(modcli.getApelido().trim().toUpperCase());
        jComTipoLogradouro.setSelectedItem(modcli.getTipoLogradouro());
        jTexLogradouro.setText(modcli.getLogradouro());
        jTexNumero.setText(modcli.getNumero());
        jTexComplemento.setText(modcli.getComplemento());
        jTexBairro.setText(modcli.getBairro());
        jTexCdMunicipioIbge.setText(modcli.getCdMunicipioIbge());
        jTexMuncipio.setText(modcli.getNomeMunicipio());
        jTexSiglaUF.setText(modcli.getSiglaUf());
        jTexCdUfIbge.setText(String.valueOf(modcli.getUfIbge()));
        jForCep.setText(modcli.getCdCep());
        jForCdCpfCnpj.setText(modcli.getCdCpfCnpj().trim());
        jTexCadastradoPor.setText(modcli.getUsuarioCadastro());
        jForDataCadastro.setText(dat.getDataConv(Date.valueOf(modcli.getDataCadastro())));
        if (modcli.getDataModificacao() != null) {
            jForDataModificacao.setText(dat.getDataConv(Date.valueOf(modcli.getDataModificacao())));
        }
        jComSituacao.setSelectedIndex(Integer.parseInt(String.valueOf(modcli.getSituacao())));

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
    private void bloquearCampos() {
        jComTipoLogradouro.setEditable(false);
        jTexLogradouro.setEditable(false);
        jTexNumero.setEditable(false);
        jTexComplemento.setEditable(false);
        jTexBairro.setEditable(false);
        jTexMuncipio.setEditable(false);
        jTexSiglaUF.setEditable(false);
        jForCep.setEditable(false);
        jComSituacao.setEditable(false);
    }

    //Loberar os campos da tela para atualização
    private void liberarCampos() {
        jComTipoLogradouro.setEditable(true);
        jTexLogradouro.setEditable(true);
        jTexNumero.setEditable(true);
        jTexComplemento.setEditable(true);
        jTexBairro.setEditable(true);
        jTexMuncipio.setEditable(true);
        jTexSiglaUF.setEditable(true);
        jForCep.setEditable(true);
        jComSituacao.setEditable(false);
    }

    private void novoRegistro() {
        limparTela();
        liberarCampos();
        jComTipoLogradouro.requestFocus();
        jButEditar.setEnabled(false);
    }

    // metodo para dar zoon no campo UF
    private void zoomUF() {
        PesquisarUnidadeFederacao zoom = new PesquisarUnidadeFederacao(new JFrame(), true,conexao, "P");
        zoom.setVisible(true);
        jTexSiglaUF.setText(zoom.getSelec1().trim());
        jTexCdUfIbge.setText(zoom.getSelec3().trim());
    }

    // metodo para dar zoon no campo Município
    private void zoomMunicipio() {
        String sql = "SELECT MU.CD_MUNICIPIO_IBGE AS Cod_Ibge,"
                + "MU.CD_MUNICIPIO AS Cod_Mun,"
                + "MU.NOME_MUNICIPIO AS Município,"
                + "MU.CD_UF_IBGE AS UF,"
                + "MU.DATA_CADASTRO AS Cadastro,"
                + "MU.DATA_MODIFICACAO AS Modificao,"
                + "MU.SITUACAO AS Sit"
                + " FROM PGSMUNICIPIO AS MU"
                + " LEFT JOIN PGSUF AS UF ON MU.CD_UF_IBGE = UF.CD_UF_IBGE"
                + " WHERE UF.CD_UF_IBGE = '"
                + jTexCdUfIbge.getText().toUpperCase().trim()
                + "' ORDER BY MU.NOME_MUNICIPIO";
        PesquisarMunicipios zoom = new PesquisarMunicipios(new JFrame(), true, "P", sql, conexao);
        zoom.setVisible(true);
        jTexCdMunicipioIbge.setText(zoom.getSelec1().trim());
        jTexMuncipio.setText(zoom.getSelec3().trim());
    }
    
// metodo para buscar CEP
    private void buscarCep() throws IOException {
        cep = new CEnderecoPostal();
        ep = new EnderecoPostal();
        if (!jForCep.getText().trim().replace("-", "").isEmpty()) {
            cep.pesquisar(conexao, jForCep.getText().replace("-", ""),su.getCharSet());
            upEndereco();
        } else if (jTexSiglaUF.getText().isEmpty() || jTexLogradouro.getText().isEmpty() || jTexMuncipio.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Você deve informar o CEP para busca, ou, na falta dele,\n informe UF, Cidade e Logradouro para buscar o CEP");
        } else {
            cep.pesquisar(conexao, jTexSiglaUF.getText().toUpperCase().trim() + "/" + jTexMuncipio.getText().toUpperCase().trim() + "/" + jTexLogradouro.getText().toUpperCase().trim(),su.getCharSet());
            upEndereco();
        }
    }

    private void upEndereco() {
        cep.mostrarPesquisa(ep);
        jComTipoLogradouro.setSelectedItem(ep.getTipoLogradouro());
        jTexLogradouro.setText(ep.getLogradouro());
        jTexComplemento.setText(ep.getComplemento());
        jTexBairro.setText(ep.getBairro());
        jTexSiglaUF.setText(ep.getSiglaUf());
        jTexCdUfIbge.setText(String.valueOf(ep.getUfIbge()));
        jTexCdMunicipioIbge.setText(ep.getCdIbge());
        jTexMuncipio.setText(ep.getMunicipioLocalidade());
        jForCep.setText(ep.getCep());
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
        jPanSecundario = new javax.swing.JPanel();
        jPanBotoes = new javax.swing.JPanel();
        jLabDataCadastro = new javax.swing.JLabel();
        jForDataCadastro = new javax.swing.JFormattedTextField();
        jLabDataModificacao = new javax.swing.JLabel();
        jForDataModificacao = new javax.swing.JFormattedTextField();
        jTexRegAtual = new javax.swing.JTextField();
        jTexRegTotal = new javax.swing.JTextField();
        jLabReg = new javax.swing.JLabel();
        jLabCadastradoPor = new javax.swing.JLabel();
        jTexCadastradoPor = new javax.swing.JTextField();
        jLabCpfCnpj = new javax.swing.JLabel();
        jLabNomeTecnico = new javax.swing.JLabel();
        jLabSituacao = new javax.swing.JLabel();
        jForCdCpfCnpj = new javax.swing.JFormattedTextField();
        jTexNomeRazaoSocial = new javax.swing.JTextField();
        jComSituacao = new javax.swing.JComboBox<>();
        jForInscEstadual = new javax.swing.JFormattedTextField();
        jLabInscEstadual = new javax.swing.JLabel();
        jLabOrgaoExpedidor = new javax.swing.JLabel();
        jPanEndereco = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jComTipoLogradouro = new javax.swing.JComboBox<>();
        jTexLogradouro = new javax.swing.JTextField();
        jLabNumero = new javax.swing.JLabel();
        jTexNumero = new javax.swing.JTextField();
        jLabComplemento = new javax.swing.JLabel();
        jTexComplemento = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jTexBairro = new javax.swing.JTextField();
        jLabCidade = new javax.swing.JLabel();
        jLabMunicipio = new javax.swing.JLabel();
        jTexMuncipio = new javax.swing.JTextField();
        jTexSiglaUF = new javax.swing.JTextField();
        jTexCdMunicipioIbge = new javax.swing.JTextField();
        jLabCep = new javax.swing.JLabel();
        jForCep = new javax.swing.JFormattedTextField();
        jTexCdUfIbge = new javax.swing.JTextField();
        jButValidarCep = new javax.swing.JButton();
        jTexApelido = new javax.swing.JTextField();
        jPanTabela = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTableEnderecos = new javax.swing.JTable();
        jMenuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItemSalvar = new javax.swing.JMenuItem();
        jMenuItemSair = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItemEditar = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manter Clientes");

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

        jPanSecundario.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jPanSecundario.setToolTipText("");

        jPanBotoes.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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

        jTexRegAtual.setEditable(false);
        jTexRegAtual.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexRegAtual.setEnabled(false);

        jTexRegTotal.setEditable(false);
        jTexRegTotal.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTexRegTotal.setEnabled(false);

        jLabReg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabReg.setText("\\");

            jLabCadastradoPor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCadastradoPor.setText("Cadastrado por:");

            jTexCadastradoPor.setEditable(false);
            jTexCadastradoPor.setEnabled(false);

            javax.swing.GroupLayout jPanBotoesLayout = new javax.swing.GroupLayout(jPanBotoes);
            jPanBotoes.setLayout(jPanBotoesLayout);
            jPanBotoesLayout.setHorizontalGroup(
                jPanBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanBotoesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(jTexRegAtual, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jLabReg, javax.swing.GroupLayout.PREFERRED_SIZE, 13, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jTexRegTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
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
                    .addGap(108, 108, 108))
            );
            jPanBotoesLayout.setVerticalGroup(
                jPanBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanBotoesLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanBotoesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
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

            jLabCpfCnpj.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCpfCnpj.setText("C.P.F. / C.N.P.J.:");

            jLabNomeTecnico.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabNomeTecnico.setText("Nome / Razão Social:");

            jLabSituacao.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabSituacao.setText("Situação:");

            jForCdCpfCnpj.setEditable(false);
            jForCdCpfCnpj.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat(""))));
            jForCdCpfCnpj.setEnabled(false);

            jTexNomeRazaoSocial.setEditable(false);
            jTexNomeRazaoSocial.setEnabled(false);
            jTexNomeRazaoSocial.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jTexNomeRazaoSocialActionPerformed(evt);
                }
            });

            jComSituacao.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Ativo", "Inativo" }));

            jForInscEstadual.setEditable(false);
            try {
                jForInscEstadual.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("*******************")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }
            jForInscEstadual.setEnabled(false);

            jLabInscEstadual.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabInscEstadual.setText("I.E.:");

            jLabOrgaoExpedidor.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabOrgaoExpedidor.setText("Fantasia / Apelido:");

            jPanEndereco.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Endereço", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jLabel1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel1.setText("Tipo Logradouro:");
            jLabel1.setVerticalAlignment(javax.swing.SwingConstants.TOP);
            jLabel1.setAutoscrolls(true);

            jComTipoLogradouro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "Aeroporto", "Alameda", "Área", "Avenida", "Campo", "Chácara", "Colônia", "Condomínio", "Conjunto", "Distrito", "Esplanada", "Estação", "Estrada", "Favela", "Feira", "Jardim", "Ladeira", "Lago", "Lagoa", "Largo", "Loteamento", "Morro", "Núcleo", "Parque", "Passarela", "Pátio", "Praça", "Quadra", "Recanto", "Residencial", "Rodovia", "Rua", "Setor", "Sítio", "Travessa", "Trecho", "Trevo", "Vale", "Vereda", "Via", "Viaduto", "Viela", "Vila" }));

            jLabNumero.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabNumero.setText("Número:");

            jLabComplemento.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabComplemento.setText("Complemento:");

            jLabel3.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabel3.setText("Bairro:");

            jLabCidade.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCidade.setText("U.F.:");

            jLabMunicipio.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabMunicipio.setText("Município:");

            jTexMuncipio.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexMuncipioKeyPressed(evt);
                }
            });

            jTexSiglaUF.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    jTexSiglaUFKeyPressed(evt);
                }
            });

            jTexCdMunicipioIbge.setEditable(false);
            jTexCdMunicipioIbge.setEnabled(false);

            jLabCep.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jLabCep.setText("C.E.P:");

            try {
                jForCep.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("#####-###")));
            } catch (java.text.ParseException ex) {
                ex.printStackTrace();
            }

            jTexCdUfIbge.setEditable(false);
            jTexCdUfIbge.setEnabled(false);

            jButValidarCep.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
            jButValidarCep.setText("Validar CEP");
            jButValidarCep.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    jButValidarCepActionPerformed(evt);
                }
            });

            javax.swing.GroupLayout jPanEnderecoLayout = new javax.swing.GroupLayout(jPanEndereco);
            jPanEndereco.setLayout(jPanEnderecoLayout);
            jPanEnderecoLayout.setHorizontalGroup(
                jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanEnderecoLayout.createSequentialGroup()
                    .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanEnderecoLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabComplemento))
                        .addComponent(jLabCidade, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(jPanEnderecoLayout.createSequentialGroup()
                            .addComponent(jComTipoLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabNumero)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jTexNumero, javax.swing.GroupLayout.PREFERRED_SIZE, 85, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanEnderecoLayout.createSequentialGroup()
                            .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jTexCdUfIbge, javax.swing.GroupLayout.DEFAULT_SIZE, 60, Short.MAX_VALUE)
                                .addComponent(jTexSiglaUF))
                            .addGap(16, 16, 16)
                            .addComponent(jLabMunicipio)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jTexMuncipio, javax.swing.GroupLayout.PREFERRED_SIZE, 392, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexCdMunicipioIbge, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanEnderecoLayout.createSequentialGroup()
                                    .addComponent(jLabCep)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(jForCep, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jButValidarCep, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addGroup(jPanEnderecoLayout.createSequentialGroup()
                            .addComponent(jTexComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(jTexBairro, javax.swing.GroupLayout.PREFERRED_SIZE, 288, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );
            jPanEnderecoLayout.setVerticalGroup(
                jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanEnderecoLayout.createSequentialGroup()
                    .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanEnderecoLayout.createSequentialGroup()
                            .addContainerGap()
                            .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComTipoLogradouro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabNumero)
                                .addComponent(jTexNumero, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jTexLogradouro)))
                        .addGroup(jPanEnderecoLayout.createSequentialGroup()
                            .addGap(14, 14, 14)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabComplemento)
                        .addComponent(jTexComplemento, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3)
                        .addComponent(jTexBairro, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCidade)
                        .addComponent(jLabMunicipio)
                        .addComponent(jTexMuncipio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabCep)
                        .addComponent(jForCep, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexSiglaUF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanEnderecoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTexCdMunicipioIbge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jTexCdUfIbge, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButValidarCep))
                    .addContainerGap())
            );

            jTexApelido.setEditable(false);
            jTexApelido.setEnabled(false);

            jPanTabela.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Endereços Cadastrados", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11))); // NOI18N

            jTableEnderecos.setModel(new javax.swing.table.DefaultTableModel(
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
                    "Seq", "Logradouro", "Bairro", "Cidade", "UF"
                }
            ) {
                Class[] types = new Class [] {
                    java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
            jScrollPane1.setViewportView(jTableEnderecos);
            if (jTableEnderecos.getColumnModel().getColumnCount() > 0) {
                jTableEnderecos.getColumnModel().getColumn(0).setResizable(false);
                jTableEnderecos.getColumnModel().getColumn(0).setPreferredWidth(10);
                jTableEnderecos.getColumnModel().getColumn(1).setResizable(false);
                jTableEnderecos.getColumnModel().getColumn(1).setPreferredWidth(300);
                jTableEnderecos.getColumnModel().getColumn(2).setResizable(false);
                jTableEnderecos.getColumnModel().getColumn(2).setPreferredWidth(200);
                jTableEnderecos.getColumnModel().getColumn(3).setResizable(false);
                jTableEnderecos.getColumnModel().getColumn(3).setPreferredWidth(200);
                jTableEnderecos.getColumnModel().getColumn(4).setResizable(false);
                jTableEnderecos.getColumnModel().getColumn(4).setPreferredWidth(10);
            }

            javax.swing.GroupLayout jPanTabelaLayout = new javax.swing.GroupLayout(jPanTabela);
            jPanTabela.setLayout(jPanTabelaLayout);
            jPanTabelaLayout.setHorizontalGroup(
                jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane1)
            );
            jPanTabelaLayout.setVerticalGroup(
                jPanTabelaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanTabelaLayout.createSequentialGroup()
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE))
            );

            javax.swing.GroupLayout jPanSecundarioLayout = new javax.swing.GroupLayout(jPanSecundario);
            jPanSecundario.setLayout(jPanSecundarioLayout);
            jPanSecundarioLayout.setHorizontalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabNomeTecnico, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabOrgaoExpedidor, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabCpfCnpj, javax.swing.GroupLayout.Alignment.TRAILING))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                            .addComponent(jForCdCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                            .addComponent(jLabInscEstadual)
                                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                            .addComponent(jForInscEstadual, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addComponent(jTexNomeRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(jLabSituacao)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(373, 373, 373))
                                .addGroup(jPanSecundarioLayout.createSequentialGroup()
                                    .addComponent(jTexApelido, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                        .addGroup(jPanSecundarioLayout.createSequentialGroup()
                            .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(jPanTabela, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jPanEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jPanBotoes, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
            );
            jPanSecundarioLayout.setVerticalGroup(
                jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanSecundarioLayout.createSequentialGroup()
                    .addGap(12, 12, 12)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jComSituacao, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabSituacao)
                        .addComponent(jLabNomeTecnico)
                        .addComponent(jTexNomeRazaoSocial, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabOrgaoExpedidor)
                        .addComponent(jTexApelido, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(jPanSecundarioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabCpfCnpj)
                        .addComponent(jForCdCpfCnpj, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabInscEstadual)
                        .addComponent(jForInscEstadual, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanEndereco, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanTabela, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                    .addComponent(jPanBotoes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap())
            );

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
                .addComponent(jPanSecundario, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 867, javax.swing.GroupLayout.PREFERRED_SIZE)
            );
            layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jTooMenuFerramentas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(jPanSecundario, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGap(2, 2, 2))
            );

            pack();
        }// </editor-fold>//GEN-END:initComponents

    private void jButSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButSairActionPerformed

    private void jButSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButSalvarActionPerformed
        // TODO add your handling code here:
        if (jForCdCpfCnpj.getText().isEmpty() || jTexNomeRazaoSocial.getText().isEmpty() || jComSituacao.getSelectedItem().toString().substring(0, 1) == " "
                || jForInscEstadual.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Os campos CPF / CNPJ, Nome/Razão Social e Situacao precisam ser preenchidos corretamente!");
        } else {
            DataSistema dat = new DataSistema();
            EnderecoCobrancaCliente ecc = new EnderecoCobrancaCliente();
            String data = null;
            String cpfCnpj = jForCdCpfCnpj.getText().trim();
            cpfCnpj = cpfCnpj.replace(".", "");
            cpfCnpj = cpfCnpj.replace("-", "");
            cpfCnpj = cpfCnpj.replace("/", "");
            ecc.setCdCpfCnpj(cpfCnpj);
            ecc.setCdInscEstadual(jForInscEstadual.getText().trim().toUpperCase());
            ecc.setNomeRazaoSocial(jTexNomeRazaoSocial.getText().trim().toUpperCase());
            ecc.setApelido(jTexApelido.getText().trim().toUpperCase());
            ecc.setTipoLogradouro(jComTipoLogradouro.getSelectedItem().toString().trim());
            ecc.setLogradouro(jTexLogradouro.getText().trim().toUpperCase());
            ecc.setNumero(jTexNumero.getText().trim().toUpperCase());
            ecc.setComplemento(jTexComplemento.getText().trim().toUpperCase());
            ecc.setBairro(jTexBairro.getText().trim().toUpperCase());
            ecc.setCdMunicipioIbge(jTexCdMunicipioIbge.getText().trim());
            ecc.setSiglaUf(jTexSiglaUF.getText().toUpperCase().toString().substring(jTexSiglaUF.getText().trim().length() - 2, 2));
            ecc.setCdCep(jForCep.getText().toString().trim());
            ecc.setUsuarioCadastro(su.getUsuarioConectado());
            dat.setData(data);
            data = dat.getData();
            ecc.setDataCadastro(data);
            ecc.setSituacao(jComSituacao.getSelectedItem().toString().substring(0, 1));
            EnderecoCobrancaClientesDAO eccdao = null;
            sql = "SELECT * FROM GCVENDCOBRANCA WHERE CPF_CNPJ = '" + cpfCnpj
                    + "'";
            try {
                eccdao = new EnderecoCobrancaClientesDAO(conexao);
                if ("N".equals(oper)) {
                    eccdao.adicionar(ecc);
                } else {
                    eccdao.atualizar(ecc);
                }
            } catch (SQLException ex) {
                Logger.getLogger(ManterEnderecoCobrancaClientes.class.getName()).log(Level.SEVERE, null, ex);
            }
            limparTela();
            bloquearCampos();
            pesquisar();
        }
    }//GEN-LAST:event_jButSalvarActionPerformed

    private void jButPesquisarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButPesquisarActionPerformed
        // TODO add your handling code here:
        jButEditar.setEnabled(true);
        String cpfCnpj = jForCdCpfCnpj.getText().trim();
                cpfCnpj = cpfCnpj.replace(".", "");
                cpfCnpj = cpfCnpj.replace("-", "");
                cpfCnpj = cpfCnpj.replace("/", "");
                
        if (jTexNomeRazaoSocial.getText().isEmpty()) {
            sql = "SELECT * FROM GCVENDCOBRANCA WHERE CPF_CNPJ = '"+cpfCnpj
                    + "'";
        } else {
            sql = "SELECT * FROM GCVENDCOBRANCA WHERE NOME_RAZAOSOCIAL LIKE '" + jTexNomeRazaoSocial.getText().trim() + "'";
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
        oper = "N";         // se cancelar a ação atual na tela do sistema a operação do sistema será N  de novo Registro
    }//GEN-LAST:event_jButCancelarActionPerformed

    private void jButEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButEditarActionPerformed
        // TODO add your handling code here:
        liberarCampos();
        jForInscEstadual.requestFocus();
        jForInscEstadual.selectAll();
        oper = "A";         // se estiver editando um registro do sistema, a operação do sistema será A de alteração de Registro
    }//GEN-LAST:event_jButEditarActionPerformed

    private void jMenuItemSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSairActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jMenuItemSairActionPerformed

    private void jMenuItemSalvarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemSalvarActionPerformed
        // TODO add your handling code here:
        jButSalvarActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemSalvarActionPerformed

    private void jMenuItemEditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItemEditarActionPerformed
        // TODO add your handling code here:
        jButEditarActionPerformed(evt);
    }//GEN-LAST:event_jMenuItemEditarActionPerformed

    private void jButNovoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButNovoActionPerformed
        // TODO add your handling code here:
        novoRegistro();
        oper = "N";
    }//GEN-LAST:event_jButNovoActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        // TODO add your handling code here:
        jButNovoActionPerformed(evt);
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButExcluirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButExcluirActionPerformed
        // TODO add your handling code here:
        if (!jForCdCpfCnpj.getText().isEmpty()) {
            try {
                EnderecoCobrancaCliente cc = new EnderecoCobrancaCliente();
                String cpfCnpj = jForCdCpfCnpj.getText().trim();
                cpfCnpj = cpfCnpj.replace(".", "");
                cpfCnpj = cpfCnpj.replace("-", "");
                cpfCnpj = cpfCnpj.replace("/", "");
                cc.setCdCpfCnpj(cpfCnpj);
                EnderecoCobrancaClientesDAO ccDAO = new EnderecoCobrancaClientesDAO(conexao);
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

    private void jButValidarCepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButValidarCepActionPerformed
        try {
            buscarCep();
        } catch (IOException ex) {
            Logger.getLogger(ManterEnderecoCobrancaClientes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButValidarCepActionPerformed

    private void jTexSiglaUFKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexSiglaUFKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomUF();
        }
    }//GEN-LAST:event_jTexSiglaUFKeyPressed

    private void jTexMuncipioKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTexMuncipioKeyPressed
        vt = new VerificarTecla();
        if ("F5".equals(vt.VerificarTecla(evt).toUpperCase())) {
            zoomMunicipio();
        }
    }//GEN-LAST:event_jTexMuncipioKeyPressed

    private void jTexNomeRazaoSocialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTexNomeRazaoSocialActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTexNomeRazaoSocialActionPerformed

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
            java.util.logging.Logger.getLogger(ManterEnderecoCobrancaClientes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ManterEnderecoCobrancaClientes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ManterEnderecoCobrancaClientes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);

        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ManterEnderecoCobrancaClientes.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ManterEnderecoCobrancaClientes(su, conexao).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButAnterior;
    private javax.swing.JButton jButCancelar;
    private javax.swing.JButton jButEditar;
    private javax.swing.JButton jButExcluir;
    private javax.swing.JButton jButNovo;
    private javax.swing.JButton jButPesquisar;
    private javax.swing.JButton jButProximo;
    private javax.swing.JButton jButSair;
    private javax.swing.JButton jButSalvar;
    private javax.swing.JButton jButValidarCep;
    private javax.swing.JComboBox<String> jComSituacao;
    private javax.swing.JComboBox<String> jComTipoLogradouro;
    private javax.swing.JFormattedTextField jForCdCpfCnpj;
    private javax.swing.JFormattedTextField jForCep;
    private javax.swing.JFormattedTextField jForDataCadastro;
    private javax.swing.JFormattedTextField jForDataModificacao;
    private javax.swing.JFormattedTextField jForInscEstadual;
    private javax.swing.JLabel jLabCadastradoPor;
    private javax.swing.JLabel jLabCep;
    private javax.swing.JLabel jLabCidade;
    private javax.swing.JLabel jLabComplemento;
    private javax.swing.JLabel jLabCpfCnpj;
    private javax.swing.JLabel jLabDataCadastro;
    private javax.swing.JLabel jLabDataModificacao;
    private javax.swing.JLabel jLabInscEstadual;
    private javax.swing.JLabel jLabMunicipio;
    private javax.swing.JLabel jLabNomeTecnico;
    private javax.swing.JLabel jLabNumero;
    private javax.swing.JLabel jLabOrgaoExpedidor;
    private javax.swing.JLabel jLabReg;
    private javax.swing.JLabel jLabSituacao;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItemEditar;
    private javax.swing.JMenuItem jMenuItemSair;
    private javax.swing.JMenuItem jMenuItemSalvar;
    private javax.swing.JPanel jPanBotoes;
    private javax.swing.JPanel jPanEndereco;
    private javax.swing.JPanel jPanSecundario;
    private javax.swing.JPanel jPanTabela;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTableEnderecos;
    private javax.swing.JTextField jTexApelido;
    private javax.swing.JTextField jTexBairro;
    private javax.swing.JTextField jTexCadastradoPor;
    private javax.swing.JTextField jTexCdMunicipioIbge;
    private javax.swing.JTextField jTexCdUfIbge;
    private javax.swing.JTextField jTexComplemento;
    private javax.swing.JTextField jTexLogradouro;
    private javax.swing.JTextField jTexMuncipio;
    private javax.swing.JTextField jTexNomeRazaoSocial;
    private javax.swing.JTextField jTexNumero;
    private javax.swing.JTextField jTexRegAtual;
    private javax.swing.JTextField jTexRegTotal;
    private javax.swing.JTextField jTexSiglaUF;
    private javax.swing.JToolBar jTooMenuFerramentas;
    // End of variables declaration//GEN-END:variables
}
