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
    
    public int calculaComprimento(int lamba, int rowCor1, int colCor1, int rowCor2, int colCor2) {
        int iBorda = 0;        
        Cor cor1 = this.Imagem.getPixels()[rowCor1][colCor1];
        Cor cor2 = this.Imagem.getPixels()[rowCor2][colCor2];

        if (cor1 instanceof Regiao && cor2 instanceof Regiao) {
            iBorda = cor1.getBorda(cor2);
        } else if (cor1 instanceof Regiao && cor2 instanceof Cor){
            iBorda = cor1.getBorda(rowCor2, colCor2);
        } else if (cor1 instanceof Cor && cor2 instanceof Regiao){
            iBorda = cor2.getBorda(rowCor1, colCor1);
        } else if(cor1 instanceof Cor && cor2 instanceof Cor) {
            iBorda = 1;
        }

        return lambda*borda;
    }
}
