package utilities

import alice.tuprolog.{NoMoreSolutionException, Prolog, SolveInfo, Struct, Term, Theory}
import gamelogic.GameState.arena
import net.liftweb.json.DefaultFormats
import net.liftweb.json.JsonAST.JValue

object Scala2P {

  def extractTerm(t:Term, i:Integer): Term = t.asInstanceOf[Struct].getArg(i).getTerm

  implicit def stringToTerm(s: String): Term = Term.createTerm(s)
  implicit def seqToTerm[T](s: Seq[T]): Term = s.mkString("[",",","]")

  def nonWalkableTiles: Term = prologSeq(arena.get.walls.map(w => prologTuple(w.position.point.x, w.position.point.y)).toSeq
    ++ arena.get.obstacles.map(o => prologTuple(o.position.point.x, o.position.point.y)).toSeq
    ++ arena.get.enemies.map(e => prologTuple(e.position.point.x, e.position.point.y))
    ++ arena.get.collectibles.map(c => prologTuple(c.position.point.x, c.position.point.y)))

  def prologGetPoint(clauses: String*): Term => Seq[Option[Point]] = {
    goal => new Iterable[Option[Point]]{
      val engine = new Prolog
      engine.setTheory(new Theory(clauses mkString " "))

      override def iterator: Iterator[Option[Point]] = new Iterator[Option[Point]]{
        var solution: Option[SolveInfo] = Some(engine.solve(goal))

        override def hasNext: Boolean = solution.isDefined && (solution.get.isSuccess || solution.get.hasOpenAlternatives)

        override def next(): Option[Point] =
          try {
            if(solution.isDefined) {
              val obj: JValue = net.liftweb.json.parse(solution.get.getTerm("Point").toJSON)
              implicit val format = DefaultFormats

              val element = (obj \\ "value").children
              Some((for(List(a,b) <- element.combinations(2).toList) yield Point(a.extract[Int], b.extract[Int])).head)
            } else None
          } finally {
            try {
              solution = Some(engine.solveNext)
            } catch {
              case _: NoMoreSolutionException => solution = None
            }
          }
      }
    }.toSeq
  }

  def prologGetPosition(clauses: String*): Term => Seq[Option[Position]] = {
    goal => new Iterable[Option[Position]]{
      val engine = new Prolog
      engine.setTheory(new Theory(clauses mkString " "))

      override def iterator: Iterator[Option[Position]] = new Iterator[Option[Position]]{
        var solution: Option[SolveInfo] = Some(engine.solve(goal))

        override def hasNext: Boolean = solution.isDefined && (solution.get.isSuccess || solution.get.hasOpenAlternatives)

        override def next(): Option[Position] =
          try {
            if(solution.isDefined) {
              val obj: JValue = net.liftweb.json.parse(solution.get.getTerm("Point").toJSON)
              implicit val format = DefaultFormats

              val element = (obj \\ "value").children
              val point = (for(List(a,b) <- element.combinations(2).toList) yield Point(a.extract[Int], b.extract[Int])).head

              solution.get.getTerm("Dir").toString match {
                case "right" => Some(Position(point, Some(Right)))
                case "left" => Some(Position(point, Some(Left)))
              }
            } else None
          } finally {
            try {
              solution = Some(engine.solveNext)
            } catch {
              case _: NoMoreSolutionException => solution = None
            }
          }
      }
    }.toSeq
  }

  def prologInt(i: Int): alice.tuprolog.Int = new alice.tuprolog.Int(i)

  def prologTuple(t1: Int, t2: Int): Term =
    Term createTerm t1 + "," + t2

  def prologSeq(s: Seq[Term]): Term = s.mkString("[",",","]")

  val engineForLeftRightMove: Term => Seq[Option[Position]] = prologGetPosition("""
      move(X1, Y1, right, XY) :- X2 is X1+1, XY = (X2,Y1).
      move(X1, Y1, left, XY) :- X2 is X1-1, XY = (X2,Y1).

      calc_point(X, Y, Non_walkable_tiles, right, Point, Dir) :- move(X,Y, right, XY), (member(XY, Non_walkable_tiles) -> calc_point(X, Y, Non_walkable_tiles,left, Point, Dir); (Point = XY, Dir = right)).
			calc_point(X, Y, Non_walkable_tiles, left, Point, Dir) :- move(X,Y, left, XY), (member(XY, Non_walkable_tiles) -> calc_point(X, Y, Non_walkable_tiles,right, Point, Dir); (Point = XY, Dir = left)).
			""")

  val engineForRandomMove: Term => Seq[Option[Point]] = prologGetPoint("""
	    move(X1, Y1, 0, XY) :- X2 is X1+1, XY = (X2,Y1).
      move(X1, Y1, 1, XY) :- X2 is X1-1, XY = (X2,Y1).
      move(X1, Y1, 2, XY) :- Y2 is Y1+1, XY = (X1,Y2).
      move(X1, Y1, 3, XY) :- Y2 is Y1-1, XY = (X1,Y2).

      calc_point(X, Y, Non_walkable_tiles, Point) :- rand_int(4, Rnd), move(X,Y, Rnd, XY), (member(XY, Non_walkable_tiles) -> calc_point(X, Y, Non_walkable_tiles, XY); Point = XY).
""")


}


/*
object TryScala2P extends App {
  import Scala2P._

  val engine: Term => Seq[Option[Point]] = prolog("""
    moveAlt(X1,Y1,X2,Y2) :- X2 is X1+1, Y2 is Y1.
    moveAlt(X1,Y1,X2,Y2) :- X2 is X1-1, Y2 is Y1.
    moveAlt(X1,Y1,X2,Y2) :- Y2 is Y1+1, X2 is X1.
    moveAlt(X1,Y1,X2,Y2) :- Y2 is Y1-1, X2 is X1.
  """)

  engine("moveAlt(1,1,X,Y)").foreach(a=> println(a))
}*/



