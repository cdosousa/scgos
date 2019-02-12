/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfr.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 08/11/2017
 */
public class TiposOperacoes {
    private String cdTipoOper;
    private String cdCfop;
    private String nomeTipoOperacao;
    private String descricaoCfop;
    private String tipoEstoque;
    private double aliquotaPis;
    private double aliquotaCofins;
    private double aliquotaSimples;
    private double aliquotaIpi;
    private double aliquotaIcms;
    private double aliquotaSuframa;
    private double aliquotaSimbahia;
    private String tributaIcms;
    private String tributaIpi;
    private String tributaSuframa;
    private String tributaSimbahia;
    private double baseCalculoIcmsOp;
    private double icmsOpBaseRed;
    private double mva;
    private double baseIcmsStRed;
    private double icmsCadeiaSemRed;
    private double aliquotaIss;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;
    
    // construtor padrao da classe
    public TiposOperacoes(){
        
    }
    
    // construtor padrao sobrecarregado da classe
    public TiposOperacoes(String cdTipoOper, String cdCfop, String nomeTipoOperacao, String descricaoCfop, 
            String tipoEstoque, double aliquotaPis, double aliquotaCofins, double aliquotaSimples, 
            double aliquotaIpi, double aliquotaIcms, double aliquotaSuframa, double aliquotaSimbahia, 
            String tributaIcms, String tributaIpi, String tributaSuframa, String tributaSimbahia, 
            double baseCalculoIcmsOp, double icmsOpBaseRed, double mva, double baseIcmsStRed, 
            double icmsCadeiaSemRed, double aliquotaIss, String usuarioCadastro, String dataCadastro, 
            String dataModificacao, String situacao){
        setCdTipoOper(cdTipoOper);
        setCdCfop(cdCfop);
        setNomeTipoOperacao(nomeTipoOperacao);
        setDescricaoCfop(descricaoCfop);
        setTipoEstoque(tipoEstoque);
        setAliquotaPis(aliquotaPis);
        setAliquotaCofins(aliquotaCofins);
        setAliquotaSimples(aliquotaSimples);
        setAliquotaIpi(aliquotaIpi);
        setAliquotaIcms(aliquotaIcms);
        setAliquotaSuframa(aliquotaSuframa);
        setAliquotaSimbahia(aliquotaSimbahia);
        setTributaIcms(tributaIcms);
        setTributaIpi(tributaIpi);
        setTributaSuframa(tributaSuframa);
        setTributaSimbahia(tributaSimbahia);
        setBaseCalculoIcmsOp(baseCalculoIcmsOp);
        setIcmsOpBaseRed(icmsOpBaseRed);
        setMva(mva);
        setBaseIcmsStRed(baseIcmsStRed);
        setIcmsCadeiaSemRed(icmsCadeiaSemRed);
        setAliquotaIss(aliquotaIss);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdTipoOper
     */
    public String getCdTipoOper() {
        return cdTipoOper;
    }

    /**
     * @param cdTipoOper the cdTipoOper to set
     */
    public void setCdTipoOper(String cdTipoOper) {
        this.cdTipoOper = cdTipoOper;
    }

    /**
     * @return the cdCfop
     */
    public String getCdCfop() {
        return cdCfop;
    }

    /**
     * @param cdCfop the cdCfop to set
     */
    public void setCdCfop(String cdCfop) {
        this.cdCfop = cdCfop;
    }

    /**
     * @return the nomeTipoOperacao
     */
    public String getNomeTipoOperacao() {
        return nomeTipoOperacao;
    }

    /**
     * @param nomeTipoOperacao the nomeTipoOperacao to set
     */
    public void setNomeTipoOperacao(String nomeTipoOperacao) {
        this.nomeTipoOperacao = nomeTipoOperacao;
    }

    /**
     * @return the descricaoCfop
     */
    public String getDescricaoCfop() {
        return descricaoCfop;
    }

    /**
     * @param descricaoCfop the descricaoCfop to set
     */
    public void setDescricaoCfop(String descricaoCfop) {
        this.descricaoCfop = descricaoCfop;
    }

    /**
     * @return the tipoEstoque
     */
    public String getTipoEstoque() {
        return tipoEstoque;
    }

    /**
     * @param tipoEstoque the tipoEstoque to set
     */
    public void setTipoEstoque(String tipoEstoque) {
        this.tipoEstoque = tipoEstoque;
    }

    /**
     * @return the aliquotaPis
     */
    public double getAliquotaPis() {
        return aliquotaPis;
    }

    /**
     * @param aliquotaPis the aliquotaPis to set
     */
    public void setAliquotaPis(double aliquotaPis) {
        this.aliquotaPis = aliquotaPis;
    }

    /**
     * @return the aliquotaCofins
     */
    public double getAliquotaCofins() {
        return aliquotaCofins;
    }

    /**
     * @param aliquotaCofins the aliquotaCofins to set
     */
    public void setAliquotaCofins(double aliquotaCofins) {
        this.aliquotaCofins = aliquotaCofins;
    }

    /**
     * @return the aliquotaSimples
     */
    public double getAliquotaSimples() {
        return aliquotaSimples;
    }

    /**
     * @param aliquotaSimples the aliquotaSimples to set
     */
    public void setAliquotaSimples(double aliquotaSimples) {
        this.aliquotaSimples = aliquotaSimples;
    }

    /**
     * @return the aliquotaIpi
     */
    public double getAliquotaIpi() {
        return aliquotaIpi;
    }

    /**
     * @param aliquotaIpi the aliquotaIpi to set
     */
    public void setAliquotaIpi(double aliquotaIpi) {
        this.aliquotaIpi = aliquotaIpi;
    }

    /**
     * @return the aliquotaIcms
     */
    public double getAliquotaIcms() {
        return aliquotaIcms;
    }

    /**
     * @param aliquotaIcms the aliquotaIcms to set
     */
    public void setAliquotaIcms(double aliquotaIcms) {
        this.aliquotaIcms = aliquotaIcms;
    }

    /**
     * @return the aliquotaSuframa
     */
    public double getAliquotaSuframa() {
        return aliquotaSuframa;
    }

    /**
     * @param aliquotaSuframa the aliquotaSuframa to set
     */
    public void setAliquotaSuframa(double aliquotaSuframa) {
        this.aliquotaSuframa = aliquotaSuframa;
    }

    /**
     * @return the aliquotaSimbahia
     */
    public double getAliquotaSimbahia() {
        return aliquotaSimbahia;
    }

    /**
     * @param aliquotaSimbahia the aliquotaSimbahia to set
     */
    public void setAliquotaSimbahia(double aliquotaSimbahia) {
        this.aliquotaSimbahia = aliquotaSimbahia;
    }

    /**
     * @return the tributaIcms
     */
    public String getTributaIcms() {
        return tributaIcms;
    }

    /**
     * @param tributaIcms the tributaIcms to set
     */
    public void setTributaIcms(String tributaIcms) {
        this.tributaIcms = tributaIcms;
    }

    /**
     * @return the tributaIpi
     */
    public String getTributaIpi() {
        return tributaIpi;
    }

    /**
     * @param tributaIpi the tributaIpi to set
     */
    public void setTributaIpi(String tributaIpi) {
        this.tributaIpi = tributaIpi;
    }

    /**
     * @return the tributaSuframa
     */
    public String getTributaSuframa() {
        return tributaSuframa;
    }

    /**
     * @param tributaSuframa the tributaSuframa to set
     */
    public void setTributaSuframa(String tributaSuframa) {
        this.tributaSuframa = tributaSuframa;
    }

    /**
     * @return the tributaSimbahia
     */
    public String getTributaSimbahia() {
        return tributaSimbahia;
    }

    /**
     * @param tributaSimbahia the tributaSimbahia to set
     */
    public void setTributaSimbahia(String tributaSimbahia) {
        this.tributaSimbahia = tributaSimbahia;
    }

    /**
     * @return the baseCalculoIcmsOp
     */
    public double getBaseCalculoIcmsOp() {
        return baseCalculoIcmsOp;
    }

    /**
     * @param baseCalculoIcmsOp the baseCalculoIcmsOp to set
     */
    public void setBaseCalculoIcmsOp(double baseCalculoIcmsOp) {
        this.baseCalculoIcmsOp = baseCalculoIcmsOp;
    }

    /**
     * @return the icmsOpBaseRed
     */
    public double getIcmsOpBaseRed() {
        return icmsOpBaseRed;
    }

    /**
     * @param icmsOpBaseRed the icmsOpBaseRed to set
     */
    public void setIcmsOpBaseRed(double icmsOpBaseRed) {
        this.icmsOpBaseRed = icmsOpBaseRed;
    }

    /**
     * @return the mva
     */
    public double getMva() {
        return mva;
    }

    /**
     * @param mva the mva to set
     */
    public void setMva(double mva) {
        this.mva = mva;
    }

    /**
     * @return the baseIcmsStRed
     */
    public double getBaseIcmsStRed() {
        return baseIcmsStRed;
    }

    /**
     * @param baseIcmsStRed the baseIcmsStRed to set
     */
    public void setBaseIcmsStRed(double baseIcmsStRed) {
        this.baseIcmsStRed = baseIcmsStRed;
    }

    /**
     * @return the icmsCadeiaSemRed
     */
    public double getIcmsCadeiaSemRed() {
        return icmsCadeiaSemRed;
    }

    /**
     * @param icmsCadeiaSemRed the icmsCadeiaSemRed to set
     */
    public void setIcmsCadeiaSemRed(double icmsCadeiaSemRed) {
        this.icmsCadeiaSemRed = icmsCadeiaSemRed;
    }

    /**
     * @return the aliquotaIss
     */
    public double getAliquotaIss() {
        return aliquotaIss;
    }

    /**
     * @param aliquotaIss the aliquotaIss to set
     */
    public void setAliquotaIss(double aliquotaIss) {
        this.aliquotaIss = aliquotaIss;
    }

    /**
     * @return the usuarioCadastro
     */
    public String getUsuarioCadastro() {
        return usuarioCadastro;
    }

    /**
     * @param usuarioCadastro the usuarioCadastro to set
     */
    public void setUsuarioCadastro(String usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    /**
     * @return the dataCadastro
     */
    public String getDataCadastro() {
        return dataCadastro;
    }

    /**
     * @param dataCadastro the dataCadastro to set
     */
    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    /**
     * @return the dataModificacao
     */
    public String getDataModificacao() {
        return dataModificacao;
    }

    /**
     * @param dataModificacao the dataModificacao to set
     */
    public void setDataModificacao(String dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    /**
     * @return the situacao
     */
    public String getSituacao() {
        return situacao;
    }

    /**
     * @param situacao the situacao to set
     */
    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }
}