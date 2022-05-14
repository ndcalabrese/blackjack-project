package blackjack;

import java.util.Random;

/**
 * This class is responsible for generating a card
 * and keeping track of its suit and value.
 */

public class Card {

    private String suit;
    private String value;
    private Random randomValues = new Random();
    private Random randomSuits = new Random();

    Card(String suit, String value) {
        this.suit = suit;
        this.value = value;
    }

    public String getSuit() {
        return suit;
    }

    public String getValue() {
        return value;
    }

    public String getRandomSuit() {
        String[] suits = { "spade", "diamond", "heart", "club" };

        return suits[randomSuits.nextInt(suits.length)];
    }

    public String getRandomValue() {
        String[] values = { "1", "2", "3", "4", "5", "6", "7",
            "8", "9", "10", "J", "Q", "K", "A" };

        return values[randomValues.nextInt(values.length)];
    }

}
