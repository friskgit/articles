SOURCE_DIR := .
IMG_DIR := $(SOURCE_DIR)/img
OUTPUT_DIR := $(SOURCE_DIR)/out
HTML_DIR := $(SOURCE_DIR)/html
REQUIRED_DIRS      = $(OUTPUT_DIR) $(HTML_DIR)
_MKDIR	          := $(shell for d in $(REQUIRED_DIRS); \
			do				\
			[[ -d $$d ]] || mkdir -p $$d; 	\
			done)

source := $(wildcard *.tex)
images := 

BIB       =  /home/henrikfr/TeX/biblio/svn/biblio/bibliography.bib

TEXMF       =  /usr
LATEX       = $(TEXMF)/bin/latex -output-directory $(OUTPUT_DIR)
BIBTEX      = $(TEXMF)/bin/bibtex
PDFLATEX = $(TEXMF)/bin/pdflatex -output-directory $(OUTPUT_DIR)
LATEX2HTML = ~/scripts/l2html.sh
RSYNC	= /usr/bin/rsync -avzr --delete

DVIPS= $(TEXMF)/bin/dvips

PS        = $(source:.tex=.ps)
BBL       =  $(source:.tex=.bbl)
HTML	= $(HTML_DIR)/index.html
CSS	= ~/scripts/article_styles_template.css

SERVER 	= henrikfr@henrikfrisk.com:www/documents/articles/WorkInProgress

all:

## RULES

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
LOGS      = $(AUX) $(BLG) $(LOG)

PS        = $(source:.tex=.ps)
PDF	  = $(source:.tex=.pdf)

all: $(PS)

.PHONY: echo_src
echo_src:
	echo $(source)

.PHONY: echo_img
echo_img:
	echo $(images)

# Some latex file needs to be run at least three times to be sure to resolve all references.

$(DVI) : $(source) $(BIB) $(images)
	$(LATEX)  $(source) ; \
	$(BIBTEX) $(OUTPUT_DIR)/$(basename $(source)) ; \
	$(LATEX) $(source) ; $(LATEX) $(source)

$(PS) : $(DVI)
	$(DVIPS) $(DVIFLAGS) -o $(OUTPUT_DIR)/$(PS) $(OUTPUT_DIR)/$(DVI)

$(PDF) : $(source) $(BIB) $(images)
	$(PDFLATEX) $(source) ; $(BIBTEX) `basename $(BIB) .bib`; $(PDFLATEX) $(source) ; $(PDFLATEX) $(source) ; $(PDFLATEX) $(source)

.PHONY: html
html : $(source)  $(images) 
	$(LATEX2HTML) -f $(source) -d $(HTML_DIR) -c $(CSS)

.PHONY: sync
sync : html $(PDF)
	$(RSYNC) -e "/usr/bin/ssh -i /home/henrikfr/henrikfrisk_com_rsync_key" --exclude "*~" --exclude "WARNINGS" $(HTML_DIR) $(SERVER)
	$(RSYNC) -e "/usr/bin/ssh -i /home/henrikfr/henrikfrisk_com_rsync_key" --exclude "*~" --exclude "WARNINGS" $(PDF) $(SERVER)

.PHONY: clean
clean:
	rm -f $(OUTPUT_DIR)/$(PS) \
	$(OUTPUT_DIR)/$(LOGS) \
	$(OUTPUT_DIR)/$(DVI) \
	$(OUTPUT_DIR)/$(BBL) \
	*~ $(HTML_DIR)/*

.PHONY: dist-clean
dist-clean:
	rm -rf $(OUTPUT_DIR) \
	*~ $(HTML_DIR) \
	auto

