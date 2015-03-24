import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/*
Logger logger = Logger.getLogger("TextBuddy");
logger.log(Level.INFO,  "going to start processing");
FileHandler fh;
try {
	fh = new FileHandler("MyLogFile.log");  
    logger.addHandler(fh);
    SimpleFormatter formatter = new SimpleFormatter();  
    fh.setFormatter(formatter);  

    // the following statement is used to log any messages  
    logger.info("My first log"); 
	createFile(args[0]); 
	readFile(args[0]); 
	System.out.println("Welcome to TextBuddy. " + args[0] + " is ready for use");
	run(args[0]);	
	
} catch (IOException ex) {
	logger.log(Level.WARNING, "processing error", ex);
		//System.out.println(e); 

}
logger.info("Hi How r u?");

}
*/



public class Logic {
	Parser p = new Parser();
	Storage s = new Storage();

	/**
	 * This method will identify the command type and details from the user input and send it to implement command method
	 * @param cmd  	This initially contains user input and will be containing command type and details after parser
	 * @param list	This is list of cone_organizer object. each element contains different commands entered by user
	 * @param gUI 
	 */
	public void executeCommand(Cone_Organizer cmd, ArrayList<Cone_Organizer> list, UI GUI) {
		assert list!=null;
		GUI.clearBuffer();
		
		p.parse(cmd);
		implementCommand(cmd, list, GUI);

	}
	/**
	 * This method imports items in the text file currently saved in same directory and put them in the array list.
	 * @param list	This is list of cone_organizer object. each element contains different commands entered by user
	 */
	public ArrayList<Cone_Organizer> import_From_File(ArrayList<Cone_Organizer> list){
		list = s.readFromFile();
		return list;
	}
	
	/**
	 * This method assess the command type and assign an appropriate method for each commandtype for implementation.
	 * @param cmd  	The current command that is being processed
	 * @param list	This is list of cone_organizer object. each element contains different commands entered by user
	 * @param GUI 
	 */
	public void implementCommand(Cone_Organizer cmd, ArrayList<Cone_Organizer> list, UI GUI) {
		switch (cmd.command_type) {
		case "add": {
			s.addCommand(cmd, list);
			GUI.print(cmd.detail + " has been successfully added!");
			break;			
		}
		
		case "display": {
			s.displayCommand(list, GUI);
			break;
		}

		case "delete": {
			int index;
			index=Integer.parseInt(cmd.detail);
			s.deleteCommand(index, list);
			GUI.print(cmd.detail + "has been deleted successfully!");
			break;
		}
		
		case "edit": {
			int index;
			index=Integer.parseInt(cmd.detail);
			s.editCommand(list, index, GUI);
			break;
		}
		
		case "clear": {
			s.clearCommand(list);
			GUI.print("All contents are cleared");
			break;
		}
		case "save": {
			s.writeToFile(list);
			GUI.print("All changes saved!");
			break;
		}
		case "mark": {
			int index;
			index=Integer.parseInt(cmd.detail);
			if(list.get(index-1).detail.contains("(completed)")){
				s.markIncomplete(list, index,GUI);
			}
			else{
				s.markCompleted(list, index,GUI);
			}
			
			break;
		}
		case "search": {
			s.searchCommand(list, GUI, cmd);
			break;
		}
		default:
		{
			GUI.print("Invalid Command");
			
		}
		}
	}
}
