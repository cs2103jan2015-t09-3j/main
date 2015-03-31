import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class UI extends JFrame {
	private static final String DEFAULT_STRING = "none";
	static Logic l = new Logic();
	static ArrayList<Tasks> list;
	static JLabel bg;
	static JLabel feedbacks;
	static JButton command_reference_button,send_button;
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
		command_reference_button = new JButton("Reference");
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
		
		String name = tableModel.getColumnName(0);
		Font f = table.getFont();
		FontMetrics fm = table.getFontMetrics(f);
		int width = fm.stringWidth(name);
		table.getColumnModel().getColumn(0).setPreferredWidth(width);

		table.setPreferredScrollableViewportSize(new Dimension(700, 100));
		table.setFillsViewportHeight(true);
		JScrollPane scrollbar = new JScrollPane(table);
		scrollbar.setMaximumSize(new Dimension(700, 500));
		
		

		feedback_panel = new JPanel();
		table_panel = new JPanel(new BorderLayout());
		table_panel.setLayout(new OverlayLayout(table_panel));
		table_panel.setOpaque(false);

		input_panel.setLayout(new FlowLayout(2));
		input_panel.add(command_reference_button);
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
			text_field.setText("add "+list.get(index-1).detail+" - from "+list.get(index-1).startDate+" to "+list.get(index-1).endDate);
			feedbacks.setText("Old task : "+ list.get(index-1).detail + "       Old Date : "+list.get(index-1).startDate+" ~ "+list.get(index-1).endDate);
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
					String feedback = DEFAULT_STRING;
					feedback = l.executeCommand(input, list);
					printList(list,"");
					printFeedback(feedback);
					
				}
			});
			command_reference_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {					
					final JFrame reference=new JFrame("Command Reference");
					reference.setVisible(true);
					reference.setSize(700, 500);
					reference.setLayout(new FlowLayout(3));
					final JPanel reference_panel = new JPanel();
					reference_panel.setLayout(new GridLayout(20,1));
					ArrayList<String> text = new ArrayList<String>(); 
					l.import_instruction(text);
					for(int i=0; i<text.size(); i++){
						
							JLabel content = new JLabel();
							content.setText(text.get(i));
							reference_panel.add(content);
							
						
					}
					
					reference.add(reference_panel);
					reference.revalidate();
					reference.repaint();
					

				}
			});
			temp = input;
		}
	}

	protected static void printList(ArrayList<Tasks> list, String keyword) {
		clearTable();
		for (int i = 0; i < list.size(); i++) {
			if(list.get(i).detail.contains(keyword) || list.get(i).startDate.contains(keyword) || list.get(i).endDate.contains(keyword)){
				int taskNum = i + 1;
				String task = list.get(i).detail;
				String startDate = list.get(i).startDate;
				String endDate = list.get(i).endDate;
				Object[] data = { taskNum, task, startDate, endDate };
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
