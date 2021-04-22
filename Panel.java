import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Panel extends JPanel{
	private static final long serialVersionUID = 1L;
	public Timer tmDraw;//таймер отрисовки
	public Image flag,fon,win;//кратинки флажка, фона, и экрана победы
	public int[][] draw=new int[20][20];//поле дл€ отрисовки элементов
	public int endGame=0;//конец игры
	public int dx=0;//нужна дл€ расположение пол€ с элементами по середине 
	public Game gm=new Game();//класс игры(в нЄм хран€тс€ методы дл€ создани€ невидимого пол€ с минами и числами)
		public class Mouse1 implements MouseListener{ //класс дл€ определени€ нажатий мыши
			int mX,mY;
			public void mouseClicked(MouseEvent e) {
				mX=e.getX();//позици€ по x
				mY=e.getY();//позици€ по y
				System.out.println(mY);
				try {
					if(e.getButton()==1) {//если нажата лева€ кнопка мыши
						if(draw[(mX-dx)/20][(mY-50)/20]>0&&e.getClickCount()==2) {//если нажал дважды по 
							Scan((mX-dx)/20,(mY-50)/20);				//заполненной цифрой €чейки вызывает Scan
						}
						if(gm.pole[(mX-dx)/20][(mY-50)/20]>0&&draw[(mX-dx)/20][(mY-50)/20]<=-2) {
							draw[(mX-dx)/20][(mY-50)/20]=gm.pole[(mX-dx)/20][(mY-50)/20];//если попал на €чейку с цифрой
						}																//отрисовывает еЄ
						else if(gm.pole[(mX-dx)/20][(mY-50)/20]==0) {
							Scan((mX-dx)/20,(mY-50)/20);//если попал на 0 €чейку 
						}								//вызывает Scan
						else if(gm.pole[(mX-dx)/20][(mY-50)/20]==-1){
							if(draw[(mX-dx)/20][(mY-50)/20]!=-1)endGame=2;//если попал на мину - проигрыш
						}
					}
					if(e.getButton()==3&&e.getClickCount()==1) { //если нажата права€ кнопка мыши
						if(draw[(mX-dx)/20][(mY-50)/20]==-1)draw[(mX-dx)/20][(mY-50)/20]=-2;//снимает флажок 
						else if(!(draw[(mX-dx)/20][(mY-50)/20]>0))draw[(mX-dx)/20][(mY-50)/20]=-1;//ставит флажок, 
					}																			//если €чейка свободна
				}catch(Exception a) {}
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
	  	}
	public Panel() {
		  START();//делает поле "пустым"
		  addMouseListener(new Mouse1());
		  setFocusable(true);
		  tmDraw = new Timer(50, new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
				  repaint();//перерисовывает
				  new Thread(() -> {
					  CheckEndGame();//провер€ет на конец игры 
				  });  
			  }
		  });
		  tmDraw.start();
		  JButton Start= new JButton();//кнопка новой игры
		  Start.setText("Start");
		  Start.setSize(150, 50);
		  Start.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
			 	gm=new Game();
			 	START();
			 	endGame=0;
			 	tmDraw.start();
			 }
		  });
		  add(Start);
		  JButton Restart= new JButton();//кнопка рестарта.("гениальные" комментарии пошли)
		  Restart.setText("Restart");
		  Restart.setSize(150, 50);
		  Restart.addActionListener(new ActionListener() {
		  public void actionPerformed(ActionEvent e) {
			  	draw=new int[20][20];
			  	START();
			  	endGame=0;
			 	tmDraw.start();	
			 }
		  });
		  add(Restart);
		  try {
			flag=ImageIO.read(new File("flag.png"));//импорт картинок
			fon=ImageIO.read(new File("fon.jpg"));//можно поставить свои
			win=ImageIO.read(new File("winner.png"));
		  } 
		  catch (IOException e) {}
	}
	public void paintComponent(Graphics gr) {
		if(dx==0)dx=(this.getWidth()/2)-200;//часто использую эту формулу, поэтому и ввЄл дл€ неЄ переменную
		super.paintComponent(gr);
		((Graphics2D) gr).setStroke(new BasicStroke(2.0f));//выставление ширины линий  
		gr.drawImage(fon,0, 0, this.getWidth(),this.getHeight(),null );
		gr.setColor(Color.BLUE);
		gr.fillRect(dx,50,400,400);//задний фон дл€ пол€
		for(int x=0;x<20;x++) {
			for(int y=0;y<20;y++) {
				gr.setColor(Color.YELLOW);
				gr.drawRect(dx+x*20,50+y*20,20,20);//отрисовка границ €чеек
				if(draw[x][y]>0) {//если €чейка не пуста€ отрисовывае еЄ зелЄным и ставит число
					gr.setColor(Color.GREEN);
					gr.fillRect(dx+x*20,50+y*20,20,20);
					gr.setColor(Color.RED);
					gr.drawString(String.valueOf(draw[x][y]), dx+10+x*20,60+y*20);
				}
				else if(draw[x][y]==0) {//если 0 €чейка, рисует серый квадрат
					gr.setColor(Color.GRAY);
					gr.fillRect(dx+x*20,50+y*20,20,20);   
				}
				else if(draw[x][y]==-1){
					gr.drawImage(flag,dx+x*20,50+y*20,20,20,null);//если €чейка с номер -1, рисует флаг
				}
			}  
		}
		if(endGame==1) {
			gr.drawImage(win,100, 50, null);
		}
	}
	public void START() {//делает все €чейки "пустыми"(так как 0,-1 зан€ты, то стоит -2, но можно и меньше поставить)
		for(int a=0;a<20;a++) {
			for(int b=0;b<20;b++) {
				draw[a][b]=-2;
			}	
		}
	}
	public void Scan(int x, int y) {//метод дл€ нахождени€ чисел вокруг выбранной точки и раскрыти€ пустых €чеек
			for(int i=-1;i<=1;i++) {
				int a=x+i;
				for(int j=-1;j<=1;j++) {
					int b=y+j;
					if(Check(a,b)) {
						if(gm.pole[a][b]==-1&&draw[a][b]!=-1) {
							endGame=2;
						}
						else {
							if(gm.pole[a][b]>0) {
								if(draw[a][b]==-1)endGame=2;//если флаг стоит не на своЄм месте - проигрыш
								draw[a][b]=gm.pole[a][b];
							}
							if(gm.pole[a][b]==0&&draw[a][b]!=gm.pole[a][b]) {//если €чейка пуста, то отрисовывает €чейку  
								draw[a][b]=gm.pole[a][b];					 //и вызывает Scan в выбранной €чейке
								Scan(a,b);
							}
						}
					}
					
				}	
			}
	}
	public static boolean Check(int x, int y) {//проверка на выход за пол€
		if(x<0||x>19||y<0||y>19) {
			return false;
		}
		else return true;
	}
	public void CheckEndGame() {//проверка на конец игры
		boolean rez = true;//совпадает отрисованное поле с неотрисованным
		if(endGame==2) {//проигрыш
			  draw=gm.pole;
			  repaint();
			  tmDraw.stop();
		}
		for(int a=0;a<20;a++) {
			for(int b=0;b<20;b++) {
				if(draw[a][b]!=gm.pole[a][b])rez=false;//проверка на совпадение отрисованного пол€ с не отрисованным полем
			}
		}
		if(rez==true&&endGame!=2) {//выйгрыш
			endGame=1;
			repaint();
			tmDraw.stop();
		}
	}
}