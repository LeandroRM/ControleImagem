package ControleImagem;

/**
 *
 * @author Leandro Ramos<leandroramosmarcelino@hotmail.com>
 */
public class Regiao extends Cor {
    private final boolean[][] pixels;
    private int media;
    
    /**
     *  Cria uma região de uma imagem, ou seja, a fusão de pixels de determinada imagem
     * 
     * @param imagem - imagem onde a região se encontra
     * @param row1 - linha do primeiro pixel
     * @param col1 - coluna do primeiro pixel
     * @param row2 - linha do segundo pixel
     * @param col2  - coluna do segundo pixel
     */
    public Regiao(Imagem imagem, int row1, int col1, int row2, int col2) {
        //Define qualquer valor
        super(0, 0, 0);
        //Cria matriz espelho da imagem
        
        pixels = new boolean[imagem.getAltura()][imagem.getLargura()];
        //Seta todos os valores como false
        populaPixels();
        
        //Seta como true as duas posições pertencentes a regiao
        pixels[row1][col1] = true;
        pixels[row2][col2] = true;
    }

    public void setMedia(int media) {
        this.media = media;
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
       setMedia((this.getAVG() + media) / 2);
       this.pixels[row][col] = true;
    }
    
    /**
     * Retorna se o pixel daquela posição pertence a região
     * 
     * @param row - linha
     * @param col - coluna
     * @return 
     */
    public boolean isPixelInRegiao(int row, int col) {
        return this.pixels[row][col];
    }
    
    /**
     * Retorna a quantidade de pixels contida na Regiao
     * @return - Area da regiao
     */
    @Override
    public int getArea() {
        int count = 0;
        for (boolean[] linha : this.pixels) {
            for (int i = 0; i < linha.length; i++) {
                if(linha[i]) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    public int getBorda(int row, int col) {
        int borda = 1;
        int nBorda = 4;

        //Verifica acima
        if (row == 0) {//Caso esteja no topo
            nBorda--;
        } else if (!this.pixels[row-1][col]) {//Caso não seja o topo
            nBorda--;
        }
        
        //Verifica esquerda
        if (col == 0){//Caso esteja no canto
            nBorda--;
        } else if (!this.pixels[row][col-1]) { //Caso contraio
            nBorda--;
        }

        //Verifica a direita
        if (col == this.pixels[row].length) { //Caso esteja no canto
            nBorda--;
        } else if (!this.pixels[row][col+1]) {//Caso contrario
            nBorda--;
        }

        //Verifica abaixo
        if (row == this.pixels.length) {//Caso esteja no final da imagem
            nBorda--;
        } else if (!this.pixels[row+1][col]){ //Caso não seja o final
            nBorda--;
        }
        
        return nBorda;
    }
    
    private void populaPixels() {
        for (boolean[] pixel : this.pixels) {
            for (int j = 0; j < this.pixels[0].length; j++) {
                pixel[j] = false;
            }
        }
    }
}
