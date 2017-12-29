package uk.me.chrs.slitherlink

class BoardState(val board: Board, segmentStates: Map[Segment, Option[Boolean]]) {

  private val POINT = "\u2022"
  private val HORIZ = "\u2012"
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

  def nextSegment: Option[Segment] = {
    segmentStates.collect {
      case(s: Segment, None) => s
    }.toSeq.sortBy(_.points.map(_.x).max).headOption
  }

  override def toString: String = stringRepresentation(emptyMarker = " ")

  def debugString: String = stringRepresentation(emptyMarker = "x")

  def updated(a: Point, b: Point, state: Option[Boolean]): BoardState = {
    updated(Segment(a, b), state)
  }

  def updated(segment: Segment, state: Option[Boolean]) : BoardState = {
    new BoardState(board, segmentStates.updated(segment, state))
  }

  def markAllZeroes: BoardState = {
    val zeroSegments: Set[Segment] = board.squares.filter(sq => sq.target.contains(0)).flatMap(board.segmentsFor)
    zeroSegments.foldLeft(this){case (state: BoardState, segment: Segment) => state.updated(segment, Some(false))}
  }

  private def stringRepresentation(emptyMarker: String) = {
    val s = new StringBuilder
    for (r <- 0 until board.height) {
      s.append(makeLineString(r, emptyMarker))
      s.append(makeSquaresString(r, emptyMarker))
    }
    s.append(makeLineString(board.height, emptyMarker))
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
    board.points.map(p => board.segmentsFor(p).map(s => segmentStates(s)))
      .map(s => s.count(_.getOrElse(false)))
      .exists(n => n > 2)
  }

  private def hasDeadEnd: Boolean = {
    board.points.map(p => board.segmentsFor(p).map(s => segmentStates(s)))
      .filter(s => s.forall(_.isDefined))
      .map(s => s.count(_.get == true))
      .contains(1)
  }

  private def makeLineString(row: Int, emptyMarker: String): String = {
    horizontalSegments(row).map(makeSegmentString(HORIZ, emptyMarker, _)) .mkString(POINT, POINT, POINT + "\n")
  }

  private def makeSquaresString(row: Int, emptyMarker: String): String = {
    val values = board.getRow(row).map(s => s.target.map(_.toString).getOrElse(" ")) :+ "\n"
    val verts = verticalSegments(row).map(makeSegmentString(VERT, emptyMarker, _))
    intersperse(verts, values).mkString
  }

  private def makeSegmentString(filledMarker: String, emptyMarker: String,  s: Segment) = {
    segmentStates(s) match {
      case Some(true) => filledMarker
      case Some(false) => emptyMarker
      case None => " "
    }
  }

  private def verticalSegments(row: Int) = {
    board.segments.filter(s => s.points.exists(_.x == row) && s.points.exists(_.x == row + 1)).toSeq
      .sortBy(_.points.head.y)
  }

  private def horizontalSegments(row: Int) = {
    board.segments.filter(s => s.points.forall(_.x == row)).toSeq
      .sortBy(_.points.map(_.y).max)
  }

  private def intersperse(l1: Seq[String], l2: Seq[String]) = l1.zip(l2) flatMap { case (a, b) => Seq(a, b) }
}

object BoardState {
  def empty(height: Int, width: Int, targets: String): BoardState = {
    val board = new Board(height, width, targets)
    new BoardState(board, board.segments.map(s => s -> None).toMap)
  }
}
