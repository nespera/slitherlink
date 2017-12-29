package uk.me.chrs.slitherlink

import org.specs2.mutable.Specification

class GameSpec extends Specification {

  "Game" should {

    "find no solution for impossible problem" in {
      val state = BoardState.empty(1, 1, "2")
      Game.solve(state) must beNone
    }

    "find a solution for a tiny problem" in {
      val state = BoardState.empty(1, 2, "3.")
      val solved = Game.solve(state)
      solved must beSome
      solved.get.toString.trim mustEqual
        """>•‒‒•‒‒•
           >| 3   |
           >•‒‒•‒‒•""".stripMargin('>')
    }

    "find a solution for a small problem" in {
      val state = BoardState.empty(1, 3, ".33")
      val solved = Game.solve(state)
      solved must beSome
      solved.get.toString.trim mustEqual
        """>•  •‒‒•‒‒•
           >   | 3  3|
           >•  •‒‒•‒‒•""".stripMargin('>')
    }

    "find a solution for a smallish problem" in {
      val state = BoardState.empty(3, 3, "...303...")
      val solved = Game.solve(state)
      solved must beSome
      solved.get.toString.trim mustEqual
        "•  •‒‒•  •\n"+
        "   |  |   \n"+
        "•‒‒•  •‒‒•\n"+
        "| 3  0  3|\n"+
        "•‒‒•  •‒‒•\n"+
        "   |  |   \n"+
        "•  •‒‒•  •"
    }
  }

}
