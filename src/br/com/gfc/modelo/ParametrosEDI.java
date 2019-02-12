/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 criado em 26/11/2018
 */
public class ParametrosEDI extends TipoPagamento {

    private String cdCodigoBeneficiario;
    private int numeroSequencial;
    private String situacaoEdi;
    private String tipoArquvo;
    private String cdTipoServico;
    private String usarVersao;
    private String versaoLayout;
    private String usarBoletoPersonalizado;
    private String cdBoletoPerson;
    private String cdCarteira;
    private String tipoEmissaoBoleto;
    private String tipoEntregaBoleto;
    private String cdJurosMora;
    private String tipoJurosMoraDiaTx;
    private String cdDesconto;
    private String cdBaixaDevolucao;
    private int qtdeDiasBaixaDevol;
    private String mensagem1;
    private String mensagem2;

    private String nomeTipoServico;
    private String nomeBeneficiario;
    
    
    /**
     * Construtor padrão da Classe
     */
    public ParametrosEDI() {

    }
    
    /**
     * 
     * @param cdTipoPagamento
     * @param cdCodigoBeneficiario
     * @param numeroSequencial
     * @param situacaoEdi
     * @param tipoArquvo
     * @param cdTipoServico
     * @param usarVersao
     * @param versaoLayout
     * @param usarBoletoPersonalizado
     * @param cdCarteira
     * @param tipoEmissaoBoleto
     * @param tipoEntregaBoleto
     * @param cdJurosMora
     * @param tipoJurosMoraDiaTx
     * @param cdDesconto
     * @param cdBaixaDevolucao
     * @param qtdeDiasBaixaDevol
     * @param mensagem1
     * @param mensagem2
     * @param usuarioCadastro
     * @param dataCadastro
     * @param horaCadastro
     * @param usuarioModificacao
     * @param dataModificacao
     * @param horaModificacao
     * @param situacao 
     */
    public ParametrosEDI(String cdTipoPagamento,String cdCodigoBeneficiario, int numeroSequencial, String situacaoEdi, String tipoArquvo, String cdTipoServico,
    String usarVersao, String versaoLayout, String usarBoletoPersonalizado, String cdBoletoPerson, String cdCarteira, String tipoEmissaoBoleto, String tipoEntregaBoleto, String cdJurosMora,
    String tipoJurosMoraDiaTx, String cdDesconto, String cdBaixaDevolucao, int qtdeDiasBaixaDevol, String mensagem1, String mensagem2, String usuarioCadastro,
    String dataCadastro, String horaCadastro, String usuarioModificacao, String dataModificacao, String horaModificacao, String situacao){
        setCdTipoPagamento(cdTipoPagamento);
        setCdCodigoBeneficiario(cdCodigoBeneficiario);
        setNumeroSequencial(numeroSequencial);
        setSituacaoEdi(situacaoEdi);
        setTipoArquvo(tipoArquvo);
        setCdTipoServico(cdTipoServico);
        setUsarVersao(usarVersao);
        setVersaoLayout(versaoLayout);
        setUsarBoletoPersonalizado(usarBoletoPersonalizado);
        setCdBoletoPerson(cdBoletoPerson);
        setCdCarteira(cdCarteira);
        setTipoEmissaoBoleto(tipoEmissaoBoleto);
        setTipoEntregaBoleto(tipoEntregaBoleto);
        setCdJurosMora(cdJurosMora);
        setTipoJurosMoraDiaTx(tipoJurosMoraDiaTx);
        setCdDesconto(cdDesconto);
        setCdBaixaDevolucao(cdBaixaDevolucao);
        setQtdeDiasBaixaDevol(qtdeDiasBaixaDevol);
        setMensagem1(mensagem1);
        setMensagem2(mensagem2);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdCodigoBeneficiario
     */
    public String getCdCodigoBeneficiario() {
        return cdCodigoBeneficiario;
    }

    /**
     * @param cdCodigoBeneficiario the cdCodigoBeneficiario to set
     */
    public void setCdCodigoBeneficiario(String cdCodigoBeneficiario) {
        this.cdCodigoBeneficiario = cdCodigoBeneficiario;
    }

    /**
     * @return the numeroSequencial
     */
    public int getNumeroSequencial() {
        return numeroSequencial;
    }

    /**
     * @param numeroSequencial the numeroSequencial to set
     */
    public void setNumeroSequencial(int numeroSequencial) {
        this.numeroSequencial = numeroSequencial;
    }

    /**
     * @return the situacaoEdi
     */
    public String getSituacaoEdi() {
        return situacaoEdi;
    }

    /**
     * @param situacaoEdi the situacaoEdi to set
     */
    public void setSituacaoEdi(String situacaoEdi) {
        this.situacaoEdi = situacaoEdi;
    }

    /**
     * @return the tipoArquvo
     */
    public String getTipoArquvo() {
        return tipoArquvo;
    }

    /**
     * @param tipoArquvo the tipoArquvo to set
     */
    public void setTipoArquvo(String tipoArquvo) {
        this.tipoArquvo = tipoArquvo;
    }

    /**
     * @return the cdTipoServico
     */
    public String getCdTipoServico() {
        return cdTipoServico;
    }

    /**
     * @param cdTipoServico the cdTipoServico to set
     */
    public void setCdTipoServico(String cdTipoServico) {
        this.cdTipoServico = cdTipoServico;
    }

    /**
     * @return the usarVersao
     */
    public String getUsarVersao() {
        return usarVersao;
    }

    /**
     * @param usarVersao the usarVersao to set
     */
    public void setUsarVersao(String usarVersao) {
        this.usarVersao = usarVersao;
    }

    /**
     * @return the versaoLayout
     */
    public String getVersaoLayout() {
        return versaoLayout;
    }

    /**
     * @param versaoLayout the versaoLayout to set
     */
    public void setVersaoLayout(String versaoLayout) {
        this.versaoLayout = versaoLayout;
    }

    /**
     * @return the usarBoletoPersonalizado
     */
    public String getUsarBoletoPersonalizado() {
        return usarBoletoPersonalizado;
    }

    /**
     * @param usarBoletoPersonalizado the usarBoletoPersonalizado to set
     */
    public void setUsarBoletoPersonalizado(String usarBoletoPersonalizado) {
        this.usarBoletoPersonalizado = usarBoletoPersonalizado;
    }

    /**
     * @return the cdBoletoPerson
     */
    public String getCdBoletoPerson() {
        return cdBoletoPerson;
    }

    /**
     * @param cdBoletoPerson the cdBoletoPerson to set
     */
    public void setCdBoletoPerson(String cdBoletoPerson) {
        this.cdBoletoPerson = cdBoletoPerson;
    }

    /**
     * @return the cdCarteira
     */
    public String getCdCarteira() {
        return cdCarteira;
    }

    /**
     * @param cdCarteira the cdCarteira to set
     */
    public void setCdCarteira(String cdCarteira) {
        this.cdCarteira = cdCarteira;
    }

    /**
     * @return the tipoEmissaoBoleto
     */
    public String getTipoEmissaoBoleto() {
        return tipoEmissaoBoleto;
    }

    /**
     * @param tipoEmissaoBoleto the tipoEmissaoBoleto to set
     */
    public void setTipoEmissaoBoleto(String tipoEmissaoBoleto) {
        this.tipoEmissaoBoleto = tipoEmissaoBoleto;
    }

    /**
     * @return the tipoEntregaBoleto
     */
    public String getTipoEntregaBoleto() {
        return tipoEntregaBoleto;
    }

    /**
     * @param tipoEntregaBoleto the tipoEntregaBoleto to set
     */
    public void setTipoEntregaBoleto(String tipoEntregaBoleto) {
        this.tipoEntregaBoleto = tipoEntregaBoleto;
    }

    /**
     * @return the cdJurosMora
     */
    public String getCdJurosMora() {
        return cdJurosMora;
    }

    /**
     * @param cdJurosMora the cdJurosMora to set
     */
    public void setCdJurosMora(String cdJurosMora) {
        this.cdJurosMora = cdJurosMora;
    }

    /**
     * @return the tipoJurosMoraDiaTx
     */
    public String getTipoJurosMoraDiaTx() {
        return tipoJurosMoraDiaTx;
    }

    /**
     * @param tipoJurosMoraDiaTx the tipoJurosMoraDiaTx to set
     */
    public void setTipoJurosMoraDiaTx(String tipoJurosMoraDiaTx) {
        this.tipoJurosMoraDiaTx = tipoJurosMoraDiaTx;
    }

    /**
     * @return the cdDesconto
     */
    public String getCdDesconto() {
        return cdDesconto;
    }

    /**
     * @param cdDesconto the cdDesconto to set
     */
    public void setCdDesconto(String cdDesconto) {
        this.cdDesconto = cdDesconto;
    }

    /**
     * @return the cdBaixaDevolucao
     */
    public String getCdBaixaDevolucao() {
        return cdBaixaDevolucao;
    }

    /**
     * @param cdBaixaDevolucao the cdBaixaDevolucao to set
     */
    public void setCdBaixaDevolucao(String cdBaixaDevolucao) {
        this.cdBaixaDevolucao = cdBaixaDevolucao;
    }

    /**
     * @return the qtdeDiasBaixaDevol
     */
    public int getQtdeDiasBaixaDevol() {
        return qtdeDiasBaixaDevol;
    }

    /**
     * @param qtdeDiasBaixaDevol the qtdeDiasBaixaDevol to set
     */
    public void setQtdeDiasBaixaDevol(int qtdeDiasBaixaDevol) {
        this.qtdeDiasBaixaDevol = qtdeDiasBaixaDevol;
    }

    /**
     * @return the mensagem1
     */
    public String getMensagem1() {
        return mensagem1;
    }

    /**
     * @param mensagem1 the mensagem1 to set
     */
    public void setMensagem1(String mensagem1) {
        this.mensagem1 = mensagem1;
    }

    /**
     * @return the mensagem2
     */
    public String getMensagem2() {
        return mensagem2;
    }

    /**
     * @param mensagem2 the mensagem2 to set
     */
    public void setMensagem2(String mensagem2) {
        this.mensagem2 = mensagem2;
    }

    /**
     * @return the nomeTipoServico
     */
    public String getNomeTipoServico() {
        return nomeTipoServico;
    }

    /**
     * @param nomeTipoServico the nomeTipoServico to set
     */
    public void setNomeTipoServico(String nomeTipoServico) {
        this.nomeTipoServico = nomeTipoServico;
    }

    /**
     * @return the nomeBeneficiario
     */
    public String getNomeBeneficiario() {
        return nomeBeneficiario;
    }

    /**
     * @param nomeBeneficiario the nomeBeneficiario to set
     */
    public void setNomeBeneficiario(String nomeBeneficiario) {
        this.nomeBeneficiario = nomeBeneficiario;
    }
}
