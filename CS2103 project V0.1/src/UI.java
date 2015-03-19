import java.util.ArrayList;
import java.util.Scanner;
public class UI {
	private static Scanner sc = new Scanner(System.in);
	static Storage s = new Storage();
	static Logic l = new Logic();
	static ArrayList<Cone_Organizer> list = new ArrayList <Cone_Organizer>();
	static Cone_Organizer cmd = new Cone_Organizer();
	public static void main(String[] args) {
		
		
		System.out.println("Welcome to Co-Ne organizer!!!!");
		
		//import the contents from the file
		list=l.import_From_File(list);
		takeCommand();
		
		
		

	}


	private static void takeCommand() {
		while(!cmd.command.equals("exit")){
			
			cmd = new Cone_Organizer();
			System.out.print("Command: ");
		
			cmd.command = sc.nextLine();
			l.executeCommand(cmd, list);
		}
		
	}
}


	