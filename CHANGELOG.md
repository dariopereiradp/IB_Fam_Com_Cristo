# Changelog

All notable changes to this project will be documented in this file.

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
- Use of [LGoodDatePicker]("https://github.com/LGoodDatePicker/LGoodDatePicker") instead of the old [JCalendar]("https://github.com/toedter/jcalendar")
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
on October 31, I decided to call it "Reforma" in honor of the [Protestant Reformation]("https://www.history.com/topics/reformation/reformation"), that changed the world radically.


## [v1.0] - 2020-08-31
- Initial version



[v2.0beta]: https://github.com/dariopereiradp/IB_Fam_Com_Cristo/compare/v1.0...v2.0
[v1.0]: https://github.com/dariopereiradp/IB_Fam_Com_Cristo/releases/tag/v1.0
