# Experiments for article in the journal MONE 2022.
# Copyright (C) 2022  Vincent A. Cicirello
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
# 
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
# 
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.
#

if __name__ == "__main__" :
    file1 = "data/lcs.SA50.original.txt"
    file2 = "data/lcs.SA50.missingColumns.txt"
    outFile = "data/lcs.SA50.txt"

    with open(file1, "r") as f1 :
        with open(file2, "r") as f2 :
            lines1 = f1.readlines()
            lines2 = f2.readlines()

    rows = []
    for i, x in enumerate(lines1) :
        y = lines2[i]
        rows.append(x.split() + y.split())

    indexes = [0, 1, 11, 2, 12, 13, 3, 4, 5, 6, 7, 8]
    
    with open(outFile, "w") as f :
        for r in rows :
            f.write(r[indexes[0]])
            for i in range(1, len(indexes)) :
                f.write("\t")
                f.write(r[indexes[i]])
            f.write("\n")
