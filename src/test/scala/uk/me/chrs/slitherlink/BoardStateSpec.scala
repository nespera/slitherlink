package uk.me.chrs.slitherlink

import org.specs2.mutable.Specification

class BoardStateSpec extends Specification {

  "A board state" should {
    val state = BoardState.empty(2, 3, ".12..3")

    "be convertible to string with no filled segments" in {
      state.toString mustEqual ". . . .\n   1 2 \n. . . .\n     3 \n. . . .\n"
    }

    "be convertible to string with filled segments" in {
      state.updated(Point(0, 0), Point(0, 1), Some(true))
        .updated(Point(0, 0), Point(1, 0), Some(true))
        .updated(Point(1, 1), Point(1, 2), Some(false))
        .toString mustEqual "._. . .\n|  1 2 \n. . . .\n     3 \n. . . .\n"
    }

    "not be invalid with no filled segments" in {
      state.isInvalid must beFalse
    }

    "be invalid when a line ends at a point" in {
      state.updated(Point(0, 0), Point(0, 1), Some(true))
        .updated(Point(0, 0), Point(1, 0), Some(false))
        .isInvalid must beTrue
    }

    "be invalid when we have a branch" in {
      state.updated(Point(1, 1), Point(0, 1), Some(true))
        .updated(Point(1, 1), Point(1, 0), Some(true))
        .updated(Point(1, 1), Point(1, 2), Some(true))
        .isInvalid must beTrue
    }
  }
}
