# TODO: Add svn commit target

# Makefile for dissertation
# 
# Note that the rsync targets rely on the private ssh key.
# This is NOT included with this Makefile. Further, this Makefile 
# relies on bash script wrapping up the latex2html parameters.
# This script can be downloaded from:
# http://www.henrikfrisk.com/documents/downloads/l2html.sh
#
# If you wish to adapt this Makefile for your own projects
# follow the excellent instructions at:
# http://troy.jdmz.net/rsync/index.html
# on how to set up a safe but fully automated ssh'ed rsync 
# conection to your server.
#
# 24 September 2007	Henrik Frisk	mail@henrikfrisk.com

SOURCE_DIR          := .
IMG_DIR           := $(SOURCE_DIR)/img
OUTPUT_DIR        := $(SOURCE_DIR)/out
HTML_DIR          := $(SOURCE_DIR)/html
REQUIRED_DIRS      = $(OUTPUT_DIR) $(HTML_DIR)
_MKDIR	          := $(shell for d in $(REQUIRED_DIRS); \
			do				\
			[[ -d $$d ]] || mkdir -p $$d; 	\
			done)

AUTHOR := "Henrik Frisk, henrik<DOT>frisk<AT>mhm<DOT>lu<DOT>se"

source := $(wildcard *.tex)
images := 

BIB       =  /home/henrikfr/TeX/biblio/svn/biblio/bibliography.bib

TEXMF       =  /usr
BIN	= /usr/bin
LATEX       = $(TEXMF)/bin/latex -output-directory $(OUTPUT_DIR)
BIBTEX      = $(TEXMF)/bin/bibtex
PDFLATEX = $(TEXMF)/bin/pdflatex  -output-directory $(OUTPUT_DIR)
LATEX2HTML = ~/scripts/l2html.sh
DVIPS= $(TEXMF)/bin/dvips
RSYNC	= $(BIN)/rsync -avzr --delete
XDVI	= $(BIN)/xdvi
LATEX2RTF 	= /usr/local/bin/latex2rtf
SVN = $(BIN)/svn

PS        = $(source:.tex=.ps)
BBL       =  $(source:.tex=.bbl)
HTML	= $(HTML_DIR)/index.html
CSS	= ~/scripts/article_styles_template.css

SERVER 	= henrikfr@henrikfrisk.com:www/documents/articles/Introduction

include img/module.mk

.SUFFIXES:
.SUFFIXES: .ps .dvi .bib .aux .ltx .pdf

.aux.bib: ; $(BIBTEX) $*

## Printer parameters

DPI         = 600      
OFFSET      = 0mm,0mm  
PAGEFORMAT  = a4       
DVIFLAGS    = -D $(DPI) -t $(PAGEFORMAT) -O $(OFFSET)

### TARGETS
DVI       = $(source:.tex=.dvi)
BLG       = $(source:.tex=.blg)
AUX       = $(source:.tex=.aux)
LOG       = $(source:.tex=.log)
LOGS      = $(OUTPUT_DIR)/$(AUX) $(OUTPUT_DIR)/$(BLG) $(OUTPUT_DIR)/$(LOG)

PS        = $(source:.tex=.ps)
PDF	      = $(source:.tex=.pdf)


all: 

.PHONY: echo_src
echo_src:
	echo $(source)
	echo $(PS)
	echo $(BIBTEX) $(OUTPUT_DIR)/$(basename $(source))

.PHONY: echo_img
echo_img:
	echo $(images)

# Some latex file needs to be run at least three times to be sure to resolve all references.

$(DVI) : $(source) $(BIB) 
	$(LATEX)  $(source) ; \
	$(BIBTEX) $(OUTPUT_DIR)/$(basename $(source)); \
	$(LATEX) $(source) ; \
	$(LATEX) $(source) ; \
	$(XDVI) out/$(DVI)

$(PS) : $(DVI)
	$(DVIPS) $(DVIFLAGS) -o $(OUTPUT_DIR)/$(PS) $(OUTPUT_DIR)/$(DVI)

pdf : $(source) $(images)
	$(PDFLATEX) $(source) ; \
	$(BIBTEX) $(OUTPUT_DIR)/$(basename $(source)); \
	$(PDFLATEX) $(source) ; \
	$(PDFLATEX) $(source)

html : $(source) $(images) pdf
	$(LATEX2HTML) -s 3 -f $(source) -d $(HTML_DIR) -a $(AUTHOR) -c $(CSS)

rtf : $(source) $(images)
	$(LATEX2RTF) -M12 -F -a$(OUTPUT_DIR)/$(AUX) -b$(OUTPUT_DIR)/$(BBL) -o$(OUTPUT_DIR)/$(source:.tex=.rtf) $(source)

sync : pdf html
	$(RSYNC) -e "/usr/bin/ssh -i /home/henrikfr/henrikfrisk_com_rsync_key" --exclude "*~" --exclude "WARNINGS" $(HTML_DIR) $(SERVER)
	$(RSYNC) -e "/usr/bin/ssh -i /home/henrikfr/henrikfrisk_com_rsync_key" $(OUTPUT_DIR)/$(source:.tex=.pdf) $(SERVER)

clean:
	rm -f  $(LOGS) $(OUTPUT_DIR)/$(PS) $(OUTPUT_DIR)/$(PDF) $(OUTPUT_DIR)/$(DVI) $(OUTPUT_DIR)/$(BBL) *~ $(HTML_DIR)/*
