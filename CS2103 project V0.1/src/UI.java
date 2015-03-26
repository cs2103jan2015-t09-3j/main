import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class UI extends JFrame {
	private static Scanner sc = new Scanner(System.in);
	static Storage s = new Storage();
	static Logic l = new Logic();
	static ArrayList<Cone_Organizer> list = new ArrayList<Cone_Organizer>();
	static ArrayList<Cone_Organizer> tempList = new ArrayList<Cone_Organizer>();
	static Cone_Organizer cmd = new Cone_Organizer();
	static JLabel bg;
	static JLabel msg;
	static JButton jb;
	JPanel jp;
	static JPanel jp2, jp3,left_col, right_col;
	JTextField jt = new JTextField(30);
	static String input = "deafult";
	static int COUNT=0;
	static int ROW_NUM = 4;


	public UI() {
		setTitle("test Program");
		setSize(700, 500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);
		jp = new JPanel();
		jp2 = new JPanel();
		jb = new JButton("send");
		
		jp3 = new JPanel();
		left_col = new JPanel();
		right_col = new JPanel();

		bg = new JLabel();
		bg.setIcon(new ImageIcon("background.png"));
		jp2.add(bg);

		jp.setLayout(new FlowLayout(2));
		jp.add(jt);
		jp.add(jb);
		jp3.setLayout(new FlowLayout(1));
		left_col.setLayout(new GridLayout(ROW_NUM,1));
		right_col.setLayout(new GridLayout(ROW_NUM,1));
		jp3.add(left_col);
		jp3.add(right_col);
		
		add(jp, BorderLayout.SOUTH);
		add(jp3, BorderLayout.NORTH);
		add(jp2);

		validate();

	}

	public void print(String text) {
		msg = new JLabel();
		if(COUNT<ROW_NUM){
			left_col.add(msg);
			
		}
		else{
			right_col.add(msg);
			
		}
		msg.setText(text);
		COUNT++;

	}

	public static void main(String[] args) {
		list = l.import_From_File(list);
		tempList.addAll(list);
		UI GUI = new UI();
		GUI.takeCommand(GUI);
		
		

	}

	public String takeCommand(UI GUI) {
		String temp = "LOL";
		

		while (!input.equals("exit") && temp != input) {
			
			//when user type enter

			jt.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					
					cmd = new Cone_Organizer();
					input = jt.getText();
					cmd.command = input;

					jt.setText("");
					
					
					
					l.executeCommand(cmd, list, tempList, GUI);
					
					

				}
			});
			//When button is pressed
			jb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {

					cmd = new Cone_Organizer();
					input = jt.getText();
					cmd.command = input;
					jt.setText("");
					
					l.executeCommand(cmd, list, tempList, GUI);
					
					

				}

			});
			

			temp = input;
			
		}
		return input;
		

	}



	protected void clearBuffer() {
		
		
		left_col.removeAll();
		right_col.removeAll();
		COUNT=0;
		
		
		
	}
}
