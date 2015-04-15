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
import java.util.Date;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

@SuppressWarnings("serial")
public class UI extends JFrame {
	private static final String DEFAULT_STRING = "none";
	static Logic l = new Logic();
	static ArrayList<Task> list;
	static JMenuBar menubar = new JMenuBar();
	static JMenu display;
	static JMenuItem today, oneweek, onemonth, all, floating, completed,
			recurring;
	static JLabel bg;
	static JLabel feedbacks;
	static JButton help_button, send_button;
	static JPanel input_panel;
	static JPanel bg_panel, feedback_panel, table_panel;
	static JTextField text_field = new JTextField(40);
	static String input = DEFAULT_STRING;
	static JTable table;
	static BufferedImage image;
	static final String[] columns = { "Task", "Task Description", "Start Date",
			"End Date" };
	protected static String print_what = "all";
	static DefaultTableModel tableModel = new DefaultTableModel(columns, 0);
	static String bgfile = "background.png";

	/**
	 * This method is basic constructor for the user interface of COne-Organizer
	 * 
	 * 
	 * 
	 */
	public UI() {
		setTitle("Cone Organizer V0.5");
		setSize(800, 700);
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setResizable(false);

		input_panel = new JPanel();
		bg_panel = new JPanel();
		help_button = new JButton("Help");
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
			

			protected void paintComponent(Graphics g) {
				g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
				super.paintComponent(g);
			}
		};
		table.setShowGrid(false);
		// table.setFont(new Font("Serif", Font.PLAIN, 17));
		table.setRowHeight(30);
		// table.setDefaultRenderer(String.class, new edittablerenderer());
		final TableColumnModel columnModel = table.getColumnModel();
		columnModel.getColumn(0).setMaxWidth(50);
		columnModel.getColumn(0).setMinWidth(49);
		columnModel.getColumn(1).setMinWidth(340);
		columnModel.getColumn(2).setMinWidth(130);
		columnModel.getColumn(3).setMinWidth(130);

		String name = tableModel.getColumnName(0);
		Font f = table.getFont();
		FontMetrics fm = table.getFontMetrics(f);
		int width = fm.stringWidth(name);
		table.getColumnModel().getColumn(0).setPreferredWidth(width);

		table.setPreferredScrollableViewportSize(new Dimension(700, 100));
		table.setFillsViewportHeight(true);
		JScrollPane scrollbar = new JScrollPane(table);
		scrollbar.setMaximumSize(new Dimension(800, 700));

		feedback_panel = new JPanel();
		table_panel = new JPanel(new BorderLayout());
		table_panel.setLayout(new OverlayLayout(table_panel));
		table_panel.setOpaque(false);

		input_panel.setLayout(new FlowLayout(2));
		input_panel.add(help_button);
		input_panel.add(text_field);
		input_panel.add(send_button);
		table_panel.add(scrollbar);

		add(input_panel, BorderLayout.SOUTH);
		add(feedback_panel, BorderLayout.NORTH);
		add(table_panel);

		setJMenuBar(menubar);
		display = new JMenu("display");
		menubar.add(display);
		today = new JMenuItem("Today");
		oneweek = new JMenuItem("In 1 week");
		onemonth = new JMenuItem("In 1 month");
		all = new JMenuItem("All tasks");
		floating = new JMenuItem("Floating tasks");
		completed = new JMenuItem("Completed tasks");
		recurring = new JMenuItem("Recurring tasks");
		display.add(today);
		display.addSeparator();
		display.add(oneweek);
		display.addSeparator();
		display.add(onemonth);
		display.addSeparator();
		display.add(all);
		display.addSeparator();
		display.add(floating);
		display.add(completed);
		display.add(recurring);

		revalidate();
		repaint();

	}

	/**
	 * This method will process the feedback given by the logic or other methods
	 * in UI for printing, and taking necessary actions
	 * 
	 * @param text
	 *            The feedback to by processed
	 * @param GUI 
	 * 
	 */

	public static void processFeedback(String text, UI GUI) {
		assert text!=null;
		clearFeedback();
		feedbacks = new JLabel();
		feedback_panel.add(feedbacks);
		if (text.contains("edit recur ")) {
			int index = Integer.parseInt(text.substring(
					text.lastIndexOf(' ') + 1, text.length()));

			text_field.setText("add " + list.get(index - 1).detail);
			list.remove(index - 1);
			feedbacks.setText("editing recurring task");
		} else if (text.contains("edit ")) {
			int index = Integer.parseInt(text.substring(text.indexOf(' ') + 1,
					text.length()));
			text_field.setText("add " + list.get(index - 1).detail + " - from "
					+ list.get(index - 1).startDate + " to "
					+ list.get(index - 1).endDate);
			feedbacks.setText("Old task : " + list.get(index - 1).detail
					+ "       Old Date : " + list.get(index - 1).startDate
					+ " ~ " + list.get(index - 1).endDate);
		} else if (text.contains("search ")) {
			String keyword = text.substring(text.indexOf(' ') + 1,
					text.length());
			printWhat(print_what, list, keyword);
			feedbacks.setText("Search Results: ");
		} else if (text.contains("display")) {
			int index = text.indexOf(" ");
			print_what = text.substring(index + 1);
			printWhat(print_what, list, "");

		} else if (text.equals("help")) {
			summonHelpScreen();
		} else if (text.contains("background__ ")) {
			bgfile = text.substring(text.indexOf(' ') + 1, text.length());
			
			feedbacks.setText("background picture has been changed to "
					+ bgfile);
			try {
				image = ImageIO.read(new File(bgfile));
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			image = makeTransparent();
			GUI.revalidate();
			GUI.repaint();
			
			
		}

		else {
			feedbacks.setText(text);
		}

	}

	/**
	 * This method will pop the new java window that contains the user
	 * instructions for each and every commands.
	 * 
	 *
	 */

	private static void summonHelpScreen() {
		final JFrame reference = new JFrame("User Help");
		reference.setVisible(true);
		reference.setSize(700, 800);
		reference.setLayout(new FlowLayout(3));
		final JPanel reference_panel = new JPanel();
		reference_panel.setLayout(new GridLayout(90, 1));
		JScrollPane scrollFrame = new JScrollPane(reference_panel);
		reference_panel.setAutoscrolls(true);
		scrollFrame.setPreferredSize(new Dimension(680, 800));

		// reference.pack();
		ArrayList<String> texts = new ArrayList<String>();
		l.import_instruction(texts);
		for (int i = 0; i < texts.size(); i++) {

			JLabel content = new JLabel();
			content.setText(texts.get(i));
			reference_panel.add(content);

		}

		reference.add(scrollFrame, reference_panel);
		reference.revalidate();
		reference.repaint();
	}

	/**
	 * The main method where the program starts and initiate necessary
	 * functions.
	 * 
	 * 
	 */

	public static void main(String[] args) throws IOException {

		image = ImageIO.read(new File(bgfile));
		image = makeTransparent();
	

		UI GUI = new UI();
		list = l.import_From_File(list);
		processFeedback("Displaying All Task:", GUI);
		printWhat(print_what, list, "");

		takeInputs(GUI);

	}

	/**
	 * This method will draw a transparent imgae onto the jframe. Transparency
	 * is set to 40% by default
	 */

	private static BufferedImage makeTransparent() {
		assert image != null;
		BufferedImage tmpImg = new BufferedImage(image.getWidth(),
				image.getHeight(), BufferedImage.TYPE_INT_ARGB);

		Graphics2D g2d = (Graphics2D) tmpImg.getGraphics();
		g2d.setComposite(AlphaComposite.SrcOver.derive(0.4f));
		// set the transparency level in range 0.0f - 1.0f
		g2d.drawImage(image, 0, 0, null);
		
		return tmpImg;
	}

	/**
	 * This method is responsible for processing the user-triggered
	 * actions/inputs such as typing in text field or pressing buttons, clicking
	 * on menu
	 * 
	 * @param GUI
	 *            The main user interface which is going to be affected by the
	 *            triggered events
	 *
	 */

	public static void takeInputs(UI GUI) {
		String temp = "default";

		while (temp != input) {

			// when user type enter
			text_field.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					input = text_field.getText();
					text_field.setText("");
					String feedback = DEFAULT_STRING;
					feedback = l.executeCommand(input, list);
					printWhat(print_what, list, "");
					processFeedback(feedback, GUI);
				}

			});

			// When menu is clicked
			today.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					print_what = "today";
					printWhat(print_what, list, "");

				}
			});
			oneweek.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					print_what = "week";
					printWhat(print_what, list, "");

				}
			});
			onemonth.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					print_what = "month";
					printWhat(print_what, list, "");

				}
			});
			all.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					print_what = "all";
					printWhat(print_what, list, "");

				}
			});
			floating.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					print_what = "floating";
					printWhat(print_what, list, "");

				}
			});
			completed.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					print_what = "completed";
					printWhat(print_what, list, "");

				}
			});
			recurring.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					print_what = "recurring";
					printWhat(print_what, list, "");

				}
			});
			// When button is pressed
			send_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					input = text_field.getText();
					text_field.setText("");
					String feedback = DEFAULT_STRING;
					feedback = l.executeCommand(input, list);
					printWhat(print_what, list, "");
					processFeedback(feedback, GUI);

				}
			});
			help_button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					summonHelpScreen();

				}
			});
			temp = input;
		}
	}

	/**
	 * This method will decide on what items to be displayed in the table and
	 * call the appropriate method.
	 * 
	 * @param printWhat
	 *            This string contains the information on which display method
	 *            to call
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 * @param keyword
	 *            The printing method will print out the items on the list that
	 *            contains this keyword. "" by default
	 * 
	 *
	 */

	protected static void printWhat(String printWhat, ArrayList<Task> list,
			String keyword) {

		switch (printWhat) {
		case "week": {
			printList_weeks(list, keyword);
			break;
		}
		case "today": {
			printList_today(list, keyword);
			break;
		}
		case "month": {
			printList_month(list, keyword);
			break;
		}
		case "all": {
			printList_entire(list, keyword);
			break;
		}
		case "floating": {
			printList_floating(list, keyword);
			break;
		}
		case "completed": {
			printList_completed(list, keyword);
			break;
		}
		case "recurring": {
			printList_recurring(list, keyword);
			break;
		}
		default: {
			if (printWhat.equals(DEFAULT_STRING)) {
				printList_entire(list, keyword);
				break;
			} else {
				printList_custom(list, printWhat, keyword);
				break;
			}
		}
		}

	}

	/**
	 * This method will print the items on the task list onto table, according
	 * to the custom input typed by user.
	 * 
	 * @param printWhat
	 *            This string contains the information on which display method
	 *            to call
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 * @param keyword
	 *            The printing method will print out the items on the list that
	 *            contains this keyword. "" by default
	 * 
	 *
	 */

	private static void printList_custom(ArrayList<Task> list,
			String printWhat, String keyword) {
		processFeedback("Displaying tasks in " + printWhat, null);
		clearTable();
		Date end = l.getDate(printWhat + " after today 23:59");
		Date today = l.getDate("00:00 today");
		int row_count = 0;
		for (int i = 0; i < list.size(); i++) {
			String task_date = list.get(i).endDate;

			if (!task_date.equals(DEFAULT_STRING)) {

				Date taskdate = l.getDate(task_date);
				if (taskdate.before(end) && taskdate.after(today)) {
					if (list.get(i).detail.contains(keyword)
							|| list.get(i).startDate.contains(keyword)
							|| list.get(i).endDate.contains(keyword)) {
						int taskNum = i + 1;
						String task = list.get(i).detail;
						String startDate = list.get(i).startDate;
						if (startDate.equals(DEFAULT_STRING)) {
							startDate = "";
						}
						String endDate = list.get(i).endDate;
						if (endDate.equals(DEFAULT_STRING)) {
							endDate = "";
						}
						checkCompleted(row_count, taskNum, task, startDate,
								endDate);
						row_count++;
					}
				}
			}
		}

	}

	/**
	 * This method will print all the recurring tasks in the list
	 * 
	 *
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 * @param keyword
	 *            The printing method will print out the items on the list that
	 *            contains this keyword. "" by default
	 * 
	 *
	 */
	private static void printList_recurring(ArrayList<Task> list, String keyword) {
		processFeedback("Displaying all recurring tasks", null);
		clearTable();
		int row_count = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).recurring_interval != 0) {
				if (list.get(i).detail.contains(keyword)
						|| list.get(i).startDate.contains(keyword)
						|| list.get(i).endDate.contains(keyword)) {
					int taskNum = i + 1;
					String task = list.get(i).detail;

					String startDate = list.get(i).startDate;
					if (startDate.equals(DEFAULT_STRING)) {
						startDate = "";
					}
					String endDate = list.get(i).endDate;
					if (endDate.equals(DEFAULT_STRING)) {
						endDate = "";
					}
					// collapseRecurring(list);
					checkCompleted(row_count, taskNum, task, startDate, endDate);
					row_count++;
				}
			}
		}

	}

	/**
	 * This method will print out all the tasks marked completed by user.
	 * 
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 * @param keyword
	 *            The printing method will print out the items on the list that
	 *            contains this keyword. "" by default
	 * 
	 *
	 */

	private static void printList_completed(ArrayList<Task> list, String keyword) {
		processFeedback("Displaying all completed tasks", null);
		clearTable();
		int row_count = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).detail.contains("(completed)")) {
				if (list.get(i).detail.contains(keyword)
						|| list.get(i).startDate.contains(keyword)
						|| list.get(i).endDate.contains(keyword)) {
					int taskNum = i + 1;
					String task = list.get(i).detail;

					String startDate = list.get(i).startDate;
					if (startDate.equals(DEFAULT_STRING)) {
						startDate = "";
					}
					String endDate = list.get(i).endDate;
					if (endDate.equals(DEFAULT_STRING)) {
						endDate = "";
					}
					checkCompleted(row_count, taskNum, task, startDate, endDate);
					row_count++;
				}
			}
		}

	}

	/**
	 * This method will print out all the floating tasks with no specific
	 * deadline
	 * 
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 * @param keyword
	 *            The printing method will print out the items on the list that
	 *            contains this keyword. "" by default
	 * 
	 *
	 */
	private static void printList_floating(ArrayList<Task> list, String keyword) {
		processFeedback("Displaying all floating tasks", null);
		clearTable();
		int row_count = 0;
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).endDate.equals(DEFAULT_STRING)) {
				if (list.get(i).detail.contains(keyword)
						|| list.get(i).startDate.contains(keyword)
						|| list.get(i).endDate.contains(keyword)) {
					int taskNum = i + 1;
					String task = list.get(i).detail;

					String startDate = list.get(i).startDate;
					if (startDate.equals(DEFAULT_STRING)) {
						startDate = "";
					}
					String endDate = list.get(i).endDate;
					if (endDate.equals(DEFAULT_STRING)) {
						endDate = "";
					}
					checkCompleted(row_count, taskNum, task, startDate, endDate);
					row_count++;
				}
			}
		}

	}

	/**
	 * This method will print out all the tasks that is due today
	 * 
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 * @param keyword
	 *            The printing method will print out the items on the list that
	 *            contains this keyword. "" by default
	 * 
	 *
	 */
	private static void printList_today(ArrayList<Task> list, String keyword) {
		processFeedback("Displaying today's tasks", null);
		clearTable();
		Date today_start = l.getDate("00:00 today");
		Date today_end = l.getDate("23:59 today");
		int row_count = 0;

		for (int i = 0; i < list.size(); i++) {
			String task_date = list.get(i).endDate;

			if (!task_date.equals(DEFAULT_STRING)) {

				Date taskdate = l.getDate(task_date);
				if (taskdate.after(today_start) && taskdate.before(today_end)) {
					if (list.get(i).detail.contains(keyword)
							|| list.get(i).startDate.contains(keyword)
							|| list.get(i).endDate.contains(keyword)) {
						int taskNum = i + 1;
						String task = list.get(i).detail;
						String startDate = list.get(i).startDate;
						if (startDate.equals(DEFAULT_STRING)) {
							startDate = "";
						}
						String endDate = list.get(i).endDate;
						if (endDate.equals(DEFAULT_STRING)) {
							endDate = "";
						}
						checkCompleted(row_count, taskNum, task, startDate,
								endDate);
						row_count++;
					}
				}
			}
		}

	}

	/**
	 * This method will print out all the tasks due in one week.
	 * 
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 * @param keyword
	 *            The printing method will print out the items on the list that
	 *            contains this keyword. "" by default
	 * 
	 *
	 */
	private static void printList_weeks(ArrayList<Task> list, String keyword) {
		processFeedback("Displaying this week's tasks", null);
		clearTable();
		Date next_week = l.getDate("23:59 in 1 week");
		Date today = l.getDate("00:00 today");
		int row_count = 0;
		for (int i = 0; i < list.size(); i++) {
			String task_date = list.get(i).endDate;

			if (!task_date.equals(DEFAULT_STRING)) {

				Date taskdate = l.getDate(task_date);
				if (taskdate.before(next_week) && taskdate.after(today)) {
					if (list.get(i).detail.contains(keyword)
							|| list.get(i).startDate.contains(keyword)
							|| list.get(i).endDate.contains(keyword)) {
						int taskNum = i + 1;
						String task = list.get(i).detail;
						String startDate = list.get(i).startDate;
						if (startDate.equals(DEFAULT_STRING)) {
							startDate = "";
						}
						String endDate = list.get(i).endDate;
						if (endDate.equals(DEFAULT_STRING)) {
							endDate = "";
						}
						checkCompleted(row_count, taskNum, task, startDate,
								endDate);
						row_count++;
					}
				}
			}
		}

	}

	/**
	 * This method will print out all the tasks due one month from today.
	 * 
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 * @param keyword
	 *            The printing method will print out the items on the list that
	 *            contains this keyword. "" by default
	 * 
	 *
	 */
	private static void printList_month(ArrayList<Task> list, String keyword) {
		processFeedback("Displaying this month's tasks", null);
		clearTable();
		Date next_month = l.getDate("23:59 in 1 month");
		Date today = l.getDate("00:00 today");
		int row_count = 0;
		for (int i = 0; i < list.size(); i++) {
			String task_date = list.get(i).endDate;

			if (!task_date.equals(DEFAULT_STRING)) {

				Date taskdate = l.getDate(task_date);
				if (taskdate.before(next_month) && taskdate.after(today)) {
					if (list.get(i).detail.contains(keyword)
							|| list.get(i).startDate.contains(keyword)
							|| list.get(i).endDate.contains(keyword)) {
						int taskNum = i + 1;
						String task = list.get(i).detail;
						String startDate = list.get(i).startDate;
						if (startDate.equals(DEFAULT_STRING)) {
							startDate = "";
						}
						String endDate = list.get(i).endDate;
						if (endDate.equals(DEFAULT_STRING)) {
							endDate = "";
						}
						checkCompleted(row_count, taskNum, task, startDate,
								endDate);
						row_count++;
					}
				}
			}
		}

	}

	/**
	 * This method checks if the item in the list is marked completed by the
	 * user.
	 * 
	 * 
	 * @param index
	 *            This is the position of item in the list to be checked.
	 * @param taskNum
	 *            This is the number of the task that is to be displayed in the
	 *            table
	 * 
	 * @param startDate
	 *            This is the startDate of the task that is to be added into the
	 *            table
	 * @param endDate
	 *            This is the finishing date of the task that is to be added
	 *            into the table
	 * 
	 *
	 */
	private static void checkCompleted(int index, int taskNum, String task,
			String startDate, String endDate) {
		if (task.contains("(completed)")) {
			Object[] data = { taskNum, task, startDate, endDate };

			tableModel.addRow(data);

			paintTable(index, "bold");

		} else {
			Object[] data = { taskNum, task, startDate, endDate };
			tableModel.addRow(data);
			paintTable(index, "plain");
		}
	}

	/**
	 * This method will print out all the tasks including floating tasks.
	 * 
	 * 
	 * @param list
	 *            This is list of Task object. each element contains different
	 *            commands entered by user
	 * 
	 * @param keyword
	 *            The printing method will print out the items on the list that
	 *            contains this keyword. "" by default
	 * 
	 *
	 */
	private static void printList_entire(ArrayList<Task> list, String keyword) {
		processFeedback("Displaying all tasks", null);
		clearTable();
		int row_count = 0;

		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).detail.contains(keyword)
					|| list.get(i).startDate.contains(keyword)
					|| list.get(i).endDate.contains(keyword)) {
				int taskNum = i + 1;
				String task = list.get(i).detail;
				String startDate = list.get(i).startDate;
				if (startDate.equals(DEFAULT_STRING)) {
					startDate = "";
				}
				String endDate = list.get(i).endDate;
				if (endDate.equals(DEFAULT_STRING)) {
					endDate = "";
				}
				checkCompleted(row_count, taskNum, task, startDate, endDate);
				row_count++;
			}
		}
	}

	/**
	 * This method is responsible for design of the table such as fond size of
	 * items and the column width
	 * 
	 * 
	 * @param rowIndex
	 *            This is the index of the items in a specific row to be changed
	 * 
	 * @param font
	 *            This string defines the font of the items to be changed.
	 * 
	 *
	 */
	private static void paintTable(int rowIndex, String font) {

		final TableCellRenderer renderer = table
				.getDefaultRenderer(Object.class);

		table.setDefaultRenderer(Object.class, new TableCellRenderer() {

			public Component getTableCellRendererComponent(JTable table,
					Object value,

					boolean isSelected, boolean hasFocus, int row, int column)

			{

				Component c = renderer.getTableCellRendererComponent(table,
						value, isSelected,

						hasFocus, row, column);
				if (font.equals("plain")) {
					c.setFont(new Font("Serif", Font.PLAIN, 17));
				}

				if (row == rowIndex) {

					if (font.equals("bold")) {
						c.setFont(new Font("Serif", Font.BOLD, 17));
					}

				}
				
			

				return c;

			}

		});

	}

	/**
	 * This method will remove all information in the table for printing updated tasks
	 * 
	 * 
	 *
	 *
	 */
	private static void clearTable() {
		int size = tableModel.getRowCount();
		for (int i = 0; i < size; i++) {
			tableModel.removeRow(0);
		}

	}

	/**
	 *This method will clear the current feedback panel for the new feedback.
	 * 
	 * 
	 * 
	 */
	protected static void clearFeedback() {

		feedback_panel.removeAll();
		feedback_panel.revalidate();
		feedback_panel.repaint();

	}
}