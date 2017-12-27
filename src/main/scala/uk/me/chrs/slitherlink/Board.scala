package uk.me.chrs.slitherlink

class Board(width: Int, height: Int) {
  val squares: Set[Square] = (for { x <- 0 until width; y <- 0 until height } yield Square(x, y)).toSet
  val points: Set[Point] = (for { x <- 0 to width; y <- 0 to height } yield Point(x, y)).toSet
  val segments: Set[Segment] = points.flatMap(p => points.filter(p.adjacent).map(Segment(p, _)))
}

case class Square(x: Int, y: Int)
case class Point(x: Int, y: Int){
  def adjacent(other: Point) : Boolean = {
    (x == other.x && Math.abs(y-other.y) == 1) || (y == other.y && Math.abs(x-other.x) == 1)
  }
}
case class Segment(a: Point, b: Point)