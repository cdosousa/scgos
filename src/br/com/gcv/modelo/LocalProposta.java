/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.gcv.modelo;

/**
 *
 * @author Cristiano de Oliveira Sousa
 * created on 11/12/2017
 */
public class LocalProposta extends Proposta {
    private int cdLocalAtend;
    
    // construtor padrao da classe
    public LocalProposta(){
        super();
    }
    
    // construtor padrao da classe sobrecarregado
    public LocalProposta(String cdProposta, String cdRevisao, int cdLocal, int cdLocalAtend, String nomeLocal,double metragemArea, 
            double percPerda, String tipoPiso, String tipoRodape, double metragemRodape, double largura,
            String comprimento, double espessura, String tingimento, String clareamento, String cdTipoVerniz,
            String cdEssencia, double valorServicos, double valorProdutos, double valorAdicionais, double valorDescontos, double valorOutrosDescontos,
            double valorTotalBruto, double valorTotalLiquido, String obsLocal, String usuarioCadastro, String dataCadastro, String horaCadastro,
            String usuarioModificacao, String dataModificacao, String horaModificacao, String situacao){
        setCdProposta(cdProposta);
        setCdRevisao(cdRevisao);
        setCdLocal(cdLocal);
        setCdLocalAtend(cdLocalAtend);
        setNomeLocal(nomeLocal);
        setMetragemArea(metragemArea);
        setPercPerda(percPerda);
        setTipoPiso(tipoPiso);
        setTipoRodape(tipoRodape);
        setMetragemRodape(metragemRodape);
        setLargura(largura);
        setComprimento(comprimento);
        setEspessura(espessura);
        setTingimento(tingimento);
        setClareamento(clareamento);
        setCdTipoVerniz(cdTipoVerniz);
        setCdEssencia(cdEssencia);
        setValorServico(valorServicos);
        setValorProdutos(valorProdutos);
        setValorAdicionais(valorAdicionais);
        setValorDescontos(valorDescontos);
        setValorOutrosDescontos(valorOutrosDescontos);
        setValorTotalBruto(valorTotalBruto);
        setValorTotalLiquido(valorTotalLiquido);
        setObs(obsLocal);
        setUsuarioCadastro(usuarioCadastro);
        setDataCadastro(dataCadastro);
        setHoraCadastro(horaCadastro);
        setUsuarioModificacao(usuarioModificacao);
        setDataModificacao(dataModificacao);
        setHoraModificacao(horaModificacao);
        setSituacao(situacao);
    }

    /**
     * @return the cdLocalAtend
     */
    public int getCdLocalAtend() {
        return cdLocalAtend;
    }

    /**
     * @param cdLocalAtend the cdLocalAtend to set
     */
    public void setCdLocalAtend(int cdLocalAtend) {
        this.cdLocalAtend = cdLocalAtend;
    }

    @Override
    /**
     * retorna o calculo entre o valor bruto e os descontos concedidos
     * @return valorTotalLiquidoLocal
     */
    public double getValorTotalLiquido() {
        double valorTotalLiquidoLocal;
        valorTotalLiquidoLocal = getValorTotalBruto() - (getValorDescontos() + getValorOutrosDescontos());
        return valorTotalLiquidoLocal;
    }
}
