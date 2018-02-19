#Python para testear el logueo realizado en Java.

#Autores: Casabella Martin, Kleiner Matias, Lopez Gaston
from colorama import init, Fore, Back, Style
import xlrd #Para trabajar con libros Excel (Lectura)

print 'Inicio del programa'



try:
	archivoA=open("logFileA.txt","r")
	archivoC= open("logFileC.txt","r")
	archivoB= open("logFileB.txt","r")
except: 
	print '\nError en la apertura del archivo'
	exit(1)


print '\nLectura de archivos'

listaA=[]
listaB=[]
listaC=[]
matrizI=[]
matrizM0=[]
matrizManterior=[]
matrizMactual=[]
listaK=[]
listaTransicionesADisparar=[]


flagComienzo=False

for linea in archivoB.readlines():
	if(linea!="\n" and flagComienzo):
		listaB.append(linea[0:len(linea)-1])
		#print listaB[0],
	if(not flagComienzo):
		flagComienzo=True

flagComienzo=False
for linea in archivoC.readlines():
	if(linea!="\n" and flagComienzo):
		listaC.append(linea)
		#print listaC[0],
	if(not flagComienzo):
		flagComienzo=True

flagComienzo=False
for linea in archivoA.readlines():
	if(linea!="\n" and flagComienzo):
		listaA.append(linea)
		#print listaA[0],
	if(not flagComienzo):
		flagComienzo=True

archivoA.close()
archivoB.close()
archivoC.close()

print '\nTest cantidad de transiciones disparadas.'

contadorDeTransicionesDisparadas=0

try:
	contadorDeTransicionesDisparadas=int(listaC[0])
	#print contadorDeTransicionesDisparadas

except:
	print 'Error en metodo int()'
	exit(1)


flagCantidadTransicionesOK=False

if(contadorDeTransicionesDisparadas==len(listaB)):
	init()
	print(Fore.GREEN+"\nTest cantidad de transiciones disparadas. OK")
	print(Style.RESET_ALL);
	flagCantidadTransicionesOK=True
    #print(chr(27)+"[4;32;47m"+"Test cantidad de transiciones disparadas. OK") 
	#print 'Test cantidad de transiciones disparadas. OK'
else:
	init()
	print(Fore.RED+"\nTest cantidad de transiciones disparadas. FAIL")
	print(Style.RESET_ALL);


if(flagCantidadTransicionesOK):

	print 'Cargado de matrices y listas'

	try:
 		book = xlrd.open_workbook("excelTren.xls")
	except:
		print 'No se pudo abrir el archivo excel.'
		exit(1)	

	#print "The number of worksheets is", book.nsheets
	#print "Worksheet name(s):", book.sheet_names()

	sheet = book.sheet_by_index(2)

	for fila in range(1,sheet.nrows):
		matrizI.append([])
		for columna in range(1,sheet.ncols):
			matrizI[fila-1].append(sheet.cell_value(rowx=fila, colx=columna))

	
	

	flagComienzo=True
	for indice in range(len(listaA)):
		if(not flagComienzo):
			if(indice%4==3):
				if("true" in listaA[indice]):
					listaK.append(True)
				elif("false" in listaA[indice]):
					listaK.append(False)
				else:
					print 'Error en listaK.'
					exit(1)
			if(indice%4==2):
				index= listaA[indice].find(":", 0, len(listaA[indice]))
				listaTransicionesADisparar.append(int(listaA[indice][index+2:]))


		if(flagComienzo):
			
			if(indice==3):
				flagComienzo=False
				if("true" in listaA[indice]):
					listaK.append(True)
				elif("false" in listaA[indice]):
					listaK.append(False)
				else:
					print 'Error en listaK.'
					exit(1)

			if(indice==2):
				index= listaA[indice].find(":", 0, len(listaA[indice]))
				try:
					listaTransicionesADisparar.append(int(listaA[indice][index+2:]))
				except:
					print 'Error en pasar a int la lista transiciones a disparar'
					exit(1)

	#for i in range(len(listaTransicionesADisparar)):
		#print listaTransicionesADisparar[i]
		#print listaK[i]

	if(len(listaK)!=len(listaTransicionesADisparar)):
		print 

	flagEvolucionMarcado=True
	print '\nTest evolucion Marcado'

	if(flagEvolucionMarcado):
		init()
		print(Fore.GREEN+"\nTest evolucion Marcado. OK")
		print(Style.RESET_ALL);
		
	    #print(chr(27)+"[4;32;47m"+"Test cantidad de transiciones disparadas. OK") 
		#print 'Test cantidad de transiciones disparadas. OK'
	else:
		init()
		print(Fore.RED+"\nTest evolucion Marcado. FAIL")
		print(Style.RESET_ALL);







print '\nFin del programa'