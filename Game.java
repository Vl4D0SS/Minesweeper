
public class Game {
	public int[][] pole= new int[20][20];//���� � ������ � �������
	public Game() {
		CreateMines();
		for(int x=0;x<20;x++) {
			for(int y=0;y<20;y++) {
				if(pole[x][y]!=-1) pole[x][y]=Search(x,y);//�������� �� ����� ���� � ���� �� ����� ���� ���������� �����
			}	
		}
	}
	public void CreateMines() {//�������� ���
		int x,y;
		for(int kol=0;kol<80;) {
			x=(int)(Math.random()*20);
			y=(int)(Math.random()*20);
			if(Check(x,y)) {
				pole[x][y]=-1;
				kol++;
			}
		}
	}
	public boolean Check(int x, int y) {//�������� �� ����� �� ���� � ���������� ����
		if(x<0||x>19||y<0||y>19||pole[x][y]==-1) return false;
		else return true;
	}
	int number,a,b;
	public int Search(int x,int y) {//����� ��� ��������� �����(�����������) ��� ������ ������
		number=0;
		for(int i=-1;i<2;i++) {
			a=x+i;
			for(int j=-1;j<2;j++) {
				b=y+j;
				if(Panel.Check(a,b)&&pole[a][b]==-1)number++;
			}	
		}
		return number;
	}
}
