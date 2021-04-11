spring remoting : ce se schimba?

- nu mai avem modulul de networking
- obiectele din domain implementeaza Serializable


pe server :

- Observer extinde Remote si arunca RemoteException la fiecare metoda
- implementarea serviciilor : prinde si RemoteException
- serverul are acel fisier de configurare (spring-server.xml) de injectare a dependentelor si publicare a obiectului remote
- pornirea serverului : se creeaza containerul spring si se creeaza acel obiect remote


pe client :

- controllerul extinde UnicastRemoteException si implementeaza interfata Serializable
- fisierul spring-client.xml prin care obtinem un proxy
- pornirea clientului : cream un container spring, obtinem o referinta catre servicii (va fi un proxy), care este transmisa controllerului