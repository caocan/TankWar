package tank;

import java.awt.*;
import java.util.List;

public class Missile {

    public static final int XSPEED = 10;
    public static final int YSPEED = 10;

    public static final int WIDTH = 10;
    public static final int HEIGHT = 10;

    int x, y;
    Direction dir;

    private TankClient tc;

    private boolean good;

    //live的getter()
    public boolean isLive() {
        return live;
    }

    private boolean live = true;

    public Missile(int x, int y, Direction dir) {
        this.x = x;
        this.y = y;
        this.dir = dir;
    }

    public Missile(int x, int y, boolean good, Direction dir, TankClient tc){
        this(x, y, dir);
        this.good = good;
        this.tc = tc;
    }

    public void draw(Graphics g) {

        if(!live){
            //让爆炸消失
            tc.missiles.remove(this);
            return;
        }


        Color c = g.getColor();
        if(good) g.setColor(Color.RED);
        else g.setColor(Color.BLACK);
        g.fillOval(x, y, WIDTH, HEIGHT);
        g.setColor(c);

        move();
    }

    private void move(){

        switch (dir) {
            case L:
                x -= XSPEED;
                break;
            case LU:
                x -= XSPEED;
                y -= YSPEED;
                break;
            case U:
                y -= YSPEED;
                break;
            case RU:
                x += XSPEED;
                y -= YSPEED;
                break;
            case R:
                x += XSPEED;
                break;
            case RD:
                x += XSPEED;
                y += YSPEED;
                break;
            case D:
                y += YSPEED;
                break;
            case LD:
                x -= XSPEED;
                y += YSPEED;
                break;
            case STOP:
                break;
        }

        if(x < 0 || y < 0 || x > TankClient.GAME_WIDTH || y > TankClient.GAME_HEIGHT){
            live = false;

        }


    }

    public Rectangle getRect(){
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    //可以通过观察者模式来重构这个问题
    public boolean hitTank(Tank t){
        if(this.live && this.getRect().intersects(t.getRect()) && t.isLive() && this.good != t.isGood()){
            if(t.isGood()){
                t.setLife(t.getLife() - 20);
                if(t.getLife() <= 0) t.setLive(false);
            }else{
                t.setLive(false);
            }

            this.live = false;
            Explode e = new Explode(x, y, tc);
            tc.explodes.add(e);
            return true;
        }

        return false;
    }

    public boolean hitTanks(List<Tank> tanks){
        for(int i = 0; i < tanks.size(); i++){
            if(hitTank(tanks.get(i)))
                return true;
        }
        return false;
    }

    //子弹撞墙的函数
    public boolean hitWall(Wall w){
        if(this.live && this.getRect().intersects(w.getRect())){
            this.live = false;
            return true;
        }
        return false;
    }
}
