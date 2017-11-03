import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class TankClient extends Frame{

    //画出红色的坦克，窗口重画的时候自动调用
    public void paint(Graphics g){
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.fillOval(50, 50,30,30);
        g.setColor(c);
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
    }


    public static void main(String[] args) {
        TankClient tc = new TankClient();
        tc.lauchFrame();
    }
}
