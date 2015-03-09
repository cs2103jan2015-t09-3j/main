import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;



public class Storage {
	
	static File file = new File("Cone.txt");
	

		
		//Storage part -- QiaoDi
		public void addCommand(Cone_Organizer cmd) {
			// TODO Auto-generated method stub
			
			try {
			         BufferedWriter output = new BufferedWriter(new FileWriter(file));
			         output.write(cmd.detail);
			         output.close();
			     } catch ( IOException e ) {
			          e.printStackTrace();
			     }		    
			
			System.out.println("\""+ cmd + "is added. \"");
		}
	
			
		//storage part -- QiaoDi
		public void displayCommand() {
		// TODO Auto-generated method stub			
		System.out.println(file.toString());
		}	

		public void deleteCommand(int index){
			deleteFromFile(index);
		}
		
		public void editCommand(Cone_Organizer cmd){
			
		}
		private static void readFromFile(Cone_Organizer cmd){
			
		}
		private static void writeToFile(Cone_Organizer cmd){
			
		}
		
		private static void deleteFromFile(int taskNum){
			File tmp = File.createTempFile("tmp", "");

		    BufferedReader br = new BufferedReader(new FileReader(file));
		    BufferedWriter bw = new BufferedWriter(new FileWriter(tmp));

		    for (int i = 0; i < taskNum; i++)
		        bw.write(String.format("%s%n", br.readLine()));

		    br.readLine(); //skip the deleted line
            
		    String line; 
		    while(null != (line = br.readLine())) {
		    	String str = taskNum + line.substring(1);
		    	bw.write(String.format("%s%n",str));
		        taskNum ++;
		    }
		     
		  
		    
		     tmp.renameTo(file);	
		     file.delete();
	   
		}

}




