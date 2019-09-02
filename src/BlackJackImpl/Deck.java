package BlackJackImpl;

import static java.util.Collections.shuffle;

import java.util.ArrayList;
import java.util.List;

//This deck class has been originally created by @natedane, I just made a few changes 
public class Deck {

	private List<Card> deck;

	public Deck() {
		deck = new ArrayList<Card>(52);
		for (int i = 1; i <= 13; i++) {
			int value = i > 10 ? 10 : i;
			if (i == 1)
				value = 11;
			deck.add(new Card('♥', valueToName(i), value));
			deck.add(new Card('♦', valueToName(i), value));
			deck.add(new Card('♣', valueToName(i), value));
			deck.add(new Card('♠', valueToName(i), value));
		}
		shuffle(deck);
	}

	public int getDeckSize() {
		return deck.size();
	}

	public Card dealCard() {
		Card top = deck.get(0);
		deck.remove(top);
		return top;
	}

	public static int getSum(List<Card> cards) {
		int points = 0;
		for (Card one : cards) {
			points += one.getValue();
		}
		return points;
	}

	public List<Card> getInitialCards() {
		List<Card> hand = new ArrayList<Card>(deck.subList(0, 2));
		deck.removeAll(hand);
		return hand;
	}

	private static String valueToName(int value) {
		String valueName[] = { "ACE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE", "TEN", "JACK",
				"QUEEN", "KING" };
		return valueName[value - 1];
	}

	public class Card implements Comparable<Card> {
		private char suit;
		private String name;
		private Integer value;

		public Card(char suit, String name, int value) {
			this.suit = suit;
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}

		public int getValue() {
			return value;
		}

		public String toString() {
			return name + " of " + suit;
		}

		@Override
		public int compareTo(Card compare) {
			return value.compareTo(compare.getValue());
		}

	}
}
