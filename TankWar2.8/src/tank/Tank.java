package tank;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.Random;

public class Tank {
    public static final int XSPEED = 5;
    public static final int YSPEED = 5;

    public static final int WIDTH = 30;
    public static final int HEIGHT = 30;

    private BloodBar bb = new BloodBar();

    public boolean isLive() {
        return live;
    }

    public void setLive(boolean live) {
        this.live = live;
    }

    //Tank存活的标志
    private boolean live = true;

    public int getLife() {
        return life;
    }

    public void setLife(int life) {
        this.life = life;
    }

    private int life = 100;

    //持有对象引用
    TankClient tc = null;

    public boolean isGood() {
        return good;
    }

    //判断Tank的正反方
    private boolean good;

    private int x, y;
    private int oldX, oldY;

    //随机数产生器
    private static Random r = new Random();

    private boolean bL = false;
    private boolean bU = false;
    private boolean bR = false;
    private boolean bD = false;



    //初始的坦克方向是静止的
    private Direction dir = Direction.STOP;

    //初始炮筒方向向下
    private Direction ptDir = Direction.D;

    private int step = r.nextInt(12) + 3;

    public Tank(int x, int y, boolean good) {
        this.x = x;
        this.y = y;
        this.oldX = x;
        this.oldY = y;
        this.good = good;
    }

    public Tank(int x, int y, boolean good, Direction dir, TankClient tc){
        this(x, y, good);
        this.dir = dir;
        this.tc = tc;
    }

    //重构绘画函数
    public void draw(Graphics g){
        if(!live) {
            if(!good)
                tc.tanks.remove(this);
            return;
        }

        Color c = g.getColor();
        if(good) g.setColor(Color.RED);
        else g.setColor(Color.BLUE);
        g.fillOval(x, y,WIDTH,HEIGHT);
        g.setColor(c);

        if(good)
            bb.draw(g);

        switch (ptDir){
            case L:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT/2);
                break;
            case LU:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y);
                break;
            case U:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y);
                break;
            case RU:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y);
                break;
            case R:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT/2);
                break;
            case RD:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH, y + Tank.HEIGHT);
                break;
            case D:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x + Tank.WIDTH/2, y + Tank.HEIGHT);
                break;
            case LD:
                g.drawLine(x + Tank.WIDTH/2, y + Tank.HEIGHT/2, x, y + Tank.HEIGHT);
                break;
        }

        move();
    }

    void move(){

        //记录上一步的位置
        this.oldX = x;
        this.oldY = y;

        switch (dir){
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

        if(this.dir != Direction.STOP){
            this.ptDir = dir;
        }

        //禁止坦克出界
        if(x < 0) x = 0;
        if(y < 30) y = 30;
        if(x + Tank.WIDTH  > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
        if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.HEIGHT;

        if(!good){
            Direction[] dirs = Direction.values();
            if(step == 0){
                step = r.nextInt(12) + 3;
                int rn = r.nextInt(dirs.length);
                dir = dirs[rn];
            }

            step--;

            if(r.nextInt(40) > 38)
                this.fire();
        }


    }

    //回退回上一步的位置
    private void stay(){
        x = oldX;
        y = oldY;
    }

    public void keyPressed(KeyEvent e){
        int key = e.getKeyCode();

        switch (key){
            case KeyEvent.VK_1:
                if(!this.live){
                    this.live = true;
                    this.life = 100;
                }
                break;
            case KeyEvent.VK_LEFT :
                bL = true;
                break;
            case KeyEvent.VK_RIGHT :
                bR = true;
                break;
            case KeyEvent.VK_UP :
                bU = true;
                break;
            case KeyEvent.VK_DOWN :
                bD = true;
                break;
        }
        locateDirection();
    }

    void locateDirection(){
        if(bL && !bU && !bR && !bD) dir = Direction.L;
        else if(bL && bU && !bR && !bD) dir = Direction.LU;
        else if(!bL && bU && !bR && !bD) dir = Direction.U;
        else if(!bL && bU && bR && !bD) dir = Direction.RU;
        else if(!bL && !bU && bR && !bD) dir = Direction.R;
        else if(!bL && !bU && bR && bD) dir = Direction.RD;
        else if(!bL && !bU && !bR && bD) dir = Direction.D;
        else if(bL && !bU && !bR && bD) dir = Direction.LD;
        else if(!bL && !bU && !bR && !bD) dir = Direction.STOP;
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        switch (key){
            case KeyEvent.VK_CONTROL:
                //按下Ctrl键添加一发炮弹
                fire();
                break;
            case KeyEvent.VK_LEFT :
                bL = false;
                break;
            case KeyEvent.VK_RIGHT :
                bR = false;
                break;
            case KeyEvent.VK_UP :
                bU = false;
                break;
            case KeyEvent.VK_DOWN :
                bD = false;
                break;
            case KeyEvent.VK_A:
                superfire();
                break;
        }
        locateDirection();
    }

    public Missile fire(){
        if(!live)   return null;
        int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
        int y = this.y + Tank.HEIGHT/2 - Missile.WIDTH/2;
        Missile m = new Missile(x, y,good, ptDir, this.tc);
        tc.missiles.add(m);
        return m;
    }

    public Missile fire(Direction dir){
        if(!live)   return null;
        int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
        int y = this.y + Tank.HEIGHT/2 - Missile.WIDTH/2;
        Missile m = new Missile(x, y,good, dir, this.tc);
        tc.missiles.add(m);
        return m;
    }

    public void superfire(){
        Direction[] dirs = Direction.values();
        for(int i = 0; i < 8; i++){
            fire(dirs[i]);
        }
    }

    public Rectangle getRect(){
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    /**
     * 撞墙
     * @param w 被撞的墙
     * @return 撞上了返回true，否则false
     */
    public boolean collidesWithWall(Wall w){
        if(this.live && this.getRect().intersects(w.getRect())){
            this.stay();
            return true;
        }
        return false;
    }

    public boolean collidesWithTanks(List<Tank> tanks){
        for(int i = 0; i < tanks.size(); i++){
            Tank t = tanks.get(i);
            if(this != t){
                if(this.live && t.isLive() && this.getRect().intersects(t.getRect())){
                    this.stay();
                    t.stay();
                    return true;
                }
            }
        }
        return false;
    }

    private class BloodBar{
        public void draw(Graphics g){
            Color c = g.getColor();
            g.setColor(Color.RED);
            g.drawRect(x, y - 10, WIDTH, 10);

            int w = WIDTH * life/100;
            g.fillRect(x, y-10, w, 10);
            g.setColor(c);
        }
    }

    public boolean eat(Blood b){
        if(this.live && b.isLive() && this.getRect().intersects(b.getRect())){
            this.life = 100;
            b.setLive(false);
            return true;
        }
        return false;
    }
}
