#Python para testear el logueo realizado en Java.

#Autores: Casabella Martin, Kleiner Matias, Lopez Gaston
from colorama import init, Fore, Back, Style
import xlrd #Para trabajar con libros Excel (Lectura)
import os #Para determinar el sistema operativo
import numpy as np

global cadenaImpresion
global cantidadPlazas
global cantidadTransiciones
global matrizM0
global matrizManterior
global matrizMactual
global constantesPinvariantes
global flagVerificarPinv
global flagVerificarTinv
global arregloFlagsTInv
global flagFinalizacionTInv


'''
#Flags de tests particulares
'''



'''
#Funciones
'''


def impresionConsolaRed(cadenaImpresion):
	if(os.name=='nt'):#Windows
		init()
		print(Fore.RED+cadenaImpresion)
		print(Style.RESET_ALL)
	elif(os.name=='posix'): 
		init()
		print(Fore.RED+cadenaImpresion)
		print(Style.RESET_ALL)
		#Linux

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



def verificarPinvariantes(matrizM, pInvariantes, constantesPinvariantes):
	flagVerificarPinv=True
	constantesPinvariantesActual=[]
	for filaPinvariante in range(len(pInvariantes)):
		constantesPinvariantesActual.append((matrizM*pInvariantes[filaPinvariante]).sum())
	constantesPinvariantesActual=np.array(constantesPinvariantesActual).reshape(len(constantesPinvariantesActual),1)
	constantesPinvariantesActual=np.asarray(constantesPinvariantesActual,dtype=int)
	vectorAuxEqual=constantesPinvariantesActual!= constantesPinvariantes
	for i in range(len(vectorAuxEqual)):
		if(vectorAuxEqual[i][0]):
			flagVerificarPinv=False
			impresionConsolaRed('Error en verificacion de PInvariantes')
			exit(1)


def verificarFlagFinalizacion(flags):
	for i in range(len(flags)):
		if(flags[i]==True):
			return True;
	return False;

def isElementoNegativo(lista):
	for i in range(len(lista)):
		if(lista[i]<0):
			return True
	return False;

def verificarTinvariantes(tInv, listaTranciones, I, M0, Multimo):
	flagFinalizacionTInv=False
	listaContadores=[]
	arregloFlagsTInv=[]
	for i in range(cantidadTransiciones):
		listaContadores.append(0)
	for transicion in listaTranciones:
		listaContadores[transicion]=listaContadores[transicion]+1
	listaContadores=np.array(listaContadores)
	listaContadoresAnterior=listaContadores
	arrayTinvariantes=np.array(tInv)
	for fila in range(len(tInv)):
		arregloFlagsTInv.append(True)
	while(not flagFinalizacionTInv):
		for fila in range(len(tInv)):
			if(arregloFlagsTInv[fila]):
				listaContadoresAnterior=listaContadores
				listaContadores=listaContadores -  arrayTinvariantes[fila]
				flagListaNegativa=isElementoNegativo(listaContadores)
				if(flagListaNegativa):
					listaContadores=listaContadoresAnterior
					arregloFlagsTInv[fila]=False
			flagFinalizacionTInv=verificarFlagFinalizacion(arregloFlagsTInv)
	listaContadores=np.asmatrix(listaContadores)
	producto=I*np.transpose(listaContadores)
	resta=Multimo-producto
	resta=np.asmatrix(resta,dtype=int)
	comparacion=resta!=M0
	for i in range(len(comparacion)):
		if(comparacion[i]):
			impresionConsolaRed('\n Error en TInvariantes')
			exit(1)


def testTrenEnUnaEstacion(marcado):
	if(marcado[0]==0):
		if(marcado[1]==0 or marcado[2]==0 or marcado[3]==0):
			impresionConsolaRed('Error. Un tren en mas de una estacion. Caso 1')
			exit(1)
	elif(marcado[1]==0):
		if(marcado[0]==0 or marcado[2]==0 or marcado[3]==0):
			impresionConsolaRed('Error. Un tren en mas de una estacion. Caso 2')
			exit(1)
	elif(marcado[2]==0):
		if(marcado[1]==0 or marcado[0]==0 or marcado[3]==0):
			impresionConsolaRed('Error. Un tren en mas de una estacion. Caso 3')
			exit(1)
	elif(marcado[3]==0):
		if(marcado[1]==0 or marcado[2]==0 or marcado[0]==0):
			impresionConsolaRed('Error. Un tren en mas de una estacion. Caso 4')
			exit(1)



print 'Inicio del programa'



try:
	archivoA=open("logFileA.txt","r")
	archivoC= open("logFileC.txt","r")
	archivoB= open("logFileB.txt","r")
except: 
	impresionConsolaRed('\nError en la apertura del archivo')
	exit(1)






print '\nLectura de archivos'

listaA=[]
listaB=[]
listaC=[]
matrizI=[]
pInvariantes=[]
tInvariantes=[]
matricesMarcado=[]
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

impresionConsolaBlue('\nTest cantidad de transiciones disparadas.')

contadorDeTransicionesDisparadas=0

try:
	contadorDeTransicionesDisparadas=int(listaC[0])
	#print contadorDeTransicionesDisparadas

except:
	impresionConsolaRed('Error en metodo int()')
	exit(1)


flagCantidadTransicionesOK=False

if(contadorDeTransicionesDisparadas==len(listaB)):
	impresionConsolaGreen("\nTest cantidad de transiciones disparadas. OK")
	
	flagCantidadTransicionesOK=True
    #print(chr(27)+"[4;32;47m"+"Test cantidad de transiciones disparadas. OK") 
	#print 'Test cantidad de transiciones disparadas. OK'
else:
	impresionConsolaRed("\nTest cantidad de transiciones disparadas. FAIL")
	

if(flagCantidadTransicionesOK):

	print 'Cargado de matriz I'

	try:
 		book = xlrd.open_workbook("excelTren.xls")
	except:
		impresionConsolaRed('No se pudo abrir el archivo excel.')
		exit(1)	

	#print "The number of worksheets is", book.nsheets
	#print "Worksheet name(s):", book.sheet_names()

	sheet = book.sheet_by_index(2)


	for fila in range(1,sheet.nrows):
		matrizI.append([])
		for columna in range(1,sheet.ncols):
			matrizI[fila-1].append(sheet.cell_value(rowx=fila, colx=columna))

	cantidadTransiciones=len(matrizI[0])
	cantidadPlazas=len(matrizI)
	
	print '\nCantidad de plazas:',
	print cantidadPlazas
	print 'Cantidad de transiciones:',
	print cantidadTransiciones

	matrizI=np.array(matrizI).reshape(cantidadPlazas,cantidadTransiciones)
	matrizI=np.asmatrix(matrizI)

	sheet = book.sheet_by_index(6)
	
	for fila in range(1,sheet.nrows):
		pInvariantes.append([])
		for columna in range(sheet.ncols):
			pInvariantes[fila-1].append(sheet.cell_value(rowx=fila, colx=columna))

	if(len(pInvariantes[0])!=cantidadPlazas):
		impresionConsolaRed('pInvariantes erroneos')
		exit(1)		


	constantesPinvariantes=np.zeros((len(pInvariantes),1),dtype=int)


	sheet = book.sheet_by_index(5)
	
	for fila in range(1,sheet.nrows):
		tInvariantes.append([])
		for columna in range(sheet.ncols):
			tInvariantes[fila-1].append(sheet.cell_value(rowx=fila, colx=columna))

	if(len(tInvariantes[0])!=cantidadTransiciones):
		impresionConsolaRed('tInvariantes erroneos')
		exit(1)		
	
	print '\nCargado de valores de K, transiciones a disparar y marcados.'
	flagComienzo=True
	for indice in range(len(listaA)):
		if(not flagComienzo):
			if(indice%4==1):
				aux=listaA[indice].split('-')
				matricesMarcado.append(aux[:len(aux)-1])
			if(indice%4==3):
				if("true" in listaA[indice]):
					listaK.append(True)
				elif("false" in listaA[indice]):
					listaK.append(False)
				else:
					impresionConsolaRed('Error en listaK.')
					exit(1)
			if(indice%4==2):
				index= listaA[indice].find(":", 0, len(listaA[indice]))
				listaTransicionesADisparar.append(int(listaA[indice][index+2:]))


		if(flagComienzo):
			if(indice==1):
				aux=listaA[indice].split('-')
				matricesMarcado.append(aux[:len(aux)-1])
				
			if(indice==3):
				flagComienzo=False
				if("true" in listaA[indice]):
					listaK.append(True)
				elif("false" in listaA[indice]):
					listaK.append(False)
				else:
					impresionConsolaRed('Error en listaK.')
					exit(1)

			if(indice==2):
				index= listaA[indice].find(":", 0, len(listaA[indice]))
				try:
					listaTransicionesADisparar.append(int(listaA[indice][index+2:]))
				except:
					impresionConsolaRed('Error en pasar a int la lista transiciones a disparar')
					exit(1)

	#for i in range(len(listaTransicionesADisparar)):
		#print listaTransicionesADisparar[i]
		#print listaK[i]

	if(len(listaK)!=len(listaTransicionesADisparar)):
		impresionConsolaRed("Lista K de distinto tamanio que lista de transiciones a disparar")
		exit(1)
	if(len(listaK)+1!=len(matricesMarcado)):
		impresionConsolaRed("Tamanio incorrecto de lista de marcados")
		print len(listaK)
		print len(matricesMarcado)
		exit(1)

	for i in range(len(matricesMarcado)):
		for j in range(len(matricesMarcado[i])):
			if(len(matricesMarcado[i])!=cantidadPlazas):
				impresionConsolaRed("Marcado con numero de plazas distinto.")
				exit(1)
			try:
				matricesMarcado[i][j]=int(matricesMarcado[i][j])
			except:
				impresionConsolaRed('Error en pasar a int los marcados')
				exit(1)



	flagEvolucionMarcado=True
	impresionConsolaBlue('\nTest evolucion Marcado')

	#Testear evolucion marcado.
	
	for marca in range(len(matricesMarcado)):
		testTrenEnUnaEstacion(matricesMarcado[marca])
		if(marca==0):
			matrizM0aux=np.array(matricesMarcado[0]).reshape(1,cantidadPlazas)
			matrizM0=np.array(matricesMarcado[0]).reshape(cantidadPlazas,1)
			for filaPinvariante in range(len(pInvariantes)):
				constantesPinvariantes[filaPinvariante]=(matrizM0aux*pInvariantes[filaPinvariante]).sum()
			matrizMactual=matrizM0
		
		else:
			matrizManterior=matrizMactual
			matrizMactual=np.array(matricesMarcado[marca]).reshape(cantidadPlazas,1);

			if(not listaK[marca-1]):
				aux=matrizMactual!=matrizManterior   #Comparo que si k es falso, la marca se mantenga constante
				for booleano in range(len(aux)):
					if(aux[booleano][0]):
						flagEvolucionMarcado=False
			else:
				transicion=listaTransicionesADisparar[marca-1]
				vectorDisparo=np.zeros((cantidadTransiciones,1), dtype=int)
				vectorDisparo[transicion][0]=1
				vectorDisparo=np.asmatrix(vectorDisparo)
				matrizProducto= matrizI*vectorDisparo
				matrizSuma=matrizManterior+matrizProducto
				aux= matrizMactual != matrizSuma   #Comparo que si k es true,  la marca evolucione correctamente segun los disparos
				for booleano in range(len(aux)):
					if(aux[booleano][0]):
						flagEvolucionMarcado=False
			if(flagEvolucionMarcado):
				matrizMactualAux=np.array(matricesMarcado[marca]).reshape(1,cantidadPlazas);
				verificarPinvariantes(matrizMactualAux, pInvariantes, constantesPinvariantes)








	if(flagEvolucionMarcado):
		impresionConsolaGreen("\nTest evolucion Marcado. OK")
	    #print(chr(27)+"[4;32;47m"+"Test cantidad de transiciones disparadas. OK") 
		#print 'Test cantidad de transiciones disparadas. OK'
	else:
		impresionConsolaRed("\nTest evolucion Marcado. FAIL")
		
	for i in range(len(listaB)):
		try:
			listaB[i]=int(listaB[i])
		except:
			impresionConsolaRed("\nError en pasar elementos de listaB a int")
			exit(1)
	verificarTinvariantes(tInvariantes, listaB, matrizI, matrizM0, matrizMactual)
	impresionConsolaBlue('\nInvariantes:')
	impresionConsolaGreen('\nPInvariantes OK.')
	impresionConsolaGreen('\nTInvariantes OK.')


	impresionConsolaBlue('\nTest particulares')
	impresionConsolaGreen('\nTest Tren en una sola estacion. OK')





print '\nFin del programa'