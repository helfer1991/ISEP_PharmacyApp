# variaveis
INCLUDES = estimate.h
SOURCES = main.c readDataFile.c createFiles.c check_dir_changes.c estimate.s
OBJFILES = main.o readDataFile.o createFiles.o check_dir_changes.o estimate.o
EXEC = park_system

#mensagem recebida por parametro na forma:
# make action msg="mensagem"
MSG = $(msg)

#Parametros de compilação (-lm for <math.h>, -m32 para compilar em 32 bits)
CP = -lm -m32

#Regras de sufixo
.SUFFIXES : .c .o
.SUFFIXES : .s .o

#como transformar um .c num .o ; $< -- nome do ficheiro
.c.o:
	gcc -g -Wall ${CP} -c $<
	
#como transformar um .s num .o ; $< -- nome do ficheiro
.s.o:
	gcc -g -Wall ${CP} -c $<
	
#fazer o programa principal
${EXEC}: ${OBJFILES}
	gcc -g ${CP} -o ${EXEC} ${OBJFILES}
	
#${OBJFILES}: ${SOURCES} ${INCLUDES}

run: ${EXEC}
	./${EXEC}

clean:
	rm -f ${OBJFILES} ${EXEC}
