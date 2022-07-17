# TODO: Add svn commit target

# Makefile for libIntegra paper 2007
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
# 20 September 2007	Henrik Frisk	mail@henrikfrisk.com

BASE_DIR          := .
IMG_DIR           := $(BASE_DIR)/img
LONG_SRC_DIR      := 8page
SHORT_SRC_DIR     := 4page
LONG_OUTPUT_DIR   := $(LONG_SRC_DIR)/out
SHORT_OUTPUT_DIR  := $(SHORT_SRC_DIR)/out
LONG_HTML_DIR     := $(LONG_SRC_DIR)/html
SHORT_HTML_DIR    := $(SHORT_SRC_DIR)/html
ODT_DIR           := $(LONG_SRC_DIR)/odt
REQUIRED_DIRS      = $(LONG_OUTPUT_DIR) $(LONG_HTML_DIR) $(SHORT_OUTPUT_DIR) $(SHORT_HTML_DIR) $(ODT_DIR)
_MKDIR	          := $(shell for d in $(REQUIRED_DIRS); \
			do				\
			[[ -d $$d ]] || mkdir -p $$d; 	\
			done)

AUTHOR := "Jamie Bullock, Henrik Frisk"

long_source      := $(wildcard $(LONG_SRC_DIR)/*.tex)
short_source     := $(wildcard $(SHORT_SRC_DIR)/*.tex) 
images           := 
long_tex_file    := $(notdir $(long_source))
short_tex_file   := $(notdir $(short_source))

LONG_BIB          =  $(long_source:.tex=.bib)
SHORT_BIB         =  $(short_source:.tex=.bib)

TEXMF       =  /usr
LATEX       = $(TEXMF)/bin/latex -output-directory
BIBTEX      = $(TEXMF)/bin/bibtex
PDFLATEX    = $(TEXMF)/bin/pdflatex -output-directory
LATEX2HTML  = ~/scripts/l2html.sh
DVIPS       = $(TEXMF)/bin/dvips
RSYNC	    = /usr/bin/rsync -avzr --delete
HTLATEX     = htlatex
HT_HTML		= $(HTLATEX) $(long_source) "xhtml,charset=utf-8,info" " -cunihtf -utf8" "-d./8page/html/"
HT_OOO		= $(HTLATEX) $(long_source) "xhtml,charset=utf-8,ooffice" "ooffice/! -cmozhtf -utf8" "-coo -cvalidate -d./8page/odt/"

LONG_PS          = $(long_tex_file:.tex=.ps)
SHORT_PS          = $(short_tex_file:.tex=.ps)
CSS	    = ~/scripts/article_styles_template.css

SERVER 	    = henrikfr@henrikfrisk.com:www/documents/articles/Integra/
LONG_DIR    = long
SHORT_DIR   = short

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

LONG_DVI       = $(LONG_OUTPUT_DIR)/$(long_tex_file:.tex=.dvi)
SHORT_DVI       = $(SHORT_OUTPUT_DIR)/$(short_tex_file:.tex=.dvi)
DVI	= $(LONG_DVI) $(SHORT_DVI) $(long_tex_file:.tex=.dvi) $(long_tex_file:.tex=.idv)
BBL	= $(SHORT_OUTPUT_DIR)/$(short_tex_file:.tex=.bbl) \
	$(LONG_OUTPUT_DIR)/$(long_tex_file:.tex=.bbl)
BLG 	= $(SHORT_OUTPUT_DIR)/$(short_tex_file:.tex=.blg) \
	$(LONG_OUTPUT_DIR)/$(long_tex_file:.tex=.blg)
AUX 	= $(SHORT_OUTPUT_DIR)/$(short_tex_file:.tex=.aux) \
	$(LONG_OUTPUT_DIR)/$(long_tex_file:.tex=.aux) \
	$(long_tex_file:.tex=.aux)
LOG 	= $(SHORT_OUTPUT_DIR)/$(short_tex_file:.tex=.log) \
	$(LONG_OUTPUT_DIR)/$(long_tex_file:.tex=.log) \
	$(long_tex_file:.tex=.log) \
	$(long_tex_file:.tex=.lg) \
	$(long_tex_file:.tex=.xref) \
	$(long_tex_file:.tex=.og) \
	$(long_tex_file:.tex=.png) \
	$(long_tex_file:.tex=.png.4og)
	$(long_tex_file:.tex=.tmp) \
	$(long_tex_file:.tex=.4ct) \
	$(long_tex_file:.tex=.4tc) \
	$(long_tex_file:.tex=.tmp)

all: 

.PHONY: echo_src
echo_src:
	echo $(long_source:.tex=.*)
	echo $(short_source)

.PHONY: echo_img
echo_img:
	echo $(ODT_DIR)

# Some latex file needs to be run at least three times to be sure to resolve all references.

long_dvi : $(long_source) $(images)
	$(shell sed -i \
	's/^% \\newcommand/\\newcommand/' \
	$(long_source))
	$(LATEX) $(LONG_OUTPUT_DIR) $(long_source) ; \
	$(BIBTEX) $(LONG_OUTPUT_DIR)/$(basename $(long_tex_file)) ; \
	$(LATEX) $(LONG_OUTPUT_DIR) $(long_source) ; \
	$(LATEX) $(LONG_OUTPUT_DIR) $(long_source)

short_dvi : $(short_source) $(images)
	$(LATEX) $(SHORT_OUTPUT_DIR) $(short_source) ; \
	$(BIBTEX) $(SHORT_OUTPUT_DIR)/$(basename $(short_tex_file)) ; \
	$(LATEX) $(SHORT_OUTPUT_DIR) $(short_source) ; \
	$(LATEX) $(SHORT_OUTPUT_DIR) $(short_source)

dvi	: long_dvi short_dvi

long_ps	: long_dvi
	$(DVIPS) $(DVIFLAGS) -o $(LONG_OUTPUT_DIR)/$(LONG_PS) $(LONG_DVI)

short_ps : short_dvi
	$(DVIPS) $(DVIFLAGS) -o $(SHORT_OUTPUT_DIR)/$(SHORT_PS) $(SHORT_DVI)

ps : short_ps long_ps

long_pdf : $(long_source) $(images)
	$(shell sed -i \
	's/^% \\newcommand/\\newcommand/' \
	$(long_source))
	$(PDFLATEX) $(LONG_OUTPUT_DIR) $(long_source) ; \
	$(BIBTEX) $(basename $(long_tex_file)); \
	$(PDFLATEX) $(LONG_OUTPUT_DIR) $(long_source) ; \
	$(PDFLATEX) $(LONG_OUTPUT_DIR) $(long_source)

short_pdf : $(short_source) $(images)
	$(PDFLATEX) $(SHORT_OUTPUT_DIR) $(short_source) ; \
	$(BIBTEX) $(basename $(long_tex_file)) ; \
	$(PDFLATEX) $(SHORT_OUTPUT_DIR) $(short_source) ; \
	$(PDFLATEX) $(SHORT_OUTPUT_DIR) $(short_source)

pdf : long_pdf short_pdf

## Removing some latex macros that confuses latex2html

.PHONY: comment_newcommand
comment_newcommand : 
	$(shell sed -i \
	's/^\\newcommand/% \\newcommand/' \
	$(long_source))

## Put them back again!
.PHONY: uncomment_newcommand
uncomment_newcommand: 
	$(shell sed -i \
	's/^% \\newcommand/\\newcommand/' \
	$(long_source))

long_html : $(long_source) $(images)
	$(shell sed -i \
	's/^\\newcommand/% \\newcommand/' \
	$(long_source))
	$(LATEX2HTML) -f $(long_tex_file) -t $(LONG_SRC_DIR) -d $(LONG_HTML_DIR) -a $(AUTHOR) -c $(CSS)

short_html : $(short_source)  $(images)
	$(LATEX2HTML) -f $(short_tex_file) -t $(SHORT_SRC_DIR) -d $(SHORT_HTML_DIR) -a $(AUTHOR) -c $(CSS)

.PHONY: html
html : long_html short_html

short_sync : short_pdf short_html
	$(RSYNC) -e "/usr/bin/ssh -i /home/henrikfr/henrikfrisk_com_rsync_key" --exclude "*~" --exclude "WARNINGS" $(SHORT_HTML_DIR) $(SERVER)$(SHORT_DIR)
	$(RSYNC) -e "/usr/bin/ssh -i /home/henrikfr/henrikfrisk_com_rsync_key" $(SHORT_OUTPUT_DIR)/$(short_tex_file:.tex=.pdf) $(SERVER)$(SHORT_DIR)

long_sync : long_html
	$(RSYNC) -e "/usr/bin/ssh -i /home/henrikfr/henrikfrisk_com_rsync_key" --exclude "*~" --exclude "WARNINGS" $(LONG_HTML_DIR) $(SERVER)$(LONG_DIR)
	$(RSYNC) -e "/usr/bin/ssh -i /home/henrikfr/henrikfrisk_com_rsync_key" $(LONG_OUTPUT_DIR)/$(long_tex_file:.tex=.pdf) $(SERVER)$(LONG_DIR)

sync : short_sync long_sync

.PHONY: clean
clean :
	rm -f $(DVI) $(AUX) $(LOG) $(BBL) $(BLG) $(SHORT_HTML_DIR)/* $(LONG_HTML_DIR)/* $(ODT_DIR)/*

.PHONY: dist-clean
dist-clean :
	rm -rf $(LONG_OUTPUT_DIR) $(SHORT_OUTPUT_DIR) $(SHORT_HTML_DIR) $(LONG_HTML_DIR) \
	auto $(LONG_SRC_DIR)/auto $(SHORT_SRC_DIR)/auto $(LONG_SRC_DIR)/*~ $(SHORT_SRC_DIR)/*~

ooo : $(long_source) dvi
	$(HT_OOO)