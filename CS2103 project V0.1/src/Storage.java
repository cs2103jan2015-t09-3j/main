import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Scanner;



public class Storage {
//	UI ui = new UI();
	private static final String DEFAULT_STRING = "DEFAULT";
	private static Scanner sc = new Scanner(System.in);
	
	static File file = new File("Cone.txt");
	ArrayList<Cone_Organizer> temp_list = new ArrayList<Cone_Organizer>();
	

		
		//Storage part -- QiaoDi
		/**
		 * This execute the command add, which creates a new task by copying the cmd to the list  
		 * @param cmd  	this contains the task content and date of the new task
		 * @param list	this is the new list that contains all the tasks
		 */
		public void addCommand(Cone_Organizer cmd, ArrayList<Cone_Organizer> list) {
			
			
			list.add(cmd);
			
			
			
		}
	
			
		//storage part -- QiaoDi
		public void displayCommand(ArrayList<Cone_Organizer> list, UI GUI) {
			for (int i = 0; i < list.size(); i++) {
				GUI.print(i+1 + ". "+ "task :"+ list.get(i).detail);
				if(!list.get(i).date.equals(DEFAULT_STRING)){
					GUI.print("date: "+ list.get(i).date);
					
				}	
			}
			if(list.size()==0){
				GUI.print("The file is empty!");
		//		System.out.println("The file is empty!");
			}
		}	

		//this method should delete the item from the list at 'index-1' and push up the list by one index
		public void deleteCommand(int index, ArrayList<Cone_Organizer> list){
			
			
			list.remove(index-1);
		}
		
		

		public void clearCommand(ArrayList<Cone_Organizer> list){
			
			list.clear();
			
			
				
		}
		
		public ArrayList<Cone_Organizer> readFromFile(){
			Cone_Organizer temp = new Cone_Organizer();
			
			
			//if file is empty, return the default Cone_Organizer
			if ((file.length() == 0)){
				return temp_list;
			} 
		
			try{
				BufferedReader br = new BufferedReader(new FileReader(file));
			    String line;
			    
				//if not, copy the file to the ArrayList Cone_Organizer
				//at the Cone_Organizer.detail && .date	
					
				while(null!= (line = br.readLine())){
					temp = new Cone_Organizer();
					temp.detail = line;
					temp.date = br.readLine();
					int index = temp.detail.indexOf(' ');
					if(index == -1){
						System.out.println("File invalid, label your tasks");
						return temp_list;
					}
					temp.detail = temp.detail.substring(index+1);
					temp.date = temp.date.substring(1);
					temp_list.add(temp);
				}
				br.close();
			} catch (IOException e){
				System.out.println(e);
			}
			
			return temp_list;
		}
		public void editCommand(ArrayList<Cone_Organizer> list, int index, UI GUI) {
			GUI.print("Old task: "+list.get(index-1).detail);
			GUI.print("Old date: "+list.get(index-1).date);
			
			GUI.jt.setText("add "+list.get(index-1).detail + " - "+ list.get(index-1).date);
			

			
			
		}
			
		
		public void replaceItem(ArrayList<Cone_Organizer> list, Cone_Organizer cmd, int index) {

			
			
			
		
			
			list.add(index-1,cmd);

			list.remove(index);
			
		
			
		}


		//this method should delete whatever is in the file and then write the contents of arraylist in to the file
		public void writeToFile(ArrayList<Cone_Organizer> list){
			
			try{
				//delete the original contents without deleting the file
				RandomAccessFile raf = new RandomAccessFile(file,"rw");			
				raf.setLength(0);
				raf.close();
			
				//write ArrayList to file
				FileWriter fw = new FileWriter(file);
				BufferedWriter bw = new BufferedWriter(fw);
			
				int size = list.size();
				for(int i = 0; i < size; i++){                    
					bw.write(i+1+". " + list.get(i).detail.toString());
					bw.newLine();
					bw.write("\t" + list.get(i).date.toString());
					bw.newLine();
				}
				bw.close();
			} catch (IOException e) {
				System.out.println(e);
				}
			}

		/**
		 * This method will mark the task as completed.
		 * @param index This is the index of the item to be marked in the list
		 * @param list	This is list of cone_organizer object. each element contains different commands entered by user
		 */
		public void markCompleted(ArrayList<Cone_Organizer> list, int index, UI GUI) {
			list.get(index-1).detail = "(completed)"+ list.get(index-1).detail;
			GUI.print("The task no."+index+" has been marked completed!");
			
			
			
		}

		/**
		 * This method will mark the task as incomplete.
		 * @param index This is the index of the item to be marked in the list
		 * @param list	This is list of cone_organizer object. each element contains different commands entered by user
		 * @param GUI 
		 */
		public void markIncomplete(ArrayList<Cone_Organizer> list, int index, UI GUI) {
			list.get(index-1).detail = list.get(index-1).detail.substring(list.get(index-1).detail.indexOf(')')+1);
			GUI.print("The task no."+index+" has been marked incompleted!");
		}


		public void searchCommand(ArrayList<Cone_Organizer> list, UI GUI, Cone_Organizer cmd) {
			GUI.print("Search results: ");
			for(int i=0; i<list.size(); i++){
				if(list.get(i).detail.contains(cmd.detail) || list.get(i).date.contains(cmd.detail)){
					GUI.print(i+1 + ". "+ "task :"+ list.get(i).detail);
					if(!list.get(i).date.equals(DEFAULT_STRING)){
						GUI.print("date: "+ list.get(i).date);
						
					}
				}
				
			}
			
		}
		}
		




