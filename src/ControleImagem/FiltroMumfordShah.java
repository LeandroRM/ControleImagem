package ControleImagem;

/**
 *
 * @author Leandro Ramos<leandroramosmarcelino@hotmail.com>
 */
public class FiltroMumfordShah {
    private final Imagem imagemBase;
    
    public FiltroMumfordShah(Imagem imagemBase) {
        this.imagemBase = imagemBase;
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
    
    public int calculaComprimento(int lamba, Cor cor1, Cor cor2) {
        
    }
}
