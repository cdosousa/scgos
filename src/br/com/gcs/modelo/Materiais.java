/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcs.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta0917 created on 03/10/2017
 */
public class Materiais {
    //Variáveis de instância
    // atributos próprios da tabela da tabela
    private String cdSequencial;
    private String cdMaterial;
    private String nomeMaterial;
    private String cdGrupo;
    private String cdSubGrupo;
    private String cdCategoria;
    private String cdMarca;
    private String cdClasse;
    private String cdEssencia;
    private String cdUnidMedida;
    private String tipoProduto;
    private String cdArmazem;
    private double pesoLiquido;
    private double pesoBruto;
    private double ultPrecoCompra;
    private String cdCcusto;
    private String cdCtaContabeReduz;
    private String ultCompra;
    private double estoqueMinimo;
    private double loteMinimo;
    private double loteMultiplo;
    private double largura;
    private double comprimento;
    private double espessura;
    private String cdOrigemCsta;
    private String cdNcm;
    private String descricaoComercial;
    private String usuarioCadastro;
    private String dataCadastro;
    private String dataModificacao;
    private String situacao;

    // atributos relacionados com materiais
    private String nomeGrupo;
    private String nomeSubGrupo;
    private String nomeCategoria;
    private String nomeMarca;
    private String nomeClasse;
    private String nomeEssencia;
    private String nomeUnidMedida;
    private String nomeArmazPadrao;
    private String nomeCCusto;
    private String nomeCtaContabil;
    // atributos relacionados com o preco do material
    private double descAlcada;
    private double valorUnitBruto;
    private double valorUnitLiquido;
    private double valorServico;

    // construtor padrão da classe
    public Materiais() {

    }

    // construtor sobrecarregado da classe com atributos da tabela
    public Materiais(String cdSequencial, String cdMaterial, String nomeMaterial, String cdGrupo, String cdSubGrupo, String cdCategoria, String cdMarca, String cdClasse,
            String cdEssencia, String cdUnidMedida, String tipoProduto, String cdArmazem, double pesoLiquido, double pesoBruto, double ultPrecoCompra,
            String cdCcusto, String cdCtaContabeReduz, String ultCompra, double estoqueMinimo, double loteMinimo, double loteMultiplo, double largura,
            double comprimento, double espessura, String cdOrigemCsta, String cdNcm, String descricaoComercial, String usuarioCadastro, String dataCadastro, String dataModificacao,
            String situacao) {
        setCdSequencial(cdSequencial);
        setCdMaterial(cdMaterial);
        setNomeMaterial(nomeMaterial);
        setCdGrupo(cdGrupo);
        setCdSubGrupo(cdSubGrupo);
        setCdCategoria(cdCategoria);
        setCdMarca(cdMarca);
        setCdClasse(cdClasse);
        setCdEssencia(cdEssencia);
        setCdUnidMedida(cdUnidMedida);
        setTipoProduto(tipoProduto);
        setCdArmazem(cdArmazem);
        setPesoLiquido(pesoLiquido);
        setPesoBruto(pesoBruto);
        setUltPrecoCompra(ultPrecoCompra);
        setCdCcusto(cdCcusto);
        setCdCtaContabeReduz(cdCtaContabeReduz);
        setUltCompra(ultCompra);
        setEstoqueMinimo(estoqueMinimo);
        setLoteMinimo(loteMinimo);
        setLoteMultiplo(loteMultiplo);
        setLargura(largura);
        setComprimento(comprimento);
        setEspessura(espessura);
        setCdOrigemCsta(cdOrigemCsta);
        setCdNcm(cdNcm);
        setDescricaoComercial(descricaoComercial);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    // construtor sobrecarregado da classe com atributos relacionados
    public Materiais(String nomeGrupo, String nomeSubGrupo, String nomeCategoria, String nomeMarca, String nomeClasse, String nomeEssencia,
            String nomeUnidMedida, String nomeArmazPadrao, String nomeCCusto, String nomeCtaContabil) {
        setNomeGrupo(nomeGrupo);
        setNomeSubGrupo(nomeSubGrupo);
        setNomeCategoria(nomeCategoria);
        setNomeMarca(nomeMarca);
        setNomeClasse(nomeClasse);
        setNomeEssencia(nomeEssencia);
        setNomeUnidMedida(nomeUnidMedida);
        setNomeArmazPadrao(nomeArmazPadrao);
        setNomeCCusto(nomeCCusto);
        setNomeCtaContabil(nomeCtaContabil);
    }

    /**
     * @return the cdSequencial
     */
    public String getCdSequencial() {
        return cdSequencial;
    }

    /**
     * @param cdSequencial the cdSequencial to set
     */
    public void setCdSequencial(String cdSequencial) {
        this.cdSequencial = cdSequencial;
    }

    /**
     * @return the cdMaterial
     */
    public String getCdMaterial() {
        return cdMaterial;
    }

    /**
     * @param cdMaterial the cdMaterial to set
     */
    public void setCdMaterial(String cdMaterial) {
        this.cdMaterial = cdMaterial;
    }

    /**
     * @return the nomeMaterial
     */
    public String getNomeMaterial() {
        return nomeMaterial;
    }

    /**
     * @param nomeMaterial the nomeMaterial to set
     */
    public void setNomeMaterial(String nomeMaterial) {
        this.nomeMaterial = nomeMaterial;
    }

    /**
     * @return the cdGrupo
     */
    public String getCdGrupo() {
        return cdGrupo;
    }

    /**
     * @param cdGrupo the cdGrupo to set
     */
    public void setCdGrupo(String cdGrupo) {
        this.cdGrupo = cdGrupo;
    }

    /**
     * @return the cdSubGrupo
     */
    public String getCdSubGrupo() {
        return cdSubGrupo;
    }

    /**
     * @param cdSubGrupo the cdSubGrupo to set
     */
    public void setCdSubGrupo(String cdSubGrupo) {
        this.cdSubGrupo = cdSubGrupo;
    }

    /**
     * @return the cdCategoria
     */
    public String getCdCategoria() {
        return cdCategoria;
    }

    /**
     * @param cdCategoria the cdCategoria to set
     */
    public void setCdCategoria(String cdCategoria) {
        this.cdCategoria = cdCategoria;
    }

    /**
     * @return the cdMarca
     */
    public String getCdMarca() {
        return cdMarca;
    }

    /**
     * @param cdMarca the cdMarca to set
     */
    public void setCdMarca(String cdMarca) {
        this.cdMarca = cdMarca;
    }

    /**
     * @return the cdClasse
     */
    public String getCdClasse() {
        return cdClasse;
    }

    /**
     * @param cdClasse the cdClasse to set
     */
    public void setCdClasse(String cdClasse) {
        this.cdClasse = cdClasse;
    }

    /**
     * @return the cdEssencia
     */
    public String getCdEssencia() {
        return cdEssencia;
    }

    /**
     * @param cdEssencia the cdEssencia to set
     */
    public void setCdEssencia(String cdEssencia) {
        this.cdEssencia = cdEssencia;
    }

    /**
     * @return the cdUnidMedida
     */
    public String getCdUnidMedida() {
        return cdUnidMedida;
    }

    /**
     * @param cdUnidMedida the cdUnidMedida to set
     */
    public void setCdUnidMedida(String cdUnidMedida) {
        this.cdUnidMedida = cdUnidMedida;
    }

    /**
     * @return the tipoProduto
     */
    public String getTipoProduto() {
        return tipoProduto;
    }

    /**
     * @param tipoProduto the tipoProduto to set
     */
    public void setTipoProduto(String tipoProduto) {
        this.tipoProduto = tipoProduto;
    }

    /**
     * @return the cdArmazem
     */
    public String getCdArmazem() {
        return cdArmazem;
    }

    /**
     * @param cdArmazem the cdArmazem to set
     */
    public void setCdArmazem(String cdArmazem) {
        this.cdArmazem = cdArmazem;
    }

    /**
     * @return the pesoLiquido
     */
    public double getPesoLiquido() {
        return pesoLiquido;
    }

    /**
     * @param pesoLiquido the pesoLiquido to set
     */
    public void setPesoLiquido(double pesoLiquido) {
        this.pesoLiquido = pesoLiquido;
    }

    /**
     * @return the pesoBruto
     */
    public double getPesoBruto() {
        return pesoBruto;
    }

    /**
     * @param pesoBruto the pesoBruto to set
     */
    public void setPesoBruto(double pesoBruto) {
        this.pesoBruto = pesoBruto;
    }

    /**
     * @return the ultPrecoCompra
     */
    public double getUltPrecoCompra() {
        return ultPrecoCompra;
    }

    /**
     * @param ultPrecoCompra the ultPrecoCompra to set
     */
    public void setUltPrecoCompra(double ultPrecoCompra) {
        this.ultPrecoCompra = ultPrecoCompra;
    }

    /**
     * @return the cdCcusto
     */
    public String getCdCcusto() {
        return cdCcusto;
    }

    /**
     * @param cdCcusto the cdCcusto to set
     */
    public void setCdCcusto(String cdCcusto) {
        this.cdCcusto = cdCcusto;
    }

    /**
     * @return the cdCtaContabeReduz
     */
    public String getCdCtaContabeReduz() {
        return cdCtaContabeReduz;
    }

    /**
     * @param cdCtaContabeReduz the cdCtaContabeReduz to set
     */
    public void setCdCtaContabeReduz(String cdCtaContabeReduz) {
        this.cdCtaContabeReduz = cdCtaContabeReduz;
    }

    /**
     * @return the ultCompra
     */
    public String getUltCompra() {
        return ultCompra;
    }

    /**
     * @param ultCompra the ultCompra to set
     */
    public void setUltCompra(String ultCompra) {
        this.ultCompra = ultCompra;
    }

    /**
     * @return the estoqueMinimo
     */
    public double getEstoqueMinimo() {
        return estoqueMinimo;
    }

    /**
     * @param estoqueMinimo the estoqueMinimo to set
     */
    public void setEstoqueMinimo(double estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    /**
     * @return the loteMinimo
     */
    public double getLoteMinimo() {
        return loteMinimo;
    }

    /**
     * @param loteMinimo the loteMinimo to set
     */
    public void setLoteMinimo(double loteMinimo) {
        this.loteMinimo = loteMinimo;
    }

    /**
     * @return the loteMultiplo
     */
    public double getLoteMultiplo() {
        return loteMultiplo;
    }

    /**
     * @param loteMultiplo the loteMultiplo to set
     */
    public void setLoteMultiplo(double loteMultiplo) {
        this.loteMultiplo = loteMultiplo;
    }

    /**
     * @return the largura
     */
    public double getLargura() {
        return largura;
    }

    /**
     * @param largura the largura to set
     */
    public void setLargura(double largura) {
        this.largura = largura;
    }

    /**
     * @return the comprimento
     */
    public double getComprimento() {
        return comprimento;
    }

    /**
     * @param comprimento the comprimento to set
     */
    public void setComprimento(double comprimento) {
        this.comprimento = comprimento;
    }

    /**
     * @return the espessura
     */
    public double getEspessura() {
        return espessura;
    }

    /**
     * @param espessura the espessura to set
     */
    public void setEspessura(double espessura) {
        this.espessura = espessura;
    }

    /**
     * @return the cdOrigemCsta
     */
    public String getCdOrigemCsta() {
        return cdOrigemCsta;
    }

    /**
     * @param cdOrigemCsta the cdOrigemCsta to set
     */
    public void setCdOrigemCsta(String cdOrigemCsta) {
        this.cdOrigemCsta = cdOrigemCsta;
    }

    /**
     * @return the cdNcm
     */
    public String getCdNcm() {
        return cdNcm;
    }

    /**
     * @param cdNcm the cdNcm to set
     */
    public void setCdNcm(String cdNcm) {
        this.cdNcm = cdNcm;
    }

    /**
     * @return the descricaoComercial
     */
    public String getDescricaoComercial() {
        return descricaoComercial;
    }

    /**
     * @param descricaoComercial the descricaoComercial to set
     */
    public void setDescricaoComercial(String descricaoComercial) {
        this.descricaoComercial = descricaoComercial;
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

    /**
     * @return the nomeGrupo
     */
    public String getNomeGrupo() {
        return nomeGrupo;
    }

    /**
     * @param nomeGrupo the nomeGrupo to set
     */
    public void setNomeGrupo(String nomeGrupo) {
        this.nomeGrupo = nomeGrupo;
    }

    /**
     * @return the nomeSubGrupo
     */
    public String getNomeSubGrupo() {
        return nomeSubGrupo;
    }

    /**
     * @param nomeSubGrupo the nomeSubGrupo to set
     */
    public void setNomeSubGrupo(String nomeSubGrupo) {
        this.nomeSubGrupo = nomeSubGrupo;
    }

    /**
     * @return the nomeCategoria
     */
    public String getNomeCategoria() {
        return nomeCategoria;
    }

    /**
     * @param nomeCategoria the nomeCategoria to set
     */
    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    /**
     * @return the nomeMarca
     */
    public String getNomeMarca() {
        return nomeMarca;
    }

    /**
     * @param nomeMarca the nomeMarca to set
     */
    public void setNomeMarca(String nomeMarca) {
        this.nomeMarca = nomeMarca;
    }

    /**
     * @return the nomeClasse
     */
    public String getNomeClasse() {
        return nomeClasse;
    }

    /**
     * @param nomeClasse the nomeClasse to set
     */
    public void setNomeClasse(String nomeClasse) {
        this.nomeClasse = nomeClasse;
    }

    /**
     * @return the nomeEssencia
     */
    public String getNomeEssencia() {
        return nomeEssencia;
    }

    /**
     * @param nomeEssencia the nomeEssencia to set
     */
    public void setNomeEssencia(String nomeEssencia) {
        this.nomeEssencia = nomeEssencia;
    }

    /**
     * @return the nomeUnidMedida
     */
    public String getNomeUnidMedida() {
        return nomeUnidMedida;
    }

    /**
     * @param nomeUnidMedida the nomeUnidMedida to set
     */
    public void setNomeUnidMedida(String nomeUnidMedida) {
        this.nomeUnidMedida = nomeUnidMedida;
    }

    /**
     * @return the nomeArmazPadrao
     */
    public String getNomeArmazPadrao() {
        return nomeArmazPadrao;
    }

    /**
     * @param nomeArmazPadrao the nomeArmazPadrao to set
     */
    public void setNomeArmazPadrao(String nomeArmazPadrao) {
        this.nomeArmazPadrao = nomeArmazPadrao;
    }

    /**
     * @return the nomeCCusto
     */
    public String getNomeCCusto() {
        return nomeCCusto;
    }

    /**
     * @param nomeCCusto the nomeCCusto to set
     */
    public void setNomeCCusto(String nomeCCusto) {
        this.nomeCCusto = nomeCCusto;
    }

    /**
     * @return the nomeCtaContabil
     */
    public String getNomeCtaContabil() {
        return nomeCtaContabil;
    }

    /**
     * @param nomeCtaContabil the nomeCtaContabil to set
     */
    public void setNomeCtaContabil(String nomeCtaContabil) {
        this.nomeCtaContabil = nomeCtaContabil;
    }

    /**
     * @return the descAlcada
     */
    public double getDescAlcada() {
        return descAlcada;
    }

    /**
     * @param descAlcada the descAlcada to set
     */
    public void setDescAlcada(double descAlcada) {
        this.descAlcada = descAlcada;
    }

    /**
     * @return the valorUnitBruto
     */
    public double getValorUnitBruto() {
        return valorUnitBruto;
    }

    /**
     * @param valorUnitBruto the valorUnitBruto to set
     */
    public void setValorUnitBruto(double valorUnitBruto) {
        this.valorUnitBruto = valorUnitBruto;
    }

    /**
     * @return the valorUnitLiquido
     */
    public double getValorUnitLiquido() {
        return valorUnitLiquido;
    }

    /**
     * @param valorUnitBruto the valorUnitLiquido to set
     */
    public void setValorUnitLiquido(double valorUnitBruto) {
        this.valorUnitLiquido = valorUnitBruto * (1 - descAlcada / 100);
    }

    /**
     * @return the valorServico
     */
    public double getValorServico() {
        return valorServico;
    }

    /**
     * @param valorServico the valorServico to set
     */
    public void setValorServico(double valorServico) {
        this.valorServico = valorServico;
    }
}
