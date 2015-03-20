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
			System.out.println(cmd.detail + " has been successfully added!");
			
			
		}
	
			
		//storage part -- QiaoDi
		public void displayCommand(ArrayList<Cone_Organizer> list) {
			for (int i = 0; i < list.size(); i++) {
				System.out.println(i+1 + ". "+ "task :"+ list.get(i).detail);
				if(!list.get(i).date.equals(DEFAULT_STRING)){
					System.out.println("date: "+list.get(i).date);
				}	
			}
			if(list.size()==0){
				System.out.println("The file is empty!");
			}
		}	

		//this method should delete the item from the list at 'index-1' and push up the list by one index
		public void deleteCommand(int index, ArrayList<Cone_Organizer> list){
			
			System.out.println("deleted from file : \"" + list.get(index-1).detail
					+ "\"");
			list.remove(index-1);
		}
		
		

		public void clearCommand(ArrayList<Cone_Organizer> list){
			
			list.clear();
			
			System.out.println("All items removed from the file!");
				
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
		public void editCommand(ArrayList<Cone_Organizer> list, int index) {
			System.out.println("Old task: "+list.get(index-1).detail);
			System.out.print("New task: ");
			list.get(index-1).detail = sc.nextLine();
			System.out.println("Old date: "+list.get(index-1).date);
			System.out.print("New date: ");
			
			list.get(index-1).date = sc.nextLine();
			
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
		public void markCompleted(ArrayList<Cone_Organizer> list, int index) {
			list.get(index-1).detail = "(completed)"+ list.get(index-1).detail;
			System.out.println("The task no."+index+" has been marked completed!");
			
			
			
		}

		/**
		 * This method will mark the task as incomplete.
		 * @param index This is the index of the item to be marked in the list
		 * @param list	This is list of cone_organizer object. each element contains different commands entered by user
		 */
		public void markIncomplete(ArrayList<Cone_Organizer> list, int index) {
			list.get(index-1).detail = list.get(index-1).detail.substring(list.get(index-1).detail.indexOf(')')+1);
			System.out.println("The task no."+index+" has been marked incompleted!");
		}
		}
		




