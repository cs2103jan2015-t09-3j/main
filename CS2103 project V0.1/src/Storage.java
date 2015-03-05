
public class Storage {
	
	public class Storage {
		
		File file = new File("CO-NE.txt");
		
		//Storage part -- QiaoDi
		public void addCommand(Cone_Organizer cmd) {
			// TODO Auto-generated method stub
			try {
			         BufferedWriter output = new BufferedWriter(new FileWriter(file));
			         output.write(cmd);
			         output.close();
			     } catch ( IOException e ) {
			          e.printStackTrace();
			     }		    
			
			System.out.println("\"", cmd, "is added."\""");
			}
			
	//storage part -- QiaoDi
		public void displayCommand() {
			// TODO Auto-generated method stub
			System.out.println(file.toString());
		}	

		}
		public void deleteCommand(Cone_Organizer cmd){
			
		}
		public void editCommand(Cone_Organizer cmd){
			
		}
		private static void readToFile(Cone_Organizer cmd){
			
		}
		private static void writeToFile(Cone_Organizer cmd){
			
		}
		private static void deleteFromFile(Cone_Organizer cmd){
			
		}
		

}



