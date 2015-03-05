import java.util.Scanner;

//UI part -- Kim
public class Cone_Organizer {
	
	public static Scanner sc = new Scanner(System.in);
	private static final String DEFAULT_STRING = "DEFAULT";
	public String command = DEFAULT_STRING;
	public String command_type = DEFAULT_STRING;
	public String detail = DEFAULT_STRING;

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
//Logic part - Kim
	private static void executeCommand(Cone_Organizer item) {
		
		parser(item);
		implementCommand(item);
		
		
	}
	//Logic part -- Kim
private static void implementCommand(Cone_Organizer item) {
	switch (item.command_type){
	case "add" :{
		addCommand(item);
	}
	case "display" :{
		displayCommand(item);
	}
	
	}
	}

//Parser part -- LIQI
private static void parser(Cone_Organizer item) {
	int index = item.command.indexOf(' ');
	item.command_type = item.command.substring(0,index);
	item.detail = item.command.substring(index, item.command.length());
}

//storage part -- Qiao Di
	private static void displayCommand(Cone_Organizer item) {
		// TODO Auto-generated method stub
		
	}
//Storage part -- Qiao Di
	private static void addCommand(Cone_Organizer item) {
		// TODO Auto-generated method stub
		
	}

}
