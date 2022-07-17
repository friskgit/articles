% Lily was here -- automatically converted by /sw/bin/midi2ly from lin-log-sin_1-3.mid
\version "2.7.15"
  #(set-global-staff-size 16)

\header {
  title = "Music for Harp and computer"
  subtitle = "second motif, version 4"
  composer = "Henrik Frisk"
  copyright = "Dinergy Music Pub."
}

%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Pedal marking as variable
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%


%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
%% Stems (doesn't work?)
%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
stemRemove = \once \override Stem #'transparent = ##t
noFlag = \once \override Stem #'flag-style = #'no-flag

stemExtend = \once \override Stem #'length = #28
stemExtendb = \once \override Stem #'length = #36
%stemExtend = \once \override Stem #'extra-X-extent = #'(10.0 . 10.0) % Adds space after this stem
stemExtendb = \once \override Stem #'lengths = #'(1.0 1.0 6.0)

\include "secondMotif.music.ly"

\score {
  <<
    \context StaffGroup = pi << 
      \context PianoStaff <<
	\set PianoStaff.instrument = "Harp   "
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
    \context StaffGroup = cp << 
      \set StaffGroup.instrument = #"Electronics"
      \context Staff = "inta" << 
	\set Staff.instrument = #"Interaction"
	\set Staff.fontSize = #-3 \override Staff.StaffSymbol #'staff-space = #(magstep -3) \override Staff.StaffSymbol #'line-count = 1
	\override Staff.VerticalAxisGroup #'minimum-Y-extent = #'(-3 . 10)
	\override Staff.NoteHead #'style = #'harmonic
	\override Staff.TimeSignature #'break-visibility = #all-invisible
	\clef percussion
	\interaction
      >>
      \context Staff = "cmp" << 
	\set Staff.instrument = #"Computer"
	\set Staff.fontSize = #-3 \override Staff.StaffSymbol #'staff-space = #(magstep -3) \override Staff.StaffSymbol #'line-count = 1
	\override Staff.VerticalAxisGroup #'minimum-Y-extent = #'(-10 . 3)
	\override Staff.NoteHead #'style = #'harmonic
	\override Staff.TimeSignature #'break-visibility = #all-invisible
	\clef treble
	\computer
      >>
    >>
  >>
}

\layout {
  raggedright = ##t
}

\paper {
  #(set-paper-size "a3" )
  topmargin = 2\cm
  headsep = 1\cm
  aftertitlespace = 2.0\cm
  betweensystempadding = 0.\cm
  annotatespacing = ##f
}