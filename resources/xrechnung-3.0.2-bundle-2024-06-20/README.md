**Ankündigung: die Peppol BIS Billing Regeln sind derzeit in CII als "Warning" implementiert. Mit dem Bugfix Release im Herbst 2024 werden die Regeln auf "Fatal"/"Error" verschärft.**

**Advance notification: severity levels of Peppol BIS Billing rules are currently set to "warning" per default in CII. They will be implemented as "error"/"fatal" with the Bugfix Release in Fall 2024.**

# XRechnung Bundle

Ein integriertes Bundle mit dem Spezifikationsdokument für den Standard [XRechnung](https://xeinkauf.de/xrechnung/) und unterstützende Komponenten.

## Überblick Bestandteile

| Name                               | Version im Bundle | Kommentar |
|------------------------------------|-------------------|-----------|
| XRechnung Specification            | 3.0.2       |           |
| XRechnung Syntax-Binding           | zu 3.0.2       |           |
| Validator                          | 1.5.0           |           |
| XRechnung Validator Konfiguration  | 2024-06-20      |           |
| XRechnung Schematron               | 2.1.0           |           |
| XRechnung Visualization            | 2024-06-20           |           |
| XRechnung Testsuite                | 2024-06-20          |           |

## Änderungen zum letzten Release

### Spezifikation

editorielle Änderungen

Details siehe Anhang C. Versionshistorie der Spezifikation XRechnung 3.0.2

### Syntax-Binding

editorielle Änderungen

Details siehe Versionshistorie des Syntaxbinding XRechnung 3.0.2

### Validator

* keine Änderungen

Details siehe: https://github.com/itplr-kosit/validator/releases/tag/v1.5.0

### Validator Konfiguration XRechnung

* Jetzt mit CEN 1.3.12, Schematron 2.1.0 und Testsuite 2024-06-20
* CII-SR-452 und CII-SR-454 wurden per custom level auf "error" erhöht
* Neue Tests hinzugefügt

**Die neuen Regeln CII-SR-461 und CII-SR-462 (als Teil der CEN-Schematron-Regeln Version 1.3.12) und die Verschärfung von CII-SR-452 und CII-SR-454 könnten zu Fehlern in Ihrem Workflow führen.**

Details siehe: https://github.com/itplr-kosit/validator-configuration-xrechnung/releases/tag/release-2024-06-20

### XRechnung Schematron Regeln

* Bug in PEPPOL-EN16931-R046 in CII behoben
* Slack Funktion wurde für den korrekten Umgang mit Rundung ohne Nachkommastellen in der Währung HUF angepasst
* Dokumentation zur Harmonisierung XRechnung und Peppol BIS Billing wurde hinzugefügt

**Ankündigung: die Peppol BIS Billing Regeln sind derzeit in CII als "Warning" implementiert. Mit dem Bugfix Release im Herbst 2024 werden die Regeln auf "Fatal"/"Error" verschärft.**

Details siehe: https://github.com/itplr-kosit/xrechnung-schematron/releases/tag/release-2.1.0

### XRechnung Visualisierung

* Darstellung des Specification Identifier (BT-24) in PDF korrigiert
* Deutsche Übersetzung von BT-10 "Buyer Reference" korrigiert

Details siehe: https://github.com/itplr-kosit/xrechnung-visualization/releases/tag/v2024-06-20

### XRechnung Testsuite

* Änderungen an den technischen Testfällen

Details siehe: https://github.com/itplr-kosit/xrechnung-testsuite/releases/tag/release-2024-06-20

## Bundle Bestandteile Details

### Validator (Prüftool)

Das Prüftool ist ein Programm, welches XML-Dateien (Dokumente) in Abhängigkeit von ihren Dokumenttypen gegen verschiedene Validierungsregeln (XML Schema und Schematron) prüft und das Ergebnis zu einem Konformitätsbericht (Konformitätsstatus *valid* oder *invalid*) mit einer Empfehlung zur Weiterverarbeitung (*accept*) oder Ablehnung (*reject*) aggregiert. Mittels Konfiguration kann bestimmt werden, welche der Konformitätsregeln durch ein Dokument, das zur Weiterverarbeitung empfohlen (*accept*) wird, verletzt sein dürfen.

Das Prüftool selbst ist fachunabhängig und kennt weder spezifische Dokumentinhalte noch Validierungsregeln. Diese werden im Rahmen einer Prüftool-Konfiguration definiert, welche zur Anwendung des Prüftools erforderlich ist.

Weitere Details auf der [Validator Projektseite](https://github.com/itplr-kosit/validator).

### Validator Konfiguration XRechnung

Eine eigenständige Konfiguration für den Standard [XRechnung](https://xeinkauf.de/xrechnung/) wird ebenfalls auf [GitHub bereitgestellt](https://github.com/itplr-kosit/validator-configuration-xrechnung) ([Releases](https://github.com/itplr-kosit/validator-configuration-xrechnung/releases)). Diese enthält alle notwendigen Ressourcen zu der Norm EN16931 (XML-Schema und [Schematron Regeln](https://github.com/CenPC434/validation) u.a.) und die [XRechnung Schematron Regeln](https://github.com/itplr-kosit/xrechnung-schematron) in ihren aktuellen Versionen.

Weitere Details auf der [Validator Konfiguration XRechnung Projektseite](https://github.com/itplr-kosit/validator-configuration-xrechnung).

### XRechnung Schematron Regeln

Technische Implementierung der Geschäftsregeln des Standards [XRechnung](https://xeinkauf.de/xrechnung/) in Schematron Rules für XML Validierung.

Weitere Details auf der [XRechnung Schematron Regeln Projektseite](https://github.com/itplr-kosit/xrechnung-schematron).

### XRechnung Visualisierung

XSL Transformatoren für die Generierung von HTML Web-Seiten und PDF Dateien.

Diese zeigen den Inhalt von elektronischen Rechnungen an, die dem Standard [XRechnung](https://xeinkauf.de/xrechnung/) entsprechen.

Weitere Details auf der [XRechnung Visualisierung Projektseite](https://github.com/itplr-kosit/xrechnung-visualization).

### XRechnung Testsuite

Valide Testdokumente des Standards [XRechnung](https://xeinkauf.de/xrechnung/).

Diese dienen dazu, bei Organisationen, die IT-Fachverfahren herstellen und betreiben, das Verständnis der [XRechnung-Spezifikation](https://xeinkauf.de/xrechnung/versionen-und-bundles/) zu fördern, indem die umfangreichen und komplexen Vorgaben und Besonderheiten der Spezifikation durch valide Testdokumente veranschaulicht werden. Die Testdokumente stehen zur freien Verfügung für die Einbindung in eigene Testverfahren.

Weitere Details auf der [XRechnung Testsuite Projektseite](https://github.com/itplr-kosit/xrechnung-testsuite).
