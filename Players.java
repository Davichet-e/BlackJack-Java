package BlackJackImpl;

import java.util.ArrayList;
import BlackJackImpl.Deck.Card;

public class Players {

	private static Deck myDeck = new Deck();

	public static class Player {
		private String name;
		private int initialMoney, actualMoney, points, actualBet;

		private ArrayList<Card> cards;

		public Player(String name, int initialMoney) {
			this.name = name;
			this.initialMoney = initialMoney;
			this.actualMoney = initialMoney;
			this.cards = myDeck.getInitialCards();
			this.points = Deck.getSum(cards);
			this.actualBet = 0;
		}

		public String getName() {
			return name;
		}

		public int getInitialMoney() {
			return initialMoney;
		}

		public int getActualMoney() {
			return actualMoney;
		}

		public void updateActualMoney(int money) {
			this.actualMoney += money;
		}

		public ArrayList<Card> getCards() {
			return cards;
		}

		public void resetCards() {
			LoserPoints();
			this.cards.clear();
			this.cards = myDeck.getInitialCards();

		}

		public void dealCard() {
			cards.add(myDeck.dealCard());
			updatePoints();
		}

		public int getPoints() {
			return points;
		}

		public void updatePoints() {
			this.points = Deck.getSum(cards);
			check_ace();
		}

		public void LoserPoints() {
			this.points = 0;
		}

		public int getActualBet() {
			return actualBet;
		}

		public void setActualBet(int bet) {
			this.actualBet = bet;
		}

		public void check_ace() {
			for (Card card : cards) {
				if(card.getName().equals("ACE"))
					if (points > 21)
						points -= 10;
			}

			
		}

	}

	public static Deck getMyDeck() {
		return myDeck;
	}

}