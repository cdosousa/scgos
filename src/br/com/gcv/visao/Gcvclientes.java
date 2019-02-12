/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.visao;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 *
 * @author cristiano
 */
@Entity
@Table(name = "gcvclientes", catalog = "asafira", schema = "")
@NamedQueries({
    @NamedQuery(name = "Gcvclientes.findAll", query = "SELECT g FROM Gcvclientes g")
    , @NamedQuery(name = "Gcvclientes.findByCpfCnpj", query = "SELECT g FROM Gcvclientes g WHERE g.cpfCnpj = :cpfCnpj")
    , @NamedQuery(name = "Gcvclientes.findByInscEstadual", query = "SELECT g FROM Gcvclientes g WHERE g.inscEstadual = :inscEstadual")
    , @NamedQuery(name = "Gcvclientes.findByTipoPessoa", query = "SELECT g FROM Gcvclientes g WHERE g.tipoPessoa = :tipoPessoa")
    , @NamedQuery(name = "Gcvclientes.findByNomeRazaosocial", query = "SELECT g FROM Gcvclientes g WHERE g.nomeRazaosocial = :nomeRazaosocial")
    , @NamedQuery(name = "Gcvclientes.findByApelido", query = "SELECT g FROM Gcvclientes g WHERE g.apelido = :apelido")
    , @NamedQuery(name = "Gcvclientes.findByOptanteSimples", query = "SELECT g FROM Gcvclientes g WHERE g.optanteSimples = :optanteSimples")
    , @NamedQuery(name = "Gcvclientes.findByOptanteSimbahia", query = "SELECT g FROM Gcvclientes g WHERE g.optanteSimbahia = :optanteSimbahia")
    , @NamedQuery(name = "Gcvclientes.findByOptanteSuframa", query = "SELECT g FROM Gcvclientes g WHERE g.optanteSuframa = :optanteSuframa")
    , @NamedQuery(name = "Gcvclientes.findByCdSuframa", query = "SELECT g FROM Gcvclientes g WHERE g.cdSuframa = :cdSuframa")
    , @NamedQuery(name = "Gcvclientes.findByTipoLogradouro", query = "SELECT g FROM Gcvclientes g WHERE g.tipoLogradouro = :tipoLogradouro")
    , @NamedQuery(name = "Gcvclientes.findByLogradouro", query = "SELECT g FROM Gcvclientes g WHERE g.logradouro = :logradouro")
    , @NamedQuery(name = "Gcvclientes.findByNumero", query = "SELECT g FROM Gcvclientes g WHERE g.numero = :numero")
    , @NamedQuery(name = "Gcvclientes.findByComplemento", query = "SELECT g FROM Gcvclientes g WHERE g.complemento = :complemento")
    , @NamedQuery(name = "Gcvclientes.findByBairro", query = "SELECT g FROM Gcvclientes g WHERE g.bairro = :bairro")
    , @NamedQuery(name = "Gcvclientes.findByCdMunicipioIbge", query = "SELECT g FROM Gcvclientes g WHERE g.cdMunicipioIbge = :cdMunicipioIbge")
    , @NamedQuery(name = "Gcvclientes.findBySiglaUf", query = "SELECT g FROM Gcvclientes g WHERE g.siglaUf = :siglaUf")
    , @NamedQuery(name = "Gcvclientes.findByCep", query = "SELECT g FROM Gcvclientes g WHERE g.cep = :cep")
    , @NamedQuery(name = "Gcvclientes.findByNumBanco", query = "SELECT g FROM Gcvclientes g WHERE g.numBanco = :numBanco")
    , @NamedQuery(name = "Gcvclientes.findByNomeBanco", query = "SELECT g FROM Gcvclientes g WHERE g.nomeBanco = :nomeBanco")
    , @NamedQuery(name = "Gcvclientes.findByAgenciaBanco", query = "SELECT g FROM Gcvclientes g WHERE g.agenciaBanco = :agenciaBanco")
    , @NamedQuery(name = "Gcvclientes.findByNumContaBanco", query = "SELECT g FROM Gcvclientes g WHERE g.numContaBanco = :numContaBanco")
    , @NamedQuery(name = "Gcvclientes.findByCdPortador", query = "SELECT g FROM Gcvclientes g WHERE g.cdPortador = :cdPortador")
    , @NamedQuery(name = "Gcvclientes.findByCdTipopagamento", query = "SELECT g FROM Gcvclientes g WHERE g.cdTipopagamento = :cdTipopagamento")
    , @NamedQuery(name = "Gcvclientes.findByCdCondpag", query = "SELECT g FROM Gcvclientes g WHERE g.cdCondpag = :cdCondpag")
    , @NamedQuery(name = "Gcvclientes.findByContato", query = "SELECT g FROM Gcvclientes g WHERE g.contato = :contato")
    , @NamedQuery(name = "Gcvclientes.findByTelefone", query = "SELECT g FROM Gcvclientes g WHERE g.telefone = :telefone")
    , @NamedQuery(name = "Gcvclientes.findByCelular", query = "SELECT g FROM Gcvclientes g WHERE g.celular = :celular")
    , @NamedQuery(name = "Gcvclientes.findByEmail", query = "SELECT g FROM Gcvclientes g WHERE g.email = :email")
    , @NamedQuery(name = "Gcvclientes.findByUsuarioCadastro", query = "SELECT g FROM Gcvclientes g WHERE g.usuarioCadastro = :usuarioCadastro")
    , @NamedQuery(name = "Gcvclientes.findByDataCadastro", query = "SELECT g FROM Gcvclientes g WHERE g.dataCadastro = :dataCadastro")
    , @NamedQuery(name = "Gcvclientes.findByDataModificacao", query = "SELECT g FROM Gcvclientes g WHERE g.dataModificacao = :dataModificacao")
    , @NamedQuery(name = "Gcvclientes.findBySituacao", query = "SELECT g FROM Gcvclientes g WHERE g.situacao = :situacao")})
public class Gcvclientes implements Serializable {

    @Transient
    private PropertyChangeSupport changeSupport = new PropertyChangeSupport(this);

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "cpf_cnpj")
    private String cpfCnpj;
    @Column(name = "insc_estadual")
    private String inscEstadual;
    @Column(name = "tipo_pessoa")
    private String tipoPessoa;
    @Column(name = "nome_razaosocial")
    private String nomeRazaosocial;
    @Column(name = "apelido")
    private String apelido;
    @Column(name = "optante_simples")
    private String optanteSimples;
    @Column(name = "optante_simbahia")
    private String optanteSimbahia;
    @Column(name = "optante_suframa")
    private String optanteSuframa;
    @Column(name = "cd_suframa")
    private String cdSuframa;
    @Column(name = "tipo_logradouro")
    private String tipoLogradouro;
    @Column(name = "logradouro")
    private String logradouro;
    @Column(name = "numero")
    private String numero;
    @Column(name = "complemento")
    private String complemento;
    @Column(name = "bairro")
    private String bairro;
    @Basic(optional = false)
    @Column(name = "cd_municipio_ibge")
    private String cdMunicipioIbge;
    @Column(name = "sigla_uf")
    private String siglaUf;
    @Column(name = "cep")
    private String cep;
    @Column(name = "num_banco")
    private String numBanco;
    @Column(name = "nome_banco")
    private String nomeBanco;
    @Column(name = "agencia_banco")
    private String agenciaBanco;
    @Column(name = "num_conta_banco")
    private String numContaBanco;
    @Column(name = "cd_portador")
    private String cdPortador;
    @Column(name = "cd_tipopagamento")
    private String cdTipopagamento;
    @Column(name = "cd_condpag")
    private String cdCondpag;
    @Column(name = "contato")
    private String contato;
    @Column(name = "telefone")
    private String telefone;
    @Column(name = "celular")
    private String celular;
    @Column(name = "email")
    private String email;
    @Column(name = "usuario_cadastro")
    private String usuarioCadastro;
    @Column(name = "data_cadastro")
    @Temporal(TemporalType.DATE)
    private Date dataCadastro;
    @Column(name = "data_modificacao")
    @Temporal(TemporalType.DATE)
    private Date dataModificacao;
    @Column(name = "situacao")
    private String situacao;

    public Gcvclientes() {
    }

    public Gcvclientes(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public Gcvclientes(String cpfCnpj, String cdMunicipioIbge) {
        this.cpfCnpj = cpfCnpj;
        this.cdMunicipioIbge = cdMunicipioIbge;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        String oldCpfCnpj = this.cpfCnpj;
        this.cpfCnpj = cpfCnpj;
        changeSupport.firePropertyChange("cpfCnpj", oldCpfCnpj, cpfCnpj);
    }

    public String getInscEstadual() {
        return inscEstadual;
    }

    public void setInscEstadual(String inscEstadual) {
        String oldInscEstadual = this.inscEstadual;
        this.inscEstadual = inscEstadual;
        changeSupport.firePropertyChange("inscEstadual", oldInscEstadual, inscEstadual);
    }

    public String getTipoPessoa() {
        return tipoPessoa;
    }

    public void setTipoPessoa(String tipoPessoa) {
        String oldTipoPessoa = this.tipoPessoa;
        this.tipoPessoa = tipoPessoa;
        changeSupport.firePropertyChange("tipoPessoa", oldTipoPessoa, tipoPessoa);
    }

    public String getNomeRazaosocial() {
        return nomeRazaosocial;
    }

    public void setNomeRazaosocial(String nomeRazaosocial) {
        String oldNomeRazaosocial = this.nomeRazaosocial;
        this.nomeRazaosocial = nomeRazaosocial;
        changeSupport.firePropertyChange("nomeRazaosocial", oldNomeRazaosocial, nomeRazaosocial);
    }

    public String getApelido() {
        return apelido;
    }

    public void setApelido(String apelido) {
        String oldApelido = this.apelido;
        this.apelido = apelido;
        changeSupport.firePropertyChange("apelido", oldApelido, apelido);
    }

    public String getOptanteSimples() {
        return optanteSimples;
    }

    public void setOptanteSimples(String optanteSimples) {
        String oldOptanteSimples = this.optanteSimples;
        this.optanteSimples = optanteSimples;
        changeSupport.firePropertyChange("optanteSimples", oldOptanteSimples, optanteSimples);
    }

    public String getOptanteSimbahia() {
        return optanteSimbahia;
    }

    public void setOptanteSimbahia(String optanteSimbahia) {
        String oldOptanteSimbahia = this.optanteSimbahia;
        this.optanteSimbahia = optanteSimbahia;
        changeSupport.firePropertyChange("optanteSimbahia", oldOptanteSimbahia, optanteSimbahia);
    }

    public String getOptanteSuframa() {
        return optanteSuframa;
    }

    public void setOptanteSuframa(String optanteSuframa) {
        String oldOptanteSuframa = this.optanteSuframa;
        this.optanteSuframa = optanteSuframa;
        changeSupport.firePropertyChange("optanteSuframa", oldOptanteSuframa, optanteSuframa);
    }

    public String getCdSuframa() {
        return cdSuframa;
    }

    public void setCdSuframa(String cdSuframa) {
        String oldCdSuframa = this.cdSuframa;
        this.cdSuframa = cdSuframa;
        changeSupport.firePropertyChange("cdSuframa", oldCdSuframa, cdSuframa);
    }

    public String getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(String tipoLogradouro) {
        String oldTipoLogradouro = this.tipoLogradouro;
        this.tipoLogradouro = tipoLogradouro;
        changeSupport.firePropertyChange("tipoLogradouro", oldTipoLogradouro, tipoLogradouro);
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        String oldLogradouro = this.logradouro;
        this.logradouro = logradouro;
        changeSupport.firePropertyChange("logradouro", oldLogradouro, logradouro);
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        String oldNumero = this.numero;
        this.numero = numero;
        changeSupport.firePropertyChange("numero", oldNumero, numero);
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        String oldComplemento = this.complemento;
        this.complemento = complemento;
        changeSupport.firePropertyChange("complemento", oldComplemento, complemento);
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        String oldBairro = this.bairro;
        this.bairro = bairro;
        changeSupport.firePropertyChange("bairro", oldBairro, bairro);
    }

    public String getCdMunicipioIbge() {
        return cdMunicipioIbge;
    }

    public void setCdMunicipioIbge(String cdMunicipioIbge) {
        String oldCdMunicipioIbge = this.cdMunicipioIbge;
        this.cdMunicipioIbge = cdMunicipioIbge;
        changeSupport.firePropertyChange("cdMunicipioIbge", oldCdMunicipioIbge, cdMunicipioIbge);
    }

    public String getSiglaUf() {
        return siglaUf;
    }

    public void setSiglaUf(String siglaUf) {
        String oldSiglaUf = this.siglaUf;
        this.siglaUf = siglaUf;
        changeSupport.firePropertyChange("siglaUf", oldSiglaUf, siglaUf);
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        String oldCep = this.cep;
        this.cep = cep;
        changeSupport.firePropertyChange("cep", oldCep, cep);
    }

    public String getNumBanco() {
        return numBanco;
    }

    public void setNumBanco(String numBanco) {
        String oldNumBanco = this.numBanco;
        this.numBanco = numBanco;
        changeSupport.firePropertyChange("numBanco", oldNumBanco, numBanco);
    }

    public String getNomeBanco() {
        return nomeBanco;
    }

    public void setNomeBanco(String nomeBanco) {
        String oldNomeBanco = this.nomeBanco;
        this.nomeBanco = nomeBanco;
        changeSupport.firePropertyChange("nomeBanco", oldNomeBanco, nomeBanco);
    }

    public String getAgenciaBanco() {
        return agenciaBanco;
    }

    public void setAgenciaBanco(String agenciaBanco) {
        String oldAgenciaBanco = this.agenciaBanco;
        this.agenciaBanco = agenciaBanco;
        changeSupport.firePropertyChange("agenciaBanco", oldAgenciaBanco, agenciaBanco);
    }

    public String getNumContaBanco() {
        return numContaBanco;
    }

    public void setNumContaBanco(String numContaBanco) {
        String oldNumContaBanco = this.numContaBanco;
        this.numContaBanco = numContaBanco;
        changeSupport.firePropertyChange("numContaBanco", oldNumContaBanco, numContaBanco);
    }

    public String getCdPortador() {
        return cdPortador;
    }

    public void setCdPortador(String cdPortador) {
        String oldCdPortador = this.cdPortador;
        this.cdPortador = cdPortador;
        changeSupport.firePropertyChange("cdPortador", oldCdPortador, cdPortador);
    }

    public String getCdTipopagamento() {
        return cdTipopagamento;
    }

    public void setCdTipopagamento(String cdTipopagamento) {
        String oldCdTipopagamento = this.cdTipopagamento;
        this.cdTipopagamento = cdTipopagamento;
        changeSupport.firePropertyChange("cdTipopagamento", oldCdTipopagamento, cdTipopagamento);
    }

    public String getCdCondpag() {
        return cdCondpag;
    }

    public void setCdCondpag(String cdCondpag) {
        String oldCdCondpag = this.cdCondpag;
        this.cdCondpag = cdCondpag;
        changeSupport.firePropertyChange("cdCondpag", oldCdCondpag, cdCondpag);
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        String oldContato = this.contato;
        this.contato = contato;
        changeSupport.firePropertyChange("contato", oldContato, contato);
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        String oldTelefone = this.telefone;
        this.telefone = telefone;
        changeSupport.firePropertyChange("telefone", oldTelefone, telefone);
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        String oldCelular = this.celular;
        this.celular = celular;
        changeSupport.firePropertyChange("celular", oldCelular, celular);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        String oldEmail = this.email;
        this.email = email;
        changeSupport.firePropertyChange("email", oldEmail, email);
    }

    public String getUsuarioCadastro() {
        return usuarioCadastro;
    }

    public void setUsuarioCadastro(String usuarioCadastro) {
        String oldUsuarioCadastro = this.usuarioCadastro;
        this.usuarioCadastro = usuarioCadastro;
        changeSupport.firePropertyChange("usuarioCadastro", oldUsuarioCadastro, usuarioCadastro);
    }

    public Date getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(Date dataCadastro) {
        Date oldDataCadastro = this.dataCadastro;
        this.dataCadastro = dataCadastro;
        changeSupport.firePropertyChange("dataCadastro", oldDataCadastro, dataCadastro);
    }

    public Date getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(Date dataModificacao) {
        Date oldDataModificacao = this.dataModificacao;
        this.dataModificacao = dataModificacao;
        changeSupport.firePropertyChange("dataModificacao", oldDataModificacao, dataModificacao);
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        String oldSituacao = this.situacao;
        this.situacao = situacao;
        changeSupport.firePropertyChange("situacao", oldSituacao, situacao);
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (cpfCnpj != null ? cpfCnpj.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gcvclientes)) {
            return false;
        }
        Gcvclientes other = (Gcvclientes) object;
        if ((this.cpfCnpj == null && other.cpfCnpj != null) || (this.cpfCnpj != null && !this.cpfCnpj.equals(other.cpfCnpj))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.gcv.visao.Gcvclientes[ cpfCnpj=" + cpfCnpj + " ]";
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        changeSupport.removePropertyChangeListener(listener);
    }
    
}
