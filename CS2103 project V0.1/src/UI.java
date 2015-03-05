import java.util.Scanner;
public class UI {
	private static Scanner sc = new Scanner(System.in);
	

	public static void main(String[] args) {
		Cone_Organizer item = new Cone_Organizer();
		
		Logic l = new Logic();
		System.out.println("Welcome to Co-Ne organizer!!!!");
		
		
		while(!item.command.equals("exit")){
			
		
		System.out.println("Command: ");
		item.command = sc.nextLine();
		l.executeCommand(item);
		}

	}
}


	