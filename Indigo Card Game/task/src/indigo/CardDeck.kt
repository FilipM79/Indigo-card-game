package indigo

val cardDeck = mutableListOf<String>()
val startingCardDeck = mutableListOf<String>()

class CardDeck {

    init {
        val cardRanks = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val cardSuits = listOf("♦", "♥", "♠", "♣")
        var cardCounter = 0

        for (i in cardSuits.indices) {
            for (j in cardRanks.indices) {
                cardDeck.add(cardCounter, cardRanks[j] + cardSuits[i])
                cardCounter++
            }
        }
        startingCardDeck.addAll(cardDeck)
    }

    fun operate() {
        var exit = false

        while (!exit) {
            val actions = listOf("reset", "shuffle", "get", "exit")

            println("Choose an action (reset, shuffle, get, exit):")
            print("> ")
            val action = readln()

            if (!actions.contains(action)) {
                println("Wrong action.")
            } else {
                when (action) {
                    "get" -> removeCardsFromDeck()
                    "shuffle" -> shuffleDeck()
                    "reset" -> resetDeck()
                    "exit" -> {
                        println("Bye")
                        exit = true
                    }
                }
            }
        }
    }

    private fun removeCardsFromDeck() {
        println("Number of cards: ")
        print("> ")
        val input = readln()
        var isDigit = true

        for (i in 0..input.lastIndex) if (!input[i].isDigit()) isDigit = false

        if (!isDigit || input.toInt() !in 1..52) {
            println("Invalid number of cards.")
        } else {
            val numberOfCards = input.toInt()
            if (numberOfCards in 1..cardDeck.size) {

                for (i in 0 until numberOfCards) {
                    print("${cardDeck.last()} ")
                    cardDeck.remove(cardDeck.last())
                }
                println()

            } else {
                println("The remaining cards are insufficient to meet the request.")
            }
        }
    }

    private fun shuffleDeck() {
        cardDeck.shuffle()
        println("Card deck is shuffled.")
    }

    private fun resetDeck() {
        cardDeck.clear()
        cardDeck.addAll(startingCardDeck)
        println("Card deck is reset.")
    }
}