package indigo

fun main() {
    val indigoGame = Game()
    indigoGame.play()
}
class Game {
    var gameOver: Boolean = false

    fun play() {
        val deck = mutableListOf <String>()
        val tableCards =  mutableListOf <String>()
        var playFirst: String

        println("Indigo Card Game")
        Player().unpackNewDeck(deck)

        while (true) {
            print("Play first? \n> ")
            playFirst = readln()
            if (playFirst == "yes" || playFirst == "no" ) {
                break
            }
        }

        Player().shuffleDeck(deck)
        Player().putInitialCardsOnTable(deck, tableCards)
        whichPlayerIsFirst(playFirst, deck, tableCards)

        println("Game Over")
    }
    private fun whichPlayerIsFirst(playFirst: String, deck: MutableList<String>, tableCards: MutableList<String>) {
        val player = Player()
        val computer = Player()

        while (!gameOver) {
            when (playFirst) {
                "yes" -> {
                    if (player.cardsInHand.isEmpty()) player.takeDealtCards(deck)
                    if (computer.cardsInHand.isEmpty()) computer.takeDealtCards(deck)
                    if (!gameOver) playerThrowsCard(computer, tableCards)
                    if (!gameOver) computerThrowsCard(player, tableCards)
                }
                "no" -> {
                    if (computer.cardsInHand.isEmpty()) computer.takeDealtCards(deck)
                    if (player.cardsInHand.isEmpty()) player.takeDealtCards(deck)
                    if (!gameOver) computerThrowsCard(player, tableCards)
                    if (!gameOver) playerThrowsCard(player, tableCards)
                }
            }
        }

    }
    private fun computerThrowsCard(computer: Player, cardsOnTheTable: MutableList<String>) {
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
    private fun playerThrowsCard(player: Player, cardsOnTheTable: MutableList<String>) {
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
        cardsInHand = sixCardsFromDeck(deck)
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
                continue
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
    private fun sixCardsFromDeck(deck: MutableList<String>): MutableList<String> {
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
        print("Initial cards on the table: ")
        for (i in 0 until 4) {
            print("${deck.last()} ")
            cardsOnTheTable.add(deck.last())
            deck.remove(deck.last())
        }
        println()
    }
}
