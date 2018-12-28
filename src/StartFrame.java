import com.sun.xml.internal.messaging.saaj.soap.JpegDataContentHandler;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StartFrame {

    JFrame mainFrame;
    final String gameRule = "2048游戏共有16个格子，开始时会随机生成两个数值为2的方块和一个数值为4的方块，\n" +
            "玩家可通过键盘上的上、下、左、右方向键来操控方块的滑动方向，\n" +
            "每按一次方向键，所有的方块会向一个方向靠拢，相同数值的方块将会相加并合并成一个方块，\n" +
            "此外，每滑动一次将会随机生成一个数值为2或者4的方块，\n" +
            "玩家需要想办法在这16个格子里凑出2048数值的方块，若16个格子被填满且无法再移动，\n" +
            "则游戏结束。";
    public StartFrame() {
        initFrame();
    }

    private void initFrame() {
        mainFrame = new JFrame("2048 Game");
        mainFrame.setSize(500, 500);
        mainFrame.setResizable(false);//固定窗口尺寸
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);//窗口居中


        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

        jPanel.add(newLine(Box.createVerticalStrut(25)));//添加空白区域

        JLabel jLabel = new JLabel("2048");
        jLabel.setForeground(new java.awt.Color(0x776e65));
        jLabel.setFont(new java.awt.Font("Dialog", 1, 92));
        jPanel.add(newLine(jLabel));

        /*
        JLabel author = new JLabel("by xxx");
        jPanel.add(newLine(author));
        */


        jPanel.add(newLine(Box.createVerticalStrut(50)));


        JButton btn1 = new JButton("开始游戏");
        btn1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Game();
                mainFrame.dispose();
            }
        });
        jPanel.add(newLine(btn1));


        jPanel.add(newLine(Box.createVerticalStrut(50)));


        JButton btn2 = new JButton("游戏规则");
        btn2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                JOptionPane.showMessageDialog(null, gameRule, "游戏规则", JOptionPane.PLAIN_MESSAGE);
            }
        });
        jPanel.add(newLine(btn2));


        jPanel.add(newLine(Box.createVerticalStrut(50)));


        JButton btn3 = new JButton("退出游戏");
        btn3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
        jPanel.add(newLine(btn3));


        mainFrame.add(jPanel);

        mainFrame.setVisible(true);
    }

    //添加新一行垂直居中的控件，通过在控件两边填充glue对象实现
    private JPanel newLine(Component c) {

        JPanel jp = new JPanel();
        jp.setLayout(new BoxLayout(jp, BoxLayout.X_AXIS));
        jp.add(Box.createHorizontalGlue());
        jp.add(c);
        jp.add(Box.createHorizontalGlue());
        jp.setOpaque(false);//设置不透明

        return jp;
    }

}
