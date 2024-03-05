import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
public class DrawPanel  extends JPanel implements CrossHairListener{
    BufferedImage background;

    //Zombie zombie;// = new Zombie(background.getWidth(), 100, 1);

    static int escaped = 0;

    static int shot = 0;

    static boolean ongoing = true;

    List<Sprite> sprites = new ArrayList<>();
    AnimationThread am;

    SpriteFactory factory;

    CrossHair crossHair = new CrossHair(this);

    Comparator<Sprite> comp = new Comparator<Sprite>() {
        @Override
        public int compare(Sprite o1, Sprite o2) {
            if(o1.isCloser(o2))
                return 1;
            return 0;
        }};

    DrawPanel(URL backgroundImagageURL, SpriteFactory factory) {
        try {
            background = ImageIO.read(backgroundImagageURL);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        this.factory = factory;
        this.addMouseListener(crossHair);
        this.addMouseMotionListener(crossHair);
        am = new AnimationThread();
    }

    @Override
    public void onShotsFired(int x, int y) {
        if(ongoing)// nie bierze strzalow bo koncu gry
        for(int i=sprites.size()-1;i>-1;i--)
            if(sprites.get(i).isHit(x,y)){
                shot++;
                sprites.remove(sprites.get(i));
                break;
            }
    }

    class AnimationThread extends Thread {
        public void run() {
            for (int i = 0; !this.isInterrupted() ; i++) {//jesli watek interrupt to koniec

                //System.out.println(this.isInterrupted());
                //if(this.isInterrupted()) break;//return;

                //System.out.println("animation");

                //ruszamy
                for (Iterator<Sprite> s=sprites.listIterator(); s.hasNext();) {
                    //przesuwamy sie
                    Sprite spr = s.next();
                    if (!spr.isVisible()){
                        escaped++;
                        //exception
                        s.remove();
                    }
                    else {
                        //spr.next();
                        spr.next(shot);
                    }
                }
                repaint();
                if (i % 30 == 0) {
                    sprites.add(factory.newSprite(getWidth(), (int) ((Math.random()*0.15 +0.7) * getHeight())));
                    sprites.sort(comp);
                }
                //System.out.println("thread");

                try {
                    sleep(1000 / 30);  // 30 klatek na sekundę
                } catch (InterruptedException e) {
                    //próba przerwania wątku zostanie wyłapana przez try/catch, trzeba propagować interrupt
                    //Thread.currentThread().interrupt();
                    this.interrupt(); // dziala lepiej
                    e.printStackTrace();
                }
                //System.out.println(this.isInterrupted());
            }
        }
    }

    public void gameOver(Graphics g){
        am.interrupt();

        if(ongoing)
            try{Statistics.update(shot);}
            catch (IOException e) {e.printStackTrace();}
        ongoing = false;

        Graphics2D g2d=(Graphics2D)g;
        AffineTransform saveAVT = g2d.getTransform();
        g2d.setColor(Color.red);

        g2d.translate(getWidth()/2,getHeight()/4);
        Font font = new Font("f1", Font.BOLD|Font.ITALIC, 18);
        g2d.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        String msg = "GAME OVER!";
        g.drawString(msg,-metrics.stringWidth(msg)/2,-metrics.getHeight()/2);

        font = new Font("f2", Font.PLAIN, 10);
        g2d.setFont(font);
        metrics = g.getFontMetrics(font);
        msg = "THANK YOU FOR PLAYING!";
        g.drawString(msg,-metrics.stringWidth(msg)/2,20);
        msg = "ZOMBIES KILLED : " + shot;
        g.drawString(msg,-metrics.stringWidth(msg)/2,35);

        g.translate(0,100);
        Statistics.display(g);

        g2d.transform(saveAVT);
    }

    public void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
        g.drawString("shot: "+shot,50,50);
        g.drawString("escaped: "+escaped,50,70);
        crossHair.Draw(g);

        synchronized (sprites) {
            for (Sprite s : sprites)
                s.draw(g2d, this);
        }
        if(escaped >= 1) {
            gameOver(g);
        }

    }
}
