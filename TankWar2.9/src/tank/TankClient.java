package tank;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * 这个类的作用是坦克游戏的主窗口
 * @author korson
 *
 */


public class TankClient extends Frame{

    /**
     * 整个坦克游戏的宽度
     */
    public static final int GAME_WIDTH = 800;
    /**
     * 整个坦克的高度
     */
    public static final int GAME_HEIGHT = 600;

    Tank myTank = new Tank(50, 50, true,Direction.STOP,this);

    Wall w1 = new Wall(100, 200 , 20, 150, this),
            w2 = new Wall(300, 100, 300, 20, this);

//    Tank enemyTank = new Tank(100, 100, false, this);

    Explode e = new Explode(70, 70, this);
    //增加一个存储子弹的容器
    List<Missile> missiles = new ArrayList<Missile>();

    List<Explode> explodes = new ArrayList<Explode>();

    List<Tank> tanks = new ArrayList<Tank>();

    //虚拟的图片
    Image offScreenImage = null;

    Blood b = new Blood();

    //画出红色的坦克，窗口重画的时候自动调用
    public void paint(Graphics g){
        g.drawString("子弹的个数" + missiles.size(), 10, 50);
        g.drawString("爆炸的个数" + explodes.size(), 10, 70);
        g.drawString("坦克的个数" + tanks.size(), 10, 90);
        g.drawString("我的生命值" + myTank.getLife(), 10, 110);

        if(tanks.size() <= 0){
            for(int i = 0; i < 10; i++){
                tanks.add(new Tank(50 + 40 *(i + 1), 50, false, Direction.D, this));
            }
        }

        for(int i = 0 ; i < missiles.size(); i++)
        {
            Missile m = missiles.get(i);
//            if(!m.isLive()) missiles.remove(m);
//            else m.draw(g);
//            m.hitTank(enemyTank);
            m.hitTanks(tanks);
            m.hitTank(myTank);
            m.hitWall(w1);
            m.hitWall(w2);
            m.draw(g);
        }

        for(int i = 0; i < explodes.size(); i++){
            Explode e = explodes.get(i);
            e.draw(g);
        }

        for(int i = 0; i < tanks.size(); i++){
            Tank t = tanks.get(i);
            t.collidesWithWall(w1);
            t.collidesWithWall(w2);
            t.collidesWithTanks(tanks);
            t.draw(g);
        }

        myTank.eat(b);
        myTank.draw(g);
        w1.draw(g);
        w2.draw(g);
        b.draw(g);
//        enemyTank.draw(g);
    }

    //解决闪烁的问题
    public void update(Graphics g){
        //创建虚拟图片
        if(offScreenImage == null){
            offScreenImage = this.createImage(GAME_WIDTH, GAME_HEIGHT);
        }

        //拿到画笔
        Graphics gOffScreen = offScreenImage.getGraphics();
        Color c = gOffScreen.getColor();
        gOffScreen.setColor(Color.BLACK);
        gOffScreen.fillRect(0, 0, GAME_WIDTH, GAME_HEIGHT);
        gOffScreen.setColor(c);
        paint(gOffScreen);
        g.drawImage(offScreenImage, 0, 0, null);
    }

    /**
     * 本方法显示坦克主窗口
     */
    public void lauchFrame(){

//
        Properties props = new Properties();
        try {
            props.load(this.getClass().getClassLoader().getResourceAsStream("config/tank.properties"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        int initTankCount = Integer.parseInt(props.getProperty("initTankCount"));

        for(int i = 0; i < initTankCount; i++){
            tanks.add(new Tank(50 + 40 *(i + 1), 50, false, Direction.D, this));
        }

        this.setLocation(400, 300);
        this.setSize(GAME_WIDTH, GAME_HEIGHT);

        //点X可以使窗口关闭
        this.addWindowListener(new WindowAdapter() {    //添加窗口关闭事件
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        //窗口大小不变化
        this.setResizable(false);
        this.setBackground(Color.GREEN);

        //创建用于监听键盘事件的监听器
        this.addKeyListener(new KeyMonitor());
        setVisible(true);

        new Thread(new PaintThread()).start();
    }


    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.lauchFrame();
    }

    //写一个内部类，用来重绘我们的窗口，使坦克动起来
    private class PaintThread implements Runnable{
        @Override
        public void run() {
            while(true){
                repaint();
                try {
                    //每个50ms，窗口就重绘一次，坦克就移动一次
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //用于监听键盘事件的类
    private class KeyMonitor extends KeyAdapter{

        //使坦克可以在键盘控制下向四个方向移动
        public void keyPressed(KeyEvent e){
            myTank.keyPressed(e);
        }

        //处理键盘抬起来时的情况
        @Override
        public void keyReleased(KeyEvent e) {
            myTank.keyReleased(e);
        }
    }
}
