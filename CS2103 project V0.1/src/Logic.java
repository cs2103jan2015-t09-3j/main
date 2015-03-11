import java.util.ArrayList;
import java.util.Scanner;



public class Logic {
	Parser p = new Parser();
	Storage s = new Storage();
	private static Scanner sc = new Scanner(System.in);

	public void executeCommand(Cone_Organizer cmd, ArrayList<Cone_Organizer> list) {

		p.parse(cmd);
		implementCommand(cmd, list);

	}

	public void implementCommand(Cone_Organizer cmd, ArrayList<Cone_Organizer> list) {
		switch (cmd.command_type) {
		case "add": {
			s.addCommand(cmd, list);
			break;
			
		}
		case "display": {
			s.displayCommand(list);
			break;
		}

		case "delete": {
			int index;
			index=Integer.parseInt(cmd.detail);
			s.deleteCommand(index, list);
			break;
		}
		case "edit": {
			int index;
			index=Integer.parseInt(cmd.detail);
			editCommand(list, index);
			break;
		}
		case "clear": {
			s.clearCommand(list);
			break;
		}
		}

	}

	private void editCommand(ArrayList<Cone_Organizer> list, int index) {
		System.out.println("Old task: "+list.get(index-1).detail +" by "+ list.get(index-1).date);
		System.out.print("New task: ");
		Cone_Organizer cmd = new Cone_Organizer();
		cmd.command = "add " + sc.nextLine();
		p.parse(cmd);
		list.get(index-1).detail = cmd.detail;
		list.get(index-1).date = cmd.date;
		
	}
}
