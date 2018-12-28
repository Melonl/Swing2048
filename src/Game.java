import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Game {

    //用于储存颜色的实体类
    private static class Color {
        public Color(int fc, int bgc) {
            fontColor = fc;//字体颜色
            bgColor = bgc;//背景颜色
        }

        public int fontColor;//字体颜色
        public int bgColor;//背景颜色
    }

    JFrame mainFrame;//主窗口对象
    JLabel[] jLabels;//方块，用jlabel代替
    int[] datas = new int[]{0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0,
            0, 0, 0, 0};//每个方块上的数值
    int[] temp = new int[4];//方块移动算法中抽离的的临时数组
    int[] temp2 = new int[16];//用于检测方块是否有合并


    List emptyBlocks = new ArrayList<Integer>(16);//在生成新方块时用到的临时list，用以存放空方块

    //存放颜色的map
    static HashMap<Integer, Color> colorMap = new HashMap<Integer, Color>() {{
        put(0, new Color(0x776e65, 0xCDC1B4));
        put(2, new Color(0x776e65, 0xeee4da));
        put(4, new Color(0x776e65, 0xede0c8));
        put(8, new Color(0xf9f6f2, 0xf2b179));
        put(16, new Color(0xf9f6f2, 0xf59563));
        put(32, new Color(0xf9f6f2, 0xf67c5f));
        put(64, new Color(0xf9f6f2, 0xf65e3b));
        put(128, new Color(0xf9f6f2, 0xedcf72));
        put(256, new Color(0xf9f6f2, 0xedcc61));
        put(512, new Color(0xf9f6f2, 0xe4c02a));
        put(1024, new Color(0xf9f6f2, 0xe2ba13));
        put(2048, new Color(0xf9f6f2, 0xecc400));
    }};

    public Game() {
        initGameFrame();
        initGame();
        refresh();
    }

    //开局时生成两个2的方块和一个4的方块
    private void initGame() {
        for (int i = 0; i < 2; i++) {
            generateBlock(datas, 2);
        }
        generateBlock(datas, 4);
    }

    //随机生成4或者2的方块
    private void randomGenerate(int arr[]) {
        int ran = (int) (Math.random() * 10);
        if (ran > 5) {
            generateBlock(arr, 4);
        } else {
            generateBlock(arr, 2);
        }

    }

    //随机生成新的方块，参数：要生成的方块数值
    private void generateBlock(int arr[], int num) {
        emptyBlocks.clear();

        for (int i = 0; i < 16; i++) {
            if (arr[i] == 0) {
                emptyBlocks.add(i);
            }
        }
        int len = emptyBlocks.size();
        if (len == 0) {
            return;
        }
        int pos = (int) (Math.random() * 100) % len;
        arr[(int) emptyBlocks.get(pos)] = num;
        refresh();

    }


    //胜负判定并做终局处理
    private void judge(int arr[]) {

        if (isWin(arr)) {
            JOptionPane.showMessageDialog(null, "恭喜，你已经成功凑出2048的方块", "你赢了", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        }
        if (isEnd(arr)) {
            int max = getMax(datas);
            JOptionPane.showMessageDialog(null, "抱歉，你没有凑出2048的方块,你的最大方块是：" + max, "游戏结束", JOptionPane.PLAIN_MESSAGE);
            System.exit(0);
        }

    }

    //判断玩家是否胜利，只要有一个方块大于等于2048即为胜利
    private boolean isWin(int arr[]) {
        for (int i : arr) {
            if (i >= 2048) {
                return true;
            }
        }
        return false;

    }

    //此函数用于判断游戏是否结束，如上下左右移后均无法产生空块，即代表方块已满，则返回真，表示游戏结束
    private boolean isEnd(int arr[]) {

        int[] tmp = new int[16];
        int isend = 0;

        System.arraycopy(arr, 0, tmp, 0, 16);
        left(tmp);
        if (isNoBlank(tmp)) {
            isend++;
        }

        System.arraycopy(arr, 0, tmp, 0, 16);
        right(tmp);
        if (isNoBlank(tmp)) {
            isend++;
        }

        System.arraycopy(arr, 0, tmp, 0, 16);
        up(tmp);
        if (isNoBlank(tmp)) {
            isend++;
        }

        System.arraycopy(arr, 0, tmp, 0, 16);
        down(tmp);
        if (isNoBlank(tmp)) {
            isend++;
        }

        if (isend == 4) {
            return true;
        } else {
            return false;
        }
    }

    //判断是否无空方块
    private boolean isNoBlank(int arr[]) {

        for (int i : arr) {
            if (i == 0) {
                return false;
            }
        }
        return true;
    }

    //获取最大的方块数值
    private int getMax(int arr[]) {
        int max = arr[0];
        for (int i : arr) {
            if (i >= max) {
                max = i;
            }
        }
        return max;
    }

    //刷新每个方块显示的数据
    private void refresh() {
        JLabel j;
        for (int i = 0; i < 16; i++) {
            int arr = datas[i];
            j = jLabels[i];
            if (arr == 0) {
                j.setText("");
            } else if (arr >= 1024) {
                j.setFont(new java.awt.Font("Dialog", 1, 42));
                j.setText(String.valueOf(datas[i]));
            } else {
                j.setFont(new java.awt.Font("Dialog", 1, 50));
                j.setText(String.valueOf(arr));
            }

            Color currColor = colorMap.get(arr);
            j.setBackground(new java.awt.Color(currColor.bgColor));
            j.setForeground(new java.awt.Color(currColor.fontColor));
        }
    }

    //初始化游戏窗口，做一些繁杂的操作
    private void initGameFrame() {

        //创建JFrame以及做一些设置
        mainFrame = new JFrame("2048 Game");
        mainFrame.setSize(500, 500);
        mainFrame.setResizable(false);//固定窗口尺寸
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        mainFrame.setLayout(new GridLayout(4, 4));
        mainFrame.getContentPane().setBackground(new java.awt.Color(0xCDC1B4));
        //添加按键监听
        mainFrame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {
            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {

                System.arraycopy(datas, 0, temp2, 0, 16);

                //根据按键的不同调用不同的处理函数
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        up(datas);
                        break;

                    case KeyEvent.VK_DOWN:
                        down(datas);
                        break;

                    case KeyEvent.VK_LEFT:
                        left(datas);
                        break;

                    case KeyEvent.VK_RIGHT:
                        right(datas);
                        break;

                }


                //判断移动后是否有方块合并，若有，生成新方块，若无，不产生新方块
                if (!Arrays.equals(datas, temp2)) {
                    randomGenerate(datas);
                }

                refresh();
                judge(datas);
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
            }
        });

        //使用系统默认的ui风格
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
        }

        //使用16个JLabel来显示16个方块
        jLabels = new JLabel[16];
        JLabel j; //引用复用，避免for里创建过多引用
        for (int i = 0; i < 16; i++) {
            jLabels[i] = new JLabel("0", JLabel.CENTER);
            j = jLabels[i];
            j.setOpaque(true);
            // 设置边界，参数：上，左，下，右，边界颜色
            j.setBorder(BorderFactory.createMatteBorder(6, 6, 6, 6, new java.awt.Color(0xBBADA0)));

            //j.setForeground(new java.awt.Color(0x776E65));
            j.setFont(new java.awt.Font("Dialog", 1, 52));
            mainFrame.add(j);
        }
        mainFrame.setVisible(true);
    }

    private void left(int arr[]) {
        moveLeft(arr);

        combineLeft(arr);

        moveLeft(arr);//合并完后会产生空位，所以要再次左移


    }

    //向左合并方块
    private void combineLeft(int arr[]) {
        for (int l = 0; l < 4; l++) {
            //0 1 2
            for (int i = 0; i < 3; i++) {
                if ((arr[l * 4 + i] != 0 && arr[l * 4 + i + 1] != 0) && arr[l * 4 + i] == arr[l * 4 + i + 1]) {
                    arr[l * 4 + i] *= 2;
                    arr[l * 4 + i + 1] = 0;
                }
            }
        }
    }

    //方块左移，针对每一行利用临时数组实现左移
    private void moveLeft(int arr[]) {
        for (int l = 0; l < 4; l++) {


            int z = 0, fz = 0;//z(零）;fz（非零）
            for (int i = 0; i < 4; i++) {
                if (arr[l * 4 + i] == 0) {
                    z++;
                } else {
                    temp[fz] = arr[l * 4 + i];
                    fz++;
                }
            }
            for (int i = fz; i < 4; i++) {
                temp[i] = 0;
            }
            for (int j = 0; j < 4; j++) {
                arr[l * 4 + j] = temp[j];
            }
        }
    }

    private void right(int arr[]) {

        moveRight(arr);
        combineRight(arr);
        moveRight(arr);

    }

    private void combineRight(int arr[]) {
        for (int l = 0; l < 4; l++) {
            //3 2 1
            for (int i = 3; i > 0; i--) {
                if ((arr[l * 4 + i] != 0 && arr[l * 4 + i - 1] != 0) && arr[l * 4 + i] == arr[l * 4 + i - 1]) {
                    arr[l * 4 + i] *= 2;
                    arr[l * 4 + i - 1] = 0;
                }
            }
        }
    }

    private void moveRight(int arr[]) {

        for (int l = 0; l < 4; l++) {

            int z = 3, fz = 3;//z(零）;fz（非零）
            for (int i = 3; i >= 0; i--) {
                if (arr[l * 4 + i] == 0) {
                    z--;
                } else {
                    temp[fz] = arr[l * 4 + i];
                    fz--;
                }
            }
            for (int i = fz; i >= 0; i--) {
                temp[i] = 0;
            }
            for (int j = 3; j >= 0; j--) {
                arr[l * 4 + j] = temp[j];
            }
        }
    }


    private void up(int arr[]) {
        moveUp(arr);
        combineUp(arr);
        moveUp(arr);

    }

    private void combineUp(int arr[]) {


        for (int r = 0; r < 4; r++) {
            for (int i = 0; i < 3; i++) {
                if ((arr[r + 4 * i] != 0 && arr[r + 4 * (i + 1)] != 0) && arr[r + 4 * i] == arr[r + 4 * (i + 1)]) {
                    arr[r + 4 * i] *= 2;
                    arr[r + 4 * (i + 1)] = 0;
                }
            }
        }
    }

    private void moveUp(int arr[]) {

        for (int r = 0; r < 4; r++) {

            int z = 0, fz = 0;//z(零）;fz（非零）
            for (int i = 0; i < 4; i++) {
                if (arr[r + 4 * i] == 0) {
                    z++;
                } else {
                    temp[fz] = arr[r + 4 * i];
                    fz++;
                }
            }
            for (int i = fz; i < 4; i++) {
                temp[i] = 0;
            }
            for (int j = 0; j < 4; j++) {
                arr[r + 4 * j] = temp[j];
            }
        }
    }


    private void down(int arr[]) {
        moveDown(arr);
        combineDown(arr);
        moveDown(arr);
    }

    private void combineDown(int arr[]) {
        for (int r = 0; r < 4; r++) {
            for (int i = 3; i > 0; i--) {
                if ((arr[r + 4 * i] != 0 && arr[r + 4 * (i - 1)] != 0) && arr[r + 4 * i] == arr[r + 4 * (i - 1)]) {
                    arr[r + 4 * i] *= 2;
                    arr[r + 4 * (i - 1)] = 0;
                }
            }
        }
    }

    private void moveDown(int arr[]) {
        for (int r = 0; r < 4; r++) {

            int z = 3, fz = 3;//z(零）;fz（非零）
            for (int i = 3; i >= 0; i--) {
                if (arr[r + 4 * i] == 0) {
                    z--;
                } else {
                    temp[fz] = arr[r + 4 * i];
                    fz--;
                }
            }
            for (int i = fz; i >= 0; i--) {
                temp[i] = 0;
            }
            for (int j = 3; j >= 0; j--) {
                arr[r + 4 * j] = temp[j];
            }
        }
    }

}
