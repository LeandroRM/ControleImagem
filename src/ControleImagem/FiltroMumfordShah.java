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
                    
                    //Tenta fazer fusão com pixel de cima
                    if (row > 0) {
                        comparaPixels(row, col, row - 1, col, lambda);
                    }
                    
                    //Tenta fazer fusão com pixel da esquerda
                    if (col > 0) {
                        comparaPixels(row, col, row, col - 1, lambda);
                    }
                    
                    //Tenta fazer fusão com pixel de baixo
                    if (row < this.imagemBase.getAltura() - 1) {
                        comparaPixels(row, col, row + 1, col, lambda);
                    }
                    
                    //Tenta fazer fusão com pixel da direita
                    if (row < this.imagemBase.getAltura() - 1) {
                        comparaPixels(row, col, row, col + 1, lambda);
                    }
                }
            }
            
            lambda *= 2;
        }
    }
    
    public void comparaPixels(int rowAtual, int colAtual, int rowComparar, int colComparar, int lambda) {
        Cor pixelAtual = this.imagemBase.getPIXELS()[rowAtual][colAtual];
        Cor pixelComparar = this.imagemBase.getPIXELS()[rowComparar][colComparar];
        
        //Verifica se o pixel acima já está em uma região
        if (pixelComparar instanceof Regiao) {
            Regiao regiao = (Regiao) pixelComparar;

            //Verifica se o pixel atual não pertence a região do pixel acima
            if (!regiao.isPixelInRegiao(rowAtual, colAtual)) {
                //Se o coeficiente da elasticidade for menor que o coeficiente do comprimento, faz a fusão
                if (this.calculaElasticidade(pixelAtual, regiao) < this.calculaComprimento(lambda, rowAtual, colAtual, rowComparar, colComparar)) {
                    this.fusao(rowAtual, colAtual, regiao);
                }
            }
        } else {
            Regiao regiao = (Regiao)pixelAtual;

            //Verifica se este pixel não está em uma região
            if (pixelAtual instanceof Regiao) {
                if (this.calculaElasticidade(regiao, pixelComparar) < this.calculaComprimento(lambda, rowAtual, colAtual, rowComparar, colComparar)) {
                    this.fusao(rowComparar, colComparar, regiao);
                }
            }
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
    
    public int calculaComprimento(int lambda, int rowCor1, int colCor1, int rowCor2, int colCor2) {
        int borda = 0;        
        Cor cor1 = this.imagemBase.getPIXELS()[rowCor1][colCor1];
        Cor cor2 = this.imagemBase.getPIXELS()[rowCor2][colCor2];
        
        if (cor1 instanceof Regiao && cor2 instanceof Regiao) {
            borda = ((Regiao) cor1).getBorda((Regiao) cor2);
        } else if (cor1 instanceof Regiao && cor2 instanceof Cor){
            borda = ((Regiao) cor1).getBorda(rowCor2, colCor2);
        } else if (cor1 instanceof Cor && cor2 instanceof Regiao){
            borda = ((Regiao) cor2).getBorda(rowCor1, colCor1);
        } else if(cor1 instanceof Cor && cor2 instanceof Cor) {
            borda = 1;
        }

        return lambda*borda;
    }
    
    /**
     * Adiciona um novo pixel a uma regiao já existente
     * 
     * @param regiao 
     */
    public void fusao(int row, int col, Regiao regiao) {
        Cor cor1 = this.imagemBase.getPIXELS()[row][col];
        regiao.addPixel(cor1.getAVG(), row, col);
        this.imagemBase.getPIXELS()[row][col] = regiao;        
    }
    
    /**
     * Cria uma nova regiao
     * 
     * @param row1
     * @param col1
     * @param row2
     * @param col2 
     */
    public void fusao(int row1, int col1, int row2, int col2) {
        Regiao regiao = new Regiao(this.imagemBase, row1, col1, row2, col2);        
        this.imagemBase.getPIXELS()[row1][col1] = regiao;        
        this.imagemBase.getPIXELS()[row2][col2] = regiao;        
    }
    
    public void fusao(Regiao regiao1, Regiao regiao2) {
        
    }
}
