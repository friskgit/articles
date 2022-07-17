\begin[quote,fragment,staffsize=18]{lilypond}
<< 
  \relative c'' {
  \set Staff.instrumentName = \markup {
    \override #'(font-name . "Vera Bold")
    \column { "Acoustic instrument" 
              \line { "('event')" }}
  }
  << 
    { c1\accent\sfz\< | 
      \override Staff.StaffSymbol #'line-count = 1
      \stopStaff \startStaff r\! | 
      \revert Staff.StaffSymbol #'line-count
      \stopStaff \startStaff        
      d\espressivo
      \hideNotes d4
    } 
  >>
}
  
  \new Staff \relative c'' {
    \set Staff.instrumentName = \markup {
      \column { "Computer" 
                \line { "('double')" }
              }
    }
    << {c1\accent\f |
        \override Staff.StaffSymbol #'line-count = 1
        \stopStaff \startStaff r | 
        \revert Staff.StaffSymbol #'line-count
        \stopStaff \startStaff        
        d\<\sfz 
        \hideNotes d4\!
      } >>
  }
>>
\end{lilypond}