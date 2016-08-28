## Rx java code eksempler

Dette repo indeholder den kode som jeg brugte til arrangementet Faglig fredag 26/8 2016.

https://home.lundogbendsen.dk/faglig-fredag-268-2016/

Dette repo er forked fra spring.io gs-spring-boot-docker (da jeg først troede jeg skulle bruge docker).

Jeg har tagget mine 6 commits (v1-v6) og de markerer de trin jeg udviklede applikationen i under præsentationen.

## Caching spørsmålet

Under præsentation blev det diskuteret om Observable.amb(DMR, DMRCache) var en optimal løsning. Hvis man ønskede at 
prioriterer DMR.

Jeg tror det man jagtede, ville kunne løses ved altid at vente min TIMEOUT på DMR og hvis dette timeout (eller anden fejl opstod så - onErrorReplace(...) svaret med et allerede hentet cache svar).

Derved venter man på den rigtige service. Og hvis den ikke svarer - eller fejler - har man allerede (forsøgt at) hente et cached svar.

Lars Szuwalski
