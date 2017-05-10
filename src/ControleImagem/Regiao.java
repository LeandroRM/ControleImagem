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
        this.pixels = new boolean[imagem.getAltura()][imagem.getLargura()];
        
        //Seta todos os valores como false
        populaPixels();
        
        //Seta como true as duas posições pertencentes a regiao
        this.pixels[row1][col1] = true;
        this.pixels[row2][col2] = true;
        
        this.setMedia((imagem.getPIXELS()[row1][col1].getAVG() + imagem.getPIXELS()[row2][col2].getAVG()) / 2); 
    }
    
    public Regiao(Regiao regiao1, Regiao regiao2) {
        //Define qualquer valor
        super(0,0,0);
        this.pixels = new boolean[regiao1.getPixels().length][regiao1.getPixels()[0].length];
        
        for (int i = 0; i < this.pixels.length - 1; i++) {
            for (int j = 0; j < this.pixels[i].length - 1; j++) {
                if (regiao1.getPixels()[i][j] || regiao2.getPixels()[i][j]) {
                    this.pixels[i][j] = true;
                }
            }
        }
        
        int novaMedia = (regiao1.getAVG() * regiao1.getArea()) + (regiao2.getAVG() * regiao2.getArea());
        this.setMedia(novaMedia / (regiao1.getArea() + regiao2.getArea()));
    }

    public void setMedia(int media) {
        this.media = media;
    }

    public boolean[][] getPixels() {
        return pixels;
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
        int area = this.getArea();
        int novaMedia = (this.getAVG() * area) + media;
        novaMedia = novaMedia / (area + 1);
        this.setMedia(novaMedia);

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
            for (boolean pixel : linha) {
                if(pixel) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    public int getBorda(Regiao regiao) {
        int borda = 0;
        
        for (int row = 0; row < this.getPixels().length -1; row++) {
            for (int col = 0; col < this.getPixels()[0].length-1; col++) {
                if (this.getPixels()[row][col]) {
                    borda += regiao.getBorda(row, col);
                }
            }
        }
        
        return borda;
    }
    
    public int getBorda(int row, int col) {
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
        if (col == this.pixels[row].length - 1) { //Caso esteja no canto
            nBorda--;
        } else if (!this.pixels[row][col+1]) {//Caso contrario
            nBorda--;
        }

        //Verifica abaixo
        if (row == this.pixels.length - 1) {//Caso esteja no final da imagem
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
