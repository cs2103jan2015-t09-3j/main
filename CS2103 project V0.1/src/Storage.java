import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.Writer;
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
			
			System.out.println("deleted from file : \"" + list.get(index-1).detail
					+ "\"");
			list.get(index-1).detail = DEFAULT_STRING;
			list.get(index-1).date = DEFAULT_STRING;
			organizeStorage(list);
		}
		private static void organizeStorage(ArrayList<Cone_Organizer> list) {
			for (int i = 0; i < list.size() - 1; i++) {
				if (list.get(i).detail == DEFAULT_STRING && list.get(i+1).detail != DEFAULT_STRING) {
					list.get(i).detail = list.get(i+1).detail;
					list.get(i+1).detail = DEFAULT_STRING;
				}
				if (list.get(i).date == DEFAULT_STRING && list.get(i+1).date != DEFAULT_STRING) {
					list.get(i).date = list.get(i+1).date;
					list.get(i+1).date = DEFAULT_STRING;
				}

			}

		}
		
		public void editCommand(Cone_Organizer cmd){
			
		}
		
		public ArrayList<Cone_Organizer> readFromFile(){
			Cone_Organizer temp = new Cone_Organizer();
			ArrayList<Cone_Organizer> temp_list = new ArrayList<Cone_Organizer>();
			
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
					temp.detail = line;
					temp.date = br.readLine();
					temp_list.add(temp);
				}
				br.close();
			} catch (IOException e){
				System.out.println(e);
			}
			
			return temp_list;
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
					bw.write(list.get(i).detail.toString() + "\n");
					bw.write(list.get(i).date.toString() + "\n");
				}
				bw.close();
			} catch (IOException e) {
				System.out.println(e);
				}
			}
		}
		




