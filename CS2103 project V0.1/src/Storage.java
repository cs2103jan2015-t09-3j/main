import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;

public class Storage {
	static File file = new File("Cone.txt");
	ArrayList<Tasks> temp_list = new ArrayList<Tasks>();
	
	public void changeDirectory(String directory){
		file = new File(directory+ "\\Cone.txt");
	}
	
	public ArrayList<Tasks> readFromFile() {
		Tasks temp = new Tasks();
		// if file is empty, return the default Tasks
		if ((file.length() == 0)) {
			return temp_list;
		}
		try {
			@SuppressWarnings("resource")
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line;

			// if not, copy the file to the ArrayList Tasks
			// at the Tasks.detail && .date

			while (null != (line = br.readLine())) {
				temp = new Tasks();
				temp.detail = line;
				temp.startDate = br.readLine();
				temp.endDate = br.readLine();
				int index = temp.detail.indexOf(' ');
				if (index == -1) {
					System.out.println("File invalid, label your tasks");
					return temp_list;
				}
				temp.detail = temp.detail.substring(index + 1);
				temp.startDate= temp.startDate.substring(1);
				temp.endDate= temp.endDate.substring(1);
				temp_list.add(temp);
			}
			br.close();
		} catch (IOException e) {
			System.out.println(e);
		}

		return temp_list;
	}

	// this method should delete whatever is in the file and then write the
	// contents of ArrayList in to the file
	public void writeToFile(ArrayList<Tasks> list) {

		try {
			// delete the original contents without deleting the file
			RandomAccessFile raf = new RandomAccessFile(file, "rw");
			raf.setLength(0);
			raf.close();

			// write ArrayList to file
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);

			int size = list.size();
			for (int i = 0; i < size; i++) {
				bw.write(i + 1 + ". " + list.get(i).detail.toString());
				bw.newLine();
				bw.write("\t" + list.get(i).startDate.toString());
				bw.newLine();
				bw.write("\t" + list.get(i).endDate.toString());
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void importInstruction(ArrayList<String> text) {
		String temp= new String();
		
		File instruction = new File("instruction.txt");
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(instruction));
		

			// if not, copy the file to the ArrayList Tasks
			// at the Tasks.detail && .date
			
			
			while(!(temp = br.readLine().toString()).contains("end")){
				text.add(temp+"\n");
			}
			br.close();
		}
		 catch (IOException e) {
			System.out.println(e);
		 }
		
	}
}
		
	

	

