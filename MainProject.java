import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.Timer;
import javax.swing.border.Border;

public class MainProject {

    ArrayList<JPanel> wall = new ArrayList<JPanel>();
    JFrame frame;
    int rows = 10;
    int columns = 10;
    JLabel Score;
    JButton Restart;
    JButton Start;
    int XVel = 0;
    int YVel = 0;
    int velocity = 4;
    int brickWidth = 68;
    int brickHeight = 28;
    int ballWidth = 10;
    int ballHeight = 10;
    int sliderWidth = 45;
    int sliderHeight = 10;
    int delay = 10;
    int score = 0;
    int change = 0;
    int bool = 0;
    static JPanel Slider;
    JPanel ball;
    JPanel ControlPanel;
    JPanel BG;
    JPanel LastLine;
    Point PrevCoord;
    static Point InitialClick;
    Border border = BorderFactory.createLineBorder(Color.BLACK, 0);

    public int Break(int BallX, int BallY, ArrayList<JPanel> wall)
    {
        for(int i = 0; i < rows * columns - score; i++)
        {
            int WallX = wall.get(i).getX();
            int WallY = wall.get(i).getY();
            int error = 3;
            // System.out.println("Reverse");

            if(!(BallX > WallX + brickWidth || WallX > BallX + ballWidth || BallY > WallY + brickHeight || WallY > BallY + ballHeight))
            {
                if(YVel < 0)
                {
                    if(BallY >= WallY + brickHeight - error)
                        YVel = -YVel;
                    else
                        XVel = -XVel;
                }
                else
                {
                    if(BallY + ballHeight <= WallY + error)
                        YVel = -YVel;
                    else
                        XVel = -XVel;
                }
                return i;
            }
        }
        return -1;
    }

    public void move() {
        Timer timer = new Timer(delay, e -> {

            int BallX = ball.getX();
            int BallY = ball.getY();
            int SliderX = Slider.getX();
            int SliderY = Slider.getY();
            change = 0;
            int error = 3;

            if(BallX > SliderX - ballWidth && BallX < SliderX + sliderWidth && BallY > SliderY - ballHeight && (bool == 0))
            {
                YVel = -YVel;
                bool = 1;
                change = BallY - SliderY + error + ballHeight + YVel;
            }
            else if(!(BallX > SliderX - ballWidth && BallX < SliderX + sliderWidth && BallY > SliderY - ballHeight))
                bool = 0;

            if(BallX <= 0 || BallX >= 728)
                XVel = -XVel;

            if(BallY <= 0)
                YVel = -YVel;

            if(BallY > 700 - error)
            {
                XVel = 0;
                YVel = 0;
                BallX = 730 - ballWidth;
                BallY = 340 - ballHeight;
                ball.setLocation(730 - ballWidth, 340 - ballHeight);
                JOptionPane.showMessageDialog(frame, "You Lost", "Game Ended", JOptionPane.ERROR_MESSAGE);
            }
            
            int index = Break(BallX, BallY, wall);
            if(index != -1)
            {
                frame.remove(wall.get(index));
                wall.remove(index);
                score++;
                Score.setText("Score: " + score);
                frame.revalidate();
                frame.repaint();

                if(score == 100)
                {
                    int bx = ball.getX();
                    int by = ball.getY();
                    int sx = Slider.getX();
                    int sy = Slider.getY();
                    JOptionPane.showMessageDialog(frame, "You Won!", "Game Completed", JOptionPane.INFORMATION_MESSAGE);
                    ball.setBounds(bx, by, 10, 10);
                    Slider.setBounds(sx, sy, sliderWidth, sliderHeight); // 350, 600, 45, 7
                    XVel = 0;
                    YVel = 0;

                    frame.remove(BG);
                    frame.add(BG);
                    frame.revalidate();
                    frame.repaint();
                }
            }
            ball.setLocation(BallX + XVel, BallY + YVel - change);
        });
        timer.start();
    }

    public MainProject()
    {
        frame = new JFrame("Breaker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(738, 800);

        Score = new JLabel("Score: 0");
        Font font1 = new Font("Serif", Font.BOLD, 23);
        // font1.
        Score.setBounds(40, 718, 100, 40);
        Score.setFont(font1);
        Score.setForeground(Color.white);
        frame.add(Score);

        Start = new JButton("Start");
        Font font2 = new Font("Serif", Font.BOLD, 15);
        Start.setBounds(600, 725, 85, 35);
        Start.setFont(font2);
        frame.add(Start);

        Restart = new JButton("Restart");
        Restart.setBounds(600, 725, 85, 35);
        Restart.setFont(font2);
        frame.add(Restart);

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                JPanel brick = new JPanel();
                brick.setBounds(70 * j + 20, 30 * i + 20, brickWidth, brickHeight);
                brick.setBackground(new Color(50 - 2*j, 48 + 23*j, 174 + 9*i)); // 255 - 10*i, 55 + 10*i + 10*j, 200 - 10*j
                brick.setBorder(border);
                wall.add(brick);
                frame.add(brick);
            }
        }

        Slider = new JPanel();
        Slider.setBounds(350, 690, sliderWidth, sliderHeight); // 350, 690, 45, 7
        Slider.setBackground(Color.black);
        frame.add(Slider);

        ball = new JPanel();
        ball.setBounds(730 - ballWidth, 340 - ballHeight, ballWidth, ballHeight); // 720, 330, 10, 10
        ball.setBackground(Color.red);
        frame.add(ball);

        Start.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == Start) {
                    XVel = -velocity;
                    YVel = velocity;
                    frame.remove(Start);
                }
            }
        });

        Restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == Restart) {
                    
                    for(JPanel p : wall)
                        frame.remove(p);
                    wall.clear();
                    score = 0;
                    ball.setBounds(720, 330, ballWidth, ballHeight);
                    Slider.setBounds(350, 690, sliderWidth, sliderHeight); // 350, 690, 38, 7
                    XVel = -velocity;
                    YVel = velocity;

                    for (int i = 0; i < columns; i++) {
                        for (int j = 0; j < rows; j++) {
                            JPanel brick = new JPanel();
                            brick.setBounds(70 * j + 20, 30 * i + 20, brickWidth, brickHeight);
                            brick.setBackground(new Color(50 - 2*j, 48 + 23*j, 174 + 9*i));
                            brick.setBorder(border);
                            wall.add(brick);
                            frame.add(brick);
                        }
                    }

                    Score.setText("Score: 0");
                    frame.remove(BG);
                    frame.add(BG);
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });

        ControlPanel = new JPanel();
        ControlPanel.setBounds(0, 600, 738, 100 + sliderHeight);
        ControlPanel.setBackground(Color.darkGray);
        frame.add(ControlPanel);

        LastLine = new JPanel();
        LastLine.setBounds(0, 700 + sliderHeight, 738, 6);
        LastLine.setBackground(Color.red);
        frame.add(LastLine);

        BG = new JPanel();
        BG.setBounds(0, 0, 738, 800);
        BG.setBackground(Color.BLACK);
        frame.add(BG);

        frame.setBackground(Color.black);
        frame.setLayout(null);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new MainProject().move(); 

        Slider.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent e) {
                InitialClick = e.getPoint();
            }
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mouseReleased(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}
        });

        Slider.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {

                int thisX = ((JPanel) e.getSource()).getX();
                int thisY = ((JPanel) e.getSource()).getY();
                int xMoved = e.getX() - InitialClick.x;
                int yMoved = e.getY() - InitialClick.y;
                int newX = thisX + xMoved;
                int newY = thisY + yMoved;

                Point new01 = new Point(newX, newY);

                if(new01.x < 0)
                    ((JPanel) e.getSource()).setLocation(0, newY);
                if(new01.x > 700)
                    ((JPanel) e.getSource()).setLocation(700, newY);
                if(new01.y < 600)
                    ((JPanel) e.getSource()).setLocation(newX, 600);
                if(new01.y > 700)
                    ((JPanel) e.getSource()).setLocation(newX, 700);
                if(new01.x < 0 && new01.y < 600)
                    ((JPanel) e.getSource()).setLocation(0, 600);
                if(new01.x < 0 && new01.y > 700)
                    ((JPanel) e.getSource()).setLocation(0, 700);
                if(new01.x > 700 && new01.y < 600)
                    ((JPanel) e.getSource()).setLocation(700, 600);
                if(new01.x > 700 && new01.y > 700)
                    ((JPanel) e.getSource()).setLocation(700, 700);
                if(new01.x >= 0 && new01.x <= 700 && new01.y <= 700 && new01.y >= 600)
                    ((JPanel) e.getSource()).setLocation(newX, newY);
            }
        });
    }
}