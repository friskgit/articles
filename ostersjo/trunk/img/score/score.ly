% Lily was here -- automatically converted by /sw/bin/midi2ly from lin-log-sin_1-3.mid
\version "2.7.15"
  #(set-global-staff-size 16)

%\header {
%  title = "Music for Harp and computer"
%  subtitle = "second motif, version X"
%  composer = "Henrik Frisk"
%  copyright = "Dinergy Music Pub."
%}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Stems (doesn't work?)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
stemRemove = \once \override Stem #'transparent = ##t
noFlag = \once \override Stem #'flag-style = #'no-flag

stemExtend = \once \override Stem #'length = #28
stemExtendb = \once \override Stem #'length = #36
%stemExtend = \once \override Stem #'extra-X-extent = #'(10.0 . 10.0) % Adds space after this stem
stemExtendb = \once \override Stem #'lengths = #'(1.0 1.0 6.0)

\include "harpVersion4.ly"

\score {
  <<
    \context StaffGroup = pi << 
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
	}
      >>
    >>
  >>
}

\layout {
  raggedright = ##f
}
