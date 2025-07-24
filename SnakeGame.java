package can;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener, KeyListener {
	
    boolean spielGestartet = false;
    boolean gameOver = false;
    
    JButton startButton;
    JButton restartButton;

    final int WIDTH = 600;
    final int HEIGHT = 600;
    final int TILE_SIZE = 20;
    final int TILES_X = WIDTH / TILE_SIZE;
    final int TILES_Y = HEIGHT / TILE_SIZE;
    int punkte = 0;
    Timer timer;
    ArrayList<Point> snake;
    Point food;
    char direction = 'R';  // U = up, D = down, L = left, R = right

    

    public SnakeGame() {
    	//spielfeld
    	int width = TILES_X * TILE_SIZE;
    	int height = TILES_Y * TILE_SIZE;
    	
    	setPreferredSize(new Dimension(width, height));
    	setBackground(Color.BLACK);
    	setFocusable(true);
    	addKeyListener(this);
    	
    	
    	setLayout(null);
    	
    	startButton = new JButton("Spiel starten");
    	startButton.setBounds(WIDTH/2 - 75, HEIGHT/2 - 20, 150, 40);
    	startButton.addActionListener(e -> {
    		spielGestartet = true;
    		startButton.setVisible(false);
    		initGame();
    	});
    	add(startButton);
    	
    	restartButton = new JButton("Erneut spielen");
    	restartButton.setBounds(WIDTH/2 - 75, HEIGHT/2 + 30, 150, 40);
    	restartButton.addActionListener(e -> {
    		spielGestartet = true;
    		restartButton.setVisible(false);
    		initGame();
    	});
    	
        add(restartButton);
        restartButton.setVisible(false);
    }

    public void initGame() {
        snake = new ArrayList<>();
        snake.add(new Point(5, 5));
        spawnFood();
        punkte = 0;
        direction = 'R';
        gameOver= false;
        
        if (timer != null) timer.stop();
        timer = new Timer(100,this);
        timer.start();
    }

    public void spawnFood() {
        Random rand = new Random();
        food = new Point(rand.nextInt(TILES_X), rand.nextInt(TILES_Y));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (!spielGestartet) return; //nicht gestartet - nichts zeichnen

        // Snake zeichnen
        g.setColor(Color.GREEN);
        for (Point p : snake) {
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Essen zeichnen
        g.setColor(Color.RED);
        g.fillOval(food.x * TILE_SIZE, food.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        //Punkte anzeigen
        g.setColor(Color.WHITE);
        g.drawString("Punkte: " + punkte, 10, 20);

        if (gameOver) {
            g.setColor(Color.WHITE);
            g.drawString("Game Over! 🐍", WIDTH / 2 - 40, HEIGHT / 2);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) return;

        Point head = snake.get(0);
        Point newHead = new Point(head);

        // Richtung bestimmen
        switch (direction) {
            case 'U': newHead.y--; break;
            case 'D': newHead.y++; break;
            case 'L': newHead.x--; break;
            case 'R': newHead.x++; break;
        }

        // Check: Wand oder sich selbst
        if (newHead.x < 0 || newHead.y < 0 || newHead.x >= TILES_X || newHead.y >= TILES_Y || snake.contains(newHead)) {
            gameOver = true;
            timer.stop();
            restartButton.setVisible(true);
            repaint();
            return;
        }

        // Bewegung
        snake.add(0, newHead);

        // Essen gefressen?
        if (newHead.equals(food)) {
            spawnFood();
            punkte++;// neues Futter
        } else {
            snake.remove(snake.size() - 1);  // hinten kürzen
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
    	if (!spielGestartet || gameOver) return; //keine steuerung wenn kein spiel
    	
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP && direction != 'D') direction = 'U';
        if (code == KeyEvent.VK_DOWN && direction != 'U') direction = 'D';
        if (code == KeyEvent.VK_LEFT && direction != 'R') direction = 'L';
        if (code == KeyEvent.VK_RIGHT && direction != 'L') direction = 'R';
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
    	SnakeGame panel = new SnakeGame();
    	
        JFrame fenster = new JFrame("Snake 🐍");
        fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        fenster.getContentPane().add(panel);
        fenster.pack();
        fenster.setResizable(false);
        fenster.setLocationRelativeTo(null);  // zentrieren
        fenster.setVisible(true);
        
    }
}
