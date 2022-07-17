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
OUTPUT_DIR        := $(SOURCE_DIR)
HTML_DIR          := $(SOURCE_DIR)/html
INCLUDE_DIR       := $(SOURCE_DIR)/include
REQUIRED_DIRS      = $(OUTPUT_DIR) $(HTML_DIR)
_MKDIR	          := $(shell for d in $(REQUIRED_DIRS); \
			do				\
			[[ -d $$d ]] || mkdir -p $$d; 	\
			done)

_MKONLIN		  = $(shell sed -i 's/href{out/href\{http:\/\/henrikfrisk.homeunix.net:8800\/disser\/out/' $(INCLUDE_DIR)/*.tex)

_MKOFFLIN		  = $(shell sed -i 's/href{http:\/\/henrikfrisk.homeunix.net:8800\/disser\/out/href{out/' $(INCLUDE_DIR)/*.tex)


AUTHOR := "Henrik Frisk, henrik<DOT>frisk<AT>mhm<DOT>lu<DOT>se"

source := ImprovisationComputersInteraction.tex
images := 
music  :=

BIB       =  /home/henrikfr/TeX/biblio/svn/biblio/bibliography.bib

TEXMF       =  /usr
BIN	= /usr/bin
LATEX       = $(TEXMF)/bin/latex -output-directory $(OUTPUT_DIR)
SCREEN_DVI	    = '\def\Status{1}\input'
SCREEN_PDF	    = '\def\Status{1}\input'
PRINT_PDF	= '\def\Status{1}\def\ForPrinting{1}\input'
BIBTEX      = $(TEXMF)/bin/bibtex
PDFLATEX = $(TEXMF)/bin/pdflatex -output-directory $(OUTPUT_DIR) -file-line-error
VMD-LATEX = $(TEXMF)/bin/pdflatex -dPDFSETTINGS=/prepress -dEmbedAllFonts=true -output-directory $(OUTPUT_DIR) -file-line-error
LATEX2HTML = ~/scripts/l2html.sh
DVIPS= $(TEXMF)/bin/dvips
RSYNC	= $(BIN)/rsync -avzr --delete
XDVI	= $(BIN)/xdvi
LATEX2RTF 	= /usr/local/bin/latex2rtf
SVN = $(BIN)/svn
HTLATEX = $(BIN)/htlatex
MAKEINDEX = $(BIN)/makeindex
LILYBOOK = $(BIN)/lilypond-book

PS        = $(source:.tex=.ps)
BBL       =  $(source:.tex=.bbl)
HTML	= $(HTML_DIR)/index.html
CSS	= ~/scripts/article_styles_template.css

SERVER 	= henrikfr@henrikfrisk.com:www/documents/articles/Introduction

include img/module.mk
include music/module.mk

.SUFFIXES: .ps .dvi .bib .aux .ltx .pdf

.aux.bib: ; $(BIBTEX) $*

## Printer parameters

DPI         = 600      
OFFSET      = 0mm,0mm  
PAGEFORMAT  = a4       
DVIFLAGS    = -D $(DPI) -t $(PAGEFORMAT) -O $(OFFSET)

### TARGETS AND SUFFIXES
HTM	      = $(wildcard $(basename $(source))*.html)
4CT       = $(source:.tex=.4ct)		
4TC       = $(source:.tex=.4tc)		
BLX       = $(source:.tex=-blx.bib)
XREF       = $(source:.tex=.xref)
IDX       = $(source:.tex=.idx)		
IDV       = $(source:.tex=.idv)
ILG       = $(source:.tex=.ilg)		
IND       = $(source:.tex=.ind)
CSS       = $(source:.tex=.css)
LG        = $(source:.tex=.lg)
TMP       = $(source:.tex=.tmp)
DVI       = $(source:.tex=.dvi)
BLG       = $(source:.tex=.blg)
AUX       = $(source:.tex=.aux)
LOG       = $(source:.tex=.log)
GLO       = $(source:.tex=.glo)
GLG       = $(source:.tex=.glg)
GLS       = $(source:.tex=.gls)
IST       = $(source:.tex=.ist)
OUT       = $(source:.tex=.out)
LOF       = $(source:.tex=.lof)
TOC       = $(source:.tex=.toc)
LOGS      = $(OUTPUT_DIR)/$(AUX) $(OUTPUT_DIR)/$(BLG) $(OUTPUT_DIR)/$(LOG)

PS        = $(source:.tex=.ps)
PDF	      = $(source:.tex=.pdf)


all: 

.PHONY: sed
sed:
	echo hej

.PHONY: echo_src
echo_src:
	echo $(source)
	echo $(PS)
	echo $(BIBTEX) $(OUTPUT_DIR)/$(basename $(source))

.PHONY: echo_img
echo_img:
	echo $(notdir $(basename $(music)))

test : $(source)
	$(LATEX)  $(SCREEN_DVI) $(source)

dvi : $(source)
	$(LATEX)  $(DVI_ENVVAR) $(source); \
	$(BIBTEX) $(OUTPUT_DIR)/$(basename $(source)); \
	$(MAKEINDEX) $(OUTPUT_DIR)/$(basename $(source)); \
	texindy -Lswedish $(IDX); \
	$(LATEX) $(DVI_ENVVAR) $(source); \
	$(LATEX) $(DVI_ENVVAR) $(source)

xdvi : dvi
	$(XDVI) $(DVI)

$(PS) : $(DVI)
	$(DVIPS) $(DVIFLAGS) -o $(OUTPUT_DIR)/$(PS) $(OUTPUT_DIR)/$(DVI)

local-screen-pdf : $(make-local) $(source) $(images)
	$(_MKOFFLIN)
	$(PDFLATEX) $(SCREEN_PDF) $(source) ; \
	$(BIBTEX) $(OUTPUT_DIR)/$(basename $(source)); \
	$(MAKEINDEX) $(OUTPUT_DIR)/$(basename $(source)); \
	texindy -Lswedish $(IDX); \
	$(PDFLATEX) $(SCREEN_PDF) $(source) ; \
	$(PDFLATEX) $(SCREEN_PDF) $(source)

remote-screen-pdf : $(source) $(images)
	$(_MKONLIN)
	$(PDFLATEX) $(SCREEN_PDF) $(source) ; \
	$(BIBTEX) $(OUTPUT_DIR)/$(basename $(source)); \
	$(MAKEINDEX) $(OUTPUT_DIR)/$(basename $(source)); \
	texindy -Lswedish $(IDX); \
	$(PDFLATEX) $(SCREEN_PDF) $(source) ; \
	$(PDFLATEX) $(SCREEN_PDF) $(source)

print-pdf : $(source) $(images)
	$(PDFLATEX) $(PRINT_PDF) $(source) ; \
	$(BIBTEX) $(OUTPUT_DIR)/$(basename $(source)); \
	$(PDFLATEX) $(PRINT_PDF) $(source) ; \
	$(PDFLATEX) $(PRINT_PDF) $(source)

test-pdf :
	$(PDFLATEX) $(PRINT_PDF) $(source)

test-font :
	$(PDFLATEX) $(PRINT_PDF) ImproTestFont.tex

vmd-pdf :
	$(VMD-LATEX) $(PRINT_PDF) $(source)

bib : $(source)
	$(BIBTEX) $(OUTPUT_DIR)/$(basename $(source));

latex2html : $(source) $(images) print-pdf
	$(LATEX2HTML) -s 3 -f $(source) -d $(HTML_DIR) -a $(AUTHOR) -c $(CSS)

html : $(source)
	$(HTLATEX) $(source) "disser,xhtml,charset=utf-8,info" " -cunihtf -utf8" "-d./html/"

rtf : $(source) $(images)
	$(LATEX2RTF) -M12 -F -a$(OUTPUT_DIR)/$(AUX) -b$(OUTPUT_DIR)/$(BBL) -o$(OUTPUT_DIR)/$(source:.tex=.rtf) $(source)

doc : $(source) $(images)
	$(HTLATEX) $(source) "xhtml,ooffice" "ooffice/! -cmozhtf" "-coo" "-cvalidate"

idx : $(source)
	$(MAKEINDEX) $(OUTPUT_DIR)/$(basename $(source))

gls : $(source)
	$(MAKEINDEX) -s $(OUTPUT_DIR)/$(IST) -t $(OUTPUT_DIR)/$(GLG) -o $(OUTPUT_DIR)/$(GLS) $(OUTPUT_DIR)/$(GLO)

lily : $(music)
	$(LILYBOOK) $(music)

.PHONY:
make-local :
	$(_MKOFFLIN)

.PHONY:
make-remote :
	$(_MKONLIN)

make-index : idx
	texindy -Lswedish $(IDX)

sync : pdf html
	$(RSYNC) -e "/usr/bin/ssh -i /home/henrikfr/henrikfrisk_com_rsync_key" --exclude "*~" --exclude "WARNINGS" $(HTML_DIR) $(SERVER)
	$(RSYNC) -e "/usr/bin/ssh -i /home/henrikfr/henrikfrisk_com_rsync_key" $(OUTPUT_DIR)/$(source:.tex=.pdf) $(SERVER)

lily-clean:
	rm -f lily-* $(notdir $(basename $(music)))* snippet-map.ly snippet-names

clean: lily-clean
	rm -f  $(LOGS) $(OUTPUT_DIR)/$(PS) $(OUTPUT_DIR)/$(PDF) $(OUTPUT_DIR)/$(DVI) $(OUTPUT_DIR)/$(BBL) *~ $(HTML_DIR)/* $(IDX) $(IDV) $(CSS) $(LG) $(TMP) $(4CT) $(4TC) $(BLX) $(XREF) $(HTM) $(ILG) $(IND) $(IST) $(OUT) $(LOF) $(TOC) $(GLO)
