# adjustPKL

Recalibrating PKL-Files derived from MS-experiments.

With this tool you can shift all m/z values of such an [PKL](http://www.matrixscience.com/help/data_file_help.html#QTOF)-file. You can decide whether to shift all spectra or just a single one of the file! After shifting the data just save it as new PKL-file, so the original values are not lost.

It contains a small spectrum viewer to visually validate the resulting spectra. In figure 1 you can see a screen shot taken from an example PKL-file.

![Screenshot of the viewer](https://binfalse.de/wp-content/uploads/2011/01/adjustPKL.png)



The following picture shows the result of a [Mascot](http://www.matrixscience.com/search_form_select.html) search before and after shifting of a spectrum. As you can see all scores increase more or less while the expects decrease, denoting a more secure hit:

![result of a Mascot search before and after shifting of a spectrum](https://binfalse.de/wp-content/uploads/2011/01/adjustpkl-showcase.png)


adjustPKL is written in JAVA to have it OS independent.

## More information

Is available through [the project's website](https://binfalse.de/software/adjustpkl/).

## License

    Copyright 2010-2017  Martin Scharm
    
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.




