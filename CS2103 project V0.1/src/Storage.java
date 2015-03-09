import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;



public class Storage {
	private static final String DEFAULT_STRING = "DEFAULT";
	
	static File file = new File("Cone.txt");
	

		
		//Storage part -- QiaoDi
		public void addCommand(Cone_Organizer cmd, ArrayList<Cone_Organizer> list) {
			
			
			list.add(cmd);
			
			
		}
	
			
		//storage part -- QiaoDi
		public void displayCommand(ArrayList<Cone_Organizer> list) {
			for (int i = 0; i < list.size(); i++) {
				if(list.get(i).detail!= DEFAULT_STRING){
					System.out.println(i+1 + ". "+ "task : "+ list.get(i).detail);
					if(list.get(i).date !=DEFAULT_STRING){
						System.out.println("date: "+list.get(i).date);
					}
				}
				}
		}	

		//this method should delete the item from the list at 'index-1' and push up the list by one index
		public void deleteCommand(int index, ArrayList<Cone_Organizer> list){
			
			System.out.println("deleted from file : \"" + list.get(index).detail
					+ "\"");
			list.get(index).detail = DEFAULT_STRING;
			organizeStorage(list);
		}
		private static void organizeStorage(ArrayList<Cone_Organizer> list) {
			for (int i = 0; i < list.size() - 1; i++) {
				if (list.get(i).detail == DEFAULT_STRING && list.get(i+1).detail != DEFAULT_STRING) {
					list.get(i).detail = list.get(i+1).detail;
					list.get(i+1).detail = DEFAULT_STRING;
				}

			}

		}
		
		public void editCommand(Cone_Organizer cmd){
			
		}
		public ArrayList<Cone_Organizer> readFromFile(){
			Cone_Organizer temp = new Cone_Organizer();
			ArrayList<Cone_Organizer> temp_list = new ArrayList<Cone_Organizer>();
			//find the detail , date
/*			temp.detail = whatever u found;
			temp.date = what u found;
			list.add(temp);
		*/	return temp_list;
		}
		
		//this method should delete whatever is in the file and then write the contents of arraylist in to the file
		public void writeToFile(ArrayList<Cone_Organizer> list){
			
		}
		
/*		private static void deleteFromFile(int taskNum){
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
*/
}




