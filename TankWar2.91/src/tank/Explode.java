package tank;

import java.awt.*;

public class Explode {

    int x, y;
    private boolean live = true;

    //火焰的直径序列
//    int [] diameter = {4, 7, 12, 18, 26, 32, 49, 30, 14, 6};

    //火焰画到第几步了
    int step = 0;

    private static boolean init = false;

    private TankClient tc = null;

    //拿到默认的工具包，可以把硬盘上的图片拿到屏幕
    private static Toolkit tk = Toolkit.getDefaultToolkit();

    //将图片设置为静态,从ClassPath下取资源
    private static Image[] imgs = {
            tk.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),
            tk.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
            tk.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
            tk.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
            tk.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
            tk.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
            tk.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
            tk.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
            tk.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
            tk.getImage(Explode.class.getClassLoader().getResource("images/9.gif")),
            tk.getImage(Explode.class.getClassLoader().getResource("images/10.gif"))
    };

    public Explode(int x, int y, TankClient tc){
        this.x = x;
        this.y = y;
        this.tc = tc;
    }


    public void draw(Graphics g){

        //对第一次打子弹初始化
        if(init == false){

            for(int i = 0; i < imgs.length; i++)
                g.drawImage(imgs[i], -100, -100, null);
            init = true;
        }

        if(!live) {
            tc.explodes.remove(this);
            return;
        }

        if(step == imgs.length){
            live = false;
            step = 0;
            return;
        }

        g.drawImage(imgs[step], x, y, null);

        step ++;
    }

}
