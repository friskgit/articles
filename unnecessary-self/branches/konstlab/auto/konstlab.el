(TeX-add-style-hook "konstlab"
 (lambda ()
    (LaTeX-add-bibliographies
     "bibliography")
    (TeX-run-style-hooks
     "fancyhdr"
     "graphicx"
     "natbib"
     "authoryear"
     "round"
     "fontenc"
     "T1"
     "babel"
     "swedish"
     "english"
     "latex2e"
     "art10"
     "article"
     "a4paper")))

