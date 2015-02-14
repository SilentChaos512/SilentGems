import sys
import re
import os

def createDirIfNeeded(name):
  if not os.path.exists(name):
    os.makedirs(name)

def createAllDirs():
  #createDirIfNeeded('output')
  createDirIfNeeded('output/blockstates')
  #createDirIfNeeded('output/models')
  createDirIfNeeded('output/models/block')
  createDirIfNeeded('output/models/item')

def writeBlockJSONs(name):
  #blockstate
  f = open('output/blockstates/' + name + '.json', 'w')
  f.write('{\n')
  f.write('  "variants": {\n')
  f.write('    "normal": { "model": "SilentGems:' + name + '" }\n')
  f.write('  }\n')
  f.write('}\n')
  f.close()
  #block model
  f = open('output/models/block/' + name + '.json', 'w')
  f.write('{\n')
  f.write('  "parent": "block/cube_all",\n')
  f.write('  "textures": {\n')
  f.write('    "all": "SilentGems:blocks/' + name + '"\n')
  f.write('  }\n')
  f.write('}\n')
  f.close()
  #item model
  f = open('output/models/item/' + name + '.json', 'w')
  f.write('{\n')
  f.write('  "parent": "SilentGems:block/' + name + '",\n')
  f.write('  "display": {\n')
  f.write('    "thirdperson": {\n')
  f.write('      "rotation": [ 10, -45, 170 ],\n')
  f.write('      "translation": [ 0, 1.5, -2.75 ],\n')
  f.write('      "scale": [ 0.375, 0.375, 0.375 ]\n')
  f.write('    }\n')
  f.write('  }\n')
  f.write('}\n')
  f.close()

def writeItemJSON(name):
  f = open('output/models/item/' + name + '.json', 'w')
  f.write('{\n')
  f.write('  "parent": "builtin/generated",\n')
  f.write('  "textures": {\n')
  f.write('    "layer0": "SilentGems:items/' + name + '"\n')
  f.write('  },\n')
  f.write('  "display": {\n')
  f.write('    "thirdperson": {\n')
  f.write('      "rotation": [-90, 0, 0],\n')
  f.write('      "translation": [0, 1, -3],\n')
  f.write('      "scale": [0.55, 0.55, 0.55 ]\n')
  f.write('    },\n')
  f.write('    "firstperson": {\n')
  f.write('      "rotation": [0, -135, 25 ],\n')
  f.write('      "translation": [0, 4, 2 ],\n')
  f.write('      "scale": [1.7, 1.7, 1.7]\n')
  f.write('    }\n')
  f.write('  }\n')
  f.write('}\n')
  f.write('\n')
  f.close()



numRegex = re.compile("^\d+$")

isBlock = False
name = ''
count = 1

for arg in sys.argv:
  argl = str.lower(arg)
  if argl == 'block':
    isBlock = True
  elif argl == 'item':
    isBlock = False
  match = numRegex.match(arg)
  if match:
    count = int(match.group(0))
  elif arg != 'makejson.py':
    name = arg

if name == '':
  print('No block/item name specified!')
  exit(1)
if count < 1:
  count = 1

createAllDirs()

for i in range(count):
  s = name
  if count > 1:
    s += str(i)
  if isBlock:
    writeBlockJSONs(s)
  else:
    writeItemJSON(s)
#