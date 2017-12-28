package uk.me.chrs.slitherlink

object Game extends App {

  if (args.length != 3) {
    println(s"Needs 3 arguments: <height> <width> <target values>")
    System.exit(1)
  } else {
    val height = args(0).toInt
    val width = args(1).toInt
    val targets = args(2)
    val state = BoardState.empty(height, width, targets)
    println(s"solving\n\n$state\n\n")
    solve(state) match {
      case Some(endState) => println(endState)
      case None => "Cannot find a solution"
    }
  }

  def solve(state: BoardState): Option[BoardState] = {
     if (state.isInvalid) {
       None
     } else if (state.isSolved) {
       Some(state)
     } else {
       state.nextSegment match {
         case Some(segment) =>
           solve(state.updated(segment, Some(true))) orElse
           solve(state.updated(segment, Some(false)))
         case _ => None
       }
     }
  }

}
