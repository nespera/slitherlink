package uk.me.chrs.slitherlink

import org.specs2.mutable.Specification

class BoardSpec extends Specification {

  "A board" should {

    "Have the expected squares" in {
      new Board(0, 0).squares mustEqual Set()
      new Board(2, 3).squares mustEqual Set(Square(0,0), Square(1,0), Square(0,1), Square(1,1), Square(0,2), Square(1,2))
    }

    "Have the expected points" in {
      new Board(0, 0).points mustEqual Set(Point(0,0))
      new Board(1, 2).points mustEqual Set(Point(0,0), Point(1,0), Point(0,1), Point(1,1), Point(0,2), Point(1,2))
    }

    "Have the expected segments" in {
      new Board(0, 0).segments mustEqual Set()
      new Board(1, 1).segments mustEqual Set(
        Segment(Point(0,0), Point(0,1)),
        Segment(Point(0,0), Point(1,0)),
        Segment(Point(0,1), Point(1,1)),
        Segment(Point(1,0), Point(1,1))
      )
    }
  }

}
