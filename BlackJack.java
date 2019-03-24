package BlackJackImpl;

//The Deck class has been created by @natedane, I just made a few changes.

import java.util.ArrayList;	
import java.util.Scanner;
import BlackJackImpl.Deck;
import BlackJackImpl.Deck.Card;

public class BlackJack {
	private static boolean checkFirstGame = true;
	public int actual_bet = 0;
	public static int Actual_money = 0;
	public static int Initial_money = 0;
	public static int Human_points = 0;
	private static int Computer_points = 0;
	private int counter_computer = 0;
	private boolean isAnInt = false;
	private Scanner ask_user = new Scanner(System.in);
	private static Deck myDeck = new Deck();
	public ArrayList<Card> Human_cards = myDeck.getInitialCards();
	private ArrayList<Card> Computer_cards = myDeck.getInitialCards();

	public BranchBlackJack() {
		// TODO Auto-generated constructor stub
		if (checkFirstGame) {
			for (int i = 0; i < 5; i++) {
				System.out.println("How much money do you have?\n");
				isAnInt = ask_user.hasNextInt();
				if (isAnInt) {
					Initial_money = ask_user.nextInt();
					ask_user.nextLine();
					Actual_money = Initial_money;
					break;
				} else {
					System.out.println("Please, use only numbers");
					ask_user.nextLine();
				}

			}
		} else
			isAnInt = true;

		if (isAnInt) {
			isAnInt = false;
			Human_points = Deck.getSum(Human_cards);
			Computer_points = Deck.getSum(Computer_cards);

			check_ace();
			check_bet();
		}

	}

	private void check_ace() {
		ArrayList<String> cards = new ArrayList<String>();
		for (int i = 0; i < Human_cards.size(); i++) {
			cards.add(Human_cards.get(i).getName());
		}
		if (cards.contains("ACE")) {
			if (Human_points > 21)
				Human_points -= 10;
		}
	}

	private void check_bet() {
		System.out.println("Your actual money is " + Actual_money);
		for (int i = 0; i < 10; i++) {
			System.out.println("What bet do you wanna make?\n");
			isAnInt = ask_user.hasNextInt();
			if (isAnInt) {
				isAnInt = false;
				actual_bet = ask_user.nextInt();
				ask_user.nextLine();
				if (actual_bet > Actual_money) {
					System.out.println("You cannot make a bet bigger than your actual money");
					System.out.println("What bet do you wanna make?\n");
					actual_bet = ask_user.nextInt();
					ask_user.nextLine();
				} else if (actual_bet <= 0) {
					System.out.println("The bet must be greater than zero");
					System.out.println("What bet do you wanna make?\n");
					actual_bet = ask_user.nextInt();
					ask_user.nextLine();
				} else {
					start_game();
					break;
				}

			} else {
				System.out.println("Please, use only numbers");
				ask_user.nextLine();
			}
		}

	}

	public void start_game() {
		System.out.println("The game has started, it is your turn.\n");
		System.out.println("Your cards are " + Human_cards.get(0) + " and " + Human_cards.get(1) + ".\n");
		System.out.println("The first card of the bank is " + Computer_cards.get(0));
		human_win_lose_condition();
		human_turn();
	}

	private void computer_turn() {
		if (counter_computer == 0) {
			System.out.println("Now it is the bank turn.");
			counter_computer = 1;
		}
		Computer_points = Deck.getSum(Computer_cards);
		for (int i = 0; i < 21; i++) {
			if (i == 0)
				System.out.print("The bank cards are: ");
			try {
				System.out.print(Computer_cards.get(i) + ", ");
			} catch (Exception e) {
				System.out.println();
				break;
			}
		}

		if (!computer_win_lose_condition()) {
			if (Computer_points < Human_points) {
				Computer_cards.add(myDeck.dealCard());
				computer_turn();
			}
		}

	}

	private boolean computer_win_lose_condition() {
		if (Computer_points > 21) {
			System.out.println("The bank busted. Congratulations, you won " + actual_bet * 2 + "€ :)");
			Actual_money += actual_bet;
			reset();
			return true;
		} else if (Computer_points > Human_points) {
			System.out.println("I'm afraid you lose this game :(\n");
			Actual_money -= actual_bet;
			reset();
			return true;
		} else if (Computer_points == Human_points) {
			System.out.println("Tie! :|\n");
			reset();
			return true;
		} else
			return false;
	}

	private void human_turn() {
		Human_points = Deck.getSum(Human_cards);
		check_ace();
		if (!human_win_lose_condition()) {
			System.out.println("Do you wanna hit? (y/n)\n");
			String question = ask_user.nextLine();
			if (question.toLowerCase().trim().equals("y") || question.toLowerCase().trim().equals("yes")) {
				Human_cards.add(myDeck.dealCard());

				for (int i = 0; i < 21; i++) {
					if (i == 0)
						System.out.print("Now, your cards are: ");
					try {
						System.out.print(Human_cards.get(i) + ", ");
					} catch (Exception e) {
						System.out.println();
						break;
					}
				}
				human_turn();
			} else
				computer_turn();

		}
	}

	private boolean human_win_lose_condition() {
		if (Human_points == 21) {
			Actual_money += actual_bet;
			System.out.println("BLACKJACK. Congratulations, you won " + actual_bet * 2 + "€");
			reset();
			return true;
		} else if (Human_points > 21) {
			Actual_money -= actual_bet;
			System.out.println("Bust.\nI'm afraid you lost this game :(");
			reset();
			return true;
		} else
			return false;
	}

	private void reset() {
		String final_balance = String.valueOf(Actual_money - Initial_money);
		if (!final_balance.contains("-"))
			final_balance = "+" + final_balance;
		if (Actual_money > 0) {
			System.out.println("Do you want to play again? (y/n)\n");
			String decision = ask_user.nextLine();
			if (decision.toLowerCase().trim().equals("y") || decision.toLowerCase().trim().equals("yes")) {
				checkFirstGame = false;
				@SuppressWarnings("unused")
				BlackJack h = new BlackJack();
			} else {
				System.out.println("Thanks for playing, Your final balance is " + final_balance + "€\n");
				ask_user.close();
			}

		} else {
			System.out.println("You have lost all you money. Thanks for playing");
		}
	}

}
