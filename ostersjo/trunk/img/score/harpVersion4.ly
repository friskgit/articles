\version "2.7.15"

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Scheme
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

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

#(define (parenthesize-callback callback)
   (define (parenthesize-stencil grob)
     (let* ((fn (ly:grob-default-font grob))
            (pclose (ly:font-get-glyph fn "accidentals.rightparen"))
            (popen (ly:font-get-glyph fn "accidentals.leftparen"))
            (subject (callback grob))

                ; get position of stem
                (stem-pos (ly:grob-property grob 'stem-attachment))
                
            ; remember old size
            (subject-dim-x (ly:stencil-extent subject 0))
            (subject-dim-y (ly:stencil-extent subject 1)))

        ;; add parens
        (set! subject
             (ly:stencil-combine-at-edge 
              (ly:stencil-combine-at-edge subject 0 1 pclose 0)
              0 -1 popen  0))
                  
                ; adjust stem position
                (ly:grob-set-property! grob 'stem-attachment
                        (cons (- (car stem-pos) 0.43) (cdr stem-pos)))

        ; adjust size
       (ly:make-stencil
        (ly:stencil-expr subject)
                  (cons (- (car subject-dim-x) 0.5) (+ (cdr
subject-dim-x) 0.5)) subject-dim-y)))
   parenthesize-stencil)

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Macros
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

blanknotes = { \once \override NoteHead #'transparent = ##t }
pdlt =  \markup { \italic \hcenter \fontsize #-2 "pdlt." }
down = { \change Staff=lower \stemUp }
up = { \change Staff=upper \stemNeutral }

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
  \override TextSpanner #'extra-offset = #'( -1.0 . 0.0 )
  \change Staff = lower r16._\mf\< ^\markup { " " \hcenter \raise #15.0 {\epsfile #"eps/nssffff.eps"} } [ 
    \acciaccatura { bis'32 [ cis32 d32 ] } \stemNeutral es32~] 
  es32 [ \acciaccatura { d'32[ es32] } 
	 \change Staff=lower es,32 
	 \change Staff=upper es''32. \acciaccatura { fes32 } 
	 \change Staff=lower <fes, fes,>64 ( 
	 \blanknotes fes64 
	 \set fontSize = #-4 \override NoteHead #'stencil = #(parenthesize-callback Note_head::print) 
	 <f f,>32.^\markup{ " " \hcenter \raise #8.0 { \bracket { \line {\small{F\smaller{\raise #.6 {\natural}}}}}}} ) ] \revert NoteHead #'stencil \set fontSize = #-0
  \change Staff=lower \acciaccatura { bis,,32 } \change Staff=upper |
				% Bar 2
  \time 3/8
  ges'''16~\ff [ \revert Beam #'positions ges64 \change Staff=lower f,32.\> \change Staff=upper \stemExtend \noFlag ges'16.-> ( g32^\markup{ " " \hcenter \raise #0.0 { \bracket { \line {\small{G\smaller{\raise #.6 {\natural}}}}}}} ] ) 
  \change Staff = lower b,,,!32 ^\markup{ " " \hcenter \raise #10.0 { \bracket { \line {\small{B\smaller{\raise #.6 {\natural}}}}}}} [ 
    \change Staff = upper f''~ f16~ ] |
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
  \override TextSpanner #'extra-offset = #'( -1.0 . -2.5 )
% Bar 1
% \clef treble
  s8 s64 \clef treble s64 s16. s16 |
% Bar 2
 \stemDown
  s16 s64 f''32. \clef bass % \once \override Voice.Stem #'flag-style = #'no-flag \stemUp \once \override NoteColumn #'force-hshift = #-6 
  d,8 s8 |
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