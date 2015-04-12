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
	ArrayList<Task> temp_list = new ArrayList<Task>();

	public void changeDirectory(String directory) {
		file = new File(directory + "\\Cone.txt");
	}

	public ArrayList<Task> readFromFile() {
		Task temp = new Task();
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
				String recurinfo,tmp;
				int startin, endin;
				
				temp = new Task();
				temp.detail = line;
				temp.startDate = br.readLine();
				temp.endDate = br.readLine();
				recurinfo = br.readLine();
				recurinfo = recurinfo.substring(1);
				int index = temp.detail.indexOf(' ');
				if (index == -1) {
					System.out.println("File invalid, label your tasks");
					return temp_list;
				}
				temp.detail = temp.detail.substring(index + 1);
				temp.startDate = temp.startDate.substring(1);
				temp.endDate = temp.endDate.substring(1);
				startin = 0;
				endin = recurinfo.indexOf("\t");
				tmp = recurinfo.substring(startin, endin);
				if(tmp.equals("true")){
					temp.isPrimaryRecurringTask=true;
				}
				startin = endin+1;
				endin = recurinfo.indexOf("\t",endin+1);
				tmp = recurinfo.substring(startin, endin);
				temp.recurring_from = tmp;
				startin = endin+1;
				endin = recurinfo.indexOf("\t",endin+1);
				tmp = recurinfo.substring(startin, endin);
				temp.recurring_interval = Integer.parseInt(tmp);
				startin = endin+1;
				endin = recurinfo.indexOf("\t",endin+1);
				tmp = recurinfo.substring(startin, endin);
				temp.recurring_period = tmp;
				startin = endin+1;
				endin = recurinfo.length();
				tmp = recurinfo.substring(startin, endin);
				temp.recurring_until = tmp;
				
				
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
	public void writeToFile(ArrayList<Task> list) {

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
				bw.write("\t"+list.get(i).isPrimaryRecurringTask + "\t"
						+ list.get(i).recurring_from + "\t"
						+ list.get(i).recurring_interval + "\t"
						+ list.get(i).recurring_period + "\t"
						+ list.get(i).recurring_until);
				bw.newLine();
			}
			bw.close();
		} catch (IOException e) {
			System.out.println(e);
		}
	}

	public void importInstruction(ArrayList<String> text) {
		String temp = new String();

		File instruction = new File("instruction.txt");

		try {
			BufferedReader br = new BufferedReader(new FileReader(instruction));

			// if not, copy the file to the ArrayList Tasks
			// at the Tasks.detail && .date

			while (!(temp = br.readLine().toString()).contains("end")) {
				text.add(temp + "\n");
			}
			br.close();
		} catch (IOException e) {
			System.out.println(e);
		}

	}
}
