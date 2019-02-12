/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 01/11/2017
 */
public class Bancos{
    private String cdBanco;
    private String nomeBanco;
    private String tipoLogradouro;
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cdMunicipioIbge;
    private String cdUfSigla;
    private String cep;
    private String contato;
    private String telefone;
    private String celular;
    private String email;
    private String usuarioCadastro;
    private String dataCadastro;
    private String horaCadastro;
    private String usuarioModificacao;
    private String dataModificacao;
    private String horaModificacao;
    private String situacao;
    
// variáveis de tabelas correlatos
    private String nomeUF;
    private String cdSiglaIbge;
    private String nomeMunicipio;
    
    // contrutor padrão da classe
    public Bancos(){
        
    }
    
    // construtor padrão sobrecarregado
    public Bancos(String cdBanco, String nomeBanco, String tipoLogradouro, String logradouro, String numero, String complemento,
            String bairro, String cdMunicipioIbge, String cdUfSigla, String cep, String contato, String telefone,
            String celular, String email, String usuarioCadastro, String dataCadastro, String dataModificacao, String situacao){
        setCdBanco(cdBanco);
        setNomeBanco(nomeBanco);
        setTipoLogradouro(tipoLogradouro);
        setLogradouro(logradouro);
        setNumero(numero);
        setComplemento(complemento);
        setBairro(bairro);
        setCdMunicipioIbge(cdMunicipioIbge);
        setCdUfSigla(cdUfSigla);
        setCep(cep);
        setContato(contato);
        setTelefone(telefone);
        setCelular(celular);
        setEmail(email);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setDataModificacao(dataModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdBanco
     */
    public String getCdBanco() {
        return cdBanco;
    }

    /**
     * @param cdBanco the cdBanco to set
     */
    public void setCdBanco(String cdBanco) {
        this.cdBanco = cdBanco;
    }

    /**
     * @return the nomeBanco
     */
    public String getNomeBanco() {
        return nomeBanco;
    }

    /**
     * @param nomeBanco the nomeBanco to set
     */
    public void setNomeBanco(String nomeBanco) {
        this.nomeBanco = nomeBanco;
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
     * @param logradouro the tipoLogradouro to set
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
     * @return the cdUfSigla
     */
    public String getCdUfSigla() {
        return cdUfSigla;
    }

    /**
     * @param cdUfSigla the cdUfSigla to set
     */
    public void setCdUfSigla(String cdUfSigla) {
        this.cdUfSigla = cdUfSigla;
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
     * @return the contato
     */
    public String getContato() {
        return contato;
    }

    /**
     * @param contato the contato to set
     */
    public void setContato(String contato) {
        this.contato = contato;
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
     * @return the horaCadastro
     */
    public String getHoraCadastro() {
        return horaCadastro;
    }

    /**
     * @param horaCadastro the horaCadastro to set
     */
    public void setHoraCadastro(String horaCadastro) {
        this.horaCadastro = horaCadastro;
    }

    /**
     * @return the usuarioModificacao
     */
    public String getUsuarioModificacao() {
        return usuarioModificacao;
    }

    /**
     * @param usuarioModificacao the usuarioModificacao to set
     */
    public void setUsuarioModificacao(String usuarioModificacao) {
        this.usuarioModificacao = usuarioModificacao;
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
     * @return the horaModificacao
     */
    public String getHoraModificacao() {
        return horaModificacao;
    }

    /**
     * @param horaModificacao the horaModificacao to set
     */
    public void setHoraModificacao(String horaModificacao) {
        this.horaModificacao = horaModificacao;
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
    
}