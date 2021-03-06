package uk.me.chrs.slitherlink

/*
 Coordinates on the board run as follows:

 (0,0) ..... (0,n)
  .           .
  .           .
 (n,0) ..... (n,n)

 */

class Board(val height: Int, val width: Int, targets: String) {

  if(targets.length != width * height) throw new IllegalArgumentException(s"Targets string is of length ${targets.length}, expected ${width * height}")
  val squares: Set[Square] = (for { x <- 0 until height; y <- 0 until width } yield Square(x, y, targetAt(x,y))).toSet
  val points: Set[Point] = (for { x <- 0 to height; y <- 0 to width } yield Point(x, y)).toSet
  val segments: Set[Segment] = points.flatMap(p => points.filter(p.adjacent).map(Segment(p, _)))

  def getRow(r: Int): Seq[Square] = squares.filter(_.x == r).toSeq.sortBy(_.y)

  def segmentsFor(p: Point): Seq[Segment] = segments.filter(_.contains(p)).toSeq
  def segmentsFor(sq: Square): Seq[Segment] = segments.filter(sq.contains).toSeq

  private def targetAt(x: Int, y: Int): Option[Int] = {
    targets(x*width + y) match {
      case '0' => Some(0)
      case '1' => Some(1)
      case '2' => Some(2)
      case '3' => Some(3)
      case '.' => None
      case c => throw  new IllegalArgumentException(s"Bad target character: $c")
    }
  }
}

case class Square(x: Int, y: Int, target: Option[Int]) {
  def contains(s: Segment): Boolean = {
    s.points.forall(contains)
  }
  def contains(p: Point): Boolean = {
    (p.x == x || p.x == x + 1) && (p.y == y || p.y == y + 1)
  }
}
case class Point(x: Int, y: Int){
  def adjacent(other: Point) : Boolean = {
    (x == other.x && Math.abs(y-other.y) == 1) || (y == other.y && Math.abs(x-other.x) == 1)
  }
}
case class Segment(points: Set[Point]){
  if(points.size != 2) throw new IllegalArgumentException("Segment must have exactly two points")
  def contains(p: Point): Boolean = points.contains(p)
}
object Segment{
  def apply(a: Point, b: Point): Segment = Segment(Set(a,b))
}