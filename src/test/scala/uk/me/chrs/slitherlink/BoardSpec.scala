package uk.me.chrs.slitherlink

import org.specs2.mutable.Specification

class BoardSpec extends Specification {

  "A board" should {

    "have a target strings for all its squares" in {
      new Board(1, 1, "") must throwAn[IllegalArgumentException]
    }

    "have targets 0,1,2,3 or a dot for no target" in {
      new Board(1, 1, "4") must throwAn[IllegalArgumentException]
    }

    "Have the expected squares" in {
      new Board(0, 0, "").squares mustEqual Set()
      new Board(2, 3, ".0123.").squares mustEqual Set(
        Square(0,0, None),
        Square(0,1, Some(0)),
        Square(0,2, Some(1)),
        Square(1,0, Some(2)),
        Square(1,1, Some(3)),
        Square(1,2, None))
    }

    "Have the expected points" in {
      new Board(0, 0, "").points mustEqual Set(Point(0,0))
      new Board(1, 2, "12").points mustEqual Set(Point(0,0), Point(0,1), Point(0,2), Point(1,0), Point(1,1), Point(1,2))
    }

    "Have the expected segments" in {
      new Board(0, 0, "").segments mustEqual Set()
      new Board(1, 1, "0").segments mustEqual Set(
        Segment(Point(0,0), Point(0,1)),
        Segment(Point(0,0), Point(1,0)),
        Segment(Point(0,1), Point(1,1)),
        Segment(Point(1,0), Point(1,1))
      )
    }
  }

  "A segment" should {
    "have exactly two points" in {
      Segment(Set()) must throwAn[IllegalArgumentException]
      Segment(Set(Point(0,0))) must throwAn[IllegalArgumentException]
      Segment(Set(Point(0,0), Point(0,1), Point(1,1))) must throwAn[IllegalArgumentException]
    }
  }

}
