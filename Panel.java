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
	public Timer tmDraw;//������ ���������
	public Image flag,fon,win;//�������� ������, ����, � ������ ������
	public int[][] draw=new int[20][20];//���� ��� ��������� ���������
	public int endGame=0;//����� ����
	public int dx=0;//����� ��� ������������ ���� � ���������� �� �������� 
	public Game gm=new Game();//����� ����(� �� �������� ������ ��� �������� ���������� ���� � ������ � �������)
		public class Mouse1 implements MouseListener{ //����� ��� ����������� ������� ����
			int mX,mY;
			public void mouseClicked(MouseEvent e) {
				mX=e.getX();//������� �� x
				mY=e.getY();//������� �� y
				System.out.println(mY);
				try {
					if(e.getButton()==1) {//���� ������ ����� ������ ����
						if(draw[(mX-dx)/20][(mY-50)/20]>0&&e.getClickCount()==2) {//���� ����� ������ �� 
							Scan((mX-dx)/20,(mY-50)/20);				//����������� ������ ������ �������� Scan
						}
						if(gm.pole[(mX-dx)/20][(mY-50)/20]>0&&draw[(mX-dx)/20][(mY-50)/20]<=-2) {
							draw[(mX-dx)/20][(mY-50)/20]=gm.pole[(mX-dx)/20][(mY-50)/20];//���� ����� �� ������ � ������
						}																//������������ �
						else if(gm.pole[(mX-dx)/20][(mY-50)/20]==0) {
							Scan((mX-dx)/20,(mY-50)/20);//���� ����� �� 0 ������ 
						}								//�������� Scan
						else if(gm.pole[(mX-dx)/20][(mY-50)/20]==-1){
							if(draw[(mX-dx)/20][(mY-50)/20]!=-1)endGame=2;//���� ����� �� ���� - ��������
						}
					}
					if(e.getButton()==3&&e.getClickCount()==1) { //���� ������ ������ ������ ����
						if(draw[(mX-dx)/20][(mY-50)/20]==-1)draw[(mX-dx)/20][(mY-50)/20]=-2;//������� ������ 
						else if(!(draw[(mX-dx)/20][(mY-50)/20]>0))draw[(mX-dx)/20][(mY-50)/20]=-1;//������ ������, 
					}																			//���� ������ ��������
				}catch(Exception a) {}
			}
			public void mousePressed(MouseEvent e) {}
			public void mouseReleased(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseExited(MouseEvent e) {}
	  	}
	public Panel() {
		  START();//������ ���� "������"
		  addMouseListener(new Mouse1());
		  setFocusable(true);
		  tmDraw = new Timer(50, new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
				  repaint();//��������������
				  new Thread(() -> {
					  CheckEndGame();//��������� �� ����� ���� 
				  });  
			  }
		  });
		  tmDraw.start();
		  JButton Start= new JButton();//������ ����� ����
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
		  JButton Restart= new JButton();//������ ��������.("����������" ����������� �����)
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
			flag=ImageIO.read(new File("flag.png"));//������ ��������
			fon=ImageIO.read(new File("fon.jpg"));//����� ��������� ����
			win=ImageIO.read(new File("winner.png"));
		  } 
		  catch (IOException e) {}
	}
	public void paintComponent(Graphics gr) {
		if(dx==0)dx=(this.getWidth()/2)-200;//����� ��������� ��� �������, ������� � ��� ��� �� ����������
		super.paintComponent(gr);
		((Graphics2D) gr).setStroke(new BasicStroke(2.0f));//����������� ������ �����  
		gr.drawImage(fon,0, 0, this.getWidth(),this.getHeight(),null );
		gr.setColor(Color.BLUE);
		gr.fillRect(dx,50,400,400);//������ ��� ��� ����
		for(int x=0;x<20;x++) {
			for(int y=0;y<20;y++) {
				gr.setColor(Color.YELLOW);
				gr.drawRect(dx+x*20,50+y*20,20,20);//��������� ������ �����
				if(draw[x][y]>0) {//���� ������ �� ������ ����������� � ������ � ������ �����
					gr.setColor(Color.GREEN);
					gr.fillRect(dx+x*20,50+y*20,20,20);
					gr.setColor(Color.RED);
					gr.drawString(String.valueOf(draw[x][y]), dx+10+x*20,60+y*20);
				}
				else if(draw[x][y]==0) {//���� 0 ������, ������ ����� �������
					gr.setColor(Color.GRAY);
					gr.fillRect(dx+x*20,50+y*20,20,20);   
				}
				else if(draw[x][y]==-1){
					gr.drawImage(flag,dx+x*20,50+y*20,20,20,null);//���� ������ � ����� -1, ������ ����
				}
			}  
		}
		if(endGame==1) {
			gr.drawImage(win,100, 50, null);
		}
	}
	public void START() {//������ ��� ������ "�������"(��� ��� 0,-1 ������, �� ����� -2, �� ����� � ������ ���������)
		for(int a=0;a<20;a++) {
			for(int b=0;b<20;b++) {
				draw[a][b]=-2;
			}	
		}
	}
	public void Scan(int x, int y) {//����� ��� ���������� ����� ������ ��������� ����� � ��������� ������ �����
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
								if(draw[a][b]==-1)endGame=2;//���� ���� ����� �� �� ���� ����� - ��������
								draw[a][b]=gm.pole[a][b];
							}
							if(gm.pole[a][b]==0&&draw[a][b]!=gm.pole[a][b]) {//���� ������ �����, �� ������������ ������  
								draw[a][b]=gm.pole[a][b];					 //� �������� Scan � ��������� ������
								Scan(a,b);
							}
						}
					}
					
				}	
			}
	}
	public static boolean Check(int x, int y) {//�������� �� ����� �� ����
		if(x<0||x>19||y<0||y>19) {
			return false;
		}
		else return true;
	}
	public void CheckEndGame() {//�������� �� ����� ����
		boolean rez = true;//��������� ������������ ���� � ��������������
		if(endGame==2) {//��������
			  draw=gm.pole;
			  repaint();
			  tmDraw.stop();
		}
		for(int a=0;a<20;a++) {
			for(int b=0;b<20;b++) {
				if(draw[a][b]!=gm.pole[a][b])rez=false;//�������� �� ���������� ������������� ���� � �� ������������ �����
			}
		}
		if(rez==true&&endGame!=2) {//�������
			endGame=1;
			repaint();
			tmDraw.stop();
		}
	}
}