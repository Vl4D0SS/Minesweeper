import java.awt.Container;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Window extends JFrame {
	private static final long serialVersionUID = 1L;
	public int w,h;
	public Window() {
		Dimension dm = Toolkit.getDefaultToolkit().getScreenSize();	//��������� �������� ������
		w = dm.width;
		h = dm.height;	
		Panel panel = new Panel();
		Container cont = getContentPane();
		cont.add(panel);
		setTitle("����");
		setBounds(w/2-450, h/2-300, 900, 600);//����� ������� ���� �� �������� ����
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setVisible(true);
	}
}
