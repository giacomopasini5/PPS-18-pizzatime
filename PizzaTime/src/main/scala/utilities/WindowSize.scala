package utilities

/** Represents the window size for the Menu and the Game */
object WindowSize extends Enumeration {
  protected case class Val(width: Double, height: Double) extends super.Val {}

  /** Implicit conversion from [[Value]] to [[Val]] */
  implicit def valueToWindowSizeTypeVal(value: Value): Val = value.asInstanceOf[Val]

  val Menu: Val = Val(1200, 800)
  val Game: Val = Val(1200, 800)

}