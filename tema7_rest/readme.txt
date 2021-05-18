Servicii Web

- aplicatii soft disponibile pe Internet (protocol HTTP) ce transmit mesaje in format XML
- rezolva problema cross-platform (se reutilizeaza niste standarde)
- componente - XML, SOAP, UDDI, WSDL
	1. furnizorul serviciului trimitea un fisier WSDL serviciului UDDI
	2. clientul contacta UDDI pt a descoperi cine este furnizorul datelor, apoi contacta furnizorul prin SOAP
	3. furnizorul valida cererea si trimitea datele cerute in format XML prin SOAP
	4. structura datelor in format XML trebuia validata de client folosind un fisier XSD (XML Schema)

REST
- omite partea ce tine de limbaj (IDL)
- clientul cere o STARE
RESTful:
	- client-server - cele 2 aplicatii ar trebui sa poata evolua independent! clientul ar trebui sa stie doar de URI-ul resursei
	- server stateless (serverul raspunde la cererea din acest moment independent de cererile anterioare - nu pastreaza info despre ultima cerere)
	- cacheable (datele returnate ca raspuns ar trebui marcate ca fiind cacheable sau nu)
	- uniform interface (dupa ce se fac publice URI-urile, acestea nu vor fi modificate; o resursa are un singur URI; resursa nu este foarte mare...)
	- layered system
	- code on demand (optional) - pe langa reprezentarea unei resurse, serverul poate raspunde si cu cod executabil, pentru a suporta o parte a aplicatiei
Resursa este orice entitate - obiect real, imagine, document, colectie de resurse etc

Obs. Fara verbe in denumirea URI-ului care identifica o resursa!
Obs. Fara "/" la finalul URI-ului! Se folosesc cratime si litere mici!
Obs. Resursele sunt decuplate de reprezentarea lor! Continutul resursei poate fi accesat folosind XML, TXT, PDF, JSON, HTML etc (si se folosesc antete)
Obs. Pentru filtrari nu se creeaza URI-uri noi ci se folosesc query parameters!

Pasi:
	1. Identificarea modelului obiectual
	2. Stabilirea URI-urilor prin care pot fi accesate resursele
	3. Determinarea reprezentarii (ex. doar JSON)
	4. atribuirea metodelor HTTP


REST Spring
- pentru servicii REST folosim componenta Spring Web MVC
- un front controller primeste cererile clientilor; pe baza URL-ului front controllerul decide carui Controller ii va delega cererea respectiva
- server web implicit: Tomcat

	@RestController -> marcheaza o clasa care va raspunde unor cereri, iar pe baza altor adnotari va specifica caror cereri va raspunde
	@RequestMapping -> identifica metodele ce trebuie sa proceseze cereri
	@GetMapping, @PostMapping, @PutMapping, @DeleteMapping -> in plus fata de @RequestMapping precizeaza metodele HTTP respective
	@PathVariable -> adnotare pt variabilele de cale extrase din URI si date ca parametri ai metodelor
	@RequestParam -> adnotare pt query parameters

	@RequestBody -> informatiile se regasesc in corpul cererii si informatiile sunt convertite la tipul precizat
	RequestEntity<E> -> wrapper pt cererea primita de la client; contine reprezentarea informatiei + date aditionale
	@ResponseBody -> obiectul returnat va fi convertit la tipul negociat si va fi pus in corpul raspunsului primit de client
	ResponseEntity<E> -> wrapper pt raspunsul dat clientului; contine obiectul returnat + putem seta antetele corespunzatoare
	
	MappingJackson2HttpMessageConverter -> converteste obiecte simple (care au atribute simple) si HashMaps in format JSON
		Obs. Daca un obiect refera un alt obiect, liste, dictionare etc el nu este "obiect simplu"!
	
	Exception Handling:
		- daca vrem sa aruncam o exceptie dintr-o metoda endpoint, avem 2 modalitati de a stabili ce primeste clientul:
			a) in controller sa avem o metoda cu @ExceptionHandler si @ResponseStatus ce primeste un parametru de tipul exceptiei
			b) in clasa in care am definit exceptia adaug @ResponseStatus (si codul de raspuns)
				
	Pornirea aplicatiei:
		- clasa main este adnotata cu @SpringBootApplication 
		  	(care adauga @Configuration, @EnableAutoConfiguration, @EnableWebMvc si @ComponentScan clasei curente)
		- obs. clasa main ar trebui sa se afle in pachetul radacina al aplicatiei! 
		  	(cautarea claselor adnotate se face pornind de la pachetul cu main)
		- Tomcat va fi descarcat si pornit automat
	
	*de citit doc RestTemplate (pt .NET: HttpClient din System.Net.Http sau Microsoft.AspNet.WebApi.Client)


