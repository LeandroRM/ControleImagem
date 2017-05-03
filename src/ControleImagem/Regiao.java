package ControleImagem;

/**
 *
 * @author Leandro Ramos<leandroramosmarcelino@hotmail.com>
 */
public class Regiao extends Cor {
    private final Cor[][] pixels;
    private int media;
    
    public Regiao(int media, Imagem imagem) {
        super(media, media, media);
        pixels = new Cor[imagem.getAltura()][imagem.getLargura()];
    }

    @Override
    public int getAVG() {
        return this.media;
    }
    
    /**
     * Adiciona um novo pixel a região de pixels
     * 
     * @param media 
     * @param row 
     * @param col 
     */
    public void addPixel(int media, int row, int col) {
       int novaMedia = (this.getAVG() + media) / 2;
        
    }
}
