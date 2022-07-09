# YOU-HR 👾

Dieses Projekt wurde im Rahmen des Moduls Objektorientierte Programmierung im Studiengang Wirtschaftsinformatik an der HWR Berlin erarbeitet.

Realisiert wurde ein Personalverwaltungssystem als Web-Applikation (inkl. Datenbankanbindung).

## Die Applikation starten

Dieses Projekt ist ein normales Maven Projekt. Um es über die Kommandozeile zu starten, 
müssen die Befehle `mvnw` (Windows), oder `./mvnw` (Mac & Linux) eingegeben werden.
Im Anschluss muss die Adresse "http://localhost:8080" im Browser geöffnet werden.

Das Projekt kann auch in eine passende IDE importiert werden. Weitere Informationen zu diesem Vorgang können unter
[Wie man Vaadin-Projekte in 
IDEs importiert](https://vaadin.com/docs/latest/flow/guide/step-by-step/importing) gefunden werden(Eclipse, IntelliJ IDEA, NetBeans, and VS Code).

## Projekt-Struktur

- `MainLayout.java` in `src/main/java` beinhaltet die Navigation in der Anwendung (bspw. die Ober- und Seitenleiste und das Menü).
- `views` package in `src/main/java` beinhaltet die server-seitigen Java-Views der Applikation.
- `entity` package in `src/main/java/data` beinhaltet die verwendeten Objekte in der Applikation.
- `repository` package in `src/main/java/data` beinhaltet die Schnittstellen mit der Datenbank.
- `service` package in `src/main/java/data` beinhaltet die Aktionen (Ändern, Löschen, etc.) für Objekte in der Datenbank.
- `security` package in `src/main/java/data` beinhaltet die Authentifizierungsklassen für den Login der Applikation.
