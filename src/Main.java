import javax.swing.*;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        // write your code here
        //DOPYTAC
        ExecutorService executor = Executors.newSingleThreadExecutor();

        JFrame frame = new JFrame("Zombie");
        SpriteFactory factory = ZombieFactory.INSTANCE.getINSTANCE();
        DrawPanel panel = new DrawPanel(Main.class.getResource("background.jpg"),factory);
        frame.setContentPane(panel);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(true);
        frame.setVisible(true);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                System.out.println("end");
                // tu zatrzymaj watek
                // ale nie za pomocą metody stop() !!!
                System.out.println("interrupt");
                panel.am.interrupt();

                //zatrzymaj też timer celownika za pomocą cancel(). Timer to także wątek...
                System.out.println("timer");
                panel.crossHair.timer.cancel();

                //przy uzyciu shutdownNow wyrzuca wyjatek dla sleep'a
                //bez uzycia w ogole executora tez wyrzuca wyjatek przy sleep
                //za to sam shutdown, bez interrupta nie zadziala
                //natomiast sam shutdownNow juz tak
                System.out.println("executor");
                executor.shutdown();
            }
        });

        executor.execute(panel.am);
        //panel.am.start(); // tez zadziala bez executora, ale sleep bedzie wyrzucal blad

    }
}