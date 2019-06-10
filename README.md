# MIDOS
This repository has the code of a project from compilers course. Basically, the project is an small version MSDOS called MIDOS

MIDOS simulates the command line so you can use commands like:

* CLS: clears the screen, we simulate it printing 10 empty lines.
* DATE: prints the current date.
* TIME: prints the current time.
* MD: creates a directory. Example: md directoryName
* CD: changes to an specific directory. Example: cd directoryName.
* VER: prints the current version.
* DIR: prints all the directories in the current path.
* RD: deletes an specific directory. Example rd directoryName.
* PROMPT: changes the appearance of the command line.
* COPY CON: creates a file. Example: COPY CON fileName.
* TYPE: shows the content of a file. Example: TYPE fileName.
* DEL: deletes a directory or a file.
* REN: changes the name of a file or directory. Example: ren currentName newName.
* TREE: prints all the directories and files in a tree structure.
* EXIT: ends the program.

This project stores its data in two txt files called MIDOSFRE.txt (stores the reamaining space) and MIDOSTRE.txt (stores the directory structure).
