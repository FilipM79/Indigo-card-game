package indigo

val cardDealer = CardDeck()
var gameOver = false

class Game {

    private val player = Player()
    private val computer = Player()
    private lateinit var playFirst: String
    var gameOver: Boolean = false

    fun play() {
        println("Indigo Card Game")

        while (true) {
            print("Play first? \n> ")
            playFirst = readln()
            if (playFirst == "yes" || playFirst == "no" ) {
                break
            }
        }

        Player().putInitialCardsOnTable()
        println()

        while (!gameOver) {
            when (playFirst) {
                "yes" -> playerIsFirst()
                "no" -> computerIsFirst()
            }
        }
        println("Game Over")
    }
    private fun playerIsFirst() {
        if (player.cardsInHand.isEmpty() && !gameOver) player.takeDealtCards()
        if (computer.cardsInHand.isEmpty() && !gameOver) computer.takeDealtCards()
        if (!gameOver) playerPlay()
        if (!gameOver) computerPlay()
    }
    private fun computerIsFirst(){
        if (computer.cardsInHand.isEmpty() && !gameOver) computer.takeDealtCards()
        if (player.cardsInHand.isEmpty() && !gameOver) player.takeDealtCards()
        if (!gameOver) computerPlay()
        if (!gameOver) playerPlay()
    }
    private fun computerPlay() {
        if (cardDealer.cardsOnTheTable.size < 52) {
            println("${cardDealer.cardsOnTheTable.size} cards on the table, and the top card is " +
                        cardDealer.cardsOnTheTable.last())
            println("Computer plays ${computer.cardsInHand.last()}")
            cardDealer.cardsOnTheTable.add(computer.cardsInHand.last())
            computer.cardsInHand.removeLast()
            println()
        } else {
            println("${cardDealer.cardsOnTheTable.size} cards on the table, and the top card is " +
                    cardDealer.cardsOnTheTable.last())
            gameOver = true
        }
    }
    private fun playerPlay() {
        gameOver = if (cardDealer.cardsOnTheTable.size < 52) {
            println("${cardDealer.cardsOnTheTable.size} cards on the table, and the top card is " +
                    cardDealer.cardsOnTheTable.last())
            println("Cards in hand: ${player.handForPrint.joinToString(" ")}")
            player.throwCard()
        } else {
            println("${cardDealer.cardsOnTheTable.size} cards on the table, and the top card is " +
                    cardDealer.cardsOnTheTable.last())
            true
        }
    }
}

