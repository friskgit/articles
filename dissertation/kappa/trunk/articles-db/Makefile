# TODO: Add svn commit target

# Recursive Makefile for dissertation Apache derby data base
# 
# 18 March 2008	Henrik Frisk	mail@henrikfrisk.com


ROOT_DIR	:= .
ethsnd		:= $(ROOT_DIR)/ethsnd
comm		:= $(ROOT_DIR)/comm
intg		:= $(ROOT_DIR)/intg
negmus		:= $(ROOT_DIR)/negmus

sources		:= $(ethsnd) $(comm) $(intg) $(negmus)

.PHONY: all $(sources)
all: $(sources)

$(sources):
	$(MAKE) --directory=$@

$(sources):
	$(MAKE) --directory=$@ $(TARGET)

# .PHONY: all 
# 	all: $(sources)
# 		for d in $(sources); 			\
# 		do								\
# 			$(MAKE) --directory=$$d;	\
# 		done
