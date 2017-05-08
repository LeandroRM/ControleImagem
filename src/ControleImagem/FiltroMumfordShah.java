package ControleImagem;

/**
 *
 * @author Leandro Ramos<leandroramosmarcelino@hotmail.com>
 */
public class FiltroMumfordShah {
    private final Imagem imagemBase;
    private final int lambdaFinal;
    
    public FiltroMumfordShah(Imagem imagemBase, int iteracoes) {
        this.imagemBase = imagemBase.escalaCinza();
        
        int auxLambdaFinal = 2;
        for (int i = 0; i < iteracoes-1; i++) {
            auxLambdaFinal *= 2;
        }
        
        this.lambdaFinal = auxLambdaFinal;
        
        aplicaFiltro();
    }

    public Imagem getImagemBase() {
        return imagemBase;
    }
    
    private void aplicaFiltro() {
        int lambda = 2;
        System.out.println("Lambda Final: " + this.lambdaFinal);
        while (lambda <= this.lambdaFinal) {
            for (int row = 0; row < this.imagemBase.getAltura() - 1; row++) {
                for (int col = 0; col < this.imagemBase.getLargura() - 1; col++) {
                    
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
                
                System.out.println("linha: " + (row+1));
            }
            
            lambda *= 2;
            System.out.println("----------------------------------------");
            System.out.println("Lambda: " + lambda);
            System.out.println("----------------------------------------");
        }
    }
    
    /**
     * 
     * @param rowAtual - linha do pixel base
     * @param colAtual - coluna do pixel base
     * @param rowComparar - linha do pixel a ser comparado
     * @param colComparar - coluna do pixel a ser comparado
     * @param lambda - valor do lambda
     */
    private void comparaPixels(int rowAtual, int colAtual, int rowComparar, int colComparar, int lambda) {
        Cor pixelAtual = this.imagemBase.getPIXELS()[rowAtual][colAtual];
        Cor pixelComparar = this.imagemBase.getPIXELS()[rowComparar][colComparar];
        
        //Verifica se o pixel a ser comparado já está em uma região
        if (pixelComparar instanceof Regiao) {
            Regiao regiao = (Regiao) pixelComparar;

            //Verifica se o pixel atual não pertence a mesma região do pixel a ser comparado
            if (!regiao.isPixelInRegiao(rowAtual, colAtual)) {
                //Se o coeficiente da elasticidade for menor que o coeficiente do comprimento, faz a fusão
                if (this.calculaElasticidade(pixelAtual, regiao) < this.calculaComprimento(lambda, rowAtual, colAtual, rowComparar, colComparar)) {
                    this.fusao(rowAtual, colAtual, regiao);
                }
            }
        } else if (pixelAtual instanceof Regiao) { //Verifica se este pixel não está em uma região
            Regiao regiao = (Regiao)pixelAtual;
            
            //Verifica se o pixel a comparar não pertence a mesma região do pixel atual
            if (!regiao.isPixelInRegiao(rowComparar, colComparar)) {
                //Se o coeficiente da elasticidade for menor que o coeficiente do comprimento, faz a fusão
                if (this.calculaElasticidade(regiao, pixelComparar) < this.calculaComprimento(lambda, rowAtual, colAtual, rowComparar, colComparar)) {
                    this.fusao(rowComparar, colComparar, regiao);
                }
            }
        } else {
            //Se o coeficiente da elasticidade for menor que o coeficiente do comprimento, faz a fusão
            if (this.calculaElasticidade(pixelAtual, pixelComparar) < this.calculaComprimento(lambda, rowAtual, colAtual, rowComparar, colComparar)) {
                this.fusao(rowAtual, colAtual, rowComparar, colComparar);
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
        double a = cor1.getArea() * cor2.getArea();
        double b = cor1.getArea() + cor2.getArea();
        
        double coeficienteArea = ((double)(cor1.getArea() * cor2.getArea())) / ((double)(cor1.getArea() + cor2.getArea()));
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
        
        regiao.addPixel((cor1.getAVG() + regiao.getAVG()) / 2, row, col);
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
        Regiao novaRegiao = new Regiao(regiao1, regiao2);
        for (int row = 0; row < novaRegiao.getPixels().length -1; row++) {
            for (int col = 0; col < novaRegiao.getPixels()[row].length -1; col++) {
                if (novaRegiao.getPixels()[row][col]) {
                    this.imagemBase.getPIXELS()[row][col] = novaRegiao;        
                }
            }
        }
    }
}
