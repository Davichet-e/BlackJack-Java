package BlackJackImpl;

public class Player extends BlackJackHand {
	public final String name;
	public final int initialMoney;
	public int actualMoney, actualBet;

	public Player(String name, int initialMoney) {
		super();
		this.name = name;
		this.initialMoney = initialMoney;
		this.actualMoney = initialMoney;
	}

	public String hand() {
		return super.toString();
	}

	@Override
	public String toString() {
		return name;
	}

}
