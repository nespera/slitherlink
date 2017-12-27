package uk.me.chrs.slitherlink

class BoardState(val board: Board, val state: Map[Segment, Option[Boolean]]) {


  override def toString: String = {
    def points(r: Int): String = Seq.fill(board.width + 1)(".").mkString(" ") + "\n"

    def squares(r: Int): String = {
      val row = board.getRow(r)
      " " + row.map(s => s.target.map(_.toString).getOrElse(" ")).mkString(" ") + " " + "\n"
    }

    val s = new StringBuilder
    for (r <- 0 until board.height){
      s.append(points(r))
      s.append(squares(r))
    }
    s.append(points(board.height + 1))
    s.toString()
  }
}

object BoardState {
  def empty(height: Int, width: Int, targets: String): BoardState = {
    val board = new Board(height, width, targets)
    new BoardState(board, board.segments.map(s => s -> None).toMap)
  }
}
