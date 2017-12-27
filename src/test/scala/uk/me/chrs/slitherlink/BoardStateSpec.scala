package uk.me.chrs.slitherlink

import org.specs2.mutable.Specification

class BoardStateSpec extends Specification {

  "A board state" should {
    "be convertible to string" in {
      BoardState.empty(2, 3, ".12..3").toString mustEqual ". . . .\n   1 2 \n. . . .\n     3 \n. . . .\n"
    }
  }
}
