

public class Storage {
	
	File file = new File("CO-NE.txt");
	
	public class Storage {
		
		//File file = new File("CO-NE.txt");
		
		//Storage part -- QiaoDi
		public void addCommand(Cone_Organizer cmd) {
			// TODO Auto-generated method stub
			String cmd = cmd.detail;
			try {
			         BufferedWriter output = new BufferedWriter(new FileWriter(file));
			         output.write(cmd);
			         output.close();
			     } catch ( IOException e ) {
			          e.printStackTrace();
			     }		    
			
			System.out.println("\"", cmd, "is added."\"");
		}
	
			
		//storage part -- QiaoDi
		public void displayCommand() {
		// TODO Auto-generated method stub			
		System.out.println(file.toString());
		}	

		public void deleteCommand(Cone_Organizer cmd){
			deleteFromFile(cmd);
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

		    for (int i = 0; i < toRemove; i++)
		        bw.write(String.format("%s%n", br.readLine()));

		    br.readLine(); //skip the deleted line
            
		    String line; 
		    while(null != (line = br.nextLine()){
		    	String str = taskNum + line.substring(1);
		    	bw.write(String.format("%s%n",str));
		        taskNum ++;
		    }
		     
		    File oldFile = new File(f);
		    if (oldFile.delete())
		        tmp.renameTo(oldFile);		    	
	   
		}

}



