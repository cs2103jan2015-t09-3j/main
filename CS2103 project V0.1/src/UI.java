import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

@SuppressWarnings("serial")
public class UI extends JFrame {
	private static final String DEFAULT_STRING = "none";
	static Logic l = new Logic();
	static ArrayList<Tasks> list;
	static JLabel bg;
	static JLabel feedbacks;
	static JButton send_button;
	static JPanel input_panel;
	static JPanel bg_panel, feedback_panel, table_panel;
	static JTextField text_field = new JTextField(40);
	static String input = DEFAULT_STRING;
	static JTable table;
	static BufferedImage image;
	static final String[] columns = { "Task no.", "Task Description", "Start Date", "End Date" };
	static DefaultTableModel tableModel = new DefaultTableModel(columns, 0);

	public UI() {
		setTitle("test Program");
		setSize(700, 500);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		input_panel = new JPanel();
		bg_panel = new JPanel();
		send_button = new JButton("send");
		table = new JTable(tableModel) {
			{
				setOpaque(false);
				
				setDefaultRenderer(Object.class,
						new DefaultTableCellRenderer() {
							{
								setOpaque(false);
							}
						});
				
			}

			public boolean isCellEditable(int data, int columns) {
				return false;
			}

			public Component prepareRenderer(TableCellRenderer r, int data,
					int columns) {
				Component c = super.prepareRenderer(r, data, columns);

				if (data % 2 == 0) {
					c.setBackground(Color.white);
				} else {
					c.setBackground(Color.LIGHT_GRAY);
				}
				if (isCellSelected(data, columns)) {
					c.setBackground(Color.green);
				}
				return c;
			}

			protected void paintComponent(Graphics g) {
				g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
				super.paintComponent(g);
			}
		};

		table.setPreferredScrollableViewportSize(new Dimension(700, 100));
		table.setFillsViewportHeight(true);
		JScrollPane scrollbar = new JScrollPane(table);
		scrollbar.setMaximumSize(new Dimension(700, 500));
		
		

		feedback_panel = new JPanel();
		table_panel = new JPanel(new BorderLayout());
		table_panel.setLayout(new OverlayLayout(table_panel));
		table_panel.setOpaque(false);

		input_panel.setLayout(new FlowLayout(2));
		input_panel.add(text_field);
		input_panel.add(send_button);
		table_panel.add(scrollbar);

		add(input_panel, BorderLayout.SOUTH);
		add(feedback_panel, BorderLayout.NORTH);
		add(table_panel);

		revalidate();
		repaint();

	}

	public static void printFeedback(String text) {
		clearFeedback();
		feedbacks = new JLabel();
		feedback_panel.add(feedbacks);
		if(text.contains("edit ")){
			int index = Integer.parseInt(text.substring(text.indexOf(' ')+1, text.length()));
			text_field.setText("add "+list.get(index-1).detail+" - "+list.get(index-1).date);
			feedbacks.setText("Old task : "+ list.get(index-1).detail + "       Old Date : "+list.get(index-1).date);
		}
		else if(text.contains("search ")){
			String keyword = text.substring(text.indexOf(' ')+1, text.length());
			printList(list, keyword);
			feedbacks.setText("Search Results: ");
		}
		else{
			feedbacks.setText(text);
		}

	}

	public static void main(String[] args) throws IOException {
		image = ImageIO.read(new File("background.png"));
		image = makeTransparent();

		UI GUI = new UI();
		list = l.import_From_File(list);
		printList(list,"");
		
		takeCommand(GUI);

	}

	private static BufferedImage makeTransparent() {
		BufferedImage tmpImg = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = (Graphics2D) tmpImg.getGraphics();
		g2d.setComposite(AlphaComposite.SrcOver.derive(0.3f));
		// set the transparency level in range 0.0f - 1.0f
		g2d.drawImage(image, 0, 0, null);
		return tmpImg;
	}

	private static void takeCommand(UI GUI) {
		String temp = "default";

		while (temp != input) {

			// when user type enter
			text_field.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					input = text_field.getText();
					text_field.setText("");
					String feedback = DEFAULT_STRING;
					feedback = l.executeCommand(input, list);
					printList(list,"");
					printFeedback(feedback);
				}

			});
			// When button is pressed
			send_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					input = text_field.getText();
					text_field.setText("");
					String feedback = new String(DEFAULT_STRING);
					feedback = l.executeCommand(input, list);
					printList(list,"");
					printFeedback(feedback);
				}
			});
			temp = input;
		}
	}

	protected static void printList(ArrayList<Tasks> list, String keyword) {
		clearTable();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).detail.contains(keyword) || list.get(i).date.contains(keyword)){
				int taskNum = i + 1;
				String task = list.get(i).detail;
				String date = list.get(i).date;
				Object[] data = { taskNum, task, date };
				tableModel.addRow(data);
			}
		}
	}

	private static void clearTable() {
		int size = tableModel.getRowCount();
		for (int i = 0; i < size; i++) {
			tableModel.removeRow(0);
		}

	}

	protected static void clearFeedback() {

		feedback_panel.removeAll();
		feedback_panel.revalidate();
		feedback_panel.repaint();

	}
}
