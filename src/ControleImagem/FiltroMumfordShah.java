package ControleImagem;

/**
 *
 * @author Leandro Ramos<leandroramosmarcelino@hotmail.com>
 */
public class FiltroMumfordShah {
    private final Imagem imagemBase;
    private final int lambaFinal;
    
    public FiltroMumfordShah(Imagem imagemBase, int iteracoes) {
        this.imagemBase = imagemBase;
        
        int auxLambdaFinal = 2;
        for (int i = 0; i < iteracoes-1; i++) {
            auxLambdaFinal *= 2;
        }
        
        this.lambaFinal = auxLambdaFinal;
        
        aplicaFiltro();
    }
    
    private void aplicaFiltro() {
        int lambda = 2;
        while (lambda <= this.lambaFinal) {
            for (int row = 0; row < this.imagemBase.getAltura(); row++) {
                for (int col = 0; col < this.imagemBase.getLargura(); col++) {
                    Cor pixelAtual = this.imagemBase.getPIXELS()[row][col];
                    
                    if (row > 0) {
                        if (this.imagemBase.getPIXELS()[row-1][col] instanceof Regiao) {
                            Regiao regiao = (Regiao) this.imagemBase.getPIXELS()[row-1][col];
                            
                            if (!regiao.isPixelInRegiao(row, col)) {
                                if (this.calculaElasticidade(pixelAtual, regiao) < this.calculaComprimento(lambda, pixelAtual, pixelAtual))
                            }
                        } else {
                            
                        }
                    }
                }
            }
            
            lambda *= 2;
        }
    }
    
    /**
     * Método responsável por calcular a elasticidade entre dois pontos
     * 
     * @param cor1 - 
     * @param cor2
     * @return 
     */
    public int calculaElasticidade(Cor cor1, Cor cor2) {
        int elasticidade = 0;
        
        double coeficienteArea = (cor1.getArea() * cor2.getArea()) / (cor1.getArea() + cor2.getArea());
        int quadradoDiferencaRegioes = cor1.getAVG() - cor2.getAVG();
        quadradoDiferencaRegioes *= quadradoDiferencaRegioes;

        elasticidade = (int) coeficienteArea * quadradoDiferencaRegioes;
        
        return elasticidade;
    }
    
    public int calculaComprimento(int lamba, Cor cor1, Cor cor2) {
        
    }
}
