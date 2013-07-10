package it.cybion.info.utils;

import java.io.*;

public class SerializationManager
{
	public static Object deserializeObject(String filepath)
	{
		Object object = null;
		try
		{
			FileInputStream fileInputStream = new FileInputStream(filepath);
			ObjectInputStream objectInputStream = new ObjectInputStream (fileInputStream);
			object = objectInputStream.readObject();
			objectInputStream.close();
		}
		catch (IOException  e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		return object;	
	}
	
	
	public static void serializeObject(Object object, String filepath)
	{
		try
		{
			
			
			File file = new File(filepath);
			if (!file.exists())
			{
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			FileOutputStream fileOutputStream = new FileOutputStream(filepath);
			ObjectOutputStream objectOutputStream = new ObjectOutputStream (fileOutputStream);
			objectOutputStream.writeObject ( object );
			objectOutputStream.close();
			fileOutputStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			System.exit(0);
		}
	
	}
}
