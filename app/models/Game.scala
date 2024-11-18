// app/models/Game.scala
package models

case class Game(
  currentWord: String,
  usedWords: Set[String],
  isGameOver: Boolean
) {
  def isValidMove(word: String): Boolean = {
    val lastChar = currentWord.last
    val firstChar = word.charAt(0)
    
    !usedWords.contains(word) && 
    lastChar == firstChar && 
    isJapanese(word)
  }
  
  private def isJapanese(text: String): Boolean = {
    text.matches("^[ぁ-ん]*$")
  }
}

object Game {
  def initialize: Game = Game("しりとり", Set("しりとり"), false)
}
