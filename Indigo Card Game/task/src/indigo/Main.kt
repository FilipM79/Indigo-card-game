package indigo

fun main() {

    val indigoGame = Game()
    indigoGame.play()
}

class Game {

    var gameOver = false
    private val deck = mutableListOf <String>()
    private val table = mutableListOf <String>()

    private var whoWonLast = ""
    private var playFirst = ""

    fun play() {

        println("Indigo Card Game")
        Player().unpackNewDeck(deck)

        while (true) {
            print("Play first? \n> ")
            playFirst = readln()
//            playFirst = "no"
            if (playFirst == "yes" || playFirst == "no") {
                break
            }
        }

        Player().shuffleDeck(deck = deck)
        Player().shuffleDeck(deck = deck)
        Player().putInitialCardsOnTable(deck = deck, table = table)

        val player = Player()
        val computer = Player()

        orderOfMoves(player = player, computer = computer)

        if (computer.winStack.size > player.winStack.size )  {
            computer.score += 3
        } else if (player.winStack.size  > computer.winStack.size ){
            player.score += 3
        } else {
            when(playFirst) {
                "yes" -> player.score += 3
                "no" -> computer.score += 3
            }
        }

        if (player.cardToThrow != "exit" && computer.cardToThrow != "exit") {
            println()
            if (table.isEmpty()) {
                println("No cards on the table")
            } else {
                println("${table.size} cards on the table, and the top card is ${table.last()}")
            }
            println("Score: Player ${player.score} - Computer ${computer.score}")
            println("Cards: Player ${player.winStack.size} - Computer ${computer.winStack.size}")
        }
        println("Game Over")
    }
    private fun orderOfMoves(player: Player, computer: Player) {

        mainLoop@ while (true) {
            when (playFirst) {
                "yes" -> {
                    if (!gameOver) playerIsOnTheMove(player = player, computer = computer) else break@mainLoop
                    if (!gameOver) computerIsOnTheMove(player = player, computer = computer) else break@mainLoop
                }

                "no" -> {
                    if (!gameOver) computerIsOnTheMove(player = player, computer = computer) else break@mainLoop
                    if (!gameOver) playerIsOnTheMove(player = player, computer = computer) else break@mainLoop

                }
            }
        }
    }
    private fun computerIsOnTheMove(player: Player, computer: Player) {
        if (computer.cardsInHand.isEmpty()) computer.takeDealtCards(deck)

        gameOver = computerPlay(player = player, computer = computer)
        computer.score = score(computer.winStack)
        val totalWinStackSize = computer.winStack.size + player.winStack.size + table.size

        if (table.isEmpty() && totalWinStackSize < 52 && computer.cardToThrow != "exit") {
            println("Computer wins cards")
            println("Score: Player ${player.score} - Computer ${computer.score}")
            println("Cards: Player ${player.winStack.size} - Computer ${computer.winStack.size}")
        }
    }
    private fun playerIsOnTheMove(player: Player, computer: Player) {

        if (player.cardsInHand.isEmpty()) player.takeDealtCards(deck)

        gameOver = playerPlay(player = player, computer = computer)
        player.score = score(winStack = player.winStack)
        val totalWinStackSize = computer.winStack.size + player.winStack.size + table.size

        if (table.isEmpty() && totalWinStackSize < 52 && player.cardToThrow != "exit") {
            println("Player wins cards")
            println("Score: Player ${player.score} - Computer ${computer.score}")
            println("Cards: Player ${player.winStack.size} - Computer ${computer.winStack.size}")
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
    private fun computerPlay(player: Player, computer: Player): Boolean {

        var gameOver = false
        val cardRank: String
        val cardSign: String
        val tableRank: String
        val tableSign: String

        if (table.isEmpty() && player.winStack.size + computer.winStack.size < 52 && computer.cardToThrow != "exit") {

            println()
            println("No cards on the table")

            computer.cardToThrow = computer.chooseCard(table = table, cardsInHand = computer.cardsInHand)
            println("Computer plays ${computer.cardToThrow}")

            table.add(computer.cardToThrow)
            computer.cardsInHand.remove(computer.cardToThrow)

        } else if (player.winStack.size + computer.winStack.size + table.size < 52 && computer.cardToThrow != "exit") {
            println()
            println("${table.size} cards on the table, and the top card is ${table.last()}")

            computer.cardToThrow = computer.chooseCard(table = table, cardsInHand = computer.cardsInHand)
            println("Computer plays ${computer.cardToThrow}")

            cardRank = computer.cardToThrow.substring(0, computer.cardToThrow.lastIndex)
            cardSign = computer.cardToThrow.last().toString()
            tableRank = table.last().substring(0, table.last().lastIndex)
            tableSign = table.last().last().toString()

            if (tableRank == cardRank || tableSign == cardSign) {
                table.add(computer.cardToThrow)
                computer.winStack.addAll(table)
                table.clear()
                computer.cardsInHand.remove(computer.cardToThrow)
                whoWonLast = "computer"
            } else {
                table.add(computer.cardToThrow)
                computer.cardsInHand.remove(computer.cardToThrow)
            }


        } else if ((player.cardsInHand.size <= 1 || computer.cardsInHand.size <= 1) && deck.size == 0) {

            if(whoWonLast == "computer") {
                computer.winStack.addAll(table)
                gameOver = true

            } else if (whoWonLast == "player") {
                player.winStack.addAll(table)
                gameOver = true

            } else if (playFirst == "yes") {
                player.winStack.addAll(table)
                gameOver = true

            } else if (playFirst == "no") {
                computer.winStack.addAll(table)
                gameOver = true
            }
        }
        return gameOver
    }
    private fun playerPlay(player: Player, computer: Player): Boolean {

        val tableRank: String
        val tableSign: String
        val cardRank: String
        val cardSign: String
        var gameOver = false
        val stack = computer.winStack.size + player.winStack.size

        if (table.isEmpty() && stack < 52) {
            println()
            println("No cards on the table")
            println("Cards in hand: ${player.handForPrint.joinToString(" ")}")

            player.cardToThrow = player.playerThrowCard()

            if (player.cardToThrow == "exit") {
                gameOver = true
            } else {
                table.add(player.cardToThrow)
            }

        } else if (stack + table.size < 52) {
            println()
            println("${table.size} cards on the table, and the top card is ${table.last()}")
            println("Cards in hand: ${player.handForPrint.joinToString(" ")}")

            player.cardToThrow = player.playerThrowCard()

            if (player.cardToThrow == "exit") {
                gameOver = true
            } else {
                cardRank = player.cardToThrow.substring(0, player.cardToThrow.lastIndex)
                cardSign = player.cardToThrow.last().toString()
                tableRank = table.last().substring(0, table.last().lastIndex)
                tableSign = table.last().last().toString()

                if (tableRank == cardRank || tableSign == cardSign) {
                    table.add(player.cardToThrow)
                    player.winStack.addAll(table)
                    table.clear()
                    player.cardsInHand.remove(player.cardToThrow)
                    whoWonLast = "player"
                } else {
                    table.add(player.cardToThrow)
                    player.cardsInHand.remove(player.cardToThrow)
                }
            }

        } else if ((computer.cardsInHand.size <= 1 || player.cardsInHand.size <= 1) && deck.isEmpty()) {

            if(whoWonLast == "player") {
                player.winStack.addAll(table)
                gameOver = true

            } else if (whoWonLast == "computer") {
                computer.winStack.addAll(table)
                gameOver = true

            } else if (playFirst == "yes") {
                player.winStack.addAll(table)
                gameOver = true

            } else if (playFirst == "no") {
                computer.winStack.addAll(table)
                gameOver = true
            }
        }
        return gameOver
    }
}

class Player : GameLogic(), CardDealer {
    var cardsInHand = mutableListOf<String>()
    var handForPrint = mutableListOf<String>()
    var score = 0
    val winStack = mutableListOf<String>()
    var cardToThrow = ""

    fun takeDealtCards(deck: MutableList<String>) {
        cardsInHand = sixCardsFromDeck(deck = deck)

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

            } else if (cardNumber.any {it.isDigit()})  {
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
            } else {
                continue
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


