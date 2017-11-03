import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame{

    int x = 50 , y = 50;
    //画出红色的坦克，窗口重画的时候自动调用
    public void paint(Graphics g){
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(x, y,30,30);
        g.setColor(c);

        y += 5;
    }

    public void lauchFrame(){
        this.setLocation(400, 300);
        this.setSize(800, 600);

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
}
