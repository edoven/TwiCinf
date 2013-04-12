
public class Parlatore
{
	public static void main(String[] args)
	{
		Parlatore parlatore = Parlatore.getInstance("Hello world!");
		parlatore.parla();
	}
	
	
	private String fraseDaDire;
	
	private static Parlatore instance = null;
	
	private Parlatore(String fraseDaDire)
	{
		this.fraseDaDire = fraseDaDire;
	}
	
	public void parla()
	{
		System.out.println(fraseDaDire);
	}
	
	public static Parlatore getInstance(String fraseDaDire)
	{
		if (instance==null)
			instance = new Parlatore(fraseDaDire);
		return instance;
	}
}
