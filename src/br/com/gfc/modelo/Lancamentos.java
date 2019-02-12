/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gfc.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * @version 2018_03Alpha criado em 14/02/2017
 */

public class Lancamentos extends Portadores{
    private String cdLancamento;
    private int sequencial;
    private String tipoLancamento;      // Pre-Previsao, Tit-Título, Ad-Adiantmento
    private String tipoMovimento;       // Ad-Adiantamento, En-Entrada, Pg-Pagar, pR-Recebido(Pagamento Recebido), Re-Receber, rR-Recebido(Receber Recebido),Sa-Saida, Tr-Tranferência
    private String titulo;
    private int cdParcela;
    private String cpfCnpj;
    private String dataEmissao;
    private String dataVencimento;
    private String dataLiquidacao;
    private double valorLancamento;
    private double valorSaldo;
    private String cdPortador;
    private String tipoDocumento;       // Ped-Pedido Comercial, OC-Ordem de Compra, OS-Ordem de Serviço, NFS-Nota Fiscal Saída, NFE-Nota Fiscal de Entrada, O-Outros
    private String documento;
    private String cdTipoPagamento;
    private String nossoNumeroBanco;
    private double valorJuros;
    private double valorMulta;
    private double valorAtualizado;
    private String gerouArquivo;
    private String cdContaReduzida;
    private String cdCCusto;
    private String contraPartida;
    private String preparado;
    private String cdHistorico;
    private String complementoHistorico;
    private String horaCadastro;
    private String usuarioModificacao;
    private String horaModificacao;
    
    private String nomeRazaoSocial;
    private String nomeTipoPagamento;
    
    /**
     * Contrutor padrão da classe
     */
    public Lancamentos(){
        
    }
    
    public Lancamentos(String cdLancamento, int sequencial, String tipoLancamento, String tipoMovimento, String titulo, int cdParcela, String cpfCnpj, String dataEmissao, String dataVencimento, String dataLiquidacao,
            double valorLancamento, double valorSaldo, String cdPortador, String tipoDocumento, String documento, String cdTipoPagamento, String nossoNumeroBanco,
            double valorJuros, double valorMulta, double valorAtualizado, int diasCartorio, int diasLiquidacao, String gerouArquivo, String cdContaReduzida, String cdCCusto,
            String contraPartida, String preparado, String cdHistorico, String complementoHistorico, String usuarioCadastro, String dataCadastro, String horaCadastro, String usuarioModificacao, String dataModificacao,
            String horaModificacao, String situacao){
        setCdLancamento(cdLancamento);
        setSequencial(sequencial);
        setTipoLancamento(tipoLancamento);
        setTipoMovimento(tipoMovimento);
        setTitulo(titulo);
        setCdParcela(cdParcela);
        setCpfCnpj(cpfCnpj);
        setDataEmissao(dataEmissao);
        setDataVencimento(dataVencimento);
        setDataLiquidacao(dataLiquidacao);
        setValorLancamento(valorLancamento);
        setValorSaldo(valorSaldo);
        setCdPortador(cdPortador);
        setTipoDocumento(tipoDocumento);
        setDocumento(documento);
        setCdTipoPagamento(cdTipoPagamento);
        setNossoNumeroBanco(nossoNumeroBanco);
        setValorJuros(valorJuros);
        setValorMulta(valorMulta);
        setDiasCartorio(diasCartorio);
        setDiasLiquidacao(diasLiquidacao);
        setGerouArquivo(gerouArquivo);
        setCdContaReduzida(cdContaReduzida);
        setCdCCusto(cdCCusto);
        setContraPartida(contraPartida);
        setPreparado(preparado);
        setCdHistorico(cdHistorico);
        setComplementoHistorico(complementoHistorico);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdLancamento
     */
    public String getCdLancamento() {
        return cdLancamento;
    }

    /**
     * @param cdLancamento the cdLancamento to set
     */
    public void setCdLancamento(String cdLancamento) {
        this.cdLancamento = cdLancamento;
    }

    /**
     * @return the tipoLancamento
     */
    public String getTipoLancamento() {
        return tipoLancamento;
    }

    /**
     * @param tipoLancamento the tipoLancamento to set
     */
    public void setTipoLancamento(String tipoLancamento) {
        this.tipoLancamento = tipoLancamento;
    }

    /**
     * @return the tipoMovimento
     */
    public String getTipoMovimento() {
        return tipoMovimento;
    }

    /**
     * @param tipoMovimento the tipoMovimento to set
     */
    public void setTipoMovimento(String tipoMovimento) {
        this.tipoMovimento = tipoMovimento;
    }

    /**
     * @return the cpfCnpj
     */
    public String getCpfCnpj() {
        return cpfCnpj;
    }

    /**
     * @param cpfCnpj the cpfCnpj to set
     */
    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
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
     * @return the dataLiquidacao
     */
    public String getDataLiquidacao() {
        return dataLiquidacao;
    }

    /**
     * @param dataLiquidacao the dataLiquidacao to set
     */
    public void setDataLiquidacao(String dataLiquidacao) {
        this.dataLiquidacao = dataLiquidacao;
    }

    /**
     * @return the valorLancamento
     */
    public double getValorLancamento() {
        return valorLancamento;
    }

    /**
     * @param valorLancamento the valorLancamento to set
     */
    public void setValorLancamento(double valorLancamento) {
        this.valorLancamento = valorLancamento;
    }

    /**
     * @return the valorSaldo
     */
    public double getValorSaldo() {
        return valorSaldo;
    }

    /**
     * @param valorSaldo the valorSaldo to set
     */
    public void setValorSaldo(double valorSaldo) {
        this.valorSaldo = valorSaldo;
    }

    /**
     * @return the cdPortador
     */
    public String getCdPortador() {
        return cdPortador;
    }

    /**
     * @param cdPortador the cdPortador to set
     */
    public void setCdPortador(String cdPortador) {
        this.cdPortador = cdPortador;
    }

    /**
     * @return the tipoDocumento
     */
    public String getTipoDocumento() {
        return tipoDocumento;
    }

    /**
     * @param tipoDocumento the tipoDocumento to set
     */
    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    /**
     * @return the documento
     */
    public String getDocumento() {
        return documento;
    }

    /**
     * @param documento the documento to set
     */
    public void setDocumento(String documento) {
        this.documento = documento;
    }

    /**
     * @return the nossoNumeroBanco
     */
    public String getNossoNumeroBanco() {
        return nossoNumeroBanco;
    }

    /**
     * @param nossoNumeroBanco the nossoNumeroBanco to set
     */
    public void setNossoNumeroBanco(String nossoNumeroBanco) {
        this.nossoNumeroBanco = nossoNumeroBanco;
    }

    /**
     * @return the valorJuros
     */
    public double getValorJuros() {
        return valorJuros;
    }

    /**
     * @param valorJuros the valorJuros to set
     */
    public void setValorJuros(double valorJuros) {
        this.valorJuros = valorJuros;
    }

    /**
     * @return the valorMulta
     */
    public double getValorMulta() {
        return valorMulta;
    }

    /**
     * @param valorMulta the valorMulta to set
     */
    public void setValorMulta(double valorMulta) {
        this.valorMulta = valorMulta;
    }

    /**
     * @return the gerouArquivo
     */
    public String getGerouArquivo() {
        return gerouArquivo;
    }

    /**
     * @param gerouArquivo the gerouArquivo to set
     */
    public void setGerouArquivo(String gerouArquivo) {
        this.gerouArquivo = gerouArquivo;
    }

    /**
     * @return the cdCCusto
     */
    public String getCdCCusto() {
        return cdCCusto;
    }

    /**
     * @param cdCCusto the cdCCusto to set
     */
    public void setCdCCusto(String cdCCusto) {
        this.cdCCusto = cdCCusto;
    }

    /**
     * @return the contraPartida
     */
    public String getContraPartida() {
        return contraPartida;
    }

    /**
     * @param contraPartida the contraPartida to set
     */
    public void setContraPartida(String contraPartida) {
        this.contraPartida = contraPartida;
    }

    /**
     * @return the preparado
     */
    public String getPreparado() {
        return preparado;
    }

    /**
     * @param preparado the preparado to set
     */
    public void setPreparado(String preparado) {
        this.preparado = preparado;
    }

    /**
     * @return the cdHistorico
     */
    public String getCdHistorico() {
        return cdHistorico;
    }

    /**
     * @param cdHistorico the cdHistorico to set
     */
    public void setCdHistorico(String cdHistorico) {
        this.cdHistorico = cdHistorico;
    }

    /**
     * @return the complementoHistorico
     */
    public String getComplementoHistorico() {
        return complementoHistorico;
    }

    /**
     * @param complementoHistorico the complementoHistorico to set
     */
    public void setComplementoHistorico(String complementoHistorico) {
        this.complementoHistorico = complementoHistorico;
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
     * @return the cdParcela
     */
    public int getCdParcela() {
        return cdParcela;
    }

    /**
     * @param cdParcela the cdParcela to set
     */
    public void setCdParcela(int cdParcela) {
        this.cdParcela = cdParcela;
    }

    /**
     * @return the cdContaReduzida
     */
    public String getCdContaReduzida() {
        return cdContaReduzida;
    }

    /**
     * @param cdContaReduzida the cdContaReduzida to set
     */
    public void setCdContaReduzida(String cdContaReduzida) {
        this.cdContaReduzida = cdContaReduzida;
    }

    /**
     * @return the nomeRazaoSocial
     */
    public String getNomeRazaoSocial() {
        return nomeRazaoSocial;
    }

    /**
     * @param nomeRazaoSocial the nomeRazaoSocial to set
     */
    public void setNomeRazaoSocial(String nomeRazaoSocial) {
        this.nomeRazaoSocial = nomeRazaoSocial;
    }

    /**
     * @return the titulo
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * @param titulo the titulo to set
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * @return the valorAtualizado
     */
    public double getValorAtualizado() {
        return valorAtualizado;
    }

    /**
     * @param valorAtualizado the valorAtualizado to set
     */
    public void setValorAtualizado(double valorAtualizado) {
        this.valorAtualizado = valorAtualizado;
    }

    /**
     * @return the cdTipoPagamento
     */
    public String getCdTipoPagamento() {
        return cdTipoPagamento;
    }

    /**
     * @param cdTipoPagamento the cdTipoPagamento to set
     */
    public void setCdTipoPagamento(String cdTipoPagamento) {
        this.cdTipoPagamento = cdTipoPagamento;
    }

    /**
     * @return the nomeTipoPagamento
     */
    public String getNomeTipoPagamento() {
        return nomeTipoPagamento;
    }

    /**
     * @param nomeTipoPagamento the nomeTipoPagamento to set
     */
    public void setNomeTipoPagamento(String nomeTipoPagamento) {
        this.nomeTipoPagamento = nomeTipoPagamento;
    }

    /**
     * @return the sequencial
     */
    public int getSequencial() {
        return sequencial;
    }

    /**
     * @param sequencial the sequencial to set
     */
    public void setSequencial(int sequencial) {
        this.sequencial = sequencial;
    }
}