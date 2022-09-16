package indigo

class CardDeck {
    val deck = mutableListOf<String>()
    private val startingDeck = mutableListOf<String>()
    val cardsOnTheTable = mutableListOf<String>()
    init {
        println("Indigo Card Game")
        val cardRanks = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val cardSuits = listOf("♦", "♥", "♠", "♣")
        var cardCounter = 0

        for (i in cardSuits.indices) {
            for (j in cardRanks.indices) {
                deck.add(cardCounter, cardRanks[j] + cardSuits[i])
                cardCounter++
            }
        }
        startingDeck.addAll(deck)
    }
    private fun shuffleDeck() {
        deck.shuffle()
    }
    fun takeSixCardsFromDeck(): MutableList<String> {
        val removedCards = mutableListOf<String>()
        for (i in 0 until 6) {
            if (deck.size > 0) {
                removedCards.add(deck.last())
                deck.remove(deck.last())
            }
        }
        return removedCards
    }
    fun putInitialCardsOnTable() {
        shuffleDeck()
        print("Initial cards on the table: ")
        for (i in 0 until 4) {
            print("${deck.last()} ")
            cardsOnTheTable.add(deck.last())
            deck.remove(deck.last())
        }
        println()
    }
}