import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public enum ZombieFactory implements SpriteFactory {

    //private static ZombieFactory INSTANCE;

    INSTANCE;
    BufferedImage tape;

    private ZombieFactory(){
        try {
            tape = ImageIO.read(getClass().getResource("walkingdead.png"));
        } catch (IOException e) {throw new RuntimeException(e);}
    }

    public ZombieFactory getINSTANCE(){
        return INSTANCE;
    }

    @Override
    public Sprite newSprite(int x, int y) {
        double scale = (Math.random()*0.5)+0.2;
        return new Zombie(x,y,scale,tape);
    }
}
