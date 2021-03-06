package utilities

/** Allows to load the various images of the game.
 *  The images include:
 *    Floor
 *    Wall
 *    Hero
 *    Enemy
 *    Bullet
 *    Bonus life
 *    Bonus score
 *    Life bars
 *    Table
 *    Stove
 *    Sink
 */
sealed trait ImageType

case object FloorImage extends ImageType { val path: String = "/images/textures/tile.png" }
case object WallImage extends ImageType { val path: String = "/images/textures/wallKitchen.png" }
case object HeroImage extends ImageType { val path: String = "/images/sprites/hero.png" }
case object EnemyImage extends ImageType { val path: String = "/images/sprites/enemy.png" }
case object BulletImage extends ImageType { val path: String = "/images/sprites/bullet.png" }
case object BonusLifeImage extends ImageType { val path: String = "/images/sprites/bonusLife.png" }
case object BonusScoreImage extends ImageType { val path: String = "/images/sprites/bonusScore.png" }
case object LifeBarImage5 extends ImageType { val path: String = "/images/sprites/lifeBar/life5.png" }
case object LifeBarImage4 extends ImageType { val path: String = "/images/sprites/lifeBar/life4.png" }
case object LifeBarImage3 extends ImageType { val path: String = "/images/sprites/lifeBar/life3.png" }
case object LifeBarImage2 extends ImageType { val path: String = "/images/sprites/lifeBar/life2.png" }
case object LifeBarImage1 extends ImageType { val path: String = "/images/sprites/lifeBar/life1.png" }
case object LifeBarImage0 extends ImageType { val path: String = "/images/sprites/lifeBar/life0.png" }
case object TableImage extends ImageType { val path: String = "/images/sprites/obstacles/obstacle1.png" }
case object StoveImage extends ImageType { val path: String = "/images/sprites/obstacles/obstacle2.png" }
case object SinkImage extends ImageType { val path: String = "/images/sprites/obstacles/obstacle3.png" }