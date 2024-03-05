import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

public class Statistics {


    static ArrayList<Integer> highscore = new ArrayList<>();

    static int score;

    //tutaj właściwie powinna być osobna klasa do wczytywania i zapisywania
    static public boolean update(int nscore) throws IOException {
        score = nscore;
        BufferedReader buf = new BufferedReader(new FileReader("stats.csv"));
        String line = buf.readLine();
        String[] rec = line.split(";");
        //highscore = new ArrayList<>();
        for(String r : rec)
            highscore.add(Integer.valueOf(r));
        for(Integer i : highscore)
            if(nscore > i) {
                highscore.add(nscore);
                highscore.sort((Integer i1, Integer i2)-> Integer.compare(i2,i1));
                StringBuilder builder = new StringBuilder();
                for(int j=0;j<4;j++)
                    builder.append(highscore.get(j) + ";");
                builder.append(highscore.get(4));
                BufferedWriter writer = new BufferedWriter(new FileWriter("stats.csv",false));//nadpisuje
                System.out.println(builder.toString());
                writer.write(builder.toString());
                writer.close();
                //highscore.remove(-1);
                return true;
            }
        return false;
    }
    static public void display(Graphics g){
        Graphics2D g2d=(Graphics2D)g;
        AffineTransform saveAVT = g2d.getTransform();
        g2d.setColor(Color.red);

        //g2d.translate();
        g2d.setColor(Color.YELLOW);
        Font font = new Font("f1", Font.BOLD|Font.ITALIC, 14);
        g2d.setFont(font);
        FontMetrics metrics = g.getFontMetrics(font);
        String msg = "STATISTICS:";
        g.drawString(msg,-metrics.stringWidth(msg)/2,-15-metrics.getHeight()/2);

        font = new Font("f2", Font.PLAIN, 12);
        g2d.setFont(font);
        for(int i=0; i<5; i++){
            msg = highscore.get(i).toString();
            g.drawString(msg,-metrics.stringWidth(msg)/2, i*15 -metrics.getHeight()/2);
        }

        if(highscore.get(4) <= score) {
            msg = "NEW HIGHSCORE !!!";
            font = new Font("f2", Font.PLAIN|Font.BOLD, 15);
            g2d.setFont(font);
            g.drawString(msg, -metrics.stringWidth(msg) / 2, 100 - metrics.getHeight() / 2);
        }
        g2d.setFont(font);


        g2d.transform(saveAVT);
    }
}
