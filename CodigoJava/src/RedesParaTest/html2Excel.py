from BeautifulSoup import BeautifulSoup #Para trabajar con archivos HTML
import urllib
import xlwt #Para trabajar con libros Excel

#Nombres de hojas de Excel.
#La segunda fila es para los invariantes
arregloNombresPaginas=[["Matriz I+", "Matriz I-", "Matriz I", "Matriz H", "Matriz M"], ["T-Invariantes", "P-Invariantes"]] 
#Titulos de tablas en HTML 
#La segunda fila es para los invariantes
arregloVerificacion=[["Forwards incidence", "Backwards incidence","Combined incidence","Inhibition matrix", "Marking"], ["T0", "P0"]] 
enteroVerificacion=0 #Para verificar inicio de tabla
#Solicitud por consola
arregloURL=["\nIngrese la URL del archivo ""Petri net incidence and marking"": ", "\nIngrese la URL del archivo ""Petri net invariant analysis results"": "]

print 'Inicio del programa\n'

libroExcel = xlwt.Workbook()
nombreArchivo= raw_input("Ingrese el nombre del archivo Excel destino: ")

flagInt=0

while(not flagInt):
	try:
		flagInt=1
		strCantTransiciones= raw_input("\nIngrese el numero de transiciones: ")
		
		cantTransiciones=int(strCantTransiciones)
		if(cantTransiciones<1):
			flagInt=0
			print 'Ingrese un valor correcto (numero positivo natural mayor a 0).'
	except ValueError:
		print 'Error. Ingrese un valor correcto (numero positivo natural mayor a 0).'
		flagInt=0

flagInt=0

while(not flagInt):
	try:
		flagInt=1
		strCantPlazas= raw_input("\nIngrese el numero de plazas: ")
		
		cantPlazas=int(strCantPlazas)
		if(cantPlazas<1):
			flagInt=0
			print 'Ingrese un valor correcto (numero positivo natural mayor a 0).'
	except ValueError:
		print 'Error. Ingrese un valor correcto (numero positivo natural mayor a 0).'
		flagInt=0


for url in range(2): #Necesito 2 url. Una para Incidence and Marking. Otro para invariantes.

	#Las tablas se repiten al recorrer con las funciones de la libreria. Este flag es para detectar la repeticion.
	flagRepeticion=False
	urlDir=raw_input(arregloURL[url])

	try:
		fileHTML = urllib.urlopen(urlDir)
	except:
		print 'No se encontro el archivo'
		exit(1)

	textHTML = fileHTML.read()
	soup = BeautifulSoup(textHTML)
	#print soup.prettify()
	#print soup
	#codigo a buscar en la _URL_
	cuerpo=soup.find("body") #Localizo body del codigo fuente HTML
	tablas=cuerpo.findAll("table") #Localizo tablas del body
	contadorPaginas=0 #Contador de las paginas u hojas del Excel
	contadorTablas=0  #Contador de tablas que se van barriendo con las funciones de la libreria. (Las tablas se repiten).
	flagSwitchPagina=False #Flag para indicar el cambio de pagina u hoja de Excel
	hoja = libroExcel.add_sheet(arregloNombresPaginas[url][0]) #Declaro Hoja 0 para cada url
			

	while contadorPaginas<len(arregloNombresPaginas[url]):
			table=tablas[contadorTablas]
			contadorTablas=contadorTablas+1
			
			filas = table.findAll("tr") #Las filas se indican en codigo HTML con tr.
			x = 0
			y= 1
			
			
			
			
			contadorInformacionDescartable=0
			#La variable contadorInformacionDescartable se utiliza para ignorar titulos y comentarios.
			for tr in filas:
				columnas = tr.findAll("td") #Las columnas se indican en codigo HTML con td.
				if not columnas:
					#fila vacia no se imprime nada
					continue
				y = 1
				if(url): #Pagina de invariantes. Cambia la forma de las tablas
					y=0
				
				for td in columnas:
					#print contadorPaginas
					if(td.text.encode("utf-8").find(arregloVerificacion[url][contadorPaginas])!=-1): #Para detectar titulo de tabla
						flagRepeticion=False
						flagSwitchPagina=True
						
					
					texto = td.text.encode("utf-8").strip
					contadorInformacionDescartable=contadorInformacionDescartable + 1

					
					if(contadorInformacionDescartable>5 and not flagRepeticion): #Control de repeticion
						if(td.text.encode("utf-8")=="" or td.text==u'T0' or (td.text=='P0' and contadorPaginas==len(arregloNombresPaginas[url])-1)): #Para evitar repeticion de tablas
							flagRepeticion=True 
							#Ante todas las condiciones anteriores aparece una repeticion.
							
					
					if((contadorInformacionDescartable>3 and url==0 ) or (url)): #Para los invariantes no hace falta ignorar titulos y comentarios
						if(not flagRepeticion): #Si no hay repeticion
							
							if(td.text.encode("utf-8")!="" and td.text.encode("utf-8").find("P")==-1 and td.text.encode("utf-8").find("T")==-1 and td.text.encode("utf-8").find("t")==-1):
								#Es un nro
								hoja.write(x, y, int(td.text.encode("utf-8")))


							else:
								#Es un String
								hoja.write(x, y, td.text)
							
							y = y + 1
							if(url): #Para ver en que instante se comienza una nueva fila en Excel. (Ver configuracion de tablas)
								if(not contadorPaginas):
									if(y>cantTransiciones-1):
										y=0
										x=x+1

								else:
									if(y>cantPlazas-1):
										y=0
										x=x+1

							else:
								if(contadorPaginas<4):
									if(y>cantTransiciones):
										y=0
										x=x+1
								else:
									if(y>cantPlazas):
										y=0
										x=x+1



			if(flagSwitchPagina): #Cambio de pagina
				contadorPaginas=contadorPaginas+1
				if(contadorPaginas<len(arregloNombresPaginas[url])): #Para evitar sobrepasamiento de limites
					hoja = libroExcel.add_sheet(arregloNombresPaginas[url][contadorPaginas])			
				flagSwitchPagina=False



try:

		libroExcel.save(nombreArchivo) #Guardo la informacion en el libro de Excel

	
except:
		print 'No se pudo guardar los datos en el archivo.'
		exit(1)	
		

print '\nFin del programa.'