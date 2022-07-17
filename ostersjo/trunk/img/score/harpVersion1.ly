% Lily was here -- automatically converted by /sw/bin/midi2ly from lin-log-sin_1-3.mid
\version "2.6.0"
  #(set-global-staff-size 16)

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Pedal marking as variable
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
pedala = \markup { 
%  "" \raise #8.0 \bracket {
%    \column { 
%      {\small{E\smaller{\raise #.3 {\flat}} F\smaller{\raise #.3 {\flat}} G\smaller{\raise #.6 {\natural}} A\smaller{\raise #.6 {\natural}}}}
%      {\small{B\smaller{\raise #.6 {\sharp}} C\smaller{\raise #.6 {\sharp}} D\smaller{\raise #.6 {\natural}}}} 
%    }
%  }
}

pedalb = \markup { 
%%$  "" \raise #5.0 \bracket {
%%$    \column { 
%%$      {\small{E\smaller{\raise #.3 {\flat}} F\smaller{\raise #.6 {\natural}} G\smaller{\raise #.3 {\flat}} A\smaller{\raise #.6 {\natural}}}}
%%$      {\small{B\smaller{\raise #.6 {\sharp}} C\smaller{\raise #.6 {\sharp}} D\smaller{\raise #.6 {\natural}}}} 
%%$    }
%%$  }
}

pedalc = \markup { 
%%$  "" \raise #3.0 \bracket {
%%$    \column { 
%%$      {\small{E\smaller{\raise #.3 {\flat}} F\smaller{\raise #.6 {\natural}} G\smaller{\raise #.6 {\natural}} A\smaller{\raise #.6 {\natural}}}}
%%$      {\small{B\smaller{\raise #.6 {\natural}} C\smaller{\raise #.6 {\sharp}} D\smaller{\raise #.6 {\natural}}}} 
%%$    }
%%$  }
}

pedald = \markup { 
  "" \raise #5.0 \bracket {
    \column { 
      {\small{E\smaller{\raise #.3 {\flat}} F\smaller{\raise #.6 {\natural}} G\smaller{\raise #.3 {\flat}} A\smaller{\raise #.6 {\natural}}}}
      {\small{B\smaller{\raise #.6 {\sharp}} C\smaller{\raise #.6 {\sharp}} D\smaller{\raise #.6 {\natural}}}} 
    }
  }
}

pedale = \markup { 
  "" \raise #9.0 \bracket {
    \column { 
      {\small{E\smaller{\raise #.3 {\flat}} F\smaller{\raise #.3 {\flat}} G\smaller{\raise #.3 {\flat}} A\smaller{\raise #.6 {\natural}}}}
      {\small{B\smaller{\raise #.6 {\sharp}} C\smaller{\raise #.6 {\sharp}} D\smaller{\raise #.6 {\sharp}}}} 
    }
  }
}

pedalf = \markup { 
  "" \raise #4.0 \bracket {
    \column { 
      {\small{E\smaller{\raise #.3 {\flat}} F\smaller{\raise #.6 {\natural}} G\smaller{\raise #.3 {\flat}} A\smaller{\raise #.6 {\natural}}}}
      {\small{B\smaller{\raise #.6 {\sharp}} C\smaller{\raise #.6 {\sharp}} D\smaller{\raise #.6 {\natural}}}} 
    }
  }
}

pedalg = \markup { 
  "" \raise #9.0 \bracket {
    \column { 
      {\small{E\smaller{\raise #.3 {\flat}} F\smaller{\raise #.6 {\natural}} G\smaller{\raise #.3 {\flat}} A\smaller{\raise #.3 {\flat}}}}
      {\small{B\smaller{\raise #.6 {\sharp}} C\smaller{\raise #.6 {\sharp}} D\smaller{\raise #.6 {\natural}}}} 
    }
  }
}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Stems (doesn't work?)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
stemRemove = \once \override Stem #'length =1
noFlag = \once \override Stem #'flag-style = #'no-flag

stemExtend = \once \override Stem #'length = #32
%stemExtend = \once \override Stem #'extra-X-extent = #'(10.0 . 10.0) % Adds space after this stem
stemExtendb = \once \override Stem #'lengths = #'(1.0 1.0 6.0)

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Time Signatures
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
% create 2/4 + 5/8
tsMarkupa =\markup {
  \number {
    \column { "2" "4" }
    \musicglyph #"scripts.stopped"
    \bracket \column { "5" "8" }
  }
}

% create 3/16 + 5/16
tsMarkupb =\markup {
  \number {
    \column { "3" "16" }
    \musicglyph #"scripts.stopped"
    \column { "5" "16" }
  }
}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Treble clef
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

upper = \relative c {
  
  % [SEQUENCE_TRACK_NAME] linear-log-sin 1/3

  \override Staff.TimeSignature  #'style = #'numbered
  \override Staff.TimeSignature #'style = ##f
  \override Staff.TimeSignature #'font-size = #1
  \override Staff.TimeSignature #'extra-offset = #'(0 . 4.5)
  \time 5/16 
  \key c \major
  \tempo 8 = 60 


%  Written out
  \stemDown c'32^\pedala[ cis16 d32~] 
  d16[ dis16 d'32 dis32] 
  dis,16[ dis''16 e32 <e, e,>32~]
  <e, e'>32[ f''16 c,,32] 
  fis''8~[ fis32 f,16. <fis' d,,>8~ ]


}

%trackA = <<
%  \context Voice = channelA \upper
%>>


\score {
  \context Staff = "upper" << 
    \upper 
  >>
}

%\paper {
%  #(set-paper-size "a4" )
%  topmargin = 2\cm
%  headsep = 1\cm
%  betweensystempadding = 0\cm
%}