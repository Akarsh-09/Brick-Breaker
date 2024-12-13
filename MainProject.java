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
    int XVel = -1;
    int YVel = 1;
    int delay = 4; // 5
    int score = 0;
    static JPanel Slider;
    JPanel ball;
    JPanel ControlPanel;
    Point PrevCoord;
    static Point InitialClick;

    public int Collide(int BallX, int BallY, ArrayList<JPanel> wall)
    {
        for(int i = 0; i < rows * columns - score; i++)
        {
            int WallX = wall.get(i).getX();
            int WallY = wall.get(i).getY();

            int point = 0;

            if(BallX >= WallX + 68 + (point++) || WallX >= BallX + 9 + (point++) || BallY >= WallY + 26 + (point++) || WallY >= BallY + 7 + (point++));
            else
            {
                if(YVel < 0)
                {
                    if(BallX < WallX + 29)
                        XVel = -1 * XVel;
                    else
                        YVel = -1 * YVel;
                    return i;
                }

                else
                {
                    if(BallX + 11 > WallX)
                        XVel = -1 * XVel;
                    else
                        YVel = -1 * YVel;
                    return i;
                }
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

            if(BallX > SliderX - 10 && BallX < SliderX + 38 && BallY < SliderY && BallY > SliderY - 10)
                YVel = -1 * YVel;

            if(BallX < 0 || BallX > 728)
                XVel = -1 * XVel;

            if(BallY < 0)
                YVel = -1 * YVel;

            if(BallY > 770)
            {
                XVel = 0;
                YVel = 0;
                BallX = 720;
                BallY = 330;
                ball.setLocation(720, 330);
                System.out.println(ball.getX() + "  " + ball.getY());
                JOptionPane.showMessageDialog(frame, "You Lost", "", JOptionPane.ERROR_MESSAGE);
            }
            
            int index = Collide(BallX, BallY, wall);
            if(index != -1)
            {
                frame.remove(wall.get(index));
                wall.remove(index);
                score++;
                Score.setText("Score: " + score);
                frame.revalidate();
                frame.repaint();
            }

            ball.setLocation(BallX + XVel, BallY + YVel);
        });

        timer.start();
    }

    public MainProject() {
        frame = new JFrame("Breaker");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(738, 800);

        Border border = BorderFactory.createLineBorder(Color.BLACK, 0);

        Score = new JLabel("Score: 0");
        Font font1 = new Font("Serif", Font.BOLD, 23);
        Score.setBounds(40, 718, 100, 40);
        Score.setFont(font1);
        frame.add(Score);

        Restart = new JButton("Restart");
        Font font2 = new Font("Serif", Font.BOLD, 15);
        Restart.setBounds(600, 715, 85, 35);
        Restart.setFont(font2);
        frame.add(Restart);

        for (int i = 0; i < columns; i++) {
            for (int j = 0; j < rows; j++) {
                JPanel brick = new JPanel();
                brick.setBounds(70 * j + 20, 30 * i + 20, 68, 28);
                brick.setBackground(new Color(50 - 2*j, 48 + 23*j, 174 + 9*i)); // 255 - 10*i, 55 + 10*i + 10*j, 200 - 10*j
                brick.setBorder(border);
                wall.add(brick);
                frame.add(brick);
            }
        }

        Slider = new JPanel();
        Slider.setBounds(350, 600, 38, 7);
        Slider.setBackground(Color.black);
        frame.add(Slider);

        ball = new JPanel();
        ball.setBounds(720, 330, 10, 10); // 720, 330, 10, 10
        ball.setBackground(Color.red);
        frame.add(ball);

        Restart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == Restart) {
                    
                    for(JPanel p : wall)
                        frame.remove(p);
                    wall.clear();
                    score = 0;
                    ball.setBounds(720, 330, 10, 10);
                    Slider.setBounds(350, 600, 38, 7);
                    XVel = -1;
                    YVel = 1;

                    for (int i = 0; i < columns; i++) {
                        for (int j = 0; j < rows; j++) {
                            JPanel brick = new JPanel();
                            brick.setBounds(70 * j + 20, 30 * i + 20, 68, 28);
                            brick.setBackground(new Color(255 - 10*i, 55 + 10*i + 10*j, 200 - 10*j));
                            brick.setBorder(border);
                            wall.add(brick);
                            frame.add(brick);
                        }
                    }

                    Score.setText("Score: 0");
                    frame.revalidate();
                    frame.repaint();
                }
            }
        });

        ControlPanel = new JPanel();
        ControlPanel.setBounds(0, 600, 738, 107);
        ControlPanel.setBackground(Color.lightGray);
        frame.add(ControlPanel);

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