package indigo

fun main() {
    val indigoGame = Game()
    indigoGame.play()
}

class Game {

    var gameOver = false
    private val deck = mutableListOf <String>()
    private val table =  mutableListOf <String>()
    private var playerScore = 0
    private var computerScore = 0
    private var playerCards = 0
    private var computerCards = 0

    private val player = Player()
    private val computer = Player()
    private val computerWinStack = mutableListOf<String>()
    private val playerWinStack = mutableListOf<String>()
    private var totalWinStackSize = 0

    fun play() {

        var playFirst: String

        println("Indigo Card Game")
        Player().unpackNewDeck(deck)

        while (true) {
            print("Play first? \n> ")
            playFirst = readln()
            if (playFirst == "yes" || playFirst == "no") {
                break
            }
        }

        Player().shuffleDeck(deck)
        Player().putInitialCardsOnTable(deck, table)
        orderOfMoves(playFirst)

        if (computerCards > playerCards) computerScore += 3
        if (playerCards > computerCards) playerScore += 3
        println("Score: Player $playerScore - Computer $computerScore")
        println("Cards: Player $playerCards - Computer $computerCards")

        if (playerScore > computerScore) {
            println("Player wins!")
        } else if (computerScore > playerScore) {
            println("Computer wins!")
        } else {
            println("It's a tie!")
        }

        println("Game Over")
    }
    private fun orderOfMoves(playFirst: String) {

        mainLoop@ while (!gameOver) {
            when (playFirst) {
                "yes" -> {
                    playerIsOnTheMove()
                    if (!gameOver) computerIsOnTheMove() else break@mainLoop
                }

                "no" -> {
                    computerIsOnTheMove()
                    if (!gameOver) playerIsOnTheMove() else break@mainLoop
                }
            }
        }
    }
    private fun computerIsOnTheMove() {
        if (computer.cardsInHand.isEmpty()) computer.takeDealtCards(deck)

        gameOver = computerPlay(computer, computerWinStack, playerWinStack.size)
        computerCards = computerWinStack.size
        computerScore = score(computerWinStack)
        totalWinStackSize = computerWinStack.size + playerWinStack.size

        if (table.isEmpty() && totalWinStackSize < 52) {
            println("Computer wins cards")
            println("Score: Player $playerScore - Computer $computerScore")
            println("Cards: Player $playerCards - Computer $computerCards")
        }
    }
    private fun playerIsOnTheMove() {
        if (player.cardsInHand.isEmpty()) player.takeDealtCards(deck)

        gameOver = playerPlay(player, playerWinStack, computerWinStack.size)
        playerCards = playerWinStack.size
        playerScore = score(playerWinStack)
        totalWinStackSize = computerWinStack.size + playerWinStack.size

        if (table.isEmpty() && totalWinStackSize < 52) {
            println("Player wins cards")
            println("Score: Player $playerScore - Computer $computerScore")
            println("Cards: Player $playerCards - Computer $computerCards")
        }
    }
    private fun score (winStack: MutableList<String>): Int {

        var score = 0
        for (card in winStack) {
            val points = listOf("A", "10", "J", "Q", "K")
            val cardRank = card.substring(0, card.lastIndex)

            if (points.contains(cardRank)) score++
        }
        return score
    }
    private fun computerPlay(computer: Player, winStack: MutableList<String>, playerWinStackSize: Int): Boolean {

        var gameOver = false
        val cardToThrow: String

        if (table.isEmpty() && playerWinStackSize + winStack.size < 52) {

            println()
            println("No cards on the table")

            cardToThrow = computer.chooseCard(table, computer.cardsInHand)
            println("Computer plays $cardToThrow")
            table.add(cardToThrow)
            computer.cardsInHand.remove(cardToThrow)

        } else {

            if (playerWinStackSize + winStack.size + table.size < 52) {
                println()
                println(
                    "${table.size} cards on the table, and the top card is " +
                            table.last()
                )

                cardToThrow = computer.chooseCard(table, computer.cardsInHand)
                println("Computer plays $cardToThrow")

                val cardRank = cardToThrow.substring(0, cardToThrow.lastIndex)
                val cardSign = cardToThrow.last()
                val tableRank = table.last().substring(0, table.last().lastIndex)
                val tableSign = table.last().last()

                if (tableRank == cardRank || tableSign == cardSign) {
                    table.add(cardToThrow)
                    winStack.addAll(table)
                    table.clear()
                    computer.cardsInHand.remove(cardToThrow)
                } else {
                    table.add(cardToThrow)
                    computer.cardsInHand.remove(cardToThrow)
                }

            } else {
                gameOver = true
            }
        }
        return gameOver
    }
    private fun playerPlay(player: Player, winStack: MutableList<String>, computerWinStackSize: Int): Boolean {

        val cardToThrow: String
        val tableRank: String
        val tableSign: String
        var gameOver = false

        if (table.isEmpty() && computerWinStackSize + winStack.size < 52) {
            println()
            println("No cards on the table")
            println("Cards in hand: ${player.handForPrint.joinToString(" ")}")

            cardToThrow = player.playerThrowCard()
            if (cardToThrow == "exit") {
                gameOver = true
            } else {
                table.add(cardToThrow)
            }

        } else if (computerWinStackSize + winStack.size + table.size < 52) {
            println()
            println("${table.size} cards on the table, and the top card is ${table.last()}")
            println("Cards in hand: ${player.handForPrint.joinToString(" ")}")

            cardToThrow = player.playerThrowCard()
            if (cardToThrow == "exit") {
                gameOver = true
            } else {
                val cardRank: String = cardToThrow.substring(0, cardToThrow.lastIndex)
                val cardSign: String = cardToThrow.last().toString()
                tableRank = table.last().substring(0, table.last().lastIndex)
                tableSign = table.last().last().toString()

                if (tableRank == cardRank || tableSign == cardSign) {
                    table.add(cardToThrow)
                    winStack.addAll(table)
                    table.clear()
                    player.cardsInHand.remove(cardToThrow)
                } else {
                    table.add(cardToThrow)
                    player.cardsInHand.remove(cardToThrow)
                }
            }
        }
        return gameOver
    }
}

class Player : GameLogic(), CardDealer {
    var cardsInHand = mutableListOf<String>()
    var handForPrint = mutableListOf<String>()

    fun takeDealtCards(deck: MutableList<String>) {
        cardsInHand = sixCardsFromDeck(deck)

        for (index in 0..cardsInHand.lastIndex) {
            handForPrint.add(index, "${index + 1})${cardsInHand[index]}")
        }
    }
    fun playerThrowCard(): String {
        var cardNumber: String
        val cardToThrow: String

        loop@ while (true) {
            println("choose a card to play (1-${cardsInHand.size}):")
            print("> ")
            cardNumber = readln()

            if (cardNumber == "exit") {
                cardToThrow = cardNumber
                Game().gameOver = true
                break@loop

            } else if (cardNumber.isEmpty() || cardNumber.any {!it.isDigit()})  {
                continue

            } else {
                if (cardNumber.toInt() in 1..cardsInHand.size) {
                    val index = cardNumber.toInt() - 1
                    cardToThrow = cardsInHand[index]
                    cardsInHand.removeAt(index)
                    handForPrint.clear()

                    for (i in 0..cardsInHand.lastIndex) {
                        handForPrint.add(i, "${i + 1})${cardsInHand[i]}")
                    }
                    break
                }
            }
        }
        return cardToThrow
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


