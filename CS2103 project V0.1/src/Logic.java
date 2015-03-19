import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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




public class Logic {
	Parser p = new Parser();
	Storage s = new Storage();


	public void executeCommand(Cone_Organizer cmd, ArrayList<Cone_Organizer> list) {
		assert list!=null;

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
			s.editCommand(list, index);
			break;
		}
		
		case "clear": {
			s.clearCommand(list);
			break;
		}
		default:
		{
			System.out.println("Invalid command");
			
		}
		}
	}
}
