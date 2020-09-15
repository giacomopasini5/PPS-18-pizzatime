package gameview.fx.gamesceneelements

import gamelogic.GameState.arena
import gamemanager.ImageLoader.images
import gameview.fx.FXGameScene.{createTile, dungeon, pointToPixel}
import javafx.application.Platform
import javafx.scene.image.ImageView
import javafx.scene.layout.GridPane
import utilities.{FloorImage, WallImage}

/** [[GridPane]] representing [[ArenaRoom]]*/
class ArenaRoom extends GameElements {
  /**
   * Checks if the door should be opened
   */
  override def update(): Unit = createDoor()

  val door: ImageView = createTile(images(FloorImage))
  var positionDoor: (Double, Double) = _

  private def createDoor(): Unit = {
    Platform.runLater(() => {
      if(arena.get.door.isDefined && !dungeon.getChildren.contains(door)) {
        dungeon.getChildren.add(door)
        positionDoor = (pointToPixel(arena.get.door.get.position.point)._1, pointToPixel(arena.get.door.get.position.point)._2)
        door.relocate(positionDoor._1, positionDoor._2)
      } else if(arena.get.door.isEmpty && dungeon.getChildren.contains(door)){
        dungeon.getChildren.remove(door)
        val wall: ImageView = createTile(images(WallImage))
        wall.relocate(positionDoor._1, positionDoor._2)
        dungeon.getChildren.add(wall)
      }
    })
  }

  /**
   * Draws entities within the game arena
   *
   * @return a new [[GridPane]] with all the game entities initialized to be displayed in the view
   */
  private def createArena(): Unit = {
    for (f <- arena.get.floor) addToDungeon (f, images(FloorImage))
    for (w <- arena.get.walls) addToDungeon (w, images(WallImage))
    for (o <- arena.get.obstacles) addToDungeon (o, images(o.`type`))

    createDoor()
  }

}

object ArenaRoom{
  /** Creates a [[ArenaRoom]].
   *
   *  @return the new [[ArenaRoom]] instance
   */
  def apply(): ArenaRoom = {
    val arena: ArenaRoom = new ArenaRoom()
    arena.createArena()
    arena
  }
}
