package ControleImagem;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author leandro
 */
public class Imagem {
    private int valorMaximoCor;
    private final Cor[][] PIXELS;

    public Imagem(Cor[][] PIXELS) {
        this.PIXELS = PIXELS;
    }
    
    public Cor[][] getPIXELS() {
        return PIXELS;
    }
    
    public int getAltura() {
        return this.PIXELS.length;
    }
    public int getLargura() {
        return this.PIXELS[0].length;
    }

    public int getValorMaximoCor() {
        return valorMaximoCor;
    }

    public void setValorMaximoCor(int valorMaximoCor) {
        this.valorMaximoCor = valorMaximoCor;
    }
    
    public int getMenorPixel() {
        int menor = this.valorMaximoCor + 1;
        
        for (Cor[] linha : this.PIXELS) {
            for (Cor pixel : linha) {
                if (pixel.getAVG() < menor) {
                    menor = pixel.getAVG();
                }
            }
        }
        
        return menor;
    }
    
    public int getMaiorPixel() {
        int maior = -1;
        
        for (Cor[] linha : this.PIXELS) {
            for (Cor pixel : linha) {
                if (pixel.getAVG() > maior) {
                    maior = pixel.getAVG();
                }
            }
        }
        
        return maior;
    }
    
    @Override
    public String toString() {
        StringBuilder imagem = new StringBuilder();
        for (Cor[] linha : this.PIXELS) {
            for (Cor pixel : linha) {
                imagem.append(pixel.getRED());
                imagem.append(" ");
                imagem.append(pixel.getGREEN());
                imagem.append(" ");
                imagem.append(pixel.getBLUE());
                imagem.append(" ");
            }
            
            imagem.append("\n");
        }
        
        return imagem.toString().trim();
    }
    
    public String toStringPGM() {
        StringBuilder imagem = new StringBuilder();
        for (Cor[] linha : this.PIXELS) {
            for (Cor pixel : linha) {
                imagem.append(pixel.getAVG());
                imagem.append(" ");
            }
            
            imagem.append("\n");
        }
        
        return imagem.toString().trim();
    }
    
    public String toStringPBM() {
        StringBuilder imagem = new StringBuilder();
        for (Cor[] linha : this.PIXELS) {
            for (Cor pixel : linha) {
                int avg = pixel.getAVG();
                if (avg >= 127) {
                    avg = 1;
                } else {
                    avg = 0;
                }
                
                imagem.append(avg);
                imagem.append(" ");
            }
            
            imagem.append("\n");
        }
        
        return imagem.toString().trim();
    }    

    public boolean gravar(String arquivo) {
        return this.gravar(arquivo, Formato.PPM);
    }
    
    public boolean gravar(String arquivo, Formato formato) {
        try {
            arquivo += Formato.getExtensao(formato);
            FileWriter arq = new FileWriter(arquivo);
            PrintWriter gravarArq = new PrintWriter(arq);
            
            StringBuilder conteudo = new StringBuilder();
            
            conteudo.append(formato.getValue());
            conteudo.append("\n");
            conteudo.append("\n");
            conteudo.append(this.PIXELS.length);
            conteudo.append(" ");
            conteudo.append(this.PIXELS[0].length);
            conteudo.append("\n");
            conteudo.append(this.valorMaximoCor);
            conteudo.append("\n");
            conteudo.append("\n");
            
            if (formato == Formato.PGM) {
                conteudo.append(this.toStringPGM());
            } else if (formato == Formato.PBM) {
                conteudo.append(this.toStringPBM());
            } else {
                conteudo.append(this.toString());
            }
            
            gravarArq.print(conteudo.toString());

            arq.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
    public static Imagem carregarImagem(String arquivo) {
        Imagem imagem = null;
        BufferedReader br = null;
        
        try {
            br = new BufferedReader(new FileReader(arquivo));
            List<List<Cor>> pixels = new ArrayList<>();
            
            int cont = 0;
            int altura = 0;
            int largura = 0;
            int valorMaximoCor = 255;

            while (br.ready()) {
                String linha = br.readLine();
                String[] colunas = linha.trim().split(" ");

                if (colunas.length > 2) {
                    pixels.add(new ArrayList<>());
                    int index = pixels.size() - 1;
                    
                    for (int i = 0; i < colunas.length - 2; i = i + 3) {
                        try {
                            int red = Integer.parseInt(colunas[i]);
                            int green = Integer.parseInt(colunas[i+1]);
                            int blue = Integer.parseInt(colunas[i+2]);

                            pixels.get(index).add(new Cor(red, green, blue));
                        } catch (NumberFormatException ex) {
                            ex.printStackTrace();
                        }
                    }
                } else {
                    if (cont == 1 && !linha.trim().contains(" ")) {
                        cont--;
                    }
                    
                    if (cont == 1) {
                        String[] tamanho = linha.split(" ");
                        altura = Integer.parseInt(tamanho[0]);
                        largura = Integer.parseInt(tamanho[1]);
                    } else if (cont == 2) {
                        valorMaximoCor = Integer.parseInt(linha);
                    }
                    
                    cont++;
                }
            }
            
            int row = 0;
            int col = 0;
            Cor[][] pixelsMatriz = new Cor[altura][largura];
            
            for(List<Cor> linha : pixels) {
                for (Cor pixel : linha) {
                    pixelsMatriz[row][col] = pixel;
                    
                    col++;
                    if (col == largura) {
                        col = 0;
                        row++;
                    }
                }
            }
            
            imagem = new Imagem(pixelsMatriz);
            imagem.setValorMaximoCor(valorMaximoCor);
        } catch(IOException ex) {
            ex.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        return imagem;
    }

    private boolean estaNaMargem(int cor, int corBase, int margem) {
        return cor >= (corBase - margem) && cor <= (corBase + margem);
    }
    
    public enum Formato {
        PBM("P1"),
        PGM("P2"),
        PPM("P3"),
        REL("P3");
        
        private final String value;

        private Formato(String value) {
            this.value = value;
        }
        
        public String getValue() {
            return this.value;
        }
        
        public static String getExtensao(Formato formato) {
            String retorno = "";
            switch (formato) {
                case PGM:
                    retorno = ".pgm";
                    break;
                case PPM:
                    retorno = ".ppm";
                    break;
                case PBM:
                    retorno = ".pbm";
                    break;
            }
            
            return retorno;
        }
    }
    
    public Imagem escalaCinza() {
        Cor[][] pixelsNova = new Cor[this.getAltura()][this.getLargura()];
        
        //Passa por todas as linhas
        for (int row = 0; row < this.getAltura(); row++) {
            //Passa por todas as colunas(pixels) da linha
            for (int col = 0; col < this.getLargura(); col++) {
                //Pega a media do RGB
                int escalaCinza = this.getPIXELS()[row][col].getAVG();
                //Cria novo RGB utilizando a media
                pixelsNova[row][col] = new Cor(escalaCinza, escalaCinza, escalaCinza);
            }
        }
        
        Imagem imagemFinal = new Imagem(pixelsNova);
        imagemFinal.setValorMaximoCor(this.getValorMaximoCor());
        
        return imagemFinal;
    } 
    
    public Imagem espelharImagem() {
        int altura = this.getAltura();
        int largura = this.getLargura();
        
        Cor[][] pixelsNova = new Cor[altura][largura];
        
        //Passa por todas as linhas
        for (int row = 0; row < altura; row++) {
            //Passa por todas as colunas(pixels) da linha
            for (int col = 0; col < largura; col++) {
                Cor pixel = this.getPIXELS()[row][col];
                pixelsNova[row][largura - (col + 1)] = pixel;
            }
        }
        
        Imagem imagemFinal = new Imagem(pixelsNova);
        imagemFinal.setValorMaximoCor(this.getValorMaximoCor());
        
        return imagemFinal;
    }
    
    public Imagem girarImagem90() {
        int altura = this.getAltura();
        int largura = this.getLargura();
        
        Cor[][] pixelsNova = new Cor[largura][altura];
        
        //Passa por todas as linhas
        for (int row = 0; row < altura; row++) {
            //Passa por todas as colunas(pixels) da linha
            for (int col = 0; col < largura; col++) {
                Cor pixel = this.getPIXELS()[row][col];
                pixelsNova[col][altura - (row + 1)] = pixel;
            }
        }
        
        Imagem imagemFinal = new Imagem(pixelsNova);
        imagemFinal.setValorMaximoCor(this.getValorMaximoCor());
        
        return imagemFinal;
    }
    
    public Imagem girarImagem180() {
        int altura = this.getAltura();
        int largura = this.getLargura();
        
        Cor[][] pixelsNova = new Cor[altura][largura];
        
        //Passa por todas as linhas
        for (int row = 0; row < altura; row++) {
            //Passa por todas as colunas(pixels) da linha
            for (int col = 0; col < largura; col++) {
                Cor pixel = this.getPIXELS()[row][col];
                pixelsNova[altura - (row + 1)][largura - (col + 1)] = pixel;
            }
        }
        
        Imagem imagemFinal = new Imagem(pixelsNova);
        imagemFinal.setValorMaximoCor(this.getValorMaximoCor());
        
        return imagemFinal;
    }
    
    public Imagem negativarImagem() {
        Cor[][] pixelsNova = new Cor[this.getAltura()][this.getLargura()];
        
        //Passa por todas as linhas
        for (int row = 0; row < this.getAltura(); row++) {
            //Passa por todas as colunas(pixels) da linha
            for (int col = 0; col < this.getLargura(); col++) {
                Cor pixel = this.getPIXELS()[row][col];
                pixelsNova[row][col] = new Cor(255 - pixel.getRED(), 255 - pixel.getGREEN(), 255 - pixel.getBLUE());
            }
        }
        
        Imagem imagemFinal = new Imagem(pixelsNova);
        imagemFinal.setValorMaximoCor(this.getValorMaximoCor());
        
        return imagemFinal;
    }
    
    public Imagem clarearImagem(float porcentagem) {
        Cor[][] pixelsNova = new Cor[this.getAltura()][this.getLargura()];
        float mult = 1 + (porcentagem / 100);
        
        //Passa por todas as linhas
        for (int row = 0; row < this.getAltura(); row++) {
            //Passa por todas as colunas(pixels) da linha
            for (int col = 0; col < this.getLargura(); col++) {
                Cor pixel = this.getPIXELS()[row][col];
                pixelsNova[row][col] = new Cor(Math.round(pixel.getRED() * mult), Math.round(pixel.getGREEN() * mult), Math.round(pixel.getBLUE() * mult));
            }
        }
        
        Imagem imagemFinal = new Imagem(pixelsNova);
        imagemFinal.setValorMaximoCor(this.getValorMaximoCor());
        
        return imagemFinal;
    }
    
    public Imagem zerarGB() {
        Cor[][] pixelsNova = new Cor[this.getAltura()][this.getLargura()];
        
        //Passa por todas as linhas
        for (int row = 0; row < this.getAltura(); row++) {
            //Passa por todas as colunas(pixels) da linha
            for (int col = 0; col < this.getLargura(); col++) {
                Cor pixel = this.getPIXELS()[row][col];
                pixelsNova[row][col] = new Cor(pixel.getRED(), 0, 0);
            }
        }
        
        Imagem imagemFinal = new Imagem(pixelsNova);
        imagemFinal.setValorMaximoCor(this.getValorMaximoCor());
        
        return imagemFinal;
    }
    
    public Imagem zerarRB() {
        Cor[][] pixelsNova = new Cor[this.getAltura()][this.getLargura()];
        
        //Passa por todas as linhas
        for (int row = 0; row < this.getAltura(); row++) {
            //Passa por todas as colunas(pixels) da linha
            for (int col = 0; col < this.getLargura(); col++) {
                Cor pixel = this.getPIXELS()[row][col];
                pixelsNova[row][col] = new Cor(0, pixel.getGREEN(), 0);
            }
        }
        
        Imagem imagemFinal = new Imagem(pixelsNova);
        imagemFinal.setValorMaximoCor(this.getValorMaximoCor());
        
        return imagemFinal;
    }
    
    public Imagem zerarRG() {
        Cor[][] pixelsNova = new Cor[this.getAltura()][this.getLargura()];
        
        //Passa por todas as linhas
        for (int row = 0; row < this.getAltura(); row++) {
            //Passa por todas as colunas(pixels) da linha
            for (int col = 0; col < this.getLargura(); col++) {
                Cor pixel = this.getPIXELS()[row][col];
                pixelsNova[row][col] = new Cor(0, 0, pixel.getBLUE());
            }
        }
        
        Imagem imagemFinal = new Imagem(pixelsNova);
        imagemFinal.setValorMaximoCor(this.getValorMaximoCor());
        
        return imagemFinal;
    }
    
    public Imagem completarGB() {
        Cor[][] pixelsNova = new Cor[this.getAltura()][this.getLargura()];
        
        //Passa por todas as linhas
        for (int row = 0; row < this.getAltura(); row++) {
            //Passa por todas as colunas(pixels) da linha
            for (int col = 0; col < this.getLargura(); col++) {
                Cor pixel = this.getPIXELS()[row][col];
                pixelsNova[row][col] = new Cor(pixel.getRED(), 255, 255);
            }
        }
        
        Imagem imagemFinal = new Imagem(pixelsNova);
        imagemFinal.setValorMaximoCor(this.getValorMaximoCor());
        
        return imagemFinal;
    }
    
    public Imagem completarRB() {
        Cor[][] pixelsNova = new Cor[this.getAltura()][this.getLargura()];
        
        //Passa por todas as linhas
        for (int row = 0; row < this.getAltura(); row++) {
            //Passa por todas as colunas(pixels) da linha
            for (int col = 0; col < this.getLargura(); col++) {
                Cor pixel = this.getPIXELS()[row][col];
                pixelsNova[row][col] = new Cor(255, pixel.getGREEN(), 255);
            }
        }
        
        Imagem imagemFinal = new Imagem(pixelsNova);
        imagemFinal.setValorMaximoCor(this.getValorMaximoCor());
        
        return imagemFinal;
    }
    
    public Imagem completarRG() {
        Cor[][] pixelsNova = new Cor[this.getAltura()][this.getLargura()];
        
        //Passa por todas as linhas
        for (int row = 0; row < this.getAltura(); row++) {
            //Passa por todas as colunas(pixels) da linha
            for (int col = 0; col < this.getLargura(); col++) {
                Cor pixel = this.getPIXELS()[row][col];
                pixelsNova[row][col] = new Cor(255, 255, pixel.getBLUE());
            }
        }
        
        Imagem imagemFinal = new Imagem(pixelsNova);
        imagemFinal.setValorMaximoCor(this.getValorMaximoCor());
        
        return imagemFinal;
    }
    
    public Imagem reduzir10x() {
        int altura = this.getAltura() / 10;
        int largura = this.getLargura() / 10;
        Cor[][] pixelsNova = new Cor[altura][largura];
        
        //Passa por todas as linhas
        for (int row = 0; row < altura; row++) {
            //Passa por todas as colunas(pixels) da linha
            for (int col = 0; col < largura; col++) {
                Cor pixel = this.getPIXELS()[row*10][col*10];
                pixelsNova[row][col] = pixel;
            }
        }
        
        Imagem imagemFinal = new Imagem(pixelsNova);
        imagemFinal.setValorMaximoCor(this.getValorMaximoCor());
        
        return imagemFinal;
    }
    
    public Imagem aplicarContraste() {
        int altura = this.getAltura();
        int largura = this.getLargura();
        Cor[][] pixelsNova = new Cor[altura][largura];
 
        int xMin = this.getMenorPixel();
        int xMax = this.getMaiorPixel();
        int a = this.valorMaximoCor/(xMax - xMin);
        int b = (a * -1)*xMin;
        
        //Passa por todas as linhas
        for (int row = 0; row < altura; row++) {
            //Passa por todas as colunas(pixels) da linha
            for (int col = 0; col < largura; col++) {
                int avg = this.getPIXELS()[row][col].getAVG();
                avg = a*avg+b;
                Cor pixel = new Cor(avg, avg, avg);
                
                pixelsNova[row][col] = pixel;
            }
        }
        
        Imagem imagemFinal = new Imagem(pixelsNova);
        imagemFinal.setValorMaximoCor(this.getValorMaximoCor());
        
        return imagemFinal;
    }
    
    public Imagem sobrepor(Imagem imagemCima) {
        return this.sobrepor(imagemCima, 0, 0);
    }
    
    public Imagem sobrepor(final Imagem imagemCima, final int alturaInicial, final int larguraInicial) {
        Cor[][] pixelsNova = this.getPIXELS();
        
        //Controla a linha da imagem de baixo
        int rowImgBaixo = alturaInicial;
        
        //Passa por todas as linhas da imagem de Cima
        for (int row = 0; row < imagemCima.getAltura(); row++) {
            //Controla a coluna da imagem de baixo
            int colImgBaixo = larguraInicial;
            
            if (rowImgBaixo >= this.getAltura()) {
                break;
            }
            
            //Passa por todas as colunas(pixels) da linha
            for (int col = 0; col < imagemCima.getLargura(); col++) {
                if (colImgBaixo >= this.getLargura()) {
                    break;
                }
                
                pixelsNova[rowImgBaixo][colImgBaixo] = imagemCima.getPIXELS()[row][col];
                colImgBaixo++;
            }
            
            rowImgBaixo++;
        }
        
        Imagem imagemFinal = new Imagem(pixelsNova);
        imagemFinal.setValorMaximoCor(this.getValorMaximoCor());
        
        return imagemFinal;
    }
    
    public Imagem sobreporComMargemRGB(final Imagem imagemCima, int alturaInicial, int larguraInicial, int valorMargem) {
        Cor[][] pixelsNova = this.getPIXELS();
        //Controla a linha da imagem de baixo
        int rowImgBaixo = alturaInicial;
        
        int corRedBase = 0;
        int corGreenBase = 0;
        int corBlueBase = 0;
        
        int margem = (imagemCima.getValorMaximoCor() / 100) * valorMargem;
        
        //Passa por todas as linhas
        for (int row = 0; row < imagemCima.getAltura(); row++) {
            //Controla a coluna da imagem de baixo
            int colImgBaixo = larguraInicial;
            
            if (rowImgBaixo >= this.getAltura()) {
                break;
            }
            
            //Passa por todas as colunas(pixels) da linha
            for (int col = 0; col < imagemCima.getLargura(); col++) {
                if (colImgBaixo >= this.getLargura()) {
                    break;
                }
                
                Cor pixel = imagemCima.getPIXELS()[row][col];
                
                if (estaNaMargem(pixel.getRED(), corRedBase, margem) 
                        && estaNaMargem(pixel.getGREEN(), corGreenBase, margem)
                        && estaNaMargem(pixel.getBLUE(), corBlueBase, margem)) {
                    
                    pixelsNova[rowImgBaixo][colImgBaixo] = pixel;
                }
                
                colImgBaixo++;
            }
            
            rowImgBaixo++;
        }
        
        Imagem imagemFinal = new Imagem(pixelsNova);
        imagemFinal.setValorMaximoCor(this.getValorMaximoCor());
        
        return imagemFinal;
    }
}

