\version "2.7.15"


#(define (compound-time grob one two divone divtwo)
  (define hmoveDivOne
   (if (equal? divone "16")
    0.6
    0.0))
  (define hmoveDivTwo
   (if (equal? divtwo "16")
    0.6
    0.0))
  (define hmoveLastCol
   (if (equal? divtwo "16")
    -0.6
    -0.3))
  (interpret-markup
   (ly:grob-layout grob)
   '(((baseline-skip . 2)
      (word-space . 1.5)
      (font-family . number) 
      (font-size . 1)))
   (markup
    #:line ( #:column ((#:translate (cons hmoveDivOne 0) one) divone)
	     #:lower 1 (#:translate (cons hmoveDivOne 0) "+") 
	     (#:translate (cons hmoveLastCol 0) #:column ((#:translate (cons hmoveDivTwo 0) two) divtwo))))))

#(define (compound-time-equal-div grob one two divone)
  (define hmoveDiv
   (if (equal? divone "16")
    -0.8
    -0.1))
  (interpret-markup
   (ly:grob-layout grob)
   '(((baseline-skip . 2.3)
      (word-space . 1.4)
      (font-family . number)
      (font-size . 1)))
   (markup
    ( #:line (one (#:column ( "+" (#:translate (cons hmoveDiv 0.) divone))) (#:translate (cons -0.35 0) two))))))


blanknotes = { \once \override NoteHead #'transparent = ##t }
%	       \override #'transparent  = ##t }
%	       \property Voice.Stem
%	       \override #'transparent = ##t }
pdlt =  \markup { \italic \hcenter \fontsize #-2 "pdlt." }

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Right hand
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

upper = \relative c {
  
  % [SEQUENCE_TRACK_NAME] linear-log-sin 1/3

  \override Staff.TimeSignature  #'style = #'numbered
  \override Staff.TimeSignature #'style = ##f
  \override Staff.TimeSignature #'font-size = #1
  \override Staff.TimeSignature #'extra-offset = #'(-1 . 4.5)
  \time 5/16 
  \key c \major
  \tempo 8 = 60 

				% Bar 1
  \once \override Staff.TimeSignature #'extra-offset = #'(-1.6 . 4.5)
  \textSpannerDown
  \override TextSpanner #'edge-text = #'("ped. gliss" . "")
  \override TextSpanner #'bound-padding = #2.5
  \override TextSpanner #'dash-fraction = #'()
  \override TextSpanner #'arrow = ##t
  \override TextSpanner #'arrow-width = #0.3
  \override TextSpanner #'font-size = #-2.0
  \override TextSpanner #'padding = #2.0
  \change Staff = lower r8.^\markup { " " \hcenter \raise #15.0 {\epsfile #"eps/nssffff.eps"} } \acciaccatura { bis'32 [ cis d es \change Staff = upper \stemDown d' es es,] } es''8 
  \acciaccatura { \stemDown fes32[ \change Staff=lower \stemUp <fes, fes,> \startTextSpan 
	\blanknotes fes <f f,> \stopTextSpan bis,,32 ] } \change Staff = upper |
				% Bar 2
  \time 3/8
  ges'''4 \revert Beam #'positions r16 \acciaccatura { \stemDown f,32[ 
    <ges' d,,> g^\markup{ " " \hcenter \raise #0.0 { \bracket { \line {\small{G\smaller{\raise #.6 {\natural}}}}}}}
  \change Staff = lower \stemUp b,,,!^\markup{ " " \hcenter \raise #6.0 { \bracket { \line {\small{B\smaller{\raise #.6 {\natural}}}}}}} \change Staff = upper \stemDown f''~]} \stemNeutral f16~  |
				% Bar 3

}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Left hand
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

lower = \relative c {
  \textSpannerDown
  \override TextSpanner #'edge-text = #'("ped. gliss" . "")
  \override TextSpanner #'bound-padding = #2.5
  \override TextSpanner #'dash-fraction = #'()
  \override TextSpanner #'arrow = ##t
  \override TextSpanner #'arrow-width = #0.3
  \override TextSpanner #'font-size = #-2.0
% Bar 1
% \clef treble
  s8 s64 \clef treble s64 s16. s16 |
% Bar 2
 \stemDown
  s16 s64 s32. \clef bass % \once \override Voice.Stem #'flag-style = #'no-flag \stemUp \once \override NoteColumn #'force-hshift = #-6 
  s8 s8 |
%% Bar 3

}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Interaction
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
interaction = \relative c {
}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Computer
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
computer = \relative c {
}