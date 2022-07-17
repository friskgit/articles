
SOURCE_DIR := .
IMG_DIR := $(SOURCE_DIR)/../../trunk/img
OUTPUT_DIR := $(SOURCE_DIR)/out
HTML_DIR := $(SOURCE_DIR)/html
REQUIRED_DIRS = $(OUTPUT_DIR) $(HTML_DIR)
_MKDIR	:=	$(shell for d in $(REQUIRED_DIRS); 	\
	do						\
	[[ -d $$d ]] || mkdir -p $$d; 			\
	done)

AUTHOR := "Henrik Frisk, Stefan Ostersjo"

source := $(wildcard *.tex)
images := 

BIB       =  $(OUTPUT_DIR)/$(source:.tex=.bib)
BBL       =  $(OUTPUT_DIR)/$(source:.tex=.bbl)

TEXMF       =  /usr
LATEX       = $(TEXMF)/bin/latex -output-directory $(OUTPUT_DIR) 
BIBTEX      = $(TEXMF)/bin/bibtex
PDFLATEX = $(TEXMF)/bin/pdflatex -output-directory $(OUTPUT_DIR) 
LATEX2HTML = ~/scripts/l2html.sh
DVIPS= $(TEXMF)/bin/dvips
RSYNC	= /usr/bin/rsync -avzr --delete

all:

include $(IMG_DIR)/module.mk

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
BLG       = $(BIB:.bib=.blg)
AUX       = $(source:.tex=.aux)
LOG       = $(source:.tex=.log)
LOGS      = $(AUX) $(BLG) $(LOG)
PS        = $(source:.tex=.ps)
PDF	  = $(source:.tex=.pdf)
CSS       = ~/scripts/article_styles_template.css
SERVER    = henrikfr@henrikfrisk.com:www/documents/articles/NegotiateICMC

include img/module.mk

all: $(PS)

.PHONY: echo_src
echo_src:
	echo $(source)
	echo $(BIB)

.PHONY: echo_img
echo_img:
	echo $(images)

# Some latex file needs to be run at least three times to be sure to resolve all references.

$(DVI) : $(source) $(images)
	$(LATEX)  $(source) ; $(BIBTEX) $(OUTPUT_DIR)/`basename $(BIB) .bib`; $(LATEX) $(source) ; $(LATEX) $(source)

$(PS) : $(DVI)
	$(DVIPS) $(DVIFLAGS) -o $(OUTPUT_DIR)/$(PS) $(OUTPUT_DIR)/$(DVI)

pdf : $(source) $(images)
	$(PDFLATEX) $(source) ; $(BIBTEX) $(OUTPUT_DIR)/`basename $(BIB) .bib`; $(PDFLATEX) $(source) ; $(PDFLATEX) $(source) ; $(PDFLATEX) $(source)

html : $(source) $(BIB) $(images) pdf
	$(LATEX2HTML) -f $(source) -d $(HTML_DIR) -a $(AUTHOR) -c $(CSS)

sync : html
	$(RSYNC) -e "/usr/bin/ssh -i /home/henrikfr/henrikfrisk_com_rsync_key" --exclude "*~" --exclude "WARNINGS" $(HTML_DIR) $(SERVER)
	$(RSYNC) -e "/usr/bin/ssh -i /home/henrikfr/henrikfrisk_com_rsync_key" --exclude "*~" $(OUTPUT_DIR)/$(PDF) $(SERVER)

clean:
	rm -f $(PS) $(LOGS) $(DVI) $(AUX) $(BBL) $(BLG) *~ $(HTML_DIR)/*