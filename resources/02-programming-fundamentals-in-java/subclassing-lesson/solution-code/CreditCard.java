
public class CreditCard extends Card{
	double cardLimit;
	
	public CreditCard(String name, String brand, double limit){
		super(name, brand);
		cardLimit = limit;
	}
}
