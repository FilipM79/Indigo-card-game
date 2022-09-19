package indigo

var gameOver = false
val cardDealer = CardDealer()

fun main() {
    val indigo = Game()
    indigo.play()
}
class Game {
    private val player = Player()
    private val computer = Player()
    private lateinit var playFirst: String
    private var gameOver: Boolean = false

    fun play() {
        println("Indigo Card Game")

        while (true) {
            print("Play first? \n> ")
            playFirst = readln()
            if (playFirst == "yes" || playFirst == "no" ) {
                break
            }
        }

        cardDealer.putInitialCardsOnTable()

        while (!gameOver) {
            when (playFirst) {
                "yes" -> playerIsFirst()
                "no" -> computerIsFirst()
            }
        }
        println("Game Over")
    }
    private fun playerIsFirst() {
        if (player.cardsInHand.isEmpty()) player.takeDealtCards()
        if (computer.cardsInHand.isEmpty()) computer.takeDealtCards()
        if (!gameOver) playerPlay()
        if (!gameOver) computerPlay()
    }
    private fun computerIsFirst(){
        if (computer.cardsInHand.isEmpty()) computer.takeDealtCards()
        if (player.cardsInHand.isEmpty()) player.takeDealtCards()
        if (!gameOver) computerPlay()
        if (!gameOver) playerPlay()
    }
    private fun computerPlay() {
        if (cardDealer.cardsOnTheTable.size < 52) {
            println()
            println("${cardDealer.cardsOnTheTable.size} cards on the table, and the top card is " +
                    cardDealer.cardsOnTheTable.last())
            println("Computer plays ${computer.cardsInHand.last()}")
            cardDealer.cardsOnTheTable.add(computer.cardsInHand.last())
            computer.cardsInHand.removeLast()
        } else {
            println()
            println("${cardDealer.cardsOnTheTable.size} cards on the table, and the top card is " +
                    cardDealer.cardsOnTheTable.last())
            gameOver = true
        }
    }
    private fun playerPlay() {
        gameOver = if (cardDealer.cardsOnTheTable.size < 52) {
            println()
            println("${cardDealer.cardsOnTheTable.size} cards on the table, and the top card is " +
                    cardDealer.cardsOnTheTable.last())
            println("Cards in hand: ${player.handForPrint.joinToString(" ")}")
            player.throwCard()
        } else {
            println()
            println("${cardDealer.cardsOnTheTable.size} cards on the table, and the top card is " +
                    cardDealer.cardsOnTheTable.last())
            true
        }
    }
}
class Player: CardDealer() {

    var cardsInHand = mutableListOf<String>()
    var handForPrint = mutableListOf<String>()

    fun takeDealtCards() {
        cardsInHand = cardDealer.takeSixCardsFromDeck()
        for (index in 0..cardsInHand.lastIndex) {
            handForPrint.add(index, "${index + 1})${cardsInHand[index]}")
        }
    }
    fun throwCard(): Boolean {
        var cardNumber: String
        var condition = gameOver

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
                    cardDealer.cardsOnTheTable.add(cardsInHand[index])
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
}
open class CardDealer {
    private val deck = mutableListOf<String>()
    private val startingDeck = mutableListOf<String>()
    val cardsOnTheTable = mutableListOf<String>()
    init {
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


