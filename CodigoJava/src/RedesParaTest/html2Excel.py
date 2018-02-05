from BeautifulSoup import BeautifulSoup #Para trabajar con archivos HTML
import urllib
import xlwt #Para trabajar con libros Excel

#Nombres de hojas de Excel.
arregloNombresPaginas=["Matriz I+", "Matriz I-", "Matriz I", "Matriz H", "Matriz M"] 
#Titulos de tablas en HTML 
arregloVerificacion=["Forwards incidence", "Backwards incidence","Combined incidence","Inhibition matrix", "Marking"]
enteroVerificacion=0 #Para verificar inicio de tabla

#Las tablas se repiten al recorrer con las funciones de la libreria. Este flag es para detectar la repeticion.
flagRepeticion=False

libroExcel = xlwt.Workbook()

url=raw_input("Ingrese la URL del archivo: ")
nombreArchivo= raw_input("Ingrese el nombre del archivo Excel destino: ")
try:
	fileHTML = urllib.urlopen(url)
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
hoja = libroExcel.add_sheet(arregloNombresPaginas[0]) #Declaro Hoja 0
		

while contadorPaginas<len(arregloNombresPaginas):
		table=tablas[contadorTablas]
		contadorTablas=contadorTablas+1
		
		filas = table.findAll("tr") #Las filas se indican en codigo HTML con tr.
		x = 0
		y= 1
		
		
		#print rows
		
		contadorInformacionDescartable=0
		for tr in filas:
			columnas = tr.findAll("td") #Las columnas se indican en codigo HTML con td.
			if not columnas:
				#fila vacia no se imprime nada
				continue
			y = 1
			for td in columnas:
				#print contadorPaginas
				if(td.text.encode("utf-8").find(arregloVerificacion[contadorPaginas])!=-1): #Para detectar titulo de tabla
					flagRepeticion=False
					flagSwitchPagina=True
				
				texto = td.text.encode("utf-8").strip
				contadorInformacionDescartable=contadorInformacionDescartable + 1

				
				if(contadorInformacionDescartable>5 and not flagRepeticion):
					if(td.text.encode("utf-8")=="" or td.text==u'T0' or (td.text=='P0' and contadorPaginas==len(arregloNombresPaginas)-1)): #Para evitar repeticion de tablas
						flagRepeticion=True
				
				if(contadorInformacionDescartable>3):
					if(not flagRepeticion):
						#print x,y,contadorPaginas, td.text
						if(td.text.encode("utf-8")!="" and td.text.encode("utf-8").find("P")==-1 and td.text.encode("utf-8").find("T")==-1 and td.text.encode("utf-8").find("t")==-1):
							#Es un nro
							hoja.write(x, y, int(td.text.encode("utf-8")))
						else:
							#Es un String
							hoja.write(x, y, td.text)
						#print(x, y, td.text)
						y = y + 1
						if(y>4):
							y=0
							x=x+1

		if(flagSwitchPagina):
			contadorPaginas=contadorPaginas+1
			if(contadorPaginas<len(arregloNombresPaginas)):
				hoja = libroExcel.add_sheet(arregloNombresPaginas[contadorPaginas])			
			flagSwitchPagina=False





try:

		libroExcel.save(nombreArchivo) #Guardo la informacion en el libro de Excel

	
except:
		print 'No se pudo guardar los datos en el archivo.'
		exit(1)	
		
