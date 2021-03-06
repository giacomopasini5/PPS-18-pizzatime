package gameview.fx.gamesceneelements

import gamelogic.GameState.arena
import gamemanager.ImageLoader.images
import gameview.SpriteAnimation
import gameview.fx.FXGameScene.dungeon
import gameview.fx.gamesceneelements.GameElements.{createElement, pointToPixel, setDimension, tileHeight, tileWidth}
import javafx.application.Platform
import javafx.scene.image.ImageView
import javafx.util.Duration
import utilities.{Direction, Down, HeroImage, Left, Position, Right, Up}

/** [[ImageView]] representing [[Player]]. */
class Player extends GameElements{
  private val player: ImageView = createElement(images(HeroImage))
  private var currentPosition: Position = arena.get.hero.position

  private val AnimDuration = 25
  private val AnimCount = 4
  private val AnimCol = 4
  private val AnimOffX = 0
  private val AnimOffY = 0
  private val AnimWidht = 100
  private val AnimHeight = 130

  val heroAnimation = new SpriteAnimation(
    player,
    Duration.millis(AnimDuration),
    count = AnimCount,
    col = AnimCol,
    offsetX = AnimOffX,
    offsetY = AnimOffY,
    width = AnimWidht,
    height = AnimHeight
  )

  /**
   * Updating position and animate player
   */
  override def update(): Unit = {
    val playerPosition: Position = arena.get.hero.position

    if (!playerPosition.equals(currentPosition)){
      currentPosition = playerPosition

      Platform.runLater(() => {
        animation(playerPosition.dir)
        player.relocate(pointToPixel(playerPosition.point)._1, pointToPixel(playerPosition.point)._2)
      })
    }
  }

  private val OffsetUp = 260
  private val OffsetDown = 0
  private val OffsetLeft = 130
  private val OffsetRight = 390

  protected def animation(playerDirection: Option[Direction]): Unit = playerDirection match {
    case Some(Up) =>
      heroAnimation.offsetY = OffsetUp
      heroAnimation.play()

    case Some(Down) =>
      heroAnimation.offsetY = OffsetDown
      heroAnimation.play()

    case Some(Left) =>
      heroAnimation.offsetY = OffsetLeft
      heroAnimation.play()

    case Some(Right) =>
      heroAnimation.offsetY = OffsetRight
      heroAnimation.play()
    case _ => None
  }
}

object Player{
  /** Creates a [[Player]].
   *
   *  @return the new [[Player]] instance
   */
  def apply(): Player = {
    val p: Player = new Player()

    setDimension(p.player, tileHeight, tileWidth)
    p.animation(arena.get.hero.position.dir)

    Platform.runLater(() => {
      p.player relocate(pointToPixel(arena.get.hero.position.point)._1, pointToPixel(arena.get.hero.position.point)._2)
      dungeon.getChildren.add(p.player)
    })

    p
  }
}
