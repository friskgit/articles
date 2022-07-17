#!/usr/bin/awk -f
{ if ($0 ~ /31--33/) print "pattern at line " NR }