package BlackJackImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class BlackJack {
	public static final String[] SUITS = { "♣", "♥", "♠", "♦" };
	public static final Map<String, Integer> CARDS = new HashMap<String, Integer>();
	static {
		CARDS.put("ACE", 11);
		CARDS.put("TWO", 2);
		CARDS.put("THREE", 3);
		CARDS.put("FOUR", 4);
		CARDS.put("FIVE", 5);
		CARDS.put("SIX", 6);
		CARDS.put("SEVEN", 7);
		CARDS.put("EIGTH", 8);
		CARDS.put("NINE", 9);
		CARDS.put("TEN", 10);
		CARDS.put("JACK", 10);
		CARDS.put("QUEEN", 10);
		CARDS.put("KING", 10);
	}
	public static List<String> Possible_cards = new ArrayList<String>(CARDS.keySet());
	public static List<List<String>> Human_cards = new ArrayList<>();
	private static List<List<String>> Computer_cards = new ArrayList<>();
	private static boolean checkFirstGame = true;
	public int actual_bet = 0;
	public static int Actual_money = 0;
	public static int Initial_money = 0;
	public int Human_points = 0;
	private int Computer_points = 0;
	private int counter_computer = 0;
	private boolean isAnInt = false;
	private Scanner ask_user = new Scanner(System.in);

	public BlackJack() {
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
			Random rand = new Random();
			List<String> toAdd1 = Arrays.asList(Possible_cards.get(rand.nextInt(Possible_cards.size())),
					SUITS[rand.nextInt(SUITS.length)]);
			List<String> toAdd2 = Arrays.asList(Possible_cards.get(rand.nextInt(Possible_cards.size())),
					SUITS[rand.nextInt(SUITS.length)]);
			Human_cards.clear();
			Human_cards.add(toAdd1);
			Human_cards.add(toAdd2);
			for (int j = 0; j < Human_cards.size(); j++) {
				Human_points += CARDS.get(Human_cards.get(j).get(0));
			}
			List<String> toAdd3 = Arrays.asList(Possible_cards.get(rand.nextInt(Possible_cards.size())),
					SUITS[rand.nextInt(SUITS.length)]);
			List<String> toAdd4 = Arrays.asList(Possible_cards.get(rand.nextInt(Possible_cards.size())),
					SUITS[rand.nextInt(SUITS.length)]);
			Computer_cards.clear();
			Computer_cards.add(toAdd3);
			Computer_cards.add(toAdd4);
			for (int j = 0; j < Computer_cards.size(); j++) {
				Computer_points += CARDS.get(Computer_cards.get(j).get(0));
			}

			check_ace();
			check_bet();
		}

	}

	private void check_ace() {
		ArrayList<String> cards = new ArrayList<String>();
		for (int i = 0; i < Human_cards.size(); i++) {
			cards.add(Human_cards.get(i).get(0));
		}
		if (cards.contains("ACE")) {
			if (Human_points > 21)
				Human_points -= 10;
		}
	}

	private void check_bet() {
		System.out.println("Your actual money is " + Actual_money);
		for (int i = 0; i < 5; i++) {
			System.out.println("What bet do you wanna make?\n");
			isAnInt = ask_user.hasNextInt();
			if (isAnInt) {
				isAnInt = false;
				actual_bet = ask_user.nextInt();
				ask_user.nextLine();

				break;
			} else {
				System.out.println("Please, use only numbers");
				ask_user.nextLine();
			}
		}
		System.out.println(actual_bet);
		for (int i = 0; i < 5; i++) {

			if (actual_bet > Actual_money) {
				System.out.println("You cannot make a bet bigger than your actual money");
				System.out.println("What bet do you wanna make?\n");
				actual_bet = ask_user.nextInt();
				ask_user.nextLine();
			} else if (actual_bet == 0) {
				System.out.println("The bet must be greater than zero");
				System.out.println("What bet do you wanna make?\n");
				actual_bet = ask_user.nextInt();
				ask_user.nextLine();
			} else {
				start_game();
				break;
			}

		}
	}

	public void start_game() {
		System.out.println("The game has started, it is your turn.\n");
		System.out.println("Your cards are " + Human_cards.get(0).get(0) + " of " + Human_cards.get(0).get(1) + " and "
				+ Human_cards.get(1).get(0) + " of " + Human_cards.get(1).get(1) + ".\n");
		System.out.println("The first card of the bank is " + Computer_cards.get(0).get(0) + " of "
				+ Computer_cards.get(0).get(1));
		human_win_lose_condition();
		human_turn();
	}

	private void computer_turn() {
		if (counter_computer == 0) {
			System.out.println("Now it is the bank turn.");
			counter_computer = 1;
		}
		update_points("computer");
		for (int i = 0; i < 21; i++) {
			if (i == 0)
				System.out.print("The bank cards are: ");
			try {
				System.out.print(Computer_cards.get(i).get(0) + " of " + Computer_cards.get(i).get(1) + ", ");
			} catch (Exception e) {
				System.out.println();
				break;
			}
		}

		if (!computer_win_lose_condition()) {
			if (Computer_points < Human_points) {
				Random rand = new Random();
				List<String> toAddComputer = Arrays.asList(Possible_cards.get(rand.nextInt(Possible_cards.size())),
						SUITS[rand.nextInt(SUITS.length)]);
				Computer_cards.add(toAddComputer);
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
		update_points("human");
		check_ace();
		if (!human_win_lose_condition()) {
			System.out.println("Do you wanna hit? (y/n)\n");
			String question = ask_user.nextLine();
			if (question.contains("y") || question.contains("yes")) {
				Random rand = new Random();
				List<String> toAddHuman = Arrays.asList(Possible_cards.get(rand.nextInt(Possible_cards.size())),
						SUITS[rand.nextInt(SUITS.length)]);
				Human_cards.add(toAddHuman);

				for (int i = 0; i < 21; i++) {
					if (i == 0)
						System.out.print("Now, your cards are: ");
					try {
						System.out.print(Human_cards.get(i).get(0) + " of " + Human_cards.get(i).get(1) + ", ");
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

	private void update_points(String selection) {
		if (selection == "human") {
			Human_points = 0;
			for (int j = 0; j < Human_cards.size(); j++) {
				Human_points += CARDS.get(Human_cards.get(j).get(0));
			}
		} else {
			Computer_points = 0;
			for (int j = 0; j < Computer_cards.size(); j++) {
				Computer_points += CARDS.get(Computer_cards.get(j).get(0));
			}
		}
	}

	private void reset() {
		String final_balance = String.valueOf(Actual_money - Initial_money);
		if (!final_balance.contains("-"))
			final_balance = "+" + final_balance;
		if (Actual_money > 0) {
			System.out.println("Do you want to play again? (y/n)\n");
			String decision = ask_user.nextLine();
			if (decision.contains("y") || decision.contains("yes")) {
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
