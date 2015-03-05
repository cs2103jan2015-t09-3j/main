
public class UI {

	public static void main(String[] args) {
		Cone_Organizer item = new Cone_Organizer();
		System.out.println("Welcome to Co-Ne organizer!!!!");
		System.out.println("Current tasks: ");
		displayCommand(item);
		while(!item.command.equals("exit")){
			
		
		System.out.println("Command: ");
		item.command = sc.nextLine();
		executeCommand(item);
		}

	}

}
