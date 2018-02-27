#Python para testear el orden de los TInvariantes.

#Autores: Casabella Martin, Kleiner Matias, Lopez Gaston



import re #Expresiones regulares
from colorama import init, Fore, Back, Style #Colores en impresion por consola para resultados de tests.
import xlrd #Para trabajar con libros Excel (Lectura)
import os #Para determinar el sistema operativo
import numpy as np #Para operaciones con matrices y vectores

'''
Cadenas globales
'''
global cadenaImpresion #Para impresion por consola
global flagNoError #Para testing



'''
Funciones

'''

#Impresion por consola en color rojo. Indica error o test fail.
def impresionConsolaRed(cadenaImpresion):
	if(os.name=='nt'):
		init()
		print(Fore.RED+cadenaImpresion)
		print(Style.RESET_ALL)
		#Windows
	elif(os.name=='posix'): 
		init()
		print(Fore.RED+cadenaImpresion)
		print(Style.RESET_ALL)
		#Linux

#Impresion por consola en color azul. Indica mensaje, titulo o informacion.
def impresionConsolaBlue(cadenaImpresion):
	if(os.name=='nt'):
		#Windows
		init()
		print(Fore.BLUE+cadenaImpresion)
		print(Style.RESET_ALL)
	elif(os.name=='posix'):
		init()
		print(Fore.BLUE+cadenaImpresion)
		print(Style.RESET_ALL)
		#Linux


#Impresion por consola en color verde. Indica test OK.
def impresionConsolaGreen(cadenaImpresion):
	if(os.name=='nt'): 
		#Windows
		init()
		print(Fore.GREEN+cadenaImpresion)
		print(Style.RESET_ALL)
	elif(os.name=='posix'):
		#Linux
		init()
		print(Fore.GREEN+cadenaImpresion)
		print(Style.RESET_ALL)
		#print(chr(27)+"[4;32;47m"+"Test cantidad de transiciones disparadas. OK") 



'''
Codigo Principal
'''


print '\nInicio del programa'


#logFileB.txt contiene: lista de transiciones disparadas (k= True)
#ordenTInvariante.txt: contiene el orden del TInvariante.



try:
	archivoOrden=open("ordenTInvariante.txt", "r")
	archivoB= open("logFileB.txt","r")
except: 
	impresionConsolaRed('\nError en la apertura del archivo')
	exit(1)


print '\nLectura de archivos'

listaOrden=[] #Patron de orden de las transiciones que pertenecen a T Invariante
listaB=[]
listaBfiltrada=[] #Unicamente transiciones disparadas que pertenecen a T Invariante
matrizI=[]

tInvariantes=[]


flagComienzo=False

for linea in archivoB.readlines():
	if(linea!="\n" and flagComienzo):
			listaB.append(linea[0:len(linea)-1])
		
		#print listaB[0],
	if(not flagComienzo):
		flagComienzo=True

flagComienzo=False
for linea in archivoOrden.readlines():
	if(linea!="\n" and flagComienzo):
			listaOrden.append(linea[:len(linea)-1])
		#print listaC[0],
	if(not flagComienzo):
		flagComienzo=True


archivoB.close()
archivoOrden.close()

print '\nOrden de los T Invariantes:\n'
print listaOrden
print

impresionConsolaBlue('\nCargado de matriz de T Invariantes')
try:
	book = xlrd.open_workbook("excelTren.xls")
except:
	impresionConsolaRed('No se pudo abrir el archivo excel.')
	exit(1)	

sheet = book.sheet_by_index(2)

#Conformacion de matriz de incidencia
for fila in range(1,sheet.nrows):
	matrizI.append([])
	for columna in range(1,sheet.ncols):
		matrizI[fila-1].append(sheet.cell_value(rowx=fila, colx=columna))


cantidadTransiciones=len(matrizI[0])


#Matriz de T Invariantes
sheet = book.sheet_by_index(5)
	
for fila in range(1,sheet.nrows):
	tInvariantes.append([])
	for columna in range(sheet.ncols):
		tInvariantes[fila-1].append(sheet.cell_value(rowx=fila, colx=columna))

if(len(tInvariantes[0])!=cantidadTransiciones):
	impresionConsolaRed('tInvariantes erroneos')
	exit(1)		


impresionConsolaGreen('Matriz T Invariante OK')

#Filtrado de listaB
flagComienzo=True
for lineaB in listaB:
	flagComienzo=True
	for orden in listaOrden:
		if(orden==lineaB and flagComienzo): 
			flagComienzo=False
			listaBfiltrada.append(lineaB)



stringB=""
for lineaB in listaBfiltrada:
	stringB=stringB+lineaB



#Conformacion de patron
stringOrden=""
for lineaOrden in listaOrden:
	stringOrden=stringOrden+lineaOrden


flagNoError=True


#Deteccion de todas las ocurrencias del patron
result= re.findall(stringOrden,stringB)
#result es un arreglo conteniendo las ocurrencias




#Test 

impresionConsolaBlue('Test. Orden T Invariantes.')


operacion=len(stringB) - len(result)*len(stringOrden)

if(not (operacion<len(stringOrden))):
	flagNoError=False
	#El excedente es mayor a un T Invariante completo. Error en la deteccion



if(flagNoError):
	stringAux=stringB[-operacion:]
	stringOrdenAux=stringOrden[:operacion]
	if(not re.search(stringOrdenAux,stringAux)):
		flagNoError=False
		#El excedente no cumple con los T Invariante



#Impresion por consola
if(flagNoError):
	impresionConsolaGreen('Test. Orden de T Invariantes. OK')
	print 'Cantidad de T Invariantes completos: ',
	print	len(result)
	print
	print 'Largo de patron: ',
	print	len(stringOrden)
	print
	print 'Resto de T Invariantes: ',
	print	operacion
	print
	
else:
	impresionConsolaRed('Test. Orden de T Invariantes. FAIL')



print 'Fin del programa.'


