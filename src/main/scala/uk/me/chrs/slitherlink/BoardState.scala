package uk.me.chrs.slitherlink

class BoardState(val board: Board, segmentStates: Map[Segment, Option[Boolean]]) {

  private val POINT = "+"
  private val HORIZ = "-"
  private val VERT  = "|"

  def isInvalid: Boolean = {
    hasDeadEnd || hasBranch || cannotMakeTarget || exceededTarget
  }

  def isFilled: Boolean = {
    segmentStates.values.forall(_.isDefined)
  }

  def isSolved: Boolean = {
    isFilled && !isInvalid && isContinuous
  }

  override def toString: String = {
    val s = new StringBuilder
    for (r <- 0 until board.height){
      s.append(makeLineString(r))
      s.append(makeSquaresString(r))
    }
    s.append(makeLineString(board.height))
    s.toString()
  }

  private def isContinuous: Boolean = {
    val filledSegments = segmentStates.collect {
      case(s: Segment, Some(true)) => s
    }
    val firstLoop: Set[Point] = filledSegments.headOption.toSet.flatMap{ seg: Segment =>
      expandPoints(seg.points, filledSegments)
    }
    val allPoints: Set[Point] = filledSegments.flatMap(_.points).toSet
    firstLoop.size == allPoints.size
  }

  private def expandPoints(points: Set[Point], segments: Iterable[Segment]): Set[Point] = {
    val expanded = segments.filter(s => s.points.intersect(points).nonEmpty).flatMap(_.points).toSet
    if (expanded.size == points.size) {
      points
    } else {
      expandPoints(expanded, segments)
    }
  }

  private def cannotMakeTarget = {
    linesRoundSquares(assumeFilled = true).exists{
      case (sq: Square, maxPossible: Int) => sq.target.exists(targetValue => targetValue > maxPossible)
    }
  }

  private def exceededTarget = {
    linesRoundSquares(assumeFilled = false).exists{
      case (sq: Square, minPossible: Int) => sq.target.exists(targetValue => minPossible > targetValue)
    }
  }

  private def linesRoundSquares(assumeFilled: Boolean): Set[(Square, Int)] = {
    board.squares.map(sq => sq -> {
      board.segmentsFor(sq)
        .map(s => segmentStates(s))
        .count(_.getOrElse(assumeFilled))
    })
  }

  private def hasBranch: Boolean = {
    countLinesFromPoints(assumeFilled = false).exists(n => n > 2)
  }

  private def hasDeadEnd: Boolean = {
    countLinesFromPoints(assumeFilled = true).contains(1)
  }

  private def countLinesFromPoints(assumeFilled: Boolean): Set[Int] = {
    board.points.map(p => board.segmentsFor(p)
      .map(s => segmentStates(s)))
      .map(s => s.count(_.getOrElse(assumeFilled)))
  }

  private def makeLineString(row: Int): String = {
    horizontalSeqments(row).map(makeSegmentString(HORIZ, _)) .mkString(POINT, POINT, POINT + "\n")
  }

  def makeSquaresString(row: Int): String = {
    val values = board.getRow(row).map(s => s.target.map(_.toString).getOrElse(" ")) :+ "\n"
    val verts = verticalSegments(row).map(makeSegmentString(VERT, _))
    intersperse(verts, values).mkString
  }

  private def makeSegmentString(str: String, s: Segment) = if (segmentStates(s).contains(true)) str else " "

  private def verticalSegments(row: Int) = {
    board.segments.filter(s => s.points.exists(_.x == row) && s.points.exists(_.x == row + 1)).toSeq
      .sortBy(_.points.head.y)
  }

  private def horizontalSeqments(row: Int) = {
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
