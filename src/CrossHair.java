import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class CrossHair implements MouseMotionListener, MouseListener {

    DrawPanel parent;

    CrossHair(DrawPanel parent) {
        this.parent = parent;
    }

    // jako atrybut w klasie
    Timer timer = new Timer("Timer");


    void Draw(Graphics g){
        if(activated)
            g.setColor(Color.RED);
        else
            g.setColor(Color.WHITE);

        g.drawLine(x+25,y,x-25,y);
        g.drawLine(x,y+25,x,y-25);
        g.drawOval(x-5,y-5,10,10);
    }

    /* x, y to współrzedne celownika
       activated - flaga jest ustawiana po oddaniu strzału (naciśnięciu przyciku myszy)
    */
    int x;
    int y;
    boolean activated = false;

    List<CrossHairListener> listeners = new ArrayList<CrossHairListener>();
    void addCrossHairListener(CrossHairListener e){
        listeners.add(e);
    }
    void notifyListeners(){
        for(var e:listeners)
            e.onShotsFired(x,y);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //NACISNIECIE I PUSZCZENIE, a nie samo wcisniecie
        //x = e.getX();
        //y= e.getY();
        //activated = true;
        //parent.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        x = e.getX();
        y= e.getY();
        activated = true;
        parent.onShotsFired(x,y);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                activated=false;
                parent.repaint();
                //timer.cancel(); // TODO //zamyka timer i powoduje wyjatki
                //timer.purge(); // nieoptymalne, zmniejsza koszt pamieci, ale zwieksza koszt czasu
            }
        },300);
        parent.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //activated = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        x = e.getX();
        y= e.getY();
        parent.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        x = e.getX();
        y= e.getY();
        parent.repaint();
    }
}