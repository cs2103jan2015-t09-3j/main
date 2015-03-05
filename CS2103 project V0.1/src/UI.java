import java.util.Scanner;
public class UI {
	private static Scanner sc = new Scanner(System.in);
	

	public static void main(String[] args) {
		Cone_Organizer cmd = new Cone_Organizer();
		
		Logic l = new Logic();
		System.out.println("Welcome to Co-Ne organizer!!!!");
		
		
		while(!cmd.command.equals("exit")){
			
		
		System.out.println("Command: ");
		cmd.command = sc.nextLine();
		l.executeCommand(cmd);
		}

	}
}


	