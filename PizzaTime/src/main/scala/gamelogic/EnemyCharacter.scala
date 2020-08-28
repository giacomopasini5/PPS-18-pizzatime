package gamelogic

/** Represents an enemy character.
 *  Implemented by [[Enemy]].
 */
trait EnemyCharacter extends MovableEntity {
  def lives: Int
  def pointsKilling: Int
  def movementBehaviour(): Boolean
  def decreaseLife(): Unit
}