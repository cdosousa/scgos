/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

import java.util.Objects;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 0.01_beta_0917 criado em 12/03/2019
 */
public class EdiOcorrencia {
    
    private String cdBanco;
    private String cdOcorrencia;
    private String nomeOcorrencia;
    private String liquidarTitulo;
    private String usuarioCadastro;
    private String dataCadastro;
    private String horaCadastro;
    private String usuarioModificacao;
    private String dataModificacao;
    private String horaModificacao;
    private String situacao;
    
    private String nomeBanco;

    /**
     * Construtor padrão da classe
     */
    public EdiOcorrencia() {
    }

    /**
     * Construtor sobrecarregado da classe
     * @param cdBanco
     * @param cdOcorrencia
     * @param nomeOcorrencia
     * @param liquidarTitulo
     * @param usuarioCadastro
     * @param dataCadastro
     * @param horaCadastro
     * @param usuarioModificacao
     * @param dataModificacao
     * @param horaModificacao
     * @param situacao 
     */
    public EdiOcorrencia(String cdBanco, String cdOcorrencia, String nomeOcorrencia, String liquidarTitulo, String usuarioCadastro, String dataCadastro, String horaCadastro, String usuarioModificacao, String dataModificacao, String horaModificacao, String situacao) {
        this.cdBanco = cdBanco;
        this.cdOcorrencia = cdOcorrencia;
        this.nomeOcorrencia = nomeOcorrencia;
        this.liquidarTitulo = liquidarTitulo;
        this.usuarioCadastro = usuarioCadastro;
        this.dataCadastro = dataCadastro;
        this.horaCadastro = horaCadastro;
        this.usuarioModificacao = usuarioModificacao;
        this.dataModificacao = dataModificacao;
        this.horaModificacao = horaModificacao;
        this.situacao = situacao;
    }

    public String getCdBanco() {
        return cdBanco;
    }

    public void setCdBanco(String cdBanco) {
        this.cdBanco = cdBanco;
    }

    public String getCdOcorrencia() {
        return cdOcorrencia;
    }

    public void setCdOcorrencia(String cdOcorrencia) {
        this.cdOcorrencia = cdOcorrencia;
    }

    public String getNomeOcorrencia() {
        return nomeOcorrencia;
    }

    public void setNomeOcorrencia(String nomeOcorrencia) {
        this.nomeOcorrencia = nomeOcorrencia;
    }

    public String getLiquidarTitulo() {
        return liquidarTitulo;
    }

    public void setLiquidarTitulo(String liquidarTitulo) {
        this.liquidarTitulo = liquidarTitulo;
    }

    public String getUsuarioCadastro() {
        return usuarioCadastro;
    }

    public void setUsuarioCadastro(String usuarioCadastro) {
        this.usuarioCadastro = usuarioCadastro;
    }

    public String getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(String dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public String getHoraCadastro() {
        return horaCadastro;
    }

    public void setHoraCadastro(String horaCadastro) {
        this.horaCadastro = horaCadastro;
    }

    public String getUsuarioModificacao() {
        return usuarioModificacao;
    }

    public void setUsuarioModificacao(String usuarioModificacao) {
        this.usuarioModificacao = usuarioModificacao;
    }

    public String getDataModificacao() {
        return dataModificacao;
    }

    public void setDataModificacao(String dataModificacao) {
        this.dataModificacao = dataModificacao;
    }

    public String getHoraModificacao() {
        return horaModificacao;
    }

    public void setHoraModificacao(String horaModificacao) {
        this.horaModificacao = horaModificacao;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
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

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 61 * hash + Objects.hashCode(this.cdBanco);
        hash = 61 * hash + Objects.hashCode(this.cdOcorrencia);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EdiOcorrencia other = (EdiOcorrencia) obj;
        if (!Objects.equals(this.cdBanco, other.cdBanco)) {
            return false;
        }
        if (!Objects.equals(this.cdOcorrencia, other.cdOcorrencia)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "EdiOcorrencia{" + "cdBanco=" + cdBanco + ", cdOcorrencia=" + cdOcorrencia + ", nomeOcorrencia=" + nomeOcorrencia + ", liquidarTitulo=" + liquidarTitulo + ", usuarioCadastro=" + usuarioCadastro + ", dataCadastro=" + dataCadastro + ", horaCadastro=" + horaCadastro + ", usuarioModificacao=" + usuarioModificacao + ", dataModificacao=" + dataModificacao + ", horaModificacao=" + horaModificacao + ", situacao=" + situacao + ", nomeBanco=" + nomeBanco + '}';
    }
    
    
    
    
}
