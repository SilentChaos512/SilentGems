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

def writeJSON(bow):
  f = open('output/models/item/' + bow + '.json', 'w')
  f.write('{\n')
  f.write('  "parent": "builtin/generated",\n')
  f.write('  "textures": {\n')
  f.write('    "layer0": "SilentGems:items/' + bow + '"\n')
  f.write('  },\n')
  f.write('  "display": {\n')
  f.write('    "thirdperson": {\n')
  f.write('      "rotation": [5, -100, -45],\n')
  f.write('      "translation": [0.75, 0, 0.25],\n')
  f.write('      "scale": [1, 1, 1 ]\n')
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

createAllDirs()
  
for i in range(12):
  bow = "Bow" + str(i) + "_"
  writeJSON(bow + "Standby")
