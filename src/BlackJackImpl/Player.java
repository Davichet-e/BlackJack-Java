package BlackJackImpl;

public class Player {
	private final Hand hand;
	private final String name;
	private final int initialMoney;
	private int actualMoney, actualBet;

	public Player(String name, int initialMoney) {
		this.name = name;
		this.initialMoney = initialMoney;
		this.actualMoney = initialMoney;
		this.hand = new Hand();
	}

	
	
	public int getActualMoney() {
		return actualMoney;
	}
	
	public void bet(int bet) {
		actualBet = bet;
	}
	
	public void win() {
		actualMoney += actualBet;
	}
	
	public void lose() {
		actualMoney -= actualBet;
	}

	public Hand getHand() {
		return hand;
	}

	public String getName() {
		return name;
	}

	public int getInitialMoney() {
		return initialMoney;
	}

	public int getActualBet() {
		return actualBet;
	}

	@Override
	public String toString() {
		return name;
	}

}
