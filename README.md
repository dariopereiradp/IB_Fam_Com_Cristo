<p align="center">
	<img width="100px" src="resources/FC.jpg" align="center" alt="IBFC" />
	<h2 align="center">Igreja Batista Famílias com Cristo</h2>
	<p align="center">Software to manage the church members and the finances</p>
</p>
<p align="center">
	<a href="https://github.com/dariopereiradp/IB_Fam_Com_Cristo/releases">
		<img src="https://img.shields.io/github/v/release/dariopereiradp/IB_Fam_Com_Cristo" />
	</a>
	<a href="https://github.com/dariopereiradp/IB_Fam_Com_Cristo/tree/master/src/dad">
		<img src="https://img.shields.io/github/languages/top/dariopereiradp/IB_Fam_Com_Cristo" />
	</a>
	<a href="https://github.com/dariopereiradp/IB_Fam_Com_Cristo/commits/master">
		<img src="https://img.shields.io/github/last-commit/dariopereiradp/IB_Fam_Com_Cristo" />
	</a>
	<a href="https://github.com/dariopereiradp/IB_Fam_Com_Cristo/tree/master/src/dad">
		<img src="https://img.shields.io/github/languages/code-size/dariopereiradp/IB_Fam_Com_Cristo" />
	</a>
	<a href="#">
		<img src="https://img.shields.io/github/repo-size/dariopereiradp/IB_Fam_Com_Cristo" />
	</a>
	<br />
	<br />
	<a href="https://www.facebook.com/AssociacaoCulturalDadivaDeDeus">
		<img src="https://img.shields.io/badge/Suported%20by-Associa%C3%A7%C3%A3o%20Cultural%20D%C3%A1diva%20de%20Deus-blue" />
	</a>
	<a href="#">
		<img src="https://img.shields.io/badge/Developed%20by-DPSoft-green" />
	</a>
</p>


* [About the Project](#about-the-project)
  * [Main functionalities](#main-functionalities)
  * [Technical details](#technical-details)
  * [Some project metrics](#some-project-metrics)
  * [Built With](#built-with)
* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Usage](#usage)
  * [Sample images](#sample-images)
* [Acknowledgements](#acknowledgements)

<!-- ABOUT THE PROJECT -->
## About The Project

This project was created by Dário Pereira, to help Igreja Batista Famílias com Cristo (a church) to manage members and finances.

### Main functionalities

* Two different themes available: Dark theme (default) or Lite theme. Both of them use the <a href="https://github.com/vincenzopalazzo/material-ui-swing">Material UI Swing</a> library, to have a modern Look-and-feel instead of the old Java Swing default Look-and-Feel.
* Add and remove persons (members). Visualize a profile page (with profile image) and edit the fields.
* Add and remove collaborators (person who have access to the program). This persons can enter the program with a password, and have limited access to it. Only the admin can have total access.
* Add and remove financial transactions and edit the fields (date, description, value). The total available in the moment of the transaction will update automatically.
* Search tool with different types of filters.
* Auto sorting in the tables.
* Visualize and save (export) images (like profile images or different formats of the logo image).
* Show many different charts for the persons and transactions.
* Generate PDFs with a list of the members (the user can choose wich types to include).
* Generate PDFs with a member profile.
* Generate PDFs with a blank member profile, to be printed and filled.
* Generate PDFs with financial reports (the user can choose the period and what to include).
* Reset a database (the user can choose wich database to reset) or to reset the whole program, including the setting.
* Backup and restore the databases and settings
* Export the database to CSV.
* Undo and redo for most operations.
* Keyboard shortcuts for most operations.
* Log files. The user can clean old logs.
* Bug reports: the user can write a description of the problem and the program will auto generate a zip file with the logs and the description.

### Technical details
* This project was compiled with Java 8. 
* Since v2.0 I started using Maven to organize the dependencies of the project.
* I developed this program using Eclipse.
* This project is a Java Swing application.
* Use of Microsoft Access database, with files encryted and protected by password, to prevent the users from modifying it outside the program.
* Use of <a href="http://ucanaccess.sourceforge.net/site.html">UCanAccess</a>, an open-source Java JDBC driver implementation to read and write in the database.
* Use of AbstractTableModel to manipulate the database in the program.
* Use of INI file to save the settings
* Use o LocalDate and LocalDateTime instead of java.util.Date (since v2.0)

### Some project metrics

I extracted this metrics using <a href="https://github.com/mariazevedo88/o3smeasures-tool">o3smeasures-tool</a> to Eclipse.

* Number of classes: 84
* Lines of code (LOC): 9386 (Max: 776 - DataGui.java)
* Numbers of Methods: 508 (Max: 63 - TableModelMembro.java)
* Number of Attributes: 349 (Max: 46 - MembroPanel.java)
* Number of packages: 12
* Number of interfaces: 3

### Built With
* [Java 8](https://java.com/)


## Getting started

### Prerequisites
This program was made in Java. You will need a JVM with the minimum version 1.8.
I adapted this program to work with Windows. But, since Java is multiplataform, it should work in other operating systems. Still, I didn't tested it.

### Instalation
Just run the jar file inside the target/ folder.


## Usage
To see a full explanation, read the manual (in portuguese). Bellow you can see some samples of the program.

### Sample images

* Members GUI
![Product Name Screen Shot][product-screenshot]

* Members GUI (with filter)
![Product Name Screen Shot2][product-screenshot2]

* Finances GUI
![Product Name Screen Shot3][product-screenshot3]

* Collaborators GUI
![Product Name Screen Shot4][product-screenshot4]

* Statistics charts
![Product Name Screen Shot5][product-screenshot5]

* Light theme
![Product Name Screen Shot6][product-screenshot6]

* Login GUI
![Product Name Screen Shot7][product-screenshot7]

* Financial report PDF sample
![Product Name Screen Shot8][product-screenshot8]

* Member profile sample
![Product Name Screen Shot9][product-screenshot9]


## Acknowledgments
* <a href="https://github.com/vincenzopalazzo/material-ui-swing">Material UI Swing v1.1.2-rc1</a> - Implements a beauty and modern Look-and-feel, based on Material Design.
* <a href="http://ucanaccess.sourceforge.net/site.html">UCanAccess 5.0.0</a> - Read and write Microsoft Access Databases
* <a href="https://jackcessencrypt.sourceforge.io">Jackcess Encrypt 3.0.0</a> - Read and write encrypted Access databases
* <a href="https://github.com/srikanth-lingala/zip4j">Zip4j 2.6.4</a> - Generate Zip files (used in Backups and BugReports)
* <a href="https://github.com/LGoodDatePicker/LGoodDatePicker">LGoodDatePicker 11.1.0</a> - Choose a date in the LocalDate type.
* <a href="https://itextpdf.com/en/products/itext-5-legacy">iTextPDF 5.5.13.2</a> - Generate a PDF file (used to the lists of members, financial reports and member profiles)
* <a href="https://knowm.org/open-source/xchart">xChart 3.6.5</a> - Generate charts (used in statistics and financial reports)
* <a href="http://miglayout.com/">MigLayout-Swing 5.2</a> - Used in some layouts (for example, in the profile page)
* <a href="https://commons.apache.org/proper/commons-csv">Apache Commons CSV 1.8</a> - Export the databases to CSV
* <a href="https://commons.apache.org/proper/commons-codec">Apache Commons Codec 1.15</a> - Used to implement AES Encryption
* <a href="https://commons.apache.org/proper/commons-io">Apache Commons IO 2.8.0</a> - FileUtils: copy files or delete directories
* <a href="https://commons.apache.org/proper/commons-lang">Commons Lang 3.11</a> - DurationFormatUtils: format the time of using the program
* <a href="https://commons.apache.org/proper/commons-text">Commons Text 1.9</a> - WordUtils.capitalize: capitalize the names
* <a href="https://commons.apache.org/proper/commons-logging">Apache Commons Logging 1.2</a> - Generate the log files


[product-screenshot]: Samples/sample.png
[product-screenshot2]: Samples/sample2.png
[product-screenshot3]: Samples/sample3.png
[product-screenshot4]: Samples/sample4.png
[product-screenshot5]: Samples/sample5.png
[product-screenshot6]: Samples/sample6.png
[product-screenshot7]: Samples/sample7.png
[product-screenshot8]: Samples/sample8.png
[product-screenshot9]: Samples/sample9.png
