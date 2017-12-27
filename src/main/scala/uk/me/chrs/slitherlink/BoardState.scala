package uk.me.chrs.slitherlink

class BoardState(val board: Board, segmentStates: Map[Segment, Option[Boolean]]) {

  override def toString: String = {
    def points(row: Int): String = {
      horizontals(row).map(makeSegmentString("_", _))
        .mkString(".", ".", ".\n")
    }

    def squares(row: Int): String = {
      val values = board.getRow(row).map(s => s.target.map(_.toString).getOrElse(" ")) :+ "\n"
      val verts = verticals(row).map(makeSegmentString("|", _))
      intersperse(verts, values).mkString
    }

    val s = new StringBuilder
    for (r <- 0 until board.height){
      s.append(points(r))
      s.append(squares(r))
    }
    s.append(points(board.height))
    s.toString()
  }

  private def makeSegmentString(str: String, s: Segment) = if (segmentStates(s).contains(true)) str else " "

  private def verticals(row: Int) = {
    board.segments.filter(s => s.points.exists(_.x == row) && s.points.exists(_.x == row + 1)).toSeq
      .sortBy(_.points.head.y)
  }

  private def horizontals(row: Int) = {
    board.segments.filter(s => s.points.forall(_.x == row)).toSeq
      .sortBy(_.points.head.y)
  }

  def updated(a: Point, b: Point, state: Option[Boolean]): BoardState = {
    new BoardState(board, segmentStates.updated(Segment(a, b), state))
  }

  private def intersperse(l1: Seq[String], l2: Seq[String]) = l1.zip(l2) flatMap { case (a, b) => Seq(a, b) }
}

object BoardState {
  def empty(height: Int, width: Int, targets: String): BoardState = {
    val board = new Board(height, width, targets)
    new BoardState(board, board.segments.map(s => s -> None).toMap)
  }
}
