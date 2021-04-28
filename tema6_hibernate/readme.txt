								Object Relational Mapping

- tehnica ce converteste informatiile dintr-un sistem orientat obiect intr-o baza de date relationala
- delegam instrumentului tot ce tine de managementul persistentei
- lucram doar cu entitatile din domeniu

Maparea - proprietatilor, metadatelor SI relatiilor
Shadow Information - informatii pe care obiectele trebuie sa le pastreze strict pt a putea fi persistate (primary keys, info pt controlul concurentei, versiunii etc)

Strategii pentru "Impedance Mismatch" (tabele vs obiecte, tipuri de date diferite, relatii):

	-> mostenirea
		- o singura tabela pt intreaga ierarhie (o singura tabela insa multe campuri goale - atribute ce nu vor fi completate pt intreaga ierarhie; tabela creste)
		- fiecare clasa are propria tabela (nu avem spatii goale dar ajungem cu multe tabele + inregistrari extra pt a rezolva mostenirea => join-uri => lent)
		- fiecare clasa concreta are propria tabela (atributele din clasa de baza sunt duplicate => nu mai trebuie sa se faca join; Obs. trebuie chei unice!)
		- maparea claselor intr-o structura de tabele generica -> mapari intre informatiile din metadate (poate fi extinsa, e flexibila dar greu de gestionat...)

	-> maparea relatiilor
		- problema 1: obiectele trateaza legaturile prin pastrearea referintelor (existente in timpul executiei), bazele de date folosesc chei in tabele (permanente)
		- problema 2: obiectele pot folosi colectii dar in BD avem normalizare => inversarea structurii de date dintre obiecte si tabele
		- rezolvare: Sablonul Identity, maparea cheii straine, tabele de asociere etc
	
	-> maparea proprietatilor statice
		- NU se introduce o coloana in tabela corespunzatoare ci avem tabele diferite pt atributele statice (dar atunci avem foarte multe tabele => s-a propus sa nu
		  avem cate o tabela pt fiecare atribut static ci o tabela cu o singura inregistrare care sa contina toate atributele statice corespunzatoare unei clase)
		- daca folosim o singura tabela cu atribute statice pt toate clasele : numar minim de tabele introdus dar apar probleme de concurenta + sa fim atenti la nume
		- putem folosi o schema generica (numar minim de tabele introdus dar necesita convertiri intre tipuri de date => pb de performanta)


Hibernate

	-> open source
	-> instructiuni SQL generate automat in timpul executiei
	-> informatiile despre mapare sunt transmise ca si document de mapare in format XML sau prin adnotari
	-> foloseste JTA (Java Transaction API), JDBC si JNDI (Java Naming and Directory Interface)
	-> pt a utiliza mediul persistent trebuie creat un SessionFactory; 
	   Session se obtine de fiecare data cand facem o operatie; 
           prin intermediul unui Session cream un Transaction (care abstractizeaza codul corespunzator unei tranzactii JDBC sau JTA, UserTransaction etc)
	   Configuration - folosit pt configurarea si pornirea Hibernate
	   Query - permite efectuarea de interogari si controleaza modul in care este executata interogarea; HQL sau SQL
	-> pasi:
		- crearea claselor din Domain
		- crearea fisierelor de mapare (sau adaugarea adnotarilor)
		- crearea fisierului de configurare Hibernate (unde este BD, cum se poate conecta la BD etc)
		- implementarea nivelului de persistenta folosind functiile corespunzatoare
		- testare
			+ dependenta de Hibernate in build.gradle


.NET ORM
	-> permite o buna decuplare a claselor de modelul relational
	-> bazat pe LINQ
	-> se foloseste de ADO.NET Provider
	-> modalitati: din obiecte -> tabele sau din tabele -> obiecte
	-> pasi pentru L2S: 
		- decorarea claselor folosind atribute .NET
		- crearea unui DataContext de la care obtinem tabelele corespunzatoare
		- utilizand LINQ facem interogari
	-> pasi pentru EF:
		- decorarea claselor folosind atribute .NET (referinta catre System.Data.Entity.dll)
		-  crearea unui Obiect de tip ObjectContext + configurari
		- pt interograri e similar cu L2S



		
