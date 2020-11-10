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
<!-- TABLE OF CONTENTS -->
## Table of Contents

* [About the Project](#about-the-project)
  * [Main functionalities](#main-functionalities)
  * [Technical details](#technical-details)
  * [Some project metrics](#some-project-metrics)
  * [Sample images](#sample-images)
  * [Built With](#built-with)

* [Getting Started](#getting-started)
  * [Prerequisites](#prerequisites)
  * [Installation](#installation)
* [Usage](#usage)
* [Roadmap](#roadmap)
* [Contributing](#contributing)
* [License](#license)
* [Contact](#contact)
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

### Sample images

<p align="center">
[![Product Name Screen Shot][product-screenshot]]
[![Product Name Screen Shot2][product-screenshot2]]
[![Product Name Screen Shot3][product-screenshot3]]
[![Product Name Screen Shot4][product-screenshot4]]
[![Product Name Screen Shot5][product-screenshot5]]
[![Product Name Screen Shot6][product-screenshot6]]
[![Product Name Screen Shot7][product-screenshot7]]
[![Product Name Screen Shot8][product-screenshot8]]
[![Product Name Screen Shot9][product-screenshot9]]
	</p>



Here's why:
* Your time should be focused on creating something amazing. A project that solves a problem and helps others
* You shouldn't be doing the same tasks over and over like creating a README from scratch
* You should element DRY principles to the rest of your life :smile:

Of course, no one template will serve all projects since your needs may be different. So I'll be adding more in the near future. You may also suggest changes by forking this repo and creating a pull request or opening an issue.

A list of commonly used resources that I find helpful are listed in the acknowledgements.

### Built With
This section should list any major frameworks that you built your project using. Leave any add-ons/plugins for the acknowledgements section. Here are a few examples.
* [Bootstrap](https://getbootstrap.com)
* [JQuery](https://jquery.com)
* [Laravel](https://laravel.com)



[product-screenshot]: Samples/sample.png
[product-screenshot2]: Samples/sample2.png
[product-screenshot3]: Samples/sample3.png
[product-screenshot4]: Samples/sample4.png
[product-screenshot5]: Samples/sample5.png
[product-screenshot6]: Samples/sample6.png
[product-screenshot7]: Samples/sample7.png
[product-screenshot8]: Samples/sample8.png
[product-screenshot9]: Samples/sample9.png
