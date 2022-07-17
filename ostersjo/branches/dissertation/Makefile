SOURCE_DIR := .
IMG_DIR := $(SOURCE_DIR)/img
OUTPUT_DIR := $(SOURCE_DIR)/out
HTML_DIR := $(SOURCE_DIR)/html

REQUIRED_DIRS = $(OUTPUT_DIR) $(HTML_DIR)
_MKDIR	:=	$(shell for d in $(REQUIRED_DIRS); 	\
	do						\
	[[ -d $$d ]] || mkdir -p $$d; 			\
	done)

source := $(wildcard *.tex)
images := 

BIB       =  $(source:.tex=.bib)

TEXMF       =  /usr
LATEX       = $(TEXMF)/bin/latex -output-directory $(OUTPUT_DIR) 
BIBTEX      = $(TEXMF)/bin/bibtex
PDFLATEX = $(TEXMF)/bin/pdflatex -output-directory $(OUTPUT_DIR)
LATEX2HTML = ~/scripts/l2html.sh
DVIPS= $(TEXMF)/bin/dvips
RSYNC	= /usr/bin/rsync -avzr --delete
LATEX2RTF 	= /usr/local/bin/latex2rtf

BBL       =  $(source:.tex=.bbl)
HTML	= $(HTML_DIR)/index.html
CSS	= ~/scripts/article_styles_template.css

SERVER 	= henrikfr@henrikfrisk.com:www/documents/articles/etherSound/spark

all:

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
BLG       = $(BIB:.bib=.blg)
AUX       = $(source:.tex=.aux)
LOG       = $(source:.tex=.log)
LOGS      = $(AUX) $(BLG) $(LOG)

PS        = $(source:.tex=.ps)
PDF	  = $($(OUTPUT_DIR)/source:.tex=.pdf)

all: $(PS)

.PHONY: echo_src
echo_src:
	echo $(source)

.PHONY: echo_img
echo_img:
	echo $(OUTPUT_DIR)/$(PS)

# Some latex file needs to be run at least three times to be sure to resolve all references.

$(DVI) : $(source) $(images)
	$(LATEX)  $(source) ; $(BIBTEX) $(OUTPUT_DIR)/`basename $(BIB) .bib`; $(LATEX) $(source) ; $(LATEX) $(source)

etherSound.ps : $(DVI)
	$(DVIPS) $(DVIFLAGS) -o $(OUTPUT_DIR)/$(PS) $(OUTPUT_DIR)/$(DVI)

pdf : $(source) $(images)
	$(PDFLATEX) $(source) ; $(BIBTEX) $(OUTPUT_DIR)/`basename $(BIB) .bib`; $(PDFLATEX) $(source) ; $(PDFLATEX) $(source) ; $(PDFLATEX) $(source)

html : $(DVI)
	$(LATEX2HTML) -f $(source) -d $(HTML_DIR) -c $(CSS)

rtf : $(source) $(images)
	$(LATEX2RTF) -M12 -F -a$(OUTPUT_DIR)/$(AUX) -b$(OUTPUT_DIR)/$(BBL) -o$(OUTPUT_DIR)/$(source:.tex=.rtf) $(source)

sync : 
	$(RSYNC) -e "/usr/bin/ssh -i /home/henrikfr/henrikfrisk_com_rsync_key" --exclude "*~" --exclude "WARNINGS" $(HTML_DIR) $(SERVER)
	$(RSYNC) -e "/usr/bin/ssh -i /home/henrikfr/henrikfrisk_com_rsync_key" $(OUTPUT_DIR)/$(source:.tex=.pdf) $(SERVER)

.PHONY: clean
clean:
	rm -f $(PS) $(LOGS) $(DVI) $(AUX) $(BBL) $(BLG) $(HTML_DIR)/*.html $(OUTPUT_DIR)/*

.PHONY: dist-clean
dist-clean:
	rm -rf $(PS) $(LOGS) $(DVI) $(AUX) $(BBL) $(BLG) $(HTML_DIR) $(OUTPUT_DIR) auto
