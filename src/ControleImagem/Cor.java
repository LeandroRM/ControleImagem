package ControleImagem;

/**
 *
 * @author leandro
 */
public class Cor {
    private final int RED;
    private final int GREEN;
    private final int BLUE;

    public Cor(int RED, int GREEN, int BLUE) {
        if (RED >= 0 && RED <= 255) {
            this.RED = RED;
        } else {
            if (RED > 255) {
                this.RED = 255;
            } else {
                this.RED = 0;
            }
        }
        
        if (GREEN >= 0 && GREEN <= 255) {
            this.GREEN = GREEN;
        } else {
            if (GREEN > 255) {
                this.GREEN = 255;
            } else {
                this.GREEN = 0;
            }
        }
        
        if (BLUE >= 0 && BLUE <= 255) {
            this.BLUE = BLUE;
        } else {
            if (BLUE > 255) {
                this.BLUE = 255;
            } else {
                this.BLUE = 0;
            }
        }
    }

    public int getAVG() {
        return (this.RED + this.GREEN + this.BLUE) / 3;
    }

    public int getRED() {
        return RED;
    }

    public int getGREEN() {
        return GREEN;
    }

    public int getBLUE() {
        return BLUE;
    }
}
