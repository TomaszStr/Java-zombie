import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Zombie implements Sprite {
    BufferedImage tape;
    int x = 500;
    int y = 500;
    double scale = 1;

    int index = 0;  // numer wyświetlanego obrazka
    int HEIGHT;// =  tape.getHeight(); // z rysunku;
    int WIDTH;// = tape.getWidth();  // z rysunku;

    @Deprecated
    Zombie(int x, int y, double scale)
    {
        this.x = x;
        this.y = y;
        this.scale = scale;
        try {
            this.tape = ImageIO.read(getClass().getResource("walkingdead.png"));
        } catch (IOException e) {throw new RuntimeException(e);}
        this.HEIGHT =  tape.getHeight();
        this.WIDTH =  tape.getWidth()/10;
    }

    Zombie(int x,int y, double scale, BufferedImage tape){
        this.x = x;
        this.y = y;
        this.scale = scale;
        this.tape = tape;
        this.HEIGHT =  tape.getHeight();
        this.WIDTH =  tape.getWidth()/10;
    }

    /**
     * Pobierz odpowiedni podobraz klatki (odpowiadającej wartości zmiennej index)
     * i wyświetl go w miejscu o współrzędnych (x,y)
     * @param g
     * @param parent
     *
     */
    public void draw(Graphics g, JPanel parent) {
        //System.out.println("zombie");
        Image img = tape.getSubimage(index*WIDTH,0,WIDTH,HEIGHT); // pobierz klatkę
        g.drawImage(img, x, y - (int) (HEIGHT * scale) / 2, (int) (WIDTH * scale), (int) (HEIGHT * scale), parent);
    }

     /**
     * Zmień stan - przejdź do kolejnej klatki
     */

    public void next() {
        x -= 20 * scale;
        index = (index + 1) % 10;
    }

    //specjalna wersja, wyzszy poziom trudnosci
    public void next(int cnt) {
        x -= 20 * scale * (1+0.15*cnt);
        index = (index + 1) % 10;
    }

    public boolean isVisible(){
        //System.out.println("isvis");
        if(x > -WIDTH*scale)
            return true;
        return false;
    }

    public boolean isHit(int _x, int _y){
        if(_x>= x &&
                _x <= x+WIDTH &&
                _y <= y+(HEIGHT * scale) / 2 &&
                _y >= y-(HEIGHT * scale) / 2)
            return true;
        return false;

    }

    @Override
    public boolean isCloser(Sprite other) {
        if(scale > ((Zombie)other).scale)
            return true;
        return false;
    }
}