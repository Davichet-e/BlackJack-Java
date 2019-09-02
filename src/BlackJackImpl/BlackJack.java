package BlackJackImpl;

//The Deck class has been created by @natedane, I just made a few changes.

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class BlackJack {
	private static Scanner askUser = new Scanner(System.in);
	private static BlackJackHand dealerHand = new BlackJackHand();
	private static List<Player> players = new ArrayList<Player>();

	public static void blackJack() {
		startGame();
		while (true) {
			System.out.println("Game started");
			System.out.println("The first card of the dealer is " + dealerHand.getCards().get(0));

			for (Player player : players)
				playerTurn(player);

			dealerTurn();
			endGame();
			if (!nextGame())
				break;

		}
	}

	private static void startGame() {
		int numberOfPeople = askNumberOfPeople();
		askAndSetPlayerAtributes(numberOfPeople);
	}

	private static int askNumberOfPeople() {
		int numberOfPeople = 0;
		while (true) {
			System.out.println("How many people are going to play? (0-5)");
			try {
				numberOfPeople = askUser.nextInt();
				askUser.nextLine();

				if (numberOfPeople > 5 || 1 < numberOfPeople) {
					System.out.println("The number of people must be between 1 and 5");
					continue;
				}
				break;

			} catch (InputMismatchException ime) {
				System.out.println("The number of people must be a number between 1 and 5");
			}
		}
		return numberOfPeople;
	}

	private static void askAndSetPlayerAttributes(int numberOfPeople) {
		for (int i = 1; i <= numberOfPeople; i++) {
			System.out.println("Please, enter your name, Player" + i);
			String name = askUser.nextLine();
			while (true) {
				System.out.println("\nHow much money do you have?\n");
				try {
					int initialMoney = askUser.nextInt();
					askUser.nextLine();
					if (initialMoney < 50) {
						System.out.println("The initial money must be greater or equal than 50");
						continue;
					}
					players.add(new Player(name, initialMoney));
					break;
				} catch (InputMismatchException ime) {
					askUser.nextLine();
					System.out.println("Please, use only numbers.\n");
				}
			}

		}

	}

	private static void askPlayerBet(Player player) {
		while (true) {
			System.out.println("What bet do you wanna make?\n");
			try {
				int bet = askUser.nextInt();
				askUser.nextLine();
				if (bet > player.actualMoney) {
					System.out.println("You cannot make a bet bigger than your actual money");
				} else if (bet <= 0) {
					System.out.println("The bet must be greater than zero");
				} else {
					player.actualBet = bet;
					break;
				}

			} catch (InputMismatchException ime) {
				askUser.nextLine();
				System.out.println("Please, use only numbers.\n");
			}

		}
	}

	private static boolean checkIfYes() {
		String answer = askUser.nextLine().trim().toLowerCase();
		if (answer.equals("yes") || answer.equals("y") || answer.equals("1"))
			return true;
		return false;
	}

	private static boolean askIfHit() {
		System.out.println("Do you wanna hit? (y/n)\n");
		return checkIfYes();
	}

	private static boolean playerWinOrLose(Player player) {
		boolean result = false;
		if (player.getPoints() == 21) {
			System.out.println("BLACKJACK!");
			result = true;
		} else if (player.getPoints() == 0) {
			System.out.println("BUST!\nI'm afraid you lose this game :(\n");
			result = true;
		}
		return result;
	}

	private static void playerTurn(Player player) {
		System.out.println(player + ", your actual money is " + player.actualMoney);
		askPlayerBet(player);
		System.out.println("Your turn has started.\nYour cards are " + player.getCards().get(0) + " and "
				+ player.getCards().get(1));
		while (!playerWinOrLose(player)) {
			if (askIfHit()) {
				player.dealCard();
				System.out.println("Now, your cards are: " + player.hand());
			} else {
				System.out.println(player + " standed.");
				break;
			}

		}
	}

	private static boolean dealerLost() {
		if (dealerHand.getPoints() == 0) {
			System.out.println("The dealer busted. The game ended :)\n");
			return true;
		}
		return false;
	}

	private static void dealerTurn() {
		System.out.println("The first card of the bank is " + dealerHand.getCards().get(0) + " and "
				+ dealerHand.getCards().get(1));
		while (!dealerLost() && dealerHand.getPoints() < 17) {
			System.out.println("The dealer is going to hit a card\n");
			dealerHand.dealCard();
			System.out.println("Now, the dealer cards are " + dealerHand);
		}
	}

	private static void endGame() {
		for (Player player : players) {
			if (player.getPoints() == 21 || player.getPoints() > dealerHand.getPoints()) {
				player.actualMoney += player.actualBet;
				System.out.println(player + " won " + player.actualBet * 2 + "€. :)\n");
			} else if (player.getPoints() == 0 || player.getPoints() < dealerHand.getPoints()) {
				player.actualMoney -= player.actualBet;
				System.out.println(player + " lost against the dealer :(\n");
			} else
				System.out.println(player + " it is a Tie :|");

		}
	}

	private static boolean askIfNextGame(Player player) {
		boolean playerResets = false;
		String finalBalance = String.valueOf(player.actualMoney - player.initialMoney) + " €";
		if (!finalBalance.contains("-"))
			finalBalance = "+" + finalBalance;

		if (player.actualMoney > 0) {
			System.out.println(player + ", do you want to play again? (y/n)");
			if (checkIfYes()) {
				player.initializeAttributes();
				playerResets = true;
			} else
				System.out.println("Thanks for playing, " + player + " your final balance is " + finalBalance);

		} else
			System.out.println(player + ", you have lost all your money, Thanks for playing\n");

		return playerResets;

	}

	private static boolean nextGame() {
		List<Player> nonReplayPlayers = new ArrayList<>();
		for (Player player : players) {
			if (!askIfNextGame(player))
				nonReplayPlayers.add(player);
		}
		players.removeAll(nonReplayPlayers);

		if (!players.isEmpty()) {
			dealerHand.initializeAttributes();
			return true;
		}

		return false;
	}

}
