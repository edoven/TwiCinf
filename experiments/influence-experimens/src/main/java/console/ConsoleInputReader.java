package console;

import java.io.Console;

public class ConsoleInputReader
{
	public static void main(String[] args)
	{
		Console console = System.console();
	    //read user name, using java.util.Formatter syntax :
	    String username = console.readLine("User Name? ");
	    System.out.println("username=" + username);
	    
	    //read the password, without echoing the output 
	    String password = console.readLine("Password? ");
	    System.out.println("password=" + password);
	}
	    
}
