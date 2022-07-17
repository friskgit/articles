SRC_DIR := .
LATEX := /usr/bin/latex
BIBLATEX := /usr/bin/bibtex	
HTLATEX := /usr/bin/htlatex
TEX2RTF := /usr/local/bin/latex2rtf
source := InteractivityTimeSpace.tex 

HT_OOO		= $(HTLATEX) $(source) "xhtml,ooffice" "ooffice/! -cmozhtf -utf8" "-coo -cvalidate"
HT_HTML		= $(HTLATEX) $(source) "xhtml,charset=utf-8,info" " -cunihtf -utf8" "-d./html/"

DVI       = $(source:.tex=.dvi)
BBL	= $(source:.tex=.bbl)
BLG 	= $(source:.tex=.blg)
AUX 	= $(source:.tex=.aux)
LOG 	= $(source:.tex=.log) \
	$(source:.tex=.log) \
	$(source:.tex=.lg) \
	$(source:.tex=.xref) \
	$(source:.tex=.og) \
	$(source:.tex=.png) \
	$(source:.tex=.png.4og) \
	$(source:.tex=.tmp) \
	$(source:.tex=.4ct) \
	$(source:.tex=.4tc) \
	$(source:.tex=.tmp)

dvi : $(source)
	$(LATEX) $(source)
	$(BIBLATEX) $(basename $(source))
	$(LATEX) $(source)	
	$(LATEX) $(source)

tex2ooo : $(source)
	$(HT_OOO)

tex2rtf : $(source)
	latex2rtf $(source)

tex2html : $(source)
	$(HT_HTML)

bib : $(source)
	$(BIBLATEX) $(basename $(source))

clean : 
	rm -f $(DVI) $(AUX) $(LOG) $(BBL) $(BLG)