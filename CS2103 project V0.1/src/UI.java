import java.util.ArrayList;
import java.util.Scanner;
public class UI {
	private static Scanner sc = new Scanner(System.in);
	static Storage s = new Storage();
	

	public static void main(String[] args) {
		ArrayList<Cone_Organizer> list = new ArrayList <Cone_Organizer>();
		//import the contents from the file
		list = s.readFromFile();
		
		Cone_Organizer cmd = new Cone_Organizer();
		
		Logic l = new Logic();
		System.out.println("Welcome to Co-Ne organizer!!!!");
		
		
		while(!cmd.command.equals("exit")){
			
			cmd = new Cone_Organizer();
			System.out.print("Command: ");
		
			cmd.command = sc.nextLine();
			l.executeCommand(cmd, list);
		}
		
		//save changes to the file
		s.writeToFile(list);

	}
}


	