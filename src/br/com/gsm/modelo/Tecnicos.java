/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gsm.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01beta_1017 created on 23/10/2017
 */
public class Tecnicos extends Especialidades{

    //variáveis de instância da classe
    private String cpf;
    private String nomeTecnico;
    private String rg;
    private String dataEmissaoRg;
    private String orgaoExpedidorRg;
    private String tipoLogradouro;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cdMunicipioIbge;
    private String cdSiglaUf;
    private String cep;
    private String telefone;
    private String celular;
    private String email;
    private String possuiHabilitacao;
    private String categoriaCnh;
    private String numCnh;

    
    // variáveis de tabelas correlatos
    private String nomeUF;
    private String cdSiglaIbge;
    private String nomeMunicipio;

    // construtor padrão da classe
    public Tecnicos() {

    }
    
    // construtor sobrecarregado da classe
    public Tecnicos(String cpf, String nomeTecnico, String rg, String dataEmissaoRg, String orgaoExpedidorRg, String tipoLogradouro, 
            String logradouro, String numero, String complemento, String bairro, String cdMunicipioIbge, String cdSiglaUf, String cep,
            String telefone, String celular, String email, String possuiHabilitacao, String categoriaCnh, String numCnh, String usuarioCadastro,
            String dataCadastro, String dataModificacao, String situacao){
        setCpf(cpf);
        setNomeTecnico(nomeTecnico);
        setRg(rg);
        setDataEmissaoRg(dataEmissaoRg);
        setOrgaoExpedidorRg(orgaoExpedidorRg);
        setTipoLogradouro(tipoLogradouro);
        setLogradouro(logradouro);
        setNumero(numero);
        setComplemento(complemento);
        setBairro(bairro);
        setCdMunicipioIbge(cdMunicipioIbge);
        setCdSiglaUf(cdSiglaUf);
        setCep(cep);
        setTelefone(telefone);
        setCelular(celular);
        setEmail(email);
        setPossuiHabilitacao(possuiHabilitacao);
        setCategoriaCnh(categoriaCnh);
        setNumCnh(numCnh);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao.toString().charAt(0));
    }

    /**
     * @return the cpf
     */
    public String getCpf() {
        return cpf;
    }

    /**
     * @param cpf the cpf to set
     */
    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    /**
     * @return the nomeTecnico
     */
    public String getNomeTecnico() {
        return nomeTecnico;
    }

    /**
     * @param nomeTecnico the nomeTecnico to set
     */
    public void setNomeTecnico(String nomeTecnico) {
        this.nomeTecnico = nomeTecnico;
    }

    /**
     * @return the rg
     */
    public String getRg() {
        return rg;
    }

    /**
     * @param rg the rg to set
     */
    public void setRg(String rg) {
        this.rg = rg;
    }

    /**
     * @return the dataEmissaoRg
     */
    public String getDataEmissaoRg() {
        return dataEmissaoRg;
    }

    /**
     * @param dataEmissaoRg the dataEmissaoRg to set
     */
    public void setDataEmissaoRg(String dataEmissaoRg) {
        this.dataEmissaoRg = dataEmissaoRg;
    }

    /**
     * @return the orgaoExpedidorRg
     */
    public String getOrgaoExpedidorRg() {
        return orgaoExpedidorRg;
    }

    /**
     * @param orgaoExpedidorRg the orgaoExpedidorRg to set
     */
    public void setOrgaoExpedidorRg(String orgaoExpedidorRg) {
        this.orgaoExpedidorRg = orgaoExpedidorRg;
    }

    /**
     * @return the tipoLogradouro
     */
    public String getTipoLogradouro() {
        return tipoLogradouro;
    }

    /**
     * @param tipoLogradouro the tipoLogradouro to set
     */
    public void setTipoLogradouro(String tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }

    /**
     * @return the logradouro
     */
    public String getLogradouro() {
        return logradouro;
    }

    /**
     * @param logradouro the logradouro to set
     */
    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    /**
     * @return the numero
     */
    public String getNumero() {
        return numero;
    }

    /**
     * @param numero the numero to set
     */
    public void setNumero(String numero) {
        this.numero = numero;
    }

    /**
     * @return the complemento
     */
    public String getComplemento() {
        return complemento;
    }

    /**
     * @param complemento the complemento to set
     */
    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    /**
     * @return the bairro
     */
    public String getBairro() {
        return bairro;
    }

    /**
     * @param bairro the bairro to set
     */
    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    /**
     * @return the cdMunicipioIbge
     */
    public String getCdMunicipioIbge() {
        return cdMunicipioIbge;
    }

    /**
     * @param cdMunicipioIbge the cdMunicipioIbge to set
     */
    public void setCdMunicipioIbge(String cdMunicipioIbge) {
        this.cdMunicipioIbge = cdMunicipioIbge;
    }

    /**
     * @return the cdSiglaUf
     */
    public String getCdSiglaUf() {
        return cdSiglaUf;
    }

    /**
     * @param cdSiglaUf the cdSiglaUf to set
     */
    public void setCdSiglaUf(String cdSiglaUf) {
        this.cdSiglaUf = cdSiglaUf;
    }

    /**
     * @return the cep
     */
    public String getCep() {
        return cep;
    }

    /**
     * @param cep the cep to set
     */
    public void setCep(String cep) {
        this.cep = cep;
    }

    /**
     * @return the telefone
     */
    public String getTelefone() {
        return telefone;
    }

    /**
     * @param telefone the telefone to set
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    /**
     * @return the celular
     */
    public String getCelular() {
        return celular;
    }

    /**
     * @param celular the celular to set
     */
    public void setCelular(String celular) {
        this.celular = celular;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the possuiHabilitacao
     */
    public String getPossuiHabilitacao() {
        return possuiHabilitacao;
    }

    /**
     * @param possuiHabilitacao the possuiHabilitacao to set
     */
    public void setPossuiHabilitacao(String possuiHabilitacao) {
        this.possuiHabilitacao = possuiHabilitacao;
    }

    /**
     * @return the categoriaCnh
     */
    public String getCategoriaCnh() {
        return categoriaCnh;
    }

    /**
     * @param categoriaCnh the categoriaCnh to set
     */
    public void setCategoriaCnh(String categoriaCnh) {
        this.categoriaCnh = categoriaCnh;
    }

    /**
     * @return the numCnh
     */
    public String getNumCnh() {
        return numCnh;
    }

    /**
     * @param numCnh the numCnh to set
     */
    public void setNumCnh(String numCnh) {
        this.numCnh = numCnh;
    }

    /**
     * @return the nomeUF
     */
    public String getNomeUF() {
        return nomeUF;
    }

    /**
     * @param nomeUF the nomeUF to set
     */
    public void setNomeUF(String nomeUF) {
        this.nomeUF = nomeUF;
    }

    /**
     * @return the nomeMunicipio
     */
    public String getNomeMunicipio() {
        return nomeMunicipio;
    }

    /**
     * @param nomeMunicipio the nomeMunicipio to set
     */
    public void setNomeMunicipio(String nomeMunicipio) {
        this.nomeMunicipio = nomeMunicipio;
    }

    /**
     * @return the cdSiglaIbge
     */
    public String getCdSiglaIbge() {
        return cdSiglaIbge;
    }

    /**
     * @param cdSiglaIbge the cdSiglaIbge to set
     */
    public void setCdSiglaIbge(String cdSiglaIbge) {
        this.cdSiglaIbge = cdSiglaIbge;
    }
}