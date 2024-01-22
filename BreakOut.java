package Game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.JOptionPane;

class Item{
	public int x, y, width, height, dx, dy;
}

class ControlWindow extends JPanel implements Runnable, KeyListener{
	
	private JButton startButton;
	private JButton levelButton;
	private JButton helpButton;
	private JComboBox<String> difficultyComboBox;
	
	
	public static final int WIDTH = 800;
	public static final int HEIGHT = 800;
	public static final int BRICK_X = 12;
	public static final int BRICK_Y = 7;
	public static final int BRICK_WIDTH = 50;
	public static final int BRICK_HEIGHT = 40;
	
	
	private static final int EASY_BRICK_COUNT = 60;
	private static final int MEDIUM_BRICK_COUNT = 72;
	private static final int HARD_BRICK_COUNT = 84;
	  // bien cờ
	private boolean gameStart = false;
	
	
 
	
	private final Random rand = new Random();
	private Item player, ball;
	private Item[] bricks;
	//private java.util.Random rand = new java.util.Random();
	
	private Thread gameThread;
	private boolean gameOver = false, gameWin = false;
	private int amount = 0;
	private  int score = 0;
	
	public void setup() {
		
		
		player = new Item();
		player.width= 120;
		player.height = 20;
		player.x = WIDTH / 2 - player.width / 2;
		player.y = HEIGHT - player.height -50;
		player.dx = 18;
		
		ball = new Item();
		ball.width= 26;
		ball.height = 26;
		ball.x = WIDTH / 2 - ball.width / 2;
		ball.y = HEIGHT  / 2 - ball.height / 2 -50;
		ball.dx = -2;
		ball.dy = 3;
		
		bricks = new Item[BRICK_X * BRICK_Y];
		for(int i = 0; i < BRICK_X; i++) {
			for(int j = 0; j < BRICK_Y; j++) {
				bricks[amount] = new Item();
				bricks[amount].x = i * BRICK_WIDTH + 102;
				bricks[amount].y = j * BRICK_HEIGHT +28;
				amount++;
				
			}
		}
		//gameThread = new Thread(this);
		//gameThread.start();
	}
	
	private  void logic() {
		if(!gameOver && !gameWin) {
			
		//System.out.println("hello");
		
		ball.x += ball.dx;
		
		for(int i = 0; i < amount; i ++ ) {
			if(new Rectangle(bricks[i].x, bricks[i].y, BRICK_WIDTH, BRICK_HEIGHT).intersects(
					new Rectangle(ball.x, ball.y, ball.width, ball.height))){
						ball.dx *= -1; 
						bricks[i].x = -100;
						score++;
					}
		}
		ball.y += ball.dy;
		for(int i = 0; i < amount; i ++ ) {
			if(new Rectangle(bricks[i].x, bricks[i].y, BRICK_WIDTH, BRICK_HEIGHT).intersects(
					new Rectangle(ball.x, ball.y, ball.width, ball.height))){
						ball.dy *= -1; 
						bricks[i].y = -100;
						score++;
					}
		}
		
		if(ball.x < 0 || ball.x > (WIDTH - ball.width)) {
			ball.dx *= - 1;
			ball.x = Math.max(0, Math.min(WIDTH - ball.width, ball.x));		
		}
		if(ball.y < 0 )
			ball.dy *= -1;
		ball.y = Math.max(0,  ball.y);
		
		if(ball.y > HEIGHT - ball.height)
			gameOver = true;
		
		if(score == amount)
			gameWin = true;
		
		if(new Rectangle(player.x, player.y, player.width, player.height).intersects(
				new Rectangle(ball.x, ball.y, ball.width, ball.height))){
					ball.dy = -Math.abs(ball.dy);
					ball.dx = rand.nextInt(5 - 2) + 2; // nextInt(max - min) + min
					ball.dx = rand.nextInt(2) == 1 ? ball.dx : - ball.dx;
				}
		}
	}
	private void initializeGame(int brickCount) {
        amount = brickCount;

        // Khởi tạo lại trạng thái trò chơi
      
       
        player.width = 120;
        player.height = 20;
        player.x = WIDTH / 2 - player.width / 2;
        player.y = HEIGHT - player.height - 50;
        player.dx = 18;
       
 

        ball.width = 26;
        ball.height = 26;
        ball.x = WIDTH / 2 - ball.width / 2;
        ball.y = HEIGHT / 2 - ball.height / 2 - 50;
        ball.dx = -2;
        ball.dy = 3;

        // Khởi tạo lại viên gạch
        for (int i = 0; i < amount; i++) {
            bricks[i] = new Item();
            bricks[i].x = (i % BRICK_X) * BRICK_WIDTH + 102;
            bricks[i].y = (i / BRICK_X) * BRICK_HEIGHT + 28;
        }

        // Thiết lập lại trạng thái game
        gameOver = false;
        gameWin = false;
        score = 0;
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!gameOver && !gameWin) {
                    if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                        player.dx = -Math.abs(player.dx);
                    }
                    if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                        player.dx = Math.abs(player.dx);
                    }
                    

                    if (player.x < 0) {
                        player.x = 0;
                       
                    }
                    if (player.x > WIDTH - player.width) {
                        player.x = WIDTH - player.width;
                        
                    }
                    player.x = Math.max(0, Math.min(WIDTH - player.width, player.x));
                }
            }
        });
        
    
        
          
        gameThread = new Thread(this);
        gameThread.start();
        this.requestFocus();
        
        
        
    }
	
   
	
	
	@Override
	public void run() {
		long time1 = System.nanoTime();
	    long time2;
	    double delta = 0.0;
	    double ticks = 60.0; // fps
	    double secs = 1e9 / ticks;

	    while (gameThread != null) {
	        time2 = System.nanoTime();
	        delta += (time2 - time1) / secs;
	        time1 = time2;

	        if (delta > 0) {
	            logic();
	            SwingUtilities.invokeLater(new Runnable() {
	                @Override
	                public void run() {
	                    repaint();
	                }
	            });
	            delta--;
	        }
	        try {
	        	Thread.sleep(55);
	        	}catch(InterruptedException e) {
	        		e.printStackTrace();
	        	}
	    }
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		g.setColor(Color.BLUE);
		g.fillRect(player.x, player.y, player.width, player.height);
		
		
		g.setColor(Color.RED);
		g.fillOval(ball.x, ball.y, ball.width, ball.height);
		
		
	
		
		for( int i = 0; i < amount ; i++) {
			g.setColor(Color.PINK);
			g.fillRect(bricks[i].x, bricks[i].y, BRICK_WIDTH, BRICK_HEIGHT);
			g.setColor(Color.BLACK);
			g.drawRect(bricks[i].x, bricks[i].y , BRICK_WIDTH + 1, BRICK_HEIGHT + 1);
			g.setColor(Color.BLACK);
			g.drawRect(bricks[i].x, bricks[i].y , BRICK_WIDTH + 2, BRICK_HEIGHT + 2);
		}
		
		g.setColor(Color.GREEN);
		g.setFont(new Font("Arial", Font.PLAIN, 20));
		g.drawString("ĐIỂM: " + score, 0, 32);
		if(gameOver) {
			g.setColor(Color.RED);
			g.setFont(new Font("Arial", Font.PLAIN, 60));
			g.drawString("Thất Bại ", WIDTH / 2 - 140, HEIGHT / 2);
		}
		if(gameWin) {
			g.setColor(Color.YELLOW);
			g.setFont(new Font("Arial", Font.PLAIN, 60));
			g.drawString("Chiến Thắng", WIDTH / 2 - 140, HEIGHT / 2);
		}
		g.dispose();
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
	
		if(!gameOver && !gameWin) {
			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
				player.dx = -Math.abs(player.dx);
				
			}
			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
				player.dx = Math.abs(player.dx);
				
			}
		player.x += player.dx;
			
			
			if( player.x < 0) {
				player.x = 0;
			}
			if(player.x > WIDTH - player.width) {
				player.x = WIDTH - player.width;
			}
			
			player.x = Math.max(0, Math.min(WIDTH - player.width, player.x));
		}
 }
	@Override
	public void keyReleased(KeyEvent e) {
		
		
	}
	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	
     public ControlWindow() {
    	 this.setLayout(new BorderLayout());
    	 this.setDoubleBuffered(true);
    	 this.setFocusable(true);
    	 this.setBackground(Color.GRAY);
    	 this.addKeyListener(this);
    	 
    	 JPanel buttonPanel = new JPanel(new FlowLayout());
    	 buttonPanel.setBackground(Color.GRAY);
    	
    	 startButton = new JButton("Bắt Đầu");
    	 startButton.addActionListener(new ActionListener() {
    		 @Override
    		 public void actionPerformed( ActionEvent e) {
    			 startGame();
    		 }
    	 });
    	 
    	 
    	 levelButton = new JButton("Mức Độ");
    	 levelButton.addActionListener(new ActionListener() {
    		 @Override
    		 public void actionPerformed( ActionEvent e) {
    			 level();
    		 }
    	 });
    	 
    	 
    	 helpButton = new JButton("Hướng dẫn");
    	 helpButton.addActionListener(new ActionListener() {
    		 @Override
    		 public void actionPerformed(ActionEvent e) {
    			 showInstructionsDialog();
    		 }
    	 });
    	
    	
    	
    	
    	 
    	 //them list chon do choi 
    	 difficultyComboBox = new JComboBox<>(new String[] {"dễ" , "trung bình", "khó"});
    	 difficultyComboBox.setSelectedIndex(0);// mac dinh la de
    	 
    	 
    	 buttonPanel.add(startButton);
    	 buttonPanel.add(difficultyComboBox);
    	 buttonPanel.add(levelButton);
    	 buttonPanel.add(helpButton);
    	 this.add(buttonPanel, BorderLayout.NORTH);
    	 this.setBackground(Color.GRAY);

    	 setup();
    	 gameThread = new  Thread(this);
    	 gameThread.start();
     }
     public enum Difficulty{
    	 EASY, MEDIUM, HARD;	
     }
     
     private void level() {
      String[] options = {"dễ", "trung bình", "khó"};
      int result =JOptionPane.showOptionDialog(this, " chọn mức độ " , " mức độ ",JOptionPane.DEFAULT_OPTION,JOptionPane.PLAIN_MESSAGE,null,options, options[0]);
      if( result >= 0) {
    	  difficultyComboBox.setSelectedIndex(result);
    	  startGame();
      }
       

      
     }
     
    

     private void startGame() {
         // Lấy mức độ khó từ JComboBox
         String selectedDifficulty = (String) difficultyComboBox.getSelectedItem();
         
        if(!gameStart) {
        	resetBallSpeed();
        	gameStart = true;
        }
         
  
         player.width = 120;
         player.height = 20;
         player.x = WIDTH / 2 - player.width / 2;
         player.y = HEIGHT - player.height - 50;
       

         ball.width = 26;
         ball.height = 26;
         ball.x = WIDTH / 2 - ball.width / 2;
         ball.y = HEIGHT / 2 - ball.height / 2 - 50;
         ball.dx = -2;
         ball.dy = 3;
         
        
         // Kiểm tra và khởi tạo trò chơi dựa trên mức độ chơi đã chọn
         switch(selectedDifficulty) {
              case "dễ":
              initializeGame(EASY_BRICK_COUNT);
            
               break;
              case "trung bình":
                  initializeGame(MEDIUM_BRICK_COUNT);
               
                  break;
              case "khó":
                  initializeGame(HARD_BRICK_COUNT);
                
                  break;
         }
       
         
  
 	
     }
     
     private void showInstructionsDialog() {
    	    //  Thực hiện nội dung hướng dẫn
    	    String instructions = "Hướng dẫn chơi game:\n\n" +
    	                          "1. Sử dụng các phím mũi tên trái/phải để di chuyển thanh trượt.\n" +
    	                          "2. Mục tiêu của bạn là phá hủy tất cả các viên gạch.\n" +
    	                          "3. Tránh để quả bóng rơi xuống dưới cùng.\n" +
    	                          "4. Nếu bạn phá hủy hết các viên gạch, bạn chiến thắng!\n" +
    	                          "5. Nếu quả bóng chạm đáy màn hình, bạn sẽ thua cuộc.\n"+
    	                          "Lưu Ý: nút bắt đầu, nút chọn mức độ chơi và nút hướng dẫn ở trên cùng màn hình game !";

    	    JOptionPane.showMessageDialog(this, instructions, "Hướng Dẫn Chơi", JOptionPane.INFORMATION_MESSAGE);
    	}

     
     private void resetBallSpeed() {
    	 ball.dx = -2;
    	 ball.dy = 2;
     }
}

public class BreakOut extends JFrame {
	
	private ControlWindow cw = new ControlWindow();
	public BreakOut() {
		this.add(cw);
		this.pack();
		this.setTitle("Breakout");
		this.setSize(ControlWindow.WIDTH, ControlWindow.HEIGHT);
		this.setLocationRelativeTo(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setVisible(true);
        cw.requestFocus();
	}
	
	public static void main(String[] args) {
		BreakOut breakout = new BreakOut();
	
	}
}

