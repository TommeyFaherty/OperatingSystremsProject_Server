
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Date;

public class server {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		ServerSocket listener;
		int clientid=0;
		try 
		{
			 listener = new ServerSocket(2005,10);
			 
			 while(true)
			 {
				System.out.println("Main thread listening for incoming new connections");
				Socket newconnection = listener.accept();
				
				System.out.println("New connection received and spanning a thread");
				Connecthandler t = new Connecthandler(newconnection, clientid);
				clientid++;

				t.start();
			 }
			
		} 
		
		catch (IOException e) 
		{
			System.out.println("Socket not opened");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}


class Connecthandler extends Thread
{

	Socket individualconnection;
	int socketid;
	ObjectOutputStream out;
	ObjectInputStream in;
	String message;
	int num1,num2,result,operation;
	String str1,str2;
	
	public Connecthandler(Socket s, int i)
	{
		individualconnection = s;
		socketid = i;
	}
	
	void sendMessage(String msg)
	{
		try{
			out.writeObject(msg);
			out.flush();
			System.out.println("client> " + msg);
		}
		catch(IOException ioException){
			ioException.printStackTrace();
		}
	}
	
	/*void sendResponse(boolean res)
	{
		try {
			System.out.println("response: "+res);
			//out.writeObject(res);
			out.writeBoolean(res);
			out.flush();
		}
		catch(IOException ioException) {
			ioException.printStackTrace();
		}
	}*/
	
	public void run()
	{
		
		try 
		{
			
			out = new ObjectOutputStream(individualconnection.getOutputStream());
			out.flush();
			in = new ObjectInputStream(individualconnection.getInputStream());
			System.out.println("Connection"+ socketid+" from IP address "+individualconnection.getInetAddress());
		
			sendMessage("Press 1 to log in\n\t2 to register");
			
			message=(String) in.readObject();
			
			//REad the files and send the line to the client
			
			//Check which option picked by user
			if(Objects.equals(message, "1")) {
				sendMessage("Selected Log in");
				logIn();
			}
			else if(Objects.equals(message, "2")) {
				sendMessage("Selected Register");
				register();
			}
			else {
				sendMessage("answer is unknown");
			}
			
			sendMessage("-12End");
		}
		
		catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		finally
		{
			try 
			{
				out.close();
				in.close();
				individualconnection.close();
			}
			catch (IOException e) 
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}	
	}
	
	public void register() {
		try {
			//Write Content
			File file = new File("register.txt");
			FileWriter writer = new FileWriter(file.getAbsoluteFile(), true); 
			
			//log in file
			File logInfo = new File("LogInInfo.txt");
			FileWriter writer2 = new FileWriter(logInfo, true);
			
			BufferedWriter bw = null;	
			BufferedWriter bw2 = null;
			bw = new BufferedWriter(writer);
			bw2 = new BufferedWriter(writer2);
			
			
			//Reads in text from register.txt
			System.out.println("Check if reads in from file\n=======================");
			BufferedReader abc = new BufferedReader(new FileReader(file));
			List<String> lines = new ArrayList<String>();
			String line;
			int i=0;
			
			while((line = abc.readLine()) != null) {
				lines.add(line);
				System.out.println(line);
			}
			abc.close();
			
			System.out.println(lines);
			
			sendMessage("REGISTERY\n=============\nPlease enter Name:");
			
			message=(String) in.readObject();
			System.out.println(message);
			bw.write(message+" ");
			
			sendMessage("Please enter Employee ID:");
			
			message=(String) in.readObject();
			
			//Ensure employee ID is unique
			while(i < lines.size())
			{
				String info;
				info = lines.get(i);
				if(info.contains(message)) {
					sendMessage("Not Unique enter a different ID:");
					message=(String) in.readObject();
					i = 0;
					continue;
				}
				i++;
			}
			
			System.out.println(message);
			bw.write(message+" ");
			bw2.write(message+" ");
			
			sendMessage("Please enter Email:");
			
			message=(String) in.readObject();
			
			//Ensure Email is unique
			i = 0;
			while(i < lines.size())
			{
				String info;
				info = lines.get(i);
				if(info.contains(message)) {
					sendMessage("Email already used. Try a diffrenet email:");
					message=(String) in.readObject();
					i = 0;
					continue;
				}
				i++;
			}
			System.out.println(message);
			bw.write(message+" ");
			
			sendMessage("Please enter Department:");
			
			message=(String) in.readObject();
			System.out.println(message);
			bw.write(message);
			bw.newLine();
			
			sendMessage("Enter the new password for your account:");
			
			message=(String) in.readObject();
			System.out.println(message);
			bw2.write(message);
			bw2.newLine();
			
				
		//finally
		try{
			if(bw != null)
			{
				bw.close();
			}
			if(writer != null)
			{
				writer.close();
			}
			if(bw2 != null)
			{
				bw2.close();
			}
			if(writer2 != null)
			{
				writer2.close();
			}
		}catch(IOException ioException)
		{
			ioException.printStackTrace();
		}
		
		}
		catch(IOException ioException)
		{
			ioException.printStackTrace();
		}
		catch (ClassNotFoundException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
	}
	
	public void logIn() {
		try {
			File logInfo = new File("LogInInfo.txt");
			System.out.println("Check if reads in from file\n=======================");
			BufferedReader abc = new BufferedReader(new FileReader(logInfo));
			List<String> lines = new ArrayList<String>();
			String line;
			
			//Variables
			int i=0;
			String account[] = {};
			
			while((line = abc.readLine()) != null) {
				lines.add(line);
				System.out.println(line);
			}
			abc.close();
			
			sendMessage("Enter your Employee ID:");
			
			message=(String)in.readObject();
			
			//Check for ID in log in file
			while(i<lines.size())
			{
				String info;
				info = lines.get(i);
				System.out.println("info "+i+": "+info);
				if(info.contains(message))
				{
					account = info.split(" ");
					System.out.println("Account found");
					i=0;
					break;
				}
				i++;
			}
			
			
			System.out.println(account[0]+" "+account[1]);
			
			sendMessage("Enter password: ");
			
			message=(String)in.readObject();
			
			while(!(message.equals(account[1])))
			{
				sendMessage("Password Incorrect. Try Again.");
				message=(String)in.readObject();
			}
			sendMessage("Log in Successfull");
			
			do{
			//Next Action
			sendMessage("Enter the following\n1-Add bug record\n2-Assign bug to user\n3-View all bug record not assigned\n"
					+ "4-View all bugs report\n5-See Update options\nOr '-12End' to terminate");
			
			message=(String)in.readObject();
			
			switch(message)
			{
			case "1":
				AddBug();
				break;
			case "2":
				AssignBug();
				break;
			case "3":
				ViewUnassigned();
				break;
			case "4":
				ViewAll();
				break;
			case "5":
				Update();
				break;
			default:
				sendMessage("Not an option. Try again");
				break;
			}
			}while(!(message.equals("-12End")));
			
		}catch(IOException e){
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
			finally {
		}
		
	}
	
	public void AddBug() {
		try {
			File bugRecord = new File("BugRecord.txt");		
			FileWriter writer = new FileWriter(bugRecord, true);
			
			BufferedWriter bw = null;	
			bw = new BufferedWriter(writer);
			bw.newLine();
			
			//Generate bug ID
			String uniqueID = UUID.randomUUID().toString();
			bw.write(uniqueID+", ");
			
			sendMessage("Enter Application name:");
			
			message=(String)in.readObject();
			bw.write(message+", ");
			
			//Date and Time Stamp
			DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			Date date = new Date();
			System.out.println(dateFormat.format(date));
			bw.write(dateFormat.format(date)+", ");
			
			sendMessage("Enter Platform");
			
			message=(String)in.readObject();
			bw.write(message+", ");

			sendMessage("Enter Problem Description");
			
			message=(String)in.readObject();
			bw.write(message+", ");
//UNFINISHED	
//Server lags behind after one option is chosen
			//Ensure Status is one of 3 options
			boolean Status = false;
			while(Status == false)
			{
				sendMessage("Enter Status: Open, Assigned or Closed");
				message=(String)in.readObject();
				
				if(message.equalsIgnoreCase("Open"))
				{
					sendMessage("Status: OPEN");
					bw.write(message+" ");
					bw.write("----------");
					//sendResponse(true);
					Status = true;
				}
				else if(message.equalsIgnoreCase("Assigned"))
				{
					sendMessage("Status: ASSIGNED");
					AssignBug(uniqueID);
					bw.write(message+" ");
					Status = true;
				}
				else if(message.equalsIgnoreCase("Closed"))
				{
					sendMessage("Status: CLOSED");
					bw.write(message+" ");
					Status = true;
				}
				else {
					continue;
				}
				
			}
			
			//close
			bw.close();
			
		}catch(IOException e) {
			e.printStackTrace();
		}catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void AssignBug(String id) {
		try{
			File reg = new File("Register.txt");
		
			List<String> lines = new ArrayList<String>();
			String line;
		
			//Variables
			int i=0;

			//Assign the bug to user
			sendMessage("Enter the User Id you would like to assign this bug to:");
			
			message=(String)in.readObject();
			
			//Check if account exists before assigning it
			BufferedReader regReader = new BufferedReader(new FileReader(reg));
			
			while((line = regReader.readLine()) != null) {
				lines.add(line);
				System.out.println(line);
			}
			regReader.close();
			
			while(i<lines.size())
			{
				String info;
				boolean exists = false;
				info = lines.get(i);
				if(info.contains(message))
				{
					System.out.println("account assigned");
					i=0;
					exists = true;
					break;
				}
				else
					i++;
				
				if(exists == false && i>=lines.size())
				{
					sendMessage("Account is not registered. Try again");
					message=(String)in.readObject();
					i=0;
					continue;
				}
			}
			
			return;
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		}
	
	public void AssignBug() {
		try
		{
			File bugRec = new File("BugRecord.txt");		
			BufferedReader bugReader = new BufferedReader(new FileReader(bugRec));
	
			//Variable
			boolean exists = false;
			int i = 0;
			String account[] = {};
			String idHolder = "";
			List<String> lines = new ArrayList<String>();
			String line;
			StringBuffer inputBuffer = new StringBuffer();
			
			//Prompt
			sendMessage("please enter the bug ID you would like to assign: ");
			message = (String)in.readObject();
			idHolder = message;
			
			//Check bug exists in record
			while((line = bugReader.readLine()) != null) {
				inputBuffer.append(line);
				inputBuffer.append("\r\n");
				lines.add(line);
				System.out.println(line);
			}
			String info = "";
			
			while(i<lines.size())
			{
				info = lines.get(i);
				if(info.contains(idHolder))
				{
					System.out.println("bug found");
					account = info.split(" ");
					i=0;
					exists = true;
					break;
				}
				else
					i++;	
			}
			System.out.println(account);
			//If bug not found in record
			if(exists == false && i>=lines.size())
				{
					sendMessage("bug is not recorded. Please add bug to record");
					
				}
			else {
				
				AssignBug(idHolder);
				
				System.out.println("The message: "+message); //Check message
				
				String inputStr = inputBuffer.toString();
				
				bugReader.close();
				
				System.out.println(inputStr);
				
				String newInfo="";
				newInfo = info.replace("open ----------", message);
				System.out.println("New Info: "+newInfo);
				
				inputStr = inputStr.replace(info, newInfo);
				
				System.out.println(inputStr);
				
				BufferedWriter fileOut = new BufferedWriter(new FileWriter("BugRecord.txt"));
		        //FileOutputStream fileOut = new FileOutputStream("BugRecord.txt");
				fileOut.write(inputStr);
				fileOut.close();
			}
				
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
		}
	}
//ArrayLsit Sent back looks ugly	
	public void ViewUnassigned() {
		try {
			File bugRec = new File("BugRecord.txt");
			BufferedReader reader = new BufferedReader(new FileReader(bugRec));
			List<String> lines = new ArrayList<String>();
			String line;
			
			while((line=reader.readLine()) != null)
			{
				if(line.contains("open ----------")) {
					lines.add(line+"\n");
					System.out.println(line);
				}
			}
			System.out.println(lines);
			sendMessage("Unassigned bugs\n================\n"+lines);
			
			reader.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void ViewAll() {
		try {
			File bugRec = new File("BugRecord.txt");
			BufferedReader reader = new BufferedReader(new FileReader(bugRec));
			List<String> lines = new ArrayList<String>();
			String line;
			
			while((line=reader.readLine()) != null)
			{
					lines.add(line+"\n");
					System.out.println(line);
			}
			
			System.out.println(lines);
			sendMessage("Unassigned bugs\n================\n"+lines);
			
			reader.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void Update() {
		try {
			File bugRec = new File("BugRecord.txt");
			BufferedReader reader = new BufferedReader(new FileReader(bugRec));
			
			//Variables
			int i = 0;
			String info = "";
			String account[] = {};
			String idHolder = "";
			boolean exists = false;
			List<String> lines = new ArrayList<String>();
			String line;
			StringBuffer inputBuffer = new StringBuffer();
			
			sendMessage("Please enter the bug ID of bug you would like to update: ");
			
			message = (String)in.readObject();
			
			//Find bug ID
			while((line = reader.readLine()) != null)
			{
				lines.add(line);
				inputBuffer.append(line);
				inputBuffer.append("\r\n");
			}
			
			while(i<lines.size())
			{
				info = lines.get(i);
				if(info.contains(message))
				{
					System.out.println("bug found");
					account = info.split(",");
					i=0;
					exists = true;
					idHolder = message;
					break;
				}
				else
					i++;	
				//If can't find ID or incorrect value entered
				if(exists == false && i >= lines.size() || message.length() < 36)
				{
					sendMessage("ID not found in record. Try Again: ");
					message = (String)in.readObject();
					i = 0;
				}
			}
			
			sendMessage("Select one of the options to update\n1-Status\n2-Problem Description\n3-Change Assigned Engineer");
			
			message = (String)in.readObject();
			
			if(message.equals("1")) {
				
				System.out.println("Status: "+account[5]);	
				
				sendMessage("Current status: "+account[5]+"\nWhat would you like to change it to:"
						+"\n1-Open\n2-Closed\n3-Assign");
				
				message = (String)in.readObject();
				
				String inputStr = inputBuffer.toString();
				String newInfo = info;
				
				switch(message)
				{
				case "1":
					newInfo = info.replace(account[5], "open ----------");
					
					inputStr = inputStr.replace(info, newInfo);
					break;
				case "2":
					newInfo = info.replace(account[5], "closed ----------");
					
					inputStr = inputStr.replace(info, newInfo);
					break;
				case "3":
					AssignBug(idHolder);
					newInfo = info.replace(account[5], message);
					
					inputStr = inputStr.replace(info, newInfo);
					break;
				}
				
				//Rewrite files with updated data
				BufferedWriter fileOut = new BufferedWriter(new FileWriter("BugRecord.txt"));
				fileOut.write(inputStr);
				fileOut.close();
			}
			else if(message.equals("2")) {
				
			}
			else if(message.equals("3")) {
				//Assign Id to bug
				AssignBug(idHolder);
				
				String inputStr = inputBuffer.toString();
				String newInfo = info.replace(account[5],message);
				
				inputStr = inputStr.replace(info, newInfo);
				
				//Rewrite file with updated data
				BufferedWriter fileOut = new BufferedWriter(new FileWriter("BugRecord.txt"));
				fileOut.write(inputStr);
				fileOut.close();
			}
			else
			{
				sendMessage("Not an option.Press any char and Enter to return to menu");
				message=(String)in.readObject();
			}
			//close
			reader.close();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
}

