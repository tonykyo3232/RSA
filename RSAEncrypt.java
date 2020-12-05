import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Hashtable;
import java.math.BigInteger;

/****************************************************
* RSAEncrypt.java
* Encrypts a file
****************************************************/

/*
You will write a Java program to encrypt an arbitrary file. 
Note that the RSA algorithm specifies how to encrypt a single number (< n).
To encrypt a file, it is sufficient to break up the file into blocks so that each block can be treated as a number and
encrypted with the RSA algorithm. In this homework, you will use a block size of 3 bytes.
To treat each block as a number, simply use 00 - 26 encoding scheme (26 for space). 
*/

public class RSAEncrypt {

	// 00-26 encoding scheme
	public static Hashtable<String, String> letter_table = new Hashtable<String, String>();

    private static void insert_letters(){
    	letter_table.put("A", "00");
        letter_table.put("B", "01");
        letter_table.put("C", "02");
        letter_table.put("D", "03");
        letter_table.put("E", "04");
        letter_table.put("F", "05");
        letter_table.put("G", "06");
        letter_table.put("H", "07");
        letter_table.put("I", "08");
        letter_table.put("J", "09");
        letter_table.put("K", "10");
        letter_table.put("L", "11");
        letter_table.put("M", "12");
        letter_table.put("N", "13");
        letter_table.put("O", "14");
        letter_table.put("P", "15");
        letter_table.put("Q", "16");
        letter_table.put("R", "17");
        letter_table.put("S", "18");
        letter_table.put("T", "19");
        letter_table.put("U", "20");
        letter_table.put("V", "21");
        letter_table.put("W", "22");
        letter_table.put("X", "23");
        letter_table.put("Y", "24");
        letter_table.put("Z", "25");
    	letter_table.put("a", "00");
        letter_table.put("b", "01");
        letter_table.put("c", "02");
        letter_table.put("d", "03");
        letter_table.put("e", "04");
        letter_table.put("f", "05");
        letter_table.put("g", "06");
        letter_table.put("h", "07");
        letter_table.put("i", "08");
        letter_table.put("j", "09");
        letter_table.put("k", "10");
        letter_table.put("l", "11");
        letter_table.put("m", "12");
        letter_table.put("n", "13");
        letter_table.put("o", "14");
        letter_table.put("p", "15");
        letter_table.put("q", "16");
        letter_table.put("r", "17");
        letter_table.put("s", "18");
        letter_table.put("t", "19");
        letter_table.put("u", "20");
        letter_table.put("v", "21");
        letter_table.put("w", "22");
        letter_table.put("x", "23");
        letter_table.put("y", "24");
        letter_table.put("z", "25");
        letter_table.put(" ", "26");
        letter_table.put(",", "27");
        letter_table.put(".", "28");
        letter_table.put("\n", "29");
    }

	private static String readFile(String fileName){
		String content = "";
		try{
            content = Files.readString(Path.of(fileName));
            System.out.println(content);
        } 
        catch (IOException e){
            e.printStackTrace();
        }
        return content;
	}

	private static void writeFile(String fileName, String content) throws IOException {
		try{
		      FileWriter myWriter = new FileWriter(fileName, true);
		      myWriter.write(content);
		      myWriter.close();
		 } 
		catch (IOException e) {
		      e.printStackTrace();
		 }	
	}


	/*
		block size of 3 bytes
		use 00 - 26 encoding scheme (26 for space)
		C = P^e
	*/
	public static void encrypt(String message, int e) throws IOException{

		BigInteger ciperText, plaintext;

		// note: when convert from string to int, the 0's on the left will disappear
		// need to check the number of digits when doing decryption
		plaintext = new BigInteger(message);
		ciperText = plaintext.pow(e);
		System.out.println("ciperText is:" + ciperText);
		writeFile("test.enc", ciperText.toString());
	}

	private static boolean isNumber(String str) { 
	  	try {  
	    	Integer.parseInt(str);  
	    	return true;
	  	} 
	  	catch(NumberFormatException e){  
	    	return false;  
	  	}  
	}

	// block size of 3 bytes
	private static ArrayList<String> getBlocks(String message){

		int count = 0;
		int temp = 0;
		ArrayList<String> msg = new ArrayList<String>();
		String data = "";
		System.out.println("message.length(): " + message.length());

		for (int i = 0; i < message.length(); i += 3) {
			// System.out.print(count + ": [");
			for (int j = i; j < i+3; j++) {
				// debug
				if(message.length() > j){
					// System.out.print(letter_table.get(Character.toString(message.charAt(j))));
					// System.out.print("(" + message.charAt(j) + ")");
					if(letter_table.get(Character.toString(message.charAt(j))) != null){
						data += letter_table.get(Character.toString(message.charAt(j)));
						temp = j;
					}
				}

			}
			// System.out.println("]");
			if(data.length() != 6){
				temp++;
				data += letter_table.get(Character.toString(message.charAt(temp)));
				i++;
			}
			msg.add(data);
			data = "";
			count++;
		}
		
		return msg;
	}


	// return e
	private static int getE(String message){

		String e_str = "";

		// base case: when e is 1 digit
		for(int i = 4; i < message.length(); i++){
			String temp = Character.toString(message.charAt(i));
			if(isNumber(temp)){
				e_str += temp;
			} else{
				break;
			}
		}
		// System.out.println("e is: [" + e_str + "]");

		return Integer.parseInt(e_str);
	}

	/*
		args[0]: text.txt
		args[1]: pub_key.txt
	*/
	public static void main(String[] args) throws IOException {
		
		// insert letters into hash table
		insert_letters();

		if(args.length == 2){
			String msg1 = readFile(args[0]);
			String msg2 = readFile(args[1]);
			int e = getE(msg2);

			ArrayList<String> m = getBlocks(msg1);
			int blocksToCheck = m.size();
			
			System.out.println("\nm: ");
			for(int i = 0; i < m.size(); i++){
				String str = m.get(i);
				System.out.println(i + ": ");
				encrypt(str, e);	
			}

		}
		else{
			// exception
		}
	}
}