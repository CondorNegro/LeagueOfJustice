#Python para testear el logueo realizado en Java.

#Autores: Casabella Martin, Kleiner Matias, Lopez Gaston

from colorama import init, Fore, Back, Style #Colores en impresion por consola para resultados de tests.
import xlrd #Para trabajar con libros Excel (Lectura)
import os #Para determinar el sistema operativo
import numpy as np #Para operaciones con matrices y vectores


'''
Cadenas globales
'''
global cadenaImpresion #Para impresion por consola

global cantidadPlazas
global cantidadTransiciones

global matrizM0 #Marcado inicial
global matrizManterior
global matrizMactual

global constantesPinvariantes  #Constantes que otorgan las ecuaciones de los Pinvariantes
global flagVerificarPinv

global flagVerificarTinv
global arregloFlagsTInv
global flagFinalizacionTInv

global politica #Politica utilizada en el monitor.



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



#Test particulares efectuados sobre la lista de contadores de disparos de las transiciones y el marcado final de la simulacion.
def testParticulares(lista, marcadoUltimo):
	impresionConsolaBlue('\nTest particulares')
	cantidadPersonasGeneradas=lista[0] + lista[1] + lista[2]+lista[3]
	cantidadPersonasSubidas=lista[6] + lista[7] + lista[9]+lista[10] + lista[11] + lista[12] + lista[23]+lista[13]
	cantidadPersonasBajadas=lista[25] + lista[26] + lista[27]+lista[28] + lista[29] + lista[31] + lista[32]+lista[33]
	cantidadAutosGeneradosA=lista[15]
	cantidadAutosGeneradosB=lista[20]
	cantidadAutosPasanBarreraA=lista[17]
	cantidadAutosPasanBarreraB=lista[22]
	print 'Resultado de politica: '
	print 'Cantidad de personas subidas: ',
	print cantidadPersonasSubidas
	print 'Cantidad de personas bajadas: ',
	print cantidadPersonasBajadas
	print 'Cantidad de personas generadas: ',
	print cantidadPersonasGeneradas
	print 'Resta: ',
	resta=cantidadPersonasSubidas-cantidadPersonasBajadas
	print resta
	print

	#Test de politica. Se efectua en base a un umbrales
	if(politica==1):
		if(resta >= 25):
			impresionConsolaGreen('Test. Politica primero suben. OK')
		else:
			impresionConsolaRed('Test. Politica primero suben. FAIL')

	if(politica==2):
		if(resta <= 25):
			impresionConsolaGreen('Test. Politica primero bajan. OK')
		else:
			impresionConsolaRed('Test. Politica primero bajan. FAIL')

	#Test para verificar que la cantidad de pasajeros que quedan al final en el tren es la diferencia entre la cantidad de 
	#veces que se dispararon las transiciones de subida menos las veces que se dispararon las de bajada
	if((resta)!=(marcadoUltimo[4]+marcadoUltimo[5])):
		impresionConsolaRed('Test. Cantidad final de personas en el tren. FAIL')
		exit(1)

	else:
		impresionConsolaGreen('Test. Cantidad final de personas en el tren. OK')



	#Test. La cantidad de personas bajadas no puede superar a la cantidad de personas subidas
	if(cantidadPersonasBajadas>cantidadPersonasSubidas):
		impresionConsolaRed('Test. Cantidad de personas subidas mayor o igual a cantidad de personas bajadas. FAIL')
		exit(1)
	else:
		impresionConsolaGreen('Test. Cantidad de personas subidas mayor o igual a cantidad de personas bajadas. OK')
	
	#Test. La cantidad de personas subidas no puede superar a la cantidad de personas generadas
	if(cantidadPersonasSubidas>cantidadPersonasGeneradas):
		impresionConsolaRed('Test. Cantidad de personas generadas mayor o igual a cantidad de personas subidas. FAIL')
		exit(1)
	else:
		impresionConsolaGreen('Test. Cantidad de personas generadas mayor o igual a cantidad de personas subidas. OK')
	
	#Test. La cantidad de autos que atraviesan la barrera A no puede superar a la cantidad de autos que se generan para cruzar dicha barrera 
	if(cantidadAutosGeneradosA<cantidadAutosPasanBarreraA):
		impresionConsolaRed('Test. Cantidad de autos generados en barrera A mayor o igual a cantidad de autos que pasaron dicha barrera. FAIL')
		exit(1)
	else:
		impresionConsolaGreen('Test. Cantidad de autos generados en barrera A mayor o igual a cantidad de autos que pasaron dicha barrera. OK')
	
	#Test. La cantidad de autos que atraviesan la barrera B no puede superar a la cantidad de autos que se generan para cruzar dicha barrera 
	if(cantidadAutosGeneradosB<cantidadAutosPasanBarreraB):
		impresionConsolaRed('Test. Cantidad de autos generados en barrera B mayor o igual a cantidad de autos que pasaron dicha barrera. FAIL')
		exit(1)
	else:
		impresionConsolaGreen('Test. Cantidad de autos generados en barrera B mayor o igual a cantidad de autos que pasaron dicha barrera. OK')
	



#Funcion para verificar los PInvariantes.
def verificarPinvariantes(matrizM, pInvariantes, constantesPinvariantes):
	flagVerificarPinv=True
	constantesPinvariantesActual=[]
	for filaPinvariante in range(len(pInvariantes)):
		constantesPinvariantesActual.append((matrizM*pInvariantes[filaPinvariante]).sum())
		#Obtengo el resultado de la ecuacion de los Pinvariantes con el marcado actual
	constantesPinvariantesActual=np.array(constantesPinvariantesActual).reshape(len(constantesPinvariantesActual),1)
	constantesPinvariantesActual=np.asarray(constantesPinvariantesActual,dtype=int)
	#Verifico si los resultados de las ecuaciones con el marcado actual versus el marcado inicial (M0) coinciden
	vectorAuxEqual=constantesPinvariantesActual!= constantesPinvariantes
	for i in range(len(vectorAuxEqual)):
		if(vectorAuxEqual[i][0]):
			#No se cumplen los Pinvariantes
			flagVerificarPinv=False
			impresionConsolaRed('Error en verificacion de PInvariantes')
			exit(1)

#Funcion auxiliar. Verifica si todos los flags correspondientes a las filas de Tinvariantes son False
def verificarFlagFinalizacion(flags):
	for i in range(len(flags)):
		if(flags[i]==True):
			return True;
	return False;

#Funcion auxiliar. Determina si algun elemento de la lista es negativo
def isElementoNegativo(lista):
	for i in range(len(lista)):
		if(lista[i]<0):
			return True
	return False;

#Funcion que permite verificar Tinvariantes
def verificarTinvariantes(tInv, listaTranciones, I, M0, Multimo):
	flagFinalizacionTInv=False
	listaContadores=[]
	arregloFlagsTInv=[]
	for i in range(cantidadTransiciones):
		listaContadores.append(0)
	for transicion in listaTranciones:
		listaContadores[transicion]=listaContadores[transicion]+1
		#Cuento la cantidad de veces que se disparo cada transicion
	listaContadores=np.array(listaContadores)
	testParticulares(listaContadores, Multimo) #Llamada a tests particulares con la listaContadores
	listaContadoresAnterior=listaContadores
	arrayTinvariantes=np.array(tInv)
	for fila in range(len(tInv)):
		arregloFlagsTInv.append(True)
		#Puedo restar a listaContadores todas las filas de Tinvariantes debido al flag True
	while(not flagFinalizacionTInv):
		for fila in range(len(tInv)):
			if(arregloFlagsTInv[fila]):
				listaContadoresAnterior=listaContadores
				#Resto a listaContadores la fila de Tinvariantes. Si la resta no arroja ningun elemento negativo, indica que se cumplio
				#el Tinvariante
				listaContadores=listaContadores -  arrayTinvariantes[fila]
				flagListaNegativa=isElementoNegativo(listaContadores)
				if(flagListaNegativa):
					listaContadores=listaContadoresAnterior
					arregloFlagsTInv[fila]=False
			flagFinalizacionTInv=verificarFlagFinalizacion(arregloFlagsTInv)
	listaContadores=np.asmatrix(listaContadores)
	#Efectuo el producto de la matriz de incidencia con el excedente que queda de listaContadores, el cual representa una suma de vectores
	#de disparo
	producto=I*np.transpose(listaContadores)
	#Resto al marcado final de la simulacion el producto anterior
	resta=Multimo-producto
	resta=np.asmatrix(resta,dtype=int)
	#La resta debe dar igual al marcado inicial, si es que se verifican los Tinvariantes
	comparacion=resta!=M0
	for i in range(len(comparacion)):
		if(comparacion[i]):
			#No se verifican los Tinvariantes
			impresionConsolaRed('\n Error en TInvariantes')
			exit(1)

#Test para verificar que el tren no puede estar en mas de una estacion
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


#Test para verificar que mientras el tren cruza la barrera, los autos no pueden atravesarla
def verificarAutosNoCruzan(marcados):
	flagPrimeraVez=True
	valorMarcadoSumideroAutos=0
	#Barrera B
	for i in range(len(marcados)):
		if(marcados[i][17]>0):
			#Tren en barrera
			if(flagPrimeraVez):
				flagPrimeraVez=False
				valorMarcadoSumideroAutos=marcados[i][19]
				#Almaceno el valor de la plaza sumidero de autos que cruzan la barrera
			else:
				if(marcados[i][19]!=valorMarcadoSumideroAutos):
					#Un auto atraveso la barrera
					impresionConsolaRed("Test. Autos no cruzan barrera B cuando pasa el tren. FAIL")
					exit(1)
		else:
			flagPrimeraVez=True
	
	flagPrimeraVez=True
	valorMarcadoSumideroAutos=0
	#Barrera A
	for i in range(len(marcados)):
		if(marcados[i][13]>0):
			if(flagPrimeraVez):
				flagPrimeraVez=False
				valorMarcadoSumideroAutos=marcados[i][15]
			else:
				if(marcados[i][15]!=valorMarcadoSumideroAutos):
					impresionConsolaRed("Test. Autos no cruzan barrera A cuando pasa el tren. FAIL")
					exit(1)
		else:
			flagPrimeraVez=True

	impresionConsolaGreen("Test. Autos no cruzan barrera A o B cuando pasa el tren. OK")


#Test para verificar que la cantidad de pasajeros es inalterable mientras el tren no se encuentre en una estacion
def verificarCantidadPasajerosInalterableEnViaje(marcados):
	flagPrimeraVez=True
	valorMarcadoVagonLugaresVacios=0
	valorMarcadoVagonLugaresOcupados=0
	valorMarcadoMaquinaLugaresVacios=0
	valorMarcadoMaquinaLugaresOcupados=0
	
	for i in range(len(marcados)):
		if(marcados[i][0]>0 and marcados[i][1]>0 and marcados[i][2]>0 and marcados[i][3]>0):
			#Tren viajando
			if(flagPrimeraVez):
				flagPrimeraVez=False
				valorMarcadoVagonLugaresVacios=marcados[i][6]
				valorMarcadoVagonLugaresOcupados=marcados[i][5]
				valorMarcadoMaquinaLugaresVacios=marcados[i][7]
				valorMarcadoMaquinaLugaresOcupados=marcados[i][4]
			else:
				if(marcados[i][6]!=valorMarcadoVagonLugaresVacios or marcados[i][7]!=valorMarcadoMaquinaLugaresVacios or marcados[i][5]!=valorMarcadoVagonLugaresOcupados or marcados[i][4]!=valorMarcadoMaquinaLugaresOcupados):
					#Se modifico el numero de pasajeros
					impresionConsolaRed("Test. Pasajeros no bajan ni suben al tren cuando el mismo esta en viaje. FAIL")
					exit(1)
		else:
			flagPrimeraVez=True
	

	impresionConsolaGreen("Test. Pasajeros no bajan ni suben al tren cuando el mismo esta en viaje. OK")



#Test para verificar que las personas que se suben al tren en una estacion no se bajen en la misma estacion
def verificarPersonasSubenNoBajanEnMismaEstacion(marcados):
	flagPrimeraVez=True
	valorMarcadoMinimoPersonasSubidas=0
	

	for i in range(len(marcados)):
		if((marcados[i][0]+ marcados[i][1]+ marcados[i][2]+marcados[i][3])==3):
			if(marcados[i][21]<1):
				#Error estructural. Debe existir un token en la plaza 21 para asegurar que no se bajen los pasajeros
				#que se subieron a esa estacion
				impresionConsolaRed("Test. Pasajeros no bajan del tren en la misma estacion en que se subieron. Token condicion. FAIL")
				exit(1)
			if(flagPrimeraVez):
				flagPrimeraVez=False
				valorMarcadoMinimoPersonasSubidas=marcados[i][22] #Almaceno el valor inicial de pasajeros que se quieren subir en esa estacion
				#Dicho valor no puede decrementarse				
			else:
				if(marcados[i][22]<valorMarcadoMinimoPersonasSubidas):
					#El numero de pasajeros que se queria subir en esa estacion se decremento. Algunos se han bajado
					impresionConsolaRed("Test. Pasajeros no bajan del tren en la misma estacion en que se subieron. FAIL")
					exit(1)
		else:
			flagPrimeraVez=True
	

	impresionConsolaGreen("Test. Pasajeros no bajan del tren en la misma estacion en que se subieron. OK")











'''
Codigo Principal
'''


print '\nInicio del programa'

#logFileA.txt contiene: transiciones a disparar, valores de k devueltos por la clase Red De Petri y el marcado de la red
#luego de ese intento de disparo
#logFileB.txt contiene: lista de transiciones disparadas (k= True)
#logFileC.txt contiene: la longitud de la lista de logFileB.txt


try:
	archivoA=open("logFileA.txt","r")
	archivoC= open("logFileC.txt","r")
	archivoB= open("logFileB.txt","r")
except: 
	impresionConsolaRed('\nError en la apertura del archivo')
	exit(1)



flagInt=0

while(not flagInt):
	try:
		flagInt=1
		strPolitica= raw_input('\nIngrese el numero adecuado de acuerdo a la politica utilizada (0 - Aleatorio; 1 - Primero Sube; 2 - Primero Baja): ')
		
		politica=int(strPolitica)
		if(politica<0 or politica>2):
			flagInt=0
			print 'Ingrese un valor correcto (0, 1 o 2).'
	except ValueError:
		print 'Error. Ingrese un valor correcto (0, 1 o 2).'
		flagInt=0


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



#Primer test. Relacionado a lectura de archivos de logueo.

impresionConsolaBlue('\nTest cantidad de transiciones disparadas.')

contadorDeTransicionesDisparadas=0

try:
	contadorDeTransicionesDisparadas=int(listaC[0])
	#print contadorDeTransicionesDisparadas

except:
	impresionConsolaRed('Error en metodo int()')
	exit(1)




#Segundo test. Relacionado a lectura de archivos de logueo.

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
	#Lectura de libro de Excel
	try:
 		book = xlrd.open_workbook("excelTren.xls")
	except:
		impresionConsolaRed('No se pudo abrir el archivo excel.')
		exit(1)	

	#Busqueda de Hoja de Excel

	sheet = book.sheet_by_index(2)

	#Conformacion de matriz de incidencia
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

	

	#Conformacion de vectores de Pinvariantes

	sheet = book.sheet_by_index(6)
	
	for fila in range(1,sheet.nrows):
		pInvariantes.append([])
		for columna in range(sheet.ncols):
			pInvariantes[fila-1].append(sheet.cell_value(rowx=fila, colx=columna))

	if(len(pInvariantes[0])!=cantidadPlazas):
		impresionConsolaRed('pInvariantes erroneos')
		exit(1)		


	constantesPinvariantes=np.zeros((len(pInvariantes),1),dtype=int)


	
	#Conformacion de vectores de Tinvariantes

	sheet = book.sheet_by_index(5)
	
	for fila in range(1,sheet.nrows):
		tInvariantes.append([])
		for columna in range(sheet.ncols):
			tInvariantes[fila-1].append(sheet.cell_value(rowx=fila, colx=columna))

	if(len(tInvariantes[0])!=cantidadTransiciones):
		impresionConsolaRed('tInvariantes erroneos')
		exit(1)		



	#Cargado de la informacion de los archivos de logueo en listas
	
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

	



	#Verificacion de cargado correcto de informacion

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






	#Testear evolucion marcado. De acuerdo a los disparos (True or False) de las transiciones a disparar
	
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







	#Impresion de resultados de test por consola
	if(flagEvolucionMarcado):
		impresionConsolaGreen("\nTest evolucion Marcado. OK")
	else:
		impresionConsolaRed("\nTest evolucion Marcado. FAIL")
		
	for i in range(len(listaB)):
		#Lista B contiene la lista de transiciones disparadas
		try:
			listaB[i]=int(listaB[i])
		except:
			impresionConsolaRed("\nError en pasar elementos de listaB a int")
			exit(1)
	


	#Verifico Tinvariantes
	verificarTinvariantes(tInvariantes, listaB, matrizI, matrizM0, matrizMactual)

	#En este punto de la ejecucion del script los Pinvariantes y los Tinvariantes han pasado con exito las pruebas
	impresionConsolaBlue('\nInvariantes:')
	impresionConsolaGreen('\nPInvariantes OK.')
	impresionConsolaGreen('\nTInvariantes OK.')

	#En este punto de la ejecucion del script el test de tren en una sola estacion ha pasado con exito. 
	impresionConsolaBlue('\nTest particulares')
	impresionConsolaGreen('\nTest Tren en una sola estacion. OK')

	#Llamada a tests
	verificarAutosNoCruzan(matricesMarcado)
	verificarCantidadPasajerosInalterableEnViaje(matricesMarcado)
	verificarPersonasSubenNoBajanEnMismaEstacion(matricesMarcado)




print '\nFin del programa'