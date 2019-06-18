package BlackJackImpl;

import java.util.List;
import java.util.stream.Collectors;

import BlackJackImpl.Deck.Card;

public class BlackJackHand {
	public static Deck deck = new Deck();
	private List<Card> cards;
	private int points;
	private int aces;

	public BlackJackHand() {
		initializeAttributes();
	}

	public List<Card> getCards() {
		return cards;
	}

	public int getPoints() {
		return points;
	}

	private void checkIfAce(Card card) {
		if (card.getName().equals("ACE"))
			aces++;
	}

	public void initializeAttributes() {
		cards = deck.getInitialCards();
		points = Deck.getSum(cards);
		for (Card card : cards)
			checkIfAce(card);
		checkAcePoints();
	}

	public void dealCard() {
		Card card = deck.dealCard();
		checkIfAce(card);
		cards.add(card);
		updatePoints(card);
		checkIfLose();
	}

	private void checkAcePoints() {
		while (points > 21 && aces > 0) {
			points -= 10;
			aces--;
		}
	}

	public void checkIfLose() {
		if (points > 21)
			points = 0;
	}

	public void updatePoints(Card card) {
		points += card.getValue();
		checkAcePoints();
	}

	public String toString() {
		return String.join(", ", cards.stream().map(Card::toString).collect(Collectors.toList())) + ".";
	}
}
