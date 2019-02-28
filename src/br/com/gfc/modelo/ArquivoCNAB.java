/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 */
public class ArquivoCNAB extends ParametrosEDI {

    /**
     * Váriáveis para informações do Registro zero (Header)
     */
    private String cdTipoRegistro;
    private String cdTipoOperacao;
    private String nomeOperacao;
    private String nomeEmpresa;
    private String dataGeracao;
    private String cdLoteHeader;
    private String tipoPessoaEmpresa;
    private String cpfCnpjEmpresa;
    private String horaGeracao;
    private String[] head;

    /**
     * Variáveis para informaões do Registro 1 (Detalhe) de remessa
     */
    private String cdLoteDetalhe;
    private String cdSegmentoRegistro;
    private String cdMovimentoRemessa;
    private String cdModalidadeCarteiraSincon;
    private String cdModalidadeCarteiraSigcb;
    private String cdInstrucaoAlegacao;
    private String nossoNumero;
    private String qtdeMoedaVariavel;
    private String cdOcorrencia;
    private String numeroDocumento;
    private String dataVencimento;
    private String valorTitulo;
    private String cdAgenciaCobradora;
    private String digVerifAgenciaCobradoa;
    private String cdEspecieTitulo;
    private String cdAceite;
    private String dataEmissao;
    private String cdInstrucao1;
    private String cdInstrucao2;
    private String jurosDe1Dia;
    private String dataLimiteDesconto;
    private String valorDesconto;
    private String valorIof;
    private String valorAbatimento;
    private String tipoPessoaCliente;
    private String cpfCnpjCliente;
    private String nomeCliente;
    private String enderecoCliente;
    private String bairroCliente;
    private String cepCliente;
    private String cidadeCliente;
    private String estadoCliente;
    private String sacadorAvalista;
    private String dataMora;
    private String prazoDias;
    private String cdMulta;
    private String dataMulta;
    private String valorMulta;
    private String cdFormaCadTitBanco;
    private String cdCodigoProtesto;
    private String cdMoeda;

    /**
     * Variáveis do registro 1 (detalhes) retorno
     */
    private String valorTarifaCobranca;
    private String valorPrincipal;
    private String valorOutrosCreditos;
    private String cdBoletoDDA;
    private String dataCredito;
    private String cdInstrucaoCancelada;
    private String mensagemErrosInfor;
    private String cdLiquidacao;
    private String[] detalhe;
    
    /**
     * Variáveis do registro 9 (trailer) retorno
     */
    private String[] trailer;
    
    /**
     * Construtor padrão da classe
     */
    public ArquivoCNAB(){
        
    }

    public ArquivoCNAB(String cdLoteDetalhe, String cdSegmentoRegistro, String cdMovimentoRemessa, String cdModalidadeCarteiraSincon, String cdModalidadeCarteiraSigcb, String cdInstrucaoAlegacao,
            String nossoNumero, String qtdeMoedaVariavel, String cdOcorrencia, String numeroDocumento, String dataVencimento, String valorTitulo, String cdAgenciaCobradora,
            String digVerifAgenciaCobradoa, String cdEspecieTitulo, String cdAceite, String dataEmissao, String cdInstrucao1, String cdInstrucao2, String jurosDe1Dia, String dataLimiteDesconto, 
            String valorDesconto, String valorIof, String valorAbatimento, String tipoPessoaCliente, String cpfCnpjCliente, String nomeCliente, String enderecoCliente, String bairroCliente, 
            String cepCliente, String cidadeCliente, String estadoCliente, String sacadorAvalista, String dataMora, String prazoDias, String cdMulta, String dataMulta, String valorMulta, 
            String cdFormaCadTitBanco, String cdCodigoProtesto, String cdMoeda, String valorTarifaCobranca, String valorPrincipal, String valorOutrosCreditos, String cdBoletoDDA, 
            String dataCredito, String cdInstrucaoCancelada, String mensagemErrosInfor, String cdLiquidacao, String[] detalhe) {
        this.cdLoteDetalhe = cdLoteDetalhe;
        this.cdSegmentoRegistro = cdSegmentoRegistro;
        this.cdMovimentoRemessa = cdMovimentoRemessa;
        this.cdModalidadeCarteiraSincon = cdModalidadeCarteiraSincon;
        this.cdModalidadeCarteiraSigcb = cdModalidadeCarteiraSigcb;
        this.cdInstrucaoAlegacao = cdInstrucaoAlegacao;
        this.nossoNumero = nossoNumero;
        this.qtdeMoedaVariavel = qtdeMoedaVariavel;
        this.cdOcorrencia = cdOcorrencia;
        this.numeroDocumento = numeroDocumento;
        this.dataVencimento = dataVencimento;
        this.valorTitulo = valorTitulo;
        this.cdAgenciaCobradora = cdAgenciaCobradora;
        this.digVerifAgenciaCobradoa = digVerifAgenciaCobradoa;
        this.cdEspecieTitulo = cdEspecieTitulo;
        this.cdAceite = cdAceite;
        this.dataEmissao = dataEmissao;
        this.cdInstrucao1 = cdInstrucao1;
        this.cdInstrucao2 = cdInstrucao2;
        this.jurosDe1Dia = jurosDe1Dia;
        this.dataLimiteDesconto = dataLimiteDesconto;
        this.valorDesconto = valorDesconto;
        this.valorIof = valorIof;
        this.valorAbatimento = valorAbatimento;
        this.tipoPessoaCliente = tipoPessoaCliente;
        this.cpfCnpjCliente = cpfCnpjCliente;
        this.nomeCliente = nomeCliente;
        this.enderecoCliente = enderecoCliente;
        this.bairroCliente = bairroCliente;
        this.cepCliente = cepCliente;
        this.cidadeCliente = cidadeCliente;
        this.estadoCliente = estadoCliente;
        this.sacadorAvalista = sacadorAvalista;
        this.dataMora = dataMora;
        this.prazoDias = prazoDias;
        this.cdMulta = cdMulta;
        this.dataMulta = dataMulta;
        this.valorMulta = valorMulta;
        this.cdFormaCadTitBanco = cdFormaCadTitBanco;
        this.cdCodigoProtesto = cdCodigoProtesto;
        this.cdMoeda = cdMoeda;
        this.valorTarifaCobranca = valorTarifaCobranca;
        this.valorPrincipal = valorPrincipal;
        this.valorOutrosCreditos = valorOutrosCreditos;
        this.cdBoletoDDA = cdBoletoDDA;
        this.dataCredito = dataCredito;
        this.cdInstrucaoCancelada = cdInstrucaoCancelada;
        this.mensagemErrosInfor = mensagemErrosInfor;
        this.cdLiquidacao = cdLiquidacao;
        this.detalhe = detalhe;
    }
    
    /**
     * @return the cdTipoRegistro
     */
    public String getCdTipoRegistro() {
        return cdTipoRegistro;
    }

    /**
     * @param cdTipoRegistro the cdTipoRegistro to set
     */
    public void setCdTipoRegistro(String cdTipoRegistro) {
        this.cdTipoRegistro = cdTipoRegistro;
    }

    /**
     * @return the cdTipoOperacao
     */
    public String getCdTipoOperacao() {
        return cdTipoOperacao;
    }

    /**
     * @param cdTipoOperacao the cdTipoOperacao to set
     */
    public void setCdTipoOperacao(String cdTipoOperacao) {
        this.cdTipoOperacao = cdTipoOperacao;
    }

    /**
     * @return the nomeOperacao
     */
    public String getNomeOperacao() {
        return nomeOperacao;
    }

    /**
     * @param nomeOperacao the nomeOperacao to set
     */
    public void setNomeOperacao(String nomeOperacao) {
        this.nomeOperacao = nomeOperacao;
    }

    /**
     * @return the nomeEmpresa
     */
    public String getNomeEmpresa() {
        return nomeEmpresa;
    }

    /**
     * @param nomeEmpresa the nomeEmpresa to set
     */
    public void setNomeEmpresa(String nomeEmpresa) {
        this.nomeEmpresa = nomeEmpresa;
    }

    /**
     * @return the dataGeracao
     */
    public String getDataGeracao() {
        return dataGeracao;
    }

    /**
     * @param dataGeracao the dataGeracao to set
     */
    public void setDataGeracao(String dataGeracao) {
        this.dataGeracao = dataGeracao;
    }

    /**
     * @return the cdLoteHeader
     */
    public String getCdLoteHeader() {
        return cdLoteHeader;
    }

    /**
     * @param cdLoteHeader the cdLoteHeader to set
     */
    public void setCdLoteHeader(String cdLoteHeader) {
        this.cdLoteHeader = cdLoteHeader;
    }

    /**
     * @return the tipoPessoaEmpresa
     */
    public String getTipoPessoaEmpresa() {
        return tipoPessoaEmpresa;
    }

    /**
     * @param tipoPessoaEmpresa the tipoPessoaEmpresa to set
     */
    public void setTipoPessoaEmpresa(String tipoPessoaEmpresa) {
        this.tipoPessoaEmpresa = tipoPessoaEmpresa;
    }

    /**
     * @return the cpfCnpjEmpresa
     */
    public String getCpfCnpjEmpresa() {
        return cpfCnpjEmpresa;
    }

    /**
     * @param cpfCnpjEmpresa the cpfCnpjEmpresa to set
     */
    public void setCpfCnpjEmpresa(String cpfCnpjEmpresa) {
        this.cpfCnpjEmpresa = cpfCnpjEmpresa;
    }

    /**
     * @return the horaGeracao
     */
    public String getHoraGeracao() {
        return horaGeracao;
    }

    /**
     * @param horaGeracao the horaGeracao to set
     */
    public void setHoraGeracao(String horaGeracao) {
        this.horaGeracao = horaGeracao;
    }

    /**
     * @return the cdLoteDetalhe
     */
    public String getCdLoteDetalhe() {
        return cdLoteDetalhe;
    }

    /**
     * @param cdLoteDetalhe the cdLoteDetalhe to set
     */
    public void setCdLoteDetalhe(String cdLoteDetalhe) {
        this.cdLoteDetalhe = cdLoteDetalhe;
    }

    /**
     * @return the cdSegmentoRegistro
     */
    public String getCdSegmentoRegistro() {
        return cdSegmentoRegistro;
    }

    /**
     * @param cdSegmentoRegistro the cdSegmentoRegistro to set
     */
    public void setCdSegmentoRegistro(String cdSegmentoRegistro) {
        this.cdSegmentoRegistro = cdSegmentoRegistro;
    }

    /**
     * @return the cdMovimentoRemessa
     */
    public String getCdMovimentoRemessa() {
        return cdMovimentoRemessa;
    }

    /**
     * @param cdMovimentoRemessa the cdMovimentoRemessa to set
     */
    public void setCdMovimentoRemessa(String cdMovimentoRemessa) {
        this.cdMovimentoRemessa = cdMovimentoRemessa;
    }

    /**
     * @return the cdModalidadeCarteiraSincon
     */
    public String getCdModalidadeCarteiraSincon() {
        return cdModalidadeCarteiraSincon;
    }

    /**
     * @param cdModalidadeCarteiraSincon the cdModalidadeCarteiraSincon to set
     */
    public void setCdModalidadeCarteiraSincon(String cdModalidadeCarteiraSincon) {
        this.cdModalidadeCarteiraSincon = cdModalidadeCarteiraSincon;
    }

    /**
     * @return the cdModalidadeCarteiraSigcb
     */
    public String getCdModalidadeCarteiraSigcb() {
        return cdModalidadeCarteiraSigcb;
    }

    /**
     * @param cdModalidadeCarteiraSigcb the cdModalidadeCarteiraSigcb to set
     */
    public void setCdModalidadeCarteiraSigcb(String cdModalidadeCarteiraSigcb) {
        this.cdModalidadeCarteiraSigcb = cdModalidadeCarteiraSigcb;
    }

    /**
     * @return the cdInstrucaoAlegacao
     */
    public String getCdInstrucaoAlegacao() {
        return cdInstrucaoAlegacao;
    }

    /**
     * @param cdInstrucaoAlegacao the cdInstrucaoAlegacao to set
     */
    public void setCdInstrucaoAlegacao(String cdInstrucaoAlegacao) {
        this.cdInstrucaoAlegacao = cdInstrucaoAlegacao;
    }

    /**
     * @return the nossoNumero
     */
    public String getNossoNumero() {
        return nossoNumero;
    }

    /**
     * @param nossoNumero the nossoNumero to set
     */
    public void setNossoNumero(String nossoNumero) {
        this.nossoNumero = nossoNumero;
    }

    /**
     * @return the qtdeMoedaVariavel
     */
    public String getQtdeMoedaVariavel() {
        return qtdeMoedaVariavel;
    }

    /**
     * @param qtdeMoedaVariavel the qtdeMoedaVariavel to set
     */
    public void setQtdeMoedaVariavel(String qtdeMoedaVariavel) {
        this.qtdeMoedaVariavel = qtdeMoedaVariavel;
    }

    /**
     * @return the cdOcorrencia
     */
    public String getCdOcorrencia() {
        return cdOcorrencia;
    }

    /**
     * @param cdOcorrencia the cdOcorrencia to set
     */
    public void setCdOcorrencia(String cdOcorrencia) {
        this.cdOcorrencia = cdOcorrencia;
    }

    /**
     * @return the numeroDocumento
     */
    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * @param numeroDocumento the numeroDocumento to set
     */
    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    /**
     * @return the dataVencimento
     */
    public String getDataVencimento() {
        return dataVencimento;
    }

    /**
     * @param dataVencimento the dataVencimento to set
     */
    public void setDataVencimento(String dataVencimento) {
        this.dataVencimento = dataVencimento;
    }

    /**
     * @return the valorTitulo
     */
    public String getValorTitulo() {
        return valorTitulo;
    }

    /**
     * @param valorTitulo the valorTitulo to set
     */
    public void setValorTitulo(String valorTitulo) {
        this.valorTitulo = valorTitulo;
    }

    /**
     * @return the cdAgenciaCobradora
     */
    public String getCdAgenciaCobradora() {
        return cdAgenciaCobradora;
    }

    /**
     * @param cdAgenciaCobradora the cdAgenciaCobradora to set
     */
    public void setCdAgenciaCobradora(String cdAgenciaCobradora) {
        this.cdAgenciaCobradora = cdAgenciaCobradora;
    }

    /**
     * @return the digVerifAgenciaCobradoa
     */
    public String getDigVerifAgenciaCobradoa() {
        return digVerifAgenciaCobradoa;
    }

    /**
     * @param digVerifAgenciaCobradoa the digVerifAgenciaCobradoa to set
     */
    public void setDigVerifAgenciaCobradoa(String digVerifAgenciaCobradoa) {
        this.digVerifAgenciaCobradoa = digVerifAgenciaCobradoa;
    }

    /**
     * @return the cdEspecieTitulo
     */
    public String getCdEspecieTitulo() {
        return cdEspecieTitulo;
    }

    /**
     * @param cdEspecieTitulo the cdEspecieTitulo to set
     */
    public void setCdEspecieTitulo(String cdEspecieTitulo) {
        this.cdEspecieTitulo = cdEspecieTitulo;
    }

    /**
     * @return the cdAceite
     */
    public String getCdAceite() {
        return cdAceite;
    }

    /**
     * @param cdAceite the cdAceite to set
     */
    public void setCdAceite(String cdAceite) {
        this.cdAceite = cdAceite;
    }

    /**
     * @return the dataEmissao
     */
    public String getDataEmissao() {
        return dataEmissao;
    }

    /**
     * @param dataEmissao the dataEmissao to set
     */
    public void setDataEmissao(String dataEmissao) {
        this.dataEmissao = dataEmissao;
    }

    /**
     * @return the cdInstrucao1
     */
    public String getCdInstrucao1() {
        return cdInstrucao1;
    }

    /**
     * @param cdInstrucao1 the cdInstrucao1 to set
     */
    public void setCdInstrucao1(String cdInstrucao1) {
        this.cdInstrucao1 = cdInstrucao1;
    }

    /**
     * @return the cdInstrucao2
     */
    public String getCdInstrucao2() {
        return cdInstrucao2;
    }

    /**
     * @param cdInstrucao2 the cdInstrucao2 to set
     */
    public void setCdInstrucao2(String cdInstrucao2) {
        this.cdInstrucao2 = cdInstrucao2;
    }

    /**
     * @return the jurosDe1Dia
     */
    public String getJurosDe1Dia() {
        return jurosDe1Dia;
    }

    /**
     * @param jurosDe1Dia the jurosDe1Dia to set
     */
    public void setJurosDe1Dia(String jurosDe1Dia) {
        this.jurosDe1Dia = jurosDe1Dia;
    }

    /**
     * @return the dataLimiteDesconto
     */
    public String getDataLimiteDesconto() {
        return dataLimiteDesconto;
    }

    /**
     * @param dataLimiteDesconto the dataLimiteDesconto to set
     */
    public void setDataLimiteDesconto(String dataLimiteDesconto) {
        this.dataLimiteDesconto = dataLimiteDesconto;
    }

    /**
     * @return the valorDesconto
     */
    public String getValorDesconto() {
        return valorDesconto;
    }

    /**
     * @param valorDesconto the valorDesconto to set
     */
    public void setValorDesconto(String valorDesconto) {
        this.valorDesconto = valorDesconto;
    }

    /**
     * @return the valorIof
     */
    public String getValorIof() {
        return valorIof;
    }

    /**
     * @param valorIof the valorIof to set
     */
    public void setValorIof(String valorIof) {
        this.valorIof = valorIof;
    }

    /**
     * @return the valorAbatimento
     */
    public String getValorAbatimento() {
        return valorAbatimento;
    }

    /**
     * @param valorAbatimento the valorAbatimento to set
     */
    public void setValorAbatimento(String valorAbatimento) {
        this.valorAbatimento = valorAbatimento;
    }

    /**
     * @return the tipoPessoaCliente
     */
    public String getTipoPessoaCliente() {
        return tipoPessoaCliente;
    }

    /**
     * @param tipoPessoaCliente the tipoPessoaCliente to set
     */
    public void setTipoPessoaCliente(String tipoPessoaCliente) {
        this.tipoPessoaCliente = tipoPessoaCliente;
    }

    /**
     * @return the cpfCnpjCliente
     */
    public String getCpfCnpjCliente() {
        return cpfCnpjCliente;
    }

    /**
     * @param cpfCnpjCliente the cpfCnpjCliente to set
     */
    public void setCpfCnpjCliente(String cpfCnpjCliente) {
        this.cpfCnpjCliente = cpfCnpjCliente;
    }

    /**
     * @return the nomeCliente
     */
    public String getNomeCliente() {
        return nomeCliente;
    }

    /**
     * @param nomeCliente the nomeCliente to set
     */
    public void setNomeCliente(String nomeCliente) {
        this.nomeCliente = nomeCliente;
    }

    /**
     * @return the enderecoCliente
     */
    public String getEnderecoCliente() {
        return enderecoCliente;
    }

    /**
     * @param enderecoCliente the enderecoCliente to set
     */
    public void setEnderecoCliente(String enderecoCliente) {
        this.enderecoCliente = enderecoCliente;
    }

    /**
     * @return the bairroCliente
     */
    public String getBairroCliente() {
        return bairroCliente;
    }

    /**
     * @param bairroCliente the bairroCliente to set
     */
    public void setBairroCliente(String bairroCliente) {
        this.bairroCliente = bairroCliente;
    }

    /**
     * @return the cepCliente
     */
    public String getCepCliente() {
        return cepCliente;
    }

    /**
     * @param cepCliente the cepCliente to set
     */
    public void setCepCliente(String cepCliente) {
        this.cepCliente = cepCliente;
    }

    /**
     * @return the cidadeCliente
     */
    public String getCidadeCliente() {
        return cidadeCliente;
    }

    /**
     * @param cidadeCliente the cidadeCliente to set
     */
    public void setCidadeCliente(String cidadeCliente) {
        this.cidadeCliente = cidadeCliente;
    }

    /**
     * @return the estadoCliente
     */
    public String getEstadoCliente() {
        return estadoCliente;
    }

    /**
     * @param estadoCliente the estadoCliente to set
     */
    public void setEstadoCliente(String estadoCliente) {
        this.estadoCliente = estadoCliente;
    }

    /**
     * @return the sacadorAvalista
     */
    public String getSacadorAvalista() {
        return sacadorAvalista;
    }

    /**
     * @param sacadorAvalista the sacadorAvalista to set
     */
    public void setSacadorAvalista(String sacadorAvalista) {
        this.sacadorAvalista = sacadorAvalista;
    }

    /**
     * @return the dataMora
     */
    public String getDataMora() {
        return dataMora;
    }

    /**
     * @param dataMora the dataMora to set
     */
    public void setDataMora(String dataMora) {
        this.dataMora = dataMora;
    }

    /**
     * @return the prazoDias
     */
    public String getPrazoDias() {
        return prazoDias;
    }

    /**
     * @param prazoDias the prazoDias to set
     */
    public void setPrazoDias(String prazoDias) {
        this.prazoDias = prazoDias;
    }

    /**
     * @return the cdMulta
     */
    public String getCdMulta() {
        return cdMulta;
    }

    /**
     * @param cdMulta the cdMulta to set
     */
    public void setCdMulta(String cdMulta) {
        this.cdMulta = cdMulta;
    }

    /**
     * @return the dataMulta
     */
    public String getDataMulta() {
        return dataMulta;
    }

    /**
     * @param dataMulta the dataMulta to set
     */
    public void setDataMulta(String dataMulta) {
        this.dataMulta = dataMulta;
    }

    /**
     * @return the valorMulta
     */
    public String getValorMulta() {
        return valorMulta;
    }

    /**
     * @param valorMulta the valorMulta to set
     */
    public void setValorMulta(String valorMulta) {
        this.valorMulta = valorMulta;
    }

    /**
     * @return the cdFormaCadTitBanco
     */
    public String getCdFormaCadTitBanco() {
        return cdFormaCadTitBanco;
    }

    /**
     * @param cdFormaCadTitBanco the cdFormaCadTitBanco to set
     */
    public void setCdFormaCadTitBanco(String cdFormaCadTitBanco) {
        this.cdFormaCadTitBanco = cdFormaCadTitBanco;
    }

    /**
     * @return the cdCodigoProtesto
     */
    public String getCdCodigoProtesto() {
        return cdCodigoProtesto;
    }

    /**
     * @param cdCodigoProtesto the cdCodigoProtesto to set
     */
    public void setCdCodigoProtesto(String cdCodigoProtesto) {
        this.cdCodigoProtesto = cdCodigoProtesto;
    }

    /**
     * @return the cdMoeda
     */
    public String getCdMoeda() {
        return cdMoeda;
    }

    /**
     * @param cdMoeda the cdMoeda to set
     */
    public void setCdMoeda(String cdMoeda) {
        this.cdMoeda = cdMoeda;
    }

    /**
     * @return the valorTarifaCobranca
     */
    public String getValorTarifaCobranca() {
        return valorTarifaCobranca;
    }

    /**
     * @param valorTarifaCobranca the valorTarifaCobranca to set
     */
    public void setValorTarifaCobranca(String valorTarifaCobranca) {
        this.valorTarifaCobranca = valorTarifaCobranca;
    }

    /**
     * @return the valorPrincipal
     */
    public String getValorPrincipal() {
        return valorPrincipal;
    }

    /**
     * @param valorPrincipal the valorPrincipal to set
     */
    public void setValorPrincipal(String valorPrincipal) {
        this.valorPrincipal = valorPrincipal;
    }

    /**
     * @return the valorOutrosCreditos
     */
    public String getValorOutrosCreditos() {
        return valorOutrosCreditos;
    }

    /**
     * @param valorOutrosCreditos the valorOutrosCreditos to set
     */
    public void setValorOutrosCreditos(String valorOutrosCreditos) {
        this.valorOutrosCreditos = valorOutrosCreditos;
    }

    /**
     * @return the cdBoletoDDA
     */
    public String getCdBoletoDDA() {
        return cdBoletoDDA;
    }

    /**
     * @param cdBoletoDDA the cdBoletoDDA to set
     */
    public void setCdBoletoDDA(String cdBoletoDDA) {
        this.cdBoletoDDA = cdBoletoDDA;
    }

    /**
     * @return the dataCredito
     */
    public String getDataCredito() {
        return dataCredito;
    }

    /**
     * @param dataCredito the dataCredito to set
     */
    public void setDataCredito(String dataCredito) {
        this.dataCredito = dataCredito;
    }

    /**
     * @return the cdInstrucaoCancelada
     */
    public String getCdInstrucaoCancelada() {
        return cdInstrucaoCancelada;
    }

    /**
     * @param cdInstrucaoCancelada the cdInstrucaoCancelada to set
     */
    public void setCdInstrucaoCancelada(String cdInstrucaoCancelada) {
        this.cdInstrucaoCancelada = cdInstrucaoCancelada;
    }

    /**
     * @return the mensagemErrosInfor
     */
    public String getMensagemErrosInfor() {
        return mensagemErrosInfor;
    }

    /**
     * @param mensagemErrosInfor the mensagemErrosInfor to set
     */
    public void setMensagemErrosInfor(String mensagemErrosInfor) {
        this.mensagemErrosInfor = mensagemErrosInfor;
    }

    /**
     * @return the cdLiquidacao
     */
    public String getCdLiquidacao() {
        return cdLiquidacao;
    }

    /**
     * @param cdLiquidacao the cdLiquidacao to set
     */
    public void setCdLiquidacao(String cdLiquidacao) {
        this.cdLiquidacao = cdLiquidacao;
    }

    /**
     * @return the head
     */
    public String[] getHead() {
        return head;
    }

    /**
     * @param head the head to set
     */
    public void setHead(String[] head) {
        this.head = head;
    }

    /**
     * @return the detalhe
     */
    public String[] getDetalhe() {
        return detalhe;
    }

    /**
     * @param detalhe the detalhe to set
     */
    public void setDetalhe(String[] detalhe) {
        this.detalhe = detalhe;
    }

    /**
     * @return the trailer
     */
    public String[] getTrailer() {
        return trailer;
    }

    /**
     * @param trailer the trailer to set
     */
    public void setTrailer(String[] trailer) {
        this.trailer = trailer;
    }
}
