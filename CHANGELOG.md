# Changelog

All notable changes to this project will be documented in this file.

## [v2.0.1] - 

### Changed
- Small image view (in both MembroDetails and Ficha_Membro_ToPDF) now preserves the original aspect ratio.
- The programa now uses a different folder to store the finnancial reports. Before it was using the same folder of members lists.
- Folder 'Listas' changed to 'Listas de Membros'
- Statistics -> Finances -> Por meses. Chosing the year is simpler now. Before it was showing all the years from 2000 to 2250 on the JComboBox. Now it only shows the years from the oldest year to the current year.
- Changed '/' to System.getProperty("file.separator")

### Fixed
- Manual opening didn't work (wrong pdf name - 2.0 instead of 2.0.1)
- Data do batismo was wrong on the pdf generation Ficha_de_Membro. It was a mistake: I was calling the wrong function (getMembro_desde())
- Image upload with wrong orientation. I fixed it using the [TwelveMonkeys ImageIO 3.6.1] library to compress the image and preserve the metadata and [JavaXT Core] library to rotate the image, according to the orientation metadata.
- Unable to Login with different String case (for example: the user is registred as 'tesoureiro'. If I try to login using 'Tesoureiro', the UCanAccess library finds the user, but TableModelFuncionario wasn't finding it, causing a NullPointerException). The method getFuncionario(String nome) now uses the method equalsIgnoreCase() of the String class. So, Usernames aren't case sensitive.
- Finding the Documents folder is language sensitive now. The program was using "Documents" string, causing non-english systems to create a new folder instead of using the Documents folder. Now, the program uses this trick to find the correct documents folder: FileSystemView.getFileSystemView().getDefaultDirectory().getPath().
- Wrong indication of the path of generated Fichas de Membros. It was indicating the path of Listas de Membros in the JOptionPane.

## [v2.0.1beta] - 2020-12-18

### Added
- Different types of users that can login to the program. Each type of user grant access to specific features of the program. This change adds a new layer of security and personal data protection. For example, a user that only needs to control the finances doesn't need to have access to personal details of the members.
- On Windows 10 Control Panel there is a proper uninstall icon on the program (modification on the Inno script)

### Changed
- As part of the new feature, the program now saves the user (object) that logged in, instead of saving only the name, in the Login class. This solution is more robust and adds a new layer of security since know the program needs to call a method (getFuncionario()) instead of accessing a public variable directly (Login.NOME).
- The admin user now can only change settings and manage the users. He doesn't have access to the whole program anymore, and cannot see anything about the members or finances. This is because the IT guy doesn't need to have access to personal or secret data to manage the program.
- The version number changed, even if the previous version was a beta version because I added a new important feature, described above. So I changed the version to 2.0.1 and it will remain in beta testing while I wait for users feedback.

### Fixed
- Grid lines in the table of collaborators wasn't showing up. Now this is fixed.


## [v2.0beta] - 2020-11-10

### Added
- Dark theme and a switch beetween light and dakr themes. The dark theme can improve your health, by saving your eyes from the radiation of the computer screen.
- Finances functionality: now it's possibly to add income and expense records, add description to records, see some useful charts and statistics, filter results and generate financial reports in PDF.
- Reset a database or the whole program (including settings).
- Auto sort in tables.
- Export tables to CSV.
- Export logo image in different formats.
- Keyboard shortcuts to open menu functionality.
- Icons on buttons and menus.

### Changed
- Almost every library was updated to a newer version.
- Maven is used to organize the dependencies.
- Packages were reorganized.
- Access database files were updated from V2003 to V2010 and now use .accdb instead of .mdb.
- Access database is not generated, but it's copied from a loaded template in resources.
- Access database is now encryted by a password.
- Settings file is now a INI file instead of plain txt.
- Use of LocalDate or LocalDateTime instead of the old java.util.Date.
- Use of [LGoodDatePicker] instead of the old [JCalendar]
- Instead of repeating the same code, now the program uses some super classes like Table, that extends JTable and has the common configurations of the tables used in the program (members, finances and collaborators) or DateChooser, that extends DatePicker with some default configurations.
- Regarding the same problem, it was created an Utils class, that groups some functions that are used in different parts of the program.
- ImageViewer opens the image in the original aspect ratio.

### Fixed
- By sorting the collaborators table, it was possible to change the admin password. Now it was fixed.

### Removed
- JPDFWritter library. Using only iText intstead.
- JCalendar library.
- BackgroundPanel class, that was useless.

### Curiosity about the version
This version was named v2.0 - Reforma because it made a great reformation in the base code of the program, making it faster, more beautiful and effective. Since the development of functionallities was finished
on October 31, I decided to call it "Reforma" in honor of the [Protestant Reformation], that changed the world radically.


## [v1.0] - 2020-08-31
- Initial version

[JavaXT Core]: https://www.javaxt.com/javaxt-core/
[TwelveMonkeys ImageIO 3.6.1]: https://github.com/haraldk/TwelveMonkeys
[JCalendar]: https://github.com/toedter/jcalendar
[LGoodDatePicker]: https://github.com/LGoodDatePicker/LGoodDatePicker
[Protestant Reformation]: https://www.history.com/topics/reformation/reformation
[v2.0.1beta]: https://github.com/dariopereiradp/IB_Fam_Com_Cristo/compare/v2.0beta-Reforma...v2.0.1beta-Reforma
[v2.0beta]: https://github.com/dariopereiradp/IB_Fam_Com_Cristo/compare/v1.0...v2.0beta-Reforma
[v1.0]: https://github.com/dariopereiradp/IB_Fam_Com_Cristo/releases/tag/v1.0
