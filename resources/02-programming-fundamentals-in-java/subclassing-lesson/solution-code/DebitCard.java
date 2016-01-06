
public class DebitCard extends Card{
	double accountBalance;
	
	public DebitCard(String name, String brand, double balance){
		super(name,brand);
		accountBalance = balance;
	}
}
