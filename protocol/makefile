NAME=dokumentace
all: $(NAME).pdf clean

$(NAME).pdf: $(NAME).tex
	pdflatex $(NAME).tex
	bibtex $(NAME)
	pdflatex $(NAME).tex
	pdflatex $(NAME).tex

clean:
	-rm $(NAME).aux $(NAME).toc $(NAME).log $(NAME).bbl $(NAME).blg
