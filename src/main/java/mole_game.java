import javax.swing.*;
import java.awt.*;

public class mole_game {
    static class background extends JFrame{
        private ImageIcon icon;

        public background() {
            icon = new ImageIcon("grass.jpg");

            JPanel background = new JPanel() {
                public void paintComponent(Graphics g) {
                    g.drawImage(icon.getImage(), 0, 0, null);
                    setOpaque(false);
                    super.paintComponent(g);
                }
            };
        }
    }

    public static void main(String[] args) {
        background frame = new background();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1024, 768);
        frame.setVisible(true);
    }
}

/*
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

class Monster {
	int x, y, xInc, yInc, diameter;
	final Random r = new Random();
	ImageIcon icon = new ImageIcon("d://monster.png");
	Image img;

	public Monster(int d) {
		this.diameter = d;
		x = (int) (Math.random() * (monstergame.WIDTH - d) + 3);
		y = (int) (Math.random() * (monstergame.HEIGHT - d) + 3);
		xInc = (int) (Math.random() * 1 + 1);
		yInc = (int) (Math.random() * 1 + 1);
	}

	public void paint(Graphics g) {
		img = icon.getImage();
		if (x < 0 || x > (monstergame.WIDTH - diameter))
			xInc = -xInc;
		if (y < 0 || y > (monstergame.HEIGHT - diameter))
			yInc = -yInc;
		x += xInc;
		y += yInc;
		g.drawImage(img, x, y, null);
	}
}

public class monstergame extends JFrame implements ActionListener {
	ArrayList<Monster> monsters = new ArrayList<Monster>();

	static final int WIDTH = 600;
	static final int HEIGHT = 400;
	private static final int PERIOD = 10;
	ImageIcon icon = new ImageIcon("d://monster.png");
	Image img;
	int count=0;
	JLabel countLabel = new JLabel("count: " + count);

	class MyPanel extends JPanel {
		public MyPanel() {
			for (int i = 0; i < 10; i++) {
				monsters.add(new Monster((int) (30 + 30 * Math.random())));
			}

		}
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			for (Monster b : monsters) {
				b.paint(g);
			}
		}
	}

	public monstergame() {
		MyPanel panel = new MyPanel();
		panel.setPreferredSize(new Dimension(WIDTH, HEIGHT));

		panel.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				for(int i=0;i<monsters.size();i++) {
					Rectangle rect = new Rectangle(monsters.get(i).x, monsters.get(i).y, 50, 53);
                    if (rect.contains(e.getPoint())) {
                   	 monsters.remove(i);
                   	 count++;
                   	 countLabel.setText("count: " + count);
                    }
				}
			}
		});

		add(panel);
		panel.add(countLabel);
		pack();

		setTitle("MonsterGame");
		Timer timer = new Timer(PERIOD, this);
		timer.start();

		setVisible(true);
		panel.addKeyListener(new MyKeyListener());
		panel.setFocusable(true);
		panel.requestFocus();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public void actionPerformed(ActionEvent evt) {
		repaint();
	}

	public class MyKeyListener extends KeyAdapter {
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER) {
				monsters.clear();
				for (int i = 0; i < 10; i++) {
					monsters.add(new Monster((int) (30 + 30 * Math.random())));
				}
				count=0;
				countLabel.setText("count: " + count);
			}
		}
	}

	public static void main(String[] args) {
		monstergame g = new monstergame();
	}

}
 */