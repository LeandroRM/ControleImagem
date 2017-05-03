package ControleImagem;

import java.util.ArrayList;

/**
 *  Classe que faz tratamento de imagem comprimida
 * 
 * @author leandro
 */
public class ImagemComprimida extends Imagem {
    private final ArrayList<Integer> RED_PIXELS_COMPRIMIDOS; 
    private final ArrayList<Integer> GREEN_PIXELS_COMPRIMIDOS; 
    private final ArrayList<Integer> BLUE_PIXELS_COMPRIMIDOS; 
    
    /**
     * Comprime a imagem para RLE
     * 
     * @param imagem - Imagem a ser comprimida
     */
    public ImagemComprimida(Imagem imagem) {
        super(imagem.getPIXELS());
        this.RED_PIXELS_COMPRIMIDOS = new ArrayList<>();
        this.GREEN_PIXELS_COMPRIMIDOS = new ArrayList<>();
        this.BLUE_PIXELS_COMPRIMIDOS = new ArrayList<>();
        
        int countRed = 0;
        boolean isEqualRed = false;
        int lastRed = -1;
        int indexRed = -1;
        
        int countGreen = 0;
        boolean isEqualGreen = false;
        int lastGreen = -1;
        int indexGreen = -1;
        
        int countBlue = 0;
        boolean isEqualBlue = false;
        int lastBlue = -1;
        int indexBlue = -1;
        
        for (Cor[] linha : imagem.getPIXELS()) {
            for (Cor pixel : linha) {
                //RED
                if (lastRed == -1) {
                    //Primeiro registro
                    indexRed = this.RED_PIXELS_COMPRIMIDOS.size();
                    this.RED_PIXELS_COMPRIMIDOS.add(pixel.getRED());
                    countRed++;
                } else {
                    if (pixel.getRED() == lastRed) {
                        if (this.RED_PIXELS_COMPRIMIDOS.size() == 1) {
                            isEqualRed = true;
                        }
                            
                        //Se já está true é pq já foi gravado o valor que repete
                        if (isEqualRed) {
                            countRed++;
                        } else {
                            //Finaliza um ciclo de valores diferentes
                            //Faz o -- porque o valor que agora se repetirá, já havia sido contado como diferente
                            this.RED_PIXELS_COMPRIMIDOS.add(indexRed,--countRed * -1);
                            //Inicia um ciclo de valores iguais
                            indexRed = this.RED_PIXELS_COMPRIMIDOS.size() - 1; //-1 pois o valor já está listado
                            //Não faz registro do valor, pois o mesmo já está listado
                            isEqualRed = true;
                            countRed = 2;
                        }
                    } else {
                        if (isEqualRed) {
                            //Finaliza um ciclo de valores iguais
                            this.RED_PIXELS_COMPRIMIDOS.add(indexRed, countRed);
                            //Inicia um ciclo de valores diferentes
                            indexRed = this.RED_PIXELS_COMPRIMIDOS.size(); //-1 pois o valor já está listado
                            isEqualRed = false;
                            countRed = 1;
                        } else {
                            countRed++;
                        }

                        this.RED_PIXELS_COMPRIMIDOS.add(pixel.getRED());
                    }
                }
                
                //GREEN
                if (lastGreen == -1) {
                    //Primeiro registro
                    indexGreen = this.GREEN_PIXELS_COMPRIMIDOS.size();
                    this.GREEN_PIXELS_COMPRIMIDOS.add(pixel.getGREEN());
                    countGreen++;
                } else {
                    if (pixel.getGREEN() == lastGreen) {
                        if (this.GREEN_PIXELS_COMPRIMIDOS.size() == 1) {
                            isEqualGreen = true;
                        }
                            
                        //Se já está true é pq já foi gravado o valor que repete
                        if (isEqualGreen) {
                            countGreen++;
                        } else {
                            //Finaliza um ciclo de valores diferentes
                            //Faz o -- porque o valor que agora se repetirá, já havia sido contado como diferente
                            this.GREEN_PIXELS_COMPRIMIDOS.add(indexGreen,--countGreen * -1);
                            //Inicia um ciclo de valores iguais
                            indexGreen = this.GREEN_PIXELS_COMPRIMIDOS.size() - 1; //-1 pois o valor já está listado
                            //Não faz registro do valor, pois o mesmo já está listado
                            isEqualGreen = true;
                            countGreen = 2;
                        }
                    } else {
                        if (isEqualGreen) {
                            //Finaliza um ciclo de valores iguais
                            this.GREEN_PIXELS_COMPRIMIDOS.add(indexGreen, countGreen);
                            //Inicia um ciclo de valores diferentes
                            indexGreen = this.GREEN_PIXELS_COMPRIMIDOS.size(); //-1 pois o valor já está listado
                            isEqualGreen = false;
                            countGreen = 1;
                        } else {
                            countGreen++;
                        }

                        this.GREEN_PIXELS_COMPRIMIDOS.add(pixel.getGREEN());
                    }
                }
                
                //BLUE
                if (lastBlue == -1) {
                    //Primeiro registro
                    indexBlue = this.BLUE_PIXELS_COMPRIMIDOS.size();
                    this.BLUE_PIXELS_COMPRIMIDOS.add(pixel.getBLUE());
                    countBlue++;
                } else {
                    if (pixel.getBLUE() == lastBlue) {
                        if (this.BLUE_PIXELS_COMPRIMIDOS.size() == 1) {
                            isEqualBlue = true;
                        }
                            
                        //Se já está true é pq já foi gravado o valor que repete
                        if (isEqualBlue) {
                            countBlue++;
                        } else {
                            //Finaliza um ciclo de valores diferentes
                            //Faz o -- porque o valor que agora se repetirá, já havia sido contado como diferente
                            this.BLUE_PIXELS_COMPRIMIDOS.add(indexBlue,--countBlue * -1);
                            //Inicia um ciclo de valores iguais
                            indexBlue = this.BLUE_PIXELS_COMPRIMIDOS.size() - 1; //-1 pois o valor já está listado
                            //Não faz registro do valor, pois o mesmo já está listado
                            isEqualBlue = true;
                            countBlue = 2;
                        }
                    } else {
                        if (isEqualBlue) {
                            //Finaliza um ciclo de valores iguais
                            this.BLUE_PIXELS_COMPRIMIDOS.add(indexBlue, countBlue);
                            //Inicia um ciclo de valores diferentes
                            indexBlue = this.BLUE_PIXELS_COMPRIMIDOS.size(); //-1 pois o valor já está listado
                            isEqualBlue = false;
                            countBlue = 1;
                        } else {
                            countBlue++;
                        }

                        this.BLUE_PIXELS_COMPRIMIDOS.add(pixel.getBLUE());
                    }
                }
                
                lastRed = pixel.getRED();
                lastGreen = pixel.getGREEN();
                lastBlue = pixel.getBLUE();
            }
        }
        
        if (!isEqualRed) {
            countRed = countRed * -1;
        }
        if (!isEqualGreen) {
            countGreen = countGreen * -1;
        }
        if (!isEqualBlue) {
            countBlue = countBlue * -1;
        }
        
        this.RED_PIXELS_COMPRIMIDOS.add(indexRed, countRed);
        this.GREEN_PIXELS_COMPRIMIDOS.add(indexGreen, countGreen);
        this.BLUE_PIXELS_COMPRIMIDOS.add(indexBlue, countBlue);
    }

    @Override
    public String toString() {
        String retorno = "";
        
        for (int pixelRed : this.RED_PIXELS_COMPRIMIDOS) {
            retorno += pixelRed + " ";
        }
        
        return retorno;
    }

    @Override
    public boolean gravar(String arquivo, Formato formato) {
        return super.gravar(arquivo, Formato.REL); //To change body of generated methods, choose Tools | Templates.
    }
}
