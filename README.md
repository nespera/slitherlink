# SlitherLink

This is code to solve [Slitherlink](https://en.wikipedia.org/wiki/Slitherlink) puzzles.
To run the code, you will need Java installed and then you can run it as follows:

```
  > ./sbt

  > runMain uk.me.chrs.slitherlink.Game 7 7 .02...0....1.23...01...2.1...30...02.3....2...33.
``` 

The parameters are height, width and then a list of the values to go in the squares. A dot is used
to indicate an empty square. The output is like this, with the initial grid, then the solved grid:

```
   solving
   
   •  •  •  •  •  •  •  •
        0  2           0 
   •  •  •  •  •  •  •  •
                 1     2 
   •  •  •  •  •  •  •  •
     3           0  1    
   •  •  •  •  •  •  •  •
           2     1       
   •  •  •  •  •  •  •  •
        3  0           0 
   •  •  •  •  •  •  •  •
     2     3             
   •  •  •  •  •  •  •  •
     2           3  3    
   •  •  •  •  •  •  •  •
   
   
   •  •  •  •‒‒•  •  •  •
        0  2|  |       0 
   •  •  •‒‒•  •‒‒•‒‒•  •
         |       1   | 2 
   •‒‒•  •‒‒•  •  •  •‒‒•
   | 3|     |    0  1   |
   •  •‒‒•  •  •  •‒‒•‒‒•
   |     | 2|    1|      
   •  •‒‒•  •‒‒•  •  •  •
   |  | 3  0   |  |    0 
   •  •‒‒•  •‒‒•  •‒‒•  •
   | 2   | 3|        |   
   •‒‒•  •‒‒•  •‒‒•  •  •
     2|        | 3| 3|   
   •  •‒‒•‒‒•‒‒•  •‒‒•  •
```