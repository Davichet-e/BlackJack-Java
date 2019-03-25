package BlackJackImpl;

//The Deck class has been created by @natedane, I just made a few changes.

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;
import BlackJackImpl.Deck.Card;
import BlackJackImpl.Players.Player;
import static BlackJackImpl.Players.getMyDeck;

public class BlackJack {
	private static boolean checkFirstGame = true;
	private int Computer_points = 0;
	private int counter_computer = 0;
	private boolean isAnInt = false;
	private Scanner ask_user = new Scanner(System.in);
	private static Deck myDeck = getMyDeck();
	private ArrayList<Card> Computer_cards = myDeck.getInitialCards();
	private ArrayList<Player> NonReplayPlayers = new ArrayList<Player>(5);
	private static ArrayList<Player> Players = new ArrayList<Player>(5);

	public BlackJack() {
		if (checkFirstGame) {
			System.out.println("How many people is it going to play? (0-5)");
			int numberOfPeople = ask_user.nextInt();
			ask_user.nextLine();
			for (int j = 0; j < numberOfPeople; j++) {
				System.out.println("Please, enter your name, Player" + (j + 1));
				String name = ask_user.nextLine();
				for (int i = 0; i < 5; i++) {
					System.out.println("\nHow much money do you have?\n");
					isAnInt = ask_user.hasNextInt();
					if (isAnInt) {
						int initialMoney = ask_user.nextInt();
						ask_user.nextLine();
						Players.add(j, new Player(name, initialMoney));
						break;
					} else {
						ask_user.nextLine();
						System.out.println("Please, use only numbers.\n");
					}
				}

			}



		} else
			isAnInt = true;
		Players.removeAll(Collections.singleton(null));
		start_game();

	}

	private void PlayerTurn(Player p) {
		System.out.println(p.getName() + ", your actual money is " + p.getActualMoney());
		if (!player_win_lose_condition(p)) {
			for (int i = 0; i < 5; i++) {
				System.out.println("What bet do you wanna make?\n");
				isAnInt = ask_user.hasNextInt();
				if (isAnInt) {
					isAnInt = false;
					p.setActualBet(ask_user.nextInt());
					ask_user.nextLine();
					if (p.getActualBet() > p.getActualMoney()) {
						System.out.println("You cannot make a bet bigger than your actual money");
						System.out.println("What bet do you wanna make?\n");
						p.setActualBet(ask_user.nextInt());
						ask_user.nextLine();
					} else if (p.getActualBet() <= 0) {
						System.out.println("The bet must be greater than zero");
						System.out.println("What bet do you wanna make?\n");
						p.setActualBet(ask_user.nextInt());
						ask_user.nextLine();
					} else
						break;
				} else
					System.out.println("Please, use only numbers.\n");

			}

			System.out.println(
					"Your turn has started.\nYour cards are " + p.getCards().get(0) + " and " + p.getCards().get(1));
			for (int j = 0; j < 100; j++) {
				if (!player_win_lose_condition(p)) {
					System.out.println("Do you wanna hit? (y/n)\n");
					String question = ask_user.nextLine();
					if (question.toLowerCase().trim().equals("y") || question.toLowerCase().trim().equals("yes")) {
						p.dealCard();
						System.out.println(p.getPoints());
						for (int i = 0; i < 21; i++) {
							if (i == 0)
								System.out.print("Now, your cards are: ");
							try {
								System.out.print(p.getCards().get(i) + ", ");
							} catch (Exception e) {
								System.out.println();
								break;
							}
						}
					} else {
						System.out.println(p.getName() + " standed.");
						break;
					}
				} else
					break;
			}
		}
	}

	public void start_game() {
		Players.removeAll(NonReplayPlayers);
		System.out.println("The game has started.\n");
		System.out.println("The first card of the bank is " + Computer_cards.get(0));

		for (Player p : Players) {
			PlayerTurn(p);
		}
		computer_turn();

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
		System.out.println();

		if (!computer_win_lose_condition()) {
			if (Computer_points < 17) {
				Computer_cards.add(myDeck.dealCard());
				computer_turn();
			} else {
				endGame();
			}
		} else {
			endGame();
		}

	}

	private boolean computer_win_lose_condition() {
		if (Computer_points > 21) {
			System.out.println("The bank busted. The game ended :)\n");
			Computer_points = 1;
			endGame();
			return true;
		} else
			return false;
	}

	private boolean player_win_lose_condition(Player p) {
		if (p.getPoints() == 21) {
			p.updateActualMoney(p.getActualBet());
			System.out.println("BLACKJACK. Congratulations, you won " + p.getActualBet() * 2 + "€");
			return true;
		} else if (p.getPoints() > 21) {
			p.updateActualMoney(-p.getActualBet());
			p.LoserPoints();
			System.out.println("Bust.\nI'm afraid you lose this game :(\n");
			return true;
		} else
			return false;
	}

	private void endGame() {
		for (Player p : Players) {
			if (p.getPoints() != 21) {
				if (p.getPoints() > Computer_points) {
					p.updateActualMoney(p.getActualBet());
					System.out.println(p.getName() + " won " + p.getActualBet() * 2 + "€.\n");
				} else if (p.getPoints() == Computer_points) {
					System.out.println(p.getName() + ", it is a Tie!.\n");
				} else {
					System.out.println(p.getName() + " lost against the bank.\n");	
					System.out.println(p.getPoints() + "p");
					System.out.println(Computer_points);
					p.updateActualMoney(-p.getActualBet());
				}
			} else {
				System.out.println(p.getName() + " won " + p.getActualBet() * 2 + "€.\n");
			}
		}
		reset();
	}

	private void reset() {
		for (Player p : Players) {
			String final_balance = String.valueOf(p.getActualMoney() - p.getInitialMoney());
			if (!final_balance.contains("-"))
				final_balance = "+" + final_balance;
			if (p.getActualMoney() > 0) {
				System.out.println(p.getName() + ", do you want to play again? (y/n)\n");
				String decision = ask_user.nextLine();
				if (decision.toLowerCase().trim().equals("y") || decision.toLowerCase().trim().equals("yes")) {
					p.resetCards();
					checkFirstGame = false;
				} else {

					System.out.println(
							"Thanks for playing, " + p.getName() + ", your final balance is " + final_balance + "€\n");
					NonReplayPlayers.add(p);
					if (Players.isEmpty())
						ask_user.close();

				}
			} else {
				NonReplayPlayers.add(p);
				if (Players.isEmpty())
					ask_user.close();
				System.out.println(p.getName() + ", you have lost all you money. Thanks for playing");

			}
		}
		Players.removeAll(NonReplayPlayers);
		if (!Players.isEmpty()) {
			@SuppressWarnings("unused")
			BlackJack g = new BlackJack();
		}
	}

}
