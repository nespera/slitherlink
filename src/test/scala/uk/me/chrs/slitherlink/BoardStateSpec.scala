package uk.me.chrs.slitherlink

import org.specs2.mutable.Specification

class BoardStateSpec extends Specification {

  "A board state" should {
    val state = BoardState.empty(2, 3, ".12..3")
    "be convertible to string" in {
      state.toString mustEqual ". . . .\n   1 2 \n. . . .\n     3 \n. . . .\n"

      val state2 = state.updated(Point(0, 0), Point(0, 1), Some(true))
        .updated(Point(0, 0), Point(1, 0), Some(true))
        .updated(Point(1,1), Point(1, 2), Some(false))

      state2.toString mustEqual "._. . .\n|  1 2 \n. . . .\n     3 \n. . . .\n"
    }
  }
}
