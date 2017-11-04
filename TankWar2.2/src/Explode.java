import java.awt.*;

public class Explode {

    int x, y;
    private boolean live = true;

    //火焰的直径序列
    int [] diameter = {4, 7, 12, 18, 26, 32, 49, 30, 14, 6};

    //火焰画到第几步了
    int step = 0;

    private TankClient tc = null;

    public Explode(int x, int y, TankClient tc){
        this.x = x;
        this.y = y;
        this.tc = tc;
    }


    public void draw(Graphics g){
        if(!live) return;

        if(step == diameter.length){
            live = false;
            step = 0;
            tc.explodes.remove(this);
            return;
        }

        Color c = g.getColor();
        g.setColor(Color.ORANGE);
        g.fillOval(x, y, diameter[step], diameter[step]);
        g.setColor(c);

        step ++;
    }
}
