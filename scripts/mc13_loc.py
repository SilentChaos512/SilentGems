"""Converts old localization files to the 1.13 JSON format.

Usage: python mc13_loc.py filename
where filename is your localization file.

Creates a file called "output.json" in the current directory
"""

import re
import sys


def processLine(line):
    originalLine = line
    line = line.strip()

    # Detect empty lines and comments
    if not line or line.startswith('#'):
        return ''
    # Remove end-of-line comments
    line = re.sub(r'#.*', '', line).strip()

    parts = line.split('=')
    key = parts[0].strip()
    # Just in case there is an '=' in the value, join all other parts
    value = '='.join(parts[1:]).strip()

    # Key processing
    key = re.sub(r'^tile', 'block', key)
    key = re.sub(r'\.name$', '', key)
    key = key.replace(':', '.')

    # Value processing
    value = re.sub(r'"', r'\"', value)

    return '"%s": "%s",' % (key, value)


filename = ''
for arg in sys.argv:
    filename = arg

if not filename:
    print('No file specified')
    exit(1)

file = open(filename, 'r', encoding='utf-8')
if not file:
    print('File not found')
    exit(1)

lines = []
for line in file.readlines():
    processed = processLine(line)
    if processed:
        lines.append(processed)
file.close()
# Remove comma from last line
lines[-1] = re.sub(r',$', '', lines[-1])

fileOut = open('output.json', 'w', encoding='utf-8')

fileOut.write('{\n')
for line in lines:
    fileOut.write('  ' + line + '\n')
fileOut.write('}\n')

fileOut.close()
