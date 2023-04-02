package com.selfstudy.ui;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Random;

public class GameJFrame extends JFrame implements KeyListener, ActionListener {

    // 创建选项下面的条目
    JMenuItem beautyItem = new JMenuItem("美女");
    JMenuItem animalItem = new JMenuItem("动物");
    JMenuItem sportItem = new JMenuItem("运动");

    JMenuItem replayItem = new JMenuItem("重新游戏");
    JMenuItem reloginItem = new JMenuItem("重新登录");
    JMenuItem closeItem = new JMenuItem("退出游戏");
    JMenuItem ruleItem = new JMenuItem("快捷键");

    JMenuItem accountItem = new JMenuItem("图片");

    // 图片类别数组
    String[] imgTypes = {"girl", "animal", "sport"};

    // 游戏图片行列数（行列数一样, 方阵）
    int squareMatrix = 4;
    // 图片数据
    int[][] data = new int[squareMatrix][squareMatrix];
    // 正确的数据
    int[][] winData = new int[squareMatrix][squareMatrix];
    // 图片对象
    JLabel[][] imgJLabels = new JLabel[squareMatrix][squareMatrix];

    // 图片索引和类型
    String imgType = imgTypes[0];
    int imgIndex = 7;

    // 空白格行列位置
    int x, y;
    // 步数
    int step;


    // 是否正在查看原图
    boolean isPreview = false;

    // 图片根路径
    String baseImgPath = "pazzlegame\\image\\";
    // 当前展示图片的路径
    String imgPath = baseImgPath + imgType + "\\" + imgType + imgIndex + "\\";

    // 创建游戏界面
    public GameJFrame() throws HeadlessException {
        initJFrame();
        initMenu();
        initWinData();
        initData();
        initImage();
        this.setVisible(true);
    }

    private void initWinData() {
        for (int i = 0; i < squareMatrix; i++) {
            for (int j = 0; j < squareMatrix; j++) {
                winData[i][j] = i * squareMatrix + j + 1;
            }
        }
        winData[squareMatrix - 1][squareMatrix - 1] = 0;
    }

    private void initData() {
        // 随机打乱数据，容易出现不可复原的情况（弃用）
        // 将0-15打乱放入一个二维数组中
        int[] randomList = new int[squareMatrix * squareMatrix];
        for (int i = 0; i < randomList.length; i++) {
            randomList[i] = i;
        }
        Random r = new Random();
        for (int i = 0; i < randomList.length; i++) {
            // 获取随机索引
            int index = r.nextInt(randomList.length);
            int temp = randomList[i];
            randomList[i] = randomList[index];
            randomList[index] = temp;
        }

        for (int i = 0; i < randomList.length; i++) {
            if (randomList[i] == 0) {
                x = i / squareMatrix;
                y = i % squareMatrix;
            }
            data[i / squareMatrix][i % squareMatrix] = randomList[i];
        }
        if (victory()) {
            // 初始化的数据为成功,则重新初始化
            initData();
        }
    }


    private void initImage() {
        this.getContentPane().removeAll();
        if (victory()) {
            // 添加胜利图片
            JLabel bgImg = new JLabel(new ImageIcon(baseImgPath + "win.png"));
            bgImg.setBounds(203, 283, 197, 73);
            this.getContentPane().add(bgImg);
        }

        JLabel stepCount = new JLabel("步数：" + step);
        stepCount.setBounds(50, 30, 100, 20);
        this.getContentPane().add(stepCount);

        for (int i = 0; i < squareMatrix; i++) {
            for (int j = 0; j < squareMatrix; j++) {
                if (data[i][j] == 0) {
                    continue;
                }
                final int X = i;
                final int Y = j;
                ImageIcon imgItem = new ImageIcon(imgPath + data[i][j] + ".jpg");
                // 创建JLabel对象(管理容器)
                JLabel jLabel = new JLabel(imgItem);
                imgJLabels[i][j] = jLabel;
                // 图片添加鼠标点击事件监听
                jLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        moveImg(X, Y);
                    }
                });
                // 指定图片位置
                jLabel.setBounds(105 * j + 83, 105 * i + 134, 105, 105);
                jLabel.setBorder(new BevelBorder(BevelBorder.LOWERED));
                // 把管理容器添加到界面
                // imgItem.addMouseListener(this);
                this.getContentPane().add(jLabel);
            }
        }

        // 添加背景图片
        JLabel bgImg = new JLabel(new ImageIcon(baseImgPath + "background.png"));
        bgImg.setBounds(40, 40, 508, 560);
        this.getContentPane().add(bgImg);

        // 刷新界面
        this.getContentPane().repaint();

    }

    private void initJFrame() {
        this.setSize(603, 680);
        this.setTitle("拼图游戏");
        // this.setBackground(Color.BLUE);
        this.getContentPane().setBackground(new Color(0xfdec8c));
        // 窗口置顶
        this.setAlwaysOnTop(true);
        // 窗口居中
        this.setLocationRelativeTo(null);
        // 窗口右上角关闭按钮的模式
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        // 取消默认的居中放置
        this.setLayout(null);
        // 给整个界面添加键盘监听事件
        this.addKeyListener(this);
    }

    private void initMenu() {
        // 创建菜单
        JMenuBar jMenuBar = new JMenuBar();

        JMenu functionJMenu = new JMenu("功能");
        JMenu aboutJMenu = new JMenu("关于我们");
        JMenu changeMenu = new JMenu("更换图片");

        changeMenu.add(beautyItem);
        changeMenu.add(animalItem);
        changeMenu.add(sportItem);

        functionJMenu.add(changeMenu);
        functionJMenu.add(replayItem);
        // functionJMenu.add(reloginItem);
        functionJMenu.add(closeItem);
        functionJMenu.add(ruleItem);

        aboutJMenu.add(accountItem);

        // 给条目绑定事件
        beautyItem.addActionListener(this);
        animalItem.addActionListener(this);
        sportItem.addActionListener(this);
        replayItem.addActionListener(this);
        reloginItem.addActionListener(this);
        closeItem.addActionListener(this);
        ruleItem.addActionListener(this);
        accountItem.addActionListener(this);

        jMenuBar.add(functionJMenu);
        // jMenuBar.add(aboutJMenu);

        // 给整个界面设置菜单
        this.setJMenuBar(jMenuBar);

    }

    /***
     * 移动图片
     * @param i 要移动的图片所在行
     * @param j 要移动的图片所在列
     */
    private void moveImg(int i, int j) {
        if (victory()) return; // 游戏已经胜利，不可移动
        // 超出二维数组，不可移动
        if (i < 0 || i >= squareMatrix || j < 0 || j >= squareMatrix) return;
        if (i == x && j + 1 == y) {
            // 右移图片
            data[x][y] = data[i][j];
            data[i][j] = 0;
            y = y - 1;
            step++;
            initImage();
        } else if (i == x && j - 1 == y) {
            // 左移图片
            data[x][y] = data[i][j];
            data[i][j] = 0;
            y = y + 1;
            step++;
            initImage();
        } else if (i + 1 == x && j == y) {
            // 下移图片
            data[x][y] = data[i][j];
            data[i][j] = 0;
            x = x - 1;
            step++;
            initImage();
        } else if (i - 1 == x && j == y) {
            // 下移图片
            data[x][y] = data[i][j];
            data[i][j] = 0;
            x = x + 1;
            step++;
            initImage();
        }
    }

    private void showAllImage() {
        this.getContentPane().removeAll();
        JLabel all = new JLabel(new ImageIcon(imgPath + "all.jpg"));
        all.setBounds(83, 134, 420, 420);
        this.getContentPane().add(all);

        // 添加背景图片
        JLabel bgImg = new JLabel(new ImageIcon(baseImgPath + "background.png"));
        bgImg.setBounds(40, 40, 508, 560);
        this.getContentPane().add(bgImg);

        // 刷新界面
        this.getContentPane().repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == 65 && !isPreview) {
            // 预览原图
            isPreview = true;
            showAllImage();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == 65) {
            isPreview = false;
            initImage();
            // 松开A键, 显示游戏视图
        } else if (code == 81) {
            // 点击Q键,退出游戏
            System.exit(0);
        }

        if (victory()) return;
        if (isPreview) return;
        if (code == 87) { // W按键，一键胜利
            data = winData;
            initImage();
        } else if (code == 67) {
            // 点击C,切换图片
            Random r = new Random();
            int imgTypeIndex = r.nextInt(3);
            int imgRangeIndex = imgTypeIndex == 0 ? 11 : imgTypeIndex == 1 ? 8 : 10;
            int imgIndex = r.nextInt(imgRangeIndex) + 1;
            changeImg(imgTypes[imgTypeIndex], imgIndex);

        } else if (code == 37) {
            // 向左按键，图片向右, 相当于鼠标点击了空白格右边的图片
            moveImg(x, y + 1);
        } else if (code == 38) {
            // 向上
            moveImg(x + 1, y);
        } else if (code == 39) {
            // 向右
            moveImg(x, y - 1);
        } else if (code == 40) {
            // 向下
            moveImg(x - 1, y);
        }

    }

    public boolean victory() {
        if (x != squareMatrix - 1 || y != squareMatrix - 1) return false; // 空白格不在右下角
        for (int i = 0; i < data.length; i++) {
            for (int j = 0; j < data[i].length; j++) {
                if (data[i][j] != winData[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == replayItem) {
            // 重新游戏
            replay();
        } else if (e.getSource() == reloginItem) {
            // 重新登录
            // 关闭当前页面
            this.setVisible(false);
            // 打开登录界面
            new LoginJFrame();
        } else if (e.getSource() == closeItem) {
            System.exit(0);
        } else if (e.getSource() == ruleItem) {
            showRule();
        } else if (e.getSource() == accountItem) {
            showAccount();
        } else if (e.getSource() == beautyItem) {
            changeImg("girl", 11);
        } else if (e.getSource() == animalItem) {
            changeImg("animal", 8);
        } else if (e.getSource() == sportItem) {
            changeImg("sport", 10);
        }
    }

    /**
     * 修改游戏的图片并重新开始游戏
     *
     * @param type 图片类型：girl，animal，sport
     * @param i    图片索引girl：1-13，animal：1-8，sport：1-10
     */
    private void changeImg(String type, int i) {
        Random r = new Random();
        int index;
        do {
            index = r.nextInt(i) + 1;
        } while (Objects.equals(imgType, type) && index == imgIndex);
        imgType = type;
        imgIndex = index;
        imgPath = baseImgPath + imgType + "\\" + imgType + imgIndex + "\\";
        replay();
    }

    private void replay() {
        step = 0;
        initData();
        initImage();
    }

    private void showRule() {
        JDialog jDialog = new JDialog();
        String text = """

                 A: 显示完整图

                 Q:退出游戏

                 C:切换图片

                """;
        JTextArea jTextArea = new JTextArea(text);
        jTextArea.setBounds(10, 10, 258, 258);
        jTextArea.setEditable(false);
        jDialog.getContentPane().add(jTextArea);
        jDialog.setSize(344, 344);
        jDialog.setAlwaysOnTop(true);
        jDialog.setLocationRelativeTo(null);
        // 设置弹窗不关闭,无法操作底下的界面
        jDialog.setModal(true);
        jDialog.setVisible(true);
    }

    private void showAccount() {
        JDialog jDialog = new JDialog();
        JLabel jLabel = new JLabel(new ImageIcon(baseImgPath + "about.jpg"));
        jLabel.setBounds(0, 0, 258, 258);
        jDialog.getContentPane().add(jLabel);
        jDialog.setSize(344, 344);
        jDialog.setAlwaysOnTop(true);
        jDialog.setLocationRelativeTo(null);
        // 设置弹窗不关闭,无法操作底下的界面
        jDialog.setModal(true);
        jDialog.setVisible(true);
    }
}
