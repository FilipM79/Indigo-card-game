package indigo

fun main() {
    val indigoGame = Game()
    indigoGame.play()
}
class Game {
    private val deck = mutableListOf <String>()
    private val cardsOnTheTable =  mutableListOf <String>()
    private val player = Player()
    private val computer = Player()
    private lateinit var playFirst: String
    var gameOver: Boolean = false

    fun play() {
        println("Indigo Card Game")
        player.unpackNewDeck(deck)

        while (true) {
            print("Play first? \n> ")
            playFirst = readln()
            if (playFirst == "yes" || playFirst == "no" ) {
                break
            }
        }

        player.putInitialCardsOnTable(deck, cardsOnTheTable)
        while (!gameOver) {
            when (playFirst) {
                "yes" -> playerIsFirst()
                "no" -> computerIsFirst()
            }
        }
        println("Game Over")
    }
    private fun playerIsFirst() {
        if (player.cardsInHand.isEmpty()) player.takeDealtCards(deck)
        if (computer.cardsInHand.isEmpty()) computer.takeDealtCards(deck)
        if (!gameOver) playerThrowsCard()
        if (!gameOver) computerThrowsCard()
    }
    private fun computerIsFirst() {
        if (computer.cardsInHand.isEmpty()) computer.takeDealtCards(deck)
        if (player.cardsInHand.isEmpty()) player.takeDealtCards(deck)
        if (!gameOver) computerThrowsCard()
        if (!gameOver) playerThrowsCard()
    }
    private fun computerThrowsCard() {
        if (cardsOnTheTable.size < 52) {
            println()
            println("${cardsOnTheTable.size} cards on the table, and the top card is " +
                    cardsOnTheTable.last())
            println("Computer plays ${computer.cardsInHand.last()}")
            cardsOnTheTable.add(computer.cardsInHand.last())
            computer.cardsInHand.removeLast()
        } else {
            println()
            println("${cardsOnTheTable.size} cards on the table, and the top card is " +
                    cardsOnTheTable.last())
            gameOver = true
        }
    }
    private fun playerThrowsCard() {
        gameOver = if (cardsOnTheTable.size < 52) {
            println()
            println("${cardsOnTheTable.size} cards on the table, and the top card is " +
                    cardsOnTheTable.last())
            println("Cards in hand: ${player.handForPrint.joinToString(" ")}")
            player.throwCard(cardsOnTheTable)
        } else {
            println()
            println("${cardsOnTheTable.size} cards on the table, and the top card is " +
                    cardsOnTheTable.last())
            true
        }
    }
}

class Player : CardDealer {
    var cardsInHand = mutableListOf<String>()
    var handForPrint = mutableListOf<String>()

    fun takeDealtCards(deck: MutableList<String>) {
        cardsInHand = takeSixCardsFromDeck(deck)
        for (index in 0..cardsInHand.lastIndex) {
            handForPrint.add(index, "${index + 1})${cardsInHand[index]}")
        }
    }
    fun throwCard(cardsOnTheTable: MutableList<String>): Boolean {
        var cardNumber: String
        var condition = Game().gameOver

        while (true) {
            println("choose a card to play (1-${cardsInHand.size}):")
            print("> ")
            cardNumber = readln()

            if (cardNumber == "exit") {
                condition = true
                break
            } else if (cardNumber.isEmpty() || cardNumber.any {!it.isDigit()})  {
                condition = false
            } else {
                if (cardNumber.toInt() in 1..cardsInHand.size) {
                    val index = cardNumber.toInt() - 1
                    cardsOnTheTable.add(cardsInHand[index])
                    cardsInHand.removeAt(index)
                    handForPrint.clear()
                    for (i in 0..cardsInHand.lastIndex) {
                        handForPrint.add(i, "${i + 1})${cardsInHand[i]}")
                    }
                    break
                }
            }
        }
        return condition
    }
    private fun takeSixCardsFromDeck(deck: MutableList<String>): MutableList<String> {
        val removedCards = mutableListOf<String>()
        for (i in 0 until 6) {
            if (deck.size > 0) {
                removedCards.add(deck.last())
                deck.remove(deck.last())
            }
        }
        return removedCards
    }
}

interface CardDealer {
    fun unpackNewDeck(deck: MutableList<String>) {
        val cardRanks = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
        val cardSuits = listOf("♦", "♥", "♠", "♣")
        var cardCounter = 0

        for (i in cardSuits.indices) {
            for (j in cardRanks.indices) {
                deck.add(cardCounter, cardRanks[j] + cardSuits[i])
                cardCounter++
            }
        }
    }
    fun shuffleDeck(deck: MutableList<String>) {
        deck.shuffle()
    }
    fun putInitialCardsOnTable(deck: MutableList<String>, cardsOnTheTable: MutableList<String>) {
        shuffleDeck(deck)
        print("Initial cards on the table: ")
        for (i in 0 until 4) {
            print("${deck.last()} ")
            cardsOnTheTable.add(deck.last())
            deck.remove(deck.last())
        }
        println()
    }
}


