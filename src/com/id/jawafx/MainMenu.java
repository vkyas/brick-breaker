package com.id.jawafx;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

class mainFun {
    public static void main(String[] args) {
        JFrame fx = new JFrame();
        MainMenu mainMenu = new MainMenu();
        fx.setBounds(10, 10, 700, 600);
        fx.setTitle("Game");
        fx.setVisible(false);
        fx.setVisible(true);
        fx.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fx.add(mainMenu);
    }
}

class mainMap {
    public int map[][];
    public int layout_weight;
    public int layout_height;
    public mainMap(int row, int col) {
        map = new int[row][col];
        /* int coef = 1;
        for(int i = 0; i < map.length; i++) {
            for(int space = 0; space < map.length -i; ++space) {
            for(int j = 0; j < i; j++) {
                if((j == 0) || (i == 0)) {
                    coef = 1;
                } else 
                    coef = coef * (i - j + 1)/j;
                    map[i][j] = 1;
                }
            }
        }
      */
      for(int i = 0; i < map.length; i++) {
        for(int j = 0; j < map[i].length; j++) {
            map[i][j] = 1;
        }
      }
      layout_weight = 540/col;
      layout_height = 200/row;
    }

    public void draw(Graphics2D g) {
        for(int i = 0; i < map.length; i++) {
            for(int j = i; j < map[i].length -i; j++) {
                if(map[i][j] > 0) {
                    g.setColor(Color.red);
                    g.fillRect(j * layout_weight +80, i * layout_height +50, layout_weight, layout_height);
                    g.setStroke(new BasicStroke(5));
                    g.setColor(Color.black);
                    g.drawRect(j * layout_weight +80, i * layout_height +50, layout_weight, layout_height);
                }
            }
        }
    }
    public void setBrickValue(int value, int row, int col) {
        map[row][col] = value;
    }
}

// GamePlay

public class MainMenu extends JPanel implements KeyListener, ActionListener {
    private boolean mulai = false;
    private int sekor = 0;

    private int totalSekor = 2000;

    private Timer waktu;
    private int delay = 8;

    private int playerX = 310;

    private int ballposX = 349;
    private int ballposY = 529;
    private int ballXdir = -1;
    private int ballYdir = -2;

    private mainMap map;

    public MainMenu() {
        map = new mainMap(20, 50);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        waktu = new Timer(delay, this);
        waktu.start();
    }

    public void paint(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        map.draw((Graphics2D) g);

        g.setColor(Color.black);
        g.fillRect(0, 0, 9, 592);
        g.fillRect(0, 0, 692, 9);
        g.fillRect(691, 0, 9, 592);

        g.setColor(Color.lightGray);
        g.setFont(new Font("serif", Font.ROMAN_BASELINE, 18));
        g.drawString(" "+sekor, 640, 18);

        g.setColor(Color.orange);
        g.fillRect(playerX, 550, 200, 4);

        g.setColor(Color.lightGray);
        g.fillOval(ballposX, ballposY, 13, 13);

        if(totalSekor <= 0) {
            mulai = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Win", 271, 300);

            g.setColor(Color.gray);
            g.setFont(new Font("serif", Font.ITALIC, 20));
            g.drawString("Press Enter to Restart", 230, 340);
        }

        if(ballposY > 570) {
            mulai = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.RED);
            g.setFont(new Font("serif", Font.BOLD, 30));
            // g.drawString("Game Over", 255, 300);

            g.setColor(Color.gray);
            g.setFont(new Font("serif", Font.ITALIC, 20));
            g.drawString("Press Enter to Restart", 230, 330);
        }
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      waktu.start();
        if(mulai) {
            if(new Rectangle(ballposX, ballposY, 20, 20)
                    .intersects(new Rectangle(playerX, 550, 200, 4))) {
                ballYdir = -ballYdir;
            }
            A: for(int i = 0; i < map.map.length; i++) {
                for(int j = 0; j < map.map[i].length -i; j++) {
                    if(map.map[i][j] > 0) {
                        int brickX = j* map.layout_weight +80;
                        int brickY = i* map.layout_height +50;
                        int bricksWidth = map.layout_weight;
                        int bricksHeight = map.layout_height;

                        Rectangle rect = new Rectangle(brickX, brickY, bricksWidth, bricksHeight);
                        Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
                        Rectangle brickRect = rect;
                        if(ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalSekor--;
                            sekor += 10;
                            if(ballposX +19 <= brickRect.x || ballposX +1 >= brickRect.x + brickRect.width) {
                                ballXdir = -ballXdir;
                            } else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                        }
                    }
                }
            }
            ballposX += ballXdir;
            ballposY += ballYdir;
            if(ballposX < 0) {
                ballXdir = -ballXdir;
            }
            if(ballposY < 0) {
                ballYdir = -ballYdir;
            }
            if(ballposX > 670) {
                ballXdir = -ballXdir;
            }
        }
        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if(playerX >= 590) {
                playerX = 590;
            } else {
                moveRight();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            if(playerX <= 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if(e.getKeyCode() == KeyEvent.VK_ENTER) {
            if(!mulai) {
                sekor = 0;
                mulai = true;
                ballposX = 345;
                ballposY = 529;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                totalSekor = 200;
                map = new mainMap(20, 50);

                repaint();
            }
        }
    }

    public void moveRight() {
        mulai = true;
        playerX += 20;
    }

    public void moveLeft() {
        mulai = true;
        playerX -= 20;
    }

    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyReleased(KeyEvent e) {}
}