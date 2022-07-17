% Lily was here -- automatically converted by /sw/bin/midi2ly from lin-log-sin_1-3.mid
\version "2.6.0"
  #(set-global-staff-size 16)

\header {

}

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
%  c'32^\pedala[ cis16 d32~ d16 dis16]
%  d'32[ dis32 dis,16 dis''16 e32 <e, e,>32~]
%  <e, e'> 32[ f''16^\pedalb c,,32 fis''8~]
%  fis32[ f,16. <fis' d,,>8~ ]

%  With grace notes
   r32.^\pedala [ \change Staff = lower \acciaccatura { \stemDown bis'32 [ cis32] \stemNeutral } \change Staff = upper d32. es32~]
  es32[ \acciaccatura { d'32[ es32] } \change Staff=lower es,32 \change Staff=upper es''32. \acciaccatura { fes32 } \change Staff=lower <fes, fes,-.>64 \change Staff=upper
  \override Beam #'positions = #'(-6 . -5) 
  r64 f'32.^\pedalb ] \change Staff=lower \acciaccatura { bis,,,32 } \change Staff=upper |
% Bar 2
  \time 3/8
  ges'''16~[ \revert Beam #'positions ges64 \change Staff=lower f,32. \change Staff=upper \stemExtend \noFlag ges'8->] \noBeam
  \acciaccatura { g32^\pedalc } \change Staff = lower b,,,32 [ \change Staff = upper f''~ f16~ ] |
% Bar 3



%  f32 d32 dis16 f32 fis2 g16 dis,,16 e32 cis8 
%  d32*21 dis16*11 e32*23 f2. fis32*25 fis16*13 g32*27 e8*7 f32*29 
%  d16*15 dis32*31 cis1 s32*5 gis''16*15 a16*15 fis16*15 g32*31 
%  e32*31 f1 s32*31 
  % [MARKER] Marker ##
  
}

lower = \relative c {
% Bar 1
  s8 \clef treble s8 s16 |
% Bar 2
  \stemDown
  s8 \clef bass \once \override Voice.Stem #'flag-style = #'no-flag \stemUp \once \override NoteColumn #'force-hshift = #-6 d'8 s8 |
% Bar 3

}

%trackA = <<
%  \context Voice = channelA \upper
%>>


\score {
    \context PianoStaff <<
      \context Staff = "upper" << 
	\upper 
      >> {
      }
      \context Staff = "lower" <<
	\override Staff.TimeSignature #'break-visibility = #all-invisible
	\clef bass
	\lower
      >> {
%	
%	\skip 1 * 10
      }
    >>
%    \midi { \tempo 8=120 }
  }

\paper {

}