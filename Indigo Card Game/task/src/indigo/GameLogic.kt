package indigo

abstract class GameLogic {
    private var win = false

    fun chooseCard(table: MutableList<String>, cardsInHand: MutableList<String>): String {
        val points = listOf("A", "10", "J", "Q", "K")
        var cardToThrow = cardsInHand.last()

        if (table.isEmpty()) {
            for (k in 0..cardsInHand.lastIndex) {
                val cardRank = cardsInHand[k].substring(0, cardsInHand[k].lastIndex)

                if (!points.contains(cardRank)) {
                    cardToThrow = cardsInHand[k]

                    if (cardToThrow.substring(0, cardToThrow.lastIndex) < cardRank) {
                        cardToThrow = cardsInHand[k]
                    } else {
                        continue
                    }

                } else {
                    cardToThrow = cardsInHand.last()
                }
            }

        }  else {
            val tableCardRank: String
            val tableCardSign: String

            if (table.size == 1) {
                tableCardRank = table[0].substring(0, table[0].lastIndex)
                tableCardSign = table[0].last().toString()
            } else {
                tableCardRank = table.last().substring(0, table.last().lastIndex)
                tableCardSign = table.last().last().toString()
            }
            val pointCardOnTable = points.contains(tableCardRank)

            loop@ for (i in 0..cardsInHand.lastIndex) {
                val cardRank = cardsInHand[i].substring(0, cardsInHand[i].lastIndex)
                val cardSign = cardsInHand[i].last().toString()
                val sameRank = cardRank == tableCardRank
                val sameSign = cardSign == tableCardSign

                if (pointCardOnTable && sameRank) {
                    if (cardToThrow.isNotEmpty() && points.contains(cardToThrow)) {
                        continue
                    } else {
                        win = true
                        cardToThrow = cardsInHand[i]
                        break@loop
                    }
                } else if (sameSign || sameRank) {
                    if (win && !points.contains(cardToThrow)) {
                        continue
                    } else {
                        win = true
                        cardToThrow = cardsInHand[i]
                        break@loop
                    }
                } else {
                    if (win) {
                        continue
                    } else {
                        win = false

                        for (j in 0..cardsInHand.lastIndex) {
                            if (!points.contains(cardRank)) {

                                cardToThrow = cardsInHand.last()
                                if (cardToThrow.substring(0, cardToThrow.lastIndex) < cardRank) {
                                    cardToThrow = cardsInHand[j]
                                } else {
                                    continue
                                }

                            } else {
                                cardToThrow = cardsInHand.last()
                            }
                        }
                    }
                }
            }
        }
        return cardToThrow
    }
}