import sys
import re
import codecs

pattern = re.compile(r'(?<=[.:])[A-Z][^.]+(?=[.=])')

output = codecs.open('temp.lang', 'w', 'utf-8')

with codecs.open('en_US.lang', 'r', 'utf-8') as file:
    for line in file.readlines():
        match = pattern.search(line)
        if match:
            line = re.sub(match.group(0), match.group(0).lower(), line)
        output.write(line)

output.close()
