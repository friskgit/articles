# Makefile for etherSound paper.
# 
# Further, this Makefile 
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
# 18 March 2008	Henrik Frisk	mail@henrikfrisk.com

ROOT_DIR          := .
SRC_IMG_DIR       := $(SRC_DIR)/img
IMG_DIR           := $(ROOT_DIR)/img
OUTPUT_DIR        := $(ROOT_DIR)/out
HTML_DIR          := $(ROOT_DIR)/html
AUTO_DIR           := $(ROOT_DIR)/auto
REQUIRED_DIRS      = $(OUTPUT_DIR) $(HTML_DIR) $(IMG_DIR)
_MKDIR	          := $(shell for d in $(REQUIRED_DIRS); \
			do				\
			[[ -d $$d ]] || mkdir -p $$d; 	\
			done)

_CP_IMG			:= $(shell cp -u $(SRC_IMG_DIR)/* $(IMG_DIR))

source 			:= $(wildcard *.tex)
images 			:= 
html_sources 	= $(wildcard $(HTML_DIR)/*.html)

BIB       		=  /home/henrikfr/TeX/biblio/svn/biblio/bibliography.bib

ABSTRACT		= "Abstract.html"
TEXMF       	=  /usr
BIN				= /usr/bin
LATEX       	= $(TEXMF)/bin/latex -output-directory $(OUTPUT_DIR)
BIBTEX      	= $(TEXMF)/bin/bibtex
PDFLATEX 		= $(TEXMF)/bin/pdflatex  -output-directory $(OUTPUT_DIR)
LATEX2HTML 		= ~/scripts/l2html.sh
DVIPS			= $(TEXMF)/bin/dvips
RSYNC			= $(BIN)/rsync -avzr --delete
XDVI			= $(BIN)/xdvi
LATEX2RTF 		= /usr/local/bin/latex2rtf
SVN 			= $(BIN)/svn

PS        		= $(source:.tex=.ps)
BBL				=  $(source:.tex=.bbl)
HTML			= $(HTML_DIR)/index.html
CSS				= ~/scripts/article_styles_template.css

SERVER			= henrikfr@henrikfrisk.com:www/documents/articles/Introduction

include img/module.mk

.SUFFIXES:
.SUFFIXES: .ps .dvi .bib .aux .ltx .pdf .html

.aux.bib: ; $(BIBTEX) $*

## Printer and DVI parameters

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

# $(call extract-abstract, file-name, prefix)
# Write the Abstract and the Epigraph into two separate files.
define extract-abstract
	$(shell sed -n						 				\
		-e '/^<H3>Abstract/,/^<\/ADDRESS/p'			 	\
		$1 >> $(HTML_DIR)/$2$(ABSTRACT))				\
			$(shell sed -i						 		\
				-e '/^<H3>Abstract/,/^<\/ADDRESS/d' 	\
				$1)
endef

#	$(shell sed -n -i -e '/^<H3>Abstract/,/^<\/ADDRESS/!d' $1) 
# $(call remove-head, file-name)
# Remove everthing but the contents of the <body> tag.
define remove-head
	$(shell sed -i 						\
		-e '/^<BODY/,/^<\/BODY/!d'		\
		-e '/^<BODY/d' 					\
		-e '/^<\/BODY>/d' 				\
		$1)
endef

# $(call edit-html-source)
# Calls remove-head for each HTML file found in the HTML_DIR directory.
define edit-html-source
	$(foreach i,$(html_sources),$(call remove-head,$i))
endef

define edit-abstract
	$(foreach i,$(html_sources),$(call extract-abstract,$i,$(DB_PREFIX)))
endef

.PHONY: all prepare-db

# Main target to call
prepare-db: db
	$(call edit-html-source) \
	$(call edit-abstract)

.PHONY: edit-abstract
 edit-abstract:
	$(call edit-abstract)

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
	$(LATEX2HTML) -f $(source) -d $(HTML_DIR) -a $(AUTHOR) -c $(CSS)

db : pdf
	$(LATEX2HTML) -f $(source) -d $(HTML_DIR) -a $(AUTHOR) -c $(CSS) -s 8 -p $(DB_PREFIX) -D

rtf : $(source) $(images)
	$(LATEX2RTF) -M12 -F -a$(OUTPUT_DIR)/$(AUX) -b$(OUTPUT_DIR)/$(BBL) -o$(OUTPUT_DIR)/$(source:.tex=.rtf) $(source)

sync : pdf html
	$(RSYNC) -e "/usr/bin/ssh -i /home/henrikfr/henrikfrisk_com_rsync_key" --exclude "*~" --exclude "WARNINGS" $(HTML_DIR) $(SERVER)
	$(RSYNC) -e "/usr/bin/ssh -i /home/henrikfr/henrikfrisk_com_rsync_key" $(OUTPUT_DIR)/$(source:.tex=.pdf) $(SERVER)

.PHONY: clean
clean:
	rm -f $(LOGS) \
			$(OUTPUT_DIR)/$(PS) \
			$(OUTPUT_DIR)/$(PDF) \
			$(OUTPUT_DIR)/$(DVI) \
			$(OUTPUT_DIR)/$(BBL) \
			*~ \
			$(HTML_DIR)/* ;
	rm -rf $(IMG_DIR) \
			$(OUTPUT_DIR) \
			$(HTML_DIR) \
			$(AUTO_DIR)