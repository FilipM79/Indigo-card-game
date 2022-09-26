package indigo

import java.util.*

abstract class GameLogic {
    private var win = false

    fun chooseCard(table: MutableList<String>, cardsInHand: MutableList<String>): String {

        val points = listOf("A", "10", "J", "Q", "K")
        val pointCards = mutableListOf<String>()
        var cardToThrow = "WTF?"
        var message = ""

        var tableCardSign = ""
        var tableCardRank = ""
        if (table.size == 1)  {
            tableCardRank = table[0].substring(0, table[0].lastIndex)
            tableCardSign = table[0].last().toString()
        } else if (table.size > 1) {
            tableCardRank = table.last().substring(0, table.last().lastIndex)
            tableCardSign = table.last().last().toString()
        }

        val candidateCards = mutableListOf<String>()
        val candSignCards = mutableListOf<String>()
        val candRankCards = mutableListOf<String>()
        val samePrefRankCards = mutableListOf<String>()
        val samePrefSignCards = mutableListOf<String>()
        val ranksInHand = mutableListOf<String>()
        val signsInHand = mutableListOf<String>()
        val mapHandBySign = mutableMapOf<String, Int>()
        val mapHandByRank = mutableMapOf<String, Int>()

        for (card in cardsInHand) {
            val cardRank = card.substring(0, card.lastIndex)
            val cardSign = card.last().toString()

            ranksInHand.add(cardRank)
            signsInHand.add(cardSign)
            if (cardSign == tableCardSign) {
                candidateCards.add(card)
                candSignCards.add(card)
            }
            if (cardRank == tableCardRank) {
                candidateCards.add(card)
                candRankCards.add(card)
            }
            if (points.contains(cardRank)) pointCards.add(card)
        }

        for (i in signsInHand.distinct()) mapHandBySign[i] = Collections.frequency(signsInHand, i)
        for (i in ranksInHand.distinct()) mapHandByRank[i] = Collections.frequency(ranksInHand, i)

        val prefSign = mapHandBySign.toList().maxByOrNull { (_, value) -> value }?.first ?: "none"
        val prefRank = mapHandByRank.maxByOrNull { it.value }?.key ?: "none"

        for (card in cardsInHand) {
            val cardRank = card.substring(0, card.lastIndex)
            val cardSign = card.last().toString()
            if (cardSign == prefSign) samePrefSignCards.add(card)
            if (cardRank == prefRank) samePrefRankCards.add(card)
//            if (candidateCards.isNotEmpty()) prefSign = tableCardSign
//            if (candidateCards.isNotEmpty()) prefRank = tableCardRank
        }
        if (samePrefSignCards.size == 1) samePrefSignCards.clear()
        if (samePrefRankCards.size == 1) samePrefRankCards.clear()
        if (candSignCards.size == 1) candSignCards.clear()
        if (candRankCards.size == 1) candRankCards.clear()

        val multipleCandidateCardsRanks = candRankCards.size >= 2
        val multipleCandidateCardsSigns = candSignCards.size >= 2

        val multipleRanks = samePrefRankCards.size >= 2
        val multipleSigns = samePrefSignCards.size >= 2
//        val multipleSignsPrevail = multipleSigns && samePrefSignCards.size >= samePrefRankCards.size

        loop@ for (card in cardsInHand) { // Looping through cards and choosing ...

            val cardSign = card.last().toString()
            val cardRank = card.substring(0, card.lastIndex)
                if (cardsInHand.size == 1) { // 1. Only one card in hand.
                    message = "1. There is only one card in hand"
                    cardToThrow = cardsInHand[0]
                    if (cardSign == tableCardRank || cardRank == tableCardRank) win = true
                    break@loop
                } else if (candidateCards.size == 1) { // 2. Only one candidate card.
                    message = "2. There is only one candidate card"
                    cardToThrow = candidateCards[0]
                    win = true
                    break@loop

                } else if (table.isEmpty()) { // 3. No cards on the table.
                    if (multipleSigns) {
                        message = "3-1. There are more cards in hand with the same suit."
                        cardToThrow = samePrefSignCards.random()
                        break@loop
                    } else if (multipleRanks) {
                        message = "3-2. There are more cards with the same rank"
                        cardToThrow = samePrefRankCards.random()
                        break@loop
                    }  else {
                        message = "3-3. There are no cards with the same suit or rank."
                        cardToThrow = cardsInHand.random()
                    }

                } else if (candidateCards.isEmpty()) { // 4. No candidate cards.
                    if (multipleSigns) {
                        message = "4-1. No candidate cards, but there are more cards with the same suit"
                        cardToThrow = samePrefSignCards.random()
                        break@loop
                    } else if (multipleRanks) {
                        message = "4-2. No candidate cards, but there are more cards with the same rank "
                        cardToThrow = samePrefRankCards.random()
                        break@loop
                    } else {
                        message = "4-3. No candidate cards, and no cards in hand with the same suit or rank"
                        cardToThrow = cardsInHand.random()
                    }

                } else { // 5.
                    if (multipleCandidateCardsSigns) {
                        message = "5-1. There are 2 or more candidate cards - same suit as the card on the table"
                        cardToThrow = candSignCards.random()
                        win = true
                        break@loop
                    } else if (multipleCandidateCardsRanks) {
                        message = "5-2. There are 2 or more candidate cards - same rank as the card on the table"
                        cardToThrow = candRankCards.random()
                        win = true
                        break@loop
                    } else {
                        message = "5-3. Nothing of the above is applicable, then throw any of the candidate cards"
                        cardToThrow = candidateCards.random()
                        win = true
                        break@loop
                    }
                }
            }

        println(cardsInHand.joinToString(" "))

//        println(message)
//        println()
//        println("Candidate cards: $candidateCards")
//        println("Preferred sign: $prefSign, preferred rank: $prefRank")
//        println("Same preferred rank cards: $samePrefRankCards, same preferred sign cards: $samePrefSignCards")
//        println("Multiple ranks: $multipleRanks, multiple signs: $multipleSigns")
//        println("Multiple signs prevail: $multipleSignsPrevail")
//        println()

        return cardToThrow
    }
}